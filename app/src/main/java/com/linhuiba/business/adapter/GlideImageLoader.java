package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/15.
 */

public class GlideImageLoader extends ImageLoader {
    private int mNewWidth;
    private int mNewHeight;
    private Activity mActivity;
    DisplayMetrics metric;
    public GlideImageLoader(Activity activity, int width, int height) {
        this.mActivity = activity;
        metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.mNewWidth = metric.widthPixels;
        this.mNewHeight = (height * mNewWidth) / width;
    }
    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        if (url != null && url.toString().length() > 0) {
            Picasso.with(context).load(url.toString())
                    .resize(mNewWidth, mNewHeight)
                    .placeholder(com.linhuiba.linhuifield.R.drawable.ic_jiazai_big)
                    .error(com.linhuiba.linhuifield.R.drawable.ic_no_pic_big)
                    .into(imageView);
        }
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
//    @Override
//    public ImageView createImageView(Context context) {
//        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
////        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
////        return simpleDraweeView;
//        return null;
//    }
}
