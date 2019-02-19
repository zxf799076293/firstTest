package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

public class SearchResListMvpModel {
    public static void getCommunityList(ApiResourcesModel apiResourcesModel,
                                        LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getresourceslist(MyAsyncHttpClient.MyAsyncHttpClient2(), handler,apiResourcesModel);
    }
    public static void getPhyReslist(ApiResourcesModel apiResourcesModel,
                                        LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getPhyReslist(MyAsyncHttpClient.MyAsyncHttpClient(), handler,apiResourcesModel);
    }

    public static void getActivityList(String city_id, int page, double lat, double lng,
                                        LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getActivityList(MyAsyncHttpClient.MyAsyncHttpClient4(), handler,city_id,lat,lng,page);
    }
    public static void getAttributesList(ArrayList<Integer> category_ids,
                                       LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getAttributesList(MyAsyncHttpClient.MyAsyncHttpClient2(), handler,category_ids);
    }
    public static void getFastList(ApiAdvResourcesModel apiResourcesModel,
                                         LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient(), handler,apiResourcesModel);    }
    public static void getPhyReslistCount(ApiResourcesModel apiResourcesModel,
                                     LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getPhyReslistCount(MyAsyncHttpClient.MyAsyncHttpClient(), handler,apiResourcesModel);
    }
    public static void getPhyRecommendedList(ApiResourcesModel apiResourcesModel,
                                          LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getPhyRecommendedList(MyAsyncHttpClient.MyAsyncHttpClient(), handler,apiResourcesModel);
    }


}
