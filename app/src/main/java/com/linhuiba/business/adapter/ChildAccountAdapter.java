package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.ChildAccountActivity;
import com.linhuiba.business.model.ChildAccountModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ChildAccountAdapter extends BaseAdapter {
    private ArrayList<ChildAccountModel> datalist = new ArrayList<>();
    private Context mcontext;
    private ChildAccountActivity mactivity;
    private LayoutInflater mInflater = null;
    public ChildAccountAdapter (Context context,ChildAccountActivity activity,ArrayList<ChildAccountModel> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.datalist = datas;
    }

    @Override
    public int getCount() {
        if(datalist != null){
            return datalist.size();
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
            convertView = mInflater.inflate(R.layout.activity_childaccount_listitem, null);
            holder.mcontact = (TextView)convertView.findViewById(R.id.childaccount_contact);
            holder.mmobile = (TextView)convertView.findViewById(R.id.childaccount_mobile);
            holder.mdeletebtn = (Button) convertView.findViewById(R.id.childaccount_delete_button);
            holder.mchildaccount_item_view = (View)convertView.findViewById(R.id.childaccount_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (position == datalist.size() - 1) {
            holder.mchildaccount_item_view.setVisibility(View.GONE);
        } else {
            holder.mchildaccount_item_view.setVisibility(View.VISIBLE);
        }
        if (datalist.get(position).getName() != null && datalist.get(position).getName().length() > 0) {
            holder.mcontact.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext) +datalist.get(position).getName());
        } else {
            holder.mcontact.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext) +
            mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (datalist.get(position).getMobile() != null && datalist.get(position).getMobile().length() > 0) {
            holder.mmobile.setText(mactivity.getResources().getString(R.string.txt_field_mobile) +datalist.get(position).getMobile());
        } else {
            holder.mmobile.setText(mactivity.getResources().getString(R.string.txt_field_mobile) +
                    mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        holder.mdeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datalist.get(position).getId() != null && datalist.get(position).getId().length() > 0)
                mactivity.delete_childaccount(position,datalist.get(position).getId());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mactivity.changeadmin == 1) {
                    Intent changeadmin = new Intent();
                    changeadmin.putExtra("accounid",datalist.get(position).getId());
                    changeadmin.putExtra("accounname",datalist.get(position).getName());
                    changeadmin.putExtra("accounmobile",datalist.get(position).getMobile());
                    mactivity.setResult(1,changeadmin);
                    mactivity.finish();
                }
            }
        });
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mcontact;
        public TextView mmobile;
        public Button mdeletebtn;
        public View mchildaccount_item_view;
    }

}
