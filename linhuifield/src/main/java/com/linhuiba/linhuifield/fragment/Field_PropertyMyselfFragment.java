package com.linhuiba.linhuifield.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.BuildConfig;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldbusiness.Field_UserApi;
import com.linhuiba.linhuifield.fieldmodel.FieldMyselfBadeInfoModel;
import com.linhuiba.linhuifield.fieldmodel.VersionModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.MyDampViewScrollview;
import com.linhuiba.linhuifield.fieldview.XCRoundImageView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */

public class Field_PropertyMyselfFragment extends FieldBaseMvpFragment implements MyDampViewScrollview.OnScrollListener {
    private View mMainContent;
    private ImageView mfragment_property_myself_setting_text;
    private RelativeLayout mfragment_property_mysef_message_layout;
    private TextView mfragment_myself_message_prompt;
    private XCRoundImageView mproperty_myphoto;
    private ImageView mPropertyGradeImaV;
    private TextView mPropertyName;
    private LinearLayout mmyselfinfo_property_account_certification_layout;
    private TextView mmyselfinfo_property_account_certification_text;
    private LinearLayout mmyself_property_integral_layout;
    private TextView mySelfPersonalCenterTV;
    private TextView mproperty_prompt_myselfinfo;
    private RelativeLayout mmyseif_property_info;
    private LinearLayout mproperty_field_fieldorderlist_linearlayout;
    private RelativeLayout mproperty_getappversion_layout;
    private RelativeLayout mproperty_invite_relativelayout;
    private RelativeLayout mproperty_service_layout;
    private RelativeLayout mMyselfTitleLL;
    private LinearLayout mMyselfTitleLLTmp;
    private MyDampViewScrollview mFragmentMyselfDampView;
    private ImageView mFragmentMyselfDampImgV;
    private RelativeLayout mFragmentMySelfWalletRL;
    private CustomDialog mCustomDialog;
    private boolean isForcedUpdating;
    private FieldMyselfBadeInfoModel mBadeInfoModel;
    private boolean isonCreate;
    private Dialog walletdialog;//开通钱包提示
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_fragment_property_myself, container, false);
            initview();
            refreshView();
            initData();
            isonCreate = true;
        }
        return mMainContent;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSwitchDialog();
        if (LoginManager.isLogin() && isonCreate) {
            refreshView();
        } else {
            Field_PropertyMyselfFragment.this.getActivity().finish();
        }
        if (!isForcedUpdating && mCustomDialog != null &&
                mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    private void initview() {
        mfragment_property_myself_setting_text = (ImageView)mMainContent.findViewById(R.id.fragment_property_myself_setting_text);
        mfragment_property_mysef_message_layout = (RelativeLayout)mMainContent.findViewById(R.id.fragment_property_mysef_message_layout);
        mfragment_myself_message_prompt = (TextView)mMainContent.findViewById(R.id.fragment_myself_message_prompt);
        mproperty_myphoto = (XCRoundImageView)mMainContent.findViewById(R.id.property_myphoto);
        mPropertyGradeImaV = (ImageView)mMainContent.findViewById(R.id.property_myself_membership_level_img);
        mPropertyName = (TextView)mMainContent.findViewById(R.id.property_myname);
        mmyselfinfo_property_account_certification_layout = (LinearLayout)mMainContent.findViewById(R.id.myselfinfo_property_account_certification_layout);
        mmyselfinfo_property_account_certification_text = (TextView)mMainContent.findViewById(R.id.myselfinfo_property_account_certification_text);
        mmyself_property_integral_layout = (LinearLayout)mMainContent.findViewById(R.id.myself_property_integral_layout);
        mySelfPersonalCenterTV = (TextView)mMainContent.findViewById(R.id.myself_personal_center_text);
        mproperty_prompt_myselfinfo = (TextView)mMainContent.findViewById(R.id.property_prompt_myselfinfo);
        mmyseif_property_info = (RelativeLayout)mMainContent.findViewById(R.id.myseif_property_info);
        mproperty_field_fieldorderlist_linearlayout = (LinearLayout)mMainContent.findViewById(R.id.property_field_fieldorderlist_linearlayout);
        mproperty_getappversion_layout = (RelativeLayout)mMainContent.findViewById(R.id.property_getappversion_layout);
        mproperty_invite_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.property_invite_relativelayout);
        mproperty_service_layout = (RelativeLayout)mMainContent.findViewById(R.id.property_service_layout);
        mMyselfTitleLL = (RelativeLayout) mMainContent.findViewById(R.id.fragment_myself_title_layout);
        mMyselfTitleLLTmp = (LinearLayout) mMainContent.findViewById(R.id.fragment_myself_title_layout_tmp);
        mFragmentMyselfDampView = (MyDampViewScrollview) mMainContent.findViewById(R.id.fragment_myself_dampview);
        mFragmentMyselfDampImgV = (ImageView) mMainContent.findViewById(R.id.fragment_myself_damp_img);
        mFragmentMySelfWalletRL = (RelativeLayout) mMainContent.findViewById(R.id.myself_my_wallet_rl);
        mfragment_property_myself_setting_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set_intent = new Intent("com.business.SettingActivity");
                startActivity(set_intent);
            }
        });
        mfragment_property_mysef_message_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mymessage = new Intent("com.business.MySelfPushMessageActivity");
                mymessage.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mymessage);
            }
        });
        mproperty_field_fieldorderlist_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitchDialog(3,getResources().getString(R.string.myself_switch_property_str),true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Field_PropertyMyselfFragment.this.getActivity().finish();
                        LoginManager.getInstance().setFieldexit(0);
                    }
                },2000);
            }
        });
        mproperty_getappversion_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate_new();
            }
        });
        mproperty_invite_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.isLogin()) {
                    Intent inviteintent = new Intent("com.business.InviteActivity");
                    startActivity(inviteintent);
                } else {
                    BaseMessageUtils.showToast(Field_PropertyMyselfFragment.this.getActivity(),getResources().getString(R.string.commoditypay_jumplogintext));
                    Intent myselfinfo_intent = new Intent("com.business.loginActivity");
                    startActivityForResult(myselfinfo_intent, 5);
                }
            }
        });
        mproperty_service_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(Field_PropertyMyselfFragment.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            }
        });
        mmyseif_property_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutusintent = new Intent("com.business.MyselfInfoActivity");
                startActivity(aboutusintent);
            }
        });
        mFragmentMySelfWalletRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.getInstance().getWallet_stauts() == 1) {
                    Intent groupIntent = new Intent("com.business.MyWalletActivity");
                    startActivity(groupIntent);
                } else {
                    show_wallet_PopupWindow(0);
                }
            }
        });
        mFragmentMyselfDampView.setImageView(mFragmentMyselfDampImgV);
        mFragmentMyselfDampView.setOnScrollListener(this);
        mMainContent.findViewById(R.id.fragment_myself_dampview).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mFragmentMyselfDampView.getScrollY());
                System.out.println(mFragmentMyselfDampView.getScrollY());
            }
        });
    }
    private void initData() {
        Field_UserApi.getbadge_info(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),badge_infoHandler);
        Field_UserApi.getuserprofile(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),userprofileHandler);//获取用户信息完整
    }
    public void checkUpdate_new() {
        Field_FieldApi.version(MyAsyncHttpClient.MyAsyncHttpClient(), mVersionHandler_new);
    }
    private LinhuiAsyncHttpResponseHandler mVersionHandler_new = new LinhuiAsyncHttpResponseHandler(VersionModel.class) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null && data instanceof VersionModel) {
                final VersionModel version = (VersionModel) data;
                if (version != null && version.getVid() > BuildConfig.VERSION_CODE){
                    if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                        View.OnClickListener uploadListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int i = view.getId();
                                if (i == R.id.upgrade_version_btn) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri content_url = Uri.parse(version.getUrl());
                                    intent.setData(content_url);
                                    getContext().startActivity(intent);
                                    //2017/12/11 强制更新不关闭
                                    if (version.getNum() == null) {

                                    } else {
                                        mCustomDialog.dismiss();
                                    }

                                } else if (i == R.id.upgrade_version_close_imgv) {
                                    mCustomDialog.dismiss();
                                }
                            }
                        };
                        CustomDialog.Builder builder = new CustomDialog.Builder(Field_PropertyMyselfFragment.this.getContext());
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
                        com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_PropertyMyselfFragment.this.getContext(),mCustomDialog);
                        mCustomDialog.show();
                    }
                } else if (version != null && version.getVid() == BuildConfig.VERSION_CODE) {
                    BaseMessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
                } else if (version.getVid() == 0) {
                    BaseMessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
                } else {
                    BaseMessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
                }

            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.toString());
        }
    };
    private LinhuiAsyncHttpResponseHandler badge_infoHandler = new LinhuiAsyncHttpResponseHandler(FieldMyselfBadeInfoModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mBadeInfoModel = (FieldMyselfBadeInfoModel)data;
            if (mBadeInfoModel != null) {
                if (mBadeInfoModel.getUnread_count() > 0) {
                    mfragment_myself_message_prompt.setVisibility(View.VISIBLE);
                } else {
                    mfragment_myself_message_prompt.setVisibility(View.GONE);
                }
                //保存钱包是否开通状态
                LoginManager.getInstance().setWallet_stauts(mBadeInfoModel.getWallet_stauts());
                LoginManager.setEnterprise_role(mBadeInfoModel.getEnterprise_role());
                LoginManager.setEnterprise_authorize_status(mBadeInfoModel.getEnterprise_authorize_status());
                refreshView();
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler userprofileHandler = new LinhuiAsyncHttpResponseHandler(LoginInfo.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                LoginInfo loginInfo = (LoginInfo) data;
                if (loginInfo != null) {
                    LoginManager.getInstance().updateUserProfile(loginInfo);
                    if (loginInfo.getAvatar() != null && loginInfo.getAvatar().length() > 0) {
                        Picasso.with(Field_PropertyMyselfFragment.this.getActivity()).load(loginInfo.getAvatar()+ "?imageView2/0/w/180/h/180").resize(180,180).into(mproperty_myphoto);
                    } else {
                        Picasso.with(Field_PropertyMyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mproperty_myphoto);
                    }
                    if (loginInfo.getMembership_level() != null && loginInfo.getMembership_level().length() > 0) {
                        mPropertyGradeImaV.setImageResource(com.linhuiba.linhuifield.connector.Constants.getMySelfGradeDrawable(loginInfo.getMembership_level()));
                        mPropertyGradeImaV.setVisibility(View.VISIBLE);
                    } else {
                        mPropertyGradeImaV.setVisibility(View.GONE);
                    }
                    LoginManager.getInstance().setIndustry_name(loginInfo.getIndustry_name());
                    if (loginInfo.getName() != null && loginInfo.getEmail() != null  && loginInfo.getIndustry_name() != null &&
                            loginInfo.isHas_contracts() && loginInfo.getName().length() > 0 &&
                            loginInfo.getEmail().length() > 0 &&
                            loginInfo.getIndustry_name().length() > 0) {
                        mproperty_prompt_myselfinfo.setVisibility(View.GONE);
                    } else {
                        mproperty_prompt_myselfinfo.setVisibility(View.VISIBLE);
                    }
                    refreshView();
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(Field_PropertyMyselfFragment.this.getActivity(), com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(Field_PropertyMyselfFragment.this.getActivity())
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(Field_PropertyMyselfFragment.this.getActivity())
                                    .build();
                            startActivityForResult(intent,10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                BaseMessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mMyselfTitleLLTmp.getTop());
        mMyselfTitleLL.layout(0, mBuyLayout2ParentTop, mMyselfTitleLL.getWidth(), mBuyLayout2ParentTop + mMyselfTitleLL.getHeight());
//        if (scrollY < com.linhuiba.linhuifield.connector.Constants.Dp2Px(Field_PropertyMyselfFragment.this.getContext(),167-44)) {
//            int progress = (int) (new Float(scrollY) / new Float(com.linhuiba.linhuifield.connector.Constants.Dp2Px(Field_PropertyMyselfFragment.this.getContext(),167-44)) * 200);
//            mMyselfTitleLL.getBackground().mutate().setAlpha(progress);
//        } else {
//            mMyselfTitleLL.getBackground().mutate().setAlpha(255);
//        }
    }
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return true;
            } else {
                return true;
            }
        }
    };
    private void refreshView() {
        if (Field_PropertyMyselfFragment.this.getActivity() != null &&
                isAdded()) {
            mfragment_myself_message_prompt.setVisibility(View.GONE);
            mmyself_property_integral_layout.setVisibility(View.VISIBLE);
            if (LoginManager.getAvatar() != null && LoginManager.getAvatar().length() > 0) {
                Picasso.with(Field_PropertyMyselfFragment.this.getActivity()).load(LoginManager.getAvatar()+ "?imageView2/0/w/180/h/180").resize(180,180).into(mproperty_myphoto);
            } else {
                Picasso.with(Field_PropertyMyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mproperty_myphoto);
            }
            if (LoginManager.getMembership_level() != null && LoginManager.getMembership_level().length() > 0) {
                mPropertyGradeImaV.setImageResource(com.linhuiba.linhuifield.connector.Constants.getMySelfGradeDrawable(LoginManager.getMembership_level()));
                mPropertyGradeImaV.setVisibility(View.VISIBLE);
            } else {
                mPropertyGradeImaV.setVisibility(View.GONE);
            }
            if (LoginManager.getEnterprise_authorize_status() != 1) {
                mmyselfinfo_property_account_certification_layout.setVisibility(View.VISIBLE);
                mmyselfinfo_property_account_certification_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent enterpriseCertification = new Intent("com.business.aboutActivity");
                        enterpriseCertification.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                        startActivity(enterpriseCertification);
                    }
                });
                mmyselfinfo_property_account_certification_text.setText(getResources().getString(R.string.myselfinfo_certification_tv_str));
                mmyselfinfo_property_account_certification_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mySelfPersonalCenterTV.setText(getResources().getString(R.string.myselfinfo_personal_center));
                if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                    mPropertyName.setText(LoginManager.getCompany_name());
                } else {
                    mPropertyName.setText(LoginManager.getMobile());
                }
            } else {
                if (LoginManager.getInstance().getEnterprise_name() != null && LoginManager.getInstance().getEnterprise_name().length() > 0) {
                    mPropertyName.setText(LoginManager.getInstance().getEnterprise_name());
                } else {
                    if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                        mPropertyName.setText(LoginManager.getCompany_name());
                    } else {
                        mPropertyName.setText(LoginManager.getMobile());
                    }
                }
                mmyselfinfo_property_account_certification_layout.setVisibility(View.VISIBLE);
                if (LoginManager.getEnterprise_authorize_status() == 1) {
                    mmyselfinfo_property_account_certification_text.setText(getResources().getString(R.string.myselfinfo_account_certification_text));
                    mmyselfinfo_property_account_certification_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                    mySelfPersonalCenterTV.setText(getResources().getString(R.string.myselfinfo_personal_enterprise_center));
                }
            }
            if (LoginManager.getName() != null && LoginManager.geteEmail() != null   &&
                    LoginManager.getInstance().getIndustry_name() != null
                    && LoginManager.isHas_contacts() && LoginManager.getName().length() > 0 &&
                    LoginManager.geteEmail().length() > 0 &&
                    LoginManager.getInstance().getIndustry_name().length() > 0) {
                mproperty_prompt_myselfinfo.setVisibility(View.GONE);
            } else {
                mproperty_prompt_myselfinfo.setVisibility(View.VISIBLE);
            }
        }
    }
    private void show_wallet_PopupWindow(int type) {
        LayoutInflater factory = LayoutInflater.from(Field_PropertyMyselfFragment.this.getContext());
        final View textEntryView = factory.inflate(R.layout.module_field_wallet_pw, null);
        TextView mwallet_open_cancel = (TextView) textEntryView.findViewById(R.id.wallet_open_cancel);
        LinearLayout mwallet_remind_alllayout = (LinearLayout) textEntryView.findViewById(R.id.wallet_remind_alllayout);
        TextView mwallet_open_determine = (TextView) textEntryView.findViewById(R.id.wallet_open_determine);
        if (type == 0) {
            mwallet_remind_alllayout.setVisibility(View.VISIBLE);
        }
        walletdialog = new AlertDialog.Builder(Field_PropertyMyselfFragment.this.getContext()).create();
        Constants.show_dialog(textEntryView,walletdialog);
        mwallet_open_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletdialog.isShowing()) {
                    walletdialog.dismiss();
                }
            }
        });
        mwallet_open_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Intent walletapply_intent = new Intent("com.business.WalletApplyActivity");
                walletapply_intent.putExtra("operation_type",1);
                startActivity(walletapply_intent);
                walletdialog.dismiss();
            }
        });
    }

}
