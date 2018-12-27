package com.linhuiba.linhuipublic.config;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.linhuiba.linhui_public.R;

/**
 * Created by Administrator on 2016/12/31.
 */

public class BaseMessageUtils {
    public static final int TOAST_DURATION = 3000;

    private static Toast mToast = null;

    private static boolean isHasSysMsg = false;
    private static boolean isHasVerifyMsg = false;
    private static boolean isHasJoinMsg = false;

    public static void showToast(int resId) {
        showToast(null, BaseApplication.context.getString(resId));
    }

    public static void showToast(String tips) {
        showToast(null, (null == tips || TextUtils.isEmpty(tips.trim())) ? "unknow" : tips);
    }

    public static void showToast(int resId, int time) {
        String toastString = BaseApplication.context.getString(resId);
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.context, "", time);
        }
        mToast.setText(toastString);
        mToast.show();
    }

    public static void showToast(Context context, String toastString) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.context, "", Toast.LENGTH_LONG);
        }
        mToast.setText(toastString);
        mToast.setGravity(Gravity.CENTER, 0, 0);
//        View view= LayoutInflater.from(XalabApplication.context).inflate(R.layout.activity_fieldinfo_dialogview, null);
//        mToast.setView(view);
        mToast.show();
    }

    public static void showToastCenter(String toastString) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.context, "", Toast.LENGTH_LONG);
        }
        mToast.setText(toastString);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener okListener,
                                  DialogInterface.OnClickListener cancelListener) {
        createDialog(context, title, message, R.string.confirm, okListener, R.string.cancel,
                cancelListener).show();
    }

    public static void showDialog(Context context, String strTitle, String strText,
                                  DialogInterface.OnClickListener listener, boolean showNegative) {
        DialogInterface.OnClickListener onNegativeListener = null;
        if (showNegative) {
            onNegativeListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        createDialog(context, strTitle, strText, R.string.confirm, listener, 0, onNegativeListener)
                .show();
    }
    public static void showDialog(Context context,AlertDialog dialog, String title, String message,
                                  DialogInterface.OnClickListener okListener,
                                  DialogInterface.OnClickListener cancelListener) {
        dialog =   createDialog(context, title, message, R.string.confirm, okListener, R.string.cancel,
                cancelListener);
        dialog.show();
    }

    public static AlertDialog createDialog(Context context, String title, String msg,
                                           int positiveBtnTx, DialogInterface.OnClickListener okListener,
                                           int negativeBtnTxt, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder dialog = createHoloBuilder(context);
        if (null != cancelListener) {
            negativeBtnTxt = negativeBtnTxt > 0 ? negativeBtnTxt : R.string.cancel;
            dialog.setNegativeButton(negativeBtnTxt, cancelListener);
        }
        positiveBtnTx = positiveBtnTx > 0 ? positiveBtnTx : R.string.confirm;
        dialog.setPositiveButton(positiveBtnTx, okListener);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setMessage(msg);
        return dialog.create();
    }

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    public static ProgressDialog getProgressDialog(Context context, int stringId) {
        ProgressDialog progressDialog = getProgressDialog(context);
        progressDialog.setMessage(context.getString(stringId));
        return progressDialog;
    }

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = getProgressDialog(context);
        progressDialog.setMessage(msg);
        return progressDialog;
    }

    public static AlertDialog.Builder createHoloBuilder(Context context) {
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        return builder;
    }

    public static void setIsHasSysMsg(boolean flg)
    {
        isHasSysMsg = flg;
    }

    public static boolean getIsHasSysMsg()
    {
        return isHasSysMsg;
    }

    public static void setIsHasVerifyMsg(boolean flg)
    {
        isHasVerifyMsg = flg;
    }

    public static boolean getIsHasVerifyMsg()
    {
        return isHasVerifyMsg;
    }

    public static void setIsHasJoinMsg(boolean flg)
    {
        isHasJoinMsg = flg;
    }

    public static boolean getIsHasJoinMsg()
    {
        return isHasJoinMsg;
    }
}
