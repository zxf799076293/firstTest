package com.linhuiba.linhuifield.fieldmvpview;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseView;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;

import java.util.ArrayList;

public interface FieldFieldListMvpView extends FieldBaseView {
    void onFieldListSuccess(ArrayList<Field_FieldlistModel> data);
    void onFieldListFailure(boolean superresult, Throwable error);
    void onFieldListMoreSuccess(ArrayList<Field_FieldlistModel> data);
    void onFieldListMoreFailure(boolean superresult, Throwable error);
    void onFieldInfoSuccess(Field_AddResourcesInfoModel data);
    void onFieldInfoFailure(boolean superresult, Throwable error);
    void onFieldThroughSuccess();
    void onFieldResCreateSuccess(Field_AddResourceCreateModel data);
    void onFieldResCreateFailure(boolean superresult, Throwable error);
}
