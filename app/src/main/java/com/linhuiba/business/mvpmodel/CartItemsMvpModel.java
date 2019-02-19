package com.linhuiba.business.mvpmodel;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/3/5.
 */

public class CartItemsMvpModel {
    public static void getCartCount(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getshopcart_itemscount(MyAsyncHttpClient.MyAsyncHttpClient2(), handler);
    }

    public static void getCartList(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getshopcart_itemslist(MyAsyncHttpClient.MyAsyncHttpClient3(),handler);
    }
    public static void deleteCartItemList(Context context,LinhuiAsyncHttpResponseHandler handler, JSON cart_item_ids) {
        FieldApi.deleteshopcart_items(context,MyAsyncHttpClient.MyAsyncHttpClient(), handler, cart_item_ids);
    }

}
