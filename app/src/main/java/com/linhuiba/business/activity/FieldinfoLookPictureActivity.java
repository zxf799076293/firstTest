package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.HomeDynamicCommunityAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FieldinfoLookPictureActivity extends BaseMvpActivity {
    @InjectView(R.id.fieldinfo_look_picture_tl)
    TabLayout mPictureTL;
    @InjectView(R.id.fieldinfo_look_picture_vp)
    ViewPager mPictureVP;
    private ArrayList<View> mListViews;
    private int mViewSize = 2;
    private RecyclerView[] mRecyclerViews = new RecyclerView[mViewSize];
    private RelativeLayout[] mNullDataRL = new RelativeLayout[mViewSize];
    private LinearLayout[] mNoDataLL = new LinearLayout[mViewSize];
    private List<ResourceSearchItemModel>[] mLists = new ArrayList[mViewSize];
    public int mCurrIndex = 0;// 当前页卡编号
    private int mTabTextViewList[] = {R.string.module_fieldinfo_look_pic_phy_tl_title, R.string.module_fieldinfo_look_pic_case_tl_title};
    private HomeDynamicCommunityAdapter[] mAdapters = new HomeDynamicCommunityAdapter[mViewSize];
    private Dialog zoom_picture_dialog;//详情页预览大图dialog
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private List<ImageView> mImageViewList;
    private DisplayMetrics mDisplayMetrics;
    private ArrayList<String> mPicList = new ArrayList<String>();
    private int mPhyEndInt;//展位图片最后一张是第几个
    private String mTitle = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_fieldinfo_look_picture);
        initView();
        initData();
    }
    private void initView() {
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("pic_list") != null) {
            mPicList = (ArrayList<String>) intent.getExtras().get("pic_list");
        }
        if (intent.getExtras() != null && intent.getExtras().get("phy_end") != null) {
            mPhyEndInt =  intent.getExtras().getInt("phy_end");
        }
        if (intent.getExtras() != null && intent.getExtras().get("phy_name") != null) {
            mTitle = intent.getExtras().getString("phy_name");
            TitleBarUtils.setTitleText(this, intent.getExtras().getString("phy_name"));
        }
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        TitleBarUtils.showBackImg(this,true);
        mLists[1] = new ArrayList<>();
        mLists[0] = new ArrayList<>();
        for (int i = 0; i < mPicList.size(); i++) {
            ResourceSearchItemModel resourceSearchItemModel = new ResourceSearchItemModel();
            resourceSearchItemModel.setPic_url(mPicList.get(i));
            if (mPhyEndInt > 0) {
                if (mPicList.size() > mPhyEndInt) {
                    if (i > mPhyEndInt - 1) {
                        mLists[1].add(resourceSearchItemModel);
                    } else {
                        mLists[0].add(resourceSearchItemModel);
                    }
                } else {
                    mLists[0].add(resourceSearchItemModel);
                }
            } else {
                mLists[0].add(resourceSearchItemModel);
            }
        }
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        if (mPhyEndInt > 0) {
            mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
            if (mPicList.size() > mPhyEndInt) {
                mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
            }
        } else {
            mListViews.add(inflater.inflate(R.layout.module_app_recycle_view, null));
        }
        for(int i = 0 ;i < mListViews.size(); i++ ) {
            SwipeRefreshLayout mSwipeRL = (SwipeRefreshLayout)mListViews.get(i).findViewById(R.id.app_swipe_refresh);
            mSwipeRL.setEnabled(false);
            mRecyclerViews[i]  = (RecyclerView) mListViews.get(i).findViewById(R.id.app_recycler_view);
            mNullDataRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.app_no_data_ll);
            TextView textView = (TextView) mListViews.get(i).findViewById(R.id.my_msg_no_data_tv);
            textView.setText(getResources().getString(R.string.module_coupon_centre_no_data));
            mNoDataLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.my_coupons_no_data_ll);
            mNoDataLL[i].setVisibility(View.VISIBLE);
        }
        mPictureVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        mPictureVP.setOnPageChangeListener(new PagerChangeListener());
        mPictureTL.post(new Runnable() {
            @Override
            public void run() {
                com.linhuiba.linhuifield.connector.Constants.setIndicator(mPictureTL,10,10);
            }
        });
        mPictureTL.setupWithViewPager(mPictureVP);
        for (int i = 0; i < mPictureTL.getTabCount(); i++) {
            if (i == 0) {
                mPictureTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
            } else if (i == 1) {
            }
        }
        if (mPhyEndInt > 0) {
            if (mPicList.size() > mPhyEndInt) {
                mPictureTL.getTabAt(0).setText(getResources().getString(mTabTextViewList[0]) +
                        "(" + String.valueOf(mPhyEndInt) + ")");
                mPictureTL.getTabAt(1).setText(getResources().getString(mTabTextViewList[1]) +
                        "(" + String.valueOf(mPicList.size() - mPhyEndInt) + ")");
            } else {
                mPictureTL.getTabAt(0).setText(getResources().getString(mTabTextViewList[0]) +
                        "(" + String.valueOf(mPicList.size()) + ")");
            }
        } else {
            mPictureTL.getTabAt(0).setText(getResources().getString(mTabTextViewList[1]) +
                    "(" + String.valueOf(mPicList.size()) + ")");
        }
    }
    private void initData() {
        if (mAdapters[mCurrIndex] == null) {
            mAdapters[mCurrIndex] = new HomeDynamicCommunityAdapter(R.layout.fragment_homeactivity_item,mLists[mCurrIndex],FieldinfoLookPictureActivity.this,
                    true);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(FieldinfoLookPictureActivity.this,2);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerViews[mCurrIndex].setLayoutManager(linearLayoutManager);
            mRecyclerViews[mCurrIndex].setAdapter(mAdapters[mCurrIndex]);
            mRecyclerViews[mCurrIndex].setNestedScrollingEnabled(false);
            mAdapters[mCurrIndex].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    // FIXME: 2018/12/17 预览大图
                    if (mCurrIndex == 0) {
                        showZoomPicDialog(position);
                    } else {
                        showZoomPicDialog(mPhyEndInt + position);
                    }
                }
            });
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
                    if (mAdapters[mCurrIndex] == null) {
                        initData();
                    }
                    break;
                case 1:
                    if (mAdapters[mCurrIndex] == null) {
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
    private void showZoomPicDialog(int position) {
        View myView = FieldinfoLookPictureActivity.this.getLayoutInflater().inflate(R.layout.module_dialog_fieldinfo_look_price, null);
        zoom_picture_dialog = new AlertDialog.Builder(FieldinfoLookPictureActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
        // FIXME: 2018/12/17 预览图片
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_back_tv);
        TextView mShowPictureTitleTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_title_tv);
        ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.fieldinfo_look_pic_zoom_dialog_viewpage);
        LinearLayout mShowPicSizeLL = (LinearLayout)myView.findViewById(R.id.fieldinfo_look_pic_size_ll);
        final LinearLayout mShowPicstatementLL = (LinearLayout)myView.findViewById(R.id.fieldinfo_look_pic_statement_ll);
        final TextView mShowPicstatementConfirmTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_statement_confirm_tv);
        final TextView mShowPicstatementTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_statement_tv);
        final TextView mShowPicPhyTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_phy_tv);
        final TextView mShowPicCaseTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_case_tv);
        final TextView mShowPicNumTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_num_tv);
        final TextView mShowPicSizeTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_size_tv);
        final ImageButton mShowPicallImgBtn = (ImageButton)myView.findViewById(R.id.fieldinfo_look_pic_show_all_imgBtn);
        mShowPicSizeTV.setText("/"+String.valueOf(mPicList.size()));
        mShowPictureTitleTV.setText(mTitle);
        int width_new = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height_new = width_new * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        int margintop = mDisplayMetrics.heightPixels / 2 + (height_new / 2 + com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldinfoLookPictureActivity.this,75 - 30 - 64));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mShowPicSizeLL.getLayoutParams());
        lp.setMargins(0, margintop, com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldinfoLookPictureActivity.this,12), 0);
        mShowPicSizeLL.setLayoutParams(lp);
        mShowPicPhyTV.setVisibility(View.GONE);
        mShowPicCaseTV.setVisibility(View.GONE);
        if (mPhyEndInt > 0) {
            mShowPicPhyTV.setVisibility(View.VISIBLE);
            if (mPicList.size() > mPhyEndInt) {
                mShowPicCaseTV.setVisibility(View.VISIBLE);
            }
        } else {
            mShowPicCaseTV.setVisibility(View.VISIBLE);
        }
        if (position > mPhyEndInt - 1) {
            mShowPicCaseTV.setTextColor(getResources().getColor(R.color.white));
            mShowPicCaseTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
            mShowPicPhyTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
            mShowPicPhyTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
        } else {
            mShowPicPhyTV.setTextColor(getResources().getColor(R.color.white));
            mShowPicPhyTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
            mShowPicCaseTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
            mShowPicCaseTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
        }
        mshowpicture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_picture_dialog.dismiss();
            }
        });
        mShowPicstatementTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mShowPicstatementLL.setVisibility(View.VISIBLE);
            }
        });

        mShowPicstatementConfirmTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mShowPicstatementLL.setVisibility(View.GONE);
            }
        });
        mShowPicallImgBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                zoom_picture_dialog.dismiss();
            }
        });
        if (mIsRefreshZoomImageview) {
            mImageViewList = new ArrayList<>();
            for (int i = 0; i < mPicList.size(); i++) {
                ZoomImageView imageView = new ZoomImageView(
                        getApplicationContext());
                Picasso.with(this).load(mPicList.get(i).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView);
                mImageViewList.add(imageView);
            }
            ZoomImageView imageView2 = new ZoomImageView(
                    getApplicationContext());
            Picasso.with(this).load(mPicList.get(mPicList.size() - 1).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView2);
            mImageViewList.add(0,imageView2);
            ZoomImageView imageView1 = new ZoomImageView(
                    getApplicationContext());
            Picasso.with(this).load(mPicList.get(0).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView1);
            mImageViewList.add(imageView1);
            mIsRefreshZoomImageview = false;
        }
        if (mImageViewList != null && mImageViewList.size() > 0) {
            com.linhuiba.linhuifield.connector.Constants.showFieldinfoPic(mImageViewList,mzoom_viewpage,position,
                    mShowPicNumTV,mShowPicPhyTV,mShowPicCaseTV,mPhyEndInt,FieldinfoLookPictureActivity.this,
                    mShowPicstatementLL);
        }
    }

}
