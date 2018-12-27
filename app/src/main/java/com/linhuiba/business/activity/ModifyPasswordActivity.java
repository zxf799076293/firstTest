package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ModifyPasswordActivity  extends BaseMvpActivity {
    @InjectView(R.id.old_pw)
    EditText edit_old_pw;
    @InjectView(R.id.new_pw)
    EditText edit_new_pw;
    @InjectView(R.id.repeat_new_pw)
    EditText edit_repeat_new_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);
        initView();
    }
    private void initView() {
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_modify_pw));
        TitleBarUtils.showBackImg(this, true);
        TextView actionbtn = (TextView)ModifyPasswordActivity.this.findViewById(R.id.action_text_top);
        actionbtn.setTextColor(getResources().getColor(R.color.home_Popular_text_color));
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.myselfinfo_save_pw), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存修改后的密码操作
                if (LoginManager.isLogin()) {
                    if (TextUtils.isEmpty(edit_old_pw.getText())) {
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.pw_oldtext));
                        return;
                    }
                    if (edit_old_pw.getText().toString().length() < 6) {
                        edit_old_pw.setText("");
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.txt_password_lenthhint));
                    }
                    if (TextUtils.isEmpty(edit_new_pw.getText()) ) {
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.pw_new_text));
                        return;
                    }
                    if (edit_new_pw.getText().toString().length() < 6) {
                        edit_new_pw.setText("");
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.txt_password_lenthhint));
                    }
                    if (TextUtils.isEmpty(edit_repeat_new_pw.getText()) ) {
                        MessageUtils.showToast(ModifyPasswordActivity.this,  getResources().getString(R.string.pw_repeat_new_text));
                        return;
                    }
                    if (edit_repeat_new_pw.getText().toString().length() < 6) {
                        edit_repeat_new_pw.setText("");
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.txt_password_lenthhint));
                    }
                    if (!(edit_repeat_new_pw.getText().toString().equals(edit_new_pw.getText().toString()))) {
                        MessageUtils.showToast(ModifyPasswordActivity.this, getResources().getString(R.string.pw_repeat_error_text));
                        edit_new_pw.setText("");
                        edit_repeat_new_pw.setText("");
                        return;
                    }
                    showProgressDialog();
                    UserApi.editUserInfo_pw(MyAsyncHttpClient.MyAsyncHttpClient(), editUserInfoHandler, edit_old_pw.getText().toString(), edit_new_pw.getText().toString(),edit_repeat_new_pw.getText().toString());
                } else {
                    LoginManager.getInstance().clearLoginInfo();
                    ModifyPasswordActivity.this.finish();
                }

            }
        });
    }
    @OnClick({
            R.id.modify_password_find_tv
    })
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.modify_password_find_tv:
                Intent findpwIntent = new Intent(this,FindPasswordActivity.class);
                findpwIntent.putExtra("is_modity_pw",1);
                startActivity(findpwIntent);
                break;
            default:
                break;
        }

    }
    private LinhuiAsyncHttpResponseHandler editUserInfoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(ModifyPasswordActivity.this,getResources().getString(R.string.pw_modify_success_text));
            ModifyPasswordActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
}
