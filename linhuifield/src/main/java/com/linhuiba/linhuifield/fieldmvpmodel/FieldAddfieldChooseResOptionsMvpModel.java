package com.linhuiba.linhuifield.fieldmvpmodel;

import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldSearchResActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;

public class FieldAddfieldChooseResOptionsMvpModel {
    public static void searchCommunites(int city_id, int district_id,int category_id,int res_type_id,String keywords,
                                        FieldAddFieldSearchResActivity activity,
                                    LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.searchCommunites(MyAsyncHttpClient.MyAsyncHttpClient(), handler, city_id, district_id,category_id,res_type_id,
                keywords,activity);
    }
    public static void getAttributes(int category_id,
                                        LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getAttributes(MyAsyncHttpClient.MyAsyncHttpClient(), handler, category_id);
    }


}
