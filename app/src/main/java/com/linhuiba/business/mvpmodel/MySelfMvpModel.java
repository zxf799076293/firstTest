package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/4/24.
 */

public class MySelfMvpModel {
    public static void getBadgeInfo(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.getbadge_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),handler);
    }
    public static void getUserProfile(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.getuserprofile(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),handler);
    }
    public static void bindWechat(String code,LinhuiAsyncHttpResponseHandler handler) {
        UserApi.bind_wechat(MyAsyncHttpClient.MyAsyncHttpClient(),handler,code);
    }

    public static void unbindWechat(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.unbind_wechat(MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }
    public static void bindQQ(String unionid,LinhuiAsyncHttpResponseHandler handler) {
        UserApi.bindQQ(MyAsyncHttpClient.MyAsyncHttpClient(),handler,unionid);
    }

    public static void unbindQQ(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.unbindQQ(MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }

}
