package com.linhuiba.linhuifield.fieldmvpmodel;

import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;

public class FieldAddFieldMainMvpModel {
    public static void addCommunities(LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.addCommunities(MyAsyncHttpClient.MyAsyncHttpClient3(), handler);
    }
    public static void updateCommunities(int id, LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.updateCommunities(id,MyAsyncHttpClient.MyAsyncHttpClient(), handler);
    }
    public static void getDefaultAddress(LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getDefaultAddress(MyAsyncHttpClient.MyAsyncHttpClient(), handler);
    }

}
