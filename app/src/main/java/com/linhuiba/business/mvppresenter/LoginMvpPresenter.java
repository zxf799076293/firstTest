package com.linhuiba.business.mvppresenter;

import android.util.Log;

import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvpview.LoginMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/2/5.
 */

public class LoginMvpPresenter extends BasePresenter<LoginMvpView> {
    public void loginApi(String userName, String pw) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        LoginMvpModel.getLoginData(userName, pw, new LinhuiAsyncHttpResponseHandler(LoginInfo.class) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        LoginInfo loginInfo = (LoginInfo) data;
                        getView().onAppLoginSuccess(loginInfo);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.login_login_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onAppLoginFailure(superresult, error);
                }
            }
        });
    }
    public void weChatLoginApi(String code,int province_id,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        LoginMvpModel.getWeChatLoginData(code, province_id, city_id,
        new LinhuiAsyncHttpResponseHandler(LoginInfo.class) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        LoginInfo loginInfo = (LoginInfo) data;
                        getView().onWeChatLoginSuccess(loginInfo);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.login_login_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onAppLoginFailure(superresult, error);
                }
            }
        });
    }
    public void qqLogin(String unionid,int province_id,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        getView().showLoading();
        LoginMvpModel.getQQLoginData(unionid, province_id, city_id,
                new LinhuiAsyncHttpResponseHandler(LoginInfo.class) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                LoginInfo loginInfo = (LoginInfo) data;
                                getView().onQQLoginSuccess(loginInfo);
                            } else {
                                getView().showToast(getView().getContext().getResources().getString(R.string.login_login_error_text));
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()) {
                            getView().hideLoading();
                            getView().onAppLoginFailure(superresult, error);
                        }
                    }
                });
    }
    public void getFastCaptcha(String mobile,String usage) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        LoginMvpModel.getFastCaptcha(mobile, usage,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onCaptchaSuccess(response);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.txt_register_send_codeb_error_remind));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onAppLoginFailure(superresult, error);
                }
            }
        });
    }
    public void fastLogin(String mobile, String code) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        LoginMvpModel.fastLogin(mobile, code,
                new LinhuiAsyncHttpResponseHandler(LoginInfo.class) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                LoginInfo loginInfo = (LoginInfo) data;
                                getView().onFastLoginSuccess(loginInfo);
                            } else {
                                getView().showToast(getView().getContext().getResources().getString(R.string.login_login_error_text));
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()) {
                            getView().hideLoading();
                            getView().onAppLoginFailure(superresult, error);
                        }
                    }
                });
    }
}
