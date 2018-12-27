package com.linhuiba.linhuifield.fieldmvpview;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseView;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;

import java.util.ArrayList;

public interface FieldAddfieldChooseResOptionsaMvpView extends FieldBaseView {
    void onSearchCommunitySuccess(ArrayList<AddfieldSearchCommunityModule> data);
    void onSearchCommunityFailure(boolean superresult, Throwable error);
    void onAttributesSuccess(FieldAddfieldAttributesModel data);
    void onAttributesFailure(boolean superresult, Throwable error);
}
