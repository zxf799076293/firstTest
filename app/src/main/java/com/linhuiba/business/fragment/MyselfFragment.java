package com.linhuiba.business.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.activity.BaseFragment;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linhuiba.business.BuildConfig;
import com.linhuiba.business.CalendarClass.ScheduleCalendarActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.ApplyForInvoiceActivity;
import com.linhuiba.business.activity.CommentCentreActivity;
import com.linhuiba.business.activity.CouponReceiveCentreActivity;
import com.linhuiba.business.activity.EnterpriseCertificationActivity;
import com.linhuiba.business.activity.EnterpriseManagementActivity;
import com.linhuiba.business.activity.FavoritesActivity;
import com.linhuiba.business.activity.GroupBookingMainActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.MyCouponsActivity;
import com.linhuiba.business.activity.MySelfPushMessageActivity;
import com.linhuiba.business.activity.MyWalletActivity;
import com.linhuiba.business.activity.SearchAdvListActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.activity.SettingActivity;
import com.linhuiba.business.activity.InviteActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MyselfInfoActivity;
import com.linhuiba.business.activity.OrderListNewActivity;
import com.linhuiba.business.activity.TheFirstRegisterCouponsActivity;
import com.linhuiba.business.activity.WalletApplyActivity;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;

import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.Badge_infoModel;
import com.linhuiba.business.mvppresenter.MyselfMvpPresenter;
import com.linhuiba.business.mvppresenter.VersionMvpPresenter;
import com.linhuiba.business.mvpview.MySelfMvpView;
import com.linhuiba.business.mvpview.VersionMvpView;
import com.linhuiba.business.view.XCRoundImageView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.MyDampViewScrollview;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.business.model.Version;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;

