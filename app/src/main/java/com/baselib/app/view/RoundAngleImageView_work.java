package com.baselib.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by necsthz on 2015/06/05.
 */
public class RoundAngleImageView_work extends SmartImageView {
    public RoundAngleImageView_work(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoundAngleImageView_work(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundAngleImageView_work(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        float[] rect = {15,15,15,15,0,0,0,0};
        clipPath.addRoundRect(new RectF(0, 0, w, h), rect, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
