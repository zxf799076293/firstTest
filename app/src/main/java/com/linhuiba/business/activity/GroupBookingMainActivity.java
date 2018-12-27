package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.fragment.GroupBookingListFragment;
import com.linhuiba.business.fragment.GroupBookingMySelfFragment;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import cn.magicwindow.mlink.annotation.MLinkRouter;

/**
 * Created by Administrator on 2017/9/18.
 */
public class GroupBookingMainActivity extends BaseMvpActivity implements TabHost.OnTabChangeListener {
    //定义FragmentTabHost对象
    public FragmentTabHost mGroupBookingTabHost;

    //定义一个布局
    private LayoutInflater mLayoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {GroupBookingListFragment.class, GroupBookingMySelfFragment.class};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.activity_groupbooking_tab_home, R.drawable.activity_groupbooking_tab_myself};

    //Tab选项卡的文字
    private int mTextviewArray[] = {R.string.groupbooding_home_str, R.string.groupbooding_myself_str};
    public String mCityIdStr;
    private int mShowDialogInt;
    public int mCurrIndex = -1;
    private CustomDialog mShowFailureDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupbooking);
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("order_type") != null) {
                mCurrIndex = bundle.getInt("order_type");
            }
            if (bundle.get("showdialog") != null) {
                mShowDialogInt = bundle.getInt("showdialog");
            }
        }
        //实例化布局对象
        mLayoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mGroupBookingTabHost = (FragmentTabHost)findViewById(R.id.groupbooking_tabhost);
        mGroupBookingTabHost.setup(this, getSupportFragmentManager(), R.id.groupbooking_framelayout);

        mGroupBookingTabHost.getTabWidget().setDividerDrawable(null);
        mGroupBookingTabHost.setOnTabChangedListener(this);
        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mGroupBookingTabHost.newTabSpec(GroupBookingMainActivity.this.getResources().getString(mTextviewArray[i])).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mGroupBookingTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
        Intent HomeIntent = getIntent();
        if (HomeIntent.getExtras() != null && HomeIntent.getExtras().get("city_id") != null &&
                HomeIntent.getExtras().get("city_id").toString().length() > 0) {
            mCityIdStr = HomeIntent.getExtras().get("city_id").toString();
        } else {
            mCityIdStr = LoginManager.getInstance().getTrackcityid();
        }
        if (mCurrIndex == 0 || mCurrIndex == 1 || mCurrIndex == 2) {
            mGroupBookingTabHost.setCurrentTab(1);
        }
        showOrderPayDialog(mShowDialogInt);
        mShowDialogInt = 0;
    }
    private View getTabItemView(int index){
        View view = mLayoutInflater.inflate(R.layout.activity_groupbooking_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.groupbooking_tab_img);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.groupbooking_tab_tv);
        textView.setText(this.getResources().getString(mTextviewArray[index]));
        View mView = view.findViewById(R.id.groupbooking_tab_view);
        if (index == 1) {
            mView.setVisibility(View.GONE);
        } else {
            mView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        for (int i = 0;i< mGroupBookingTabHost.getTabWidget().getChildCount() ;i++){
            TextView textView = (TextView) mGroupBookingTabHost.getTabWidget().getChildAt(i).findViewById(R.id.groupbooking_tab_tv);
            if (this.getResources().getString(mTextviewArray[i]).equals(tabId)){
                textView.setTextColor(getResources().getColor(R.color.default_redbg));
            } else{
                textView.setTextColor(getResources().getColor(R.color.headline_tv_color ));
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get("order_type") != null) {
                mCurrIndex = bundle.getInt("order_type");
            }
            if (bundle.get("showdialog") != null) {
                mShowDialogInt = bundle.getInt("showdialog");
            }
            if (mCurrIndex == 0 || mCurrIndex == 1 || mCurrIndex == 2) {
                mGroupBookingTabHost.setCurrentTab(1);
            }
            showOrderPayDialog(mShowDialogInt);
            mShowDialogInt = 0;
        }
    }
    private void showOrderPayDialog(int type) {
        if ((mShowFailureDialog == null || !mShowFailureDialog.isShowing()) &&
                (type == 1 || type == 2)) {
            String msg = "";
            if (type == 1) {
                msg = getResources().getString(R.string.order_pay_showdialog);
            } else if (type == 2) {
                msg = getResources().getString(R.string.order_refuse_offline_pay_failuremessage);
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_perfect:
                            mShowFailureDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            mShowFailureDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,listener)
                    .setText(R.id.dialog_title_msg_tv,
                            getResources().getString(R.string.order_pay_error_message))
                    .setText(R.id.dialog_title_textview,msg)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.btn_cancel,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(this,mShowFailureDialog);
            mShowFailureDialog.show();
        }

    }

}
