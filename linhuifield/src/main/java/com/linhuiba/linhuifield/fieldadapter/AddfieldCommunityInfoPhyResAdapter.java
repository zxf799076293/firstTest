package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.FieldAddfieldCommunityInfoActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldPhyResModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddfieldCommunityInfoPhyResAdapter extends BaseQuickAdapter<FieldAddfieldPhyResModel, BaseViewHolder> {
    private Context mContext;
    private FieldAddfieldCommunityInfoActivity mActivity;
    private List<FieldAddfieldPhyResModel> mDatas;
    private int type;
    public AddfieldCommunityInfoPhyResAdapter(int layoutResId, @Nullable List<FieldAddfieldPhyResModel> data, Context mContext, FieldAddfieldCommunityInfoActivity activity, int type) {
        super(layoutResId, data);
        this.mDatas = data;
        this.mContext = mContext;
        this.mActivity = activity;
        this.type = type;
    }
    @Override
    protected void convert(BaseViewHolder helper, FieldAddfieldPhyResModel item) {
        helper.setText(R.id.addfield_community_phy_name_tv, item.getName());
        helper.setText(R.id.addfield_community_phy_seq_tv, mContext.getResources().getString(R.string.module_addfield_community_phy_item_seq_str) +
        String.valueOf(helper.getLayoutPosition() + 1));
        ImageView imageView = (ImageView) helper.getView(R.id.addfield_community_phy_imgv);
        if (item.getResource_img().size() > 0 &&
                item.getResource_img().get(0).getPic_url() != null &&
                item.getResource_img().get(0).getPic_url().length() > 0) {
            Picasso.with(mContext).load(item.getResource_img().get(0).getPic_url() + Config.Linhui_Min_Watermark)
                    .placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                    .resize(Constants.Dp2Px(mContext,50), Constants.Dp2Px(mContext,40)).into(imageView);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small)
                    .placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                    .resize(Constants.Dp2Px(mContext,50), Constants.Dp2Px(mContext,40)).into(imageView);

        }
    }
}

