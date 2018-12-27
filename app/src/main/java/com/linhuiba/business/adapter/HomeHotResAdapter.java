package com.linhuiba.business.adapter;

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

import com.linhuiba.business.R;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeHotResAdapter extends BaseAdapter{
    private ArrayList<ResourceSearchItemModel> data = new ArrayList<ResourceSearchItemModel>();
    private Context mcontext;
    private HomeFragment mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private int width;
    private int height;
    public HomeHotResAdapter(Context context, HomeFragment activity, ArrayList<ResourceSearchItemModel> datas, int invoicetype) {
        if (context != null && activity != null) {
            this.mInflater = LayoutInflater.from(context);
            this.mcontext = activity.getContext();
            this.mactivity = activity;
            this.data = datas;
            this.type = invoicetype;
            DisplayMetrics metric = new DisplayMetrics();
            mactivity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;     // 屏幕宽度（像素）
            height = metric.heightPixels;
        }

    }
    @Override
    public int getCount() {
        if(data != null){
            return data.size();
        } else{
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
            convertView = mInflater.inflate(R.layout.fragment_homeactivity_item, null);
            holder.mimage_layout = (RelativeLayout)convertView.findViewById(R.id.image_layout);
            holder.mhome_activity_img = (OvalImageView)convertView.findViewById(R.id.home_activity_img);
            holder.mproject_type_layout = (LinearLayout)convertView.findViewById(R.id.project_type_layout);
            holder.mhome_activity_typelayout = (LinearLayout)convertView.findViewById(R.id.home_activity_typelayout);
            holder.mhome_activity_name = (TextView)convertView.findViewById(R.id.home_activity_name);
            holder.mhome_activity_price = (TextView)convertView.findViewById(R.id.home_activity_price);
            holder.mactivitylistitem_start_date_text = (TextView)convertView.findViewById(R.id.activitylistitem_start_date_text);
            holder.mactivitylistitem_end_date_text = (TextView)convertView.findViewById(R.id.activitylistitem_end_date_text);
            holder.mHomeHotLbelImgv = (ImageView)convertView.findViewById(R.id.home_activity_label_img);
            holder.mHomeSubsidyLL = (LinearLayout)convertView.findViewById(R.id.home_subsidy_ll);
            holder.mHomeSubsidyTV = (TextView) convertView.findViewById(R.id.home_subsidy_tv);
            holder.mHomeHotPriceLL = (LinearLayout)convertView.findViewById(R.id.home_hot_item_price_ll);
            holder.mHomeHotInquiryTV = (TextView) convertView.findViewById(R.id.home_hot_item_inquiry_tv);

            holder.mHomeServiceProvideLL = (LinearLayout)convertView.findViewById(R.id.home_service_provider_ll);
            holder.mHomeServiceProvideNameTV = (TextView)convertView.findViewById(R.id.home_service_provider_name_tv);
            holder.mHomeServiceProvideLabelOneTV = (TextView)convertView.findViewById(R.id.home_service_provider_label_one_tv);
            holder.mHomeServiceProvideLabelTwoTV = (TextView)convertView.findViewById(R.id.home_service_provider_label_two_tv);
            holder.mHomeServiceProvideCityTV = (TextView)convertView.findViewById(R.id.home_service_provider_city_tv);
            holder.mHomeServiceProvidePhoneTV= (TextView)convertView.findViewById(R.id.home_service_provider_phone_tv);
            holder.mHomeServiceProviderView = (View)convertView.findViewById(R.id.home_service_provider_view);
            holder.mHomeServiceProviderRightView = (View)convertView.findViewById(R.id.home_service_provider_right_view);
            holder.mHomeServiceProviderCutOffRule = (View)convertView.findViewById(R.id.home_service_provider_cut_off_rule_view);
            holder.getmHomeServiceProvideLabelLL = (LinearLayout) convertView.findViewById(R.id.home_service_provider_label_ll);
            holder.mHomeCommunityOrderTV = (TextView) convertView.findViewById(R.id.home_community_order_size_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 1 || type == 2) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width)* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                    (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338);
            holder.mimage_layout.setLayoutParams(paramgroups);
            holder.getmHomeServiceProvideLabelLL.setVisibility(View.GONE);
            holder.mhome_activity_typelayout.setVisibility(View.VISIBLE);
            holder.mHomeServiceProvideLL.setVisibility(View.GONE);
            holder.mHomeServiceProviderView.setVisibility(View.GONE);
            holder.mHomeServiceProviderRightView.setVisibility(View.GONE);
            holder.mhome_activity_name.setText(data.get(position).getResource_name());
            if (type == 1) {
                holder.mHomeHotLbelImgv.setVisibility(View.VISIBLE);
                holder.mHomeSubsidyLL.setVisibility(View.GONE);
                holder.mproject_type_layout.setVisibility(View.GONE);
                if (data.get(position).getRes_type_id() == 3) {
                    holder.mproject_type_layout.setVisibility(View.VISIBLE);
                    Picasso.with(mcontext).load(R.drawable.ic_xianshi_three_two).into(holder.mHomeHotLbelImgv);
                    if (data.get(position).getDeadline() != null && data.get(position).getDeadline().length() > 0) {
                        holder.mactivitylistitem_end_date_text.setText(data.get(position).getDeadline().replaceAll("-", "."));
                    }
                    if (data.get(position).getActivity_start_date() != null && data.get(position).getActivity_start_date().length() > 0) {
                        holder.mactivitylistitem_start_date_text.setText(data.get(position).getActivity_start_date().replaceAll("-", "."));
                    }
                } else if (data.get(position).getRes_type_id() == 1) {
                    if (data.get(position).getIs_hot() == 1) {
                        Picasso.with(mcontext).load(R.drawable.ic_remai_three_two).into(holder.mHomeHotLbelImgv);
                    } else {
                        holder.mHomeHotLbelImgv.setVisibility(View.GONE);
                    }
                } else {
                    holder.mHomeHotLbelImgv.setVisibility(View.GONE);
                }
            } else if (type == 2) {
                holder.mHomeSubsidyLL.setVisibility(View.VISIBLE);
                holder.mHomeHotLbelImgv.setVisibility(View.GONE);
                if (data.get(position).getSubsidy_str() != null &&
                        data.get(position).getSubsidy_str().length() > 0) {
                    holder.mHomeSubsidyTV.setText(data.get(position).getSubsidy_str());
                } else {
                    holder.mHomeSubsidyLL.setVisibility(View.GONE);
                }
            }
            if (type == 1 && data.get(position).getIs_enquiry() == 1) {
                holder.mHomeHotPriceLL.setVisibility(View.GONE);
                holder.mHomeHotInquiryTV.setVisibility(View.VISIBLE);
            } else {
                holder.mHomeHotPriceLL.setVisibility(View.VISIBLE);
                holder.mHomeHotInquiryTV.setVisibility(View.GONE);
                if (data.get(position).getPrice() != null) {
                    if (data.get(position).getPrice().length() > 0) {
                        if (data.get(position).getSubsidy_fee() != null &&
                                data.get(position).getSubsidy_fee().length() > 0) {
                            holder.mhome_activity_price.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getdoublepricestring(Double.parseDouble(data.get(position).getPrice()) -
                                            Double.parseDouble(data.get(position).getSubsidy_fee()), 0.01)),12));
                        } else {
                            holder.mhome_activity_price.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                                    Constants.getpricestring(data.get(position).getPrice(), 0.01)),12));
                        }
                    } else {
                        holder.mhome_activity_price.setText("");
                    }
                } else {
                    holder.mhome_activity_price.setText("");
                }
            }

        } else if (type == 3) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width)* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH)
                    + com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,10),
                    (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(((width)* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH)
                    + com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,10),
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,84));
            holder.mHomeServiceProvideLL.setLayoutParams(params);
