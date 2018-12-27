package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/15.
 */

public class FieldAddFieldSearchResAdapter extends BaseAdapter {
    private ArrayList<AddfieldSearchCommunityModule> templateResourceModels = new ArrayList<>();
    private LayoutInflater layoutInflater = null;
    private Context context;
    public FieldAddFieldSearchResAdapter(Context context, ArrayList<AddfieldSearchCommunityModule> templateResourceModels) {
        this.context = context;
        this.templateResourceModels = templateResourceModels;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return templateResourceModels.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.field_activity_field_addfield_template_listitem, null);
            holder.mtemplate_search_list_item_text = (TextView)convertView.findViewById(R.id.template_search_list_item_text);
            holder.mAddressTV = (TextView)convertView.findViewById(R.id.addfield_search_res_address_tv);
            holder.mtemplate_search_list_item_view  = (View) convertView.findViewById(R.id.template_search_list_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == templateResourceModels.size() - 1) {
            holder.mtemplate_search_list_item_view.setVisibility(View.GONE);
        } else {
            holder.mtemplate_search_list_item_view.setVisibility(View.VISIBLE);
        }
        if (templateResourceModels.get(position).getCommunity_data().getName() != null &&
                templateResourceModels.get(position).getCommunity_data().getName().length() > 0) {
            holder.mtemplate_search_list_item_text.setText(templateResourceModels.get(position).getCommunity_data().getName());
        }
        String addressStr = "";
        if (templateResourceModels.get(position).getCommunity_data().getCity() != null &&
                templateResourceModels.get(position).getCommunity_data().getCity().getName() != null) {
            addressStr = addressStr + templateResourceModels.get(position).getCommunity_data().getCity().getName();
        }
        if (templateResourceModels.get(position).getCommunity_data().getDistrict() != null &&
                templateResourceModels.get(position).getCommunity_data().getDistrict().getName() != null) {
            addressStr = addressStr + templateResourceModels.get(position).getCommunity_data().getDistrict().getName();
        }
        if (templateResourceModels.get(position).getCommunity_data().getAddress() != null &&
                templateResourceModels.get(position).getCommunity_data().getAddress().length() > 0) {
            addressStr = addressStr + templateResourceModels.get(position).getCommunity_data().getAddress();
        }
        holder.mAddressTV.setText(addressStr);

        return convertView;
    }
    static class ViewHolder {
        TextView mtemplate_search_list_item_text;
        TextView mAddressTV;
        View mtemplate_search_list_item_view;
    }

}
