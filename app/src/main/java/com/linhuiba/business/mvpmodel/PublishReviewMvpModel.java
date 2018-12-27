package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PublishReviewMvpModel {
    public static void getReviewInfo(String field_order_item_id,
                                    LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getreview_fieldinfo(MyAsyncHttpClient.MyAsyncHttpClient2(),handler,field_order_item_id);
    }
    public static void confirmReview( String fieldid,int score,int score_of_visitorsflowrate,
                                       int score_of_userparticipation,int score_of_propertymatching,
                                       int score_of_goalcompletion , String content,int anonymity,
                                       String tags,String images,String number_of_people,
            LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.published_resources_comments(MyAsyncHttpClient.MyAsyncHttpClient2(),handler,
                fieldid,score,score_of_visitorsflowrate,score_of_userparticipation,
                score_of_propertymatching,score_of_goalcompletion,content,anonymity,tags,images,number_of_people);
    }
}
