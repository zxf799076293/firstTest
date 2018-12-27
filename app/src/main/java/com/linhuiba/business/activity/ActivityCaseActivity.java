package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.ActivityCaseAdapterTmp;
import com.linhuiba.business.adapter.ActivityCaseSearchAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningItemAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.CaseInfoModel;
import com.linhuiba.business.model.CaseListModel;
import com.linhuiba.business.model.CaseThemeModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.CaseMvpPresenter;
import com.linhuiba.business.mvpview.CaseMvpView;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ActivityCaseActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener,
        CaseMvpView {
    @InjectView(R.id.case_recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.case_search_field_type_ll)
    LinearLayout mCaseSearchFieleTypeLL;
    @InjectView(R.id.case_search_field_type_tv)
    TextView mCaseSearchFieleTypeTV;
    @InjectView(R.id.case_search_theme_type_ll)
    LinearLayout mCaseSearchThemeTypeLL;
    @InjectView(R.id.case_search_theme_type_tv)
    TextView mCaseSearchThemeTypeTV;
    @InjectView(R.id.case_search_label_type_tv)
    TextView mCaseSearchLabelTypeTV;
    @InjectView(R.id.case_search_view)
    View mCaseSearchView;
    @InjectView(R.id.case_swipe_refresh)
    SwipeRefreshLayout mCaseSwipeRefresh;
    @InjectView(R.id.case_no_data_ll)
    LinearLayout mCaseNoDataLL;
    @InjectView(R.id.case_selection_ll)
    LinearLayout mCaseSelectionLL;
    @InjectView(R.id.case_search_spread_ways_ll)
    LinearLayout mCaseSpreadWaysLL;
    @InjectView(R.id.case_search_spread_ways_tv)
    TextView mCaseSpreadWaysTV;
    @InjectView(R.id.case_search_promotion_purposes_ll)
    LinearLayout mCasePromotionPurposesLL;
    @InjectView(R.id.case_search_promotion_purposes_tv)
    TextView mCasePromotionPurposesTV;
    @InjectView(R.id.case_search_city_ll)
    LinearLayout mCaseCityLL;
    @InjectView(R.id.case_search_city_tv)
    TextView mCaseCityTV;
    private ActivityCaseAdapterTmp mActivityCaseAdapterTmp;
    private ArrayList<CaseListModel> mDatas = new ArrayList<>();
    private SupportPopupWindow mSearchSortPw;
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private Drawable mSortBlueUpDrawable;//排序
    private Drawable mSortBlueDownDrawable;//排序
    private String ShareTitleStr = "";//分享的标题
    private String mSharePYQTitleStr;
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String share_linkurl = "";//微信分享的url
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private Dialog shareDialog;
    private IWXAPI mIWXAPI;
    private int page = 1;
    private CaseMvpPresenter mCaseMvpPresenter;
    private ArrayList<Integer> mFieldTypeIds = new ArrayList<>();
    private ArrayList<Integer> mCaseLabelTypeIds = new ArrayList<>();
    private ArrayList<Integer> mIndustriesIds = new ArrayList<>();//行业
    private ArrayList<Integer> mSpreadWaysIds = new ArrayList<>();//形式 推广方式
    private ArrayList<Integer> mPromotionPurposesIds = new ArrayList<>();//目的 推广目的
    private ArrayList<Integer> mCityIds = new ArrayList<>();//城市

    private ArrayList<CaseThemeModel> mFieldTypeList = new ArrayList<>();//类型（类目）
    private ArrayList<CaseThemeModel> mLabelTypeList = new ArrayList<>();//标签(特色推荐) 推荐
    private ArrayList<CaseThemeModel> mIndustriesList = new ArrayList<>();//行业
    private ArrayList<CaseThemeModel> mSpreadWaysList = new ArrayList<>();//形式 推广方式
    private ArrayList<CaseThemeModel> mPromotionPurposesList = new ArrayList<>();//目的 推广目的
    private ArrayList<CaseThemeModel> mCityList = new ArrayList<>();//城市
    private int mCityId;
    private boolean isShare;
    private DisplayMetrics metric = new DisplayMetrics();
    private CaseThemeModel mCaseThemeModel;//筛选的数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_activity_case);
        ButterKnife.inject(this);
        initView();
        showProgressDialog();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCaseMvpPresenter != null) {
            mCaseMvpPresenter.detachView();
        }
    }

    private void initView() {
        mCaseMvpPresenter = new CaseMvpPresenter();
        mCaseMvpPresenter.attachView(this);
        mSortGreyUpDrawable = getResources().getDrawable(R.drawable.ic_open_gary_one_button_normal_three);
        mSortGreyUpDrawable.setBounds(0, 0, mSortGreyUpDrawable.getMinimumWidth(), mSortGreyUpDrawable.getMinimumHeight());
        mSortGreyDownDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_normal_three);
        mSortGreyDownDrawable.setBounds(0, 0, mSortGreyDownDrawable.getMinimumWidth(), mSortGreyDownDrawable.getMinimumHeight());
        mSortBlueUpDrawable = getResources().getDrawable(R.drawable.ic_openup_button_blue_normal_three);
        mSortBlueUpDrawable.setBounds(0, 0, mSortBlueUpDrawable.getMinimumWidth(), mSortBlueUpDrawable.getMinimumHeight());
        mSortBlueDownDrawable = getResources().getDrawable(R.drawable.ic_open_one_button_blue_normal_three);
        mSortBlueDownDrawable.setBounds(0, 0, mSortBlueDownDrawable.getMinimumWidth(), mSortBlueDownDrawable.getMinimumHeight());
        mCaseSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mCaseSwipeRefresh.setOnRefreshListener(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_case_title));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.showTitleBarRightCard(this, true, R.drawable.popup_ic_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //客服功能
                AndPermission.with(ActivityCaseActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();

            }
        });
        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.popup_ic_share), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas != null && mDatas.size() > 0) {
                    if (!isShare) {
                        isShare = true;
                        share();
                    }

                } else {
                    MessageUtils.showToast(getResources().getString(R.string.module_case_share_no_data));
                }
            }
        });
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        ActivityCaseActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        ShareTitleStr = getResources().getString(R.string.module_case_share_title);
        mSharePYQTitleStr = getResources().getString(R.string.module_case_share_title);
        mCaseMvpPresenter.getCaseSelection();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().get("city_id") != null) {
                mCityId = intent.getExtras().getInt("city_id");
            }
            if (intent.getExtras().get("field_type_ids") != null) {
                mFieldTypeIds = (ArrayList<Integer>) intent.getExtras().get("field_type_ids");
            }
            if (intent.getExtras().get("case_label_ids") != null) {
                mCaseLabelTypeIds = (ArrayList<Integer>) intent.getExtras().get("case_label_ids");
            }
            if (intent.getExtras().get("city_ids") != null) {
                mCityIds = (ArrayList<Integer>) intent.getExtras().get("city_ids");
            }
            if (!mCityIds.contains(mCityId)) {
                mCityIds.add(mCityId);
            }
            if (intent.getExtras().get("industry_ids") != null) {
                mIndustriesIds = (ArrayList<Integer>) intent.getExtras().get("industry_ids");
            }
            if (intent.getExtras().get("spread_way_ids") != null) {
                mSpreadWaysIds = (ArrayList<Integer>) intent.getExtras().get("spread_way_ids");
            }
            if (intent.getExtras().get("promotion_purpose_ids") != null) {
                mPromotionPurposesIds = (ArrayList<Integer>) intent.getExtras().get("promotion_purpose_ids");
            }
            refreshSearchView(false, 0);
            refreshSearchView(false, 1);
            refreshSearchView(false, 2);
            refreshSearchView(false, 3);
            refreshSearchView(false, 4);
            refreshSearchView(false, 5);
        }
        if (mCityId == 0) {
            if (LoginManager.getInstance().getTrackcityid() != null &&
                    LoginManager.getInstance().getTrackcityid().length() > 0) {
                mCityId = Integer.parseInt(LoginManager.getInstance().getTrackcityid());
            }
        }
        //浏览记录
        if (LoginManager.isLogin()) {
            try {
                String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl(
                        0,mFieldTypeIds,mIndustriesIds,mSpreadWaysIds,mPromotionPurposesIds,mCityIds,mCaseLabelTypeIds,page,mCityId
                ));
                LoginMvpModel.sendBrowseHistories("case_list",parameter,LoginManager.getTrackcityid());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    private void initData() {
        mCaseMvpPresenter.getCaseListData(0,mFieldTypeIds,mIndustriesIds,mSpreadWaysIds,mPromotionPurposesIds,mCityIds
                ,mCaseLabelTypeIds,page,mCityId);
    }
    private void listPopupWindow (final ArrayList<CaseThemeModel> list, final int type) {
        if (list == null || list.size() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            return;
        }
        refreshSearchView(true, type);
        ResourcesScreeningItemAdapter adapter = null;
        View myView = ActivityCaseActivity.this.getLayoutInflater().inflate(R.layout.activit_search_sort_pw_layout, null);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        mSearchSortPw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mSearchSortPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mSearchSortPw.getBackground().setAlpha(155);
        //设置焦点为可点击
        mSearchSortPw.setFocusable(true);//可以试试设为false的结果
        mSearchSortPw.showAsDropDown(mCaseSearchView);
        mSearchSortPw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchSortPw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mSearchSortPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                refreshSearchView(false, type);
            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSortPw.isShowing()) {
                    mSearchSortPw.dismiss();
                }
            }
        });
        final MyGridview gv = (MyGridview) myView.findViewById(R.id.search_sort_pw_gv);
        LinearLayout svll = (LinearLayout) myView.findViewById(R.id.search_sort_pw_sv);
        ListView lv = (ListView) myView.findViewById(R.id.search_sort_pw_lv);
        LinearLayout ll = (LinearLayout) myView.findViewById(R.id.search_sort_pw_btn_ll);
        Button resetBtn = (Button) myView.findViewById(R.id.search_sort_resetbtn);
        Button confirmBtn = (Button) myView.findViewById(R.id.search_sort_confirmbtn);
        lv.setVisibility(View.GONE);
        svll.setVisibility(View.VISIBLE);
        ll.setVisibility(View.VISIBLE);
        final ActivityCaseSearchAdapter mActivityCaseSearchAdapter = new ActivityCaseSearchAdapter(list,this,this);
        // FIXME: 2018/12/8 案例筛选项选中赋值
        for (int i = 0; i < list.size(); i++) {
            if (type == 0) {
                if (mFieldTypeIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            } else if (type == 1) {
                if (mIndustriesIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            } else if (type == 2) {
                if (mSpreadWaysIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            } else if (type == 3) {
                if (mPromotionPurposesIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            } else if (type == 4) {
                if (mCityIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            } else if (type == 5) {
                if (mCaseLabelTypeIds.contains(Integer.parseInt(String.valueOf(list.get(i).getId())))) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), true);
                } else {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                }
            }

        }
        gv.setAdapter(mActivityCaseSearchAdapter);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    mActivityCaseSearchAdapter.getCheckedList().put(list.get(i).getId(), false);
                    mActivityCaseSearchAdapter.notifyDataSetChanged();
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    if (mFieldTypeIds != null) {
                        mFieldTypeIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mFieldTypeIds.add(list.get(i).getId());
                        }
                    }
                } else if (type == 1) {
                    if (mIndustriesIds != null) {
                        mIndustriesIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mIndustriesIds.add(list.get(i).getId());
                        }
                    }
                } else if (type == 2) {
                    if (mSpreadWaysIds != null) {
                        mSpreadWaysIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mSpreadWaysIds.add(list.get(i).getId());
                        }
                    }
                } else if (type == 3) {
                    if (mPromotionPurposesIds != null) {
                        mPromotionPurposesIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mPromotionPurposesIds.add(list.get(i).getId());
                        }
                    }
                } else if (type == 4) {
                    if (mCityIds != null) {
                        mCityIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mCityIds.add(list.get(i).getId());
                        }
                    }
                } else if (type == 5) {
                    if (mCaseLabelTypeIds != null) {
                        mCaseLabelTypeIds.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() > 0 &&
                                mActivityCaseSearchAdapter.getCheckedList().get(list.get(i).getId())) {
                            mCaseLabelTypeIds.add(list.get(i).getId());
                        }
                    }
                }
                // FIXME: 2018/12/8 案例筛选项选中确定操作
                mSearchSortPw.dismiss();
                refreshSearchView(false,type);
                showProgressDialog();
                page = 1;
                mCaseMvpPresenter.getCaseListData(0,mFieldTypeIds,mIndustriesIds,mSpreadWaysIds,mPromotionPurposesIds,mCityIds
                        ,mCaseLabelTypeIds,page,mCityId);
            }
        });
    }
    @OnClick({
            R.id.case_search_field_type_ll,
            R.id.case_search_theme_type_ll,
            R.id.case_search_label_type_ll,
            R.id.case_search_spread_ways_ll,
            R.id.case_search_promotion_purposes_ll,
            R.id.case_search_city_ll
    })
    public void OnClicK(View view) {
        switch (view.getId()) {
            case R.id.case_search_field_type_ll:
                listPopupWindow(mFieldTypeList,0);
                break;
            case R.id.case_search_theme_type_ll:
                listPopupWindow(mIndustriesList,1);
                break;
            case R.id.case_search_label_type_ll:
                listPopupWindow(mLabelTypeList,5);
                break;
            case R.id.case_search_spread_ways_ll:
                listPopupWindow(mSpreadWaysList,2);
                break;
            case R.id.case_search_promotion_purposes_ll:
                listPopupWindow(mPromotionPurposesList,3);
                break;
            case R.id.case_search_city_ll:
                listPopupWindow(mCityList,4);
                break;
            default:
                break;
        }
    }
    private void refreshSearchView(boolean isShow, int type) {//isShow: pw是否要弹出；type：0 场地类型，1：主题类型;2 标签,isChoose : 是否选了类型 点击了listitem
        if (isShow) {
            if (type == 0) {
                if (mFieldTypeIds.size() > 0) {
                    mCaseSearchFieleTypeTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCaseSearchFieleTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchFieleTypeTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCaseSearchFieleTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 1) {
                if (mIndustriesIds.size() > 0) {
                    mCaseSearchThemeTypeTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCaseSearchThemeTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchThemeTypeTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCaseSearchThemeTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 2) {
                if (mSpreadWaysIds.size() > 0) {
                    mCaseSpreadWaysTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCaseSpreadWaysTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSpreadWaysTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCaseSpreadWaysTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 3) {
                if (mPromotionPurposesIds.size() > 0) {
                    mCasePromotionPurposesTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCasePromotionPurposesTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCasePromotionPurposesTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCasePromotionPurposesTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 4) {
                if (mCityIds.size() > 0) {
                    mCaseCityTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCaseCityTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseCityTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCaseCityTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 5) {
                if (mCaseLabelTypeIds.size() > 0) {
                    mCaseSearchLabelTypeTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mCaseSearchLabelTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchLabelTypeTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mCaseSearchLabelTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            }
        } else {
            if (type == 0) {
                if (mFieldTypeIds.size() > 0) {
                    mCaseSearchFieleTypeTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCaseSearchFieleTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchFieleTypeTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCaseSearchFieleTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 1) {
                if (mIndustriesIds.size() > 0) {
                    mCaseSearchThemeTypeTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCaseSearchThemeTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchThemeTypeTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCaseSearchThemeTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 2) {
                if (mSpreadWaysIds.size() > 0) {
                    mCaseSpreadWaysTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCaseSpreadWaysTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSpreadWaysTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCaseSpreadWaysTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 3) {
                if (mPromotionPurposesIds.size() > 0) {
                    mCasePromotionPurposesTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCasePromotionPurposesTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCasePromotionPurposesTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCasePromotionPurposesTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 4) {
                if (mCityIds.size() > 0) {
                    mCaseCityTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCaseCityTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseCityTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCaseCityTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            } else if (type == 5) {
                if (mCaseLabelTypeIds.size() > 0) {
                    mCaseSearchLabelTypeTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mCaseSearchLabelTypeTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    mCaseSearchLabelTypeTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mCaseSearchLabelTypeTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                }
            }
        }
    }
    @Override
    public void onRefresh() {
        page = 1;
        mActivityCaseAdapterTmp = null;
        initData();
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(ActivityCaseActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
                    @Override
                    public void onSuccess(String clientId) {
                        if (LoginManager.isLogin()) {
                            HashMap<String, String> clientInfo = new HashMap<>();
                            clientInfo.put("name", LoginManager.getCompany_name());
                            clientInfo.put("email", LoginManager.geteEmail());
                            if (LoginManager.getRole_id().equals("2")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_property_str));
                            } else if (LoginManager.getRole_id().equals("3")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_business_str));
                            } else if (LoginManager.getRole_id().equals("1")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_admin_str));
                            }
                            clientInfo.put("avatar", "https://banner.linhuiba.com/o_1b555h2jjoj6u1716tr12dl2rg7.jpg");
                            clientInfo.put("tel", LoginManager.getMobile());
                            // 相同的 id 会被识别为同一个顾客
                            Intent intent = new MQIntentBuilder(ActivityCaseActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(ActivityCaseActivity.this)
                                    .build();
                            startActivityForResult(intent,10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });

            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };

    private void share() {
        //2017/9/23 分享功能
        //2018/7/4 title设置 朋友圈 和小程序
        if (ShareBitmap == null || miniShareBitmap == null) {
            showProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 2018/7/4 判断bitmap
                    if (mDatas.size() > 0) {
                        //2018/7/4 获取图片url
                        if (mDatas != null && mDatas.size() > 0) {
                            ShareIconStr = mDatas.get(0).getCover_pic_url() + "?imageView2/0/w/300/h/240";
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                            miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                            ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 300, 220, true);//压缩Bitmap
                        }
                        if (ShareBitmap == null) {
                            ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        }
                        if (miniShareBitmap == null) {
                            miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        }
                    } else {
                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            final View myView = ActivityCaseActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            shareDialog = new AlertDialog.Builder(ActivityCaseActivity.this).create();
            //2018/7/4 设置 url
            sharewxMiniShareLinkUrl = Config.SHARE_CASE_LIST_WXMINI_URL+"community_type_ids="+JSON.toJSONString(mFieldTypeIds,true).trim() +
                    "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                    "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                    "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                    "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                    "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                    "&city_id=" + String.valueOf(mCityId);
            share_linkurl = Config.SHARE_CASE_LIST_PYQ_URL+"community_type_ids="+JSON.toJSONString(mFieldTypeIds,true).trim() +
                    "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                    "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                    "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                    "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                    "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                    "&city_id=" + String.valueOf(mCityId);

            if (shareDialog!= null && shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            isShare = false;
            Constants constants = new Constants(ActivityCaseActivity.this,
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(ActivityCaseActivity.this,myView,shareDialog,mIWXAPI,share_linkurl,
                    ShareTitleStr,
                    SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    hideProgressDialog();
                    final View myView = ActivityCaseActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    shareDialog = new AlertDialog.Builder(ActivityCaseActivity.this).create();
                    //2018/7/4 设置url
                    sharewxMiniShareLinkUrl = Config.SHARE_CASE_LIST_WXMINI_URL+"community_type_ids="+JSON.toJSONString(mFieldTypeIds,true).trim() +
                            "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                            "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                            "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                            "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                            "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                            "&city_id=" + String.valueOf(mCityId);
                    share_linkurl = Config.SHARE_CASE_LIST_PYQ_URL+"community_type_ids="+JSON.toJSONString(mFieldTypeIds,true).trim() +
                            "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                            "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                            "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                            "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                            "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                            "&city_id=" + String.valueOf(mCityId);
                    if (shareDialog!= null && shareDialog.isShowing()) {
                        shareDialog.dismiss();
                    }
                    isShare = false;
                    Constants constants = new Constants(ActivityCaseActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(ActivityCaseActivity.this,myView,shareDialog,mIWXAPI,share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public void onCaseListSuccess(ArrayList<CaseListModel> caseListModels) {
        if (mCaseSwipeRefresh.isShown()) {
            mCaseSwipeRefresh.setRefreshing(false);
        }
        if (mDatas != null) {
            mDatas.clear();
        }
        if (mActivityCaseAdapterTmp == null) {
            mDatas = caseListModels;
        } else {
            mDatas.addAll(caseListModels);
        }
        if (mDatas != null && mDatas.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 2018/7/4 判断bitmap
                    ShareIconStr = mDatas.get(0).getCover_pic_url()+ "?imageView2/0/w/300/h/240";
                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                    miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                    ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 300, 220, true);//压缩Bitmap
                }
            }).start();
            mCaseNoDataLL.setVisibility(View.GONE);
            ActivityCaseAdapterTmp.clearHeights();
            for (int i = 0; i < mDatas.size(); i++) {
                Glide.with(getApplicationContext())
                        .load(mDatas.get(i).getCover_pic_url())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = metric.widthPixels / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(ActivityCaseActivity.this,20) * bitmap.getHeight() / width;
                                ActivityCaseAdapterTmp.getHeights().add(height);
                            }
                        });
            }
            if (mActivityCaseAdapterTmp == null) {
                mActivityCaseAdapterTmp = new ActivityCaseAdapterTmp(R.layout.module_recycle_item_activity_case,mDatas,ActivityCaseActivity.this,ActivityCaseActivity.this,0);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                //设置布局管理器
                mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                //设置为垂直布局，这也是默认的
                //设置Adapter
                mRecyclerView.setAdapter(mActivityCaseAdapterTmp);
                mActivityCaseAdapterTmp.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (view.getId() == R.id.iv_img) {
//
//                } else if (view.getId() == R.id.tv_title) {
//
//                }
                        //滚动到第几个
//                mRecyclerView.smoothScrollToPosition(0);
                        Intent caseInfoIntent = new Intent(ActivityCaseActivity.this,ActivityCaseInfoActivity.class);
                        caseInfoIntent.putExtra("id",mDatas.get(position).getId());
                        if (mFieldTypeIds.size() > 0) {
                            caseInfoIntent.putExtra("community_type_ids",(Serializable) mFieldTypeIds);
                        }
                        if (mIndustriesIds.size() > 0) {
                            caseInfoIntent.putExtra("industry_ids",(Serializable) mIndustriesIds);
                        }
                        if (mSpreadWaysIds.size() > 0) {
                            caseInfoIntent.putExtra("spread_way_ids",(Serializable) mSpreadWaysIds);
                        }

                        if (mPromotionPurposesIds.size() > 0) {
                            caseInfoIntent.putExtra("promotion_purpose_ids",(Serializable) mPromotionPurposesIds);
                        }

                        if (mCityIds.size() > 0) {
                            caseInfoIntent.putExtra("city_ids",(Serializable) mCityIds);
                        }

                        if (mCaseLabelTypeIds.size() > 0) {
                            caseInfoIntent.putExtra("label_ids",(Serializable) mCaseLabelTypeIds);
                        }
                        startActivity(caseInfoIntent);
                    }
                });
                mActivityCaseAdapterTmp.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        page = page + 1;
                        initData();
                    }
                });
            } else {
                mActivityCaseAdapterTmp.notifyDataSetChanged();
            }
            mActivityCaseAdapterTmp.loadMoreComplete();
            if (mDatas.size() < 10) {
                mActivityCaseAdapterTmp.loadMoreEnd();
            }
        } else {
            mCaseNoDataLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCaseListFailure(boolean superresult, Throwable error) {
        if (mCaseSwipeRefresh.isShown()) {
            mCaseSwipeRefresh.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onCaseListMoreSuccess(ArrayList<CaseListModel> caseListModels) {
        ArrayList<CaseListModel> tmp = caseListModels;
        if (tmp != null && tmp.size() > 0) {
            for( CaseListModel fieldDetail: tmp ){
                mDatas.add(fieldDetail);
                Glide.with(getApplicationContext())
                        .load(fieldDetail.getCover_pic_url())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = metric.widthPixels / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(ActivityCaseActivity.this,20) * bitmap.getHeight() / width;
                                ActivityCaseAdapterTmp.getHeights().add(height);
                            }
                        });
            }
            mActivityCaseAdapterTmp.notifyDataSetChanged();
            mActivityCaseAdapterTmp.loadMoreComplete();
            if (tmp.size() < 10) {
                mActivityCaseAdapterTmp.loadMoreEnd();
            }
        } else {
            mActivityCaseAdapterTmp.loadMoreEnd();
        }
    }

    @Override
    public void onCaseListMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mActivityCaseAdapterTmp.loadMoreFail();
        page --;
    }

    @Override
    public void onCaseInfoSuccess(CaseInfoModel caseInfoModel) {

    }

    @Override
    public void onCaseInfoFailure(boolean superresult, Throwable error) {

    }


    @Override
    public void onCaseSelectionFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }
    @Override
    public void onCaseSelectionSuccess(CaseThemeModel caseThemeModel) {
        mCaseThemeModel = caseThemeModel;
        if (mCaseThemeModel != null) {
            mCaseSelectionLL.setVisibility(View.VISIBLE);
            if (mCaseThemeModel.getCategories() != null &&
                    mCaseThemeModel.getCategories().size() > 0) {
                mFieldTypeList = mCaseThemeModel.getCategories();
            }
            if (mCaseThemeModel.getLabels() != null &&
                    mCaseThemeModel.getLabels().size() > 0) {
                mLabelTypeList = mCaseThemeModel.getLabels();
            }
            if (mCaseThemeModel.getIndustries() != null &&
                    mCaseThemeModel.getIndustries().size() > 0) {
                mIndustriesList = mCaseThemeModel.getIndustries();
            }
            if (mCaseThemeModel.getSpread_ways() != null &&
                    mCaseThemeModel.getSpread_ways().size() > 0) {
                mSpreadWaysList = mCaseThemeModel.getSpread_ways();
            }
            if (mCaseThemeModel.getPromotion_purposes() != null &&
                    mCaseThemeModel.getPromotion_purposes().size() > 0) {
                mPromotionPurposesList = mCaseThemeModel.getPromotion_purposes();
            }
            if (mCaseThemeModel.getCities() != null &&
                    mCaseThemeModel.getCities().size() > 0) {
                mCityList = mCaseThemeModel.getCities();
            }
        }
    }

    private HashMap<String, String> getBrowseHistoriesUrl(
            int is_home,
            ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
            ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
            ArrayList<Integer> city_ids,
            ArrayList<Integer> label_ids,int page,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (is_home == 1) {
            paramsMap.put("is_home_page",String.valueOf(is_home));
        }
        if (community_type_ids != null && community_type_ids.size() > 0) {
            for (int i = 0; i < community_type_ids.size(); i++) {
                paramsMap.put("community_type_ids["+String.valueOf(i)+"]",String.valueOf(community_type_ids.get(i)));
            }
        }
        if (industry_ids != null && industry_ids.size() > 0) {
            for (int i = 0; i < industry_ids.size(); i++) {
                paramsMap.put("industry_ids["+String.valueOf(i)+"]",String.valueOf(industry_ids.get(i)));
            }
        }
        if (spread_way_ids != null && spread_way_ids.size() > 0) {
            for (int i = 0; i < spread_way_ids.size(); i++) {
                paramsMap.put("spread_way_ids["+String.valueOf(i)+"]",String.valueOf(spread_way_ids.get(i)));
            }
        }
        if (promotion_purpose_ids != null && promotion_purpose_ids.size() > 0) {
            for (int i = 0; i < promotion_purpose_ids.size(); i++) {
                paramsMap.put("promotion_purpose_ids["+String.valueOf(i)+"]",String.valueOf(promotion_purpose_ids.get(i)));
            }
        }

        if (city_ids != null && city_ids.size() > 0) {
            for (int i = 0; i < city_ids.size(); i++) {
                paramsMap.put("city_ids["+String.valueOf(i)+"]",String.valueOf(city_ids.get(i)));
            }
        }
        if (label_ids != null && label_ids.size() > 0) {
            for (int i = 0; i < label_ids.size(); i++) {
                paramsMap.put("label_ids["+String.valueOf(i)+"]",String.valueOf(label_ids.get(i)));
            }
        }
        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        } else {
            paramsMap.put("city_id", LoginManager.getInstance().getTrackcityid());
        }
        return paramsMap;
    }
}

