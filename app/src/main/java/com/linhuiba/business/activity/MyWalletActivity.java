package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/17.
 */

public class MyWalletActivity extends BaseMvpActivity {
    @InjectView(R.id.mywallet_recharge_btn)
    Button mmywallet_recharge_btn;
    @InjectView(R.id.wallet_balance_textview)
    TextView mwallet_balance_textview;
    @InjectView(R.id.wallet_wait_for_posting_textview)
    TextView mwallet_wait_for_posting_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        initview();
    }
    @Override
    protected void onResume() {
        if (!LoginManager.isLogin()) {
            this.finish();
        } else {
            showProgressDialog();
            UserApi.getwalletsinfo(MyAsyncHttpClient.MyAsyncHttpClient2(),WalletInfoHandler);
        }
        super.onResume();
    }
    private void initview() {
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_title_text));
        TitleBarUtils.showBackImg(this,true);
    }
    @OnClick({
            R.id.mywallet_recharge_btn,
            R.id.walletrechargeparticulars_layout,
            R.id.wallet_pay_setting_layout,
            R.id.wallet_recharge_consumption_layout,
            R.id.wallet_explain_layout
    })
    public void mywalletOnClick(View view) {
        switch (view.getId()) {
            case R.id.mywallet_recharge_btn:
                Intent RechargeIntent = new Intent(MyWalletActivity.this,WalletRechargeActivity.class);
                startActivity(RechargeIntent);
                break;
            case R.id.walletrechargeparticulars_layout:
                Intent paymentsetting_intent = new Intent(MyWalletActivity.this,WalletRechargeParticularsActivity.class);
                paymentsetting_intent.putExtra("type",1);
                startActivity(paymentsetting_intent);
                break;
            case R.id.wallet_pay_setting_layout:
                Intent walletrechargeparticulars_intent = new Intent(MyWalletActivity.this,WalletPaymentSettingActivity.class);
                startActivity(walletrechargeparticulars_intent);
                break;
            case R.id.wallet_recharge_consumption_layout:
                Intent consumption_intent = new Intent(MyWalletActivity.this,WalletRechargeParticularsActivity.class);
                consumption_intent.putExtra("type",2);
                startActivity(consumption_intent);
                break;
            case R.id.wallet_explain_layout:
                Intent aboutusintent = new Intent(MyWalletActivity.this, AboutUsActivity.class);
                aboutusintent.putExtra("type", Config.WALLET_PLAIN_WEB_INT);
                startActivity(aboutusintent);
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler WalletInfoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data instanceof JSONObject && data.toString().length() > 0 &&
                    JSONObject.parseObject(data.toString()) != null) {
                if (JSONObject.parseObject(data.toString()).get("balance") != null &&
                        JSONObject.parseObject(data.toString()).get("balance").toString().length() > 0 &&
                        Double.parseDouble(JSONObject.parseObject(data.toString()).get("balance").toString()) > 0) {
                    mwallet_balance_textview.setText(Constants.addComma(JSONObject.parseObject(data.toString()).get("balance").toString(),0.01));
                } else {
                    mwallet_balance_textview.setText("0");
                }
                if (JSONObject.parseObject(data.toString()).get("unconfirmed") != null &&
                        JSONObject.parseObject(data.toString()).get("unconfirmed").toString().length() > 0 &&
                        Double.parseDouble(JSONObject.parseObject(data.toString()).get("unconfirmed").toString()) > 0) {
                    mwallet_wait_for_posting_textview.setText(Constants.addComma(JSONObject.parseObject(data.toString()).get("unconfirmed").toString(),0.01));
                } else {
                    mwallet_wait_for_posting_textview.setText("0");
                }

            } else {
                MessageUtils.showToast(getResources().getString(R.string.review_error_text));
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

}
