package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.Field_OrderRefuseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Field_OrderRefuseAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private Context mcontext;
    private Field_OrderRefuseActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<String, Boolean> isSelected_addfield_orderrefused = new HashMap<String, Boolean>();

    public Field_OrderRefuseAdapter(Context context, Field_OrderRefuseActivity activity, ArrayList<HashMap<String, String>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
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
            convertView = mInflater.inflate(R.layout.activity_field_ordersrefuseditem, null);
            holder.minvoice_checkbox = (CheckBox) convertView.findViewById(R.id.orderrefused_checkbox);
            holder.morderrefused_layout = (LinearLayout) convertView.findViewById(R.id.orderrefused_layout);
            holder.morderrefused_textview = (TextView) convertView.findViewById(R.id.orderrefused_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).get("reason") != null) {
            holder.morderrefused_textview.setText(data.get(position).get("reason").toString());
        }
        if (isSelected_addfield_orderrefused.get(data.get(position).get("reason").toString())) {
            holder.minvoice_checkbox.setChecked(true);
        } else {
            holder.minvoice_checkbox.setChecked(false);
        }
        holder.morderrefused_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected_addfield_orderrefused.get(data.get(position).get("reason").toString())) {
                    isSelected_addfield_orderrefused.put(data.get(position).get("reason").toString(), false);
                    setisSelected_addfield_orderrefused(isSelected_addfield_orderrefused);
                    notifyDataSetChanged();
                    mactivity.mrefused_edit.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        if (i == position) {
                            isSelected_addfield_orderrefused.put(data.get(i).get("reason"), true);
                        } else {
                            isSelected_addfield_orderrefused.put(data.get(i).get("reason"), false);
                        }
                    }
                    isSelected_addfield_orderrefused.put(data.get(position).get("reason").toString(), true);
                    setisSelected_addfield_orderrefused(isSelected_addfield_orderrefused);
                    notifyDataSetChanged();
                    if (data.get(position).get("reason").toString().equals("其他")) {
                        mactivity.mrefused_edit.setVisibility(View.VISIBLE);
                    } else {
                        mactivity.mrefused_edit.setVisibility(View.GONE);
                    }
                }
            }
        });
        holder.minvoice_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected_addfield_orderrefused.get(data.get(position).get("reason").toString())) {

                } else {
                    for (int i = 0; i < data.size(); i++) {
                        if (i == position) {
                            isSelected_addfield_orderrefused.put(data.get(i).get("reason"), true);
                        } else {
                            isSelected_addfield_orderrefused.put(data.get(i).get("reason"), false);
                        }
                    }
                    isSelected_addfield_orderrefused.put(data.get(position).get("reason").toString(), true);
                    setisSelected_addfield_orderrefused(isSelected_addfield_orderrefused);
                    notifyDataSetChanged();
                    if (data.get(position).get("reason").toString().equals("其他")) {
                        mactivity.mrefused_edit.setVisibility(View.VISIBLE);
                    } else {
                        mactivity.mrefused_edit.setVisibility(View.GONE);
                    }
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public CheckBox minvoice_checkbox;
        public LinearLayout morderrefused_layout;
        public TextView morderrefused_textview;
    }

    public static HashMap<String, Boolean> getisSelected_addfield_orderrefused() {
        return isSelected_addfield_orderrefused;
    }

    public static void setisSelected_addfield_orderrefused(HashMap<String, Boolean> isSelected) {
        Field_OrderRefuseAdapter.isSelected_addfield_orderrefused = isSelected;
    }

    public static void clear_isSelected_addfield_orderrefused() {
        if (isSelected_addfield_orderrefused != null) {
            isSelected_addfield_orderrefused.clear();
        }
    }
}
