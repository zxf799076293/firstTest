package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.CommunityInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeDynamicCommunityAdapter extends BaseQuickAdapter<ResourceSearchItemModel, BaseViewHolder> {
    private Context mContext;
    private List<ResourceSearchItemModel> mDatas;
    private HomeFragment mHomeFragment;
    private int width;
    private int height;
    private boolean isFieldinfoLookPicture;
    public HomeDynamicCommunityAdapter(int layoutResId, @Nullable List<ResourceSearchItemModel> data,
                                       Context context, HomeFragment homeFragment) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        this.mHomeFragment = homeFragment;
        DisplayMetrics metric = new DisplayMetrics();
        mHomeFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
    }
    public HomeDynamicCommunityAdapter(int layoutResId, @Nullable List<ResourceSearchItemModel> data,
                                       Activity context, boolean isFieldinfoLookPicture) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
        this.isFieldinfoLookPicture = isFieldinfoLookPicture;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ResourceSearchItemModel item) {
        RelativeLayout imgvRL = (RelativeLayout)helper.getView(R.id.image_layout);
        LinearLayout mHomeSubsidyLL = (LinearLayout)helper.getView(R.id.home_subsidy_ll);
        LinearLayout mproject_type_layout = (LinearLayout)helper.getView(R.id.project_type_layout);
        OvalImageView mhome_activity_img = (OvalImageView)helper.getView(R.id.home_activity_img);
        ImageView mHomeHotLbelImgv = (ImageView)helper.getView(R.id.home_activity_label_img);
        TextView mhome_activity_price = (TextView)helper.getView(R.id.home_activity_price);
        TextView mHomePriceUnitTV = (TextView)helper.getView(R.id.home_activity_price_unit_tv);
        LinearLayout mHomeHotPriceLL = (LinearLayout)helper.getView(R.id.home_hot_item_price_ll);
        TextView mHomeHotInquiryTV = (TextView) helper.getView(R.id.home_hot_item_inquiry_tv);
        LinearLayout mHomeServiceProvideLabelLL = (LinearLayout) helper.getView(R.id.home_service_provider_label_ll);
        LinearLayout mHomeCommunityItemLL = (LinearLayout) helper.getView(R.id.home_community_all_ll);
        ImageView mHomeCommunityItemImgv = (ImageView) helper.getView(R.id.home_community_no_data_imgv);
        LinearLayout mHomeCommunityItemNoDataLL = (LinearLayout) helper.getView(R.id.home_community_no_data_ll);
        ImageView mHomeCommunitySubsudyImg = (ImageView) helper.getView(R.id.home_community_subsidy_img);
        mHomeCommunitySubsudyImg.setVisibility(View.GONE);
        mHomeHotLbelImgv.setVisibility(View.VISIBLE);
        mHomeSubsidyLL.setVisibility(View.GONE);
        mproject_type_layout.setVisibility(View.GONE);
        mHomeServiceProvideLabelLL.setVisibility(View.GONE);
        mHomeHotPriceLL.setVisibility(View.VISIBLE);
        mHomeHotInquiryTV.setVisibility(View.GONE);
        if (item.getId() != null) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width)* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                    (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338);
            imgvRL.setLayoutParams(paramgroups);
            mHomeCommunityItemLL.setVisibility(View.VISIBLE);
            mHomeCommunityItemNoDataLL.setVisibility(View.GONE);
            if (item.getCommunity_img() != null &&
                    item.getCommunity_img().getPic_url() != null &&
                    item.getCommunity_img().getPic_url().length() > 0) {
                Picasso.with(mContext).load(item.getCommunity_img().getPic_url().toString()+ Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(mhome_activity_img);
            } else {
                Picasso.with(mContext).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(mhome_activity_img);
            }
            if (item.getName() != null &&
                    item.getName().length() > 0) {
                helper.setText(R.id.home_activity_name,item.getName());
            } else {
                helper.setText(R.id.home_activity_name,"");
            }
            mHomePriceUnitTV.setVisibility(View.GONE);
            if (item.getFloor_price() != null &&
                    item.getFloor_price().length() > 0) {
                if (Double.parseDouble(item.getFloor_price()) > 0) {
                    mHomePriceUnitTV.setVisibility(View.VISIBLE);
                    mhome_activity_price.setText(Constants.getPriceUnitStr(mContext,(mContext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getpricestring(item.getFloor_price(), 0.01)),12));
                } else {
                    mhome_activity_price.setText(mContext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                }
            } else {
                mhome_activity_price.setText(mContext.getResources().getString(R.string.home_hot_inquiry_tv_str));
            }
            helper.setText(R.id.home_community_order_size_tv,String.valueOf(item.getOrder_quantity()));
            if (item.getIs_subsidy() == 1) {
                mHomeCommunitySubsudyImg.setVisibility(View.VISIBLE);
            }
        } else {
            mHomeCommunityItemLL.setVisibility(View.GONE);
            mHomeCommunityItemNoDataLL.setVisibility(View.VISIBLE);
            if (isFieldinfoLookPicture) {
                // FIXME: 2018/12/17 详情图片所有图片预览
                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(width/2 - Constants.Dp2Px(mContext,5),
                        (width/2 - Constants.Dp2Px(mContext,5)) * 136 / 170);
                mHomeCommunityItemNoDataLL.setLayoutParams(params);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mHomeCommunityItemImgv.getLayoutParams());
                lp.setMargins(0, 0, 0, com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,10));
                mHomeCommunityItemImgv.setLayoutParams(lp);
                if (item.getPic_url() != null &&
                        item.getPic_url().length() > 0) {
                    Picasso.with(mContext).load(item.getPic_url().toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width/2  - Constants.Dp2Px(mContext,5), (width/2 - Constants.Dp2Px(mContext,5)) * 136 / 170).into(mHomeCommunityItemImgv);
                } else {
                    Picasso.with(mContext).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width/2  - Constants.Dp2Px(mContext,5), (width/2 - Constants.Dp2Px(mContext,5)) * 136 / 170).into(mHomeCommunityItemImgv);
                }
            } else {
                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(((width)* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                        (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338 +
                                Constants.Dp2Px(mContext,62));
                mHomeCommunityItemNoDataLL.setLayoutParams(params);
                Picasso.with(mContext).load(R.drawable.ic_logo_three_six_one).placeholder(R.drawable.ic_logo_three_six_one).error(R.drawable.ic_logo_three_six_one).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                        (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338+
                                Constants.Dp2Px(mContext,42))
                        .into(mHomeCommunityItemImgv);
            }
        }
        mHomeCommunityItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getId() != null) {
                    Intent fieldinfo = null;
                    if (item.getType() != null) {
                        if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_COMMUNITY_RES)) {
                            fieldinfo = new Intent(mHomeFragment.getActivity(), CommunityInfoActivity.class);
                            fieldinfo.putExtra("city_id", item.getCity().getId());
                            fieldinfo.putExtra("id", item.getId());
                            mHomeFragment.startActivity(fieldinfo);
                        } else if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_PHYSICAL_RES)) {
                            if (item.getTop_physical_id() != null) {
                                fieldinfo = new Intent(mHomeFragment.getActivity(), FieldInfoActivity.class);
                                fieldinfo.putExtra("fieldId", String.valueOf(item.getTop_physical_id()));
                                fieldinfo.putExtra("community_id", item.getId());
                                mHomeFragment.startActivity(fieldinfo);
                            }
                        } else if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_SELLING_RES)) {
                            if (item.getTop_physical_id() != null) {
                                fieldinfo = new Intent(mHomeFragment.getActivity(), FieldInfoActivity.class);
                                fieldinfo.putExtra("sell_res_id", String.valueOf(item.getTop_physical_id()));
                                fieldinfo.putExtra("is_sell_res", true);
                                fieldinfo.putExtra("community_id", item.getId());
                                mHomeFragment.startActivity(fieldinfo);
                            }
                        }
                    }
                }
            }
        });
    }
}
