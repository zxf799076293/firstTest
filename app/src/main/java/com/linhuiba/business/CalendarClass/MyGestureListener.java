package com.linhuiba.business.CalendarClass;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.baselib.app.util.MessageUtils;

/**
 * Created by Administrator on 2017/9/27.
 */

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    /** 左右滑动的最短距离 */
    private int distance = 120;
    /** 左右滑动的最大速度 */
    private int velocity = 200;

    private GestureDetector gestureDetector;

    public MyGestureListener(Context context) {
        super();
        gestureDetector = new GestureDetector(context, this);
    }

    /**
     * 向左滑的时候调用的方法，子类应该重写
     * @return
     */
    public boolean left() {
        return false;
    }

    /**
     * 向右滑的时候调用的方法，子类应该重写
     * @return
     */
    public boolean right() {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度（像素/秒）
        // velocityY：Y轴上的移动速度（像素/秒）

        // 向左滑
        if (e1.getX() - e2.getX() > distance
                && Math.abs(velocityX) > velocity) {
            MessageUtils.showToast("向左");
        }
        // 向右滑
        if (e2.getX() - e1.getX() > distance
                && Math.abs(velocityX) > velocity) {
            MessageUtils.showToast("向右");
        }
        return false;
    }
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}