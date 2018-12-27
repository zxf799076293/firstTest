package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.fingerprintrecognition.FingerprintCore;
import com.linhuiba.business.activity.fingerprintrecognition.KeyguardLockScreenManager;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldmodel.WalletFingerprintPayModel;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/20.
 */

public class WalletPaymentSettingActivity extends BaseMvpActivity {
    @InjectView(R.id.ToggleButton_wallet_fingerprint_payment)
    Switch mToggleButton_wallet_fingerprint_payment;
    @InjectView(R.id.fingerprint_payment_layout)
    RelativeLayout mfingerprint_payment_layout;
    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;
    private Dialog newdialog;
    private TextView mwallet_dialog_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletpaymentsetting);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.wallet_mywallet_pay_setting_text));
        TitleBarUtils.showBackImg(this,true);
        initFingerprintCore();
        initview();

    }
    @Override
    protected void onResume() {
        if (!LoginManager.isLogin()) {
            this.finish();
        }
        super.onResume();
    }

    private void initview() {
        if (mFingerprintCore.isSupport()) {
            mfingerprint_payment_layout.setVisibility(View.VISIBLE);
            if (Constants.is_open_fingerprint()) {
                mToggleButton_wallet_fingerprint_payment.setChecked(true);
            } else {
                mToggleButton_wallet_fingerprint_payment.setChecked(false);
            }
            mToggleButton_wallet_fingerprint_payment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mToggleButton_wallet_fingerprint_payment.isChecked()) {
                        startFingerprintRecognition();
                    } else {
                        cancelFingerprintRecognition();
                        if (LoginManager.getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
                            ArrayList<WalletFingerprintPayModel> walletFingerprintPaydatalist = (ArrayList<WalletFingerprintPayModel>) JSONArray.parseArray(LoginManager.getInstance().getFingerprint_validation(),WalletFingerprintPayModel.class);
                            for (int i = 0; i < walletFingerprintPaydatalist.size(); i++) {
                                if (walletFingerprintPaydatalist.get(i).getUid().equals(LoginManager.getUid())) {
                                    walletFingerprintPaydatalist.get(i).setOpen_fingerprint_pay(false);
                                    break;
                                }
                            }
                            LoginManager.getInstance().setFingerprint_validation(JSON.toJSONString(walletFingerprintPaydatalist, true));
                        }
                    }
                }
            });
        } else {
            mfingerprint_payment_layout.setVisibility(View.GONE);
        }
    }
    @OnClick({
            R.id.reset_payment_password_layout
    })
    public void payment_setting_intent(View view) {
        switch (view.getId()) {
            case R.id.reset_payment_password_layout:
                Intent reset_payment_password = new Intent(WalletPaymentSettingActivity.this,WalletApplyActivity.class);
                reset_payment_password.putExtra("operation_type",2);
                startActivity(reset_payment_password);
                break;
            default:
                break;
        }
    }
    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(this);
    }
    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {

            if (mwallet_dialog_textview != null) {
                mwallet_dialog_textview.setText(getResources().getString(R.string.wallet_fingerprint_validation_success_text));
            }
            if (newdialog != null && newdialog.isShowing()) {
                newdialog.dismiss();
            }
            Intent fingerprint_validation = new Intent(WalletPaymentSettingActivity.this,WalletFingerprintValidationActivity.class);
            startActivityForResult(fingerprint_validation,1);
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
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

            } else if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT){
                MessageUtils.showToast(getResources().getString(R.string.wallet_fingerprint_validation_MoreThanTheNumberOfFive_text));
            }
            cancelFingerprintRecognition();
            mToggleButton_wallet_fingerprint_payment.setChecked(false);
            if (newdialog != null && newdialog.isShowing()) {
                newdialog.dismiss();
            }
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
                mToggleButton_wallet_fingerprint_payment.setChecked(false);
                return;
            }

            if (mFingerprintCore.isAuthenticating()) {
                mFingerprintCore.startAuthenticate();
            } else {
                mFingerprintCore.startAuthenticate();
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.wallet_fingerprint_validation_nonsupport_text));
            mToggleButton_wallet_fingerprint_payment.setChecked(false);
        }
    }
    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 1) {
           if (resultCode == 1) {
               if (data.getExtras()!= null && data.getExtras().get("success") != null &&
                       data.getExtras().getInt("success") == 1) {
                   mToggleButton_wallet_fingerprint_payment.setChecked(true);
               } else {
                   cancelFingerprintRecognition();
                   mToggleButton_wallet_fingerprint_payment.setChecked(false);
               }
           } else {
               cancelFingerprintRecognition();
               mToggleButton_wallet_fingerprint_payment.setChecked(false);
           }
        }
    }
    //指纹录入弹出的提示框
    private void showdialog() {
        LayoutInflater factory = LayoutInflater.from(WalletPaymentSettingActivity.this);
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
        newdialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,newdialog);
        mwallet_dialog_cancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelFingerprintRecognition();
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
    }
}
