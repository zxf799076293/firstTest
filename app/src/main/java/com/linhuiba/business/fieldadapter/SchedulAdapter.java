package com.linhuiba.business.fieldadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linhuiba.business.CalendarClass.ScheduleCalendarActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ScheduleCalendarlistModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/4.
 */
public class SchedulAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ResourceSearchItemModel> mFeildList;
    private LayoutInflater mInflater = null;
    private int type;
    private ArrayList<ScheduleCalendarlistModel> data = new ArrayList<ScheduleCalendarlistModel>();
    private ScheduleCalendarActivity mactivity;
    private String weather;
    public SchedulAdapter(Context context ,ArrayList<ScheduleCalendarlistModel> list , int type,ScheduleCalendarActivity mactivity,String weather) {
        this.mInflater = LayoutInflater.from(context);
        this.data =list;
        this.type = type;
        this.mactivity = mactivity;
        this.context = context;
        this.weather = weather;
    }
    @Override
    public int getCount() {
        if(data != null){
            return data.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            convertView = mInflater.inflate(R.layout.activity_calendar_listitem, null);
            holder.scheduletype = (TextView)convertView.findViewById(R.id.schedule_type);
            holder.schedulename = (TextView)convertView.findViewById(R.id.schedule_name);
            holder.scheduleaddress = (TextView)convertView.findViewById(R.id.schedule_address);
            holder.scheduleweather = (TextView)convertView.findViewById(R.id.schedule_weather);
            holder.mtell_property = (TextView)convertView.findViewById(R.id.tell_property);
            holder.mstatus_textview = (TextView)convertView.findViewById(R.id.status_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (data.get(position).getOrder_type_name() != null) {
            if (data.get(position).getOrder_type_name().equals(context.getResources().getString(R.string.leftimg_text))) {
                holder.scheduletype.setVisibility(View.VISIBLE);
                holder.scheduletype.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.textview_bg));
            } else if (data.get(position).getOrder_type_name().equals(context.getResources().getString(R.string.righttimg_text))) {
                holder.scheduletype.setVisibility(View.VISIBLE);
                holder.scheduletype.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.calendar_date_advertisingbg));
            } else if (data.get(position).getOrder_type_name().equals(context.getResources().getString(R.string.calendar_activity_txt)) ||
                    data.get(position).getOrder_type_name().equals(context.getResources().getString(R.string.home_group_booking_str))) {
                holder.scheduletype.setVisibility(View.VISIBLE);
                holder.scheduletype.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.calendar_date_activitybg));
            } else {
                holder.scheduletype.setVisibility(View.GONE);
            }
        } else {
            holder.scheduletype.setVisibility(View.GONE);
        }
        if (data.get(position).getStatus() != null) {
            if (data.get(position).getStatus().length()>0) {
                if (data.get(position).getStatus().equals(context.getResources().getString(R.string.myselfinfo_waiting))) {
                    holder.mtell_property.setVisibility(View.VISIBLE);
                } else {
                    holder.mtell_property.setVisibility(View.GONE);
                }
                holder.mstatus_textview.setVisibility(View.VISIBLE);
                holder.mstatus_textview.setText(data.get(position).getStatus());
            } else {
                holder.mstatus_textview.setVisibility(View.GONE);
                holder.mtell_property.setVisibility(View.GONE);
            }
        } else {
            holder.mtell_property.setVisibility(View.GONE);
        }
        holder.schedulename.setText(data.get(position).getResource_name());
        holder.scheduleweather.setText(context.getResources().getString(
                com.linhuiba.linhuifield.connector.Constants.getWeatherStr(data.get(position).getWeather())
        ));
        holder.mtell_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getMobile() != null &&
                        data.get(position).getOrder_item_id() != null &&
                        data.get(position).getMobile().length() > 0 &&
                        data.get(position).getOrder_item_id().length() > 0) {
                    mactivity.setVirtualNumber(-1, Integer.parseInt(data.get(position).getOrder_item_id()),
                            data.get(position).getMobile());

                }
            }
        });
        holder.scheduleaddress.setText(data.get(position).getAddress());
        return convertView;
    }
    static class ViewHolder
    {
        public TextView scheduletype;
        public TextView schedulename;
        public TextView scheduleaddress;
        public TextView scheduleweather;
        public TextView mtell_property;
        public TextView mstatus_textview;
    }
}
