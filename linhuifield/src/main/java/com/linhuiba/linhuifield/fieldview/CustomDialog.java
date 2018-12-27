package com.linhuiba.linhuifield.fieldview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/12/6.
 */

public class CustomDialog extends Dialog {
    private Context context;
    private int height, width;
    private boolean cancelTouchout;
    private View view;

    private CustomDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }


    private CustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchout);
        Window win = getWindow();
        win.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        win.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT; //设置宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置宽度
        win.setAttributes(lp);
    }

    public static final class Builder {

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder heightpx(int val) {
            height = val;
            return this;
        }

        public Builder widthpx(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = Constants.Dp2Px(context, val);
            return this;
        }

        public Builder widthdp(int val) {
            width = Constants.Dp2Px(context, val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes,View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }
        public Builder setText(int viewRes,String str) {
            TextView tv = (TextView)view.findViewById(viewRes);
            tv.setText(str);
            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
            return this;
        }
        public Builder showView(int viewRes,int visible){
            view.findViewById(viewRes).setVisibility(visible);
            return this;
        }
        public Builder setFakeBoldText(int viewRes){
            TextView tv = (TextView)view.findViewById(viewRes);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            return this;
        }

        public Builder setBgColor(int viewRes,int visible){
            view.findViewById(viewRes).setBackgroundColor(visible);
            return this;
        }
        public Builder setImgvGif(Activity activity,int viewRes, int visible) {
            Glide.with(activity).load(visible)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into((ImageView) view.findViewById(viewRes));
            return this;
        }
        public Builder setOvalImgvUrl(Activity activity,int viewRes, String url,int width, int height) {
            OvalImageView ovalImageView = (OvalImageView) view.findViewById(viewRes);
            RelativeLayout.LayoutParams paramgroups= new RelativeLayout.LayoutParams(width,
                    height);
//            ovalImageView.setLayoutParams(paramgroups);
            Picasso.with(activity).load(url).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width,height)
                    .into(ovalImageView);
            return this;
        }
        public Builder setImgvUrl(Activity activity,int viewRes, String url,int width, int height) {
            ImageView ovalImageView = (ImageView) view.findViewById(viewRes);
            Picasso.with(activity).load(url).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width,height)
                    .into(ovalImageView);
            return this;
        }

        public Builder setTextColor(int viewRes,int visible){
            TextView textView = (TextView)view.findViewById(viewRes);
            textView.setTextColor(visible);
            return this;
        }
        public Builder setTextSize(int viewRes,int size){
            TextView textView = (TextView)view.findViewById(viewRes);
            textView.setTextSize(size);
            return this;
        }

        public Builder showImageView(int viewRes,int drawable){
            ImageView imageView = (ImageView) view.findViewById(viewRes);
            imageView.setBackgroundResource(drawable);
            return this;
        }

        public CustomDialog build() {
            if (resStyle != -1) {
                return new CustomDialog(this, resStyle);
            } else {
                return new CustomDialog(this);
            }
        }
    }

    public View getView() {
        return view;
    }
}
