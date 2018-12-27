package com.linhuiba.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/2.
 */
public class AreaListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> list;
    private int type;
    private Context context;
    public int clicked_position = -1;
    private static HashMap<String, Boolean> isFirstChoose = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSecondChoose = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isThirdChoose = new HashMap<String, Boolean>();
    public AreaListViewAdapter(Context context, ArrayList<HashMap<String,String>> list, int type, int clickedposition) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
        this.context = context;
        this.clicked_position = clickedposition;
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
            convertView = inflater.inflate(R.layout.homefragment_arealist_item, null);
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
        holder.list_name.setText(list.get(position).get("name"));
        if (type == 0) {
            if (position == list.size() - 1) {
                holder.list_View.setVisibility(View.GONE);
                holder.list_view_last.setVisibility(View.VISIBLE);
            } else {
                holder.list_View.setVisibility(View.VISIBLE);
                holder.list_view_last.setVisibility(View.GONE);
            }
            if (clicked_position != -1 && position == clicked_position) {
                holder.list_img.setVisibility(View.GONE);
                holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
            } else {
                holder.list_name.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
                holder.list_img.setVisibility(View.GONE);
            }
        } else if (type == 1 || type == 2 || type == 3) {
            holder.list_View.setVisibility(View.GONE);
            holder.list_view_last.setVisibility(View.GONE);
            holder.mSearchAreaBottomView.setVisibility(View.VISIBLE);
            if (type == 2) {
                holder.mSearchAreaRightView.setVisibility(View.VISIBLE);
            } else {
                holder.mSearchAreaRightView.setVisibility(View.GONE);
            }
            if (clicked_position != -1 && position == clicked_position) {
                holder.list_name.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                holder.list_img.setVisibility(View.GONE);
                if (type == 1) {
                    holder.mSearchAreaImav.setVisibility(View.VISIBLE);
                } else {
                    holder.mSearchAreaImav.setVisibility(View.GONE);
                }
            } else {
                holder.list_name.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
                holder.list_img.setVisibility(View.GONE);
                holder.mSearchAreaImav.setVisibility(View.GONE);
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
    public static HashMap<String, Boolean> getIsFirstChoose() {
        return isFirstChoose;
    }

    public static void setIsFirstChoose(HashMap<String, Boolean> isFirstChoose) {
        AreaListViewAdapter.isFirstChoose = isFirstChoose;
    }
    public static void clearIsFirstChoose() {
        if (isFirstChoose != null) {
            isFirstChoose.clear();
        }
    }
    public static HashMap<String, Boolean> getIsSecondChoose() {
        return isSecondChoose;
    }

    public static void setIsSecondChoose(HashMap<String, Boolean> isSecondChoose) {
        AreaListViewAdapter.isSecondChoose = isSecondChoose;
    }
    public static void clearIsSecondChoose() {
        if (isSecondChoose != null) {
            isSecondChoose.clear();
        }
    }
    public static HashMap<String, Boolean> getIsThirdChoose() {
        return isThirdChoose;
    }

    public static void setIsThirdChoose(HashMap<String, Boolean> isThirdChoose) {
        AreaListViewAdapter.isThirdChoose = isThirdChoose;
    }
    public static void clearIsThirdChoose() {
        if (isThirdChoose != null) {
            isThirdChoose.clear();
        }
    }
}
