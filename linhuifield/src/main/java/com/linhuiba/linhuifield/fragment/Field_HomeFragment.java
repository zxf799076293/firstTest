package com.linhuiba.linhuifield.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.BuildConfig;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.ChildAccountManagementActivity;
import com.linhuiba.linhuifield.fieldactivity.CommunityResourcesActivity;
import com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_Enterprise_CertificationActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_OrderRefuseActivity;
import com.linhuiba.linhuifield.fieldactivity.PropertyDataStatisticalActivity;
import com.linhuiba.linhuifield.fieldadapter.Field_OrdersAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.Field_OrderModel;
import com.linhuiba.linhuifield.fieldmodel.VersionModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldOrderPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldOrderMvpView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.FieldMyListView;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Field_HomeFragment extends FieldBaseMvpFragment implements Field_AddFieldChoosePictureCallBack.FieldOrderApproved,
         Field_MyScrollview.OnScrollListener, FieldOrderMvpView {
    private View mMainContent;
    private ImageView mImgFieldHome;
    private FieldMyListView orderremind_listview;
    private LinearLayout mfield_home_neworder_remind_layout;
    private LinearLayout mfield_fragment_field_layout;
    private LinearLayout mfield_fragment_activity_layout;

    private LinearLayout mfield_fragment_payment_account_layout;
    private LinearLayout mfield_fragment_data_statistics_layout;
    private LinearLayout mfield_fragment_notice_guard_layout;
    private LinearLayout mfield_fragment_exclusive_layout;
    private LinearLayout mmyenterprise_all_layout;
    private LinearLayout mfield_fragment_myenterprise_layout;
    private LinearLayout mfield_fragment_enterprise_attestation_layout;
    private Field_MyScrollview mfield_home_scrollview;
    private LinearLayout mFieldSwitchBusinessLL;
    private HashMap<String, String> mbunner_map;
    private ArrayList<String> mPicList = new ArrayList<String>();//banner list
    private Field_OrdersAdapter field_ordersAdapter;
    private List<Field_OrderModel> mgrouplistdata  = new  ArrayList();
    private int deleteposition;
    private String approvedid;
    private FieldMainTabActivity mainactivity;
    private CustomDialog mCustomDialog;
    private boolean isForcedUpdating;
    private CustomDialog mCertificateDialog;
    private boolean isCreate;
    private CustomDialog mOrderOperationDialog;
    //虚拟呼叫
    private final int CALL_PHONE_CODE = 110;
    private Handler mDialogHandler = new Handler();
    public String mAdapterPhoneNum;
    private String mCallPhoneNum;
    public FieldOrderPresenter mFieldOrderPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_fragment_field_home, container, false);
            initview();
            //首页显示待审核订单
            showProgressDialog();
            initData();
            isCreate = true;
            hideSwitchDialog();
        }
        return mMainContent;
    }
    private void initview() {
        mFieldOrderPresenter = new FieldOrderPresenter();
        mFieldOrderPresenter.attachView(this);
        mImgFieldHome = (ImageView) mMainContent.findViewById(R.id.field_home_image);
        orderremind_listview = (FieldMyListView)mMainContent.findViewById(R.id.field_home_neworder_remind_listview);
        mfield_home_neworder_remind_layout = (LinearLayout)mMainContent.findViewById(R.id.field_home_neworder_remind_layout);
        mainactivity = (FieldMainTabActivity)Field_HomeFragment.this.getActivity();
        mfield_fragment_field_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_field_layout);
        mfield_fragment_activity_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_activity_layout);
        mfield_fragment_payment_account_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_payment_account_layout);
        mfield_fragment_data_statistics_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_data_statistics_layout);
        mfield_fragment_notice_guard_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_notice_guard_layout);
        mfield_fragment_exclusive_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_exclusive_layout);
        mmyenterprise_all_layout = (LinearLayout)mMainContent.findViewById(R.id.myenterprise_all_layout);
        mfield_fragment_myenterprise_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_myenterprise_layout);
        mfield_fragment_enterprise_attestation_layout = (LinearLayout)mMainContent.findViewById(R.id.field_fragment_enterprise_attestation_layout);
        mfield_home_scrollview = (Field_MyScrollview) mMainContent.findViewById(R.id.field_home_scrollview);
        mFieldSwitchBusinessLL = (LinearLayout)mMainContent.findViewById(R.id.property_home_switch_business_ll);
        TitleBarUtils.setTitleText(mMainContent,getResources().getString(R.string.field_home_title_text));
        mFieldSwitchBusinessLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitchDialog(3,getResources().getString(R.string.myself_switch_property_str),true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginManager.getInstance().setFieldexit(0);
                        Field_HomeFragment.this.getActivity().finish();
                    }
                },2000);
            }
        });
        mfield_fragment_field_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent field_intent = new Intent(Field_HomeFragment.this.getActivity(),Field_FieldListActivity.class);
                field_intent.putExtra("res_type_id",1);
                startActivity(field_intent);
            }
        });
        mfield_fragment_activity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_intent = new Intent(Field_HomeFragment.this.getActivity(),Field_FieldListActivity.class);
                activity_intent.putExtra("res_type_id",3);
                startActivity(activity_intent);
            }
        });
        //2018/3/15 收款账号
        mfield_fragment_payment_account_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.business.aboutActivity");
                intent.putExtra("type", Config.RECEIVE_ACCOUNT_INT);
                startActivityForResult(intent,Config.RECEIVE_ACCOUNT_INT);
            }
        });
        mfield_fragment_data_statistics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent field_intent = new Intent(Field_HomeFragment.this.getActivity(),PropertyDataStatisticalActivity.class);
                startActivity(field_intent);
            }
        });
        mfield_fragment_notice_guard_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent(Field_HomeFragment.this.getActivity(), ChildAccountManagementActivity.class);
                startActivity(childaccount);
            }
        });
        mfield_fragment_exclusive_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityresource = new Intent(Field_HomeFragment.this.getActivity(), CommunityResourcesActivity.class);
                startActivity(communityresource);
            }
        });
        mfield_fragment_myenterprise_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnterpriseManagement = new Intent(Field_HomeFragment.this.getActivity(),Field_Enterprise_CertificationActivity.class);
                startActivity(EnterpriseManagement);
            }
        });
        mfield_fragment_enterprise_attestation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enterpriseCertification = new Intent("com.business.aboutActivity");
                enterpriseCertification.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                startActivity(enterpriseCertification);
            }
        });
        if (LoginManager.getEnterprise_authorize_status() == 1) {
            mfield_fragment_myenterprise_layout.setVisibility(View.VISIBLE);
            mfield_fragment_enterprise_attestation_layout.setVisibility(View.GONE);
        } else {
            mfield_fragment_myenterprise_layout.setVisibility(View.GONE);
            mfield_fragment_enterprise_attestation_layout.setVisibility(View.VISIBLE);
        }
        if (mfield_fragment_enterprise_attestation_layout.getVisibility() == View.GONE &&
                mfield_fragment_myenterprise_layout.getVisibility() == View.GONE) {
            mmyenterprise_all_layout.setVisibility(View.GONE);
        } else {
            mmyenterprise_all_layout.setVisibility(View.VISIBLE);
        }
        //轮播功能
