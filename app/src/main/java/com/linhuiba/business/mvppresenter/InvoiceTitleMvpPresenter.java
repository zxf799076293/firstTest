package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.mvpmodel.InvoiceTitleMvpModel;
import com.linhuiba.business.mvpview.InvoiceTitleMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/7/21.
 */

public class InvoiceTitleMvpPresenter extends BasePresenter<InvoiceTitleMvpView> {
    public void getInvoiceTitleList(int invoice_type, final int page) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.getInvoiceTitleList(invoice_type, page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<InvoiceInfomationModel> list = (ArrayList<InvoiceInfomationModel>) JSONArray.parseArray(jsonObject.get("data").toString(),InvoiceInfomationModel.class);
                            if (page > 1) {
                                getView().onInvoiceTitleListMoreSuccess(list);
                            } else {
                                getView().onInvoiceTitleListSuccess(list);
                            }
                        } else {
                            getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                        }
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    if (page > 1) {
                        getView().onInvoiceTitleListMoreFailure(superresult, error);
                    } else {
                        getView().onInvoiceTitleFailure(superresult, error);
                    }
                }
            }
        });
    }
    public void addInvoiceTitle(InvoiceInfomationModel invoiceInfomationModel) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.addInvoiceTitle(invoiceInfomationModel, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onInvoiceTitleAddSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onInvoiceTitleFailure(superresult, error);
                }
            }
        });
    }
    public void editInvoiceTitle(InvoiceInfomationModel invoiceInfomationModel) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.editInvoiceTitle(invoiceInfomationModel, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onInvoiceTitleEditSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onInvoiceTitleFailure(superresult, error);
                }
            }
        });
    }
    public void deleteInvoiceTitle(int id) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.deleteInvoiceTitle(id, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onInvoiceTitleDeleteSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onInvoiceTitleFailure(superresult, error);
                }
            }
        });
    }
    public void getInvoiceTitle(int id) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.getInvoiceTitle(id, new LinhuiAsyncHttpResponseHandler(InvoiceInfomationModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        InvoiceInfomationModel invoiceInfomationModel = (InvoiceInfomationModel) data;
                        getView().onInvoiceTitleInfoSuccess(invoiceInfomationModel);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onInvoiceTitleFailure(superresult, error);
                }
            }
        });
    }
    public void setInvoiceTitleDefault(int id) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        InvoiceTitleMvpModel.setInvoiceTitleDefault(id, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onInvoiceTitleDefaultSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onInvoiceTitleFailure(superresult, error);
                }
            }
        });
    }
}
