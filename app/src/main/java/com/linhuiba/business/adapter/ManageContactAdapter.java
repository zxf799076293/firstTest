package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.InvoiceInfoEditorAddress;
import com.linhuiba.business.activity.ManageContactActivity;
import com.linhuiba.business.model.AddressContactModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ManageContactAdapter extends BaseAdapter {
    private Context mcontext;
    private ManageContactActivity mactivity;
    private LayoutInflater mInflater = null;
    private ArrayList<AddressContactModel> data = new ArrayList<>();
    private int type;
    private static HashMap<String, Boolean> isSelected_managecontact = new HashMap<String, Boolean>();
    public ManageContactAdapter(Context context,ManageContactActivity activity,ArrayList<AddressContactModel> datas, int types) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = types;
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
            convertView = mInflater.inflate(R.layout.activity_managecontact_listitem, null);
            holder.mcontact_view_layout = (LinearLayout)convertView.findViewById(R.id.contact_view_layout);
            holder.mname = (TextView)convertView.findViewById(R.id.managecontact_name);
            holder.mmoblie = (TextView)convertView.findViewById(R.id.managecontact_mobile);
            holder.mmanagecontact_address = (TextView)convertView.findViewById(R.id.managecontact_address);
            holder.maddress_checkbox = (CheckBox)convertView.findViewById(R.id.managecontact_checkbox);
            holder.meditortxt = (Button)convertView.findViewById(R.id.managecontact_editor);
            holder.mdelete = (Button)convertView.findViewById(R.id.managecontact_deletetxt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 0) {
            if (position == data.size() - 1) {
                holder.mcontact_view_layout.setVisibility(View.GONE);
            } else {
                holder.mcontact_view_layout.setVisibility(View.VISIBLE);
            }
            if (data.get(position).getMobile() != null && data.get(position).getMobile().length() > 0) {
                holder.mmoblie.setText(data.get(position).getMobile());
            } else {
                holder.mmoblie.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (data.get(position).getAddress() != null && data.get(position).getAddress().length() > 0) {
                holder.mmanagecontact_address.setText(mactivity.getResources().getString(R.string.field_list_item_address)+
                        data.get(position).getProvince()+data.get(position).getCity()+data.get(position).getDistrict()+
                        data.get(position).getAddress());
            } else {
                holder.mmanagecontact_address.setText(mactivity.getResources().getString(R.string.field_list_item_address)+
                        mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (data.get(position).getName() != null && data.get(position).getName().length() > 0) {
                holder.mname.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext)+
                        data.get(position).getName());
            } else {
                holder.mname.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext)+
                        mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }

            holder.meditortxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editoraddress = new Intent(mactivity, InvoiceInfoEditorAddress.class);
                    editoraddress.putExtra("Operationtype", mactivity.EDIT_ACCOUNT_TYPE);
                    if (mactivity.mGetIntentInt == mactivity.ORDER_CHOOSE_ACCOUNT_TYPE) {
                        editoraddress.putExtra("order_choose", mactivity.ORDER_CHOOSE_ACCOUNT_TYPE);
                    }
                    editoraddress.putExtra("AddressContactModel", (Serializable) data.get(position));
                    mactivity.startActivityForResult(editoraddress, mactivity.EDIT_ACCOUNT_TYPE);
                }
            });
            holder.mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mactivity.deleteinvoicelistitem(position, data.get(position).getId());
                }
            });
            holder.maddress_checkbox.setChecked(isSelected_managecontact.get(data.get(position).getId()));
            holder.maddress_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_managecontact.get(data.get(position).getId().toString())) {
                        mactivity.editoraddress(data.get(position), position);
                    } else {
                        notifyDataSetChanged();
                    }

                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mactivity.mGetIntentInt == 1) {
                        if (data.get(position).getName() != null &&
                                data.get(position).getMobile() != null &&
                                data.get(position).getName().length() > 0 &&
                                data.get(position).getMobile().length() > 0) {
                            Intent intent = new Intent();
                            intent.putExtra("AddressContactModel",(Serializable)data.get(position));
                            mactivity.setResult(1,intent);
                            mactivity.finish();
                        } else {
                            MessageUtils.showToast(mcontext.getResources().getString(R.string.orderconfirm_contact_no_data_str));
                        }
                    }
                }
            });
        }
        return convertView;
    }
    static class ViewHolder
    {
        public LinearLayout mcontact_view_layout;
        public TextView mname;
        public TextView mmoblie;
        public TextView mmanagecontact_address;
        public CheckBox maddress_checkbox;
        public Button meditortxt;
        public Button mdelete;
    }
    public static HashMap<String, Boolean> getIsSelected_invoice() {
        return isSelected_managecontact;
    }

    public static void setIsSelected_invoice(HashMap<String, Boolean> isSelected) {
        ManageContactAdapter.isSelected_managecontact = isSelected;
    }
    public static void clear_isSelectedlist_invoice() {
        if (isSelected_managecontact != null) {
            isSelected_managecontact.clear();
        }
    }
}
