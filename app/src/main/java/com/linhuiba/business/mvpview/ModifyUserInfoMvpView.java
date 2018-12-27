package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;

/**
 * Created by Administrator on 2018/4/11.
 */

public interface ModifyUserInfoMvpView extends BaseView{
    void onModifyUserInfoSuccess(String msg);
    void onModifyUserInfoFailure(boolean superresult, Throwable error);
    void onSetUserNameSuccess(String msg);
}
