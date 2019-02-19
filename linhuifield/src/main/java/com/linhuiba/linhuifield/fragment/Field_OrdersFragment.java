package com.linhuiba.linhuifield.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.FieldBaseFragment;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_OrderRefuseActivity;
import com.linhuiba.linhuifield.fieldadapter.Field_OrdersAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.Field_OrderModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldOrderPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldOrderMvpView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.FieldLoadMoreListView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/8/5.
 */
public class Field_OrdersFragment extends FieldBaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener, FieldLoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.FieldOrderApproved,
        FieldOrderMvpView {
    private ViewPager mFieldOrderViewPager;
    private int mCurrIndex = 0;// 当前页卡编号
    private ArrayList<View> mListViews;
    private FieldLoadMoreListView[] mFieldOrderLV = new FieldLoadMoreListView[5];
    private RelativeLayout[] mFieldOrderNullLL = new RelativeLayout[5];
    private SwipeRefreshLayout[] mSwipList= new SwipeRefreshLayout[5];
    private Field_OrdersAdapter[] mFieldOrderAdapter = new Field_OrdersAdapter[5];
    private List<Field_OrderModel>[] mFieldOrderDataList  = new  ArrayList[5];
    private int[] mListPageSize = new int[5];
    private int mDeletePosition;
    private String mApprovedId;
    private View mMainContent;
    private FieldMainTabActivity activity;
    private TabLayout mFieldOrderTableLayout;
    private int mTabTextViewInt[] = {R.string.myselfinfo_check, R.string.myselfinfo_waiting,
            R.string.myselfinfo_review,R.string.myselfinfo_reviewed, R.string.myselfinfo_refused};
    private CustomDialog mOrderOperationDialog;
    private int removedPageSize;
    private final int CALL_PHONE_CODE = 110;
    private CustomDialog mCustomDialog;
    private Handler mDialogHandler = new Handler();
    public String mAdapterPhoneNum;
    private String mCallPhoneNum;
    public FieldOrderPresenter mFieldOrderPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_activity_field_orders, container, false);
            TitleBarUtils.setTitleText(mMainContent,getResources().getString(R.string.field_maintab_order_management));
            if (!(LoginManager.isLogin())) {
                LoginManager.getInstance().clearLoginInfo();
                Intent intent = new Intent("com.business.loginActivity");
                startActivity(intent);
                Field_OrdersFragment.this.getActivity().finish();
            } else {
                if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
                    TitleBarUtils.showBackButton(mMainContent, Field_OrdersFragment.this.getActivity(),true,
                            Field_OrdersFragment.this.getActivity().getResources().getString(R.string.back),
                            Field_OrdersFragment.this.getActivity().getResources().getColor(R.color.default_bluebg),14);
                }
                initView();
                showProgressDialog();
                initData();
            }
        }
        return mMainContent;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((activity != null && //友盟错误日志
                mCurrIndex == 0 && activity.isOrderRefreshOrder)
                || (activity != null &&
                activity.isNewIntentRefreshOrder)) {
            showProgressDialog();
            removedPageSize = 0;
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFieldOrderPresenter != null) {
            mFieldOrderPresenter.detachView();
        }
    }

    private void initView() {
        mFieldOrderPresenter = new FieldOrderPresenter();
        mFieldOrderPresenter.attachView(this);
        mFieldOrderTableLayout = (TabLayout)mMainContent.findViewById(R.id.field_order_tablayout);
        activity = (FieldMainTabActivity)Field_OrdersFragment.this.getActivity();
        if (activity.OrderFragment_currIndex > -1) {
            mCurrIndex = activity.OrderFragment_currIndex;
        }
        mFieldOrderViewPager = (ViewPager) mMainContent.findViewById(R.id.fieldorder_viewpager);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = Field_OrdersFragment.this.getActivity().getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.field_loadmore_expandablelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmore_expandablelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmore_expandablelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmore_expandablelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmore_expandablelist_viewpager, null));
        mFieldOrderViewPager.setAdapter(new OrderPagerAdapter(mListViews));
        for( int i=0 ;i< mListViews.size(); i++ ){
            mFieldOrderLV[i]  = (FieldLoadMoreListView) mListViews.get(i).findViewById(R.id.ordernew_loadmore_expendlist);
            mFieldOrderLV[i].setDividerHeight(0);
            mFieldOrderLV[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mSwipList[i] = (SwipeRefreshLayout)  mListViews.get(i).findViewById(R.id.order_swipe_refresh);
            mFieldOrderLV[i].setLoadMoreListen(this);
            mSwipList[i].setOnRefreshListener(this);
            mSwipList[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mFieldOrderNullLL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.lay_no_order);
            mFieldOrderNullLL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    removedPageSize = 0;
                    initData();
                }
            });
        }
        mFieldOrderViewPager.setOnPageChangeListener(new OrderPagerChangeListener());
        mFieldOrderViewPager.setCurrentItem(mCurrIndex);
        mFieldOrderTableLayout.setupWithViewPager(mFieldOrderViewPager);
        for (int i = 0; i < mFieldOrderTableLayout.getTabCount(); i++) {
            mFieldOrderTableLayout.getTabAt(i).setText(getResources().getString(mTabTextViewInt[i]));
        }
    }
    private void initData() {
        mListPageSize[mCurrIndex] = 1;
        Field_FieldApi.getfieldorderlistitemscount(MyAsyncHttpClient.MyAsyncHttpClient2(), orderlistitemscountHandler);
    }
    private LinhuiAsyncHttpResponseHandler getfieldorderHandler = new LinhuiAsyncHttpResponseHandler() {
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
            if(mSwipList[mCurrIndex].isShown()){
                mSwipList[mCurrIndex].setRefreshing(false);
            }
            mFieldOrderDataList[mCurrIndex] = (ArrayList<Field_OrderModel>) JSONArray.parseArray(jsonObject.get("data").toString(),Field_OrderModel.class);
            if( mFieldOrderDataList[mCurrIndex] == null ||  mFieldOrderDataList[mCurrIndex].isEmpty()) {
                mFieldOrderNullLL[mCurrIndex].setVisibility(View.VISIBLE);
            } else {
                mFieldOrderNullLL[mCurrIndex].setVisibility(View.GONE);
                mFieldOrderAdapter[mCurrIndex] = new Field_OrdersAdapter(Field_OrdersFragment.this.getContext(),Field_OrdersFragment.this,mFieldOrderDataList[mCurrIndex],mCurrIndex);
                mFieldOrderLV[mCurrIndex].setAdapter(mFieldOrderAdapter[mCurrIndex]);
                if (mDeletePosition > 0 && removedPageSize > 0) {
                    mFieldOrderLV[mCurrIndex].setSelection(mDeletePosition - 1);
                }
                if (mFieldOrderDataList[mCurrIndex].size() < 10) {
                    mFieldOrderLV[mCurrIndex].set_loaded();
                }
            }
            if (mCurrIndex == 0) {
                activity.isOrderRefreshOrder = false;
            }
            if (activity.isNewIntentRefreshOrder) {
                activity.isNewIntentRefreshOrder = false;
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mSwipList[mCurrIndex].isShown()){
                mSwipList[mCurrIndex].setRefreshing(false);
            }
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
            removedPageSize = 0;
        }
    };
    private LinhuiAsyncHttpResponseHandler getfieldorderMoreHandler = new LinhuiAsyncHttpResponseHandler() {
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
            ArrayList<Field_OrderModel> tmp = (ArrayList<Field_OrderModel>) JSONArray.parseArray(jsonObject.get("data").toString(),Field_OrderModel.class);
            if (data != null && tmp.size() > 0) {
                for( Field_OrderModel fieldDetail: tmp ){
                    mFieldOrderDataList[mCurrIndex].add(fieldDetail);
                }
                mFieldOrderAdapter[mCurrIndex].notifyDataSetChanged();
                mFieldOrderLV[mCurrIndex].onLoadComplete();
                if (tmp.size() < 10 ) {
                    mFieldOrderLV[mCurrIndex].set_loaded();
                    removedPageSize = 0;
                }
            } else {
                mListPageSize[mCurrIndex] = mListPageSize[mCurrIndex]-1;
                mFieldOrderLV[mCurrIndex].set_loaded();
                removedPageSize = 0;
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mFieldOrderLV[mCurrIndex].onLoadComplete();
            mListPageSize[mCurrIndex] = mListPageSize[mCurrIndex]-1;
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());

            checkAccess(error);
        }
    };
    @Override
    public void loadMore() {
        mListPageSize[mCurrIndex] = mListPageSize[mCurrIndex] +1;
        String status = "0";
        if(mCurrIndex == 0) {
            status = "paid";
        } else if(mCurrIndex == 1) {
            status = "approved";
        } else if(mCurrIndex == 2) {
            status = "finished";
        }else if(mCurrIndex == 3) {
            status = "reviewed";
        } else if(mCurrIndex == 4) {
            status = "canceled";
        }
        if(!mFieldOrderDataList[mCurrIndex].isEmpty()){
            String pageSize = "10";
            if (removedPageSize > 0) {
                pageSize = String.valueOf(removedPageSize);
            }
            Field_FieldApi.getfieldorderlistitems(MyAsyncHttpClient.MyAsyncHttpClient2(), getfieldorderMoreHandler, status, String.valueOf(mListPageSize[mCurrIndex]), pageSize);
        }
    }

    @Override
    public void onRefresh() {
        removedPageSize = 0;
        initData();
    }

    @Override
    public void postOrderApproved(int state, int position, final String str) {
        mApprovedId = str;
        mDeletePosition = position;
        if (state == 0) {
            showDialog(1);
        } else if (state == 1) {
            showDialog(0);
        }

    }
    public void editoraddress(int state,int position, String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(state,position,str);
        test.editorOrderApproved(this);
    }

    @Override
    public void onVirtualNumberSuccess(final String data) {
        if (mCustomDialog == null || !mCustomDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mCustomDialog.dismiss();
                        if (data != null && data.toString().length() > 0) {
                            mCallPhoneNum = data.toString();
                            AndPermission.with(Field_OrdersFragment.this)
                                    .requestCode(CALL_PHONE_CODE)
                                    .permission(
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.READ_PHONE_STATE)
                                    .callback(listener)
                                    .start();
                        }


                    } else if (i == R.id.btn_cancel) {
                        mCustomDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_OrdersFragment.this.getActivity());
            mCustomDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel, uploadListener)
                    .addViewOnclick(R.id.btn_perfect, uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.moduld_field_order_call_service_phone_str))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.cancel))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .showView(R.id.linhuiba_logo_imgv, View.GONE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_OrdersFragment.this.getActivity(), mCustomDialog);
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
    public void onVirtualNumberFailure(boolean superresult, Throwable error) {
        if (mAdapterPhoneNum != null && mAdapterPhoneNum.toString().length() > 0) {
            mCallPhoneNum = mAdapterPhoneNum;
            AndPermission.with(Field_OrdersFragment.this)
                    .requestCode(CALL_PHONE_CODE)
                    .permission(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE)
                    .callback(listener)
                    .start();
        }
    }

    /**
     * ViewPager适配器
     * */
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
        public void destroyItem(View container, int position, Object object)
        {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

    }
    /**
     * 页卡切换监听
     * */
    public class OrderPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            mCurrIndex = arg0;
            switch (arg0) {
                case 0:
                    setPageView();
                    break;
                case 1:
                    setPageView();
                    break;
                case 2:
                    setPageView();
                    break;
                case 3:
                    setPageView();
                    break;
                case 4:
                    setPageView();
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

    }
    private void setPageView() {
        if (mFieldOrderAdapter[mCurrIndex] != null && mFieldOrderDataList[mCurrIndex] != null &&
                mFieldOrderDataList[mCurrIndex].size() > 0) {

        } else {
            showProgressDialog();
            removedPageSize = 0;
            initData();
        }
    }
    private LinhuiAsyncHttpResponseHandler orderlistitemscountHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null ) {
                com.alibaba.fastjson.JSONObject jsonobject =  JSON.parseObject(data.toString());
                if (jsonobject.getString("paid")!= null) {
                    if (jsonobject.getString("paid").toString().length() !=0) {
                        if (!(jsonobject.getString("paid").toString().equals("0"))  ) {
                            activity.fieldMainBV[1].setBadgeNumber(Integer.parseInt(jsonobject.getString("paid").toString()));
                        } else {
                            activity.fieldMainBV[1].hide(false);
                        }
                    }  else {
                        activity.fieldMainBV[1].hide(false);
                    }
                } else {
                    activity.fieldMainBV[1].hide(false);
                }
                mListPageSize[mCurrIndex] = 1;
                String status = "0";
                if(mCurrIndex == 0) {
                    status = "paid";
                } else if(mCurrIndex == 1) {
                    status = "approved";
                } else if(mCurrIndex == 2) {
                    status = "finished";
                }else if(mCurrIndex == 3) {
                    status = "reviewed";
                } else if(mCurrIndex == 4) {
                    status = "canceled";
                }
                mFieldOrderLV[mCurrIndex].set_refresh();
                String pageSize = "10";
                if (removedPageSize > 0) {
                    pageSize = String.valueOf(removedPageSize);
                }
                Field_FieldApi.getfieldorderlistitems(MyAsyncHttpClient.MyAsyncHttpClient2(),getfieldorderHandler,status,String.valueOf(mListPageSize[mCurrIndex]),pageSize);
            } else {
                hideProgressDialog();
                activity.fieldMainBV[1].hide(false);
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            mFieldOrderNullLL[mCurrIndex].setVisibility(View.VISIBLE);
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler OrderapprovedHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (mFieldOrderDataList[mCurrIndex].size() > mDeletePosition) {//友盟错误日志修改
                removedPageSize = (mFieldOrderDataList[mCurrIndex].size() / 10 + 1) * 10;
            } else {
                removedPageSize = 0;
            }
            initData();
            activity.isHomeRefreshOrder = true;
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(!superresult)
                BaseMessageUtils.showToast(error.getMessage());
           checkAccess(error);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data != null) {
                    if (data.getExtras().get("refused")!= null) {
                        if (data.getExtras().getInt("refused") == 1) {
                            mFieldOrderDataList[mCurrIndex].remove(mDeletePosition);
                            mFieldOrderAdapter[mCurrIndex].notifyDataSetChanged();
                            if (mFieldOrderDataList[mCurrIndex] == null ||
                                    mFieldOrderDataList[mCurrIndex].size() == 0) {
                                mFieldOrderNullLL[mCurrIndex].setVisibility(View.VISIBLE);
                            }
                            if (activity.fieldMainBV[1].getBadgeText().toString().trim().length() > 0) {
                                if (activity.fieldMainBV[1].getBadgeText().toString().equals("99+")) {
                                    removedPageSize = 0;
                                    showProgressDialog();
                                    initData();
                                } else {
                                    if (Integer.parseInt(activity.fieldMainBV[1].getBadgeText().toString().trim()) > 1) {
                                        activity.fieldMainBV[1].setBadgeNumber(Integer.parseInt(activity.fieldMainBV[1].getBadgeText().toString().trim()) - 1);
                                    } else {
                                        activity.fieldMainBV[1].hide(false);
                                    }
                                }
                            }
                            activity.isHomeRefreshOrder = true;
                        }
                    }
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showDialog(final int type) {//type: 0同意；1：拒绝
        String msg = "";
        if (type == 0) {
            if (mFieldOrderDataList[mCurrIndex].get(mDeletePosition).getNumber_of_reserve() > 0) {
                msg = getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_first) +
                        String.valueOf(mFieldOrderDataList[mCurrIndex].get(mDeletePosition).getNumber_of_reserve()) +
                        getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_second);
            } else {
                msg = getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_third);
            }
        } else if (type == 1) {
            msg = getResources().getString(R.string.moduld_field_order_deny_order_dialog_msg);
        }
        if (mOrderOperationDialog == null || !mOrderOperationDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mOrderOperationDialog.dismiss();
                        if (type == 1) {
                            Intent orderrefused = new Intent(Field_OrdersFragment.this.getActivity(), Field_OrderRefuseActivity.class);
                            orderrefused.putExtra("approvedid", mFieldOrderDataList[mCurrIndex].get(mDeletePosition).getOrder_item_id());
                            startActivityForResult(orderrefused, 1);
                        } else if (type == 0) {
                            showProgressDialog();
                            Field_FieldApi.fieldorderlistitemapproved(MyAsyncHttpClient.MyAsyncHttpClient2(), OrderapprovedHandler, mApprovedId);
                        }
                    } else if (i == R.id.btn_cancel) {
                        mOrderOperationDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_OrdersFragment.this.getActivity());
            mOrderOperationDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,msg)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_OrdersFragment.this.getActivity(),mOrderOperationDialog);
            mOrderOperationDialog.show();
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == CALL_PHONE_CODE) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + mCallPhoneNum));
                if (ActivityCompat.checkSelfPermission(Field_OrdersFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == CALL_PHONE_CODE) {
                BaseMessageUtils.showToast(getResources().getString(R.string.permission_message_permission_rationale));
            }
        }
    };

}
