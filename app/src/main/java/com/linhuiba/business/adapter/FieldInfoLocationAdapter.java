package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoLocationActivity;
import com.linhuiba.business.model.ResourceInfoCommunityInfoResourcesModel;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/16.
 */

public class FieldInfoLocationAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private FieldInfoLocationActivity fieldInfoLocationActivity;
    private ArrayList<ResourceInfoCommunityInfoResourcesModel> data = new ArrayList<>();
    private static HashMap<Integer, Boolean> isLocationSelected = new HashMap<Integer, Boolean>();
    public FieldInfoLocationAdapter(Context context, FieldInfoLocationActivity activity, ArrayList<ResourceInfoCommunityInfoResourcesModel> data) {
        this.data = data;
        this.fieldInfoLocationActivity = activity;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
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
            convertView = layoutInflater.inflate(R.layout.activity_fieldinfolocation_list_item,null);
            holder.mfieldinfo_location_title_text = (TextView)convertView.findViewById(R.id.fieldinfo_location_title_text);
            holder.mfieldinfo_location_price_text = (TextView)convertView.findViewById(R.id.fieldinfo_location_price_text);
            holder.mfieldinfo_location_number_of_people_text = (TextView)convertView.findViewById(R.id.fieldinfo_location_number_of_people_text);
            holder.mfieldinfo_location_checkbok = (CheckBox)convertView.findViewById(R.id.fieldinfo_location_checkbox);
            holder.mfieldinfo_location_layout = (LinearLayout)convertView.findViewById(R.id.fieldinfo_location_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (isLocationSelected.get(data.get(position).getId())) {
            holder.mfieldinfo_location_checkbok.setChecked(true);
        } else {
            holder.mfieldinfo_location_checkbok.setChecked(false);
        }
        if (data.get(position).getName() != null && data.get(position).getDoLocation().length() > 0) {
            holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation());
        }
        if (data.get(position).getDoLocation() != null &&
                data.get(position).getDoLocation().length() > 0) {
            holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation());
            if (data.get(position).getIndoor() > -1) {
                if (data.get(position).getIndoor() == 0) {
                    holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation() +
                            fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_location_unindoor_text));
                    if (data.get(position).getIs_agent() > -1) {
                        if (data.get(position).getIs_agent() == 1) {
                            holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation() +
                                    fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_location_unindoor_text)+
                                    fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_is_agent_text));
                        }
                    }


                } else if (data.get(position).getIndoor() == 1) {
                    holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation() +
                            fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_location_indoor_text));
                    if (data.get(position).getIs_agent() > -1) {
                        if (data.get(position).getIs_agent() == 1) {
                            holder.mfieldinfo_location_title_text.setText(data.get(position).getDoLocation() +
                                    fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_location_indoor_text) +
                                    fieldInfoLocationActivity.getResources().getString(R.string.fieldinfo_is_agent_text));
                        }
                    }
                }
            }
        }

        if (data.get(position).getMin_price() == data.get(position).getMax_price()) {
            holder.mfieldinfo_location_price_text.setText(Constants.getPriceUnitStr(context,(context.getResources().getString(com.linhuiba.linhuifield.R.string.order_listitem_price_unit_text)
                    +Constants.getpricestring(String.valueOf(data.get(position).getMin_price()),0.01)),10));
        } else {
            holder.mfieldinfo_location_price_text.setText(Constants.getPriceUnitStr(context,(context.getResources().getString(com.linhuiba.linhuifield.R.string.order_listitem_price_unit_text)
                    +Constants.getpricestring(String.valueOf(data.get(position).getMin_price()),0.01)),10) +"~" +
                    Constants.getPriceUnitStr(context,(context.getResources().getString(com.linhuiba.linhuifield.R.string.order_listitem_price_unit_text)
                            +Constants.getpricestring(String.valueOf(data.get(position).getMax_price()),0.01)),10));
        }
        if (data.get(position).getNumber_of_people() > 0) {
            holder.mfieldinfo_location_number_of_people_text.setText(String.valueOf(data.get(position).getNumber_of_people()));
        } else {
            holder.mfieldinfo_location_number_of_people_text.setText(context.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }

        holder.mfieldinfo_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationSelected.get(data.get(position).getId())) {
                    isLocationSelected.put(data.get(position).getId(),false);
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        if (i == position) {
                            isLocationSelected.put(data.get(i).getId(),true);
                        } else {
                            isLocationSelected.put(data.get(i).getId(),false);
                        }
                    }
                }
                setIsLocationSelected(isLocationSelected);
                notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("id",data.get(position).getId());
                fieldInfoLocationActivity.setResult(4,intent);
                fieldInfoLocationActivity.finish();
            }
        });

        return convertView;
    }
    static class ViewHolder {
        TextView mfieldinfo_location_title_text;
        TextView mfieldinfo_location_price_text;
        TextView mfieldinfo_location_number_of_people_text;
        CheckBox mfieldinfo_location_checkbok;
        LinearLayout mfieldinfo_location_layout;
    }
    public static HashMap<Integer, Boolean> getIsLocationSelected() {
        return isLocationSelected;
    }

    public static void setIsLocationSelected(HashMap<Integer, Boolean> isLocationSelected) {
        FieldInfoLocationAdapter.isLocationSelected = isLocationSelected;
    }
    public static void clear_IsLocationSelected() {
        if (isLocationSelected != null) {
            isLocationSelected.clear();
        }
    }
}
