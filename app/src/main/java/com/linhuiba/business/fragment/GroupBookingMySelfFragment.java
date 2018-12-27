package com.linhuiba.business.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseFragment;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.EnterpriseManagementActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.GroupBookingInfoActivity;
import com.linhuiba.business.activity.GroupBookingMainActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.OrderConfirmActivity;
import com.linhuiba.business.activity.OrderConfirmChoosePayWayActivity;
import com.linhuiba.business.activity.OrderListNewActivity;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.GroupBookingFragmentOrderAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.model.GroupBookingOrderListModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/9/19.
 */

public class GroupBookingMySelfFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,
        Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall ,
        Field_AddFieldChoosePictureCallBack.FieldOrderApproved{
    @InjectView(R.id.groupbooking_myself_tablayout)
    TabLayout mGroupBookingMyselfTL;
    @InjectView(R.id.groupbooking_myself_viewpager)
    ViewPager mGroupBookingMyselfVP;

    private View mMainContentView;
    private int mTabTextViewList[] = {R.string.all_order_txt, R.string.myselfinfo_pay,R.string.groupbooding_mysefl_orders_underway_status_str, R.string.groupbooding_mysefl_orders_success_status_str,
            R.string.groupbooding_mysefl_orders_fail_status_str};
    public int mCurrIndex = 0;// 当前页卡编号
    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipeRL= new SwipeRefreshLayout[5];
    private LoadMoreListView[] mGroupBookingMyselfLV = new LoadMoreListView[5];
    private GroupBookingFragmentOrderAdapter[] mGroupBookingMyselfAdapter= new GroupBookingFragmentOrderAdapter[5];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[5];
    private int[] mPagePosition = new int[5];
    private ArrayList<GroupBookingOrderListModel>[] mGroupBookingMyselfDataList = new ArrayList[5] ;
    private int mDeleteOrderPosition;
    public boolean isPayOperation;
    private GroupBookingOrderListModel groupBookingOrderListModel = new GroupBookingOrderListModel();
    private final int mLoginIntent = 1;
    private String mServicePhone;
    private CustomDialog mCustomDialog;
    private Handler mDialogHandler = new Handler();
    private final int CALL_PHONE_CODE = 110;
    private String callPhoneStr = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContentView == null) {
            mMainContentView = inflater.inflate(R.layout.fragment_groupbooking_myself,container,false);
            initView();
        }
        return mMainContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginManager.isLogin()) {
            GroupBookingMainActivity activity = (GroupBookingMainActivity)GroupBookingMySelfFragment.this.getActivity();
            if (activity.mCurrIndex == 1) {
                mCurrIndex = 1;
            } else if (activity.mCurrIndex == 2) {
                mCurrIndex = 2;
            }
            refreshView();
            mGroupBookingMyselfVP.setCurrentItem(mCurrIndex);
        } else {
            Intent intent = new Intent(GroupBookingMySelfFragment.this.getActivity(), LoginActivity.class);
            startActivityForResult(intent, mLoginIntent);
        }
    }

    private void initView() {
        ButterKnife.inject(this,mMainContentView);
        TitleBarUtils.setTitleText(mMainContentView,getResources().getString(R.string.groupbooding_myself_str));
        TitleBarUtils.showBackImg(mMainContentView,GroupBookingMySelfFragment.this.getActivity(),true);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.fragment_groupbooking_myself_vp_item, null));
        mListViews.add(inflater.inflate(R.layout.fragment_groupbooking_myself_vp_item, null));
        mListViews.add(inflater.inflate(R.layout.fragment_groupbooking_myself_vp_item, null));
        mListViews.add(inflater.inflate(R.layout.fragment_groupbooking_myself_vp_item, null));
        mListViews.add(inflater.inflate(R.layout.fragment_groupbooking_myself_vp_item, null));
        for( int i=0 ;i< mListViews.size(); i++ ) {
            mGroupBookingMyselfDataList[i] = new ArrayList<GroupBookingOrderListModel>();
            mSwipeRL[i] = (SwipeRefreshLayout)mListViews.get(i). findViewById(R.id.groupbooking_swipe_refresh);
            mSwipeRL[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mGroupBookingMyselfLV[i]  = (LoadMoreListView) mListViews.get(i).findViewById(R.id.groupbooking_listview);
            mSwipeRL[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mSwipeRL[i].setOnRefreshListener(this);
            mGroupBookingMyselfLV[i].setLoadMoreListen(this);
            mNullDataRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.groupbooking_lay_no_order);
            mNullDataRL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更新数据
                    showProgressDialog();
                    initData();
                }
            });
        }
        mGroupBookingMyselfVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        mGroupBookingMyselfVP.setOnPageChangeListener(new PagerChangeListener());
        mGroupBookingMyselfTL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mGroupBookingMyselfTL,8,8);
            }
        });
        mGroupBookingMyselfTL.setupWithViewPager(mGroupBookingMyselfVP);
        for (int i=0;i<mGroupBookingMyselfTL.getTabCount();i++) {
            mGroupBookingMyselfTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }
    }
    private void initData() {
        mGroupBookingMyselfLV[mCurrIndex].set_refresh();
        mPagePosition[mCurrIndex] = 1;
        FieldApi.getGroupBookingListOrders(MyAsyncHttpClient.MyAsyncHttpClient(),getPublicActivityListOrdersHandler,mPagePosition[mCurrIndex],getStatusStr(mCurrIndex));
    }
    public void orderOperation(int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position,str);
        test.getfieldsize_pricenuit(this);
    }

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        if (position == -1) {
            //催审核
            AndPermission.with(GroupBookingMySelfFragment.this)
                    .requestCode(Constants.PermissionRequestCode)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(listener)
                    .start();

        } else {
            if (isPayOperation) {
                groupBookingOrderListModel = JSONObject.parseObject(str,GroupBookingOrderListModel.class);
                if (groupBookingOrderListModel.getSelling_resource().getGroup_purchase().getId() != null &&
                        groupBookingOrderListModel.getSelling_resource().getGroup_purchase().getId() > 0) {
                    FieldApi.getGroupStatus(MyAsyncHttpClient.MyAsyncHttpClient(),getGroupStatusHandler,
                            String.valueOf(groupBookingOrderListModel.getSelling_resource().getGroup_purchase().getId()));
                }
            } else {
                mDeleteOrderPosition = position;
                final String id = str;
                MessageUtils.showDialog(GroupBookingMySelfFragment.this.getContext(), getResources().getString(R.string.dialog_prompt),
                        getResources().getString(R.string.delete_prompt),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                FieldApi.deleteordersitemlist(GroupBookingMySelfFragment.this.getContext(), MyAsyncHttpClient.MyAsyncHttpClient(), deleteOrderHandler, id);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
            isPayOperation = false;
        }
    }
    public void setVirtualNumber(int state,int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(state,position,str);
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

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrIndex = position;
            switch (position) {
                case 0:
                    refreshView();
                    break;
                case 1:
                    refreshView();
                    break;
                case 2:
                    refreshView();
                    break;
                case 3:
                    refreshView();
                    break;
                case 4:
                    refreshView();
                    break;
                default:
                    break;
            }

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void refreshView() {
        if (LoginManager.isLogin()) {
            showProgressDialog();
            initData();
        } else {
            LoginManager.getInstance().clearLoginInfo();
        }
    }
    private LinhuiAsyncHttpResponseHandler getPublicActivityListOrdersHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    if(mSwipeRL[mCurrIndex].isShown()){
                        mSwipeRL[mCurrIndex].setRefreshing(false);
                    }
                    mGroupBookingMyselfDataList[mCurrIndex] = (ArrayList<GroupBookingOrderListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingOrderListModel.class);
                    if( mGroupBookingMyselfDataList[mCurrIndex] == null ||  mGroupBookingMyselfDataList[mCurrIndex].isEmpty()) {
                        mNullDataRL[mCurrIndex].setVisibility(View.VISIBLE);
                        return;
                    }
                    mNullDataRL[mCurrIndex].setVisibility(View.GONE);
                    if (mGroupBookingMyselfDataList[mCurrIndex].size() < 10) {
                        mGroupBookingMyselfLV[mCurrIndex].set_loaded();
                    }
                    mGroupBookingMyselfAdapter[mCurrIndex]= new GroupBookingFragmentOrderAdapter(GroupBookingMySelfFragment.this,GroupBookingMySelfFragment.this.getContext(),mGroupBookingMyselfDataList[mCurrIndex]);
                    mGroupBookingMyselfLV[mCurrIndex].setAdapter(mGroupBookingMyselfAdapter[mCurrIndex]);
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mSwipeRL[mCurrIndex].isShown()){
                mSwipeRL[mCurrIndex].setRefreshing(false);
            }
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
        }
    };
    private LinhuiAsyncHttpResponseHandler getPublicActivityListOrdersMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mGroupBookingMyselfLV[mCurrIndex].onLoadComplete();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    ArrayList<GroupBookingOrderListModel> tmp = (ArrayList<GroupBookingOrderListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingOrderListModel.class);
                    if( tmp == null ||  tmp.isEmpty()) {
                        mPagePosition[mCurrIndex] = mPagePosition[mCurrIndex] - 1;
                        mGroupBookingMyselfLV[mCurrIndex].set_loaded();
                        return;
                    }
                    for(GroupBookingOrderListModel listitem: tmp ){
                        mGroupBookingMyselfDataList[mCurrIndex].add(listitem);
                    }
                    mGroupBookingMyselfAdapter[mCurrIndex].notifyDataSetChanged();
                    if (tmp.size() < 10) {
                        mGroupBookingMyselfLV[mCurrIndex].set_loaded();
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mGroupBookingMyselfLV[mCurrIndex].onLoadComplete();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
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
                        switch (view.getId()){
                            case R.id.btn_perfect:
                                mCustomDialog.dismiss();
                                if (data != null && data.toString().length() > 0) {
                                    callPhoneStr = data.toString();
                                    AndPermission.with(GroupBookingMySelfFragment.this)
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
                CustomDialog.Builder builder = new CustomDialog.Builder(GroupBookingMySelfFragment.this.getActivity());
                mCustomDialog = builder
                        .cancelTouchout(false)
                        .view(R.layout.field_activity_field_orders_success_dialog)
                        .addViewOnclick(R.id.btn_cancel,uploadListener)
                        .addViewOnclick(R.id.btn_perfect,uploadListener)
                        .setText(R.id.dialog_title_textview,
                                getResources().getString(R.string.order_call_service_phone_str))
                        .setText(R.id.btn_cancel,
                                getResources().getString(R.string.cancel))
                        .setText(R.id.btn_perfect,
                                getResources().getString(R.string.confirm))
                        .showView(R.id.linhuiba_logo_imgv,View.GONE)
                        .build();
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(GroupBookingMySelfFragment.this.getActivity(),mCustomDialog);
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
                AndPermission.with(GroupBookingMySelfFragment.this)
                        .requestCode(CALL_PHONE_CODE)
                        .permission(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE)
                        .callback(listener)
                        .start();
            }
        }
    };
    private String getStatusStr(int mCurrIndex) {
        String statusStr ="";
        switch (mCurrIndex) {
            case 0:
                statusStr = "all";
                break;
            case 1:
                statusStr = "submitted";
                break;
            case 2:
                statusStr ="grouping";
                break;
            case 3:
                statusStr = "success";
                break;
            case 4:
                statusStr = "fail";
                break;
            default:
                break;
        }
        return statusStr;
    }
    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void loadMore() {
        mPagePosition[mCurrIndex] ++;
        FieldApi.getGroupBookingListOrders(MyAsyncHttpClient.MyAsyncHttpClient(),getPublicActivityListOrdersMoreHandler,mPagePosition[mCurrIndex],getStatusStr(mCurrIndex));

    }
    private LinhuiAsyncHttpResponseHandler deleteOrderHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.cancel_success));
            mGroupBookingMyselfDataList[mCurrIndex].remove(mDeleteOrderPosition);
            mGroupBookingMyselfAdapter[mCurrIndex].notifyDataSetChanged();
            if (mGroupBookingMyselfDataList[mCurrIndex].size() == 0) {
                mNullDataRL[mCurrIndex].setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccesstoken(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getGroupStatusHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null) {
                    if (jsonObject.get("flag") != null &&
                            jsonObject.get("flag").toString().length() > 0) {
                        if (Integer.parseInt(jsonObject.get("flag").toString()) == 1) {
                            MessageUtils.showDialog(GroupBookingMySelfFragment.this.getContext(),"",
                                    getResources().getString(R.string.groupbooding_pay_next_status_tv_str),
                                    R.string.confirm,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent choosepayway_intent = new Intent(getActivity(),OrderConfirmChoosePayWayActivity.class);
                                            choosepayway_intent.putExtra("payment_option",3);
                                            choosepayway_intent.putExtra("order_id",String.valueOf(groupBookingOrderListModel.getField_order().get("id")));
                                            choosepayway_intent.putExtra("order_num",groupBookingOrderListModel.getField_order().get("order_num"));
                                            choosepayway_intent.putExtra("order_price", com.linhuiba.business.connector.Constants.getdoublepricestring(groupBookingOrderListModel.getReal_cost(),1));
                                            startActivity(choosepayway_intent);
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                        } else if (Integer.parseInt(jsonObject.get("flag").toString()) == 2) {
                            MessageUtils.showToast(getResources().getString(R.string.groupbooding_pay_finish_status_tv_str));
                        } else {
                            Intent choosepayway_intent = new Intent(getActivity(),OrderConfirmChoosePayWayActivity.class);
                            choosepayway_intent.putExtra("payment_option",3);
                            choosepayway_intent.putExtra("order_id",String.valueOf(groupBookingOrderListModel.getField_order().get("id")));
                            choosepayway_intent.putExtra("order_num",groupBookingOrderListModel.getField_order().get("order_num"));
                            choosepayway_intent.putExtra("order_price", com.linhuiba.business.connector.Constants.getdoublepricestring(groupBookingOrderListModel.getReal_cost(),1));
                            startActivity(choosepayway_intent);
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(GroupBookingMySelfFragment.this.getActivity(), com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(GroupBookingMySelfFragment.this.getActivity())
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(GroupBookingMySelfFragment.this.getActivity())
                                    .build();
                            startActivityForResult(intent,10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));            }
                });
            } else if (requestCode == CALL_PHONE_CODE) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + callPhoneStr));
                if (ActivityCompat.checkSelfPermission(GroupBookingMySelfFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            } else if (requestCode == CALL_PHONE_CODE) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_rationale));
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mLoginIntent) {
            if (LoginManager.isLogin()) {
                initView();
            } else {
                GroupBookingMainActivity activity = (GroupBookingMainActivity)GroupBookingMySelfFragment.this.getActivity();
                activity.mGroupBookingTabHost.setCurrentTab(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
