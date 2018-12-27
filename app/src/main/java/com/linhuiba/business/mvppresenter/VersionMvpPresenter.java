package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.Version;
import com.linhuiba.business.mvpmodel.VersionMvpModel;
import com.linhuiba.business.mvpview.VersionMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/4/24.
 */

public class VersionMvpPresenter extends BasePresenter<VersionMvpView> {
    public void getVersion() {
        if (!isViewAttached()) {
            return;
        }
        VersionMvpModel.getVersion(new LinhuiAsyncHttpResponseHandler(Version.class) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    if (data != null && data instanceof Version) {
                        Version version = (Version) data;
                        getView().onVersionSuccess(version);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onVersionFailure(superresult, error);
                }
            }
        });
    }
}
