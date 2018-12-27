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
import com.linhuiba.business.model.CaseThemeModel;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/9.
 */

public class ActivityCaseSearchAdapter extends BaseAdapter {
    private ArrayList<CaseThemeModel> list;
    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mInflater = null;
    private LinearLayout.LayoutParams mParamgroups;
    private HashMap<Integer,Boolean> checkedList = new HashMap<Integer,Boolean>();
    public ActivityCaseSearchAdapter(ArrayList<CaseThemeModel> list,
                                     Context mContext, Activity mActivity) {
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
        this.mContext = mContext;
        this.mActivity = mActivity;
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        mParamgroups = new LinearLayout.LayoutParams(((width -40)/4) - Constants.Dp2Px(mContext,12), Constants.Dp2Px(mContext,32));
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
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
        holder.mgridviewitem_searchlayout.setLayoutParams(mParamgroups);
        holder.mScreenAllRL.setLayoutParams(mParamgroups);
        holder.mScreenImgV.setVisibility(View.GONE);
        holder.mfieldsize.setText(list.get(position).getDisplay_name());
        if ((boolean)checkedList.get(list.get(position).getId())) {
            holder.mfieldsize.setTextColor(mContext.getResources().getColor(R.color.default_bluebg));
            holder.mfieldsize.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_addfield_price_unit_item_selected_bg));
        } else {
            holder.mfieldsize.setTextColor(mContext.getResources().getColor(R.color.top_title_center_txt_color));
            holder.mfieldsize.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_bg));
        }
        holder.mfieldsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(boolean)checkedList.get(list.get(position).getId())) {
                    checkedList.put(list.get(position).getId(), true);
                } else {
                    checkedList.put(list.get(position).getId(), false);
                }
                setCheckedList(checkedList);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    static class GridviewViewHolder {
        public TextView mfieldsize;
        public LinearLayout mgridviewitem_searchlayout;
        public ImageView mScreenImgV;
        public RelativeLayout mScreenAllRL;
    }

    public HashMap<Integer, Boolean> getCheckedList() {
        return checkedList;
    }

    public void setCheckedList(HashMap<Integer, Boolean> checkedList) {
        this.checkedList = checkedList;
    }
}
