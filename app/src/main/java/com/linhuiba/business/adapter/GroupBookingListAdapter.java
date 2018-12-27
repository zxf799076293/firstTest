package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.NetworkUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/19.
 */

public class GroupBookingListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GroupBookingListModel> mDataList;
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    private int mHeight;
    //用于退出 Activity,避免 Countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters;

    public GroupBookingListAdapter(Context context, Activity activity, ArrayList<GroupBookingListModel> datalist) {
        if (context != null && activity != null) {
            this.mContext = context;
            this.mDataList = datalist;
            mLayoutInflater = LayoutInflater.from(context);
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            this.mWidth = metric.widthPixels;     // 屏幕宽度（像素）
            this.mHeight = metric.heightPixels;
            this.countDownCounters = new SparseArray<>();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.fragment_groupbooking_listitem, null);
            holder.mGroupBookingListImgAllRL = (RelativeLayout) convertView.findViewById(R.id.groupbooking_list_item_rl);
            holder.mGroupBookingListImg = (ImageView) convertView.findViewById(R.id.groupbooking_list_item_img);
            holder.mGroupBookingListResTypeTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_restype_tv);
            holder.mGroupBookingListDateTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_date_tv);
            holder.mGroupBookingListPeopleTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_people_tv);
            holder.mGroupBookingListResNameTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_resname_tv);
            holder.mGroupBookingListPriceTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_price_tv);
            holder.mGroupBookingListRealityPriceTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_reality_price_tv);
            holder.mGroupBookingListGroupOfPeopleTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_groupofpeople_tv);
            holder.mGroupBookingListResidueTimeTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_residue_time_tv);
            holder.mGroupBookingListParticipateTV = (TextView) convertView.findViewById(R.id.groupbooking_list_item_participate_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams mParamGroups = new LinearLayout.LayoutParams(mWidth,
                (mWidth * 490) / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH);
        holder.mGroupBookingListImgAllRL.setLayoutParams(mParamGroups);
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url") != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url").length() > 0) {
            Picasso.with(mContext).load(mDataList.get(position).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url").toString()
                    + Config.Linhui_Max_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(mWidth, (mWidth * 490) / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH).into(holder.mGroupBookingListImg);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(mWidth, (mWidth * 490) / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH).into(holder.mGroupBookingListImg);
        }
        if (mDataList.get(position).getActivity_start() != null && mDataList.get(position).getActivity_end() != null &&
                mDataList.get(position).getActivity_start().length() > 0 && mDataList.get(position).getActivity_end().length() > 0) {
            holder.mGroupBookingListDateTV.setText(mContext.getResources().getString(R.string.fieldinfo_activity_time_text) +
                    mDataList.get(position).getActivity_start() + "-" + mDataList.get(position).getActivity_end());
        } else {
            holder.mGroupBookingListDateTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getNumber_of_people() != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getNumber_of_people() > 0) {
            holder.mGroupBookingListPeopleTV.setText(mContext.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                    String.valueOf(mDataList.get(position).getSelling_resource().getPhysical_resource().getNumber_of_people()));
        } else {
            holder.mGroupBookingListPeopleTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getFirst_selling_resource_price() != null
                && mDataList.get(position).getSelling_resource().getFirst_selling_resource_price().get("price") != null) {
            holder.mGroupBookingListPriceTV.setText(com.linhuiba.business.connector.Constants.getPriceUnitStr(mContext, (mContext.getResources().getString(R.string.order_listitem_price_unit_text) +
                    com.linhuiba.business.connector.Constants.getpricestring(mDataList.get(position).getSelling_resource().getFirst_selling_resource_price().get("price").toString(), 0.01)), 14));
        } else {
            holder.mGroupBookingListPriceTV.setText("");
        }
        if (mDataList.get(position).getOrigin_price() != null) {
            holder.mGroupBookingListRealityPriceTV.setText(com.linhuiba.business.connector.Constants.getSpannableAllStr(mContext, (mContext.getResources().getString(R.string.order_listitem_price_unit_text) +
                            com.linhuiba.business.connector.Constants.getdoublepricestring(mDataList.get(position).getOrigin_price(), 1)), 14, 0, 1,
                    true, 0, com.linhuiba.business.connector.Constants.getdoublepricestring(mDataList.get(position).getOrigin_price(), 1).length() + 1, null, 0, 0, false, 0, 0));
        } else {
            holder.mGroupBookingListRealityPriceTV.setText("");
        }
        CountDownTimer countDownTimer = countDownCounters.get(holder.mGroupBookingListResidueTimeTV.hashCode());
        if (countDownTimer != null) {
            //将复用的倒计时清除
            countDownTimer.cancel();
        }
        if (mDataList.get(position).getNumber_of_group_purchase_now() != null &&
                mDataList.get(position).getMin() != null && mDataList.get(position).getTime_left() != null) {
            holder.mGroupBookingListGroupOfPeopleTV.setVisibility(View.VISIBLE);
            holder.mGroupBookingListResidueTimeTV.setVisibility(View.VISIBLE);
            holder.mGroupBookingListParticipateTV.setVisibility(View.VISIBLE);
            if (mDataList.get(position).getNumber_of_group_purchase_now() == mDataList.get(position).getMax() ||
                    mDataList.get(position).getNumber_of_group_purchase_now() > mDataList.get(position).getMax()) {
                holder.mGroupBookingListParticipateTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_full_strength_str));
                if (mDataList.get(position).getNumber_of_group_purchase_now() != null) {
                    holder.mGroupBookingListGroupOfPeopleTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_people_full_strength_str) +
                            String.valueOf(mDataList.get(position).getNumber_of_group_purchase_now()) + mContext.getResources().getString(R.string.fieldinfo_restaurant_unit_text));
                } else {
                    holder.mGroupBookingListGroupOfPeopleTV.setText("");
                }
                holder.mGroupBookingListResidueTimeTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_full_strength_str));
            } else {
                if (mDataList.get(position).getTime_left() > 0) {
                    holder.mGroupBookingListParticipateTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_participate_str));
                    if (mDataList.get(position).getNumber_of_group_purchase_now() != null) {
                        holder.mGroupBookingListGroupOfPeopleTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_people_timeend_str) +
                                String.valueOf(mDataList.get(position).getNumber_of_group_purchase_now()) + mContext.getResources().getString(R.string.groupbooding_list_item_peoplesecond_str));
                    } else {
                        holder.mGroupBookingListGroupOfPeopleTV.setText("");
                    }
                } else {
                    if (mDataList.get(position).getNumber_of_group_purchase_now() != null) {
                        holder.mGroupBookingListGroupOfPeopleTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_people_timeend_str) +
                                String.valueOf(mDataList.get(position).getNumber_of_group_purchase_now()) + mContext.getResources().getString(R.string.groupbooding_list_item_peoplesecond_str));
                    } else {
                        holder.mGroupBookingListGroupOfPeopleTV.setText("");
                    }
                    holder.mGroupBookingListResidueTimeTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_residue_tiem_end_str));
                    holder.mGroupBookingListParticipateTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_groupbooking_list_item_time_end_tv_bg));
                    holder.mGroupBookingListParticipateTV.setTextColor(mContext.getResources().getColor(R.color.groupbooking_list_item_time_end_tv_color));
                    holder.mGroupBookingListParticipateTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_residue_tiem_end_str));
                }
            }
            if (mDataList.get(position).getTime_left() > 0) {
                holder.mGroupBookingListParticipateTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.addfield_editorprice_dateedittextbg));
                holder.mGroupBookingListParticipateTV.setTextColor(mContext.getResources().getColor(R.color.white));
                // FIXME: 2018/12/12 倒计时
                long useTime = Long.parseLong(String.valueOf(mDataList.get(position).getTime_left() * 1000));
                holder.mGroupBookingListResidueTimeTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_residue_tiem_first_str) +
                        com.linhuiba.linhuifield.connector.Constants.getFormatTime(useTime,1));
            } else {
                notifyDataSetChanged();
                holder.mGroupBookingListResidueTimeTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_residue_tiem_end_str));
                holder.mGroupBookingListParticipateTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_groupbooking_list_item_time_end_tv_bg));
                holder.mGroupBookingListParticipateTV.setTextColor(mContext.getResources().getColor(R.color.groupbooking_list_item_time_end_tv_color));
                holder.mGroupBookingListParticipateTV.setText(mContext.getResources().getString(R.string.groupbooding_list_item_residue_tiem_end_str));
            }
        } else {
            holder.mGroupBookingListGroupOfPeopleTV.setVisibility(View.GONE);
            holder.mGroupBookingListResidueTimeTV.setVisibility(View.GONE);
            holder.mGroupBookingListParticipateTV.setVisibility(View.GONE);
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getName() != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getName().length() > 0) {
            holder.mGroupBookingListResNameTV.setText(mDataList.get(position).getSelling_resource().getPhysical_resource().getName());
        } else {
            holder.mGroupBookingListResNameTV.setText("");
        }
        if (mDataList.get(position).getSelling_resource().getPhysical_resource().getField_type() != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name() != null &&
                mDataList.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
            holder.mGroupBookingListResTypeTV.setText(mDataList.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name());
            holder.mGroupBookingListResTypeTV.setVisibility(View.VISIBLE);
        } else {
            holder.mGroupBookingListResTypeTV.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout mGroupBookingListImgAllRL;
        public ImageView mGroupBookingListImg;
        public TextView mGroupBookingListResTypeTV;
        public TextView mGroupBookingListDateTV;
        public TextView mGroupBookingListPeopleTV;
        public TextView mGroupBookingListResNameTV;
        public TextView mGroupBookingListPriceTV;
        public TextView mGroupBookingListRealityPriceTV;
        public TextView mGroupBookingListGroupOfPeopleTV;
        public TextView mGroupBookingListResidueTimeTV;
        public TextView mGroupBookingListParticipateTV;
    }
}
