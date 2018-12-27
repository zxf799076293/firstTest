package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.CommunityInfoActivity;
import com.linhuiba.business.activity.CouponReceiveCentreActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.IntegralReceiveCouponsActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MyCouponsActivity;
import com.linhuiba.business.activity.MySelfPushMessageActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.activity.UseCouponsActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.commonsdk.debug.E;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyCouponsAdapter extends BaseQuickAdapter<MyCouponsModel, BaseViewHolder> {
    private Context mContext;
    private List<MyCouponsModel> mDatas;
    private int type;//1:我的优惠券，2：详情，3：使用优惠券，4：积分兑换优惠券，5:领券中心
    private int expired;
    private int used;
    private IntegralReceiveCouponsActivity mIntegralReceiveCouponsActivity;
    private MyCouponsActivity mMyCouponsActivity;
    private UseCouponsActivity mUseCouponsActivity;
    private FieldInfoActivity mFieldInfoActivity;
    private CommunityInfoActivity mCommunityInfoActivity;
    private CouponReceiveCentreActivity mCouponReceiveCentreActivity;
    private int is_received;
    public static HashMap<Integer,String> couponsNumMap = new HashMap<>();
    public static HashMap<Integer,Boolean> useCouponsMap = new HashMap<>();
    public HashMap<Integer,Integer> mCouponRemindMap = new HashMap<>();
    private Drawable mDrawable;
    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context, CouponReceiveCentreActivity activity,int type) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.mCouponReceiveCentreActivity = activity;
        mDrawable = mContext.getResources().getDrawable(R.drawable.ic_shijian_three_nine);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        getCouponRemindMap();
    }
    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context, int type, FieldInfoActivity activity,
                            int is_received) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.mFieldInfoActivity = activity;
        this.is_received = is_received;
    }
    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context, int type, CommunityInfoActivity activity,
                            int is_received) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.mCommunityInfoActivity = activity;
        this.is_received = is_received;
    }


    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context,int type, UseCouponsActivity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.mUseCouponsActivity = activity;
    }

    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context, int type, int expired, int used, MyCouponsActivity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.expired = expired;
        this.used = used;
        this.mMyCouponsActivity = activity;
    }
    public MyCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                            Context context, int type, IntegralReceiveCouponsActivity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.type = type;
        this.mDatas = data;
        this.mIntegralReceiveCouponsActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyCouponsModel item) {
        //需要显示隐藏的
        TextView notConvertTV = (TextView) helper.getView(R.id.coupons_item_default_view_tv);
        TextView timeTV = (TextView) helper.getView(R.id.coupons_item_time_tv);
        LinearLayout integralConvertChooseNumLL = (LinearLayout) helper.getView(R.id.coupons_item_integral_convert_num_all_ll);//积分兑换的
        RelativeLayout couponCentreReceiveRL = (RelativeLayout) helper.getView(R.id.coupon_centre_item_receive_rl);//领券中心
        //显示
        View firstView = (View)helper.getView(R.id.coupons_first_view);
        View lastView = (View)helper.getView(R.id.coupons_last_view);
        LinearLayout itemAllLL = (LinearLayout) helper.getView(R.id.coupons_item_bg_ll);
        LinearLayout itemShowLL = (LinearLayout) helper.getView(R.id.coupons_item_show_ll);
        TextView couponsPriceTV = (TextView) helper.getView(R.id.coupons_item_price_tv);
        TextView resPriceTV = (TextView) helper.getView(R.id.coupons_item_res_price_tv);
        TextView nameTV = (TextView) helper.getView(R.id.coupons_item_name);
        TextView restrictTV = (TextView) helper.getView(R.id.coupons_item_restrict_tv);
        TextView convertIntegralTV = (TextView) helper.getView(R.id.coupons_item_integral_convert_integral_tv);
        TextView downTV = (TextView) helper.getView(R.id.coupons_item_integral_convert_down_tv);
        final EditText couponsNumET = (EditText) helper.getView(R.id.coupons_item_integral_convert_num_et);
        TextView addTV = (TextView) helper.getView(R.id.coupons_item_integral_convert_add_tv);
        TextView convertNumBtnTV = (TextView) helper.getView(R.id.coupons_item_integral_convert_num_btn_tv);
        //最右边ll需要隐藏的
        LinearLayout statusOrUsedLL = (LinearLayout) helper.getView(R.id.coupons_item_status_ll);
        LinearLayout integralConvertUsedLL = (LinearLayout) helper.getView(R.id.coupons_item_integral_convert_used_ll);//积分需要的
        LinearLayout integralConvertLL = (LinearLayout) helper.getView(R.id.coupons_item_integral_convert_ll);
        CheckBox integralConvertCB = (CheckBox) helper.getView(R.id.coupons_item_convert_use_cb);
        //最右边ll show
        ImageView statusImgv = (ImageView) helper.getView(R.id.coupons_item_use_status_imgv);//状态
        TextView couponsUseBtnTV = (TextView) helper.getView(R.id.coupons_item_use_btn_tv);
        TextView couponsUseIntegralTV = (TextView) helper.getView(R.id.coupons_item_integral_tv);
        TextView integralConvertBtnTV = (TextView) helper.getView(R.id.coupons_item_integral_convert_used_btn_tv);
        //领券中心
        LinearLayout couponCentreReceiveLL = (LinearLayout) helper.getView(R.id.coupon_centre_item_receive_ll);
        ImageView couponCentreStatusImgv = (ImageView) helper.getView(R.id.coupon_centre_item_use_status_imgv);
        TextView couponCentreStartDateTV = (TextView) helper.getView(R.id.coupons_item_start_date_tv);
        TextView couponCentreStartTimeTV = (TextView) helper.getView(R.id.coupons_item_start_tiem_tv);
        TextView couponCentreReceiveTV = (TextView) helper.getView(R.id.coupon_centre_receive_btn_tv);
        LinearLayout couponCentreReceiveTVLL = (LinearLayout) helper.getView(R.id.coupon_centre_receive_btn_ll);
        TextView couponCentreCountDownTV = (TextView) helper.getView(R.id.coupons_centre_item_count_down_tv);

        if (helper.getLayoutPosition() == 0) {
            if (type != 2) {
                firstView.setVisibility(View.VISIBLE);
            } else {
                firstView.setVisibility(View.GONE);
            }
            lastView.setVisibility(View.VISIBLE);
        } else {
            firstView.setVisibility(View.GONE);
            lastView.setVisibility(View.VISIBLE);
        }
        if (type == 1 || type == 2 || type == 3) {
            notConvertTV.setVisibility(View.VISIBLE);
            integralConvertChooseNumLL.setVisibility(View.GONE);
            couponsUseBtnTV.setVisibility(View.GONE);
            itemShowLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_youhiquan_three_seven));
            if (type != 3) {
                statusOrUsedLL.setVisibility(View.VISIBLE);
                integralConvertUsedLL.setVisibility(View.GONE);
                if (type == 1) {
                    if (expired == 2) {
                        statusImgv.setVisibility(View.VISIBLE);
                        statusImgv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable._three_sevenic_youhuiquan_guoqi));
                        itemShowLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_youhuiquan_gary_three_seven));
                    } else {
                        if (used == 2) {
                            statusImgv.setVisibility(View.VISIBLE);
                            statusImgv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_youhuiquan_shiyong_three_seven));
                            itemShowLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_youhuiquan_gary_three_seven));
                        } else {
                            couponsUseBtnTV.setVisibility(View.VISIBLE);
                            if (item.getExpired() == 1) {
                                statusImgv.setVisibility(View.VISIBLE);
                                statusImgv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable._three_sevenic_jijiangguoqi));
                            } else {
                                statusImgv.setVisibility(View.INVISIBLE);
                            }
                            couponsUseBtnTV.setOnClickListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    Intent searchListIntent = new Intent(mMyCouponsActivity,SearchListActivity.class);
                                    if (item.getScope() != 1) {
                                        if (item.getValue() != null && item.getValue().size() > 0) {
                                            if (item.getField().equals("physical_resource_id")) {
                                                Intent fieldinfo = new Intent(mMyCouponsActivity, FieldInfoActivity.class);
                                                fieldinfo.putExtra("good_type", 1);
                                                fieldinfo.putExtra("fieldId", String.valueOf(item.getValue().get(0)));
                                                mMyCouponsActivity.startActivity(fieldinfo);
                                                return;
                                            } else {
                                                ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
                                                apiResourcesModel.setOrder_by("weight_score");
                                                apiResourcesModel.setOrder("desc");
                                                int cityId = 90;
                                                if (LoginManager.getInstance().getTrackcityid().length() > 0) {
                                                    cityId = Integer.parseInt(LoginManager.getInstance().getTrackcityid());
                                                }
                                                ArrayList<Integer> city_ids = new ArrayList<>();
                                                city_ids.add(cityId);
                                                apiResourcesModel.setCity_ids(city_ids);
                                                apiResourcesModel.setPage("1");
                                                apiResourcesModel.setPage_size(4);
                                                if (item.getField().equals("community_type_ids")) {
                                                    apiResourcesModel.setCommunity_type_ids(item.getValue());
                                                } else if (item.getField().equals("label_ids")) {
                                                    apiResourcesModel.setLabel_ids(item.getValue());
                                                }
                                                searchListIntent.putExtra("is_home_page",4);
                                                searchListIntent.putExtra("ApiResourcesModel",(Serializable) apiResourcesModel);
                                            }
                                        } else {
                                            MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupons_my_coupons_no_community));
                                            return;
                                        }
                                    }
                                    mMyCouponsActivity.startActivity(searchListIntent);
                                }
                            });
                        }
                    }
                } else if (type == 2) {
                    if (is_received == 1) {
                        couponsUseBtnTV.setVisibility(View.VISIBLE);
                        statusImgv.setVisibility(View.INVISIBLE);
                        if (item.getMethod() == 1) {
                            couponsUseBtnTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_pw_receive_coupons));
                        } else if (item.getMethod() == 3) {
                            couponsUseBtnTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_pw_integral_recceive_coupons));
                        }
                        couponsUseBtnTV.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                if (mFieldInfoActivity != null) {
                                    if (LoginManager.isLogin()) {
                                        if (item.getMethod() == 1) {
                                            mFieldInfoActivity.isReceiveClick = true;
                                        }
                                        mFieldInfoActivity.showIntegerDialog(item.getMethod(),String.valueOf(item.getPoint()),item.getId());
                                    } else {
                                        Intent loginIntent = new Intent(mFieldInfoActivity,LoginActivity.class);
                                        mFieldInfoActivity.startActivity(loginIntent);
                                    }
                                } else if (mCommunityInfoActivity != null) {
                                    if (LoginManager.isLogin()) {
                                        if (item.getMethod() == 1) {
                                            mCommunityInfoActivity.isReceiveClick = true;
                                        }
                                        mCommunityInfoActivity.showIntegerDialog(item.getMethod(),String.valueOf(item.getPoint()),item.getId());
                                    } else {
                                        Intent loginIntent = new Intent(mCommunityInfoActivity,LoginActivity.class);
                                        mCommunityInfoActivity.startActivity(loginIntent);
                                    }
                                }
                            }
                        });
                    } else if (is_received == 2) {
                        statusOrUsedLL.setVisibility(View.VISIBLE);
                        integralConvertUsedLL.setVisibility(View.GONE);
                        statusImgv.setVisibility(View.VISIBLE);
                        statusImgv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_three_sevenic_youhuiquan_yilingqu));
                    }


                }
            } else {
                statusOrUsedLL.setVisibility(View.GONE);
                integralConvertUsedLL.setVisibility(View.VISIBLE);
                if (item.getReceive() != 1 && item.getId() == 0 && item.getPoint() != null) {
                    integralConvertLL.setVisibility(View.VISIBLE);
                    integralConvertCB.setVisibility(View.GONE);
                    integralConvertBtnTV.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            mUseCouponsActivity.showIntegerDialog(String.valueOf(item.getPoint()),item.getCoupon_id(),item);
                        }
                    });
                } else {
                    integralConvertLL.setVisibility(View.GONE);
                    integralConvertCB.setVisibility(View.VISIBLE);
                    integralConvertCB.setChecked(useCouponsMap.get(item.getId()));
                    checkboxClick(integralConvertCB,item);
                }
                couponsUseIntegralTV.setText(String.valueOf(item.getPoint()));
            }
        } else if (type == 4) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(mContext,123));
            itemAllLL.setLayoutParams(paramgroups);
            itemShowLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_three_sevenimage_youhuiquan_integral_receive));
            notConvertTV.setVisibility(View.GONE);
            integralConvertChooseNumLL.setVisibility(View.VISIBLE);
            statusOrUsedLL.setVisibility(View.GONE);
            integralConvertUsedLL.setVisibility(View.GONE);
            convertIntegralTV.setText(String.valueOf(item.getPoint()));
            downTV.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (Integer.parseInt(couponsNumET.getText().toString()) > 0) {
                        couponsNumMap.put(item.getId(),String.valueOf(Integer.parseInt(couponsNumET.getText().toString()) - 1));
                        couponsNumET.setText(couponsNumMap.get(item.getId()));
                    }
                }
            });
            addTV.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (item.getPoint() != null && item.getPoint() > 0) {
                        if (item.getPoint() * Integer.parseInt(couponsNumET.getText().toString()) +
                                item.getPoint() >
                                Integer.parseInt(LoginManager.getInstance().getConsumption_point())) {
                        } else {
                            couponsNumMap.put(item.getId(),String.valueOf(Integer.parseInt(couponsNumET.getText().toString()) + 1));
                            couponsNumET.setText(couponsNumMap.get(item.getId()));
                        }
                    }
                }
            });
            if (item.getPoint() != null &&
                    item.getPoint() > 0 && LoginManager.getInstance().getConsumption_point() != null &&
                    LoginManager.getInstance().getConsumption_point().length() > 0) {
                if (couponsNumET.getTag() instanceof TextWatcher) {
                    couponsNumET.removeTextChangedListener((TextWatcher) (couponsNumET.getTag()));
                }
                // 第2步：移除TextWatcher之后，设置EditText的Text。
                couponsNumET.setText(couponsNumMap.get(item.getId()));


                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().length() > 0) {
                            if (item.getPoint() * Double.parseDouble(editable.toString()) >
                                    Double.parseDouble(LoginManager.getInstance().getConsumption_point())) {
                                MessageUtils.showToast(mContext.getResources().getString(R.string.module_integral_recceive_coupons_insufficient));
                                couponsNumMap.put(item.getId(),"0");
                                couponsNumET.setText("0");
                            } else {
                                couponsNumMap.put(item.getId(),couponsNumET.getText().toString());
                            }
                        } else {
                            couponsNumMap.put(item.getId(),"0");
                            couponsNumET.setText("0");
                        }
                    }
                };
                couponsNumET.addTextChangedListener(watcher);
                couponsNumET.setTag(watcher);
            }
            convertNumBtnTV.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (Double.parseDouble(couponsNumET.getText().toString()) > 0 &&
                            item.getId() > 0 && item.getPoint() != null) {
                        //弹窗提示
                        mIntegralReceiveCouponsActivity.showIntegerDialog(Constants.getpricestring(
                                String.valueOf(item.getPoint()),Double.parseDouble(couponsNumET.getText().toString())
                        ),item.getId(),couponsNumET.getText().toString());
                    }
                }
            });
        } else if (type == 5) {
            notConvertTV.setVisibility(View.VISIBLE);
            integralConvertChooseNumLL.setVisibility(View.GONE);
            statusOrUsedLL.setVisibility(View.GONE);
            integralConvertUsedLL.setVisibility(View.GONE);
            couponCentreReceiveRL.setVisibility(View.VISIBLE);
            itemShowLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_youhiquan_three_seven));
            couponCentreStatusImgv.setVisibility(View.GONE);
            couponCentreReceiveLL.setVisibility(View.GONE);
            couponCentreStartDateTV.setVisibility(View.GONE);
            couponCentreStartTimeTV.setVisibility(View.GONE);
            couponCentreReceiveTVLL.setVisibility(View.GONE);
            couponCentreReceiveTV.setCompoundDrawables(null, null, null, null);
            if (item.getStatus() == 1) {//进行中
                couponCentreReceiveLL.setVisibility(View.VISIBLE);
                couponCentreReceiveTVLL.setVisibility(View.VISIBLE);
                // FIXME: 2018/12/11 判断是否领取
                couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_pw_receive_coupons));
                couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        // FIXME: 2018/12/11 立即领取
                        if (LoginManager.isLogin()) {
                            if (item.getAccount_limit() != null &&
                                    item.getUser_coupons_count() >= item.getAccount_limit()) {
                                MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupon_centre_received_no_have));
                            } else {
                                mCouponReceiveCentreActivity.mCouponsMvpPresenter.receiveCoupons(item.getId(),1);
                            }
                        } else {
                            Intent intent = new Intent(mCouponReceiveCentreActivity, LoginActivity.class);
                            mCouponReceiveCentreActivity.startActivity(intent);
                        }
                    }
                });
            } else if (item.getStatus() == 2) {//即将结束
                couponCentreStatusImgv.setVisibility(View.VISIBLE);
                couponCentreReceiveLL.setVisibility(View.VISIBLE);
                couponCentreReceiveTVLL.setVisibility(View.VISIBLE);
                couponCentreStatusImgv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_jijaingjieshu_three_nine));
                // FIXME: 2018/12/11 判断是否领取
                couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_pw_receive_coupons));
                couponCentreReceiveTVLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
                couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (LoginManager.isLogin()) {
                            if (item.getAccount_limit() != null &&
                                    item.getUser_coupons_count() >= item.getAccount_limit()) {
                                MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupon_centre_received_no_have));
                            } else {
                                mCouponReceiveCentreActivity.mCouponsMvpPresenter.receiveCoupons(item.getId(),1);
                            }
                        } else {
                            Intent intent = new Intent(mCouponReceiveCentreActivity, LoginActivity.class);
                            mCouponReceiveCentreActivity.startActivity(intent);
                        }
                    }
                });
            } else if (item.getStatus() == 3) {//即将开始
                couponCentreReceiveLL.setVisibility(View.VISIBLE);
                couponCentreReceiveTVLL.setVisibility(View.VISIBLE);
                couponCentreStartDateTV.setVisibility(View.VISIBLE);
                couponCentreStartTimeTV.setVisibility(View.VISIBLE);
                couponCentreReceiveTVLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
                if (item.getShelf_time() != null && item.getShelf_time().length() > 0) {
                    String [] arr = item.getShelf_time().split("\\s+");
                    if (arr.length == 2) {
                        couponCentreStartDateTV.setText(arr[0]);
                        couponCentreStartTimeTV.setText(arr[1] +
                                mContext.getResources().getString(R.string.module_coupon_centre_receive_start));
                    }
                }
                // FIXME: 2018/12/12
                MyCouponsModel timeDownBean = mDatas.get(helper.getLayoutPosition());
                if (timeDownBean.getRemind_time() > 1) {
                    long useTime = timeDownBean.getRemind_time();
                    // FIXME: 2018/12/21 倒计时显示
                    if (useTime < 1800) {
                        couponCentreCountDownTV.setText(com.linhuiba.linhuifield.connector.Constants.getFormatTime(useTime * 1000,3));
                    }
                    if (mCouponRemindMap.get(item.getId()) != null &&
                            mCouponRemindMap.get(item.getId()) == 1) {
                        couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_coupon_centre_cancel_remind));
                        couponCentreReceiveTV.setCompoundDrawables(null, null, null, null);
                        couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                // FIXME: 2018/12/11 日历取消提醒
                                mCouponReceiveCentreActivity.cancelRemind(item.getTitle() +
                                        mContext.getResources().getString(R.string.module_coupon_centre_title)+
                                        String.valueOf(item.getId()));
                                mCouponRemindMap.put(item.getId(),0);
                                setCouponRemindMap();
                                MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupon_centre_receive_remind_cancel));
                            }
                        });

                    } else if (item.getCoupon_remind() == 0) {
                        couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_coupon_centre_remind_me));
                        couponCentreReceiveTV.setCompoundDrawables(mDrawable, null, null, null);
                        couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                // FIXME: 2018/12/11 日历提醒
                                mCouponReceiveCentreActivity.setRemindClick(item.getId(),item.getTitle() +
                                        mContext.getResources().getString(R.string.module_coupon_centre_title)+
                                        String.valueOf(item.getId()),item.getShelf_time());
                            }
                        });
                    }
                } else {
                    // FIXME: 2018/12/11 倒计时测试
                    couponCentreReceiveTV.setCompoundDrawables(null, null, null, null);
                    couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_fieldinfo_pw_receive_coupons));
                    couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            if (LoginManager.isLogin()) {
                                if (item.getAccount_limit() != null &&
                                        item.getUser_coupons_count() >= item.getAccount_limit()) {
                                    MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupon_centre_received_no_have));
                                } else {
                                    mCouponReceiveCentreActivity.mCouponsMvpPresenter.receiveCoupons(item.getId(),1);
                                }
                            } else {
                                Intent intent = new Intent(mCouponReceiveCentreActivity, LoginActivity.class);
                                mCouponReceiveCentreActivity.startActivity(intent);
                            }
                        }
                    });
                }
            } else if (item.getStatus() == 4) {//已领完
                couponCentreReceiveLL.setVisibility(View.VISIBLE);
                couponCentreReceiveTVLL.setVisibility(View.VISIBLE);
                couponCentreReceiveTV.setText(mContext.getResources().getString(R.string.module_coupon_centre_no_have));
                couponCentreReceiveTVLL.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
                couponCentreReceiveTVLL.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                        MessageUtils.showToast(mContext.getResources().getString(R.string.module_coupon_centre_receive_finished));
                    }
                });

            }
        }
        if (item.getAmount() != null && item.getAmount().length() > 0) {
            couponsPriceTV.setText(
                    Constants.getpricestring(item.getAmount(),1));
            if (item.getMin_goods_amount() != null && item.getMin_goods_amount().length() > 0) {
                if (Double.parseDouble(item.getMin_goods_amount()) > 0) {
                    resPriceTV.setText(
                            mContext.getResources().getString(R.string.module_coupons_first_register_item_amount_first_str)
                                    + Constants.getpricestring(item.getMin_goods_amount(),1) +
                                    mContext.getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                    Constants.getpricestring(item.getAmount(),1));
                } else {
                    resPriceTV.setText(
                            mContext.getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
                }
            } else {
                resPriceTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
        } else {
            couponsPriceTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
            resPriceTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (item.getTitle() != null && item.getTitle().length() > 0) {
            nameTV.setText(item.getTitle());
        } else {
            nameTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (item.getHint() != null && item.getHint().length() > 0) {
            restrictTV.setText(item.getHint());
        } else {
            restrictTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (item.getEffective_start_time() != null && item.getEffective_start_time().length() > 0 &&
                item.getEffective_end_time() != null && item.getEffective_end_time().length() > 0 ) {
            timeTV.setText(item.getEffective_start_time() +
            mContext.getResources().getString(R.string.module_my_coupons_item_time_str) +
                    item.getEffective_end_time());
        } else {
            String str = "";
            if (item.getMethod() == 1) {
                str = mContext.getResources().getString(R.string.module_fieldinfo_receive);
            } else if (item.getMethod() == 3) {
                str = mContext.getResources().getString(R.string.module_coupons_item_exchange_hint);
                timeTV.setText(mContext.getResources().getString(R.string.module_coupons_item_exchange_hint)
                        + mContext.getResources().getString(R.string.module_coupons_item_time_hint));
            }
            if (item.getRelative_time() != null &&
                    item.getRelative_time().length() > 0) {
                timeTV.setText(str
                        + mContext.getResources().getString(R.string.module_coupons_item_time_hint) +
                        item.getRelative_time() +
                        mContext.getResources().getString(R.string.module_coupons_item_time_hint_end));
            } else {
                timeTV.setText(str +
                        mContext.getResources().getString(R.string.module_coupons_item_time_hint) +
                        "30" +
                        mContext.getResources().getString(R.string.module_coupons_item_time_hint_end));
            }
        }
    }
    public static HashMap<Integer, String> getCouponsNumMap() {
        return couponsNumMap;
    }

    public static void setCouponsNumMap(HashMap<Integer, String> couponsNumMap) {
        MyCouponsAdapter.couponsNumMap = couponsNumMap;
    }
    public static void clearCouponsNumMap() {
        if (couponsNumMap != null) {
            couponsNumMap.clear();
        }
    }

    public static HashMap<Integer, Boolean> getUseCouponsMap() {
        return useCouponsMap;
    }

    public static void setUseCouponsMap(HashMap<Integer, Boolean> useCouponsMap) {
        MyCouponsAdapter.useCouponsMap = useCouponsMap;
    }
    public static void clearUseCouponsMap() {
        if (useCouponsMap != null) {
            useCouponsMap.clear();
        }
    }
    private void checkboxClick(final CheckBox checkBox, final MyCouponsModel item) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选中优惠券后的操作
                if (!useCouponsMap.get(item.getId())) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).getId() == item.getId()) {
                            useCouponsMap.put(item.getId(),true);
                        } else {
                            useCouponsMap.put(item.getId(),false);
                        }
                    }
                    notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.putExtra("type",2);
                    intent.putExtra("model",(Serializable) item);
                    intent.putExtra("res_id",mUseCouponsActivity.mResId);
                    if (mUseCouponsActivity.intentCouponsId > 0) {
                        intent.putExtra("coupon_id",mUseCouponsActivity.intentCouponsId);
                    }
                    mUseCouponsActivity.setResult(2,intent);
                    mUseCouponsActivity.finish();
                } else {
                    // 取消选中优惠券后的操作
                    checkBox.setChecked(true);
                }
            }
        });
    }
    public void getCouponRemindMap() {
        String remindStr =
        LoginManager.getInstance().getCoupon_remind();
        if (remindStr != null && remindStr.length() > 0) {
            mCouponRemindMap = (HashMap<Integer, Integer>) JSON.parseObject(remindStr, Map.class);
        }
    }
    public void setCouponRemindMap() {
        LoginManager.getInstance().setCoupon_remind(JSON.toJSONString(mCouponRemindMap));
    }
}
