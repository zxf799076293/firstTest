package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.CalendarUtil;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.ApplyForInvoiceActivity;
import com.linhuiba.business.activity.InvoiceInformationActivity;
import com.linhuiba.business.activity.MySelfPushMessageActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.InvoicesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/7/5.
 */
public class ApplyForInvoiceAdapter extends BaseAdapter {
    private ArrayList<InvoicesModel> data = new ArrayList<InvoicesModel>();
    private Context mcontext;
    private ApplyForInvoiceActivity mactivity;
    private LayoutInflater mInflater = null;
    private InvoiceInformationActivity invoiceInformationActivity;
    private int type;
    private static HashMap<String, Boolean> isSelected_invoice = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSelectedSupplementInvoice = new HashMap<String, Boolean>();
    public ApplyForInvoiceAdapter (Context context,ApplyForInvoiceActivity activity,ArrayList<InvoicesModel> datas,int invoicetype) {
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
            convertView = mInflater.inflate(R.layout.activity_applyforinvoice_listitem, null);
            holder.mname = (TextView)convertView.findViewById(R.id.invoice_name);
            holder.mtime = (TextView)convertView.findViewById(R.id.invoice_time);
            holder.mApplyForInvoiceSpecialTV = (TextView)convertView.findViewById(R.id.applyforinvoice_special_tv);
            holder.mprice = (TextView)convertView.findViewById(R.id.invoice_price_txt);
            holder.mApplyForInvoiceBottomView = (View) convertView.findViewById(R.id.applyforinvoice_bottom_view);
            holder.minvoice_checkbox = (CheckBox)convertView.findViewById(R.id.invoice_checkbox);
            holder.mapplyinvoice_checkbox_grey = (TextView)convertView.findViewById(R.id.applyinvoice_checkbox_grey);
            holder.mApplyForInvoiceStateDatedImg = (ImageView)convertView.findViewById(R.id.applyforinvoice_state_dated_img);
            holder.mApplyForInvoiceStateDatedLL = (LinearLayout) convertView.findViewById(R.id.applyforinvoice_state_dated_ll);
            holder.minvoice_size = (TextView)convertView.findViewById(R.id.invoice_size);
            holder.mapply_invoice_item_relativelayout = (RelativeLayout)convertView.findViewById(R.id.apply_invoice_item_relativelayout);

            holder.mcreate_invoice_layout =(LinearLayout)convertView.findViewById(R.id.create_invoice_layout);
            holder.mapplyforinvoice_createinvoice_itemname = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_itemname);
            holder.mapplyforinvoice_createinvoice_iteminvoice_state = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_iteminvoice_state);
            holder.mapplyforinvoice_createinvoice_iteminvoicetype = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_iteminvoicetype);
            holder.mapplyforinvoice_createinvoice_itemprice = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_itemprice);
            holder.mapplyforinvoice_createinvoice_delivery_text = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_delivery_text);
            holder.mapplyforinvoice_createinvoice_item_courier_number = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_item_courier_number);
            holder.mapplyforinvoice_createinvoice_item_courier_number_copy = (TextView)convertView.findViewById(R.id.applyforinvoice_createinvoice_item_courier_number_copy);
            holder.mapplyforinvoice_createinvoice_item_courier_number_layout = (LinearLayout)convertView.findViewById(R.id.applyforinvoice_createinvoice_item_courier_number_layout);
            holder.mapplyforinvoice_item_end_view = (View)convertView.findViewById(R.id.applyforinvoice_item_end_view);
            holder.minvoice_listitem_imageview = (ImageView)convertView.findViewById(R.id.invoice_listitem_imageview);
            holder.mapplyforinvoice_txt_fieldtitle = (TextView)convertView.findViewById(R.id.applyforinvoice_txt_fieldtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (data.get(position).getSize()!= null&& data.get(position).getSize().length() > 0) {
            holder.minvoice_size.setText("规格："+data.get(position).getSize());
            if (data.get(position).getLease_term_type() != null) {
                if (data.get(position).getLease_term_type().length() > 0) {
                    holder.minvoice_size.setText("规格："+data.get(position).getSize()+"("+data.get(position).getLease_term_type()+")");
                }
            }
        } else {
            holder.minvoice_size.setText("规格："+mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (type == 1) {
            holder.mcreate_invoice_layout.setVisibility(View.VISIBLE);
            holder.mapply_invoice_item_relativelayout.setVisibility(View.GONE);
            if (data.get(position).getTitle() != null && data.get(position).getTitle().length() > 0) {
                holder.mapplyforinvoice_createinvoice_itemname.setText(data.get(position).getTitle().toString());
            }
            if (data.get(position).getTicket_type() != null&& data.get(position).getTicket_type().length() > 0) {
                holder.mapplyforinvoice_createinvoice_iteminvoicetype.setVisibility(View.VISIBLE);
                holder.mapplyforinvoice_createinvoice_iteminvoicetype.setText(mcontext.getResources().getString(R.string.applyforinvoice_invoice_type)+
                        data.get(position).getTicket_type());
            } else {
                holder.mapplyforinvoice_createinvoice_iteminvoicetype.setVisibility(View.GONE);
            }
            if (data.get(position).getStatus() != null && data.get(position).getStatus().length() > 0) {
                if (data.get(position).getStatus().equals(mactivity.getResources().getString(R.string.applyforinvoice_true_txt)) ||
                        data.get(position).getStatus().equals(mactivity.getResources().getString(R.string.module_applyforinvoice_setatus_post))) {
                    holder.mapplyforinvoice_createinvoice_iteminvoice_state.setTextColor(mcontext.getResources().getColor(R.color.order_list_item_finished_status_tv_color));
                    holder.mapplyforinvoice_createinvoice_item_courier_number_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.mapplyforinvoice_createinvoice_iteminvoice_state.setTextColor(mcontext.getResources().getColor(R.color.default_redbg));
                    holder.mapplyforinvoice_createinvoice_item_courier_number_layout.setVisibility(View.GONE);
                }
                holder.mapplyforinvoice_createinvoice_iteminvoice_state.setText(data.get(position).getStatus().toString());
            } else {
                holder.mapplyforinvoice_createinvoice_item_courier_number_layout.setVisibility(View.GONE);
            }
            OnClick(holder.mapplyforinvoice_createinvoice_item_courier_number_copy,holder.mapplyforinvoice_createinvoice_item_courier_number);
            if (data.get(position).getDeliver() != null && data.get(position).getDeliver().length() > 0) {
                holder.mapplyforinvoice_createinvoice_delivery_text.setText(data.get(position).getDeliver());
                if (data.get(position).getDelivery_num() != null && data.get(position).getDelivery_num().length() > 0) {
                    holder.mapplyforinvoice_createinvoice_item_courier_number.setText(data.get(position).getDelivery_num().toString());
                    holder.mapplyforinvoice_createinvoice_item_courier_number_copy.setVisibility(View.VISIBLE);
                } else {
                    holder.mapplyforinvoice_createinvoice_item_courier_number.setText(mcontext.getString(R.string.fieldinfo_no_parameter_message));
                    holder.mapplyforinvoice_createinvoice_item_courier_number_copy.setVisibility(View.VISIBLE);
                }
            } else {
                holder.mapplyforinvoice_createinvoice_item_courier_number.setText(mcontext.getString(R.string.fieldinfo_no_parameter_message));
                holder.mapplyforinvoice_createinvoice_item_courier_number_copy.setVisibility(View.VISIBLE);
            }
            if (data.get(position).getSum() != null && data.get(position).getSum().length() > 0) {
                holder.mapplyforinvoice_createinvoice_itemprice.setText(mcontext.getResources().getString(R.string.invoiceinfo_price_text) +
                        Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                        Constants.getpricestring(data.get(position).getSum().toString(), 0.01)),12));
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent invoiceinfo = new Intent(mactivity,AboutUsActivity.class);
                    invoiceinfo.putExtra("type",  Config.INVOICE_INFO_WEB_INT);
                    invoiceinfo.putExtra("id",data.get(position).getId());
                    mactivity.startActivity(invoiceinfo);
                }
            });
            if (position == data.size() - 1) {
                holder.mapplyforinvoice_item_end_view.setVisibility(View.GONE);
            } else {
                holder.mapplyforinvoice_item_end_view.setVisibility(View.VISIBLE);
            }
        } else if (type == 0) {
            holder.mcreate_invoice_layout.setVisibility(View.GONE);
            // : 2017/10/18 过期显示
            if (data.get(position).getIssue_invoice() != null &&
                    data.get(position).getIssue_invoice() == false) {
                holder.mApplyForInvoiceStateDatedImg.setVisibility(View.VISIBLE);
                holder.mApplyForInvoiceStateDatedLL.setVisibility(View.VISIBLE);
                holder.minvoice_checkbox.setEnabled(false);
            } else {
                holder.mApplyForInvoiceStateDatedImg.setVisibility(View.GONE);
                holder.mApplyForInvoiceStateDatedLL.setVisibility(View.GONE);
                holder.minvoice_checkbox.setEnabled(true);
            }
            if (data.get(position).getSelling_resource().getPhysical_resource().getField_type_id() != null) {
                if (data.get(position).getSelling_resource().getPhysical_resource().getField_type_id() == 1) {
                    if (data.get(position).getSelling_resource().getPhysical_resource().getField_type() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.VISIBLE);
                        holder.mapplyforinvoice_txt_fieldtitle.setText(data.get(position).getSelling_resource().getPhysical_resource().getField_type().getDisplay_name());
                    } else {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.GONE);
                    }
                } else if (data.get(position).getSelling_resource().getPhysical_resource().getField_type_id() == 2) {
                    if (data.get(position).getSelling_resource().getPhysical_resource().getAd_type() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getAd_type().getDisplay_name() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getAd_type().getDisplay_name().length() > 0) {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.VISIBLE);
                        holder.mapplyforinvoice_txt_fieldtitle.setText(data.get(position).getSelling_resource().getPhysical_resource().getAd_type().getDisplay_name());
                    } else {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.GONE);
                    }
                } else if (data.get(position).getSelling_resource().getPhysical_resource().getField_type_id() == 3) {
                    if (data.get(position).getSelling_resource().getPhysical_resource().getActivity_type() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getActivity_type().getDisplay_name() != null &&
                            data.get(position).getSelling_resource().getPhysical_resource().getActivity_type().getDisplay_name().length() > 0) {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.VISIBLE);
                        holder.mapplyforinvoice_txt_fieldtitle.setText(data.get(position).getSelling_resource().getPhysical_resource().getActivity_type().getDisplay_name());
                    } else {
                        holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.GONE);
                    }
                }

            } else {
                holder.mapplyforinvoice_txt_fieldtitle.setVisibility(View.GONE);
            }

            if (position == data.size() - 1) {
                holder.mApplyForInvoiceBottomView.setVisibility(View.GONE);
            } else {
                holder.mApplyForInvoiceBottomView.setVisibility(View.VISIBLE);
            }
            if (data.get(position).getTitle() != null &&
                    data.get(position).getTitle().length() > 0) {
                holder.mname.setText(data.get(position).getTitle());
            } else {
                holder.mname.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (data.get(position).getStart() != null && data.get(position).getStart().length() > 0) {
                holder.mtime.setText(mcontext.getResources().getString(R.string.home_activity_item_timetxt) +
                        data.get(position).getStart().toString());
            } else {
                holder.mtime.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            // : 2017/10/17 特殊规格显示
            if (data.get(position).getActual_fee() != null && data.get(position).getActual_fee().length() > 0) {
                holder.mprice.setText(Constants.getPriceUnitStr(mcontext,(mactivity.getResources().getString(R.string.order_listitem_price_unit_text)+
                        Constants.getpricestring(data.get(position).getActual_fee().toString(), 0.01)),12));
            } else {
                holder.mprice.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mactivity.isSupplement) {
                holder.minvoice_checkbox.setChecked(isSelectedSupplementInvoice.get(data.get(position).getField_order_item_id().toString()));
            } else {
                holder.minvoice_checkbox.setChecked(isSelected_invoice.get(data.get(position).getField_order_item_id().toString()));
            }
            holder.minvoice_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mactivity.isSupplement) {
                        if (!isSelectedSupplementInvoice.get(data.get(position).getField_order_item_id().toString())) {
                            isSelectedSupplementInvoice.put(data.get(position).getField_order_item_id().toString(), true);
                        } else {
                            isSelectedSupplementInvoice.put(data.get(position).getField_order_item_id().toString(), false);
                        }
                    } else {
                        if (!isSelected_invoice.get(data.get(position).getField_order_item_id().toString())) {
                            isSelected_invoice.put(data.get(position).getField_order_item_id().toString(), true);
                        } else {
                            isSelected_invoice.put(data.get(position).getField_order_item_id().toString(), false);
                        }
                    }
                    mactivity.getInvoice_clicksize_price(Integer.parseInt(getchecksize_str()),getinvoiceprice());
                    if (mactivity.isSupplement) {
                        setIsSelectedSupplementInvoice(isSelectedSupplementInvoice);
                    } else {
                        setIsSelected_invoice(isSelected_invoice);
                    }
                    notifyDataSetChanged();
                    mactivity.getnextbtn_state();
                }
            });
            if (data.get(position).getTicket() != null && data.get(position).getTicket().length() > 0) {
                holder.minvoice_checkbox.setVisibility(View.VISIBLE);
                holder.mapplyinvoice_checkbox_grey.setVisibility(View.GONE);
            }
            holder.mapplyinvoice_checkbox_grey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageUtils.showToast(mactivity.getResources().getString(R.string.applyforinvoice_onlyreceipt_txt));
                }
            });
            if (data.get(position).getPic_url() != null &&
                    data.get(position).getPic_url().length() > 0) {
               Picasso.with(mactivity).load(data.get(position).getPic_url() + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(160, 160).into(holder.minvoice_listitem_imageview);
            } else {
                Picasso.with(mactivity).load(R.drawable.ic_no_pic_small).resize(160, 160).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.minvoice_listitem_imageview);
            }
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mname;
        public TextView mtime;
        public TextView mApplyForInvoiceSpecialTV;
        public TextView mprice;
        public View mApplyForInvoiceBottomView;
        public CheckBox minvoice_checkbox;
        public TextView mapplyinvoice_checkbox_grey;
        public ImageView mApplyForInvoiceStateDatedImg;
        public LinearLayout mApplyForInvoiceStateDatedLL;
        public TextView minvoice_size;
        public RelativeLayout mapply_invoice_item_relativelayout;
        public LinearLayout mcreate_invoice_layout;
        public TextView mapplyforinvoice_createinvoice_itemname;
        public TextView mapplyforinvoice_createinvoice_iteminvoice_state;
        public TextView mapplyforinvoice_createinvoice_iteminvoicetype;
        public TextView mapplyforinvoice_createinvoice_itemprice;
        public TextView mapplyforinvoice_createinvoice_delivery_text;
        public TextView mapplyforinvoice_createinvoice_item_courier_number;
        public TextView mapplyforinvoice_createinvoice_item_courier_number_copy;
        public LinearLayout mapplyforinvoice_createinvoice_item_courier_number_layout;
        public View mapplyforinvoice_item_end_view;
        public ImageView minvoice_listitem_imageview;
        public TextView mapplyforinvoice_txt_fieldtitle;

    }
    public static HashMap<String, Boolean> getIsSelected_invoice() {
        return isSelected_invoice;
    }

    public static void setIsSelected_invoice(HashMap<String, Boolean> isSelected) {
        ApplyForInvoiceAdapter.isSelected_invoice = isSelected;
    }
    public static void clear_isSelectedlist_invoice() {
        if (isSelected_invoice != null) {
            isSelected_invoice.clear();
        }
    }
    public static HashMap<String, Boolean> getIsSelectedSupplementInvoice() {
        return isSelectedSupplementInvoice;
    }

    public static void setIsSelectedSupplementInvoice(HashMap<String, Boolean> isSelected) {
        ApplyForInvoiceAdapter.isSelectedSupplementInvoice = isSelected;
    }
    public static void clearIsSelectedSupplementInvoice() {
        if (isSelectedSupplementInvoice != null) {
            isSelectedSupplementInvoice.clear();
        }
    }
    public  String getchecksize_str() {
        int listsize = 0;
        String str_listsize ="";
        if (mactivity.isSupplement) {
            for (int i = 0; i < data.size(); i ++) {
                if (isSelectedSupplementInvoice.get(data.get(i).getField_order_item_id())) {
                    listsize ++;
                }
            }

        } else {
            for (int i = 0; i < data.size(); i ++) {
                if (isSelected_invoice.get(data.get(i).getField_order_item_id())) {
                    listsize ++;
                }
            }
        }
        str_listsize = String.valueOf(listsize);
        return str_listsize;
    }
    public  String getinvoiceprice() {
        double fieldprice = 0;
        String price = "";
        if (mactivity.isSupplement) {
            for (int i = 0; i < data.size(); i ++) {
                if (isSelectedSupplementInvoice.get(data.get(i).getField_order_item_id())) {
                    if (data.get(i).getActual_fee() != null) {
                        if (data.get(i).getActual_fee().length() > 0) {
                            double price_tem = Double.parseDouble(data.get(i).getActual_fee().toString());
                            fieldprice = fieldprice + price_tem;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < data.size(); i ++) {
                if (isSelected_invoice.get(data.get(i).getField_order_item_id())) {
                    if (data.get(i).getActual_fee() != null) {
                        if (data.get(i).getActual_fee().length() > 0) {
                            double price_tem = Double.parseDouble(data.get(i).getActual_fee().toString());
                            fieldprice = fieldprice + price_tem;
                        }
                    }
                }
            }

        }
        price = (Constants.getSearchPriceStr(String.valueOf(fieldprice), 0.01));
        return price;
    }
    private void OnClick(final TextView textViewcopy, final TextView textView) {
        textViewcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().length() > 0 &&
                        !(textView.getText().toString().trim().equals(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message)))) {
                    Constants.getInstance().onClickCopy(textView,
                            mactivity.getResources().getString(R.string.applyforinvoice_invoice_copy_courier_number_point));
                } else {
                    MessageUtils.showToast(mcontext.getResources().getString(R.string.applyforinvoice_not_have_express_number_str));

                }
            }
        });
    }
}
