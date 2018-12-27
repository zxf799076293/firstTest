package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

public class SubmitDemandMvpModel {
    public static void appealDemand(String product,String minimum_area,String maximum_area,
                                    String single_field_budget,String start, String end,
                                    String name, String mobile, String community_resource_id,
                                    String physical_resource_id,ArrayList<Integer> city_ids,
                                    ArrayList<Integer> community_type_ids,
                                    LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.appealDemand(MyAsyncHttpClient.MyAsyncHttpClient4(), handler,
                product,minimum_area, maximum_area,
                single_field_budget, start,  end,
                name, mobile, community_resource_id,
                physical_resource_id, city_ids,
                community_type_ids);
    }

}
