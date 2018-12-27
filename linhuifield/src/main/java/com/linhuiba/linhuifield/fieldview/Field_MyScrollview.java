package com.linhuiba.linhuifield.fieldview;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/19.
 */

public class Field_MyScrollview extends ScrollView {
    private OnScrollListener onScrollListener;
    private OnScrollToBottomListener onScrollToBottom;
    public Field_MyScrollview(Context context) {
        super(context);
    }

    public Field_MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Field_MyScrollview(Context context, AttributeSet attrs, int defStyle) {
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
        View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d == 0) {
            if (onScrollToBottom != null) {
                onScrollToBottom.onScrollBottomListener(true);
            }
        }
        if(onScrollListener != null){
            onScrollListener.onScroll(t);
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
         * 回调方法， 返回Field_MyScrollview滑动的Y方向距离
         * @param scrollY
         *
         */
        public void onScroll(int scrollY);
    }
    //RecyclerView冲突
//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
//                                  boolean clampedY) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        if(scrollY != 0 && null != onScrollToBottom){
//            onScrollToBottom.onScrollBottomListener(clampedY);
//        }
//    }
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }
    public interface OnScrollToBottomListener {
        public void onScrollBottomListener(boolean isBottom);
    }
}
