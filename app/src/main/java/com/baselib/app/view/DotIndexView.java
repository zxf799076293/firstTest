package com.baselib.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class DotIndexView extends View {

    private static final int DEFAULT_MARGIN = 40;

    private int mTotal = 3;

    private int mCurrent = 1;

    private int mMargin = DEFAULT_MARGIN;

    private Bitmap mNormalDot;
    private Bitmap mSelectedDot;

    private Rect mRectSrc = new Rect();
    private Rect[] mRectDst;

    private Paint mPaint;

    public DotIndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DotIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotIndexView(Context context) {
        super(context);
    }

    private void init() {
        mRectSrc = new Rect(0, 0, mNormalDot.getWidth(), mNormalDot.getHeight());
    }

    private void prepared() {
        init();
        mPaint = new Paint();
        mRectDst = new Rect[mTotal];
        for (int i = 0; i < mTotal; i++) {
            int left = i * (mMargin + mNormalDot.getWidth());
            mRectDst[i] = new Rect(left, 0, left + mNormalDot.getWidth(), mNormalDot.getHeight());
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(outMetrics);
        measure(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRectSrc != null) {
            int dotWidth = mRectSrc.right - mRectSrc.left;
            int dotHeight = mRectSrc.bottom - mRectSrc.top;
            int width = mTotal * dotWidth + (mTotal - 1) * mMargin;
            setMeasuredDimension(width & MEASURED_SIZE_MASK, dotHeight & MEASURED_SIZE_MASK);
        }
    }

    public void setDotBitmap(Bitmap normal, Bitmap select) {
        if (normal != null && !normal.isRecycled()) {
            mNormalDot = normal;
        }
        if (select != null && !normal.isRecycled()) {
            mSelectedDot = select;
        }
        mRectSrc = new Rect(0, 0, mNormalDot.getWidth(), mNormalDot.getHeight());
    }

    public void setDotBitmapResource(int normal, int select) {
        mNormalDot = BitmapFactory.decodeResource(getResources(), normal);
        mSelectedDot = BitmapFactory.decodeResource(getResources(), select);
        mRectSrc = new Rect(0, 0, mNormalDot.getWidth(), mNormalDot.getHeight());
    }

    public void setMarginBetweenDot(int margin) {
        mMargin = margin;
    }

    public void setTotal(int total) {
        mTotal = total;
        prepared();
    }

    public void setCurrent(int current) {
        mCurrent = current;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNormalDot == null || mSelectedDot == null) {
            return;
        }
        Bitmap bitmap = mNormalDot;
        for (int i = 0; i < mTotal; i++) {
            if (i == mCurrent) {
                bitmap = mSelectedDot;
            } else {
                bitmap = mNormalDot;
            }
            Rect dst = mRectDst[i];
            canvas.drawBitmap(bitmap, mRectSrc, dst, mPaint);
        }
    }

}