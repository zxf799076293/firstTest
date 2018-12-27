package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.view.MyGridview;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/11.
 */

public class AdvertisingInfoPricelistAdapter extends BaseAdapter {
    private Context context;
    private AdvertisingInfoActivity fieldInfoActivity;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String,String>> data = new ArrayList<>();
    public AdvertisingInfoPricelistAdapter (Context context,AdvertisingInfoActivity activity,ArrayList<HashMap<String,String>> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fieldInfoActivity = activity;
    }
    @Override
    public int getCount() {
        if(data != null){
            return data.size();
        } else{
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
            convertView = mInflater.inflate(R.layout.activity_advertising_info_pricelistitem, null);
            holder.mfieldinfo_specifications_sizetextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_sizetextview);
            holder.mfieldinfo_specifications_tiemtextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_tiemtextview);
            holder.mfieldinfo_specifications_specialtextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_specialtextview);
            holder.mfieldinfo_specifications_pricetextview = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_pricetextview);
            holder.mpricelistview = (View) convertView.findViewById(R.id.mpricelistview);
            holder.mfieldinfo_specifications_btn = (TextView)convertView.findViewById(R.id.fieldinfo_specifications_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (data.get(position).get("size") != null &&
                data.get(position).get("size").length() > 0) {
            holder.mfieldinfo_specifications_sizetextview.setText(data.get(position).get("size"));
        }
        if (data.get(position).get("lease_term_type_name") != null &&
                data.get(position).get("lease_term_type_name").length() > 0) {
            holder.mfieldinfo_specifications_tiemtextview.setText(data.get(position).get("lease_term_type_name").toString());
        }
        if (data.get(position).get("custom_dimension") != null &&
                data.get(position).get("custom_dimension").length() > 0) {
            holder.mfieldinfo_specifications_specialtextview.setTextColor(fieldInfoActivity.getResources().getColor(R.color.top_title_center_txt_color));
            holder.mfieldinfo_specifications_specialtextview.setText(data.get(position).get("custom_dimension"));
        } else {
            holder.mfieldinfo_specifications_specialtextview.setText(fieldInfoActivity.getResources().getString(R.string.order_refuse_ordertxtnull));
            holder.mfieldinfo_specifications_specialtextview.setTextColor(fieldInfoActivity.getResources().getColor(R.color.register_edit_color));
        }
        if (data.get(position).get("price") != null &&
                data.get(position).get("price").length() > 0) {
            holder.mfieldinfo_specifications_pricetextview.setText(Constants.getpricestring(data.get(position).get("price"),0.01));
        }
        if (position == data.size() - 1) {
            holder.mpricelistview.setVisibility(View.GONE);
        } else {
            holder.mpricelistview.setVisibility(View.VISIBLE);
        }
        holder.mfieldinfo_specifications_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldInfoActivity.prices_list_OnClickListener(position);
            }
        });
        return convertView;
    }
    static class ViewHolder {
        TextView mfieldinfo_specifications_sizetextview;
        TextView mfieldinfo_specifications_tiemtextview;
        TextView mfieldinfo_specifications_specialtextview;
        TextView mfieldinfo_specifications_pricetextview;
        View mpricelistview;
        TextView mfieldinfo_specifications_btn;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(boolean isUnfold,
                                     ArrayList<HashMap<String,String>> tempData) {
        if (tempData == null || 0 == tempData.size()) {
            return;
        }
        ArrayList<HashMap<String,String>> tempdata = new ArrayList<>();
        tempdata.addAll(tempData);
        data.clear();
        // 如果是展开的，则加入全部data，反之则只显示3条
        if (isUnfold) {
            data.addAll(tempdata);
        } else {
            for (int i = 0; i < 6; i++ ) {
                if (tempdata.size()> i) {
                    data.add(tempdata.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
