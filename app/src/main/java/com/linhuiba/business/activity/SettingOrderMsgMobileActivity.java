package com.linhuiba.business.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/28.
 */

public class SettingOrderMsgMobileActivity extends BaseMvpActivity {
    @InjectView(R.id.set_order_msg_captcha_btn)
    Button mset_order_msg_captcha_btn;
    @InjectView(R.id.set_order_msg_mobile_edit)
    EditText mset_order_msg_mobile_edit;
    @InjectView(R.id.set_order_msg_captcha_edit)
    EditText mset_order_msg_captcha_edit;
    @InjectView(R.id.set_order_msg_verify_btn)
    Button mset_order_msg_verify_btn;
    private TimeCount mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingordermsgmobile);
        ButterKnife.inject(this);
        initview();
    }
    @OnClick({
            R.id.set_order_msg_captcha_btn,
            R.id.set_order_msg_verify_btn
    })
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.set_order_msg_captcha_btn:
                if (TextUtils.getTrimmedLength(mset_order_msg_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mset_order_msg_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mset_order_msg_mobile_edit.getText().toString(),"3");
                break;
            case R.id.set_order_msg_verify_btn:

                break;
            default:
                break;
        }
    }
    private void initview() {
        mTime = new TimeCount(60000, 1000);

    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mset_order_msg_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mset_order_msg_captcha_btn.setClickable(true);
            mset_order_msg_captcha_btn.setBackgroundResource(R.color.white);
            mset_order_msg_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mset_order_msg_captcha_btn.setClickable(false);
            mset_order_msg_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mset_order_msg_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mset_order_msg_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                mTime.start();
                mset_order_msg_captcha_btn.setEnabled(true);
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

}
