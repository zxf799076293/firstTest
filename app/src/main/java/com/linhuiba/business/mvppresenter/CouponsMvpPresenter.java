package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvpmodel.CouponsMvpModel;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class CouponsMvpPresenter extends BasePresenter<CouponsMvpView> {
    public void getCouponsGifts(final int page) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.getCouponsGifts(page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) JSONArray.parseArray(jsonObject.get("data").toString(),MyCouponsModel.class);
                            if (page > 1) {
                                getView().onMyCouponsMoreSuccess(list);
                            } else {
                                getView().onMyCouponsListSuccess(list);
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
                        getView().onMyCouponsListMoreFailure(superresult, error);
                    } else {
                        getView().onMyCouponsListFailure(superresult, error);
                    }

                }
            }
        });
    }

    public void getCouponsList(int expired, int used, final int page) { //expired:是否过期 1/未过期 2/已过期;used:是否使用 1/未使用 2/已使用
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.getMyCoupons(expired, used, page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) JSONArray.parseArray(jsonObject.get("data").toString(),MyCouponsModel.class);
                            if (page > 1) {
                                getView().onMyCouponsMoreSuccess(list);
                            } else {
                                getView().onMyCouponsListSuccess(list);
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
                        getView().onMyCouponsListMoreFailure(superresult, error);
                    } else {
                        getView().onMyCouponsListFailure(superresult, error);
                    }

                }
            }
        });
    }
    public void getCouponsExchangeList(final int page) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.getCouponsExchangeList(page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) JSONArray.parseArray(jsonObject.get("data").toString(),MyCouponsModel.class);
                            if (page > 1) {
                                getView().onMyCouponsMoreSuccess(list);
                            } else {
                                getView().onMyCouponsListSuccess(list);
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
                        getView().onMyCouponsListMoreFailure(superresult, error);
                    } else {
                        getView().onMyCouponsListFailure(superresult, error);
                    }

                }
            }
        });
    }
    public void exchangeCoupons(ArrayList<Integer> coupons) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.exchangeCoupons(coupons, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onExchangeCouponsSuccess(response);
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onMyCouponsListFailure(superresult, error);
                }
            }
        });
    }
    public void receiveCoupons(int id, int quantity) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        CouponsMvpModel.receiveCoupons(id, quantity, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onExchangeCouponsSuccess(response);
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onMyCouponsListFailure(superresult, error);
                }
            }
        });
    }
    public void getCommunityCoupons(int community_id, final int is_received, int page_size, final int page) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.getCommunityCoupons(community_id, is_received, page_size, page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) JSONArray.parseArray(jsonObject.get("data").toString(),MyCouponsModel.class);
                            if (is_received == 2) {
                                getView().onMyCouponsMoreSuccess(list);
                            } else {
                                getView().onMyCouponsListSuccess(list);
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
                    if (is_received == 2) {
                        getView().onMyCouponsListMoreFailure(superresult, error);
                    } else {
                        getView().onMyCouponsListFailure(superresult, error);
                    }

                }
            }
        });
    }
    public void getCouponCentreList(int status, final int page) { //expired:是否过期 1/未过期 2/已过期;used:是否使用 1/未使用 2/已使用
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        CouponsMvpModel.getCouponCentreList(status, page, new LinhuiAsyncHttpResponseHandler(MyCouponsModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        ArrayList<MyCouponsModel> list = (ArrayList<MyCouponsModel>) data;
                        if (list != null) {
                            if (page > 1) {
                                getView().onMyCouponsMoreSuccess(list);
                            } else {
                                getView().onMyCouponsListSuccess(list);
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
                        getView().onMyCouponsListMoreFailure(superresult, error);
                    } else {
                        getView().onMyCouponsListFailure(superresult, error);
                    }

                }
            }
        });
    }

}
