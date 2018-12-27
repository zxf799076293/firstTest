package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_FieldPricesActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/8.
 */

public class Field_AddField_FourSpecialAdapter extends BaseAdapter{
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private Context mcontext;
    private Field_AddField_FieldPricesActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<Integer, Integer> isSelected_addfield_fieldsize = new HashMap<Integer, Integer>();

    public Field_AddField_FourSpecialAdapter(Context context, Field_AddField_FieldPricesActivity activity, ArrayList<HashMap<String, String>> datas, int invoicetype) {
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
            convertView = mInflater.inflate(R.layout.activity_addfield_four_special_lisviewitem, null);
            holder.maddfield_denominated_fieldsizelayout = (LinearLayout) convertView.findViewById(R.id.addfield_denominated_special_fieldsizelayout);
            holder.mdenominated_checkbox = (CheckBox) convertView.findViewById(R.id.addfield_denominated_special_checkbox);
            holder.maddfield_denominated_edittext = (EditText) convertView.findViewById(R.id.addfield_denominated_special_edittext);
            holder.maddfield_denominated_view = (View) convertView.findViewById(R.id.addfield_denominated_special_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.maddfield_denominated_fieldsizelayout.setVisibility(View.VISIBLE);
        holder.maddfield_denominated_view.setVisibility(View.VISIBLE);
        if (mactivity.editor_special_txt) {
            if (data.get(position).get("edittext_special") != null) {
                holder.maddfield_denominated_edittext.setText(data.get(position).get("edittext_special").toString());
            }
        }
        if (isSelected_addfield_fieldsize.get(position) == 1) {
            holder.maddfield_denominated_fieldsizelayout.setVisibility(View.VISIBLE);
            holder.maddfield_denominated_view.setVisibility(View.VISIBLE);
            holder.mdenominated_checkbox.setChecked(true);
        } else if (isSelected_addfield_fieldsize.get(position) == 2) {
            if (position == 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(28, -(110), 28, -getlistheight()+110+24);//4个参数按顺序分别是左上右下
                mactivity.maddfield_special_fieldsizelistview.setLayoutParams(layoutParams);
                holder.maddfield_denominated_edittext.setText("");
                holder.mdenominated_checkbox.setVisibility(View.GONE);
                holder.maddfield_denominated_edittext.setBackgroundColor(mactivity.getResources().getColor(R.color.white));
                holder.maddfield_denominated_fieldsizelayout.setVisibility(View.INVISIBLE);
                holder.maddfield_denominated_view.setVisibility(View.INVISIBLE);
            } else {
                holder.maddfield_denominated_edittext.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                holder.mdenominated_checkbox.setVisibility(View.VISIBLE);
                holder.maddfield_denominated_fieldsizelayout.setVisibility(View.GONE);
                holder.maddfield_denominated_view.setVisibility(View.GONE);
            }
        } else if (isSelected_addfield_fieldsize.get(position) == 3) {
            holder.maddfield_denominated_edittext.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
            holder.mdenominated_checkbox.setVisibility(View.VISIBLE);
            holder.maddfield_denominated_fieldsizelayout.setVisibility(View.VISIBLE);
            holder.maddfield_denominated_view.setVisibility(View.VISIBLE);
            holder.maddfield_denominated_edittext.setText("");
            holder.mdenominated_checkbox.setChecked(false);
        }
        checkboxonclick(holder.maddfield_denominated_fieldsizelayout,holder.maddfield_denominated_view,
                holder.mdenominated_checkbox,position);
        setListener(holder.maddfield_denominated_edittext, holder.mdenominated_checkbox, position);
        return convertView;
    }

    static class ViewHolder {
        public LinearLayout maddfield_denominated_fieldsizelayout;
        public CheckBox mdenominated_checkbox;
        public EditText maddfield_denominated_edittext;
        public View maddfield_denominated_view;
    }

    public static HashMap<Integer, Integer> getIsSelected_addfield_fieldsize() {
        return isSelected_addfield_fieldsize;
    }

    public static void setIsSelected_addfield_fieldsize(HashMap<Integer, Integer> isSelected) {
        Field_AddField_FourSpecialAdapter.isSelected_addfield_fieldsize = isSelected;
    }

    public static void clear_isSelectedlist_addfield_fieldsize() {
        if (isSelected_addfield_fieldsize != null) {
            isSelected_addfield_fieldsize.clear();
        }
    }
    public void setListener(final EditText edit,final CheckBox chenbox,final int position){
        edit.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSelected_addfield_fieldsize.get(position) == 3) {
                    if (edit.getText().toString().trim().length() > 0) {
                        isSelected_addfield_fieldsize.put(position,1);
                        chenbox.setChecked(true);
                        isSelected_addfield_fieldsize.put(position + 1, 3);
                        mactivity.add_speciallistitem(-1);
                    }

                }
            }

        });
    }
    private void checkboxonclick(final View layout, final View view,View checkbox, final int position) {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected_addfield_fieldsize.get(position) == 3) {
                    isSelected_addfield_fieldsize.put(position, 1);
                    isSelected_addfield_fieldsize.put(position + 1, 3);
                    mactivity.add_speciallistitem(-1);
                } else if (isSelected_addfield_fieldsize.get(position) == 1) {
                    isSelected_addfield_fieldsize.put(position, 2);
                    setIsSelected_addfield_fieldsize(isSelected_addfield_fieldsize);
                    notifyDataSetChanged();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(28, 20, 28, -(getlistheight()));//4个参数按顺序分别是左上右下
                    mactivity.maddfield_special_fieldsizelistview.setLayoutParams(layoutParams);
                } else {

                }

            }
        });

    }
    private int  getlistheight() {
        int height = 0;
        int goneitemnum = isSelected_addfield_fieldsize.size();
        for (int i = 0; i < isSelected_addfield_fieldsize.size(); i++) {
            if (isSelected_addfield_fieldsize.get(i) == 2) {
                goneitemnum--;
                height++;
            }
        }
        height = 110*height;
        return height;
    }

}
