package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/4/6.
 */
public interface WeiXinCallBack {
    //购物车返回支付成功操作 type=-2//订单列表返回支付成功操作type=-4
    public void	 Refresh(int type);
}
