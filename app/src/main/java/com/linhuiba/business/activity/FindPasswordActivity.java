package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class FindPasswordActivity extends BaseMvpActivity {
    @InjectView(R.id.edit_mobile) EditText mMobile;
    @InjectView(R.id.edit_code)  EditText mCode;
    @InjectView(R.id.edit_password)  EditText mPassword;
    @InjectView(R.id.confirm_pw)  EditText mmconfirm_Password;
    @InjectView(R.id.btncode) Button mBtn_code;
    @InjectView(R.id.btnfindpassword) Button mFindPwBtn;
    private TimeCount mTime;
    private int isModifyInt;//1为修改密码跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.pw_find));
        TitleBarUtils.showBackImg(this,true);
        mTime = new TimeCount(60000, 1000);
        addTextChangedListener(mMobile,mCode,mPassword,mmconfirm_Password,2);
        addTextChangedListener(mCode,mMobile,mPassword,mmconfirm_Password,1);
        addTextChangedListener(mPassword,mCode,mMobile,mmconfirm_Password,1);
        addTextChangedListener(mmconfirm_Password,mCode,mPassword,mMobile,1);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("is_modity_pw") != null &&
                intent.getExtras().getInt("is_modity_pw") == 1) {
            isModifyInt = 1;
            if (LoginManager.getMobile() != null && LoginManager.getMobile().length() > 0) {
                mMobile.setText(LoginManager.getMobile());
                mMobile.setEnabled(false);
            }
        }
    }
    @OnClick({

            R.id.btnfindpassword,
            R.id.btncode,

    })
    public void onModifyOnClick (View view){
        switch (view.getId()){
            case R.id.btnfindpassword:
                if (TextUtils.isEmpty(mMobile.getText())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (mMobile.getText().length() != 11) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mMobile.getText().toString())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }

                if (!CommonTool.isMobileNO(mMobile.getText().toString())) {
                    MessageUtils.showToast(FindPasswordActivity.this, "请输入有效手机号码");
                    return;
                }
                if (TextUtils.isEmpty(mCode.getText()) || mCode.getText().length() < 4 ) {
                    MessageUtils.showToast(this,getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                if (TextUtils.isEmpty(mPassword.getText()) ) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_password_hint));
                    return;
                }
                if (mPassword.getText().toString().length() < 6) {
                    mPassword.setText("");
                    MessageUtils.showToast(FindPasswordActivity.this, getResources().getString(R.string.txt_password_lenthhint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mmconfirm_Password.getText().toString()) == 0) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_confirm_pw_hint));
                    return;
                }
                if (mmconfirm_Password.getText().toString().length() < 6) {
                    mmconfirm_Password.setText("");
                    MessageUtils.showToast(FindPasswordActivity.this, getResources().getString(R.string.txt_password_lenthhint));
                    return;
                }

                if (!(mPassword.getText().toString().equals(mmconfirm_Password.getText().toString()))) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_confirm_possword_remind));
                    mPassword.setText("");
                    mmconfirm_Password.setText("");
                    return;
                }
                showProgressDialog(this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauth_reset_password(MyAsyncHttpClient.MyAsyncHttpClient(), ModifyPassWordHandler,
                        mMobile.getText().toString(), mCode.getText().toString(), mPassword.getText().toString(),
                        mmconfirm_Password.getText().toString()
                );
                break;

            case R.id.btncode:
                if (TextUtils.isEmpty(mMobile.getText())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mMobile.getText().toString())) {
                    MessageUtils.showToast(this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }

                showProgressDialog(this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getVCodeHandler,
                        mMobile.getText().toString(),"3");
                break;
            default:
                break;
        }
    }

    private LinhuiAsyncHttpResponseHandler getVCodeHandler = new LinhuiAsyncHttpResponseHandler(null) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if ( response.code ==1){
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
    private LinhuiAsyncHttpResponseHandler ModifyPassWordHandler = new LinhuiAsyncHttpResponseHandler(null) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if(response.code == 1) {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.pw_modify_success_text));
                if (isModifyInt == 1) {
                    LoginMvpModel.loginOut();
                    LoginManager.getInstance().clearLoginInfo();
                    //退出登录时清除公告信息
                    LoginManager.getInstance().setNoticesUrl("");
                    LoginManager.getInstance().setNoticescount(0);
                    LoginManager.getInstance().setNoticesid(0);
                    LoginManager.getInstance().setNoticesTitle("");
                    //退出登录时清空保存的 评价提醒时间
                    LoginManager.getInstance().setHome_review_show_time(0);
                    //友盟退出登录统计
                    MobclickAgent.onProfileSignOff();
                    Intent cartsIntent = new Intent(FindPasswordActivity.this, MainTabActivity.class);
                    cartsIntent.putExtra("new_tmpintent", "goto_myself");
                    cartsIntent.putExtra("goto_login", 1);
                    cartsIntent.putExtra("goto_login", 1);
                    startActivity(cartsIntent);
                } else {
                    finish();
                }
            } else {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.pw_modify_error_text));
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }

    };
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mBtn_code.setText(R.string.btn_securitycode);
            if (mMobile.getText().toString().length() > 0) {
                mBtn_code.setClickable(true);
                mBtn_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mBtn_code.setClickable(false);
            mBtn_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
            mBtn_code.setText(str+"("+(millisUntilFinished / 1000)+")");
        }
    }
    private void addTextChangedListener(final EditText accountET,
                                        final EditText codeET,
                                        final EditText pwET,
                                        final EditText confirmPwET,
                                        final int type) {
        accountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String oldStr = com.linhuiba.business.connector.Constants.
                        registerStringFilter(accountET.getText().toString());
                if(!accountET.getText().toString().
                        equals(oldStr)){
                    accountET.setText(oldStr); //设置EditText的字符
                    accountET.setSelection(com.linhuiba.business.connector.Constants.
                            registerStringFilter(accountET.getText().toString()).length()); //因为删除了字符，要重写设置新的光标所在位置
                }
                if (type == 1) {
                    if (pwET.getText().toString().length() > 0 &&
                            accountET.getText().toString().length() > 0 &&
                            confirmPwET.getText().toString().length() > 0 &&
                            codeET.getText().toString().length() > 0) {
                        mFindPwBtn.setEnabled(true);
                        mFindPwBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                    } else {
                        mFindPwBtn.setEnabled(false);
                        mFindPwBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                    }
                } else if (type == 2) {
                    if (pwET.getText().toString().length() > 0 &&
                            accountET.getText().toString().length() > 0 &&
                            confirmPwET.getText().toString().length() > 0 &&
                            codeET.getText().toString().length() > 0) {
                        if (mBtn_code.getText().toString().equals(getResources().getString(R.string.btn_securitycode))) {
                            mBtn_code.setEnabled(true);
                            mBtn_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                        }
                        mFindPwBtn.setEnabled(true);
                        mFindPwBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                    } else {
                        mFindPwBtn.setEnabled(false);
                        mFindPwBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                        if (accountET.getText().toString().length() > 0 &&
                                mBtn_code.getText().toString().equals(getResources().getString(R.string.btn_securitycode))) {
                            mBtn_code.setEnabled(true);
                            mBtn_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                        } else {
                            mBtn_code.setEnabled(false);
                            mBtn_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_unenable_bg));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
