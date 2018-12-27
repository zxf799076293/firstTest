package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.ReviewFieldInfoModel;

/**
 * Created by Administrator on 2018/7/12.
 */

public interface PublishReviewMvpView extends BaseView {
    void onReviewInfoSuccess(ReviewFieldInfoModel mReviewFieldInfoModel);
    void onReviewSuccess();
    void onReviewFailure(boolean superresult, Throwable error);
}
