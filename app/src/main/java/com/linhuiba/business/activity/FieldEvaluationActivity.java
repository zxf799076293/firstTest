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
import com.linhuiba.business.model.ReviewModel;
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
public class FieldEvaluationActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.FieldreviewCall{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldevaluation);
        ButterKnife.inject(this);
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.review_title_first_str));
        Intent fieldinfoIntent = getIntent();
        fieldid = fieldinfoIntent.getStringExtra("fieldid");
        initview();
        initdata();
    }
    private void initview() {
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
    private void initdata() {
        ReviewListloadmoreList.set_refresh();
        if (LoginManager.isLogin()) {
            reviewlistpagesize = 1;
            showProgressDialog(this.getResources().getString(R.string.txt_waiting));
            FieldApi.get_resources_commentslist(MyAsyncHttpClient.MyAsyncHttpClient3(), ReviewHandler, fieldid, String.valueOf(reviewlistpagesize), "10");
        } else {
            LoginManager.getInstance().clearLoginInfo();
            LoginActivity.BaesActivityreloadLogin(this);
            FieldEvaluationActivity.this.finish();
        }

    }
    private LinhuiAsyncHttpResponseHandler ReviewHandler = new LinhuiAsyncHttpResponseHandler(ReviewModel.class,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mReviewList = (ArrayList<ReviewModel>) data;
            if (mReviewList == null || mReviewList.isEmpty()) {
                mlay_no_review.setVisibility(View.VISIBLE);
                if(ReviewListswipList.isShown()){
                    ReviewListswipList.setRefreshing(false);
                }
                return;
            }
            if (response.detailScore != null && response.detailScore.length() > 0) {
                CommentScoreModel commentScoreModel = com.alibaba.fastjson.JSONObject.parseObject(response.detailScore,CommentScoreModel.class);
                if (commentScoreModel != null) {
                    mReviewList.get(0).setDetailScore(commentScoreModel);
                    if (mReviewList.get(0).getDetailScore().getCount_of_composite_score() != null) {
                        TitleBarUtils.setTitleText(FieldEvaluationActivity.this,getResources().getString(R.string.review_title_first_str) +"(" +
                                String.valueOf(mReviewList.get(0).getDetailScore().getCount_of_composite_score()) +
                                getResources().getString(R.string.review_people_count_unit_text)+")");
                    }
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
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
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
    };
    private LinhuiAsyncHttpResponseHandler ReviewMoreHandler = new LinhuiAsyncHttpResponseHandler(ReviewModel.class,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            ArrayList<ReviewModel> tmp = (ArrayList<ReviewModel>) data;
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
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
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
    };
    @Override
    public void loadMore() {
        if (LoginManager.isLogin()) {
            if (mReviewList != null) {
                if (mReviewList.size() != 0) {
                    reviewlistpagesize = reviewlistpagesize + 1;
                    FieldApi.get_resources_commentslist(MyAsyncHttpClient.MyAsyncHttpClient3(), ReviewMoreHandler, fieldid, String.valueOf(reviewlistpagesize), "10");
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
}
