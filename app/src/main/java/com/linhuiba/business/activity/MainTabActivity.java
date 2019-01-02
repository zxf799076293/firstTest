package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fragment.CommoditypayFragment;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.mlink.MLinkCallback;
import cn.magicwindow.mlink.YYBCallback;
import cn.magicwindow.mlink.annotation.MLinkDefaultRouter;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2016/3/1.
 */
//2017/12/13 魔窗
@MLinkDefaultRouter
public class MainTabActivity extends BaseMvpActivity implements TabHost.OnTabChangeListener {
    public LinearLayout mSearchStatusBarLL;

    //定义FragmentTabHost对象
    public FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class, SearchListFragment.class, CommoditypayFragment.class, MyselfFragment.class};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_add_home_btn, R.drawable.activity_maintab_self_support_img_btn_bg,R.drawable.tab_add_commoditypay_btn,R.drawable.tab_add_myself_btn};

    //Tab选项卡的文字
    private int mTextviewArray[] = {R.string.tab_txt_Home, R.string.tab_revise_fields_str,
            R.string.tab_txt_shopping, R.string.tab_txt_my};

    private InputMethodManager mManager;

    AsyncTask mEnterTask;
    private String fieldintnetstr;
//    public TextView[] maintop_prompt = new TextView[4];
    private Intent fieldintent;
    public int restart = -1;
    private Uri mLinkData;
    public QBadgeView[] MainBV = new QBadgeView[4];
    public boolean isClickTab;
    public int mNotchHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintab);
        initView();
    }
    @Override
    public void onStop() {
        super.onStop();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(MainTabActivity.this.getContext())) {
                restart = 0;
            } else {
                restart = 1;
            }

        } else if (screen == false) {
            restart = 0;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(MainTabActivity.this.getContext())) {
                restart = 0;
            } else {
                restart = 1;
            }

        } else if (screen == false) {
            restart = 0;
        }

    }
    @Override
    public void onResume() {
        super.onResume();  // 调用父类方法
        MobclickAgent.onResume(this);
        if (!LoginManager.isLogin()) {
            MainBV[2].hide(false);
            MainBV[3].hide(false);
        } else if (LoginManager.isLogin()) {
            if (restart ==  1) {
                FieldApi.getshopcart_itemscount(MyAsyncHttpClient.MyAsyncHttpClient(), getShoppingCartCountHandler);
                FieldApi.getUnreadNoticedCount(MyAsyncHttpClient.MyAsyncHttpClient(), getUnreadHandler);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fieldintnetstr != null && fieldintnetstr.length()> 0 &&
                fieldintnetstr.equals("app_exit")) {
            LoginManager.getInstance().setFieldexit(1);
        }
    }

    private void initView() {
        setSteepStatusBar();
        int stausBarHeight = getSteepStatusBarHeight();
        if (stausBarHeight > 0) {
            mNotchHeight = stausBarHeight;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置后键盘弹出会好点
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        mSearchStatusBarLL = (LinearLayout)findViewById(R.id.main_status_bar_ll);
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(MainTabActivity.this.getResources().getString(mTextviewArray[i])).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        hideKeyboard();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.getTopView(), InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(this.getTopView().getWindowToken(), 0);
        if (LoginManager.isLogin()) {
            FieldApi.getshopcart_itemscount(MyAsyncHttpClient.MyAsyncHttpClient(), getShoppingCartCountHandler);
            FieldApi.getUnreadNoticedCount(MyAsyncHttpClient.MyAsyncHttpClient(), getUnreadHandler);
        } else {
            MainBV[2].hide(false);
            MainBV[3].hide(false);
        }
        //2017/12/14 魔窗
        register(this);
//        MLinkAPIFactory.createAPI(this).deferredRouter();//来实现场景还原。
        mLinkData = getIntent().getData();
        if (mLinkData != null) {
            MLinkAPIFactory.createAPI(MainTabActivity.this).router(mLinkData);
            //跳转后结束当前activity
        } else {
            //动画等耗时操作结束后再调用checkYYB(),一般写在starActivity前即可
            MLinkAPIFactory.createAPI(MainTabActivity.this)
                    .checkYYB(MainTabActivity.this, new YYBCallback() {
                        @Override
                        public void onFailed(Context context) {
                        }
                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }
    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(this.getResources().getString(mTextviewArray[index]));
        LinearLayout tab_all_ll = (LinearLayout) view.findViewById(R.id.main_tab_item_all_ll);
        MainBV[index] = new QBadgeView(MainTabActivity.this.getContext());
        MainBV[index].bindTarget(tab_all_ll).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeTextSize(10,true).setGravityOffset(26,0,true).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color)).setShowShadow(false);
        return view;
    }
    @Override
    public void onTabChanged(String tabId) {
        isClickTab = true;
        for (int i =0;i<mTabHost.getTabWidget().getChildCount() ;i++){
            TextView textView = (TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.textview);
            if (this.getResources().getString(mTextviewArray[i]).equals(tabId)){
                textView.setTextColor(getResources().getColor(R.color.default_bluebg ));
            } else{
                textView.setTextColor(getResources().getColor(R.color.register_edit_color ));
            }
        }
    }
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                mManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    long backtime = 0L;
    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - backtime < 2000) {
            if (!isFinishing()) {
                super.onBackPressed();
            }
        }
        MessageUtils.showToast(getResources().getString(R.string.module_maintab_back_msg));
        backtime = System.currentTimeMillis();
    }
    private LinhuiAsyncHttpResponseHandler getShoppingCartCountHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null) {
                com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(data.toString());
                if (jsonobject.get("count") != null) {
                    if (jsonobject.get("count").toString().length() != 0) {
                        if (!(jsonobject.get("count").toString().equals("0"))) {
                            MainBV[2].setBadgeNumber(Integer.parseInt(jsonobject.get("count").toString()));
                        } else {
                            MainBV[2].hide(false);
                        }
                    } else {
                        MainBV[2].hide(false);
                    }
                } else {
                    MainBV[2].hide(false);
                }
            } else {
                MainBV[2].hide(false);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private LinhuiAsyncHttpResponseHandler getUnreadHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null) {
                com.alibaba.fastjson.JSONObject jsonobject = JSON.parseObject(data.toString());
                if (jsonobject.get("message_noticed") != null &&
                        jsonobject.get("message_noticed").toString().length() > 0 &&
                        Integer.parseInt(jsonobject.get("message_noticed").toString()) == 1) {
                    MainBV[3].setBadgeText(" ");
                } else {
                    MainBV[3].hide(false);
                }
            } else {
                MainBV[3].hide(false);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            MainBV[3].hide(false);
        }
    };
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        fieldintent = getIntent();
        if (fieldintent != null && fieldintent.getExtras() != null) {
            if (fieldintent.getExtras() != null &&
                    fieldintent.getExtras().get("mLinkInt") != null) {
                if (Integer.parseInt(fieldintent.getExtras().get("mLinkInt").toString()) == 1) {
                    if (mLinkData != null) {
                        MLinkAPIFactory.createAPI(MainTabActivity.this).router(mLinkData);
                        //跳转后结束当前activity
                    } else {
                        //动画等耗时操作结束后再调用checkYYB(),一般写在starActivity前即可
                        MLinkAPIFactory.createAPI(MainTabActivity.this)
                                .checkYYB(MainTabActivity.this, new YYBCallback() {
                                    @Override
                                    public void onFailed(Context context) {
                                    }
                                    @Override
                                    public void onSuccess() {
                                    }
                                });
                    }
                }
            } else {
                fieldintnetstr = fieldintent.getStringExtra("new_tmpintent");
                if (fieldintnetstr != null) {
                    if (fieldintnetstr.equals("goto_cartitems")) {
                        mTabHost.setCurrentTab(2);
                    } else if (fieldintnetstr.equals("goto_invoite")) {
                        Intent inviteintent = new Intent(this,InviteActivity.class);
                        startActivity(inviteintent);
                    } else if (fieldintnetstr.equals("goto_homepage")) {
                        mTabHost.setCurrentTab(0);
                    } else if (fieldintnetstr.equals("goto_myself")) {
                        mTabHost.setCurrentTab(3);
                        if (fieldintent.getExtras().get("goto_login") != null &&
                                fieldintent.getExtras().getInt("goto_login") == 1) {
                            Intent loginIntent = new Intent(this,LoginActivity.class);
                            loginIntent.putExtra("is_modity_pw",1);
                            startActivity(loginIntent);
                        }
                    } else if (fieldintnetstr.equals("app_exit")) {
                        LoginManager.getInstance().setFieldexit(1);
                        finish();
                    }
                }
            }
        }
    }
    //2017/12/13 魔窗
    private void register(Context context){
        MLinkAPIFactory.createAPI(context).registerDefault(new MLinkCallback() {
            @Override
            public void execute(Map paramMap, Uri uri, Context context) {
                //HomeActivity 为你的首页
                MLinkAPIFactory.createAPI(MainTabActivity.this).registerWithAnnotation(MainTabActivity.this);
            }
        });
// mLinkKey:  mLink 的 key, mLink的唯一标识
        MLinkAPIFactory.createAPI(context).register("resourceList", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                String link_jsonobject = Constants.getlisturl_jsonobject(URLDecoder.decode(paramMap.get("url_str").toString()),2);
                if (link_jsonobject != null) {
                    ApiResourcesModel apiResourcesModel = (ApiResourcesModel) JSONObject.parseObject(link_jsonobject,ApiResourcesModel.class);
                    if (apiResourcesModel.getCity_ids() != null &&
                            apiResourcesModel.getCity_ids().size() > 0) {
                        ArrayList<Integer> cityIds = new ArrayList<>();
                        cityIds.add(apiResourcesModel.getCity_ids().get(0));
                        apiResourcesModel.setCity_ids(cityIds);
                    } else {
                        ArrayList<Integer> cityList = new ArrayList();
                        cityList.add(90);
                        apiResourcesModel.setCity_ids(cityList);
                    }
                    Intent searchListIntent = new Intent(MainTabActivity.this,SearchListActivity.class);
                    searchListIntent.putExtra("is_home_page",4);
                    searchListIntent.putExtra("ApiResourcesModel",(Serializable) apiResourcesModel);
                    startActivity(searchListIntent);
                }
            }
        });
        MLinkAPIFactory.createAPI(context).register("activityCaseList", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                String urlStr = URLDecoder.decode(paramMap.get("url_str").toString());
                Intent caseIntent = new Intent(MainTabActivity.this,ActivityCaseActivity.class);
                if (!Constants.getnotices_urlstring(urlStr,"city_id").equals("-")) {
                    caseIntent.putExtra("city_id",Integer.parseInt(Constants.getnotices_urlstring(urlStr,"city_id")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"city_ids").equals("-")) {
                    caseIntent.putExtra("city_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"city_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"industry_ids").equals("-")) {
                    caseIntent.putExtra("industry_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"industry_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"spread_way_ids").equals("-")) {
                    caseIntent.putExtra("spread_way_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"spread_way_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"promotion_purpose_ids").equals("-")) {
                    caseIntent.putExtra("promotion_purpose_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"promotion_purpose_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"label_ids").equals("-")) {
                    caseIntent.putExtra("case_label_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"label_ids"),Integer.class));
                }
                if (!Constants.getnotices_urlstring(urlStr,"community_type_ids").equals("-")) {
                    caseIntent.putExtra("field_type_ids",
                            (Serializable)JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"community_type_ids"),Integer.class));
                }
                startActivity(caseIntent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("activityCaseDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                String urlStr = URLDecoder.decode(paramMap.get("url_str").toString());
                Intent caseIntent = new Intent(MainTabActivity.this,ActivityCaseInfoActivity.class);
                if (!Constants.getnotices_urlstring(urlStr,"city_id").equals("-")) {
                    caseIntent.putExtra("city_id",Integer.parseInt(Constants.getnotices_urlstring(urlStr,"city_id")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"city_ids").equals("-")) {
                    caseIntent.putExtra("city_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"city_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"industry_ids").equals("-")) {
                    caseIntent.putExtra("industry_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"industry_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"spread_way_ids").equals("-")) {
                    caseIntent.putExtra("spread_way_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"spread_way_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"promotion_purpose_ids").equals("-")) {
                    caseIntent.putExtra("promotion_purpose_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"promotion_purpose_ids")));
                }
                if (!Constants.getnotices_urlstring(urlStr,"label_ids").equals("-")) {
                    caseIntent.putExtra("label_ids",
                            (Serializable) JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"label_ids"),Integer.class));
                }
                if (!Constants.getnotices_urlstring(urlStr,"community_type_ids").equals("-")) {
                    caseIntent.putExtra("community_type_ids",
                            (Serializable)JSONArray.parseArray(Constants.getnotices_urlstring(urlStr,"community_type_ids"),Integer.class));
                }
                if (!Constants.getnotices_urlstring(urlStr,"caseId").equals("-")) {
                    caseIntent.putExtra("id",
                            Integer.parseInt(Constants.getnotices_urlstring(urlStr,"caseId")));
                }
                startActivity(caseIntent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("fieldsDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent fieldinfoIntent = new Intent(MainTabActivity.this, FieldInfoActivity.class);
                ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
                if (paramMap.get("url_str") != null && paramMap.get("url_str").toString().length() > 0) {
                    String link_jsonobject = Constants.getlisturl_jsonobject(URLDecoder.decode(paramMap.get("url_str").toString()),2);
                    apiResourcesModel = new ApiResourcesModel();
                    if (link_jsonobject != null) {
                        apiResourcesModel = (ApiResourcesModel) JSONObject.parseObject(link_jsonobject,ApiResourcesModel.class);
                    }
                    fieldinfoIntent.putExtra("model",(Serializable)apiResourcesModel);
                }
                if (paramMap.get("res_type_id") != null && paramMap.get("res_type_id").toString().length() > 0 &&
                        paramMap.get("res_type_id").equals("3")) {
                    fieldinfoIntent.putExtra("sell_res_id", paramMap.get("resource_id").toString());
                    fieldinfoIntent.putExtra("is_sell_res", true);
                } else {
                    fieldinfoIntent.putExtra("resource_id", paramMap.get("resource_id").toString());
                }
                startActivity(fieldinfoIntent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("communityDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent communityIntent = new Intent(MainTabActivity.this, CommunityInfoActivity.class);
                communityIntent.putExtra("id",Integer.parseInt(paramMap.get("id").toString()));
                communityIntent.putExtra("city_id",Integer.parseInt(paramMap.get("city_id").toString()));
                startActivity(communityIntent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("activitiesList", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent activityIntent = new Intent(MainTabActivity.this, SelfSupportShopActivity.class);
                activityIntent.putExtra("res_type_id",3);
                activityIntent.putExtra("city_id",paramMap.get("city_id").toString());
                startActivity(activityIntent);
            }
        });


        MLinkAPIFactory.createAPI(context).register("groupBuyList", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent groupBookingIntent = new Intent(MainTabActivity.this, GroupBookingMainActivity.class);
                groupBookingIntent.putExtra("city_id",paramMap.get("city_id").toString());
                startActivity(groupBookingIntent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("specialRequirement", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent aboutusintent = new Intent(MainTabActivity.this, AboutUsActivity.class);
                aboutusintent.putExtra("type", com.linhuiba.business.config.Config.ADD_DEMAND_WEB_INT);
                startActivity(aboutusintent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("thematicDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent aboutusintent = new Intent(MainTabActivity.this, AboutUsActivity.class);
                aboutusintent.putExtra("type", com.linhuiba.business.config.Config.THEMATIC_INFO_INT);
                aboutusintent.putExtra("resource_id",paramMap.get("resource_id").toString());
                startActivity(aboutusintent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("facilitatorDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent aboutusintent = new Intent(MainTabActivity.this, AboutUsActivity.class);
                aboutusintent.putExtra("type", com.linhuiba.business.config.Config.FACILITATOR_INFO_INT);
                aboutusintent.putExtra("resource_id",paramMap.get("resource_id").toString());
                startActivity(aboutusintent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("caseDetail", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent aboutusintent = new Intent(MainTabActivity.this, AboutUsActivity.class);
                aboutusintent.putExtra("type", com.linhuiba.business.config.Config.FACILITATOR_CASE_INFO_INT);
                aboutusintent.putExtra("resource_id",paramMap.get("resource_id").toString());
                startActivity(aboutusintent);
            }
        });
        MLinkAPIFactory.createAPI(context).register("couponReceiveCentre", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent aboutusintent = new Intent(MainTabActivity.this, CouponReceiveCentreActivity.class);
                startActivity(aboutusintent);
            }
        });

    }
}
