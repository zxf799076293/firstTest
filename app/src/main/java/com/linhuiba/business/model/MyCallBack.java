package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface MyCallBack {
        public void	 get_check(Boolean check,String price, String pay_orderlistsize,String subsidy_fee);
        //购物车listitem=-2，order_item_id=-2// 订单列表确认订单 listitem=-4，order_item_id=-4//
        public void  getdeletelistitem(int listitem,int order_item_id);
}
