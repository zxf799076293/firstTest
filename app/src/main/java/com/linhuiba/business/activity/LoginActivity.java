package com.linhuiba.business.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.mvppresenter.LoginMvpPresenter;
import com.linhuiba.business.mvpview.LoginMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.connect.UnionInfo;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.linhuiba.linhuifield.config.Config.UM_ACCOUNT_LOGIN;

/**
 * Created by Administrator on 2016/3/2.
 */
public class LoginActivity extends BaseMvpActivity implements LoginMvpView {
    @InjectView(R.id.edit_login_pw)
    EditText mLoginPwET;
    @InjectView(R.id.edit_mobile)
    EditText mLoginAccountET;
    @InjectView(R.id.btn_login)
    TextView mLoginBtn;
    @InjectView(R.id.fast_btncode)
    Button mLoginCodeBtn;
    @InjectView(R.id.fast_edit_mobile)
    EditText mfast_edit_mobile;
    @InjectView(R.id.fast_edit_code)
    EditText mfast_edit_code;
    @InjectView(R.id.fastlogin)
    TextView mFastLoginBtn;
    @InjectView(R.id.login_register_ll)
    LinearLayout mLoginRegisterLL;
    @InjectView(R.id.login_register_agreement_checkbox)
    CheckBox mLoginRegisterCB;
    @InjectView(R.id.login_register_agreement_text)
    TextView mLoginRegisterAgreementLLTV;

