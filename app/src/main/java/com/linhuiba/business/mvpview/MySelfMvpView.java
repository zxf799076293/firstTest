package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.Badge_infoModel;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface MySelfMvpView extends BaseView {
    void onBadgeSuccess(Badge_infoModel badeInfoModel);
    void onBadgeFailure(boolean superresult, Throwable error);
    void onProfileSuccess(LoginInfo loginInfos);
    void onBindWXSuccess();
    void onUnBindWXSuccess();
    void onBindQQSuccess();
    void onUnBindQQSuccess();
}