//        getimgpath();
//        show_banner();
        checkUpdate_new();
        if (LoginManager.getEnterprise_authorize_status() != 1 &&
                LoginManager.getInstance().getCertificate() == 0) {
            LoginManager.getInstance().setCertificate(1);
            showEnterpriseCertificate();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFieldOrderPresenter != null) {
            mFieldOrderPresenter.detachView();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isCreate) {
            if (LoginManager.getEnterprise_authorize_status() == 1) {
                mfield_fragment_myenterprise_layout.setVisibility(View.VISIBLE);
                mfield_fragment_enterprise_attestation_layout.setVisibility(View.GONE);
            } else {
                mfield_fragment_myenterprise_layout.setVisibility(View.GONE);
                mfield_fragment_enterprise_attestation_layout.setVisibility(View.VISIBLE);
            }
            if (mfield_fragment_enterprise_attestation_layout.getVisibility() == View.GONE &&
                    mfield_fragment_myenterprise_layout.getVisibility() == View.GONE) {
                mmyenterprise_all_layout.setVisibility(View.GONE);
            } else {
                mmyenterprise_all_layout.setVisibility(View.VISIBLE);
            }
        }
        if (mainactivity.isHomeRefreshOrder) {
            showProgressDialog();
            initData();
        }
        if (!isForcedUpdating && mCustomDialog != null &&
                mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    private void initData() {
        Field_FieldApi.getfieldorderlistitemscount(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), orderlistitemscountHandler);
    }
    private LinhuiAsyncHttpResponseHandler orderlistitemscountHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null ) {
                com.alibaba.fastjson.JSONObject jsonobject =  JSON.parseObject(data.toString());
                if (jsonobject.getString("paid")!= null) {
                    if (jsonobject.getString("paid").toString().length() !=0) {
                        if (!(jsonobject.getString("paid").toString().equals("0"))  ) {
                            mainactivity.fieldMainBV[1].setBadgeNumber(Integer.parseInt(jsonobject.getString("paid").toString()));
                            Field_FieldApi.getfieldorderlistitems(MyAsyncHttpClient.MyAsyncHttpClient2(),getfieldorderHandler,"paid",String.valueOf(1),String.valueOf(Integer.MAX_VALUE));
                        } else {
                            mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                            mainactivity.fieldMainBV[1].hide(false);
                        }
                    }  else {
                        mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                        mainactivity.fieldMainBV[1].hide(false);
                    }
                } else {
                    mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                }
            } else {
                hideProgressDialog();
                mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                mainactivity.fieldMainBV[1].hide(false);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            mfield_home_neworder_remind_layout.setVisibility(View.GONE);
            checkAccess(error);
        }
    };

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
            mgrouplistdata = (ArrayList<Field_OrderModel>) JSONArray.parseArray(jsonObject.get("data").toString(),Field_OrderModel.class);
            if( mgrouplistdata == null ||  mgrouplistdata.isEmpty()) {
                mfield_home_neworder_remind_layout.setVisibility(View.GONE);
            } else {
                mfield_home_neworder_remind_layout.setVisibility(View.VISIBLE);
                field_ordersAdapter = new Field_OrdersAdapter(Field_HomeFragment.this.getContext(),Field_HomeFragment.this,mgrouplistdata,0);
                orderremind_listview.setAdapter(field_ordersAdapter);
            }
            mainactivity.isHomeRefreshOrder = false;
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    public void editoraddress(int state,int position, String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(state,position,str);
        test.editorOrderApproved(this);
    }

    @Override
    public void postOrderApproved(int state, int position, String str) {
        approvedid = str;
        deleteposition = position;
        if (state == 0) {
            showDialog(1);
        } else if (state == 1) {
            showDialog(0);
        }

    }
    private LinhuiAsyncHttpResponseHandler OrderapprovedHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mgrouplistdata.remove(deleteposition);
            field_ordersAdapter.notifyDataSetChanged();
            if (mgrouplistdata.size() > 0) {
                if (mainactivity.fieldMainBV[1].getBadgeText().toString().length() > 0) {
                    if (mainactivity.fieldMainBV[1].getBadgeText().toString().equals("99+")) {
                        showProgressDialog();
                        initData();
                    } else {
                        if (Integer.parseInt(mainactivity.fieldMainBV[1].getBadgeText().toString()) > 1) {
                            mainactivity.fieldMainBV[1].setBadgeNumber(Integer.parseInt(mainactivity.fieldMainBV[1].getBadgeText().toString()) - 1);
                        } else {
                            mainactivity.fieldMainBV[1].hide(false);
                        }
                    }
                }
            } else {
                mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                mainactivity.fieldMainBV[1].hide(false);
            }
            mainactivity.isOrderRefreshOrder = true;
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
                            mgrouplistdata.remove(deleteposition);
                            field_ordersAdapter.notifyDataSetChanged();
                            if (mgrouplistdata.size() > 0) {
                                if (mainactivity.fieldMainBV[1].getBadgeText().toString().length() > 0) {
                                    if (mainactivity.fieldMainBV[1].getBadgeText().toString().equals("99+")) {
                                        showProgressDialog();
                                        initData();
                                    } else {
                                        if (Integer.parseInt(mainactivity.fieldMainBV[1].getBadgeText().toString()) > 1) {
                                            mainactivity.fieldMainBV[1].setBadgeNumber(Integer.parseInt(mainactivity.fieldMainBV[1].getBadgeText().toString()) - 1);
                                        } else {
                                            mainactivity.fieldMainBV[1].hide(false);
                                        }
                                    }
                                }
                            } else {
                                mfield_home_neworder_remind_layout.setVisibility(View.GONE);
                                mainactivity.fieldMainBV[1].hide(false);
                            }
                            mainactivity.isOrderRefreshOrder = true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onScroll(int scrollY) {

    }
    public void checkUpdate_new() {
        if (System.currentTimeMillis() - LoginManager.getInstance().getuodatatime() > 24 * 3600 * 1000) {
            Field_FieldApi.version(MyAsyncHttpClient.MyAsyncHttpClient(), mVersionHandler_new);
        }
    }
    private LinhuiAsyncHttpResponseHandler mVersionHandler_new = new LinhuiAsyncHttpResponseHandler(VersionModel.class) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null && data instanceof VersionModel) {
                final VersionModel version = (VersionModel) data;
                if (version != null && version.getVid() > BuildConfig.VERSION_CODE) {
                    if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                        View.OnClickListener uploadListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int i = view.getId();
                                if (i == R.id.upgrade_version_btn) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri content_url = Uri.parse(version.getUrl());
                                    intent.setData(content_url);
                                    Field_HomeFragment.this.getActivity().startActivity(intent);
                                    //2017/12/11 强制更新不关闭
                                    if (version.getForce_update() == 1) {

                                    } else {
                                        LoginManager.getInstance().setupdatatime();
                                        mCustomDialog.dismiss();
                                    }

                                } else if (i == R.id.upgrade_version_close_imgv) {
                                    mCustomDialog.dismiss();
                                    LoginManager.getInstance().setupdatatime();

                                }
                            }
                        };
                        CustomDialog.Builder builder = new CustomDialog.Builder(Field_HomeFragment.this.getContext());
                        //2017/12/8 判断是否强制更新
                        if (version.getForce_update() == 1) {
                            isForcedUpdating = true;
                            mCustomDialog = builder
                                    .cancelTouchout(false)
                                    .view(R.layout.app_upgrade_version)
                                    .addViewOnclick(R.id.upgrade_version_btn,uploadListener)
                                    .showView(R.id.upgrade_version_close_imgv,View.GONE)
                                    .setText(R.id.upgrade_version_content_tv,version.getNewfeature())
                                    .build();
                            mCustomDialog.setOnKeyListener(keylistener);
                        } else {
                            mCustomDialog = builder
                                    .cancelTouchout(false)
                                    .view(R.layout.app_upgrade_version)
                                    .addViewOnclick(R.id.upgrade_version_btn,uploadListener)
                                    .addViewOnclick(R.id.upgrade_version_close_imgv,uploadListener)
                                    .setText(R.id.upgrade_version_content_tv,version.getNewfeature())
                                    .build();
                        }
                        com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_HomeFragment.this.getContext(),mCustomDialog);
                        mCustomDialog.show();
                    }
                }
            }
        }
        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.toString());
        }
    };
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return true;
            } else {
                return true;
            }
        }
    };
    private void showEnterpriseCertificate() {
        if (mCertificateDialog == null || !mCertificateDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mCertificateDialog.dismiss();
                        Intent enterpriseCertification = new Intent("com.business.aboutActivity");
                        enterpriseCertification.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                        startActivity(enterpriseCertification);
                    } else if (i == R.id.btn_cancel) {
                        mCertificateDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_HomeFragment.this.getActivity());
            mCertificateDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel, uploadListener)
                    .addViewOnclick(R.id.btn_perfect, uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.home_certificate_msg_tv_str))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.cancel))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.home_certificate_btn_str))
                    .showView(R.id.linhuiba_logo_imgv, View.GONE)
                    .showView(R.id.dialog_title_msg_tv, View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_HomeFragment.this.getActivity(), mCertificateDialog);
            mCertificateDialog.show();
        }
    }
    private void showDialog(final int type) {//type: 0同意；1：拒绝
        String msg = "";
        if (type == 0) {
            if ( mgrouplistdata.get(deleteposition).getNumber_of_reserve() > 0) {
                msg = getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_first) +
                        String.valueOf( mgrouplistdata.get(deleteposition).getNumber_of_reserve()) +
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
                            Intent orderrefused = new Intent(Field_HomeFragment.this.getActivity(), Field_OrderRefuseActivity.class);
                            orderrefused.putExtra("approvedid", mgrouplistdata.get(deleteposition).getOrder_item_id());
                            startActivityForResult(orderrefused, 1);
                        } else if (type == 0) {
                            showProgressDialog();
                            Field_FieldApi.fieldorderlistitemapproved(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), OrderapprovedHandler, approvedid);
                        }
                    } else if (i == R.id.btn_cancel) {
                        mOrderOperationDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_HomeFragment.this.getActivity());
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
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_HomeFragment.this.getActivity(),mOrderOperationDialog);
            mOrderOperationDialog.show();
        }
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
                            AndPermission.with(Field_HomeFragment.this)
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
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_HomeFragment.this.getActivity());
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
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_HomeFragment.this.getActivity(), mCustomDialog);
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
            AndPermission.with(Field_HomeFragment.this)
                    .requestCode(CALL_PHONE_CODE)
                    .permission(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE)
                    .callback(listener)
                    .start();
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == CALL_PHONE_CODE) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + mCallPhoneNum));
                if (ActivityCompat.checkSelfPermission(Field_HomeFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
