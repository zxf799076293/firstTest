package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HomeMvpModel {
    public static void getCooperationCaseData(String article_type_id,
                                     String pagesize,
                                     LinhuiAsyncHttpResponseHandler
                                             Handler) {
        FieldApi.getarticleslist(MyAsyncHttpClient.MyAsyncHttpClient2(), Handler,
                article_type_id,pagesize);
    }
    //服务商列表
    public static void getServiceProviderData(String pageSize,String page,
                                              String city_id,
                                              LinhuiAsyncHttpResponseHandler
                                                      Handler) {
        FieldApi.getServiceProviderList(pageSize,page,city_id,
                MyAsyncHttpClient.MyAsyncHttpClient2(), Handler);
    }
    //获取公告列表
    public static void getNotices(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.get_notices(MyAsyncHttpClient.MyAsyncHttpClient3(),handler);
    }
    //获取首页banner
    public static void getBanners(String city_id, LinhuiAsyncHttpResponseHandler handler) {
        UserApi.getbanners(MyAsyncHttpClient.MyAsyncHttpClient(),handler,city_id);
    }
    //获取首页弹窗
    public static void getHomeMessages(LinhuiAsyncHttpResponseHandler handler) {
        UserApi.getMessageNotices(MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }
    //删除首页弹窗
    public static void deleteMessageNotices(String id, LinhuiAsyncHttpResponseHandler handler) {
        UserApi.deleteMessageNotices(id, MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }
    public static void getNavigationBar(int plate,
                                        LinhuiAsyncHttpResponseHandler
                                                Handler) {
        FieldApi.getNavigationBar(MyAsyncHttpClient.MyAsyncHttpClient(), Handler,
                plate);
    }
    public static void getCategoriesBarList(
                                        LinhuiAsyncHttpResponseHandler
                                                Handler) {
        FieldApi.getCategoriesBarList(MyAsyncHttpClient.MyAsyncHttpClient(), Handler);
    }
    public static void getHomeLinhuiData(LinhuiAsyncHttpResponseHandler
                                                      Handler) {
        FieldApi.getLinhuiData(MyAsyncHttpClient.MyAsyncHttpClient2(), Handler);
    }
    public static void getAppHoverWindow(LinhuiAsyncHttpResponseHandler
                                                 Handler) {
        FieldApi.getAppHoverWindow(MyAsyncHttpClient.MyAsyncHttpClient(), Handler);
    }

}
