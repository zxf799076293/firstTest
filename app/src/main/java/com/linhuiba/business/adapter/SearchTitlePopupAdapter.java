package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhuiba.business.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/8.
 */

public class SearchTitlePopupAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,Object>> mDataList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public SearchTitlePopupAdapter(Context context, ArrayList<HashMap<String,Object>> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDataList = list;
    }
    @Override
    public int getCount() {
        return mDataList.size();
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
            convertView = mLayoutInflater.inflate(R.layout.activity_search_title_popup_item,null);
            holder.mSearchPopUpImg = (ImageView) convertView.findViewById(R.id.search_titel_popup_item_img);
            holder.mSearchPopUpTv = (TextView) convertView.findViewById(R.id.search_titel_popup_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDataList.get(position).get("name") != null && mDataList.get(position).get("name").toString().length() > 0) {
            holder.mSearchPopUpTv.setText(mDataList.get(position).get("name").toString());
        }
        if (mDataList.get(position).get("resId") != null && mDataList.get(position).get("resId").toString().length() > 0) {
            holder.mSearchPopUpImg.setImageResource((int)mDataList.get(position).get("resId"));
        }
        return convertView;
    }
    static class ViewHolder {
        public TextView mSearchPopUpTv;
        public ImageView mSearchPopUpImg;
    }
}
