package com.linhuiba.business.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.PayResult;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/17.
 */

public class WalletRechargeActivity extends BaseMvpActivity {
    @InjectView(R.id.wallet_recharge_weixin_pay_checkbox)
    CheckBox mwallet_recharge_weixin_pay_checkbox;
    @InjectView(R.id.wallet_recharge_alipay_pay_checkbox)
    CheckBox mwallet_recharge_alipay_pay_checkbox;
    @InjectView(R.id.wallet_recharge_account_pay_checkbox)
    CheckBox mwallet_recharge_account_pay_checkbox;
    @InjectView(R.id.mywallet_recharge_price_edit)
    EditText mmywallet_recharge_price_edit;
    @InjectView(R.id.mywallet_recharge_price_btn)
    Button mmywallet_recharge_price_btn;
    private final int offline_pay_int = 0;
    private final int weixin_pay_int = 6;
    private final int alipay_pay_int = 10;
    private int payment_mode = 6;//0:线下支付 6：5w微信app支付 2:微信公众号支付 3:微信原生扫码支付
    private IWXAPI iwxapi;
    private String roadcast_action ="";
    private boolean wxpay_boolean;
    private MyBroadcastReciver myBroadcastReciver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_recharge);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_recharge_btn_text));
        TitleBarUtils.showBackImg(this,true);
        initview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (roadcast_action.length() == 0 && wxpay_boolean == true) {
            MessageUtils.showToast(getResources().getString(R.string.wallet_recharge_price_error_text));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReciver != null) {
            this.unregisterReceiver(myBroadcastReciver);
            myBroadcastReciver = null;
        }
    }

    private void initview() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        iwxapi.registerApp(Constants.APP_ID);
        regist_receiver();
        mwallet_recharge_account_pay_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mwallet_recharge_account_pay_checkbox.isChecked()) {
                    mwallet_recharge_weixin_pay_checkbox.setChecked(false);
                    mwallet_recharge_alipay_pay_checkbox.setChecked(false);
                    payment_mode = offline_pay_int;
                } else {
                    mwallet_recharge_account_pay_checkbox.setChecked(true);
                    payment_mode = offline_pay_int;
                }
            }
        });
        mwallet_recharge_weixin_pay_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mwallet_recharge_weixin_pay_checkbox.isChecked()) {
                    mwallet_recharge_alipay_pay_checkbox.setChecked(false);
                    mwallet_recharge_account_pay_checkbox.setChecked(false);
                    payment_mode = weixin_pay_int;
                } else {
                    mwallet_recharge_weixin_pay_checkbox.setChecked(true);
                    payment_mode = weixin_pay_int;
                }
            }
        });
        mwallet_recharge_alipay_pay_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mwallet_recharge_alipay_pay_checkbox.isChecked()) {
                    mwallet_recharge_weixin_pay_checkbox.setChecked(false);
                    mwallet_recharge_account_pay_checkbox.setChecked(false);
                    payment_mode = alipay_pay_int;
                } else {
                    mwallet_recharge_alipay_pay_checkbox.setChecked(true);
                    payment_mode = alipay_pay_int;
                }
            }
        });
        mmywallet_recharge_price_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment_mode == weixin_pay_int) {
                    if (!iwxapi.isWXAppInstalled()) {
                        MessageUtils.showToast(getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                }
                if (mmywallet_recharge_price_edit.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_txt));
                    return;
                }
                if (Double.parseDouble(mmywallet_recharge_price_edit.getText().toString()) == 0 ||
                        Double.parseDouble(mmywallet_recharge_price_edit.getText().toString()) < 0) {
                    MessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_correct_txt));
                    return;
                }
                if (Double.parseDouble(mmywallet_recharge_price_edit.getText().toString().trim()) > 99999999) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                    return;
                }
                showProgressDialog();
                UserApi.wallet_recharge(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),WalletRechargeHandler,
                        Constants.getorderdoublepricestring(Double.parseDouble(mmywallet_recharge_price_edit.getText().toString().trim()),100),payment_mode,"");
            }
        });
        mmywallet_recharge_price_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mmywallet_recharge_price_edit.setText(s);
                        mmywallet_recharge_price_edit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mmywallet_recharge_price_edit.setText(s);
                    mmywallet_recharge_price_edit.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mmywallet_recharge_price_edit.setText(s.subSequence(0, 1));
                        mmywallet_recharge_price_edit.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });
    }
    @OnClick({
            R.id.mywallet_recharge_price_btn,
    })
    public void walletrecharge(View view) {
        switch (view.getId()) {
            case R.id.mywallet_recharge_price_btn:

                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler WalletRechargeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, final Object data) {
            hideProgressDialog();
            if (payment_mode == offline_pay_int) {
                com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(data.toString());
                WalletRechargeActivity.this.finish();
                Intent AccountpayIntent = new Intent(WalletRechargeActivity.this,OrderConfirmUploadVoucherActivity.class);
                AccountpayIntent.putExtra("remittance",2);
                if (jsonobject.get("transaction_id") != null) {
                    AccountpayIntent.putExtra("order_id", jsonobject.getString("transaction_id"));
                }
                if (jsonobject.get("out_trade_no") != null) {
                    AccountpayIntent.putExtra("order_num", jsonobject.getString("out_trade_no"));
                }
                AccountpayIntent.putExtra("payment_price",mmywallet_recharge_price_edit.getText().toString());
                startActivity(AccountpayIntent);
            } else if (payment_mode == weixin_pay_int) {
                com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(data.toString());
                PayReq req = new PayReq();
                req.appId = jsonobject.getString("appid");
                req.partnerId = jsonobject.getString("partnerid");
                req.prepayId = jsonobject.getString("prepayid");
                req.nonceStr = jsonobject.getString("noncestr");
                req.timeStamp = jsonobject.getString("timestamp");
                req.packageValue = jsonobject.getString("package");
                req.sign = jsonobject.getString("sign");
                iwxapi.sendReq(req);
            } else if (payment_mode == alipay_pay_int) {
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(WalletRechargeActivity.this);
                        // 调用支付接口，获取支付结果
                        try {
                            Map params = JSON.parseObject(URLDecoder.decode(data.toString(), "UTF-8").toString());
                            String orderParam = Constants.buildOrderParam(params);
                            Map<String, String> result = alipay.payV2(orderParam, true);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
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
    private void regist_receiver() {
        myBroadcastReciver = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CALL_BACK_Success");
        intentFilter.addAction("CALL_BACK_Error");
        this.registerReceiver(myBroadcastReciver, intentFilter);
    }
    private class  MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            roadcast_action = intent.getAction();
            if(roadcast_action.equals("CALL_BACK_Success")) {
                WalletRechargeActivity.this.finish();
                Intent rechargesuccessIntent = new Intent(WalletRechargeActivity.this,WalletRechargeSuccessActivity.class);
                rechargesuccessIntent.putExtra("payment_mode",payment_mode);
                rechargesuccessIntent.putExtra("payment_price",mmywallet_recharge_price_edit.getText().toString());
                startActivityForResult(rechargesuccessIntent,1);
            } else if (roadcast_action.equals("CALL_BACK_Error")) {

            }
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    String a = msg.obj.toString();
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (payResult.getMemo() != null && payResult.getMemo().length() > 0) {
                            MessageUtils.showToast(payResult.getMemo());
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.order_pay_success_message));
                        }
                        WalletRechargeActivity.this.finish();
                        Intent rechargesuccessIntent = new Intent(WalletRechargeActivity.this,WalletRechargeSuccessActivity.class);
                        rechargesuccessIntent.putExtra("payment_mode",payment_mode);
                        rechargesuccessIntent.putExtra("payment_price",mmywallet_recharge_price_edit.getText().toString());
                        startActivityForResult(rechargesuccessIntent,1);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            if (payResult.getMemo() != null && payResult.getMemo().length() > 0) {
                                MessageUtils.showToast(payResult.getMemo());
                            } else {
                                MessageUtils.showToast(getResources().getString(R.string.order_pay_error_message));
                            }

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if (payResult.getMemo() != null && payResult.getMemo().length() > 0) {
                                MessageUtils.showToast(payResult.getMemo());
                            } else {
                                MessageUtils.showToast(getResources().getString(R.string.order_pay_error_message));
                            }
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
}
