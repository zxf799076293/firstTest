package com.linhuiba.linhuifield;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
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

import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.network.Response;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/11/25.
 */

public class FieldBaseActivity  extends ActionBarActivity {
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
        super.onResume();
        if ((System.currentTimeMillis() - LoginManager.getConfig_updatetime()) > 24 * 3600 * 1000 && LoginManager.getConfig_updatetime() > 0) {
            Constants constants = new Constants(getContext());
            constants.getConfig();
        }
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

    public synchronized void showProgressDialog(String msg, boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = BaseMessageUtils.getProgressDialog(this, msg);
        }
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
    //继承BaseActivity时的账号重复登录的处理
    protected void checkAccess(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            Intent intent = new Intent("com.business.loginActivity");
            startActivity(intent);
        }
    }
    protected void checkAccess_new(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            Intent intent = new Intent("com.business.loginActivity");
            startActivity(intent);
            this.finish();
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

}
