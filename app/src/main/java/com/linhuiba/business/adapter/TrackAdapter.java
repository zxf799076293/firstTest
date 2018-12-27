package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.TrackActivity;
import com.linhuiba.business.activity.TrackRemarksActivity;
import com.linhuiba.business.model.TrackModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/12.
 */
public class TrackAdapter extends BaseAdapter {
    private ArrayList<TrackModel> data = new ArrayList<TrackModel>();
    private Context mcontext;
    private TrackActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<String, Boolean> isSelected_invoice = new HashMap<String, Boolean>();
    public TrackAdapter (Context context,TrackActivity activity,ArrayList<TrackModel> datas,int invoicetype) {
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
            convertView = mInflater.inflate(R.layout.activityfootprintlistitem, null);
            holder.mfootprint_markerinfolayout = (LinearLayout)convertView.findViewById(R.id.footprint_markerinfolayout);
            holder.mtrack_name = (TextView)convertView.findViewById(R.id.track_name);
            holder.mtrack_start = (TextView)convertView.findViewById(R.id.track_start);
            holder.mtrack_size = (TextView)convertView.findViewById(R.id.track_size);
            holder.mtrack_number_of_people_txt = (TextView)convertView.findViewById(R.id.track_number_of_people_txt);
            holder.mtrack_number_of_people = (TextView)convertView.findViewById(R.id.track_number_of_people);
            holder.mtrack_sale_txt = (TextView)convertView.findViewById(R.id.track_sale_txt);
            holder.mtrack_sale = (TextView)convertView.findViewById(R.id.track_sale);
            holder.mtrack_number_of_fans_txt = (TextView)convertView.findViewById(R.id.track_number_of_fans_txt);
            holder.mtrack_number_of_fans = (TextView)convertView.findViewById(R.id.track_number_of_fans);
            holder.marker_reviewimgbtn = (TextView)convertView.findViewById(R.id.marker_reviewimgbtn);
            holder.mfootprint_add_btn = (Button)convertView.findViewById(R.id.footprint_add_btn);
            holder.mfootprint_editorbtn = (Button)convertView.findViewById(R.id.footprint_editorbtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 0) {
            if (data.get(position).getTrack_id() != null) {
                if (data.get(position).getTrack_id().toString().length() != 0) {
                    if (!(data.get(position).getTrack_id().toString().equals("0"))) {
                        holder.mfootprint_add_btn.setVisibility(View.GONE);
                        holder.mfootprint_editorbtn.setVisibility(View.VISIBLE);
                    } else {
                        holder.mfootprint_add_btn.setVisibility(View.VISIBLE);
                        holder.mfootprint_editorbtn.setVisibility(View.GONE);
                    }

                } else {
                    holder.mfootprint_add_btn.setVisibility(View.VISIBLE);
                    holder.mfootprint_editorbtn.setVisibility(View.GONE);
                }
            } else {
                holder.mfootprint_add_btn.setVisibility(View.VISIBLE);
                holder.mfootprint_editorbtn.setVisibility(View.GONE);
            }
            if (data.get(position).getName() != null) {
                if (data.get(position).getName().length() > 0) {
                    holder.mtrack_name.setText(data.get(position).getName().toString());
                }
            }
            if (data.get(position).getStart() != null) {
                if (data.get(position).getStart().length() > 0) {
                    holder.mtrack_start.setText(data.get(position).getStart().toString());
                }
            }
            if (data.get(position).getSize() != null) {
                if (data.get(position).getSize().length() > 0) {
                    holder.mtrack_size.setText("规格:"+data.get(position).getSize().toString());
                }
            }
            if (data.get(position).getNumber_of_fans() != null) {
                if (data.get(position).getNumber_of_fans().length() > 0) {
                    holder.mtrack_number_of_fans_txt.setVisibility(View.VISIBLE);
                    holder.mtrack_number_of_fans.setVisibility(View.VISIBLE);
                    holder.mtrack_number_of_fans.setText(data.get(position).getNumber_of_fans());
                } else {
                    holder.mtrack_number_of_fans_txt.setVisibility(View.GONE);
                    holder.mtrack_number_of_fans.setVisibility(View.GONE);
                }
            } else {
                holder.mtrack_number_of_fans_txt.setVisibility(View.GONE);
                holder.mtrack_number_of_fans.setVisibility(View.GONE);
            }

            if (data.get(position).getNumber_of_people() != null) {
                if (data.get(position).getNumber_of_people().length() > 0) {
                    holder.mtrack_number_of_people_txt.setVisibility(View.VISIBLE);
                    holder.mtrack_number_of_people.setVisibility(View.VISIBLE);
                    holder.mtrack_number_of_people.setText(data.get(position).getNumber_of_people());
                } else {
                    holder.mtrack_number_of_people_txt.setVisibility(View.GONE);
                    holder.mtrack_number_of_people.setVisibility(View.GONE);
                }
            } else {
                holder.mtrack_number_of_people_txt.setVisibility(View.GONE);
                holder.mtrack_number_of_people.setVisibility(View.GONE);
            }
            if (data.get(position).getSale() != null) {
                if (data.get(position).getSale().length() > 0) {
                    holder.mtrack_sale_txt.setVisibility(View.VISIBLE);
                    holder.mtrack_sale.setVisibility(View.VISIBLE);
                    holder.mtrack_sale.setText(data.get(position).getSale().toString());
                } else {
                    holder.mtrack_sale_txt.setVisibility(View.GONE);
                    holder.mtrack_sale.setVisibility(View.GONE);
                }
            } else {
                holder.mtrack_sale_txt.setVisibility(View.GONE);
                holder.mtrack_sale.setVisibility(View.GONE);
            }
            holder.marker_reviewimgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(position).getPic_url() != null) {
                        if (data.get(position).getPic_url().size() > 0) {
                            mactivity.getconfigurate(position);
                        } else {
                            MessageUtils.showToast(mactivity.getResources().getString(R.string.invoiceinfo_add_trackimg_hint));
                        }
                    }  else {
                        MessageUtils.showToast(mactivity.getResources().getString(R.string.invoiceinfo_add_trackimg_hint));
                    }
                }
            });
            holder.mfootprint_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addremarksintent = new Intent(mactivity,TrackRemarksActivity.class);
                    addremarksintent.putExtra("trackitemtype", 0);
                    addremarksintent.putExtra("editorposition", position);
                    addremarksintent.putExtra("trackModel", (Serializable) data.get(position));
                    mactivity.startActivityForResult(addremarksintent, 0);
                }
            });
            holder.mfootprint_editorbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editorremarksintent = new Intent(mactivity,TrackRemarksActivity.class);
                    editorremarksintent.putExtra("trackitemtype", 1);
                    editorremarksintent.putExtra("editorposition", position);
                    editorremarksintent.putExtra("trackModel", (Serializable) data.get(position));
                    mactivity.startActivityForResult(editorremarksintent,1);
                }
            });

        }

        return convertView;
    }
    static class ViewHolder
    {
        public LinearLayout mfootprint_markerinfolayout;
        public TextView mtrack_name;
        public TextView mtrack_start;
        public TextView mtrack_size;
        public TextView mtrack_number_of_people_txt;
        public TextView mtrack_number_of_people;
        public TextView mtrack_sale_txt;
        public TextView mtrack_sale;
        public TextView mtrack_number_of_fans_txt;
        public TextView mtrack_number_of_fans;
        public TextView marker_reviewimgbtn;
        public Button mfootprint_add_btn;
        public Button mfootprint_editorbtn;
    }
    public static HashMap<String, Boolean> getIsSelected_invoice() {
        return isSelected_invoice;
    }

    public static void setIsSelected_invoice(HashMap<String, Boolean> isSelected) {
        TrackAdapter.isSelected_invoice = isSelected;
    }
    public static void clear_isSelectedlist_invoice() {
        if (isSelected_invoice != null) {
            isSelected_invoice.clear();
        }
    }
}
