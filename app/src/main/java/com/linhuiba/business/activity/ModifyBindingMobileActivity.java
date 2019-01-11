package com.linhuiba.business.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/4/12.
 */

public class ModifyBindingMobileActivity extends BaseMvpActivity {
    @InjectView(R.id.mobile_authenticate_text)
    TextView mmobile_authenticate_text;
    @InjectView(R.id.mobile_authenticate_img)
    ImageView mmobile_authenticate_img;
    @InjectView(R.id.mobile_new_mobileverify_text)
    TextView mmobile_new_mobileverify_text;
    @InjectView(R.id.mobile_mobile_edit)
    EditText mmobile_mobile_edit;
    @InjectView(R.id.mobile_captcha_btn)
    Button mmobile_captcha_btn;
    @InjectView(R.id.mobile_captcha_edit)
    EditText mmobile_captcha_edit;
    @InjectView(R.id.mobile_modify_btn)
    Button mmobile_modify_btn;

    @InjectView(R.id.new_mobile_mobile_edit)
    EditText mnew_mobile_mobile_edit;
    @InjectView(R.id.new_mobile_captcha_btn)
    Button mnew_mobile_captcha_btn;
    @InjectView(R.id.new_mobile_captcha_edit)
    EditText mnew_mobile_captcha_edit;
    @InjectView(R.id.new_mobile_modify_btn)
    Button mnew_mobile_modify_btn;
    @InjectView(R.id.mobile_authenticate_layout)
    LinearLayout mmobile_authenticate_layout;
    @InjectView(R.id.new_mobile_authenticate_layout)
    LinearLayout mnew_mobile_authenticate_layout;
    private final int old_mobilde_verify_view = 1;
    private final int new_mobilde_verify_view = 2;
    private int modify_viewpage = old_mobilde_verify_view;
    private TimeCount mTime;
    private NewTimeCount mNewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifymobile);
        ButterKnife.inject(this);
        initview();
    }
    @OnClick({
            R.id.mobile_modify_btn,
            R.id.mobile_captcha_btn,
            R.id.new_mobile_modify_btn,
            R.id.new_mobile_captcha_btn
    })
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.mobile_modify_btn:
                if (TextUtils.getTrimmedLength(mmobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mmobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mmobile_captcha_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_code_hint));
                    return;
                }
                if (mmobile_captcha_edit.getText().toString().length() != 6) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                showProgressDialog();
                UserApi.validate_captcha(MyAsyncHttpClient.MyAsyncHttpClient(),validate_captchaHandler,mmobile_mobile_edit.getText().toString(),
                        mmobile_captcha_edit.getText().toString());
                break;
            case R.id.mobile_captcha_btn:
                if (TextUtils.getTrimmedLength(mmobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mmobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(ModifyBindingMobileActivity.this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mmobile_mobile_edit.getText().toString(),"3");
                break;
            case R.id.new_mobile_modify_btn:
                if (TextUtils.getTrimmedLength(mnew_mobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mnew_mobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                if (TextUtils.getTrimmedLength(mnew_mobile_captcha_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_code_hint));
                    return;
                }
                if (mnew_mobile_captcha_edit.getText().toString().length() != 6) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_register_remind_codebtn_txt));
                    return;
                }
                showProgressDialog();
                UserApi.change_mobile(MyAsyncHttpClient.MyAsyncHttpClient(),change_mobileHandler,mmobile_captcha_edit.getText().toString().trim(),
                        mnew_mobile_mobile_edit.getText().toString(),mnew_mobile_captcha_edit.getText().toString());

                break;
            case R.id.new_mobile_captcha_btn:
                if (TextUtils.getTrimmedLength(mnew_mobile_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mnew_mobile_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog();
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mnew_mobile_mobile_edit.getText().toString(),"3");
                break;
            default:
                break;
        }
    }
    private void initview() {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_modifymobile_title_text));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modify_viewpage == old_mobilde_verify_view) {
                    ModifyBindingMobileActivity.this.finish();
                } else {
                    modify_viewpage = old_mobilde_verify_view;
                    show_madify_mobile();
                }
            }
        });
        mTime = new TimeCount(60000, 1000);
        mNewTime = new NewTimeCount(60000, 1000);

        mmobile_mobile_edit.setEnabled(false);
        mmobile_mobile_edit.setText(LoginManager.getMobile());
        mmobile_captcha_edit.setEnabled(false);
        mmobile_captcha_edit.setText("");
        show_madify_mobile();
    }
    private void show_madify_mobile() {
        if (modify_viewpage == old_mobilde_verify_view) {
            mmobile_authenticate_layout.setVisibility(View.VISIBLE);
            mnew_mobile_authenticate_layout.setVisibility(View.GONE);
            mmobile_authenticate_text.setTextColor(getResources().getColor(R.color.default_bluebg));
            mmobile_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch));
            mmobile_new_mobileverify_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
        } else if (modify_viewpage == new_mobilde_verify_view) {
            mmobile_authenticate_layout.setVisibility(View.GONE);
            mnew_mobile_authenticate_layout.setVisibility(View.VISIBLE);
            mmobile_authenticate_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            mmobile_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
            mmobile_new_mobileverify_text.setTextColor(getResources().getColor(R.color.default_bluebg));
            mnew_mobile_mobile_edit.setText("");
            mnew_mobile_captcha_edit.setText("");
            mnew_mobile_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mnew_mobile_captcha_btn.setEnabled(true);
            mnew_mobile_captcha_btn.setBackgroundResource(R.color.white);
            mnew_mobile_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mmobile_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mmobile_captcha_btn.setClickable(true);
            mmobile_captcha_btn.setBackgroundResource(R.color.white);
            mmobile_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mmobile_captcha_btn.setClickable(false);
            mmobile_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mmobile_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mmobile_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }
    class NewTimeCount extends CountDownTimer {
        public NewTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mnew_mobile_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mnew_mobile_captcha_btn.setClickable(true);
            mnew_mobile_captcha_btn.setBackgroundResource(R.color.white);
            mnew_mobile_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mnew_mobile_captcha_btn.setClickable(false);
            mnew_mobile_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mnew_mobile_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mnew_mobile_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (modify_viewpage == old_mobilde_verify_view) {
                this.finish();
                return true;
            } else {
                modify_viewpage = old_mobilde_verify_view;
                show_madify_mobile();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                if (modify_viewpage == old_mobilde_verify_view) {
                    mTime.start();
                    mmobile_captcha_edit.setEnabled(true);
                } else {
                    mNewTime.start();
                    mnew_mobile_captcha_edit.setEnabled(true);
                }

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
    private LinhuiAsyncHttpResponseHandler validate_captchaHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            modify_viewpage = new_mobilde_verify_view;
            show_madify_mobile();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler change_mobileHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            LoginManager.setMobile(mnew_mobile_mobile_edit.getText().toString());
            finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
}
