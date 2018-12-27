package com.linhuiba.linhuifield.fieldmvpview;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseView;
import com.linhuiba.linhuifield.fieldmodel.AddressContactModel;

public interface FieldAddFieldMainMvpView extends FieldBaseView  {
    void onFieldAddCommunitySuccess();
    void onFieldAddCommunityFailure(boolean superresult, Throwable error);
    void onDefaultAddressSuccess(AddressContactModel addressContactModel);
}
