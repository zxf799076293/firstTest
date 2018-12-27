package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_UploadingPictureActivity;
import com.linhuiba.linhuipublic.config.Config;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Field_ChoosePictureGridViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> result;
    private Field_AddField_UploadingPictureActivity mactivity;
    private int type;
    public Field_ChoosePictureGridViewAdapter(Context context, Field_AddField_UploadingPictureActivity activity, ArrayList<String> pathlist, int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mactivity = activity;
        this.result = pathlist;
        this.type = type;
    }

    @Override
    public int getCount() {
        if(result != null){
            return result.size();
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
        //如果缓存convertView为空，则需要创建View
        if(convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mLayoutInflater.inflate(R.layout.field_adapter_choosepicture_item, null);
            holder.gridview_img = (ImageView)convertView.findViewById(R.id.choosepicture_img);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if (result != null) {
            if (result.size() > 0) {
                if (result.get(position) != null) {
                    if (result.get(position).toString().equals("firstgridviewitem")) {
                        Glide.with(context)
                                .load(R.drawable.ic_add_upload_pictures)
                                .centerCrop()
                                .into(holder.gridview_img);

                    } else {
                        if (result.get(position).indexOf("http") != -1) {
                            Glide.with(context)
                                    .load(result.get(position) + Config.Linhui_Min_Watermark)
                                    .centerCrop()
                                    .into(holder.gridview_img);
                        } else {
                            Glide.with(context)
                                    .load(result.get(position))
                                    .centerCrop()
                                    .into(holder.gridview_img);
                        }
                    }
                }

                if (type == 0) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position) != null) {
                                            if (result.get(position).toString().equals("firstgridviewitem")) {
                                                mactivity.Intentpayorder(result);
                                            } else {
                                                mactivity.showpreviewpicture(position);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });

                }

            }

        }

        return convertView;
    }
    static class ViewHolder
    {
        public ImageView gridview_img;
    }
}
