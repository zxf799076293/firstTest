package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.model.MapCommunityInfoModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MapOtherPhyAdapter extends BaseQuickAdapter<MapCommunityInfoModel, BaseViewHolder> {
    private List<MapCommunityInfoModel> data = new ArrayList<MapCommunityInfoModel>();
    private Context mcontext;
    private Activity mactivity;
    private int type;

    public MapOtherPhyAdapter(Context context, Activity activity,
                              int layoutResId, @Nullable List<MapCommunityInfoModel> data, int type) {
        super(layoutResId, data);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = data;
        this.type = type;
    }
    @Override
    protected void convert(BaseViewHolder helper, MapCommunityInfoModel item) {
        if (data != null) {
            if (data.size() > 0) {
                ImageView mRecommendResImg = (ImageView) helper.getView(R.id.map_item_other_phy_info_imgv);
                TextView mRecommendResPriceTV = (TextView) helper.getView(R.id.map_other_phy_price_tv);
                TextView mRecommendResNameTV = (TextView) helper.getView(R.id.map_item_other_phy_name_tv);
                TextView mNumberOfPeopleTV = (TextView) helper.getView(R.id.map_number_of_people_tv);
                TextView mNumberOfOrderTV = (TextView) helper.getView(R.id.map_number_of_order_tv);
                TextView mOtherPhySubsidyLabelTV = (TextView) helper.getView(R.id.map_item_other_phy_subsidy_label_tv);
                TextView mOtherPhyNoDepositTV = (TextView) helper.getView(R.id.map_other_phy_no_deposit_tv);
                TextView mOtherPhyIndoorTV = (TextView) helper.getView(R.id.map_other_phy_indoor_tv);
                TextView mOtherPhySubsidyTV = (TextView) helper.getView(R.id.map_item_subsidy_tv);
                TextView mOtherPhyCouponTV = (TextView) helper.getView(R.id.map_item_coupon_tv);

                if (item.getPhysical_resource_first_img() != null && item.getPhysical_resource_first_img().getPic_url() != null &&
                        item.getPhysical_resource_first_img().getPic_url().length() > 0) {
                    Picasso.with(mcontext).load(item.getPhysical_resource_first_img().getPic_url().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).
                            error(R.drawable.ic_no_pic_big).resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,100),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,100)).into(mRecommendResImg);
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,100)
                            , com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,100)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(mRecommendResImg);
                }
                if (item.getHas_coupons() == 1) {
                    mOtherPhyCouponTV.setVisibility(View.VISIBLE);
                } else {
                    mOtherPhyCouponTV.setVisibility(View.GONE);
                }
                if (item.getIs_subsidy() == 1) {
                    mOtherPhySubsidyTV.setVisibility(View.VISIBLE);
                } else {
                    mOtherPhySubsidyTV.setVisibility(View.GONE);
                }
                if (item.getDisplay_price() != null && item.getDisplay_price().length() > 0) {
                    if (item.getDisplay_price().trim().equals(mcontext.getResources().getString(R.string.enquary_order_discuss_personally_tv_str))) {
                        mRecommendResPriceTV.setText(item.getDisplay_price());
                    } else {
                        mRecommendResPriceTV.setText(Constants.getpricestring(
                                item.getDisplay_price(),0.01));
                    }
                } else {
                    mRecommendResPriceTV.setText("");
                }
                if (item.getName() != null && item.getName().length() > 0) {
                    mRecommendResNameTV.setText(item.getName());
                } else {
                    mRecommendResNameTV.setText("");
                }
                if (item.getNumber_of_people() != null && item.getNumber_of_people().length() > 0) {
                    mNumberOfPeopleTV.setText(item.getNumber_of_people());
                } else {
                    mNumberOfPeopleTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                mNumberOfOrderTV.setText(String.valueOf(item.getNumber_of_order()));
                if (item.getDeposit() != null && item.getDeposit().length() > 0) {
                    mOtherPhyNoDepositTV.setVisibility(View.VISIBLE);
                    mOtherPhyNoDepositTV.setText(item.getDeposit());
                } else {
                    mOtherPhyNoDepositTV.setVisibility(View.GONE);
                }
                //室内
                if (item.getPosition() != null &&
                        item.getPosition().length() > 0) {
                    mOtherPhyIndoorTV.setVisibility(View.VISIBLE);
                    mOtherPhyIndoorTV.setText(item.getPosition());
                } else {
                    mOtherPhyIndoorTV.setVisibility(View.GONE);
                }
                //打折
                if (item.getDiscount() != null &&
                        item.getDiscount().length() > 0) {
                    mOtherPhySubsidyLabelTV.setText(item.getDiscount());
                    mOtherPhySubsidyLabelTV.setVisibility(View.GONE);
                } else {
                    mOtherPhySubsidyLabelTV.setVisibility(View.GONE);
                }

            }
        }
    }
}
