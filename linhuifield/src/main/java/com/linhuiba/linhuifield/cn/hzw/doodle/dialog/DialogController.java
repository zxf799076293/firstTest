package com.linhuiba.linhuifield.cn.hzw.doodle.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.cn.hzw.doodle.util.DrawUtil;

/**
 * Created by huangziwei on 2017/4/21.
 */

public class DialogController {

    public static Dialog showInputTextDialog(Activity activity, final String text, final View.OnClickListener enterClick, final View.OnClickListener cancelClick) {
        boolean fullScreen = (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        final Dialog dialog = getDialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        ViewGroup container = (ViewGroup) View.inflate(activity, R.layout.module_dialog_doodle_create_text, null);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(container);
        if (fullScreen) {
            DrawUtil.assistActivity(dialog.getWindow());
        }

        final EditText textView = (EditText) container.findViewById(R.id.doodle_selectable_edit);
        final View cancelBtn = container.findViewById(R.id.doodle_text_cancel_btn);
        final TextView enterBtn = (TextView) container.findViewById(R.id.doodle_text_enter_btn);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = (textView.getText() + "").trim();
                if (TextUtils.isEmpty(text)) {
                    enterBtn.setEnabled(false);
                    enterBtn.setTextColor(0xffb3b3b3);
                } else {
                    enterBtn.setEnabled(true);
                    enterBtn.setTextColor(0xff232323);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textView.setText(text == null ? "" : text);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (cancelClick != null) {
                    cancelClick.onClick(cancelBtn);
                }
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (enterClick != null) {
                    enterBtn.setTag((textView.getText() + "").trim());
                    enterClick.onClick(enterBtn);
                }
            }
        });
        return dialog;
    }


    private static Dialog getDialog(Activity activity) {
        boolean fullScreen = (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        Dialog dialog = null;
        if (fullScreen) {
            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        } else {
            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        }
        return dialog;
    }

}
