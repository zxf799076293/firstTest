package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.WalletRechargeParticularsInfoModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/28.
 */

public class WalletRechargeParticularsInfoActivity extends BaseMvpActivity {
    @InjectView(R.id.consumption_status_text)
    TextView mconsumption_status_text;
    @InjectView(R.id.consumption_price_text)
    TextView mconsumption_price_text;
    @InjectView(R.id.consumption_orderid_text)
    TextView mconsumption_orderid_text;
    @InjectView(R.id.consumption_payment_way_text)
    TextView mconsumption_payment_way_text;
    @InjectView(R.id.consumption_commodity_description_text)
    TextView mconsumption_commodity_description_text;
    @InjectView(R.id.consumption_create_time_text)
    TextView mconsumption_create_time_text;

    @InjectView(R.id.wallet_tax_tv)
    TextView mWalletTaxTV;
    @InjectView(R.id.wallet_delivery_fee_tv)
    TextView mWalletDeliveryFeeTV;
    @InjectView(R.id.wallet_delivery_amount_tv)
    TextView mWalletDeliveryAmountTV;
    @InjectView(R.id.waller_order_number_ll)
    LinearLayout mWalletOrderNumberLL;
    @InjectView(R.id.wallet_delivery_ll)
    LinearLayout mWalletDeliveryLL;

    private WalletRechargeParticularsInfoModel walletRechargeParticularsInfoModel;
    private final int wallet_pay_int = 4;
    private final int transaction_type_pay = 2;
    private final int transaction_type_refund = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletrechargeparticulars_listitem_info);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_charge_particulars_info_title_text));
        TitleBarUtils.showBackImg(this,true);
        initview();
    }
    private void initview() {
        Intent rechargeinfo = getIntent();
        if (rechargeinfo.getExtras()!= null && rechargeinfo.getExtras().get("id") != null &&
                rechargeinfo.getExtras().getInt("id") > 0) {
            showProgressDialog();
            UserApi.gettransactionsinfo(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),TransactionsinfoHandler,
                    String.valueOf(rechargeinfo.getExtras().getInt("id")));
        }
    }
    private LinhuiAsyncHttpResponseHandler TransactionsinfoHandler = new LinhuiAsyncHttpResponseHandler(WalletRechargeParticularsInfoModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                walletRechargeParticularsInfoModel = (WalletRechargeParticularsInfoModel) data;
                if (walletRechargeParticularsInfoModel != null) {
                    if (walletRechargeParticularsInfoModel.getTransaction_type() == 4 ||
                            walletRechargeParticularsInfoModel.getTransaction_type() == 6) {
                        mWalletOrderNumberLL.setVisibility(View.GONE);
                        mWalletDeliveryLL.setVisibility(View.VISIBLE);
                        if (walletRechargeParticularsInfoModel.getInvoice_order() != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("tax") != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("tax").length() > 0) {
                            mWalletTaxTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                            Constants.getpricestring(walletRechargeParticularsInfoModel.getInvoice_order().get("tax"),1));
                        }
                        if (walletRechargeParticularsInfoModel.getInvoice_order() != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("delivery_fee") != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("delivery_fee").length() > 0) {
                            mWalletDeliveryFeeTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                    Constants.getpricestring(walletRechargeParticularsInfoModel.getInvoice_order().get("delivery_fee"),1));
                        }
                        if (walletRechargeParticularsInfoModel.getInvoice_order() != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("amount") != null &&
                                walletRechargeParticularsInfoModel.getInvoice_order().get("amount").length() > 0) {
                            mWalletDeliveryAmountTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                    Constants.getpricestring(walletRechargeParticularsInfoModel.getInvoice_order().get("amount"),1));
                        }
                    } else {
                        mWalletOrderNumberLL.setVisibility(View.VISIBLE);
                        mWalletDeliveryLL.setVisibility(View.GONE);
                        if (walletRechargeParticularsInfoModel.getPayment_mode() == wallet_pay_int) {
                            mconsumption_payment_way_text.setText(getResources().getString(R.string.orderconfirm_balance_payment_text));
                        }
                        if (walletRechargeParticularsInfoModel.getOut_trade_no() != null && walletRechargeParticularsInfoModel.getOut_trade_no().length() > 0) {
                            mconsumption_orderid_text.setText(walletRechargeParticularsInfoModel.getOut_trade_no());
                        }
                    }
                    if (walletRechargeParticularsInfoModel.getName() != null &&
                            walletRechargeParticularsInfoModel.getName().length() > 0) {
                            mconsumption_status_text.setText(walletRechargeParticularsInfoModel.getName());
                    } else {
                        mconsumption_status_text.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                    }
                    if (walletRechargeParticularsInfoModel.getAmount() != null) {
                        if (walletRechargeParticularsInfoModel.getAmount() < 0) {
                            mconsumption_price_text.setText(Constants.getdoublepricestring(walletRechargeParticularsInfoModel.getAmount(),0.01));
                        } else {
                            mconsumption_price_text.setText("+"+Constants.getdoublepricestring(walletRechargeParticularsInfoModel.getAmount(),0.01));
                        }
                    } else {
                        mconsumption_price_text.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                    }

                    if (walletRechargeParticularsInfoModel.getGoods() != null && walletRechargeParticularsInfoModel.getGoods().length() > 0) {
                        mconsumption_commodity_description_text.setText(walletRechargeParticularsInfoModel.getGoods());
                    }
                    if (walletRechargeParticularsInfoModel.getCreated_at() != null && walletRechargeParticularsInfoModel.getCreated_at().length() > 0) {
                        mconsumption_create_time_text.setText(walletRechargeParticularsInfoModel.getCreated_at());
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
}
