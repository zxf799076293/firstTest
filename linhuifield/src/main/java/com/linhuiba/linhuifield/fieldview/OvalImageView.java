package com.linhuiba.linhuifield.fieldview;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;

/**
 * Created by Administrator on 2017/4/25.
 */

public class OvalImageView extends ImageView {
    private Paint mPaint;
    private Paint mPaint2;
    private boolean leftup;
    private boolean rightup;
    private boolean leftdown;
    private boolean rightdown;
    private int roundHeight = 10;
    private int roundWidth = 10;

    public OvalImageView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public OvalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.OvalImageView);
            leftup = a.getBoolean(R.styleable.OvalImageView_show_leftup, false);
            rightup = a.getBoolean(R.styleable.OvalImageView_show_rightup, false);
            leftdown = a.getBoolean(R.styleable.OvalImageView_show_leftdown, false);
            rightdown = a.getBoolean(R.styleable.OvalImageView_show_rightdown, false);
            roundHeight = Constants.Dp2Px(context, a.getInt(R.styleable.OvalImageView_round, 10));
            roundWidth = Constants.Dp2Px(context, a.getInt(R.styleable.OvalImageView_round, 10));
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        init();
    }

    public OvalImageView(Context context) {
        super(context);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        //16种状态
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPaint2 = new Paint();
        mPaint2.setXfermode(null);
    }



    @Override
    public void onDraw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.onDraw(canvas2);
        if (leftup) {
            drawLeftUp(canvas2);
        }
        if (rightup) {
            drawRightUp(canvas2);
        }
        if (leftdown) {
            drawLeftDown(canvas2);
        }
        if (rightdown) {
            drawRightDown(canvas2);
        }
        canvas.drawBitmap(bitmap, 0, 0, mPaint2);
        bitmap.recycle();
    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        //arcTo的第二个参数是以多少度为开始点，第三个参数-90度表示逆时针画弧，正数表示顺时针
        path.arcTo(new RectF(0,0,roundWidth*2,roundHeight*2),-90,-90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight()-roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0,getHeight()-roundHeight*2,0+roundWidth*2,getHeight()),90,90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth()-roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight()-roundHeight);
        path.arcTo(new RectF(getWidth()-roundWidth*2,getHeight()-roundHeight*2,getWidth(),getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth()-roundWidth, 0);
        path.arcTo(new RectF(getWidth()-roundWidth*2,0,getWidth(),0+roundHeight*2),-90,90);
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
