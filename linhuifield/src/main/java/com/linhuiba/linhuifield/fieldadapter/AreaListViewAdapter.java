package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/2.
 */
public class AreaListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> list;
    private int type;
    private Context context;
    public int clicked_position = -1;
    public AreaListViewAdapter(Context context, ArrayList<HashMap<String,String>> list, int type, int clickedposition) {
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
            convertView = inflater.inflate(R.layout.homefragment_arealist_item, null);
            holder.list_img = (ImageView)convertView.findViewById(R.id.area_choose_img);
            holder.list_name = (TextView)convertView.findViewById(R.id.list_text_area);
            holder.list_View = (View)convertView.findViewById(R.id.list_view);
            holder.list_view_last = (View)convertView.findViewById(R.id.list_view_last);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 1) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(holder.list_name.getLayoutParams());
            lp.setMargins(40, 25, 0, 0);
            holder.list_name.setLayoutParams(lp);
        }

        holder.list_name.setText(list.get(position).get("name"));
        if (clicked_position != -1 && position == clicked_position) {
            holder.list_img.setVisibility(View.VISIBLE);
        } else {
            holder.list_img.setVisibility(View.GONE);
        }
        if (position == list.size() - 1) {
            holder.list_View.setVisibility(View.GONE);
            holder.list_view_last.setVisibility(View.VISIBLE);
        } else {
            holder.list_View.setVisibility(View.VISIBLE);
            holder.list_view_last.setVisibility(View.GONE);
        }


        return convertView;
    }
    static class ViewHolder
    {
        public ImageView list_img;
        public TextView list_name;
        public View list_View;
        public View list_view_last;

    }
}
