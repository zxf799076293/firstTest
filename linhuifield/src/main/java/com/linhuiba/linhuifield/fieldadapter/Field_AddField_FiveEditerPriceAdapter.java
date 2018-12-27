package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_FieldPricesActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_FiveEditerPriceActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Field_AddField_FiveEditerPriceAdapter extends BaseAdapter {
    private ArrayList<Map<String, String>> editorpricedata = new ArrayList<Map<String, String>>();
    private Context mcontext;
    private Field_AddField_FiveEditerPriceActivity meditorpriceactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<Integer, Boolean> isSelected_addfield_fieldsize = new HashMap<Integer, Boolean>();

    public Field_AddField_FiveEditerPriceAdapter(Context context, Field_AddField_FiveEditerPriceActivity activity, ArrayList<Map<String, String>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.meditorpriceactivity = activity;
        this.editorpricedata = datas;
        this.type = invoicetype;
    }

    @Override
    public int getCount() {
        if (editorpricedata != null) {
            return editorpricedata.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return editorpricedata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.field_activity_field_addfield_fiveediterpricelistitem, null);

            holder.maddfield_denominated_special_fieldsize = (TextView) convertView.findViewById(R.id.addfield_denominated_special_fieldsize);
            holder.maddfield_denominatedunit_special_txt = (TextView) convertView.findViewById(R.id.addfield_denominatedunit_special_txt);
            holder.maddfield_denominated_special_datetxt = (TextView) convertView.findViewById(R.id.addfield_denominated_special_datetxt);
            holder.maddfield_special_type_txt = (TextView) convertView.findViewById(R.id.addfield_special_type_txt);
            holder.meditprice_special_itemedit = (EditText) convertView.findViewById(R.id.editprice_special_itemedit);
            holder.maddfield_five_listiten_special_view = (View)convertView.findViewById(R.id.addfield_five_listiten_special_view);
            holder.addFieldEditPriceSpecifaction = (TextView) convertView.findViewById(R.id.addfield_edit_price_specification_tv);
            holder.mAddfieldDepositLL = (LinearLayout) convertView.findViewById(R.id.addfield_deposit_item_ll);
            holder.mAddfieldDepositET = (EditText) convertView.findViewById(R.id.addfield_deposit_item_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == 2) {
            if (meditorpriceactivity.isChooseDeposit) {
                if (editorpricedata.get(position).get("deposit") != null &&
                        editorpricedata.get(position).get("deposit").toString().length() > 0 &&
                        Double.parseDouble(editorpricedata.get(position).get("deposit").toString()) > 0) {
                    holder.mAddfieldDepositET.setText(editorpricedata.get(position).get("deposit").toString());
                } else {
                    holder.mAddfieldDepositET.setText("");
                }
                holder.mAddfieldDepositLL.setVisibility(View.VISIBLE);
            } else {
                holder.mAddfieldDepositLL.setVisibility(View.GONE);
            }
            holder.maddfield_denominated_special_fieldsize.setText(editorpricedata.get(position).get("fieldsize").toString());
            if (editorpricedata.get(position).get("custom_dimension").toString().trim().length() > 0) {
                holder.maddfield_special_type_txt.setVisibility(View.VISIBLE);
                holder.maddfield_special_type_txt.setText("  " + editorpricedata.get(position).get("custom_dimension").toString() + "  ");
                holder.addFieldEditPriceSpecifaction.setText(mcontext.getResources().getString(R.string.addfield_five_specialprice_txt));
            } else {
                holder.maddfield_special_type_txt.setVisibility(View.GONE);
                holder.addFieldEditPriceSpecifaction.setText(mcontext.getResources().getString(R.string.addfield_five_addfieldprice_txt));
            }
            if (editorpricedata.get(position).get("price") != null && editorpricedata.get(position).get("price").length() > 0 &&
                    Double.parseDouble(editorpricedata.get(position).get("price").toString()) > 0) {
                holder.meditprice_special_itemedit.setText(editorpricedata.get(position).get("price").toString());
            }
            Constants.textchangelistener(holder.meditprice_special_itemedit);
            Constants.textchangelistener(holder.mAddfieldDepositET);
            if (editorpricedata.get(position).get("m_lease_term_type_id").toString().equals("1")) {
                holder.maddfield_denominatedunit_special_txt.setText("元/天");
                holder.maddfield_denominated_special_datetxt.setVisibility(View.VISIBLE);
                holder.maddfield_denominated_special_datetxt.setText("工作日");
            } else if (editorpricedata.get(position).get("m_lease_term_type_id").toString().equals("2")) {
                holder.maddfield_denominatedunit_special_txt.setText("元/天");
                holder.maddfield_denominated_special_datetxt.setVisibility(View.VISIBLE);
                holder.maddfield_denominated_special_datetxt.setText("周末");
            } else {
                holder.maddfield_denominatedunit_special_txt.setText(editorpricedata.get(position).get("denominatedunit").toString());
                holder.maddfield_denominated_special_datetxt.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView maddfield_denominated_special_fieldsize;
        public TextView maddfield_denominatedunit_special_txt;
        public TextView maddfield_denominated_special_datetxt;
        public TextView maddfield_special_type_txt;
        public EditText meditprice_special_itemedit;
        public View maddfield_five_listiten_special_view;
        public TextView addFieldEditPriceSpecifaction;
        public LinearLayout mAddfieldDepositLL;
        public EditText mAddfieldDepositET;
    }

    public static HashMap<Integer, Boolean> getIsSelected_addfield_fieldsize() {
        return isSelected_addfield_fieldsize;
    }

    public static void setIsSelected_addfield_fieldsize(HashMap<Integer, Boolean> isSelected) {
        Field_AddField_FiveEditerPriceAdapter.isSelected_addfield_fieldsize = isSelected;
    }

    public static void clear_isSelectedlist_addfield_fieldsize() {
        if (isSelected_addfield_fieldsize != null) {
            isSelected_addfield_fieldsize.clear();
        }
    }
}
