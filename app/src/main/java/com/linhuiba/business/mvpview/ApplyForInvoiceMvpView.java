package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;

/**
 * Created by Administrator on 2018/4/3.
 */

public interface ApplyForInvoiceMvpView extends BaseView {
    void onNoInvoiceListSuccess(Object data);
    void onNoInvoiceListFailure(boolean superresult, Throwable error);
    void onNoInvoiceListMoreSuccess(Object data);
    void onNoInvoiceListMoreFailure(boolean superresult, Throwable error);
}
