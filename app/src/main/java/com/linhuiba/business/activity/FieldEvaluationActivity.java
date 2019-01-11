package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldEvaluationAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.CommentScoreModel;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvppresenter.PublishReviewMvpPresenter;
import com.linhuiba.business.mvpview.PublishReviewMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuipublic.config.LoginManager;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FieldEvaluationActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.FieldreviewCall,
        PublishReviewMvpView{
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout ReviewListswipList;
    @InjectView(R.id.order_list)
    LoadMoreListView ReviewListloadmoreList;
    @InjectView(R.id.lay_no_review)
    RelativeLayout mlay_no_review;
    private FieldEvaluationAdapter mFieldEvaluationAdapter;
    private ArrayList<ReviewModel> mReviewList = new ArrayList<ReviewModel>();
    private String fieldid;
    private int reviewlistpagesize;
    private ArrayList<String> pathtmp = new ArrayList<>();
    private int mImageList_size;
    private int newPosition;
    private Dialog mZoomPictureDialog;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private boolean mIsRefreshZoomImageview = true;
    private boolean isSellRes;
    private PublishReviewMvpPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldevaluation);
        ButterKnife.inject(this);
        initview();
        initdata();
    }
    private void initview() {
        mPresenter = new PublishReviewMvpPresenter();
        mPresenter.attachView(this);
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.review_title_first_str));
        Intent fieldinfoIntent = getIntent();
        fieldid = fieldinfoIntent.getStringExtra("fieldid");
        if (fieldinfoIntent.getExtras().get("is_sell_res") != null &&
                fieldinfoIntent.getExtras().getInt("is_sell_res") == 1) {
            isSellRes = true;
        }
        ReviewListswipList.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        ReviewListloadmoreList.setLoadMoreListen(this);
        ReviewListswipList.setOnRefreshListener(this);
        mlay_no_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdata();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initdata() {
        ReviewListloadmoreList.set_refresh();
        if (LoginManager.isLogin()) {
            reviewlistpagesize = 1;
            showProgressDialog(this.getResources().getString(R.string.txt_waiting));
            mPresenter.getComments(fieldid, reviewlistpagesize, 10,isSellRes);
        } else {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            FieldEvaluationActivity.this.finish();
        }

    }
    @Override
    public void loadMore() {
        if (LoginManager.isLogin()) {
            if (mReviewList != null) {
                if (mReviewList.size() != 0) {
                    reviewlistpagesize = reviewlistpagesize + 1;
                    mPresenter.getComments(fieldid, reviewlistpagesize, 10,isSellRes);
                } else {
                    ReviewListloadmoreList.onLoadComplete();
                }
            } else {
                ReviewListloadmoreList.onLoadComplete();
            }
        } else {
            ReviewListloadmoreList.onLoadComplete();
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            FieldEvaluationActivity.this.finish();
        }
    }
    @Override
    public void onRefresh() {
        if (LoginManager.isLogin()) {
            if (mReviewList != null) {
                mReviewList.clear();
            }
            mFieldEvaluationAdapter = new FieldEvaluationAdapter(FieldEvaluationActivity.this,mReviewList,this);
            ReviewListloadmoreList.setAdapter(mFieldEvaluationAdapter);
            initdata();
        } else {
            if(ReviewListswipList.isShown()){
                ReviewListswipList.setRefreshing(false);
            }
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            FieldEvaluationActivity.this.finish();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void fieldshowreviewpicture(ArrayList<String> path, int position) {
        if (pathtmp != null) {
            pathtmp.clear();
        }
        pathtmp.addAll(path);
        showPreviewZoom(position);
    }
    public void showpreviewpicture(ArrayList<String> path,int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(path,position);
        test.showreview(this);
    }
    private void showPreviewZoom(int position) {
        mZoomPictureDialog = new AlertDialog.Builder(this).create();
        View mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        final TextView mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        TextView mShowPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        ViewPager mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);
        com.linhuiba.linhuifield.connector.Constants mConstants = new com.linhuiba.linhuifield.connector.Constants(FieldEvaluationActivity.this,FieldEvaluationActivity.this,newPosition,mImageList_size,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mZoomPictureDialog,mImageViewList,pathtmp,mIsRefreshZoomImageview,
                position,false);
        mZoomViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                newPosition = position % mImageViewList.size();
            }

            @Override
            public void onPageSelected(int position) {
                mShowPictureSizeTV.setText(String.valueOf(position % mImageViewList.size()+1)+"/" + String.valueOf(mImageViewList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onReviewInfoSuccess(ReviewFieldInfoModel mReviewFieldInfoModel) {

    }

    @Override
    public void onReviewSuccess() {

    }

    @Override
    public void onReviewFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onResReviewSuccess(ArrayList<ReviewModel> list,String detailScore) {
        mReviewList = list;
        if (mReviewList == null || mReviewList.isEmpty()) {
            mlay_no_review.setVisibility(View.VISIBLE);
            if(ReviewListswipList.isShown()){
                ReviewListswipList.setRefreshing(false);
            }
            return;
        }
        if (detailScore != null && detailScore.length() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(detailScore);
            if (jsonObject.get("detailScore") != null &&
                    jsonObject.get("detailScore").toString().length() > 0) {
                CommentScoreModel commentScoreModel = com.alibaba.fastjson.JSONObject.parseObject(detailScore,CommentScoreModel.class);
                mReviewList.get(0).setDetailScore(commentScoreModel);
            } else {
                mReviewList.get(0).setDetailScore(new CommentScoreModel());
            }
            if (jsonObject.get("total") != null &&
                    jsonObject.get("total").toString().length() > 0) {
                TitleBarUtils.setTitleText(FieldEvaluationActivity.this,getResources().getString(R.string.review_title_first_str) +"(" +
                        jsonObject.get("total").toString() +
                        getResources().getString(R.string.review_people_count_unit_text)+")");

            }
        }
        mlay_no_review.setVisibility(View.GONE);
        mFieldEvaluationAdapter = new FieldEvaluationAdapter(FieldEvaluationActivity.this,mReviewList,FieldEvaluationActivity.this);
        ReviewListloadmoreList.setAdapter(mFieldEvaluationAdapter);
        if (mReviewList.size() <10) {
            ReviewListloadmoreList.set_loaded();
        }
        if(ReviewListswipList.isShown()){
            ReviewListswipList.setRefreshing(false);
        }
    }

    @Override
    public void onResReviewFailure(boolean superresult, Throwable error) {
        if(ReviewListswipList.isShown()){
            ReviewListswipList.setRefreshing(false);
        }
        if (!superresult) {
            MessageUtils.showToast(getContext(), error.getMessage());
        }
        if (error != null && error instanceof Response.LinhuiResponseException) {
            if (((Response.LinhuiResponseException) error).code == -99) {
                LoginManager.getInstance().clearLoginInfo();
                LoginActivity.BaesActivityreloadLogin(FieldEvaluationActivity.this);
                FieldEvaluationActivity.this.finish();
            }
        }
    }

    @Override
    public void onResReviewMoreSuccess(ArrayList<ReviewModel> list,String detailScore) {
        ArrayList<ReviewModel> tmp = list;
        if( (tmp == null || tmp.isEmpty())){
            reviewlistpagesize = reviewlistpagesize-1;
            ReviewListloadmoreList.onLoadComplete();
            ReviewListloadmoreList.set_loaded();
            return;
        }
        for( ReviewModel order: tmp ){
            mReviewList.add(order);
        }
        mFieldEvaluationAdapter.notifyDataSetChanged();
        ReviewListloadmoreList.onLoadComplete();
        if (tmp.size() < 10 ) {
            ReviewListloadmoreList.set_loaded();
        }
    }

    @Override
    public void onResReviewMoreFailure(boolean superresult, Throwable error) {
        ReviewListloadmoreList.onLoadComplete();
        reviewlistpagesize = reviewlistpagesize-1;
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
        if (error != null && error instanceof Response.LinhuiResponseException) {
            if (((Response.LinhuiResponseException) error).code == -99) {
                LoginManager.getInstance().clearLoginInfo();
                LoginActivity.BaesActivityreloadLogin(FieldEvaluationActivity.this);
                FieldEvaluationActivity.this.finish();
            }
        }
    }
}
