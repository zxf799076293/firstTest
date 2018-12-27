package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldFieldListPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldFieldListMvpView;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldview.FieldLoadMoreListView;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class Field_FieldListActivity extends FieldBaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener,
        FieldLoadMoreListView.OnLoadMore, FieldFieldListMvpView {
    private ViewPager mFieldListListViewPager;
    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipList;
    private FieldLoadMoreListView[] mOrderList;
    private RelativeLayout[] mFieldListNullLL;
    private FeildListAdapter[] mFeildListAdapter;
    private ArrayList<Field_FieldlistModel>[] mDataList;
    private int mCurrIndex = 0;// 当前页卡编号
    private int mCheckItemInt;
    private int[] mListPageSizeInt;
    private InputMethodManager mManager;
    private TabLayout mFieldListTL;
    private RelativeLayout mFieldListRemindRL;
    private int shopcardrestart = -1;
    private PopupWindow pw;
    private int mResTypeId;
    private int mTabTextViewInt[] = {R.string.myselfinfo_check, R.string.btn_maintab_field_list,
            R.string.fieldinfo_no_resources_two_text, R.string.myselfinfo_refused};
    private int mAdvTabTextViewInt[] = {R.string.fieldlist_resources_type_check_text, R.string.btn_maintab_field_list,
            R.string.fieldlist_resources_type_close_text, R.string.order_cancel_toast, R.string.myselfinfo_refused};
    private FieldFieldListPresenter mFieldFieldListPresenter;
    private int resourceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_fragment_field_list_test);
        mFieldFieldListPresenter = new FieldFieldListPresenter();
        mFieldFieldListPresenter.attachView(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.field_maintab_fieldlist_title_text));
        TitleBarUtils.showBackButton(this, true,
                "",
                getResources().getColor(R.color.headline_tv_color), 14);
        if (!(LoginManager.isLogin())) {
            LoginManager.getInstance().clearLoginInfo();
            Intent intent = new Intent("com.business.loginActivity");
            startActivity(intent);
            finish();
        } else {
            if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
                mResTypeId = 3;
            } else {
                Intent intent = getIntent();
                if (intent.getExtras() != null && intent.getExtras().get("res_type_id") != null) {
                    mResTypeId = intent.getExtras().getInt("res_type_id");
                }
            }
            Intent intent = getIntent();
            if (intent.getExtras().get("status") != null &&
                    intent.getExtras().get("status").toString().length() > 0) {
                String status = intent.getExtras().get("status").toString();
                if (status.equals("waiting_for_review")) {
                    mCurrIndex = 0;
                } else if (status.equals("ready_for_sale")) {
                    mCurrIndex = 1;
                } else if (mResTypeId == 3 &&
                        status.equals("closed")) {
                    mCurrIndex = 2;
                } else if (mResTypeId == 1 &&
                        status.equals("removed_from_sale")) {
                    mCurrIndex = 2;
                } else if (mResTypeId == 3 &&
                        status.equals("removed_from_sale")) {
                    mCurrIndex = 3;
                } else if (mResTypeId == 1 &&
                        status.equals("rejected")) {
                    mCurrIndex = 3;
                } else if (status.equals("rejected") &&
                        mResTypeId == 3) {
                    mCurrIndex = 4;
                }
            }
            mFieldListTL = (TabLayout) findViewById(R.id.field_list_tablayout);
            mFieldListRemindRL = (RelativeLayout) findViewById(R.id.fieldlist_remind_layout);
            if (mResTypeId == 3) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.field_maintab_fieldlist_activity_title));
                mSwipList = new SwipeRefreshLayout[5];
                mOrderList = new FieldLoadMoreListView[5];
                mFieldListNullLL = new RelativeLayout[5];
                mFeildListAdapter = new FeildListAdapter[5];
                mDataList = new ArrayList[5];
                mListPageSizeInt = new int[5];
            } else {
                mSwipList = new SwipeRefreshLayout[4];
                mOrderList = new FieldLoadMoreListView[4];
                mFieldListNullLL = new RelativeLayout[4];
                mFeildListAdapter = new FeildListAdapter[4];
                mDataList = new ArrayList[4];
                mListPageSizeInt = new int[4];
            }
            if (LoginManager.getEnterprise_authorize_status() != 1) {
                mFieldListRemindRL.setVisibility(View.VISIBLE);
                mFieldListRemindRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent enterpriseCertification = new Intent("com.business.aboutActivity");
                        enterpriseCertification.putExtra("type", com.linhuiba.linhuifield.config.Config.ENTERPRISE_CERTIFICATE_INT);
                        startActivityForResult(enterpriseCertification,com.linhuiba.linhuifield.config.Config.ENTERPRISE_CERTIFICATE_INT);
                    }
                });
            }

            TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.txt_childaccount_titleaddtxt),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (LoginManager.isRight_to_publish()) {
                                Field_AddResourcesModel.getnewInstance();
                                if (LoginManager.getInstance().getAddfield_resourcesmodel() != null && LoginManager.getInstance().getAddfield_resourcesmodel().length() > 0) {
                                    Field_AddResourcesModel.getnewInstance(JSON.parseObject(LoginManager.getInstance().getAddfield_resourcesmodel(), Field_AddResourcesModel.class));
                                }
                                Field_AddResourcesModel.getInstance().getResource().setRes_type_id(mResTypeId);
                                if (LoginManager.getInstance().getAddfield_show_guide() == 0) {
                                    Intent addfield = new Intent(Field_FieldListActivity.this, FieldAddFieldGuideActivity.class);
                                    addfield.putExtra("show_type",1);
                                    startActivity(addfield);
                                } else {
                                    Intent chooseTypeIntent = new Intent(Field_FieldListActivity.this,FieldAddFieldChooseResOptionsActivity.class);
                                    startActivity(chooseTypeIntent);
                                }
                            } else {
                               BaseMessageUtils.showToast(getResources().getString(R.string.module_field_list_no_authority));
                            }
                        }
                    });
            mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            hideKeyboard();
            initView();
            showProgressDialog();
            initData();
            mFieldFieldListPresenter.getFieldResCreate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(getContext())) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }

        } else if (screen == false) {
            shopcardrestart = 0;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //屏幕亮灭
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            //是否处于后台
            if (Constants.isBackground(this)) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }
        } else if (screen == false) {
            shopcardrestart = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFieldFieldListPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shopcardrestart == 1) {
            showProgressDialog();
            initData();
        }
    }

    private void initView() {
        //获取发布配置的控件
        mFieldListListViewPager = (ViewPager) findViewById(R.id.friend_viewpager);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = this.getLayoutInflater();

        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        if (mResTypeId == 3) {
            mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        }
        mFieldListListViewPager.setAdapter(new OrderPagerAdapter(mListViews));
        for (int i = 0; i < mListViews.size(); i++) {

            mSwipList[i] = (SwipeRefreshLayout) mListViews.get(i).findViewById(R.id.field_swipe_refresh);
            mOrderList[i] = (FieldLoadMoreListView) mListViews.get(i).findViewById(R.id.field_order_list);
            mSwipList[i].setOnRefreshListener(this);
            mOrderList[i].setLoadMoreListen(this);
            mOrderList[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrIndex == 1) {
                        Intent resourceinfo = new Intent("com.business.aboutActivity");
                        resourceinfo.putExtra("type", com.linhuiba.linhuifield.config.Config.RESOURCE_INFO_WEB_INT);
                        resourceinfo.putExtra("resourceid", Integer.parseInt(mDataList[mCurrIndex].get(position).getResource_id()));
                        resourceinfo.putExtra("resources_type", Integer.parseInt(mDataList[mCurrIndex].get(position).getResource_type_id()));
                        startActivityForResult(resourceinfo, 1);
                    }

                }
            });
            mSwipList[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mFieldListNullLL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.lay_no_order);
            mFieldListNullLL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    initData();
                }
            });
        }
        mFieldListListViewPager.setOnPageChangeListener(new PagerChangeListener());
        mFieldListListViewPager.setCurrentItem(mCurrIndex);
        mFieldListTL.setupWithViewPager(mFieldListListViewPager);
        for (int i = 0; i < mFieldListTL.getTabCount(); i++) {
            if (mResTypeId == 3) {
                mFieldListTL.getTabAt(i).setText(getResources().getString(mAdvTabTextViewInt[i]));
            } else {
                mFieldListTL.getTabAt(i).setText(getResources().getString(mTabTextViewInt[i]));
            }
        }
    }

    @Override
    public void onFieldListSuccess(ArrayList<Field_FieldlistModel> data) {
        if (mSwipList[mCurrIndex].isShown()) {
            mSwipList[mCurrIndex].setRefreshing(false);
        }
        mDataList[mCurrIndex] = data;
        if (mDataList[mCurrIndex] == null || mDataList[mCurrIndex].isEmpty()) {
            mFieldListNullLL[mCurrIndex].setVisibility(View.VISIBLE);
        } else {
            mFieldListNullLL[mCurrIndex].setVisibility(View.GONE);
            mFeildListAdapter[mCurrIndex] = new FeildListAdapter(Field_FieldListActivity.this, mDataList[mCurrIndex], mCurrIndex);
            mOrderList[mCurrIndex].setAdapter(mFeildListAdapter[mCurrIndex]);
            if (mDataList[mCurrIndex].size() < 10) {
                mOrderList[mCurrIndex].set_loaded();
            }
        }
    }

    @Override
    public void onFieldListFailure(boolean superresult, Throwable error) {
        if (mSwipList[mCurrIndex].isShown()) {
            mSwipList[mCurrIndex].setRefreshing(false);
        }
        if (!superresult)
            BaseMessageUtils.showToast(getContext(), error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onFieldListMoreSuccess(ArrayList<Field_FieldlistModel> data) {
        ArrayList<Field_FieldlistModel> tmp = data;
        if (data != null && tmp.size() > 0) {
            for (Field_FieldlistModel fieldDetail : tmp) {
                mDataList[mCurrIndex].add(fieldDetail);
            }
            mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
            mOrderList[mCurrIndex].onLoadComplete();
            if (tmp.size() < 10) {
                mOrderList[mCurrIndex].set_loaded();
            }
        } else {
            mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex] - 1;
            mOrderList[mCurrIndex].set_loaded();
        }
    }

    @Override
    public void onFieldListMoreFailure(boolean superresult, Throwable error) {
        mOrderList[mCurrIndex].onLoadComplete();
        mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex] - 1;
        if (!superresult)
            BaseMessageUtils.showToast(getContext(), error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onFieldInfoSuccess(Field_AddResourcesInfoModel data) {
        if (data != null) {
            Field_AddResourcesModel.getInstance().setResource(data);
            Field_AddResourcesModel.getInstance().getResource().setIsRedact(1);
            Field_AddResourcesModel.getInstance().getResource().setRes_type_id(mResTypeId);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setId(resourceId);
            Intent addfieldintent = new Intent(Field_FieldListActivity.this, FieldAddFieldMainActivity.class);
            startActivity(addfieldintent);
        } else {
            BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
        }
    }

    @Override
    public void onFieldInfoFailure(boolean superresult, Throwable error) {
        if (!superresult)
            BaseMessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    public void onFieldThroughSuccess() {
        mDataList[mCurrIndex].remove(mCheckItemInt);
        mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
        BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.field_added_toast));
    }

    @Override
    public void onFieldResCreateSuccess(Field_AddResourceCreateModel data) {
        Field_AddResourceCreateModel field_addResourceCreateModel = data;
        if (field_addResourceCreateModel.getCity() != null && field_addResourceCreateModel.getField_type() != null &&
                field_addResourceCreateModel.getActivity_type() != null &&
                field_addResourceCreateModel.getContraband() != null && field_addResourceCreateModel.getRequirement() != null &&
                field_addResourceCreateModel.getCategories() != null && field_addResourceCreateModel.getBuilding_years() != null &&
                field_addResourceCreateModel.getLocation_type() != null &&
                field_addResourceCreateModel.getCity().size() > 0 && field_addResourceCreateModel.getField_type().size() > 0 &&
                field_addResourceCreateModel.getActivity_type().size() > 0 &&
                field_addResourceCreateModel.getContraband().size() > 0 && field_addResourceCreateModel.getRequirement().size() > 0 &&
                field_addResourceCreateModel.getCategories().size() > 0 && field_addResourceCreateModel.getBuilding_years().size() > 0 &&
                field_addResourceCreateModel.getLocation_type().size() > 0) {
            Field_AddResourcesModel.getnewInstance();
            if (LoginManager.getInstance().getAddfield_resourcesmodel() != null && LoginManager.getInstance().getAddfield_resourcesmodel().length() > 0) {
                Field_AddResourcesModel.getnewInstance(JSON.parseObject(LoginManager.getInstance().getAddfield_resourcesmodel(), Field_AddResourcesModel.class));
            }
            Field_AddResourcesModel.getInstance().setOptions(field_addResourceCreateModel);
            LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
        }
    }

    @Override
    public void onFieldResCreateFailure(boolean superresult, Throwable error) {

    }

    public class OrderPagerAdapter extends PagerAdapter {
        public List<View> mListViews = null;

        public OrderPagerAdapter(List<View> mListViews) {
            super();
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mListViews.get(position), 0);
//			setPagerInfo(0);
            return mListViews.get(position);
        }

    }

    @Override
    public void loadMore() {
        String status = "0";
        if (mCurrIndex == 0) {
            status = "waiting_for_review";
        } else if (mCurrIndex == 1) {
            status = "ready_for_sale";
        } else if (mCurrIndex == 2) {
            if (mResTypeId == 3) {
                status = "closed";
            } else {
                status = "removed_from_sale";
            }
        } else if (mCurrIndex == 3) {
            if (mResTypeId == 3) {
                status = "removed_from_sale";
            } else {
                status = "rejected";
            }
        } else if (mCurrIndex == 4) {
            status = "rejected";
        }
        mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex] + 1;
        if (!mDataList[mCurrIndex].isEmpty()) {
            mFieldFieldListPresenter.getFieldListData(mResTypeId, status,
                    String.valueOf(mListPageSizeInt[mCurrIndex]), "10");
        }

    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void setPageView() {
        showProgressDialog();
        initData();
    }

    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrIndex = position;
            switch (position) {
                case 0:
                    setPageView();
                    break;
                case 1:
                    setPageView();
                    break;
                case 2:
                    setPageView();
                    break;
                case 3:
                    setPageView();
                    break;
                case 4:
                    setPageView();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class FeildListAdapter extends BaseAdapter {
        private ArrayList<Field_FieldlistModel> mFeildList;
        private LayoutInflater mInflater = null;
        private int type;
        private Context mContext;

        public FeildListAdapter(Context context, ArrayList<Field_FieldlistModel> list, int type) {
            this.mFeildList = list;
            this.mInflater = LayoutInflater.from(context);
            this.type = type;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            if (mFeildList != null) {
                return mFeildList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.fragment_fieldlist_item, null);
                holder.img = (ImageView) convertView.findViewById(R.id.imageView);
                holder.price = (TextView) convertView.findViewById(R.id.field_price_value);
                holder.title = (TextView) convertView.findViewById(R.id.field_title_value);
                holder.address = (TextView) convertView.findViewById(R.id.field_address_value);
                holder.mfield_editorbtn = (TextView) convertView.findViewById(R.id.field_editorbtn);
                holder.mfieldlist_item_resourcestype_laber = (TextView) convertView.findViewById(R.id.fieldlist_item_resourcestype_laber);
                holder.btnshelves = (TextView) convertView.findViewById(R.id.field_shelves);
                holder.btnadded = (TextView) convertView.findViewById(R.id.field_added);
                holder.mfieldlist_item_field_operation_layout = (LinearLayout) convertView.findViewById(R.id.fieldlist_item_field_operation_layout);
                holder.mfieldlist_item_view = (View) convertView.findViewById(R.id.fieldlist_item_view);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == mFeildList.size() - 1) {
                holder.mfieldlist_item_view.setVisibility(View.GONE);
            } else {
                holder.mfieldlist_item_view.setVisibility(View.VISIBLE);
            }
            if (mFeildList.get(position).getPic_url() != null) {
                if (mFeildList.get(position).getPic_url().length() != 0) {
                    Picasso.with(Field_FieldListActivity.this)
                            .load(mFeildList.get(position).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .resize(160, 160).into(holder.img);

                } else {
                    Picasso.with(Field_FieldListActivity.this)
                            .load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .into(holder.img);
                }
            } else {
                Picasso.with(Field_FieldListActivity.this)
                        .load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                        .into(holder.img);
            }
            if (mFeildList.get(position).getIs_enquiry() == 1) {
                holder.price.setText(getResources().getString(R.string.fieldlist_enquiry_type_tv_str));
            } else {
                holder.price.setText(Constants.getPriceUnitStr(mContext, (getResources().getString(R.string.order_listitem_price_unit_text)
                        + Constants.getdoublepricestring(mFeildList.get(position).getOrigin_price(), 0.01)), 10));
            }
            if (mFeildList.get(position).getType() != null &&
                    mFeildList.get(position).getType().length() > 0) {
                holder.mfieldlist_item_resourcestype_laber.setVisibility(View.VISIBLE);
                holder.mfieldlist_item_resourcestype_laber.setText(mFeildList.get(position).getType());
            } else {
                holder.mfieldlist_item_resourcestype_laber.setVisibility(View.GONE);
            }
            if (mFeildList.get(position).getResource_type_id() != null && mFeildList.get(position).getResource_type_id().length() > 0) {
                if (LoginManager.isRight_to_publish() || LoginManager.isIs_supplier()) {
                    holder.mfieldlist_item_field_operation_layout.setVisibility(View.VISIBLE);
                    holder.mfield_editorbtn.setVisibility(View.VISIBLE);
                    holder.mfield_editorbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //编辑场地
                            if (LoginManager.isRight_to_publish()) {
                                showProgressDialog();
                                resourceId = Integer.parseInt(mFeildList.get(position).getResource_id());
                                mFieldFieldListPresenter.getFieldinfo(mFeildList.get(position).getResource_id());
                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.module_field_list_no_authority));
                            }
                        }
                    });
                    if (!mFeildList.get(position).getResource_type_id().equals("3")) {
                        if (type == 1) {
                            holder.btnshelves.setVisibility(View.VISIBLE);
                            holder.btnshelves.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   Intent fieldshelvesintent = new Intent(Field_FieldListActivity.this, Field_Field_ShelvesActivity.class);
                                   fieldshelvesintent.putExtra("fieldId", mFeildList.get(position).getResource_id());
                                   mCheckItemInt = position;
                                   startActivityForResult(fieldshelvesintent, 2);
                                }
                            });
                        } else if (type == 2) {
                            holder.btnadded.setVisibility(View.VISIBLE);
                            holder.btnadded.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (LoginManager.isRight_to_publish()) {
                                        new AlertDialog.Builder(Field_FieldListActivity.this)
                                                .setTitle("继续执行？")
                                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                        mCheckItemInt = position;
                                                        mFieldFieldListPresenter.editStatusThrough(mFeildList.get(position).getResource_id());
                                                    }
                                                })
                                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        dialog.dismiss();

                                                    }
                                                }).show();
                                    } else {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.module_field_list_no_authority));
                                    }
                                }
                            });
                        }
                    }
                } else {
                    holder.mfield_editorbtn.setVisibility(View.GONE);
                    holder.btnshelves.setVisibility(View.GONE);
                    holder.btnadded.setVisibility(View.GONE);
                    holder.mfieldlist_item_field_operation_layout.setVisibility(View.GONE);
                }
                holder.title.setText((String) mFeildList.get(position).getName());
                holder.address.setText((String) mFeildList.get(position).getAddress());
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView img;
        public TextView price;
        public TextView title;
        public TextView address;
        public TextView mfield_editorbtn;
        public TextView mfieldlist_item_resourcestype_laber;
        public TextView btnshelves;
        public TextView btnadded;
        public LinearLayout mfieldlist_item_field_operation_layout;
        public View mfieldlist_item_view;
    }

    private void initData() {
        String status = "0";
        if (mCurrIndex == 0) {
            status = "waiting_for_review";
        } else if (mCurrIndex == 1) {
            status = "ready_for_sale";
        } else if (mCurrIndex == 2) {
            if (mResTypeId == 3) {
                status = "closed";
            } else {
                status = "removed_from_sale";
            }
        } else if (mCurrIndex == 3) {
            if (mResTypeId == 3) {
                status = "removed_from_sale";
            } else {
                status = "rejected";
            }
        } else if (mCurrIndex == 4) {
            status = "rejected";
        }
        mListPageSizeInt[mCurrIndex] = 1;
        mOrderList[mCurrIndex].set_refresh();
        mFieldFieldListPresenter.getFieldListData(mResTypeId,
                status, String.valueOf(mListPageSizeInt[mCurrIndex]), "10");
    }
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                mManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data.getExtras() != null) {
                    if (data.getExtras().getInt("updata") == 1) {
                        showProgressDialog();
                        initData();
                    }
                }

                break;
            case 2:
                if (data.getExtras() != null) {
                    if (data.getExtras().getInt("updata") == 1) {
                        mDataList[mCurrIndex].remove(mCheckItemInt);
                        mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
                        BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.field_shelves_toast));
                    }
                }

                break;
            default:
                break;
        }
        switch (requestCode) {
            case com.linhuiba.linhuifield.config.Config.ENTERPRISE_CERTIFICATE_INT:
                if (LoginManager.getEnterprise_authorize_status() == 1) {
                    mFieldListRemindRL.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        mCurrIndex = 0;
        mFieldListListViewPager.setCurrentItem(mCurrIndex);
        if (intent.getExtras() != null &&
                intent.getExtras().get("editor_agen") != null &&
                intent.getExtras().getInt("editor_agen") == 1) {
            if (intent.getExtras().get("id") != null) {
                resourceId = intent.getExtras().getInt("id");
                mFieldFieldListPresenter.getFieldinfo(String.valueOf(resourceId));
            }
        }
    }

}
