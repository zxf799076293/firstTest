package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.PublishReviewMvpModel;
import com.linhuiba.business.mvpview.PublishReviewMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/7/12.
 */

public class PublishReviewMvpPresenter extends BasePresenter<PublishReviewMvpView> {
    public void getReviewInfo(String field_order_item_id) {
        if (!isViewAttached()) {
            return;
        }
        PublishReviewMvpModel.getReviewInfo(field_order_item_id,
                new LinhuiAsyncHttpResponseHandler(ReviewFieldInfoModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        ReviewFieldInfoModel reviewFieldInfoModel = (ReviewFieldInfoModel)data;
                        getView().onReviewInfoSuccess(reviewFieldInfoModel);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onReviewFailure(superresult,error);
                }
            }
        });
    }
    public void confirmReview(String fieldid,int score,int score_of_visitorsflowrate
            ,int score_of_propertymatching,
                              int score_of_goalcompletion , String content,int anonymity,
                              ArrayList<Integer> tags,ArrayList<String> images,ArrayList<Integer> field_order_item_ids) {
        if (!isViewAttached()) {
            return;
        }
        if (field_order_item_ids != null && field_order_item_ids.size() > 0) {
            PublishReviewMvpModel.commentsOrderItems(score,score_of_visitorsflowrate,
                    score_of_propertymatching,score_of_goalcompletion,content,anonymity,tags,images,field_order_item_ids,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                if (response.code == 1) {
                                    getView().onReviewSuccess();
                                } else {
                                    getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                                }
                            }
                        }

                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().onReviewFailure(superresult,error);
                            }
                        }
                    });

        } else {
            PublishReviewMvpModel.confirmReview(fieldid,score,score_of_visitorsflowrate,
                    score_of_propertymatching,score_of_goalcompletion,content,anonymity,tags,images,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                if (response.code == 1) {
                                    getView().onReviewSuccess();
                                } else {
                                    getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                                }
                            }
                        }

                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().onReviewFailure(superresult,error);
                            }
                        }
                    });
        }
    }
    public void getCommentCentre() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        PublishReviewMvpModel.getCommentCentre(
                new LinhuiAsyncHttpResponseHandler(ReviewModel.class, true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                getView().onResReviewSuccess((ArrayList<ReviewModel>) data,null);
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
    public void getComments(String fieldid, final int page, int pageSize,boolean isActivity) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        if (isActivity) {
            PublishReviewMvpModel.getSellResCpmments(fieldid, String.valueOf(page), String.valueOf(pageSize) ,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if(isViewAttached()) {
                                getView().hideLoading();
                                if (data != null && data.toString().length() > 0) {
                                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                    if (jsonObject.get("data") != null &&
                                            jsonObject.get("data").toString().length() > 0) {
                                       String detailScore = "";
                                        if (jsonObject.get("meta") != null &&
                                                jsonObject.get("meta").toString().length() > 0) {
                                            detailScore = jsonObject.get("meta").toString();
                                        }
                                        ArrayList<ReviewModel> list = (ArrayList<ReviewModel>) JSONArray.parseArray(jsonObject.get("data").toString(), ReviewModel.class);
                                        if (page > 1) {
                                            getView().onResReviewMoreSuccess(list,detailScore);
                                        } else {
                                            getView().onResReviewSuccess(list,detailScore);
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if(isViewAttached()){
                                getView().hideLoading();
                                if (page > 1) {
                                    getView().onResReviewMoreFailure(superresult, error);
                                } else {
                                    getView().onResReviewFailure(superresult, error);
                                }
                            }
                        }
                    });
        } else {
            PublishReviewMvpModel.getPhyResComments(fieldid, String.valueOf(page), String.valueOf(pageSize) ,
                    new LinhuiAsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if(isViewAttached()) {
                                getView().hideLoading();
                                if (data != null && data.toString().length() > 0) {
                                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                    if (jsonObject.get("data") != null &&
                                            jsonObject.get("data").toString().length() > 0) {
                                        String detailScore = "";
                                        if (jsonObject.get("meta") != null &&
                                                jsonObject.get("meta").toString().length() > 0) {
                                            detailScore = jsonObject.get("meta").toString();
                                        }
                                        ArrayList<ReviewModel> list = (ArrayList<ReviewModel>) JSONArray.parseArray(jsonObject.get("data").toString(), ReviewModel.class);
                                        if (page > 1) {
                                            getView().onResReviewMoreSuccess(list,detailScore);
                                        } else {
                                            getView().onResReviewSuccess(list,detailScore);
                                        }
                                    }
                                }
                            }

                        }
                        @Override
                        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if(isViewAttached()){
                                getView().hideLoading();
                                if (page > 1) {
                                    getView().onResReviewMoreFailure(superresult, error);
                                } else {
                                    getView().onResReviewFailure(superresult, error);
                                }
                            }
                        }
                    });
        }
    }
}
