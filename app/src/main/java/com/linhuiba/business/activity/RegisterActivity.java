package com.linhuiba.business.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.AppViewPageAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.ShuoMClickableSpan;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.model.LoginInfoModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/29.
 */
public class RegisterActivity extends BaseMvpActivity {
    /*注册的界面 考虑到以后做原生的需要
    @InjectView(R.id.edit_mobile)
    EditText medit_mobile;
    @InjectView(R.id.edit_code)
    EditText medit_code;
    @InjectView(R.id.edit_password)
    EditText medit_password;
    @InjectView(R.id.confirm_pw)
    EditText mconfirm_pw;
    @InjectView(R.id.btnregister)
    TextView mbtnregister;
    @InjectView(R.id.btncode)
    Button mbtn_code;
    @InjectView(R.id.yaoqingma)
    EditText minvite_code;
    @InjectView(R.id.register_agreement_checkbox)
    CheckBox mregister_agreement_checkbox;
    @InjectView(R.id.register_agreement_text)
    TextView mregister_agreement_text;
    @InjectView(R.id.register_tablayout)
    TabLayout mRegisterTabL;
    @InjectView(R.id.register_viewpager)
    ViewPager mRegisterVP;
    private TimeCount mTime;
    private String cityId;
    private int mTabTvStrList[] = {
            R.string.register_tab_corporate_account_str, R.string.register_tab_personal_account_str
    };
    private ArrayList<View> mListViews;
    private int currIndex;
    private LinearLayout[] mRegisterEnterpriseLL = new LinearLayout[2];
    private LinearLayout[] mRegisterConfirmEnterpriseLL = new LinearLayout[2];
    private LinearLayout[] mRegisterAccountLL = new LinearLayout[2];
    private TextView[] mRegisterInstructionsTV = new TextView[2];
    private TextView[] mRegisterServiceNumberTV = new TextView[2];
    private TextView[] mRegisterServiceTimeTV = new TextView[2];
    private Button[] mRegisterNextBtn = new Button[2];
    */
    @InjectView(R.id.register_webview)
    BridgeWebView mRegisterWV;
    @InjectView(R.id.register_success_ll)
    LinearLayout mRegisterSuccessLL;
    @InjectView(R.id.register_success_attestation_tv)
    TextView mRegisterSuccessAttestationTV;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 2;
    public static final int REQUEST_SELECT_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initView();
        /*
        mTime = new TimeCount(60000, 1000);
        mbtn_code.setClickable(false);
        mbtn_code.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
        medit_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mbtn_code.setClickable(true);
                    mbtn_code.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mbtn_code.setClickable(false);
                    mbtn_code.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
                }
            }
        });
        initView();
        * */

    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.register_activity_name_str));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.register_activity_name_str));
        MobclickAgent.onPause(this);
    }
    @OnClick({
            R.id.register_success_home_tv,
            R.id.register_success_add_demand_tv,
            R.id.register_success_attestation_tv,
            R.id.register_success_coupons_imgv
    })
    public void onTvClick(View view) {
        switch (view.getId()) {
            case R.id.register_success_home_tv:
                Intent maintabintent = new Intent(RegisterActivity.this, MainTabActivity.class);
                maintabintent.putExtra("new_tmpintent", "goto_homepage");
                startActivity(maintabintent);
                break;
            case R.id.register_success_add_demand_tv:
                Intent AddDemand = new Intent(RegisterActivity.this,AboutUsActivity.class);
                AddDemand.putExtra("type", Config.ADD_DEMAND_WEB_INT);
                startActivity(AddDemand);
                finish();
                break;
            case R.id.register_success_attestation_tv:
                if (LoginManager.getEnterprise_authorize_status() == 1) {
                    if (LoginManager.isRight_to_publish()) {
                        Intent fieldMainIntent = new Intent(RegisterActivity.this, com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity.class);
                        fieldMainIntent.putExtra("new_tmpintent", "fieldlist");
                        startActivity(fieldMainIntent);
                    } else {
                        Intent intent = new Intent(RegisterActivity.this,AboutUsActivity.class);
                        intent.putExtra("type", Config.APPLY_FOR_RELEASE_INT);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(RegisterActivity.this,AboutUsActivity.class);
                    intent.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.register_success_coupons_imgv:
                if (com.linhuiba.business.connector.Constants.isFastClick()) {
                    Intent firstRegisterCouponsIntent = new Intent(RegisterActivity.this, TheFirstRegisterCouponsActivity.class);
                    startActivity(firstRegisterCouponsIntent);
                }
                break;
            default:
                break;
        }
    }
    private void initView() {
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_register_btn_txt));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterWV != null &&
                        mRegisterWV.canGoBack()) {
                    mRegisterWV.goBack();// 返回前一个页面
                } else {
                    RegisterActivity.this.finish();
                }
            }
        });
        mRegisterWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mRegisterWV.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mRegisterWV.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mRegisterWV.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mRegisterWV.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mRegisterWV.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mRegisterWV.getSettings().setAppCacheEnabled(false);//是否使用缓存
        mRegisterWV.getSettings().setDomStorageEnabled(true);//DOM Storage
        mRegisterWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mRegisterWV.loadUrl(Config.Domain_Name + Config.REGISTER_URL);
        AndPermission.with(RegisterActivity.this)
                .requestCode(com.linhuiba.business.connector.Constants.PermissionRequestCode)
                .permission(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();

        //注册完成
        mRegisterWV.registerHandler("registerSuccess", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null && data.length() > 0) {
                    LoginInfo loginInfo = JSONObject.parseObject(data.toString(), LoginInfo.class);
                    if (loginInfo != null) {
                        LoginManager.getInstance().updateLoginInfo(loginInfo);
                        com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(RegisterActivity.this);
                        constants.binding_devices();
                        LoginManager.getInstance().setNoticesUrl("");
                        LoginManager.getInstance().setNoticescount(0);
                        LoginManager.getInstance().setNoticesid(0);
                        LoginManager.getInstance().setNoticesTitle("");
                        LoginManager.getInstance().setNoticesshow(1);
                        mRegisterSuccessLL.setVisibility(View.VISIBLE);
                        if (LoginManager.getEnterprise_authorize_status() == 1) {
                            mRegisterSuccessAttestationTV.setText(getResources().getString(R.string.mTxt_release_field));
                        } else {
                            mRegisterSuccessAttestationTV.setText(getResources().getString(R.string.module_register_success_goto_approve));
                        }
                    }
                }
                MobclickAgent.onEvent(RegisterActivity.this,com.linhuiba.linhuifield.config.Config.UM_REGISTER_CLICK);
            }
        });
        //联系我们
        mRegisterWV.registerHandler("telUs", new BridgeHandler() {
            @Override
            public void handler(final String data, CallBackFunction function) {
                Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + data));
                startActivity(intent_mobile);
            }
        });
    }
    private void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                mRegisterWV != null &&
                mRegisterWV.canGoBack()) {
            mRegisterWV.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == com.linhuiba.business.connector.Constants.PermissionRequestCode) {
                mRegisterWV.setWebChromeClient(new WebChromeClient() {
                    // For 3.0+ Devices (Start)
                    // onActivityResult attached before constructor
                    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                        mUploadMessage = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        RegisterActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                    }

                    // For Lollipop 5.0+ Devices
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        if (uploadMessage != null) {
                            uploadMessage.onReceiveValue(null);
                            uploadMessage = null;
                        }
                        uploadMessage = filePathCallback;
                        try {
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("*/*");
                            startActivityForResult(Intent.createChooser(i, "File Chooser"), REQUEST_SELECT_FILE);
                        } catch (ActivityNotFoundException e) {
                            uploadMessage = null;
                            MessageUtils.showToast(getBaseContext(), "Cannot Open File Chooser");
                            return false;
                        }
                        return true;
                    }

                    //For Android 4.1 only
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        mUploadMessage = uploadMsg;
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        RegisterActivity.this.startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
                    }

                    //for Android <3.0
                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        mUploadMessage = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        RegisterActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                    }
                });
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == com.linhuiba.business.connector.Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_FILE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (uploadMessage == null)
                        return;
