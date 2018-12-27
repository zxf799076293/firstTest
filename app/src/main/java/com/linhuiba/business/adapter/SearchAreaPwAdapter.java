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
import com.linhuiba.business.activity.fingerprintrecognition.FingerprintCore;
import com.linhuiba.business.model.SearchAreaPwModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/15.
 */

public class SearchAreaPwAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<SearchAreaPwModel> list;
    private int type;
    private Context context;
    private static HashMap<Integer, Boolean> isFirstChoose = new HashMap<Integer, Boolean>();
    private static HashMap<Integer, Boolean> isSecondChoose = new HashMap<Integer, Boolean>();
    private static HashMap<Integer, Boolean> isThirdChoose = new HashMap<Integer, Boolean>();
    public SearchAreaPwAdapter(Context context, ArrayList<SearchAreaPwModel> list, int type) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
        this.context = context;
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_searchlist__area_pw_item, null);
            holder.list_img = (ImageView)convertView.findViewById(R.id.area_choose_img);
            holder.list_name = (TextView)convertView.findViewById(R.id.list_text_area);
            holder.list_View = (View)convertView.findViewById(R.id.list_view);
            holder.list_view_last = (View)convertView.findViewById(R.id.list_view_last);
            holder.mSearchFieldRL = (RelativeLayout)convertView.findViewById(R.id.search_choose_layout);
            holder.mSearchAreaRightView = (View) convertView.findViewById(R.id.search_area_right_view);
            holder.mSearchAreaBottomView = (View) convertView.findViewById(R.id.search_area_bottom_view);
            holder.mSearchAreaImav = (ImageView) convertView.findViewById(R.id.search_area_imgv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.list_name.setText(list.get(position).getName());
        if (type == 1 || type == 2 || type == 3 || type == 4) {
            holder.list_View.setVisibility(View.GONE);
            holder.list_view_last.setVisibility(View.GONE);
            holder.mSearchAreaBottomView.setVisibility(View.VISIBLE);
            if (type == 1) {
                holder.list_img.setVisibility(View.GONE);
                holder.mSearchAreaRightView.setVisibility(View.GONE);
                if (isFirstChoose.get(list.get(position).getId())) {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.mSearchFieldRL.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                    holder.mSearchFieldRL.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                if (list.get(position).isChoose()) {
                    holder.mSearchAreaImav.setVisibility(View.VISIBLE);
                } else {
                    holder.mSearchAreaImav.setVisibility(View.GONE);
                }
            } else if (type == 2) {
                holder.mSearchAreaRightView.setVisibility(View.VISIBLE);
                holder.mSearchAreaImav.setVisibility(View.GONE);
                if (isSecondChoose.get(list.get(position).getId())) {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.list_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chose_blue_selected_three_one));
                    holder.list_img.setVisibility(View.VISIBLE);
                } else {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                    holder.list_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chose_gary_nor_three_one));
                    holder.list_img.setVisibility(View.VISIBLE);
                }
            } else if (type == 4) {
                holder.mSearchAreaRightView.setVisibility(View.VISIBLE);
                holder.mSearchAreaImav.setVisibility(View.GONE);
                holder.list_img.setVisibility(View.GONE);
                if (isSecondChoose.get(list.get(position).getId())) {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                } else {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 3) {
                holder.list_name.setText(list.get(position).getStation_name());
                holder.mSearchAreaRightView.setVisibility(View.GONE);
                holder.mSearchAreaImav.setVisibility(View.GONE);
                if (isThirdChoose.get(list.get(position).getId())) {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.list_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chose_blue_selected_three_one));
                    holder.list_img.setVisibility(View.VISIBLE);
                } else {
                    holder.list_name.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                    holder.mSearchAreaImav.setVisibility(View.GONE);
                    holder.list_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chose_gary_nor_three_one));
                    holder.list_img.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }
    static class ViewHolder
    {
        public ImageView list_img;
        public TextView list_name;
        public View list_View;
        public View list_view_last;
        public RelativeLayout mSearchFieldRL;
        public View mSearchAreaRightView;
        public View mSearchAreaBottomView;
        public ImageView mSearchAreaImav;
    }
    public static HashMap<Integer, Boolean> getIsFirstChoose() {
        return isFirstChoose;
    }

    public static void setIsFirstChoose(HashMap<Integer, Boolean> isFirstChoose) {
        SearchAreaPwAdapter.isFirstChoose = isFirstChoose;
    }
    public static void clearIsFirstChoose() {
        if (isFirstChoose != null) {
            isFirstChoose.clear();
        }
    }
    public static HashMap<Integer, Boolean> getIsSecondChoose() {
        return isSecondChoose;
    }

    public static void setIsSecondChoose(HashMap<Integer, Boolean> isSecondChoose) {
        SearchAreaPwAdapter.isSecondChoose = isSecondChoose;
    }
    public static void clearIsSecondChoose() {
        if (isSecondChoose != null) {
            isSecondChoose.clear();
        }
    }
    public static HashMap<Integer, Boolean> getIsThirdChoose() {
        return isThirdChoose;
    }

    public static void setIsThirdChoose(HashMap<Integer, Boolean> isThirdChoose) {
        SearchAreaPwAdapter.isThirdChoose = isThirdChoose;
    }
    public static void clearIsThirdChoose() {
        if (isThirdChoose != null) {
            isThirdChoose.clear();
        }
    }
}

