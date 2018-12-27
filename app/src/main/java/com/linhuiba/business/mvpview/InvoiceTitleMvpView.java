package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/21.
 */

public interface InvoiceTitleMvpView extends BaseView {
    void onInvoiceTitleListSuccess(ArrayList<InvoiceInfomationModel> data);
    void onInvoiceTitleListMoreSuccess(ArrayList<InvoiceInfomationModel> data);
    void onInvoiceTitleListMoreFailure(boolean superresult, Throwable error);
    void onInvoiceTitleInfoSuccess(InvoiceInfomationModel data);
    void onInvoiceTitleAddSuccess();
    void onInvoiceTitleEditSuccess();
    void onInvoiceTitleDeleteSuccess();
    void onInvoiceTitleDefaultSuccess();
    void onInvoiceTitleFailure(boolean superresult, Throwable error);
}
