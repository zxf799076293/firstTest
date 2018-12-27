package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.List;

public class TheFirstRegisterCouponsAdapter extends BaseQuickAdapter<MyCouponsModel, BaseViewHolder> {
    private Context mContext;
    private List<MyCouponsModel> mDatas;
    private DisplayMetrics mDisplayMetrics;
    public TheFirstRegisterCouponsAdapter(int layoutResId, @Nullable List<MyCouponsModel> data,
                                          Context context, Activity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCouponsModel item) {
        LinearLayout mAllLL= (LinearLayout) helper.getView(R.id.first_register_coupons_item_ll);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(
                mDisplayMetrics.widthPixels/2 -  Constants.Dp2Px(mContext,40)  ,
                Constants.Dp2Px(mContext,57));
        mAllLL.setLayoutParams(params);
        if (item.getAmount() != null && item.getAmount().length() > 0) {
            helper.setText(R.id.first_register_coupons_price_tv,
                    Constants.getpricestring(item.getAmount(),1));
        } else {
            helper.setText(R.id.first_register_coupons_price_tv,
                    mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (item.getMin_goods_amount() != null && item.getMin_goods_amount().length() > 0) {
            helper.setText(R.id.first_register_coupons_res_price_tv,
                    mContext.getResources().getString(R.string.module_coupons_first_register_item_amount_first_str)
            + Constants.getpricestring(item.getMin_goods_amount(),1) +
                            mContext.getResources().getString(R.string.module_coupons_first_register_item_amount_last_str));
        } else {
            helper.setText(R.id.first_register_coupons_res_price_tv,
                    mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
    }
}
