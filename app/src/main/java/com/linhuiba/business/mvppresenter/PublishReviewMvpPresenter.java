package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.mvpmodel.PublishReviewMvpModel;
import com.linhuiba.business.mvpview.PublishReviewMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

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
    public void confirmReview(String fieldid,int score,int score_of_visitorsflowrate,
                              int score_of_userparticipation,int score_of_propertymatching,
                              int score_of_goalcompletion , String content,int anonymity,
                              String tags,String images,String number_of_people) {
        if (!isViewAttached()) {
            return;
        }
        PublishReviewMvpModel.confirmReview(fieldid,score,score_of_visitorsflowrate,score_of_userparticipation,
                score_of_propertymatching,score_of_goalcompletion,content,anonymity,tags,images,number_of_people,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            if (response.code == 1) {
                                getView().onReviewSuccess();
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
