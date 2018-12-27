package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.OrderConfirmChoosePayWayActivity;
import com.linhuiba.business.activity.OrderListNewActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.GroupBookingMySelfFragment;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.model.GroupBookingOrderListModel;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/11.
 */

public class GroupBookingFragmentOrderAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private GroupBookingMySelfFragment mActivity;
    private Context mContext;
    private ArrayList<GroupBookingOrderListModel> mDataList = new ArrayList<>();
    public GroupBookingFragmentOrderAdapter(GroupBookingMySelfFragment activity, Context context,
                                            ArrayList<GroupBookingOrderListModel> dataList) {
        if (context != null && activity != null) {
            this.mActivity = activity;
            this.mContext = context;
            this.mDataList =dataList;
            mLayoutInflater = LayoutInflater.from(context);
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
            convertView = mLayoutInflater.inflate(R.layout.fragment_groupbooking_myself_list_item, null);
            holder.mGroupBookOrderItemResNameTV = (TextView) convertView.findViewById(R.id.groupbooking_order_res_name_tv);
            holder.mGroupBookOrderItemStatusTV = (TextView) convertView.findViewById(R.id.groupbooking_order_status_tv);
            holder.mGroupBookOrderItemImgView = (ImageView) convertView.findViewById(R.id.groupbooking_order_imgView);
            holder.mGroupBookOrderItemTimeTV = (TextView) convertView.findViewById(R.id.groupbooking_order_time_tv);
            holder.mGroupBookOrderItemSizeTV = (TextView) convertView.findViewById(R.id.groupbooking_order_size_tv);
            holder.mGroupBookOrderItemPriceTV = (TextView) convertView.findViewById(R.id.groupbooking_order_price_tv);
            holder.mGroupBookOrderItemspecificationTV = (TextView) convertView.findViewById(R.id.groupbooking_order_specification_tv);
            holder.mGroupBookOrderItemSellPriceTV = (TextView) convertView.findViewById(R.id.groupbooking_order_sell_price_tv);
            holder.mGroupBookOrderItemActualPriceTV = (TextView) convertView.findViewById(R.id.groupbooking_order_actual_price_tv);
            holder.mGroupBookOrderContactUsTV = (TextView) convertView.findViewById(R.id.groupbooking_order_contact_us_tv);
            holder.mGroupBookOrderClickTV = (TextView) convertView.findViewById(R.id.groupbooking_order_click_tv);
            holder.mGroupBookOrderClickLL = (LinearLayout) convertView.findViewById(R.id.groupbooking_order_btn_ll);
            holder.mGroupBookOrderRefuseLL = (LinearLayout) convertView.findViewById(R.id.group_order_refuse_ll);
            holder.mGroupBookOrderRefuseTV = (TextView) convertView.findViewById(R.id.group_order_refuse_tv);
            holder.mGroupBookOrderInfoll = (LinearLayout) convertView.findViewById(R.id.group_order_item_info_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_name() != null) {
            holder.mGroupBookOrderItemResNameTV.setText(mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_name());
        } else {
            holder.mGroupBookOrderItemResNameTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() != null) {
            Picasso.with(mContext).load(mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(136, 136).into(holder.mGroupBookOrderItemImgView);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small).resize(136, 136).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.mGroupBookOrderItemImgView);
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_name() != null) {
            holder.mGroupBookOrderItemResNameTV.setText(mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_name());
        } else {
            holder.mGroupBookOrderItemResNameTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getGroup_purchase().getField_length() != null &&
                mDataList.get(position).getSelling_resource().getGroup_purchase().getField_width() != null) {
            holder.mGroupBookOrderItemSizeTV.setText(mDataList.get(position).getSelling_resource().getGroup_purchase().getField_length() +
            mContext.getResources().getString(R.string.mandatory_textview) +
                    mDataList.get(position).getSelling_resource().getGroup_purchase().getField_width());
        } else {
            holder.mGroupBookOrderItemSizeTV.setText("");
        }
        if (mDataList.get(position).getReal_cost() != null) {
            holder.mGroupBookOrderItemActualPriceTV.setText(Constants.getdoublepricestring(mDataList.get(position).getReal_cost(),0.01));
        } else {
            holder.mGroupBookOrderItemActualPriceTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_allow() != null &&
                mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_allow().length() > 0) {
            holder.mGroupBookOrderItemspecificationTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderItemspecificationTV.setText(mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_allow());
        } else {
            holder.mGroupBookOrderItemspecificationTV.setVisibility(View.GONE);
        }
        if (mDataList.get(position).getSelling_resource().getGroup_purchase().getOrigin_price() != null) {
            holder.mGroupBookOrderItemSellPriceTV.setText(Constants.getSpannableAllStr(mContext,
                    (mContext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getdoublepricestring(mDataList.get(position).getSelling_resource().getGroup_purchase().getOrigin_price(),1)),
                    0,0,0,true,0,Constants.getdoublepricestring(mDataList.get(position).getSelling_resource().getGroup_purchase().getOrigin_price(),1).length() + 1,null,0,0,
                    false,0,0));

        } else {
            holder.mGroupBookOrderItemSellPriceTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getFirst_selling_resource_price() != null &&
                mDataList.get(position).getSelling_resource().getFirst_selling_resource_price().get("price") != null &&
                mDataList.get(position).getSelling_resource().getFirst_selling_resource_price().get("price").toString().length() > 0) {
            holder.mGroupBookOrderItemPriceTV.setText(mContext.getResources().getString(R.string.order_listitem_price_unit_text)+
                    Constants.getpricestring(mDataList.get(position).getSelling_resource().getFirst_selling_resource_price().get("price").toString(),0.01));
        } else {
            holder.mGroupBookOrderItemPriceTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() != null &&
                (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url") != null &&
                        mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url").length() > 0)) {
            Picasso.with(mContext).load(mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url")
                    + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(136, 136).into(holder.mGroupBookOrderItemImgView);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_small).resize(136, 136).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.mGroupBookOrderItemImgView);
        }
        if (mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_start() != null &&
                mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_end() != null &&
                mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_start().length() > 0 &&
                mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_end().length() > 0) {
            holder.mGroupBookOrderItemTimeTV.setText(mContext.getResources().getString(R.string.home_activity_item_timetxt)+
                    mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_start() +
            "-" +mDataList.get(position).getSelling_resource().getGroup_purchase().getActivity_end());
        } else {
            holder.mGroupBookOrderItemTimeTV.setText("");
        }
        holder.mGroupBookOrderRefuseLL.setVisibility(View.GONE);
        if (mDataList.get(position).getOrder_status().equals("submitted")) {
            holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.myselfinfo_pay));
            holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.default_redbg));
            holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderClickTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setText(mContext.getResources().getString(R.string.order_cancel_ordertxt));
            holder.mGroupBookOrderClickTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_item_pay_tv_str));
            holder.mGroupBookOrderContactUsTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_cancel_paybtn_bg));
            holder.mGroupBookOrderContactUsTV.setTextColor(mContext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            holder.mGroupBookOrderClickTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_paybtn_bg));
            holder.mGroupBookOrderClickTV.setTextColor(mContext.getResources().getColor(R.color.fieldinfo_reserve_tv_bg));
            holder.mGroupBookOrderContactUsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < mDataList.size()) {
                        mActivity.isPayOperation = false;
                        mActivity.orderOperation(position,String.valueOf(mDataList.get(position).getField_order_id()));
                    }
                }
            });
            holder.mGroupBookOrderClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付
                    if (position < mDataList.size()) {
                        mActivity.isPayOperation = true;
                        mActivity.orderOperation(position, JSONObject.toJSONString(mDataList.get(position),true));
                    }
                }
            });
        } if (mDataList.get(position).getOrder_status().equals("paid")) {
            //待审核
            holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_underway_status_str));
            holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.default_redbg));
            holder.mGroupBookOrderContactUsTV.setVisibility(View.GONE);
            holder.mGroupBookOrderClickTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderClickTV.setText(mContext.getResources().getString(R.string.order_listitem_paid_right_btn_str));
            holder.mGroupBookOrderClickTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_approved_btn_bg));
            holder.mGroupBookOrderClickTV.setTextColor(mContext.getResources().getColor(R.color.checked_tv_color));
            holder.mGroupBookOrderClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //催审核
                    if (position < mDataList.size()) {
                        mActivity.orderOperation(-1,"-1");
                    }
                }
            });

        } else if (mDataList.get(position).getOrder_status().equals("approved")) {
            //进行中
            if (mDataList.get(position).getSelling_resource().getGroup_purchase().getGroup_status() != null) {
                if (mDataList.get(position).getSelling_resource().getGroup_purchase().getGroup_status() == 0) {
                    holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_underway_status_str));
                    holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.default_redbg));
                    holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
                } else if (mDataList.get(position).getSelling_resource().getGroup_purchase().getGroup_status() == 1) {
                    holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_success_status_str));
                    holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.order_list_item_finished_status_tv_color));
                    holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
                } else if (mDataList.get(position).getSelling_resource().getGroup_purchase().getGroup_status() == 2) {
                    holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_fail_status_str));
                    holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.default_redbg));
                    holder.mGroupBookOrderClickLL.setVisibility(View.GONE);
                }
            }
            holder.mGroupBookOrderContactUsTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderClickTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setText(mContext.getResources().getString(R.string.order_listitem_approved_left_btn_str));
            holder.mGroupBookOrderClickTV.setText(mContext.getResources().getString(R.string.order_listitem_approved_right_btn_str));
            holder.mGroupBookOrderContactUsTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_cancel_paybtn_bg));
            holder.mGroupBookOrderContactUsTV.setTextColor(mContext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            holder.mGroupBookOrderClickTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_approved_btn_bg));
            holder.mGroupBookOrderClickTV.setTextColor(mContext.getResources().getColor(R.color.checked_tv_color));
            holder.mGroupBookOrderContactUsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < mDataList.size()) {
                        if (mDataList.get(position).getService_phone()!= null &&
                                mDataList.get(position).getId() != null &&
                                mDataList.get(position).getService_phone().length() > 0) {
                            mActivity.setVirtualNumber(-1,mDataList.get(position).getId(),
                                    mDataList.get(position).getService_phone());
                        }
                    }
                }
            });
            holder.mGroupBookOrderClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2017/11/1
                    //查看交易凭证
                    if (position < mDataList.size()) {
                        Intent orderinfo = new Intent(mActivity.getActivity(),AboutUsActivity.class);
                        orderinfo.putExtra("id", mDataList.get(position).getField_order_id().toString());
                        orderinfo.putExtra("type", com.linhuiba.business.config.Config.DEAL_VOUCHER_INT);
                        mActivity.getActivity().startActivity(orderinfo);
                    }
                }
            });
        } else if (mDataList.get(position).getOrder_status().equals("canceled")) {
            //已拒绝
            holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_fail_status_str));
            holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.default_redbg));
            holder.mGroupBookOrderClickLL.setVisibility(View.GONE);
            holder.mGroupBookOrderRefuseLL.setVisibility(View.VISIBLE);
            if (mDataList.get(position).getObjection() != null &&
                    mDataList.get(position).getObjection().length() > 0) {
                holder.mGroupBookOrderRefuseTV.setText(mDataList.get(position).getObjection());
            } else {
                holder.mGroupBookOrderRefuseTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
        } else if (mDataList.get(position).getOrder_status().equals("finished")) {
            //待评价
            holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_success_status_str));
            holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.order_list_item_finished_status_tv_color));
            holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderClickTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setText(mContext.getResources().getString(R.string.order_listitem_approved_right_btn_str));
            holder.mGroupBookOrderClickTV.setText(mContext.getResources().getString(R.string.order_listitem_finish_right_btn_str));
            holder.mGroupBookOrderContactUsTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_cancel_paybtn_bg));
            holder.mGroupBookOrderContactUsTV.setTextColor(mContext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
            holder.mGroupBookOrderClickTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_paybtn_bg));
            holder.mGroupBookOrderClickTV.setTextColor(mContext.getResources().getColor(R.color.fieldinfo_reserve_tv_bg));
            holder.mGroupBookOrderContactUsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2017/11/1
                    //查看交易凭证
                    if (position < mDataList.size()) {
                        Intent orderinfo = new Intent(mActivity.getActivity(),AboutUsActivity.class);
                        orderinfo.putExtra("id", mDataList.get(position).getField_order_id().toString());
                        orderinfo.putExtra("type", com.linhuiba.business.config.Config.DEAL_VOUCHER_INT);
                        mActivity.getActivity().startActivity(orderinfo);
                    }
                }
            });
            holder.mGroupBookOrderClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //评价
                    if (position < mDataList.size()) {
                        if (LoginManager.isLogin()) {
                            Intent publidhreview = new Intent(mActivity.getActivity(), PublishReviewActivity.class);
                            publidhreview.putExtra("orderid", mDataList.get(position).getField_order_id());
                            mActivity.startActivityForResult(publidhreview, 1);
                        } else {
                            LoginManager.getInstance().clearLoginInfo();
                            LoginActivity.BaesActivityreloadLogin(mActivity.getActivity());
                            mActivity.getActivity().finish();
                        }
                    }
                }
            });
        } else if (mDataList.get(position).getOrder_status().equals("finished")) {
            //已评价
            holder.mGroupBookOrderItemStatusTV.setText(mContext.getResources().getString(R.string.groupbooding_mysefl_orders_success_status_str));
            holder.mGroupBookOrderItemStatusTV.setTextColor(mContext.getResources().getColor(R.color.order_list_item_finished_status_tv_color));
            holder.mGroupBookOrderClickLL.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderContactUsTV.setVisibility(View.GONE);
            holder.mGroupBookOrderClickTV.setVisibility(View.VISIBLE);
            holder.mGroupBookOrderClickTV.setText(mContext.getResources().getString(R.string.order_listitem_delete_order_btn_str));
            holder.mGroupBookOrderClickTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_orderlist_item_approved_btn_bg));
            holder.mGroupBookOrderClickTV.setTextColor(mContext.getResources().getColor(R.color.checked_tv_color));
            holder.mGroupBookOrderClickTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除订单
                    if (position < mDataList.size()) {
                        mActivity.isPayOperation = false;
                        mActivity.orderOperation(position,String.valueOf(mDataList.get(position).getSelling_resource_id()));

                    }
                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderinfo = new Intent(mActivity.getActivity(), AboutUsActivity.class);
                if (position < mDataList.size()) {
                    orderinfo.putExtra("id", mDataList.get(position).getField_order_id());
                    if (mDataList.get(position).getOrder_status() != null) {
                        if (mDataList.get(position).getOrder_status().equals("submitted")) {
                            orderinfo.putExtra("type", com.linhuiba.business.config.Config.SUBMITTED_BIG_ORDER_INFO_INT);
                            orderinfo.putExtra("group_order", 1);
                            mActivity.getActivity().startActivity(orderinfo);
                        } else {
                            orderinfo.putExtra("type", com.linhuiba.business.config.Config.BIG_ORDER_INFO_INT);
                            mActivity.getActivity().startActivity(orderinfo);
                        }
                    }

                }
            }
        });

        return convertView;
    }
    static class ViewHolder {
        public TextView mGroupBookOrderItemResNameTV;
        public TextView mGroupBookOrderItemStatusTV;
        public ImageView mGroupBookOrderItemImgView;
        public TextView mGroupBookOrderItemTimeTV;
        public TextView mGroupBookOrderItemSizeTV;
        public TextView mGroupBookOrderItemPriceTV;
        public TextView mGroupBookOrderItemspecificationTV;
        public TextView mGroupBookOrderItemSellPriceTV;
        public TextView mGroupBookOrderItemActualPriceTV;
        public TextView mGroupBookOrderContactUsTV;
        public TextView mGroupBookOrderClickTV;
        public LinearLayout mGroupBookOrderClickLL;
        public LinearLayout mGroupBookOrderRefuseLL;
        public TextView mGroupBookOrderRefuseTV;
        public LinearLayout mGroupBookOrderInfoll;
    }
}
