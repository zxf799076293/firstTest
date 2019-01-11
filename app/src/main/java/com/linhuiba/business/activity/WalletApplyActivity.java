package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.WalletApplyPasswordAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldmodel.WalletFingerprintPayModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15.
 */

public class WalletApplyActivity extends BaseMvpActivity {
    @InjectView(R.id.walletapply_captcha_btn)
    Button mwalletapply_captcha_btn;
    @InjectView(R.id.walletapply_mobile_edit)
    EditText mwalletapply_mobile_edit;
    @InjectView(R.id.walletapply_captcha_edit)
    EditText mwalletapply_captcha_edit;
    @InjectView(R.id.wallet_open_authenticate_layout)
    LinearLayout mwallet_open_authenticate_layout;
    @InjectView(R.id.wallet_open_set_password_layout)
    LinearLayout mwallet_open_set_password_layout;
    @InjectView(R.id.walletapply_pasword_all_layout)
    LinearLayout mwalletapply_pasword_all_layout;
    @InjectView(R.id.wallet_authenticate_text)
    TextView mwallet_authenticate_text;
    @InjectView(R.id.wallet_authenticate_img)
    ImageView mwallet_authenticate_img;

    @InjectView(R.id.wallet_set_the_password_text)
    TextView mwallet_set_the_password_text;
    @InjectView(R.id.wallet_set_the_password_img)
    ImageView mwallet_set_the_password_img;

