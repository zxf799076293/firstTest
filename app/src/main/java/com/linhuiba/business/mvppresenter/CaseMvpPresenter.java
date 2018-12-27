package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.CaseInfoModel;
import com.linhuiba.business.model.CaseListModel;
import com.linhuiba.business.model.CaseThemeModel;
import com.linhuiba.business.mvpmodel.CaseMvpModel;
import com.linhuiba.business.mvpview.CaseMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseMvpPresenter extends BasePresenter<CaseMvpView>  {
    public void getCaseListData(int is_home, ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                ArrayList<Integer> city_ids, ArrayList<Integer> label_ids,final int page,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CaseMvpModel.getCaseList(is_home, community_type_ids,industry_ids,spread_way_ids,promotion_purpose_ids,city_ids, label_ids,
                page,city_id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                            if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null) {
                                    ArrayList<CaseListModel> list = (ArrayList<CaseListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                            CaseListModel.class);
                                    if (page > 1) {
                                        getView().onCaseListMoreSuccess(list);
                                    } else {
                                        getView().onCaseListSuccess(list);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (page > 1) {
                                getView().onCaseListMoreFailure(superresult, error);
                            } else {
                                getView().onCaseListFailure(superresult, error);
                            }
                        }
                    }
                });
    }
    public void getOterCaseList(int id, final int page,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CaseMvpModel.getOterCaseList(id, page,city_id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (data != null) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null) {
                                    ArrayList<CaseListModel> list = (ArrayList<CaseListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                            CaseListModel.class);
                                    if (page > 1) {
                                        getView().onCaseListMoreSuccess(list);
                                    } else {
                                        getView().onCaseListSuccess(list);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            if (page > 1) {
                                getView().onCaseListMoreFailure(superresult, error);
                            } else {
                                getView().onCaseListFailure(superresult, error);
                            }
                        }
                    }
                });
    }
    public void getCaseInfo(int id,ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                            ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                            ArrayList<Integer> city_ids,
             ArrayList<Integer> label_ids,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CaseMvpModel.getCaseInfo(id, community_type_ids, industry_ids,spread_way_ids,promotion_purpose_ids,city_ids,label_ids,city_id,
                new LinhuiAsyncHttpResponseHandler(CaseInfoModel.class,false) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()) {
                            getView().hideLoading();
                            if (data != null) {
                                getView().onCaseInfoSuccess((CaseInfoModel)data);
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onCaseInfoFailure(superresult, error);
                        }
                    }
                });
    }
    public void getCaseSelection() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CaseMvpModel.getCaseSelection(new LinhuiAsyncHttpResponseHandler(CaseThemeModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    if (data != null) {
                        getView().onCaseSelectionSuccess((CaseThemeModel)data);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onCaseSelectionFailure(superresult, error);
                }
            }
        });
    }

}
