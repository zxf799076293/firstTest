package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

public interface CouponsMvpView extends BaseView {
    void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data);
    void onMyCouponsMoreSuccess(ArrayList<MyCouponsModel> data);
    void onMyCouponsListFailure(boolean superresult, Throwable error);
    void onMyCouponsListMoreFailure(boolean superresult, Throwable error);
    void onExchangeCouponsSuccess(Response response);
}
