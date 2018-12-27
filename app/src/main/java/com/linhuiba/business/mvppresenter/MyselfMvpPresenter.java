package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.Badge_infoModel;
import com.linhuiba.business.mvpmodel.MySelfMvpModel;
import com.linhuiba.business.mvpview.MySelfMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/4/24.
 */

public class MyselfMvpPresenter extends BasePresenter<MySelfMvpView> {
    public void getBadgeInfo() {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.getBadgeInfo(new LinhuiAsyncHttpResponseHandler(Badge_infoModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        Badge_infoModel badgeInfoModel = (Badge_infoModel)data;
                        getView().onBadgeSuccess(badgeInfoModel);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onBadgeFailure(superresult,error);
                }
            }
        });
    }
    public void getUserProfile() {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.getUserProfile(new LinhuiAsyncHttpResponseHandler(LoginInfo.class, false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        LoginInfo loginInfo = (LoginInfo) data;
                        getView().onProfileSuccess(loginInfo);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                }
            }
        });
    }
    public void bindWechat(String code) {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.bindWechat(code,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onBindWXSuccess();
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onBadgeFailure(superresult,error);
                }
            }
        });
    }
    public void unBindWechat() {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.unbindWechat(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onUnBindWXSuccess();
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onBadgeFailure(superresult,error);
                }
            }
        });
    }
    public void bindQQ(String unionid) {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.bindQQ(unionid,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onBindQQSuccess();
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onBadgeFailure(superresult,error);
                }
            }
        });
    }
    public void unbindQQ() {
        if (!isViewAttached()) {
            return;
        }
        MySelfMvpModel.unbindQQ(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onUnBindQQSuccess();
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onBadgeFailure(superresult,error);
                }
            }
        });
    }
}
