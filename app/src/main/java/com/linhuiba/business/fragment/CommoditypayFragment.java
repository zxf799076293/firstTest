package com.linhuiba.business.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.OrderConfirmActivity;
import com.linhuiba.business.activity.ResourcesCartItemsActivity;
import com.linhuiba.business.adapter.CommoditypayAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.CommoditypayModel;
import com.linhuiba.business.mvppresenter.CartItemMvpPresenter;
import com.linhuiba.business.mvpview.CartItemMvpView;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/1.
 */
public class CommoditypayFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener,
        Field_AddFieldChoosePictureCallBack.ShopCartCall,
        Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,
        CartItemMvpView {
    @InjectView(R.id.order_loadmore_list)
    ExpandableListView mexpendlist;
    @InjectView(R.id.order_swipe_refresh)
    SwipeRefreshLayout mcommondity_swipe_refresh;
    @InjectView(R.id.commoditypay_nodata)
    RelativeLayout mcommoditypay_nodata;
    @InjectView(R.id.commodity_jump_home)
    TextView mcommodity_jump_home;
    @InjectView(R.id.commodity_selectall_checkbox)
    CheckBox mcommodity_selectall_checkbox;
    @InjectView(R.id.total_text)
    TextView mtotal_text;
    @InjectView(R.id.linearlayout_check)
    LinearLayout mlinearlayout_check;
    @InjectView(R.id.btn_pay)
    public Button mbtn_pay;
    @InjectView(R.id.commodity_title_layout)
    LinearLayout mCommdityTitleLL;
    @InjectView(R.id.cart_item_statusbar_ll)
    LinearLayout mCartItemStatusbarLL;
    @InjectView(R.id.card_failure_ll)
    LinearLayout mCartItemFailerLL;
    @InjectView(R.id.card_failure_tv)
    TextView mCartItemFailerTV;

    private View mMainContent;
    private CommoditypayAdapter madapter;
    public ArrayList<CommoditypayModel> CommdityList = new ArrayList<>();//购物车所有的list不分组
    private int shopcardlistpagesize = 0;
    private int advertsing_choosenum = 1;
    private int shopcardrestart = -1;
    private ArrayList<Integer> deleteshopcartlist = new ArrayList<>();
    private ArrayList<Integer> cart_item_ids = new ArrayList<>();
    private double deposit = 0;
    public List<CommoditypayModel> mGroupCardList = new ArrayList<>();
    public List<List<CommoditypayModel> > mChildCardList= new ArrayList<>();
    private CartItemMvpPresenter mCartItemMvpPresenter;
    public int invalidCount;
    private ArrayList<String> mResourceIdList;//结算选中的resource id
    private HashMap<String,String> mResourceNameMap;//结算选中的大于一天起订的resource的名字map
    private HashMap<String,Integer> mResourceMinOrderMap;//结算选中的大于一天起订的resource的起订天数map
    private HashMap<String,Integer> mResourceItemMap;//结算选中的大于一天起订的resource的预定个数map
    private CustomDialog mMinOrderNumberDialog;
    private ArrayList<Integer> mCommunityIds = new ArrayList<>();
    private HashMap<String,Double> mResourceItemDepositMap;//每个资源的最大押金

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.fragment_commoditypay, container , false);
            ButterKnife.inject(this, mMainContent);
            initview();
        }

        return mMainContent;

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.cartitem_fragment_name_str));
        PowerManager pm = (PowerManager) CommoditypayFragment.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(CommoditypayFragment.this.getContext())) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }

        } else if (screen == false) {
            shopcardrestart = 0;
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        //屏幕亮灭
        PowerManager pm = (PowerManager) CommoditypayFragment.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            //是否处于后台
            if (Constants.isBackground(CommoditypayFragment.this.getContext())) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }
        } else if (screen == false) {
            shopcardrestart = 0;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (CommoditypayFragment.this.getActivity() instanceof MainTabActivity) {
            MainTabActivity mainTabActivity = (MainTabActivity) CommoditypayFragment.this.getActivity();
            mainTabActivity.mSearchStatusBarLL.setVisibility(View.GONE);
        }
        MobclickAgent.onPageStart(getResources().getString(R.string.cartitem_fragment_name_str));
        mCommdityTitleLL.setBackgroundColor(getResources().getColor(R.color.default_bluebg));
        if (!(LoginManager.isLogin())) {
            TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));
            mcommoditypay_nodata.setVisibility(View.VISIBLE);
            mcommodity_jump_home.setText(getResources().getString(R.string.commoditypay_jumplogintext));
        } else {
            if (shopcardrestart == 1) {
                TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));
                mcommoditypay_nodata.setVisibility(View.GONE);
                CommoditypayAdapter.clear_isSelectedlist();
                if (CommdityList != null) {
                    CommdityList.clear();
                }
                mlinearlayout_check.setVisibility(View.GONE);
                if (mGroupCardList  != null) {
                    mGroupCardList.clear();
                }
                if (mChildCardList  != null) {
                    mChildCardList.clear();
                }
                madapter = new CommoditypayAdapter(CommoditypayFragment.this.getContext(),CommoditypayFragment.this,mGroupCardList,mChildCardList);
                mexpendlist.setAdapter(madapter);
                mexpendlist.setGroupIndicator(null);
                for (int i = 0; i < mGroupCardList.size(); i++) {
                    mexpendlist.expandGroup(i);
                }
                initdata(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCartItemMvpPresenter != null) {
            mCartItemMvpPresenter.detachView();
        }
    }

    private void initview() {
        mCartItemMvpPresenter = new CartItemMvpPresenter();
        mCartItemMvpPresenter.attachView(this);
        if (getActivity() instanceof ResourcesCartItemsActivity) {
            mCartItemStatusbarLL.setVisibility(View.GONE);
            TitleBarUtils.showBackImg(mMainContent,CommoditypayFragment.this.getActivity(),true);
        } else {
            MainTabActivity mMainTabActivity = (MainTabActivity) CommoditypayFragment.this.getActivity();
            if (mMainTabActivity.mNotchHeight > 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mMainTabActivity.mNotchHeight);
                mCartItemStatusbarLL.setLayoutParams(layoutParams);
            }
        }
        mcommondity_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mcommodity_selectall_checkbox.setChecked(false);
        mcommodity_selectall_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcommodity_selectall_checkbox.isChecked()) {
                    for (int i = 0; i < CommdityList.size(); i++) {
                        if (CommdityList.get(i).isValid()) {
                            CommoditypayAdapter.getIsSelected().put(CommdityList.get(i).getShopping_cart_item_id(), true);
                        }
                    }
                    for (int i = 0; i < mGroupCardList.size(); i++) {
                        mGroupCardList.get(i).setValid(true);
                    }
                    madapter.notifyDataSetChanged();
                    mtotal_text.setText(Constants.getPriceUnitStr(getContext(),getprice(),12));
                    mbtn_pay.setText(getorderpaylist_str());
                    int listsize = 0;
                    for (int i = 0; i < CommdityList.size(); i ++) {
                        if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                            listsize ++;
                        }
                    }
                    if (listsize < 1) {
                        mcommodity_selectall_checkbox.setChecked(false);
                        mbtn_pay.setEnabled(false);
                        mbtn_pay.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
                    } else {
                        mbtn_pay.setEnabled(true);
                        mbtn_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_choose_specifications_paid_order_bg));
                    }
                } else {
                    for (int i = 0; i < CommdityList.size(); i++) {
                        CommoditypayAdapter.getIsSelected().put(CommdityList.get(i).getShopping_cart_item_id(), false);
                    }
                    for (int i = 0; i < mGroupCardList.size(); i++) {
                        mGroupCardList.get(i).setValid(false);
                    }
                    madapter.notifyDataSetChanged();
                    mtotal_text.setText(Constants.getPriceUnitStr(getContext(),getprice(),12));
                    mbtn_pay.setText(getorderpaylist_str());
                    int listsize = 0;
                    for (int i = 0; i < CommdityList.size(); i ++) {
                        if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                            listsize ++;
                        }
                    }
                    if (listsize < 1) {
                        mbtn_pay.setEnabled(false);
                        mbtn_pay.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
                    } else {
                        mbtn_pay.setEnabled(true);
                        mbtn_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_choose_specifications_paid_order_bg));
                    }
                }
            }

        });
        mcommondity_swipe_refresh.setOnRefreshListener(this);
        if (!(LoginManager.isLogin())) {
            TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));
            mcommoditypay_nodata.setVisibility(View.VISIBLE);
            mcommodity_jump_home.setText(getResources().getString(R.string.commoditypay_jumplogintext));
        } else {
                TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));
                mcommoditypay_nodata.setVisibility(View.GONE);
                CommoditypayAdapter.clear_isSelectedlist();
                if (CommdityList != null) {
                    CommdityList.clear();
                }
                mlinearlayout_check.setVisibility(View.GONE);
                if (mGroupCardList  != null) {
                    mGroupCardList.clear();
                }
                if (mChildCardList  != null) {
                    mChildCardList.clear();
                }
                madapter = new CommoditypayAdapter(CommoditypayFragment.this.getContext(),CommoditypayFragment.this,mGroupCardList,mChildCardList);
                mexpendlist.setAdapter(madapter);
                mexpendlist.setGroupIndicator(null);
                for (int i = 0; i < mGroupCardList.size(); i++) {
                    mexpendlist.expandGroup(i);
                }
                advertsing_choosenum = 1;
                initdata(true);
        }
    }
    private void initdata(boolean show) {
        mtotal_text.setText(Constants.getPriceUnitStr(getContext(),(getActivity().getResources().getString(R.string.order_listitem_price_unit_text)+"0"),12));
        mbtn_pay.setText("结算(0)");
        mbtn_pay.setEnabled(false);
        mbtn_pay.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
        mcommodity_selectall_checkbox.setChecked(false);
        if (LoginManager.isLogin()) {
            if (CommdityList != null) {
                CommdityList.clear();
            }
            CommoditypayAdapter.clear_isSelectedlist();
            shopcardlistpagesize = 1;
            if (show) {
                showProgressDialog();
            }
            mCartItemFailerLL.setVisibility(View.GONE);
            mCartItemMvpPresenter.getCartItemCount();
        } else {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(CommoditypayFragment.this.getActivity());
        }


    }

    @Override
    public void onRefresh() {
        CommoditypayAdapter.clear_isSelectedlist();
        if (CommdityList != null) {
            CommdityList.clear();
        }
        mlinearlayout_check.setVisibility(View.GONE);
        if (mGroupCardList  != null) {
            mGroupCardList.clear();
        }
        if (mChildCardList  != null) {
            mChildCardList.clear();
        }
        madapter = new CommoditypayAdapter(CommoditypayFragment.this.getContext(),CommoditypayFragment.this,mGroupCardList,mChildCardList);
        mexpendlist.setAdapter(madapter);
        mexpendlist.setGroupIndicator(null);
        for (int i = 0; i < mGroupCardList.size(); i++) {
            mexpendlist.expandGroup(i);
        }
        initdata(false);
    }
    @OnClick({
            R.id.commodity_jump_home,
            R.id.btn_pay,
            R.id.commoditypay_nodata,
            R.id.card_failure_ll
    })
    public void commondityclick(View view) {
        switch (view.getId()) {
            case R.id.commodity_jump_home:
                if (mcommodity_jump_home.getText().toString().equals(getResources().getString(R.string.commoditypay_jumphome_text))) {
                    if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                        MainTabActivity activity = (MainTabActivity)CommoditypayFragment.this.getActivity();
                        activity.mTabHost.setCurrentTab(0);
                    } else {
                        Intent maintabintent = new Intent(CommoditypayFragment.this.getActivity(), MainTabActivity.class);
                        maintabintent.putExtra("new_tmpintent", "goto_homepage");
                        startActivity(maintabintent);
                    }

                } else if (mcommodity_jump_home.getText().toString().equals(getResources().getString(R.string.commoditypay_jumplogintext))) {
                    Intent commodity_jumplogin= new Intent(CommoditypayFragment.this.getActivity(),LoginActivity.class);
                    startActivityForResult(commodity_jumplogin, 3);
                }

                break;
            case R.id.btn_pay:
                Intent orderconfir = new Intent(CommoditypayFragment.this.getActivity(), OrderConfirmActivity.class);
                orderconfir.putExtra("submitorderlist",(Serializable)getsubmitorderlist());
                orderconfir.putExtra("community_ids",(Serializable) mCommunityIds);
                if (cart_item_ids != null) {
                    if (cart_item_ids.size() > 0) {
                        deposit = 0;
                        for (int i = 0; i < mResourceIdList.size(); i++) {
                            if (mResourceItemDepositMap.get(mResourceIdList.get(i)) != null) {
                                deposit = deposit + mResourceItemDepositMap.get(mResourceIdList.get(i));
                            }
                            if (mResourceNameMap.get(mResourceIdList.get(i)) != null) {
                                if (mResourceMinOrderMap.get(mResourceIdList.get(i)) -
                                        mResourceItemMap.get(mResourceIdList.get(i)) > 0) {
                                    String msg = mResourceNameMap.get(mResourceIdList.get(i)) +
                                                    getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first) +
                                            String.valueOf(mResourceMinOrderMap.get(mResourceIdList.get(i))) +
                                                            getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity) +
                                                    getResources().getString(R.string.module_commoditypay_min_order_num_str) +
                                                    String.valueOf(mResourceMinOrderMap.get(mResourceIdList.get(i)) -
                                                                    mResourceItemMap.get(mResourceIdList.get(i))) +
                                                    getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str);
                                    showMinOrderDialog(msg);
                                    return;
                                }
                            }
                        }
                        orderconfir.putExtra("cart_item_ids",(Serializable)cart_item_ids);
                    }
                }
                orderconfir.putExtra("deposit",deposit);
                orderconfir.putExtra("ordertype",3);
                startActivity(orderconfir);
                break;
            case R.id.commoditypay_nodata:
                if (mcommodity_jump_home.getText().toString().equals(getResources().getString(R.string.commoditypay_jumphome_text))) {
                    if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                        MainTabActivity activity = (MainTabActivity)CommoditypayFragment.this.getActivity();
                        activity.mTabHost.setCurrentTab(0);
                    } else {
                        Intent maintabintent = new Intent(CommoditypayFragment.this.getActivity(), MainTabActivity.class);
                        maintabintent.putExtra("new_tmpintent", "goto_homepage");
                        startActivity(maintabintent);
                    }

                } else if (mcommodity_jump_home.getText().toString().equals(getResources().getString(R.string.commoditypay_jumplogintext))) {
                    Intent commodity_jumplogin= new Intent(CommoditypayFragment.this.getActivity(),LoginActivity.class);
                    startActivityForResult(commodity_jumplogin, 3);
                }
                break;
            case R.id.card_failure_ll:
                mexpendlist.setSelectedGroup(mGroupCardList.size() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 6:
                shopcardrestart = 0;
                MainTabActivity maintabactivity = (MainTabActivity)CommoditypayFragment.this.getActivity();
                maintabactivity.restart = 0;
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private int getchecknum() {
        int selectcount = 0;
        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id().toString())) {
                selectcount++;
            }
        }
        return selectcount;
    }
    public void getcheckdata(boolean check,String price , String subsidy_fee,String pay_orderlistsize) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(check,price,subsidy_fee,pay_orderlistsize);
        test.getcartitemdata(this);
    }
    public void deleteshopcard(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    @Override
    public void getcartitemdata(Boolean check, String price, String subsidy_fee, String orderpaylistsize) {
        mcommodity_selectall_checkbox.setChecked(check);
        mtotal_text.setText(Constants.getPriceUnitStr(getContext(),price,12));
        int listsize = 0;
        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                listsize ++;
            }
        }
        if (listsize < 1) {
            mbtn_pay.setEnabled(false);
            mbtn_pay.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
        } else {
            mbtn_pay.setEnabled(true);
            mbtn_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_choose_specifications_paid_order_bg));
        }
        mbtn_pay.setText(orderpaylistsize);
    }
    @Override
    public void getfieldsize_pricenuit(final int position, final String str) {
        if (deleteshopcartlist != null) {
            deleteshopcartlist.clear();
        }
        final String deleteitem = str;
        new AlertDialog.Builder(CommoditypayFragment.this.getActivity())
                .setTitle("确定删除？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ArrayList<Integer> orderlist = new ArrayList<Integer>();
                        if (deleteitem.length() == 0 && position == -1) {
                            for (int i = 0; i < CommdityList.size(); i++) {
                                if (!CommdityList.get(i).isValid()) {
                                    orderlist.add(Integer.parseInt(CommdityList.get(i).getShopping_cart_item_id()));
                                }
                            }
                        } else {
                            orderlist.add(Integer.parseInt(deleteitem));
                        }
                        deleteshopcartlist.addAll(orderlist);
                        mCartItemMvpPresenter.deleteCartItem(CommoditypayFragment.this.getContext(),JSONArray.parseArray(JSON.toJSONString(orderlist, true)));
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                    }
                }).show();

    }
    public  String getprice() {
        double fieldprice = 0;
        String price = "";
        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                if (CommdityList.get(i).getPrice() != null &&
                        CommdityList.get(i).getPrice().length() > 0) {
                    if (CommdityList.get(i).getSubsidy_fee() != null &&
                            CommdityList.get(i).getSubsidy_fee().length() > 0) {
                        if (CommdityList.get(i).getRes_type_id().equals("2")) {
                            double  SubsidyFee= (Double.parseDouble(CommdityList.get(i).getSubsidy_fee().toString())) * CommoditypayAdapter.getMtw_num().get(CommdityList.get(i).getShopping_cart_item_id());
                            double price_tem = (Double.parseDouble(CommdityList.get(i).getPrice().toString())) * CommoditypayAdapter.getMtw_num().get(CommdityList.get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice - SubsidyFee + price_tem;
                        } else if (CommdityList.get(i).getRes_type_id().equals("1")  || CommdityList.get(i).getRes_type_id().equals("3")) {
                            double  SubsidyFee= Double.parseDouble(CommdityList.get(i).getSubsidy_fee().toString());
                            double price_tem = Double.parseDouble(CommdityList.get(i).getPrice().toString());
                            fieldprice = fieldprice - SubsidyFee + price_tem;
                        }
                    } else {
                        if (CommdityList.get(i).getRes_type_id().equals("2")) {
                            double price_tem = (Double.parseDouble(CommdityList.get(i).getPrice().toString())) * CommoditypayAdapter.getMtw_num().get(CommdityList.get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice + price_tem;
                        } else if (CommdityList.get(i).getRes_type_id().equals("1") || CommdityList.get(i).getRes_type_id().equals("3")) {
                            double price_tem = Double.parseDouble(CommdityList.get(i).getPrice().toString());
                            fieldprice = fieldprice + price_tem;
                        }
                    }
                }
            }
        }
        price = (getResources().getString(R.string.order_listitem_price_unit_text)+
                Constants.getdoublepricestring(fieldprice,0.01));
        return price;
    }
    public  String getsubsidy_fee() {
        int fieldprice = 0;
        String price = "";
        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                if (CommdityList.get(i).getSubsidy_fee() != null) {
                    if (CommdityList.get(i).getSubsidy_fee().length() > 0) {
                        if (CommdityList.get(i).getRes_type_id().equals("2")) {
                            int price_tem = (Integer.parseInt(CommdityList.get(i).getSubsidy_fee().toString())) * CommoditypayAdapter.getMtw_num().get(CommdityList.get(i).getShopping_cart_item_id());
                            fieldprice = fieldprice + price_tem;
                        } else if (CommdityList.get(i).getRes_type_id().equals("1")) {
                            int price_tem = Integer.parseInt(CommdityList.get(i).getSubsidy_fee().toString());
                            fieldprice = fieldprice + price_tem;
                        }

                    }
                }
            }
        }
        price = getResources().getString(R.string.commoditypay_subsidy_text)+ getActivity().getResources().getString(R.string.order_listitem_price_unit_text)+ Constants.getpricestring(String.valueOf(fieldprice),0.01);
        return price;
    }
    public  String getorderpaylist_str() {
        int listsize = 0;
        String str_listsize ="";

        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                listsize ++;
            }
        }
        if (listsize > 999) {
            str_listsize = CommoditypayFragment.this.getResources().getString(R.string.commoditypay_payorder_text) + "(" + "999+" + ")";
        } else {
            str_listsize = CommoditypayFragment.this.getResources().getString(R.string.commoditypay_payorder_text) + "("+String.valueOf(listsize)+")";
        }
        return str_listsize;
    }
    private ArrayList<HashMap<String,Object>> getsubmitorderlist() {
        if (cart_item_ids != null) {
            cart_item_ids.clear();
        }
        ArrayList<HashMap<String,Object>> jsonArray = new ArrayList<HashMap<String,Object>>();
        mResourceIdList = new ArrayList<>();
        mCommunityIds = new ArrayList<>();
        mResourceNameMap = new HashMap<>();
        mResourceItemMap = new HashMap<>();
        mResourceMinOrderMap = new HashMap<>();
        mResourceItemDepositMap = new HashMap<>();
        for (int i = 0; i < CommdityList.size(); i++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                HashMap<String,Object> json = new HashMap<String,Object>();
                json.put("id", CommdityList.get(i).getResource_id());
                if (CommdityList.get(i).getMinimum_order_quantity() != null &&
                        CommdityList.get(i).getMinimum_order_quantity() > 1) {
                    mResourceMinOrderMap.put(CommdityList.get(i).getResource_id(),CommdityList.get(i).getMinimum_order_quantity());
                    mResourceNameMap.put(CommdityList.get(i).getResource_id(),CommdityList.get(i).getName());
                    if (mResourceItemMap.get(CommdityList.get(i).getResource_id()) != null) {
                        mResourceItemMap.put(CommdityList.get(i).getResource_id(),mResourceItemMap.get(CommdityList.get(i).getResource_id())+1);
                    } else {
                        mResourceItemMap.put(CommdityList.get(i).getResource_id(),1);
                    }
                }
                if (mResourceIdList.size() > 0 && mResourceIdList.contains(CommdityList.get(i).getResource_id())) {
                    if (CommdityList.get(i).getDeposit() != null) {
                        if (mResourceItemDepositMap.get(CommdityList.get(i).getResource_id()) <
                                CommdityList.get(i).getDeposit()) {
                            mResourceItemDepositMap.put(CommdityList.get(i).getResource_id(),
                                    CommdityList.get(i).getDeposit());
                        }
                    }

                } else {
                    if (CommdityList.get(i).getDeposit() != null) {
                        mResourceItemDepositMap.put(CommdityList.get(i).getResource_id(),
                                CommdityList.get(i).getDeposit());
                        mResourceIdList.add(CommdityList.get(i).getResource_id());
                    }
                }
                if (mCommunityIds.size() > 0 && mCommunityIds.contains(CommdityList.get(i).getCommunity_id())) {

                } else {
                    mCommunityIds.add(CommdityList.get(i).getCommunity_id());
                }
                cart_item_ids.add(Integer.parseInt(CommdityList.get(i).getShopping_cart_item_id()));
                json.put("date", CommdityList.get(i).getDate());
                json.put("community_id", CommdityList.get(i).getCommunity_id());
                json.put("size",CommdityList.get(i).getSize());
                json.put("custom_dimension", CommdityList.get(i).getCustom_dimension());
                json.put("lease_term_type_id", CommdityList.get(i).getLease_term_type_id());
                json.put("lease_term_type", CommdityList.get(i).getLease_term_type());
                json.put("count", CommoditypayAdapter.getMtw_num().get(CommdityList.get(i).getShopping_cart_item_id()));
                json.put("count_of_time_unit", CommdityList.get(i).getCount_of_time_unit());
                json.put("price",CommdityList.get(i).getPrice());
                json.put("subsidy_fee",CommdityList.get(i).getSubsidy_fee());
                json.put("resourcename", CommdityList.get(i).getName());
                json.put("field_type", CommdityList.get(i).getField_type());
                String imagepath = "";
                if (CommdityList.get(i).getPic_url() != null) {
                    if (CommdityList.get(i).getPic_url().length() > 0 ) {
                        imagepath = CommdityList.get(i).getPic_url();
                    }
                }
                json.put("imagepath",imagepath);
                if (CommdityList.get(i).getDiscount_rate() != null) {
                    if (CommdityList.get(i).getDiscount_rate().length() > 0) {
                        json.put("discount_rate", CommdityList.get(i).getDiscount_rate());
                    } else {
                        json.put("discount_rate", "0");
                    }
                } else {
                    json.put("discount_rate", "0");
                }
                if (CommdityList.get(i).getTax_point() != null) {
                    if (CommdityList.get(i).getTax_point().length() > 0) {
                        json.put("tax_point", CommdityList.get(i).getTax_point());
                    } else {
                        json.put("tax_point", "0");
                    }
                } else {
                    json.put("tax_point", "0");
                }
                if (CommdityList.get(i).getSpecial_tax_point() != null &&
                        CommdityList.get(i).getSpecial_tax_point().length() > 0) {
                    json.put("special_tax_point", CommdityList.get(i).getSpecial_tax_point());
                } else {
                    json.put("special_tax_point", "0");
                }
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    @Override
    public void onCartListSuccess(ArrayList<CommoditypayModel> list, String invalid_count) {
        //2018/3/5 失效个数显示
        ArrayList<CommoditypayModel> CommdityTmpList = new ArrayList<>();//购物车所有的list不分组
        CommdityTmpList = list;
        ArrayList<CommoditypayModel> fieldList = new ArrayList<>();
        ArrayList<CommoditypayModel> advList = new ArrayList<>();
        ArrayList<CommoditypayModel> failureList = new ArrayList<>();
        if (CommdityTmpList.size() > 0) {
            if(mcommondity_swipe_refresh.isShown()){
                mcommondity_swipe_refresh.setRefreshing(false);
            }
            if (invalid_count != null && invalid_count.length() > 0 &&
                    Integer.parseInt(invalid_count) > 0) {
                invalidCount = Integer.parseInt(invalid_count);
                mCartItemFailerLL.setVisibility(View.VISIBLE);
                mCartItemFailerTV.setText(invalid_count +
                getResources().getString(R.string.commoditypay_failure_tv_str));
            }
            mlinearlayout_check.setVisibility(View.VISIBLE);
            mcommoditypay_nodata.setVisibility(View.GONE);
            try {
                CommdityList = (ArrayList<CommoditypayModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(CommdityTmpList);
                for (int i = 0; i < CommdityList.size(); i++) {
                    CommoditypayAdapter.getIsSelected().put(CommdityList.get(i).getShopping_cart_item_id(),false);
                }
                for (int i = 0; i < CommdityList.size(); i++) {
                    CommoditypayAdapter.getMtw_num().put(CommdityList.get(i).getShopping_cart_item_id(),CommdityList.get(i).getCount());
                }
                for (int i = 0; i < CommdityTmpList.size(); i++) {
                    if (CommdityTmpList.get(i).isValid()) {
                        if (CommdityTmpList.get(i).getRes_type_id() != null) {
                            if (CommdityTmpList.get(i).getRes_type_id().equals("1") ||
                                    CommdityTmpList.get(i).getRes_type_id().equals("3")) {
                                fieldList.add(CommdityTmpList.get(i));
                            } else {
                                advList.add(CommdityTmpList.get(i));
                            }
                        } else {
                            fieldList.add(CommdityTmpList.get(i));
                        }
                    } else {
                        failureList.add(CommdityTmpList.get(i));
                    }
                }
                if (mGroupCardList != null) {
                    mGroupCardList.clear();
                }
                if (mChildCardList  != null) {
                    mChildCardList.clear();
                }
                if (fieldList.size() > 0) {
                    CommoditypayModel commoditypayModel = new CommoditypayModel();
                    commoditypayModel.setRes_type_id("1");
                    commoditypayModel.setName(getResources().getString(R.string.commoditypay_group_field_tv_str));
                    mGroupCardList.add(commoditypayModel);
                    mChildCardList.add(fieldList);
                }
                if (advList.size() > 0) {
                    CommoditypayModel commoditypayModel = new CommoditypayModel();
                    commoditypayModel.setRes_type_id("2");
                    commoditypayModel.setName(getResources().getString(R.string.commoditypay_group_advs_tv_str));
                    mGroupCardList.add(commoditypayModel);
                    mChildCardList.add(advList);
                }
                if (failureList.size() > 0) {
                    CommoditypayModel commoditypayModel = new CommoditypayModel();
                    commoditypayModel.setRes_type_id("0");
                    commoditypayModel.setName(getResources().getString(R.string.commoditypay_failure_resources));
                    mGroupCardList.add(commoditypayModel);
                    mChildCardList.add(failureList);
                }
                madapter = new CommoditypayAdapter(CommoditypayFragment.this.getContext(),CommoditypayFragment.this,mGroupCardList,mChildCardList);
                mexpendlist.setAdapter(madapter);
                mexpendlist.setGroupIndicator(null);
                for (int i = 0; i < mGroupCardList.size(); i++) {
                    mexpendlist.expandGroup(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mlinearlayout_check.setVisibility(View.GONE);
            mcommoditypay_nodata.setVisibility(View.VISIBLE);
            mcommodity_jump_home.setText(getResources().getString(R.string.commoditypay_jumphome_text));
        }
    }

    @Override
    public void onCartListFailure(boolean superresult, Throwable error) {
        if(mcommondity_swipe_refresh.isShown()){
            mcommondity_swipe_refresh.setRefreshing(false);
        }
        mlinearlayout_check.setVisibility(View.GONE);
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());

        checkAccesstoken(error);
        return;
    }

    @Override
    public void onCartCountSuccess(String count) {
        MainTabActivity activity = null;
        if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
            activity = (MainTabActivity)CommoditypayFragment.this.getActivity();
        }
        if (count != null) {
            com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(count);
            if (jsonobject.get("count") != null) {
                if (jsonobject.get("count").toString().length() != 0) {
                    if (!(jsonobject.get("count").toString().equals("0"))) {
                        TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt) + "(" + jsonobject.get("count").toString() + ")");//购物车自后面 +（5）
                        if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                            activity.MainBV[2].setBadgeNumber(Integer.parseInt(jsonobject.get("count").toString()));
                        }
                        if (Integer.parseInt(jsonobject.get("count").toString()) > 0) {
                            TitleBarUtils.showActionImg(mMainContent, true, getResources().getDrawable(R.drawable.ic_delete_three), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (true) {
                                        int listsize = 0;
                                        for (int i = 0; i < CommdityList.size(); i ++) {
                                            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                                                listsize ++;
                                            }
                                        }
                                        if (listsize > 0) {
                                            new AlertDialog.Builder(CommoditypayFragment.this.getActivity())
                                                    .setTitle("确定删除？")
                                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            if (deleteshopcartlist != null) {
                                                                deleteshopcartlist.clear();
                                                            }
                                                            ArrayList<Integer> orderlist = new ArrayList<Integer>();
                                                            for (int i = 0; i < CommdityList.size(); i++) {
                                                                if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                                                                    orderlist.add(Integer.parseInt(CommdityList.get(i).getShopping_cart_item_id()));
                                                                }
                                                            }
                                                            deleteshopcartlist.addAll(orderlist);
                                                            mCartItemMvpPresenter.deleteCartItem(CommoditypayFragment.this.getContext(),JSONArray.parseArray(JSON.toJSONString(orderlist, true)));
                                                        }
                                                    })
                                                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.dismiss();

                                                        }
                                                    }).show();

                                        } else {
                                            MessageUtils.showToast(getResources().getString(R.string.commoditypay_choose_resources));
                                        }

                                    } else {
                                        MessageUtils.showToast(CommoditypayFragment.this.getActivity(),getResources().getString(R.string.map_search_resource_num));
                                    }

                                }
                            });

                        }
                    } else {
                        TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));//购物车自后面 +（5）
                        if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                            activity.MainBV[2].hide(false);
                        }
                        TitleBarUtils.showActionImg(mMainContent, false, getResources().getDrawable(R.drawable.ic_delete_three), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    }
                    invalidCount = 0;
                    mCartItemMvpPresenter.getCartItemList();
                } else {
                    hideProgressDialog();
                    TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));//购物车自后面 +（5）
                    if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                        activity.MainBV[2].hide(false);
                    }
                    TitleBarUtils.showActionImg(mMainContent, false, getResources().getDrawable(R.drawable.ic_delete_three), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            } else {
                hideProgressDialog();
                TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));//购物车自后面 +（5）
                if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                    activity.MainBV[2].hide(false);
                }
                TitleBarUtils.showActionImg(mMainContent, false, getResources().getDrawable(R.drawable.ic_delete_three), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

        } else {
            hideProgressDialog();
            TitleBarUtils.setTitleText(mMainContent, getActivity().getResources().getString(R.string.commoditypay_title_txt));//购物车自后面 +（5）
            if (!(getActivity() instanceof ResourcesCartItemsActivity)) {
                activity.MainBV[2].hide(false);
            }
            TitleBarUtils.showActionImg(mMainContent, false, getResources().getDrawable(R.drawable.ic_delete_three), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    public void onCartCountFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
        checkAccesstoken(error);
        return;
    }

    @Override
    public void onDeleteCartSuccess(String data) {
        mCartItemFailerLL.setVisibility(View.GONE);
        mCartItemMvpPresenter.getCartItemCount();
        MessageUtils.showToast(CommoditypayFragment.this.getActivity(),getResources().getString(R.string.delete_shoppingcart_success_txt));
        for (int i = 0; i < mGroupCardList.size(); i++) {
            if (!mGroupCardList.get(i).getRes_type_id().equals("0")) {
                for (int l = 0; l < mChildCardList.get(i).size(); l++) {
                    if (deleteshopcartlist.contains(Integer.parseInt(mChildCardList.get(i).get(l).getShopping_cart_item_id()))) {
                        mChildCardList.get(i).remove(l);
                        break;
                    }
                }
            }
        }
        for (int k = 0; k < mChildCardList.size(); k++) {
            if (mChildCardList.get(k).size() == 0) {
                mGroupCardList.remove(k);
            }
        }
        for (int i = 0; i < CommdityList.size(); i++) {
            if (deleteshopcartlist.contains(Integer.parseInt(CommdityList.get(i).getShopping_cart_item_id()))) {
                CommoditypayAdapter.getIsSelected().remove(CommdityList.get(i).getShopping_cart_item_id());
                CommoditypayAdapter.getMtw_num().remove(CommdityList.get(i).getShopping_cart_item_id());
                CommdityList.remove(i);
                break;
            }
        }
        if (deleteshopcartlist != null) {
            deleteshopcartlist.clear();
        }
        madapter.notifyDataSetChanged();
        mtotal_text.setText(Constants.getPriceUnitStr(getContext(),getprice(),12));
        mbtn_pay.setText(getorderpaylist_str());
        int listsize = 0;
        for (int i = 0; i < CommdityList.size(); i ++) {
            if (CommoditypayAdapter.getIsSelected().get(CommdityList.get(i).getShopping_cart_item_id())) {
                listsize ++;
            }
        }
        if (listsize < 1) {
            mcommodity_selectall_checkbox.setChecked(false);
            mbtn_pay.setEnabled(false);
            mbtn_pay.setBackgroundColor(getResources().getColor(R.color.field_chair_textcolor));
        } else {
            mbtn_pay.setEnabled(true);
            mbtn_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_choose_specifications_paid_order_bg));
        }
    }

    @Override
    public void onDeleteCartFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
        checkAccesstoken(error);
    }
    private void showMinOrderDialog(String msg) {
        if (mMinOrderNumberDialog == null || !mMinOrderNumberDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.btn_cancel:
                            mMinOrderNumberDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(CommoditypayFragment.this.getActivity());
            mMinOrderNumberDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            msg)
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.confirm))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .showView(R.id.btn_perfect,View.GONE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(CommoditypayFragment.this.getActivity(),mMinOrderNumberDialog);
            mMinOrderNumberDialog.show();
        }
    }
}
