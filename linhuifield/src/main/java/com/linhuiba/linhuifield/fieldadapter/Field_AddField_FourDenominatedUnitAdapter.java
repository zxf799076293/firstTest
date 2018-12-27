package com.linhuiba.linhuifield.fieldadapter;

import android.app.Activity;
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
 * Created by Administrator on 2016/7/15.
 */
public class Field_AddField_FourDenominatedUnitAdapter extends BaseAdapter {
    private ArrayList<HashMap<Object,Object>> data = new ArrayList<HashMap<Object,Object>>();
    private Context mcontext;
    private Activity addfieldactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<String, Boolean> isSelected_addfielddenominatedunittype = new HashMap<String, Boolean>();
    public Field_AddField_FourDenominatedUnitAdapter(Context context, Activity activity, ArrayList<HashMap<Object, Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.addfieldactivity = activity;
        this.data = datas;
        this.type = invoicetype;
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
       if (type == 1 || type == 2) {
           addfieldactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
           int width = metric.widthPixels;     // 屏幕宽度（像素）
           int height = metric.heightPixels;
            if (type == 2) {
                LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/7)-24, Constants.Dp2Px(mcontext,25));
                holder.mAddfieldPriceItemRV.setLayoutParams(paramgroup);
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
                holder.mfieldsize.setText(data.get(position).get("weekStr").toString());
                if (data.get(position).get("weekChoosed") != null &&
                        Integer.parseInt(data.get(position).get("weekChoosed").toString()) == 1) {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.default_bluebg));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_addfield_price_unit_item_selected_bg));
                } else {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_bg));
                }
            } else if (type == 1) {
                LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3)-24,Constants.Dp2Px(mcontext,33));
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
                holder.mAddfieldPriceItemRV.setLayoutParams(paramgroup);
                if (data.get(position).get("denominatedunit") != null) {
                    holder.mfieldsize.setText(data.get(position).get("denominatedunit").toString());
                }
                if (data.get(position).get("denominatedunit") != null &&
                        isSelected_addfielddenominatedunittype.get(data.get(position).get("denominatedunit").toString())) {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.default_bluebg));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_addfield_price_unit_item_selected_bg));
                } else {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_bg));
                }
                holder.mfieldsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSelected_addfielddenominatedunittype.get(data.get(position).get("denominatedunit").toString())) {
                            isSelected_addfielddenominatedunittype.put(data.get(position).get("denominatedunit").toString(), true);
                        } else {
                            isSelected_addfielddenominatedunittype.put(data.get(position).get("denominatedunit").toString(), false);
                        }
                        setIsSelected_addfielddenominatedunit(isSelected_addfielddenominatedunittype);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mfieldsize;
        public LinearLayout mgridviewitem_searchlayout;
        public RelativeLayout mAddfieldPriceItemRV;
    }
    public static HashMap<String, Boolean> getIsSelected_addfielddenominatedunit() {
        return isSelected_addfielddenominatedunittype;
    }

    public static void setIsSelected_addfielddenominatedunit(HashMap<String, Boolean> isSelected) {
        Field_AddField_FourDenominatedUnitAdapter.isSelected_addfielddenominatedunittype = isSelected;
    }

}
