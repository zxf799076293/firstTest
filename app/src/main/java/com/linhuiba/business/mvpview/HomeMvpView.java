package com.linhuiba.business.mvpview;

import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.ArticleslistModel;
import com.linhuiba.business.model.HomeDynamicCommunityModel;
import com.linhuiba.business.model.HomeHoverModel;
import com.linhuiba.business.model.HomeMessageModel;
import com.linhuiba.business.model.HomeNoticesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/27.
 */

public interface HomeMvpView extends BaseView {
    void onHomeFailure(boolean superresult, Throwable error);
    void onHomeCommunitySuccess(ArrayList<HomeDynamicCommunityModel> list);
    void onHomeCaseSuccess(ArrayList<ArticleslistModel> list);
    void onHomeHeadlinesSuccess(ArrayList<ArticleslistModel> list);
    void onServiceProviderSuccess(ArrayList<ResourceSearchItemModel> list);
    void onNoticesSuccess(ArrayList<HomeNoticesModel> list);
    void onNoticesFailure(boolean superresult, Throwable error);
    void onBannerSuccess(JSONArray jsonArray);
    void onMessageSuccess(ArrayList<HomeMessageModel> list);
    void onNavigationsSuccess(ArrayList<HomeDynamicCommunityModel> list);
    void onLinhuiDataSuccess(Object data);
    void onHoverWindowSuccess(HomeHoverModel data);
}
