package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ChildAccountInvitationActivity extends BaseMvpActivity {
    private EditText msend_email_edittext;
    private Button msend_email_button;
    private Button mshare_email_button;
    private String shareurl = "";
    private Dialog pw;
    private IWXAPI api;
    private Bitmap ShareBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childaccountinvitation);
        initview();
        OnClick();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
    }
    private void initview() {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.childaccount_invitation_newaccount));
        TitleBarUtils.showBackImg(this,true);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        msend_email_edittext = (EditText)findViewById(R.id.send_email_edittext);
        msend_email_button = (Button)findViewById(R.id.send_email_button);
        mshare_email_button = (Button)findViewById(R.id.share_email_button);
    }
    private void OnClick() {
        msend_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msend_email_edittext.getText().toString().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.applyforrelease_email_hinttxt));
                    return;
                }
                if (!CommonTool.isEmail(msend_email_edittext.getText().toString())) {
                    MessageUtils.showToast(ChildAccountInvitationActivity.this.getResources().getString(R.string.applyforrelease_email_errortxt));
                    msend_email_edittext.setText("");
                    return;
                }
                UserApi.send_invitation(MyAsyncHttpClient.MyAsyncHttpClient(),sendinvitationHandler,msend_email_edittext.getText().toString());
            }
        });
        mshare_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                UserApi.child_accounts_share_link(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),share_linkHandler);
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler sendinvitationHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.txt_childaccounteditor_dialogtxt));
            ChildAccountInvitationActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler share_linkHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data instanceof JSONObject && JSONObject.parseObject(data.toString()) != null &&
                    JSONObject.parseObject(data.toString()).get("url") != null &&
                    JSONObject.parseObject(data.toString()).get("url").toString().length() > 0) {
                shareurl = JSONObject.parseObject(data.toString()).get("url").toString();
                View myView = ChildAccountInvitationActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                pw = new AlertDialog.Builder(ChildAccountInvitationActivity.this).create();
                ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
                Constants constants = new Constants(ChildAccountInvitationActivity.this,
                        "");
                constants.share_showPopupWindow(ChildAccountInvitationActivity.this,myView,pw,api,shareurl,
                        LoginManager.getInstance().getEnterprise_name()+ getResources().getString(R.string.txt_childaccountinvation_sharetitle_two_txt)+
                                getResources().getString(R.string.txt_childaccountinvation_sharetitle_one_txt),
                        getResources().getString(R.string.txt_childaccountinvation_sharedesciption_txt), ShareBitmap);
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
