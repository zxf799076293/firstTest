package com.linhuiba.business.fieldview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Field_MyGridView extends GridView {
    public Field_MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Field_MyGridView(Context context) {
        super(context);
    }

    public Field_MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}