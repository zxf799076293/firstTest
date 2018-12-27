package com.linhuiba.business.CalendarClass;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ChooseSpecificationsMygridviewAdapter extends BaseAdapter {
    private ArrayList<String> data = new ArrayList<>();
    private Context mcontext;
    private ChooseSpecificationsActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<String, Boolean> isSelected_fieldsize = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSelected_priceunit = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSelected_custom_dimension = new HashMap<String, Boolean>();
    public ChooseSpecificationsMygridviewAdapter (Context context,ChooseSpecificationsActivity activity,ArrayList<String> datas,int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
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
            convertView = mInflater.inflate(R.layout.activity_choosespecifications_mygridviewitem, null);
            holder.mfieldsize = (TextView)convertView.findViewById(R.id.gridviewitem_fieldsize_txt);
            holder.mgridviewitem_searchlayout = (LinearLayout)convertView.findViewById(R.id.gridviewitem_searchlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        DisplayMetrics metric = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        holder.mfieldsize.setText(data.get(position).toString());
        if (type == 0) {
            LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3)-16, 70);
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
            if (isSelected_fieldsize.get(data.get(position).toString())) {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.default_bluebg));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
            } else {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_fieldsize.get(data.get(position).toString())) {
                        for (int i = 0; i < data.size(); i++) {
                            if (i == position) {
                                isSelected_fieldsize.put(data.get(i).toString(), true);
                            } else {
                                isSelected_fieldsize.put(data.get(i).toString(), false);
                            }
                        }
                        setIsSelected_fieldsize(isSelected_fieldsize);
                        notifyDataSetChanged();
                        mactivity.getselectfieldsize(type,data.get(position));
                    }
                }
            });
        } else if (type == 1) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(158, 70);
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroups);
            if (isSelected_priceunit.get(data.get(position).toString())) {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.default_bluebg));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
            } else {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_priceunit.get(data.get(position).toString())) {
                        if (mactivity.resourcetype == 3) {
                            if (data.get(position).toString().trim().equals("天") &&
                                    Integer.parseInt(mactivity.term_types.get(data.get(position).trim()).toString()) == 7) {
                                for (int i = 0; i < data.size(); i++) {
                                    if (i == position) {
                                        isSelected_priceunit.put(data.get(i).toString(), true);
                                    } else {
                                        isSelected_priceunit.put(data.get(i).toString(), false);
                                    }
                                }
                                setIsSelected_priceunit(isSelected_priceunit);
                                notifyDataSetChanged();
                                mactivity.getselectfieldsize(type,data.get(position));
                            } else {
                                MessageUtils.showToast(mactivity.getResources().getString(R.string.fieldinfo_activity_time_text));
                            }
                        } else {
                            for (int i = 0; i < data.size(); i++) {
                                if (i == position) {
                                    isSelected_priceunit.put(data.get(i).toString(), true);
                                } else {
                                    isSelected_priceunit.put(data.get(i).toString(), false);
                                }
                            }
                            setIsSelected_priceunit(isSelected_priceunit);
                            notifyDataSetChanged();
                            mactivity.getselectfieldsize(type,data.get(position));
                        }
                    }
                }
            });
        } else if (type == 2) {
            LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3)-16, 70);
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
            if (isSelected_custom_dimension.get(data.get(position).toString())) {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.default_bluebg));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
            } else {
                holder.mfieldsize.setTextColor(mactivity.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mfieldsize.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_custom_dimension.get(data.get(position).toString())) {
                        for (int i = 0; i < data.size(); i++) {
                            if (i == position) {
                                isSelected_custom_dimension.put(data.get(i).toString(), true);
                            } else {
                                isSelected_custom_dimension.put(data.get(i).toString(), false);
                            }
                        }
                        setIsSelected_custom_dimension(isSelected_custom_dimension);
                        notifyDataSetChanged();
                        mactivity.getselectfieldsize(type,data.get(position));
                    } else {
                        isSelected_custom_dimension.put(data.get(position).toString(), false);
                        setIsSelected_custom_dimension(isSelected_custom_dimension);
                        notifyDataSetChanged();
                        mactivity.getselectfieldsize(-2,data.get(position));
                    }
                }
            });
        }

        return convertView;
    }
    static class ViewHolder
    {
        public TextView mfieldsize;
        public LinearLayout mgridviewitem_searchlayout;
    }
    public static HashMap<String, Boolean> getIsSelected_fieldsize() {
        return isSelected_fieldsize;
    }

    public static void setIsSelected_fieldsize(HashMap<String, Boolean> isSelected) {
        ChooseSpecificationsMygridviewAdapter.isSelected_fieldsize = isSelected;
    }
    public static void clear_isSelectedlist_fieldsize() {
        if (isSelected_fieldsize != null) {
            isSelected_fieldsize.clear();
        }
    }
    public static HashMap<String, Boolean> getIsSelected_priceunit() {
        return isSelected_priceunit;
    }

    public static void setIsSelected_priceunit(HashMap<String, Boolean> isSelected) {
        ChooseSpecificationsMygridviewAdapter.isSelected_priceunit = isSelected;
    }
    public static void clear_isSelectedlist_priceunit() {
        if (isSelected_priceunit != null) {
            isSelected_priceunit.clear();
        }
    }
    public static HashMap<String, Boolean> getIsSelected_custom_dimension() {
        return isSelected_custom_dimension;
    }

    public static void setIsSelected_custom_dimension(HashMap<String, Boolean> isSelected_custom_dimension) {
        ChooseSpecificationsMygridviewAdapter.isSelected_custom_dimension = isSelected_custom_dimension;
    }

}
