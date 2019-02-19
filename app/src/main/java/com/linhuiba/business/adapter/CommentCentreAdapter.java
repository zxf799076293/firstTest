package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.CommentCentreActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentCentreAdapter extends BaseQuickAdapter<ReviewModel, BaseViewHolder> {
    private List<ReviewModel> mDatas;
    private CommentCentreActivity mContext;
    public CommentCentreAdapter(int layoutResId, @Nullable List<ReviewModel> data,
                                CommentCentreActivity mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
        this.mDatas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ReviewModel item) {
        ImageView mCommentImgv = (ImageView)helper.getView(R.id.comment_item_imgv);
        TextView mCommentName = (TextView)helper.getView(R.id.comment_item_name_tv);
        final TextView mCommentSize = (TextView)helper.getView(R.id.comment_item_size_tv);
        TextView mCommentCommentedTV = (TextView)helper.getView(R.id.comment_item_commented_tv);
        TextView mCommentUnCommentedTV = (TextView)helper.getView(R.id.comment_item_uncommented_tv);
        TextView mCommentDateTV = (TextView)helper.getView(R.id.comment_item_date_tv);
        if (item.getPhysical_resource_first_img() != null &&
                item.getPhysical_resource_first_img().getPic_url() != null &&
                item.getPhysical_resource_first_img().getPic_url().length() > 0) {
                Picasso.with(mContext).load(item.getPhysical_resource_first_img().getPic_url() + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(160, 160).into(mCommentImgv);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small).resize(160, 160).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(mCommentImgv);
        }
        if (item.getFull_name() != null && item.getFull_name().length() > 0) {
            mCommentName.setText(item.getFull_name());
        } else {
            mCommentName.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        String size = "";
        if (item.getSizes() != null && item.getSizes().size() > 0) {
            for (int i = 0; i < item.getSizes().size(); i++) {
                String itemSize = "";
                if (item.getSizes().get(i).getSize() != null &&
                        item.getSizes().get(i).getSize().length() > 0) {
                    itemSize = item.getSizes().get(i).getSize();
                }
                if (item.getSizes().get(i).getType() != null &&
                        item.getSizes().get(i).getType().length() > 0) {
                    if (itemSize.length() > 0) {
                        itemSize = itemSize + "、";
                    }
                    itemSize = itemSize + item.getSizes().get(i).getType();
                }
                if (item.getSizes().get(i).getCustom_dimension() != null &&
                        item.getSizes().get(i).getCustom_dimension().length() > 0) {
                    if (itemSize.length() > 0) {
                        itemSize = itemSize + "、";
                    }
                    itemSize = itemSize + item.getSizes().get(i).getCustom_dimension();
                }
                if (size.length() > 0) {
                    size = size + ",";
                }
                size = size + itemSize;
            }
            if (size.length() > 0) {
                mCommentSize.setText(mContext.getResources().getString(R.string.module_comment_centre_item_size) + size);
            } else {
                mCommentSize.setText(mContext.getResources().getString(R.string.module_comment_centre_item_size) +
                        mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
        } else {
            mCommentSize.setText(mContext.getResources().getString(R.string.module_comment_centre_item_size) +
                    mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (item.getReviewed() == 1) {
            mCommentCommentedTV.setVisibility(View.VISIBLE);
            mCommentUnCommentedTV.setVisibility(View.GONE);
        } else {
            mCommentCommentedTV.setVisibility(View.GONE);
            mCommentUnCommentedTV.setVisibility(View.VISIBLE);
            mCommentUnCommentedTV.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    //2019/1/9 点击跳转到发表评价
                    Intent commentIntent = new Intent(mContext, PublishReviewActivity.class);
                    item.setSize(mCommentSize.getText().toString());
                    ReviewModel reviewModel = item;
                    commentIntent.putExtra("model", reviewModel);
                    mContext.startActivityForResult(commentIntent,1);
                }
            });
        }
        if (item.getExecute_time() != null && item.getExecute_time().length() > 0) {
            mCommentDateTV.setText(mContext.getResources().getString(R.string.module_comment_centre_item_execute_time) +
                    item.getExecute_time());
        } else {
            mCommentDateTV.setText(mContext.getResources().getString(R.string.module_comment_centre_item_execute_time) +
                    mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
    }
}
