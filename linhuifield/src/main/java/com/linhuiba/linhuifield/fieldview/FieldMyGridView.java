package com.linhuiba.linhuifield.fieldview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/6/8.
 */
public class FieldMyGridView extends GridView {
    public FieldMyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMyGridView(Context context) {
        super(context);
    }

    public FieldMyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}