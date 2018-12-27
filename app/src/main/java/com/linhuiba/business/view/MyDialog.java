package com.linhuiba.business.view;

import android.app.Dialog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.linhuiba.business.R;

/**
 * Created by Administrator on 2017/9/27.
 */

public class MyDialog extends Dialog {
    private GestureDetector mGestureDetector;
    public MyDialog(Context context) {
        super(context);
        // new一个自定义的手势监听
        this.mGestureDetector = new GestureDetector(context, new ViewGestureListener());
    }
    public boolean onTouchEvent(MotionEvent event) {

        return mGestureDetector.onTouchEvent(event);
    }


    class ViewGestureListener implements GestureDetector.OnGestureListener {
        // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
        public boolean onDown(MotionEvent e) {
            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
        // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
        public void onLongPress(MotionEvent e) {
        }

        // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


            return false;
        }

        // 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
        // 注意和onDown()的区别，强调的是没有松开或者拖动的状态
        public void onShowPress(MotionEvent e) {
        }

        // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }// end class ViewGestureListener
}
