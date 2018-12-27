package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.Version;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface VersionMvpView extends BaseView {
    void onVersionSuccess(Version versions);
    void onVersionFailure(boolean superresult, Throwable error);
}
