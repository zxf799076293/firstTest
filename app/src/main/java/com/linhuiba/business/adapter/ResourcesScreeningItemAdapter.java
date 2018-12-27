package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.fragment.SearchAdvListFragment;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/10.
 */

public class ResourcesScreeningItemAdapter extends BaseAdapter{
    private ArrayList<HashMap<Object,Object>> itemdata = new ArrayList<HashMap<Object,Object>>();
    private Context mcontext;
    private SearchListFragment mactivity;
    private BaiduMapActivity baiduMapActivity;
    private LayoutInflater mInflater = null;
    private int type;//0 列表和地图筛选 1详情筛选 2详情筛选无结果时
    private SearchAdvListFragment mAdvActivity;
    private int mAdapterPosition = -1;
    private FieldInfoActivity fieldInfoActivity;
    private static HashMap<Object,Object> resourcescreeninglist = new HashMap<Object,Object>();
    public ResourcesScreeningItemAdapter(Context context, SearchListFragment activity, ArrayList<HashMap<Object, Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.type = invoicetype;
    }
    public ResourcesScreeningItemAdapter(Context context, BaiduMapActivity activity, ArrayList<HashMap<Object, Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.baiduMapActivity = activity;
        this.type = invoicetype;
    }
    public ResourcesScreeningItemAdapter(Context context, FieldInfoActivity activity,
                                         ArrayList<HashMap<Object, Object>> datas,
                                         int type,int adapterPosition) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.fieldInfoActivity = activity;
        this.type = type;
        this.mAdapterPosition = adapterPosition;
    }

    public ResourcesScreeningItemAdapter(Context context, SearchAdvListFragment activity,
                                         ArrayList<HashMap<Object, Object>> datas,
                                         int type) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mAdvActivity = activity;
        this.type = type;
    }

    @Override
    public int getCount() {
        return itemdata == null ? 0 : itemdata.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GridviewViewHolder holder = null;
        if (convertView == null) {
            holder = new GridviewViewHolder();
            convertView = mInflater.inflate(R.layout.field_activity_addfield_price_unit_choose_item, null);
            holder.mfieldsize = (TextView)convertView.findViewById(R.id.resourcesscreening_gridviewitem_fieldsize_txt);
            holder.mgridviewitem_searchlayout = (LinearLayout)convertView.findViewById(R.id.resourcesscreening_gridviewitem_searchlayout);
            holder.mScreenAllRL = (RelativeLayout)convertView.findViewById(R.id.screen_tv_all_rl);
            holder.mScreenImgV = (ImageView) convertView.findViewById(R.id.screen_imgv);
            convertView.setTag(holder);
        } else {
            holder = (GridviewViewHolder)convertView.getTag();
        }
        holder.mScreenImgV.setVisibility(View.GONE);
        DisplayMetrics metric = new DisplayMetrics();
        if (type == 0 ) {
            if (baiduMapActivity == null && mactivity != null) {
                mactivity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            } else if (baiduMapActivity != null && mactivity == null) {
                baiduMapActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            } else if (baiduMapActivity == null && mactivity == null) {
                if (mAdvActivity != null) {
                    mAdvActivity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
                }
            }
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;
            if (baiduMapActivity == null && mactivity != null &&
                    mactivity.GridviewNumColumns == 4) {
                    LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width -40)/4) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,32));
                    holder.mgridviewitem_searchlayout.setLayoutParams(paramgroups);
                    holder.mScreenAllRL.setLayoutParams(paramgroups);
            } else if (baiduMapActivity != null && mactivity == null &&
                    baiduMapActivity.GridviewNumColumns == 4) {
                    LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width -40)/4) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,32));
                    holder.mgridviewitem_searchlayout.setLayoutParams(paramgroups);
                    holder.mScreenAllRL.setLayoutParams(paramgroups);
            } else if (baiduMapActivity == null && mactivity == null && mAdvActivity != null &&
                    mAdvActivity.GridviewNumColumns == 4) {
                LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width -40)/4) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,32));
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroups);
                holder.mScreenAllRL.setLayoutParams(paramgroups);
            } else {
                LinearLayout.LayoutParams paramgroup= new LinearLayout.LayoutParams(((width -40)/3) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,32));
                holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
                holder.mScreenAllRL.setLayoutParams(paramgroup);
            }
        } else if (type == 1) {
            fieldInfoActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;
            LinearLayout.LayoutParams paramgroup = null;
            if (mAdapterPosition == 2) {
                paramgroup= new LinearLayout.LayoutParams(((width -40)/mAdapterPosition) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,33));
            } else if (mAdapterPosition == 3) {
                paramgroup= new LinearLayout.LayoutParams(((width -40)/mAdapterPosition) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,33));
            } else if (mAdapterPosition == 4) {
                paramgroup= new LinearLayout.LayoutParams(((width -40)/mAdapterPosition) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,33));
            } else if (mAdapterPosition == 0) {//计价单位
                paramgroup= new LinearLayout.LayoutParams(((width -40)/mAdapterPosition) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,33));
            }
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroup);
            holder.mScreenAllRL.setLayoutParams(paramgroup);
        } else if (type == 2) {
            fieldInfoActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;
            LinearLayout.LayoutParams paramgroup = null;
            paramgroup= new LinearLayout.LayoutParams(((width -40)/3) - Constants.Dp2Px(mcontext,12), Constants.Dp2Px(mcontext,36));
            holder.mScreenAllRL.setLayoutParams(paramgroup);
            LinearLayout.LayoutParams paramgroupLL = null;
            paramgroupLL= new LinearLayout.LayoutParams(((width -40)/3) - Constants.Dp2Px(mcontext,20), Constants.Dp2Px(mcontext,28));
            holder.mgridviewitem_searchlayout.setLayoutParams(paramgroupLL);
            if (itemdata.get(position).get("type") != null) {
                if (itemdata.get(position).get("type").toString().equals("lease_term_type_id")) {
                    holder.mfieldsize.setText(mcontext.getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_first) +
                            itemdata.get(position).get("name").toString() +
                            mcontext.getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_second));
                } else {
                    holder.mfieldsize.setText(itemdata.get(position).get("name").toString());
                }
                holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_fieldinfo_screen_checkbox_false_bg));
                holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.default_bluebg));
            }
            holder.mScreenImgV.setVisibility(View.VISIBLE);
        }
        if (type != 2) {
            if (itemdata.get(position).get("type").toString().equals("listviewtype")) {
                holder.mfieldsize.setVisibility(View.GONE);
            } else {
                holder.mfieldsize.setVisibility(View.VISIBLE);
                if (mAdapterPosition == 0) {
                    holder.mfieldsize.setText(mcontext.getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_first) +
                            itemdata.get(position).get(itemdata.get(position).get("type")).toString() +
                            mcontext.getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_second));
                } else {
                    holder.mfieldsize.setText(itemdata.get(position).get(itemdata.get(position).get("type")).toString());
                }
                holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.default_bluebg));
                if ((boolean)resourcescreeninglist.get(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()).toString())) {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.default_bluebg));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_addfield_price_unit_item_selected_bg));
                } else {
                    holder.mfieldsize.setTextColor(mcontext.getResources().getColor(R.color.top_title_center_txt_color));
                    holder.mfieldsize.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_bg));
                }

                holder.mfieldsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemdata.get(position).get("type").toString().equals("mfieldtype_list")) {
                            if (!(boolean)resourcescreeninglist.get(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()))) {
                                resourcescreeninglist.put(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()), true);
                            } else {
                                resourcescreeninglist.put(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()), false);
                            }
                        } else {
                            if (!(boolean)resourcescreeninglist.get(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()))) {
                                resourcescreeninglist.put(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()), true);
                                //详情中筛选点击后判断是否选中第一个规格
                                if (type == 1) {
                                    fieldInfoActivity.prices_list_OnClickListener(-1,
                                            itemdata.get(position).get(itemdata.get(position).get("type")).toString());
                                }
                            } else {
                                resourcescreeninglist.put(itemdata.get(position).get("type").toString()+itemdata.get(position).get(itemdata.get(position).get("type").toString()), false);
                                if (type == 1) {
                                    fieldInfoActivity.prices_list_OnClickListener(-1,
                                            itemdata.get(position).get(itemdata.get(position).get("type")).toString());
                                }
                            }
                            if (type == 0) {
                                if (baiduMapActivity == null && mactivity != null &&
                                        itemdata.get(position).get("type") != null &&
                                        itemdata.get(position).get("type").toString().equals("category")) {
                                    mactivity.getAttributesList();
                                } else if (baiduMapActivity != null && mactivity == null &&
                                        itemdata.get(position).get("type") != null &&
                                        itemdata.get(position).get("type").toString().equals("category")) {
                                    baiduMapActivity.getAttributesList();
                                }
                            }
                        }
                        setresourcescreeninglist(resourcescreeninglist);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }
    static class GridviewViewHolder
    {
        public TextView mfieldsize;
        public LinearLayout mgridviewitem_searchlayout;
        public ImageView mScreenImgV;
        public RelativeLayout mScreenAllRL;
    }
    public  static HashMap<Object,Object> getresourcescreeninglist() {
        return resourcescreeninglist;
    }

    public  static void setresourcescreeninglist(HashMap<Object,Object> isSelected) {
        ResourcesScreeningItemAdapter.resourcescreeninglist = isSelected;
    }
    public  static void clear_resourcescreeninglist() {
        if (resourcescreeninglist != null) {
            resourcescreeninglist.clear();
        }
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public void notifyDataSetChanged(boolean isUnfold,
                                     ArrayList<HashMap<Object,Object>> tempData) {
        if (tempData == null || 0 == tempData.size()) {
            return;
        }
        ArrayList<HashMap<Object,Object>> tempdata = new ArrayList<>();
        tempdata.addAll(tempData);
        itemdata.clear();
        // 如果是展开的，则加入全部data，反之则只显示3条
        if (isUnfold) {
            itemdata.addAll(tempdata);
        } else {
            if (baiduMapActivity == null && mactivity != null) {
                for (int i = 0; i < mactivity.GridviewNumColumns; i++ ) {
                    if (tempdata.size()> i) {
                        itemdata.add(tempdata.get(i));
                    }
                }
            } else if (baiduMapActivity != null && mactivity == null) {
                for (int i = 0; i < baiduMapActivity.GridviewNumColumns; i++ ) {
                    if (tempdata.size()> i) {
                        itemdata.add(tempdata.get(i));
                    }
                }
            } else if (baiduMapActivity == null && mactivity == null) {
                if (mAdvActivity != null) {
                    for (int i = 0; i < mAdvActivity.GridviewNumColumns; i++ ) {
                        if (tempdata.size()> i) {
                            itemdata.add(tempdata.get(i));
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
