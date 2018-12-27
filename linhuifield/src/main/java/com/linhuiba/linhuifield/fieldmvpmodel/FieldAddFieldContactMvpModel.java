package com.linhuiba.linhuifield.fieldmvpmodel;

import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;

public class FieldAddFieldContactMvpModel {
    public static void getBeneficiary(LinhuiAsyncHttpResponseHandler handler) {
        Field_FieldApi.getBeneficiary(MyAsyncHttpClient.MyAsyncHttpClient3(),handler);
    }
}
