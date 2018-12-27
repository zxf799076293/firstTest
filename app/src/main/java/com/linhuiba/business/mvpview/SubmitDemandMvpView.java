package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;

public interface SubmitDemandMvpView extends BaseView {
    void onSubmitDemandSuccess();
    void onSubmitDemandFailure(boolean superresult, Throwable error);
}
