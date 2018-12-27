package com.linhuiba.linhuifield.fieldmvppresenter;

import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldSearchResActivity;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBasePresenter;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmvpmodel.FieldAddfieldChooseResOptionsMvpModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddfieldChooseResOptionsaMvpView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class FieldAddfieldChooseResOptionsMvpPresenter extends FieldBasePresenter<FieldAddfieldChooseResOptionsaMvpView> {
    public void searchCommunities(int city_id, int district_id,int category_id,int res_type_id,String keywords,FieldAddFieldSearchResActivity activity) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddfieldChooseResOptionsMvpModel.searchCommunites(city_id,district_id,category_id,res_type_id,
                keywords,activity,
                new LinhuiAsyncHttpResponseHandler(AddfieldSearchCommunityModule.class, true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                ArrayList<AddfieldSearchCommunityModule> list = (ArrayList<AddfieldSearchCommunityModule>) data;
                                getView().onSearchCommunitySuccess(list);
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onSearchCommunityFailure(superresult,error);
                        }
                    }
                });
    }
    public void getAttributes(int category_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        FieldAddfieldChooseResOptionsMvpModel.getAttributes(category_id,
                new LinhuiAsyncHttpResponseHandler(FieldAddfieldAttributesModel.class, false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                FieldAddfieldAttributesModel list = (FieldAddfieldAttributesModel) data;
                                getView().onAttributesSuccess(list);
                            }
                        }

                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onAttributesFailure(superresult,error);
                        }
                    }
                });
    }
}
