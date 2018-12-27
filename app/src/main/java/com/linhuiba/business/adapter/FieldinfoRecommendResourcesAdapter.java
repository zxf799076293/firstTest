package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class FieldinfoRecommendResourcesAdapter extends BaseQuickAdapter<ResourceSearchItemModel, BaseViewHolder> {
    private List<ResourceSearchItemModel> data = new ArrayList<ResourceSearchItemModel>();
    private Context mcontext;
    private Activity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private int width;
    private int height;
    public int sort;
    public FieldinfoRecommendResourcesAdapter(Context context, Activity activity,
                                              int layoutResId, @Nullable List<ResourceSearchItemModel> data,int invoicetype) {
        super(layoutResId, data);
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = data;
        this.type = invoicetype;
        DisplayMetrics metric = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
    }
    public FieldinfoRecommendResourcesAdapter(Context context, Activity activity,
                                              int layoutResId, @Nullable List<ResourceSearchItemModel> data,int invoicetype,int sort) {
        super(layoutResId, data);
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = data;
        this.type = invoicetype;
        DisplayMetrics metric = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
        this.sort = sort;
    }
    @Override
    protected void convert(BaseViewHolder helper, ResourceSearchItemModel item) {
        if (data != null) {
            if (data.size() > 0) {
                RelativeLayout mRecommendResImgRL = (RelativeLayout) helper.getView(R.id.resource_info_recommend_item_img_rl);
                ImageView mRecommendResImg = (ImageView) helper.getView(R.id.resource_info_recommend_item_img);
                TextView mRecommendResTimeTV = (TextView) helper.getView(R.id.resource_info_recommend_item_tiem_tv);
                TextView mRecommendResPriceTV = (TextView) helper.getView(R.id.resource_info_recommend_item_price_tv);
                TextView mRecommendResPriceUnitTV = (TextView) helper.getView(R.id.resource_info_recommend_item_price_unit_tv);
                TextView mRecommendResNameTV = (TextView) helper.getView(R.id.resource_info_recommend_item_name_tv);
                LinearLayout mRecommendResTimeLL = (LinearLayout) helper.getView(R.id.resource_info_recommend_item_tiem_ll);
                TextView mOtherPhySizeTV = (TextView) helper.getView(R.id.fieldinfo_other_phy_size_tv);
                LinearLayout mRecommendResNameLL = (LinearLayout) helper.getView(R.id.resource_recommend_item_tv_ll);
                ImageView mSubsidyImgv = (ImageView) helper.getView(R.id.search_other_phy_res_subsidy_txt_img);
                TextView mActivityTypeTV = (TextView) helper.getView(R.id.fieldinfo_activity_type_tv);
                LinearLayout mFieldinfoOtherResRightLL = (LinearLayout) helper.getView(R.id.fieldinfo_other_res_right_ll);
                TextView mFieldinfoOtherResNameTV = (TextView) helper.getView(R.id.fieldinfo_orher_res_name_tv);
                TextView mFieldinfoOtherResPriceTV = (TextView) helper.getView(R.id.fieldinfo_orher_res_price_tv);
                TextView mFieldinfoOtherResOriginalPriceUnitTV = (TextView) helper.getView(R.id.fieldinfo_orher_res_original_price_unit_tv);
                RelativeLayout mFieldinfoRecommendRL = (RelativeLayout) helper.getView(R.id.fieldinfo_other_res_recommend_rl);
                OvalImageView mOtherResImg = (OvalImageView) helper.getView(R.id.resource_info_other_phy_img);
                LinearLayout mOtherResSelectedLL = (LinearLayout) helper.getView(R.id.fieldinfo_other_res_selected_ll);
                View mOtherResBottomView = (View) helper.getView(R.id.fieldinfo_other_res_bottom_view);
                if (type != 2) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)),
                            (width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)) * 262 / 338);
                    mRecommendResImgRL.setLayoutParams(params);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,80));
                    mRecommendResNameLL.setLayoutParams(layoutParams);
                    mFieldinfoRecommendRL.setVisibility(View.VISIBLE);
                    mFieldinfoOtherResRightLL.setVisibility(View.GONE);
                } else {
                    mFieldinfoRecommendRL.setVisibility(View.GONE);
                    mFieldinfoOtherResRightLL.setVisibility(View.VISIBLE);
                }
                if (type == 1) {
                    if (item.getCommunity_img() != null && item.getCommunity_img().getPic_url() != null &&
                            item.getCommunity_img().getPic_url().length() > 0) {
                        Picasso.with(mcontext).load(item.getCommunity_img().getPic_url().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).
                                error(R.drawable.ic_no_pic_big).resize((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)),
                                (width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)) * 262 / 338).into(mRecommendResImg);
                    } else {
                        Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)), (width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)) * 262 / 338).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(mRecommendResImg);
                    }
                    mOtherPhySizeTV.setVisibility(View.GONE);
                    mRecommendResPriceUnitTV.setVisibility(View.GONE);
                    if (item.getFloor_price() != null && item.getFloor_price().length() > 0) {
                        if (Double.parseDouble(item.getFloor_price()) > 0) {
                            mRecommendResPriceTV.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(item.getFloor_price(),0.01)),11));
                            mRecommendResPriceUnitTV.setVisibility(View.VISIBLE);
                        } else {
                            mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                        }
                    } else {
                        mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                    }
                    mRecommendResTimeLL.setVisibility(View.GONE);
                } else if (type == 2) {
                    if (item.getPhysical_resource_first_img() != null && item.getPhysical_resource_first_img().getPic_url() != null &&
                            item.getPhysical_resource_first_img().getPic_url().length() > 0) {
                        Picasso.with(mcontext).load(item.getPhysical_resource_first_img().getPic_url().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).
                                error(R.drawable.ic_no_pic_big).resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,56),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,56)).into(mOtherResImg);
                    } else {
                        Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,56),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,56)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(mOtherResImg);
                    }
                    mOtherPhySizeTV.setVisibility(View.VISIBLE);
                    mOtherPhySizeTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_phy_str) +
                    String.valueOf(item.getSort()));
