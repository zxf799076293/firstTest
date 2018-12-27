package com.linhuiba.business.connector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ShuoMClickableSpan extends ClickableSpan {
    private String string;
    private Context context;
    private int mType;
    public ShuoMClickableSpan (String string, Context context, int type) {
        this.string = string;
        this.context = context;
        this.mType = type;
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        if (mType == 1) {
            ds.setColor(context.getResources().getColor(R.color.default_bluebg));
        } else {
            ds.setColor(context.getResources().getColor(R.color.register_edit_color));
        }

    }
    @Override
    public void onClick(View widget) {
        if (mType == 1) {
            Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + string));
            context.startActivity(intent_mobile);
        }
    }
}
