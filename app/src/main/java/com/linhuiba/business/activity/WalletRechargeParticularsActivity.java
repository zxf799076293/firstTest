package com.linhuiba.business.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.WalletRechargeParticularsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.WalletRechargeParticularsModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/17.
 */

public class WalletRechargeParticularsActivity extends BaseMvpActivity implements LoadMoreListView.OnLoadMore,SwipeRefreshLayout.OnRefreshListener, com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack.CalendarClickCall{
    @InjectView(R.id.particulars_swipe_refresh)
    SwipeRefreshLayout mparticulars_swipe_refresh;
    @InjectView(R.id.particulars_loadmorelistview)
    LoadMoreListView mparticulars_loadmorelistview;
    @InjectView(R.id.lay_no_charge_data)
    RelativeLayout mlay_no_charge_data;
    private WalletRechargeParticularsAdapter walletRechargeParticularsAdapter;
    private ArrayList<WalletRechargeParticularsModel> mdatalist = new ArrayList<>();
    private int datapagesize;
    private int adapter_type;
    private final int RechangeDetail = 1;
    private final int BalanceConsumptionDetail = 2;
    public boolean buttom_show;
    private int delete_item_position;
    private String delete_item_id;
    private boolean loading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletrechargeparticulars);
        ButterKnife.inject(this);
        TitleBarUtils.showBackImg(this,true);
        initview();
        showProgressDialog();
        initdata();
    }
    private void initview() {
        mparticulars_swipe_refresh.setOnRefreshListener(this);
        mparticulars_loadmorelistview.setLoadMoreListen(this);
        mparticulars_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        Intent recharge_intent = getIntent();
        if (recharge_intent.getExtras() != null && recharge_intent.getExtras().get("type") != null &&
                recharge_intent.getExtras().getInt("type") > 0) {
            adapter_type = recharge_intent.getExtras().getInt("type");
        }
        if (adapter_type == RechangeDetail) {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_recharge_bill_text));
        } else if (adapter_type == BalanceConsumptionDetail) {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_consumption_text));
        }
    }
    private void initdata() {
        loading = true;
        datapagesize = 1;
        buttom_show = false;
        UserApi.gettransactions(MyAsyncHttpClient.MyAsyncHttpClient2(),TransactionsHandler,adapter_type,datapagesize);
    }
    @OnClick({
            R.id.lay_no_charge_data
    })
    public void nochargelayout_click(View view) {
        switch (view.getId()) {
            case R.id.lay_no_charge_data:
                showProgressDialog();
                initdata();
                break;
            default:
                break;
        }
    }
    @Override
    public void loadMore() {
        if (loading) {
            datapagesize = datapagesize + 1;
            UserApi.gettransactions(MyAsyncHttpClient.MyAsyncHttpClient2(),TransactionsMoreHandler,adapter_type,datapagesize);
        } else {
            mparticulars_loadmorelistview.onLoadComplete();
        }
    }

    @Override
    public void onRefresh() {
        initdata();
    }
    private LinhuiAsyncHttpResponseHandler TransactionsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data instanceof JSONObject && data.toString().length() > 0 &&
                   JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("data")!= null &&
                    JSONObject.parseObject(data.toString()).get("data").toString().length() > 0) {
                if (JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString()).size() > 0) {
                        mlay_no_charge_data.setVisibility(View.GONE);
                    mdatalist = (ArrayList<WalletRechargeParticularsModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString(),WalletRechargeParticularsModel.class);
                    walletRechargeParticularsAdapter = new WalletRechargeParticularsAdapter(WalletRechargeParticularsActivity.this,
                            WalletRechargeParticularsActivity.this,mdatalist,adapter_type);
                    mparticulars_loadmorelistview.setAdapter(walletRechargeParticularsAdapter);
                    if (mdatalist.size() < 10) {
                        buttom_show = true;
                        loading = false;
                    }
                } else {
                    mlay_no_charge_data.setVisibility(View.VISIBLE);
                }
            } else {
                mlay_no_charge_data.setVisibility(View.VISIBLE);
            }
            if (mparticulars_swipe_refresh.isShown()) {
                mparticulars_swipe_refresh.setRefreshing(false);
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
    private LinhuiAsyncHttpResponseHandler TransactionsMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null && data instanceof JSONObject && data.toString().length() > 0 &&
                    JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("data")!= null &&
                    JSONObject.parseObject(data.toString()).get("data").toString().length() > 0) {
                if (JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString()).size() > 0) {
                    ArrayList<WalletRechargeParticularsModel> mdatalist_temp = (ArrayList<WalletRechargeParticularsModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString(),WalletRechargeParticularsModel.class);
                    if((mdatalist_temp == null || mdatalist_temp.isEmpty())){
                        loading = false;
                        buttom_show = true;
                        datapagesize = datapagesize - 1;
                        return;
                    }
                    if (mdatalist_temp.size() < 10) {
                        buttom_show = true;
                        loading = false;
                    }
                    for( WalletRechargeParticularsModel walletRechargeParticularsModel : mdatalist_temp ){
                        mdatalist.add(walletRechargeParticularsModel);
                    }
                    walletRechargeParticularsAdapter.notifyDataSetChanged();
                } else {
                    datapagesize = datapagesize - 1;
                    buttom_show = true;
                    loading = false;
                }
            } else {
                datapagesize = datapagesize - 1;
                buttom_show = true;
                loading = false;
            }
            mparticulars_loadmorelistview.onLoadComplete();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            mparticulars_loadmorelistview.onLoadComplete();
            datapagesize = datapagesize - 1;
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        delete_item_position = position;
        delete_item_id = str;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.wallet_charge_particulars_dialog_prompt_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        UserApi.cancel_transactions(MyAsyncHttpClient.MyAsyncHttpClient2(),CancelTransactuionsHandler,delete_item_id,
                                WalletRechargeParticularsActivity.this);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }
    @Override
    public void CalendarClick(int position) {
        Intent recharge_info_intent = new Intent(WalletRechargeParticularsActivity.this,WalletRechargeParticularsInfoActivity.class);
        recharge_info_intent.putExtra("id",position);
        startActivity(recharge_info_intent);
    }
    public void intent_recharge_info(int id) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(id);
        test.CalendarClicklisten(this);
    }
    public void cacel_payment(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    private LinhuiAsyncHttpResponseHandler CancelTransactuionsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mdatalist.remove(delete_item_position);
            walletRechargeParticularsAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                showProgressDialog();
                initdata();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }}
