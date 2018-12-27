package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.CommoditypayFragment;
import com.linhuiba.business.model.CommoditypayModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anet.channel.strategy.HttpDnsAdapter;

/**
 * Created by Administrator on 2016/8/18.
 */
public class CommoditypayAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private CommoditypayFragment mactivity;
    private LayoutInflater mInflater = null;
    private static HashMap<String, Boolean> isSelected = new HashMap<String, Boolean>();
    private static HashMap<String, Integer> mtw_num = new HashMap<String, Integer>();
    private boolean isfailure_resources = false;
    public boolean isOnLongClick = false;
    private MiusThread miusThread;
    private PlusThread plusThread;
    private boolean miusEnable;
    private boolean plusEnable;
    private int item_position;
    private int item_group_position;
    private List<CommoditypayModel> groupList;//外层的数据源
    private List<List<CommoditypayModel>> childList;//里层的数据源
    public CommoditypayAdapter(Context context, CommoditypayFragment activity, List<CommoditypayModel> groupList,List<List<CommoditypayModel>> childList ){
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childList.get(groupPosition) != null) {
            return childList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_carditem_group_item, null);
            holder.mCartItemGroupCB = (CheckBox)convertView.findViewById(R.id.fragment_group_item_cb);
            holder.mCartItemGroupResNameTV = (TextView) convertView.findViewById(R.id.fragment_group_item_tv);
            holder.mCartItemGroupLL = (LinearLayout)convertView.findViewById(R.id.cart_item_group_ll);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder)convertView.getTag();
        }
        if (groupList.get(groupPosition).getName() != null) {
            holder.mCartItemGroupResNameTV.setText(groupList.get(groupPosition).getName());
        } else {
            holder.mCartItemGroupResNameTV.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (groupList.get(groupPosition).isValid()) {
            holder.mCartItemGroupCB.setChecked(true);
        } else {
            holder.mCartItemGroupCB.setChecked(false);
        }
        if (groupList.get(groupPosition).getRes_type_id().equals("0")) {
            holder.mCartItemGroupLL.setVisibility(View.GONE);
        } else {
            holder.mCartItemGroupLL.setVisibility(View.VISIBLE);
        }
        holder.mCartItemGroupCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (groupList.get(groupPosition).isValid()) {
                    for (int i = 0; i < childList.get(groupPosition).size(); i++) {
                        isSelected.put(childList.get(groupPosition).get(i).getShopping_cart_item_id(),false);
                    }
                    mactivity.mGroupCardList.get(groupPosition).setValid(false);
                    notifyDataSetChanged();
                    mactivity.getcheckdata(false, getprice(groupPosition), getsubsidy_fee(groupPosition),getorderpaylist_str(groupPosition));

                } else {
                    for (int i = 0; i < childList.get(groupPosition).size(); i++) {
                        isSelected.put(childList.get(groupPosition).get(i).getShopping_cart_item_id(),true);
                    }
                    mactivity.mGroupCardList.get(groupPosition).setValid(true);
                    notifyDataSetChanged();
                    mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_commoditypay_listitem, null);
            holder.mcommoditypay_fieldname = (TextView)convertView.findViewById(R.id.commoditypay_fieldname);
            holder.mcommonditypay_field_start = (TextView)convertView.findViewById(R.id.commonditypay_field_start);
            holder.mcheckbox = (CheckBox) convertView.findViewById(R.id.commodity_checkbox);
            holder.mfield_shop_checkbox_grey = (TextView)convertView.findViewById(R.id.field_shop_checkbox_grey);
            holder.mcommodity_txt_fieldtitle = (TextView)convertView.findViewById(R.id.commodity_txt_fieldtitle);
            holder.morder_subsidy_detailed_txt = (TextView)convertView.findViewById(R.id.order_subsidy_detailed_txt);
            holder.morder_price = (TextView)convertView.findViewById(R.id.order_price);
            holder.morder_img = (ImageView)convertView.findViewById(R.id.order_img);
            holder.mfield_shop_size = (TextView)convertView.findViewById(R.id.field_shop_size);
            holder.mcommodity_custom_dimensiontxt = (TextView)convertView.findViewById(R.id.commodity_custom_dimensiontxt);
            holder.mShopCardFieldItemBottomView = (View)convertView.findViewById(R.id.shop_card_field_item_bottom);
            holder.mShopCardFieldBottomView = (View)convertView.findViewById(R.id.shop_card_field_item_view);
            holder.mShopCardFieldTopView = (View)convertView.findViewById(R.id.shop_card_field_item_top_view);

            holder.madv_order_img = (ImageView)convertView.findViewById(R.id.adv_order_img);
            holder.madvertising_shop_item = (RelativeLayout)convertView.findViewById(R.id.advertising_shop_item);
            holder.madvertising_shop_chose = (CheckBox)convertView.findViewById(R.id.advertising_shop_checkbox);
            holder.madv_commodity_txt_fieldtitle = (TextView)convertView.findViewById(R.id.adv_commodity_txt_fieldtitle);
            holder.madvertising_shop_checkbox_grey = (TextView)convertView.findViewById(R.id.advertising_shop_checkbox_grey);
            holder.madvertiding_shop_subsidy = (TextView)convertView.findViewById(R.id.advertiding_shop_subsidy);
            holder.madvertiding_shop_name = (TextView)convertView.findViewById(R.id.advertiding_shop_advertidingname);
            holder.madvertiding_shop_peoplenum = (TextView)convertView.findViewById(R.id.advertiding_shop_peoplenum);
            holder.madvertiding_shop_equipment = (TextView)convertView.findViewById(R.id.advertiding_shop_equipment);
            holder.madvertising_shop_size = (TextView)convertView.findViewById(R.id.advertising_shop_size);
            holder.mad_shop_type = (TextView)convertView.findViewById(R.id.ad_shop_type);
            holder.mad_shop_fieldtype = (TextView)convertView.findViewById(R.id.ad_shop_fieldtype);
            holder.mad_shop_adress = (TextView)convertView.findViewById(R.id.ad_shop_adress);
            holder.madvertising_shop_mtw_down = (TextView)convertView.findViewById(R.id.advertising_shop_mtw_down);
            holder.madvertising_shop_mtw_num = (EditText)convertView.findViewById(R.id.advertising_shop_mtw_num);
            holder.madvertising_shop_mtw_add = (TextView)convertView.findViewById(R.id.advertising_shop_mtw_add);
            holder.madvertising_shop_price = (TextView)convertView.findViewById(R.id.advertising_shop_price);
            holder.mlinearlayout_chair_visibility = (LinearLayout) convertView.findViewById(R.id.linearlayout_chair_visibility);
            holder.moccupancy_rate = (TextView)convertView.findViewById(R.id.occupancy_rate);
            holder.mcommoditypay_delete_failure_textview = (TextView)convertView.findViewById(R.id.commoditypay_delete_failure_textview);
            holder.mShopCardAdvItemBottomView = (View)convertView.findViewById(R.id.shop_card_adv_item_bottom);
            holder.mCartDaysInAdvanceTV = (TextView)convertView.findViewById(R.id.card_item_days_in_advance_tv);
            holder.mCartMinimumOrderQuantityTV = (TextView)convertView.findViewById(R.id.card_item_minimum_order_quantity_tv);
            holder.mCartDepositTV = (TextView)convertView.findViewById(R.id.card_item_deposit_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (childList.get(groupPosition).get(childPosition).getRes_type_id().equals("1") || childList.get(groupPosition).get(childPosition).getRes_type_id().equals("3")) {
            if (childPosition == childList.get(groupPosition).size() - 1) {
                holder.mShopCardFieldItemBottomView.setVisibility(View.GONE);
                holder.mShopCardFieldBottomView.setVisibility(View.VISIBLE);
            } else {
                holder.mShopCardFieldItemBottomView.setVisibility(View.VISIBLE);
                holder.mShopCardFieldBottomView.setVisibility(View.GONE);
            }
            if (childPosition == 0 &&
                ! (childList.get(groupPosition).get(childPosition).isValid())) {
                holder.mShopCardFieldTopView.setVisibility(View.VISIBLE);
            } else {
                holder.mShopCardFieldTopView.setVisibility(View.GONE);
            }

            holder.madvertising_shop_item.setVisibility(View.GONE);
            holder.mlinearlayout_chair_visibility.setVisibility(View.VISIBLE);
            holder.mcommoditypay_fieldname.setText(childList.get(groupPosition).get(childPosition).getName());
            if (childList.get(groupPosition).get(childPosition).getDate() != null && childList.get(groupPosition).get(childPosition).getDate().length() > 0) {
                holder.mcommonditypay_field_start.setText(mcontext.getResources().getString(R.string.commoditypay_item_data_text)+
                        childList.get(groupPosition).get(childPosition).getDate());
            } else {
                holder.mcommonditypay_field_start.setText(mcontext.getResources().getString(R.string.commoditypay_item_data_text)+
                        mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (childList.get(groupPosition).get(childPosition).getRes_type_id() != null) {
                if (childList.get(groupPosition).get(childPosition).getRes_type_id().equals("1")) {
                    if (childList.get(groupPosition).get(childPosition).getField_type() != null && childList.get(groupPosition).get(childPosition).getField_type().length() > 0) {
                        holder.mcommodity_txt_fieldtitle.setVisibility(View.VISIBLE);
                        holder.mcommodity_txt_fieldtitle.setText(childList.get(groupPosition).get(childPosition).getField_type());
                    } else {
                        holder.mcommodity_txt_fieldtitle.setVisibility(View.GONE);
                    }
                } else if (childList.get(groupPosition).get(childPosition).getRes_type_id().equals("3")) {
                    if (childList.get(groupPosition).get(childPosition).getActivity_type() != null && childList.get(groupPosition).get(childPosition).getActivity_type().length() > 0) {
                        holder.mcommodity_txt_fieldtitle.setVisibility(View.VISIBLE);
                        holder.mcommodity_txt_fieldtitle.setText(childList.get(groupPosition).get(childPosition).getActivity_type());
                    } else {
                        holder.mcommodity_txt_fieldtitle.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.mcommodity_txt_fieldtitle.setVisibility(View.GONE);
            }

            if (childList.get(groupPosition).get(childPosition).getPic_url() != null) {
                if (childList.get(groupPosition).get(childPosition).getPic_url().length() > 0) {
                    Picasso.with(mcontext)
                            .load(childList.get(groupPosition).get(childPosition).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .resize(200, 200).into(holder.morder_img);
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.morder_img);
                }
            } else {
                Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.morder_img);
            }
            if (childList.get(groupPosition).get(childPosition).getSize() != null && childList.get(groupPosition).get(childPosition).getSize().length() > 0) {
                holder.mfield_shop_size.setText(mcontext.getResources().getString(R.string.order_listitem_sizetxt) + childList.get(groupPosition).get(childPosition).getSize());
                if (childList.get(groupPosition).get(childPosition).getLease_term_type() != null) {
                    if (childList.get(groupPosition).get(childPosition).getLease_term_type().length() > 0) {
                        holder.mfield_shop_size.setText(mcontext.getResources().getString(R.string.order_listitem_sizetxt) + childList.get(groupPosition).get(childPosition).getSize()+
                                "("+childList.get(groupPosition).get(childPosition).getLease_term_type()+")");
                    }
                }
            } else {
                holder.mfield_shop_size.setText(mcontext.getResources().getString(R.string.order_listitem_sizetxt) +
                        mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (childList.get(groupPosition).get(childPosition).getDeposit() != null) {
                holder.mCartDepositTV.setVisibility(View.VISIBLE);
                holder.mCartDepositTV.setText(mcontext.getResources().getString(R.string.orderconfirm_deposit_text) +
                        Constants.getdoublepricestring(childList.get(groupPosition).get(childPosition).getDeposit(),1) +
                        mcontext.getResources().getString(R.string.term_types_unit_txt));
            } else {
                holder.mCartDepositTV.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getCustom_dimension() != null && childList.get(groupPosition).get(childPosition).getCustom_dimension().length() > 0) {
                holder.mcommodity_custom_dimensiontxt.setText(childList.get(groupPosition).get(childPosition).getCustom_dimension());
                holder.mcommodity_custom_dimensiontxt.setVisibility(View.VISIBLE);
            } else {
                holder.mcommodity_custom_dimensiontxt.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getSubsidy_fee() != null &&
                    childList.get(groupPosition).get(childPosition).getSubsidy_fee().length() > 0 &&
                    Double.parseDouble(childList.get(groupPosition).get(childPosition).getSubsidy_fee()) > 0) {
                if (childList.get(groupPosition).get(childPosition).getPrice() != null &&
                        childList.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                    holder.morder_price.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getdoublepricestring(Double.parseDouble(childList.get(groupPosition).get(childPosition).getPrice())- Double.parseDouble(childList.get(groupPosition).get(childPosition).getSubsidy_fee()),
                                    0.01)),10));
                    holder.morder_subsidy_detailed_txt.setVisibility(View.VISIBLE);
                    holder.morder_subsidy_detailed_txt.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getpricestring(childList.get(groupPosition).get(childPosition).getPrice(), 0.01)),10));
                    holder.morder_subsidy_detailed_txt.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.morder_price.setText("");
                    holder.morder_subsidy_detailed_txt.setVisibility(View.GONE);
                }
            } else {
                holder.morder_subsidy_detailed_txt.setVisibility(View.GONE);
                if (childList.get(groupPosition).get(childPosition).getPrice() != null &&
                        childList.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                    holder.morder_price.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+ Constants.getpricestring(childList.get(groupPosition).get(childPosition).getPrice(), 0.01)),10));
                } else {
                    holder.morder_price.setText("");
                }
            }
            if (childList.get(groupPosition).get(childPosition).isValid()) {
                holder.mcheckbox.setEnabled(true);
                holder.mfield_shop_checkbox_grey.setVisibility(View.GONE);
                holder.mcheckbox.setVisibility(View.VISIBLE);
                holder.mcommoditypay_fieldname.setTextColor(mcontext.getResources().getColor(R.color.headline_tv_color));
                holder.mcommonditypay_field_start.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mfield_shop_size.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mcommodity_custom_dimensiontxt.setTextColor(mcontext.getResources().getColor(R.color.checked_tv_color));
                holder.morder_price.setTextColor(mcontext.getResources().getColor(R.color.default_redbg));
                holder.morder_subsidy_detailed_txt.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));

                holder.mcheckbox.setChecked(isSelected.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()));
                holder.mcheckbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if  (isSelected.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id())) {
                            isSelected.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(),false);
                            mactivity.mGroupCardList.get(groupPosition).setValid(false);
                            notifyDataSetChanged();
                            mactivity.getcheckdata(false, getprice(groupPosition), getsubsidy_fee(groupPosition),getorderpaylist_str(groupPosition));
                        } else {
                            isSelected.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(),true);
                            notifyDataSetChanged();
                            if (isChooseAllGroup(groupPosition)) {
                                mactivity.mGroupCardList.get(groupPosition).setValid(true);
                            } else {
                                mactivity.mGroupCardList.get(groupPosition).setValid(false);
                            }
                            mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));
                        }
                    }
                });
            } else {
                isfailure_resources = true;
                holder.mcheckbox.setEnabled(false);
                holder.mfield_shop_checkbox_grey.setVisibility(View.VISIBLE);
                holder.mcheckbox.setVisibility(View.INVISIBLE);

                holder.mcommonditypay_field_start.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mfield_shop_size.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mcommodity_custom_dimensiontxt.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.morder_price.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.morder_subsidy_detailed_txt.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mcommoditypay_fieldname.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mCartDaysInAdvanceTV.setVisibility(View.GONE);
                holder.mCartMinimumOrderQuantityTV.setVisibility(View.GONE);
                holder.mCartDepositTV.setVisibility(View.GONE);
            }

        } else if (childList.get(groupPosition).get(childPosition).getRes_type_id().equals("2")) {
            if (childPosition == childList.get(groupPosition).size() - 1) {
                holder.mShopCardAdvItemBottomView.setVisibility(View.GONE);
            } else {
                holder.mShopCardAdvItemBottomView.setVisibility(View.VISIBLE);
            }
            holder.madvertising_shop_item.setVisibility(View.VISIBLE);
            holder.mlinearlayout_chair_visibility.setVisibility(View.GONE);
            holder.madvertiding_shop_name.setText(childList.get(groupPosition).get(childPosition).getName());
            if (childList.get(groupPosition).get(childPosition).getPic_url() != null) {
                if (childList.get(groupPosition).get(childPosition).getPic_url().length() > 0) {
                    Picasso.with(mcontext)
                            .load(childList.get(groupPosition).get(childPosition).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .resize(200, 200).into(holder.madv_order_img);
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.madv_order_img);
                }
            } else {
                Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.madv_order_img);
            }
            if (childList.get(groupPosition).get(childPosition).getAd_type() != null && childList.get(groupPosition).get(childPosition).getAd_type().length() > 0) {
                holder.madv_commodity_txt_fieldtitle.setVisibility(View.VISIBLE);
                holder.madv_commodity_txt_fieldtitle.setText(childList.get(groupPosition).get(childPosition).getAd_type());
            } else {
                holder.madv_commodity_txt_fieldtitle.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getNumber_of_people() != null && childList.get(groupPosition).get(childPosition).getNumber_of_people().length() > 0) {
                if (Integer.parseInt(childList.get(groupPosition).get(childPosition).getNumber_of_people()) > 0) {
                    holder.madvertiding_shop_peoplenum.setVisibility(View.VISIBLE);
                    holder.madvertiding_shop_peoplenum.setText(mcontext.getResources().getString(R.string.fieldinfo_number_of_people_text)
                            +childList.get(groupPosition).get(childPosition).getNumber_of_people());
                } else {
                    holder.madvertiding_shop_peoplenum.setVisibility(View.GONE);
                }
            } else {
                holder.madvertiding_shop_peoplenum.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getOccupancy_rate() != null && childList.get(groupPosition).get(childPosition).getOccupancy_rate().length() > 0) {
                if (Integer.parseInt(childList.get(groupPosition).get(childPosition).getOccupancy_rate()) > 0) {
                    holder.moccupancy_rate.setVisibility(View.VISIBLE);
                    holder.moccupancy_rate.setText(mcontext.getResources().getString(R.string.txt_occupancy_ratetxt)
                            +childList.get(groupPosition).get(childPosition).getOccupancy_rate());
                } else {
                    holder.moccupancy_rate.setVisibility(View.GONE);
                }
            } else {
                holder.moccupancy_rate.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getAd_print_type() != null && childList.get(groupPosition).get(childPosition).getAd_print_type().length() > 0) {
                holder.madvertiding_shop_equipment.setVisibility(View.VISIBLE);
                holder.madvertiding_shop_equipment.setText(mcontext.getResources().getString(R.string.commoditypay_item_equipment_tv_str)
                        +childList.get(groupPosition).get(childPosition).getAd_print_type());
            } else {
                holder.madvertiding_shop_equipment.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getSize() != null) {
                holder.madvertising_shop_size.setText(mcontext.getResources().getString(R.string.order_listitem_sizetxt) + childList.get(groupPosition).get(childPosition).getSize());
            }
            if (childList.get(groupPosition).get(childPosition).getLease_term_type() != null) {
                if (childList.get(groupPosition).get(childPosition).getLease_term_type().length() > 0) {
                    holder.madvertising_shop_size.setText(mcontext.getResources().getString(R.string.order_listitem_sizetxt) + childList.get(groupPosition).get(childPosition).getSize()+
                            childList.get(groupPosition).get(childPosition).getLease_term_type());
                }
            }
            if (childList.get(groupPosition).get(childPosition).getAd_type() != null) {
                if (childList.get(groupPosition).get(childPosition).getAd_type().length() > 0) {
                    holder.mad_shop_type.setVisibility(View.VISIBLE);
                    holder.mad_shop_type.setText(childList.get(groupPosition).get(childPosition).getAd_type());
                } else {
                    holder.mad_shop_type.setVisibility(View.GONE);
                }
            }  else {
                holder.mad_shop_type.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getField_type() != null) {
                if (childList.get(groupPosition).get(childPosition).getField_type().length() > 0) {
                    holder.mad_shop_fieldtype.setVisibility(View.VISIBLE);
                    holder.mad_shop_fieldtype.setText(childList.get(groupPosition).get(childPosition).getField_type());
                }  else {
                    holder.mad_shop_fieldtype.setVisibility(View.GONE);
                }
            }  else {
                holder.mad_shop_fieldtype.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getDistrict() != null) {
                if (childList.get(groupPosition).get(childPosition).getDistrict().length() > 0) {
                    holder.mad_shop_adress.setVisibility(View.VISIBLE);
                    holder.mad_shop_adress.setText(childList.get(groupPosition).get(childPosition).getDistrict());
                } else {
                    holder.mad_shop_adress.setVisibility(View.GONE);
                }
            }  else {
                holder.mad_shop_adress.setVisibility(View.GONE);
            }
            if (childList.get(groupPosition).get(childPosition).getSubsidy_fee() != null &&
                    childList.get(groupPosition).get(childPosition).getSubsidy_fee().length() > 0 &&
                    Double.parseDouble(childList.get(groupPosition).get(childPosition).getSubsidy_fee()) > 0) {
                if (childList.get(groupPosition).get(childPosition).getPrice() != null &&
                        childList.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                    holder.madvertising_shop_price.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getdoublepricestring(Double.parseDouble(childList.get(groupPosition).get(childPosition).getPrice())- Double.parseDouble(childList.get(groupPosition).get(childPosition).getSubsidy_fee()),
                                    0.01)),10));
                    holder.madvertiding_shop_subsidy.setVisibility(View.VISIBLE);
                    holder.madvertiding_shop_subsidy.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getpricestring(childList.get(groupPosition).get(childPosition).getPrice(), 0.01)),10));
                    holder.madvertiding_shop_subsidy.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.madvertising_shop_price.setText("");
                    holder.madvertiding_shop_subsidy.setVisibility(View.GONE);
                }
            } else {
                holder.madvertiding_shop_subsidy.setVisibility(View.GONE);
                if (childList.get(groupPosition).get(childPosition).getPrice() != null &&
                        childList.get(groupPosition).get(childPosition).getPrice().length() > 0) {
                    holder.madvertising_shop_price.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+ Constants.getpricestring(childList.get(groupPosition).get(childPosition).getPrice(), 0.01)),10));
                } else {
                    holder.madvertising_shop_price.setText("");
                }
            }

            final Drawable drawable_down = mcontext.getResources().getDrawable(R.drawable.down_img);
            drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
            final Drawable drawable_down_pressed = mcontext.getResources().getDrawable(R.drawable.down_img_pressed);
            drawable_down_pressed.setBounds(0, 0, drawable_down_pressed.getMinimumWidth(), drawable_down_pressed.getMinimumHeight());
            Drawable drawable_add = mcontext.getResources().getDrawable(R.drawable.add_img);
            drawable_add.setBounds(0, 0, drawable_add.getMinimumWidth(), drawable_add.getMinimumHeight());
            Drawable drawable_add_pressed = mcontext.getResources().getDrawable(R.drawable.add_img_presseed);
            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
            if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) > 1) {
                holder.madvertising_shop_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
            } else {
                holder.madvertising_shop_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
            }
            if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) > 0) {
                if (childList.get(groupPosition).get(childPosition).getMaximum_count() > 0) {
                    if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) < childList.get(groupPosition).get(childPosition).getMaximum_count()) {
                        holder.madvertising_shop_mtw_add.setCompoundDrawables(drawable_add_pressed, null,null, null);
                    } else {
                        holder.madvertising_shop_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                    }
                } else {
                    holder.madvertising_shop_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                }
            } else {
                holder.madvertising_shop_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
            }
            if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) > 0) {
                holder.madvertising_shop_mtw_num.setText(String.valueOf(mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id())));
            } else {
                holder.madvertising_shop_mtw_num.setText("0");

            }


            if (childList.get(groupPosition).get(childPosition).isValid()) {
                holder.madvertising_shop_checkbox_grey.setVisibility(View.GONE);
                holder.madvertising_shop_chose.setVisibility(View.VISIBLE);
                holder.madvertising_shop_chose.setEnabled(true);
                holder.madvertising_shop_chose.setChecked(isSelected.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()));

                holder.madvertiding_shop_peoplenum.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.moccupancy_rate.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.madvertiding_shop_equipment.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.madvertising_shop_size.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                holder.mad_shop_type.setTextColor(mcontext.getResources().getColor(R.color.checked_tv_color));
                holder.mad_shop_type.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                holder.mad_shop_fieldtype.setTextColor(mcontext.getResources().getColor(R.color.checked_tv_color));
                holder.mad_shop_fieldtype.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                holder.mad_shop_adress.setTextColor(mcontext.getResources().getColor(R.color.checked_tv_color));
                holder.mad_shop_adress.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                holder.madvertising_shop_price.setTextColor(mcontext.getResources().getColor(R.color.default_redbg));
                holder.madvertising_shop_mtw_num.setEnabled(true);
                holder.madvertising_shop_mtw_add.setEnabled(true);
                holder.madvertising_shop_mtw_down.setEnabled(true);
                holder.madvertiding_shop_subsidy.setTextColor(mcontext.getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));

                holder.madvertising_shop_chose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if  (isSelected.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id())) {
                            isSelected.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(),false);
                            mactivity.mGroupCardList.get(groupPosition).setValid(false);
                            notifyDataSetChanged();
                            mactivity.getcheckdata(false, getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                        } else {
                            isSelected.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(),true);
                            if (isChooseAllGroup(groupPosition)) {
                                mactivity.mGroupCardList.get(groupPosition).setValid(true);
                            } else {
                                mactivity.mGroupCardList.get(groupPosition).setValid(false);
                            }
                            notifyDataSetChanged();
                            mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                        }

                    }
                });
                holder.madvertising_shop_mtw_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) > 1) {
                            mtw_num.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(), (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) - 1));
                            notifyDataSetChanged();
                            mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                        } else {

                        }
                    }
                });
                holder.madvertising_shop_mtw_down.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        item_position = childPosition;
                        item_group_position = groupPosition;
                        miusThread = new MiusThread();
                        isOnLongClick = true;
                        miusThread.start();
                        return true;
                    }
                });

                holder.madvertising_shop_mtw_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) > 0) {
                            if (childList.get(groupPosition).get(childPosition).getMaximum_count() > 0) {
                                if (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) < childList.get(groupPosition).get(childPosition).getMaximum_count())
                                    mtw_num.put(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id(), (mtw_num.get(childList.get(groupPosition).get(childPosition).getShopping_cart_item_id()) + 1));
                                notifyDataSetChanged();
                                mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                            }
                        }
                    }
                });
                holder.madvertising_shop_mtw_add.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        item_position = childPosition;
                        item_group_position = groupPosition;
                        plusThread = new PlusThread();
                        isOnLongClick = true;
                        plusThread.start();
                        return true;
                    }
                });
                addTextChangedListener(holder.madvertising_shop_mtw_num);
                OnFocusChangeListener(holder.madvertising_shop_mtw_num,groupPosition,childPosition);
            } else {
                isfailure_resources = true;
                holder.madvertising_shop_chose.setEnabled(false);
                holder.madvertising_shop_chose.setVisibility(View.INVISIBLE);
                holder.madvertising_shop_checkbox_grey.setVisibility(View.VISIBLE);

                holder.madvertiding_shop_name.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.madvertiding_shop_peoplenum.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.moccupancy_rate.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.madvertiding_shop_equipment.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.madvertising_shop_size.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mad_shop_type.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mad_shop_type.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_gray_bg));
                holder.mad_shop_fieldtype.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mad_shop_fieldtype.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_gray_bg));
                holder.mad_shop_adress.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mad_shop_adress.setBackgroundDrawable(mcontext.getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_gray_bg));
                holder.madvertising_shop_price.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.madvertising_shop_mtw_num.setText("1");
                holder.madvertising_shop_mtw_num.setEnabled(false);
                holder.madvertising_shop_mtw_add.setEnabled(false);
                holder.madvertising_shop_mtw_down.setEnabled(false);
                holder.madvertiding_shop_subsidy.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childList.get(groupPosition).get(childPosition).isValid()) {
                    Intent fieldinfointent = null;
                    if (Integer.parseInt(childList.get(groupPosition).get(childPosition).getRes_type_id()) != 2) {
                        fieldinfointent = new Intent(mcontext, FieldInfoActivity.class);
                        if (Integer.parseInt(childList.get(groupPosition).get(childPosition).getRes_type_id()) == 3) {
                            fieldinfointent.putExtra("is_sell_res", true);
                            fieldinfointent.putExtra("sell_res_id", String.valueOf(childList.get(groupPosition).get(childPosition).getSelling_resource_id()));
                        } else {
                            fieldinfointent.putExtra("fieldId", String.valueOf(childList.get(groupPosition).get(childPosition).getPhysical_resource_id()));
                        }
                    } else {
                        fieldinfointent = new Intent(mcontext, AdvertisingInfoActivity.class);
                        fieldinfointent.putExtra("fieldId", childList.get(groupPosition).get(childPosition).getResource_id());
                    }
                    fieldinfointent.putExtra("good_type", Integer.parseInt(childList.get(groupPosition).get(childPosition).getRes_type_id()));
                    mactivity.startActivity(fieldinfointent);

                } else {
                    MessageUtils.showToast(mcontext.getResources().getString(R.string.commoditypay_click_errormsg));
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mactivity.deleteshopcard(childPosition,childList.get(groupPosition).get(childPosition).getShopping_cart_item_id());

                return true;
            }
        });
        holder.mcommoditypay_delete_failure_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空失效资源
                mactivity.deleteshopcard(-1,"");
            }
        });
        if (groupPosition == groupList.size() - 1 &&
                childPosition == childList.get(groupPosition).size() - 1) {
            if (isfailure_resources) {
                holder.mcommoditypay_delete_failure_textview.setVisibility(View.VISIBLE);
            } else {
                holder.mcommoditypay_delete_failure_textview.setVisibility(View.GONE);
            }
        } else {
            holder.mcommoditypay_delete_failure_textview.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder
    {
        public TextView mcommoditypay_fieldname;
        public TextView mcommonditypay_field_start;
        public CheckBox mcheckbox;
        public TextView mfield_shop_checkbox_grey;
        public TextView mcommodity_txt_fieldtitle;
        public LinearLayout mlinearlayout_chair_visibility;
        public TextView morder_subsidy_detailed_txt;
        public TextView morder_price;
        public ImageView morder_img;
        public TextView mfield_shop_size;
        public TextView mcommodity_custom_dimensiontxt;
        public View mShopCardFieldItemBottomView;
        public View mShopCardFieldBottomView;
        public View mShopCardFieldTopView;

        public RelativeLayout madvertising_shop_item;
        public CheckBox madvertising_shop_chose;
        public TextView madv_commodity_txt_fieldtitle;
        public TextView madvertising_shop_checkbox_grey;
        public TextView madvertiding_shop_subsidy;
        public TextView madvertiding_shop_name;
        public TextView madvertiding_shop_peoplenum;
        public TextView madvertiding_shop_equipment;
        public TextView madvertising_shop_size;
        public TextView mad_shop_type;
        public TextView mad_shop_fieldtype;
        public TextView mad_shop_adress;
        public TextView madvertising_shop_mtw_down;
        public EditText madvertising_shop_mtw_num;
        public TextView madvertising_shop_mtw_add;
        public TextView madvertising_shop_price;
        public TextView moccupancy_rate;
        public ImageView madv_order_img;
        public View mShopCardAdvItemBottomView;

        public TextView mcommoditypay_delete_failure_textview;
        public TextView mCartDaysInAdvanceTV;
        public TextView mCartMinimumOrderQuantityTV;
        public TextView mCartDepositTV;
    }
    static class GroupViewHolder {
        public CheckBox mCartItemGroupCB;
        public TextView mCartItemGroupResNameTV;
        public LinearLayout mCartItemGroupLL;
    }
    public static HashMap<String, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<String, Boolean> isSelected) {
        CommoditypayAdapter.isSelected = isSelected;
    }
    public static void clear_isSelectedlist() {
        if (isSelected != null) {
            isSelected.clear();
        }
    }
    public  String getprice(int groupPosition) {
        double fieldprice = 0;
        String price = "";
        for (int i = 0; i < childList.get(groupPosition).size(); i ++) {
            if (isSelected.get(childList.get(groupPosition).get(i).getShopping_cart_item_id())) {
                if (childList.get(groupPosition).get(i).getPrice() != null &&
                        childList.get(groupPosition).get(i).getPrice().length() > 0) {
                    if (childList.get(groupPosition).get(i).getSubsidy_fee() != null &&
                            childList.get(groupPosition).get(i).getSubsidy_fee().length() > 0) {
                        if (childList.get(groupPosition).get(i).getRes_type_id().equals("2")) {
                            double SubsidyFee = (Double.parseDouble(childList.get(groupPosition).get(i).getSubsidy_fee().toString())) * mtw_num.get(childList.get(groupPosition).get(i).getShopping_cart_item_id());
                            double price_tem = (Double.parseDouble(childList.get(groupPosition).get(i).getPrice().toString())) * mtw_num.get(childList.get(groupPosition).get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice - SubsidyFee + price_tem;
                        } else if (childList.get(groupPosition).get(i).getRes_type_id().equals("1")  || childList.get(groupPosition).get(i).getRes_type_id().equals("3")) {
                            double  SubsidyFee= Double.parseDouble(childList.get(groupPosition).get(i).getSubsidy_fee().toString());
                            double price_tem = Double.parseDouble(childList.get(groupPosition).get(i).getPrice().toString());
                            fieldprice = fieldprice - SubsidyFee + price_tem;
                        }
                    } else {
                        if (childList.get(groupPosition).get(i).getRes_type_id().equals("2")) {
                            double price_tem = (Double.parseDouble(childList.get(groupPosition).get(i).getPrice().toString())) * mtw_num.get(childList.get(groupPosition).get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice + price_tem;
                        } else if (childList.get(groupPosition).get(i).getRes_type_id().equals("1") || childList.get(groupPosition).get(i).getRes_type_id().equals("3")) {
                            double price_tem = Double.parseDouble(childList.get(groupPosition).get(i).getPrice().toString());
                            fieldprice = fieldprice + price_tem;
                        }
                    }
                }
            }
        }
        price = (mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                Constants.getdoublepricestring(fieldprice,0.01));
        return price;
    }
    public  String getsubsidy_fee(int groupPosition) {
        double fieldprice = 0;
        String price = "";
        for (int i = 0; i < childList.get(groupPosition).size(); i ++) {
            if (isSelected.get(childList.get(groupPosition).get(i).getShopping_cart_item_id())) {
                if (childList.get(groupPosition).get(i).getSubsidy_fee() != null) {
                    if (childList.get(groupPosition).get(i).getSubsidy_fee().length() > 0) {
                        if (childList.get(groupPosition).get(i).getRes_type_id().equals("2")) {
                            double price_tem = (Double.parseDouble(childList.get(groupPosition).get(i).getSubsidy_fee().toString())) * mtw_num.get(childList.get(groupPosition).get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice + price_tem;
                        } else if (childList.get(groupPosition).get(i).getRes_type_id().equals("1")  || childList.get(groupPosition).get(i).getRes_type_id().equals("3")) {
                            double price_tem = Double.parseDouble(childList.get(groupPosition).get(i).getSubsidy_fee().toString());
                            fieldprice = fieldprice + price_tem;
                        }

                    }
                }
            }
        }
        price = mcontext.getResources().getString(R.string.commoditypay_subsidy_text)+ mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                Constants.getdoublepricestring(fieldprice,0.01);
        return price;
    }
    public  String getorderpaylist_str(int groupPosition) {
        int listsize = 0;
        String str_listsize ="";

        for (int i = 0; i < childList.get(groupPosition).size(); i ++) {
            if (isSelected.get(childList.get(groupPosition).get(i).getShopping_cart_item_id())) {
                listsize ++;
            }
        }
        if (listsize > 999) {
            str_listsize = mcontext.getResources().getString(R.string.commoditypay_payorder_text) + "(" + "999+" + ")";
        } else {
            str_listsize = mcontext.getResources().getString(R.string.commoditypay_payorder_text) + "("+String.valueOf(listsize)+")";
        }
        return str_listsize;
    }
    private boolean getcheckalltype(int groupPosition) {
        int check_true_num = 0;
        int checkallnum = mactivity.CommdityList.size() - mactivity.invalidCount;
        for (int i = 0; i < mactivity.CommdityList.size(); i++) {
            if (isSelected.get(mactivity.CommdityList.get(i).getShopping_cart_item_id())) {
                check_true_num++;
            } else {

            }
        }
        if (check_true_num == checkallnum) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isChooseAllGroup(int groupPosition) {
        int check_true_num = 0;
        int checkallnum = childList.get(groupPosition).size();
        for (int i = 0; i < childList.get(groupPosition).size(); i++) {
            if (isSelected.get(childList.get(groupPosition).get(i).getShopping_cart_item_id())) {
                check_true_num++;
            } else {

            }
        }
        if (check_true_num == checkallnum) {
            return true;
        } else {
            return false;
        }
    }

    public static void setMtw_num(HashMap<String, Integer> Mtw_Num) {
        CommoditypayAdapter.mtw_num = Mtw_Num;
    }
    public static HashMap<String, Integer> getMtw_num() {
        return mtw_num;
    }
    public static void clear_mtw_num() {
        if (mtw_num != null) {
            mtw_num.clear();
        }
    }
    //减操作
    class MiusThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i ++) {
                try {
                    Thread.sleep(300);
                    myHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    //加操作
    class PlusThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i ++) {
                try {
                    Thread.sleep(300);
                    myHandler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    //更新文本框的值
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (miusEnable) {
                        mtw_num.put(childList.get(item_group_position).get(item_position).getShopping_cart_item_id(), (mtw_num.get(childList.get(item_group_position).get(item_position).getShopping_cart_item_id()) - 1));
                        notifyDataSetChanged();
                        mactivity.getcheckdata(getcheckalltype(item_group_position), getprice(item_group_position), getsubsidy_fee(item_group_position), getorderpaylist_str(item_group_position));

                    }
                    break;
                case 2:
                    if (plusEnable) {
                        mtw_num.put(childList.get(item_group_position).get(item_position).getShopping_cart_item_id(), (mtw_num.get(childList.get(item_group_position).get(item_position).getShopping_cart_item_id()) + 1));
                        notifyDataSetChanged();
                        mactivity.getcheckdata(getcheckalltype(item_group_position), getprice(item_group_position), getsubsidy_fee(item_group_position), getorderpaylist_str(item_group_position));

                    }
                    break;
            }
            setBtnEnable();
        }
    };

    //超出最大最小值范围按钮的可能与不可用
    private void setBtnEnable() {

        if (mtw_num != null && mtw_num.size() > 1 && mtw_num.size() > item_position &&
                mtw_num.get(childList.get(item_group_position).get(item_position).getShopping_cart_item_id()) > 1) {
            miusEnable = true;
        } else {
            miusEnable = false;
        }
        if (mtw_num != null && mtw_num.size() > 0 && mtw_num.size() > item_position &&
                mtw_num.get(childList.get(item_group_position).get(item_position).getShopping_cart_item_id()) < childList.get(item_group_position).get(item_position).getMaximum_count()) {
            plusEnable = true;
        } else {
            plusEnable = false;
        }
    }
    private void OnTouchListener(final EditText editText, final int type) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (type == 1) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        plusThread = new PlusThread();
                        isOnLongClick = true;
                        plusThread.start();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (plusThread != null) {
                            isOnLongClick = false;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (plusThread != null) {
                            isOnLongClick = true;
                        }
                    }
                } else if (type == -1) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        miusThread = new MiusThread();
                        isOnLongClick = true;
                        miusThread.start();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (miusThread != null) {
                            isOnLongClick = false;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (miusThread != null) {
                            isOnLongClick = true;
                        }
                    }
                }

                return true;
            }
        });
    }
    private void addTextChangedListener(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    editText.clearFocus();
                }
            }
        });

    }
    private void OnFocusChangeListener(final EditText editText,final int groupPosition,final int position) {
        editText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                } else {
                    if (editText.getText().toString().length() > 0) {
                        if (Integer.parseInt(editText.getText().toString()) > childList.get(groupPosition).get(position).getMaximum_count()) {
                            mtw_num.put(childList.get(groupPosition).get(position).getShopping_cart_item_id(), childList.get(groupPosition).get(position).getMaximum_count());
                            notifyDataSetChanged();
                            mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                        } else {
                            mtw_num.put(childList.get(groupPosition).get(position).getShopping_cart_item_id(), Integer.parseInt(editText.getText().toString()));
                            notifyDataSetChanged();
                            mactivity.getcheckdata(getcheckalltype(groupPosition), getprice(groupPosition), getsubsidy_fee(groupPosition), getorderpaylist_str(groupPosition));

                        }
                    } else {
                        MessageUtils.showToast("请输入");
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                notifyDataSetChanged();
                            }
                        }, 4000);

                    }
                }
            }
        });

    }
}
