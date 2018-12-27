package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;

public class MyPushMsgMvpModel {
    public static void getUserMsg(String id,int type,int page,
                                  LinhuiAsyncHttpResponseHandler Handler) {
        UserApi.getUserMsg(MyAsyncHttpClient.MyAsyncHttpClient2(),Handler,id,type,page);
    }
    public static void deleteUserMsgs(ArrayList<Integer> ids,
                                  LinhuiAsyncHttpResponseHandler Handler) {
        UserApi.deleteUserMsgs(MyAsyncHttpClient.MyAsyncHttpClient2(),Handler,ids);
    }
    public static void setMsgsRead(String uid,
                                  ArrayList<Integer> ids,
                                  LinhuiAsyncHttpResponseHandler Handler) {
        UserApi.setMsgsRead(MyAsyncHttpClient.MyAsyncHttpClient2(),Handler,uid,ids);
    }
    public static void setAllMsgsRead(
            LinhuiAsyncHttpResponseHandler Handler) {
        UserApi.setAllMsgsRead(MyAsyncHttpClient.MyAsyncHttpClient2(),Handler);
    }

}
