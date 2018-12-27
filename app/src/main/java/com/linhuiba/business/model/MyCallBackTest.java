package com.linhuiba.business.model;

import android.view.View;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyCallBackTest {
    private boolean check;
    private String price;
    private String pay_orderlistsize;
    private String subsidy_fee;
    private int orderitem;
    private int order_item_id;
    private int RefreshType;
    private String addcart_jsonstr;
    private View view;
    public MyCallBackTest(boolean check, String price,String pay_orderlistsize,String subsidy_fee) {
        this.check=check;
        this.price = price;
        this.pay_orderlistsize = pay_orderlistsize;
        this.subsidy_fee = subsidy_fee;
    }
    public MyCallBackTest(int orderitem, int order_item_id) {
        this.orderitem = orderitem;
        this.order_item_id = order_item_id;
    }
    public MyCallBackTest(int RefreshType) {
        this.RefreshType = RefreshType;
    }
    public MyCallBackTest(String jsonstr,View textview) {
        this.addcart_jsonstr = jsonstr;
        this.view = textview;
    }
    public  void setcheck(MyCallBack callBack) {
        callBack.get_check(check,price,pay_orderlistsize,subsidy_fee);
    }
    public  void deleteitem(MyCallBack callBack) {
        callBack.getdeletelistitem(orderitem, order_item_id);
    }
    public  void Refresh(WeiXinCallBack callBack) {
        callBack.Refresh(RefreshType);
    }
    public  void addshopcard(Search_addshoppingcard_Call callBack) {
        callBack.addshopcart(addcart_jsonstr,view);
    }
}
