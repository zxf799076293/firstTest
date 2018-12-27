package com.linhuiba.business.mvpmodel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;

import java.util.ArrayList;

public class OrderConfirmMvpModel {
    public static void getDefaultAddress(com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getDefaultAddress(MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }
    public static void getUserprofile(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.getuserprofile(MyAsyncHttpClient.MyAsyncHttpClient3(),handler);
    }
    public static void createOrderPay(JSONArray resources, int total_actual_price, int delegated, int need_decoration,
                                      int need_transportation, int need_invoice, String contact, String mobile,
                                      String address, String cart_item_ids,
                                      InvoiceInfomationModel invoiceInfomationModeldata,
                                      LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.createorder_pay(MyAsyncHttpClient.MyAsyncHttpClient8(),handler,
                resources,total_actual_price,delegated,need_decoration,
                need_transportation,need_invoice,contact,mobile,
                address,cart_item_ids,invoiceInfomationModeldata);
    }
    public static void getOrderCommunityCoupons(ArrayList<Integer> community_ids,ArrayList<Integer> coupon_ids,
                                                final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getOrderCommunityCoupons(
                MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                community_ids,coupon_ids);
    }
    public static void setOrderNoticed(int ordering_noticed,
                                                final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.setOrderNoticed(
                MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                ordering_noticed);
    }
}
