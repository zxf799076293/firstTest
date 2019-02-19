package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.CommunityInfoModel;
import com.linhuiba.business.model.GroupBookingInfoModel;
import com.linhuiba.business.model.PhyResInfoModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.FieldinfoMvpModel;
import com.linhuiba.business.mvpview.FieldinfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/2/28.
 */

public class FieldinfoMvpPresenter extends BasePresenter<FieldinfoMvpView> {
    public void getResInfo(String id, ApiResourcesModel apiResourcesModel) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getPhyResInfo(id,apiResourcesModel,
                new LinhuiAsyncHttpResponseHandler(PhyResInfoModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onFieldinfoResSuccess((PhyResInfoModel)data);
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onFieldinfoFailure(superresult, error);
                }
            }
        });
    }
    public void getResInfoReview(String fieldid,String page,String pageSize,boolean isActivity) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        if (isActivity) {
            FieldinfoMvpModel.getSellResInfoCpmments(fieldid, page, pageSize ,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if(isViewAttached()) {
                                getView().hideLoading();
                                if (data != null && data.toString().length() > 0) {
                                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                    if (jsonObject.get("data") != null &&
                                            jsonObject.get("data").toString().length() > 0) {
                                        ArrayList<ReviewModel> list = (ArrayList<ReviewModel>) JSONArray.parseArray(jsonObject.get("data").toString(), ReviewModel.class);
                                        getView().onResReviewSuccess(list);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if(isViewAttached()){
                                getView().hideLoading();
                                getView().onResReviewFailure(superresult, error);
                            }
                        }
                    });

        } else {
            FieldinfoMvpModel.getResInfoReviewData(fieldid, page, pageSize ,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if(isViewAttached()) {
                                getView().hideLoading();
                                if (data != null && data.toString().length() > 0) {
                                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                    if (jsonObject.get("data") != null &&
                                            jsonObject.get("data").toString().length() > 0) {
                                        ArrayList<ReviewModel> list = (ArrayList<ReviewModel>) JSONArray.parseArray(jsonObject.get("data").toString(), ReviewModel.class);
                                        getView().onResReviewSuccess(list);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if(isViewAttached()){
                                getView().hideLoading();
                                getView().onResReviewFailure(superresult, error);
                            }
                        }
                    });
        }
    }
    public void getGroupResInfo(String id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getGroupResInfoData(id,
                new LinhuiAsyncHttpResponseHandler(GroupBookingInfoModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                getView().onGroupinfoResSuccess((GroupBookingInfoModel)data);
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldinfoFailure(superresult, error);
                        }
                    }
                });
    }
    public void getNearbyResList(int id, int city_id, final int page, int pageSize) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getNearbyResData(id,city_id,page,pageSize,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null && data.toString().length() > 0) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null &&
                                        jsonObject.get("data").toString().length() >0) {
                                    ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),ResourceSearchItemModel.class);
                                    if (page > 1) {
                                        getView().onNearbyResMoreSuccess(list);
                                    } else {
                                        getView().onNearbyResSuccess(list);
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

                    }
                });
    }
    public void getOtherPhyRes(int community_id,final int page, int pageSize, int physical_resource_id,
                               ApiResourcesModel apiResourcesModel) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getOtherPhyRes(community_id,page,pageSize,physical_resource_id,
                apiResourcesModel,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            if (data != null && data.toString().length() > 0) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null &&
                                        jsonObject.get("data").toString().length() >0) {
                                    int csprt = 0;
                                    if (jsonObject.get("csort") != null &&
                                            jsonObject.get("csort").toString().length() >0) {
                                        csprt = Integer.parseInt(jsonObject.get("csort").toString());
                                    }
                                    ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),ResourceSearchItemModel.class);
                                    if (page > 1) {
                                        getView().onOtherPhyResMoreSuccess(list);
                                    } else {
                                        getView().onOtherPhyResSuccess(list,csprt,response.total);
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
    public void getCommunityInfo(int id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getCommunityInfo(id,
                new LinhuiAsyncHttpResponseHandler(CommunityInfoModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                getView().onCommunityInfoSuccess((CommunityInfoModel)data);
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldinfoFailure(superresult, error);
                        }
                    }
                });
    }
    public void getRecommendedRes(int id, int page_size, final int page,
                                  int city_id,int category_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getRecommendedRes(id,page_size,page,city_id,category_id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null && data.toString().length() > 0) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null &&
                                        jsonObject.get("data").toString().length() >0) {
                                    ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),ResourceSearchItemModel.class);
                                    if (page > 1) {
                                        getView().onRecommendResMoreSuccess(list);
                                    } else {
                                        getView().onRecommendResSuccess(list);
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

                    }
                });
    }
    public void releaseFeedbacks(int resource_id,int community_id,String content) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.releaseFeedbacks(resource_id,community_id,content,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFeedbacksSuccess();
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFeedbacksFailure(superresult, error);
                        }
                    }
                });
    }
    public void getSellRes(String id,String res_type_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getSellRes(id,res_type_id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            if (data != null && data.toString().length() > 0) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null &&
                                        jsonObject.get("data").toString().length() >0) {
                                    ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),ResourceSearchItemModel.class);
                                    getView().onOtherPhyResMoreSuccess(list);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
    public void getSellResInfo(String id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.getSellResInfo(id,
                new LinhuiAsyncHttpResponseHandler(PhyResInfoModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                getView().onFieldinfoResSuccess((PhyResInfoModel)data);
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldinfoFailure(superresult, error);
                        }
                    }
                });
    }
    public void enquiry(String sid, String name, String mobile) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldinfoMvpModel.enquiry(sid, name, mobile,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (response.code  == 1 && data != null && data.toString().length() > 0) {
                                getView().onEnquirySuccess(data.toString());
                            } else {
                                getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onEnquiryFailure(superresult, error);
                        }
                    }
                });
    }

}
