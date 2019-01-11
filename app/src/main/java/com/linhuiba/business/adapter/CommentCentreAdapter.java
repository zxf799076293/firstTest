package com.linhuiba.business.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentCentreAdapter extends BaseQuickAdapter<ReviewModel, BaseViewHolder> {
    private List<ReviewModel> mDatas;
    private Context mContext;
    public CommentCentreAdapter(int layoutResId, @Nullable List<ReviewModel> data,
                                Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
        this.mDatas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReviewModel item) {

    }
}
