package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_FieldPricesActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/3.
 */

public class Field_AddField_FieldPriceAdapter  extends BaseAdapter {
    private ArrayList<HashMap<String,String>> data = new ArrayList<>();
    private Context mcontext;
    private Field_AddField_FieldPricesActivity addfieldactivity;
    private LayoutInflater mInflater = null;
    private static HashMap<String, Boolean> isSelected_addfield_custom_size = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSelected_custom_dimension = new HashMap<String, Boolean>();

    private int type;
    public Field_AddField_FieldPriceAdapter(Context context, Field_AddField_FieldPricesActivity activity, ArrayList<HashMap<String, String>> datas, int type) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.addfieldactivity = activity;
        this.data = datas;
        this.type = type;
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
            convertView = mInflater.inflate(R.layout.field_activity_addfield_price_unit_choose_item, null);
            holder.mfieldsize = (TextView)convertView.findViewById(R.id.resourcesscreening_gridviewitem_fieldsize_txt);
            holder.mgridviewitem_searchlayout = (LinearLayout)convertView.findViewById(R.id.resourcesscreening_gridviewitem_searchlayout);
            holder.mAddfieldPriceItemRV = (RelativeLayout)convertView.findViewById(R.id.screen_tv_all_rl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        DisplayMetrics metric = new DisplayMetrics();
        addfieldactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        if (type == 0) {
            if (addfieldactivity.GridviewNumColumns == 4) {
                LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/4)-16, Constants.Dp2Px(mcontext,44));
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
                holder.mAddfieldPriceItemRV.setLayoutParams(paramgroup);
            } else {
                LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3)-16, Constants.Dp2Px(mcontext,44));
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
            }
            holder.mfieldsize.setText(data.get(position).get("denominatedunit").toString());
            if (isSelected_addfield_custom_size.get(data.get(position).get("fieldsize_str").toString())) {
                holder.mfieldsize.setTextColor(addfieldactivity.getResources().getColor(R.color.default_bluebg));
                holder.mfieldsize.setBackgroundDrawable(addfieldactivity.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
            } else {
                holder.mfieldsize.setTextColor(addfieldactivity.getResources().getColor(R.color.top_title_center_txt_color));
                holder.mfieldsize.setBackgroundDrawable(addfieldactivity.getResources().getDrawable(R.drawable.field_addfield_fieldprice_item_clicked_bg));
            }

            holder.mfieldsize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_addfield_custom_size.get(data.get(position).get("fieldsize_str").toString())) {
                        isSelected_addfield_custom_size.put(data.get(position).get("fieldsize_str").toString(), true);
                    } else {
                        isSelected_addfield_custom_size.put(data.get(position).get("fieldsize_str").toString(), false);
                    }
                    setIsSelected_addfield_custom_size(isSelected_addfield_custom_size);
                    notifyDataSetChanged();
                }
            });
        } else if (type == 1) {
            LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3)-48,Constants.Dp2Px(mcontext,44));
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
            holder.mAddfieldPriceItemRV.setLayoutParams(paramgroup);
            holder.mfieldsize.setText(data.get(position).get("custom_dimension").toString());
            if (isSelected_custom_dimension.get(data.get(position).get("custom_dimension").toString())) {
                holder.mfieldsize.setTextColor(addfieldactivity.getResources().getColor(R.color.default_bluebg));
                holder.mfieldsize.setBackgroundDrawable(addfieldactivity.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
            } else {
                holder.mfieldsize.setTextColor(addfieldactivity.getResources().getColor(R.color.top_title_center_txt_color));
                holder.mfieldsize.setBackgroundDrawable(addfieldactivity.getResources().getDrawable(R.drawable.field_addfield_fieldprice_item_clicked_bg));
            }

            holder.mfieldsize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_custom_dimension.get(data.get(position).get("custom_dimension").toString())) {
                        isSelected_custom_dimension.put(data.get(position).get("custom_dimension").toString(), true);
                    } else {
                        isSelected_custom_dimension.put(data.get(position).get("custom_dimension").toString(), false);
                    }
                    setIsSelected_custom_dimension(isSelected_custom_dimension);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mfieldsize;
        public LinearLayout mgridviewitem_searchlayout;
        public RelativeLayout mAddfieldPriceItemRV;

    }
    public static HashMap<String, Boolean> getIsSelected_addfield_custom_size() {
        return isSelected_addfield_custom_size;
    }

    public static void setIsSelected_addfield_custom_size(HashMap<String, Boolean> isSelected) {
        isSelected_addfield_custom_size = isSelected;
    }
    public static HashMap<String, Boolean> getIsSelected_custom_dimension() {
        return isSelected_custom_dimension;
    }

    public static void setIsSelected_custom_dimension(HashMap<String, Boolean> isSelected) {
        isSelected_custom_dimension = isSelected;
    }

}
