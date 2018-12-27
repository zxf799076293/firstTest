package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvpmodel.CouponsMvpModel;
import com.linhuiba.business.mvpmodel.OrderConfirmMvpModel;
import com.linhuiba.business.mvpview.OrderConfirmMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class OrderConfirmMvpPresenter extends BasePresenter<OrderConfirmMvpView> {
    public void getDefaultAddress() {
        if (!isViewAttached()) {
            return;
        }
        OrderConfirmMvpModel.getDefaultAddress(
                new com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler(AddressContactModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, com.linhuiba.linhuifield.network.Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            if (data != null && data.toString().length() > 0) {
                                AddressContactModel addressContactModel = (AddressContactModel)data;
                                getView().onDefaultAddressSuccess(addressContactModel);
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onDefaultAddressFailure(superresult,error);
                        }
                    }
                });
    }
    public void getUserprofile() {
        if (!isViewAttached()) {
            return;
        }
        OrderConfirmMvpModel.getUserprofile(
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onUserProfileSuccess(data);
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onUserProfileFailure(superresult,error);
                        }
                    }
                });
    }
    public void createOrderPay(
            JSONArray resources, int total_actual_price, int delegated, int need_decoration,
            int need_transportation, int need_invoice, String contact, String mobile,
            String address, String cart_item_ids,
            InvoiceInfomationModel invoiceInfomationModeldata) {
        if (!isViewAttached()) {
            return;
        }
        OrderConfirmMvpModel.createOrderPay(resources,total_actual_price,delegated,need_decoration,
                need_transportation,need_invoice,contact,mobile,
                address,cart_item_ids,invoiceInfomationModeldata,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onCreateOrderPaySuccess(data);
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onCreateOrderPayFailure(superresult,error);
                        }
                    }
                });
    }
    public void getOrderCommunityCoupons(ArrayList<Integer> community_ids,
                                         ArrayList<Integer> coupon_ids) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        OrderConfirmMvpModel.getOrderCommunityCoupons(community_ids,coupon_ids, new LinhuiAsyncHttpResponseHandler(MyCouponsModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) data;
                        getView().onOrderCouponsListSuccess(list);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onOrderCouponsListFailure(superresult, error);
                }
            }
        });
    }
    public void setOrderNoticed(int ordering_noticed) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        OrderConfirmMvpModel.setOrderNoticed(ordering_noticed, new LinhuiAsyncHttpResponseHandler(MyCouponsModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onOrderNoticedSuccess();
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
