package com.linhuiba.linhuifield.fieldmvpmodel;

import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;

public class FieldOrderMvpModel {
    public static void getVirtualNumber(String field_order_item_id,int type,
                         LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getVirtualNumber(MyAsyncHttpClient.MyAsyncHttpClient(),
                handler, field_order_item_id,type);
    }
}
