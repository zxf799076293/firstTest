package com.linhuiba.business.mvpview;

import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.basemvp.BaseView;

/**
 * Created by Administrator on 2018/4/10.
 */

public interface SearchCityMvpView extends BaseView {
    void onHotCitySuccess(JSONArray jsonArray);
    void onHotCityFailure(boolean superresult, Throwable error);
}
