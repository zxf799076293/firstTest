package com.linhuiba.business.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/4/13.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        String a = "s";
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                WXEntryActivity.this.finish();
                Intent intent = new Intent();
//                UserApi.third_party_wechat_login(MyAsyncHttpClient.MyAsyncHttpClient(),bind_wechatHandler,((SendAuth.Resp) baseResp).code,0,0);

                intent.putExtra("code",((SendAuth.Resp) baseResp).code);
                intent.setAction("Binding_Weixin_Success");
                //发送 一个无序广播
                WXEntryActivity.this.sendBroadcast(intent);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                WXEntryActivity.this.finish();
                Intent intent_cancel = new Intent();
                intent_cancel.setAction("Binding_Weixin_Error");
                //发送 一个无序广播
                WXEntryActivity.this.sendBroadcast(intent_cancel);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                WXEntryActivity.this.finish();
                Intent intent_refuse = new Intent();
                intent_refuse.setAction("Binding_Weixin_Error");
                //发送 一个无序广播
                WXEntryActivity.this.sendBroadcast(intent_refuse);
                finish();
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler bind_wechatHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            MessageUtils.showToast(getResources().getString(R.string.childaccount_changeadministrator_success_text));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
        }
    };
}
