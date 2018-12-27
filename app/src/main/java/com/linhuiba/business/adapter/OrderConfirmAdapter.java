package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.OrderConfirmActivity;
import com.linhuiba.business.activity.UseCouponsActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/8.
 */
public class OrderConfirmAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
    private Context mcontext;
    private OrderConfirmActivity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<Integer, MyCouponsModel> orderCouponsMap = new HashMap<Integer, MyCouponsModel>();
    private static HashMap<Integer, String> leaveMap = new HashMap<Integer, String>();

    public OrderConfirmAdapter(Context context, OrderConfirmActivity activity, ArrayList<HashMap<String,Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
    }


    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
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
            convertView = mInflater.inflate(R.layout.activity_orderconfirmlistitem, null);
            holder.mname = (TextView) convertView.findViewById(R.id.confirmorder_nametxt);
            holder.mdate = (TextView) convertView.findViewById(R.id.confirmorder_datetxt);
            holder.msize = (TextView) convertView.findViewById(R.id.confirmorder_sizetxt);
            holder.mconfirmorder_custom_dimensiontxt = (TextView) convertView.findViewById(R.id.confirmorder_custom_dimensiontxt);
            holder.mCustomDimensuionLL = (LinearLayout) convertView.findViewById(R.id.orderconfirm_custom_dimension_ll);
            holder.mprice = (TextView) convertView.findViewById(R.id.confirmorder_pricetxt);
            holder.message = (EditText) convertView.findViewById(R.id.confirm_message);
            holder.mbottom_view_layout = (LinearLayout)convertView.findViewById(R.id.bottom_view_layout);
            holder.morderconfirm_imageview = (ImageView)convertView.findViewById(R.id.orderconfirm_imageview);
            holder.morderconfirm_resourcestype_text = (TextView)convertView.findViewById(R.id.orderconfirm_resourcestype_text);
            holder.mOrderConfirmActualPriceTV = (TextView)convertView.findViewById(R.id.confirmorder_actual_price_tv);
            holder.mOrderConfirmUseCouponsLL = (LinearLayout)convertView.findViewById(R.id.confirmorder_use_coupons_ll);
            holder.mOrderConfirmCouponsTV = (TextView) convertView.findViewById(R.id.orderconfirm_coupons_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mname.setText(data.get(position).get("resourcename").toString());
        if (data.get(position).get("date") != null && data.get(position).get("date").toString().length() > 0) {
            if (data.get(position).get("lease_term_type_id") != null && data.get(position).get("lease_term_type_id").toString().length() > 0) {
                if (data.get(position).get("lease_term_type_id").toString().equals("1") || data.get(position).get("lease_term_type_id").toString().equals("2")) {
                    holder.mdate.setText(mactivity.getResources().getString(R.string.home_activity_item_timetxt)+data.get(position).get("date").toString());
                } else {
                    holder.mdate.setText(mactivity.getResources().getString(R.string.home_activity_item_timetxt)+"自"+data.get(position).get("date").toString()+"起 "+"("+data.get(position).get("lease_term_type")+")");
                }
            } else {
                holder.msize.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt)+mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
        } else {
            holder.msize.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt)+mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (data.get(position).get("size") != null && data.get(position).get("size").toString().length() > 0) {
            holder.msize.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt)+data.get(position).get("size").toString());
            if (data.get(position).get("lease_term_type") != null && data.get(position).get("lease_term_type").toString().length() > 0) {
                holder.msize.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt)+data.get(position).get("size").toString()+"("+data.get(position).get("lease_term_type")+")");
            }
        } else {
            holder.msize.setText(mactivity.getResources().getString(R.string.order_listitem_sizetxt)+mactivity.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (data.get(position).get("custom_dimension") != null && data.get(position).get("custom_dimension").toString().length() > 0) {
            holder.mCustomDimensuionLL.setVisibility(View.VISIBLE);
            holder.mconfirmorder_custom_dimensiontxt.setText(data.get(position).get("custom_dimension").toString());
        } else {
            holder.mCustomDimensuionLL.setVisibility(View.INVISIBLE);
        }
        if (mactivity.mGroupPayType == 1) {
            if (data.get(position).get("actual_fee") != null &&
                    data.get(position).get("actual_fee").toString().length() > 0) {
                if (data.get(position).get("subsidy_fee") != null &&
                        data.get(position).get("subsidy_fee").toString().length() > 0 &&
                        Double.parseDouble(data.get(position).get("subsidy_fee").toString()) > 0) {
                    if (data.get(position).get("price") != null &&
                            data.get(position).get("price").toString().length() > 0) {
                        holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                        Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01)), 11,0,1,false,0,0,
                                11,Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01).length() + 1 - 2,
                                Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01).length() + 1,
                                false,0,0));
                        holder.mOrderConfirmActualPriceTV.setText(Constants.getSpannableAllStr(mcontext,
                                (mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                                        Constants.getpricestring(data.get(position).get("price").toString(), 0.01)),
                                0,0,0,true,0,Constants.getpricestring(data.get(position).get("price").toString(), 0.01).length() + 1,null,0,0,false,0,0));

                    } else {
                        holder.mprice.setText("");
                        holder.mOrderConfirmActualPriceTV.setText("");
                    }
                } else {
                    holder.mOrderConfirmActualPriceTV.setText("");
                    if (data.get(position).get("actual_fee") != null &&
                            data.get(position).get("actual_fee").toString().length() > 0) {
                        holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                        Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01)), 11,0,1,false,0,0,
                                11,Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01).length() + 1 - 2,
                                Constants.getSearchPriceStr(data.get(position).get("actual_fee").toString(), 0.01).length() + 1,
                                false,0,0));
                    } else {
                        holder.mprice.setText("");
                    }
                }
            } else {
                holder.mOrderConfirmActualPriceTV.setText("");
                holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                Constants.getpricestring(data.get(position).get("actual_fee").toString(), 0.01)), 11,0,1,false,0,0,
                        11,Constants.getpricestring(data.get(position).get("actual_fee").toString(), 0.01).length() + 1 - 2,
                        Constants.getpricestring(data.get(position).get("actual_fee").toString(), 0.01).length() + 1,
                        false,0,0));
            }
        } else {
            if (data.get(position).get("price") != null &&
                    data.get(position).get("price").toString().length() > 0) {
                if (data.get(position).get("subsidy_fee") != null &&
                        data.get(position).get("subsidy_fee").toString().length() > 0 &&
                        Double.parseDouble(data.get(position).get("subsidy_fee").toString()) > 0) {
                    if (data.get(position).get("price") != null &&
                            data.get(position).get("price").toString().length() > 0) {
                        holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                        Constants.getSearchPriceStr(Constants.getdoublepricestring(Double.parseDouble(data.get(position).get("price").toString()) -
                                                Double.parseDouble(data.get(position).get("subsidy_fee").toString()),1), 0.01)), 11,0,1,false,0,0,
                                11,Constants.getSearchPriceStr(Constants.getdoublepricestring(Double.parseDouble(data.get(position).get("price").toString()) -
                                        Double.parseDouble(data.get(position).get("subsidy_fee").toString()),1), 0.01).length() + 1 - 2,
                                Constants.getSearchPriceStr(Constants.getdoublepricestring(Double.parseDouble(data.get(position).get("price").toString()) -
                                        Double.parseDouble(data.get(position).get("subsidy_fee").toString()),1), 0.01).length() + 1,
                                false,0,0));
                        holder.mOrderConfirmActualPriceTV.setText(Constants.getSpannableAllStr(mcontext,
                                (mcontext.getResources().getString(R.string.order_listitem_price_unit_text)+
                                        Constants.getpricestring(data.get(position).get("price").toString(), 0.01)),
                                0,0,0,true,0,Constants.getpricestring(data.get(position).get("price").toString(), 0.01).length() + 1,null,0,0,false,0,0));

                    } else {
                        holder.mprice.setText("");
                        holder.mOrderConfirmActualPriceTV.setText("");
                    }
                } else {
                    holder.mOrderConfirmActualPriceTV.setText("");
                    if (data.get(position).get("price") != null &&
                            data.get(position).get("price").toString().length() > 0) {
                        holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                        Constants.getSearchPriceStr(data.get(position).get("price").toString(), 0.01)), 11,0,1,false,0,0,
                                11,Constants.getSearchPriceStr(data.get(position).get("price").toString(), 0.01).length() + 1 - 2,
                                Constants.getSearchPriceStr(data.get(position).get("price").toString(), 0.01).length() + 1,
                                false,0,0));
                    } else {
                        holder.mprice.setText("");
                    }
                }
            } else {
                holder.mOrderConfirmActualPriceTV.setText("");
                holder.mprice.setText(Constants.getSpannableAllStr(mcontext,(mcontext.getResources().getString(R.string.order_listitem_price_unit_text) +
                                Constants.getpricestring(data.get(position).get("price").toString(), 0.01)), 11,0,1,false,0,0,
                        11,Constants.getpricestring(data.get(position).get("price").toString(), 0.01).length() + 1 - 2,
                        Constants.getpricestring(data.get(position).get("price").toString(), 0.01).length() + 1,
                        false,0,0));
            }
        }

        if (position == data.size() - 1) {
            holder.mbottom_view_layout.setVisibility(View.GONE);
        } else {
            holder.mbottom_view_layout.setVisibility(View.VISIBLE);
        }
        if (data.get(position).get("imagepath").toString().length() > 0) {
            Picasso.with(mactivity).load(data.get(position).get("imagepath").toString()).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(180, 180).into(holder.morderconfirm_imageview);
        } else {
            Picasso.with(mactivity).load(R.drawable.ic_no_pic_small).resize(180, 180).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.morderconfirm_imageview);
        }
        if (data.get(position).get("field_type") != null && data.get(position).get("field_type").toString().length() > 0) {
            holder.morderconfirm_resourcestype_text.setVisibility(View.VISIBLE);
            holder.morderconfirm_resourcestype_text.setText(data.get(position).get("field_type").toString());
        } else {
            holder.morderconfirm_resourcestype_text.setVisibility(View.GONE);
        }
        holder.mOrderConfirmUseCouponsLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mactivity.mIsAdv == 0) {
                    Intent useCouponsIntent = new Intent(mactivity,UseCouponsActivity.class);
                    useCouponsIntent.putExtra("communityids",(Serializable) mactivity.mCommunityIds);
                    useCouponsIntent.putExtra("actual_fee",data.get(position).get("actual_fee").toString());
                    useCouponsIntent.putExtra("res_id",Integer.parseInt(data.get(position).get("res_position").toString()));
                    if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())) != null &&
                            orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getId() > 0) {
                        ArrayList<Integer> useCouponsList = new ArrayList<>();
                        useCouponsList.addAll(mactivity.mUseCouponsList);
                        useCouponsList.remove(new Integer(orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getId()));
                        useCouponsIntent.putExtra("couponids",(Serializable) useCouponsList);
                        useCouponsIntent.putExtra("coupon_id",orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getId());
                    } else {
                        useCouponsIntent.putExtra("couponids",(Serializable) mactivity.mUseCouponsList);
                    }
                    mactivity.startActivityForResult(useCouponsIntent,2);
                }
            }
        });
        if (mactivity.mIsAdv == 0) {
            if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())) != null) {
                if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getId() == -1) {
                    holder.mOrderConfirmCouponsTV.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                    holder.mOrderConfirmCouponsTV.setText(mcontext.getResources().getString(R.string.module_orderconfirm_un_use_coupons));
                } else {
                    holder.mOrderConfirmCouponsTV.setTextColor(mcontext.getResources().getColor(R.color.headline_tv_color));
                    String hintStr1 = "";
                    String hintStr2 = "";
                    if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getScope() == 1) {
                        hintStr1 = mcontext.getResources().getString(R.string.module_orderconfirm_item_all_type_coupon);
                    } else if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getScope() == 2) {
                        hintStr1 = mcontext.getResources().getString(R.string.module_orderconfirm_item_field_type_coupon);
                    } else if (orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getScope() == 3) {
                        hintStr1 = mcontext.getResources().getString(R.string.module_orderconfirm_item_category_type_coupon);
                    }
                    if (Double.parseDouble(orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getMin_goods_amount()) > 0) {
                        hintStr2 = mcontext.getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
                                Constants.getpricestring(orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getMin_goods_amount(),1) +
                                mcontext.getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                Constants.getpricestring(orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getAmount(),1);
                    } else {
                        hintStr2 = Constants.getpricestring(orderCouponsMap.get(Integer.parseInt(data.get(position).get("res_position").toString())).getAmount(),1) +
                                mcontext.getResources().getString(R.string.term_types_unit_txt) +
                                mcontext.getResources().getString(R.string.module_fieldinfo_coupons_no_threshold);
                    }
                    holder.mOrderConfirmCouponsTV.setText(hintStr1 + hintStr2);
                }
            } else {
                holder.mOrderConfirmCouponsTV.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
                holder.mOrderConfirmCouponsTV.setText(mcontext.getResources().getString(R.string.module_orderconfirm_no_coupons));
            }
        } else {
            holder.mOrderConfirmCouponsTV.setTextColor(mcontext.getResources().getColor(R.color.register_edit_color));
            holder.mOrderConfirmCouponsTV.setText(mcontext.getResources().getString(R.string.module_orderconfirm_no_coupons));
        }

        if (leaveMap.get(Integer.parseInt(data.get(position).get("id").toString())) != null) {
            holder.message.setText(leaveMap.get(Integer.parseInt(data.get(position).get("id").toString())));
        }
        setOnFocusChange(Integer.parseInt(data.get(position).get("id").toString()),
                holder.message);
        return convertView;
    }

    static class ViewHolder {
        public TextView mname;
        public TextView msize;
        public TextView mconfirmorder_custom_dimensiontxt;
        public LinearLayout mCustomDimensuionLL;
        public TextView mdate;
        public TextView mprice;
        public EditText message;
        public LinearLayout mbottom_view_layout;
        public ImageView morderconfirm_imageview;
        public TextView morderconfirm_resourcestype_text;
        public TextView mOrderConfirmActualPriceTV;
        public LinearLayout mOrderConfirmUseCouponsLL;
        public TextView mOrderConfirmCouponsTV;
    }

    public static HashMap<Integer, MyCouponsModel> getOrderCouponsMap() {
        return orderCouponsMap;
    }

    public static void setOrderCouponsMap(HashMap<Integer, MyCouponsModel> orderCouponsMap) {
        OrderConfirmAdapter.orderCouponsMap = orderCouponsMap;
    }
    public static void clearOrderCouponsMap() {
        if (orderCouponsMap != null) {
            orderCouponsMap.clear();
        }
    }

    public static HashMap<Integer, String> getLeaveMap() {
        return leaveMap;
    }

    public static void setLeaveMap(HashMap<Integer, String> leaveMap) {
        OrderConfirmAdapter.leaveMap = leaveMap;
    }

    public static void clearLeaveMap() {
        if (leaveMap != null) {
            leaveMap.clear();
        }
    }
    private void setOnFocusChange (final int id, final EditText editText) {
        editText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    leaveMap.put(id,editText.getText().toString());
                }
            }
        });

    }

}
