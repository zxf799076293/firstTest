package com.linhuiba.linhuifield.fieldbasemvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import com.linhuiba.linhuifield.FieldBaseFragmentActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

/**
 * Created by Administrator on 2018/2/27.
 */

public class FieldBaseMvpFragment extends Fragment implements FieldBaseView {
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
    @Override
    public void onPause() {
        super.onPause();
    }

    protected void resolveIntent() {
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
        return this.getActivity();
    }

    @Override
    public void showToast(String msg) {
        BaseMessageUtils.showToast(msg);
    }

    protected void initApp() {
    }

    public ActionBar showActionBar(String title) {
        ActionBar mActionBar = ((FieldBaseMvpActivity) getActivity()).getSupportActionBar();
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
        FieldBaseMvpActivity activity = (FieldBaseMvpActivity) getActivity();
        if (activity != null) activity.showProgressDialog(msg, cancelable);
    }

    public void hideProgressDialog() {
        FieldBaseMvpActivity activity = (FieldBaseMvpActivity) getActivity();
        if (activity != null) activity.hideProgressDialog();
    }

    public void showFragmentProgressDialog(String msg, boolean cancelable) {
        FieldBaseFragmentActivity activity = (FieldBaseFragmentActivity) getActivity();
        if (activity != null) activity.showProgressDialog(msg, cancelable);
    }

    public void hideFragmentProgressDialog() {
        FieldBaseFragmentActivity activity = (FieldBaseFragmentActivity) getActivity();
        if (activity != null) activity.hideProgressDialog();
    }


    //继承fragmentactivity时账号已登录处理
    protected void checkAccess(Throwable error){
        if (Response.isAccesstokenError(error)) {
            LoginManager.getInstance().clearLoginInfo();
            Intent intent = new Intent("com.business.loginActivity");
            startActivity(intent);
            this.getActivity().finish();
        }
    }
    public void showSwitchDialog(int role, String msg, boolean isNoDiss) {
        FieldBaseMvpActivity activity = (FieldBaseMvpActivity) getActivity();
        if (activity != null) activity.showSwitchDialog(role, msg, isNoDiss);
    }
    public void hideSwitchDialog() {
        FieldBaseMvpActivity activity = (FieldBaseMvpActivity) getActivity();
        if (activity != null) activity.hideSwitchDialog();
    }
}