    private boolean isPushMessageIntent = false;
    private IWXAPI WXapi;
    private String roadcast_action = "";//广播返回的 action
    private MyBroadcastReciver myBroadcastReciver;
    private LoginInfo loginInfo;
    private int mLinkInt;
    private Uri mLinkData;
    private LoginMvpPresenter mLoginMvpPresenter;
    private static final String RESOURCE_ACCOUNT = "account";
    private static final String BIND_WECHAT_SUCCESS_ACTION = "Binding_Weixin_Success";
    private static final String BIND_WECHAT_ERROR_ACTION = "Binding_Weixin_Error";
    private static final String PUSH_MSG_INTENT = "PushMessageIntent";
    private static final String WECHAT_LOGIN_SCOPE = "snsapi_userinfo";
    private static final String WECHAT_LOGIN_STATE = "linhuiba_business";
    private Tencent mTencent;
    private IUiListener mIUiListener;
    private UnionInfo mUserInfo;
    private IUiListener mUserInfoListener;
    private TimeCount mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.showRegisterText(this, getResources().getString(R.string.txt_register_btn_txt),
                16,
                getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,2);
            }
        });
        regist_receiver();
        Intent msgintent = getIntent();
        if (msgintent.getExtras() !=  null) {
            if (msgintent.getExtras().get(PUSH_MSG_INTENT) != null) {
                isPushMessageIntent = msgintent.getBooleanExtra(PUSH_MSG_INTENT,false);
            }
            if (msgintent.getExtras().get("mLink") != null) {
                mLinkInt = Integer.parseInt(msgintent.getExtras().get("mLink").toString());
                mLinkData = msgintent.getData();
            }
            if (msgintent.getExtras().get("is_modity_pw") != null &&
                    msgintent.getExtras().getInt("is_modity_pw") == 1) {
                Intent fastlogin_intent = new Intent(this,FastLoginActivity.class);
                fastlogin_intent.putExtra("is_modity_pw",1);//修改密码跳转
                startActivityForResult(fastlogin_intent, 1);
            }
        }
        mLoginMvpPresenter = new LoginMvpPresenter();
        mLoginMvpPresenter.attachView(this);
        addTextChangedListener(mLoginAccountET, mLoginPwET);
        addTextChangedListener(mLoginPwET, mLoginAccountET);
        WXapi = WXAPIFactory.createWXAPI(LoginActivity.this.getContext(), com.linhuiba.linhuifield.connector.Constants.APP_ID, true);
        mTencent = Tencent.createInstance(com.linhuiba.linhuipublic.config.Config.TENGXUN_QQ_APPID,LoginActivity.this.getContext());
        mIUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o != null) {
                    JSONObject jsonObject = JSONObject.parseObject(o.toString());
                    String openID = jsonObject.getString("openid");
                    String accessToken = jsonObject.getString("access_token");
                    String expires = jsonObject.getString("expires_in");
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);
                    mUserInfo = new UnionInfo(LoginActivity.this, mTencent.getQQToken());
                    mUserInfo.getUnionId(mUserInfoListener);
                }
            }

            @Override
            public void onError(UiError uiError) {
                MessageUtils.showToast(getResources().getString(R.string.login_qq_authorization_error));
            }

            @Override
            public void onCancel() {

            }
        };
        mUserInfoListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o != null) {
                    JSONObject jo = JSONObject.parseObject(o.toString());
                    if (jo != null && jo.get("unionid") != null &&
                            jo.get("unionid").toString().length() > 0) {
                        int city_id = 0;
                        int province_id = 0;
                        if (LoginManager.getInstance().getRegisterCurrentCityid() != null &&
                                LoginManager.getInstance().getRegisterCurrentCityid().length() > 0) {
                            city_id = Integer.parseInt(LoginManager.getInstance().getRegisterCurrentCityid());
                            province_id = Constants.getProvinceid(LoginActivity.this,city_id);
                        }
                        mLoginMvpPresenter.qqLogin(jo.get("unionid").toString(),province_id,city_id);
                    }
                }
            }

            @Override
            public void onError(UiError uiError) {
                MessageUtils.showToast(getResources().getString(R.string.login_qq_authorization_error));
            }

            @Override
            public void onCancel() {

            }
        };
        addFastTextChangedListener(mfast_edit_code,mfast_edit_mobile,2);
        addFastTextChangedListener(mfast_edit_mobile,mfast_edit_code,1);
        mTime = new TimeCount(60000, 1000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.fastlogin_activity_name_str));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.fastlogin_activity_name_str));
        MobclickAgent.onPause(this);
        hideProgressDialog();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReciver != null) {
            this.unregisterReceiver(myBroadcastReciver);
            myBroadcastReciver = null;
        }
        if (mLoginMvpPresenter != null) {
            mLoginMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.btn_login,
            R.id.text_fastlogin,
            R.id.login_weixin_login_img,
            R.id.login_qq_login_img,
            R.id.fast_btncode,
            R.id.fastlogin,
            R.id.login_register_agreement_text,
            R.id.login_register_agreement_checkbox
    })
    public void LoginOnclick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(mLoginAccountET.getText())) {
                    MessageUtils.showToast(getResources().getString(R.string.login_no_account_text));
                } else if (TextUtils.isEmpty(mLoginPwET.getText()) ) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_password_hint));
                }  else if (mLoginPwET.getText().toString().length() < 6 ||
                        mLoginPwET.getText().toString().trim().length() > 30) {
                    mLoginPwET.setText("");
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_password_lenthhint));
                } else {
                    mLoginMvpPresenter.loginApi(mLoginAccountET.getText().toString(), mLoginPwET.getText().toString());
                }
                MobclickAgent.onEvent(LoginActivity.this,UM_ACCOUNT_LOGIN);
                break;
            case R.id.text_fastlogin:
                //账号密码登录
                Intent fastlogin_intent = new Intent(this,FastLoginActivity.class);
                startActivityForResult(fastlogin_intent, 1);
                break;

            case R.id.login_weixin_login_img:
                //微信登录
                WXapi.registerApp(com.linhuiba.linhuifield.connector.Constants.APP_ID);
                if (!WXapi.isWXAppInstalled()) {
                    MessageUtils.showToast(getResources().getString(R.string.commoditypay_weixinapp_toast));
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = WECHAT_LOGIN_SCOPE;
                req.state = WECHAT_LOGIN_STATE;
                WXapi.sendReq(req);
                MobclickAgent.onEvent(LoginActivity.this.getContext(), Config.UM_WECHAT_LOGIN);
                break;
            case R.id.login_qq_login_img:
                mTencent.login(LoginActivity.this, "all", mIUiListener);
                break;
            case R.id.fast_btncode:
                if (TextUtils.getTrimmedLength(mfast_edit_mobile.getText().toString()) == 0) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mfast_edit_mobile.getText().toString())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog();
                mLoginMvpPresenter.getFastCaptcha(mfast_edit_mobile.getText().toString(),"3");
                break;
            case R.id.fastlogin:
                if (TextUtils.getTrimmedLength(mfast_edit_mobile.getText().toString()) == 0) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mfast_edit_mobile.getText().toString())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mfast_edit_code.getText().toString()) == 0 || mfast_edit_code.getText().length() < 4) {
                    MessageUtils.showToast(this,getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                showProgressDialog();
                mLoginMvpPresenter.fastLogin(mfast_edit_mobile.getText().toString(),
                        mfast_edit_code.getText().toString());
                MobclickAgent.onEvent(LoginActivity.this, Config.UM_WECHAT_FAST_LOGIN);
                break;
            case R.id.login_register_agreement_text:
                Intent  protocolIntent = new Intent(this,AboutUsActivity.class);
                protocolIntent.putExtra("type", com.linhuiba.business.config.Config.REGISTER_AGREEMENT_WEB_INT);
                startActivity(protocolIntent);
                break;
            case R.id.login_register_agreement_checkbox:
                if (!mLoginRegisterCB.isChecked()) {
                    mLoginRegisterCB.setChecked(false);
                    mFastLoginBtn.setEnabled(false);
                    mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                } else {
                    mLoginRegisterCB.setChecked(true);
                    if (mfast_edit_code.getText().toString().length() > 0 &&
                            mfast_edit_mobile.getText().toString().length() > 0) {
                        mFastLoginBtn.setEnabled(true);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                    } else {
                        mFastLoginBtn.setEnabled(false);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                    }
                }
                break;
            default:
                break;
        }
    }
    public static void reloadLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static void BaesActivityreloadLogin(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data.getStringExtra("Finish").equals("LoginActivityfinish")) {
                    if (isPushMessageIntent) {
                        Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                }
                break;
            case 3:
                if (isPushMessageIntent) {
                    Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    LoginActivity.this.finish();
                }
                break;
            case 4:
                if (data != null &&
                        data.getExtras() != null &&
                        data.getExtras().get(RESOURCE_ACCOUNT) != null &&
                        data.getExtras().get(RESOURCE_ACCOUNT).toString().length() >0) {
                    mLoginAccountET.setText(data.getExtras().get(RESOURCE_ACCOUNT).toString().trim());
                }
                break;
            default:
                break;
        }
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        } else if (requestCode == 2) {
            if (LoginManager.isLogin()) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Intent pushmessage = getIntent();
        if (pushmessage.getExtras() != null) {
            isPushMessageIntent = pushmessage.getBooleanExtra(PUSH_MSG_INTENT,false);
        }
        mLoginAccountET.setText("");
        mLoginPwET.setText("");
    }
    private void regist_receiver() {
        myBroadcastReciver = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BIND_WECHAT_SUCCESS_ACTION);
        intentFilter.addAction(BIND_WECHAT_ERROR_ACTION);
        this.registerReceiver(myBroadcastReciver, intentFilter);
    }
    @Override
    public void onAppLoginFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onAppLoginSuccess(LoginInfo loginInfo) {
        MobclickAgent.onProfileSignIn(mLoginAccountET.getText().toString());
        LoginManager.getInstance().updateLoginInfo(loginInfo);
        if (isPushMessageIntent) {
            Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            if (mLinkInt == 1) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainTabActivity.class);
                intent.putExtra("mLinkInt",mLinkInt);
                startActivity(intent);
            }
            LoginActivity.this.finish();
        }
        Constants constants = new Constants(LoginActivity.this);
        constants.binding_devices();
        LoginManager.getInstance().setNoticesUrl("");
        LoginManager.getInstance().setNoticescount(0);
        LoginManager.getInstance().setNoticesid(0);
        LoginManager.getInstance().setNoticesTitle("");
        LoginManager.getInstance().setNoticesshow(1);
    }

    @Override
    public void onWeChatLoginSuccess(LoginInfo loginInfoModel) {
        loginInfo = loginInfoModel;
        MobclickAgent.onProfileSignIn("WX",mLoginAccountET.getText().toString());
        if (loginInfo.getMobile() == null || loginInfo.getMobile().length() == 0) {
            Intent bingmobile_intent = new Intent(LoginActivity.this,BinDingMobileActivity.class);
            bingmobile_intent.putExtra("token",loginInfo.getApikey());
            startActivityForResult(bingmobile_intent,3);
        } else {
            LoginManager.getInstance().updateLoginInfo(loginInfo);
            if (isPushMessageIntent) {
                Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                LoginActivity.this.finish();
            }
            Constants constants = new Constants(LoginActivity.this);
            constants.binding_devices();
            LoginManager.getInstance().setNoticesUrl("");
            LoginManager.getInstance().setNoticescount(0);
            LoginManager.getInstance().setNoticesid(0);
            LoginManager.getInstance().setNoticesTitle("");
            LoginManager.getInstance().setNoticesshow(1);
        }
    }

    @Override
    public void onCaptchaSuccess(Response response) {
        if (response.data != null && response.data.toString().length() > 0) {
            mLoginRegisterCB.setChecked(true);
            JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
            if (jsonObject != null && jsonObject.get("old_user") != null) {
                if (jsonObject.getBoolean("old_user")) {
                    mLoginRegisterLL.setVisibility(View.GONE);
                    mFastLoginBtn.setText(getResources().getString(R.string.login_btn_text));
                } else {
                    mLoginRegisterLL.setVisibility(View.VISIBLE);
                    mFastLoginBtn.setText(getResources().getString(R.string.module_reister_login));
                }
            }
        }
        mTime.start();
    }

    @Override
    public void onFastLoginSuccess(LoginInfo loginInfo) {
        MobclickAgent.onProfileSignIn(mLoginAccountET.getText().toString());
        LoginManager.getInstance().updateLoginInfo(loginInfo);
        if (isPushMessageIntent) {
            Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            if (mLinkInt == 1) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainTabActivity.class);
                intent.putExtra("mLinkInt",mLinkInt);
                startActivity(intent);
            }
            LoginActivity.this.finish();
        }
        Constants constants = new Constants(LoginActivity.this);
        constants.binding_devices();
        LoginManager.getInstance().setNoticesUrl("");
        LoginManager.getInstance().setNoticescount(0);
        LoginManager.getInstance().setNoticesid(0);
        LoginManager.getInstance().setNoticesTitle("");
        LoginManager.getInstance().setNoticesshow(1);
    }

    @Override
    public void onQQLoginSuccess(LoginInfo loginInfoModel) {
        loginInfo = loginInfoModel;
        if (loginInfo.getMobile() == null || loginInfo.getMobile().length() == 0) {
            Intent bingmobile_intent = new Intent(LoginActivity.this,BinDingMobileActivity.class);
            bingmobile_intent.putExtra("token",loginInfo.getApikey());
            startActivityForResult(bingmobile_intent,3);
        } else {
            LoginManager.getInstance().updateLoginInfo(loginInfo);
            if (isPushMessageIntent) {
                Intent intent = new Intent(LoginActivity.this, com.linhuiba.business.activity.MySelfPushMessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                LoginActivity.this.finish();
            }
            Constants constants = new Constants(LoginActivity.this);
            constants.binding_devices();
            LoginManager.getInstance().setNoticesUrl("");
            LoginManager.getInstance().setNoticescount(0);
            LoginManager.getInstance().setNoticesid(0);
            LoginManager.getInstance().setNoticesTitle("");
            LoginManager.getInstance().setNoticesshow(1);
        }
    }

    private class  MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            roadcast_action = intent.getAction();
            if(roadcast_action.equals(BIND_WECHAT_SUCCESS_ACTION)) {
                //授权完成界面
                String code = intent.getExtras().getString("code");
                int city_id = 0;
                int province_id = 0;
                if (LoginManager.getInstance().getRegisterCurrentCityid() != null &&
                        LoginManager.getInstance().getRegisterCurrentCityid().length() > 0) {
                    city_id = Integer.parseInt(LoginManager.getInstance().getRegisterCurrentCityid());
                    province_id = Constants.getProvinceid(LoginActivity.this,city_id);
                }
                mLoginMvpPresenter.weChatLoginApi(code,province_id,city_id);
            } else if (roadcast_action.equals(BIND_WECHAT_ERROR_ACTION)) {
                //授权失败界面
                MessageUtils.showToast(getResources().getString(R.string.login_weixin_login_error_text));
            }
        }

    }
    //账号的限制 onTextChanged
    private void addTextChangedListener(final EditText accountET,
                                        final EditText pwET) {
        pwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String oldStr = com.linhuiba.business.connector.Constants.
                        registerStringFilter(pwET.getText().toString());
                if(!pwET.getText().toString().
                        equals(oldStr)){
                    pwET.setText(oldStr); //设置EditText的字符
                    pwET.setSelection(com.linhuiba.business.connector.Constants.
                            registerStringFilter(pwET.getText().toString()).length()); //因为删除了字符，要重写设置新的光标所在位置
                }
                if (pwET.getText().toString().length() > 0 &&
                        accountET.getText().toString().length() > 0) {
                    mLoginBtn.setEnabled(true);
                    mLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void addFastTextChangedListener(final EditText accountET,
                                        final EditText pwET, final int type) {
        pwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String oldStr = com.linhuiba.business.connector.Constants.
                        registerStringFilter(pwET.getText().toString());
                if(!pwET.getText().toString().
                        equals(oldStr)){
                    pwET.setText(oldStr); //设置EditText的字符
                    pwET.setSelection(com.linhuiba.business.connector.Constants.
                            registerStringFilter(pwET.getText().toString()).length()); //因为删除了字符，要重写设置新的光标所在位置
                }
                if (type == 1) {
                    if (pwET.getText().toString().length() > 0 &&
                            accountET.getText().toString().length() > 0 &&
                            mLoginRegisterCB.isChecked()) {
                        mFastLoginBtn.setEnabled(true);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                    } else {
                        mFastLoginBtn.setEnabled(false);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                    }
                } else if (type == 2) {
                    if (pwET.getText().toString().length() > 0 &&
                            accountET.getText().toString().length() > 0 &&
                            mLoginRegisterCB.isChecked()) {
                        mFastLoginBtn.setEnabled(true);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                        if (mLoginCodeBtn.getText().toString().equals(getResources().getString(R.string.txt_register_codebtn_txt))) {
                            mLoginCodeBtn.setEnabled(true);
                            mLoginCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                        }
                    } else {
                        mFastLoginBtn.setEnabled(false);
                        mFastLoginBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                        if (pwET.getText().toString().length() > 0 &&
                                mLoginCodeBtn.getText().toString().equals(getResources().getString(R.string.txt_register_codebtn_txt))) {
                            mLoginCodeBtn.setEnabled(true);
                            mLoginCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                        } else {
                            mLoginCodeBtn.setEnabled(false);
                            mLoginCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mLoginCodeBtn.setText(R.string.txt_register_codebtn_txt);
            if (mfast_edit_mobile.getText().toString().length() > 0) {
                mLoginCodeBtn.setEnabled(true);
                mLoginCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mLoginCodeBtn.setEnabled(false);
            mLoginCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
            mLoginCodeBtn.setText(str+"("+(millisUntilFinished / 1000)+")");
        }
    }
}
