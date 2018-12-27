package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.ModifyUserInfoMvpModel;
import com.linhuiba.business.mvpview.ModifyUserInfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/4/11.
 */

public class ModifyUserInfoMvpPressenter extends BasePresenter<ModifyUserInfoMvpView> {
    public void modifyUserInfo(int type,String info) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        ModifyUserInfoMvpModel.modifyUserInfo(type, info, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onModifyUserInfoSuccess(response.msg);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onModifyUserInfoFailure(superresult, error);
                }
            }
        });
    }
    public void setUserName(String info) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        ModifyUserInfoMvpModel.setUserName(info, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onSetUserNameSuccess(response.msg);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onModifyUserInfoFailure(superresult, error);
                }
            }
        });
    }

}
