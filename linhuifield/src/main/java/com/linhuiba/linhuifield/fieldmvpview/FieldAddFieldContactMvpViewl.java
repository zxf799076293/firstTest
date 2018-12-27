package com.linhuiba.linhuifield.fieldmvpview;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseView;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;

import java.util.ArrayList;

public interface FieldAddFieldContactMvpViewl extends FieldBaseView {
    void onFieldAddContactSuccess(ArrayList<ReceiveAccountModel> list);
    void onFieldAddContactFailure(boolean superresult, Throwable error);
}
