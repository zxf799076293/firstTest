package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.model.ReviewModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/12.
 */

public interface PublishReviewMvpView extends BaseView {
    void onReviewInfoSuccess(ReviewFieldInfoModel mReviewFieldInfoModel);
    void onReviewSuccess();
    void onReviewFailure(boolean superresult, Throwable error);
    void onResReviewSuccess(ArrayList<ReviewModel> list,String detailScore);
    void onResReviewFailure(boolean superresult, Throwable error);
    void onResReviewMoreSuccess(ArrayList<ReviewModel> list,String detailScore);
    void onResReviewMoreFailure(boolean superresult, Throwable error);
}
