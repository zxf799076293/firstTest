package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.linhuiba.business.R;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ResourceMapSearchTypeAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Field_AddResourceCreateItemModel> list;
    private int type;
    private Context context;
    public int clicked_position = -1;
    public ResourceMapSearchTypeAdapter(Context context, ArrayList<Field_AddResourceCreateItemModel> list, int type, int clickedposition) {
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
            if (list.get(position).getName() != null) {
                if (list.get(position).getSearch_map_address_back_map().get("city") != null &&
                        list.get(position).getSearch_map_address_back_map().get("city").toString().length() > 0) {
                    holder.list_name.setText("[" + list.get(position).getSearch_map_address_back_map().get("city").toString() +"]"+
                            list.get(position).getName());
                } else {
                    holder.list_name.setText(list.get(position).getName());
                }
            }
        } else if (type == 0) {
            if (list.get(position).getName() != null) {
                holder.list_name.setText(list.get(position).getName());
            }
        }
        if (clicked_position != -1 && position == clicked_position) {
            holder.list_img.setVisibility(View.VISIBLE);
            holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
        } else {
            holder.list_name.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
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
