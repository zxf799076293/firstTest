package com.linhuiba.linhuifield.fieldadapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/16.
 */

public class FieldBannerImageLoader extends ImageLoader {
    private int mNewWidth;
    private int mNewHeight;
    private Activity mActivity;

    public FieldBannerImageLoader(Activity activity, int width, int height) {
        this.mNewWidth = width;
        this.mNewHeight = height;
        this.mActivity = activity;
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.mNewWidth = metric.widthPixels;     // 屏幕宽度（像素）
        this.mNewHeight = mNewWidth * height / width;
    }

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        Picasso.with(context).load(url.toString())
                .resize(mNewWidth, mNewHeight)
                .placeholder(com.linhuiba.linhuifield.R.drawable.ic_jiazai_big)
                .error(com.linhuiba.linhuifield.R.drawable.ic_no_pic_big)
                .into(imageView);
    }
}
