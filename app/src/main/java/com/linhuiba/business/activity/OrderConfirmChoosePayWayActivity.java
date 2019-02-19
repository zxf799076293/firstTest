package com.linhuiba.business.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.fingerprintrecognition.FingerprintCore;
import com.linhuiba.business.activity.fingerprintrecognition.KeyguardLockScreenManager;
import com.linhuiba.business.adapter.OrderConfirmSuccessReviewOrderAdapter;
import com.linhuiba.business.adapter.OrderExpandableListAdapter;
import com.linhuiba.business.adapter.WalletApplyPasswordAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldmodel.WalletFingerprintPayModel;
import com.linhuiba.business.model.OrderInfoModel;
import com.linhuiba.business.model.PayResult;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyLoadMoreExpandablelistView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/24.
 */

public class OrderConfirmChoosePayWayActivity extends BaseMvpActivity {
    @InjectView(R.id.order_account_pay_checkbox)
    CheckBox morder_account_pay_checkbox;
    @InjectView(R.id.order_account_pay_layout)
    RelativeLayout morder_account_pay_layout;
    @InjectView(R.id.weixin_pay_checkbox)
    CheckBox mweixin_pay_checkbox;
    @InjectView(R.id.order_balance_payment_checkbox)
    CheckBox morder_balance_payment_checkbox;
    @InjectView(R.id.order_balance_text)
    TextView morder_balance_text;
    @InjectView(R.id.order_balance_payment_layout)
    RelativeLayout morder_balance_payment_layout;
    @InjectView(R.id.alipay_pay_checkbox)
    CheckBox malipay_pay_checkbox;
    @InjectView(R.id.orderconfirmchoosepayment_alllayout)
    LinearLayout mInvoiceSuccessLL;

