package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.OrderConfirmAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.OrderConfirmMvpPresenter;
import com.linhuiba.business.mvpview.OrderConfirmMvpView;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/8.
 */
public class OrderConfirmActivity extends BaseMvpActivity implements OrderConfirmMvpView {
    @InjectView(R.id.orderconfirmname_txt)
    TextView morderconfirmname_txt;
    @InjectView(R.id.orderconfirmaccount_txt)
    TextView morderconfirmaccount_txt;
    @InjectView(R.id.orderconfirmname_textview)
    TextView morderconfirmname_textview;
    @InjectView(R.id.orderconfirmaccount_textview)
    TextView morderconfirmaccount_textview;
    @InjectView(R.id.orderconfirm_listview)
    MyGridview morderconfirm_listview;
    @InjectView(R.id.submit_btn)
    Button msubmit_btn;
    @InjectView(R.id.orderconfirm_resources_total_amount_text)
    TextView morderconfirm_resources_total_amount_text;
    @InjectView(R.id.orderconfirm_deposit_textview)
    TextView morderconfirm_deposit_textview;
    @InjectView(R.id.orderconfirm_deposit_layout)
    RelativeLayout morderconfirm_deposit_layout;
    @InjectView(R.id.orderconfirm_total_amount_text)
    TextView morderconfirm_total_amount_text;
    @InjectView(R.id.orderconfirm_service_fee_ll)
    RelativeLayout mOrderServiceFeeLL;
    @InjectView(R.id.orderconfirm_service_fee_tv)
    TextView mOrderServiceFeeTV;
    @InjectView(R.id.orderconfirm_remind_rl)
    RelativeLayout mOrderConfirmInvoiceRemindRL;
    @InjectView(R.id.orderconfirm_agreement_checkbox)
    CheckBox mOrderconfirmAgreementCB;
    @InjectView(R.id.orderconfirm_coupons_tv)
    TextView mOrderconfirmCouponTV;
    @InjectView(R.id.orderconfirm_coupons_rl)
    RelativeLayout mOrderconfirmCouponRL;

