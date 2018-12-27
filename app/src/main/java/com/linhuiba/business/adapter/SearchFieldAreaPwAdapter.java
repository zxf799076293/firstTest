package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/17.
 */

public class SearchFieldAreaPwAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> list;
    private int type;
    private Context context;
    public int clicked_position = -1;
    public SearchFieldAreaPwAdapter(Context context, ArrayList<HashMap<String,String>> list, int type, int clickedposition) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
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
            convertView = inflater.inflate(R.layout.activity_search_field_area_pw_listitem, null);
            holder.list_name = (TextView)convertView.findViewById(R.id.list_text_area);
            holder.mMapSearchName = (TextView)convertView.findViewById(R.id.map_search_list_tv);
            holder.list_View = (View)convertView.findViewById(R.id.list_view);
            holder.list_view_last = (View)convertView.findViewById(R.id.list_view_last);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 1) {
            holder.mMapSearchName.setVisibility(View.VISIBLE);
            holder.list_name.setVisibility(View.GONE);
            holder.mMapSearchName.setText(list.get(position).get("name"));
            holder.mMapSearchName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.mMapSearchName.setVisibility(View.GONE);
            holder.list_name.setVisibility(View.VISIBLE);
            holder.list_name.setText(list.get(position).get("name"));
        }


        if (clicked_position != -1 && position == clicked_position) {
            holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
        } else {
            holder.list_name.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
        }
        if (position == list.size() - 1) {
            holder.list_View.setVisibility(View.GONE);
            if (type == 0) {
                holder.list_view_last.setVisibility(View.VISIBLE);
            } else {
                holder.list_view_last.setVisibility(View.GONE);
            }
        } else {
            holder.list_View.setVisibility(View.VISIBLE);
            holder.list_view_last.setVisibility(View.GONE);
        }


        return convertView;
    }
    static class ViewHolder {
        public TextView list_name;
        public TextView mMapSearchName;
        public View list_View;
        public View list_view_last;
    }
}
