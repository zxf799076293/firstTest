package com.linhuiba.business.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.fragment.SelfSupportShopFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/10/16.
 */

public class SelfSupportShopActivity extends BaseMvpActivity {
    @InjectView(R.id.selfsupportshop_tabhost)
    FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    public int mResTypeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfsupportshop);
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
        mTabHost.setup(this, getSupportFragmentManager(), R.id.selfsupportshop_realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(SelfSupportShopActivity.this.getResources().getString(R.string.selfsupportshop_title_str)).setIndicator(view);
        mTabHost.addTab(tabSpec, SelfSupportShopFragment.class, null);
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("res_type_id") != null) {
            mResTypeId = intent.getExtras().getInt("res_type_id");
        }
    }
}
