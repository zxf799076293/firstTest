package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.ActivityCaseActivity;
import com.linhuiba.business.activity.ActivityCaseInfoActivity;
import com.linhuiba.business.model.CaseListModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/6/27.
 */

public class ActivityCaseAdapterTmp extends BaseQuickAdapter<CaseListModel, BaseViewHolder> {
    public static List<Integer> heights = new ArrayList<>();
    public static List<Integer> caseInfoHeights = new ArrayList<>();
    private ArrayList<CaseListModel> mDatas;
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private int type;
    private ActivityCaseActivity mActivityCaseActivity;
    private ActivityCaseInfoActivity mActivityCaseInfo;
    public ActivityCaseAdapterTmp(int layoutResId, @Nullable ArrayList<CaseListModel> data, Context mContext, ActivityCaseActivity activity, int type) {
        super(layoutResId, data);
        this.mDatas = data;
        this.mContext = mContext;
        this.mActivityCaseActivity = activity;
        this.type = type;
        mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }
    public ActivityCaseAdapterTmp(int layoutResId, @Nullable ArrayList<CaseListModel> data, Context mContext, ActivityCaseInfoActivity activity, int type) {
        super(layoutResId, data);
        this.mDatas = data;
        this.mContext = mContext;
        this.mActivityCaseInfo = activity;
        this.type = type;
        mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    @Override
    protected void convert(BaseViewHolder helper, CaseListModel item) {
//        helper.setText(R.id.activity_case_tv,String.valueOf(helper.getLayoutPosition()*100));
        helper.setText(R.id.activity_case_tv,item.getTitle());
        ViewGroup.LayoutParams params =  helper.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        if (helper.getLayoutPosition() < mDatas.size()) {
            int heightListSize;
            if (type == 0) {
                heightListSize = heights.size();
            } else {
                heightListSize = caseInfoHeights.size();
            }
            int imgvHeight;
            if (helper.getLayoutPosition() >=  heightListSize) {
                Random random = new Random();
                imgvHeight = com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,(int)random.nextInt(300 - 190) + 190);
                params.height = imgvHeight;//把随机的高度赋予item布局
            } else {
                if (type == 0) {
                    imgvHeight = heights.get(helper.getLayoutPosition());
                } else {
                    imgvHeight = caseInfoHeights.get(helper.getLayoutPosition());
                }
                params.height = imgvHeight;//把随机的高度赋予item布局
            }
            helper.itemView.setLayoutParams(params);//把params设置给item布局
            RelativeLayout imgvRL = (RelativeLayout)helper.getView(R.id.activity_case_rl);
            OvalImageView imgv = (OvalImageView)helper.getView(R.id.activity_case_imgv);
            RelativeLayout.LayoutParams imgvParams= new RelativeLayout.LayoutParams(mDisplayMetrics.widthPixels / 2 - Constants.Dp2Px(mContext,20),
                    imgvHeight - Constants.Dp2Px(mContext,36));
            imgvRL.setLayoutParams(imgvParams);
            if (item.getCover_pic_url() != null && item.getCover_pic_url().length() > 0) {
                Picasso.with(mContext).load(item.getCover_pic_url()).placeholder(com.linhuiba.linhuifield.R.drawable.ic_jiazai_small).error(com.linhuiba.linhuifield.R.drawable.ic_no_pic_small)
                        .resize(mDisplayMetrics.widthPixels / 2 - Constants.Dp2Px(mContext,20),
                                imgvHeight  - Constants.Dp2Px(mContext,36)).centerCrop().into(imgv);
            }
            TextView tv = (TextView)helper.getView(R.id.activity_case_tv);
            ViewGroup.LayoutParams tvParams = tv.getLayoutParams();//得到item的LayoutParams布局参数
            tvParams.height = Constants.Dp2Px(mContext,36);//把随机的高度赋予item布局
            tv.setLayoutParams(tvParams);
            if (type == 1) {
                helper.itemView.setOnTouchListener(new View.OnTouchListener() {
                    // 将gridview中的触摸事件回传给gestureDetector
                    public boolean onTouch(View v, MotionEvent event) {
                        return mActivityCaseInfo.mGestureDetector.onTouchEvent(event);
                    }
                });
            }
        }
    }

    public static List<Integer> getHeights() {
        return heights;
    }

    public static void setHeights(List<Integer> heights) {
        ActivityCaseAdapterTmp.heights = heights;
    }

    public static List<Integer> getCaseInfoHeights() {
        return caseInfoHeights;
    }

    public static void setCaseInfoHeights(List<Integer> caseInfoHeights) {
        ActivityCaseAdapterTmp.caseInfoHeights = caseInfoHeights;
    }

    public static void clearHeights() {
        if (heights != null) {
            heights.clear();
        }
    }
    public static void clearCaseInfoHeights() {
        if (caseInfoHeights != null) {
            caseInfoHeights.clear();
        }
    }


}
