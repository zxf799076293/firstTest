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
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.InvoicesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/10.
 */
public class FieldinfoOtherResourcesAdapter extends BaseAdapter {
    private ArrayList<ResourceSearchItemModel> data = new ArrayList<ResourceSearchItemModel>();
    private Context mcontext;
    private Activity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private int width;
    private int height;
    public FieldinfoOtherResourcesAdapter(Context context, Activity activity, ArrayList<ResourceSearchItemModel> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
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
            convertView = mInflater.inflate(R.layout.activity_fieldinfo_other_resource_item, null);
            holder.mRecommendResImgRL = (RelativeLayout)convertView.findViewById(R.id.resource_info_recommend_item_img_rl);
            holder.mRecommendResImg = (ImageView)convertView.findViewById(R.id.resource_info_recommend_item_img);
            holder.mRecommendResTimeTV = (TextView)convertView.findViewById(R.id.resource_info_recommend_item_tiem_tv);
            holder.mRecommendResPriceTV = (TextView)convertView.findViewById(R.id.resource_info_recommend_item_price_tv);
            holder.mRecommendResSecondPriceTV = (TextView)convertView.findViewById(R.id.other_resource_item_froup_second_price_tv);
            holder.mRecommendResNameTV = (TextView)convertView.findViewById(R.id.resource_info_recommend_item_name_tv);
            holder.mRecommendResTimeLL = (LinearLayout)convertView.findViewById(R.id.resource_info_recommend_item_tiem_ll);
            holder.mRecommendResView = (View)convertView.findViewById(R.id.resource_info_recommend_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (data != null) {
            if (data.size() > 0) {
                if (type == 1){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                            (width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 300 / 428);

                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                            (width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 300 / 428);
//                    LinearLayout.LayoutParams paramdata = new LinearLayout.LayoutParams((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), 100);
//                    holder.mthemes_gridview_price_layout.setLayoutParams(paramdata);
                    holder.mRecommendResImgRL.setLayoutParams(params);
                }
                if (data.get(position).getPic_url() != null) {
                    if (data.get(position).getPic_url().length() != 0) {
                        Picasso.with(mcontext).load(data.get(position).getPic_url().toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).
                                error(R.drawable.ic_no_pic_big).resize((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                                ((width - 90) / 2) * 300 / 428+70).into(holder.mRecommendResImg);
                    } else {
                        Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 300 / 428).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.mRecommendResImg);
                    }
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 300 / 428).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.mRecommendResImg);
                }
                if (data.get(position).getPrice() != null && data.get(position).getPrice().length() > 0) {
                    holder.mRecommendResPriceTV.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                Constants.getpricestring(data.get(position).getPrice(),0.01)),11));
                } else {
                    holder.mRecommendResPriceTV.setText("");
                }
                if (mactivity instanceof FieldInfoActivity) {
                    holder.mRecommendResSecondPriceTV.setVisibility(View.GONE);
                } else {
                    holder.mRecommendResSecondPriceTV.setVisibility(View.VISIBLE);
                }
                if (mactivity instanceof FieldInfoActivity) {
                    if (data.get(position).getActivity_start_date() != null && data.get(position).getDeadline() != null &&
                            data.get(position).getActivity_start_date().length() > 0 && data.get(position).getDeadline().length() > 0) {
                        holder.mRecommendResTimeTV.setText(mcontext.getResources().getString(R.string.fieldinfo_activity_time_text) +
                                data.get(position).getActivity_start_date().replaceAll("-", ".") + "-" + data.get(position).getDeadline().replaceAll("-", "."));
                        holder.mRecommendResTimeLL.setVisibility(View.VISIBLE);
                    } else {
                        holder.mRecommendResTimeLL.setVisibility(View.GONE);
                        holder.mRecommendResTimeTV.setText("");
                    }
                } else {
                    if (data.get(position).getNumber_of_group_purchase_now() != null &&
                            data.get(position).getNumber_of_group_purchase_now() > 0) {
                        holder.mRecommendResTimeTV.setText(mcontext.getResources().getString(R.string.groupbooding_list_hot_item_people_first_str) +
                                String.valueOf(data.get(position).getNumber_of_group_purchase_now()) +
                                mcontext.getResources().getString(R.string.groupbooding_list_hot_item_people_second_str));
                        holder.mRecommendResTimeLL.setVisibility(View.VISIBLE);
                    } else {
                        holder.mRecommendResTimeLL.setVisibility(View.GONE);
                        holder.mRecommendResTimeTV.setText("");
                    }
                }

                if (data.get(position).getName() != null && data.get(position).getName().length() > 0) {
                    holder.mRecommendResNameTV.setText(data.get(position).getName());
                } else {
                    holder.mRecommendResNameTV.setText("");
                }
                if (position == data.size() - 1) {
                    holder.mRecommendResView.setVisibility(View.VISIBLE);
                } else {
                    holder.mRecommendResView.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }
    static class ViewHolder {
        public RelativeLayout mRecommendResImgRL;
        public ImageView mRecommendResImg;
        public TextView mRecommendResTimeTV;
        public TextView mRecommendResPriceTV;
        public TextView mRecommendResSecondPriceTV;
        public TextView mRecommendResNameTV;
        public LinearLayout mRecommendResTimeLL;
        public View mRecommendResView;
    }
}
