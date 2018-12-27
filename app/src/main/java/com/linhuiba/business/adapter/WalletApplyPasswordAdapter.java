package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.linhuiba.business.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/16.
 */

public class WalletApplyPasswordAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,String>> passwordlist = new ArrayList<>();
    private Context mcontext;
    private Activity mactivity;
    private LayoutInflater mInflater = null;
    public WalletApplyPasswordAdapter (Activity activity,Context context, ArrayList<HashMap<String,String>> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.passwordlist = datas;
    }
    @Override
    public int getCount() {
        return passwordlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_walletapply_enter_password_pw_gridview_item, null);
            holder.mwallet_password_num_text = (TextView) convertView.findViewById(R.id.wallet_password_num_text);
            holder.mwallet_password_num_imagebutton = (LinearLayout) convertView.findViewById(R.id.wallet_password_num_imagebutton);
            holder.mwalletapply_item_alllayout = (LinearLayout) convertView.findViewById(R.id.walletapply_item_alllayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        if(position == 9){
            holder.mwallet_password_num_imagebutton.setVisibility(View.GONE);
            holder.mwallet_password_num_text.setVisibility(View.VISIBLE);
            holder.mwallet_password_num_text.setBackgroundResource(R.color.new_viewbg);
            holder.mwallet_password_num_text.setEnabled(false);
            holder.mwallet_password_num_text.setText("");
        } else if(position == 11){
            holder.mwallet_password_num_imagebutton.setVisibility(View.VISIBLE);
            holder.mwallet_password_num_text.setVisibility(View.GONE);
            holder.mwallet_password_num_text.setBackgroundResource(R.color.new_viewbg);
        } else {
            holder.mwallet_password_num_imagebutton.setVisibility(View.GONE);
            holder.mwallet_password_num_text.setVisibility(View.VISIBLE);
            holder.mwallet_password_num_text.setText(passwordlist.get(position).get("name"));
        }
        return convertView;
    }
    static class ViewHolder {
        public TextView mwallet_password_num_text;
        public LinearLayout mwallet_password_num_imagebutton;
        public LinearLayout mwalletapply_item_alllayout;
    }
}
