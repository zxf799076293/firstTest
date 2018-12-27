package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.CaseInfoModel;
import com.linhuiba.business.model.CaseListModel;
import com.linhuiba.business.model.CaseThemeModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/5.
 */

public interface CaseMvpView extends BaseView {
    void onCaseListSuccess(ArrayList<CaseListModel> caseListModels);
    void onCaseListFailure(boolean superresult, Throwable error);
    void onCaseListMoreSuccess(ArrayList<CaseListModel> caseListModels);
    void onCaseListMoreFailure(boolean superresult, Throwable error);
    void onCaseInfoSuccess(CaseInfoModel caseInfoModel);
    void onCaseInfoFailure(boolean superresult, Throwable error);
    void onCaseSelectionFailure(boolean superresult, Throwable error);
    void onCaseSelectionSuccess(CaseThemeModel caseThemeModel);
}
