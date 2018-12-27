package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface InvoiceInfoMvpView extends BaseView {
    void onInvoiceTaxPointSuccess(String data);
    void onInvoiceTaxPointFailure(boolean superresult, Throwable error);
    void onInvoiceInfoSuccess(InvoiceInfomationModel data);
    void onAddInvoiceSuccess();
    void onAddInvoiceFailure(boolean superresult, Throwable error);
}
