package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.app.util.NetworkUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.FieldInfoSellResourceModel;
import com.linhuiba.business.model.FieldInfoSizeModel;
import com.linhuiba.business.view.MyListView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class FieldInfoPriceSizeAdapter extends BaseExpandableListAdapter {
    private List<FieldInfoSizeModel> groupList;//外层的数据源
    private List<List<FieldInfoSellResourceModel>> childList;//里层的数据源
    private Context context;
    private FieldInfoActivity activity;
    private LayoutInflater mInflater = null;
    public FieldInfoPriceSizeAdapter(Context context, FieldInfoActivity activity, List<FieldInfoSizeModel> groupList,List<List<FieldInfoSellResourceModel>> childList ){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        this.groupList = groupList;
        this.childList = childList;
    }
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childList.get(groupPosition).size() > 1) {
            return childList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.activity_fieldinfopricelistitem, null);
            holder.mfieldinfo_specifications_sizetextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_sizetextview);
            holder.mfieldinfo_specifications_tiemtextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_tiemtextview);
            holder.mfieldinfo_specifications_specialtextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_specialtextview);
            holder.mFieldinfoDayInAdvanceTV = (TextView)convertView.findViewById(R.id.fieldinfo_item_days_in_advance);
            holder.mFieldinfoChooseCustomDimensionLL = (LinearLayout)convertView.findViewById(R.id.choose_custom_dimension_ll);
            holder.mfieldinfo_specifications_pricetextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_pricetextview);
            holder.mSpecoficationItemPriceTV = (TextView) convertView.findViewById(R.id.specification_item_price_tv);
            holder.mSpecoficationItemPriceWeekUnitTV = (TextView) convertView.findViewById(R.id.specification_item_price_week_unit_tv);
            holder.mSpecoficationLVItemReserveTV = (Button) convertView.findViewById(R.id.specification_lv_item_reserve_tv);
            holder.mSpecoficationSubsidyImg = (ImageView) convertView.findViewById(R.id.specification_item_subsidy_img);
            holder.mSpecoficationActualPriceTV = (TextView) convertView.findViewById(R.id.specification_item_actual_price_tv);
            holder.mSpecoficationActualPriceLL = (LinearLayout) convertView.findViewById(R.id.specification_item_actual_price_ll);
            holder.mSpecoficationMoreSizeLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_specifications_more_size_res_layout);
            holder.mSpecoficationOneSizeLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_specifications_one_size_res_layout);
            holder.mSpecoficationOneSizeServiceLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_specifications_one_size_service_layout);

            holder.mSpecoficationNotProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_not_provided_service_ll);
            holder.mSpecoficationProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_service_ll);
            holder.mOtherSizeImgV = (ImageView) convertView.findViewById(R.id.fieldinfo_other_size_img);
            holder.mFieldinfoChooseSizeFirstDividingLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_choose_size_first_dividing_line);
            holder.mFieldinfoChooseSizeSubsidyLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_choose_size_subsidy_ll);
            holder.mFieldinfoGroupView = (View) convertView.findViewById(R.id.fieldinfo_size_group_view);
            holder.mFieldinfoServiceTV = (TextView) convertView.findViewById(R.id.fieldinfo_sizelist_service_tv);
            holder.mFieldinfoCooperationTypeTV = (TextView) convertView.findViewById(R.id.fieldinfo_item_cooperation_type_tv);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder)convertView.getTag();
        }
        if (groupPosition == 0) {
            holder.mFieldinfoChooseSizeFirstDividingLL.setVisibility(View.VISIBLE);
        } else {
            holder.mFieldinfoChooseSizeFirstDividingLL.setVisibility(View.VISIBLE);
        }
        if (childList.get(groupPosition).size() > 1) {
            holder.mFieldinfoGroupView.setVisibility(View.VISIBLE);
        } else {
            holder.mFieldinfoGroupView.setVisibility(View.GONE);
        }
        String leaseTermType = "";
        if (groupList.get(groupPosition).getDimension() != null &&
                groupList.get(groupPosition).getDimension().getLease_term_type() != null &&
                groupList.get(groupPosition).getDimension().getLease_term_type().length() > 0) {
            leaseTermType = "/" +
                    groupList.get(groupPosition).getDimension().getLease_term_type();
            holder.mfieldinfo_specifications_tiemtextview.setText(leaseTermType);
        } else {
            holder.mfieldinfo_specifications_tiemtextview.setText("");
        }
        if (groupList.get(groupPosition).getDimension() != null &&
                groupList.get(groupPosition).getDimension().getCustom_dimension() != null &&
                groupList.get(groupPosition).getDimension().getCustom_dimension().length() > 0) {
            holder.mfieldinfo_specifications_specialtextview.setText(groupList.get(groupPosition).getDimension().getCustom_dimension());
            holder.mFieldinfoChooseCustomDimensionLL.setVisibility(View.VISIBLE);
        } else {
            holder.mFieldinfoChooseCustomDimensionLL.setVisibility(View.GONE);
        }
        if (groupList.get(groupPosition).getResource().size() > 1) {
            if (groupList.get(groupPosition).getDimension() != null &&
                    groupList.get(groupPosition).getDimension().getSize() != null &&
                    groupList.get(groupPosition).getDimension().getSize().length() > 0) {
                holder.mfieldinfo_specifications_sizetextview.setText(context.getResources().getString(R.string.order_listitem_sizetxt) +
                        groupList.get(groupPosition).getDimension().getSize() + "(" +
                        context.getResources().getString(R.string.module_fieldinfo_size_unit) + ")");
            } else {
                holder.mfieldinfo_specifications_sizetextview.setText("");
            }
            holder.mSpecoficationMoreSizeLL.setVisibility(View.GONE);
            holder.mSpecoficationOneSizeLL.setVisibility(View.GONE);
            holder.mSpecoficationOneSizeServiceLL.setVisibility(View.GONE);
        } else {
            holder.mSpecoficationMoreSizeLL.setVisibility(View.GONE);
            holder.mSpecoficationOneSizeLL.setVisibility(View.VISIBLE);
            holder.mSpecoficationOneSizeServiceLL.setVisibility(View.VISIBLE);
            if (groupList.get(groupPosition).getDimension() != null &&
                    groupList.get(groupPosition).getDimension().getSize() != null &&
                    groupList.get(groupPosition).getDimension().getSize().length() > 0) {
                holder.mfieldinfo_specifications_sizetextview.setText(context.getResources().getString(R.string.order_listitem_sizetxt) +
                        groupList.get(groupPosition).getDimension().getSize()+ "(" +
                        context.getResources().getString(R.string.module_fieldinfo_size_unit) + ")");
            } else {
                holder.mfieldinfo_specifications_sizetextview.setText("");
            }
            if (groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() != null &&
                    groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() == 12) {
                holder.mFieldinfoCooperationTypeTV.setText(
                        context.getResources().getString(R.string.order_exclusiver_txt));
                holder.mFieldinfoCooperationTypeTV.setVisibility(View.VISIBLE);
            } else if (groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() != null &&
                    groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() == 13) {
                holder.mFieldinfoCooperationTypeTV.setText(
                        context.getResources().getString(R.string.module_fieldinfo_size_item_is_agent));
                holder.mFieldinfoCooperationTypeTV.setVisibility(View.VISIBLE);
            } else if (groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() != null &&
                    groupList.get(groupPosition).getResource().get(0).getCooperation_type_id() == 14) {
                holder.mFieldinfoCooperationTypeTV.setText(
                        context.getResources().getString(R.string.module_fieldinfo_size_item_is_property));
                holder.mFieldinfoCooperationTypeTV.setVisibility(View.VISIBLE);
            } else {
                holder.mFieldinfoCooperationTypeTV.setVisibility(View.GONE);
            }
            holder.mFieldinfoChooseSizeSubsidyLL.setVisibility(View.GONE);
            holder.mSpecoficationItemPriceWeekUnitTV.setVisibility(View.GONE);
            if (groupList.get(groupPosition).getResource().get(0).getPrice() != null) {
                if (groupList.get(groupPosition).getDimension().getLease_term_type() != null &&
                        groupList.get(groupPosition).getDimension().getLease_term_type().equals(
                                context.getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                    holder.mSpecoficationItemPriceWeekUnitTV.setVisibility(View.VISIBLE);
                }
                if (groupList.get(groupPosition).getResource().get(0).getMin_after_subsidy() != null &&
                        groupList.get(groupPosition).getResource().get(0).getPrice() -
                                groupList.get(groupPosition).getResource().get(0).getMin_after_subsidy() > 0) {
                    holder.mSpecoficationActualPriceLL.setVisibility(View.VISIBLE);
                    holder.mSpecoficationSubsidyImg.setVisibility(View.VISIBLE);
                    activity.isShareSubsidy = true;
                    holder.mSpecoficationItemPriceTV.setText(Constants.getSpannableAllStr(context,(context.getResources().getString(R.string.order_listitem_price_unit_text) +
                                    Constants.getSearchPriceStr(Constants.getdoublepricestring(groupList.get(groupPosition).getResource().get(0).getMin_after_subsidy(),1), 0.01)), 14,0,1,false,0,0,
                            12,Constants.getSearchPriceStr(Constants.getdoublepricestring(groupList.get(groupPosition).getResource().get(0).getMin_after_subsidy(),1), 0.01).length() + 1 - 2,
                            Constants.getSearchPriceStr(Constants.getdoublepricestring(groupList.get(groupPosition).getResource().get(0).getMin_after_subsidy(),1), 0.01).length() + 1,
                            false,0,0));
                    holder.mSpecoficationActualPriceTV.setText(Constants.getSpannableAllStr(context,
                            (context.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(String.valueOf(groupList.get(groupPosition).getResource().get(0).getPrice()),0.01)+ leaseTermType),
                            0,0,0,true,0,Constants.getpricestring(
                                    String.valueOf(groupList.get(groupPosition).getResource().get(0).getPrice()),0.01).length() + 1 + leaseTermType.length(),null,0,0,false,0,0));
                    holder.mFieldinfoChooseSizeSubsidyLL.setVisibility(View.VISIBLE);
                } else {
                    holder.mSpecoficationActualPriceLL.setVisibility(View.GONE);
                    holder.mSpecoficationSubsidyImg.setVisibility(View.GONE);
                    holder.mSpecoficationItemPriceTV.setText(Constants.getSpannableAllStr(context,(context.getResources().getString(R.string.order_listitem_price_unit_text) +
                                    Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(groupList.get(groupPosition).getResource().get(0).getPrice()),1), 0.01)), 14,0,1,false,0,0,
                            12,Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(groupList.get(groupPosition).getResource().get(0).getPrice()),1), 0.01).length() + 1 - 2,
                            Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(groupList.get(groupPosition).getResource().get(0).getPrice()),1), 0.01).length() + 1,
                            false,0,0));
                }
            } else {
                holder.mSpecoficationItemPriceTV.setText("");
                holder.mSpecoficationActualPriceLL.setVisibility(View.GONE);
                holder.mSpecoficationSubsidyImg.setVisibility(View.GONE);
            }
            String serviceStr = "";
            if (groupList.get(groupPosition).getResource().get(0).getHas_power() != null &&
                    groupList.get(groupPosition).getResource().get(0).getHas_power() == 1) {
                serviceStr = context.getResources().getString(R.string.review_power_text);
            }
            if (groupList.get(groupPosition).getResource().get(0).getHas_chair() != null &&
                    groupList.get(groupPosition).getResource().get(0).getHas_chair() == 1) {
                if (serviceStr.length() > 0) {
                    serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_chairs_text);
                } else {
                    serviceStr = context.getResources().getString(R.string.review_chairs_text);
                }
            }
            if (groupList.get(groupPosition).getResource().get(0).getHas_tent() != null &&
                    groupList.get(groupPosition).getResource().get(0).getHas_tent() == 1) {
                if (serviceStr.length() > 0) {
                    serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_tent_str);
                } else {
                    serviceStr = context.getResources().getString(R.string.review_tent_str);
                }
            }
            if (groupList.get(groupPosition).getResource().get(0).getLeaflet() != null &&
                    groupList.get(groupPosition).getResource().get(0).getLeaflet() == 1) {
                if (serviceStr.length() > 0) {
                    serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_leaf_str);
                } else {
                    serviceStr = context.getResources().getString(R.string.review_leaf_str);
                }
            }
            if (groupList.get(groupPosition).getResource().get(0).getOvernight_material() != null &&
                    groupList.get(groupPosition).getResource().get(0).getOvernight_material() == 1) {
                if (serviceStr.length() > 0) {
                    serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_goodshelp_text);
                } else {
                    serviceStr = context.getResources().getString(R.string.review_goodshelp_text);
                }
            }
            if (serviceStr.trim().length() == 0) {
                holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
                holder.mSpecoficationOneSizeServiceLL.setVisibility(View.GONE);
                holder.mSpecoficationProvidServiceedLL.setVisibility(View.GONE);
            } else {
                holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
                holder.mSpecoficationOneSizeServiceLL.setVisibility(View.VISIBLE);
                holder.mSpecoficationProvidServiceedLL.setVisibility(View.VISIBLE);
                holder.mFieldinfoServiceTV.setText(serviceStr);
            }
            // : 2017/10/23 认证的预定背景
            if (groupList.get(groupPosition).getResource().get(0).getIdentification() != null &&
                    groupList.get(groupPosition).getResource().get(0).getIdentification() == 1) {
                if (groupList.get(groupPosition).getResource().get(0).isExpired()) {
                    holder.mSpecoficationLVItemReserveTV.setEnabled(false);
                    holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_orderauthentica_disabled_three));
                    holder.mSpecoficationLVItemReserveTV.setText("");
                } else {
                    holder.mSpecoficationLVItemReserveTV.setEnabled(true);
                    holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldinfo_specifications_btn_bg));
                    holder.mSpecoficationLVItemReserveTV.setText("");
                }

            } else {
                if (groupList.get(groupPosition).getResource().get(0).isExpired()) {
                    holder.mSpecoficationLVItemReserveTV.setEnabled(false);
                    holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_order_disabed_three));
                    holder.mSpecoficationLVItemReserveTV.setText("");
                } else {
                    holder.mSpecoficationLVItemReserveTV.setEnabled(true);
                    holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldinfo_specification_reserve_tv_bg));
                    holder.mSpecoficationLVItemReserveTV.setText(context.getResources().getString(R.string.fieldinfo_special_item_reserve_str));
                }
            }
            if (!groupList.get(groupPosition).getResource().get(0).isExpired()) {
                holder.mSpecoficationLVItemReserveTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.prices_list_OnClickListener(groupPosition,String.valueOf(0));
                    }
                });
            }
            //2018/11/13 显示提前预定天数
            if (groupList.get(groupPosition).getResource() != null &&
                    groupList.get(groupPosition).getResource().get(0) != null &&
                    groupList.get(groupPosition).getResource().get(0).getDays_in_advance() != null) {
                if (groupList.get(groupPosition).getResource().get(0).getDays_in_advance() > 0) {
                    holder.mFieldinfoDayInAdvanceTV.setText(context.getResources().getString(R.string.fieldinfo_days_in_advance_str) +
                            context.getResources().getString(R.string.fieldinfo_days_in_advance_first_str) +
                            String.valueOf(groupList.get(groupPosition).getResource().get(0).getDays_in_advance()) +
                            context.getResources().getString(R.string.fieldinfo_days_in_advance_second_str));
                } else {
                    holder.mFieldinfoDayInAdvanceTV.setText(context.getResources().getString(R.string.fieldinfo_days_in_advance_the_day));
                }
                holder.mFieldinfoDayInAdvanceTV.setVisibility(View.VISIBLE);
            } else {
                holder.mFieldinfoDayInAdvanceTV.setVisibility(View.GONE);
            }
        }
        //如果组是展开状态