    private int pay_way_type = 6;
    private String order_id;
    private String order_num;
    private Double order_price;
    private IWXAPI api;
    private boolean wxpay_boolean = false;
    private String roadcast_action = "";
    private DisplayMetrics metric;
    private Dialog newdialog;
    private TextView[] tvList = new TextView[6];
    private int currentIndex = -1;
    private String strConfirmPassword = "";
    private String[] listConfirmPassword = new String[6];
    private WalletApplyPasswordAdapter walletApplyPasswordAdapter;
    private ArrayList<HashMap<String,String>> passwordlist= new ArrayList<>();
    private LinearLayout mwallet_fingerprint_pay_layout;
    private LinearLayout mwallet_pw_pay_layout;

    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;
    private Dialog fingerprint_dialog;
    private TextView mwallet_dialog_textview;
    private double wallet_parce;
    private String fingerprint_pw = "";
    private boolean fingerprint_pw_modify;//指纹支付
    private PopupWindow pw;
    private final int offline_pay_int = 0;
    private final int weixin_pay_int = 6;
    private final int wallet_pay_int = 4;
    private final int alipay_pay_int = 10;
    private MyBroadcastReciver myBroadcastReciver;
    private int payment_option;//下单结算类型
    private final int payment_option_order = 1;//普通场地下单结算
    private final int payment_option_invoice = 2;//开发票结算
    private final int payment_option_group_order = 3;//拼团下单结算
    private MyLoadMoreExpandablelistView mconfirmorder_success_review_order_listview;
    private LinearLayout mconfirmorder_success_review_order_layout;
    private List<Map<String,Object>> mData  = new  ArrayList();
    private List<List<OrderInfoModel>> OrderChildList = new ArrayList();
    private OrderConfirmSuccessReviewOrderAdapter orderExpandableListAdapter;
    private Call mOrderReviewCall;
    private int mGroupId;
    private boolean isResourcePaidSuccess;//除拼团普通场地支付成功的标志
    private boolean isInvoicePaidSuccess;//开发票支付成功的标志
    private int mInvoiceType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirmchoosepaymay);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.confirmorder_pay_way));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment_option == 1) {
                    Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);
                    bundle_ordertype1.putInt("showdialog", 1);
                    orderpayintent.putExtras(bundle_ordertype1);
                    orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(orderpayintent);
                } else if (payment_option == payment_option_group_order) {
                    Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, GroupBookingMainActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);
                    bundle_ordertype1.putInt("showdialog", 1);
                    orderpayintent.putExtras(bundle_ordertype1);
                    startActivity(orderpayintent);
                }
                OrderConfirmChoosePayWayActivity.this.finish();
            }
        });
        initview();
        regist_receiver();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isResourcePaidSuccess) {
            MobclickAgent.onPageStart(getResources().getString(R.string.resource_paid_success_activity_name_str));
        } else if (isInvoicePaidSuccess) {
            MobclickAgent.onPageStart(getResources().getString(R.string.invoice_paid_success_activity_name_str));
        }
        MobclickAgent.onResume(this);
        if (roadcast_action.length() == 0 && wxpay_boolean == true) {
            if (payment_option == payment_option_order) {
                Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
                Bundle bundle_ordertype1 = new Bundle();
                bundle_ordertype1.putInt("order_type", 1);
                bundle_ordertype1.putInt("showdialog", 1);
                orderpayintent.putExtras(bundle_ordertype1);
                orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(orderpayintent);
                OrderConfirmChoosePayWayActivity.this.finish();
            } else if (payment_option == payment_option_group_order) {
                Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, GroupBookingMainActivity.class);
                Bundle bundle_ordertype1 = new Bundle();
                bundle_ordertype1.putInt("order_type", 1);
                bundle_ordertype1.putInt("showdialog", 1);
                orderpayintent.putExtras(bundle_ordertype1);
                startActivity(orderpayintent);
                OrderConfirmChoosePayWayActivity.this.finish();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isResourcePaidSuccess) {
            MobclickAgent.onPageEnd(getResources().getString(R.string.resource_paid_success_activity_name_str));
        } else if (isInvoicePaidSuccess) {
            MobclickAgent.onPageEnd(getResources().getString(R.string.invoice_paid_success_activity_name_str));
        }
        MobclickAgent.onPause(this);
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
        Intent createorder_intent = getIntent();
        if (createorder_intent.getExtras() != null && createorder_intent.getExtras().get("payment_option") != null &&
                createorder_intent.getExtras().getInt("payment_option") > 0) {
            payment_option = createorder_intent.getExtras().getInt("payment_option");
        }
        if (createorder_intent.getExtras() != null && createorder_intent.getExtras().get("invoice_type") != null) {
            mInvoiceType = createorder_intent.getExtras().getInt("invoice_type");
        }
        if (mInvoiceType == 1 &&
                payment_option == payment_option_invoice) {//代付开收据
            mInvoiceSuccessLL.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    show_paysuccess_PopupWindow();
                }
            }, 500);

            return;
        }
        if ((payment_option == payment_option_order ||
                payment_option == payment_option_group_order) &&
                createorder_intent.getExtras().get("order_id") != null &&
                createorder_intent.getExtras().get("order_num") != null &&
                createorder_intent.getExtras().get("order_price") != null &&
                createorder_intent.getExtras().getString("order_id").length() > 0 &&
                createorder_intent.getExtras().getString("order_num").length() > 0 &&
                createorder_intent.getExtras().get("order_price").toString().length() > 0) {
            order_id = createorder_intent.getExtras().getString("order_id");
            order_num = createorder_intent.getExtras().getString("order_num");
            order_price = Double.parseDouble(createorder_intent.getExtras().get("order_price").toString());
            if (payment_option == payment_option_group_order) {
                if (createorder_intent.getExtras().get("group_id") != null) {
                    mGroupId = createorder_intent.getExtras().getInt("group_id");
                }
                morder_account_pay_layout.setVisibility(View.GONE);
            } else {
                morder_account_pay_layout.setVisibility(View.VISIBLE);
            }
        } else if (payment_option == payment_option_invoice && createorder_intent.getExtras().get("order_id") != null &&
                createorder_intent.getExtras().get("order_price") != null &&
                createorder_intent.getExtras().getString("order_id").length() > 0 &&
                createorder_intent.getExtras().get("order_price").toString().length() > 0){
            morder_account_pay_layout.setVisibility(View.GONE);
            order_id = createorder_intent.getExtras().getString("order_id");
            order_price = Double.parseDouble(createorder_intent.getExtras().get("order_price").toString());
        }
        if (LoginManager.getInstance().getWallet_stauts() == 1) {
            morder_balance_payment_layout.setVisibility(View.VISIBLE);
            morder_balance_payment_checkbox.setEnabled(false);
            UserApi.getwalletsinfo(MyAsyncHttpClient.MyAsyncHttpClient2(),WalletInfoHandler);
        } else {
            morder_balance_payment_layout.setVisibility(View.GONE);
        }
        metric = new DisplayMetrics();
        OrderConfirmChoosePayWayActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
    }
    @OnClick ({
            R.id.order_account_pay_checkbox,
            R.id.weixin_pay_checkbox,
            R.id.order_balance_payment_checkbox,
            R.id.orderconfirm_pay_btn,
            R.id.alipay_pay_checkbox
    })
    public void choosepayway_click(View view) {
        switch (view.getId()) {
            case R.id.order_account_pay_checkbox:
                if (morder_account_pay_checkbox.isChecked()) {
                    morder_balance_payment_checkbox.setChecked(false);
                    mweixin_pay_checkbox.setChecked(false);
                    malipay_pay_checkbox.setChecked(false);
                    pay_way_type = offline_pay_int;
                } else {
                    morder_account_pay_checkbox.setChecked(true);
                    pay_way_type = offline_pay_int;
                }
                break;
            case R.id.weixin_pay_checkbox:
                if (mweixin_pay_checkbox.isChecked()) {
                    morder_account_pay_checkbox.setChecked(false);
                    morder_balance_payment_checkbox.setChecked(false);
                    malipay_pay_checkbox.setChecked(false);
                    pay_way_type = weixin_pay_int;
                } else {
                    mweixin_pay_checkbox.setChecked(true);
                    pay_way_type = weixin_pay_int;
                }
                break;
            case R.id.order_balance_payment_checkbox:
                if (morder_balance_payment_checkbox.isChecked()) {
                    morder_account_pay_checkbox.setChecked(false);
                    mweixin_pay_checkbox.setChecked(false);
                    malipay_pay_checkbox.setChecked(false);
                    pay_way_type = wallet_pay_int;
                } else {
                    morder_balance_payment_checkbox.setChecked(true);
                    pay_way_type = wallet_pay_int;
                }
                break;
            case R.id.orderconfirm_pay_btn:
                if (pay_way_type == -1) {
                    MessageUtils.showToast(getResources().getString(R.string.orderconfirm_choose_payway_remind_text));
                    return;
                } else if (pay_way_type == weixin_pay_int) {
                    if (!api.isWXAppInstalled()) {
                        MessageUtils.showToast(getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                } else if (pay_way_type == wallet_pay_int) {
                    if (order_price >wallet_parce*100 ) {
                        MessageUtils.showToast(getResources().getString(R.string.wallet_mywallet_lack_of_balance_text));
                        return;
                    }
                    show_walletpay_doalog(false);
                    return;
                }
                if (pay_way_type != -1 && pay_way_type != wallet_pay_int) {
                    showProgressDialog();
                    if (payment_option == payment_option_order ||payment_option == payment_option_group_order) {
                        FieldApi.payorder(MyAsyncHttpClient.MyAsyncHttpClient4(), payOrderHandler,order_id,pay_way_type,-1);
                    } else if (payment_option == payment_option_invoice) {
                        FieldApi.pay_invoice_order(MyAsyncHttpClient.MyAsyncHttpClient3(), payOrderHandler,order_id,pay_way_type,-1);
                    }
                }
                break;
            case R.id.alipay_pay_checkbox:
                if (malipay_pay_checkbox.isChecked()) {
                    morder_account_pay_checkbox.setChecked(false);
                    morder_balance_payment_checkbox.setChecked(false);
                    mweixin_pay_checkbox.setChecked(false);
                    pay_way_type = alipay_pay_int;
                } else {
                    malipay_pay_checkbox.setChecked(true);
                    pay_way_type = alipay_pay_int;
                }
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler payOrderHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, final Object data) {
            hideProgressDialog();
            if (pay_way_type == offline_pay_int) {
                Intent AccountpayIntent = new Intent(OrderConfirmChoosePayWayActivity.this,OrderConfirmUploadVoucherActivity.class);
                AccountpayIntent.putExtra("remittance",1);
                AccountpayIntent.putExtra("order_id", order_id);
                AccountpayIntent.putExtra("order_num", order_num);
                if (payment_option == payment_option_group_order) {
                    AccountpayIntent.putExtra("isGroupOrder", 1);
                }
                startActivityForResult(AccountpayIntent,2);
            } else if (pay_way_type == weixin_pay_int) {
                if (data != null && data instanceof JSONObject && JSONObject.parseObject(data.toString()) != null) {
                    com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(data.toString());
                    PayReq req = new PayReq();
                    req.appId = jsonobject.getString("appid");
                    req.partnerId = jsonobject.getString("partnerid");
                    req.prepayId = jsonobject.getString("prepayid");
                    req.nonceStr = jsonobject.getString("noncestr");
                    req.timeStamp = jsonobject.getString("timestamp");
                    req.packageValue = jsonobject.getString("package");
                    req.sign = jsonobject.getString("sign");
                    api.sendReq(req);
                    wxpay_boolean = true;
                }
            } else if (pay_way_type == wallet_pay_int) {
                if (fingerprint_pw_modify == true) {
                    if (Constants.is_open_fingerprint()) {
                        if (LoginManager.getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
                            ArrayList<WalletFingerprintPayModel> walletFingerprintPaydatalist = (ArrayList<WalletFingerprintPayModel>) JSONArray.parseArray(LoginManager.getInstance().getFingerprint_validation(),WalletFingerprintPayModel.class);
                            for (int i = 0; i < walletFingerprintPaydatalist.size(); i++) {
                                if (walletFingerprintPaydatalist.get(i).getUid().equals(LoginManager.getUid())) {
                                    walletFingerprintPaydatalist.get(i).setWallet_pw(strConfirmPassword);
                                    break;
                                }
                            }
                            LoginManager.getInstance().setFingerprint_validation(JSON.toJSONString(walletFingerprintPaydatalist, true));
                        }
                    }

                }
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
                if (payment_option == payment_option_group_order) {
                    Intent mGroupSuccessIntent = new Intent(OrderConfirmChoosePayWayActivity.this,
                            GroupBookingPaidSuccessActivity.class);
                    mGroupSuccessIntent.putExtra("id",order_id);
                    mGroupSuccessIntent.putExtra("group_id",mGroupId);
                    startActivity(mGroupSuccessIntent);
                    finish();
                } else {
                    show_paysuccess_PopupWindow();
                }
            } else if (pay_way_type == alipay_pay_int) {
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(OrderConfirmChoosePayWayActivity.this);
                        // 调用支付接口，获取支付结果
                        try {
                            Map  params = JSON.parseObject(URLDecoder.decode(data.toString(), "UTF-8").toString());
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
            if (strConfirmPassword.length() == 6) {
                currentIndex = -1;
                strConfirmPassword = "";
                for (int i = 0; i < 6; i++) {
                    listConfirmPassword[i] = "";
                    tvList[i].setText("");
                    tvList[i].setVisibility(View.GONE);
                }
            } else if (fingerprint_pw.length() == 6) {
                if (isNoResources(error,-9)) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            fingerprint_pw_modify = true;
                            MessageUtils.showDialog(OrderConfirmChoosePayWayActivity.this, "",
                                    getResources().getString(R.string.wallet_fingerprint_payment_error_text),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            show_walletpay_doalog(true);
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                        }
                    },500);
                    return;
                }
            }
            if (isNoResources(error,-10)) {
                if (newdialog != null && newdialog.isShowing()) {
                    newdialog.dismiss();
                }
                MessageUtils.showDialog(OrderConfirmChoosePayWayActivity.this,"",getResources().getString(R.string.wallet_fingerprint_payment_error_three_text),
                        R.string.pw_find,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent find_wallet_pw = new Intent(OrderConfirmChoosePayWayActivity.this,WalletApplyActivity.class);
                                find_wallet_pw.putExtra("operation_type",2);
                                startActivity(find_wallet_pw);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                return;
            }

            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    //为了判断微信是否支付成功加的广播
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
                //支付完成的界面
                if (payment_option == payment_option_group_order) {
                    Intent mGroupSuccessIntent = new Intent(OrderConfirmChoosePayWayActivity.this,
                            GroupBookingPaidSuccessActivity.class);
                    mGroupSuccessIntent.putExtra("id",order_id);
                    mGroupSuccessIntent.putExtra("group_id",mGroupId);
                    startActivity(mGroupSuccessIntent);
                    finish();
                } else {
                    show_paysuccess_PopupWindow();
                }
            } else if (roadcast_action.equals("CALL_BACK_Error")) {
                if (payment_option == payment_option_order) {
                    Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);//订单类型：待支付
                    bundle_ordertype1.putInt("showdialog", 1);
                    orderpayintent.putExtras(bundle_ordertype1);
                    orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(orderpayintent);
                    OrderConfirmChoosePayWayActivity.this.finish();
                } else if (payment_option == payment_option_group_order) {
                    Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, GroupBookingMainActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);//订单类型：待支付
                    bundle_ordertype1.putInt("showdialog", 1);
                    orderpayintent.putExtras(bundle_ordertype1);
                    startActivity(orderpayintent);
                    OrderConfirmChoosePayWayActivity.this.finish();
                }
            }
        }
    }
    private void show_walletpay_doalog(boolean pw_type) {
        LayoutInflater factory = LayoutInflater.from(OrderConfirmChoosePayWayActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_wallet_pay_dialog, null);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        mwallet_fingerprint_pay_layout = (LinearLayout)textEntryView.findViewById(R.id.wallet_fingerprint_pay_layout);
        mwallet_pw_pay_layout = (LinearLayout)textEntryView.findViewById(R.id.wallet_pw_pay_layout);
        TextView mwallet_fingerprint_pay_user_pw_text = (TextView) textEntryView.findViewById(R.id.wallet_fingerprint_pay_user_pw_text);
        LinearLayout mwallet_pw_pay_pasword_all_layout = (LinearLayout)textEntryView.findViewById(R.id.wallet_pw_pay_pasword_all_layout);
        GridView gridView = (GridView) textEntryView.findViewById(R.id.wallet_pw_pay_gridview);
        ImageButton mwallet_pw_pay_close_btn = (ImageButton)textEntryView.findViewById(R.id.wallet_pw_pay_close_btn);
        TextView mwallet_pw_pay_forgot_password_btn = (TextView)textEntryView.findViewById(R.id.wallet_pw_pay_forgot_password_btn);
        ImageButton mwallet_fingerprint_pay_close_btn = (ImageButton)textEntryView.findViewById(R.id.wallet_fingerprint_pay_close_btn);
        TextView mwallet_fingerprint_price_text = (TextView)textEntryView.findViewById(R.id.wallet_fingerprint_price_text);
        TextView mwallet_pw_price_text = (TextView)textEntryView.findViewById(R.id.wallet_pw_price_text);
        Button mwallet_fingerprint_pay_button = (Button)textEntryView.findViewById(R.id.wallet_fingerprint_pay_button);
        for (int i = 0; i < tvList.length; i++) {
            int id = getResources().getIdentifier("choosepayway_pw_text" + i, "id", getPackageName());
            TextView textView = (TextView) textEntryView.findViewById(id);
            tvList[i]= (textView);
        }
        if (passwordlist != null) {
            passwordlist.clear();
        }
        currentIndex = -1;
        strConfirmPassword = "";
        fingerprint_pw = "";
        for (int i = 1; i < 13; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 12) {
                map.put("name", "<<-");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            }
            passwordlist.add(map);
        }
        walletApplyPasswordAdapter = new WalletApplyPasswordAdapter(this,this,passwordlist);
        gridView.setAdapter(walletApplyPasswordAdapter);
        mwallet_fingerprint_price_text.setText(Constants.getdoublepricestring(order_price,0.01));
        mwallet_pw_price_text.setText(Constants.getdoublepricestring(order_price,0.01));
        if (!pw_type) {
            if (Constants.is_open_fingerprint()) {
                mwallet_fingerprint_pay_layout.setVisibility(View.VISIBLE);
                mwallet_pw_pay_layout.setVisibility(View.GONE);
                initFingerprintCore();
            } else {
                mwallet_fingerprint_pay_layout.setVisibility(View.GONE);
                mwallet_pw_pay_layout.setVisibility(View.VISIBLE);
            }
        } else {
            mwallet_fingerprint_pay_layout.setVisibility(View.GONE);
            mwallet_pw_pay_layout.setVisibility(View.VISIBLE);
        }
        newdialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,newdialog);
        mwallet_fingerprint_pay_user_pw_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mwallet_fingerprint_pay_layout.setVisibility(View.GONE);
                mwallet_pw_pay_layout.setVisibility(View.VISIBLE);
            }
        });
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 1) {
                    strConfirmPassword = "";
                    for (int i = 0; i < 6; i++) {
                        strConfirmPassword += listConfirmPassword[i].toString().trim();
                    }
                    showProgressDialog();
                    if (payment_option == payment_option_order || payment_option == payment_option_group_order) {
                        FieldApi.payorder(MyAsyncHttpClient.MyAsyncHttpClient4(), payOrderHandler,order_id,pay_way_type,Integer.parseInt(strConfirmPassword));
                    } else if (payment_option == payment_option_invoice) {
                        FieldApi.pay_invoice_order(MyAsyncHttpClient.MyAsyncHttpClient3(), payOrderHandler,order_id,pay_way_type,Integer.parseInt(strConfirmPassword));
                    }
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 11 && position != 9) {   //点击0~9按钮
                    if (currentIndex >= -1 && currentIndex < 5) {     //判断输入位置————要小心数组越界
                        listConfirmPassword[++currentIndex] = passwordlist.get(position).get("name");
                        tvList[currentIndex].setVisibility(View.VISIBLE);
                        tvList[currentIndex].setText(passwordlist.get(position).get("name"));
                    }
                } else {
                    if (position == 11) {      //点击退格键
                        if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界
                            tvList[currentIndex].setText("");
                            tvList[currentIndex].setVisibility(View.GONE);
                            listConfirmPassword[currentIndex--] = "";
                        }
                    }
                }
            }
        });
        mwallet_fingerprint_pay_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
        mwallet_pw_pay_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
        mwallet_fingerprint_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
                startFingerprintRecognition();
            }
        });
        mwallet_pw_pay_forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset_payment_password = new Intent(OrderConfirmChoosePayWayActivity.this,WalletApplyActivity.class);
                reset_payment_password.putExtra("operation_type",2);
                startActivity(reset_payment_password);
            }
        });

    }
    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(this);
    }
    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
