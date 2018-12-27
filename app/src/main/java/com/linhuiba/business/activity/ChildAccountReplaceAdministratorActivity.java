package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ChildAccountReplaceAdministratorActivity extends BaseMvpActivity {
    private EditText mreplaceadministrator_captcha_edit;
    private EditText mreplaceadministrator_mobile_edit;
    private Button mreplaceadministrator_btn;
    private LinearLayout mreplaceadministrator_choose_layout;
    private Button mreplaceadministrator_captcha_btn;
    private RelativeLayout mchoildaccount_changeadmin_layout;
    private TextView mchangeadmin_name_text;
    private TextView mnew_administrator_name;
    private TextView mnew_administrator_mobile;
    private TimeCount mTime;
    private String account_id = "";
    private String account_name = "";
    private String account_mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childaccountreplaceadministrator);
        initView();
    }
    private void initView() {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.txt_childaccounteditor_change_the_administrator_txt));
        TitleBarUtils.showBackImg(this,true);
        mreplaceadministrator_mobile_edit = (EditText)findViewById(R.id.replaceadministrator_mobile_edit);
        mreplaceadministrator_captcha_edit = (EditText) findViewById(R.id.replaceadministrator_captcha_edit);
        mreplaceadministrator_btn = (Button)findViewById(R.id.replaceadministrator_btn);
        mreplaceadministrator_choose_layout = (LinearLayout)findViewById(R.id.replaceadministrator_choose_layout);
        mreplaceadministrator_captcha_btn = (Button)findViewById(R.id.replaceadministrator_captcha_btn);
        mchoildaccount_changeadmin_layout = (RelativeLayout)findViewById(R.id.choildaccount_changeadmin_layout);
        mnew_administrator_name = (TextView)findViewById(R.id.new_administrator_name);
        mnew_administrator_mobile = (TextView)findViewById(R.id.new_administrator_mobile);
        mchangeadmin_name_text = (TextView)findViewById(R.id.changeadmin_name_text);
        mTime = new TimeCount(60000, 1000);
        mchoildaccount_changeadmin_layout.setVisibility(View.GONE);
        if (LoginManager.isLogin()) {
            mreplaceadministrator_mobile_edit.setText(LoginManager.getMobile());
            OnClick();
        } else {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            this.finish();
        }
    }
    private void OnClick() {
        mreplaceadministrator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.getTrimmedLength(mreplaceadministrator_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mreplaceadministrator_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mreplaceadministrator_captcha_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.txt_code));
                    return;
                }
                if (account_id.trim().length() == 0) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.childaccount_binding_administrator_hint_text));
                    return;
                }
                showProgressDialog();
                UserApi.child_accounts_change_admin(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),change_adminHandler,
                        mreplaceadministrator_mobile_edit.getText().toString(),mreplaceadministrator_captcha_edit.getText().toString(),account_id);
            }
        });
        mreplaceadministrator_choose_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent(ChildAccountReplaceAdministratorActivity.this,ChildAccountActivity.class);
                childaccount.putExtra("changeadmin",1);
                startActivityForResult(childaccount,1);
            }
        });
        mreplaceadministrator_captcha_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.getTrimmedLength(mreplaceadministrator_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mreplaceadministrator_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(ChildAccountReplaceAdministratorActivity.this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(ChildAccountReplaceAdministratorActivity.this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mreplaceadministrator_mobile_edit.getText().toString(),"3");
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
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
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler change_adminHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            LoginManager.setEnterprise_role(2);
            mchoildaccount_changeadmin_layout.setVisibility(View.VISIBLE);
            if (account_name.length() > 0) {
                mnew_administrator_name.setText(account_name);
                mnew_administrator_name.setVisibility(View.VISIBLE);
            } else {
                mnew_administrator_name.setVisibility(View.GONE);
            }
            if (account_mobile.length() > 0) {
                mnew_administrator_mobile.setText(account_mobile);
                mnew_administrator_mobile.setVisibility(View.VISIBLE);
            } else {
                mnew_administrator_mobile.setVisibility(View.GONE);
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
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mreplaceadministrator_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mreplaceadministrator_captcha_btn.setClickable(true);
            mreplaceadministrator_captcha_btn.setBackgroundResource(R.color.white);
            mreplaceadministrator_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mreplaceadministrator_captcha_btn.setClickable(false);
            mreplaceadministrator_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mreplaceadministrator_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mreplaceadministrator_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data != null && data.getExtras() != null) {
                    if (data.getExtras().get("accounid") != null && data.getExtras().getString("accounid").length() > 0) {
                        account_id = data.getExtras().getString("accounid");
                    }
                    if (data.getExtras().get("accounmobile") != null && data.getExtras().getString("accounmobile").length() > 0) {
                        mchangeadmin_name_text.setText(data.getExtras().getString("accounmobile"));
                        account_mobile = data.getExtras().getString("accounmobile");
                    }
                    if (data.getExtras().get("accounname") != null && data.getExtras().getString("accounname").length() > 0) {
                        account_name = data.getExtras().getString("accounname");
                    }

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
