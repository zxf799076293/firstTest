package com.linhuiba.business.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.linhuiba.business.R;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.MyCallBack;
import com.linhuiba.business.model.MyCallBackTest;
import com.linhuiba.business.model.WeiXinCallBack;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/3/31.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler,MyCallBack {
    private IWXAPI api;
    private int dellistitem;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dellistitem = -1;
        type=-1;
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq baseReq) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.order_pay);
        builder.setMessage("微信支付结果：" + String.valueOf(baseResp.errCode));
        builder.show();
        //baseResp.errCode = 0成功  -1失败  -2取消支付
        if (baseResp.errCode == -1 || baseResp.errCode == -2) {
            this.finish();
            Intent intent = new Intent();
            intent.setAction("CALL_BACK_Error");
            //发送 一个无序广播
            WXPayEntryActivity.this.sendBroadcast(intent);
        } else if (baseResp.errCode == 0) {
            this.finish();
            Intent intent = new Intent();
            intent.setAction("CALL_BACK_Success");
            //发送 一个无序广播
            WXPayEntryActivity.this.sendBroadcast(intent);
        }
    }

    @Override
    public void get_check(Boolean check, String price, String pay_orderlistsize, String subsidy_fee) {

    }

    @Override
    public void getdeletelistitem(int listitem, int order_item_id) {
    }
    public void Intentpayorder(int type) {
        MyCallBackTest test = new MyCallBackTest(type);
        test.Refresh((WeiXinCallBack) this);
    }
}