//            holder.mHomeServiceProvideLL.setPadding(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,5),0,
//                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,5),
//                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,6));
            holder.mimage_layout.setLayoutParams(paramgroups);
            holder.mimage_layout.setPadding(com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,5),
                    0,com.linhuiba.linhuifield.connector.Constants.Dp2Px(mcontext,5),0);
            holder.getmHomeServiceProvideLabelLL.setVisibility(View.VISIBLE);
            holder.mHomeServiceProvideLL.setVisibility(View.VISIBLE);
            holder.mhome_activity_typelayout.setVisibility(View.GONE);
            holder.mHomeServiceProvideLabelOneTV.setVisibility(View.GONE);
            holder.mHomeServiceProvideLabelTwoTV.setVisibility(View.GONE);
            if (data.get(position).getCompany() != null) {
                holder.mHomeServiceProvideNameTV.setText(data.get(position).getCompany());
            }
            if (LoginManager.isLogin()) {
                if (data.get(position).getMobile() != null) {
                    holder.mHomeServiceProvidePhoneTV.setText(data.get(position).getMobile());
                }
            } else {
                holder.mHomeServiceProvidePhoneTV.setText(mcontext.getResources().getString(R.string.home_no_login_phone_str));
                holder.mHomeServiceProvidePhoneTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mactivity.isMobileClick = true;
                        Intent loginIntent = new Intent(mactivity.getActivity(), LoginActivity.class);
                        mactivity.startActivity(loginIntent);
                    }
                });
            }
            holder.mHomeServiceProvideCityTV.setText(data.get(position).getCity().getName());
            if (data.get(position).getMany_service_items().size() > 0) {
                holder.mHomeServiceProvideLabelOneTV.setVisibility(View.VISIBLE);
                holder.mHomeServiceProvideLabelOneTV.setText(data.get(position).getMany_service_items().get(0).getTitle());
                if (data.get(position).getMany_service_items().size() > 1) {
                    holder.mHomeServiceProvideLabelTwoTV.setVisibility(View.VISIBLE);
                    holder.mHomeServiceProvideLabelTwoTV.setText(data.get(position).getMany_service_items().get(1).getTitle());
                }
            }
            if (position == 0) {
                holder.mHomeServiceProviderView.setVisibility(View.VISIBLE);
                holder.mHomeServiceProviderCutOffRule.setVisibility(View.GONE);
            } else {
                holder.mHomeServiceProviderView.setVisibility(View.GONE);
                holder.mHomeServiceProviderCutOffRule.setVisibility(View.VISIBLE);
            }
            if (position == data.size() - 1) {
                holder.mHomeServiceProviderRightView.setVisibility(View.VISIBLE);
            } else {
                holder.mHomeServiceProviderRightView.setVisibility(View.GONE);
            }
        }

        if (data.get(position).getPic_url() != null) {
            if (data.get(position).getPic_url().length() != 0) {
                if (type == 1 || type == 2) {
                    Picasso.with(mcontext).load(data.get(position).getPic_url().toString()+ Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(holder.mhome_activity_img);
                } else if (type == 3) {
                    Picasso.with(mcontext).load(data.get(position).getPic_url().toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(holder.mhome_activity_img);
                }
            } else {
                Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(holder.mhome_activity_img);
            }
        } else {
            Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH), (width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338).into(holder.mhome_activity_img);
        }
        return convertView;
    }
    static class ViewHolder
    {
        public RelativeLayout mimage_layout;
        public OvalImageView mhome_activity_img;
        public LinearLayout mproject_type_layout;
        public LinearLayout mhome_activity_typelayout;
        public TextView mhome_activity_name;
        public TextView mhome_activity_price;
        public TextView mactivitylistitem_start_date_text;
        public TextView mactivitylistitem_end_date_text;
        public ImageView mHomeHotLbelImgv;
        public LinearLayout mHomeSubsidyLL;
        public TextView mHomeSubsidyTV;
        public TextView mHomeHotInquiryTV;
        public LinearLayout mHomeHotPriceLL;
        public LinearLayout mHomeServiceProvideLL;
        public TextView mHomeServiceProvideNameTV;
        public TextView mHomeServiceProvideLabelOneTV;
        public TextView mHomeServiceProvideLabelTwoTV;
        public TextView mHomeServiceProvideCityTV;
        public TextView mHomeServiceProvidePhoneTV;
        public View mHomeServiceProviderView;
        public View mHomeServiceProviderRightView;
        public View mHomeServiceProviderCutOffRule;
        public LinearLayout getmHomeServiceProvideLabelLL;
        public TextView mHomeCommunityOrderTV;
    }
}
