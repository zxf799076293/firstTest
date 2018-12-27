package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.util.TitleBarUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/17.
 */
public class WalletRechargeSuccessActivity extends BaseMvpActivity {
    /**
     * The Mwallet recharge success payway textview.
     */
    @InjectView(R.id.wallet_recharge_success_payway_textview)
    TextView mwallet_recharge_success_payway_textview;
    /**
     * The Mwallet recharge success price textview.
     */
    @InjectView(R.id.wallet_recharge_success_price_textview)
    TextView mwallet_recharge_success_price_textview;
    private final int offline_pay_int = 0;
    private final int weixin_pay_int = 6;
    private final int mIntAlipayPay= 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletrechargesuccess);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_recharge_btn_text));
        TitleBarUtils.showBackImg(this,true);
        initview();
    }

    private void initview() {
        Intent recharge_success_intent = getIntent();
        if (recharge_success_intent.getExtras() != null) {
            if (recharge_success_intent.getExtras().get("payment_price") != null &&
                    recharge_success_intent.getExtras().get("payment_price").toString().length() > 0) {
                mwallet_recharge_success_price_textview.setText(recharge_success_intent.getExtras().get("payment_price").toString());
            }
            if (recharge_success_intent.getExtras().get("payment_mode") != null &&
                    (recharge_success_intent.getExtras().getInt("payment_mode") > -1 )) {
                if (recharge_success_intent.getExtras().getInt("payment_mode") == offline_pay_int) {
                    mwallet_recharge_success_payway_textview.setText(getResources().getString(R.string.confirmorder_account_pay_checkprompt));
                } else if (recharge_success_intent.getExtras().getInt("payment_mode") == weixin_pay_int) {
                    mwallet_recharge_success_payway_textview.setText(getResources().getString(R.string.confirmorder_weixin_paie));
                } else if (recharge_success_intent.getExtras().getInt("payment_mode") == mIntAlipayPay) {
                    mwallet_recharge_success_payway_textview.setText(getResources().getString(R.string.confirmorder_alipay_paie));
                }
            }
        }
    }

    /**
     * Walletrechargesuccess.
     *
     * @param view the view
     */
    @OnClick({
            R.id.mywallet_recharge_success_btn
    })
    public void walletrechargesuccess (View view) {
        switch (view.getId()) {
            case R.id.mywallet_recharge_success_btn:
                this.finish();
                break;
            default:
                break;
        }
    }
}
