package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.mvpmodel.InvoiceInfoMvpModel;
import com.linhuiba.business.mvpview.InvoiceInfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/3/5.
 */

public class InvoiceInfoMvpPresenter extends BasePresenter<InvoiceInfoMvpView> {
    public void getInvoiceInfoTaxPoitn() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceInfoMvpModel.getTaxPoint(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject != null && jsonObject.get("data") != null) {
                            getView().onInvoiceTaxPointSuccess(jsonObject.get("data").toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onInvoiceTaxPointFailure(superresult, error);
                }
            }
        });
    }
    public void getInvoiceInfo() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceInfoMvpModel.getInvoiceInfo(new LinhuiAsyncHttpResponseHandler(InvoiceInfomationModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        InvoiceInfomationModel invoiceInfomationModel = (InvoiceInfomationModel) data;
                        getView().onInvoiceInfoSuccess(invoiceInfomationModel);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onInvoiceTaxPointFailure(superresult, error);
                }
            }
        });
    }
    public void addInvoice(ArrayList<HashMap<Object,Object>> content, int invoice_title_id,
                           int is_paper, int invoice_content_id, int consignee_addresses_id,
                           String email, String remark) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceInfoMvpModel.addInvoice(content, invoice_title_id, is_paper,invoice_content_id,
                consignee_addresses_id,
                email,remark,
                new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onAddInvoiceSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onAddInvoiceFailure(superresult, error);
                }
            }
        });
    }


}
