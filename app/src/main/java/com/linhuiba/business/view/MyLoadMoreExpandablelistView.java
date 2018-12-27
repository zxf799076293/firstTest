package com.linhuiba.business.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/7/3.
 */

public class MyLoadMoreExpandablelistView extends ExpandableListView {
    public MyLoadMoreExpandablelistView(Context context) {
        super(context);
    }

    public MyLoadMoreExpandablelistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLoadMoreExpandablelistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLoadMoreExpandablelistView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                , View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
