package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/4/24.
 */

public class VersionMvpModel {
    public static void getVersion(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.version(MyAsyncHttpClient.MyAsyncHttpClient(), handler);
    }
}
