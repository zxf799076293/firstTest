package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.CommunityInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;
import com.umeng.commonsdk.debug.E;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchCommunityListAdapter extends BaseQuickAdapter<ResourceSearchItemModel, BaseViewHolder> {
    private List<ResourceSearchItemModel> mFeildList;
    private SearchListFragment mactivity;
    private Context mContext;
    private Drawable mDrawable;
    public SearchCommunityListAdapter(int layoutResId, @Nullable List<ResourceSearchItemModel> data
        ,Context context,SearchListFragment mactivity) {
        super(layoutResId, data);
        this.mFeildList = data;
        this.mactivity = mactivity;
        this.mContext = context;
        mDrawable = mContext.getResources().getDrawable(R.drawable.ic_kuaisuding2_blue_three_six_one);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ResourceSearchItemModel item) {
        ImageView fieldimg = (ImageView) helper.getView(R.id.searchlist_item_img);
        TextView field_name = (TextView) helper.getView(R.id.searchlist_item_nametxt);
        TextView addressTV = (TextView) helper.getView(R.id.community_address_tv);
        ImageView msubsidy_txt_img = (ImageView) helper.getView(R.id.searchlist_item_subsidy_txt_img);
        TextView msearchlist_txt_fieldtitle = (TextView) helper.getView(R.id.searchlist_txt_fieldtitle);
        LinearLayout msearchlist_txt_fieldtitle_layout = (LinearLayout) helper.getView(R.id.searchlist_txt_fieldtitle_layout);
        TextView price = (TextView) helper.getView(R.id.searchlist_item_price);
        LinearLayout mSearchListLabelsLL = (LinearLayout) helper.getView(R.id.search_list_item_label_ll);
        TextView mSearchListLabels0TV = (TextView) helper.getView(R.id.search_list_item_label0);
        TextView mSearchListLabels1TV = (TextView) helper.getView(R.id.search_list_item_label1);
        TextView mSearchListLabels2TV = (TextView) helper.getView(R.id.search_list_item_label2);
        TextView mSearchListOrderSizeTV = (TextView) helper.getView(R.id.search_list_item_order_quantity);
        TextView mSearchListDistanceTV = (TextView) helper.getView(R.id.community_distance_location_tv);
        View mSearchListDistanceView = (View) helper.getView(R.id.community_distance_view);
        LinearLayout mPhyResCommunityNameLL = (LinearLayout) helper.getView(R.id.phy_res_community_name_ll);
        TextView mPhyResCommunityNameTV = (TextView) helper.getView(R.id.phy_res_community_name_tv);
        LinearLayout mPhyResPriceLL = (LinearLayout) helper.getView(R.id.searchlist_common_price_ll);
        LinearLayout mPhyResEnquiryPriceLL = (LinearLayout) helper.getView(R.id.searchlist_item_enquiry_ll);
        LinearLayout mSearchListDemandLL = helper.getView(R.id.searchlist_item_demand_ll);
        LinearLayout mSearchlistPriceLL = helper.getView(R.id.searchlist_item_price_ll);
        TextView mSearchListQuantityOrderSizeTV = (TextView) helper.getView(R.id.search_list_item_quantity_order_quantity);

        if (item.getCommunity_img() != null &&
                item.getCommunity_img().getPic_url() != null &&
                item.getCommunity_img().getPic_url().length() > 0) {
            Picasso.with(mContext).load(item.getCommunity_img().getPic_url() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).
                    resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,104),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,104)).into(fieldimg);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small).resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,104),
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,104)).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(fieldimg);
        }
        field_name.setCompoundDrawables(null, null, null, null);
        if (item.getName() != null &&
                item.getName().length() > 0) {
            field_name.setText(item.getName());
            if (item.getIndoor() != null) {
                if (item.getIndoor() == 0) {
                    field_name.setText(item.getName()
                            + mContext.getResources().getString(R.string.fieldinfo_location_unindoor_text));
                } else if (item.getIndoor() == 1) {
                    field_name.setText(item.getName()
                            + mContext.getResources().getString(R.string.fieldinfo_location_indoor_text));
                }
            }
            if (item.getCommunity() != null && item.getCommunity().length() > 0) {
                String name = field_name.getText().toString();
                field_name.setText(item.getCommunity() + "-" + name);
            }
            if (item.getIs_fast_booking() == 1) {
                field_name.setCompoundDrawables(mDrawable, null, null, null);
            }
        } else {
            field_name.setText("");
        }
        String address = "";
        if (item.getCity() != null && item.getCity().getName() != null &&
                item.getCity().getName().length() > 0) {
            address = address + item.getCity().getName();
        }
        if (item.getDistrict() != null && item.getDistrict().length() > 0) {
            Field_AddResourceCreateItemModel district = JSONObject.parseObject(item.getDistrict(),Field_AddResourceCreateItemModel.class);
            if (district.getName() != null &&
                    district.getName().length() > 0) {
                address = address + district.getName();
            }
        }
        if (item.getDetailed_address() != null &&
                item.getDetailed_address().length() > 0) {
            address = address + item.getDetailed_address();
        }
        addressTV.setText(address);
        mSearchListOrderSizeTV.setText(
                String.valueOf(item.getNumber_of_order()) +
                        mContext.getResources().getString(R.string.module_searchlist_item_otder_size));
        if (item.getIs_subsidy() == 1) {
            msubsidy_txt_img.setVisibility(View.VISIBLE);
        } else {
            msubsidy_txt_img.setVisibility(View.GONE);
        }
        if (item.getHas_selling() > 0) {
            if (item.isIs_only_enquiry()) {
                // FIXME: 2018/12/13 询价
                mPhyResEnquiryPriceLL.setVisibility(View.VISIBLE);
                mPhyResPriceLL.setVisibility(View.GONE);
                mSearchListQuantityOrderSizeTV.setText(
                        String.valueOf(item.getNumber_of_order()) +
                                mContext.getResources().getString(R.string.module_searchlist_item_otder_size));
            } else {
                mPhyResEnquiryPriceLL.setVisibility(View.GONE);
                mPhyResPriceLL.setVisibility(View.VISIBLE);
                if (item.getSubsidy_price() != null &&
                        item.getSubsidy_price().length() > 0) {
                    mSearchListDemandLL.setVisibility(View.GONE);
                    mSearchlistPriceLL.setVisibility(View.VISIBLE);
                    String mActualPrice = Constants.getpricestring(item.getSubsidy_price(),0.01);
                    price.setText(mActualPrice);
                } else {
                    mSearchListDemandLL.setVisibility(View.VISIBLE);
                    mSearchlistPriceLL.setVisibility(View.GONE);
                }
            }
        } else {
            mPhyResPriceLL.setVisibility(View.VISIBLE);
            mSearchListDemandLL.setVisibility(View.VISIBLE);
            mSearchlistPriceLL.setVisibility(View.GONE);
        }
        if (item.getCategory() != null &&
                item.getCategory().length() > 0) {
            Field_AddResourceCreateItemModel category = (Field_AddResourceCreateItemModel) JSONObject.parseObject(item.getCategory(),
                    Field_AddResourceCreateItemModel.class);
            if (category.getName() != null &&
                    category.getName().length() > 0) {
                msearchlist_txt_fieldtitle_layout.setVisibility(View.VISIBLE);
                // FIXME: 2018/12/22 标签
                if (item.getTotal_area() != null && item.getTotal_area().length() > 0) {
                    msearchlist_txt_fieldtitle.setText(category.getName() + "·" +
                    Constants.getpricestring(item.getTotal_area(),1) + mContext.getResources().getString(R.string.myselfinfo_company_demand_area_unit_text));
                } else {
                    msearchlist_txt_fieldtitle.setText(category.getName());
                }
            } else {
                msearchlist_txt_fieldtitle_layout.setVisibility(View.GONE);
            }
        } else {
            msearchlist_txt_fieldtitle_layout.setVisibility(View.GONE);
        }
        if (item.getLabels() != null &&
                item.getLabels().size() > 0) {
            mSearchListLabelsLL.setVisibility(View.VISIBLE);
            mSearchListLabels0TV.setVisibility(View.GONE);
            mSearchListLabels1TV.setVisibility(View.GONE);
            mSearchListLabels2TV.setVisibility(View.GONE);
            for (int i = 0; i < item.getLabels().size(); i++) {
                if (i < 3) {
                    String label = "";
                    if (item.getLabels().get(i).getDisplay_name() != null &&
                            item.getLabels().get(i).getDisplay_name().length() > 0) {
                        label = item.getLabels().get(i).getDisplay_name();
                    } else {
                        if (item.getLabels().get(i).getName() != null &&
                                item.getLabels().get(i).getName().length() > 0) {
                            label = item.getLabels().get(i).getName();
                        }
                    }
                    if (label.length() > 0) {
                        if (i == 0) {
                            mSearchListLabels0TV.setVisibility(View.VISIBLE);
                            mSearchListLabels0TV.setText(label);
                        } else if (i == 1) {
                            mSearchListLabels1TV.setVisibility(View.VISIBLE);
                            mSearchListLabels1TV.setText(label);
                        } else if (i == 2) {
                            mSearchListLabels2TV.setVisibility(View.VISIBLE);
                            mSearchListLabels2TV.setText(label);
                        }
                    }
                }
            }
        } else {
            mSearchListLabelsLL.setVisibility(View.GONE);
        }

        if (item.getDistance() != null &&
                item.getDistance().length() > 0) {
            mSearchListDistanceTV.setVisibility(View.VISIBLE);
            mSearchListDistanceView.setVisibility(View.VISIBLE);
            if (Double.parseDouble(item.getDistance()) > 1000) {
                mSearchListDistanceTV.setText(Constants.getpricestring(item.getDistance(),0.001) + "km");
            } else {
                mSearchListDistanceTV.setText(item.getDistance() + "m");
            }
        } else {
            mSearchListDistanceTV.setVisibility(View.GONE);
            mSearchListDistanceView.setVisibility(View.GONE);
        }
        // FIXME: 2018/12/8 场地名字显示
        if (item.getCommunity() != null && item.getCommunity().length() > 0) {
            mPhyResCommunityNameLL.setVisibility(View.VISIBLE);
            mPhyResCommunityNameTV.setText(item.getCommunity());
            mPhyResCommunityNameLL.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent communityIntent;
                    communityIntent = new Intent(mContext, CommunityInfoActivity.class);
                    if (item.getCity() != null && item.getCity().getId() > 0) {
                        communityIntent.putExtra("city_id", item.getCity().getId());
                    }
                    communityIntent.putExtra("id", item.getCommunity_id());
                    mContext.startActivity(communityIntent);
                }
            });
        } else {
            mPhyResCommunityNameLL.setVisibility(View.GONE);
        }

        helper.setOnClickListener(R.id.search_field_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.getLayoutPosition() < mFeildList.size()) {
                    Intent fieldinfo = null;
                    if (item.isIs_only_activity()) {
                        if (item.getActivity_id() != null) {
                            fieldinfo = new Intent(mactivity.getActivity(), FieldInfoActivity.class);
                            fieldinfo.putExtra("sell_res_id", String.valueOf(item.getActivity_id()));
                            fieldinfo.putExtra("is_sell_res", true);
                            fieldinfo.putExtra("community_id", item.getCommunity_id());
                            fieldinfo.putExtra("model", (Serializable) mactivity.apiResourcesModel);
                            mactivity.startActivity(fieldinfo);
                        }
                    } else {
                        if (item.getId() != null) {
                            fieldinfo = new Intent(mactivity.getActivity(), FieldInfoActivity.class);
                            fieldinfo.putExtra("fieldId", String.valueOf(item.getId()));
                            fieldinfo.putExtra("community_id", item.getCommunity_id());
                            fieldinfo.putExtra("model", (Serializable) mactivity.apiResourcesModel);
                            mactivity.startActivity(fieldinfo);
                        }
                    }
//                    if (item.getType() != null) {
//
//                        if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_COMMUNITY_RES)) {
//                            fieldinfo = new Intent(mactivity.getActivity(), CommunityInfoActivity.class);
//                            fieldinfo.putExtra("city_id", item.getCity().getId());
//                            fieldinfo.putExtra("id", item.getId());
//                            mactivity.startActivity(fieldinfo);
//                        } else if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_PHYSICAL_RES)) {
//                            if (item.getTop_physical_id() != null) {
//                                fieldinfo = new Intent(mactivity.getActivity(), FieldInfoActivity.class);
//                                fieldinfo.putExtra("fieldId", String.valueOf(item.getTop_physical_id()));
//                                fieldinfo.putExtra("community_id", item.getId());
//                                fieldinfo.putExtra("model", (Serializable) mactivity.apiResourcesModel);
//                                mactivity.startActivity(fieldinfo);
//                            }
//                        } else if (item.getType().equals(com.linhuiba.business.config.Config.JUMP_SELLING_RES)) {
//                            if (item.getTop_physical_id() != null) {
//                                fieldinfo = new Intent(mactivity.getActivity(), FieldInfoActivity.class);
//                                fieldinfo.putExtra("sell_res_id", String.valueOf(item.getTop_physical_id()));
//                                fieldinfo.putExtra("is_sell_res", true);
//                                fieldinfo.putExtra("community_id", item.getId());
//                                fieldinfo.putExtra("model", (Serializable) mactivity.apiResourcesModel);
//                                mactivity.startActivity(fieldinfo);
//                            }
//                        }
//                    }
                }
            }
        });
    }
}
