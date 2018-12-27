package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.fieldactivity.ChildAccountManagementActivity;
import com.linhuiba.linhuifield.fieldactivity.ChildAccountManagementEditorActivity;
import com.linhuiba.linhuifield.fieldmodel.ChildAccountManagementModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ChildAccountManagementAdapter extends BaseAdapter {
    private ArrayList<ChildAccountManagementModel> data = new ArrayList<>();
    private Context mcontext;
    private ChildAccountManagementActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    public ChildAccountManagementAdapter (Context context, ChildAccountManagementActivity activity, ArrayList<ChildAccountManagementModel> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
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
            convertView = mInflater.inflate(R.layout.activity_childaccountmanagement_listitem, null);
            holder.mchildaccountname = (TextView)convertView.findViewById(R.id.childaccountname);
            holder.mchildaccountphone = (TextView)convertView.findViewById(R.id.childaccountphone);
            holder.mchildaccountresourcestxt = (TextView)convertView.findViewById(R.id.childaccountresourcestxt);
            holder.mchildaccountresourcesname = (TextView)convertView.findViewById(R.id.childaccountresourcesname);
            holder.mdelete_childaccount = (Button)convertView.findViewById(R.id.delete_childaccount);
            holder.meditor_childaccount = (Button)convertView.findViewById(R.id.editor_childaccount);
            holder.mnotice_guard_layout  = (LinearLayout)convertView.findViewById(R.id.field_notice_guard_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (position == data.size() - 1) {
            holder.mnotice_guard_layout.setVisibility(View.VISIBLE);
            holder.mnotice_guard_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent account = new Intent("com.business.aboutActivity");
                    account.putExtra("type", Config.GUARD_WEB_INT);
                    mactivity.startActivity(account);
                }
            });
        } else {
            holder.mnotice_guard_layout.setVisibility(View.GONE);
        }
        if (data.get(position).getName() != null && data.get(position).getName().length() > 0) {
            holder.mchildaccountname.setText(mactivity.getResources().getString(R.string.childaccount_name_text)+
                    data.get(position).getName().toString());
        } else {
            holder.mchildaccountname.setText(mactivity.getResources().getString(R.string.childaccount_name_text)+
                    mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (data.get(position).getMobile() != null && data.get(position).getMobile().length() > 0) {
            holder.mchildaccountphone.setText(mactivity.getResources().getString(R.string.childaccount_mobile_text)+
                    data.get(position).getMobile().toString());
        } else {
            holder.mchildaccountphone.setText(mactivity.getResources().getString(R.string.childaccount_mobile_text)+
                    mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (data.get(position).getRes_ids() != null) {
            String resourcesname = "";
            for (int i = 0; i < data.get(position).getRes_ids().size(); i++) {
                if (i != data.get(position).getRes_ids().size()-1) {
                    resourcesname = resourcesname +  data.get(position).getRes_ids().get(i).get("name").toString() +" / ";
                } else {
                    resourcesname = resourcesname +  data.get(position).getRes_ids().get(i).get("name").toString();
                }
            }
            holder.mchildaccountresourcesname.setText(resourcesname);
        }

        holder.mdelete_childaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mactivity.deleteinvoicelistitem(position, data.get(position).getId());
            }
        });
        holder.meditor_childaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editchildaccountintent = new Intent(mactivity, ChildAccountManagementEditorActivity.class);
                editchildaccountintent.putExtra("itemdata",(Serializable)data.get(position));
                editchildaccountintent.putExtra("type",1);
                editchildaccountintent.putExtra("resListmap",(Serializable)mactivity.resListmap);
                mactivity.startActivity(editchildaccountintent);
            }
        });

        return convertView;
    }
    static class ViewHolder
    {
        public TextView mchildaccountname;
        public TextView mchildaccountphone;
        public TextView mchildaccountresourcestxt;
        public TextView mchildaccountresourcesname;
        public Button mdelete_childaccount;
        public Button meditor_childaccount;
        public LinearLayout mnotice_guard_layout;
    }
}
