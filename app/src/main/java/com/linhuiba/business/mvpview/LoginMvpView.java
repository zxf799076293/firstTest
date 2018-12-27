package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;

/**
 * Created by Administrator on 2018/2/5.
 */

public interface LoginMvpView extends BaseView {
    void onAppLoginFailure(boolean superresult, Throwable error);
    void onAppLoginSuccess(LoginInfo loginInfo);
    void onWeChatLoginSuccess(LoginInfo loginInfo);
    void onCaptchaSuccess(Response response);
    void onFastLoginSuccess(LoginInfo loginInfo);
    void onQQLoginSuccess(LoginInfo loginInfo);
}
