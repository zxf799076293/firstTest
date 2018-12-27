package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/4/11.
 */

public class ModifyUserInfoMvpModel {
    public static void modifyUserInfo(int type,String info, LinhuiAsyncHttpResponseHandler handler) {
        UserApi.modifyUserInfo(MyAsyncHttpClient.MyAsyncHttpClient(),handler,
                type,info);
    }

    public static void setUserName(String username, LinhuiAsyncHttpResponseHandler handler) {
        UserApi.setUsetName(username, MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }
}
