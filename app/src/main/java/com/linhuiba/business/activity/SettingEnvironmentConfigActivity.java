package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.debug.E;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingEnvironmentConfigActivity extends BaseMvpActivity {
    @InjectView(R.id.set_environment_recycle)
    RecyclerView mRecyclerView;
    private List<HashMap<String,String>> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_environment_config);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_setting_environment_title));
        mList = new ArrayList<>();
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("name",getResources().getString(R.string.module_setting_environment_pe));
        map1.put("url",Config.BASE_API_URL_PHP_PE);
        mList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name",getResources().getString(R.string.module_setting_environment_petest));
        map2.put("url",Config.BASE_API_URL_PHP_PE_TEST);
        mList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name",getResources().getString(R.string.module_setting_environment_se));
        map3.put("url",Config.BASE_API_URL_PHP_SE);
        mList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name",getResources().getString(R.string.module_setting_environment_ie));
        map4.put("url",Config.BASE_API_URL_PHP_IE);
        mList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name",getResources().getString(R.string.module_setting_environment_dell));
        map5.put("url",Config.BASE_API_URL_PHP_DETESTLL);
        mList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name",getResources().getString(R.string.module_setting_environment_dezr));
        map6.put("url",Config.BASE_API_URL_PHP_DETESTZR);
        mList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name",getResources().getString(R.string.module_setting_environment_dejlp));
        map7.put("url",Config.BASE_API_URL_PHP_DETESTJLP);
        mList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name",getResources().getString(R.string.module_setting_environment_dewap));
        map8.put("url",Config.BASE_API_URL_PHP_DETEWAP);
        mList.add(map8);
        Adapter adapter = new Adapter(R.layout.activity_search_sort_lv_item,mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP)) {
                    return;
                }
                ((RelativeLayout)view.findViewById(R.id.search_sort_item_rl)).setBackgroundColor(getResources().getColor(R.color.top_title_line_color));
                Intent cartsIntent = new Intent(SettingEnvironmentConfigActivity.this, MainTabActivity.class);
                cartsIntent.putExtra("new_tmpintent", "goto_myself");
                cartsIntent.putExtra("goto_login", 2);
                startActivity(cartsIntent);
                new Thread(){
                    public void run(){
                        //退出登录接口
                        LoginMvpModel.loginOut();
                        //退出登录时清空token
                        LoginManager.getInstance().clearLoginInfo();
                        //退出登录时清除公告信息
                        LoginManager.getInstance().setNoticesUrl("");
                        LoginManager.getInstance().setNoticescount(0);
                        LoginManager.getInstance().setNoticesid(0);
                        LoginManager.getInstance().setNoticesTitle("");
                        //友盟退出登录统计
                        MobclickAgent.onProfileSignOff();
                        if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_PE)) {
                            Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_PE;
                            Config.Domain_Name = Config.Domain_Name_PE;
                            com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_PE;
                            com.linhuiba.linhuifield.config.Config.Domain_Name = Config.Domain_Name_PE;
                        } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_PE_TEST)) {
                            Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_PE_TEST;
                            Config.Domain_Name = Config.Domain_Name_PE_TEST;
                            com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_PE_TEST;
                            com.linhuiba.linhuifield.config.Config.Domain_Name = Config.Domain_Name_PE_TEST;
                        } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_SE)) {
                            Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_SE;
                            Config.Domain_Name = Config.Domain_Name_SE;
                            com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_SE;
                            com.linhuiba.linhuifield.config.Config.Domain_Name = Config.Domain_Name_SE;
                        } else {
                            Config.Domain_Name = Config.Domain_Name_SE;
                            com.linhuiba.linhuifield.config.Config.Domain_Name = Config.Domain_Name_SE;
                            if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_IE)) {
                                Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_IE;
                                com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_IE;
                            } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_DETESTLL)) {
                                Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTLL;
                                com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTLL;
                            } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_DETESTZR)) {
                                Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTZR;
                                com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTZR;
                            } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_DETESTJLP)) {
                                Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTJLP;
                                com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_DETESTJLP;
                            } else if (mList.get(position).get("url").equals(Config.BASE_API_URL_PHP_DETEWAP)) {
                                Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_SE;
                                com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = Config.BASE_API_URL_PHP_SE;
                                Config.Domain_Name = Config.BASE_API_URL_PHP_DETEWAP;
                                com.linhuiba.linhuifield.config.Config.Domain_Name = Config.BASE_API_URL_PHP_DETEWAP;
                            }
                        }
                        LoginManager.setversion("0");
                        LoginManager.getInstance().cleanConfig_updatetime();
                    }
                }.start();
            }
        });
    }
    class Adapter extends BaseQuickAdapter<HashMap<String,String>, BaseViewHolder> {

        public Adapter(int layoutResId, @Nullable List<HashMap<String,String>> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HashMap<String,String> item) {
            ImageView mSearchSortItemImg = (ImageView)helper.getView(R.id.search_sort_item_img);
            TextView mSearchSortItemTV = (TextView)helper.getView(R.id.search_sort_item_tv);
            mSearchSortItemTV.setText(item.get("name"));
            if (item.get("url").equals(Config.BASE_API_URL_PHP)) {
                mSearchSortItemImg.setVisibility(View.VISIBLE);
            } else {
                mSearchSortItemImg.setVisibility(View.GONE);
            }
        }
    }
}
