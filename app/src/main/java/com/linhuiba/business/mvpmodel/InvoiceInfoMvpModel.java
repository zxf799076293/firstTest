package com.linhuiba.business.mvpmodel;

import com.alibaba.fastjson.JSON;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/5.
 */

public class InvoiceInfoMvpModel {
    public static void getTaxPoint(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getInvoiceTaxPointContent(MyAsyncHttpClient.MyAsyncHttpClient(),
                handler);
    }
    public static void getInvoiceInfo(LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.getInvoiceInfo(MyAsyncHttpClient.MyAsyncHttpClient2(),
                handler);
    }
    public static void addInvoice(ArrayList<HashMap<Object,Object>> content, int invoice_title_id,
                                  int is_paper, int invoice_content_id,
                                  int consignee_addresses_id,
                                  String email, String remark, LinhuiAsyncHttpResponseHandler handler) {
        FieldApi.addInvoice(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),
                handler, content, invoice_title_id, is_paper,invoice_content_id,
                consignee_addresses_id,
                email,remark);
    }

}
