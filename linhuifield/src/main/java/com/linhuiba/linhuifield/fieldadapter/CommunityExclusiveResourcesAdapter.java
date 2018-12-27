package com.linhuiba.linhuifield.fieldadapter;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.CommunityResourcesActivity;
import com.linhuiba.linhuifield.fieldmodel.CommunityResourcesItemsModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CommunityExclusiveResourcesAdapter extends BaseAdapter {
    private ArrayList<CommunityResourcesItemsModel> data = new ArrayList<CommunityResourcesItemsModel>();
    private Context mcontext;
    private CommunityResourcesActivity mactivity;
    private LayoutInflater mInflater = null;
    private int width;
    private int height;
    private int type;
    public CommunityExclusiveResourcesAdapter(Context context, CommunityResourcesActivity activity, ArrayList<CommunityResourcesItemsModel> datas,
                                              int type) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = type;
        DisplayMetrics metric = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
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
            convertView = mInflater.inflate(R.layout.field_activity_themsinfo_listitem, null);
            holder.themes_gridview_img = (ImageView)convertView.findViewById(R.id.themes_gridview_img);
            holder.themes_gridview_txt_name = (TextView)convertView.findViewById(R.id.themes_txt_name);
            holder.themes_gridview_txt_price = (TextView)convertView.findViewById(R.id.themes_txt_price);
            holder.mthemes_gridview_img_layout = (LinearLayout)convertView.findViewById(R.id.themes_gridview_img_layout);
            holder.mthemes_gridview_price_layout = (LinearLayout)convertView.findViewById(R.id.themes_gridview_price_layout);
            holder.mthemes_txt_numberofpeople_textview = (TextView)convertView.findViewById(R.id.themes_txt_numberofpeople_textview);
            holder.mhome_fieldlist_txt_fieldtitle = (TextView)convertView.findViewById(R.id.home_fieldlist_txt_fieldtitle);
            holder.mcommunity_resources_activity_data_layout = (LinearLayout)convertView.findViewById(R.id.community_resources_activity_data_layout);
            holder.mcommunity_resources_start_date_text = (TextView)convertView.findViewById(R.id.community_resources_start_date_text);
            holder.mcommunity_resources_end_date_text = (TextView)convertView.findViewById(R.id.community_resources_end_date_text);
            holder.mthemes_imageview_layout = (RelativeLayout)convertView.findViewById(R.id.themes_imageview_layout);
            holder.mcommunity_resource_address_layout = (LinearLayout)convertView.findViewById(R.id.community_resource_address_layout);
            holder.mcommunity_resource_address_text = (TextView)convertView.findViewById(R.id.community_resource_address_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (data != null) {
            if (data.size() > 0) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(((width-90)/2),
                        ((width-90)/2)*276/345);
                LinearLayout.LayoutParams paramdata = new LinearLayout.LayoutParams(((width-90)/2), Constants.Dp2Px(mcontext,50));
                LinearLayout.LayoutParams paramactivitydata = new LinearLayout.LayoutParams(((width-90)/2), Constants.Dp2Px(mcontext,70));
                if (type == 1) {
//                    holder.mthemes_gridview_img_layout.setLayoutParams(paramactivity);
                    holder.mthemes_imageview_layout.setLayoutParams(param);
                    holder.mthemes_gridview_price_layout.setLayoutParams(paramactivitydata);
                    holder.mthemes_gridview_price_layout.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_themesinfo_listitem_price_layout_bg));
                    holder.mcommunity_resource_address_layout.setVisibility(View.VISIBLE);
                    holder.mcommunity_resources_activity_data_layout.setVisibility(View.VISIBLE);
                    if (data.get(position).getAddress() != null &&
                            data.get(position).getAddress().length() > 0) {
                        holder.mcommunity_resource_address_text.setText(data.get(position).getAddress());
                    }
                    if (data.get(position).getActivity_start_date() != null && data.get(position).getActivity_start_date().length() > 0) {
                        holder.mcommunity_resources_start_date_text.setText(data.get(position).getActivity_start_date().replaceAll("-", "."));
                    }
                    if (data.get(position).getDeadline() != null && data.get(position).getDeadline().length() > 0) {
                        holder.mcommunity_resources_end_date_text.setText(data.get(position).getDeadline().replaceAll("-", "."));
                    }
                } else if (type == 0){
//                    holder.mthemes_gridview_img_layout.setLayoutParams(params);
                    holder.mthemes_imageview_layout.setLayoutParams(param);
                    holder.mthemes_gridview_price_layout.setLayoutParams(paramdata);
                    holder.mthemes_gridview_price_layout.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.activity_themesinfo_listitem_price_layout_bg));
                    holder.mcommunity_resource_address_layout.setVisibility(View.GONE);
                    holder.mcommunity_resources_activity_data_layout.setVisibility(View.GONE);
                }
                if (data.get(position).getPic_url() != null) {
                    if (data.get(position).getPic_url().length() != 0) {
                        Picasso.with(mcontext).load(data.get(position).getPic_url().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width - 90) / 2, ((width - 90) / 2) *276/345).into(holder.themes_gridview_img);
                    } else {
                        Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width - 90) / 2, ((width - 90) / 2) *276/345).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.themes_gridview_img);
                    }
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width - 90) / 2, ((width - 90) / 2) *276/345).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.themes_gridview_img);
                }
                if (data.get(position).getField_type() != null &&
                        data.get(position).getField_type().length() > 0) {
                    holder.mhome_fieldlist_txt_fieldtitle.setVisibility(View.VISIBLE);
                    holder.mhome_fieldlist_txt_fieldtitle.setText(data.get(position).getField_type());
                } else {
                    holder.mhome_fieldlist_txt_fieldtitle.setVisibility(View.GONE);
                }
                holder.themes_gridview_txt_name.setText((String) data.get(position).getResource_name());
                if (data.get(position).getPrice() != null) {
                    if (data.get(position).getPrice().length() > 0) {
                        holder.themes_gridview_txt_price.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                Constants.getpricestring(data.get(position).getPrice(),0.01)),9));
                    } else {
                        holder.themes_gridview_txt_price.setText("");
                    }
                } else {
                    holder.themes_gridview_txt_price.setText("");
                }
                if (data.get(position).getNumber_of_people() != null) {
                    if (data.get(position).getNumber_of_people().length() > 0) {
                        holder.mthemes_txt_numberofpeople_textview.setText("人流量："+data.get(position).getNumber_of_people());
                    } else {
                        holder.mthemes_txt_numberofpeople_textview.setText("");
                    }
                } else {
                    holder.mthemes_txt_numberofpeople_textview.setText("");
                }
            }
        }

        return convertView;
    }
    static class ViewHolder {
        public ImageView themes_gridview_img;
        public TextView themes_gridview_txt_name;
        public TextView themes_gridview_txt_price;
        public LinearLayout mthemes_gridview_img_layout;
        public LinearLayout mthemes_gridview_price_layout;
        public TextView mthemes_txt_numberofpeople_textview;
        public TextView mhome_fieldlist_txt_fieldtitle;
        public LinearLayout mcommunity_resources_activity_data_layout;
        public TextView mcommunity_resources_start_date_text;
        public TextView mcommunity_resources_end_date_text;
        public RelativeLayout mthemes_imageview_layout;
        public LinearLayout mcommunity_resource_address_layout;
        public TextView mcommunity_resource_address_text;
    }
}
