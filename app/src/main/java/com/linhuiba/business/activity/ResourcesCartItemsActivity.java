package com.linhuiba.business.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.fragment.CommoditypayFragment;
import com.linhuiba.business.fragment.SelfSupportShopFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ResourcesCartItemsActivity extends BaseMvpActivity {
    @InjectView(R.id.res_cartitem_tabhost)
    FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_cart_item);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        ButterKnife.inject(this);
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.res_cartitem_realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getResources().getString(R.string.tab_txt_shopping)).setIndicator(view);
        mTabHost.addTab(tabSpec, CommoditypayFragment.class, null);
    }
}