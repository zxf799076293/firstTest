package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.ApplyForInvoiceMvpModel;
import com.linhuiba.business.mvpview.ApplyForInvoiceMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ApplyForInvoiceMvpPresenter extends BasePresenter<ApplyForInvoiceMvpView> {
    public void getOnInvoiceData(final String page, String pageSize, String tax_type, boolean isAlready) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        if (isAlready) {
            ApplyForInvoiceMvpModel.getAlreadyInvoiceList(page, pageSize, new LinhuiAsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                    if(isViewAttached()){
                        getView().hideLoading();
                        if (data != null) {
                            if (Integer.parseInt(page) > 1) {
                                getView().onNoInvoiceListMoreSuccess(data);
                            } else {
                                getView().onNoInvoiceListSuccess(data);
                            }
                        }
                    }

                }

                @Override
                public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(isViewAttached()){
                        getView().hideLoading();
                        getView().onNoInvoiceListFailure(superresult, error);
                    }

                }
            });
        } else {
            ApplyForInvoiceMvpModel.getNoInvoiceList(page, pageSize, tax_type, new LinhuiAsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                    if(isViewAttached()){
                        getView().hideLoading();
                        if (data != null) {
                            if (Integer.parseInt(page) > 1) {
                                getView().onNoInvoiceListMoreSuccess(data);
                            } else {
                                getView().onNoInvoiceListSuccess(data);
                            }
                        }
                    }

                }

                @Override
                public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(isViewAttached()){
                        getView().hideLoading();
                        getView().onNoInvoiceListFailure(superresult, error);
                    }

                }
            });
        }
    }
}