//                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    uploadMessage = null;
                    onActivityResultAboveL(requestCode, resultCode, data);
                }
                break;
            case FILECHOOSER_RESULTCODE:
                if (null == mUploadMessage)
                    return;
                // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
                // Use RESULT_OK only if you're implementing WebView inside an Activity
                Uri result = data == null || resultCode != AboutUsActivity.RESULT_OK ? null : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_SELECT_FILE || uploadMessage == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessage.onReceiveValue(results);
        uploadMessage = null;
    }
    /*
    @OnClick({
            R.id.btncode,
            R.id.btnregister,
            R.id.register_agreement_layout,
            R.id.register_agreement_checkbox,
            R.id.register_agreement_text
    })
    public void registerclient (View view){
        switch (view.getId()) {
            case R.id.btncode:
                if (TextUtils.getTrimmedLength(medit_mobile.getText().toString()) == 0) {
                    MessageUtils.showToast(RegisterActivity.this, getResources().getString(R.string.txt_phonenumber_hint));
                } else if (!CommonTool.isMobileNO(medit_mobile.getText().toString())) {
                    MessageUtils.showToast(RegisterActivity.this, getResources().getString(R.string.txt_correctphonenumber_hint));
                } else {
                    showProgressDialog(this.getResources().getString(R.string.txt_waiting));
                    UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getVCodeHandler,
                            medit_mobile.getText().toString(), "1");
                }

                break;
            case R.id.btnregister:
                closeBoard();
                if (TextUtils.getTrimmedLength(medit_mobile.getText().toString()) == 0) {
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(medit_mobile.getText().toString())) {
                    MessageUtils.showToast(RegisterActivity.this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(medit_code.getText().toString()) == 0 || medit_code.getText().toString().length() < 4) {
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                if (TextUtils.getTrimmedLength(medit_password.getText().toString()) == 0) {
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_password_hint));
                    return;
                }
                if (medit_password.getText().toString().length() < 6) {
                    medit_password.setText("");
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_password_lenthhint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mconfirm_pw.getText().toString()) == 0) {
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_confirm_pw_hint));
                    return;
                }
                if (mconfirm_pw.getText().toString().length() < 6) {
                    mconfirm_pw.setText("");
                    MessageUtils.showToast(RegisterActivity.this,getResources().getString(R.string.txt_password_lenthhint));
                    return;
                }
                if (!(medit_password.getText().toString().equals(mconfirm_pw.getText().toString()))) {
                    MessageUtils.showToast(RegisterActivity.this, getResources().getString(R.string.txt_confirm_possword_remind));
                    return;
                }
                showProgressDialog(this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthregister(MyAsyncHttpClient.MyAsyncHttpClient(), registerHandler,
                        medit_mobile.getText().toString(), medit_code.getText().toString(), medit_password.getText().toString(), minvite_code.getText().toString(),cityId);
                MobclickAgent.onEvent(RegisterActivity.this,"re_click_register");
                break;
            case R.id.register_agreement_layout:
                if (mregister_agreement_checkbox.isChecked()) {
                    mregister_agreement_checkbox.setChecked(false);
                    mbtnregister.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttom_btn_gray_bg));
                    mbtnregister.setEnabled(false);
                } else {
                    mregister_agreement_checkbox.setChecked(true);
                    mbtnregister.setEnabled(true);
                    mbtnregister.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                }
                break;
            case R.id.register_agreement_checkbox:
                if (mregister_agreement_checkbox.isChecked()) {
                    mregister_agreement_checkbox.setChecked(false);
                    mbtnregister.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttom_btn_gray_bg));
                    mbtnregister.setEnabled(false);
                } else {
                    mregister_agreement_checkbox.setChecked(true);
                    mbtnregister.setEnabled(true);
                    mbtnregister.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                }
                break;
            case R.id.register_agreement_text:
                Intent about_intrgral = new Intent(this,AboutUsActivity.class);
                about_intrgral.putExtra("type", Config.REGISTER_AGREEMENT_WEB_INT);
                startActivity(about_intrgral);
                break;
            default:
                break;
        }

    };
    // 定义一个倒计时的内部类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mbtn_code.setText(R.string.txt_register_codebtn_txt);
            mbtn_code.setClickable(true);
            mbtn_code.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mbtn_code.setClickable(false);
            mbtn_code.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
            mbtn_code.setText(str+"("+(millisUntilFinished / 1000)+")");
        }
    }
    private LinhuiAsyncHttpResponseHandler getVCodeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                MessageUtils.showToast(getContext(), getResources().getString(R.string.txt_register_send_codeb_remind));
                mTime.start();
            } else {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.txt_register_send_codeb_error_remind));
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
        }

    };
    private LinhuiAsyncHttpResponseHandler registerHandler = new LinhuiAsyncHttpResponseHandler(LoginInfoModel.class) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.txt_register_success_remind));
                startActivity(new Intent(getContext(), LoginActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                ));
                finish();

            } else {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.txt_register_error_remind));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode,okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult){
                MessageUtils.showToast(RegisterActivity.this, error.getMessage());
            }

        }
    };
    //关闭软键盘
    private  void closeBoard() {
        InputMethodManager imm = (InputMethodManager) this.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void initView() {
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_register_btn_txt));
        TitleBarUtils.showBackImg(this, true);
        if (LoginManager.getInstance().getRegisterCurrentCityid() != null &&
                LoginManager.getInstance().getRegisterCurrentCityid().length() > 0) {
            cityId = LoginManager.getInstance().getRegisterCurrentCityid();
        }

        mListViews = new ArrayList<View>();
        LayoutInflater inflater = this.getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.activity_register_tab_view, null));
        mListViews.add(inflater.inflate(R.layout.activity_register_tab_view, null));
        for( int i = 0; i < mListViews.size(); i++ ) {
            mRegisterEnterpriseLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.register_enterprise_verify_ll);
            mRegisterConfirmEnterpriseLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.register_confirm_enterprise_ll);
            mRegisterAccountLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.register_account_ll);
            mRegisterInstructionsTV[i] = (TextView) mListViews.get(i).findViewById(R.id.register_upload_instructions_tv);
            mRegisterServiceNumberTV[i] = (TextView) mListViews.get(i).findViewById(R.id.register_check_service_number_tv);
            mRegisterServiceTimeTV[i] = (TextView) mListViews.get(i).findViewById(R.id.register_check_service_time_tv);
            mRegisterNextBtn [i] = (Button) mListViews.get(i).findViewById(R.id.register_next_btn);
        }
        mRegisterVP.setAdapter(new AppViewPageAdapter(mListViews));
        mRegisterVP.setCurrentItem(currIndex);
        mRegisterVP.setOnPageChangeListener(new PagerChangeListener());
        setmRegisterView(currIndex);
        mRegisterTabL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mRegisterTabL,60,60);
            }
        });
        mRegisterTabL.setupWithViewPager(mRegisterVP);
        for (int i = 0; i < mRegisterTabL.getTabCount(); i++) {
            mRegisterTabL.getTabAt(i).setText(getResources().getString(mTabTvStrList[i]));
        }
    }
    private class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            switch (position) {
                case 0:
                    setmRegisterView(currIndex);
                    break;
                case 1:
                    setmRegisterView(currIndex);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void setmRegisterView(int type) {
        switch (type) {
            case 0:
                mRegisterEnterpriseLL[currIndex].setVisibility(View.VISIBLE);
                mRegisterAccountLL[currIndex].setVisibility(View.GONE);
                //一个textview显示多种格式并有点击事件
                String string = getResources().getString(R.string.register_upload_instructions_first_str);
                String serviceNumber = LoginManager.getServicephoneNumber();
                String serviceTime = "(" +
                        LoginManager.getServicetime() + ")。";
                SpannableString seconeStr = new SpannableString(serviceNumber);
                ClickableSpan secondSpan = new ShuoMClickableSpan(serviceNumber,
                        this,1);
                seconeStr.setSpan(secondSpan, 0, serviceNumber.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mRegisterInstructionsTV[0].setText(string);
                mRegisterInstructionsTV[0].append(seconeStr);
                mRegisterInstructionsTV[0].append(serviceTime);
                mRegisterInstructionsTV[0].setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 1:
                mRegisterEnterpriseLL[currIndex].setVisibility(View.GONE);
                mRegisterAccountLL[currIndex].setVisibility(View.VISIBLE);
                break;
        }
    }
    */

}
