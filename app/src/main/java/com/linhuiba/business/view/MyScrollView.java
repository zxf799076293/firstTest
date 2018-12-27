package com.linhuiba.business.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.util.AttributeSet;

import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MyScrollView extends ScrollView {
    private OnScrollListener onScrollListener;
    private float xDistance, yDistance, xLast, yLast;
    private OnScrollToBottomListener onScrollToBottom;
    private OnScrollSlideLintener mScrollSlideLintener;
    /**
     * 上次滑动的时间
     */
    private long lastScrollUpdate = -1;
    /**
     * Runnable延迟执行的时间
     */
    private long delayMillis = 100;
    private Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                if (mScrollSlideLintener != null) {
                    mScrollSlideLintener.onScrollSlide(false);//滑动结束
                }
            } else {
                postDelayed(this, delayMillis);
            }
        }
    };
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
        if (mScrollSlideLintener != null) {
            if (lastScrollUpdate == -1) {
                mScrollSlideLintener.onScrollSlide(true);//滑动开始
                postDelayed(scrollerTask, delayMillis);
            }
            // 更新ScrollView的滑动时间
            lastScrollUpdate = System.currentTimeMillis();
        }

    }
    /**
     *
     * 滚动的回调接口
     *
     * @author xiaanming
     *
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *
         */
        public void onScroll(int scrollY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY != 0 && null != onScrollToBottom){
            onScrollToBottom.onScrollBottomListener(clampedY);
        }
    }
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }
    public interface OnScrollToBottomListener {
        public void onScrollBottomListener(boolean isBottom);
    }
    public void setOnScrollSlideLintener(OnScrollSlideLintener listener){
        mScrollSlideLintener = listener;
    }
    public interface OnScrollSlideLintener {
        public void onScrollSlide(boolean isSlide);
    }
}