//                    if (item.getActivity_start_date() != null && item.getDeadline() != null &&
//                            item.getActivity_start_date().length() > 0 && item.getDeadline().length() > 0) {
//                        mRecommendResTimeTV.setText(
//                                item.getActivity_start_date().replaceAll("-", ".") + "-" + item.getDeadline().replaceAll("-", "."));
//                        mRecommendResTimeLL.setVisibility(View.VISIBLE);
//                    } else {
//                        mRecommendResTimeLL.setVisibility(View.GONE);
//                        mRecommendResTimeTV.setText("");
//                    }
//                    mRecommendResPriceUnitTV.setVisibility(View.GONE);
//                    if (item.getMin_price() != null && item.getMin_price().length() > 0) {
//                        if (Double.parseDouble(item.getMin_price()) > 0) {
//                            mRecommendResPriceTV.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
//                                    Constants.getpricestring(item.getMin_price(),0.01)),11));
//                            mRecommendResPriceUnitTV.setVisibility(View.VISIBLE);
//                        } else {
//                            mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
//                        }
//                    } else {
//                        mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
//                    }
                    mFieldinfoOtherResOriginalPriceUnitTV.setVisibility(View.GONE);
                    String name = "";
                    if (item.getName() != null && item.getName().length() > 0) {
                        if (item.getLocation_type() != null && item.getLocation_type().getDisplay_name() != null &&
                                item.getLocation_type().getDisplay_name().length() > 0) {
                            name = name + "【" + item.getLocation_type().getDisplay_name() + "】 ";
                        }
                        if (item.getCommunity() != null && item.getCommunity().length() > 0) {
                            Field_AddResourceCreateItemModel model = JSON.parseObject(item.getCommunity(),Field_AddResourceCreateItemModel.class);
                            if (model.getName() != null && model.getName().length() > 0) {
                                name = name + model.getName()+ "-" ;
                            }
                        }
                        name = name + item.getName();
                        mFieldinfoOtherResNameTV.setText(name);
                    } else {
                        mFieldinfoOtherResNameTV.setText("");
                    }
                    if (item.getMin_price() != null && item.getMin_price().length() > 0) {
                        if (Double.parseDouble(item.getMin_price()) > 0) {
                            mFieldinfoOtherResPriceTV.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(item.getMin_price(),0.01)),11));
                            mFieldinfoOtherResOriginalPriceUnitTV.setVisibility(View.VISIBLE);
                        } else {
                            mFieldinfoOtherResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                        }
                    } else {
                        mFieldinfoOtherResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                    }
                    if (item.getSelected() == 1) {
                        mOtherResSelectedLL.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.module_bg_fieldinfo_other_phy_res));
                    } else {
                        mOtherResSelectedLL.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                    }
                    if (helper.getLayoutPosition() == data.size() - 1) {
                        mOtherResBottomView.setVisibility(View.GONE);
                    } else {
                        mOtherResBottomView.setVisibility(View.VISIBLE);
                    }
                } else if (type == 3) {
                    if (item.getSelling_resource_img() != null && item.getSelling_resource_img().length() > 0) {
                        Picasso.with(mcontext).load(item.getSelling_resource_img().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).
                                error(R.drawable.ic_no_pic_big).resize((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)),
                                (width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)) * 262 / 338).into(mRecommendResImg);
                    } else {
                        Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)), (width / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,16)) * 262 / 338).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(mRecommendResImg);
                    }
                    mOtherPhySizeTV.setVisibility(View.VISIBLE);
                    mOtherPhySizeTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_phy_str) +
                            String.valueOf(sort));
                    if (item.getActivity_start_date() != null && item.getDeadline() != null &&
                            item.getActivity_start_date().length() > 0 && item.getDeadline().length() > 0) {
                        mRecommendResTimeTV.setText(
                                item.getActivity_start_date().replaceAll("-", ".") + "-" + item.getDeadline().replaceAll("-", "."));
                        mRecommendResTimeLL.setVisibility(View.VISIBLE);
                    } else {
                        mRecommendResTimeLL.setVisibility(View.GONE);
                        mRecommendResTimeTV.setText("");
                    }
                    if (item.getCustom_name() != null && item.getCustom_name().length() > 0) {
                        if (item.getActivity_type() != null && item.getActivity_type().length() > 0) {
                            mActivityTypeTV.setText(item.getActivity_type());
                            mActivityTypeTV.setVisibility(View.VISIBLE);
                            mRecommendResNameTV.setText( Constants.getDifferentColorStr(".........." +
                                    item.getCustom_name(),0,10,mContext.getResources().getColor(R.color.color_null)));
                        } else {
                            mActivityTypeTV.setVisibility(View.GONE);
                            mRecommendResNameTV.setText(item.getCustom_name());
                        }
                    } else {
                        mRecommendResNameTV.setText("");
                    }
                    if (item.getPrice() != null && item.getPrice().length() > 0) {
                        if (Double.parseDouble(item.getPrice()) > 0) {
                            mRecommendResPriceTV.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(item.getPrice(),0.01)),11));
                            mRecommendResPriceUnitTV.setVisibility(View.VISIBLE);
                        } else {
                            mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                        }
                    } else {
                        mRecommendResPriceTV.setText(mcontext.getResources().getString(R.string.home_hot_inquiry_tv_str));
                    }
                }
                if (type != 3 && type != 2) {
                    if (item.getIs_subsidy() == 1) {
                        mSubsidyImgv.setVisibility(View.VISIBLE);
                    } else {
                        mSubsidyImgv.setVisibility(View.GONE);
                    }
                    if (item.getName() != null && item.getName().length() > 0) {
                        mRecommendResNameTV.setText(item.getName());
                    } else {
                        mRecommendResNameTV.setText("");
                    }
                }
            }
        }
    }
}
