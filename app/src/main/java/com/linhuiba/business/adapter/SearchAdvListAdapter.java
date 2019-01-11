package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
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
import com.linhuiba.business.activity.SelfSupportShopActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.SelfSupportShopFragment;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SearchAdvListAdapter extends BaseAdapter {
    private ArrayList<SearchSellResModel> mFeildList;
    private LayoutInflater mInflater = null;
    private Activity mactivity;
    private Context mContext;
    private boolean isSelfSupportShop;
    public SearchAdvListAdapter(Context context, ArrayList<SearchSellResModel> list, int type, Activity mactivity, boolean isSelfSupportShop) {
        if (context != null && mactivity != null) {
            this.mFeildList = list;
            this.mactivity = mactivity;
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.isSelfSupportShop = isSelfSupportShop;
        }
    }

    @Override
    public int getCount() {
        if (mFeildList != null) {
            return mFeildList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        //获取数据集中与指定索引对应的数据项
        return position;
    }

    @Override
    public long getItemId(int position) {
        //获取在列表中与指定索引对应的行id
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.module_lv_item_adv_list, null);
            holder.fieldimg = (ImageView) convertView.findViewById(R.id.searchlist_item_img);
            holder.msearchlist_item_img_text = (TextView) convertView.findViewById(R.id.searchlist_item_img_text);
            holder.field_name = (TextView) convertView.findViewById(R.id.searchlist_item_nametxt);
            holder.address = (TextView) convertView.findViewById(R.id.searchlist_item_addresstxt);
            holder.msubsidy_txt_img = (ImageView) convertView.findViewById(R.id.subsidy_txt_tv);
            holder.reviewed_count = (TextView) convertView.findViewById(R.id.evaluation_size_txt);
            holder.mnumberofpeople_txt = (TextView) convertView.findViewById(R.id.numberofpeople_txt);
            holder.mpeople_relativelayout = (LinearLayout) convertView.findViewById(R.id.people_relativelayout);
            holder.msearchlist_activity_item_layout = (LinearLayout) convertView.findViewById(R.id.searchlist_activity_item_layout);
            holder.msearchlist_activity_starttime = (TextView) convertView.findViewById(R.id.searchlist_activity_starttime);
            holder.msearchlist_activity_endtime = (TextView) convertView.findViewById(R.id.searchlist_activity_endtime);
            holder.msearchlist_txt_fieldtitle = (TextView) convertView.findViewById(R.id.searchlist_txt_fieldtitle);
            holder.msearchlist_txt_fieldtitle_layout = (LinearLayout) convertView.findViewById(R.id.searchlist_txt_fieldtitle_layout);

            holder.msearchlist_item_activity_data_title = (TextView) convertView.findViewById(R.id.searchlist_item_activity_data_title);
            holder.msearchlist_item_activity_data_middle_text = (TextView) convertView.findViewById(R.id.searchlist_item_activity_data_middle_text);
            holder.price = (TextView) convertView.findViewById(R.id.searchlist_item_price);
            holder.mSearchItemSubsidyTV = (TextView) convertView.findViewById(R.id.search_subsidy_tv);
            holder.field_item = (RelativeLayout) convertView.findViewById(R.id.search_field_item);
            holder.mselfOperatedImg = (ImageView) convertView.findViewById(R.id.searchlist_self_operated_img);
            holder.mSearchListCommonPriceLL = (LinearLayout) convertView.findViewById(R.id.searchlist_common_price_ll);
            holder.mSearchListEnquiryLL = (LinearLayout) convertView.findViewById(R.id.searchlist_item_enquiry_ll);
            holder.mSearchListEnquiryPriceLL = (LinearLayout) convertView.findViewById(R.id.searchlist_item_enquiry_price_ll);
            holder.mSearchListMinPriceTV = (TextView) convertView.findViewById(R.id.searchlist_item_enquiry_min_price_tv);
            holder.mSearchListMaxPriceTV = (TextView) convertView.findViewById(R.id.searchlist_item_enquiry_max_price_tv);
            holder.mSearchLabelIV = (ImageView) convertView.findViewById(R.id.searchlist_label_iv);
            holder.mSearchAdvListTimeLL = (LinearLayout) convertView.findViewById(R.id.search_adv_list_time_ll);
            holder.mSearchListLabelsLL = (LinearLayout) convertView.findViewById(R.id.search_list_item_label_ll);
            holder.mSearchListLabels0TV = (TextView) convertView.findViewById(R.id.search_list_item_label0);
            holder.mSearchListLabels1TV = (TextView) convertView.findViewById(R.id.search_list_item_label1);
            holder.mSearchListLabels2TV = (TextView) convertView.findViewById(R.id.search_list_item_label2);
            holder.mSearchListActivityAddressLL = (LinearLayout) convertView.findViewById(R.id.searchlist_activity_address_ll);
            holder.mSearchListDistanceTV = (TextView) convertView.findViewById(R.id.community_distance_location_tv);
            holder.mSearchListDistanceView = (View) convertView.findViewById(R.id.community_distance_view);
            holder.mSearchListActivityAddressTV = (TextView) convertView.findViewById(R.id.community_address_tv);
            holder.mSearchListNumberOfPeopleTV = (TextView) convertView.findViewById(R.id.search_list_item_number_of_people_tv);
            holder.mSearchListDemandLL = (LinearLayout) convertView.findViewById(R.id.searchlist_item_demand_ll);
            holder.mSearchlistPriceLL = (LinearLayout) convertView.findViewById(R.id.searchlist_item_price_ll);
            holder.mSearchListOrderSizeTV = (TextView) convertView.findViewById(R.id.search_list_item_order_quantity);

            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.field_item.setVisibility(View.VISIBLE);
        if (mFeildList != null) {
            if (mFeildList.size() != 0) {
                if (position == 0 && isSelfSupportShop) {
                    holder.mselfOperatedImg.setVisibility(View.VISIBLE);
                } else {
                    holder.mselfOperatedImg.setVisibility(View.GONE);
                }
                //2018/12/8 广告人流量
                holder.mSearchListLabelsLL.setVisibility(View.GONE);
                holder.mSearchAdvListTimeLL.setVisibility(View.GONE);
                holder.address.setVisibility(View.GONE);
                holder.mSearchListActivityAddressLL.setVisibility(View.GONE);
                holder.mSearchListNumberOfPeopleTV.setVisibility(View.GONE);
                holder.mSearchListDemandLL.setVisibility(View.GONE);
                holder.mSearchListOrderSizeTV.setVisibility(View.GONE);

                if (isSelfSupportShop) {
                    if (mFeildList.get(position).getLabels() != null &&
                            mFeildList.get(position).getLabels().size() > 0) {
                        holder.mSearchListLabelsLL.setVisibility(View.VISIBLE);
                        holder.mSearchListLabels0TV.setVisibility(View.GONE);
                        holder.mSearchListLabels1TV.setVisibility(View.GONE);
                        holder.mSearchListLabels2TV.setVisibility(View.GONE);
                        for (int i = 0; i < mFeildList.get(position).getLabels().size(); i++) {
                            if (i < 3) {
                                String label = "";
                                if (mFeildList.get(position).getLabels().get(i).getDisplay_name() != null &&
                                        mFeildList.get(position).getLabels().get(i).getDisplay_name().length() > 0) {
                                    label = mFeildList.get(position).getLabels().get(i).getDisplay_name();
                                } else {
                                    if (mFeildList.get(position).getLabels().get(i).getName() != null &&
                                            mFeildList.get(position).getLabels().get(i).getName().length() > 0) {
                                        label = mFeildList.get(position).getLabels().get(i).getName();
                                    }
                                }
                                if (label.length() > 0) {
                                    if (i == 0) {
                                        holder.mSearchListLabels0TV.setVisibility(View.VISIBLE);
                                        holder.mSearchListLabels0TV.setText(label);
                                    } else if (i == 1) {
                                        holder.mSearchListLabels1TV.setVisibility(View.VISIBLE);
                                        holder.mSearchListLabels1TV.setText(label);
                                    } else if (i == 2) {
                                        holder.mSearchListLabels2TV.setVisibility(View.VISIBLE);
                                        holder.mSearchListLabels2TV.setText(label);
                                    }
                                }
                            }
                        }
                    } else {
                        holder.mSearchListLabelsLL.setVisibility(View.GONE);
                    }
                    if (mFeildList.get(position).getDistance() != null &&
                            mFeildList.get(position).getDistance().length() > 0) {
                        holder.mSearchListActivityAddressLL.setVisibility(View.VISIBLE);
                        holder.mSearchListDistanceTV.setVisibility(View.VISIBLE);
                        holder.mSearchListDistanceView.setVisibility(View.VISIBLE);
                        if (Double.parseDouble(mFeildList.get(position).getDistance()) > 1000) {
                            holder.mSearchListDistanceTV.setText(mContext.getResources().getString(R.string.search_list_station_first_str) +
                                    Constants.getpricestring(mFeildList.get(position).getDistance(),0.001) + "km");
                        } else {
                            holder.mSearchListDistanceTV.setText(mContext.getResources().getString(R.string.search_list_station_first_str) +
                                    mFeildList.get(position).getDistance() + "m");
                        }
                    } else {
                        holder.mSearchListDistanceTV.setVisibility(View.GONE);
                        holder.mSearchListDistanceView.setVisibility(View.GONE);
                    }
                    String address = "";
                    if (mFeildList.get(position).getCity() != null &&
                            mFeildList.get(position).getCity().length() > 0) {
                        address = mFeildList.get(position).getCity();
                    }
                    if (mFeildList.get(position).getDistrict() != null &&
                            mFeildList.get(position).getDistrict().length() > 0) {
                        address = address + mFeildList.get(position).getDistrict();
                    }
                    if (mFeildList.get(position).getAddress() != null &&
                            mFeildList.get(position).getAddress().length() > 0) {
                        address = address + mFeildList.get(position).getAddress();
                    }
                    if (address.length() > 0) {
                        holder.mSearchListActivityAddressLL.setVisibility(View.VISIBLE);
                        holder.mSearchListActivityAddressTV.setText(address);
                    }
                    if (mFeildList.get(position).getNumber_of_people() != null && mFeildList.get(position).getNumber_of_people().length() > 0) {
                        holder.mSearchListNumberOfPeopleTV.setText(mContext.getResources().getString(R.string.search_numberofpeople_first_str) +
                                mFeildList.get(position).getNumber_of_people());
                        holder.mSearchListNumberOfPeopleTV.setVisibility(View.VISIBLE);
                    } else {
                        holder.mSearchListNumberOfPeopleTV.setText("");
                        holder.mSearchListNumberOfPeopleTV.setVisibility(View.GONE);
                    }
                    holder.mSearchListOrderSizeTV.setVisibility(View.VISIBLE);
                    holder.mSearchListOrderSizeTV.setText(
                            String.valueOf(mFeildList.get(position).getNumber_of_order()) +
                                    mContext.getResources().getString(R.string.module_searchlist_item_otder_size));
                    //2018/12/8 是否显示我有需求
                    holder.mSearchListDemandLL.setVisibility(View.GONE);

                    if (mFeildList.get(position).getRes_type_id() == 3) {
                        holder.mSearchAdvListTimeLL.setVisibility(View.VISIBLE);
                        holder.mpeople_relativelayout.setVisibility(View.GONE);
                        holder.msearchlist_activity_item_layout.setVisibility(View.VISIBLE);
                        if (mFeildList.get(position).getDeadline() != null && mFeildList.get(position).getDeadline().length() > 0) {
                            holder.msearchlist_activity_endtime.setText(mFeildList.get(position).getDeadline().replaceAll("-", "."));
                        }
                        if (mFeildList.get(position).getActivity_start_date() != null && mFeildList.get(position).getActivity_start_date().length() > 0) {
                            holder.msearchlist_activity_starttime.setText(mFeildList.get(position).getActivity_start_date().replaceAll("-", "."));
                        }
                        if (mFeildList.get(position).getRes_type_id() == 3 && mFeildList.get(position).isExpired()) {
                            holder.msearchlist_item_img_text.setVisibility(View.VISIBLE);
                            holder.field_name.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.address.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.mSearchListActivityAddressTV.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            //2017/9/7 活动过期后的惠的图标替换
                            holder.msubsidy_txt_img.setImageResource(R.drawable.ic_no_subsidy_normal_three);
                            holder.reviewed_count.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.mnumberofpeople_txt.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.price.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.msearchlist_activity_starttime.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.msearchlist_activity_endtime.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.msearchlist_item_activity_data_title.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.msearchlist_item_activity_data_middle_text.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                        } else {
                            holder.msearchlist_item_img_text.setVisibility(View.GONE);
                            holder.field_name.setTextColor(mactivity.getResources().getColor(R.color.home_advertising_text_color));
                            holder.address.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.mSearchListActivityAddressTV.setTextColor(mactivity.getResources().getColor(R.color.headline_tv_color));
                            //2017/9/7 惠的图标替换
                            holder.msubsidy_txt_img.setImageResource(R.drawable.ic_subsidy_normal_three_copy);
                            holder.reviewed_count.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.mnumberofpeople_txt.setTextColor(mactivity.getResources().getColor(R.color.register_edit_color));
                            holder.price.setTextColor(mactivity.getResources().getColor(R.color.default_redbg));
                            holder.msearchlist_activity_starttime.setTextColor(mactivity.getResources().getColor(R.color.default_redbg));
                            holder.msearchlist_activity_endtime.setTextColor(mactivity.getResources().getColor(R.color.default_redbg));
                            holder.msearchlist_item_activity_data_title.setTextColor(mactivity.getResources().getColor(R.color.title_bar_txtcolor));
                            holder.msearchlist_item_activity_data_middle_text.setTextColor(mactivity.getResources().getColor(R.color.default_redbg));
                        }
                    }
                } else {
                    holder.mSearchAdvListTimeLL.setVisibility(View.VISIBLE);
                    holder.mpeople_relativelayout.setVisibility(View.VISIBLE);
                    holder.msearchlist_activity_item_layout.setVisibility(View.GONE);
                    holder.address.setVisibility(View.VISIBLE);
                    holder.reviewed_count.setText(mactivity.getResources().getString(R.string.search_numberofpaid_first_str) + String.valueOf(mFeildList.get(position).getOrder_count()));
                    if (mFeildList.get(position).getNumber_of_people() != null &&
                            mFeildList.get(position).getNumber_of_people().length() > 0) {
                        holder.mnumberofpeople_txt.setVisibility(View.VISIBLE);
                        holder.mnumberofpeople_txt.setText(mactivity.getResources().getString(R.string.search_numberofpeople_first_str) + mFeildList.get(position).getNumber_of_people());
                    } else {
                        holder.mnumberofpeople_txt.setVisibility(View.GONE);
                    }
                    if (mFeildList.get(position).getAddress() != null) {
                        if (mFeildList.get(position).getAddress().length() != 0) {
                            holder.address.setText((String) mFeildList.get(position).getAddress());
                        } else {
                            holder.address.setText("");
                        }
                    } else {
                        holder.address.setText("");
                    }

                }

                if (mFeildList.get(position).getRes_type_id() == 2) {
                    if (mFeildList.get(position).getAd_type() != null &&
                            mFeildList.get(position).getAd_type().length() > 0) {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.VISIBLE);
                        holder.msearchlist_txt_fieldtitle.setText(mFeildList.get(position).getAd_type());
                    } else {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.GONE);
                    }
                } else if (mFeildList.get(position).getRes_type_id() == 1) {
                    if (mFeildList.get(position).getField_type() != null &&
                            mFeildList.get(position).getField_type().length() > 0) {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.VISIBLE);
                        if (mFeildList.get(position).getTotal_area() != null && mFeildList.get(position).getTotal_area().length() > 0) {
                            holder.msearchlist_txt_fieldtitle.setText(mFeildList.get(position).getField_type() + "·" +
                                    Constants.getpricestring(mFeildList.get(position).getTotal_area(),1) + mContext.getResources().getString(R.string.myselfinfo_company_demand_area_unit_text));
                        } else {
                            holder.msearchlist_txt_fieldtitle.setText(mFeildList.get(position).getField_type());
                        }
                    } else {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.GONE);
                    }
                } else if (mFeildList.get(position).getRes_type_id() == 3) {
                    if (mFeildList.get(position).getActivity_type() != null &&
                            mFeildList.get(position).getActivity_type().length() > 0) {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.VISIBLE);
                        if (mFeildList.get(position).getTotal_area() != null && mFeildList.get(position).getTotal_area().length() > 0) {
                            holder.msearchlist_txt_fieldtitle.setText(mFeildList.get(position).getActivity_type() + "·" +
                                    Constants.getpricestring(mFeildList.get(position).getTotal_area(),1) + mContext.getResources().getString(R.string.myselfinfo_company_demand_area_unit_text));
                        } else {
                            holder.msearchlist_txt_fieldtitle.setText(mFeildList.get(position).getActivity_type());
                        }
                    } else {
                        holder.msearchlist_txt_fieldtitle_layout.setVisibility(View.GONE);
                    }
                }
                if (mFeildList.get(position).getPic_url() != null) {
                    if (mFeildList.get(position).getPic_url().length() != 0) {
                        Picasso.with(mactivity).load(mFeildList.get(position).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(200, 200).into(holder.fieldimg);
                    } else {
                        Picasso.with(mactivity).load(R.drawable.ic_no_pic_small).resize(200, 200).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.fieldimg);
                    }
                } else {
                    Picasso.with(mactivity).load(R.drawable.ic_no_pic_small).resize(200, 200).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.fieldimg);
                }
                if (mFeildList.get(position).getResource_name() != null) {
                    if (mFeildList.get(position).getResource_name().length() != 0) {
                        holder.field_name.setText(mFeildList.get(position).getResource_name());
                        if (mFeildList.get(position).getIndoor() != null) {
                            if (mFeildList.get(position).getIndoor() == 0) {
                                holder.field_name.setText(mFeildList.get(position).getResource_name()
                                        + mContext.getResources().getString(R.string.fieldinfo_location_unindoor_text));
                            } else if (mFeildList.get(position).getIndoor() == 1) {
                                holder.field_name.setText(mFeildList.get(position).getResource_name()
                                        + mContext.getResources().getString(R.string.fieldinfo_location_indoor_text));
                            }
                        }
                    } else {
                        holder.field_name.setText("");
                    }
                } else {
                    holder.field_name.setText("");
                }

                if (mFeildList.get(position).getSubsidy_fee() != null) {
                    if (!(TextUtils.isEmpty(mFeildList.get(position).getSubsidy_fee()))) {
                        double double_Subsidy_fee = Double.parseDouble(mFeildList.get(position).getSubsidy_fee());
                        if (double_Subsidy_fee > 0) {
                            holder.msubsidy_txt_img.setVisibility(View.VISIBLE);
                            //2017/9/7 补贴价格显示
                            holder.mSearchItemSubsidyTV.setText(Constants.getSpannableAllStr(mContext,
                                    (mContext.getResources().getString(R.string.order_listitem_price_unit_text)+
                                            Constants.getpricestring(mFeildList.get(position).getPrice(),0.01)),
                                    0,0,0,true,0,Constants.getpricestring(mFeildList.get(position).getPrice(),0.01).length() + 1,null,0,0,
                                    false,0,0));
                        } else {
                            holder.msubsidy_txt_img.setVisibility(View.GONE);
                        }
                    } else {
                        holder.msubsidy_txt_img.setVisibility(View.GONE);
                    }
                } else {
                    holder.msubsidy_txt_img.setVisibility(View.GONE);
                }

                if (mFeildList.get(position).getPrice() != null) {
                    if (mFeildList.get(position).getPrice().length() != 0) {
                        String mActualPrice =Constants.getSearchPriceStr(Constants.getdoublepricestring(Double.parseDouble(mFeildList.get(position).getPrice()) -
                                (Double.parseDouble(mFeildList.get(position).getSubsidy_fee())),1),0.01);

                        holder.price.setText(Constants.getSpannableAllStr(mContext,(mContext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                        mActualPrice), 14,0,1,false,0,0,
                                14,mActualPrice.length() + 1 - 2,
                                mActualPrice.length() + 1,
                                true,1,mActualPrice.length() + 1 - 3));
                    } else {
                        holder.price.setText("");
                    }
                } else {
                    holder.price.setText("");
                }
                if (mFeildList.get(position).getIs_enquiry() != null &&
                        mFeildList.get(position).getIs_enquiry() == 1) {
                    holder.mSearchListEnquiryLL.setVisibility(View.VISIBLE);
                    holder.mSearchListCommonPriceLL.setVisibility(View.GONE);
                    if (mFeildList.get(position).getRefer_min_price() != null) {
                        holder.mSearchListEnquiryPriceLL.setVisibility(View.VISIBLE);
                        holder.mSearchListMinPriceTV.setText(
                                mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                        Constants.getpricestring(String.valueOf(mFeildList.get(position).getRefer_min_price()),0.01));
                        if (mFeildList.get(position).getRefer_max_price() != null) {
                            holder.mSearchListMaxPriceTV.setText("~" +
                                    mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                    Constants.getpricestring(String.valueOf(mFeildList.get(position).getRefer_max_price()),0.01));
                        }
                    } else {
                        if (mFeildList.get(position).getRefer_max_price() != null) {
                            holder.mSearchListMaxPriceTV.setText(mContext.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                    Constants.getpricestring(String.valueOf(mFeildList.get(position).getRefer_max_price()),0.01));
                        }
                    }
                } else {
                    holder.mSearchListEnquiryLL.setVisibility(View.GONE);
                    holder.mSearchListCommonPriceLL.setVisibility(View.VISIBLE);
                }
                if (mFeildList.get(position).getRes_type_id() == 3) {
                    holder.mSearchLabelIV.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(R.drawable.ic_xianshi_three_two).into(holder.mSearchLabelIV);
                } else {
                    if (mFeildList.get(position).getIs_hot() == 1) {
                        holder.mSearchLabelIV.setVisibility(View.VISIBLE);
                        Picasso.with(mContext).load(R.drawable.ic_remai_three_two).into(holder.mSearchLabelIV);
                    } else {
                        holder.mSearchLabelIV.setVisibility(View.GONE);
                    }
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView fieldimg;
        public TextView msearchlist_item_img_text;
        public TextView field_name;
        public TextView address;
        public ImageView msubsidy_txt_img;
        public TextView reviewed_count;
        public TextView mnumberofpeople_txt;
        public LinearLayout mpeople_relativelayout;
        public LinearLayout msearchlist_activity_item_layout;
        public TextView msearchlist_activity_starttime;
        public TextView msearchlist_activity_endtime;
        public TextView msearchlist_item_activity_data_title;
        public TextView msearchlist_item_activity_data_middle_text;
        public TextView msearchlist_txt_fieldtitle;
        public LinearLayout msearchlist_txt_fieldtitle_layout;
        public TextView price;
        public TextView mSearchItemSubsidyTV;
        public RelativeLayout field_item;
        public ImageView mselfOperatedImg;
        public LinearLayout mSearchListCommonPriceLL;
        public LinearLayout mSearchListEnquiryLL;
        public LinearLayout mSearchListEnquiryPriceLL;
        public TextView mSearchListMinPriceTV;
        public TextView mSearchListMaxPriceTV;
        public ImageView mSearchLabelIV;
        public LinearLayout mSearchAdvListTimeLL;
        public LinearLayout mSearchListLabelsLL;
        public TextView mSearchListLabels0TV;
        public TextView mSearchListLabels1TV;
        public TextView mSearchListLabels2TV;
        public LinearLayout mSearchListActivityAddressLL;
        public TextView mSearchListDistanceTV;
        public View mSearchListDistanceView;
        public TextView mSearchListActivityAddressTV;
        public TextView mSearchListNumberOfPeopleTV;
        public LinearLayout mSearchListDemandLL;
        public LinearLayout mSearchlistPriceLL;
        public TextView mSearchListOrderSizeTV;
    }
}