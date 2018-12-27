package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.model.IndustriesModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/14.
 */
public class Industry_ChooseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<IndustriesModel> list;
    private Context context;
    private int type;
    private static HashMap<Integer, Boolean> isSelected_industry = new HashMap<Integer, Boolean>();
    public Industry_ChooseAdapter(Context context, ArrayList<IndustriesModel> list, int type) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.type = type;
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
            convertView = inflater.inflate(R.layout.activity_industry_choose_listviewitem, null);
            holder.mindustry_textview = (TextView)convertView.findViewById(R.id.industry_textview);
            holder.mindustry_layout = (LinearLayout)convertView.findViewById(R.id.industry_layout);
            holder.mindustry_view = (View)convertView.findViewById(R.id.industry_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mindustry_textview.setText(list.get(position).getDisplay_name());
        if (type == 0) {
            if (isSelected_industry.get(list.get(position).getId())) {
                holder.mindustry_layout.setBackgroundColor(context.getResources().getColor(R.color.field_view_bg));
                holder.mindustry_textview.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                holder.mindustry_textview.getPaint().setFakeBoldText(true);
            } else {
                holder.mindustry_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mindustry_textview.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
                holder.mindustry_textview.getPaint().setFakeBoldText(false);
            }
            holder.mindustry_view.setBackgroundColor(context.getResources().getColor(R.color.industry_view_bg));
        } else if (type == 1) {
            holder.mindustry_layout.setBackgroundColor(context.getResources().getColor(R.color.field_view_bg));
            holder.mindustry_view.setBackgroundColor(context.getResources().getColor(R.color.top_title_line_color));
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mindustry_textview;
        public LinearLayout mindustry_layout;
        public View mindustry_view;
    }
    public static HashMap<Integer, Boolean> getisSelected_industry() {
        return isSelected_industry;
    }

    public static void setisSelected_industry(HashMap<Integer, Boolean> isSelected) {
        Industry_ChooseAdapter.isSelected_industry= isSelected;
    }
    public static void clear_isSelected_industry() {
        if (isSelected_industry != null) {
            isSelected_industry.clear();
        }
    }
}
