package com.linhuiba.linhuifield.fieldmvpmodel;

import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;

public class FieldFieldListMvpModel {
    public static void getFieldList(int res_type_id, String status, String page,String pageSize,
                                        LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getresourceslist(MyAsyncHttpClient.MyAsyncHttpClient3(), handler, res_type_id, status,
                page, pageSize);
    }
    public static void getFieldInfo(String fieldId, LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getresources(MyAsyncHttpClient.MyAsyncHttpClient(), handler, fieldId);
    }
    public static void editStatusThrough(String fieldId, LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.editFieldStatusthrough(MyAsyncHttpClient.MyAsyncHttpClient3(), handler,
                fieldId);
    }
    public static void getFieldResCreate(LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getresources_create(MyAsyncHttpClient.MyAsyncHttpClient(),handler);
    }



}