import javax.net.ssl.ManagerFactoryParameters;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MyselfFragment extends BaseMvpFragment implements MyDampViewScrollview.OnScrollListener,
        VersionMvpView, MySelfMvpView {
    @InjectView(R.id.myname)
    TextView mmyname;
    @InjectView(R.id.myphoto)
    XCRoundImageView mmyphoto;
    @InjectView(R.id.fragment_myself_membership_level_img)
    ImageView mFragmentMyselfGradeImgV;//等级imgv
    @InjectView(R.id.prompt_myselfinfo)
    TextView mprompt_myselfinfo;
    @InjectView(R.id.myselfinfo_account_certification_text)
    TextView mmyselfinfo_account_certification_text;
    @InjectView(R.id.activity_management_layout)
    RelativeLayout mactivity_management_layout;
    @InjectView(R.id.myselfinfo_notices_layout)
    RelativeLayout mmyselfinfo_notices_layout;
    @InjectView(R.id.myselfinfo_notices_textview)
    TextView mmyselfinfo_notices_textview;
    @InjectView(R.id.fragment_myself_message_prompt)
    TextView mfragment_myself_message_prompt;
    @InjectView(R.id.myself_order)
    RelativeLayout mmyself_order;
    @InjectView(R.id.fragment_myself_title_layout)
    LinearLayout mMyselfTitleLL;
    @InjectView(R.id.fragment_myself_title_layout_tmp)
    LinearLayout mMyselfTitleLLTmp;

    @InjectView(R.id.myself_info_top_layout)
    RelativeLayout mSelfInfoTopLL;
    @InjectView(R.id.fragment_myself_dampview)
    MyDampViewScrollview mFragmentMyselfDampView;
    @InjectView(R.id.fragment_myself_damp_img)
    ImageView mFragmentMyselfDampImgV;
    @InjectView(R.id.myself_info_perfect_ll)
    LinearLayout mInfoPerfectLL;
    @InjectView(R.id.myself_personal_center_text)
    TextView myLelfPerfectTV;

    @InjectView(R.id.order_pay_relativelayout)
    RelativeLayout mPayRL;
    @InjectView(R.id.order_check_relativelayout)
    RelativeLayout mOrderCheckRL;
    @InjectView(R.id.order_waiting_relativelayout)
    RelativeLayout mWaittingRL;
    @InjectView(R.id.order_review_relativelayout)
    RelativeLayout mReviewRL;
    @InjectView(R.id.myself_fragment_navbar_ll)
    LinearLayout mNavbarLL;
    @InjectView(R.id.myself_my_coupons_new_imgv)
    ImageView mCouponsNewImgv;
    @InjectView(R.id.myself_coupon_centre_new_imgv)
    ImageView mCouponCentreNewImgv;
    private View mMainContent;
    private int myselfdrestart = -1;
    private Badge_infoModel badge_infoModel;
    private Dialog walletdialog;
    private int LoginIntent = 6;
    private CustomDialog mCustomDialog;
    private QBadgeView mPayBV;
    private QBadgeView mOrderCheckBv;
    private QBadgeView mWaitingBV;
    private QBadgeView mReviewBV;
    private CustomDialog mCertificateDialog;
    private CustomDialog mApplyForReleaseDialog;
    private static final int CERTIFICATE_DIALOG_TYPE = 1;
    private static final int APPLy_FOR_RELEASE_DIALOG_TYPE = 2;
    private VersionMvpPresenter mVersionPresenter;
    private MyselfMvpPresenter mMySelfPresenter;
    private static final int LOGIN_REQUEST_CODE = 7;
    private boolean isRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.fragment_myself, container , false);
            ButterKnife.inject(this, mMainContent);
            mPayBV = new QBadgeView(MyselfFragment.this.getContext());
            mPayBV.bindTarget(mPayRL).setGravityOffset(24,12,true).setShowShadow(false).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color));
            mOrderCheckBv = new QBadgeView(MyselfFragment.this.getContext());
            mOrderCheckBv.bindTarget(mOrderCheckRL).setGravityOffset(24,12,true).setShowShadow(false).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color));
            mWaitingBV = new QBadgeView(MyselfFragment.this.getContext());
            mWaitingBV.bindTarget(mWaittingRL).setGravityOffset(24,12,true).setShowShadow(false).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color));
            mReviewBV = new QBadgeView(MyselfFragment.this.getContext());
            mReviewBV.bindTarget(mReviewRL).setGravityOffset(24,12,true).setShowShadow(false).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color));
            initview();
        }
        return mMainContent;
    }
    @Override
    public void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) MyselfFragment.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(MyselfFragment.this.getContext())) {
                myselfdrestart = 0;
            } else {
                myselfdrestart = 1;
            }
        } else if (screen == false) {
            myselfdrestart = 0;
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        PowerManager pm = (PowerManager) MyselfFragment.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(MyselfFragment.this.getContext())) {
                myselfdrestart = 0;
            } else {
                myselfdrestart = 1;
            }

        } else if (screen == false) {
            myselfdrestart = 0;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (MyselfFragment.this.getActivity() instanceof MainTabActivity) {
            MainTabActivity mainTabActivity = (MainTabActivity) MyselfFragment.this.getActivity();
            mainTabActivity.mSearchStatusBarLL.setVisibility(View.GONE);
        }
        mMyselfTitleLL.setBackgroundColor(getResources().getColor(R.color.white));
        hideSwitchDialog();
        if (LoginManager.isLogin()) {
            if (myselfdrestart == 1) {
                initview();
            }
        } else {
            mmyname.setText(getResources().getString(R.string.myselfinfo_uesrname_text));
            mprompt_myselfinfo.setVisibility(View.GONE);
            //未登录显示的图标
            mactivity_management_layout.setVisibility(View.GONE);//活动管理功能隐藏砍掉了
            Picasso.with(MyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mmyphoto);
            mFragmentMyselfGradeImgV.setVisibility(View.GONE);
            if (myselfdrestart != -1) {
                mPayBV.hide(false);
                mOrderCheckBv.hide(false);
                mWaitingBV.hide(false);
                mReviewBV.hide(false);
            }
            mfragment_myself_message_prompt.setVisibility(View.GONE);
            mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myself_enterprise_certification_tv_log_out_str));
            mInfoPerfectLL.setVisibility(View.GONE);
        }
        if (LoginManager.getInstance().getNoticesUrl() != null && LoginManager.getInstance().getNoticesUrl().length() > 0) {
            mmyselfinfo_notices_layout.setVisibility(View.VISIBLE);
            if (LoginManager.getInstance().getNoticesTitle() != null &&
                    LoginManager.getNoticesTitle().length() > 0) {
                mmyselfinfo_notices_textview.setText(LoginManager.getNoticesTitle());
            }
        } else {
            mmyselfinfo_notices_layout.setVisibility(View.GONE);
        }
        // FIXME: 2019/1/16 上新显示
        Constants.showCouponNew(1,mCouponsNewImgv);
        Constants.showCouponNew(2,mCouponCentreNewImgv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVersionPresenter != null) {
            mVersionPresenter.detachView();
        }
        if (mMySelfPresenter != null) {
            mMySelfPresenter.detachView();
        }
    }

    private void initview() {
        mVersionPresenter = new VersionMvpPresenter();
        mVersionPresenter.attachView(this);
        mMySelfPresenter = new MyselfMvpPresenter();
        mMySelfPresenter.attachView(this);
        MainTabActivity mMainTabActivity = (MainTabActivity) MyselfFragment.this.getActivity();
        if (mMainTabActivity.mNotchHeight > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mMainTabActivity.mNotchHeight);
            mNavbarLL.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(
                            MyselfFragment.this.getContext(),140 + (mMainTabActivity.mNotchHeight / 2 - 25)));
            mFragmentMyselfDampImgV.setLayoutParams(layoutParams1);
        }
        if (LoginManager.isLogin()) {
            if (LoginManager.getAvatar() != null && LoginManager.getAvatar().length() > 0) {
                Picasso.with(MyselfFragment.this.getActivity()).load(LoginManager.getAvatar()+ "?imageView2/0/w/180/h/180").resize(180,180).into(mmyphoto);
            } else {
                Picasso.with(MyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mmyphoto);
            }
            if (LoginManager.getMembership_level() != null && LoginManager.getMembership_level().length() > 0) {
                mFragmentMyselfGradeImgV.setImageResource(com.linhuiba.linhuifield.connector.Constants.getMySelfGradeDrawable(LoginManager.getMembership_level()));
                mFragmentMyselfGradeImgV.setVisibility(View.VISIBLE);
            } else {
                mFragmentMyselfGradeImgV.setVisibility(View.GONE);
            }
            if (LoginManager.getEnterprise_authorize_status() == 1) {
                if (LoginManager.getEnterprise_name() != null && LoginManager.getEnterprise_name().length() > 0) {
                    mmyname.setText(LoginManager.getEnterprise_name());
                } else {
                    if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                        mmyname.setText(LoginManager.getCompany_name());
                    } else {
                        mmyname.setText(LoginManager.getMobile());
                    }
                }
            } else {
                if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                    mmyname.setText(LoginManager.getCompany_name());
                } else {
                    mmyname.setText(LoginManager.getMobile());
                }
            }
            if (LoginManager.getEnterprise_authorize_status() == 1) {
                mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myselfinfo_account_certification_text));
                myLelfPerfectTV.setText(getResources().getString(R.string.myselfinfo_personal_enterprise_center));
            } else {
                mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myself_enterprise_certification_tv_str));
                myLelfPerfectTV.setText(getResources().getString(R.string.myselfinfo_personal_center));
            }
            if (LoginManager.getName() != null && LoginManager.geteEmail() != null  &&
                    LoginManager.getInstance().getIndustry_name() != null &&
                    LoginManager.isHas_contacts() && LoginManager.getName().length() > 0 &&
                    LoginManager.geteEmail().length() > 0
                    && LoginManager.getInstance().getIndustry_name().length() > 0) {
                mprompt_myselfinfo.setVisibility(View.GONE);
            } else {
                mprompt_myselfinfo.setVisibility(View.VISIBLE);
            }
            //获取用户的信息 头像公司等
            mMySelfPresenter.getUserProfile();//获取积分
            //获取登录后的用户状态信息 ：各种订单数量 钱包状态 账号角色
            mMySelfPresenter.getBadgeInfo();
            mInfoPerfectLL.setVisibility(View.VISIBLE);
        } else {
            mmyname.setText(getResources().getString(R.string.myselfinfo_uesrname_text));
            mprompt_myselfinfo.setVisibility(View.GONE);
            mactivity_management_layout.setVisibility(View.GONE);//活动管理功能隐藏砍掉了
            Picasso.with(MyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mmyphoto);
            mFragmentMyselfGradeImgV.setVisibility(View.GONE);
            mPayBV.hide(false);
            mOrderCheckBv.hide(false);
            mWaitingBV.hide(false);
            mReviewBV.hide(false);
            mfragment_myself_message_prompt.setVisibility(View.GONE);
            mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myself_enterprise_certification_tv_log_out_str));
            mInfoPerfectLL.setVisibility(View.GONE);
        }
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
    @OnClick({
            R.id.myself_info_top_layout,
            R.id.service_layout,
            R.id.myself_advs_reserve_rl,
            R.id.myself_my_group_rl,
            R.id.getappversion_layout,
            R.id.myself_order,
            R.id.order_pay_relativelayout,
            R.id.order_check_relativelayout,
            R.id.order_waiting_relativelayout,
            R.id.order_review_relativelayout,
            R.id.invite_relativelayout,
            R.id.focuson_relativelayout,
            R.id.schedule_relativelayout,
            R.id.paper_relativelayout,
            R.id.account_verify_layout,
            R.id.field_fieldorderlist_layout,
            R.id.activity_management_layout,
            R.id.myselfinfo_notices_layout,
            R.id.fragment_myself_setting_text,
            R.id.fragment_mysef_message_layout,
            R.id.fragment_myself_wallet_alllayout,
            R.id.muself_enquiry_ll,
            R.id.myselfinfo_account_certification_layout,
            R.id.myself_coupon_centre_ll,
            R.id.myself_customization_ll,
            R.id.myself_my_coupons,
    })
    public void myselfinfo_click(View view) {
        switch (view.getId()) {
            case R.id.myself_info_top_layout:
                if (LoginManager.isLogin()) {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), MyselfInfoActivity.class);
                    startActivityForResult(myselfinfo_intent, 4);
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LOGIN_REQUEST_CODE);
                }
                break;
            case R.id.service_layout:
                AndPermission.with(MyselfFragment.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
                break;
            case R.id.myself_advs_reserve_rl:
                Intent AdvertisinglistIntent = new Intent(getActivity(), SearchAdvListActivity.class);
                AdvertisinglistIntent.putExtra("cityname",LoginManager.getInstance().getTrackCityName());
                AdvertisinglistIntent.putExtra("cityname_code",LoginManager.getInstance().getTrackcityid());
                AdvertisinglistIntent.putExtra("good_type",2);
                startActivity(AdvertisinglistIntent);
                MobclickAgent.onEvent(MyselfFragment.this.getContext(), com.linhuiba.linhuifield.config.Config.UM_ADV_CLICK);
                break;
            case R.id.field_fieldorderlist_layout:
                if (LoginManager.isLogin()) {
                    //2018/4/12 发布确认isRight_to_publish字段的使用
                    if (!LoginManager.isRight_to_publish() &&
                            !LoginManager.isIs_supplier()) {
                        if (LoginManager.getEnterprise_authorize_status() != 1) {
                            if (mCertificateDialog != null) {
                                if (!mCertificateDialog.isShowing()) {
                                    mCertificateDialog.show();
                                }
                            } else {
                                showMsgDialog(CERTIFICATE_DIALOG_TYPE);
                            }
                            return;
                        }
                        if (!LoginManager.isRight_to_publish()) {
                            if (mApplyForReleaseDialog != null) {
                                if (!mApplyForReleaseDialog.isShowing()) {
                                    mApplyForReleaseDialog.show();
                                }
                            } else {
                                showMsgDialog(APPLy_FOR_RELEASE_DIALOG_TYPE);
                            }
                            return;
                        }
                    }
                    showSwitchDialog(2,getResources().getString(R.string.myself_switch_business_str),true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent fieldorderlist = new Intent(MyselfFragment.this.getActivity(), com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity.class);
                            fieldorderlist.putExtra("new_tmpintent", "fieldlist");
                            if (badge_infoModel != null) {
                                fieldorderlist.putExtra("all_orders",badge_infoModel.getAll_orders());
                            }
                            startActivity(fieldorderlist);
                            LoginManager.getInstance().setFieldexit(1);
                        }
                    },2000);
                } else {
                    Intent loginIntent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
                }
                break;
            case R.id.myself_my_group_rl:
                Intent groupIntent = new Intent(MyselfFragment.this.getActivity(), GroupBookingMainActivity.class);
                groupIntent.putExtra("order_type", 0);
                startActivity(groupIntent);
                break;
            case R.id.getappversion_layout:
                //检查版本更新
                checkUpdate_new();
                break;
            case R.id.invite_relativelayout:
                //邀请有奖
                if (LoginManager.isLogin()) {
                    Intent inviteintent = new Intent(MyselfFragment.this.getActivity(),InviteActivity.class);
                    startActivity(inviteintent);
                } else {
                    MessageUtils.showToast(MyselfFragment.this.getActivity(),getResources().getString(R.string.commoditypay_jumplogintext));
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.myself_order:
                if (LoginManager.isLogin()) {
                    Intent orderintent = new Intent(MyselfFragment.this.getActivity(), OrderListNewActivity.class);
                    Bundle bundle_ordertype0 = new Bundle();
                    bundle_ordertype0.putInt("order_type", 0);
                    orderintent.putExtras(bundle_ordertype0);
                    orderintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(orderintent, 5);

                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }

                break;
            case R.id.order_pay_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent orderpayintent = new Intent(MyselfFragment.this.getActivity(), OrderListNewActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);
                    orderpayintent.putExtras(bundle_ordertype1);
                    orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(orderpayintent, 5);

                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.order_check_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent ordercheckintent = new Intent(MyselfFragment.this.getActivity(), OrderListNewActivity.class);
                    Bundle bundle_ordertype2 = new Bundle();
                    bundle_ordertype2.putInt("order_type", 2);
                    ordercheckintent.putExtras(bundle_ordertype2);
                    ordercheckintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(ordercheckintent, 5);

                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.order_waiting_relativelayout:

                if (LoginManager.isLogin()) {
                    Intent orderwaitintent = new Intent(MyselfFragment.this.getActivity(), OrderListNewActivity.class);
                    Bundle bundle_ordertype3 = new Bundle();
                    bundle_ordertype3.putInt("order_type", 3);
                    orderwaitintent.putExtras(bundle_ordertype3);
                    orderwaitintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(orderwaitintent, 5);

                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.order_review_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent comtentCentreIntent = new Intent(MyselfFragment.this.getActivity(),CommentCentreActivity.class);
                    startActivity(comtentCentreIntent);
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;

            case R.id.focuson_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent favoriteintent = new Intent(MyselfFragment.this.getActivity(), FavoritesActivity.class);
                    startActivity(favoriteintent);
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.schedule_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent schedule = new Intent(MyselfFragment.this.getActivity(), ScheduleCalendarActivity.class);
                    startActivity(schedule);
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.paper_relativelayout:
                if (LoginManager.isLogin()) {
                    Intent InvoiceInfoEntry = new Intent(MyselfFragment.this.getActivity(), ApplyForInvoiceActivity.class);
                    startActivity(InvoiceInfoEntry);
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.activity_management_layout:
                if (LoginManager.isLogin()) {
                    Intent Field_activitylist = new Intent(MyselfFragment.this.getActivity(),com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity.class);
                    Field_activitylist.putExtra("new_tmpintent","fieldorder");
                    if (badge_infoModel != null) {
                        Field_activitylist.putExtra("all_orders",badge_infoModel.getActivity_orders());
                    }
                    startActivity(Field_activitylist);
                } else {
                    Intent intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(intent, LoginIntent);
                }
                break;
            case R.id.myselfinfo_notices_layout:
                if (LoginManager.getNoticesUrl() != null && LoginManager.getNoticesUrl().length() > 0) {
                    Intent about_notices = new Intent(MyselfFragment.this.getActivity(),AboutUsActivity.class);
                    about_notices.putExtra("type", Config.REPORT_WEB_INT);
                    about_notices.putExtra("url",LoginManager.getNoticesUrl());
                    startActivity(about_notices);
                }
                break;
            case R.id.fragment_myself_setting_text:
                Intent set_intent = new Intent(MyselfFragment.this.getActivity(), SettingActivity.class);
                startActivity(set_intent);
                break;
            case R.id.fragment_mysef_message_layout:
                Intent mymessage = new Intent(MyselfFragment.this.getActivity(), MySelfPushMessageActivity.class);
                mymessage.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mymessage);
                break;
            case R.id.fragment_myself_wallet_alllayout:
                if (LoginManager.isLogin()) {
                    if (LoginManager.getInstance().getWallet_stauts() == 1) {
                        Intent walletapply_intent = new Intent(MyselfFragment.this.getActivity(), MyWalletActivity.class);
                        startActivity(walletapply_intent);
                    } else {
                        show_wallet_PopupWindow(0);
                    }
                } else {
                    Intent myselfinfo_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(myselfinfo_intent, LoginIntent);
                }
                break;
            case R.id.muself_enquiry_ll:
                if (LoginManager.isLogin()) {
                    Intent enquiryIntent = new Intent(MyselfFragment.this.getActivity(), AboutUsActivity.class);
                    enquiryIntent.putExtra("type",Config.ENQUIRY_LIST_WEB_INT);
                    startActivity(enquiryIntent);
                } else {
                    Intent enquiryIntent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(enquiryIntent, LoginIntent);
                }
                break;
            case R.id.myselfinfo_account_certification_layout:
                if (LoginManager.getEnterprise_authorize_status() != 1) {
                    if (LoginManager.isLogin()) {
                        Intent intent = new Intent(MyselfFragment.this.getActivity(),AboutUsActivity.class);
                        intent.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                        startActivityForResult(intent, LOGIN_REQUEST_CODE);
                    }
                } else if (LoginManager.getEnterprise_authorize_status() == 1) {
                    if (LoginManager.isLogin()) {
                        Intent EnterpriseManagement = new Intent(MyselfFragment.this.getActivity(),EnterpriseManagementActivity.class);
                        startActivity(EnterpriseManagement);
                    } else {
                        Intent intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                        startActivityForResult(intent, LOGIN_REQUEST_CODE);
                    }
                }
                break;
            case R.id.myself_coupon_centre_ll:
                //2018/12/12 test coupon centre
                Intent couponsCentreIntent = new Intent(MyselfFragment.this.getActivity(), CouponReceiveCentreActivity.class);
                startActivity(couponsCentreIntent);
                // FIXME: 2019/1/16 设置上新
                Constants.setCouponNewShow(2,mCouponCentreNewImgv);
                break;
            case R.id.myself_customization_ll:
                if (LoginManager.isLogin()) {
                    Intent DemandList = new Intent(MyselfFragment.this.getActivity(),AboutUsActivity.class);
                    DemandList.putExtra("type", Config.DEMAND_LIST_WEB_INT);
                    startActivity(DemandList);
                } else {
                    Intent demand_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(demand_intent, LoginIntent);
                }
                break;
            case R.id.myself_my_coupons:
                if (LoginManager.isLogin()) {
                    if (Constants.isFastClick()) {
                        Intent couponsIntent = new Intent(MyselfFragment.this.getActivity(), MyCouponsActivity.class);
                        startActivity(couponsIntent);
                        // FIXME: 2019/1/16 设置上新
                        Constants.setCouponNewShow(1,mCouponsNewImgv);
                    }
                } else {
                    Intent demand_intent = new Intent(MyselfFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(demand_intent, LoginIntent);
                }
                break;
            default:
                break;
        }
    }

    public void checkUpdate_new() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
        mVersionPresenter.getVersion();
    }
    private void show_wallet_PopupWindow(int type) {
        LayoutInflater factory = LayoutInflater.from(MyselfFragment.this.getContext());
        final View textEntryView = factory.inflate(R.layout.activity_fieldinfo_refund_price_popuwindow, null);
        TextView mwallet_open_cancel = (TextView) textEntryView.findViewById(R.id.wallet_open_cancel);
        LinearLayout mwallet_remind_alllayout = (LinearLayout) textEntryView.findViewById(R.id.wallet_remind_alllayout);
        TextView mwallet_open_determine = (TextView) textEntryView.findViewById(R.id.wallet_open_determine);
        if (type == 0) {
            mwallet_remind_alllayout.setVisibility(View.VISIBLE);
        }
        walletdialog = new AlertDialog.Builder(MyselfFragment.this.getContext()).create();
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
                Intent walletapply_intent = new Intent(MyselfFragment.this.getActivity(), WalletApplyActivity.class);
                walletapply_intent.putExtra("operation_type",1);
                startActivity(walletapply_intent);
                walletdialog.dismiss();
            }
        });
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(MyselfFragment.this.getActivity(), com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(MyselfFragment.this.getActivity())
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(MyselfFragment.this.getActivity())
                                    .build();
                            startActivityForResult(intent,10);
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (LoginManager.isLogin()) {
                isRequest = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mMyselfTitleLLTmp.getTop());
        mMyselfTitleLL.layout(0, mBuyLayout2ParentTop, mMyselfTitleLL.getWidth(), mBuyLayout2ParentTop + mMyselfTitleLL.getHeight());

        if (scrollY < com.linhuiba.linhuifield.connector.Constants.Dp2Px(MyselfFragment.this.getContext(),167-69)) {
            int progress = (int) (new Float(scrollY) / new Float(com.linhuiba.linhuifield.connector.Constants.Dp2Px(MyselfFragment.this.getContext(),167-69)) * 200);
            mMyselfTitleLL.getBackground().mutate().setAlpha(progress);
        } else {
            mMyselfTitleLL.getBackground().mutate().setAlpha(255);
        }
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
    private void showMsgDialog(final int type) {
        View.OnClickListener uploadListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                if (i == R.id.btn_perfect) {
                    if (type == CERTIFICATE_DIALOG_TYPE) {
                        mCertificateDialog.dismiss();
                        Intent enterpriseCertification = new Intent(MyselfFragment.this.getActivity(),AboutUsActivity.class);
                        enterpriseCertification.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                        startActivity(enterpriseCertification);
                    } else if (type == APPLy_FOR_RELEASE_DIALOG_TYPE) {
                        Intent applyforreleasepermissions = new Intent(MyselfFragment.this.getActivity(),AboutUsActivity.class);
                        applyforreleasepermissions.putExtra("type", Config.APPLY_FOR_RELEASE_INT);
                        startActivityForResult(applyforreleasepermissions, Config.APPLY_FOR_RELEASE_INT);
                    }
                } else if (i == R.id.btn_cancel) {
                    if (type == CERTIFICATE_DIALOG_TYPE) {
                        mCertificateDialog.dismiss();
                    } else if (type == APPLy_FOR_RELEASE_DIALOG_TYPE) {
                        mApplyForReleaseDialog.dismiss();
                    }
                }
            }
        };
        CustomDialog.Builder builder = new CustomDialog.Builder(MyselfFragment.this.getActivity());
        if (type == CERTIFICATE_DIALOG_TYPE) {
            mCertificateDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel, uploadListener)
                    .addViewOnclick(R.id.btn_perfect, uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.myself_certificate_msg_tv_str))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.myself_apply_for_release_cancel_btn_str))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.myself_certificate_btn_str))
                    .showView(R.id.linhuiba_logo_imgv, View.GONE)
                    .showView(R.id.dialog_title_msg_tv, View.VISIBLE)
                    .setTextColor(R.id.btn_cancel,
                            getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor))
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MyselfFragment.this.getActivity(), mCertificateDialog);
            mCertificateDialog.show();
        } else if (type == APPLy_FOR_RELEASE_DIALOG_TYPE) {
            mApplyForReleaseDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel, uploadListener)
                    .addViewOnclick(R.id.btn_perfect, uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.myself_apply_for_release_msg_tv_str))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.myself_apply_for_release_cancel_btn_str))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.myself_apply_for_release_btn_str))
                    .showView(R.id.linhuiba_logo_imgv, View.GONE)
                    .showView(R.id.dialog_title_msg_tv, View.VISIBLE)
                    .setTextColor(R.id.btn_cancel,
                            getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor))
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MyselfFragment.this.getActivity(), mApplyForReleaseDialog);
            mApplyForReleaseDialog.show();
        }

    }

    @Override
    public void onVersionSuccess(Version versions) {
        final Version version = versions;
        if ((version != null && version.getVid() > BuildConfig.VERSION_CODE) ||
                (version != null && version.getForce_update() == 1)) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.upgrade_version_btn:
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri content_url = Uri.parse(version.getUrl());
                            intent.setData(content_url);
                            getContext().startActivity(intent);
                            //2017/12/11 强制更新不关闭
                            if (version.getForce_update() == 1) {

                            } else {
                                mCustomDialog.dismiss();
                            }
                            break;
                        case R.id.upgrade_version_close_imgv:
                            mCustomDialog.dismiss();
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(MyselfFragment.this.getContext());
            //2017/12/8 判断是否强制更新
            if (version.getForce_update() == 1) {
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
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MyselfFragment.this.getContext(),mCustomDialog);
            mCustomDialog.show();
        } else if ((version != null && version.getVid() == BuildConfig.VERSION_CODE) ||
                (version != null && version.getVid() < BuildConfig.VERSION_CODE)) {
            MessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
        } else if (version != null && version.getVid() == 0) {
            MessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
        } else {
            MessageUtils.showToast(getContext(), getContext().getResources().getString(R.string.txt_version));
        }
    }

    @Override
    public void onVersionFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.toString());
    }

    @Override
    public void onBadgeSuccess(Badge_infoModel badeInfoModel) {
        badge_infoModel = badeInfoModel;
        if (badge_infoModel != null) {
            if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
                mactivity_management_layout.setVisibility(View.GONE);//活动管理功能隐藏砍掉了
            } else {
                mactivity_management_layout.setVisibility(View.GONE);
            }
            if (badge_infoModel.getUnread_count() > 0) {
                mfragment_myself_message_prompt.setVisibility(View.VISIBLE);
            } else {
                mfragment_myself_message_prompt.setVisibility(View.GONE);
            }
            //保存钱包是否开通状态
            LoginManager.getInstance().setWallet_stauts(badge_infoModel.getWallet_stauts());
            if (badge_infoModel.getEnterprise_role() == 1 || badge_infoModel.getEnterprise_role() == 2) {
                if (badge_infoModel.getEnterprise_name() != null && badge_infoModel.getEnterprise_name().length() > 0) {
                    LoginManager.getInstance().setEnterprise_name(badge_infoModel.getEnterprise_name());
                    mmyname.setText(badge_infoModel.getEnterprise_name());
                } else {
                    LoginManager.getInstance().setEnterprise_name("");
                    if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                        mmyname.setText(LoginManager.getCompany_name());
                    } else {
                        mmyname.setText(LoginManager.getMobile());
                    }
                }
                if (badge_infoModel.getEnterprise_role() == 1) {
                    mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myselfinfo_account_certification_text));
                    myLelfPerfectTV.setText(getResources().getString(R.string.myselfinfo_personal_enterprise_center));
                } else if (badge_infoModel.getEnterprise_role() == 2) {
                    mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myselfinfo_account_child_certification_text));
                    myLelfPerfectTV.setText(getResources().getString(R.string.myselfinfo_personal_enterprise_center));
                } else {
                    mmyselfinfo_account_certification_text.setText(getResources().getString(R.string.myself_enterprise_certification_tv_str));
                    myLelfPerfectTV.setText(getResources().getString(R.string.myselfinfo_personal_center));
                }
            } else {
                if (LoginManager.getCompany_name() != null && LoginManager.getCompany_name().length() > 0) {
                    mmyname.setText(LoginManager.getCompany_name());
                } else {
                    mmyname.setText(LoginManager.getMobile());
                }
            }
            LoginManager.setEnterprise_role(badge_infoModel.getEnterprise_role());
            LoginManager.setEnterprise_authorize_status(badge_infoModel.getEnterprise_authorize_status());
            if (badge_infoModel.getSubmitted() > 0) {
                mPayBV.setBadgeNumber(badge_infoModel.getSubmitted());
            } else {
                mPayBV.hide(false);
            }
            if (badge_infoModel.getPaid() > 0) {
                mOrderCheckBv.setBadgeNumber(badge_infoModel.getPaid());
            } else {
                mOrderCheckBv.hide(false);
            }
            if (badge_infoModel.getApproved() > 0) {
                mWaitingBV.setBadgeNumber(badge_infoModel.getApproved());
            } else {
                mWaitingBV.hide(false);
            }
            if (badge_infoModel.getFinished() > 0) {
                mReviewBV.setBadgeNumber(badge_infoModel.getFinished());
            } else {
                mReviewBV.hide(false);
            }
        }
    }

    @Override
    public void onBadgeFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccesstoken(error);
    }

    @Override
    public void onProfileSuccess(LoginInfo loginInfos) {
        LoginInfo loginInfo = loginInfos;
        if (loginInfo != null) {
            LoginManager.getInstance().updateUserProfile(loginInfo);
            if (loginInfo.getAvatar() != null && loginInfo.getAvatar().length() > 0) {
                Picasso.with(MyselfFragment.this.getActivity()).load(loginInfo.getAvatar()+ "?imageView2/0/w/180/h/180").resize(180,180).into(mmyphoto);
            } else {
                Picasso.with(MyselfFragment.this.getActivity()).load(R.drawable.image_touxiang_three_two).into(mmyphoto);
            }
            if (loginInfo.getMembership_level() != null && loginInfo.getMembership_level().length() > 0) {
                mFragmentMyselfGradeImgV.setImageResource(com.linhuiba.linhuifield.connector.Constants.getMySelfGradeDrawable(loginInfo.getMembership_level()));
                mFragmentMyselfGradeImgV.setVisibility(View.VISIBLE);
            } else {
                mFragmentMyselfGradeImgV.setVisibility(View.GONE);
            }
            LoginManager.getInstance().setIndustry_name(loginInfo.getIndustry_name());
            if (loginInfo.getName() != null && loginInfo.getEmail() != null   &&
                    loginInfo.isHas_contracts() && loginInfo.getName().length() > 0 &&
                    loginInfo.getEmail().length() > 0  &&
                    LoginManager.getInstance().getIndustry_name() != null && LoginManager.getIndustry_name().length() > 0) {
                mprompt_myselfinfo.setVisibility(View.GONE);
            } else {
                mprompt_myselfinfo.setVisibility(View.VISIBLE);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    gotoFieldActivityHandler.sendEmptyMessage(1);
                }
            }).start();

        }
    }

    @Override
    public void onBindWXSuccess() {

    }

    @Override
    public void onUnBindWXSuccess() {

    }

    @Override
    public void onBindQQSuccess() {

    }

    @Override
    public void onUnBindQQSuccess() {

    }

    private Handler gotoFieldActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (isRequest &&
                        (LoginManager.isRight_to_publish() || LoginManager.isIs_supplier())) {
                    isRequest = false;
                    Intent fieldorderlist = new Intent(MyselfFragment.this.getActivity(), com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity.class);
                    fieldorderlist.putExtra("new_tmpintent", "fieldlist");
                    startActivity(fieldorderlist);
                    LoginManager.getInstance().setFieldexit(1);
                }
            }
        }
    };

}
