package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fragment.Field_HomeFragment;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fragment.Field_OrdersFragment;
import com.linhuiba.linhuifield.fieldmodel.Field_OrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class Field_OrdersAdapter extends BaseAdapter {
    private List<Field_OrderModel> grouplistdata;
    private Context mcontext;
    private Field_OrdersFragment mactivity;
    private Field_HomeFragment field_homeFragment;
    private LayoutInflater mInflater = null;
    private int type;
    public Field_OrdersAdapter(Context cxt, Field_OrdersFragment activity, List<Field_OrderModel> data, int type) {
        if (cxt != null && activity != null) {
            this.mInflater = LayoutInflater.from(cxt);
            this.mcontext = cxt;
            this.grouplistdata =data;
            this.mactivity = activity;
            this.type = type;
        }
    }
    public Field_OrdersAdapter(Context cxt, Field_HomeFragment activity, List<Field_OrderModel> data, int type) {
        if (cxt != null && activity != null) {
            this.mInflater = LayoutInflater.from(cxt);
            this.mcontext = activity.getContext();
            this.grouplistdata =data;
            this.field_homeFragment = activity;
            this.type = type;
        }
    }
    @Override
    public int getCount() {
        if (grouplistdata != null) {
            return grouplistdata.size();
        } else {
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
        ViewHolderOrder holder = null;
        if (convertView == null) {
            holder = new ViewHolderOrder();
            convertView = mInflater.inflate(R.layout.field_fragment_ordersfragment_listitem, null);
            holder.morder_title_value = (TextView)convertView.findViewById(R.id.order_title_value);
            holder.morder_connect_value = (TextView)convertView.findViewById(R.id.order_connect_value);
            holder.morder_mobile_value = (TextView)convertView.findViewById(R.id.order_mobile_value);
            holder.morder_mobile_layout = (LinearLayout)convertView.findViewById(R.id.order_mobile_layout);
            holder.mcompany_name = (TextView)convertView.findViewById(R.id.company_name);
            holder.mleave_message = (TextView)convertView.findViewById(R.id.order_leave_message_text);
            holder.mfield_time_value = (TextView)convertView.findViewById(R.id.field_time_value);
            holder.mfieldorder_size = (TextView)convertView.findViewById(R.id.fieldorder_size);
            holder.mlinearlayout_agree = (LinearLayout)convertView.findViewById(R.id.userinfo_layout_margin);
            holder.mbtnagree = (TextView)convertView.findViewById(R.id.btnagree);
            holder.mbtnrefuse = (TextView)convertView.findViewById(R.id.btnrefuse);
            holder.mbottom_view = (View)convertView.findViewById(R.id.bottom_view);
            holder.mfieldorder_price_new = (TextView)convertView.findViewById(R.id.fieldorder_price_new);
            holder.morder_business_mobile = (ImageButton)convertView.findViewById(R.id.order_business_mobile);
            holder.morder_business_mobile_view = (View) convertView.findViewById(R.id.order_business_mobile_view);
            holder.morder_business_companyinfo = (ImageButton)convertView.findViewById(R.id.order_business_companyinfo);
            holder.mOrderInfoRL = (RelativeLayout) convertView.findViewById(R.id.field_order_rl);
            holder.mOrderBusinessInfoLL = (LinearLayout) convertView.findViewById(R.id.field_order_business_info_ll);
            holder.msearchlist_txt_fieldtitle = (TextView)convertView.findViewById(R.id.searchlist_txt_fieldtitle);
            holder.mfield_order_resource_img = (ImageView) convertView.findViewById(R.id.field_order_resource_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolderOrder)convertView.getTag();
        }
        if (grouplistdata.get(position).getResource_name() != null && grouplistdata.get(position).getResource_name().length() > 0) {
            holder.morder_title_value.setText(grouplistdata.get(position).getResource_name());
        } else {
            holder.morder_title_value.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getContact() != null && grouplistdata.get(position).getContact().length() > 0) {
            holder.morder_connect_value.setText(grouplistdata.get(position).getContact());
        } else {
            holder.morder_connect_value.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getMobile() != null && grouplistdata.get(position).getMobile().length() > 0 &&
                type == 1) {
            holder.morder_mobile_value.setText(grouplistdata.get(position).getMobile());
        } else {
            holder.morder_mobile_value.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getCompany() != null && grouplistdata.get(position).getCompany().length() > 0) {
            holder.mcompany_name.setText(grouplistdata.get(position).getCompany());
        } else {
            holder.mcompany_name.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getLeave_words() != null && grouplistdata.get(position).getLeave_words().length() > 0) {
            holder.mleave_message.setText(grouplistdata.get(position).getLeave_words());
        } else {
            holder.mleave_message.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getSize() != null && grouplistdata.get(position).getSize().length() > 0) {
            holder.mfieldorder_size.setText(grouplistdata.get(position).getSize());
            if (grouplistdata.get(position).getLease_term_type() != null && grouplistdata.get(position).getLease_term_type().length() > 0) {
                holder.mfieldorder_size.setText(grouplistdata.get(position).getSize()+"("+grouplistdata.get(position).getLease_term_type()+")");
            }
        } else {
            holder.mfieldorder_size.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getTotal_fee() != null && grouplistdata.get(position).getTotal_fee().length() > 0) {
            holder.mfieldorder_price_new.setText(Constants.getPriceUnitStr(mcontext,(mcontext.getResources().getString(R.string.order_field_listitem_price_unit_text)+
                    Constants.getpricestring(grouplistdata.get(position).getTotal_fee(),0.01)),10));
        } else {
            holder.mfieldorder_price_new.setText("");
        }
        if (grouplistdata.get(position).getStart() != null) {
            holder.mfield_time_value.setText(grouplistdata.get(position).getStart()+" " + Constants.dayForWeek(grouplistdata.get(position).getStart().toString()));
        } else {
            holder.mfield_time_value.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (grouplistdata.get(position).getField_type() != null &&
                grouplistdata.get(position).getField_type().length() > 0) {
            holder.msearchlist_txt_fieldtitle.setVisibility(View.VISIBLE);
            holder.msearchlist_txt_fieldtitle.setText(grouplistdata.get(position).getField_type());
        } else {
            holder.msearchlist_txt_fieldtitle.setVisibility(View.GONE);
        }
        if (grouplistdata.get(position).getPic_url() != null) {
            if (grouplistdata.get(position).getPic_url().length() != 0) {
                Picasso.with(mcontext).load(grouplistdata.get(position).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(180, 180).into(holder.mfield_order_resource_img);
            } else {
                Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).resize(180, 180).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.mfield_order_resource_img);
            }
        } else {
            Picasso.with(mcontext).load(R.drawable.ic_no_pic_small).resize(180, 180).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.mfield_order_resource_img);
        }
        if (type == 1) {
            holder.morder_business_mobile_view.setVisibility(View.VISIBLE);
            holder.morder_business_mobile.setVisibility(View.VISIBLE);
            holder.morder_business_mobile.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mactivity != null &&
                            field_homeFragment == null) {
                        mactivity.mAdapterPhoneNum = grouplistdata.get(position).getMobile();
                        mactivity.mFieldOrderPresenter.getVirtualNumber(grouplistdata.get(position).getOrder_item_id(),2);
                    } else if (mactivity == null &&
                            field_homeFragment != null) {
                        field_homeFragment.mAdapterPhoneNum = grouplistdata.get(position).getMobile();
                        field_homeFragment.mFieldOrderPresenter.getVirtualNumber(grouplistdata.get(position).getOrder_item_id(),2);
                    }
                }
            });

        } else {
            holder.morder_business_mobile_view.setVisibility(View.GONE);
            holder.morder_business_mobile.setVisibility(View.GONE);
        }
        holder.mOrderBusinessInfoLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent help = new Intent("com.business.aboutActivity");
                help.putExtra("type",com.linhuiba.linhuifield.config.Config.BUSINESS_COMPANY_WEB_INT);
                help.putExtra("urlstr","business_id="+grouplistdata.get(position).getUser_id()+"&field_order_id="+grouplistdata.get(position).getOrder_id()+"&user_id="+ LoginManager.getUid());
                mcontext.startActivity(help);
            }
        });
        if (position == grouplistdata.size() - 1) {
            holder.mbottom_view.setVisibility(View.GONE);
        } else {
            holder.mbottom_view.setVisibility(View.VISIBLE);
        }
        if (grouplistdata.get(position).getStatus_name() != null) {
            if (grouplistdata.get(position).getStatus_name().equals(mcontext.getResources().getString(R.string.myselfinfo_check))) {
                holder.mlinearlayout_agree.setVisibility(View.VISIBLE);
                holder.morder_mobile_layout.setVisibility(View.GONE);
                holder.morder_mobile_value.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            } else {
                holder.mlinearlayout_agree.setVisibility(View.GONE);
                holder.morder_mobile_layout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mlinearlayout_agree.setVisibility(View.GONE);
            holder.morder_mobile_layout.setVisibility(View.GONE);
        }
        holder.mbtnagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mactivity != null && field_homeFragment == null) {
                    mactivity.editoraddress(1,position,grouplistdata.get(position).getOrder_item_id());
                } else if (mactivity == null && field_homeFragment != null) {
                    field_homeFragment.editoraddress(1,position,grouplistdata.get(position).getOrder_item_id());
                }
            }
        });
        holder.mbtnrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mactivity != null && field_homeFragment == null) {
                    mactivity.editoraddress(0,position,grouplistdata.get(position).getOrder_item_id());
                } else if (mactivity == null && field_homeFragment != null) {
                    field_homeFragment.editoraddress(0,position,grouplistdata.get(position).getOrder_item_id());
                }
            }
        });
        holder.mOrderInfoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent("com.business.aboutActivity");
                help.putExtra("type", com.linhuiba.linhuifield.config.Config.ORDER_INFO_INT);
                help.putExtra("id",grouplistdata.get(position).getOrder_item_id());
                mcontext.startActivity(help);
            }
        });
        return convertView;
    }
    static class ViewHolderOrder
    {
        public TextView morder_title_value;
        public TextView morder_connect_value;
        public TextView morder_mobile_value;
        public LinearLayout morder_mobile_layout;
        public TextView mcompany_name;
        public TextView mleave_message;
        public TextView mfield_time_value;
        public TextView mfieldorder_size;
        public TextView mfieldorder_price_new;
        public ImageButton morder_business_mobile;
        public View morder_business_mobile_view;
        public ImageButton morder_business_companyinfo;
        public RelativeLayout mOrderInfoRL;
        public LinearLayout mOrderBusinessInfoLL;
        public LinearLayout mlinearlayout_agree;
        public TextView mbtnagree;
        public TextView mbtnrefuse;
        public View mbottom_view;
        public ImageView mfield_order_resource_img;
        public TextView msearchlist_txt_fieldtitle;
    }

}
