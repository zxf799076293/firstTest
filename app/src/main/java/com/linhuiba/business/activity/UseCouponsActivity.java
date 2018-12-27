package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvppresenter.OrderConfirmMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.mvpview.OrderConfirmMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UseCouponsActivity extends BaseMvpActivity implements OrderConfirmMvpView,
        CouponsMvpView, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.use_coupons_srf)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.use_coupons_rv)
    RecyclerView mCouponsRV;
    @InjectView(R.id.use_coupons_no_data_rv)
    RelativeLayout mNullDataRL;
    @InjectView(R.id.use_coupons_cb)
    CheckBox mUseCouponsCB;
    private List<MyCouponsModel> mLists = new ArrayList();
    private MyCouponsAdapter mCouponsAdapter;
    public OrderConfirmMvpPresenter mOrderConfirmMvpPresenter;
    public CouponsMvpPresenter mCouponsMvpPresenter;
    private ArrayList<Integer> mCommunityIds;
    private ArrayList<Integer> mCouponsIds;
    private double price;
    private CustomDialog mIntegralDialog;
    private MyCouponsModel mCouponsModel;
    public int mResId;
    public int intentCouponsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_use_coupons);
        initView();
        showProgressDialog();
        initData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCouponsMvpPresenter != null) {
            mCouponsMvpPresenter.detachView();
        }
        if (mOrderConfirmMvpPresenter != null) {
            mOrderConfirmMvpPresenter.detachView();
        }
    }
    private void initView() {
        ButterKnife.inject(this);
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        mOrderConfirmMvpPresenter = new OrderConfirmMvpPresenter();
        mOrderConfirmMvpPresenter.attachView(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_use_coupons_title));
        TitleBarUtils.showBackImg(this, true);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("couponids") != null) {
            mCouponsIds = (ArrayList<Integer>) intent.getExtras().get("couponids");
        }
        if (intent.getExtras() != null && intent.getExtras().get("communityids") != null) {
            mCommunityIds = (ArrayList<Integer>) intent.getExtras().get("communityids");
        }
        if (intent.getExtras() != null && intent.getExtras().get("actual_fee") != null &&
                intent.getExtras().get("actual_fee").toString().length() > 0) {
            price = Double.parseDouble(intent.getExtras().get("actual_fee").toString());
        }
        if (intent.getExtras() != null && intent.getExtras().get("res_id") != null) {
            mResId = intent.getExtras().getInt("res_id");
        }
        if (intent.getExtras() != null && intent.getExtras().get("coupon_id") != null) {
            intentCouponsId = intent.getExtras().getInt("coupon_id");
        }
    }
    @OnClick({
            R.id.use_coupons_cb
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.use_coupons_cb:
                if (mUseCouponsCB.isChecked()) {
                    Intent intent = getIntent();
                    intent.putExtra("type",2);
                    if (mResId > 0) {
                        intent.putExtra("res_id",mResId);
                    }
                    if (intentCouponsId > 0) {
                        intent.putExtra("coupon_id",intentCouponsId);
                    }
                    setResult(2,intent);
                    finish();
                }
                break;
        }
    }
    private void initData() {
        if (mCouponsAdapter == null) {
            mOrderConfirmMvpPresenter.getOrderCommunityCoupons(mCommunityIds,mCouponsIds);
        } else {
            if (mSwipeRefreshLayout.isShown()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    @Override
    public void onRefresh() {
        mCouponsAdapter = null;
        initData();
    }

    @Override
    public void onDefaultAddressSuccess(AddressContactModel addressContactModel) {

    }

    @Override
    public void onDefaultAddressFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onUserProfileSuccess(Object data) {

    }

    @Override
    public void onUserProfileFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onCreateOrderPaySuccess(Object data) {

    }

    @Override
    public void onCreateOrderPayFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onOrderCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        if (mSwipeRefreshLayout.isShown()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mLists != null) {
            mLists.clear();
        }
        if (data.get(0).getCoupons() != null && data.get(0).getCoupons().size() > 0 && price > 0) {
            List<MyCouponsModel> mReceivedLists = new ArrayList<>();
            List<MyCouponsModel> mUnReceivedLists = new ArrayList<>();
            for (int i = 0; i < data.get(0).getCoupons().size(); i++) {
                if (data.get(0).getCoupons().get(i).getMin_goods_amount() == null ||
                        data.get(0).getCoupons().get(i).getMin_goods_amount().length() == 0 ||
                        Double.parseDouble(data.get(0).getCoupons().get(i).getMin_goods_amount()) == 0) {
                    if (price >= Double.parseDouble(data.get(0).getCoupons().get(i).getAmount()) * 100) {
                        if (data.get(0).getCoupons().get(i).getId() > 0) {
                            mReceivedLists.add(data.get(0).getCoupons().get(i));
                        } else {
                            mUnReceivedLists.add(data.get(0).getCoupons().get(i));
                        }
                    }
                } else {
                    if (price >= Double.parseDouble(data.get(0).getCoupons().get(i).getMin_goods_amount()) * 100) {
                        if (data.get(0).getCoupons().get(i).getId() > 0) {
                            mReceivedLists.add(data.get(0).getCoupons().get(i));
                        } else {
                            mUnReceivedLists.add(data.get(0).getCoupons().get(i));
                        }
                    }
                }
            }
            if (mUnReceivedLists.size() > 0) {
                mReceivedLists.add(0,mUnReceivedLists.get(0));
            }
            if (mCouponsAdapter == null) {
                mLists = mReceivedLists;
            } else {
                mLists.addAll(mReceivedLists);
            }
        }
        if (mLists != null &&
                mLists.size() > 0) {
            mNullDataRL.setVisibility(View.GONE);
            for (int i = 0; i < mLists.size(); i++) {
                if (intentCouponsId > 0 && intentCouponsId == mLists.get(i).getId()) {
                    MyCouponsAdapter.getUseCouponsMap().put(mLists.get(i).getId(),true);
                } else {
                    MyCouponsAdapter.getUseCouponsMap().put(mLists.get(i).getId(),false);
                }
            }
            if (mCouponsAdapter == null) {
                mCouponsAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,mLists,UseCouponsActivity.this,
                        3,UseCouponsActivity.this);
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
        } else {
            mNullDataRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderCouponsListFailure(boolean superresult, Throwable error) {
        if (mSwipeRefreshLayout.isShown()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onOrderNoticedSuccess() {

    }

    @Override
    public void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data) {

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
        //关闭刷新
        if (response.code == 1) {
            if (response.data != null && response.data.toString().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
                if (jsonObject.get("id") != null && jsonObject.get("consumption_point") != null &&
                        jsonObject.get("id").toString().length() > 0 &&
                        jsonObject.get("consumption_point").toString().length() > 0) {
                    LoginManager.getInstance().setConsumption_point(jsonObject.get("consumption_point").toString());
                    mCouponsModel.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    Intent intent = new Intent();
                    intent.putExtra("type",2);
                    intent.putExtra("model",(Serializable) mCouponsModel);
                    intent.putExtra("res_id",mResId);
                    if (intentCouponsId > 0) {
                        intent.putExtra("coupon_id",intentCouponsId);
                    }
                    setResult(2,intent);
                    finish();
                }
            }
        }
    }
    public void showIntegerDialog(String point, final int coupon_id, MyCouponsModel model) {
        mCouponsModel = model;
        if (mIntegralDialog == null || !mIntegralDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            showProgressDialog();
                            mCouponsMvpPresenter.receiveCoupons(coupon_id,1);
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
            CustomDialog.Builder builder = new CustomDialog.Builder(UseCouponsActivity.this);
            mIntegralDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.module_use_coupons_integral_receive_dialog_msg_first) + point
                                    +getResources().getString(R.string.module_use_coupons_integral_receive_dialog_msg_second))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,
                            getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(UseCouponsActivity.this,mIntegralDialog);
            mIntegralDialog.show();
        }
    }
}
