package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/2/28.
 */

public class FieldinfoMvpModel {
    public static void getPhyResInfo(String id, ApiResourcesModel apiResourcesModel,
                                    final LinhuiAsyncHttpResponseHandler
                                            fieldidHandler) {
        FieldApi.getPhyResInfo(MyAsyncHttpClient.MyAsyncHttpClient(), fieldidHandler, id,apiResourcesModel);
    }
    public static void getResInfoReviewData(String fieldid,String page,String pageSize,
                                      final LinhuiAsyncHttpResponseHandler
                                              fieldidHandler) {
        FieldApi.get_resources_commentslist(MyAsyncHttpClient.MyAsyncHttpClient3(), fieldidHandler, fieldid,page,pageSize);
    }
    public static void getGroupResInfoData(String id,
                                      final LinhuiAsyncHttpResponseHandler
                                              fieldidHandler) {
        FieldApi.getGroupBookingInfo(MyAsyncHttpClient.MyAsyncHttpClient(), fieldidHandler, id);
    }
    public static void getNearbyResData(int id, int city_id, int page, int pageSize,
                                           final LinhuiAsyncHttpResponseHandler
                                                   Handler) {
        FieldApi.getNearbyRes(id,city_id,page,pageSize,MyAsyncHttpClient.MyAsyncHttpClient(), Handler);
    }
    public static void getOtherPhyRes(int community_id, int page, int pageSize,int physical_resource_id,
                                        ApiResourcesModel apiResourcesModel,
                                        final LinhuiAsyncHttpResponseHandler
                                                Handler) {
        FieldApi.getOtherPhyRes(MyAsyncHttpClient.MyAsyncHttpClient2(),Handler,community_id,page,pageSize,physical_resource_id,apiResourcesModel);
    }
    public static void getCommunityInfo(int id,
                                      final LinhuiAsyncHttpResponseHandler
                                              fieldidHandler) {
        FieldApi.getCommunityInfo(MyAsyncHttpClient.MyAsyncHttpClient2(), fieldidHandler, id);
    }
    public static void getRecommendedRes(int id, int page_size, int page,
                                         int city_id,int category_id,
                                        final LinhuiAsyncHttpResponseHandler
                                                fieldidHandler) {
        FieldApi.getRecommendedRes(MyAsyncHttpClient.MyAsyncHttpClient2(), fieldidHandler,id,page_size,page,city_id,category_id);
    }
    public static void releaseFeedbacks(int resource_id,int community_id,String content,
                                         final LinhuiAsyncHttpResponseHandler
                                                 fieldidHandler) {
        FieldApi.release_feedbacks(MyAsyncHttpClient.MyAsyncHttpClient2(), fieldidHandler, resource_id,community_id,content);
    }
    public static void getSellRes(String id,String res_type_id,
                                  LinhuiAsyncHttpResponseHandler Handler) {
        FieldApi.getSellRes(MyAsyncHttpClient.MyAsyncHttpClient(),Handler,id,res_type_id);
    }
    public static void getSellResInfo(String id,
                                  LinhuiAsyncHttpResponseHandler Handler) {
        FieldApi.getSellResInfo(MyAsyncHttpClient.MyAsyncHttpClient6(),Handler,id);
    }

}
