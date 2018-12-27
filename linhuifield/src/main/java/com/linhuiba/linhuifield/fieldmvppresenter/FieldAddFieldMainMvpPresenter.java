package com.linhuiba.linhuifield.fieldmvppresenter;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBasePresenter;
import com.linhuiba.linhuifield.fieldmodel.AddressContactModel;
import com.linhuiba.linhuifield.fieldmvpmodel.FieldAddFieldMainMvpModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldMainMvpView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class FieldAddFieldMainMvpPresenter extends FieldBasePresenter<FieldAddFieldMainMvpView> {
    public void addCommunities() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddFieldMainMvpModel.addCommunities(
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddCommunitySuccess();
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddCommunityFailure(superresult,error);
                        }

                    }
                });
    }
    public void updateCommunities(int id) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddFieldMainMvpModel.updateCommunities(id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddCommunitySuccess();
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddCommunityFailure(superresult,error);
                        }

                    }
                });
    }
    public void getDefaultAddress() {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddFieldMainMvpModel.getDefaultAddress(
                new LinhuiAsyncHttpResponseHandler(AddressContactModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            if (data != null && data.toString().length() > 0) {
                                AddressContactModel addressContactModel = (AddressContactModel)data;
                                getView().onDefaultAddressSuccess(addressContactModel);
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddCommunityFailure(superresult,error);
                        }

                    }
                });
    }


}
