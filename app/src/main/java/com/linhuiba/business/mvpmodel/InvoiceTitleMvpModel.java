package com.linhuiba.business.mvpmodel;

import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

/**
 * Created by Administrator on 2018/7/21.
 */

public class InvoiceTitleMvpModel {
    public static void getInvoiceTitleList(int invoice_type, int page,
                                    final LinhuiAsyncHttpResponseHandler
                                            handler) {
        FieldApi.getInvoiceTitleList(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                invoice_type, page);
    }
    public static void addInvoiceTitle(InvoiceInfomationModel invoiceInfomationModel,
                                           final LinhuiAsyncHttpResponseHandler
                                                   handler) {
        FieldApi.addInvoiceTitle(MyAsyncHttpClient.MyAsyncHttpClient2(), handler,
                invoiceInfomationModel);
    }
    public static void editInvoiceTitle(InvoiceInfomationModel invoiceInfomationModel,
                                           final LinhuiAsyncHttpResponseHandler
                                                   handler) {
        FieldApi.editInvoiceTitle(MyAsyncHttpClient.MyAsyncHttpClient2(), handler,
                invoiceInfomationModel);
    }
    public static void deleteInvoiceTitle(int id,
                                            final LinhuiAsyncHttpResponseHandler
                                                    handler) {
        FieldApi.deleteInvoiceTitle(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                id);
    }
    public static void getInvoiceTitle(int id,
                                          final LinhuiAsyncHttpResponseHandler
                                                  handler) {
        FieldApi.getInvoiceTitle(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                id);
    }
    public static void setInvoiceTitleDefault(int id,
                                       final LinhuiAsyncHttpResponseHandler
                                               handler) {
        FieldApi.setInvoiceTitleDefault(MyAsyncHttpClient.MyAsyncHttpClient(), handler,
                id);
    }
}
