package com.linhuiba.linhuifield.fieldbusiness;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Request;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Field_UserApi {
    //获取当前用户提醒标记接口/2.3
    public static void getbadge_info(OkHttpClient client,
                                     LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/badge_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取当前用户信息  获取完整 2.3
    public static void getuserprofile(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/profile",
                paramsMap,3));
        call.enqueue(handler);
    }
    //预约签约接口
    public static void appointment_sign(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/current/appointment_sign",
                paramsMap,1));
        call.enqueue(handler);
    }
}
