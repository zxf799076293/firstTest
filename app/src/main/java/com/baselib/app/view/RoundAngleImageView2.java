package com.baselib.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 圆角矩形图片
 * */
public class RoundAngleImageView2 extends SmartImageView {

	public RoundAngleImageView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RoundAngleImageView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundAngleImageView2(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();  
        int w = this.getWidth();  
        int h = this.getHeight();  
        clipPath.addRoundRect(new RectF(0, 0, w, h), 20.0f, 20.0f, Path.Direction.CW);
        canvas.clipPath(clipPath);  
        super.onDraw(canvas); 
	}
     
}
