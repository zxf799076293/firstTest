package com.linhuiba.business.mvppresenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.CommoditypayModel;
import com.linhuiba.business.mvpmodel.CartItemsMvpModel;
import com.linhuiba.business.mvpview.CartItemMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/3/5.
 */

public class CartItemMvpPresenter extends BasePresenter<CartItemMvpView> {
    public void getCartItemCount() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CartItemsMvpModel.getCartCount(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onCartCountSuccess(data.toString());
                    }
                }

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onCartCountFailure(superresult, error);
                }
            }
        });
    }

    public void getCartItemList() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CartItemsMvpModel.getCartList(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject != null &&
                                jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() > 0) {
                            ArrayList<CommoditypayModel> list = (ArrayList<CommoditypayModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                    CommoditypayModel.class);
                            String invalidCount = null;
                            if (jsonObject.get("invalid_count") != null) {
                                invalidCount = jsonObject.get("invalid_count").toString();
                            }
                            getView().onCartListSuccess(list,invalidCount);
                        }
                    }
                }

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onCartListFailure(superresult, error);
                }

            }
        });
    }
    public void deleteCartItem(Context context, JSON cart_item_ids) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CartItemsMvpModel.deleteCartItemList(context ,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onDeleteCartSuccess(data.toString());
                    }
                }

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onDeleteCartFailure(superresult, error);
                }

            }
        },cart_item_ids);
    }

}
