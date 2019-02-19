/**
 * 
 */

package com.baselib.app.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.BuildConfig;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.OrderListNewActivity;
import com.linhuiba.business.activity.SplashScreenActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.magicwindow.MLink;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.Session;


//import com.easemob.chat.EMMessage;
//import com.easemob.chatui.activity.mMainActivity;
//import com.easemob.chatui.utils.CommonUtils;
//import com.easemob.util.EasyUtils;

/**
 * @author chentefu
 * @date 2015-12-2
 */
public class BaseActivity extends ActionBarActivity {
    private static final int notifiId = 11;

    private static final String TAG = "BaseActivity";

    private View topView;

    private ProgressDialog progressDialog;

    private View maskView;

    private View errorPageView;

    private long resumeUptime;

    private GestureDetector mGestureDetector;

    public static final String SYSTEM_EXIT = "com.example.exitsystem.system_exit";
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟错误统计
        MobclickAgent.setCatchUncaughtExceptions(true);
        //友盟推送启动数据
        PushAgent.getInstance(this).onAppStart();
        //权限设置
        ActionBar mActionBar = getSupportActionBar();
        if (null != mActionBar) {
            mActionBar.hide();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(false);
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(SYSTEM_EXIT);
//            receiver = new MyReceiver();
//            this.registerReceiver(receiver, filter);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Uri mLink = getIntent().getData();
        if (mLink != null) {
            MLinkAPIFactory.createAPI(this).router(mLink);
        } else {
            MLinkAPIFactory.createAPI(this).checkYYB();
        }
    }
    @Override
    protected void onDestroy() {
        maskView = null;
        errorPageView = null;
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
        //this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    protected void resolveIntent() {
    }

    protected void initApp() {
    }

    public ActionBar showActionBar(String title) {
        ActionBar mActionBar = getSupportActionBar();
        if (null != mActionBar) {
            mActionBar.show();
            mActionBar.setTitle(title);
        }
        return mActionBar;
    }

    public ActionBar showActionBar(String title, boolean displayHomeAsUp) {
        ActionBar showActionBar = showActionBar(title);
        if (showActionBar != null && displayHomeAsUp) {
            showActionBar.setDisplayHomeAsUpEnabled(true);
        }
        return showActionBar;
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        topView = inflater.inflate(layoutResID, null);
        super.setContentView(topView);
    }

    @Override
    public void setContentView(View view) {
        topView = view;
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        topView = view;
        super.setContentView(view, params);
    }

    public Context getContext() {
        return this;
    }

    public View getTopView() {
        return topView;
    }
    @Override
    protected void onResume() {
        resumeUptime = SystemClock.uptimeMillis();
        //2017/12/13 魔窗
        Session.onResume(this);
        super.onResume();
        if (getContext() instanceof SplashScreenActivity || getContext() instanceof MainTabActivity) {
            if (getContext() instanceof SplashScreenActivity) {
                Constants constants = new Constants(getContext());
                constants.getConfig();
            }
        } else {
            if ((System.currentTimeMillis() - LoginManager.getConfig_updatetime()) > 24 * 3600 * 1000 && LoginManager.getConfig_updatetime() > 0) {
                Constants constants = new Constants(getContext());
                constants.getConfig();
            }
        }
    }

    @Override
    protected void onPause() {
        //2017/12/13 魔窗
        Session.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setNetwork(View view) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void hideIMM() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public Rect getVisibleRect() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect;
    }

    public synchronized void showProgressDialog(String msg) {
        showProgressDialog(msg, true);
    }

    public synchronized void showProgressDialog() {
        showProgressDialog(this.getResources().getString(R.string.txt_waiting), true);
    }
    public synchronized void showMapProgressDialog() {
        showProgressDialog(this.getResources().getString(R.string.txt_waiting), true,true);
    }

    public synchronized void showProgressDialog(String msg, boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = MessageUtils.getProgressDialog(this, msg);
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
    public synchronized void showProgressDialog(String msg, boolean cancelable,boolean bottom) {
        if (progressDialog == null) {
            progressDialog = MessageUtils.getProgressDialog(this, msg);
        }
        WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;//设置ProgressDialog的重心
        params.y = 50;
        progressDialog.getWindow().setAttributes(params);
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }


    public void setOnProgressDismissListener(DialogInterface.OnCancelListener l) {
        if (progressDialog != null) {
            progressDialog.setOnCancelListener(l);
        }
    }

    public synchronized void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

//    public synchronized void showErrorPage(String error) {
//        if (errorPageView == null) {
//            errorPageView = findViewById(R.id.common_error_page);
//            if (errorPageView == null) {
//                errorPageView = LayoutInflater
//                        .from(XalabApplication.context)
//                        .inflate(R.layout.common_error_page,
//                                (ViewGroup) getTopView())
//                        .findViewById(R.id.common_error_title);
//            }
//        }
//        ((TextView) errorPageView.findViewById(R.id.common_error_title)).setText(error);
//        errorPageView.setVisibility(View.VISIBLE);
//        errorPageView.bringToFront();
//    }

    public synchronized void hideErrorPage() {
        if (errorPageView != null && errorPageView.isShown()) {
            errorPageView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.isFinishing()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                long eventtime = event.getEventTime();
                if (Math.abs(eventtime - resumeUptime) < 400) {
                    Log.d(TAG,
                            "baseactivity onKeyDown after onResume to close, do none");
                    return true;
                }
            }
            if (!(event.getRepeatCount() > 0) && !onPanelKeyDown(keyCode, event)) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String getCurrentActivity() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

    // text view helper
    public void setViewText(int id, String text) {
        View view = findViewById(id);
        if (view != null && view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    public void onRefreshClick(View view) {

    }
//    继承BaseActivity时的账号重复登录的处理
    protected void checkAccess(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            this.finish();
        }
    }
    protected void checkAccess_new(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
        }
    }
    protected boolean isNoResources(Throwable error,int code) {
        boolean noresource = false;
        if (Response.isFieldinfo_NoResources(error,code)) {
            noresource = true;
        } else {
            noresource = false;
        }
        return noresource;
    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
    protected void setSteepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

//    /**
//     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
//     * 如果不需要，注释掉即可
//     * @param message
//     */
//    protected void notifyNewMessage(EMMessage message) {
//        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
//        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
//        if(!EasyUtils.isAppRunningForeground(this)){
//            return;
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(getApplicationInfo().icon)
//                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
//
//        String ticker = CommonUtils.getMessageDigest(message, this);
//        String st = getResources().getString(R.string.expression);
//        if(message.getType() == EMMessage.Type.TXT)
//            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
//        //设置状态栏提示
//        mBuilder.setTicker(message.getFrom() + ": " + ticker);
//
//        //必须设置pendingintent，否则在2.3的机器上会有bug
//        Intent intent = new Intent(this, mMainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
//        mBuilder.setContentIntent(pendingIntent);
//
//        Notification notification = mBuilder.build();
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
//    }
}
