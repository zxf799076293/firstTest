package com.linhuiba.business.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.OrderExpandableListAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.OnCouponTimerTask;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.OrderInfoModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreExpandablelistView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/3/11.
 */
public class OrderListNewActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreExpandablelistView.OnLoadMore, Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,
        Field_AddFieldChoosePictureCallBack.AdField_deleteChoosePictureCall,
        Field_AddFieldChoosePictureCallBack.FieldOrderApproved {
    @InjectView(R.id.orderlist_tablayout)
    TabLayout mOrderTabLayout;
    public int currIndex = 0;// 当前页卡编号
    private ArrayList<View> mListViews;
    private ViewPager fPager;
    private SwipeRefreshLayout[] mswipList = new SwipeRefreshLayout[6];
    private LoadMoreExpandablelistView[] mOrderExpandableList = new LoadMoreExpandablelistView[6];
    private OrderExpandableListAdapter[] mOrderExpandableListAdapter = new OrderExpandableListAdapter[6];
    private RelativeLayout[] mLay_no_order = new RelativeLayout[6];
    private int[] orderlistpagesize = new int[6];

    private List<Map<String, Object>>[] mData = new ArrayList[6];
    private List<List<OrderInfoModel>>[] OrderChildList = new ArrayList[6];
    private int onResume = -1;
    private int showdialog;
    private PopupWindow pw;
    private int deleteitem;
    private int mTabTextViewList[] = {R.string.all_order_txt, R.string.myselfinfo_pay, R.string.myselfinfo_check,
            R.string.myselfinfo_waiting, R.string.order_cancel_toast,
            R.string.myselfinfo_review};
    private Call mOrderAllListCall;
    private Call mOrderPayListCall;
    private String mServicePhone;
    private CustomDialog mCustomDialog;
    private Handler mDialogHandler = new Handler();
    private CustomDialog mShowFailureDialog;
    private final int CALL_PHONE_CODE = 110;
    private String callPhoneStr = "";
    private Timer[] mTimers = new Timer[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.order_title_txt));
        TitleBarUtils.showBackImg(this, true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("order_type") != null) {
                currIndex = bundle.getInt("order_type");
            }
            if (bundle.get("showdialog") != null) {
                showdialog = bundle.getInt("showdialog");
            }
        }
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();  // 调用父类方法
        onResume = 1;
    }

    @Override
    public void onStop() {
        super.onStop();  // 调用父类方法
        onResume = 1;
    }

    @Override
    public void onResume() {
        super.onResume();  // 调用父类方法
        if (onResume == 1) {
            showProgressDialog();
            String status = "0";
            if (currIndex == 0) {
                status = "all";
            } else if (currIndex == 1) {
                status = "submitted";
            } else if (currIndex == 2) {
                status = "paid";
            } else if (currIndex == 3) {
                status = "approved";
            } else if (currIndex == 4) {
                status = "canceled";
            } else if (currIndex == 5) {
                status = "finished";
            }
            orderlistpagesize[currIndex] = 1;
            mOrderExpandableList[currIndex].set_refresh();
            if (currIndex == 0 || currIndex == 1) {
                if (mTimers[currIndex] != null) {
                    mTimers[currIndex].cancel();
                    mTimers[currIndex] = null;
                }
            }
            if (mOrderAllListCall != null && !mOrderAllListCall.isCanceled()) {
                mOrderAllListCall.cancel();
            }
            if (mOrderPayListCall != null && !mOrderPayListCall.isCanceled()) {
                mOrderPayListCall.cancel();
            }
            if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
                FieldApi.getpurchased_resourceslist(mOrderPayListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            } else if (currIndex == 0 || currIndex == 1) {
                FieldApi.getordersitemlist(mOrderAllListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getAllOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            }
        }
        showOrderPayDialog(showdialog);
        showdialog = 0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimers[0] != null) {
            mTimers[0].cancel();
        }
        if (mTimers[1] != null) {
            mTimers[1].cancel();
        }
    }

    private void initView() {
        fPager = (ViewPager) this.findViewById(R.id.fieldorder_viewpager);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = this.getLayoutInflater();

        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        mListViews.add(inflater.inflate(R.layout.viewpage_field_list, null));
        for (int i = 0; i < mListViews.size(); i++) {
            mData[i] = new ArrayList<Map<String, Object>>();
            OrderChildList[i] = new ArrayList<List<OrderInfoModel>>();
            mswipList[i] = (SwipeRefreshLayout) mListViews.get(i).findViewById(R.id.order_swipe_refresh);
            mswipList[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mOrderExpandableList[i] = (LoadMoreExpandablelistView) mListViews.get(i).findViewById(R.id.ordernew_loadmore_expendlist);
            mOrderExpandableList[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mswipList[i].setOnRefreshListener(this);
            mOrderExpandableList[i].setLoadMoreListen(this);
            mLay_no_order[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.lay_no_order);
            TextView noTV = (TextView) mListViews.get(i).findViewById(R.id.no_data_tv);
            ImageView noImgv = (ImageView) mListViews.get(i).findViewById(R.id.no_data_img);
            noTV.setText(getResources().getString(R.string.order_nodata_toast));
            noImgv.setImageResource(R.drawable.ic_invoice_title_no);
            mLay_no_order[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更新数据
                    initdata();
                }
            });
        }
        fPager.setAdapter(new OrderPagerAdapter(mListViews));
        setordertypeview(currIndex);
        fPager.setCurrentItem(currIndex);
        fPager.setOnPageChangeListener(new PagerChangeListener());
        mOrderTabLayout.setupWithViewPager(fPager);
        for (int i = 0; i < mOrderTabLayout.getTabCount(); i++) {
            mOrderTabLayout.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }

    }

    @Override
    public void loadMore() {
        if (mswipList[currIndex].isShown()) {
            mswipList[currIndex].setRefreshing(false);
        }
        loadmoredata();
    }

    @Override
    public void onRefresh() {
        onRefreshData();
    }
    public void onRefreshData() {
        if (currIndex == 0 || currIndex == 1) {
            if (mTimers[currIndex] != null) {
                mTimers[currIndex].cancel();
                mTimers[currIndex] = null;
            }
        }
        String status = "0";
        if (currIndex == 0) {
            status = "all";
        } else if (currIndex == 1) {
            status = "submitted";
        } else if (currIndex == 2) {
            status = "paid";
        } else if (currIndex == 3) {
            status = "approved";
        } else if (currIndex == 4) {
            status = "canceled";
        } else if (currIndex == 5) {
            status = "finished";
        }
        if (mOrderAllListCall != null && !mOrderAllListCall.isCanceled()) {
            mOrderAllListCall.cancel();
        }
        if (mOrderPayListCall != null && !mOrderPayListCall.isCanceled()) {
            mOrderPayListCall.cancel();
        }
        orderlistpagesize[currIndex] = 1;
        mOrderExpandableList[currIndex].set_refresh();
        if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
            FieldApi.getpurchased_resourceslist(mOrderPayListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getOrderListHandler,
                    status, String.valueOf(orderlistpagesize[currIndex]), "10");

        } else if (currIndex == 0 || currIndex == 1) {
            FieldApi.getordersitemlist(mOrderAllListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getAllOrderListHandler,
                    status, String.valueOf(orderlistpagesize[currIndex]), "10");

        }
    }
    private void setView() {
//        fPager.setCurrentItem(position);
        if (LoginManager.isLogin()) {
            initdata();
        } else {
            LoginManager.getInstance().clearLoginInfo();
            OrderListNewActivity.this.finish();
        }

    }

    private void initdata() {
        String status = "0";
        if (currIndex == 0) {
            status = "all";
        } else if (currIndex == 1) {
            status = "submitted";
        } else if (currIndex == 2) {
            status = "paid";
        } else if (currIndex == 3) {
            status = "approved";
        } else if (currIndex == 4) {
            status = "canceled";
        } else if (currIndex == 5) {
            status = "finished";
        }
        if ((mData[currIndex] == null || mData[currIndex].isEmpty())) {
            showProgressDialog();
            orderlistpagesize[currIndex] = 1;
            mOrderExpandableList[currIndex].set_refresh();
            if (mOrderAllListCall != null && !mOrderAllListCall.isCanceled()) {
                mOrderAllListCall.cancel();
            }
            if (mOrderPayListCall != null && !mOrderPayListCall.isCanceled()) {
                mOrderPayListCall.cancel();
            }
            if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
                FieldApi.getpurchased_resourceslist(mOrderPayListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            } else if (currIndex == 0 || currIndex == 1) {
                FieldApi.getordersitemlist(mOrderAllListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getAllOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            }

        }
//        else {
//            mLay_no_order[currIndex].setVisibility(View.GONE);
//            mOrderExpandableListAdapter[currIndex] = new OrderExpandableListAdapter(this, mData[currIndex], OrderChildList[currIndex], OrderListNewActivity.this, currIndex);
//            mOrderExpandableList[currIndex].setAdapter(mOrderExpandableListAdapter[currIndex]);
//            if (mswipList[currIndex].isShown()) {
//                mswipList[currIndex].setRefreshing(false);
//            }
//            for (int n = 0; n < mData[currIndex].size(); n++) {
//                mOrderExpandableList[currIndex].expandGroup(n);
//            }
//            mOrderExpandableList[currIndex].setGroupIndicator(null);
//            mOrderExpandableList[currIndex].setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//                @Override
//                public boolean onGroupClick(ExpandableListView parent, View v,
//                                            int groupPosition, long id) {
//                    return true;
//                }
//            });
//        }

    }

    private void loadmoredata() {
        if (mData[currIndex] != null &&
                mData[currIndex].size() > 0 && OrderChildList[currIndex] != null &&
                OrderChildList[currIndex].size() > 0) {
            String status = "0";
            if (currIndex == 0) {
                status = "all";
            } else if (currIndex == 1) {
                status = "submitted";
            } else if (currIndex == 2) {
                status = "paid";
            } else if (currIndex == 3) {
                status = "approved";
            } else if (currIndex == 4) {
                status = "canceled";
            } else if (currIndex == 5) {
                status = "finished";
            }
            orderlistpagesize[currIndex] = orderlistpagesize[currIndex] + 1;
            if (mOrderAllListCall != null && !mOrderAllListCall.isCanceled()) {
                mOrderAllListCall.cancel();
            }
            if (mOrderPayListCall != null && !mOrderPayListCall.isCanceled()) {
                mOrderPayListCall.cancel();
            }
            if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
                FieldApi.getpurchased_resourceslist(mOrderPayListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getMoreOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            } else if (currIndex == 0 || currIndex == 1) {
                FieldApi.getordersitemlist(mOrderAllListCall, MyAsyncHttpClient.MyAsyncHttpClient3(), getMoreOrderListHandler,
                        status, String.valueOf(orderlistpagesize[currIndex]), "10");

            }
        } else {
            mOrderExpandableList[currIndex].onLoadComplete();
        }

    }

    private LinhuiAsyncHttpResponseHandler getAllOrderListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data == null || data.toString().length() == 0) {
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            if (jsonObject == null || jsonObject.get("data") == null ||
                    jsonObject.get("data").toString().length() == 0) {
                return;
            }
            if (mData[currIndex] != null) {
                mData[currIndex].clear();
            }
            if (OrderChildList[currIndex] != null) {
                OrderChildList[currIndex].clear();
            }
            com.alibaba.fastjson.JSONArray orderarray = JSON.parseArray(jsonObject.get("data").toString());
            if (orderarray == null || orderarray.isEmpty()) {
                mLay_no_order[currIndex].setVisibility(View.VISIBLE);
                if (mswipList[currIndex].isShown()) {
                    mswipList[currIndex].setRefreshing(false);
                }
                return;
            }
            if (orderarray.size() < 10) {
                mOrderExpandableList[currIndex].set_loaded();
            }
            for (int i = 0; i < orderarray.size(); i++) {
                if (currIndex == 0 || currIndex == 1) {
                    com.alibaba.fastjson.JSONObject orderjsonobject = JSON.parseObject(orderarray.get(i).toString());
                    if (orderjsonobject.getString("resources") != null) {
                        ArrayList<OrderInfoModel> orderlist_tmp = (ArrayList<OrderInfoModel>) (JSON.parseArray(orderjsonobject.getString("resources").toString(), OrderInfoModel.class));
                        if (orderlist_tmp != null) {
                            if (orderlist_tmp.size() > 0) {
                                Map<String, Object> groupmap = new HashMap<String, Object>();
                                if (orderjsonobject.get("order_id") != null && orderjsonobject.getString("order_id").toString().length() > 0) {
                                    groupmap.put("order_id", orderjsonobject.getString("order_id").toString());
                                }
                                if (orderjsonobject.get("order_num") != null && orderjsonobject.getString("order_num").toString().length() > 0) {
                                    groupmap.put("order_num", orderjsonobject.getString("order_num").toString());
                                }
                                if (orderjsonobject.get("actual_fee") != null && orderjsonobject.getString("actual_fee").toString().length() > 0) {
                                    groupmap.put("actual_fee", orderjsonobject.getString("actual_fee").toString());
                                }
                                if (orderjsonobject.get("status") != null && orderjsonobject.getString("status").toString().length() > 0) {
                                    groupmap.put("status", orderjsonobject.getString("status").toString());
                                }
                                if (orderjsonobject.get("order_deposit") != null) {
                                    groupmap.put("order_deposit", Double.valueOf(orderjsonobject.get("order_deposit").toString()));
                                }
                                if (orderjsonobject.get("service_fee") != null) {
                                    groupmap.put("service_fee", Double.valueOf(orderjsonobject.get("service_fee").toString()));
                                }
                                if (orderjsonobject.get("tax") != null) {
                                    groupmap.put("tax", Double.valueOf(orderjsonobject.get("tax").toString()));
                                }
                                if (orderjsonobject.get("delivery_fee") != null) {
                                    groupmap.put("delivery_fee", Double.valueOf(orderjsonobject.get("delivery_fee").toString()));
                                }
                                if (orderjsonobject.get("offline_pay") != null && orderjsonobject.getString("offline_pay").toString().length() > 0) {
                                    groupmap.put("offline_pay", orderjsonobject.getInteger("offline_pay"));
                                }
                                if (orderjsonobject.get("voucher_image_url") != null && orderjsonobject.get("voucher_image_url").toString().length() > 0) {
                                    groupmap.put("voucher_image_url", orderjsonobject.getString("voucher_image_url"));
                                }
                                if (orderjsonobject.get("ordered_user") != null && orderjsonobject.get("ordered_user").toString().length() > 0) {
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id").toString().length() > 0) {
                                        groupmap.put("ordered_user_id", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("id").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile").toString().length() > 0) {
                                        groupmap.put("ordered_user_mobile", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("mobile").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name").toString().length() > 0) {
                                        groupmap.put("ordered_user_name", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("name").toString());
                                    }
                                }
                                if (orderjsonobject.get("paid_by") != null && orderjsonobject.get("paid_by").toString().length() > 0) {
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id").toString().length() > 0) {
                                        groupmap.put("paid_by_id", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("id").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile").toString().length() > 0) {
                                        groupmap.put("paid_by_mobile", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("mobile").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name").toString().length() > 0) {
                                        groupmap.put("paid_by_name", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("name").toString());
                                    }
                                }
                                if (orderjsonobject.get("remind_time") != null && orderjsonobject.get("remind_time").toString().length() > 0) {
                                    groupmap.put("remind_time", orderjsonobject.getString("remind_time") + "000");
                                }
                                if (orderjsonobject.get("alter_notice") != null && orderjsonobject.get("alter_notice").toString().length() > 0) {
                                    groupmap.put("alter_notice", orderjsonobject.getString("alter_notice"));//付款是否要弹窗提醒超时
                                }

                                mData[currIndex].add(groupmap);
                                OrderChildList[currIndex].add(orderlist_tmp);
                            }
                        }
                    }
                }
            }
            mOrderExpandableListAdapter[currIndex] = new OrderExpandableListAdapter(OrderListNewActivity.this.getContext(), mData[currIndex], OrderChildList[currIndex], OrderListNewActivity.this, currIndex);
            mOrderExpandableList[currIndex].setAdapter(mOrderExpandableListAdapter[currIndex]);
            if (mswipList[currIndex].isShown()) {
                mswipList[currIndex].setRefreshing(false);
            }
            for (int n = 0; n < mData[currIndex].size(); n++) {
                mOrderExpandableList[currIndex].expandGroup(n);
            }
            mOrderExpandableList[currIndex].setGroupIndicator(null);
            mOrderExpandableList[currIndex].setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
            //2018/12/12 倒计时
            if (currIndex == 0 || currIndex == 1) {
                if (mTimers[currIndex] == null) {
                    mTimers[currIndex] = new Timer();
                }
                mTimers[currIndex].schedule(new OnCouponTimerTask(mData[currIndex],mOrderExpandableListAdapter[currIndex],
                        OrderListNewActivity.this,1,0),0,1000);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (mswipList[currIndex].isShown()) {
                mswipList[currIndex].setRefreshing(false);
            }
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);

        }
    };

    private LinhuiAsyncHttpResponseHandler getOrderListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data == null || data.toString().length() == 0) {
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            if (jsonObject == null || jsonObject.get("data") == null ||
                    jsonObject.get("data").toString().length() == 0) {
                return;
            }
            if (mData[currIndex] != null) {
                mData[currIndex].clear();
            }
            if (OrderChildList[currIndex] != null) {
                OrderChildList[currIndex].clear();
            }
            com.alibaba.fastjson.JSONArray orderarray = JSON.parseArray(jsonObject.get("data").toString());
            if (orderarray == null || orderarray.isEmpty()) {
                mLay_no_order[currIndex].setVisibility(View.VISIBLE);
                if (mswipList[currIndex].isShown()) {
                    mswipList[currIndex].setRefreshing(false);
                }
                return;
            }
            if (orderarray.size() < 10) {
                mOrderExpandableList[currIndex].set_loaded();
            }
            for (int i = 0; i < orderarray.size(); i++) {
                if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
                    com.alibaba.fastjson.JSONArray jsonArray = new JSONArray();
                    jsonArray.add(JSON.parseObject(orderarray.get(i).toString()));
                    ArrayList<OrderInfoModel> orderlist_tmp = (ArrayList<OrderInfoModel>) (JSON.parseArray(jsonArray.toString(), OrderInfoModel.class));
                    com.alibaba.fastjson.JSONObject orderjsonobject = JSON.parseObject(orderarray.get(i).toString());
                    Map<String, Object> groupmap = new HashMap<String, Object>();
                    if (orderjsonobject.get("status") != null && orderjsonobject.getString("status").toString().length() > 0) {
                        groupmap.put("status", orderjsonobject.getString("status").toString());
                    }
                    if (orderjsonobject.get("deposit") != null) {
                        groupmap.put("order_deposit", Double.valueOf(orderjsonobject.get("deposit").toString()));
                    }
                    if (orderjsonobject.get("service_fee") != null) {
                        groupmap.put("service_fee", Double.valueOf(orderjsonobject.get("service_fee").toString()));
                    }

                    if (orderjsonobject.get("field_order_item_id") != null && orderjsonobject.getString("field_order_item_id").toString().length() > 0) {
                        groupmap.put("order_id", orderjsonobject.getString("field_order_item_id").toString());
                    }
                    if (orderjsonobject.get("real_pay") != null && orderjsonobject.getString("real_pay").toString().length() > 0) {
                        groupmap.put("actual_fee", orderjsonobject.getString("real_pay").toString());
                    }
                    groupmap.put("order_num", "order_num");
                    if (orderjsonobject.get("ordered_user") != null && orderjsonobject.get("ordered_user").toString().length() > 0) {
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id").toString().length() > 0) {
                            groupmap.put("ordered_user_id", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("id").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile").toString().length() > 0) {
                            groupmap.put("ordered_user_mobile", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("mobile").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name").toString().length() > 0) {
                            groupmap.put("ordered_user_name", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("name").toString());
                        }
                    }

                    if (orderjsonobject.get("paid_by") != null && orderjsonobject.get("paid_by").toString().length() > 0) {
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id").toString().length() > 0) {
                            groupmap.put("paid_by_id", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("id").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile").toString().length() > 0) {
                            groupmap.put("paid_by_mobile", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("mobile").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name").toString().length() > 0) {
                            groupmap.put("paid_by_name", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("name").toString());
                        }
                    }
                    mData[currIndex].add(groupmap);
                    OrderChildList[currIndex].add(orderlist_tmp);
                }
            }
            mOrderExpandableListAdapter[currIndex] = new OrderExpandableListAdapter(OrderListNewActivity.this.getContext(), mData[currIndex], OrderChildList[currIndex], OrderListNewActivity.this, currIndex);
            mOrderExpandableList[currIndex].setAdapter(mOrderExpandableListAdapter[currIndex]);
            if (mswipList[currIndex].isShown()) {
                mswipList[currIndex].setRefreshing(false);
            }
            for (int n = 0; n < mData[currIndex].size(); n++) {
                mOrderExpandableList[currIndex].expandGroup(n);
            }
            mOrderExpandableList[currIndex].setGroupIndicator(null);
            mOrderExpandableList[currIndex].setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (mswipList[currIndex].isShown()) {
                mswipList[currIndex].setRefreshing(false);
            }
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);

        }
    };
    private LinhuiAsyncHttpResponseHandler getMoreOrderListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data == null || data.toString().length() == 0) {
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            if (jsonObject == null || jsonObject.get("data") == null ||
                    jsonObject.get("data").toString().length() == 0) {
                return;
            }
            com.alibaba.fastjson.JSONArray orderarray = JSON.parseArray(jsonObject.get("data").toString());
            if (orderarray == null || orderarray.isEmpty()) {
                orderlistpagesize[currIndex] = orderlistpagesize[currIndex] - 1;
                mOrderExpandableList[currIndex].set_loaded();
                return;
            }
            if (orderarray.size() < 10) {
                mOrderExpandableList[currIndex].set_loaded();
            }
            int startNum = mData[currIndex].size();
            for (int i = 0; i < orderarray.size(); i++) {
                if (currIndex == 2 || currIndex == 3 || currIndex == 4 || currIndex == 5) {
                    com.alibaba.fastjson.JSONArray jsonArray = new JSONArray();
                    jsonArray.add(JSON.parseObject(orderarray.get(i).toString()));
                    ArrayList<OrderInfoModel> orderlist_tmp = (ArrayList<OrderInfoModel>) (JSON.parseArray(jsonArray.toString(), OrderInfoModel.class));
                    com.alibaba.fastjson.JSONObject orderjsonobject = JSON.parseObject(orderarray.get(i).toString());
                    Map<String, Object> groupmap = new HashMap<String, Object>();
                    if (orderjsonobject.get("status") != null && orderjsonobject.getString("status").toString().length() > 0) {
                        groupmap.put("status", orderjsonobject.getString("status").toString());
                    }
                    if (orderjsonobject.get("deposit") != null) {
                        groupmap.put("order_deposit", Double.valueOf(orderjsonobject.get("deposit").toString()));
                    }
                    if (orderjsonobject.get("service_fee") != null) {
                        groupmap.put("service_fee", Double.valueOf(orderjsonobject.get("service_fee").toString()));
                    }

                    if (orderjsonobject.get("field_order_item_id") != null && orderjsonobject.getString("field_order_item_id").toString().length() > 0) {
                        groupmap.put("order_id", orderjsonobject.getString("field_order_item_id").toString());
                    }
                    if (orderjsonobject.get("real_pay") != null && orderjsonobject.getString("real_pay").toString().length() > 0) {
                        groupmap.put("actual_fee", orderjsonobject.getString("real_pay").toString());
                    }
                    groupmap.put("order_num", "order_num");
                    if (orderjsonobject.get("ordered_user") != null && orderjsonobject.get("ordered_user").toString().length() > 0) {
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id").toString().length() > 0) {
                            groupmap.put("ordered_user_id", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("id").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile").toString().length() > 0) {
                            groupmap.put("ordered_user_mobile", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("mobile").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name") != null &&
                                JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name").toString().length() > 0) {
                            groupmap.put("ordered_user_name", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("name").toString());
                        }
                    }

                    if (orderjsonobject.get("paid_by") != null && orderjsonobject.get("paid_by").toString().length() > 0) {
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id").toString().length() > 0) {
                            groupmap.put("paid_by_id", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("id").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile").toString().length() > 0) {
                            groupmap.put("paid_by_mobile", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("mobile").toString());
                        }
                        if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name") != null &&
                                JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name").toString().length() > 0) {
                            groupmap.put("paid_by_name", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("name").toString());
                        }
                    }
                    mData[currIndex].add(groupmap);
                    OrderChildList[currIndex].add(orderlist_tmp);

                } else if (currIndex == 0 || currIndex == 1) {
                    com.alibaba.fastjson.JSONObject orderjsonobject = JSON.parseObject(orderarray.get(i).toString());
                    if (orderjsonobject.get("resources") != null) {
                        ArrayList<OrderInfoModel> orderlist_tmp = (ArrayList<OrderInfoModel>) (JSON.parseArray(orderjsonobject.getString("resources").toString(), OrderInfoModel.class));
                        if (orderlist_tmp != null) {
                            if (orderlist_tmp.size() > 0) {
                                Map<String, Object> groupmap = new HashMap<String, Object>();
                                if (orderjsonobject.get("order_id") != null && orderjsonobject.getString("order_id").toString().length() > 0) {
                                    groupmap.put("order_id", orderjsonobject.getString("order_id").toString());
                                }
                                if (orderjsonobject.get("order_num") != null && orderjsonobject.getString("order_num").toString().length() > 0) {
                                    groupmap.put("order_num", orderjsonobject.getString("order_num").toString());
                                }
                                if (orderjsonobject.get("actual_fee") != null && orderjsonobject.getString("actual_fee").toString().length() > 0) {
                                    groupmap.put("actual_fee", orderjsonobject.getString("actual_fee").toString());
                                }
                                if (orderjsonobject.get("status") != null && orderjsonobject.getString("status").toString().length() > 0) {
                                    groupmap.put("status", orderjsonobject.getString("status").toString());
                                }
                                if (orderjsonobject.get("order_deposit") != null) {
                                    groupmap.put("order_deposit", Double.valueOf(orderjsonobject.get("order_deposit").toString()));
                                }
                                if (orderjsonobject.get("service_fee") != null) {
                                    groupmap.put("service_fee", Double.valueOf(orderjsonobject.get("service_fee").toString()));
                                }
                                if (orderjsonobject.get("tax") != null) {
                                    groupmap.put("tax", Double.valueOf(orderjsonobject.get("tax").toString()));
                                }
                                if (orderjsonobject.get("delivery_fee") != null) {
                                    groupmap.put("delivery_fee", Double.valueOf(orderjsonobject.get("delivery_fee").toString()));
                                }
                                if (orderjsonobject.get("offline_pay") != null && orderjsonobject.getString("offline_pay").toString().length() > 0) {
                                    groupmap.put("offline_pay", orderjsonobject.getInteger("offline_pay"));
                                }
                                if (orderjsonobject.get("voucher_image_url") != null && orderjsonobject.get("voucher_image_url").toString().length() > 0) {
                                    groupmap.put("voucher_image_url", orderjsonobject.getString("voucher_image_url"));
                                }
                                if (orderjsonobject.get("ordered_user") != null && orderjsonobject.get("ordered_user").toString().length() > 0) {
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id").toString().length() > 0) {
                                        groupmap.put("ordered_user_id", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("id").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile").toString().length() > 0) {
                                        groupmap.put("ordered_user_mobile", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("mobile").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name").toString().length() > 0) {
                                        groupmap.put("ordered_user_name", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("name").toString());
                                    }
                                }

                                if (orderjsonobject.get("paid_by") != null && orderjsonobject.get("paid_by").toString().length() > 0) {
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id").toString().length() > 0) {
                                        groupmap.put("paid_by_id", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("id").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile").toString().length() > 0) {
                                        groupmap.put("paid_by_mobile", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("mobile").toString());
                                    }
                                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name") != null &&
                                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name").toString().length() > 0) {
                                        groupmap.put("paid_by_name", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("name").toString());
                                    }
                                    if (orderjsonobject.get("remind_time") != null && orderjsonobject.get("remind_time").toString().length() > 0) {
                                        groupmap.put("remind_time", orderjsonobject.getString("remind_time") + "000");
                                    }
                                    if (orderjsonobject.get("alter_notice") != null && orderjsonobject.get("alter_notice").toString().length() > 0) {
                                        groupmap.put("alter_notice", orderjsonobject.getString("alter_notice"));//付款是否要弹窗提醒超时
                                    }
                                }
                                mData[currIndex].add(groupmap);
                                OrderChildList[currIndex].add(orderlist_tmp);
                            }
                        }
                    }
                }
            }
            mOrderExpandableListAdapter[currIndex].notifyDataSetChanged();
            for (int n = 0; n < mData[currIndex].size(); n++) {
                mOrderExpandableList[currIndex].expandGroup(n);
            }
            mOrderExpandableList[currIndex].setGroupIndicator(null);
            mOrderExpandableList[currIndex].setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
            mOrderExpandableList[currIndex].onLoadComplete();

            //2018/12/12 倒计时
            if (currIndex == 0 || currIndex == 1) {
                if (mTimers[currIndex] == null) {
                    mTimers[currIndex] = new Timer();
                }
                mTimers[currIndex].schedule(new OnCouponTimerTask(mData[currIndex],mOrderExpandableListAdapter[currIndex],
                        OrderListNewActivity.this,1,startNum),0,1000);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            mOrderExpandableList[currIndex].onLoadComplete();
            orderlistpagesize[currIndex] = orderlistpagesize[currIndex] - 1;
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };

    public void deleteorderitem(int position, String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position, str);
        test.getfieldsize_pricenuit(this);
    }

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        final String id = str;
        deleteitem = position;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        FieldApi.deleteordersitemlist(OrderListNewActivity.this, MyAsyncHttpClient.MyAsyncHttpClient(), deleteorderHandler, id);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void setVirtualNumber(int state, int position, String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(state, position, str);
        test.editorOrderApproved(this);
    }

    @Override
    public void postOrderApproved(int state, int position, String str) {
        if (state == -1) {
            if (position > 0 && str != null && str.length() > 0) {
                mServicePhone = str;
                showProgressDialog();
                FieldApi.getVirtualNumber(MyAsyncHttpClient.MyAsyncHttpClient(),
                        virtualNumberHandler, String.valueOf(position));
            }
        }
    }

    public class OrderPagerAdapter extends PagerAdapter {
        public List<View> mListViews = null;

        public OrderPagerAdapter(List<View> mListViews) {
            super();
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mListViews.get(position), 0);
//			setPagerInfo(0);
            return mListViews.get(position);
        }

    }

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            switch (position) {
                case 0:
                    setView();
                    break;
                case 1:
                    setView();
                    break;
                case 2:
                    setView();
                    break;
                case 3:
                    setView();
                    break;
                case 4:
                    setView();
                    break;
                case 5:
                    setView();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setordertypeview(int type) {
        switch (type) {
            case 0:
                setView();
                break;
            case 1:
                setView();
                break;
            case 2:
                setView();
                break;
            case 3:
                setView();
                break;
            case 4:
                setView();
                break;
            case 5:
                setView();
                break;
            default:
                break;
        }

    }

    private LinhuiAsyncHttpResponseHandler deleteorderHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.cancel_success));
            mData[currIndex].remove(deleteitem);
            OrderChildList[currIndex].remove(deleteitem);
            mOrderExpandableListAdapter[currIndex].notifyDataSetChanged();
            if (mData[currIndex].size() == 0 && OrderChildList[currIndex].size() == 0) {
                mLay_no_order[currIndex].setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };

    @Override
    public void addfield_deletechoosepicture(int item_num) {
        AndPermission.with(OrderListNewActivity.this)
                .requestCode(Constants.PermissionRequestCode)
                .permission(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();

    }

    public void showService(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyDeleteChoosePicture(this);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(OrderListNewActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
                    @Override
                    public void onSuccess(String clientId) {
                        if (LoginManager.isLogin()) {
                            HashMap<String, String> clientInfo = new HashMap<>();
                            clientInfo.put("name", LoginManager.getCompany_name());
                            clientInfo.put("email", LoginManager.geteEmail());
                            if (LoginManager.getRole_id().equals("2")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_property_str));
                            } else if (LoginManager.getRole_id().equals("3")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_business_str));
                            } else if (LoginManager.getRole_id().equals("1")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_admin_str));
                            }
                            clientInfo.put("avatar", "https://banner.linhuiba.com/o_1b555h2jjoj6u1716tr12dl2rg7.jpg");
                            clientInfo.put("tel", LoginManager.getMobile());
                            // 相同的 id 会被识别为同一个顾客
                            Intent intent = new MQIntentBuilder(OrderListNewActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent, 10);
                        } else {
                            Intent intent = new MQIntentBuilder(OrderListNewActivity.this)
                                    .build();
                            startActivityForResult(intent, 10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });
            } else if (requestCode == CALL_PHONE_CODE) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + callPhoneStr));
                if (ActivityCompat.checkSelfPermission(OrderListNewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            } else if (requestCode == CALL_PHONE_CODE) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_rationale));
            }
        }
    };
    private LinhuiAsyncHttpResponseHandler virtualNumberHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, final Object data) {
            hideProgressDialog();
            if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                View.OnClickListener uploadListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_perfect:
                                mCustomDialog.dismiss();
                                if (data != null && data.toString().length() > 0) {
                                    callPhoneStr = data.toString();
                                    AndPermission.with(OrderListNewActivity.this)
                                            .requestCode(CALL_PHONE_CODE)
                                            .permission(
                                                    Manifest.permission.CALL_PHONE,
                                                    Manifest.permission.READ_PHONE_STATE)
                                            .callback(listener)
                                            .start();
                                }

                                break;
                            case R.id.btn_cancel:
                                mCustomDialog.dismiss();
                        }
                    }
                };
                CustomDialog.Builder builder = new CustomDialog.Builder(OrderListNewActivity.this);
                mCustomDialog = builder
                        .cancelTouchout(false)
                        .view(R.layout.field_activity_field_orders_success_dialog)
                        .addViewOnclick(R.id.btn_cancel, uploadListener)
                        .addViewOnclick(R.id.btn_perfect, uploadListener)
                        .setText(R.id.dialog_title_textview,
                                getResources().getString(R.string.order_call_service_phone_str))
                        .setText(R.id.btn_cancel,
                                getResources().getString(R.string.cancel))
                        .setText(R.id.btn_perfect,
                                getResources().getString(R.string.confirm))
                        .showView(R.id.linhuiba_logo_imgv, View.GONE)
                        .build();
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(OrderListNewActivity.this, mCustomDialog);
                mCustomDialog.show();
                mDialogHandler.removeMessages(0);
                mDialogHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                    }
                }, 30000);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (mServicePhone != null && mServicePhone.toString().length() > 0) {
                callPhoneStr = mServicePhone.toString();
                AndPermission.with(OrderListNewActivity.this)
                        .requestCode(CALL_PHONE_CODE)
                        .permission(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE)
                        .callback(listener)
                        .start();
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("order_type") != null) {
                currIndex = bundle.getInt("order_type");
            }
            if (bundle.get("showdialog") != null) {
                showdialog = bundle.getInt("showdialog");
            }
        }
    }

    private void showOrderPayDialog(int type) {
        if ((mShowFailureDialog == null || !mShowFailureDialog.isShowing()) &&
                (type == 1 || type == 2)) {
            String msg = "";
            if (type == 1) {
                msg = getResources().getString(R.string.order_pay_showdialog);
            } else if (type == 2) {
                msg = getResources().getString(R.string.order_refuse_offline_pay_failuremessage);
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            mShowFailureDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            mShowFailureDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect, listener)
                    .setText(R.id.dialog_title_msg_tv,
                            getResources().getString(R.string.order_pay_error_message))
                    .setText(R.id.dialog_title_textview, msg)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .showView(R.id.linhuiba_logo_imgv, View.GONE)
                    .showView(R.id.btn_cancel, View.GONE)
                    .showView(R.id.dialog_title_msg_tv, View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(this, mShowFailureDialog);
            mShowFailureDialog.show();
        }
    }
    public void showPayOvertimeDialog(final String order_id, final String order_num, final String order_price) {
        if ((mShowFailureDialog == null || !mShowFailureDialog.isShowing())) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.app_defaylt_cancel_tv:
                            mShowFailureDialog.dismiss();
                            break;
                        case R.id.app_defaylt_confirm_tv:
                            Intent choosepayway_intent = new Intent(OrderListNewActivity.this,OrderConfirmChoosePayWayActivity.class);
                            choosepayway_intent.putExtra("payment_option",1);
                            choosepayway_intent.putExtra("order_id",order_id);
                            choosepayway_intent.putExtra("order_num",order_num);
                            choosepayway_intent.putExtra("order_price",order_price);
                            startActivity(choosepayway_intent);
                            mShowFailureDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            mShowFailureDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.activity_fieldinfo_refund_price_popuwindow)
                    .addViewOnclick(R.id.app_defaylt_cancel_tv, listener)
                    .addViewOnclick(R.id.app_defaylt_confirm_tv, listener)
                    .setText(R.id.app_defaylt_title_tv,
                            getResources().getString(R.string.module_order_pay_overtime_remind_title))
                    .setText(R.id.app_defaylt_content_tv,
                            getResources().getString(R.string.module_order_pay_overtime_remind_content))
                    .setText(R.id.app_defaylt_confirm_tv,
                            getResources().getString(R.string.module_order_pay_overtime_remind_keep_apy))
                    .showView(R.id.app_defaylt_dialog_ll,View.VISIBLE)
                    .showView(R.id.app_defaylt_dialog_remind_ll, View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(this, mShowFailureDialog);
            mShowFailureDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            currIndex = 0;
            setordertypeview(currIndex);
            fPager.setCurrentItem(currIndex);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
