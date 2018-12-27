package com.linhuiba.business.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.HomeDynamicCommunityModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeModuleBarAdapter  extends BaseQuickAdapter<HomeDynamicCommunityModel, BaseViewHolder> {
    private Context mContext;
    private List<HomeDynamicCommunityModel> mDatas;
    private HomeFragment mHomeFragment;
    private int width;
    private int height;
    private int type;//1：类目动态 2：导航
    public HomeModuleBarAdapter(int layoutResId, @Nullable List<HomeDynamicCommunityModel> data,
                                       Context context, HomeFragment homeFragment,int type) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        this.mHomeFragment = homeFragment;
        DisplayMetrics metric = new DisplayMetrics();
        mHomeFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDynamicCommunityModel item) {
        LinearLayout mHomeModuleBarLL = (LinearLayout)helper.getView(R.id.home_bar_item_ll);
        ImageView mHomeModuleBarImav = (ImageView)helper.getView(R.id.home_bar_item_imgv);
        TextView mHomeModuleBarTV = (TextView)helper.getView(R.id.home_bar_item_tv);
        View mHomeModuleBarFirstView = (View)helper.getView(R.id.home_bar_item_first_view);
        View mHomeModuleBarLastView = (View)helper.getView(R.id.home_bar_item_last_view);
        if (type == 2) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(((width)* 200 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                    (width* 200 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 136 / 200);
            mHomeModuleBarLL.setLayoutParams(paramgroups);
            mHomeModuleBarFirstView.setVisibility(View.VISIBLE);
            if (helper.getLayoutPosition() == mDatas.size() - 1) {
                mHomeModuleBarLastView.setVisibility(View.VISIBLE);
            } else {
                mHomeModuleBarLastView.setVisibility(View.GONE);
            }
            mHomeModuleBarTV.setTextSize(14);
            mHomeModuleBarTV.getPaint().setFakeBoldText(true);//加粗
            mHomeModuleBarImav.setImageResource(item.getDrawable());
            mHomeModuleBarTV.setText(item.getName());
        } else if (type == 1) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(width/4 -
                    Constants.Dp2Px(
                            mContext,6), Constants.Dp2Px(
                    mContext,70));
            mHomeModuleBarLL.setLayoutParams(paramgroups);
            mHomeModuleBarLL.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mHomeModuleBarFirstView.setVisibility(View.VISIBLE);
            if (item.getPic_url() != null && item.getPic_url().length() > 0) {
                Picasso.with(mContext).load(item.getPic_url() + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).
                        resize(Constants.Dp2Px(mContext,28), Constants.Dp2Px(mContext,28)).into(mHomeModuleBarImav);
            } else {
                Picasso.with(mContext).load(R.drawable.ic_else_threesixone).resize(Constants.Dp2Px(mContext,28), Constants.Dp2Px(mContext,28)).placeholder(R.drawable.ic_else_threesixone).error(R.drawable.ic_else_threesixone).into(mHomeModuleBarImav);
            }
            if (item.getTitle() != null) {
                mHomeModuleBarTV.setText(item.getTitle());
                mHomeModuleBarTV.setTextSize(12);
                mHomeModuleBarTV.setPadding(0,8,0,0);
            }
        }
    }
}
