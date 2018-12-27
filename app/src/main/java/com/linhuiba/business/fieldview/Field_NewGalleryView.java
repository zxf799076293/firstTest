package com.linhuiba.business.fieldview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by Administrator on 2016/1/27.
 */
public class Field_NewGalleryView extends Gallery {
    public Field_NewGalleryView(Context context) {
        super(context);
    }

    public Field_NewGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public Field_NewGalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);

//        if (this.getSelectedItemPosition() == 0) {// 实现后退功能
//            this.setSelection(3);
//        }
        return false;

    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

}
