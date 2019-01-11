package com.linhuiba.business.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.CommentCentreAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvppresenter.PublishReviewMvpPresenter;
import com.linhuiba.business.mvpview.PublishReviewMvpView;
import com.linhuiba.business.util.TitleBarUtils;

import java.util.ArrayList;

import butterknife.InjectView;

public class CommentCentreActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener,PublishReviewMvpView {
    @InjectView(R.id.comment_centre_swiperefreshll)
    SwipeRefreshLayout mCommentSwipeRefresh;
    @InjectView(R.id.comment_centre_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.coment_centre_no_data_rl)
    RelativeLayout mCommentNoDataRL;

    private PublishReviewMvpPresenter mPresenter;
    private CommentCentreAdapter mCommentCentreAdapter;
    private ArrayList<ReviewModel> mDatas = new ArrayList<>();
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_comment_centre);
        initView();
        showProgressDialog();
        initData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
    private void initView() {
        mPresenter = new PublishReviewMvpPresenter();
        mPresenter.attachView(this);
        mCommentSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mCommentSwipeRefresh.setOnRefreshListener(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_case_title));
        TitleBarUtils.showBackImg(this,true);
    }
    private void initData() {
        mPresenter.getCommentCentre("1125",1,10);
    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();
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
    public void onResReviewSuccess(ArrayList<ReviewModel> list) {
        if (mCommentSwipeRefresh.isShown()) {
            mCommentSwipeRefresh.setRefreshing(false);
        }
        if (mDatas != null) {
            mDatas.clear();
        }
        if (mCommentCentreAdapter == null) {
            mDatas = list;
        } else {
            mDatas.addAll(list);
        }
        if (mDatas != null && mDatas.size() > 0) {
            if (mCommentCentreAdapter == null) {
                mCommentCentreAdapter = new CommentCentreAdapter(R.layout.module_recycle_item_comment_centre,mDatas,CommentCentreActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentCentreActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                //设置布局管理器
                mRecyclerView.setLayoutManager(linearLayoutManager);
                //设置为垂直布局，这也是默认的
                //设置Adapter
                mRecyclerView.setAdapter(mCommentCentreAdapter);
                mCommentCentreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (view.getId() == R.id.iv_img) {
//
//                } else if (view.getId() == R.id.tv_title) {
//
//                }
                        //滚动到第几个
//                mRecyclerView.smoothScrollToPosition(0);

                    }
                });
                mCommentCentreAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        page = page + 1;
                        initData();
                    }
                });
            } else {
                mCommentCentreAdapter.notifyDataSetChanged();
            }
            mCommentCentreAdapter.loadMoreComplete();
            if (mDatas.size() < 10) {
                mCommentCentreAdapter.loadMoreEnd();
            }
            mCommentNoDataRL.setVisibility(View.GONE);
        } else {
            mCommentNoDataRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResReviewFailure(boolean superresult, Throwable error) {
        if (mCommentSwipeRefresh.isShown()) {
            mCommentSwipeRefresh.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onResReviewMoreSuccess(ArrayList<ReviewModel> list) {
        ArrayList<ReviewModel> tmp = list;
        if (tmp != null && tmp.size() > 0) {
            for(ReviewModel fieldDetail: tmp ){
                mDatas.add(fieldDetail);
            }
            mCommentCentreAdapter.notifyDataSetChanged();
            mCommentCentreAdapter.loadMoreComplete();
            if (tmp.size() < 10) {
                mCommentCentreAdapter.loadMoreEnd();
            }
        } else {
            mCommentCentreAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onResReviewMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mCommentCentreAdapter.loadMoreFail();
        page --;
    }
}
