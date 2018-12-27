package com.linhuiba.linhuifield.fieldadapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldactivity.Field_OrderRefuseActivity;
import com.linhuiba.linhuifield.fragment.Field_ResourceCalendarFragment;
import com.linhuiba.linhuifield.fieldmodel.ScheduleCalendarlistModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ResourceCalendarMyListviewAdapter extends BaseAdapter {
    private ArrayList<ScheduleCalendarlistModel> data = new ArrayList<>();
    private Context mcontext;
    private Field_ResourceCalendarFragment mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    public ResourceCalendarMyListviewAdapter (Context context, Field_ResourceCalendarFragment activity, ArrayList<ScheduleCalendarlistModel> datas, int invoicetype) {
       if (context != null && activity != null) {
           this.mInflater = LayoutInflater.from(context);
           this.mcontext = context;
           this.mactivity = activity;
           this.data = datas;
           this.type = invoicetype;
       }
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
        return data.get(position);
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
            convertView = mInflater.inflate(R.layout.activity_resourcecalendarlistitem, null);
            holder.mresourcename = (TextView)convertView.findViewById(R.id.resourcename);
            holder.mresourceprice = (TextView)convertView.findViewById(R.id.resourceprice);
            holder.mresourcepriceunit = (TextView)convertView.findViewById(R.id.resourcepriceunit);
            holder.mresourcecalendar_tellphonebtn = (ImageButton)convertView.findViewById(R.id.resourcecalendar_tellphonebtn);
            holder.mresource_businessname = (TextView)convertView.findViewById(R.id.resource_businessname);
            holder.mresourcesize = (TextView)convertView.findViewById(R.id.resourcesize);
            holder.mrespurcecalendar_througn_btn = (Button)convertView.findViewById(R.id.respurcecalendar_througn_btn);
            holder.mrespurcecalendar_refused_btn = (Button)convertView.findViewById(R.id.respurcecalendar_refused_btn);
            holder.mrespurcecalendar_statu_btn = (TextView)convertView.findViewById(R.id.respurcecalendar_statu_btn);
            holder.mbtn_layout = (LinearLayout)convertView.findViewById(R.id.btn_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 0) {

        }
        holder.mresourcename.setText(data.get(position).getResource_name().toString());
        if (data.get(position).getTotal_fee() != null) {
            if (data.get(position).getTotal_fee().length() > 0) {
                holder.mresourceprice.setText(mactivity.getResources().getString(R.string.order_field_listitem_price_unit_text)+
                        Constants.getpricestring(data.get(position).getTotal_fee(),0.01));
            }
        }
        if (data.get(position).getSize() != null) {
            if (data.get(position).getSize().length() > 0) {
                holder.mresourcesize.setText("规格："+ data.get(position).getSize());
            }
        }
        if (data.get(position).getLease_term_type() != null) {
            if (data.get(position).getLease_term_type().length() > 0) {
                holder.mresourcesize.setText("规格："+ data.get(position).getSize()+"("+data.get(position).getLease_term_type()+")");
            }
        }
        if (data.get(position).getCompany() != null) {
            if (data.get(position).getCompany().length() > 0) {
                holder.mresource_businessname.setText("商家："+ data.get(position).getCompany());
            }
        }
        if (data.get(position).getStatus() != null) {
            if (data.get(position).getStatus().length() > 0) {
                holder.mrespurcecalendar_statu_btn.setVisibility(View.VISIBLE);
                holder.mrespurcecalendar_statu_btn.setText(data.get(position).getStatus());
                if (data.get(position).getStatus().equals("待审核")) {
                    holder.mbtn_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.mbtn_layout.setVisibility(View.GONE);
                }
            } else {
                holder.mbtn_layout.setVisibility(View.GONE);
                holder.mrespurcecalendar_statu_btn.setVisibility(View.GONE);
            }
        } else {
            holder.mbtn_layout.setVisibility(View.GONE);
            holder.mrespurcecalendar_statu_btn.setVisibility(View.GONE);
        }
        holder.mrespurcecalendar_refused_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mactivity.getconfiguratenew(1,position,data.get(position).getOrder_item_id());
            }
        });
        holder.mrespurcecalendar_througn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mactivity.getconfiguratenew(0,position,data.get(position).getOrder_item_id());
            }
        });

        return convertView;
    }
    static class ViewHolder
    {
        public TextView mresourcename;
        public TextView mresourceprice;
        public TextView mresourcepriceunit;
        public ImageButton mresourcecalendar_tellphonebtn;
        public TextView mresource_businessname;
        public TextView mresourcesize;
        public Button mrespurcecalendar_througn_btn;
        public Button mrespurcecalendar_refused_btn;
        public TextView mrespurcecalendar_statu_btn;
        public LinearLayout mbtn_layout;
    }
}
