package com.linhuiba.business.basemvp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
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
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.BuildConfig;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.SplashScreenActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.Session;

/**
 * Created by Administrator on 2018/2/5.
 */

public class BaseMvpActivity extends ActionBarActivity implements BaseView {
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
    private CustomDialog customDialog;
    private MessageReceiver mMessageReceiver = new MessageReceiver();
    private CustomDialog mMQDialog;
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
        //判断是debug还是release
        Config.DEBUG = BuildConfig.DEBUG?true:false;
        // 注册美洽接收即时消息广播
        IntentFilter intentFilter = new IntentFilter(MQMessageManager.ACTION_NEW_MESSAGE_RECEIVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);

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
        // 取消注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    public Context getContext() {
        return this;
    }
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showToast(String msg) {
        MessageUtils.showToast(msg);
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
    public synchronized void showSwitchDialog(int role, String msg, boolean isNoDiss) {
        int llBg = 0;
        int imgvBg = 0;
        int tvBg = 0;
        int imgvDrawable = 0;
        if (role == 3) {
            llBg = getResources().getColor(R.color.switch_layout_bg_color);
            imgvBg = getResources().getColor(R.color.switch_layout_bg_color);
            tvBg = getResources().getColor(R.color.white);
            imgvDrawable = R.drawable.ic_shangjizhuan_three_three;
        } else if (role == 2) {
            llBg = getResources().getColor(R.color.white);
            imgvBg = getResources().getColor(R.color.white);
            tvBg = getResources().getColor(R.color.switch_layout_bg_color);
            imgvDrawable = R.drawable.ic_wuyezhuan_three_three;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        customDialog = builder
                .cancelTouchout(false)
                .view(R.layout.app_switch_layout)
                .setText(R.id.app_switch_tv,
                        msg)
                .setBgColor(R.id.app_switch_imgv,imgvBg)
                .setBgColor(R.id.app_switch_rl,llBg)
                .setTextColor(R.id.app_switch_tv,tvBg)
                .setImgvGif(this,R.id.app_switch_imgv,imgvDrawable)
                .build();
        com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(this,customDialog);
        if (isNoDiss) {
            customDialog.setOnKeyListener(keylistener);
        }
        customDialog.show();
        Window window = customDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (role == 3) {
            window.setBackgroundDrawableResource(R.color.switch_layout_bg_color);
        } else if (role == 2) {
            window.setBackgroundDrawableResource(R.color.white);
        }

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        customDialog.getWindow().setAttributes(lp);
    }
    public synchronized void hideSwitchDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }
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
        }
    }
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return true;
            } else {
                return true;
            }
        }
    };
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu(){
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取 ACTION
            final String action = intent.getAction();
            // 接收新消息
            if (MQMessageManager.ACTION_NEW_MESSAGE_RECEIVED.equals(action)) {
                // 从 intent 获取消息 id
                String msgId = intent.getStringExtra("msgId");
                // 从 MCMessageManager 获取消息对象
                MQMessageManager messageManager = MQMessageManager.getInstance(context);
                MQMessage message = messageManager.getMQMessage(msgId);
                // do something
                if (!com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(getApplicationContext(),"com.meiqia.meiqiasdk.activity.MQConversationActivity") &&
                        com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(getApplicationContext(),getActivity().getClass().getName())) {
                    if ((LoginManager.getInstance().getFieldexit() == -1 || LoginManager.getInstance().getFieldexit() == 1)) {

                    } else {
                        showMQMsg(message.getContent());
                    }

                }
            }
        }
    }
    private void showMQMsg(String msg) {
        if (mMQDialog == null || !mMQDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            mMQDialog.dismiss();
                            MQConfig.init(getContext(), com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
                                @Override
                                public void onSuccess(String clientId) {
                                    if (LoginManager.isLogin()) {
                                        HashMap<String, String> clientInfo = new HashMap<>();
                                        clientInfo.put("name", LoginManager.getCompany_name());
                                        clientInfo.put("email", LoginManager.geteEmail());
                                        if (LoginManager.getRole_id().equals("2")) {
                                            clientInfo.put("comment", getResources().getString(R.string.module_myself_role_property_str));
                                        } else if (LoginManager.getRole_id().equals("3")) {
                                            clientInfo.put("comment", getResources().getString(R.string.module_myself_role_business_str));
                                        } else if (LoginManager.getRole_id().equals("1")) {
                                            clientInfo.put("comment", getResources().getString(R.string.module_myself_role_admin_str));
                                        }
                                        clientInfo.put("avatar", "https://banner.linhuiba.com/o_1b555h2jjoj6u1716tr12dl2rg7.jpg");
                                        clientInfo.put("tel", LoginManager.getMobile());
                                        // 相同的 id 会被识别为同一个顾客
                                        Intent intent = new MQIntentBuilder(getContext())
                                                .setClientInfo(clientInfo)
                                                .setCustomizedId(LoginManager.getUid())
                                                .build();
                                        startActivityForResult(intent,10);
                                    } else {
                                        Intent intent = new MQIntentBuilder(getContext())
                                                .build();
                                        startActivityForResult(intent,10);
                                    }

                                }

                                @Override
                                public void onFailure(int code, String message) {
                                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                                }
                            });
                            break;
                        case R.id.btn_cancel:
                            mMQDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
            mMQDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .setText(R.id.dialog_title_textview, msg)
                    .setText(R.id.dialog_title_msg_tv,getResources().getString(R.string.module_mq_service_msg))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.myselfinfo_service))
                    .setText(R.id.btn_cancel,getResources().getString(com.linhuiba.linhuifield.R.string.permission_cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(getContext(),mMQDialog);
            mMQDialog.show();
        }
    }
    //判断是否是华为刘海屏
    public static boolean isHuaweiScreenHasGroove(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        } finally {
            return ret;
        }
    }

    //获取华为刘海的高宽 //int[0]值为刘海宽度 int[1]值为刘海高度
    public static int getHuaweiNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        }  catch (Exception e) {

        } finally {
            return ret[1];
        }
    }
    //判断手机是否有刘海 Oppo 固定40dp
    private boolean isOppoScreenHasGroove(Context context) {
        boolean isHasGroove = context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        return isHasGroove;
    }
    //判断是否是voio刘海屏 27dp
    private int NOTCH_IN_SCREEN_VOIO = 0x00000020;//是否有凹槽
    private int ROUNDED_IN_SCREEN_VOIO = 0x00000008;//是否有圆角

    private boolean isVoioScreenHasGroove(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);
        }  catch (Exception e) {

        } finally {
            return ret;
        }
    }
    //小米刘海尺寸
    private int getStatusBarHeight(Context context) {
        int defaultVal = 0;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method getMethod = cls.getMethod("getInt", String.class, int.class);
            defaultVal = (Integer)getMethod.invoke(cls, "ro.miui.notch", 0);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int statusBarHeight = 0;
        if (defaultVal == 1) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }
    //基于Google
    private int getGoogleStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    protected int getSteepStatusBarHeight() {
       int height = 0;
       if (isHuaweiScreenHasGroove(getContext()) &&
               getHuaweiNotchSize(getContext()) > 0) {
           height =  getHuaweiNotchSize(getContext());
       } else if (isOppoScreenHasGroove(getContext())) {
           height = com.linhuiba.linhuifield.connector.Constants.Dp2Px(getContext(),40);
       } else if (isVoioScreenHasGroove(getContext())) {
           height = com.linhuiba.linhuifield.connector.Constants.Dp2Px(getContext(),30);
       } else if (getStatusBarHeight(getContext()) > 0) {
           height = getStatusBarHeight(getContext());
       } else if (getGoogleStatusBarHeight(getContext()) > 0) {
           height = getGoogleStatusBarHeight(getContext());
       }
       return height;
    }


}
