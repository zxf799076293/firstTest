package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.SearchKeyWordsMvpModel;
import com.linhuiba.business.mvpview.SearchKeyWordsMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/2/28.
 */

public class SearchKeyWordsMvpPresenter extends BasePresenter<SearchKeyWordsMvpView> {
    public void getFastSearchList(SearchFieldAreaActivity activity, String keyword,
                                  String city_id, String res_type_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        SearchKeyWordsMvpModel.getResInfoData(activity, keyword,city_id,res_type_id,
                new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    if (data != null && data.toString().length() > 0) {
                        com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(data.toString());
                        if (jsonArray != null &&
                                jsonArray.size() > 0) {
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.get("name") != null &&
                                        jsonObject.get("name").toString().length() > 0) {
                                    list.add(jsonObject.get("name").toString());
                                }
                            }
                            getView().onSearchWordsSuccess(list);
                        }
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().onSearchWordsFailure(superresult,error);
                }
            }
        });
    }
}
