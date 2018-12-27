package com.linhuiba.linhuifield.fieldview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.CommonValue;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class FieldLoadMoreExpandablelistView extends ExpandableListView {
    public FieldLoadMoreExpandablelistView(final Context context) {
        super(context);
    }

    public FieldLoadMoreExpandablelistView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldLoadMoreExpandablelistView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FieldLoadMoreExpandablelistView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        //经过初步测试，只有在Android 7.0平台以上的系统才会出现软键盘自动切换的问题。
        if (Build.VERSION.SDK_INT >= 24 && changed)
            super.onLayout(changed, l, t, r, b);
        else
            super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                , View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
