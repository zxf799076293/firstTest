package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.baselib.app.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.InvoiceTitleAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.mvppresenter.InvoiceTitleMvpPresenter;
import com.linhuiba.business.mvpview.InvoiceTitleMvpView;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.CustomDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2018/7/19.
 */

public class InvoiceTitleListActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener,
        InvoiceTitleMvpView {
    @InjectView(R.id.invoice_title_list_tablayout)
    TabLayout mInvoiceTitleTL;
    @InjectView(R.id.invoice_title_list_vp)
    ViewPager mInvoiceTitleVP;
    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipeRL= new SwipeRefreshLayout[4];
    private RecyclerView[] mRecyclerViews = new RecyclerView[4];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[4];
    private View[] mNoDataRL = new View[4];
    private ImageView[] mNoDataImg = new ImageView[4];
    private TextView[] mNoDataTV = new TextView[4];
    private Button[] mNoDataBtn = new Button[4];
    private int[] mPagePosition = new int[4];
    private List<InvoiceInfomationModel>[] mLists = new ArrayList[4];
    public int mCurrIndex = 0;// 当前页卡编号
    private int mTabTextViewList[] = {R.string.module_invoice_title_invoice_type_all, R.string.module_invoice_title_invoice_common_type,R.string.module_invoice_title_invoice_dedicated_type,
            R.string.module_invoice_title_invoice_receipt_type};
    private InvoiceTitleAdapter[] mInvoiceTitleAdapters = new InvoiceTitleAdapter[5];
    public InvoiceTitleMvpPresenter mInvoiceTitleMvpPresenter;
    private final int INFO_TITLE_INT = 1;//详情
    private final int ADD_TITLE_INT = 2;//新增
    public final int EDIT_TITLE_INT = 3;//编辑
    private CustomDialog mDialog;
    public int operationType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_invoice_title_list);
        initView();
        showProgressDialog();
        mPagePosition[mCurrIndex] = 1;
        initData();
    }
    private void initView() {
        ButterKnife.inject(this);
        mInvoiceTitleMvpPresenter = new InvoiceTitleMvpPresenter();
        mInvoiceTitleMvpPresenter.attachView(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_invoice_title_tielebar_name));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.showRegisterText(this, getResources().getString(R.string.myself_account_addtxt),15,
                getResources().getColor(R.color.default_bluebg),
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceInfomationModel invoiceInfomationModel = new InvoiceInfomationModel();
                int invoiceType = 0;
                if (mCurrIndex == 0) {
                    invoiceType = 0;
                } else if (mCurrIndex == 1) {
                    invoiceType = 2;
                } else if (mCurrIndex == 2) {
                    invoiceType = 3;
                } else if (mCurrIndex == 3) {
                    invoiceType = 1;
                }
                invoiceInfomationModel.setInvoice_type(invoiceType);
                Intent titleAddIntent = new Intent(InvoiceTitleListActivity.this,InvoiceTitleEditActivity.class);
                titleAddIntent.putExtra("show_type",ADD_TITLE_INT);
                titleAddIntent.putExtra("title_model",(Serializable) invoiceInfomationModel);
                startActivityForResult(titleAddIntent,ADD_TITLE_INT);
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("operation_type") != null) {
            operationType = intent.getExtras().getInt("operation_type");
        }

        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
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
            mNoDataRL[i] = (View) mListViews.get(i).findViewById(R.id.app_no_data_view);
            mNoDataImg[i] = (ImageView) mNoDataRL[i].findViewById(R.id.no_data_img);
            mNoDataTV[i] = (TextView) mNoDataRL[i].findViewById(R.id.no_data_tv);
            mNoDataBtn[i] = (Button) mNoDataRL[i].findViewById(R.id.no_data_btn);
            mNoDataImg[i].setImageResource(R.drawable.ic_invoice_title_no);
            mNoDataTV[i].setText(getResources().getString(R.string.module_invoice_title_nodata_tv_str));
            mNoDataBtn[i].setText(getResources().getString(R.string.module_invoice_title_nodata_btn_str));
            mNoDataBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InvoiceInfomationModel invoiceInfomationModel = new InvoiceInfomationModel();
                    int invoiceType = 0;
                    if (mCurrIndex == 0) {
                        invoiceType = 0;
                    } else if (mCurrIndex == 1) {
                        invoiceType = 2;
                    } else if (mCurrIndex == 2) {
                        invoiceType = 3;
                    } else if (mCurrIndex == 3) {
                        invoiceType = 1;
                    }
                    invoiceInfomationModel.setInvoice_type(invoiceType);
                    Intent titleAddIntent = new Intent(InvoiceTitleListActivity.this,InvoiceTitleEditActivity.class);
                    titleAddIntent.putExtra("show_type",ADD_TITLE_INT);
                    titleAddIntent.putExtra("title_model",(Serializable) invoiceInfomationModel);
                    startActivityForResult(titleAddIntent,ADD_TITLE_INT);
                }
            });
            mNoDataRL[i].setVisibility(View.VISIBLE);
        }
        mInvoiceTitleVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        mInvoiceTitleVP.setOnPageChangeListener(new PagerChangeListener());
        mInvoiceTitleTL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mInvoiceTitleTL,10,10);
            }
        });
        mInvoiceTitleTL.setupWithViewPager(mInvoiceTitleVP);
        for (int i = 0; i < mInvoiceTitleTL.getTabCount(); i++) {
            mInvoiceTitleTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInvoiceTitleMvpPresenter != null) {
            mInvoiceTitleMvpPresenter.detachView();
        }
    }

    @Override
    public void onInvoiceTitleListSuccess(ArrayList<InvoiceInfomationModel> data) {
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (mLists[mCurrIndex] != null) {
            mLists[mCurrIndex].clear();
        }
        if (mInvoiceTitleAdapters[mCurrIndex] == null) {
            mLists[mCurrIndex] = data;
        } else {
            mLists[mCurrIndex].addAll(data);
        }
        if (mLists[mCurrIndex] != null &&
                mLists[mCurrIndex].size() > 0) {
            mNullDataRL[mCurrIndex].setVisibility(View.GONE);
            if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                mInvoiceTitleAdapters[mCurrIndex] = new InvoiceTitleAdapter(R.layout.module_recycle_item_activity_invoice_title,mLists[mCurrIndex],InvoiceTitleListActivity.this,
                        InvoiceTitleListActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerViews[mCurrIndex].setLayoutManager(linearLayoutManager);
                mRecyclerViews[mCurrIndex].setAdapter(mInvoiceTitleAdapters[mCurrIndex]);
                mInvoiceTitleAdapters[mCurrIndex].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (operationType == INFO_TITLE_INT) {
                            Intent intent = new Intent();
                            intent.putExtra("title_model",(Serializable) mLists[mCurrIndex].get(position));
                            setResult(ADD_TITLE_INT,intent);
                            finish();
                        }
                    }
                });
                mInvoiceTitleAdapters[mCurrIndex].setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        mPagePosition[mCurrIndex] = mPagePosition[mCurrIndex] + 1;
                        int invoiceType = 0;
                        if (mCurrIndex == 0) {
                            invoiceType = 0;
                        } else if (mCurrIndex == 1) {
                            invoiceType = 2;
                        } else if (mCurrIndex == 2) {
                            invoiceType = 3;
                        } else if (mCurrIndex == 3) {
                            invoiceType = 1;
                        }
                        mInvoiceTitleMvpPresenter.getInvoiceTitleList(invoiceType,mPagePosition[mCurrIndex]);
                    }
                });
            } else {
                mInvoiceTitleAdapters[mCurrIndex].notifyDataSetChanged();
            }
            mInvoiceTitleAdapters[mCurrIndex].loadMoreComplete();
            if (mLists[mCurrIndex].size() < 10) {
                mInvoiceTitleAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mNullDataRL[mCurrIndex].setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onInvoiceTitleListMoreSuccess(ArrayList<InvoiceInfomationModel> data) {
        List<InvoiceInfomationModel> mList = new ArrayList<>();
        mList = data;
        if (mList != null && data.size() > 0) {
            for (int i = 10; i < mList.size(); i++) {
                mLists[mCurrIndex].add(mList.get(i));
            }
            mInvoiceTitleAdapters[mCurrIndex].notifyDataSetChanged();
            mInvoiceTitleAdapters[mCurrIndex].loadMoreComplete();
            if (mLists[mCurrIndex].size() < 10) {
                mInvoiceTitleAdapters[mCurrIndex].loadMoreEnd();
            }
        } else {
            mInvoiceTitleAdapters[mCurrIndex].loadMoreEnd();
        }
    }

    @Override
    public void onInvoiceTitleListMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mInvoiceTitleAdapters[mCurrIndex].loadMoreFail();
        mPagePosition[mCurrIndex] --;
    }

    @Override
    public void onInvoiceTitleInfoSuccess(InvoiceInfomationModel data) {

    }

    @Override
    public void onInvoiceTitleAddSuccess() {

    }

    @Override
    public void onInvoiceTitleEditSuccess() {

    }

    @Override
    public void onInvoiceTitleDeleteSuccess() {
        showProgressDialog();
        mPagePosition[mCurrIndex] = 1;
        for (int i = 0; i < 4; i ++) {
            mInvoiceTitleAdapters[i] = null;
        }
        initData();
    }

    @Override
    public void onInvoiceTitleDefaultSuccess() {
        showProgressDialog();
        mPagePosition[mCurrIndex] = 1;
        mInvoiceTitleAdapters[mCurrIndex] = null;
        initData();
    }

    @Override
    public void onInvoiceTitleFailure(boolean superresult, Throwable error) {
        if (mSwipeRL[mCurrIndex].isShown()) {
            mSwipeRL[mCurrIndex].setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
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
                    if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                    }
                    mPagePosition[mCurrIndex] = 1;
                    initData();
                    break;
                case 1:
                    if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                    }
                    mPagePosition[mCurrIndex] = 1;
                    initData();
                    break;
                case 2:
                    if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                    }
                    mPagePosition[mCurrIndex] = 1;
                    initData();
                    break;
                case 3:
                    if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                    }
                    mPagePosition[mCurrIndex] = 1;
                    initData();
                    break;
                case 4:
                    if (mInvoiceTitleAdapters[mCurrIndex] == null) {
                        showProgressDialog();
                    }
                    mPagePosition[mCurrIndex] = 1;
                    initData();
                    break;
                default:
                    break;
            }

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initData() {
        if (mInvoiceTitleAdapters[mCurrIndex] == null) {
            int invoiceType = 0;
            if (mCurrIndex == 0) {
                invoiceType = 0;
            } else if (mCurrIndex == 1) {
                invoiceType = 2;
            } else if (mCurrIndex == 2) {
                invoiceType = 3;
            } else if (mCurrIndex == 3) {
                invoiceType = 1;
            }
            mInvoiceTitleMvpPresenter.getInvoiceTitleList(invoiceType,mPagePosition[mCurrIndex]);
        } else {
            if (mSwipeRL[mCurrIndex].isShown()) {
                mSwipeRL[mCurrIndex].setRefreshing(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        mPagePosition[mCurrIndex] = 1;
        mInvoiceTitleAdapters[mCurrIndex] = null;
        initData();
    }
    public void showMsgDialog(final int type, final int id) {//type: 0同意；1：拒绝
        String msg = "";
        if (type == 0) {
            msg = getResources().getString(R.string.delete_prompt);
        } else if (type == 1) {

        }
        if (mDialog == null || !mDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mDialog.dismiss();
                        if (type == 1) {

                        } else if (type == 0) {
                            mInvoiceTitleMvpPresenter.deleteInvoiceTitle(id);
                        }
                    } else if (i == R.id.btn_cancel) {
                        mDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(InvoiceTitleListActivity.this);
            mDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,msg)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(InvoiceTitleListActivity.this,mDialog);
            mDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ADD_TITLE_INT:
                showProgressDialog();
                mPagePosition[mCurrIndex] = 1;
                for (int i = 0; i < 4; i ++) {
                    mInvoiceTitleAdapters[i] = null;
                }
                initData();
                break;
            case EDIT_TITLE_INT:
                showProgressDialog();
                mPagePosition[mCurrIndex] = 1;
                for (int i = 0; i < 4; i ++) {
                    mInvoiceTitleAdapters[i] = null;
                }
                initData();
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
