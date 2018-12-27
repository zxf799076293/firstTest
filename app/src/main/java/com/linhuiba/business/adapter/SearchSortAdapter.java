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
 * Created by Administrator on 2017/9/9.
 */

public class SearchSortAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> list;
    private Context context;
    public int clicked_position = -1;
    public SearchSortAdapter(Context context, ArrayList<HashMap<String,String>> list, int clickedposition) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.clicked_position = clickedposition;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflater.inflate(R.layout.activity_search_sort_lv_item, null);
            holder.mSearchSortItemImg = (ImageView)convertView.findViewById(R.id.search_sort_item_img);
            holder.mSearchSortItemTV = (TextView)convertView.findViewById(R.id.search_sort_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mSearchSortItemTV.setText(list.get(position).get("name"));
        if (clicked_position != -1 && position == clicked_position) {
            holder.mSearchSortItemImg.setVisibility(View.GONE);
            holder.mSearchSortItemTV.setTextColor(context.getResources().getColor(R.color.default_bluebg));
        } else {
            holder.mSearchSortItemTV.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
            holder.mSearchSortItemImg.setVisibility(View.GONE);
        }
        return convertView;
    }
    static class ViewHolder
    {
        public ImageView mSearchSortItemImg;
        public TextView mSearchSortItemTV;
    }
}
