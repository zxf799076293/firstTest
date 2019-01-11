package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PublishReviewMvpModel {
    public static void getReviewInfo(String field_order_item_id,
                                    LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getreview_fieldinfo(MyAsyncHttpClient.MyAsyncHttpClient2(),handler,field_order_item_id);
    }
    public static void confirmReview( String fieldid,int score,int score_of_visitorsflowrate,int score_of_propertymatching,
                                       int score_of_goalcompletion , String content,int anonymity,
                                      ArrayList<Integer> tags,ArrayList<String> images,
            LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.published_resources_comments(MyAsyncHttpClient.MyAsyncHttpClient3(),handler,
                fieldid,score,anonymity,score_of_visitorsflowrate,
                score_of_propertymatching,score_of_goalcompletion,content,tags,images);
    }
    public static void getCommentCentre(final LinhuiAsyncHttpResponseHandler
                                                    fieldidHandler) {
        FieldApi.getCommentCentreOrders(MyAsyncHttpClient.MyAsyncHttpClient4(), fieldidHandler);
    }
    public static void getPhyResComments(String fieldid,String page,String pageSize,
                                        final LinhuiAsyncHttpResponseHandler
                                                fieldidHandler) {
        FieldApi.get_resources_commentslist(MyAsyncHttpClient.MyAsyncHttpClient4(), fieldidHandler, fieldid,page,pageSize);
    }

    public static void getSellResCpmments(String fieldid,String page,String pageSize,
                                        final LinhuiAsyncHttpResponseHandler
                                                fieldidHandler) {
        FieldApi.getSellResCpmments(MyAsyncHttpClient.MyAsyncHttpClient3(), fieldidHandler, fieldid,page,pageSize);
    }

    public static void commentsOrderItems(int score,int score_of_visitorsflowrate,int score_of_propertymatching,
                                      int score_of_goalcompletion , String content,int anonymity,
                                      ArrayList<Integer> tags,ArrayList<String> images,ArrayList<Integer> field_order_item_ids,
                                      LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.commentsOrderItems(MyAsyncHttpClient.MyAsyncHttpClient(),handler,
                score,anonymity,score_of_visitorsflowrate,
                score_of_propertymatching,score_of_goalcompletion,content,tags,images,field_order_item_ids);
    }

}
