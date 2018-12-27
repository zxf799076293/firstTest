package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.InvoiceInfoActivity;
import com.linhuiba.business.activity.InvoiceInfoEditorAddress;
import com.linhuiba.business.activity.ManageContactActivity;
import com.linhuiba.business.model.AddressContactModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/5.
 */
public class InvoiceInfoAdapter extends BaseAdapter {
    private Context mcontext;
    private InvoiceInfoActivity mactivity;
    private LayoutInflater mInflater = null;
    private ArrayList<AddressContactModel> data = new ArrayList<AddressContactModel>();
    private static HashMap<String, Boolean> isSelected_defaultaddress = new HashMap<String, Boolean>();
    private int type;
    public InvoiceInfoAdapter(Context context,InvoiceInfoActivity activity,ArrayList<AddressContactModel> datas,int types) {
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
            convertView = mInflater.inflate(R.layout.activity_invoiceinfo_listitem, null);
            holder.mname = (TextView)convertView.findViewById(R.id.invoiceinfo_name);
            holder.mmoblie = (TextView)convertView.findViewById(R.id.invoiceinfo_mobile);
            holder.maddress = (TextView)convertView.findViewById(R.id.invoiceinfo_address);
            holder.maddress_checkbox = (CheckBox)convertView.findViewById(R.id.invoiceinfo_checkbox);
            holder.meditortxt = (Button)convertView.findViewById(R.id.invoiceinfo_editor);
            holder.mdelete = (Button)convertView.findViewById(R.id.invoiceinfo_deletetxt);
            holder.defaultaddress = (TextView)convertView.findViewById(R.id.invoiceinfo_defaultaddress);
            holder.mcontact_view_layout = (LinearLayout)convertView.findViewById(R.id.incoiceinfo_view_layout);
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

            if (data.get(position).getName() != null && data.get(position).getName().length() > 0) {
                holder.mname.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext)+
                        data.get(position).getName());
            } else {
                holder.mname.setText(mactivity.getResources().getString(R.string.confirmorder_contact_prompttext)+
                        mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (data.get(position).getAddress() != null && data.get(position).getAddress().length() > 0) {
                holder.maddress.setText(mactivity.getResources().getString(R.string.field_list_item_address)+
                        data.get(position).getProvince()+data.get(position).getCity()+data.get(position).getDistrict()+
                        data.get(position).getAddress());
            } else {
                holder.maddress.setText(mactivity.getResources().getString(R.string.field_list_item_address)+
                        mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            holder.maddress_checkbox.setChecked(isSelected_defaultaddress.get(data.get(position).getId()));
            holder.meditortxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editoraddress = new Intent(mactivity, InvoiceInfoEditorAddress.class);
                    editoraddress.putExtra("Operationtype", mactivity.EDIT_INVOICE_ADDRESS);
                    editoraddress.putExtra("AddressContactModel", (Serializable) data.get(position));
                    editoraddress.putExtra("order_choose", mactivity.EDIT_INVOICE_ADDRESS);
                    mactivity.startActivityForResult(editoraddress, mactivity.EDIT_INVOICE_ADDRESS);
                }
            });
            holder.mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mactivity.deleteinvoicelistitem(position, data.get(position).getId());
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(position).getMobile() != null &&
                            data.get(position).getName() != null &&
                            data.get(position).getAddress() != null &&
                            data.get(position).getProvince_id() != null &&
                            data.get(position).getCity_id() != null &&
                            data.get(position).getDistrict_id() != null &&
                            data.get(position).getProvince() != null &&
                            data.get(position).getCity() != null &&
                            data.get(position).getDistrict() != null &&

                            data.get(position).getMobile().length() > 0 &&
                            data.get(position).getName().length() > 0 &&
                            data.get(position).getAddress().length() > 0 &&
                            data.get(position).getProvince_id().length() > 0 &&
                            data.get(position).getCity_id().length() > 0 &&
                            data.get(position).getDistrict_id().length() > 0 &&
                            data.get(position).getProvince().length() > 0 &&
                            data.get(position).getCity().length() > 0 &&
                            data.get(position).getDistrict().length() > 0) {
                        Intent editoraddress = new Intent();
                        editoraddress.putExtra("Operationtype", 2);
                        editoraddress.putExtra("AddressContactModel", (Serializable) data.get(position));
                        mactivity.setResult(mactivity.EDIT_INVOICE_ADDRESS,editoraddress);
                        mactivity.finish();
                    } else {
                        MessageUtils.showToast(mcontext.getResources().getString(R.string.moduld_invoice_replenish_address));
                    }
                }
            });
            holder.maddress_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected_defaultaddress.get(data.get(position).getId().toString())) {
                        mactivity.editoraddress(data.get(position),position);
                    } else {
                        notifyDataSetChanged();
                    }

                }
            });

        } else if (type == 1) {

        }

        return convertView;
    }
    static class ViewHolder
    {
        public TextView mname;
        public TextView mmoblie;
        public TextView maddress;
        public CheckBox maddress_checkbox;
        public Button meditortxt;
        public Button mdelete;
        public TextView defaultaddress;
        public LinearLayout mcontact_view_layout;
    }
    public static HashMap<String, Boolean> getIsSelected_invoice() {
        return isSelected_defaultaddress;
    }

    public static void setIsSelected_invoice(HashMap<String, Boolean> isSelected) {
        InvoiceInfoAdapter.isSelected_defaultaddress = isSelected;
    }
    public static void clear_isSelectedlist_invoice() {
        if (isSelected_defaultaddress != null) {
            isSelected_defaultaddress.clear();
        }
    }
}
