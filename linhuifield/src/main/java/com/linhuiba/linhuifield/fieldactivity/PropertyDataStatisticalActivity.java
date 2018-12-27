package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_UserApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PropertyDataStatisticalActivity extends FieldBaseMvpActivity {
    private Dialog pw_dialog;
    private PopupWindow popupWindow;
    private WebView mstatistical_WebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_propertydata_statistical);
        initView();
    }
    private void initView() {
        mstatistical_WebView = (WebView)findViewById(R.id.statistical_WebView);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.field_home_data_statistical_text));
        TitleBarUtils.showBackImg(this,true);
        mstatistical_WebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mstatistical_WebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mstatistical_WebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mstatistical_WebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mstatistical_WebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mstatistical_WebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mstatistical_WebView.getSettings().setAppCacheEnabled(false);//是否使用缓存
        mstatistical_WebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        mstatistical_WebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){

                //handler.cancel(); 默认的处理方式，WebView变成空白页
                //handler.process();接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }
        });

        Map<String,String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("authorization", "bearer"+ LoginManager.getAccessToken());
        String RandomStr = com.linhuiba.linhuifield.connector.Constants.getRandomString(10);
        mstatistical_WebView.loadUrl(Config.PropertyDataStatistical +
                "?key="+ RandomStr.substring(0, 2) + LoginManager.getAccessToken() + RandomStr.substring(2),extraHeaders);
        if (LoginManager.getRelation_id() > -1) {
            if (LoginManager.getRelation_id() != 2) {
                showsigning_dialog();
            }
        }
    }
    private void showsigning_dialog() {
        LayoutInflater factory = LayoutInflater.from(PropertyDataStatisticalActivity.this);
        final View myView = factory.inflate(com.linhuiba.linhuifield.R.layout.field_activity_field_orders_success_dialog, null);
        TextView mdialog_title_textview = (TextView)myView.findViewById(R.id.dialog_title_textview);
        TextView mcancel_share_btn = (TextView) myView.findViewById(R.id.btn_cancel);
        TextView mbtn_perfect = (TextView) myView.findViewById(R.id.btn_perfect);
        LinearLayout mIvLL = (LinearLayout) myView.findViewById(R.id.linhuiba_logo_ll);
        pw_dialog = new AlertDialog.Builder(PropertyDataStatisticalActivity.this).create();
        Constants.show_dialog(myView,pw_dialog);
        mdialog_title_textview.setText(getResources().getString(R.string.myselfinfo_signing_title_text));
        mcancel_share_btn.setText(getResources().getString(R.string.cancel));
        mbtn_perfect.setText(getResources().getString(R.string.myselfinfo_signing_text));
        mIvLL.setVisibility(View.VISIBLE);
        TextPaint paint = mbtn_perfect.getPaint();
        paint.setFakeBoldText(true);
        pw_dialog.setOnKeyListener(keylistener);
        mcancel_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw_dialog.isShowing()) {
                    pw_dialog.dismiss();
                }
                PropertyDataStatisticalActivity.this.finish();
            }
        });
        mbtn_perfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Field_UserApi.appointment_sign(MyAsyncHttpClient.MyAsyncHttpClient(),appointment_signHandler);
            }
        });
    }
    private void show_paysuccess_PopupWindow() {
        View myView = PropertyDataStatisticalActivity.this.getLayoutInflater().inflate(R.layout.field_activity_property_data_statisistical_signing_success_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        PropertyDataStatisticalActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        popupWindow = new PopupWindow(myView, width, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        popupWindow.getBackground().setAlpha(155);
        //设置焦点为可点击
        popupWindow.setFocusable(false);//可以试试设为false的结果
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //将window视图显示在myButton下面
        //pw.showAsDropDown();
        popupWindow.showAtLocation(PropertyDataStatisticalActivity.this.findViewById(R.id.property_data_statistical_all_layout), Gravity.RIGHT | Gravity.CENTER, 0, 0);
        TextView mtxt_back_top = (TextView) myView.findViewById(R.id._signning_txt_back_top);
        mtxt_back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                PropertyDataStatisticalActivity.this.finish();
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler appointment_signHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (pw_dialog.isShowing()) {
                pw_dialog.dismiss();
            }
            show_paysuccess_PopupWindow();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mstatistical_WebView.canGoBack()) {
                if (LoginManager.getRelation_id() == 2) {
                    mstatistical_WebView.goBack();// 返回前一个页面
                } else {
                    PropertyDataStatisticalActivity.this.finish();
                }
            } else {
                PropertyDataStatisticalActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                PropertyDataStatisticalActivity.this.finish();
                return false;
            } else {
                return true;
            }
        }
    };
}
