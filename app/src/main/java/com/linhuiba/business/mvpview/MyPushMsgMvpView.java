package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.MyPushMsgModel;

import java.util.ArrayList;

public interface MyPushMsgMvpView extends BaseView {
    void onUserPushMsgSuccess(ArrayList<MyPushMsgModel> data,String sys_count,String lh_count);
    void onUserPushMsgFailure(boolean superresult, Throwable error);
    void onUserPushMsgMoreSuccess(ArrayList<MyPushMsgModel> data,String sys_count,String lh_count);
    void onUserPushMsgMoreFailure(boolean superresult, Throwable error);
    void onDeleteUserPushMsgSuccess();
    void onDeleteUserPushMsgFailure(boolean superresult, Throwable error);
    void onSetUserPushMsgReadSuccess();
    void onSetUserPushMsgReadFailure(boolean superresult, Throwable error);
}
