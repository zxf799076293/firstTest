package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.mvppresenter.ModifyUserInfoMvpPressenter;
import com.linhuiba.business.mvpview.ModifyUserInfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ModifyUserInfoActivity extends BaseMvpActivity implements ModifyUserInfoMvpView {
    @InjectView(R.id.modify_contact)
    EditText mContactET;
    @InjectView(R.id.contact_txt)
    TextView mContactTV;
    @InjectView(R.id.username_rl)
    RelativeLayout mUsetNameRemindRL;
    @InjectView(R.id.set_username_close_iv)
    ImageView mUsetNameCloseIV;
    private String mModifyStr;
    private ModifyUserInfoMvpPressenter mPressenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyuserinfo);
        initview();
    }
    private void initview() {
        ButterKnife.inject(this);
        mPressenter = new ModifyUserInfoMvpPressenter();
        mPressenter.attachView(this);
        Intent modify_userinfotype = getIntent();
        mModifyStr = modify_userinfotype.getStringExtra("modifytype_text");
        mContactTV.setText(mModifyStr);
        if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_mailbox)))) {
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.pw_modify_text) + mModifyStr);
            if (LoginManager.isLogin() && LoginManager.geteEmail() != null &&
                    LoginManager.geteEmail().length() > 0) {
                mContactET.setText(LoginManager.geteEmail());
            }
        } else if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_username)))) {
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.pw_modify_text) + mModifyStr);
            if (LoginManager.isLogin() && LoginManager.getName() != null &&
                    LoginManager.getName().length() > 0) {
                mContactET.setText(LoginManager.getName());
            }
        } else if (mModifyStr.equals(getResources().getString(R.string.myselfinfo_username_text))) {
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_setting_text) + mModifyStr);
            mUsetNameRemindRL.setVisibility(View.VISIBLE);
            mContactTV.setVisibility(View.GONE);
            addTextChangedListener(mContactET);
            mContactET.setHint(getResources().getString(R.string.myselfinfo_set_username_hint_str));
        }

        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.myselfinfo_save_pw), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                //closeBoard();
                if (TextUtils.isEmpty(mContactET.getText())) {
                    MessageUtils.showToast(ModifyUserInfoActivity.this, getResources().getString(R.string.pw_modify_ueserinfo_text));
                    return;
                }
                if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_mailbox)))) {
                    if (!CommonTool.isEmail(mContactET.getText().toString())) {
                        MessageUtils.showToast(ModifyUserInfoActivity.this, getResources().getString(R.string.applyforrelease_email_errortxt));
                        return;
                    }
                    if (LoginManager.isLogin()) {
                        mPressenter.modifyUserInfo(1,mContactET.getText().toString());
                    } else {
                        LoginManager.getInstance().clearLoginInfo();
                        ModifyUserInfoActivity.this.finish();
                    }
                    return;
                } else if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_username)))) {
                    if (LoginManager.isLogin()) {
                        mPressenter.modifyUserInfo(2,mContactET.getText().toString());
                    } else {
                        LoginManager.getInstance().clearLoginInfo();
                        ModifyUserInfoActivity.this.finish();
                    }
                    return;
                } else if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_username_text)))) {
                    if (LoginManager.isLogin()) {
                        if (mContactET.getText().toString().length() > 10) {
                            MessageUtils.showToast(getResources().getString(R.string.myselfinfo_set_username_error_hint_str));
                            return;
                        }
                        String oldStr = com.linhuiba.business.connector.Constants.
                                userNameStringFilter(mContactET.getText().toString());
                        if(!mContactET.getText().toString().
                                equals(oldStr)) {
                            MessageUtils.showToast(getResources().getString(R.string.myselfinfo_set_username_error_hint_str));
                            return;
                        }
                        mPressenter.setUserName(mContactET.getText().toString().trim());
                    } else {
                        LoginManager.getInstance().clearLoginInfo();
                        ModifyUserInfoActivity.this.finish();
                    }
                }

            }
        });
    }
    @OnClick({
            R.id.username_remind_ib,
            R.id.set_username_close_iv
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.username_remind_ib:
                mUsetNameRemindRL.setVisibility(View.GONE);
                break;
            case R.id.set_username_close_iv:
                mContactET.setText("");
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPressenter != null) {
            mPressenter.detachView();
        }
    }

    @Override
    public void onModifyUserInfoSuccess(String msg) {
        MessageUtils.showToast(ModifyUserInfoActivity.this, getResources().getString(R.string.pw_modify_success_text));
        if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_mailbox)))) {
            LoginManager.setEmail(mContactET.getText().toString());
        } else if (mModifyStr.equals((getResources().getString(R.string.myselfinfo_username)))) {
            LoginManager.setName(mContactET.getText().toString());
        }
        ModifyUserInfoActivity.this.finish();
    }

    @Override
    public void onModifyUserInfoFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onSetUserNameSuccess(String msg) {
        MessageUtils.showToast(ModifyUserInfoActivity.this, getResources().getString(R.string.fieldinfo_username_success_str));
        LoginManager.setUser_name(mContactET.getText().toString());
        ModifyUserInfoActivity.this.finish();
    }
    //账号的限制 onTextChanged
    private void addTextChangedListener(final EditText accountET) {
        accountET.addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                String oldStr = com.linhuiba.business.connector.Constants.
                        userNameStringFilter(accountET.getText().toString());
                if(!accountET.getText().toString().
                        equals(oldStr)) {
                    MessageUtils.showToast(getResources().getString(R.string.myselfinfo_set_username_error_hint_str));
                    accountET.setText(oldStr); //设置EditText的字符
                    accountET.setSelection(com.linhuiba.business.connector.Constants.
                            registerStringFilter(accountET.getText().toString()).length()); //因为删除了字符，要重写设置新的光标所在位置
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mUsetNameCloseIV.setVisibility(View.VISIBLE);
                } else {
                    mUsetNameCloseIV.setVisibility(View.GONE);
                }
                //获取光标开始的位置
                selectionStart = accountET.getSelectionStart();
                //获取光标结束的位置
                selectionEnd = accountET.getSelectionEnd();
                if (temp.length() > 10) {
                    MessageUtils.showToast(getResources().getString(R.string.myselfinfo_set_username_error_hint_str));
                    //删除部分字符串 为[x,y) 包含x位置不包含y
                    //也就是说删除 位置x到y-1
                }
            }
        });
    }
}
