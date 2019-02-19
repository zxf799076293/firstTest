package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.TheFirstRegisterCouponsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TheFirstRegisterCouponsActivity extends BaseMvpActivity implements CouponsMvpView {
    @InjectView(R.id.first_register_coupons_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.first_register_coupons_btn_ll)
    LinearLayout mGetCouponsLL;
    @InjectView(R.id.first_register_coupons_btn_tv)
    TextView mGetCouponsTV;
    @InjectView(R.id.first_register_activity_regulation_ll)
    LinearLayout mRegulationLL;
    @InjectView(R.id.first_register_coupons_no_data_ll)
    LinearLayout mFirstRegisterCouponsNoDataLL;
    private CouponsMvpPresenter mCouponsMvpPresenter;
    private List<MyCouponsModel> mMyCouponsModels = new ArrayList<>();
    private TheFirstRegisterCouponsAdapter mAdapter;
    private DisplayMetrics mDisplayMetrics;
    private Bitmap mShareBitmap;
    private Dialog mShareDialog;
    private IWXAPI mIWXAPI;
    private CustomDialog mShowDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_the_first_register_coupons);
        initView();
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
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_coupons_first_register_title));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.popup_ic_share), new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //share
                share();
            }
        });
        mGetCouponsTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //一键领取功能
                if (LoginManager.isLogin()) {
                    ArrayList<Integer> ids = new ArrayList<>();
                    for (int i = 0; i < mMyCouponsModels.size(); i++) {
                        ids.add(mMyCouponsModels.get(i).getId());
                    }
                    showProgressDialog();
                    mCouponsMvpPresenter.exchangeCoupons(ids);
                } else {
                    Intent loginIntent = new Intent(TheFirstRegisterCouponsActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
        mRegulationLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //活动规则
                showGiftSuccessDialog(2);
            }
        });
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mIWXAPI = WXAPIFactory.createWXAPI(this, com.linhuiba.business.connector.Constants.APP_ID);
        mIWXAPI.registerApp(com.linhuiba.business.connector.Constants.APP_ID);
        mGetCouponsLL.setVisibility(View.GONE);
    }
    private void initData() {
        mCouponsMvpPresenter.getCouponsGifts(0);
    }
    @Override
    public void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        mMyCouponsModels = data;
        if (mMyCouponsModels!= null && mMyCouponsModels.size() > 0) {
            mGetCouponsLL.setVisibility(View.VISIBLE);
            int a = mMyCouponsModels.size() % 2  + mMyCouponsModels.size()/2;
            if (mDisplayMetrics.heightPixels - Constants.Dp2Px(this,405+ 65 * a) > 0) {
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        mDisplayMetrics.heightPixels - Constants.Dp2Px(this,405) + 30);
                mRecyclerView.setLayoutParams(params);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(TheFirstRegisterCouponsActivity.this,2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mAdapter = new TheFirstRegisterCouponsAdapter(R.layout.module_recycle_item_first_register_coupons,
                    mMyCouponsModels,TheFirstRegisterCouponsActivity.this,
                    TheFirstRegisterCouponsActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
        } else {
            mFirstRegisterCouponsNoDataLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMyCouponsMoreSuccess(ArrayList<MyCouponsModel> data) {

    }

    @Override
    public void onMyCouponsListFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onMyCouponsListMoreFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onExchangeCouponsSuccess(Response response) {
        showGiftSuccessDialog(1);
    }

    private void share() {
        if (mShareBitmap == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(Config.COUPONS_SHARE_URL);
                    mShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(mShareBitmap);
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            if (mShareDialog != null && mShareDialog.isShowing()) {
                mShareDialog.dismiss();
            }
            mShareDialog = new AlertDialog.Builder(TheFirstRegisterCouponsActivity.this).create();
            View myView = TheFirstRegisterCouponsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(TheFirstRegisterCouponsActivity.this,
                    Config.COUPONS_SHARE_URL);
            constants.shareWXMiniPopupWindow(TheFirstRegisterCouponsActivity.this,myView,mShareDialog,mIWXAPI, com.linhuiba.business.config.Config.Domain_Name +
                            com.linhuiba.business.config.Config.WX_SHARE_GIFT_URL,
                    getResources().getString(R.string.module_coupons_first_register_share_title),
                    getResources().getString(R.string.module_coupons_first_register_share_description),
                    mShareBitmap,com.linhuiba.business.config.Config.WX_MINI_SHARE_GIFT_URL,
                    mShareBitmap,getResources().getString(R.string.module_coupons_first_register_share_title));
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mShareDialog != null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                    }
                    mShareDialog = new AlertDialog.Builder(TheFirstRegisterCouponsActivity.this).create();
                    View myView = TheFirstRegisterCouponsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(TheFirstRegisterCouponsActivity.this,
                            Config.COUPONS_SHARE_URL);
                    constants.shareWXMiniPopupWindow(TheFirstRegisterCouponsActivity.this,myView,mShareDialog,mIWXAPI, com.linhuiba.business.config.Config.Domain_Name +
                                    com.linhuiba.business.config.Config.WX_SHARE_GIFT_URL,
                            getResources().getString(R.string.module_coupons_first_register_share_title),
                            getResources().getString(R.string.module_coupons_first_register_share_description),
                            mShareBitmap, com.linhuiba.business.config.Config.WX_MINI_SHARE_GIFT_URL,
                            mShareBitmap,getResources().getString(R.string.module_coupons_first_register_share_title));
                    break;
                default:
                    break;
            }
        }

    };

    /**
     *
     * @param type 1:领取成功 2：优惠券规则
     */
    private void showGiftSuccessDialog(int type) {
        if (mShowDialog == null || !mShowDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            mShowDialog.dismiss();
                            Intent myCouponsIntent = new Intent(TheFirstRegisterCouponsActivity.this,MyCouponsActivity.class);
                            startActivity(myCouponsIntent);
                            finish();
                            break;
                        case R.id.btn_cancel:
                            mShowDialog.dismiss();
                            break;
                        case R.id.dialog_long_btn_tv:
                            mShowDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            int isBtnShow;
            int isTitleShow;
            int isOneBtnShow;
            String title = "";
            String content = "";
            if (type == 1) {
                isBtnShow = View.VISIBLE;
                isTitleShow = View.GONE;
                isOneBtnShow = View.GONE;
                content = getResources().getString(R.string.module_coupons_first_register_exchange_success);
                title = "";
            } else {
                isBtnShow = View.GONE;
                isTitleShow = View.VISIBLE;
                isOneBtnShow = View.VISIBLE;
                content = getResources().getString(R.string.module_coupons_first_register_exchange_success_content);
                title = getResources().getString(R.string.module_coupons_first_register_activity_regulation);
            }
            CustomDialog.Builder builder = new CustomDialog.Builder(TheFirstRegisterCouponsActivity.this);
            mShowDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .addViewOnclick(R.id.dialog_long_btn_tv,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            content)
                    .setText(R.id.dialog_content_tv,
                            content)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.home_exclusive_show_btntext))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.confirm))
                    .setText(R.id.dialog_title_msg_tv,title)
                    .showView(R.id.btn_cancel,View.VISIBLE)
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,isTitleShow)
                    .showView(R.id.dialog_long_btn_tv,isOneBtnShow)
                    .showView(R.id.dialog_title_textview,isBtnShow)
                    .showView(R.id.dialog_content_tv,isOneBtnShow)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(TheFirstRegisterCouponsActivity.this,mShowDialog);
            mShowDialog.show();
        }
    }
}
