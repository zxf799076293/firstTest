package com.linhuiba.linhuifield.fieldmvppresenter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBasePresenter;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;
import com.linhuiba.linhuifield.fieldmvpmodel.FieldAddFieldContactMvpModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldContactMvpViewl;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class FieldAddFieldContactMvpPresenter extends FieldBasePresenter<FieldAddFieldContactMvpViewl> {
    public void getBeneficiary() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddFieldContactMvpModel.getBeneficiary(
                new LinhuiAsyncHttpResponseHandler(ReceiveAccountModel.class, true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                ArrayList<ReceiveAccountModel> list = (ArrayList<ReceiveAccountModel>) data;
                                getView().onFieldAddContactSuccess(list);
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onFieldAddContactFailure(superresult,error);
                        }
                    }
                });
    }

}

