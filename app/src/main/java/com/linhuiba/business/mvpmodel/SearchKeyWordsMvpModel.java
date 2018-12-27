package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/2/28.
 */

public class SearchKeyWordsMvpModel {
    public static void getResInfoData(SearchFieldAreaActivity activity,
                                      String keyword,String city_id, String res_type_id,
                                      LinhuiAsyncHttpResponseHandler
                                              Handler) {
        FieldApi.getFastSearchList(activity, keyword,city_id,res_type_id,
                MyAsyncHttpClient.MyAsyncHttpClient2(), Handler);
    }
}
