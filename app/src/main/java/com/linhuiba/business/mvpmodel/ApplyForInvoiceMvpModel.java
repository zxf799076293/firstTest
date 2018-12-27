package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ApplyForInvoiceMvpModel {
    public static void getNoInvoiceList(String page,String pageSize,String tax_type,
                                        LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getnoinvoices(MyAsyncHttpClient.MyAsyncHttpClient3(), handler,
                page, pageSize,tax_type);
    }
    public static void getAlreadyInvoiceList(String page,String pageSize,LinhuiAsyncHttpResponseHandler
                                             handler) {
        FieldApi.getalreadyinvoices(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                page, pageSize);
    }
}
