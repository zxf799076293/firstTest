package com.linhuiba.business.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class IntegralReceiveCouponsActivity extends BaseMvpActivity implements CouponsMvpView,
        SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.integral_recceive_coupons_srf)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.integral_recceive_coupons_rv)
    RecyclerView mCouponsRV;
    @InjectView(R.id.integral_recceive_coupons_no_data_rv)
    RelativeLayout mNullDataRL;
    @InjectView(R.id.integral_receive_integral_tv)
    TextView mIntegralTV;
    private List<MyCouponsModel> mLists = new ArrayList();
    private MyCouponsAdapter mCouponsAdapter;
    public CouponsMvpPresenter mCouponsMvpPresenter;
    private int mPage;
    private CustomDialog mIntegralDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_integral_receive_coupons);
        initView();
        showProgressDialog();
        mPage = 1;
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
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_integral_recceive_coupons_title));
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
                        Intent  couponsIntent = new Intent(IntegralReceiveCouponsActivity.this,AboutUsActivity.class);
                        couponsIntent.putExtra("type", com.linhuiba.business.config.Config.INTEGRAL_EXCHANGE_COUPON_REGULATION_WEB_INT);
                        startActivity(couponsIntent);
                    }
                });
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mIntegralTV.setText(LoginManager.getInstance().getConsumption_point());
    }
    private void initData() {
        if (mCouponsAdapter == null) {
            mCouponsMvpPresenter.getCouponsExchangeList(mPage);
        } else {
            if (mSwipeRefreshLayout.isShown()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    @OnClick({
            R.id.integral_receive_integral_ll
    })
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.integral_receive_integral_ll:
                Intent about_intrgral = new Intent(this,AboutUsActivity.class);
                about_intrgral.putExtra("type", Config.POINT_INFO_WEB_INT);
                startActivity(about_intrgral);
                break;
            default:
                break;
        }
    }
    @Override
    public void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        if (mSwipeRefreshLayout.isShown()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLists != null) {
            mLists.clear();
        }
        if (mCouponsAdapter == null) {
            mLists = data;
        } else {
            mLists.addAll(data);
        }
        if (mLists != null &&
                mLists.size() > 0) {
            for (int i = 0; i < mLists.size(); i++) {
                if (LoginManager.getInstance().getConsumption_point().length() > 0) {
                    MyCouponsAdapter.getCouponsNumMap().put(mLists.get(i).getId(),
                            String.valueOf(Integer.parseInt(LoginManager.getInstance().getConsumption_point()) /
                                    mLists.get(i).getPoint()));
                } else {
                    MyCouponsAdapter.getCouponsNumMap().put(mLists.get(i).getId(),"0");
                }
            }
            mNullDataRL.setVisibility(View.GONE);
            if (mCouponsAdapter == null) {
                mCouponsAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,mLists,IntegralReceiveCouponsActivity.this,
                        4,IntegralReceiveCouponsActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mCouponsRV.setLayoutManager(linearLayoutManager);
                mCouponsRV.setAdapter(mCouponsAdapter);
                mCouponsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
            } else {
                mCouponsAdapter.notifyDataSetChanged();
            }
            mCouponsAdapter.loadMoreComplete();
            mCouponsAdapter.loadMoreEnd();
        } else {
            mNullDataRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMyCouponsMoreSuccess(ArrayList<MyCouponsModel> data) {
        List<MyCouponsModel> mList = new ArrayList<>();
        mList = data;
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                mLists.add(mList.get(i));
                if (LoginManager.getInstance().getConsumption_point().length() > 0) {
                    MyCouponsAdapter.getCouponsNumMap().put(mList.get(i).getId(),
                            String.valueOf(Integer.parseInt(LoginManager.getInstance().getConsumption_point()) /
                                    mList.get(i).getPoint()));
                } else {
                    MyCouponsAdapter.getCouponsNumMap().put(mList.get(i).getId(),"0");
                }
            }
            mCouponsAdapter.notifyDataSetChanged();
            mCouponsAdapter.loadMoreComplete();
            if (mLists.size() < 10) {
                mCouponsAdapter.loadMoreEnd();
            }
        } else {
            mCouponsAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onMyCouponsListFailure(boolean superresult, Throwable error) {
        if (mSwipeRefreshLayout.isShown()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onMyCouponsListMoreFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onExchangeCouponsSuccess(Response response) {
        if (response.code == 1) {
            if (response.data != null && response.data.toString().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
                if (jsonObject.get("consumption_point") != null &&
                        jsonObject.get("consumption_point").toString().length() > 0) {
                    LoginManager.getInstance().setConsumption_point(jsonObject.get("consumption_point").toString());
                    mIntegralTV.setText(LoginManager.getInstance().getConsumption_point());
                }
            }
            showProgressDialog();
            mPage = 1;
            mCouponsAdapter = null;
            initData();
        } else {
            MessageUtils.showToast(response.msg);
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mCouponsAdapter = null;
        initData();
    }
    public void showIntegerDialog(String point, final int coupon_id, final String coupons_count) {
        if (mIntegralDialog == null || !mIntegralDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            showProgressDialog();
                            mCouponsMvpPresenter.receiveCoupons(coupon_id,Integer.parseInt(coupons_count));
                            mIntegralDialog.dismiss();
                            break;
                        case R.id.btn_cancel:
                            mIntegralDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(IntegralReceiveCouponsActivity.this);
            mIntegralDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.module_coupons_integral_receive_dialog_msg_first) + point
                                    +getResources().getString(R.string.module_coupons_integral_receive_dialog_msg_second) + coupons_count +
                                    getResources().getString(R.string.module_coupons_integral_receive_dialog_msg_third))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(IntegralReceiveCouponsActivity.this,mIntegralDialog);
            mIntegralDialog.show();
        }
    }

}
