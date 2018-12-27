package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.MapCommunityInfoModel;
import com.linhuiba.business.model.MapCommunitySearchListModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.network.Response;

import java.util.ArrayList;

public interface CommunityMapMvpView extends BaseView {
    void onOtherPhyResSuccess(ArrayList<MapCommunityInfoModel> list);
    void onOtherPhyResFailure(boolean superresult, Throwable error);
    void onMapSearchSuccess(MapCommunitySearchListModel list,Response response);
    void onMapSearchFailure(boolean superresult, Throwable error);
    void onAttributesSuccess(ArrayList<SearchListAttributesModel> list);
    void onAttributesFailure(boolean superresult, Throwable error);
}

