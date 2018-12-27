package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.MyPushMsgModel;
import com.linhuiba.business.mvpmodel.MyPushMsgMvpModel;
import com.linhuiba.business.mvpmodel.MySelfMvpModel;
import com.linhuiba.business.mvpview.MyPushMsgMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class MyPushMsgMvpPresenter extends BasePresenter<MyPushMsgMvpView> {
    public void getUserMsg(String id, int type, final int page) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        MyPushMsgMvpModel.getUserMsg(id, type, page, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() >0) {
                            ArrayList<MyPushMsgModel> list = (ArrayList<MyPushMsgModel>) JSONArray.parseArray(jsonObject.get("data").toString(),MyPushMsgModel.class);
                            String lh_count = "0";
                            String sys_count = "0";
                            if (jsonObject.get("unread_sys_count") != null &&
                                    jsonObject.get("unread_sys_count").toString().length() >0) {
                                sys_count = jsonObject.get("unread_sys_count").toString();
                            }
                            if (jsonObject.get("unread_lh_count") != null &&
                                    jsonObject.get("unread_lh_count").toString().length() >0) {
                                lh_count = jsonObject.get("unread_lh_count").toString();
                            }

                            if (page > 1) {
                                getView().onUserPushMsgMoreSuccess(list,sys_count,lh_count);
                            } else {
                                getView().onUserPushMsgSuccess(list,sys_count,lh_count);
                            }
                        } else {
                            getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                        }
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    if (page > 1) {
                        getView().onUserPushMsgMoreFailure(superresult, error);
                    } else {
                        getView().onUserPushMsgFailure(superresult, error);
                    }
                }
            }
        });
    }
    public void deleteUserMsgs(ArrayList<Integer> ids) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        MyPushMsgMvpModel.deleteUserMsgs(ids, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onDeleteUserPushMsgSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onDeleteUserPushMsgFailure(superresult, error);
                }
            }
        });
    }
    public void setMsgsRead(String uid, ArrayList<Integer> ids) {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        MyPushMsgMvpModel.setMsgsRead(uid,ids, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onSetUserPushMsgReadSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onSetUserPushMsgReadFailure(superresult, error);
                }
            }
        });
    }
    public void setAllMsgsRead() {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        MyPushMsgMvpModel.setAllMsgsRead(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (response.code == 1) {
                        getView().onSetUserPushMsgReadSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onSetUserPushMsgReadFailure(superresult, error);
                }
            }
        });
    }

}
