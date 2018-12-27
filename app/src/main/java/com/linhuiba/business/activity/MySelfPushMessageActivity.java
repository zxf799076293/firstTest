package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.MyPushMsgAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyPushMsgModel;
import com.linhuiba.business.mvppresenter.MyPushMsgMvpPresenter;
import com.linhuiba.business.mvpview.MyPushMsgMvpView;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MySelfPushMessageActivity extends BaseMvpActivity implements MyPushMsgMvpView,
        SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.my_push_msg_tablayout)
    TabLayout mPushMsgTL;
    @InjectView(R.id.my_push_msg_vp)
    ViewPager mPushMsgVP;
    @InjectView(R.id.my_push_msg_bottom_ll)
    LinearLayout mPushMsgBottomLL;
    @InjectView(R.id.my_push_msg_read_sign_tv)
    TextView mPushMsgReadSignTV;
    @InjectView(R.id.my_push_msg_delete_tv)
    TextView mPushMsgDeleteTV;

    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipeRL= new SwipeRefreshLayout[2];
    private RecyclerView[] mRecyclerViews = new RecyclerView[2];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[2];
    private LinearLayout[] mNoDataLL = new LinearLayout[2];
    private int[] mPage = new int[2];
    private List<MyPushMsgModel>[] mLists = new ArrayList[2];
    public int mCurrIndex = 0;// 当前页卡编号
    private MyPushMsgAdapter[] mPushMsgAdapters = new MyPushMsgAdapter[2];
    public MyPushMsgMvpPresenter mMyPushMsgMvpPresenter;
    public boolean isRedact; //是否编辑
    public boolean isNoDataChoose;//没选中消息可以全部删除或设为已读
    private CustomDialog mDeleteDialog;
    private boolean isClickInfo;//是否点击了消息详情
    private int[] mMsgCount = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myselfpushmessage);
        initview();
        if (!LoginManager.isLogin()) {
            Intent loginintent = new Intent(this, LoginActivity.class);
            loginintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginintent.putExtra("PushMessageIntent",true);
            startActivityForResult(loginintent,1);
            return;
        }
        showProgressDialog();
        mPage[mCurrIndex] = 1;
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyPushMsgMvpPresenter != null) {
            mMyPushMsgMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.my_push_msg_read_sign_tv,
            R.id.my_push_msg_delete_tv
    })
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.my_push_msg_read_sign_tv:
                if (isNoDataChoose) {
                    if (mLists[mCurrIndex].size() > 0) {
                        showDeleteDialog(null,2);
                    }
                } else {
                    ArrayList ids = getChooseIds();
                    if (ids.size() > 0) {
                        showDeleteDialog(ids,2);
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_my_push_msg_plase_choose_item));
                    }
                }
                break;
            case R.id.my_push_msg_delete_tv:
                if (isNoDataChoose) {
                    MessageUtils.showToast(getResources().getString(R.string.module_my_push_msg_plase_choose_item));
                } else {
                    ArrayList ids = getChooseIds();
                    if (ids.size() > 0) {
                        showDeleteDialog(ids,1);
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_my_push_msg_plase_choose_item));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initview() {
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_push_message_title_text));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.invoiceinfo_editor_address_txt), 16, new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (!isRedact) {
                    isRedact = true;
                    mPushMsgBottomLL.setVisibility(View.VISIBLE);
                    TitleBarUtils.setNextViewText(MySelfPushMessageActivity.this,
                            getResources().getString(R.string.cancel));
                    setBottomTVText();
                } else {
                    isRedact = false;
                    mPushMsgBottomLL.setVisibility(View.GONE);
                    TitleBarUtils.setNextViewText(MySelfPushMessageActivity.this,
                            getResources().getString(R.string.invoiceinfo_editor_address_txt));
                }
            }
        });

        mMyPushMsgMvpPresenter = new MyPushMsgMvpPresenter();
        mMyPushMsgMvpPresenter.attachView(this);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        for(int i = 0 ;i< mListViews.size(); i++ ) {
            mSwipeRL[i] = (SwipeRefreshLayout)mListViews.get(i). findViewById(R.id.app_swipe_refresh);
            mSwipeRL[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mRecyclerViews[i]  = (RecyclerView) mListViews.get(i).findViewById(R.id.app_recycler_view);
            mSwipeRL[i].setBackgroundColor(getResources().getColor(R.color.app_linearlayout_bg));
            mSwipeRL[i].setOnRefreshListener(this);
            mNullDataRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.app_no_data_ll);
            mNoDataLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.my_coupons_no_data_ll);
            mNoDataLL[i].setVisibility(View.VISIBLE);
            TextView noDataTV = (TextView) mListViews.get(i).findViewById(R.id.my_msg_no_data_tv);
            noDataTV.setText(getResources().getString(R.string.module_my_push_msg_no_data_remind));
            mNullDataRL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage[mCurrIndex] = 1;
                    mPushMsgAdapters[mCurrIndex] = null;
                    initData();
                }
            });
        }
        mPushMsgVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        mPushMsgVP.setOnPageChangeListener(new PagerChangeListener());
        mPushMsgTL.setupWithViewPager(mPushMsgVP);
        for (int i = 0; i < mPushMsgTL.getTabCount(); i++) {
            if (i == 0) {
                mPushMsgTL.getTabAt(i).setText(getResources().getString(R.string.module_my_push_msg_system_msg));
            } else if (i == 1) {
                mPushMsgTL.getTabAt(i).setText(getResources().getString(R.string.module_my_push_msg_linhui_msg));
            }
        }
        mPushMsgTL.post(new Runnable() {
            @Override
            public void run() {
                com.linhuiba.linhuifield.connector.Constants.setIndicator(mPushMsgTL,
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(MySelfPushMessageActivity.this,15),
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(MySelfPushMessageActivity.this,15));
            }
        });
    }
    private void initData() {
        if (mPushMsgAdapters[mCurrIndex] == null) {
            mMyPushMsgMvpPresenter.getUserMsg(LoginManager.getUid(),mCurrIndex + 1,mPage[mCurrIndex]);
        } else {
            if (mSwipeRL[mCurrIndex].isShown()) {
                mSwipeRL[mCurrIndex].setRefreshing(false);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (LoginManager.isLogin()) {
                    mPage[mCurrIndex] = 1;
                    initData();
                } else {
                    this.finish();
                }

                break;
            default:
                break;
        }
        switch (requestCode) {
            case 1:
                if (LoginManager.isLogin()) {
                    mPage[mCurrIndex] = 1;
                    initData();
                } else {
                    this.finish();
                }

                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        if (!LoginManager.isLogin()) {
            Intent loginintent = new Intent(this, LoginActivity.class);
            loginintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginintent.putExtra("PushMessageIntent",true);
            startActivityForResult(loginintent,1);
        } else {
            mPage[mCurrIndex] = 1;
            initData();
        }
    }
    @Override
    public void onRefresh() {
        mPage[mCurrIndex] = 1;
        mPushMsgAdapters[mCurrIndex] = null;
        initData();
    }

    @Override
    public void onUserPushMsgSuccess(ArrayList<MyPushMsgModel> data,String sys_count,String lh_count) {
        isRedact = false;
        mPushMsgBottomLL.setVisibility(View.GONE);
        mMsgCount[0] = Integer.parseInt(sys_count);
        mMsgCount[1] = Integer.parseInt(lh_count);
        TitleBarUtils.setNextViewText(MySelfPushMessageActivity.this,
                getResources().getString(R.string.invoiceinfo_editor_address_txt));
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (mLists[mCurrIndex] != null) {
            mLists[mCurrIndex].clear();
        }
        if (sys_count.length() > 0) {
            mPushMsgTL.getTabAt(0).setText(getResources().getString(R.string.module_my_push_msg_system_msg) + "(" +
            sys_count + ")");
        }
        if (lh_count.length() > 0) {
            mPushMsgTL.getTabAt(1).setText(getResources().getString(R.string.module_my_push_msg_linhui_msg) + "(" +
                    lh_count + ")");
        }
        if (mPushMsgAdapters[mCurrIndex] == null) {
            mLists[mCurrIndex] = data;
        } else {
            mLists[mCurrIndex].addAll(data);
        }
        if (mLists[mCurrIndex] != null &&
                mLists[mCurrIndex].size() > 0) {
            for (int i = 0; i < mLists[mCurrIndex].size(); i++) {
                MyPushMsgAdapter.getIsSelected().put(mLists[mCurrIndex].get(i).getId(),false);
            }
            mNullDataRL[mCurrIndex].setVisibility(View.GONE);
            if (mPushMsgAdapters[mCurrIndex] == null) {
                mPushMsgAdapters[mCurrIndex] = new MyPushMsgAdapter(R.layout.module_recycle_item_push_msg,mLists[mCurrIndex],MySelfPushMessageActivity.this,
                        MySelfPushMessageActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerViews[mCurrIndex].setLayoutManager(linearLayoutManager);
                mRecyclerViews[mCurrIndex].setAdapter(mPushMsgAdapters[mCurrIndex]);
                mPushMsgAdapters[mCurrIndex].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mPushMsgAdapters[mCurrIndex].setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        mPage[mCurrIndex] = mPage[mCurrIndex] + 1;
                        mMyPushMsgMvpPresenter.getUserMsg(LoginManager.getUid(),mCurrIndex + 1,mPage[mCurrIndex]);
                    }
                });
            } else {
                mPushMsgAdapters[mCurrIndex].notifyDataSetChanged();
            }
            mPushMsgAdapters[mCurrIndex].loadMoreComplete();
            if (mLists[mCurrIndex].size() < 10) {
                mPushMsgAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mNullDataRL[mCurrIndex].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserPushMsgFailure(boolean superresult, Throwable error) {
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onUserPushMsgMoreSuccess(ArrayList<MyPushMsgModel> data,String sys_count,String lh_count) {
        mMsgCount[0] = Integer.parseInt(sys_count);
        mMsgCount[1] = Integer.parseInt(lh_count);
        if (sys_count.length() > 0) {
            mPushMsgTL.getTabAt(0).setText(getResources().getString(R.string.module_my_push_msg_system_msg) + "(" +
                    sys_count + ")");
        }
        if (lh_count.length() > 0) {
            mPushMsgTL.getTabAt(1).setText(getResources().getString(R.string.module_my_push_msg_linhui_msg) + "(" +
                    lh_count + ")");
        }
        List<MyPushMsgModel> mList = new ArrayList<>();
        mList = data;
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                MyPushMsgAdapter.getIsSelected().put(mList.get(i).getId(),false);
            }
            for (int i = 1; i < mList.size(); i++) {
                mLists[mCurrIndex].add(mList.get(i));
            }
            mPushMsgAdapters[mCurrIndex].notifyDataSetChanged();
            mPushMsgAdapters[mCurrIndex].loadMoreComplete();
            if (mList.size() < 10) {
                mPushMsgAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mPushMsgAdapters[mCurrIndex].loadMoreEnd();
        }
    }

    @Override
    public void onUserPushMsgMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mPushMsgAdapters[mCurrIndex].loadMoreFail();
        mPage[mCurrIndex] --;
        checkAccess_new(error);
    }

    @Override
    public void onDeleteUserPushMsgSuccess() {
        mPage[mCurrIndex] = 1;
        mPushMsgAdapters[mCurrIndex] = null;
        initData();
    }

    @Override
    public void onDeleteUserPushMsgFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onSetUserPushMsgReadSuccess() {
        if (!isClickInfo) {
            mPage[mCurrIndex] = 1;
            mPushMsgAdapters[mCurrIndex] = null;
            initData();
        } else {
            isClickInfo = false;
        }

    }

    @Override
    public void onSetUserPushMsgReadFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
        isClickInfo = false;
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
                    if (mPushMsgAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                        mPage[mCurrIndex] = 1;
                        initData();
                    }
                    break;
                case 1:
                    if (mPushMsgAdapters[mCurrIndex] == null) {
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
    public void setBottomTVText() {
        if (MyPushMsgAdapter.isNoChoose(mLists[mCurrIndex])) {
            isNoDataChoose = true;
            mPushMsgReadSignTV.setText(getResources().getString(R.string.module_my_push_msg_set_all_read));
            mPushMsgDeleteTV.setText(getResources().getString(R.string.invoiceinfo_delete_address_txt));
            if (mMsgCount[mCurrIndex] > 0) {
                mPushMsgReadSignTV.setEnabled(true);
                mPushMsgReadSignTV.setTextColor(getResources().getColor(R.color.default_bluebg));
            } else {
                mPushMsgReadSignTV.setEnabled(false);
                mPushMsgReadSignTV.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
            }
        } else {
            isNoDataChoose = false;
            mPushMsgReadSignTV.setText(getResources().getString(R.string.module_my_push_msg_read_sign));
            mPushMsgDeleteTV.setText(getResources().getString(R.string.invoiceinfo_delete_address_txt));
            if (isHaveReadChoose(mLists[mCurrIndex])) {
                mPushMsgReadSignTV.setEnabled(true);
                mPushMsgReadSignTV.setTextColor(getResources().getColor(R.color.default_bluebg));
            } else {
                mPushMsgReadSignTV.setEnabled(false);
                mPushMsgReadSignTV.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
            }
        }
    }
    private ArrayList<Integer> getChooseIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mLists[mCurrIndex].size(); i++) {
            if (MyPushMsgAdapter.getIsSelected().get(mLists[mCurrIndex].get(i).getId())) {
                ids.add(mLists[mCurrIndex].get(i).getId());
            }
        }
        return ids;
    }

    /**
     *
     * @param ids
     * @param type 1删除，2设置为已读
     */
    private void showDeleteDialog(final ArrayList<Integer> ids, final int type) {
        if (mDeleteDialog == null || !mDeleteDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            mDeleteDialog.dismiss();
                            if (type == 1) {
                                showProgressDialog();
                                mMyPushMsgMvpPresenter.deleteUserMsgs(ids);
                            } else if (type == 2) {
                                if (isNoDataChoose) {
                                    showProgressDialog();
                                    mMyPushMsgMvpPresenter.setAllMsgsRead();
                                } else {
                                    showProgressDialog();
                                    mMyPushMsgMvpPresenter.setMsgsRead(LoginManager.getUid(),ids);
                                }
                            }
                            break;
                        case R.id.btn_cancel:
                            mDeleteDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            String title = "";
            if (type == 1) {
                title = getResources().getString(R.string.module_my_push_msg_delete_remind);
            } else if (type == 2) {
                if (isNoDataChoose) {
                    title = getResources().getString(R.string.module_my_push_msg_set_all_read_remind);
                } else {
                    title = getResources().getString(R.string.module_my_push_msg_set_read_remind);
                }
            }
            CustomDialog.Builder builder = new CustomDialog.Builder(MySelfPushMessageActivity.this);
            mDeleteDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            title)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MySelfPushMessageActivity.this,mDeleteDialog);
            mDeleteDialog.show();
        }
    }
    public void onInfoClick(String data,int msgId,int position) {
        if (data != null && data.length() > 0) {
            Constants.pushUrlJumpActivity(data,MySelfPushMessageActivity.this,false);
        }
        if (mLists[mCurrIndex].get(position).getId() == msgId) {
            isClickInfo = true;
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(msgId);
            mMyPushMsgMvpPresenter.setMsgsRead(LoginManager.getUid(),ids);
            mLists[mCurrIndex].get(position).setIs_read(1);
            mPushMsgAdapters[mCurrIndex].notifyDataSetChanged();
        }
    }
    private  Boolean isHaveReadChoose(List<MyPushMsgModel> data) {
        boolean isNoChoose = false;
        for (int i = 0; i < data.size(); i++) {
            if (MyPushMsgAdapter.getIsSelected().get(data.get(i).getId()) && data.get(i).getIs_read() == 0) {
                isNoChoose = true;
                break;
            }
        }
        return isNoChoose;
    }

}
