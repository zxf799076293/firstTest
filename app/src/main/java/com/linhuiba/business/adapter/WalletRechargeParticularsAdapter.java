package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.OrderConfirmUploadVoucherActivity;
import com.linhuiba.business.activity.WalletRechargeParticularsActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.WalletRechargeParticularsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/18.
 */

public class WalletRechargeParticularsAdapter extends BaseAdapter {
    private Context mcontext;
    private WalletRechargeParticularsActivity mactivity;
    private ArrayList<WalletRechargeParticularsModel> mdatalist;
    private LayoutInflater layoutInflater = null;
    private int type;
    public WalletRechargeParticularsAdapter (Context context, WalletRechargeParticularsActivity activity,
                                             ArrayList<WalletRechargeParticularsModel> data,int type) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.mdatalist = data;
        this.type = type;
    }
    @Override
    public int getCount() {
        if (mdatalist != null) {
            return mdatalist.size();
        } else {
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
            convertView = layoutInflater.inflate(R.layout.activity_walletrechargeparticulars_listitem,null);
            holder.mcharge_particulars_serial_number_text = (TextView)convertView.findViewById(R.id.charge_particulars_serial_number_text);
            holder.mcharge_particulars_state_text = (TextView)convertView.findViewById(R.id.charge_particulars_state_text);
            holder.mcharge_particulars_price_text = (TextView)convertView.findViewById(R.id.charge_particulars_price_text);
            holder.mcharge_particulars_time_text = (TextView)convertView.findViewById(R.id.charge_particulars_time_text);
            holder.mcharge_particulars_pay_state_imageview = (ImageView)convertView.findViewById(R.id.charge_particulars_pay_state_imageview);
            holder.mcharge_particulars_pay_state_text = (TextView)convertView.findViewById(R.id.charge_particulars_pay_state_text);
            holder.mcharge_particulars_cancel_payment_text = (TextView)convertView.findViewById(R.id.charge_particulars_cancel_payment_text);
            holder.mcharge_particulars_already_payment_text = (TextView)convertView.findViewById(R.id.charge_particulars_already_payment_text);
            holder.mwallet_mywallet_recharge_bottom_view = (View)convertView.findViewById(R.id.wallet_mywallet_recharge_bottom_view);
            holder.mcharge_particulars_bottom_text = (TextView)convertView.findViewById(R.id.charge_particulars_bottom_text);
            holder.mcharge_particulars_rechange_item_layout = (LinearLayout)convertView.findViewById(R.id.charge_particulars_rechange_item_layout);
            holder.mcharge_particulars_consumption_item_layout = (LinearLayout)convertView.findViewById(R.id.charge_particulars_consumption_item_layout);
            holder.mcharge_particulars_order_status_text = (TextView)convertView.findViewById(R.id.charge_particulars_order_status_text);
            holder.mcharge_particulars_consumption_price_text = (TextView)convertView.findViewById(R.id.charge_particulars_consumption_price_text);
            holder.mcharge_particulars_order_num_text = (TextView)convertView.findViewById(R.id.charge_particulars_order_num_text);
            holder.mChargeParticularsOrderNumTV = (TextView)convertView.findViewById(R.id.charge_particulars_order_num_tv);
            holder.mcharge_particulars_consumption_time_text = (TextView)convertView.findViewById(R.id.charge_particulars_consumption_time_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (mactivity.buttom_show) {
            if (position == mdatalist.size() - 1) {
                holder.mwallet_mywallet_recharge_bottom_view.setVisibility(View.GONE);
                holder.mcharge_particulars_bottom_text.setVisibility(View.VISIBLE);
                if (type == 1) {
                    holder.mcharge_particulars_bottom_text.setText(mactivity.getString(R.string.wallet_mywallet_recharge_bottom_text));
                } else if (type == 2) {
                    holder.mcharge_particulars_bottom_text.setText(mactivity.getString(R.string.wallet_mywallet_recharge_consumption_bottom_text));
                }
            } else {
                holder.mwallet_mywallet_recharge_bottom_view.setVisibility(View.VISIBLE);
                holder.mcharge_particulars_bottom_text.setVisibility(View.GONE);
            }
        } else {
            holder.mwallet_mywallet_recharge_bottom_view.setVisibility(View.VISIBLE);
            holder.mcharge_particulars_bottom_text.setVisibility(View.GONE);
        }
        if (type == 1) {
            holder.mcharge_particulars_rechange_item_layout.setVisibility(View.VISIBLE);
            holder.mcharge_particulars_consumption_item_layout.setVisibility(View.GONE);
            if (mdatalist.get(position).getOut_trade_no() != null && mdatalist.get(position).getOut_trade_no().length() > 0) {
                holder.mcharge_particulars_serial_number_text.setText(mdatalist.get(position).getOut_trade_no());
            } else {
                holder.mcharge_particulars_serial_number_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mdatalist.get(position).getAmount() != null) {
                holder.mcharge_particulars_price_text.setText(Constants.getdoublepricestring(mdatalist.get(position).getAmount(),0.01));
            } else {
                holder.mcharge_particulars_price_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mdatalist.get(position).getCreated_at() != null && mdatalist.get(position).getCreated_at().length() > 0) {
                holder.mcharge_particulars_time_text.setText(mdatalist.get(position).getCreated_at());
            } else {
                holder.mcharge_particulars_time_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mdatalist.get(position).getPayment_mode() == 0) {
                holder.mcharge_particulars_pay_state_text.setText(mactivity.getResources().getString(R.string.confirmorder_account_pay_checkprompt));
                Picasso.with(mcontext).load(R.drawable.ic_paid_two_four_one).into(holder.mcharge_particulars_pay_state_imageview);
                holder.mcharge_particulars_already_payment_text.setVisibility(View.VISIBLE);
                if (mdatalist.get(position).getVoucher_image_url() != null && mdatalist.get(position).getVoucher_image_url().length() > 0) {
                    holder.mcharge_particulars_already_payment_text.setText(mactivity.getString(R.string.order_refuse_offline_pay_look));
                } else {
                    holder.mcharge_particulars_already_payment_text.setText(mactivity.getString(R.string.order_refuse_offline_pay_upload));
                }
            } else if (mdatalist.get(position).getPayment_mode() == 1 ||
                    mdatalist.get(position).getPayment_mode() == 6 ||
                    mdatalist.get(position).getPayment_mode() == 2 ||
                    mdatalist.get(position).getPayment_mode() == 3 ||
                    mdatalist.get(position).getPayment_mode() == 5 ||
                    mdatalist.get(position).getPayment_mode() == 7) {
                holder.mcharge_particulars_pay_state_text.setText(mactivity.getResources().getString(R.string.confirmorder_weixin_paie));
                Picasso.with(mcontext).load(R.drawable.ic_wechat_two_four_one).into(holder.mcharge_particulars_pay_state_imageview);
                holder.mcharge_particulars_already_payment_text.setVisibility(View.GONE);
            } else if (mdatalist.get(position).getPayment_mode() == 10 ||
                    mdatalist.get(position).getPayment_mode() == 8 ||
                    mdatalist.get(position).getPayment_mode() == 9) {
                holder.mcharge_particulars_pay_state_text.setText(mactivity.getResources().getString(R.string.confirmorder_alipay_paie));
                Picasso.with(mcontext).load(R.drawable.ic_alipay_two_six).into(holder.mcharge_particulars_pay_state_imageview);
                holder.mcharge_particulars_already_payment_text.setVisibility(View.GONE);
            }
            if (mdatalist.get(position).getConfirmed() == 0) {
                holder.mcharge_particulars_state_text.setText(mactivity.getString(R.string.wallet_mywallet_wait_for_posting_text));
                holder.mcharge_particulars_cancel_payment_text.setVisibility(View.VISIBLE);
            } else if (mdatalist.get(position).getConfirmed() == 1) {
                holder.mcharge_particulars_state_text.setText(mactivity.getString(R.string.wallet_mywallet_confirmed_posting_text));
                holder.mcharge_particulars_cancel_payment_text.setVisibility(View.GONE);
            }
            holder.mcharge_particulars_already_payment_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent uploadvoucher = new Intent(mactivity, OrderConfirmUploadVoucherActivity.class);
                    uploadvoucher.putExtra("order_id", String.valueOf(mdatalist.get(position).getId()).toString());
                    uploadvoucher.putExtra("type", 2);
                    if (mdatalist.get(position).getVoucher_image_url() != null &&
                            mdatalist.get(position).getVoucher_image_url().length() > 0) {
                        uploadvoucher.putExtra("voucher_image_url", mdatalist.get(position).getVoucher_image_url().toString());
                    }
                    if (mdatalist.get(position).getConfirmed() == 1) {
                        uploadvoucher.putExtra("voucher_confirmed", 1);
                    }
                    uploadvoucher.putExtra("order_num", mdatalist.get(position).getOut_trade_no().toString());
                    uploadvoucher.putExtra("payment_price", mdatalist.get(position).getAmount().toString());
                    uploadvoucher.putExtra("remittance",2);
                    mactivity.startActivityForResult(uploadvoucher,1);
                }
            });
            holder.mcharge_particulars_cancel_payment_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mactivity.cacel_payment(position,String.valueOf(mdatalist.get(position).getId()));
                }
            });
        } else if (type == 2) {
            holder.mcharge_particulars_rechange_item_layout.setVisibility(View.GONE);
            holder.mcharge_particulars_consumption_item_layout.setVisibility(View.VISIBLE);
            if (mdatalist.get(position).getCreated_at() != null && mdatalist.get(position).getCreated_at().length() > 0) {
                holder.mcharge_particulars_consumption_time_text.setText(mdatalist.get(position).getCreated_at());
            } else {
                holder.mcharge_particulars_consumption_time_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            //2017/12/29 退押金的状态判断
            if (mdatalist.get(position).getName() != null &&
                    mdatalist.get(position).getName().length() > 0) {
                holder.mcharge_particulars_order_status_text.setText(mdatalist.get(position).getName());
            } else {
                holder.mcharge_particulars_order_status_text.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mdatalist.get(position).getAmount() != null) {
                if (mdatalist.get(position).getAmount() < 0) {
                    holder.mcharge_particulars_consumption_price_text.setTextColor(mactivity.getResources().getColor(R.color.top_title_center_txt_color));
                    holder.mcharge_particulars_consumption_price_text.setText(Constants.getdoublepricestring(mdatalist.get(position).getAmount(),0.01));
                } else {
                    holder.mcharge_particulars_consumption_price_text.setTextColor(mactivity.getResources().getColor(R.color.default_redbg));
                    holder.mcharge_particulars_consumption_price_text.setText("+"+Constants.getdoublepricestring(mdatalist.get(position).getAmount(),0.01));
                }
            } else {
                holder.mcharge_particulars_consumption_price_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mdatalist.get(position).getTransaction_type() == 4 ||
                    mdatalist.get(position).getTransaction_type() == 6) {
                holder.mChargeParticularsOrderNumTV.setText(mcontext.getResources().getString(R.string.invoiceinfo_price_text));
                if (mdatalist.get(position).getInvoice_order() != null && mdatalist.get(position).getInvoice_order().get("tax") != null &&
                        mdatalist.get(position).getInvoice_order().get("tax").length() > 0) {
                    if (mdatalist.get(position).getInvoice_order().get("amount") != null &&
                            mdatalist.get(position).getInvoice_order().get("amount").length() > 0) {
                        holder.mcharge_particulars_order_num_text.setText(mactivity.getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(mdatalist.get(position).getInvoice_order().get("amount"),1));
                    } else {
                        holder.mcharge_particulars_order_num_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                    }
                }
            } else {
                holder.mChargeParticularsOrderNumTV.setText(mcontext.getResources().getString(R.string.order_confirm_ordernum_text));
                if (mdatalist.get(position).getOut_trade_no() != null && mdatalist.get(position).getOut_trade_no().length() > 0) {
                    holder.mcharge_particulars_order_num_text.setText(mdatalist.get(position).getOut_trade_no());
                } else {
                    holder.mcharge_particulars_order_num_text.setText(mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mactivity.intent_recharge_info(mdatalist.get(position).getId());
                }
            });
        }
        return convertView;
    }
    static class ViewHolder {
        public TextView mcharge_particulars_serial_number_text;
        public TextView mcharge_particulars_state_text;
        public TextView mcharge_particulars_price_text;
        public TextView mcharge_particulars_time_text;
        public ImageView mcharge_particulars_pay_state_imageview;
        public TextView mcharge_particulars_pay_state_text;
        public TextView mcharge_particulars_cancel_payment_text;
        public TextView mcharge_particulars_already_payment_text;
        public View mwallet_mywallet_recharge_bottom_view;
        public TextView mcharge_particulars_bottom_text;
        public LinearLayout mcharge_particulars_rechange_item_layout;
        public LinearLayout mcharge_particulars_consumption_item_layout;
        public TextView mcharge_particulars_order_status_text;
        public TextView mcharge_particulars_consumption_price_text;
        public TextView mcharge_particulars_order_num_text;
        public TextView mChargeParticularsOrderNumTV;
        public TextView mcharge_particulars_consumption_time_text;
    }
}
