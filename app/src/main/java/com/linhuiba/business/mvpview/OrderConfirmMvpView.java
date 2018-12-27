package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.model.MyCouponsModel;

import java.util.ArrayList;

public interface OrderConfirmMvpView extends BaseView {
    void onDefaultAddressSuccess(AddressContactModel addressContactModel);
    void onDefaultAddressFailure(boolean superresult, Throwable error);
    void onUserProfileSuccess(Object data);
    void onUserProfileFailure(boolean superresult, Throwable error);
    void onCreateOrderPaySuccess(Object data);
    void onCreateOrderPayFailure(boolean superresult, Throwable error);
    void onOrderCouponsListSuccess(ArrayList<MyCouponsModel> data);
    void onOrderCouponsListFailure(boolean superresult, Throwable error);
    void onOrderNoticedSuccess();
}
