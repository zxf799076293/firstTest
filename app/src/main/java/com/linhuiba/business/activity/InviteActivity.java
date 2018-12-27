package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.GroupBookingListFragment;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/27.
 */
public class InviteActivity extends BaseMvpActivity {
    @InjectView(R.id.Invite_WebView)
    BridgeWebView minvitewebview;
    private IWXAPI api;
    private Dialog mShareDialog;
    private Bitmap ShareBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.inject(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        minvitewebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        minvitewebview.clearHistory();
        minvitewebview.clearFormData();
        minvitewebview.clearCache(true);
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
    }

    private void initView() {
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.tab_txt_invite));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minvitewebview.canGoBack()) {
                    minvitewebview.goBack();// 返回前一个页面
                } else {
                    InviteActivity.this.finish();
                }
            }
        });

        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.tab_ic_share), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.isLogin()) {
                    showPopupWindow();
                } else {
                    LoginActivity.BaesActivityreloadLogin(InviteActivity.this);
                }
            }
        });
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        minvitewebview.registerHandler("shareToWechat", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (LoginManager.isLogin()) {
                    showPopupWindow();
                } else {
                    LoginActivity.BaesActivityreloadLogin(InviteActivity.this);
                }
            }
        });
        minvitewebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        minvitewebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        minvitewebview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        minvitewebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        minvitewebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        minvitewebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        minvitewebview.getSettings().setAppCacheEnabled(false);//是否使用缓存
        minvitewebview.getSettings().setDomStorageEnabled(true);//DOM Storage
        String RandomStr = com.linhuiba.linhuifield.connector.Constants.getRandomString(10);
        syncCookie(Config.Domain_Name,RandomStr.substring(0, 2) + LoginManager.getAccessToken() + RandomStr.substring(2));
        minvitewebview.loadUrl(Config.INVITE_URL+
                "?invite_code=" + LoginManager.getinvitecode());
    }

    private void showPopupWindow() {
        if (ShareBitmap == null) {
            ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_xianjinghongbao);
            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
            ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 300, 220, true);//压缩Bitmap
            mHandler.sendEmptyMessage(0);
        } else {
            mShareDialog = new AlertDialog.Builder(InviteActivity.this).create();
            View myView = InviteActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(InviteActivity.this,
                    "");
            constants.shareWXMiniPopupWindow(InviteActivity.this,myView,mShareDialog,api, Config.SHARE_INVITE_CODE_URL + LoginManager.getinvitecode()+"&userid="+LoginManager.getUid(),
                    getResources().getString(R.string.invite_share_title_text),
                    getResources().getString(R.string.invite_share_description_text),
                    ShareBitmap,Config.WX_MINI_SHARE_INVITE_CODE_URL+ LoginManager.getinvitecode()+"&userid="+LoginManager.getUid(),
                    null,getResources().getString(R.string.invite_share_title_text));
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mShareDialog = new AlertDialog.Builder(InviteActivity.this).create();
                    View myView = InviteActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(InviteActivity.this,
                            "");
                    constants.shareWXMiniPopupWindow(InviteActivity.this,myView,mShareDialog,api, Config.SHARE_INVITE_CODE_URL + LoginManager.getinvitecode()+"&userid="+LoginManager.getUid(),
                            getResources().getString(R.string.invite_share_title_text),
                            getResources().getString(R.string.invite_share_description_text),
                            ShareBitmap,Config.WX_MINI_SHARE_INVITE_CODE_URL+ LoginManager.getinvitecode()+"&userid="+LoginManager.getUid(),
                            null,getResources().getString(R.string.invite_share_title_text));
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * bitmap中的透明色用白色替换
     *
     * @param bitmap
     * @return
     */
    public static Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取和白色混合颜色
     *
     * @return
     */
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite

                        (green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    /**
     * 获取单色的混合值
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && minvitewebview.canGoBack()) {
            minvitewebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public boolean syncCookie(String url,String key) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, "LINHUIBA_KEY="+key);
        String newCookie = cookieManager.getCookie(url);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(InviteActivity.this);
            cookieSyncManager.sync();
        }
        return TextUtils.isEmpty(newCookie) ? false : true;
    }

}
