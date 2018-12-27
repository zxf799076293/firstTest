package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseMvpModel {
    public static void getCaseList(int is_home, ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                   ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                   ArrayList<Integer> city_ids, ArrayList<Integer> label_ids,final int page,int city_id, LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCaseList(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), handler,
                is_home, community_type_ids, industry_ids,spread_way_ids,promotion_purpose_ids,city_ids
                ,label_ids, page,city_id);
    }
    public static void getCaseInfo(int id,ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                   ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                   ArrayList<Integer> city_ids,
                                   ArrayList<Integer> label_ids,int city_id,
                                   LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCaseInfo(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), handler,
                id,community_type_ids,industry_ids,spread_way_ids,promotion_purpose_ids,city_ids,label_ids,city_id);
    }
    public static void getOterCaseList(int id, int page, int city_id, LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getOtherCaseList(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), handler,
                id, page,city_id);
    }
    public static void getCaseSelection(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getCaseSelection(MyAsyncHttpClient.MyAsyncHttpClient(), handler);
    }


}
