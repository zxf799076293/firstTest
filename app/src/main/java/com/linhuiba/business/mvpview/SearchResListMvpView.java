package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;

import java.util.ArrayList;

public interface SearchResListMvpView extends BaseView {
    void onSearchResListSuccess(ArrayList<ResourceSearchItemModel> list,Response response);
    void onSearchResListFailure(boolean superresult, Throwable error);
    void onSearchResListMoreSuccess(ArrayList<ResourceSearchItemModel> list,Response response);
    void onSearchResListMoreFailure(boolean superresult, Throwable error);
    void onAttributesSuccess(ArrayList<SearchListAttributesModel> list);
    void onAttributesFailure(boolean superresult, Throwable error);
    void onSearchSellResListSuccess(ArrayList<SearchSellResModel> list, Response response);
    void onSearchSellResListMoreSuccess(ArrayList<SearchSellResModel> list,Response response);
    void onSearchResListCountSuccess(int count);

}
