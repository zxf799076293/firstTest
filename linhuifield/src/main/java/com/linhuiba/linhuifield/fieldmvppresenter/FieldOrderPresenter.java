package com.linhuiba.linhuifield.fieldmvppresenter;

import com.linhuiba.linhuifield.fieldbasemvp.FieldBasePresenter;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldmvpmodel.FieldOrderMvpModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldOrderMvpView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;

import okhttp3.internal.http2.Header;

public class FieldOrderPresenter extends FieldBasePresenter<FieldOrderMvpView> {
    public void getVirtualNumber(String id,int type) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldOrderMvpModel.getVirtualNumber(id,type, new LinhuiAsyncHttpResponseHandler(Field_FieldlistModel.class, true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                               getView().onVirtualNumberSuccess(data.toString());
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onVirtualNumberFailure(superresult, error);
                        }

                    }
                });
    }

}