    @InjectView(R.id.wallet_confirm_the_password_text)
    TextView mwallet_confirm_the_password_text;
    private TimeCount mTime;
    private WalletApplyPasswordAdapter walletApplyPasswordAdapter;
    private PopupWindow password_pw;
    private ArrayList<HashMap<String,String>> passwordlist= new ArrayList<>();
    private TextView[] tvList = new TextView[6];
    private int currentIndex = -1;
    private String strSetPassword = "";
    private String strConfirmPassword = "";
    private String[] listSetPassword = new String[6];
    private String[] listConfirmPassword = new String[6];
    private LinearLayout menter_password_alllayout;
    private int walletapply_page_type = 1;//操作步骤页面顺序
    private String captcha_str = "";
    private DisplayMetrics metric;
    private int operation_type = 0; //1是开通2是重置密码
    private final int ApplyWalletFunctionInt = 1;//开通
    private final int WalletPasswordResetFunctionInt = 2;//重置密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletapply);
        ButterKnife.inject(this);
        initview();
    }
    private void walletapply_page_show() {
        if (walletapply_page_type == 1) {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_fingerprint_validation_title_text));
            mwallet_authenticate_text.setTextColor(getResources().getColor(R.color.default_bluebg));
            mwallet_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch));
            mwallet_set_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            mwallet_set_the_password_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
            mwallet_confirm_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            mwalletapply_captcha_edit.setText("");
            captcha_str = "";
            mwallet_open_authenticate_layout.setVisibility(View.VISIBLE);
            mwallet_open_set_password_layout.setVisibility(View.GONE);
            if (password_pw != null) {
                password_pw.dismiss();
            }
        } else if (walletapply_page_type == 2 || walletapply_page_type == 3) {
            currentIndex = -1;
            if (menter_password_alllayout != null &&
                    menter_password_alllayout.getVisibility() == View.VISIBLE) {
                menter_password_alllayout.setVisibility(View.GONE);
            }
            mwallet_open_authenticate_layout.setVisibility(View.GONE);
            mwallet_open_set_password_layout.setVisibility(View.VISIBLE);
            if (password_pw != null && password_pw.isShowing()) {
                if (menter_password_alllayout != null &&
                        menter_password_alllayout.getVisibility() == View.GONE) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            menter_password_alllayout.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }
            } else {
                showPopupWindow();
            }
            if (walletapply_page_type == 2) {
                TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_open_set_the_password_titletext));
                mwallet_authenticate_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                mwallet_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
                mwallet_set_the_password_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mwallet_set_the_password_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch));
                mwallet_confirm_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));

                strSetPassword = "";
                for (int i = 0; i < 6; i++) {
                    listSetPassword[i] = "";
                    tvList[i].setText("");
                    tvList[i].setVisibility(View.GONE);
                }
            } else if (walletapply_page_type == 3) {
                TitleBarUtils.setTitleText(this,getResources().getString(R.string.txt_findpassword_pw_confirm_edit_text));
                mwallet_authenticate_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                mwallet_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
                mwallet_set_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                mwallet_set_the_password_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
                mwallet_confirm_the_password_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                strConfirmPassword = "";
                for (int i = 0; i < 6; i++) {
                    listConfirmPassword[i] = "";
                    tvList[i].setText("");
                    tvList[i].setVisibility(View.GONE);
                }
            }
        }
    }
    private void initview() {
        mTime = new TimeCount(60000, 1000);
        metric = new DisplayMetrics();
        WalletApplyActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = (width-56-10)/6;
        final LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width-56-50), height);
        Intent password_operation = getIntent();
        if (password_operation.getExtras() != null && password_operation.getExtras().get("operation_type") != null &&
                password_operation.getExtras().getInt("operation_type") > 0) {
            operation_type = password_operation.getExtras().getInt("operation_type");
        }
        mwalletapply_pasword_all_layout.setLayoutParams(paramgroups);
        mwallet_authenticate_text.setTextColor(getResources().getColor(R.color.default_bluebg));
        mwallet_authenticate_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch));
        mwallet_set_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
        mwallet_set_the_password_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_grayswitch));
        mwallet_confirm_the_password_text.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
        mwallet_open_authenticate_layout.setVisibility(View.VISIBLE);
        mwallet_open_set_password_layout.setVisibility(View.GONE);
        if (password_pw != null && menter_password_alllayout != null) {
            if (menter_password_alllayout.getVisibility() == View.VISIBLE) {
                menter_password_alllayout.setVisibility(View.GONE);
            }
        }
        mwalletapply_mobile_edit.setText(LoginManager.getMobile());
        mwalletapply_captcha_edit.setEnabled(false);
        for (int i = 0; i < tvList.length; i++) {
            int id = getResources().getIdentifier("wallet_apply_text" + i, "id", getPackageName());
            TextView textView = (TextView) findViewById(id);
            tvList[i]= (textView);
        }
        mwalletapply_captcha_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.getTrimmedLength(mwalletapply_mobile_edit.getText().toString()) == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(mwalletapply_mobile_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(WalletApplyActivity.this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        mwalletapply_mobile_edit.getText().toString(),"3");
            }
        });
        mwalletapply_captcha_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    captcha_str = s.toString().trim();
                    //关闭软键盘
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    walletapply_page_type = 2;
                    walletapply_page_show();
                }
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
                    if (walletapply_page_type == 2) {
                        strSetPassword = "";
                        for (int i = 0; i < 6; i++) {
                            strSetPassword += listSetPassword[i].toString().trim();
                        }
                        walletapply_page_type = 3;
                        menter_password_alllayout.setVisibility(View.GONE);
                        walletapply_page_show();
                    } else if (walletapply_page_type == 3) {
                        strConfirmPassword = "";
                        for (int i = 0; i < 6; i++) {
                            strConfirmPassword += listConfirmPassword[i].toString().trim();
                        }
                        if (strConfirmPassword.equals(strSetPassword)) {
                            if (password_pw != null) {
                                password_pw.dismiss();
                            }
                            showPopupWindow();
                            if (operation_type == ApplyWalletFunctionInt) {
                                UserApi.dredgewallets(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),DredgeWalletsHandler,
                                        strSetPassword,strConfirmPassword,LoginManager.getMobile(),captcha_str);
                            } else if (operation_type == WalletPasswordResetFunctionInt) {
                                UserApi.modifywallets_pw(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),DredgeWalletsHandler,
                                        strSetPassword,strConfirmPassword,LoginManager.getMobile(),captcha_str);
                            }
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.wallet_apply_password_inconformity_text));
                            walletapply_page_type = 2;
                            walletapply_page_show();
                        }
                    }
                }
            }
        });
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletapply_page_type == 1) {
                    WalletApplyActivity.this.finish();
                } else {
                    if (walletapply_page_type == 2) {
                        walletapply_page_type = 1;
                        if (password_pw != null && password_pw.isShowing()) {
                            password_pw.dismiss();
                        }
                    } else if (walletapply_page_type == 3) {
                        MessageUtils.showToast("前后密码不一致，请重新输入");
                        walletapply_page_type = 2;
                    }
                    walletapply_page_show();
                }
            }
        });

    }
    @OnClick({
            R.id.walletapply_pasword_all_layout
    })
    public void walletapplyclick (View view) {
        switch (view.getId()) {
            case R.id.walletapply_pasword_all_layout:
                if (password_pw.isShowing()) {
                    if (menter_password_alllayout.getVisibility() == View.GONE) {
                        menter_password_alllayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    showPopupWindow();
                }
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                mTime.start();
                mwalletapply_captcha_edit.setEnabled(true);
            } else {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.txt_register_send_codeb_error_remind));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mwalletapply_captcha_btn.setText(R.string.txt_register_codebtn_txt);
            mwalletapply_captcha_btn.setClickable(true);
            mwalletapply_captcha_btn.setBackgroundResource(R.color.white);
            mwalletapply_captcha_btn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            mwalletapply_captcha_btn.setClickable(false);
            mwalletapply_captcha_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            mwalletapply_captcha_btn.setText(str+"("+(millisUntilFinished / 1000)+")");
            mwalletapply_captcha_btn.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private LinhuiAsyncHttpResponseHandler DredgeWalletsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            WalletApplyActivity.this.finish();
            if (operation_type == ApplyWalletFunctionInt) {
                Intent mywalletintent = new Intent(WalletApplyActivity.this,MyWalletActivity.class);
                startActivity(mywalletintent);
                WalletApplyActivity.this.finish();
            } else if (operation_type == WalletPasswordResetFunctionInt) {
                MessageUtils.showToast(getResources().getString(R.string.pw_modify_success_text));
                WalletApplyActivity.this.finish();
                if (Constants.is_open_fingerprint()) {
                    if (LoginManager.getInstance().getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
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
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            if (isNoResources(error,-3)) {
                walletapply_page_type = 1;
                walletapply_page_show();
                MessageUtils.showToast(getResources().getString(R.string.txt_register_codebtn_error_txt));
            } else {
                walletapply_page_type = 1;
                walletapply_page_show();
            }
            checkAccess_new(error);
        }
    };
    private void showPopupWindow( ) {
        View myView = WalletApplyActivity.this.getLayoutInflater().inflate(R.layout.activity_walletapply_enter_password_pw, null);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        password_pw = new PopupWindow(myView, width, height-240, true);
        //设置焦点为可点击
        password_pw.setFocusable(false);
        password_pw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        password_pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //将window视图显示在myButton下面
        LinearLayout relativeLayout = (LinearLayout)findViewById(R.id.wallet_apply_all_layout);
        password_pw.showAtLocation(relativeLayout, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
        GridView gridView = (GridView) myView.findViewById(R.id.walletapply_password_gridview);
        menter_password_alllayout = (LinearLayout)myView.findViewById(R.id.walletapply_enter_password_alllayout);
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
                    if (currentIndex >= -1 && currentIndex < 5) {     //判断输入位置
                        if (walletapply_page_type == 2) {
                            listSetPassword[++currentIndex] = passwordlist.get(position).get("name");
                        } else if (walletapply_page_type == 3) {
                            listConfirmPassword[++currentIndex] = passwordlist.get(position).get("name");
                        }
                        tvList[currentIndex].setVisibility(View.VISIBLE);
                        tvList[currentIndex].setText(passwordlist.get(position).get("name"));
                    }
                } else {
                    if (position == 11) {      //点击退格键
                        if (currentIndex - 1 >= -1) {      //判断是否删除完毕
                            tvList[currentIndex].setText("");
                            tvList[currentIndex].setVisibility(View.GONE);
                            if (walletapply_page_type == 2) {
                                listSetPassword[currentIndex--] = "";
                            } else if (walletapply_page_type == 3) {
                                listConfirmPassword[currentIndex--] = "";
                            }
                        }
                    }
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (walletapply_page_type == 1) {
            WalletApplyActivity.this.finish();
        } else {
            if (walletapply_page_type == 2) {
                walletapply_page_type = 1;
                if (password_pw != null && password_pw.isShowing()) {
                    password_pw.dismiss();
                }
            } else if (walletapply_page_type == 3) {
                MessageUtils.showToast(getResources().getString(R.string.wallet_apply_password_inconformity_text));
                walletapply_page_type = 2;
            }
            walletapply_page_show();
        }
        return false;
    }

}
