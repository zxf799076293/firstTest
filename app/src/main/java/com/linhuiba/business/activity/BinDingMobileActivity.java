package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.WalletApplyPasswordAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/4/18.
 */

public class BinDingMobileActivity extends BaseMvpActivity {
    @InjectView(R.id.bingding_mobile_mobile_edit)
    EditText mbingding_mobile_mobile_edit;
    @InjectView(R.id.bingding_mobile_captcha_btn)
    Button mbingding_mobile_captcha_btn;
    @InjectView(R.id.bingding_mobile_captcha_edit)
    EditText mbingding_mobile_captcha_edit;
    @InjectView(R.id.bingding_mobile_btn)
    Button mbingding_mobile_btn;
    private TimeCount mTime;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingmobilesettingpw);
        ButterKnife.inject(this);
        initview();
    }
    private void initview() {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_bindingmobile_title_text));
        TitleBarUtils.showBackImg(this,true);
        mTime = new TimeCount(60000, 1000);
        Intent login_intent = getIntent();
        if (login_intent.getExtras() != null) {
            if (login_intent.getExtras().get("token") != null &&
                    login_intent.getExtras().getString("token").length() > 0) {
                token = login_intent.getExtras().getString("token");
                if (token != null && token.length() > 0) {

                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    finish();
                }
            }
        }

    }
    @OnClick({
            R.id.bingding_mobile_btn,
            R.id.bingding_mobile_captcha_btn
    })
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.bingding_mobile_btn:
                if (TextUtils.getTrimmedLength(mbingding_mobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mbingding_mobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mbingding_mobile_captcha_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_code_hint));
                    return;
                }
                if (mbingding_mobile_captcha_edit.getText().toString().length() != 6) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                if (token != null && token.length() > 0) {
                    UserApi.bind_mobile(MyAsyncHttpClient.MyAsyncHttpClient(),BindMobileHandler,
                            mbingding_mobile_mobile_edit.getText().toString(),mbingding_mobile_captcha_edit.getText().toString(),token);
                } else {
                    UserApi.bind_mobile(MyAsyncHttpClient.MyAsyncHttpClient(),BindMobileHandler,
                            mbingding_mobile_mobile_edit.getText().toString(),mbingding_mobile_captcha_edit.getText().toString());
                }
                break;
            case R.id.bingding_mobile_captcha_btn:
                if (TextUtils.getTrimmedLength(mbingding_mobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mbingding_mobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mbingding_mobile_mobile_edit.getText().toString(),"3");
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                mTime.start();
                mbingding_mobile_captcha_btn.setEnabled(true);
            } else {
                MessageUtils.showToast(getResources().getString(R.string.txt_register_send_codeb_error_remind));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mbingding_mobile_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mbingding_mobile_captcha_btn.setClickable(true);
            mbingding_mobile_captcha_btn.setBackgroundResource(R.color.white);
            mbingding_mobile_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mbingding_mobile_captcha_edit.setEnabled(true);
            mbingding_mobile_captcha_btn.setClickable(false);
            mbingding_mobile_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mbingding_mobile_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mbingding_mobile_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private LinhuiAsyncHttpResponseHandler BindMobileHandler = new LinhuiAsyncHttpResponseHandler(LoginInfo.class,false) {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                LoginInfo loginInfo = (LoginInfo) data;
                if (loginInfo.getMobile() != null && loginInfo.getMobile().length() > 0) {
                    LoginManager.getInstance().updateLoginInfo((LoginInfo) data);
                    Constants constants = new Constants(BinDingMobileActivity.this);
                    constants.binding_devices();
                    LoginManager.getInstance().setNoticesUrl("");
                    LoginManager.getInstance().setNoticescount(0);
                    LoginManager.getInstance().setNoticesid(0);
                    LoginManager.getInstance().setNoticesTitle("");
                    LoginManager.getInstance().setNoticesshow(1);
                    Intent login_intent = getIntent();
                    setResult(3,login_intent);
                    finish();
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    finish();
                }
            } else {
                MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                finish();
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            if (Response.isAccesstokenError(error)) {
                LoginManager.getInstance().clearLoginInfo();
                finish();
            }

        }
    };
}
