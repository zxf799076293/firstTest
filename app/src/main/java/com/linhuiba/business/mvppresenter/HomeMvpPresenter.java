package com.linhuiba.business.mvppresenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BasePresenter;
import com.linhuiba.business.model.ArticleslistModel;
import com.linhuiba.business.model.HomeDynamicCommunityModel;
import com.linhuiba.business.model.HomeHoverModel;
import com.linhuiba.business.model.HomeMessageModel;
import com.linhuiba.business.model.HomeNoticesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.mvpmodel.CaseMvpModel;
import com.linhuiba.business.mvpmodel.HomeMvpModel;
import com.linhuiba.business.mvpview.HomeMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HomeMvpPresenter extends BasePresenter<HomeMvpView> {
    public void getNavigationBar(int plate) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getNavigationBar(plate, new LinhuiAsyncHttpResponseHandler(HomeDynamicCommunityModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onHomeCommunitySuccess((ArrayList<HomeDynamicCommunityModel>)data);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.field_tupesize_errortoast));
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()) {
                    getView().hideLoading();
                    getView().onHomeFailure(superresult, error);
                }
            }
        });
    }
    public void getCaseList(final String article_type_id, String pagesize) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getCooperationCaseData(article_type_id,pagesize, new LinhuiAsyncHttpResponseHandler(ArticleslistModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        if (article_type_id.equals("17")) {
                            getView().onHomeHeadlinesSuccess((ArrayList<ArticleslistModel>)data);
                        }

                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onHomeFailure(superresult, error);
                }
            }
        });
    }
    public void getServiceProviderList(String pageSize,String page,
                                       String city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getServiceProviderData(pageSize,page,city_id, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject != null && jsonObject.get("data") != null &&
                                jsonObject.get("data").toString().length() > 0) {
                            ArrayList<ResourceSearchItemModel> list = (ArrayList<ResourceSearchItemModel>) JSONArray.parseArray(jsonObject.get("data").toString(),ResourceSearchItemModel.class);
                            getView().onServiceProviderSuccess(list);
                        }
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().hideLoading();
                    getView().onHomeFailure(superresult, error);
                }
            }
        });
    }
    public void getNotices() {
        if (!isViewAttached()) {
            return;
        }
        HomeMvpModel.getNotices(new LinhuiAsyncHttpResponseHandler(HomeNoticesModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    ArrayList<HomeNoticesModel> homeNoticesModellist = (ArrayList<HomeNoticesModel>) data;
                    getView().onNoticesSuccess(homeNoticesModellist);
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isViewAttached()){
                    getView().onNoticesFailure(superresult, error);
                }
            }
        });
    }
    public void getBanners(String city_id) {
        if (!isViewAttached()) {
            return;
        }
        HomeMvpModel.getBanners(city_id, new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null && data.toString().length() > 0 && JSONArray.parseArray(data.toString()) != null &&
                            JSONArray.parseArray(data.toString()).size() > 0) {
                        com.alibaba.fastjson.JSONArray bannerarray = JSONArray.parseArray(data.toString());
                        getView().onBannerSuccess(bannerarray);
                    }
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void getmessage() {
        if (!isViewAttached()) {
            return;
        }
        HomeMvpModel.getHomeMessages(new LinhuiAsyncHttpResponseHandler(HomeMessageModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    ArrayList<HomeMessageModel> homeMessageModels = (ArrayList<HomeMessageModel>) data;
                    getView().onMessageSuccess(homeMessageModels);
                }
            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void deleteMessageNotices(String id) {
        if (!isViewAttached()) {
            return;
        }
        HomeMvpModel.deleteMessageNotices(id, new LinhuiAsyncHttpResponseHandler(HomeMessageModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {

            }

            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void getHomeCaseList(int is_home,  ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                ArrayList<Integer> city_ids,
                                ArrayList<Integer> label_ids, final int page,int city_id) {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        CaseMvpModel.getCaseList(is_home, community_type_ids, industry_ids,spread_way_ids,promotion_purpose_ids,city_ids, label_ids,page,city_id,
                new LinhuiAsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                        if(isViewAttached()) {
                            getView().hideLoading();
                            if (data != null) {
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                if (jsonObject.get("data") != null) {
                                    ArrayList<ArticleslistModel> list = (ArrayList<ArticleslistModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                                            ArticleslistModel.class);
                                    getView().onHomeCaseSuccess(list);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(isViewAttached()){
                            getView().hideLoading();
                            getView().onHomeFailure(superresult, error);
                        }
                    }
                });
    }
    public void getCategoriesBarList() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getCategoriesBarList(new LinhuiAsyncHttpResponseHandler(HomeDynamicCommunityModel.class,true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onNavigationsSuccess((ArrayList<HomeDynamicCommunityModel>)data);
                    } else {
                        getView().showToast(getView().getContext().getResources().getString(R.string.review_error_text));
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void getLinhuiData() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getHomeLinhuiData(new LinhuiAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    if (data != null) {
                        getView().onLinhuiDataSuccess(data);
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public void getAppHoverWindow() {
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        HomeMvpModel.getAppHoverWindow(new LinhuiAsyncHttpResponseHandler(HomeHoverModel.class,false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
                if(isViewAttached()){
                    getView().hideLoading();
                    HomeHoverModel homeHoverModel = (HomeHoverModel) data;
                    if (homeHoverModel != null) {
                        getView().onHoverWindowSuccess(homeHoverModel);
                    }
                }
            }
            @Override
            public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
