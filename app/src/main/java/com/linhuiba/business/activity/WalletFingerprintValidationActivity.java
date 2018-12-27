package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.WalletApplyPasswordAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldmodel.WalletFingerprintPayModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/25.
 */

public class WalletFingerprintValidationActivity extends BaseMvpActivity {
    @InjectView(R.id.walletapply_pasword_all_layout)
    LinearLayout mwalletapply_pasword_all_layout;
    private DisplayMetrics metric;
    private WalletApplyPasswordAdapter walletApplyPasswordAdapter;
    private PopupWindow password_pw;
    private ArrayList<HashMap<String,String>> passwordlist= new ArrayList<>();
    private TextView[] tvList = new TextView[6];
    private int currentIndex = -1;
    private String strConfirmPassword = "";
    private String[] listConfirmPassword = new String[6];
    private LinearLayout fingerprintenter_password_alllayout;
    private ArrayList<WalletFingerprintPayModel> walletFingerprintPaydatalist = new ArrayList<>();
    private WalletFingerprintPayModel walletFingerprintPayModels = new WalletFingerprintPayModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletfingerprintvalidation);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_fingerprint_validation_title_text));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.pw_find), 14, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset_payment_password = new Intent(WalletFingerprintValidationActivity.this,WalletApplyActivity.class);
                reset_payment_password.putExtra("operation_type",2);
                startActivity(reset_payment_password);
            }
        });
        ininview();
    }
    private void ininview() {
        metric = new DisplayMetrics();
        WalletFingerprintValidationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = (width-56-10)/6;
        LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width - 56 -50), height);
        mwalletapply_pasword_all_layout.setLayoutParams(paramgroups);
        for (int i = 0; i < tvList.length; i++) {
            int id = getResources().getIdentifier("wallet_pay_pw_text" + i, "id", getPackageName());
            TextView textView = (TextView) findViewById(id);
            tvList[i]= (textView);
        }
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
                        if (strConfirmPassword.length() == 6) {
                            showPopupWindow();
                            UserApi.validate_password(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),validate_passwordHandler,
                                    strConfirmPassword);
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                            currentIndex = -1;
                            strConfirmPassword = "";
                            for (int i = 0; i < 6; i++) {
                                listConfirmPassword[i] = "";
                                tvList[i].setText("");
                                tvList[i].setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                showPopupWindow();
            }
        }, 500);
    }
    @OnClick({
            R.id.walletapply_pasword_all_layout
    })
    public void walletapplyclick (View view) {
        switch (view.getId()) {
            case R.id.walletapply_pasword_all_layout:
                if (password_pw != null && password_pw.isShowing()) {
                    if (fingerprintenter_password_alllayout.getVisibility() == View.GONE) {
                        fingerprintenter_password_alllayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    showPopupWindow();
                }
                break;
            default:
                break;
        }
    }
    private void showPopupWindow() {
        View myView = WalletFingerprintValidationActivity.this.getLayoutInflater().inflate(R.layout.activity_walletapply_enter_password_pw, null);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        password_pw = new PopupWindow(myView, width, height-240, true);
        //设置焦点为可点击
        password_pw.setFocusable(false);//可以试试设为false的结果
        password_pw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        password_pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //将window视图显示在myButton下面
        LinearLayout relativeLayout = (LinearLayout)findViewById(R.id.wallet_fingerprint_validation_all_layout);
        password_pw.showAtLocation(relativeLayout, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
        GridView gridView = (GridView) myView.findViewById(R.id.walletapply_password_gridview);
        fingerprintenter_password_alllayout = (LinearLayout)myView.findViewById(R.id.walletapply_enter_password_alllayout);
        ImageButton mhide_enterpw_all_btn = (ImageButton)myView.findViewById(R.id.hide_enterpw_all_btn);
        mhide_enterpw_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_pw.dismiss();
            }
        });
        if (passwordlist != null) {
            passwordlist.clear();
        }
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
    }
    private LinhuiAsyncHttpResponseHandler validate_passwordHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            Intent fingerprint_intent = new Intent();
            fingerprint_intent.putExtra("success",1);
            setResult(1,fingerprint_intent);
            WalletFingerprintValidationActivity.this.finish();
            if (LoginManager.getInstance().getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
                walletFingerprintPaydatalist = (ArrayList<WalletFingerprintPayModel>) JSONArray.parseArray(LoginManager.getFingerprint_validation(),WalletFingerprintPayModel.class);
                boolean cycle_mark = false;
                for (int i = 0; i < walletFingerprintPaydatalist.size(); i++) {
                    if (walletFingerprintPaydatalist.get(i).getUid().equals(LoginManager.getUid())) {
                        walletFingerprintPaydatalist.get(i).setOpen_fingerprint_pay(true);
                        walletFingerprintPaydatalist.get(i).setWallet_pw(strConfirmPassword);
                        cycle_mark = true;
                        break;
                    }
                }
                if (!cycle_mark) {
                    walletFingerprintPayModels.setOpen_fingerprint_pay(true);
                    walletFingerprintPayModels.setUid(LoginManager.getUid());
                    walletFingerprintPayModels.setWallet_pw(strConfirmPassword);
                    walletFingerprintPaydatalist.add(walletFingerprintPayModels);
                }
                LoginManager.getInstance().setFingerprint_validation(JSON.toJSONString(walletFingerprintPaydatalist, true));
            } else {
                walletFingerprintPayModels.setOpen_fingerprint_pay(true);
                walletFingerprintPayModels.setUid(LoginManager.getUid());
                walletFingerprintPayModels.setWallet_pw(strConfirmPassword);
                walletFingerprintPaydatalist.add(walletFingerprintPayModels);
                LoginManager.getInstance().setFingerprint_validation(JSON.toJSONString(walletFingerprintPaydatalist, true));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode,okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            currentIndex = -1;
            strConfirmPassword = "";
            for (int i = 0; i < 6; i++) {
                listConfirmPassword[i] = "";
                tvList[i].setText("");
                tvList[i].setVisibility(View.GONE);
            }
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
}
