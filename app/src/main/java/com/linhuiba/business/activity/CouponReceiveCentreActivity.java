package com.linhuiba.business.activity;

import android.Manifest;
import android.content.Intent;
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
import com.linhuiba.business.connector.CalendarReminderUtils;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnCouponTimerTask;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CouponReceiveCentreActivity extends BaseMvpActivity implements CouponsMvpView,
        SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.coupon_centre_tablayout)
    TabLayout mCouponsTL;
    @InjectView(R.id.coupon_centre_vp)
    ViewPager mCouponsVP;
    private ArrayList<View> mListViews;
    private int mViewSize = 4;
    private SwipeRefreshLayout[] mSwipeRL= new SwipeRefreshLayout[mViewSize];
    private RecyclerView[] mRecyclerViews = new RecyclerView[mViewSize];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[mViewSize];
    private LinearLayout[] mNoDataLL = new LinearLayout[mViewSize];
    private int[] mPage = new int[mViewSize];
    private List<MyCouponsModel>[] mLists = new ArrayList[mViewSize];
    public int mCurrIndex = 0;// 当前页卡编号
    private int mTabTextViewList[] = {R.string.enquary_all_orders, R.string.myselfinfo_waiting,
            R.string.module_coupon_centre_be_about_to_finish,R.string.module_coupon_centre_be_about_to_start};
    private MyCouponsAdapter[] mCouponsAdapters = new MyCouponsAdapter[mViewSize];
    public CouponsMvpPresenter mCouponsMvpPresenter;
    private Timer[] mTimers = new Timer[2];
    // FIXME: 2018/12/7 日历提醒
    private static final int CALENDAR_REQUEST_INT = 11;//权限 日历选项
    private String mCouponRemindTitleStr = "";
    private String mCouponRemindTime = "";
    private int mClickCouponId;
    private SimpleDateFormat mSimpleDateFormat;
    private int LOGIN_INTENT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_coupon_receive_centre);
        initView();
        showProgressDialog();
        mPage[mCurrIndex] = 1;
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrIndex == 0 || mCurrIndex == 3) {
            if (mCouponsAdapters[mCurrIndex] != null) {
                mCouponsAdapters[mCurrIndex].getCouponRemindMap();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCouponsMvpPresenter != null) {
            mCouponsMvpPresenter.detachView();
        }
        if (mTimers[0] != null) {
            mTimers[0].cancel();
        }
        if (mTimers[1] != null) {
            mTimers[1].cancel();
        }
    }

    private void initView() {
        ButterKnife.inject(this);
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        TitleBarUtils.setTitleImg(this,R.drawable.ic_linquanzhongxin_three_nine);
        TitleBarUtils.showBackImg(this,true);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        for(int i = 0 ;i < mListViews.size(); i++ ) {
            mSwipeRL[i] = (SwipeRefreshLayout)mListViews.get(i). findViewById(R.id.app_swipe_refresh);
            mSwipeRL[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mRecyclerViews[i]  = (RecyclerView) mListViews.get(i).findViewById(R.id.app_recycler_view);
            ((SimpleItemAnimator)mRecyclerViews[i].getItemAnimator()).setSupportsChangeAnimations(false);
            mSwipeRL[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mSwipeRL[i].setOnRefreshListener(this);
            mNullDataRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.app_no_data_ll);
            TextView textView = (TextView) mListViews.get(i).findViewById(R.id.my_msg_no_data_tv);
            textView.setText(getResources().getString(R.string.module_coupon_centre_no_data));
            mNoDataLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.my_coupons_no_data_ll);
            mNoDataLL[i].setVisibility(View.VISIBLE);
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
//        mCouponsTL.post(new Runnable() {
//            @Override
//            public void run() {
//                Constants.setIndicator(mCouponsTL,15,15);
//            }
//        });
        mCouponsTL.setupWithViewPager(mCouponsVP);
        for (int i = 0; i < mCouponsTL.getTabCount(); i++) {
            mCouponsTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }
    }
    private void initData() {
        if (mCouponsAdapters[mCurrIndex] == null) {
            mCouponsMvpPresenter.getCouponCentreList(mCurrIndex,mPage[mCurrIndex]);
        } else {
            if (mSwipeRL[mCurrIndex].isShown()) {
                mSwipeRL[mCurrIndex].setRefreshing(false);
            }
        }
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
                case 4:
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

    @Override
    public void onRefresh() {
        mPage[mCurrIndex] = 1;
        mCouponsAdapters[mCurrIndex] = null;
        if (mCurrIndex == 0 || mCurrIndex == 3) {
            int timeSize;
            if (mCurrIndex == 0) {
                timeSize = 0;
            } else {
                timeSize = 1;
            }
            if (mTimers[timeSize] != null) {
                mTimers[timeSize].cancel();
                mTimers[timeSize] = null;
            }
        }
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
        // FIXME: 2018/12/12 测试数据要删除
//        mLists[mCurrIndex].get(0).setStatus(3);
//        Calendar calendat = Calendar.getInstance();
//        calendat.add(Calendar.MINUTE, 5);
//        mLists[mCurrIndex].get(0).setShelf_time(mSimpleDateFormat.format(calendat.getTime()));
//        mLists[mCurrIndex].get(7).setStatus(3);
//        calendat.add(Calendar.MINUTE, 5);
//        mLists[mCurrIndex].get(7).setShelf_time(mSimpleDateFormat.format(calendat.getTime()));

        if (mLists[mCurrIndex] != null &&
                mLists[mCurrIndex].size() > 0) {
            // FIXME: 2018/12/18 测试数据要删除
//            if (mCurrIndex == 0 || mCurrIndex == 3) {
//                for (int i = 0; i < mLists[mCurrIndex].size(); i++) {
//                    if (mLists[mCurrIndex].get(i).getStatus() == 3 &&
//                            mLists[mCurrIndex].get(i).getShelf_time() != null &&
//                            mLists[mCurrIndex].get(i).getShelf_time().length() > 0) {
//                        Calendar cal = Calendar.getInstance();
//                        try {
//                            cal.setTime(mSimpleDateFormat.parse(mLists[mCurrIndex].get(i).getShelf_time()));
//                            mLists[mCurrIndex].get(i).setRemind_time((cal.getTimeInMillis() - System.currentTimeMillis()));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
            mNullDataRL[mCurrIndex].setVisibility(View.GONE);
            if (mCouponsAdapters[mCurrIndex] == null) {
                mCouponsAdapters[mCurrIndex] = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,mLists[mCurrIndex],CouponReceiveCentreActivity.this,
                        CouponReceiveCentreActivity.this,5);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerViews[mCurrIndex].setLayoutManager(linearLayoutManager);
                mRecyclerViews[mCurrIndex].setAdapter(mCouponsAdapters[mCurrIndex]);
                mCouponsAdapters[mCurrIndex].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
//                mCouponsAdapters[mCurrIndex].setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//                    @Override
//                    public void onLoadMoreRequested() {
//                        mPage[mCurrIndex] = mPage[mCurrIndex] + 1;
//                        mCouponsMvpPresenter.getCouponCentreList(mCurrIndex,mPage[mCurrIndex]);
//                    }
//                });
            } else {
                mCouponsAdapters[mCurrIndex].notifyDataSetChanged();
            }
            mCouponsAdapters[mCurrIndex].loadMoreEnd();
            // FIXME: 2018/12/12 倒计时
            if (mCurrIndex == 0 || mCurrIndex == 3) {
                int timeSize;
                if (mCurrIndex == 0) {
                    timeSize = 0;
                } else {
                    timeSize = 1;
                }
                if (mTimers[timeSize] == null) {
                    mTimers[timeSize] = new Timer();
                }
                // FIXME: 2018/12/24 测试数据
                mLists[mCurrIndex].get(0).setRemind_time(180);
                mTimers[timeSize].schedule(new OnCouponTimerTask(mLists[mCurrIndex],mCouponsAdapters[mCurrIndex],
                        CouponReceiveCentreActivity.this,0),0,1000);
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
            int startNum = mLists[mCurrIndex].size();
            for (int i = 1; i < mList.size(); i++) {
                mLists[mCurrIndex].add(mList.get(i));
            }
            mCouponsAdapters[mCurrIndex].notifyDataSetChanged();
            // FIXME: 2018/12/12 倒计时
            if (mCurrIndex == 0 || mCurrIndex == 3) {
                int timeSize;
                if (mCurrIndex == 0) {
                    timeSize = 0;
                } else {
                    timeSize = 1;
                }
                mTimers[timeSize].schedule(new OnCouponTimerTask(mList,mCouponsAdapters[mCurrIndex],
                        CouponReceiveCentreActivity.this,startNum),0,1000);
            }
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
        if (response.code == 1) {
            MessageUtils.showToast(getResources().getString(R.string.module_coupon_centre_receive_success));
            mPage[mCurrIndex] = 1;
            mCouponsAdapters[mCurrIndex] = null;
            initData();
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            // FIXME: 2018/12/7 日历提醒
            if (requestCode == CALENDAR_REQUEST_INT) {
                if (mCouponRemindTitleStr.length() > 0 &&
                mCouponRemindTime.length() > 0)
                setRemind(mCouponRemindTitleStr,mCouponRemindTime);
                mCouponsAdapters[mCurrIndex].mCouponRemindMap.put(mClickCouponId,1);
                mCouponsAdapters[mCurrIndex].setCouponRemindMap();
                MessageUtils.showToast(getResources().getString(R.string.module_coupon_centre_receive_remind));
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            mCouponRemindTitleStr = "";
            mCouponRemindTime = "";
        }
    };
    public void setRemindClick(int couponId, String title, String reminderTime) {
        // FIXME: 2018/12/7 日历功能
        mCouponRemindTitleStr = title;
        mCouponRemindTime = reminderTime;
        mClickCouponId = couponId;
        AndPermission.with(CouponReceiveCentreActivity.this)
                .requestCode(CALENDAR_REQUEST_INT)
                .permission(Manifest.permission.WRITE_CALENDAR,
                        Manifest.permission.READ_CALENDAR)
                .callback(listener)
                .start();
    }
    private void setRemind(String title, String reminderTime) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(mSimpleDateFormat.parse(reminderTime));
//            cal.add(Calendar.MINUTE, -2);// 24小时制 什么时候开始这个事件
            CalendarReminderUtils.addCalendarEvent(CouponReceiveCentreActivity.this, title,
                    getResources().getString(R.string.module_coupon_centre_description) +
                    getResources().getString(R.string.module_coupon_centre_mlink_key), cal.getTimeInMillis(), 0);
//        CalendarReminderUtils.addCalendarEvent(CouponReceiveCentreActivity.this, "日历提醒",
//                "点击链接领取：\n https://aj4j3o.mlinks.cc/Ad7u", cal.getTimeInMillis(), 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void cancelRemind(String title) {
        // FIXME: 2018/12/7 日历功能
        CalendarReminderUtils.deleteCalendarEvent(CouponReceiveCentreActivity.this,title);
    }
    @OnClick({
            R.id.coupons_centre_goto_my_coupons_ll
    })
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.coupons_centre_goto_my_coupons_ll:
                if (LoginManager.isLogin()) {
                    if (Constants.isFastClick()) {
                        Intent couponsIntent = new Intent(CouponReceiveCentreActivity.this, MyCouponsActivity.class);
                        startActivity(couponsIntent);
                    }
                } else {
                    Intent demand_intent = new Intent(CouponReceiveCentreActivity.this, LoginActivity.class);
                    startActivityForResult(demand_intent, LOGIN_INTENT_CODE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_INTENT_CODE) {
            if (LoginManager.isLogin()) {
                Intent couponsIntent = new Intent(CouponReceiveCentreActivity.this, MyCouponsActivity.class);
                startActivity(couponsIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
