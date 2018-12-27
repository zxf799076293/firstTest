package com.linhuiba.business.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.Badge_infoModel;
import com.linhuiba.business.mvppresenter.MyselfMvpPresenter;
import com.linhuiba.business.mvpview.MySelfMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;
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

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/7/11.
 */
public class BinDingWeixinActivity extends BaseMvpActivity implements MySelfMvpView {
    @InjectView(R.id.bindingweixin_remind_layout)
    RelativeLayout mbindingweixin_remind_layout;
    @InjectView(R.id.bindingweixin_btn)
    TextView mbindingweixin_btn;
    @InjectView(R.id.binding_qq_ll)
    TextView mBinDingQQTV;
    private IWXAPI WXapi;
    private String roadcast_action = "";//广播返回的 action
    private MyBroadcastReciver myBroadcastReciver;
    private Tencent mTencent;
    private IUiListener mIUiListener;
    private UnionInfo mUserInfo;
    private IUiListener mUserInfoListener;
    private MyselfMvpPresenter mMyselfMvpPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindingweixin);
        regist_receiver();
        initView();
    }
    private void initView() {
        mMyselfMvpPresenter = new MyselfMvpPresenter();
        mMyselfMvpPresenter.attachView(this);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_weixinname));
        TitleBarUtils.showBackImg(this,true);
        if (LoginManager.isWechat_bound()) {
            mbindingweixin_btn.setText(getResources().getString(R.string.myselfinfo_unbindingweixin_text));
        } else {
            mbindingweixin_btn.setText(getResources().getString(R.string.myselfinfo_bindingweixin_text));
        }
        if (LoginManager.isQq_bound()) {
            mBinDingQQTV.setText(getResources().getString(R.string.module_myselfinfo_unbinding_qq_str));
        } else {
            mBinDingQQTV.setText(getResources().getString(R.string.module_myselfinfo_binding_qq_str));
        }
        mTencent = Tencent.createInstance(com.linhuiba.linhuipublic.config.Config.TENGXUN_QQ_APPID,BinDingWeixinActivity.this.getContext());
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
                    mUserInfo = new UnionInfo(BinDingWeixinActivity.this, mTencent.getQQToken());
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
                        mMyselfMvpPresenter.bindQQ(jo.get("unionid").toString());
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
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReciver != null) {
            this.unregisterReceiver(myBroadcastReciver);
            myBroadcastReciver = null;
        }
        if (mMyselfMvpPresenter != null) {
            mMyselfMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.bindingweixin_remind_close,
            R.id.bindingweixin_btn,
            R.id.binding_qq_ll
    })
    public void onclick (View view) {
        switch (view.getId()) {
            case R.id.bindingweixin_remind_close:
                mbindingweixin_remind_layout.setVisibility(View.GONE);
                break;
            case R.id.bindingweixin_btn:
                if (mbindingweixin_btn.getText().toString().equals(getResources().getString(R.string.myselfinfo_bindingweixin_text)) && !LoginManager.isWechat_bound()) {
                    WXapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
                    WXapi.registerApp(Constants.APP_ID);
                    if (!WXapi.isWXAppInstalled()) {
                        MessageUtils.showToast(getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "linhuiba_business";
                    WXapi.sendReq(req);
                } else if (mbindingweixin_btn.getText().toString().equals(getResources().getString(R.string.myselfinfo_unbindingweixin_text)) && LoginManager.isWechat_bound()) {
                    showProgressDialog();
                    mMyselfMvpPresenter.unBindWechat();
                }
                break;
            case R.id.binding_qq_ll:
                if (mBinDingQQTV.getText().toString().equals(getResources().getString(R.string.module_myselfinfo_binding_qq_str)) && !LoginManager.isQq_bound()) {
                    mTencent.login(BinDingWeixinActivity.this, "all", mIUiListener);
                } else if (mBinDingQQTV.getText().toString().equals(getResources().getString(R.string.module_myselfinfo_unbinding_qq_str))
                        && LoginManager.isQq_bound()) {
                    showProgressDialog();
                    mMyselfMvpPresenter.unbindQQ();
                }
                break;
            default:
                break;
        }
    }
    private void regist_receiver() {
        myBroadcastReciver = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Binding_Weixin_Success");
        intentFilter.addAction("Binding_Weixin_Error");
        this.registerReceiver(myBroadcastReciver, intentFilter);
    }

    @Override
    public void onBadgeSuccess(Badge_infoModel badeInfoModel) {

    }

    @Override
    public void onBadgeFailure(boolean superresult, Throwable error) {
        hideProgressDialog();
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onProfileSuccess(LoginInfo loginInfos) {

    }

    @Override
    public void onBindWXSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.childaccount_changeadministrator_success_text));
        mbindingweixin_btn.setText(getResources().getString(R.string.myselfinfo_unbindingweixin_text));
        LoginManager.setWechat_bound(true);
    }

    @Override
    public void onUnBindWXSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.childaccount_unbinding_success_text));
        mbindingweixin_btn.setText(getResources().getString(R.string.myselfinfo_bindingweixin_text));
        LoginManager.setWechat_bound(false);
    }

    @Override
    public void onBindQQSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.childaccount_changeadministrator_success_text));
        LoginManager.setQq_bound(true);
        mBinDingQQTV.setText(getResources().getString(R.string.module_myselfinfo_unbinding_qq_str));
    }

    @Override
    public void onUnBindQQSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.childaccount_unbinding_success_text));
        mTencent.logout(this);
        LoginManager.setQq_bound(false);
        mBinDingQQTV.setText(getResources().getString(R.string.module_myselfinfo_binding_qq_str));
    }

    private class  MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            roadcast_action = intent.getAction();
            if(roadcast_action.equals("Binding_Weixin_Success")) {
                //授权完成界面
                String code = intent.getExtras().getString("code");
                showProgressDialog();
                mMyselfMvpPresenter.bindWechat(code);
            } else if (roadcast_action.equals("Binding_Weixin_Error")) {
                //授权失败界面
                MessageUtils.showToast(getResources().getString(R.string.login_weixin_login_error_text));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