//            MessageUtils.showToast("指纹识别成功");
            if (mwallet_dialog_textview != null) {
                mwallet_dialog_textview.setText(getResources().getString(R.string.wallet_fingerprint_validation_success_text));
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (fingerprint_dialog != null && fingerprint_dialog.isShowing()) {
                        fingerprint_dialog.dismiss();
                        showProgressDialog();
                        fingerprint_pw = Constants.get_fingerprint_pw();
                        if (fingerprint_pw.length() == 6) {
                            if (payment_option == payment_option_order || payment_option == payment_option_group_order) {
                                FieldApi.payorder(MyAsyncHttpClient.MyAsyncHttpClient4(), payOrderHandler,order_id,pay_way_type,Integer.parseInt(fingerprint_pw));
                            } else if (payment_option == payment_option_invoice) {
                                FieldApi.pay_invoice_order(MyAsyncHttpClient.MyAsyncHttpClient3(), payOrderHandler,order_id,pay_way_type,Integer.parseInt(fingerprint_pw));
                            }
                        } else {
                            show_walletpay_doalog(true);
                        }
                    }
                }
            },100);
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
//            MessageUtils.showToast("指纹识别失败");
            if (mwallet_dialog_textview != null) {
                mwallet_dialog_textview.setText(getResources().getString(R.string.wallet_fingerprint_validation_failure_text));
                if (mwallet_dialog_textview.getText().toString().equals(getResources().getString(R.string.wallet_fingerprint_validation_failure_text))) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mwallet_dialog_textview.setText(getResources().getString(R.string.wallet_fingerprint_validation_start_text));
                        }
                    },500);
                }
            }
        }

        @Override
        public void onAuthenticateError(int errMsgId) {
            if (errMsgId == 5) {

            } else if (errMsgId == 7){
                MessageUtils.showToast(getResources().getString(R.string.wallet_fingerprint_validation_MoreThanTheNumberOfFive_text));
            }
            cancelFingerprintRecognition();

            if (fingerprint_dialog != null && fingerprint_dialog.isShowing()) {
                fingerprint_dialog.dismiss();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    show_walletpay_doalog(true);
                }
            },100);
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {
            showdialog();
            mwallet_dialog_textview.setText(getResources().getString(R.string.wallet_fingerprint_validation_start_text));
        }
    };
    private void startFingerprintRecognition() {
        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
                MessageUtils.showToast(getResources().getString(R.string.wallet_fingerprint_not_entry_text));
                //进入设置界面
//                Intent intent = new Intent("android.settings.SETTINGS");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                return;
            }

            if (mFingerprintCore.isAuthenticating()) {
                mFingerprintCore.startAuthenticate();
            } else {
                mFingerprintCore.startAuthenticate();
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.wallet_fingerprint_validation_nonsupport_text));
        }
    }
    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
            Bundle bundle_ordertype1 = new Bundle();
            bundle_ordertype1.putInt("order_type", 0);
            orderpayintent.putExtras(bundle_ordertype1);
            orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(orderpayintent);
            finish();
        }
        if (requestCode == 2) {
            if (resultCode == 2) {
                if (payment_option == payment_option_group_order) {
                    Intent mGroupSuccessIntent = new Intent(OrderConfirmChoosePayWayActivity.this,
                            GroupBookingPaidSuccessActivity.class);
                    mGroupSuccessIntent.putExtra("id",order_id);
                    mGroupSuccessIntent.putExtra("group_id",mGroupId);
                    startActivity(mGroupSuccessIntent);
                    finish();
                } else {
                    show_paysuccess_PopupWindow();
                }
            } else {
                finish();
            }
        }
    }
    private void showdialog() {
        LayoutInflater factory = LayoutInflater.from(OrderConfirmChoosePayWayActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_wallet_fingerprintrecognition_dialog, null);
        LinearLayout mwallet_all_dialog_layout = (LinearLayout) textEntryView.findViewById(R.id.wallet_all_dialog_layout);
        mwallet_dialog_textview = (TextView) textEntryView.findViewById(R.id.wallet_dialog_title_textview);
        Button mwallet_dialog_cancel_textview = (Button) textEntryView.findViewById(R.id.wallet_dialog_cancel_textview);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels - 120;     // 屏幕宽度（像素）
        int height = (width*452)/628;
        LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(width, height);
        mwallet_all_dialog_layout.setLayoutParams(paramgroups);
        fingerprint_dialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,fingerprint_dialog);
        mwallet_dialog_cancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelFingerprintRecognition();
                if (fingerprint_dialog.isShowing()) {
                    fingerprint_dialog.dismiss();
                }
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler WalletInfoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data instanceof JSONObject && data.toString().length() > 0 &&
                    JSONObject.parseObject(data.toString()) != null) {
                if (JSONObject.parseObject(data.toString()).get("balance") != null &&
                        JSONObject.parseObject(data.toString()).get("balance").toString().length() > 0) {
                    morder_balance_text.setText(getResources().getString(R.string.orderconfirm_available_balance_text)+
                            getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getpricestring(JSONObject.parseObject(data.toString()).get("balance").toString(),0.01) +
                            getResources().getString(R.string.term_types_unit_txt));
                    morder_balance_payment_checkbox.setEnabled(true);
                    wallet_parce = Double.parseDouble(Constants.getpricestring(JSONObject.parseObject(data.toString()).get("balance").toString(),0.01));
                }

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
    private void show_paysuccess_PopupWindow() {
        View myView = OrderConfirmChoosePayWayActivity.this.getLayoutInflater().inflate(R.layout.activity_confirmorder_success_popupwindow, null);
        DisplayMetrics metric = new DisplayMetrics();
        OrderConfirmChoosePayWayActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        pw = new PopupWindow(myView, width, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        pw.getBackground().setAlpha(155);
        //设置焦点为可点击
        pw.setFocusable(false);//可以试试设为false的结果
        pw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //将window视图显示在myButton下面
        //pw.showAsDropDown();
        pw.showAtLocation(OrderConfirmChoosePayWayActivity.this.findViewById(R.id.orderconfirmchoosepayment_alllayout), Gravity.RIGHT | Gravity.CENTER, 0, 0);
        TextView mtxt_back_top = (TextView) myView.findViewById(R.id.txt_back_top);
        Button mgoto_invoite = (Button) myView.findViewById(R.id.goto_invoite);
        Button mgoto_homepage = (Button) myView.findViewById(R.id.goto_homepage);
        TextView msuccess_pw_title_text = (TextView)myView.findViewById(R.id.success_pw_title_text);
        TextView msuccess_pw_title_message_text = (TextView)myView.findViewById(R.id.success_pw_title_message_text);
        LinearLayout mconfirmorder_success_order_review_alllayout = (LinearLayout)myView.findViewById(R.id.confirmorder_success_order_review_alllayout);
        mconfirmorder_success_review_order_listview = (MyLoadMoreExpandablelistView)myView.findViewById(R.id.confirmorder_success_review_order_listview);
        mconfirmorder_success_review_order_layout = (LinearLayout)myView.findViewById(R.id.confirmorder_success_review_order_layout);
        if (payment_option == payment_option_invoice) {
            isInvoicePaidSuccess = true;
            mconfirmorder_success_order_review_alllayout.setVisibility(View.GONE);
            mtxt_back_top.setVisibility(View.GONE);
            mgoto_homepage.setVisibility(View.GONE);
            mgoto_invoite.setText(getResources().getString(R.string.invoiceinfo_payment_success_btn_txt));
            msuccess_pw_title_text.setText(getResources().getString(R.string.invoiceinfo_payment_success_title_txt));
            msuccess_pw_title_message_text.setText(getResources().getString(R.string.invoiceinfo_payment_success_message_one_txt));
            mgoto_invoite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderConfirmChoosePayWayActivity.this.finish();
                    Intent maintabintent = new Intent(OrderConfirmChoosePayWayActivity.this, ApplyForInvoiceActivity.class);
                    maintabintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    maintabintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(maintabintent);
                }
            });

        } else if (payment_option == payment_option_order) {
            isResourcePaidSuccess = true;
            mconfirmorder_success_order_review_alllayout.setVisibility(View.VISIBLE);
            FieldApi.getpurchased_resourceslist(mOrderReviewCall,MyAsyncHttpClient.MyAsyncHttpClient3(), getReviewOrderListHandler,
                    "finished", "1", String.valueOf(Integer.MAX_VALUE));
            mtxt_back_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderConfirmChoosePayWayActivity.this.finish();
                }
            });
            mgoto_invoite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderConfirmChoosePayWayActivity.this.finish();
                    Intent resources_web = new Intent(OrderConfirmChoosePayWayActivity.this, AboutUsActivity.class);
                    resources_web.putExtra("type", Config.FACILITATOR_INT);
                    resources_web.putExtra("city_id", LoginManager.getInstance().getTrackcityid());
                    startActivity(resources_web);
                }
            });

            mgoto_homepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderConfirmChoosePayWayActivity.this.finish();
                    Intent maintabintent = new Intent(OrderConfirmChoosePayWayActivity.this, MainTabActivity.class);
                    maintabintent.putExtra("new_tmpintent", "goto_homepage");
                    startActivity(maintabintent);
                }
            });
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
                        if (payment_option == payment_option_group_order) {
                            Intent mGroupSuccessIntent = new Intent(OrderConfirmChoosePayWayActivity.this,
                                    GroupBookingPaidSuccessActivity.class);
                            mGroupSuccessIntent.putExtra("id",order_id);
                            mGroupSuccessIntent.putExtra("group_id",mGroupId);
                            startActivity(mGroupSuccessIntent);
                            finish();
                        } else {
                            show_paysuccess_PopupWindow();
                        }
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
                        if (payment_option == payment_option_order) {
                            Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
                            Bundle bundle_ordertype1 = new Bundle();
                            bundle_ordertype1.putInt("order_type", 1);//订单类型：待支付
                            bundle_ordertype1.putInt("showdialog", 1);
                            orderpayintent.putExtras(bundle_ordertype1);
                            orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(orderpayintent);
                            OrderConfirmChoosePayWayActivity.this.finish();
                        } else if (payment_option == payment_option_group_order) {
                            Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, GroupBookingMainActivity.class);
                            Bundle bundle_ordertype1 = new Bundle();
                            bundle_ordertype1.putInt("order_type", 1);//订单类型：待支付
                            bundle_ordertype1.putInt("showdialog", 1);
                            orderpayintent.putExtras(bundle_ordertype1);
                            startActivity(orderpayintent);
                            OrderConfirmChoosePayWayActivity.this.finish();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
    private LinhuiAsyncHttpResponseHandler getReviewOrderListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data == null || data.toString().length() == 0) {
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            if (jsonObject == null || jsonObject.get("data") == null ||
                    jsonObject.get("data").toString().length() == 0) {
                return;
            }
            if (mData != null) {
                mData.clear();
            }
            if (OrderChildList != null) {
                OrderChildList.clear();
            }
            com.alibaba.fastjson.JSONArray orderarray = JSON.parseArray(jsonObject.get("data").toString());
            if (orderarray == null || orderarray.isEmpty()) {
                mconfirmorder_success_review_order_layout.setVisibility(View.GONE);
                return;
            }
            mconfirmorder_success_review_order_layout.setVisibility(View.VISIBLE);
            for (int i = 0; i < orderarray.size(); i++) {
                com.alibaba.fastjson.JSONArray jsonArray = new JSONArray();
                jsonArray.add(JSON.parseObject(orderarray.get(i).toString()));
                ArrayList<OrderInfoModel> orderlist_tmp = (ArrayList<OrderInfoModel>) (JSON.parseArray(jsonArray.toString(), OrderInfoModel.class));
                com.alibaba.fastjson.JSONObject orderjsonobject = JSON.parseObject(orderarray.get(i).toString());
                Map<String, Object> groupmap = new HashMap<String, Object>();
                if (orderjsonobject.get("status") != null && orderjsonobject.getString("status").toString().length() > 0) {
                    groupmap.put("status",orderjsonobject.getString("status").toString());
                }
                if (orderjsonobject.get("deposit") != null) {
                    groupmap.put("order_deposit",Double.valueOf(orderjsonobject.get("deposit").toString()));
                }
                if (orderjsonobject.get("field_order_item_id") != null && orderjsonobject.getString("field_order_item_id").toString().length() > 0) {
                    groupmap.put("order_id", orderjsonobject.getString("field_order_item_id").toString());
                }
                if (orderjsonobject.get("real_pay") != null && orderjsonobject.getString("real_pay").toString().length() > 0) {
                    groupmap.put("actual_fee", orderjsonobject.getString("real_pay").toString());
                }
                groupmap.put("order_num", "order_num");
                if (orderjsonobject.get("ordered_user") != null && orderjsonobject.get("ordered_user").toString().length() > 0) {
                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id") != null &&
                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("id").toString().length() > 0) {
                        groupmap.put("ordered_user_id", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("id").toString());
                    }
                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile") != null &&
                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("mobile").toString().length() > 0) {
                        groupmap.put("ordered_user_mobile", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("mobile").toString());
                    }
                    if (JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name") != null &&
                            JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).get("name").toString().length() > 0) {
                        groupmap.put("ordered_user_name", JSONObject.parseObject(orderjsonobject.get("ordered_user").toString()).getString("name").toString());
                    }
                }

                if (orderjsonobject.get("paid_by") != null && orderjsonobject.get("paid_by").toString().length() > 0) {
                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id") != null &&
                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("id").toString().length() > 0) {
                        groupmap.put("paid_by_id", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("id").toString());
                    }
                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile") != null &&
                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("mobile").toString().length() > 0) {
                        groupmap.put("paid_by_mobile", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("mobile").toString());
                    }
                    if (JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name") != null &&
                            JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).get("name").toString().length() > 0) {
                        groupmap.put("paid_by_name", JSONObject.parseObject(orderjsonobject.get("paid_by").toString()).getString("name").toString());
                    }
                }
                mData.add(groupmap);
                OrderChildList.add(orderlist_tmp);
            }
            orderExpandableListAdapter = new OrderConfirmSuccessReviewOrderAdapter(OrderConfirmChoosePayWayActivity.this.getContext(), mData,OrderChildList,OrderConfirmChoosePayWayActivity.this,5);
            mconfirmorder_success_review_order_listview.setAdapter(orderExpandableListAdapter);
            for (int n = 0; n < mData.size(); n++) {
                mconfirmorder_success_review_order_listview.expandGroup(n);
            }
            mconfirmorder_success_review_order_listview.setGroupIndicator(null);
            mconfirmorder_success_review_order_listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (payment_option == 1) {
                Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, OrderListNewActivity.class);
                Bundle bundle_ordertype1 = new Bundle();
                bundle_ordertype1.putInt("order_type", 1);
                bundle_ordertype1.putInt("showdialog", 1);
                orderpayintent.putExtras(bundle_ordertype1);
                orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(orderpayintent);
            } else if (payment_option == payment_option_group_order) {
                Intent orderpayintent = new Intent(OrderConfirmChoosePayWayActivity.this, GroupBookingMainActivity.class);
                Bundle bundle_ordertype1 = new Bundle();
                bundle_ordertype1.putInt("order_type", 1);
                bundle_ordertype1.putInt("showdialog", 1);
                orderpayintent.putExtras(bundle_ordertype1);
                startActivity(orderpayintent);
            }
            OrderConfirmChoosePayWayActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }}
