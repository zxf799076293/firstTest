package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.InvoiceInformationActivity;
import com.linhuiba.business.connector.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InvoiceInfoContentAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,String>> mDataList = new ArrayList<>();
    private Context mContext;
    private InvoiceInformationActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private static HashMap<String, Boolean> isSelectedContent = new HashMap<String, Boolean>();
    public InvoiceInfoContentAdapter(Context context, InvoiceInformationActivity activity,
                                     ArrayList<HashMap<String,String>> mDataList) {
        this.mContext = context;
        this.mActivity = activity;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDataList = mDataList;
    }
    @Override
    public int getCount() {
        int size = 0;
        if (mDataList != null) {
            if (mDataList.size() > 0) {
                if (mActivity.isShowAllInvoiceNameList) {
                    size =  mDataList.size();
                } else {
                    size = 1;
                }
            } else {
                size = 0;
            }
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.activity_invoice_info_content_listitem,null);
            holder.mInvoiceContentTV = (TextView) convertView.findViewById(R.id.invoice_info_item_content_tv);
            holder.mInvoiceTaxPointTV = (TextView) convertView.findViewById(R.id.invoice_info_item_tax_point_tv);
            holder.mInvoiceCheckBox = (CheckBox) convertView.findViewById(R.id.invoice_info_item_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDataList.get(position).get("name") != null &&
                mDataList.get(position).get("name").toString().length() > 0) {
            holder.mInvoiceContentTV.setText(mDataList.get(position).get("name"));
        } else {
            holder.mInvoiceContentTV.setText("");
        }
        if (mDataList.get(position).get("name") != null &&
                mDataList.get(position).get("name").toString().length() > 0) {
            holder.mInvoiceTaxPointTV.setText("（" +
                    mContext.getResources().getString(R.string.invoiceinfo_tax_tv_str) +
                    Constants.getpricestring(
                            mDataList.get(position).get("tax_point").toString(),0.01) +
                    mContext.getResources().getString(R.string.fieldinfo_man_proportion_unit_text) + "）");
            holder.mInvoiceTaxPointTV.setVisibility(View.VISIBLE);
        } else {
            holder.mInvoiceTaxPointTV.setVisibility(View.GONE);
        }
        if (mDataList.get(position).get("id") != null) {
            if (isSelectedContent.get(mDataList.get(position).get("id"))) {
                holder.mInvoiceCheckBox.setChecked(true);
            } else {
                holder.mInvoiceCheckBox.setChecked(false);
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
    static class ViewHolder {
        public TextView mInvoiceContentTV;
        public TextView mInvoiceTaxPointTV;
        public CheckBox mInvoiceCheckBox;
    }
    public static HashMap<String, Boolean> getIsSelectedContent() {
        return isSelectedContent;
    }

    public static void setIsSelectedContent(HashMap<String, Boolean> isSelected) {
        isSelectedContent = isSelected;
    }
    public static void clearIsSelectedContent() {
        if (isSelectedContent != null) {
            isSelectedContent.clear();
        }
    }
}