//        if (isExpanded) {
//            //箭头向下
//            holder.mOtherSizeImgV.setImageResource(R.drawable.ic_dropup);
//        } else {//如果组是伸缩状态
//            //箭头向右
//            holder.mOtherSizeImgV.setImageResource(R.drawable.ic_dropdown);
//        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_fieldinfo_specifications_item_lv_item, null);
            holder.mSpecoficationItemPriceTV = (TextView) convertView.findViewById(R.id.specification_item_price_tv);
            holder.mSpecoficationItemPriceWeekUnitTV = (TextView) convertView.findViewById(R.id.specification_item_price_week_unit_tv);
            holder.mSpecoficationLVItemReserveTV = (Button) convertView.findViewById(R.id.specification_lv_item_reserve_tv);
            holder.mSpecoficationSubsidyImg = (ImageView) convertView.findViewById(R.id.specification_item_subsidy_img);
            holder.mSpecoficationActualPriceTV = (TextView) convertView.findViewById(R.id.specification_item_actual_price_tv);
            holder.mSpecoficationActualPriceLL = (LinearLayout) convertView.findViewById(R.id.specification_item_actual_price_ll);
            holder.mSpecoficationNotProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_not_provided_service_ll);
            holder.mSpecoficationProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_service_ll);
            holder.mSpecoficationCompanyNameImg = (ImageView) convertView.findViewById(R.id.specification_item_companyname_tv);
            holder.mFieldinfoChooseSizeItemDividingLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_choose_size_item_dividing_line);
            //阴影
            holder.mFieldinfoChooseSizeEndDividingLL = (View) convertView.findViewById(R.id.fieldinfo_choose_size_end_dividing_line);
            holder.mFieldinfoChooseSizeSubsidyLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_choose_size_subsidy_ll);
            holder.mFieldinfoServiceTV = (TextView) convertView.findViewById(R.id.fieldinfo_sizelist_service_tv);
            holder.mFieldinfoSizeItemLeaseTermTypeTV = (TextView) convertView.findViewById(R.id.specification_item_lease_term_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (childPosition == childList.get(groupPosition).size() - 1) {
            holder.mFieldinfoChooseSizeItemDividingLL.setVisibility(View.GONE);
        } else {
            holder.mFieldinfoChooseSizeItemDividingLL.setVisibility(View.VISIBLE);
        }
        if (childPosition == 0) {
            holder.mFieldinfoChooseSizeEndDividingLL.setVisibility(View.GONE);
        } else {
            holder.mFieldinfoChooseSizeEndDividingLL.setVisibility(View.GONE);
        }
        holder.mFieldinfoChooseSizeSubsidyLL.setVisibility(View.GONE);
        holder.mSpecoficationItemPriceWeekUnitTV.setVisibility(View.GONE);
        String leaseTermType = "";
        if (groupList.get(groupPosition).getDimension() != null &&
                groupList.get(groupPosition).getDimension().getLease_term_type() != null &&
                groupList.get(groupPosition).getDimension().getLease_term_type().length() > 0) {
            leaseTermType = "/" +
                    groupList.get(groupPosition).getDimension().getLease_term_type();
            holder.mFieldinfoSizeItemLeaseTermTypeTV.setText(leaseTermType);
        } else {
            holder.mFieldinfoSizeItemLeaseTermTypeTV.setText("");
        }
        if (groupList.get(groupPosition).getDimension().getLease_term_type() != null &&
                !groupList.get(groupPosition).getDimension().getLease_term_type().equals(
                        context.getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
            holder.mSpecoficationItemPriceWeekUnitTV.setVisibility(View.VISIBLE);
            holder.mSpecoficationItemPriceTV.setText(Constants.getSpannableAllStr(context,(context.getResources().getString(R.string.order_listitem_price_unit_text) +
                            Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01)), 14,0,1,false,0,0,
                    12,Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01).length() + 1 - 2,
                    Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01).length() + 1,
                    false,0,0));
            holder.mSpecoficationActualPriceLL.setVisibility(View.GONE);
            holder.mSpecoficationSubsidyImg.setVisibility(View.GONE);
        } else {
            if (childList.get(groupPosition).get(childPosition).getPrice() != null) {
                if (childList.get(groupPosition).get(childPosition).getMin_after_subsidy() != null &&
                        childList.get(groupPosition).get(childPosition).getPrice() -
                                childList.get(groupPosition).get(childPosition).getMin_after_subsidy() > 0) {
                    holder.mSpecoficationActualPriceLL.setVisibility(View.VISIBLE);
                    holder.mSpecoficationSubsidyImg.setVisibility(View.VISIBLE);
                    holder.mSpecoficationActualPriceLL.setVisibility(View.VISIBLE);
                    holder.mSpecoficationSubsidyImg.setVisibility(View.VISIBLE);
                    activity.isShareSubsidy = true;
                    holder.mSpecoficationItemPriceTV.setText(Constants.getSpannableAllStr(context,(context.getResources().getString(R.string.order_listitem_price_unit_text) +
                                    Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01)), 14,0,1,false,0,0,
                            12,Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01).length() + 1 - 2,
                            Constants.getSearchPriceStr(Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getMin_after_subsidy(),1), 0.01).length() + 1,
                            false,0,0));
                    holder.mSpecoficationActualPriceTV.setText(Constants.getSpannableAllStr(context,
                            (context.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(String.valueOf(childList.get(groupPosition).get(childPosition).getPrice()),0.01) +
                                    leaseTermType),
                            0,0,0,true,0,Constants.getpricestring(
                                    String.valueOf(childList.get(groupPosition).get(childPosition).getPrice()),0.01).length() + 1 + leaseTermType.length(),null,0,0,false,0,0));
                    holder.mFieldinfoChooseSizeSubsidyLL.setVisibility(View.VISIBLE);
                } else {
                    holder.mSpecoficationItemPriceTV.setText(Constants.getSpannableAllStr(context,(context.getResources().getString(R.string.order_listitem_price_unit_text) +
                                    Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(childList.get(groupPosition).get(childPosition).getPrice()),1), 0.01)), 14,0,1,false,0,0,
                            12,Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(childList.get(groupPosition).get(childPosition).getPrice()),1), 0.01).length() + 1 - 2,
                            Constants.getSearchPriceStr(Constants.getpricestring(String.valueOf(childList.get(groupPosition).get(childPosition).getPrice()),1), 0.01).length() + 1,
                            false,0,0));
                    holder.mSpecoficationActualPriceLL.setVisibility(View.GONE);
                    holder.mSpecoficationSubsidyImg.setVisibility(View.GONE);
                }
            } else {
                holder.mSpecoficationItemPriceTV.setText("");
                holder.mSpecoficationActualPriceTV.setText("");
                holder.mSpecoficationActualPriceLL.setVisibility(View.GONE);
                holder.mSpecoficationSubsidyImg.setVisibility(View.GONE);
            }
        }
        String serviceStr = "";
        if (childList.get(groupPosition).get(childPosition).getHas_power() != null &&
                childList.get(groupPosition).get(childPosition).getHas_power() == 1) {
            serviceStr = context.getResources().getString(R.string.review_power_text);
        }
        if (childList.get(groupPosition).get(childPosition).getHas_chair() != null &&
                childList.get(groupPosition).get(childPosition).getHas_chair() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_chairs_text);
            } else {
                serviceStr = context.getResources().getString(R.string.review_chairs_text);
            }
        }
        if (childList.get(groupPosition).get(childPosition).getHas_tent() != null &&
                childList.get(groupPosition).get(childPosition).getHas_tent() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_tent_str);
            } else {
                serviceStr = context.getResources().getString(R.string.review_tent_str);
            }
        }
        if (childList.get(groupPosition).get(childPosition).getLeaflet() != null &&
                childList.get(groupPosition).get(childPosition).getLeaflet() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_leaf_str);
            } else {
                serviceStr = context.getResources().getString(R.string.review_leaf_str);
            }
        }
        if (childList.get(groupPosition).get(childPosition).getOvernight_material() != null &&
                childList.get(groupPosition).get(childPosition).getOvernight_material() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + context.getResources().getString(R.string.review_goodshelp_text);
            } else {
                serviceStr = context.getResources().getString(R.string.review_goodshelp_text);
            }
        }
        if (serviceStr.trim().length() == 0) {
            holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.GONE);
        } else {
            holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.VISIBLE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.VISIBLE);
            holder.mFieldinfoServiceTV.setText(serviceStr);
        }
        // : 2017/10/23 自营 代理 物业 显示
        if (childList.get(groupPosition).get(childPosition).getCooperation_type_id() != null &&
                childList.get(groupPosition).get(childPosition).getCooperation_type_id() == 12) {
            holder.mSpecoficationCompanyNameImg.setVisibility(View.VISIBLE);
            holder.mSpecoficationCompanyNameImg.setImageResource(R.drawable.ic_self_support_three);
        } else if (childList.get(groupPosition).get(childPosition).getCooperation_type_id() != null &&
                childList.get(groupPosition).get(childPosition).getCooperation_type_id() == 13) {
            holder.mSpecoficationCompanyNameImg.setVisibility(View.VISIBLE);
            holder.mSpecoficationCompanyNameImg.setImageResource(R.drawable.ic_third_agency_three);
        } else if (childList.get(groupPosition).get(childPosition).getCooperation_type_id() != null &&
                childList.get(groupPosition).get(childPosition).getCooperation_type_id() == 14) {
            holder.mSpecoficationCompanyNameImg.setVisibility(View.VISIBLE);
            holder.mSpecoficationCompanyNameImg.setImageResource(R.drawable.ic_property_manage_three);
        } else {
            holder.mSpecoficationCompanyNameImg.setVisibility(View.GONE);
        }
        // : 2017/10/23 认证的预定背景

        if (childList.get(groupPosition).get(childPosition).getIdentification() != null &&
                childList.get(groupPosition).get(childPosition).getIdentification() == 1) {
            if (childList.get(groupPosition).get(childPosition).isExpired()) {
                holder.mSpecoficationLVItemReserveTV.setEnabled(false);
                holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_orderauthentica_disabled_three));
                holder.mSpecoficationLVItemReserveTV.setText("");
            } else {
                holder.mSpecoficationLVItemReserveTV.setEnabled(true);
                holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldinfo_specifications_btn_bg));
                holder.mSpecoficationLVItemReserveTV.setText("");
            }
        } else {
            if (childList.get(groupPosition).get(childPosition).isExpired()) {
                holder.mSpecoficationLVItemReserveTV.setEnabled(false);
                holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_order_disabed_three));
                holder.mSpecoficationLVItemReserveTV.setText("");
            } else {
                holder.mSpecoficationLVItemReserveTV.setEnabled(true);
                holder.mSpecoficationLVItemReserveTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldinfo_specification_reserve_tv_bg));
                holder.mSpecoficationLVItemReserveTV.setText(context.getResources().getString(R.string.fieldinfo_special_item_reserve_str));
            }
        }
        if (!childList.get(groupPosition).get(childPosition).isExpired()) {
            holder.mSpecoficationLVItemReserveTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.prices_list_OnClickListener(groupPosition,String.valueOf(childPosition));
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    /**
     * 当组收缩状态的时候此方法被调用。
     */
    @Override
    public void onGroupCollapsed(int groupPosition) {
        activity.isGetSecondNavHeight = true;
    }
    /**
     * 当组展开状态的时候此方法被调用。
     */
    @Override
    public void onGroupExpanded(int groupPosition) {
        activity.isGetSecondNavHeight = true;
    }
    static class GroupViewHolder {
        public TextView mfieldinfo_specifications_sizetextview;
        public TextView mfieldinfo_specifications_tiemtextview;
        public TextView mfieldinfo_specifications_specialtextview;
        public LinearLayout mFieldinfoChooseCustomDimensionLL;
        public TextView mFieldinfoDayInAdvanceTV;
        public TextView mfieldinfo_specifications_pricetextview;
        public TextView mSpecoficationItemPriceTV;
        public TextView mSpecoficationItemPriceWeekUnitTV;
        public Button mSpecoficationLVItemReserveTV;
        public ImageView mSpecoficationSubsidyImg;
        public TextView mSpecoficationActualPriceTV;
        public LinearLayout mSpecoficationActualPriceLL;
        public LinearLayout mSpecoficationOneSizeLL;
        public LinearLayout mSpecoficationOneSizeServiceLL;
        public LinearLayout mSpecoficationMoreSizeLL;
        public LinearLayout mSpecoficationNotProvidServiceedLL;
        public LinearLayout mSpecoficationProvidServiceedLL;
        public ImageView mOtherSizeImgV;
        public LinearLayout mFieldinfoChooseSizeFirstDividingLL;
        public LinearLayout mFieldinfoChooseSizeSubsidyLL;
        public View mFieldinfoGroupView;
        public TextView mFieldinfoServiceTV;
        public TextView mFieldinfoCooperationTypeTV;
    }
    static class ViewHolder {
        public TextView mSpecoficationItemPriceTV;
        public TextView mSpecoficationItemPriceWeekUnitTV;
        public Button mSpecoficationLVItemReserveTV;
        public ImageView mSpecoficationSubsidyImg;
        public TextView mSpecoficationActualPriceTV;
        public LinearLayout mSpecoficationActualPriceLL;
        public LinearLayout mSpecoficationNotProvidServiceedLL;
        public LinearLayout mSpecoficationProvidServiceedLL;

        public ImageView mSpecoficationCompanyNameImg;
        public LinearLayout mFieldinfoChooseSizeItemDividingLL;
        public View mFieldinfoChooseSizeEndDividingLL;
        public LinearLayout mFieldinfoChooseSizeSubsidyLL;
        public TextView mFieldinfoServiceTV;
        public TextView mFieldinfoSizeItemLeaseTermTypeTV;
    }
}
