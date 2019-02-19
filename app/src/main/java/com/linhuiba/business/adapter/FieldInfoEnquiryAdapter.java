package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.FieldInfoSellResourceModel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class FieldInfoEnquiryAdapter extends BaseAdapter {
    private FieldInfoActivity mActivity;
    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<FieldInfoSellResourceModel> mDataList;//里层的数据源
    public FieldInfoEnquiryAdapter(Context context, FieldInfoActivity activity,
                                   List<FieldInfoSellResourceModel> list){
        if (context != null &&
                activity != null) {
            this.mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.mActivity = activity;
            this.mDataList = list;
        }
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_fieldinfopricelistitem,null);
            holder.mEnquiryResourcesRL = (RelativeLayout)convertView.findViewById(R.id.fieldinfo_enquiry_resource_rl);
            holder.mSizeResourcesRL = (RelativeLayout)convertView.findViewById(R.id.fieldinfo_size_rl);
            holder.mFieldinfoEnquiryPriceTV = (TextView)convertView.findViewById(R.id.enquiry_price_tv);
            holder.mFieldinfoEnquiryBtn = (TextView) convertView.findViewById(R.id.enquiry_btn);
            holder.mSpecoficationOneSizeServiceLL = (LinearLayout) convertView.findViewById(R.id.fieldinfo_specifications_one_size_service_layout);
            holder.mSpecoficationNotProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_not_provided_service_ll);
            holder.mSpecoficationProvidServiceedLL = (LinearLayout) convertView.findViewById(R.id.specification_item_service_ll);
            holder.mFieldinfoServiceTV = (TextView) convertView.findViewById(R.id.fieldinfo_sizelist_service_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mSizeResourcesRL.setVisibility(View.GONE);
        holder.mEnquiryResourcesRL.setVisibility(View.VISIBLE);
        if (mDataList.get(position).getRefer_min_price() != null &&
                mDataList.get(position).getRefer_max_price() != null) {
            holder.mFieldinfoEnquiryPriceTV.setText(
                    mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                            Constants.getpricestring(String.valueOf(mDataList.get(position).getRefer_min_price()),0.01) +
                            "~" +
                            mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                            Constants.getpricestring(String.valueOf(mDataList.get(position).getRefer_max_price()),0.01));
        } else {
            if (mDataList.get(position).getMin_price() != null) {
                holder.mFieldinfoEnquiryPriceTV.setText(
                        mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(String.valueOf(mDataList.get(position).getRefer_min_price()),0.01));
            }
            if (mDataList.get(position).getRefer_max_price() != null) {
                holder.mFieldinfoEnquiryPriceTV.setText(
                        mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(String.valueOf(mDataList.get(position).getRefer_max_price()),0.01));
            }
        }
        String serviceStr = "";
        if (mDataList.get(position).getHas_power() != null &&
                mDataList.get(position).getHas_power() == 1) {
            serviceStr = mContext.getResources().getString(R.string.review_power_text);
        }
        if (mDataList.get(position).getHas_chair() != null &&
                mDataList.get(position).getHas_chair() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + mContext.getResources().getString(R.string.review_chairs_text);
            } else {
                serviceStr = mContext.getResources().getString(R.string.review_chairs_text);
            }
        }
        if (mDataList.get(position).getHas_tent() != null &&
                mDataList.get(position).getHas_tent() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + mContext.getResources().getString(R.string.review_tent_str);
            } else {
                serviceStr = mContext.getResources().getString(R.string.review_tent_str);
            }
        }
        if (mDataList.get(position).getLeaflet() != null &&
                mDataList.get(position).getLeaflet() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + mContext.getResources().getString(R.string.review_leaf_str);
            } else {
                serviceStr = mContext.getResources().getString(R.string.review_leaf_str);
            }
        }
        if (mDataList.get(position).getOvernight_material() != null &&
                mDataList.get(position).getOvernight_material() == 1) {
            if (serviceStr.length() > 0) {
                serviceStr = serviceStr + "、" + mContext.getResources().getString(R.string.review_goodshelp_text);
            } else {
                serviceStr = mContext.getResources().getString(R.string.review_goodshelp_text);
            }
        }
        if (serviceStr.trim().length() == 0) {
            holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationOneSizeServiceLL.setVisibility(View.GONE);
        } else {
            holder.mSpecoficationNotProvidServiceedLL.setVisibility(View.GONE);
            holder.mSpecoficationProvidServiceedLL.setVisibility(View.VISIBLE);
            holder.mSpecoficationOneSizeServiceLL.setVisibility(View.VISIBLE);
            holder.mFieldinfoServiceTV.setText(serviceStr);
        }
        // : 2017/10/23 认证的预定背景
        if (mDataList.get(position).getIdentification() != null &&
                mDataList.get(position).getIdentification() == 1) {
            if (mDataList.get(position).isExpired()) {
                holder.mFieldinfoEnquiryBtn.setEnabled(false);
                holder.mFieldinfoEnquiryBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_vxunjia_disabled_three_one));
            } else {
                holder.mFieldinfoEnquiryBtn.setEnabled(true);
                holder.mFieldinfoEnquiryBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.fieldinfo_enquiry_item_identification_btn_bg));
            }

        } else {
            if (mDataList.get(position).isExpired()) {
                holder.mFieldinfoEnquiryBtn.setEnabled(false);
                holder.mFieldinfoEnquiryBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_xunjia_disabled_three_one));
            } else {
                holder.mFieldinfoEnquiryBtn.setEnabled(true);
                holder.mFieldinfoEnquiryBtn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_fieldinfo_specification_reserve_tv_bg));
            }
        }
        if (!mDataList.get(position).isExpired()) {
            holder.mFieldinfoEnquiryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.prices_list_OnClickListener(position,
                            mContext.getResources().getString(R.string.fieldinfo_enquiry_str));
                }
            });
        }
        return convertView;
    }
    static class ViewHolder {
        public RelativeLayout mEnquiryResourcesRL;
        public RelativeLayout mSizeResourcesRL;
        public TextView mFieldinfoEnquiryPriceTV;
        public TextView mFieldinfoEnquiryBtn;
        public LinearLayout mSpecoficationNotProvidServiceedLL;
        public LinearLayout mSpecoficationProvidServiceedLL;
        public TextView mFieldinfoServiceTV;
        public LinearLayout mSpecoficationOneSizeServiceLL;
    }
}
