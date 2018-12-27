package com.linhuiba.linhuifield;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.linhuiba.linhuipublic.config.BaseMessageUtils;


/**
 * Created by Administrator on 2016/11/25.
 */

public class FieldBaseFragmentActivity  extends FragmentActivity {
    private ProgressDialog progressDialog;

    public synchronized void showProgressDialog(String msg) {
        showProgressDialog(msg, true);
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
}
