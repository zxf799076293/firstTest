package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.OnMultiClickListener;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ActivityCasePicSaveAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context mContext;
    private int width;
    private int height;
    private static HashMap<Integer, Boolean> isChecked = new HashMap<Integer, Boolean>();
    public ActivityCasePicSaveAdapter(int layoutResId, @Nullable List<String> data, Context context, Activity activity) {
        super(layoutResId, data);
        this.mContext = context;
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        RelativeLayout imgRL = (RelativeLayout) helper.getView(R.id.case_pic_save_rl);
        OvalImageView imgv = (OvalImageView) helper.getView(R.id.case_pic_save_imgv);
        CheckBox chooseCB = (CheckBox) helper.getView(R.id.case_pic_save_cb);
        LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width -
                Constants.Dp2Px(mContext,23))/4,
                (width -
                        Constants.Dp2Px(mContext,23))/4);
        imgRL.setLayoutParams(paramgroups);
        if (item != null &&
                item.length() > 0) {
            Picasso.with(mContext).load(item).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize((width -
                    Constants.Dp2Px(mContext,23))/4, (width -
                    Constants.Dp2Px(mContext,23))/4).into(imgv);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small).resize((width -
                    Constants.Dp2Px(mContext,23))/4, (width -
                    Constants.Dp2Px(mContext,23))/4).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(imgv);
        }
        if (isChecked.get(helper.getLayoutPosition())) {
            chooseCB.setChecked(true);
        } else {
            chooseCB.setChecked(false);
        }
        chooseCB.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isChecked.get(helper.getLayoutPosition())) {
                    isChecked.put(helper.getLayoutPosition(),false);
                } else {
                    isChecked.put(helper.getLayoutPosition(),true);
                }
                notifyItemChanged(helper.getLayoutPosition());
            }
        });
    }

    public static HashMap<Integer, Boolean> getIsChecked() {
        return isChecked;
    }

    public static void setIsChecked(HashMap<Integer, Boolean> isChecked) {
        ActivityCasePicSaveAdapter.isChecked = isChecked;
    }
    public static void cleanIsChecked() {
        if (isChecked != null) {
            isChecked.clear();
        }
    }

}
