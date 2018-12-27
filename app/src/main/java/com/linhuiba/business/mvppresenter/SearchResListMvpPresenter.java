package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.mvpmodel.SearchResListMvpModel;
import com.linhuiba.business.mvpview.SearchResListMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldCommunityModel;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class SearchResListMvpPresenter extends BasePresenter<SearchResListMvpView> {
    public void getCommunityList(final ApiResourcesModel apiResourcesModel) {
        if (!isViewAttached()) {
            return;
        }
        SearchResListMvpModel.getCommunityList(apiResourcesModel,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null) {
                            ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                    ResourceSearchItemModel.class);

                            if (Integer.parseInt(apiResourcesModel.getPage()) > 1) {
                                getView().onSearchResListMoreSuccess(list,response);
                            } else {
                                getView().onSearchResListSuccess(list,response);
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
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSearchResListFailure(superresult, error);
                }
            }
        });
    }
    public void getPhyReslist(final ApiResourcesModel apiResourcesModel) {
        if (!isViewAttached()) {
            return;
        }
        SearchResListMvpModel.getPhyReslist(apiResourcesModel,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null) {
                            ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                    ResourceSearchItemModel.class);

                            if (Integer.parseInt(apiResourcesModel.getPage()) > 1) {
                                getView().onSearchResListMoreSuccess(list,response);
                            } else {
                                getView().onSearchResListSuccess(list,response);
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
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSearchResListFailure(superresult, error);
                }
            }
        });
    }
    public void getActivityList(String city_id,double lat,double lng, final int page) {
        if (!isViewAttached()) {
            return;
        }
        SearchResListMvpModel.getActivityList(city_id,page,lat,lng,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null) {
                            ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                    ResourceSearchItemModel.class);
                            if (page > 1) {
                                getView().onSearchResListMoreSuccess(list,response);
                            } else {
                                getView().onSearchResListSuccess(list,response);
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
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSearchResListFailure(superresult, error);
                }
            }
        });
    }
    public void getAttributesList(ArrayList<Integer> category_ids) {
        if (!isViewAttached()) {
            return;
        }
        SearchResListMvpModel.getAttributesList(category_ids,new LinhuiAsyncHttpResponseHandler(SearchListAttributesModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        getView().onAttributesSuccess((ArrayList<SearchListAttributesModel>) data);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onAttributesFailure(superresult, error);
                }
            }
        });
    }
    public void getSelfResList(final ApiAdvResourcesModel apiResourcesModel) {
        if (!isViewAttached()) {
            return;
        }
        SearchResListMvpModel.getFastList(apiResourcesModel,new LinhuiAsyncHttpResponseHandler(ResourceSearchItemModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>)data;
                        if (Integer.parseInt(apiResourcesModel.getPage()) > 1) {
                            getView().onSearchResListMoreSuccess(list,response);
                        } else {
                            getView().onSearchResListSuccess(list,response);
                        }
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSearchResListFailure(superresult, error);
                }
            }
        });
    }

}
