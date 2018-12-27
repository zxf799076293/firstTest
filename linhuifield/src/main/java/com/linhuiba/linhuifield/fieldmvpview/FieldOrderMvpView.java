package com.linhuiba.linhuifield.fieldmvpview;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseView;

public interface FieldOrderMvpView extends FieldBaseView {
    void onVirtualNumberSuccess(String data);
    void onVirtualNumberFailure(boolean superresult, Throwable error);
}
