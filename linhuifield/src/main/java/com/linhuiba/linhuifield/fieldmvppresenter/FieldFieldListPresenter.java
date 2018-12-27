package com.linhuiba.linhuifield.fieldmvppresenter;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBasePresenter;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldmvpmodel.FieldFieldListMvpModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldFieldListMvpView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class FieldFieldListPresenter extends FieldBasePresenter<FieldFieldListMvpView> {
    public void getFieldListData(int res_type_id, String status, final String page, String pageSize) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldFieldListMvpModel.getFieldList(res_type_id,status,
                page, pageSize, new LinhuiAsyncHttpResponseHandler(Field_FieldlistModel.class, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        ArrayList<Field_FieldlistModel> list = (ArrayList<Field_FieldlistModel>) data;
                        if (Integer.parseInt(page) > 1) {
                            getView().onFieldListMoreSuccess(list);
                        } else {
                            getView().onFieldListSuccess(list);
                        }
                    }
                }

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (Integer.parseInt(page) > 1) {
                        getView().onFieldListMoreFailure(superresult, error);
                    } else {
                        getView().onFieldListFailure(superresult, error);
                    }
                }

            }
        });
    }
    public void getFieldinfo(String fieldId) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldFieldListMvpModel.getFieldInfo(fieldId,
                new LinhuiAsyncHttpResponseHandler(Field_AddResourcesInfoModel.class, false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                Field_AddResourcesInfoModel list = (Field_AddResourcesInfoModel) data;
                                getView().onFieldInfoSuccess(list);
                            } else {
                                getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldInfoFailure(superresult,error);
                        }
                    }
                });
    }
    public void editStatusThrough(String fieldId) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldFieldListMvpModel.editStatusThrough(fieldId,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldThroughSuccess();
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldInfoFailure(superresult,error);
                        }
                    }
                });
    }
    public void getFieldResCreate() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldFieldListMvpModel.getFieldResCreate(
                new LinhuiAsyncHttpResponseHandler(Field_AddResourceCreateModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()) {
                            getView().hideLoading();
                            if (data != null) {
                                Field_AddResourceCreateModel list = (Field_AddResourceCreateModel) data;
                                getView().onFieldResCreateSuccess(list);
                            }
                        }
                    }
                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldResCreateFailure(superresult,error);
                        }
                    }
                });
    }
}
