package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/2/5.
 */

public class LoginMvpModel {
    public static void getLoginData(final String username, final String pw,
                                  final LinhuiAsyncHttpResponseHandler
                                          loginHandler) {
        UserApi.apiauthlogin(MyAsyncHttpClient.MyAsyncHttpClient(), loginHandler,
                username, pw);
    }
    public static void getWeChatLoginData(String code,int province_id,int city_id, LinhuiAsyncHttpResponseHandler
                                            loginHandler) {
        UserApi.third_party_wechat_login(MyAsyncHttpClient.MyAsyncHttpClient(), loginHandler,
                code,province_id,city_id);
    }
    public static void getFastCaptcha(String mobile, String usage, LinhuiAsyncHttpResponseHandler
            handler) {
        UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                mobile,usage);
    }
    public static void fastLogin(String mobile, String code,
                                      LinhuiAsyncHttpResponseHandler
            handler) {
        UserApi.apiauthfast_login(MyAsyncHttpClient.MyAsyncHttpClient(), handler, mobile,
                code);
    }
    public static void getQQLoginData(String unionid,int province_id,int city_id, LinhuiAsyncHttpResponseHandler
            loginHandler) {
        UserApi.qqLogin(MyAsyncHttpClient.MyAsyncHttpClient(), loginHandler,
                unionid,province_id,city_id);
    }
    public static void sendBrowseHistories(String view, String parameter,String cur_city_id) {
        UserApi.sendBrowseHistories(MyAsyncHttpClient.MyAsyncHttpClient(), new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                },view,parameter,cur_city_id);
    }
    public static void loginOut() {
        UserApi.logout(MyAsyncHttpClient.MyAsyncHttpClient(), new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
