package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.FavoritesActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.FavoritesModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FavoritesAdapter extends BaseAdapter {
    private Context mcontext;
    private FavoritesActivity mactivity;
    private LayoutInflater mInflater = null;
    private ArrayList<FavoritesModel> data = new ArrayList<FavoritesModel>();
    public FavoritesAdapter(Context context,FavoritesActivity activity,ArrayList<FavoritesModel> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
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
            convertView = mInflater.inflate(R.layout.activity_favoriteslistitem, null);
            holder.mname = (TextView)convertView.findViewById(R.id.favoritesname);
            holder.mprice = (TextView)convertView.findViewById(R.id.favoritesprice);
            holder.maddress = (TextView)convertView.findViewById(R.id.favoritesaddress);
            holder.mfavorites_imageview = (ImageView) convertView.findViewById(R.id.favorites_imageview);
            holder.mfavorites_txt_fieldtitle = (TextView) convertView.findViewById(R.id.favorites_txt_fieldtitle);
            holder.mfavorites_cancel_btn = (ImageButton) convertView.findViewById(R.id.favorites_cancel_btn);
            holder.mfavorites_item_view = (View) convertView.findViewById(R.id.favorites_item_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (position == data.size() - 1) {
            holder.mfavorites_item_view.setVisibility(View.GONE);
        } else {
            holder.mfavorites_item_view.setVisibility(View.VISIBLE);
        }
        holder.mname.setText(data.get(position).getName());
        if (data.get(position).getFixed_price() > 0) {
            holder.mprice.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                    Constants.getpricestring(String.valueOf(data.get(position).getFixed_price()),0.01)),12));
        }
        holder.maddress.setText(mcontext.getResources().getString(R.string.field_list_item_address)
                +data.get(position).getDetail_address());
        if (data.get(position).getPic_url() != null && data.get(position).getPic_url().length() > 0) {
            Picasso.with(mactivity).load(data.get(position).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(180, 180).into(holder.mfavorites_imageview);
        } else {
            Picasso.with(mactivity).load(R.drawable.ic_no_pic_small).resize(180, 180).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.mfavorites_imageview);
        }
        if (data.get(position).getResource_type() != null) {
            if (data.get(position).getResource_type() == 1) {
                if (data.get(position).getField_type() != null &&
                        data.get(position).getField_type().length() > 0) {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.VISIBLE);
                    holder.mfavorites_txt_fieldtitle.setText(data.get(position).getField_type());
                } else {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.GONE);
                }
            } else if (data.get(position).getResource_type() == 2) {
                if (data.get(position).getAd_type() != null &&
                        data.get(position).getAd_type().length() > 0) {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.VISIBLE);
                    holder.mfavorites_txt_fieldtitle.setText(data.get(position).getAd_type());
                } else {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.GONE);
                }
            } else if (data.get(position).getResource_type() == 3) {
                if (data.get(position).getActivity_type() != null &&
                        data.get(position).getActivity_type().length() > 0) {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.VISIBLE);
                    holder.mfavorites_txt_fieldtitle.setText(data.get(position).getActivity_type());
                } else {
                    holder.mfavorites_txt_fieldtitle.setVisibility(View.GONE);
                }
            }
        } else {
            holder.mfavorites_txt_fieldtitle.setVisibility(View.GONE);
        }
        holder.mfavorites_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mactivity.delete_favorite(position,data.get(position).getTarget_id());
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getPid() != null &&
                        data.get(position).getPid().length() > 0) {
                    Intent editoraddress = null;
                    if (data.get(position).getResource_type() != null) {
                        if (data.get(position).getResource_type() != 2) {
                            editoraddress = new Intent(mactivity, FieldInfoActivity.class);
                            if (data.get(position).getResource_type() == 3) {
                                editoraddress.putExtra("sell_res_id", data.get(position).getTarget_id());
                                editoraddress.putExtra("is_sell_res", true);
                            } else {
                                editoraddress.putExtra("fieldId", data.get(position).getPid());
                            }
                        } else {
                            editoraddress = new Intent(mactivity, AdvertisingInfoActivity.class);
                            editoraddress.putExtra("fieldId", data.get(position).getPid());
                        }
                    }
                    mactivity.startActivityForResult(editoraddress,1);
                }
            }
        });
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mname;
        public TextView mprice;
        public TextView maddress;
        public ImageView mfavorites_imageview;
        public TextView mfavorites_txt_fieldtitle;
        public ImageButton mfavorites_cancel_btn;
        public View mfavorites_item_view;
    }
}
