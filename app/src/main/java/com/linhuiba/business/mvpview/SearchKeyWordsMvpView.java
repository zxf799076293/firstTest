package com.linhuiba.business.mvpview;


import com.linhuiba.business.basemvp.BaseView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface SearchKeyWordsMvpView extends BaseView {
    void onSearchWordsSuccess(ArrayList<String> list);
    void onSearchWordsFailure(boolean superresult, Throwable error);

}
