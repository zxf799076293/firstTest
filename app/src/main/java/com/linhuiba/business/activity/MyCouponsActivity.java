package com.linhuiba.business.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyCouponsActivity extends BaseMvpActivity implements CouponsMvpView,
        SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.my_coupons_tablayout)
    TabLayout mCouponsTL;
    @InjectView(R.id.my_coupons_vp)
    ViewPager mCouponsVP;
    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipeRL= new SwipeRefreshLayout[3];
    private RecyclerView[] mRecyclerViews = new RecyclerView[3];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[3];
    private LinearLayout[] mNoDataLL = new LinearLayout[3];
    private int[] mPage = new int[3];
    private List<MyCouponsModel>[] mLists = new ArrayList[3];
    public int mCurrIndex = 0;// 当前页卡编号
    private int mTabTextViewList[] = {R.string.module_my_coupons_unused, R.string.module_my_coupons_used,
            R.string.module_my_coupons_expired};
    private MyCouponsAdapter[] mCouponsAdapters = new MyCouponsAdapter[3];
    private CouponsMvpPresenter mCouponsMvpPresenter;
    private int expired;
    private int used;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_my_coupons);
        initView();
        showProgressDialog();
        mPage[mCurrIndex] = 1;
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCouponsMvpPresenter != null) {
            mCouponsMvpPresenter.detachView();
        }
    }

    private void initView() {
        ButterKnife.inject(this);
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_my_coupons_title));
        TitleBarUtils.showBackImg(this,true);
        Drawable drawable = getResources().getDrawable(R.drawable._three_sevenic_youhuiquan_jieshi);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        TitleBarUtils.showRegisterText(this, getResources().getString(R.string.module_coupons_first_register_activity_regulation),
                12,
                getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor),
                drawable,
                new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent  couponsIntent = new Intent(MyCouponsActivity.this,AboutUsActivity.class);
                        couponsIntent.putExtra("type", com.linhuiba.business.config.Config.COUPON_REGULATION_WEB_INT);
                        startActivity(couponsIntent);
                    }
                });
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        for(int i=0 ;i< mListViews.size(); i++ ) {
            mSwipeRL[i] = (SwipeRefreshLayout)mListViews.get(i). findViewById(R.id.app_swipe_refresh);
            mSwipeRL[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mRecyclerViews[i]  = (RecyclerView) mListViews.get(i).findViewById(R.id.app_recycler_view);
            mSwipeRL[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mSwipeRL[i].setOnRefreshListener(this);
            mNullDataRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.app_no_data_ll);
            mNoDataLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.my_coupons_no_data_ll);
            mNoDataLL[i].setVisibility(View.VISIBLE);
            if (i == 0) {
                TextView noDataGotoTV = (TextView) mListViews.get(i).findViewById(R.id.my_msg_no_data_goto_tv);
                noDataGotoTV.setVisibility(View.VISIBLE);
                noDataGotoTV.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent centreIntent = new Intent(MyCouponsActivity.this, CouponReceiveCentreActivity.class);
                        startActivity(centreIntent);
                    }
                });
            }
            mNullDataRL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage[mCurrIndex] = 1;
                    mCouponsAdapters[mCurrIndex] = null;
                    initData();
                }
            });
        }
        mCouponsVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        mCouponsVP.setOnPageChangeListener(new PagerChangeListener());
        mCouponsTL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mCouponsTL,15,15);
            }
        });
        mCouponsTL.setupWithViewPager(mCouponsVP);
        for (int i = 0; i < mCouponsTL.getTabCount(); i++) {
            mCouponsTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }
    }
    private void initData() {
        if (mCouponsAdapters[mCurrIndex] == null) {
            expired = 1;
            used = 1;
            if (mCurrIndex == 0) {
                used = 1;
                expired = 1;
            } else if (mCurrIndex == 1) {
                used = 2;
                expired = 1;
            } else if (mCurrIndex == 2) {
                expired = 2;
                used = 1;
            }
            mCouponsMvpPresenter.getCouponsList(expired,used,mPage[mCurrIndex]);
        } else {
            if (mSwipeRL[mCurrIndex].isShown()) {
                mSwipeRL[mCurrIndex].setRefreshing(false);
            }
        }
    }
    @Override
    public void onRefresh() {
        mPage[mCurrIndex] = 1;
        mCouponsAdapters[mCurrIndex] = null;
        initData();
    }

    @Override
    public void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (mLists[mCurrIndex] != null) {
            mLists[mCurrIndex].clear();
        }
        if (mCouponsAdapters[mCurrIndex] == null) {
            mLists[mCurrIndex] = data;
        } else {
            mLists[mCurrIndex].addAll(data);
        }
        if (mLists[mCurrIndex] != null &&
                mLists[mCurrIndex].size() > 0) {
            mNullDataRL[mCurrIndex].setVisibility(View.GONE);
            if (mCouponsAdapters[mCurrIndex] == null) {
                mCouponsAdapters[mCurrIndex] = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,mLists[mCurrIndex],MyCouponsActivity.this,
                        1,expired,used,MyCouponsActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerViews[mCurrIndex].setLayoutManager(linearLayoutManager);
                mRecyclerViews[mCurrIndex].setAdapter(mCouponsAdapters[mCurrIndex]);
                mCouponsAdapters[mCurrIndex].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mCouponsAdapters[mCurrIndex].setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        mPage[mCurrIndex] = mPage[mCurrIndex] + 1;
                        expired = 1;
                        used = 1;
                        if (mCurrIndex == 0) {
                            used = 1;
                            expired = 1;
                        } else if (mCurrIndex == 1) {
                            used = 2;
                            expired = 1;
                        } else if (mCurrIndex == 2) {
                            expired = 2;
                            used = 1;
                        }
                        mCouponsMvpPresenter.getCouponsList(expired,used,mPage[mCurrIndex]);
                    }
                });
            } else {
                mCouponsAdapters[mCurrIndex].notifyDataSetChanged();
            }
            mCouponsAdapters[mCurrIndex].loadMoreComplete();
            if (mLists[mCurrIndex].size() < 10) {
                mCouponsAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mNullDataRL[mCurrIndex].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMyCouponsMoreSuccess(ArrayList<MyCouponsModel> data) {
        List<MyCouponsModel> mList = new ArrayList<>();
        mList = data;
        if (mList != null && mList.size() > 0) {
            for (int i = 1; i < mList.size(); i++) {
                mLists[mCurrIndex].add(mList.get(i));
            }
            mCouponsAdapters[mCurrIndex].notifyDataSetChanged();
            mCouponsAdapters[mCurrIndex].loadMoreComplete();
            if (mList.size() < 10) {
                mCouponsAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mCouponsAdapters[mCurrIndex].loadMoreEnd();
        }
    }

    @Override
    public void onMyCouponsListFailure(boolean superresult, Throwable error) {
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onMyCouponsListMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mCouponsAdapters[mCurrIndex].loadMoreFail();
        mPage[mCurrIndex] --;
    }

    @Override
    public void onExchangeCouponsSuccess(Response response) {

    }

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrIndex = position;
            switch (position) {
                case 0:
                    if (mCouponsAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                        mPage[mCurrIndex] = 1;
                        initData();
                    }
                    break;
                case 1:
                    if (mCouponsAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                        mPage[mCurrIndex] = 1;
                        initData();
                    }

                    break;
                case 2:
                    if (mCouponsAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                        mPage[mCurrIndex] = 1;
                        initData();
                    }
                    break;
                case 3:
                    if (mCouponsAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                        mPage[mCurrIndex] = 1;
                        initData();
                    }
                    break;
                default:
                    break;
            }

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    @OnClick({
            R.id.my_coupons_goto_centre_ll
    })
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.my_coupons_goto_centre_ll:
                Intent centreIntent = new Intent(MyCouponsActivity.this, CouponReceiveCentreActivity.class);
                startActivity(centreIntent);
                break;
            default:
                break;
        }
    }
}
