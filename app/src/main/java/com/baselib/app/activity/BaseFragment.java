
package com.baselib.app.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by snowd on 14-8-30.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((System.currentTimeMillis() - LoginManager.getConfig_updatetime()) >  24 * 3600 * 1000 && LoginManager.getConfig_updatetime() > 0) {
            Constants constants = new Constants(getContext());
            constants.getConfig();
        }
    }

    public Context getContext() {
        return this.getActivity();
    }
    public ActionBar showActionBar(String title) {
        ActionBar mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
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

    public void showProgressDialog(String msg) {
        showProgressDialog(msg, true);
    }
    public synchronized void showProgressDialog() {
        showProgressDialog(this.getResources().getString(R.string.txt_waiting), true);
    }

    public void showProgressDialog(String msg, boolean cancelable) {
       BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) activity.showProgressDialog(msg, cancelable);
    }

    public void hideProgressDialog() {
       BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) activity.hideProgressDialog();
    }

    public void showFragmentProgressDialog(String msg, boolean cancelable) {
        BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
        if (activity != null) activity.showProgressDialog(msg, cancelable);
    }

    public void hideFragmentProgressDialog() {
        BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
        if (activity != null) activity.hideProgressDialog();
    }


//继承fragmentactivity时账号已登录处理
    protected void checkAccesstoken(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.reloadLogin(getActivity());
            //getActivity().finish();
        }
    }
    protected boolean isServerResponseCode(Throwable error,int code) {
        boolean noresource = false;
        if (Response.isFieldinfo_NoResources(error,code)) {
            noresource = true;
        } else {
            noresource = false;
        }
        return noresource;
    }
    protected void setSteepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    protected void setNoSteepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.default_bluebg));
        }
    }


}
