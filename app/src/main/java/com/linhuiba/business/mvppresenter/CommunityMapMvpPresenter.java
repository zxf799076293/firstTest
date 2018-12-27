package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.MapCommunitySearchListModel;
import com.linhuiba.business.model.MapCommunityInfoModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.mvpmodel.CommunityMapMvpModel;
import com.linhuiba.business.mvpview.CommunityMapMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class CommunityMapMvpPresenter extends BasePresenter<CommunityMapMvpView> {
    public void getCommunityAllPhyRes(int community_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CommunityMapMvpModel.getCommunityAllPhyRes(community_id,
                new LinhuiAsyncHttpResponseHandler(MapCommunityInfoModel.class,true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            if (data != null && data.toString().length() > 0) {
                                getView().onOtherPhyResSuccess((ArrayList<MapCommunityInfoModel>) data);
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        getView().onOtherPhyResFailure(superresult,error);
                    }
                });
    }
    public void getMapResourcesList(BaiduMapActivity baiduMapActivity,
                                    final ApiResourcesModel apiResourcesModel) {
        if (!isViewAttached()) {
            return;
        }
        CommunityMapMvpModel.getMapResourcesList(baiduMapActivity,apiResourcesModel,
                new LinhuiAsyncHttpResponseHandler(MapCommunitySearchListModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            if (data != null) {
                                getView().onMapSearchSuccess((MapCommunitySearchListModel) data, response);
                            } else {
                                getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().onMapSearchFailure(superresult, error);
                        }
                    }
                });
    }
    public void getAttributesList(ArrayList<Integer> category_ids) {
        if (!isViewAttached()) {
            return;
        }
        CommunityMapMvpModel.getAttributesList(category_ids,new LinhuiAsyncHttpResponseHandler(SearchListAttributesModel.class,true) {
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

}
