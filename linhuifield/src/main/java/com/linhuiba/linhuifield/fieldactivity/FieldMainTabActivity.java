package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fragment.Field_FieldListFragmentnew;
import com.linhuiba.linhuifield.fragment.Field_HomeFragment;
import com.linhuiba.linhuifield.fragment.Field_MyseflFragment;
import com.linhuiba.linhuifield.fragment.Field_OrdersFragment;
import com.linhuiba.linhuifield.fragment.Field_PropertyMyselfFragment;
import com.linhuiba.linhuifield.fragment.Field_ResourceCalendarFragment;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class FieldMainTabActivity extends FieldBaseMvpActivity implements TabHost.OnTabChangeListener{
    public FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    //定义数组来存放Fragment界面fall_orders
    private Class fragmentArray[] = new Class[] {Field_HomeFragment.class,Field_OrdersFragment.class,Field_ResourceCalendarFragment.class,Field_PropertyMyselfFragment.class};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = new int[] {R.drawable.tab_add_home_btn,R.drawable.field_activity_maintab_ordersmanage_bg,
            R.drawable.field_activity_maintab_resources_schedule_bg, R.drawable.tab_add_myself_btn};
    //Tab选项卡的文字
    private int mTextviewArray[] = new int[] {R.string.tab_txt_Home, R.string.btn_maintab_field_deal,
            R.string.field_maintab_resources_calendar, R.string.field_maintab_myselfinfo };
//    public TextView[] maintop_prompt = new TextView[4];
    public int OrderFragment_currIndex = -1;
    private long resumeUptime;
    public boolean isHomeRefreshOrder;
    public boolean isOrderRefreshOrder;
    public boolean isNewIntentRefreshOrder;
    public QBadgeView[] fieldMainBV = new QBadgeView[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_maintab);
        initview();
    }
    private void initview() {
        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost)findViewById(R.id.field_tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.field_realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(FieldMainTabActivity.this.getResources().getString(mTextviewArray[i])).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        Intent fieldintent = getIntent();
        if (fieldintent.getExtras() != null) {
            String fieldintnetstr = fieldintent.getStringExtra("new_tmpintent");
            if (fieldintnetstr != null) {
                if (fieldintnetstr.equals("fieldlist")) {
                    mTabHost.setCurrentTab(0);
                } else if (fieldintnetstr.equals("fieldorder")) {
                    mTabHost.setCurrentTab(1);
                } else if (fieldintnetstr.equals("myself")) {
                    mTabHost.setCurrentTab(3);
                }
            }
            if (fieldintent.getExtras().get("all_orders") != null && fieldintent.getExtras().getInt("all_orders") > 0) {
                fieldMainBV[1].setBadgeNumber(fieldintent.getExtras().getInt("all_orders"));
            } else {
                Field_FieldApi.getbadge_info(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),badge_infoHandler);
            }
            if (fieldintent.getExtras().get("OrderFragment_currIndex") != null && fieldintent.getExtras().getInt("OrderFragment_currIndex") > 0) {
                OrderFragment_currIndex = fieldintent.getExtras().getInt("OrderFragment_currIndex");
                mTabHost.setVisibility(View.GONE);
            }
        }
        if (LoginManager.getInstance().getUMmsg_start_app() != null && LoginManager.getInstance().getUMmsg_start_app().length() > 0) {
            String data = LoginManager.getInstance().getUMmsg_start_app();
            Constants.pushUrlJumpActivity(data,FieldMainTabActivity.this);
            LoginManager.getInstance().setUMmsg_start_app("");
        }
    }
    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.activity_field_tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.field_tabitem_imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.field_tabitem_textview);
        textView.setText(this.getResources().getString(mTextviewArray[index]));
        LinearLayout tab_all_ll = (LinearLayout) view.findViewById(R.id.field_tab_item_all_ll);
        fieldMainBV[index] = new QBadgeView(FieldMainTabActivity.this.getContext());
        fieldMainBV[index].bindTarget(tab_all_ll).setBadgeTextSize(10,true).setBadgeGravity(Gravity.END|Gravity.TOP).setGravityOffset(26,0,true).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color)).setShowShadow(false);
        return view;
    }
    private LinhuiAsyncHttpResponseHandler badge_infoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data.toString().length() > 0 && JSONObject.parseObject(data.toString()) != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("all_orders") != null && jsonObject.get("all_orders").toString().length() > 0) {
                    if (Double.parseDouble(jsonObject.get("all_orders").toString()) > 0) {
                        fieldMainBV[1].setBadgeNumber(Integer.parseInt(jsonObject.get("all_orders").toString()));
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };

    @Override
    public void onTabChanged(String tabId) {
        for (int i =0;i<mTabHost.getTabWidget().getChildCount() ;i++){
            TextView textView = (TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.field_tabitem_textview);
            if (this.getResources().getString(mTextviewArray[i]).equals(tabId)){
                textView.setTextColor(getResources().getColor(R.color.default_bluebg ));
            }else{
                textView.setTextColor(getResources().getColor(R.color.register_edit_color ));
            }
        }
    }

    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Intent fieldintent = getIntent();
        String fieldintnetstr = fieldintent.getStringExtra("new_tmpintent");
        if (fieldintnetstr != null) {
            if (fieldintnetstr.equals("fieldlist")) {
                mTabHost.setCurrentTab(0);
            } else if (fieldintnetstr.equals("fieldorder")) {
                if (fieldintent.getExtras().get("OrderFragment_currIndex") != null && fieldintent.getExtras().getInt("OrderFragment_currIndex") > 0) {
                    OrderFragment_currIndex = fieldintent.getExtras().getInt("OrderFragment_currIndex");
                }
                mTabHost.setCurrentTab(1);
                isNewIntentRefreshOrder = true;
            } else if (fieldintnetstr.equals("myself")) {
                mTabHost.setCurrentTab(3);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LoginManager.isRight_to_publish() || LoginManager.isIs_supplier()) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - resumeUptime) > 2000) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.app_exit));
                    resumeUptime = System.currentTimeMillis();
                } else {
                    Intent maintabintent = new Intent("com.business.MainTabActivity");
                    maintabintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    maintabintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    maintabintent.putExtra("new_tmpintent", "app_exit");
                    startActivity(maintabintent);
                    this.finish();
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
