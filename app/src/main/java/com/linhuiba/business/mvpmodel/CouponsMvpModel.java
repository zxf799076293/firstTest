package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

public class CouponsMvpModel {
    public static void getMyCoupons(int expired,int used, int page,
                                           final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getMyCoupons(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                expired, used, page);
    }
    public static void getCouponsGifts(int page,
                                    final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCouponsGifts(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                page);
    }
    public static void getCouponsExchangeList(int page,
                                       final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCouponsExchangeList(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                page);
    }
    public static void exchangeCoupons(ArrayList<Integer> coupons,
                                              final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.exchangeCoupons(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                coupons);
    }
    public static void receiveCoupons(int id, int quantity,
                                       final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.receiveCoupons(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                id,quantity);
    }
    public static void getCommunityCoupons(int community_id, int is_received, int page_size,int page,
                                      final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCommunityCoupons(
                MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                community_id,is_received,page_size,page);
    }
    public static void getCouponCentreList(int status, int page,
                                    final LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCouponCentre(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                status, page);
    }

}
