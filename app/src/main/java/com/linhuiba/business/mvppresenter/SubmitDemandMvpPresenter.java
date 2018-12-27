package com.linhuiba.business.mvppresenter;

import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.mvpmodel.SubmitDemandMvpModel;
import com.linhuiba.business.mvpview.SubmitDemandMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

public class SubmitDemandMvpPresenter extends BasePresenter<SubmitDemandMvpView> {
    public void appealDemand(String product,String minimum_area,String maximum_area,
                             String single_field_budget,String start, String end,
                             String name, String mobile, String community_resource_id,
                             String physical_resource_id,ArrayList<Integer> city_ids,
                             ArrayList<Integer> community_type_ids) {
        if (!isViewAttached()) {
            return;
        }
        SubmitDemandMvpModel.appealDemand(product,minimum_area, maximum_area,
                single_field_budget, start,  end,
                name, mobile, community_resource_id,
                physical_resource_id, city_ids,
                community_type_ids,new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if (isViewAttached()) {
                    if (response.code == 1) {
                        getView().onSubmitDemandSuccess();
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSubmitDemandFailure(superresult,error);
                }
            }
        });
    }

}
