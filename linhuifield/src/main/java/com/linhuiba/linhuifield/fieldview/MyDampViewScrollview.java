package com.linhuiba.linhuifield.fieldview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MyDampViewScrollview extends ScrollView {
    private OnScrollListener onScrollListener;
    private int lastScrollY;

    private static final int LEN = 0xc8;
    private static final int DURATION = 500;
    private static final int MAX_DY = 200;
    private Scroller mScroller;
    TouchTool tool;
    int left, top;
    float startX, startY, currentX, currentY;
    int imageViewH;
    int rootW, rootH;
    ImageView imageView;
    boolean scrollerType;

    private float xDistance, yDistance, xLast, yLast;
    private OnScrollToBottomListener onScrollToBottom;
    public MyDampViewScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MyDampViewScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public MyDampViewScrollview(Context context) {
        super(context);

    }


    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {

        return 0;
    }

    private int[] li = new int[2];
    private int[] li2 = new int[2];
    private float lastLy;
    private boolean startIsTop = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        int action = event.getAction();
        if (!mScroller.isFinished()) {
            return super.onTouchEvent(event);
        }
        currentX = event.getX();
        currentY = event.getY();
        imageView.getLocationInWindow(li);
        getLocationOnScreen(li2);
        imageView.getTop();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (li[1] != li2[1]) {// 判断开始触摸时，imageview和窗口顶部对齐没
                    startIsTop = false;
                }
                left = imageView.getLeft();
                top = imageView.getBottom();
                rootW = getWidth();
                rootH = getHeight();
                imageViewH = imageView.getHeight();
                startX = currentX;
                startY = currentY;
                tool = new TouchTool(imageView.getLeft(), imageView.getBottom(), imageView.getLeft(),
                        imageView.getBottom() + LEN);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!startIsTop && li[1] == li2[1]) {
                    startY = currentY;
                    startIsTop = true;
                }
                if (imageView.isShown() && imageView.getTop() >= 0) {
                    if (tool != null) {
                        int t = tool.getScrollY(currentY - startY);
                        if (!scrollerType && currentY < lastLy && imageView.getHeight() > imageViewH) {
                            scrollTo(0, 0);
                            imageView.getLocationInWindow(li);
                            getLocationOnScreen(li2);
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            imageView.setLayoutParams(params);
                            if (imageView.getHeight() == imageViewH && li[1] == li2[1]) {
                                scrollerType = true;
                            }
                            if (startIsTop && li[1] != li2[1]) {
                                startIsTop = false;
                            }
                        }
                        if (t >= top && t <= imageView.getBottom() + LEN && li[1] == li2[1] && currentY > lastLy) {
                            android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = t;
                            imageView.setLayoutParams(params);
                        }
                    }
                    scrollerType = false;
                }

                lastLy = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (li[1] == li2[1]) {
                    scrollerType = true;
                    mScroller.startScroll(imageView.getLeft(), imageView.getBottom(), 0 - imageView.getLeft(),
                            imageViewH - imageView.getBottom(), DURATION);
                    invalidate();
                }
                startIsTop = true;
                break;
        }

        return true;
    }



    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            imageView.layout(0, 0, x + imageView.getWidth(), y);
            invalidate();
            if (!mScroller.isFinished() && scrollerType && y > MAX_DY) {
                android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = y;
                imageView.setLayoutParams(params);
            }
        }
    }

    public class TouchTool {

        private int startX, startY;

        public TouchTool(int startX, int startY, int endX, int endY) {
            super();
            this.startX = startX;
            this.startY = startY;
        }

        public int getScrollX(float dx) {
            int xx = (int) (startX + dx / 2.5F);
            return xx;
        }

        public int getScrollY(float dy) {
            int yy = (int) (startY + dy / 2.5F);
            return yy;
        }
    }


    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }
    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyDampViewScrollview.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }

        }

    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }








    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener != null){
            onScrollListener.onScroll(t);
        }
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
    public interface OnScrollToBottomListener{
        public void onScrollBottomListener(boolean isBottom);
    }
}