    private ArrayList<HashMap<String, Object>> orderconfirmlist = new ArrayList<>();
    private OrderConfirmAdapter orderConfirmAdapter;
    private int ordertype;//1详情3购物车
    private int delegated;//需要兼职 1
    private int need_decoration;//需要布置 1
    private int need_transportation;//需要运输 1
    public int need_invoice = 0;//需要发票 1
    private int payprice;//接口需要的支付金额
    private ArrayList<Integer> cart_item_ids = new ArrayList<>();//购物车传来的购物车的id列表
    //    private Dialog pw_dialog;
    private double deposit;//押金
    private String mOrderAddressStr = "";
    public int mGroupPayType;//1是拼团
    private int mGroupId;
    private AddressContactModel addressContactModel;//默认联系人信息model
    private double mServiceFee;//服务费 元
    private int inquiryInt;
    private InvoiceInfomationModel invoiceInfomationModeldata;
    private static final int ORDER_CHOOSE_ACCOUNT_TYPE = 1;
    private static final int ORDER_ADD_ACCOUNT_TYPE = 5;
    public ArrayList<Integer> mCommunityIds = new ArrayList<>();
    private OrderConfirmMvpPresenter mPresenter;
    private ArrayList<MyCouponsModel> mOrderCouponsList;//场地对应的优惠券数组
    public ArrayList<Integer> mUseCouponsList = new ArrayList<>();//使用过的优惠券id数组
    private Map<Integer,ArrayList<HashMap<String,String>>> mOrderCommunityResMap = new HashMap<>();//场地对应的所有供给
    private CustomDialog mNoticeDialog;
    private double mCouponsPrice;
    private Dialog mShowMsgDialog;
    public int mIsAdv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        initview();
        initdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.confirm_order_activity_name_str));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.confirm_order_activity_name_str));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initview() {
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.confirmorder_title_txt));
        TitleBarUtils.showBackImg(this, true);
        mPresenter = new OrderConfirmMvpPresenter();
        mPresenter.attachView(this);
        Intent OrderConfirm = getIntent();
        if (OrderConfirm.getExtras().get("community_ids") != null) {
            mCommunityIds = (ArrayList<Integer>) OrderConfirm.getSerializableExtra("community_ids");
        }
        if (OrderConfirm.getExtras().get("is_adv") != null) {
            mIsAdv = OrderConfirm.getExtras().getInt("is_adv");
        }
        if (OrderConfirm.getExtras().get("submitorderlist") != null) {
            orderconfirmlist = (ArrayList<HashMap<String, Object>>) OrderConfirm.getSerializableExtra("submitorderlist");
            if (mIsAdv == 0) {
                for (int i = 0; i < orderconfirmlist.size(); i++) {
                    orderconfirmlist.get(i).put("res_position",i);
                    if (mOrderCommunityResMap.get(Integer.parseInt(orderconfirmlist.get(i).get("community_id").toString())) != null) {
                        ArrayList<HashMap<String,String>> ids = mOrderCommunityResMap.get(Integer.parseInt(orderconfirmlist.get(i).get("community_id").toString()));
                        HashMap<String,String> map = new HashMap<>();
                        map.put("id",orderconfirmlist.get(i).get("res_position").toString());
                        if (orderconfirmlist.get(i).get("price") != null &&
                                orderconfirmlist.get(i).get("price").toString().length() > 0) {
                            if (orderconfirmlist.get(i).get("subsidy_fee") != null &&
                                    orderconfirmlist.get(i).get("subsidy_fee").toString().length() > 0 &&
                                    Double.parseDouble(orderconfirmlist.get(i).get("subsidy_fee").toString()) > 0) {
                                map.put("price",Constants.getdoublepricestring(
                                        Double.parseDouble(orderconfirmlist.get(i).get("price").toString()) -
                                                Double.parseDouble(orderconfirmlist.get(i).get("subsidy_fee").toString()),1));
                                orderconfirmlist.get(i).put("actual_fee",map.get("price"));
                            } else {
                                map.put("price",orderconfirmlist.get(i).get("price").toString());
                                orderconfirmlist.get(i).put("actual_fee",map.get("price"));
                            }
                        }
                        ids.add(map);
                        mOrderCommunityResMap.put(Integer.parseInt(orderconfirmlist.get(i).get("community_id").toString()),ids);
                    } else {
                        HashMap<String,String> map = new HashMap<>();
                        map.put("id",orderconfirmlist.get(i).get("res_position").toString());
                        if (orderconfirmlist.get(i).get("price") != null &&
                                orderconfirmlist.get(i).get("price").toString().length() > 0) {
                            if (orderconfirmlist.get(i).get("subsidy_fee") != null &&
                                    orderconfirmlist.get(i).get("subsidy_fee").toString().length() > 0 &&
                                    Double.parseDouble(orderconfirmlist.get(i).get("subsidy_fee").toString()) > 0) {
                                map.put("price",Constants.getdoublepricestring(
                                        Double.parseDouble(orderconfirmlist.get(i).get("price").toString()) -
                                                Double.parseDouble(orderconfirmlist.get(i).get("subsidy_fee").toString()),1));
                                orderconfirmlist.get(i).put("actual_fee",map.get("price"));
                            } else {
                                map.put("price",orderconfirmlist.get(i).get("price").toString());
                                orderconfirmlist.get(i).put("actual_fee",map.get("price"));
                            }
                        }
                        ArrayList<HashMap<String,String>> ids = new ArrayList<>();
                        ids.add(map);
                        mOrderCommunityResMap.put(Integer.parseInt(orderconfirmlist.get(i).get("community_id").toString()),ids);
                    }
                }
            }
        }
        if (OrderConfirm.getExtras().get("ordertype") != null) {
            ordertype = OrderConfirm.getExtras().getInt("ordertype");
        }
        if (OrderConfirm.getExtras().get("deposit") != null) {
            deposit = OrderConfirm.getExtras().getDouble("deposit");
        }
        if (OrderConfirm.getExtras().get("service_fee") != null) {
            mServiceFee = OrderConfirm.getExtras().getDouble("service_fee");
        }
        if (OrderConfirm.getExtras().get("inquiry") != null) {
            inquiryInt = OrderConfirm.getExtras().getInt("inquiry");
        }
        if (OrderConfirm.getExtras().get("mGroupPayType") != null) {
            mGroupPayType = OrderConfirm.getExtras().getInt("mGroupPayType");
            if (mGroupPayType == 1) {
                if (OrderConfirm.getExtras().get("group_id") != null) {
                    mGroupId = OrderConfirm.getExtras().getInt("group_id");
                }
            }
        }
        if (mServiceFee > 0) {
            mOrderServiceFeeTV.setText(Constants.getPriceUnitStr(OrderConfirmActivity.this, getResources().getString(R.string.order_listitem_price_unit_text) +
                    Constants.getdoublepricestring(mServiceFee, 1), 10));
            mOrderServiceFeeLL.setVisibility(View.VISIBLE);
        } else {
            mOrderServiceFeeLL.setVisibility(View.GONE);
        }
        if (deposit > 0) {
            morderconfirm_deposit_textview.setText(Constants.getPriceUnitStr(OrderConfirmActivity.this, getResources().getString(R.string.order_listitem_price_unit_text) +
                    Constants.getdoublepricestring(deposit, 1), 10));
            morderconfirm_deposit_layout.setVisibility(View.VISIBLE);
        } else {
            morderconfirm_deposit_layout.setVisibility(View.GONE);
        }
        if (OrderConfirm.getExtras().get("cart_item_ids") != null) {
            cart_item_ids = (ArrayList<Integer>) OrderConfirm.getSerializableExtra("cart_item_ids");
        }
        orderConfirmAdapter = new OrderConfirmAdapter(this, this, orderconfirmlist, 0);
        morderconfirm_listview.setAdapter(orderConfirmAdapter);
        morderconfirm_listview.setFocusable(false);
        morderconfirm_resources_total_amount_text.setText(Constants.getPriceUnitStr(OrderConfirmActivity.this, (getResources().getString(R.string.order_listitem_price_unit_text) +
                Constants.getdoublepricestring(getpayprice(), 0.01)), 11));
        morderconfirm_total_amount_text.setText(Constants.getSpannableAllStr(this, (getResources().getString(R.string.order_listitem_price_unit_text) +
                        Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01)), 11, 0, 1, false, 0, 0,
                11, Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1 - 2,
                Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1,
                true, 1, Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1 - 3));
        if ((getprice() - getsubsidy_fee() > 2090000000) || (getprice() - getsubsidy_fee() == 2090000000)) {
            MessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
            msubmit_btn.setEnabled(false);
            msubmit_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
        } else {
            payprice = Integer.parseInt(Constants.getorderdoublepricestring((getprice() - getsubsidy_fee()), 1));
            if (LoginManager.getOrdering_noticed() == 0) {
                showCreateOrderNoticeDialog();
                mOrderconfirmAgreementCB.setChecked(false);
                msubmit_btn.setEnabled(false);
                msubmit_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
            } else {
                mOrderconfirmAgreementCB.setChecked(true);
                msubmit_btn.setEnabled(true);
                msubmit_btn.setBackgroundColor(getResources().getColor(R.color.groupbooking_confirm_order_btn_bg));
            }
        }
    }

    private void initdata() {
        //多个场地的优惠券列表
        if (mCommunityIds != null && mCommunityIds.size() > 0) {
            mPresenter.getOrderCommunityCoupons(mCommunityIds,null);
        }
        mPresenter.getDefaultAddress();
    }

    @OnClick({
            R.id.submit_btn,
            R.id.orderconfir_addresslayout,
            R.id.orderconfirm_remind_imgBtn,
            R.id.orderconfirm_notice_tv,
            R.id.orderconfirm_coupons_imgv
    })
    public void orderconfirintent(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (morderconfirmname_txt.getText().toString().length() == 0) {
                    MessageUtils.showToast(getContext(), OrderConfirmActivity.this.getResources().getString(R.string.invoiceinfo_account_errormsg));
                    gotoAccountActivity();
                    return;
                }
                if (morderconfirmaccount_txt.getText().toString().length() == 0) {
                    MessageUtils.showToast(getContext(), OrderConfirmActivity.this.getResources().getString(R.string.invoiceinfo_account_errormsg));
                    gotoAccountActivity();
                    return;
                }
                com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
                for (int i = 0; i < morderconfirm_listview.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) morderconfirm_listview.getChildAt(i);// 获得子item的layout
                    EditText message_edit = (EditText) layout.findViewById(R.id.confirm_message);//
                    JSONObject json = new JSONObject();
                    json.put("id", Integer.parseInt(orderconfirmlist.get(i).get("id").toString()));
                    json.put("date", orderconfirmlist.get(i).get("date"));
                    if (orderconfirmlist.get(i).get("size") != null) {
                        json.put("size", orderconfirmlist.get(i).get("size"));
                    }
                    json.put("custom_dimension", orderconfirmlist.get(i).get("custom_dimension").toString());
                    json.put("lease_term_type_id", Integer.parseInt(orderconfirmlist.get(i).get("lease_term_type_id").toString()));
                    json.put("count", Integer.parseInt(orderconfirmlist.get(i).get("count").toString()));
                    json.put("count_of_time_unit", Integer.parseInt(orderconfirmlist.get(i).get("count_of_time_unit").toString()));
                    json.put("leave_words", message_edit.getText().toString());
                    if (orderconfirmlist.get(i).get("enquiry_id") != null) {
                        json.put("enquiry_id", orderconfirmlist.get(i).get("enquiry_id").toString());
                    }
                    if (mIsAdv == 0) {
                        if (OrderConfirmAdapter.getOrderCouponsMap().get(
                                Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())) != null &&
                                OrderConfirmAdapter.getOrderCouponsMap().get(
                                        Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())).getId() > 0) {
                            json.put("coupon_id", OrderConfirmAdapter.getOrderCouponsMap().get(
                                    Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())).getId());
                        }
                    }
                    jsonArray.add(json);
                }
                if (ordertype == 1) {
                    showProgressDialog();
                    //立即下单
                    mPresenter.createOrderPay(jsonArray,
                            Integer.parseInt(Constants.getorderdoublepricestring(getOrderActualFee(), 1)), delegated, need_decoration, need_transportation, need_invoice, morderconfirmname_txt.getText().toString()
                            , morderconfirmaccount_txt.getText().toString(), mOrderAddressStr, null, invoiceInfomationModeldata);
                } else if (ordertype == 2) {

                } else if (ordertype == 3) {
                    if (cart_item_ids.size() > 0) {
                        showProgressDialog();
                        //购物车结算
                        mPresenter.createOrderPay(jsonArray, Integer.parseInt(Constants.getorderdoublepricestring(getOrderActualFee(), 1)),
                                delegated, need_decoration, need_transportation, need_invoice, morderconfirmname_txt.getText().toString(),
                                morderconfirmaccount_txt.getText().toString(), mOrderAddressStr, JSON.toJSONString(cart_item_ids, true), invoiceInfomationModeldata);
                    }
                }
                break;
            case R.id.orderconfir_addresslayout:
                Intent managecontact = new Intent(OrderConfirmActivity.this, ManageContactActivity.class);
                managecontact.putExtra("intentType", ORDER_CHOOSE_ACCOUNT_TYPE);
                startActivityForResult(managecontact, ORDER_CHOOSE_ACCOUNT_TYPE);
                break;
            case R.id.orderconfirm_remind_imgBtn:
                mOrderConfirmInvoiceRemindRL.setVisibility(View.GONE);
                break;
            case R.id.orderconfirm_notice_tv:
                showCreateOrderNoticeDialog();
                break;
            case R.id.orderconfirm_coupons_imgv:
                if (Constants.isFastClick()) {
                    show_paysuccess_PopupWindow(0);
                }
                break;
            default:
                break;
        }
    }

    public double getprice() {
        double fieldprice = 0;
        for (int i = 0; i < orderconfirmlist.size(); i++) {
//            if (mGroupPayType == 1) {
//                if (orderconfirmlist.get(i).get("actual_fee").toString() != null) {
//                    if (orderconfirmlist.get(i).get("actual_fee").toString().length() > 0) {
//                        double price_tem = (Integer.parseInt(orderconfirmlist.get(i).get("actual_fee").toString())) * Integer.parseInt(orderconfirmlist.get(i).get("count").toString());
//                        fieldprice = fieldprice + Double.parseDouble(Constants.getdoublepricestring(price_tem, 1));
//                    }
//                }
//
//            } else {
            if (orderconfirmlist.get(i).get("price").toString() != null) {
                if (orderconfirmlist.get(i).get("price").toString().length() > 0) {
                    double price_tem = (Integer.parseInt(orderconfirmlist.get(i).get("price").toString())) * Integer.parseInt(orderconfirmlist.get(i).get("count").toString());
                    fieldprice = fieldprice + Double.parseDouble(Constants.getdoublepricestring(price_tem, 1));
                }
            }
//            }
        }
        return fieldprice;
    }

    public double getsubsidy_fee() {
        double fieldprice = 0;
        for (int i = 0; i < orderconfirmlist.size(); i++) {
            if (orderconfirmlist.get(i).get("price").toString() != null) {
                if (orderconfirmlist.get(i).get("price").toString().length() > 0) {
                    double price_tem = 0;
                    if (orderconfirmlist.get(i).get("subsidy_fee") != null &&
                            orderconfirmlist.get(i).get("subsidy_fee").toString().length() > 0) {
                        price_tem = (Double.parseDouble(orderconfirmlist.get(i).get("subsidy_fee").toString())) * Integer.parseInt(orderconfirmlist.get(i).get("count").toString());
                    } else {

                    }
                    fieldprice = fieldprice + Double.parseDouble(Constants.getdoublepricestring(price_tem, 1));
                }
            }
        }
        return fieldprice;
    }

    private double getpayprice() {
        double payprice = 0;
        payprice = getprice() - getsubsidy_fee();
        return payprice;
    }

    private double getOrderActualFee() {
        double actual_fee = 0;
        actual_fee = getpayprice() + deposit * 100 + mServiceFee * 100 - mCouponsPrice * 100;
        return actual_fee;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ORDER_CHOOSE_ACCOUNT_TYPE:
                setResultAccountData(data);
                break;
            case ORDER_ADD_ACCOUNT_TYPE:
                setResultAccountData(data);
                break;
            case 2:
                //返回键操作
                if (data.getExtras().get("type") != null) {
                    if (data.getExtras().getInt("type") == 2) {
                        MyCouponsModel myCouponsModel = null;
                        if (data.getExtras().get("model") != null &&
                                data.getExtras().get("res_id") != null) {
                            myCouponsModel = (MyCouponsModel) data.getExtras().get("model");
                            if (data.getExtras().get("coupon_id") != null) {
                                mUseCouponsList.remove(new Integer(data.getExtras().getInt("coupon_id")));
                            }
                            mUseCouponsList.add(myCouponsModel.getId());
                            OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(data.getExtras().get("res_id").toString()),
                                    myCouponsModel);
                            orderConfirmAdapter.notifyDataSetChanged();
                        } else {
                            if (data.getExtras().get("coupon_id") != null) {
                                mUseCouponsList.remove(new Integer(data.getExtras().getInt("coupon_id")));
                            }
                            if (data.getExtras().get("res_id") != null) {
                                myCouponsModel = new MyCouponsModel();
                                myCouponsModel.setId(-1);
                                OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(data.getExtras().get("res_id").toString()),
                                        myCouponsModel);
                                orderConfirmAdapter.notifyDataSetChanged();
                            }
                        }
                        setCouponsTVStr();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void gotoAccountActivity() {
        Intent editoraddress = new Intent(OrderConfirmActivity.this, InvoiceInfoEditorAddress.class);
        editoraddress.putExtra("Operationtype", ORDER_ADD_ACCOUNT_TYPE);
        editoraddress.putExtra("order_choose", ORDER_CHOOSE_ACCOUNT_TYPE);
        startActivityForResult(editoraddress, ORDER_ADD_ACCOUNT_TYPE);
    }

    private void setResultAccountData(Intent data) {
        if (data != null && data.getExtras() != null &&
                data.getExtras().get("AddressContactModel") != null) {
            if (data.getExtras().get("AddressContactModel").toString().length() > 0) {
                AddressContactModel addressContactModel = (AddressContactModel) data.getSerializableExtra("AddressContactModel");
                if (addressContactModel.toString().length() > 0) {
                    if (addressContactModel.getName() != null &&
                            addressContactModel.getName().length() > 0) {
                        morderconfirmname_textview.setText(getResources().getString(R.string.txt_order_connect));
                        morderconfirmname_txt.setText(addressContactModel.getName());
                    } else {
                        morderconfirmname_txt.setText("");
                    }
                    if (addressContactModel.getMobile() != null &&
                            addressContactModel.getMobile().length() > 0) {
                        morderconfirmaccount_textview.setText(getResources().getString(R.string.txt_field_mobile));
                        morderconfirmaccount_txt.setText(addressContactModel.getMobile());
                    } else {
                        morderconfirmaccount_txt.setText("");
                    }
                }
            }
        }
    }

    @Override
    public void onDefaultAddressSuccess(AddressContactModel model) {
        addressContactModel = model;
        if (addressContactModel != null) {
            if (addressContactModel.getName() != null) {
                morderconfirmname_textview.setText(getResources().getString(R.string.txt_order_connect));
                morderconfirmname_txt.setText(addressContactModel.getName());
            }
            if (addressContactModel.getMobile() != null) {
                morderconfirmaccount_textview.setText(getResources().getString(R.string.txt_field_mobile));
                morderconfirmaccount_txt.setText(addressContactModel.getMobile());
            }
        }
    }

    @Override
    public void onDefaultAddressFailure(boolean superresult, Throwable error) {
        checkAccess(error);
    }

    @Override
    public void onUserProfileSuccess(Object data) {

    }

    @Override
    public void onUserProfileFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess(error);
    }

    @Override
    public void onCreateOrderPaySuccess(Object data) {
        if (data != null && data instanceof JSONObject && JSONObject.parseObject(data.toString()) != null &&
                JSONObject.parseObject(data.toString()).get("order_id") != null && JSONObject.parseObject(data.toString()).get("order_num") != null &&
                JSONObject.parseObject(data.toString()).get("order_id").toString().length() > 0 &&
                JSONObject.parseObject(data.toString()).get("order_num").toString().length() > 0) {
            Intent choosepayway_intent = new Intent(OrderConfirmActivity.this, OrderConfirmChoosePayWayActivity.class);
            if (mGroupPayType == 1) {
                //拼团传的参数
                choosepayway_intent.putExtra("payment_option", 3);
                choosepayway_intent.putExtra("group_id", mGroupId);
            } else {
                choosepayway_intent.putExtra("payment_option", 1);
            }
            choosepayway_intent.putExtra("order_id", JSONObject.parseObject(data.toString()).get("order_id").toString());
            choosepayway_intent.putExtra("order_num", JSONObject.parseObject(data.toString()).get("order_num").toString());
            choosepayway_intent.putExtra("order_price", String.valueOf(payprice + deposit* 100 +
                    mServiceFee * 100 - mCouponsPrice * 100));
            startActivity(choosepayway_intent);
            OrderConfirmActivity.this.finish();
        }
    }

    @Override
    public void onCreateOrderPayFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onOrderCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        mOrderCouponsList = data;
        if (mOrderCouponsList != null && mOrderCouponsList.size() > 0) {
            if (mUseCouponsList != null) {
                mUseCouponsList.clear();
            }
            OrderConfirmAdapter.clearOrderCouponsMap();
            for (int j = 0; j < mOrderCouponsList.size(); j++) {
                ArrayList<MyCouponsModel> couponsList = mOrderCouponsList.get(j).getCoupons();
                ArrayList<HashMap<String,String>> resIds = mOrderCommunityResMap.get(mOrderCouponsList.get(j).getId());
                if (resIds != null && resIds.size() > 0) {
                    for (int i = 0; i < resIds.size(); i++) {
                        for (int k = 0; k < couponsList.size(); k++) {
                            if (couponsList.get(k).getMin_goods_amount() == null ||
                                    couponsList.get(k).getMin_goods_amount().length() == 0 ||
                                    Double.parseDouble(couponsList.get(k).getMin_goods_amount()) == 0) {
                                if (couponsList.get(k).getId() > 0 &&
                                        (Double.parseDouble(resIds.get(i).get("price").toString()) * 0.01 > //判断优惠券抵用价格是否符合
                                                Double.parseDouble(couponsList.get(k).getAmount()) ||
                                        Double.parseDouble(couponsList.get(k).getAmount()) ==
                                                Double.parseDouble(resIds.get(i).get("price").toString()) * 0.01)) {
                                    if (mUseCouponsList.size() > 0) { //分配优惠券
                                        if (!mUseCouponsList.contains(new Integer(couponsList.get(k).getId()))) {
                                            mUseCouponsList.add(couponsList.get(k).getId());
                                            OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(resIds.get(i).get("id")),
                                                    couponsList.get(k));
                                            break;
                                        }
                                    } else {
                                        mUseCouponsList.add(couponsList.get(k).getId());
                                        OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(resIds.get(i).get("id")),
                                                couponsList.get(k));
                                        break;
                                    }
                                }
                            } else {
                                if (couponsList.get(k).getId() > 0 &&
                                        (Double.parseDouble(resIds.get(i).get("price").toString()) * 0.01 > //判断优惠券抵用价格是否符合
                                                Double.parseDouble(couponsList.get(k).getMin_goods_amount()) ||
                                        Double.parseDouble(couponsList.get(k).getMin_goods_amount()) ==
                                                Double.parseDouble(resIds.get(i).get("price").toString()) * 0.01)) {
                                    if (mUseCouponsList.size() > 0) { //分配优惠券
                                        if (!mUseCouponsList.contains(new Integer(couponsList.get(k).getId()))) {
                                            mUseCouponsList.add(couponsList.get(k).getId());
                                            OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(resIds.get(i).get("id")),
                                                    couponsList.get(k));
                                            break;
                                        }
                                    } else {
                                        mUseCouponsList.add(couponsList.get(k).getId());
                                        OrderConfirmAdapter.getOrderCouponsMap().put(Integer.parseInt(resIds.get(i).get("id")),
                                                couponsList.get(k));
                                        break;
                                    }
                                }
                            }
                        }
                }

                }
            }
            orderConfirmAdapter.notifyDataSetChanged();
            setCouponsTVStr();
        }
    }

    @Override
    public void onOrderCouponsListFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onOrderNoticedSuccess() {
        LoginManager.setOrdering_noticed(1);
    }

    private void showCreateOrderNoticeDialog() {
        if (mNoticeDialog == null || !mNoticeDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.module_orderconfirm_notice_refuce_tv:
                            //下单须知确定按钮操作
                            mOrderconfirmAgreementCB.setChecked(false);
                            mNoticeDialog.dismiss();
                            msubmit_btn.setEnabled(false);
                            msubmit_btn.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
                            break;
                        case R.id.module_orderconfirm_notice_agee_tv:
                            mOrderconfirmAgreementCB.setChecked(true);
                            mNoticeDialog.dismiss();
                            if (LoginManager.getOrdering_noticed() == 0) {
                                mPresenter.setOrderNoticed(1);
                            }
                            msubmit_btn.setEnabled(true);
                            msubmit_btn.setBackgroundColor(getResources().getColor(R.color.groupbooking_confirm_order_btn_bg));
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(OrderConfirmActivity.this);
            mNoticeDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.module_dialog_orderconfirm_notic)
                    .addViewOnclick(R.id.module_orderconfirm_notice_refuce_tv,uploadListener)
                    .addViewOnclick(R.id.module_orderconfirm_notice_agee_tv,uploadListener)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(OrderConfirmActivity.this,mNoticeDialog);
            mNoticeDialog.show();
        }
    }
    private void setCouponsTVStr() {
        double price = 0;
        if (mUseCouponsList != null &&
                mUseCouponsList.size() > 0 &&
                OrderConfirmAdapter.getOrderCouponsMap() != null &&
                OrderConfirmAdapter.getOrderCouponsMap().size() > 0) {
            for (int i = 0; i < orderconfirmlist.size(); i++) {
                if (OrderConfirmAdapter.getOrderCouponsMap().get(Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())) != null &&
                        OrderConfirmAdapter.getOrderCouponsMap().get(Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())).getId() > 0) {
                    price = price + Double.parseDouble(OrderConfirmAdapter.getOrderCouponsMap().get(Integer.parseInt(orderconfirmlist.get(i).get("res_position").toString())).getAmount());
                }
            }
            mCouponsPrice = price;
            if (price > 0) {
                mOrderconfirmCouponRL.setVisibility(View.VISIBLE);
                mOrderconfirmCouponTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                        Constants.getdoublepricestring(price,1));
            } else {
                mOrderconfirmCouponRL.setVisibility(View.GONE);
            }
        } else {
            mCouponsPrice = price;
            mOrderconfirmCouponRL.setVisibility(View.GONE);
        }
        morderconfirm_total_amount_text.setText(Constants.getSpannableAllStr(this, (getResources().getString(R.string.order_listitem_price_unit_text) +
                        Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01)), 11, 0, 1, false, 0, 0,
                11, Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1 - 2,
                Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1,
                true, 1, Constants.getSearchPriceStr(Constants.getdoublepricestring(getOrderActualFee(), 1), 0.01).length() + 1 - 3));
    }
    private void show_paysuccess_PopupWindow(final int type) {
        if (mShowMsgDialog != null && mShowMsgDialog.isShowing()) {
            mShowMsgDialog.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(OrderConfirmActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_fieldinfo_refund_price_popuwindow, null);
        TextView mrefunt_price_detailed = (TextView) textEntryView.findViewById(R.id.refunt_price_detailed);
        ImageButton mrefunt_price_detailed_btn = (ImageButton) textEntryView.findViewById(R.id.fieldinfo_explain_close_imgbtn);
        RelativeLayout mfieldinfo_all_dialog_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_all_dialog_layout);
        RelativeLayout mfieldinfo_refunt_price_detailed_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_refunt_price_detailed_layout);
        RelativeLayout mfieldinfo_error_recovery_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_layout);
        LinearLayout mfieldinfo_error_recovery_top_layout = (LinearLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_top_layout);
        TextView merror_correction_cancel_text = (TextView) textEntryView.findViewById(R.id.error_correction_cancel_text);
        TextView merror_recovery_resourcesname_text = (TextView) textEntryView.findViewById(R.id.error_recovery_resourcesname_text);
        TextView mrefunt_price_text = (TextView) textEntryView.findViewById(R.id.refunt_price_text);
        final EditText merror_recovery_content_edit = (EditText) textEntryView.findViewById(R.id.error_recovery_content_edit);
        if (type == 0) {
            mrefunt_price_detailed_btn.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.VISIBLE);
            mfieldinfo_error_recovery_layout.setVisibility(View.GONE);
            if (type == 0) {
                mrefunt_price_text.setText(getResources().getString(R.string.module_coupons_regulation));
                mrefunt_price_detailed.setText(getResources().getString(R.string.module_orderconfirm_coupons_explain_hint));
            }
        }
        mShowMsgDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mShowMsgDialog);
        mrefunt_price_detailed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowMsgDialog.isShowing()) {
                    mShowMsgDialog.dismiss();
                }
            }
        });
        merror_correction_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowMsgDialog.isShowing()) {
                    mShowMsgDialog.dismiss();
                }
            }
        });
    }

}
