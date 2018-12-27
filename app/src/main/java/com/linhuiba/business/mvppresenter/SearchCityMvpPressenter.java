package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.SearchCityMvpModel;
import com.linhuiba.business.mvpview.SearchCityMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/4/10.
 */

public class SearchCityMvpPressenter extends BasePresenter<SearchCityMvpView> {
    public void getHotCityList() {
        if (!isViewAttached()) {
            //如果没有View引用就不加载数据
            return;
        }
        SearchCityMvpModel.getSearchCityData(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONArray jsonArray = JSONArray.parseArray(data.toString());
                        if (jsonArray != null && jsonArray.size() > 0) {
                            getView().onHotCitySuccess(jsonArray);
                        }
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onHotCityFailure(superresult, error);
                }
            }
        });
    }

}
