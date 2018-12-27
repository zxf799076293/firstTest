package com.linhuiba.business.activity.searchcity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.linhuiba.business.R;

import java.util.List;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class MeituanAdapter extends CommonAdapter<MeiTuanBean> {
    private static final int RESULT_INT = 1;
    private static final int ADDFIELD_COMMUNITY_RESULT_INT = 3;
    private SearchCityActivity mActivity;
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas,
                          SearchCityActivity activity) {
        super(context, layoutId, datas);
        this.mActivity = activity;
    }

    @Override
    public void convert(final ViewHolder holder, final MeiTuanBean cityBean) {
        holder.setText(R.id.tvCity, cityBean.getName());
        // 2018/4/8 城市item点击
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", cityBean.getId());
                intent.putExtra("name",cityBean.getName());
                if (mActivity.mIsCommunitySearchCity == 1) {
                    mActivity.setResult(ADDFIELD_COMMUNITY_RESULT_INT,intent);
                } else {
                    mActivity.setResult(RESULT_INT,intent);
                }
                mActivity.finish();
            }
        });
    }
}