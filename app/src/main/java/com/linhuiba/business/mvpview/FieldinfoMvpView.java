package com.linhuiba.business.mvpview;

import com.linhuiba.business.basemvp.BaseView;
import com.linhuiba.business.model.CommunityInfoModel;
import com.linhuiba.business.model.FieldInfoModel;
import com.linhuiba.business.model.GroupBookingInfoModel;
import com.linhuiba.business.model.PhyResInfoModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ReviewModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface FieldinfoMvpView extends BaseView {
    void onFieldinfoFailure(boolean superresult, Throwable error);
    void onFieldinfoResSuccess(PhyResInfoModel resInfo);
    void onResReviewSuccess(ArrayList<ReviewModel> list);
    void onResReviewFailure(boolean superresult, Throwable error);
    void onGroupinfoResSuccess(GroupBookingInfoModel resInfo);
    void onNearbyResSuccess(ArrayList<ResourceSearchItemModel> list);
    void onNearbyResMoreSuccess(ArrayList<ResourceSearchItemModel> list);
    void onOtherPhyResSuccess(ArrayList<ResourceSearchItemModel> list,int csort,int total);
    void onOtherPhyResMoreSuccess(ArrayList<ResourceSearchItemModel> list);
    void onCommunityInfoSuccess(CommunityInfoModel resInfo);
    void onRecommendResSuccess(ArrayList<ResourceSearchItemModel> list);
    void onRecommendResMoreSuccess(ArrayList<ResourceSearchItemModel> list);
    void onFeedbacksSuccess();
    void onFeedbacksFailure(boolean superresult, Throwable error);
    void onEnquirySuccess(String id);
    void onEnquiryFailure(boolean superresult, Throwable error);
}
