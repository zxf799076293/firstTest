package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.OrderConfirmChoosePayWayActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.OrderInfoModel;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/3.
 */

public class OrderConfirmSuccessReviewOrderAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater mInflater = null;
    private int type;
    private List<Map<String,Object>> mData;
    private List<List<OrderInfoModel>> mChildData;
    private OrderConfirmChoosePayWayActivity mactivity;
    public OrderConfirmSuccessReviewOrderAdapter(Context cxt,List<Map<String,Object>> data,List<List<OrderInfoModel>>childdata,OrderConfirmChoosePayWayActivity mactivity,int type) {
        this.mInflater = LayoutInflater.from(cxt);
        this.context = cxt;
        this.mData =data;
        this.mChildData = childdata;
        this.mactivity = mactivity;
        this.type = type;
    }
    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(groupPosition).get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(final int GroupPositio, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null) {
            holder = new GroupViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.activity_orderlist_item, null);
            holder.morderimg = (ImageView)convertView.findViewById(R.id.order_img);
            holder.mfield_name = (TextView)convertView.findViewById(R.id.order_fieldname);
            holder.mprice = (TextView)convertView.findViewById(R.id.order_price);
            holder.mstart = (TextView)convertView.findViewById(R.id.field_start);
            holder.morder_status = (TextView)convertView.findViewById(R.id.order_status);
            holder.morder_type_btn_txt = (TextView)convertView.findViewById(R.id.order_type_btn_txt);//订单不同时的操作按钮（支付/评价）
            holder.mgroup_hidelayout = (LinearLayout)convertView.findViewById(R.id.group_hidelayout);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else
        {
            holder = (GroupViewHolder)convertView.getTag();
        }
        holder.mgroup_hidelayout.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.activity_orderlist_item, null);
            holder.morderimg = (ImageView)convertView.findViewById(R.id.order_img);
            holder.mfield_name = (TextView)convertView.findViewById(R.id.order_fieldname);
            holder.mprice = (TextView)convertView.findViewById(R.id.order_price);
            holder.mOrderInfoOriginalPriceTV = (TextView)convertView.findViewById(R.id.orderlist_item_actual_price);
            holder.mstart = (TextView)convertView.findViewById(R.id.field_start);
            holder.morder_status = (TextView)convertView.findViewById(R.id.order_status);
            holder.morder_size = (TextView)convertView.findViewById(R.id.field_size);
            holder.morderlist_custom_dimensiontxt = (TextView)convertView.findViewById(R.id.orderlist_custom_dimensiontxt);
            holder.morder_type_btn_txt = (TextView)convertView.findViewById(R.id.order_type_btn_txt);//订单不同时的操作按钮（支付/评价）
            holder.mgroup_hidelayout = (LinearLayout)convertView.findViewById(R.id.group_hidelayout);
            holder.mhideview_layout = (LinearLayout)convertView.findViewById(R.id.hideview_layout);
            holder.mgroup_layout = (LinearLayout)convertView.findViewById(R.id.group_layout);
            holder.morder_all_price = (TextView)convertView.findViewById(R.id.order_all_price);
            holder.morder_all_price_textview = (TextView)convertView.findViewById(R.id.order_all_price_textview);
            holder.mOrderItemDepositTV = (TextView) convertView.findViewById(R.id.orderlist_item_deposit_price);
            holder.morder_num = (TextView)convertView.findViewById(R.id.order_num);
            holder.mcount_week = (TextView)convertView.findViewById(R.id.count_week);
            holder.mcount_frame = (TextView)convertView.findViewById(R.id.count_frame);
            holder.mcancelbtn = (TextView)convertView.findViewById(R.id.order_cancel_btn_txt);
            holder.mrefused_linearlayout = (LinearLayout)convertView.findViewById(R.id.refused_linearlayout);
            holder.mrefused_textview = (TextView)convertView.findViewById(R.id.refused_textview);
            holder.mrefused_tmplinearlayout = (LinearLayout)convertView.findViewById(R.id.refused_tmplinearlayout);
            holder.morder_listitem_btnlayout = (LinearLayout)convertView.findViewById(R.id.order_listitem_btnlayout);
            holder.morder_listitem_ordernumlayout = (RelativeLayout)convertView.findViewById(R.id.order_listitem_ordernumlayout);
            holder.morderlist_item_cancel_img = (ImageView)convertView.findViewById(R.id.orderlist_item_cancel_img);
            holder.morder_listitem_orderpricelayout = (RelativeLayout)convertView.findViewById(R.id.order_listitem_orderpricelayout);
            holder.mOrderListItemResNameView = (LinearLayout) convertView.findViewById(R.id.orderlist_item_resname_view);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (mChildData!=null) {
            if (mChildData.size() != 0) {
                if (mChildData.get(groupPosition) != null) {
                    if (mChildData.get(groupPosition).size() != 0) {
                        holder.mgroup_hidelayout.setVisibility(View.VISIBLE);
                        if (mChildData.get(groupPosition).get(childPosition).getPic_url() != null) {
                            if (mChildData.get(groupPosition).get(childPosition).getPic_url().toString().length() != 0) {
                                Picasso.with(context).load(mChildData.get(groupPosition).get(childPosition).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(180, 180).into(holder.morderimg);
                            } else {
                                Picasso.with(context).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.morderimg);
                            }
                        } else {
                            Picasso.with(context).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.morderimg);
                        }
                        if (mChildData.get(groupPosition).get(childPosition).getField_name() !=null) {
                            holder.mfield_name.setText(mChildData.get(groupPosition).get(childPosition).getField_name().toString());
                        } else {
                            holder.mfield_name.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                        }
                        if (mChildData.get(groupPosition).get(childPosition).getPrice()!=null &&
                                mChildData.get(groupPosition).get(childPosition).getSubsidy_fee() != null &&
                                mChildData.get(groupPosition).get(childPosition).getPrice().length() > 0 &&
                                mChildData.get(groupPosition).get(childPosition).getSubsidy_fee().length() > 0 &&
                                Double.parseDouble(mChildData.get(groupPosition).get(childPosition).getSubsidy_fee()) > 0) {
                            holder.mOrderInfoOriginalPriceTV.setText(Constants.getPriceUnitStr(context,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(mChildData.get(groupPosition).get(childPosition).getPrice(),0.01)),12));
                            holder.mOrderInfoOriginalPriceTV.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                            if (mChildData.get(groupPosition).get(childPosition).getPrice()!=null &&
                                    mChildData.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                                holder.mprice.setText(Constants.getPriceUnitStr(context,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                        com.linhuiba.linhuifield.connector.Constants.getdoublepricestring(Double.parseDouble(mChildData.get(groupPosition).get(childPosition).getPrice()) -
                                                Double.parseDouble(mChildData.get(groupPosition).get(childPosition).getSubsidy_fee()),0.01)),12));
                            } else {
                                holder.mprice.setText("");
                            }
                        } else {
                            holder.mOrderInfoOriginalPriceTV.setText("");
                            if (mChildData.get(groupPosition).get(childPosition).getPrice()!=null &&
                                    mChildData.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                                holder.mprice.setText(Constants.getPriceUnitStr(context,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                        Constants.getpricestring(mChildData.get(groupPosition).get(childPosition).getPrice(),0.01)),12));
                            } else {
                                holder.mprice.setText("");
                            }
                        }

                        if (mChildData.get(groupPosition).get(childPosition).getStart()!=null) {
                            holder.mstart.setText(mactivity.getResources().getString(R.string.home_activity_item_timetxt)+(String) mChildData.get(groupPosition).get(childPosition).getStart().toString());
                        } else {
                            holder.mstart.setText(mactivity.getResources().getString(R.string.home_activity_item_timetxt)+
                                    mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                        }
                        if (mChildData.get(groupPosition).get(childPosition).getSize() != null) {
                            holder.morder_size.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt) + mChildData.get(groupPosition).get(childPosition).getSize());
                            if (mChildData.get(groupPosition).get(childPosition).getLease_term_type() != null
                                    && mChildData.get(groupPosition).get(childPosition).getLease_term_type().length() > 0) {
                                holder.morder_size.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt) + mChildData.get(groupPosition).get(childPosition).getSize()+
                                        "（"+ mChildData.get(groupPosition).get(childPosition).getLease_term_type()+"）");
                            }
                        } else {
                            holder.morder_size.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt) +
                                    mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                        }
                        if (mChildData.get(groupPosition).get(childPosition).getCustom_dimension() != null &&
                                mChildData.get(groupPosition).get(childPosition).getCustom_dimension().length() > 0) {
                            holder.morderlist_custom_dimensiontxt.setVisibility(View.VISIBLE);
                            holder.morderlist_custom_dimensiontxt.setText(mChildData.get(groupPosition).get(childPosition).getCustom_dimension());
                        } else {
                            holder.morderlist_custom_dimensiontxt.setVisibility(View.GONE);
                        }
                        if (mChildData.get(groupPosition).get(childPosition).getOrder_type()!= null) {
                            if (mChildData.get(groupPosition).get(childPosition).getOrder_type().toString().length()  != 0) {
                                if (mChildData.get(groupPosition).get(childPosition).getOrder_type().toString().equals("1")) {
                                    holder.mcount_week.setVisibility(View.GONE);
                                    holder.mcount_frame.setVisibility(View.GONE);
                                } else if (mChildData.get(groupPosition).get(childPosition).getOrder_type().toString().equals("2")) {
                                    holder.mcount_week.setVisibility(View.GONE);
                                    holder.mcount_frame.setVisibility(View.VISIBLE);
                                    if (mChildData.get(groupPosition).get(childPosition).getCount_of_frame() != null &&
                                            mChildData.get(groupPosition).get(childPosition).getCount_of_frame().length() > 0) {
                                        holder.mcount_frame.setText(mChildData.get(groupPosition).get(childPosition).getCount_of_frame() + "个");
                                    }
                                } else if (mChildData.get(groupPosition).get(childPosition).getOrder_type().toString().equals("3")) {
                                    holder.mcount_week.setVisibility(View.GONE);
                                    holder.mcount_frame.setVisibility(View.GONE);
                                } else {
                                    holder.mcount_week.setVisibility(View.GONE);
                                    holder.mcount_frame.setVisibility(View.GONE);
                                }
                            } else {
                                holder.mcount_week.setVisibility(View.GONE);
                                holder.mcount_frame.setVisibility(View.GONE);
                            }
                        } else {
                            holder.mcount_week.setVisibility(View.GONE);
                            holder.mcount_frame.setVisibility(View.GONE);
                        }
                        holder.mrefused_linearlayout.setVisibility(View.GONE);
                        holder.morderlist_item_cancel_img.setVisibility(View.GONE);
                        holder.morder_listitem_ordernumlayout.setVisibility(View.GONE);
                        if (mChildData.get(groupPosition).get(childPosition).getStatus() != null) {
                            if (mChildData.get(groupPosition).get(childPosition).getStatus().length()>0) {
                                if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("canceled")) {
                                    holder.morderlist_custom_dimensiontxt.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.orderlist_canceled_order_custom_dimensionbg));
                                    holder.mprice.setTextColor(mactivity.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                                    holder.mfield_name.setTextColor(mactivity.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                                    holder.morderlist_item_cancel_img.setVisibility(View.VISIBLE);
                                } else {
                                    holder.morderlist_custom_dimensiontxt.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.field_activity_addfield_five_specialtype_bg));
                                    holder.mprice.setTextColor(mactivity.getResources().getColor(R.color.top_title_center_txt_color));
                                    holder.mfield_name.setTextColor(mactivity.getResources().getColor(R.color.top_title_center_txt_color));
                                    holder.morderlist_item_cancel_img.setVisibility(View.GONE);
                                }
                                if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("submitted")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_pay));
                                } else if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("canceled")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_refused));
                                } else if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("paid")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_check));
                                } else if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("approved")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_waiting));
                                } else if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("finished")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_review));
                                } else if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("reviewed")) {
                                    holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_reviewed));
                                }
                                holder.mOrderListItemResNameView.setVisibility(View.GONE);
                                if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("submitted")) {
                                    if (childPosition == 0) {
                                        holder.morder_listitem_ordernumlayout.setVisibility(View.VISIBLE);
                                        holder.mOrderListItemResNameView.setVisibility(View.GONE);
                                        holder.morder_num.setText(mactivity.getResources().getString(R.string.order_confirm_ordernum_text) + mData.get(groupPosition).get("order_num").toString());
                                    } else {
                                        holder.morder_listitem_ordernumlayout.setVisibility(View.GONE);
                                        holder.mOrderListItemResNameView.setVisibility(View.VISIBLE);
                                    }
                                    if (mData.get(groupPosition).get("offline_pay") != null && mData.get(groupPosition).get("offline_pay").toString().length() > 0
                                            && Integer.parseInt(mData.get(groupPosition).get("offline_pay").toString()) == 1 ) {
                                        if (mData.get(groupPosition).get("voucher_image_url") != null && mData.get(groupPosition).get("voucher_image_url").toString().length() >0) {
                                            holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_offline_pay));
                                        } else {
                                            holder.morder_status.setText(mactivity.getResources().getString(R.string.myselfinfo_offline_pay_noupload));
                                        }
                                    }
                                }
                                if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("canceled")) {
                                    if (mChildData.get(groupPosition).get(childPosition).getObjection() != null &&
                                            mChildData.get(groupPosition).get(childPosition).getObjection().length() > 0) {
                                        holder.mrefused_textview.setText(mChildData.get(groupPosition).get(childPosition).getObjection().trim());
                                    } else {
                                        holder.mrefused_textview.setText(mactivity.getResources().getString(R.string.order_refuse_ordertxtnull));
                                    }
                                    holder.mrefused_linearlayout.setVisibility(View.VISIBLE);
                                }

                                if (mChildData.get(groupPosition).get(childPosition).getStatus().toString().equals("finished")) {
                                    holder.morder_status.setTextColor(context.getResources().getColor(R.color.order_list_item_finished_status_tv_color));
                                } else {
                                    holder.morder_status.setTextColor(context.getResources().getColor(R.color.default_redbg));
                                }

                            } else {
                                holder.morder_status.setText("");
                            }
                        } else {
                            holder.morder_status.setText("");
                        }
                        //改成convertView后 第二页中的按钮会失去字体和焦点
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent orderinfo = new Intent(mactivity, AboutUsActivity.class);
                                orderinfo.putExtra("id", mChildData.get(groupPosition).get(childPosition).getField_order_item_id());
                                orderinfo.putExtra("type", com.linhuiba.business.config.Config.SMALL_ORDER_INFO_INT);
                                orderinfo.putExtra("role_id", 3);
                                mactivity.startActivity(orderinfo);
                            }
                        });
                        if (childPosition == mChildData.get(groupPosition).size() - 1) {
                            if (mData != null && mData.size() != 0) {
                                if (groupPosition != mData.size()-1){
                                    holder.mhideview_layout.setVisibility(View.VISIBLE);
                                } else {
                                    holder.mhideview_layout.setVisibility(View.GONE);
                                }
                            }  else {
                                holder.mhideview_layout.setVisibility(View.GONE);
                            }
                            holder.morder_listitem_orderpricelayout.setVisibility(View.VISIBLE);
                            holder.morder_listitem_btnlayout.setVisibility(View.GONE);
                            if (mData.get(groupPosition).get("actual_fee") != null &&
                                    mData.get(groupPosition).get("actual_fee").toString().length() > 0) {
                                holder.morder_all_price.setText(Constants.getpricestring(mData.get(groupPosition).get("actual_fee").toString(), 0.01));
                            } else {
                                holder.morder_all_price.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                            }
                            if (mData.get(groupPosition).get("order_deposit") != null &&
                                    (double)mData.get(groupPosition).get("order_deposit") > 0) {
                                holder.mOrderItemDepositTV.setText("("+context.getResources().getString(R.string.order_listitem_deposit_tv_str) +
                                        Constants.getpricestring(mData.get(groupPosition).get("order_deposit").toString(),1) +")");
                            } else {
                                holder.mOrderItemDepositTV.setText("");
                            }

                            if (mData.get(groupPosition).get("status") != null) {
                                if (mData.get(groupPosition).get("status").equals("finished")) {
                                    holder.morder_type_btn_txt.setVisibility(View.VISIBLE);
                                    holder.mcancelbtn.setVisibility(View.GONE);
                                    holder.morder_listitem_btnlayout.setVisibility(View.VISIBLE);
                                    holder.morder_type_btn_txt.setTextColor(mactivity.getResources().getColor(R.color.fieldinfo_reserve_tv_bg));
                                    holder.morder_type_btn_txt.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.activity_orderlist_item_paybtn_bg));
                                    if (mData.get(groupPosition).get("status").equals("finished")) {
                                        holder.morder_listitem_btnlayout.setVisibility(View.VISIBLE);
                                        holder.morder_type_btn_txt.setText(mactivity.getResources().getString(R.string.order_listitem_finish_right_btn_str));
                                        holder.morder_type_btn_txt.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (LoginManager.isLogin()) {
                                                    Intent publidhreview = new Intent(mactivity, PublishReviewActivity.class);
                                                    publidhreview.putExtra("orderid", mChildData.get(groupPosition).get(childPosition).getField_order_item_id());
                                                    mactivity.startActivityForResult(publidhreview, 1);
                                                } else {
                                                    LoginManager.getInstance().clearLoginInfo();
                                                    LoginActivity.BaesActivityreloadLogin(mactivity);
                                                    mactivity.finish();
                                                }
                                            }
                                        });
                                        holder.mcancelbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //2017/10/24 加载web交易凭证
                                                Intent orderinfo = new Intent(mactivity,AboutUsActivity.class);
                                                if (groupPosition < mData.size() && childPosition < mChildData.get(groupPosition).size()) {
                                                    orderinfo.putExtra("id", mChildData.get(groupPosition).get(childPosition).getField_order_item_id());
                                                    orderinfo.putExtra("type", com.linhuiba.business.config.Config.DEAL_VOUCHER_INT);
                                                }
                                                mactivity.startActivity(orderinfo);
                                            }
                                        });
                                    }

                                }
                            } else {
                                //加上这个就显示不出来  原因还未弄清楚
//                                    holder.morder_listitem_btnlayout.setVisibility(View.GONE);
                            }
                        } else {
                            holder.mhideview_layout.setVisibility(View.GONE);
                            holder.morder_listitem_orderpricelayout.setVisibility(View.GONE);
                            holder.morder_listitem_btnlayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, int childPosition) {
        return true;
    }
    static class ViewHolder
    {
        public ImageView morderimg;
        public TextView mfield_name;
        public TextView mprice;
        public TextView mOrderInfoOriginalPriceTV;
        public TextView mstart;
        public TextView morder_status;
        public TextView morder_size;
        public TextView morderlist_custom_dimensiontxt;
        public TextView morder_type_btn_txt;
        public LinearLayout mgroup_hidelayout;
        public LinearLayout mhideview_layout;
        public LinearLayout mgroup_layout;
        public TextView morder_all_price;
        public TextView morder_all_price_textview;
        public TextView mOrderItemDepositTV;
        public TextView morder_num;
        public TextView mcount_week;
        public TextView mcount_frame;
        public TextView mcancelbtn;
        public LinearLayout mrefused_linearlayout;
        public TextView mrefused_textview;
        public LinearLayout mrefused_tmplinearlayout;
        public LinearLayout morder_listitem_btnlayout;
        public RelativeLayout morder_listitem_ordernumlayout;
        public ImageView morderlist_item_cancel_img;
        public RelativeLayout morder_listitem_orderpricelayout;
        private LinearLayout mOrderListItemResNameView;
    }

    static class GroupViewHolder
    {
        public ImageView morderimg;
        public TextView mfield_name;
        public TextView mprice;
        public TextView mstart;
        public ImageView morder_type;
        public TextView morder_status;
        public TextView morder_type_btn_txt;
        public LinearLayout mgroup_hidelayout;
    }
}
