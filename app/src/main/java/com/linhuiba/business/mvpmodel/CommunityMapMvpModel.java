package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

public class CommunityMapMvpModel {
    public static void getCommunityAllPhyRes(int community_id,
                                      final LinhuiAsyncHttpResponseHandler
                                              Handler) {
        FieldApi.getCommunityAllPhyRes(MyAsyncHttpClient.MyAsyncHttpClient(),Handler,community_id);
    }
    public static void getMapResourcesList(BaiduMapActivity baiduMapActivity,
                                           ApiResourcesModel apiResourcesModel,
                                           LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getMapResourcesList(baiduMapActivity,MyAsyncHttpClient.MyAsyncHttpClient2(), handler,apiResourcesModel);
    }
    public static void getAttributesList(ArrayList<Integer> category_ids,
                                         LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getAttributesList(MyAsyncHttpClient.MyAsyncHttpClient2(), handler,category_ids);
    }

}
