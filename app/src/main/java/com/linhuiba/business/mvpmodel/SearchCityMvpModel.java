package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/4/10.
 */

public class SearchCityMvpModel {
    public static void getSearchCityData(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getHotCityList(MyAsyncHttpClient.MyAsyncHttpClient(), handler);
    }
}
