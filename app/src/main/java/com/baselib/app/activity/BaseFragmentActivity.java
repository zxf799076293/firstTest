package com.baselib.app.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.baselib.app.util.MessageUtils;

/**
 * Created by 2172980000632 on 2015/05/21.
 */
public class BaseFragmentActivity extends FragmentActivity {


    private ProgressDialog progressDialog;

    public synchronized void showProgressDialog(String msg) {
        showProgressDialog(msg, true);
    }

    public synchronized void showProgressDialog(String msg, boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = MessageUtils.getProgressDialog(this, msg);
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


}
