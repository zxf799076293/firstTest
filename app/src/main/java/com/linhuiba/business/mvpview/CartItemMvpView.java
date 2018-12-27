package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.CommoditypayModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface CartItemMvpView extends BaseView {
    void onCartListSuccess(ArrayList<CommoditypayModel> list, String invalid_count);
    void onCartListFailure(boolean superresult, Throwable error);
    void onCartCountSuccess(String count);
    void onCartCountFailure(boolean superresult, Throwable error);
    void onDeleteCartSuccess(String data);
    void onDeleteCartFailure(boolean superresult, Throwable error);
}
