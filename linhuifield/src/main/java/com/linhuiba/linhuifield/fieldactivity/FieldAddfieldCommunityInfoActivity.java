package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldadapter.AddfieldCommunityInfoAdapter;
import com.linhuiba.linhuifield.fieldadapter.AddfieldCommunityInfoPhyResAdapter;
import com.linhuiba.linhuifield.fieldadapter.FieldBannerImageLoader;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldCommunityCategoriesModel;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldPhyResModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldAddfieldChooseResOptionsMvpPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddfieldChooseResOptionsaMvpView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.ModuleViewAddfieldCommunityInfo;
import com.linhuiba.linhuifield.fieldview.WheelView;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.OnWheelChangedListener;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldAddfieldCommunityInfoActivity extends FieldBaseMvpActivity
        implements FieldAddfieldChooseResOptionsaMvpView {
    private Banner mAddfieldMainBanner;
    private LinearLayout mAddfieldBannerDefaultLL;
    private LinearLayout mAddfieldBannerNoLL;
    private RelativeLayout mAddfieldBannerRL;
    private LinearLayout mAddfieldDynamicCategoryLL;
    private RecyclerView mPhyResRC;
    private LinearLayout mCommunityCityLL;
    private TextView mCommunityCityTV;
    private LinearLayout mCommunityDistrictLL;
    private TextView mCommunityDistrictTV;
    private EditText mCommunityNameET;
    private EditText mCommunityAddressET;
    private LinearLayout mCommunityDeveloperLL;
    private TextView mCommunityDeveloperTV;
    private LinearLayout mCommunityBuileYearLL;
    private TextView mCommunityBuileYearTV;
    private LinearLayout mCommunityCategoryLL;
    private TextView mCommunityCategoryTV;
    private TextView mCommunityDescriptionTV;
    private LinearLayout mCommunityDescriptionLL;
    private EditText mCommunityAreaET;
    private Button mCommunityBtn;
    private LinearLayout mCommunityBtnLL;
    private LinearLayout mAddCommunityInfoLL;
    private LinearLayout mChooseCommunityInfoLL;
    private TextView mChooseCommunityInfoNameTV;
    private TextView mChooseCommunityInfoAddressTV;
    private LinearLayout mAddfieldDynamicCategoryOptionsLL;
    private LinearLayout mAddCommunityInfoPerfectLL;
    private LinearLayout mAddCommunityInfoPerfectClickLL;
    private ImageView mAddCommunityInfoPerfectImgv;
    private LinearLayout mAddfieldCommunityShowLL;
    private TextView mAddfieldCommunityShowTypeTV;
    private TextView mAddfieldCommunityShowAreaTV;
    private TextView mAddfieldCommunityShowTradingAreaTV;
    private TextView mAddfieldCommunityShowBuildTV;
    private TextView mAddfieldCommunityShowDevelopTV;
    private LinearLayout mAddfieldCommunityShowAttributesLL;
    private ArrayList<String> mPicList = new ArrayList<>();
    private DisplayMetrics mDisplayMetrics;
    private FieldBannerImageLoader mAddfieldMainImgLoader;
    private int height;
    private int width;
    private List<ModuleViewAddfieldCommunityInfo> mViewList = new ArrayList<>();
    private Dialog mDialog;
    private int wheelviewSelectInt = -1;

    private ArrayList<FieldAddfieldAttributesModel> attributes = new ArrayList<>();
    private AddfieldCommunityInfoPhyResAdapter mAddfieldCommunityInfoPhyResAdapter;
    private Integer mCityId;
    private Integer mDeveloperId;
    private Integer mCategoryId;
    private String mDescriptionStr;
    private Integer mProvinceId;
    private Integer mDistrictId;
    private List<Field_AddResourceCreateItemModel> mDistrictList = new ArrayList<>();
    private AddfieldSearchCommunityModule mPhyResList;
    private int wheelviewSelectInt1 = -1;
    private int wheelviewSelectInt2 = -1;
    private int wheelviewSelectInt3 = -1;
    private int wheelviewSelectInt4 = -1;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView1;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView2;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView3;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView4;
    private FieldAddfieldAttributesModel mFieldAddfieldAttributesModel;
    private HashMap<Integer,String> attributeEditMap = new HashMap<>();//多文本
    private HashMap<Integer,HashMap<String,Boolean>> attributeMultipleChoice = new HashMap<>();//多选不
    public HashMap<Integer,HashMap<Integer,Boolean>> attributeMultipleIsInputChoice = new HashMap<>();//多选都有输入框 的选中属性 和属性多选的option 的 id
    public HashMap<Integer,HashMap<Integer,String>> attributeMultipleIsInputEditMap = new HashMap<>();//多选都有输入框 的选中属性 和属性多选的option 的 id 的输入框内容
    public HashMap<Integer,Integer> attributeRadioChooseMap = new HashMap<>();//单选选中 的id
    public HashMap<Integer,String> attributeRadioChooseNameMap = new HashMap<>();//单选选中 的id 的name
    private FieldAddfieldChooseResOptionsMvpPresenter mPresenter;
    private int isAddfieldMainIntent;
    private TextView mAttributeTestMoreTV;
    private CustomDialog mOrderOperationDialog;
    private static final int SEARCH_CITY_RESULT_INT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_field_activity_community_info);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        mPresenter = new FieldAddfieldChooseResOptionsMvpPresenter();
        mPresenter.attachView(this);
        mPhyResRC = (RecyclerView) findViewById(R.id.addfield_community_phy_rv);
        mCommunityCityLL = (LinearLayout) findViewById(R.id.addfield_community_city_ll);
        mCommunityCityTV = (TextView) findViewById(R.id.addfield_community_city_tv);
        mCommunityDistrictLL = (LinearLayout) findViewById(R.id.addfield_community_districts_ll);
        mCommunityDistrictTV = (TextView) findViewById(R.id.addfield_community_districts_tv);
        mCommunityNameET = (EditText) findViewById(R.id.addfield_community_name_et);
        mCommunityAddressET = (EditText) findViewById(R.id.addfield_community_address_et);
        mCommunityDeveloperLL = (LinearLayout) findViewById(R.id.addfield_community_developer_ll);
        mCommunityDeveloperTV = (TextView) findViewById(R.id.addfield_community_developer_tv);
        mCommunityBuileYearLL = (LinearLayout) findViewById(R.id.addfield_community_build_year_ll);
        mCommunityBuileYearTV = (TextView) findViewById(R.id.addfield_community_build_year_tv);
        mCommunityCategoryLL = (LinearLayout) findViewById(R.id.addfield_community_category_ll);
        mCommunityCategoryTV = (TextView) findViewById(R.id.addfield_community_category_tv);
        mCommunityDescriptionTV = (TextView) findViewById(R.id.addfield_community_description_tv);
        mCommunityDescriptionLL = (LinearLayout) findViewById(R.id.addfield_community_description_ll);
        mCommunityAreaET = (EditText) findViewById(R.id.addfield_community_area_et);
        mCommunityBtn = (Button) findViewById(R.id.addfield_community_btn);
        mCommunityBtnLL = (LinearLayout) findViewById(R.id.addfield_community_btn_ll);
        mAddCommunityInfoLL = (LinearLayout) findViewById(R.id.addfield_communityinfo_new_ll);
        mChooseCommunityInfoLL = (LinearLayout) findViewById(R.id.addfield_communityinfo_edit_ll);
        mChooseCommunityInfoNameTV = (TextView) findViewById(R.id.addfield_communityinfo_choose_name_tv);
        mChooseCommunityInfoAddressTV = (TextView) findViewById(R.id.addfield_communityinfo_choose_address_tv);
        mAddCommunityInfoPerfectLL = (LinearLayout) findViewById(R.id.addfield_community_dynamic_category_optional_all_ll);
        mAddCommunityInfoPerfectClickLL = (LinearLayout) findViewById(R.id.addfield_community_dynamic_category_optional_click_ll);
        mAddCommunityInfoPerfectImgv = (ImageView) findViewById(R.id.addfield_community_dynamic_category_optional_click_imgv);
        mAddfieldDynamicCategoryOptionsLL = (LinearLayout) findViewById(R.id.addfield_community_dynamic_category_optional_ll);
        mAddfieldCommunityShowLL = (LinearLayout) findViewById(R.id.addfield_community_info_show_ll);
        mAddfieldCommunityShowTypeTV = (TextView) findViewById(R.id.addfield_community_type_tv);
        mAddfieldCommunityShowAreaTV = (TextView) findViewById(R.id.addfield_community_area_tv);
        mAddfieldCommunityShowTradingAreaTV = (TextView) findViewById(R.id.addfield_community_trading_area_tv);
        mAddfieldCommunityShowBuildTV = (TextView) findViewById(R.id.addfield_community_build_tv);
        mAddfieldCommunityShowDevelopTV = (TextView) findViewById(R.id.addfield_community_develop_tv);
        mAddfieldCommunityShowAttributesLL = (LinearLayout) findViewById(R.id.addfield_community_show_attributes_ll);
        Constants.textchangelistener(mCommunityAreaET);
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1 &&
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIs_not_check() == 0) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                    } else {
                        Field_AddResourcesModel.getInstance().setIs_hava_community_info(0);
                    }
                    saveData();
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                }
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 1) {
                    Intent intent = new Intent();
                    setResult(1,intent);
                }
                finish();
            }
        });

        mCommunityCityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                    if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getCity()!= null &&
                            Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                        Intent searchCityIntent = new Intent("com.business.SearchCityActivity");
                        searchCityIntent.putExtra("addfield_searchcity_list",(Serializable) Field_AddResourcesModel.getInstance().getOptions().getCity());
                        searchCityIntent.putExtra("name",mCommunityCityTV.getText().toString().trim());
                        searchCityIntent.putExtra("id",mCityId);
                        searchCityIntent.putExtra("is_community_search_city",1);
                        startActivityForResult(searchCityIntent,SEARCH_CITY_RESULT_INT);
                    }
                }
            }
        });
        mCommunityDistrictLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCityId == null || mCommunityCityTV.getText().toString().length() == 0 ||
                        mProvinceId == null) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_city_hint));
                    return;
                }
                if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                    showChooseDialog(mCommunityDistrictTV, (ArrayList<Field_AddResourceCreateItemModel>) mDistrictList,2);
                }
            }
        });

        if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getBuilding_years() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getBuilding_years().size() > 0) {
            Collections.reverse(Field_AddResourcesModel.getInstance().getOptions().getBuilding_years());
        }
        mCommunityBuileYearLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getBuilding_years() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getBuilding_years().size() > 0) {
                    showChooseDialog(mCommunityBuileYearTV,null,4);
                }
            }
        });

        mCommunityCategoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCategories() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCategories().size() > 0) {
                    showCategoriesDialog(Field_AddResourcesModel.getInstance().getOptions().getCategories());
                }
            }
        });
        mCommunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1 &&
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 0) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                        saveData();
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setEditable(1);
                        Intent phyIntent = new Intent(FieldAddfieldCommunityInfoActivity.this,FieldAddFieldPhysicalResActivity.class);
                        startActivity(phyIntent);
                    }
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                    Field_AddResourcesModel.getInstance().getResource().setPhysical_data(new FieldAddfieldPhyResModel());
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIsSearchChoose(0);
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIs_not_check(0);
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setEditable(1);
                    Intent phyIntent = new Intent(FieldAddfieldCommunityInfoActivity.this,FieldAddFieldPhysicalResActivity.class);
                    startActivity(phyIntent);
                }
            }
        });
        mCommunityDescriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FieldAddfieldCommunityInfoActivity.this,FieldAddfieldEditTextActivity.class);
                intent.putExtra("common_type",1);
                if (mDescriptionStr != null) {
                    intent.putExtra("edit_str",mDescriptionStr);
                }
                intent.putExtra("str_size",255);
                startActivityForResult(intent,2);
            }
        });
        mAddCommunityInfoPerfectClickLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddfieldDynamicCategoryOptionsLL.getVisibility() == View.VISIBLE)  {
                    mAddfieldDynamicCategoryOptionsLL.setVisibility(View.GONE);
                    mAddCommunityInfoPerfectImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_open_gary_button_uncheck));
                } else {
                    mAddCommunityInfoPerfectImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_open_gary_button_check));
                    mAddfieldDynamicCategoryOptionsLL.setVisibility(View.VISIBLE);
                }
            }
        });
        //判断新增图片是否能点击
        //banner
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        height = width * 300 / 375;
        mAddfieldMainBanner = (Banner)findViewById(R.id.addfield_community_banner);
        mAddfieldBannerDefaultLL = (LinearLayout) findViewById(R.id.addfield_community_default_pic_ll);
        mAddfieldBannerNoLL = (LinearLayout) findViewById(R.id.addfield_community_no_pic_ll);
        mAddfieldBannerRL = (RelativeLayout) findViewById(R.id.addfield_community_banner_ll);
        mAddfieldDynamicCategoryLL = (LinearLayout) findViewById(R.id.addfield_community_dynamic_category);
        mAddfieldMainImgLoader = new FieldBannerImageLoader(FieldAddfieldCommunityInfoActivity.this,375,300);
        //显示图片
        updataBanner();
        mAddfieldBannerDefaultLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1 &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 0) {
                    Intent addfield_three = new Intent(FieldAddfieldCommunityInfoActivity.this, Field_AddField_UploadingPictureActivity.class);
                    addfield_three.putExtra("picture_type",1);
                    startActivityForResult(addfield_three,1);
                }
            }
        });

        //选择了场地显示展位信息
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 0 ||
            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 1) {
            mChooseCommunityInfoAddressTV.setText(getResources().getString(R.string.field_list_item_address) +
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAddress());
            mChooseCommunityInfoNameTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getName());
            mChooseCommunityInfoLL.setVisibility(View.VISIBLE);
            mAddCommunityInfoLL.setVisibility(View.GONE);
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_addfield_community_info_title));
            Intent intent = getIntent();
            if (intent.getExtras() != null &&
                    intent.getExtras().get("is_radect") != null) {
                isAddfieldMainIntent = intent.getExtras().getInt("is_radect");
            }
            if (intent.getExtras() != null && intent.getExtras().get("model") != null) {
                mPhyResList = (AddfieldSearchCommunityModule) intent.getExtras().get("model");
                mAddfieldCommunityInfoPhyResAdapter = new AddfieldCommunityInfoPhyResAdapter(R.layout.module_recycle_item_community_info_phy_res,
                        mPhyResList.getPhysical_data(),this,
                        this,1);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mPhyResRC.setNestedScrollingEnabled(false);
                mPhyResRC.setLayoutManager(linearLayoutManager);
                mPhyResRC.setAdapter(mAddfieldCommunityInfoPhyResAdapter);
                mAddfieldCommunityInfoPhyResAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (mPhyResList.getPhysical_data().get(position).getHas_selling_resource() == 1 &&
                                Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 1) {
                            showMsgDialog(position);
                        } else {
                            Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                            Field_AddResourcesModel.getInstance().getResource().setPhysical_data(mPhyResList.getPhysical_data().get(position));
                            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIsSearchChoose(1);
                            if (mPhyResList.getPhysical_data().get(position).getEditable() == 1) {
                                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIs_not_check(1);
                            }
                            Intent addfield = new Intent(FieldAddfieldCommunityInfoActivity.this, FieldAddFieldPhysicalResActivity.class);
                            startActivity(addfield);
                        }

                    }
                });
                mAddfieldCommunityShowLL.setVisibility(View.GONE);
            } else {
                mAddfieldCommunityShowLL.setVisibility(View.VISIBLE);
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getName() != null) {
                    mAddfieldCommunityShowTypeTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getName());
                }
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuilding_area() != null) {
                    mAddfieldCommunityShowAreaTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuilding_area());
                }
                //商圈
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getTrading_area() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getTrading_area().getName() != null) {
                    mAddfieldCommunityShowTradingAreaTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getTrading_area().getName());
                }
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuild_year() != null) {
                    mAddfieldCommunityShowBuildTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuild_year());
                }
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDeveloper() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDeveloper().getDisplay_name() != null) {
                    mAddfieldCommunityShowDevelopTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDeveloper().getDisplay_name());
                }
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().size() > 0) {
                    for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().size(); j++) {
                        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getName() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getName().length() > 0) {
                            final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,9);
                            mAddfieldCommunityShowAttributesLL.addView(moduleViewAddfieldCommunityInfo);
                            moduleViewAddfieldCommunityInfo.mTextView.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getName() + "：");
                            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getOptions() != null &&
                                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getOptions().size() > 0) {
                                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getType() == 1 ||
                                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getType() == 2) {
                                    String tvStr = "";
                                    ArrayList<FieldAddfieldAttributesModel> options = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getOptions();
                                    if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getType() == 1) {
                                        if (options.get(0).getName() != null &&
                                                options.get(0).getName().length() > 0) {
                                            tvStr = tvStr + options.get(0).getName();
                                            if (options.get(0).getMark() != null &&
                                                    options.get(0).getMark().length() > 0) {
                                                tvStr = tvStr + " (" + options.get(0).getMark() + ")";
                                            }
                                        }
                                    } else {
                                        for (int k = 0; k < options.size(); k++) {
                                            if (options.get(k).getName() != null &&
                                                    options.get(k).getName().length() > 0) {
                                                if (tvStr.length() == 0) {
                                                    tvStr = tvStr + options.get(k).getName();
                                                } else {
                                                    tvStr = tvStr + " 、" + options.get(k).getName();
                                                }
                                                if (options.get(k).getMark() != null &&
                                                        options.get(k).getMark().length() > 0) {
                                                    tvStr = tvStr + " (" + options.get(k).getMark() + ")";
                                                }
                                            }
                                        }
                                    }
                                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(tvStr);
                                } else {
                                    ArrayList<FieldAddfieldAttributesModel> options = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getOptions();
                                    if (options.get(0).getMark() != null &&
                                            options.get(0).getMark().length() > 0) {
                                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(options.get(0).getMark());
                                    }
                                }
                            }
                        }
                    }

                }

            }
            if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                mCommunityBtnLL.setVisibility(View.GONE);
            } else {
                if (isAddfieldMainIntent == 1) {
                    mCommunityBtnLL.setVisibility(View.GONE);
                } else {
                    mCommunityBtnLL.setVisibility(View.VISIBLE);
                }
            }
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                mCommunityBtn.setText(getResources().getString(R.string.module_addfield_community_info_add_activity));
            } else {
                mCommunityBtn.setText(getResources().getString(R.string.module_addfield_community_info_add_field));
            }
        } else {
            mChooseCommunityInfoLL.setVisibility(View.GONE);
            mAddCommunityInfoLL.setVisibility(View.VISIBLE);
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_addfield_main_community_info));
            if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
                TitleBarUtils.shownextTextView(this, "",
                        17,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1) {
                                    if (checkInput()) {
                                        Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                                        saveData();
                                        finish();
                                    }
                                } else {
                                    Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setEditable(1);
                                    Intent phyIntent = new Intent(FieldAddfieldCommunityInfoActivity.this,FieldAddFieldPhysicalResActivity.class);
                                    startActivity(phyIntent);
                                }
                            }
                        }
                );
            }
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                mCommunityBtn.setText(getResources().getString(R.string.module_addfield_community_next_activity_btn));
            }
            showData();
        }

    }
    private void addView(final FieldAddfieldAttributesModel category) {
        mAddCommunityInfoPerfectLL.setVisibility(View.GONE);
        if (mViewList != null && mViewList.size() > 0) {
            mViewList.clear();
            mAddfieldDynamicCategoryLL.removeAllViews();
            mAddfieldDynamicCategoryOptionsLL.removeAllViews();
        }
        if (attributeEditMap != null && attributeEditMap.size() > 0) {
            attributeEditMap.clear();
        }
        if (attributeMultipleChoice != null && attributeMultipleChoice.size() > 0) {
            attributeMultipleChoice.clear();
        }
        if (attributeMultipleIsInputChoice != null && attributeMultipleIsInputChoice.size() > 0) {
            attributeMultipleIsInputChoice.clear();
        }
        if (attributeMultipleIsInputEditMap != null && attributeMultipleIsInputEditMap.size() > 0) {
            attributeMultipleIsInputEditMap.clear();
        }
        if (attributeRadioChooseMap != null && attributeRadioChooseMap.size() > 0) {
            attributeRadioChooseMap.clear();
        }
        if (attributeRadioChooseNameMap != null && attributeRadioChooseNameMap.size() > 0) {
            attributeRadioChooseNameMap.clear();
        }
        if (category != null &&
                category.getAttributes().size() > 0) {
            for (int i = 0; i < category.getAttributes().size(); i++) {
                if (category.getAttributes().get(i).getType() == 1 &&
                        category.getAttributes().get(i).getOptions().size() > 0) {
                    final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,5);
                    mViewList.add(moduleViewAddfieldCommunityInfo);
                    if (category.getAttributes().get(i).getRequired() == 0) {
                        mAddCommunityInfoPerfectLL.setVisibility(View.VISIBLE);
                        moduleViewAddfieldCommunityInfo.mTextView.setCompoundDrawables(null, null, null, null);
                        mAddfieldDynamicCategoryOptionsLL.addView(moduleViewAddfieldCommunityInfo);
                    } else {
                        mAddfieldDynamicCategoryLL.addView(moduleViewAddfieldCommunityInfo);
                    }
                    moduleViewAddfieldCommunityInfo.mTextView.setText(category.getAttributes().get(i).getName());
                    moduleViewAddfieldCommunityInfo.mBackTextView.setHint(category.getAttributes().get(i).getHint());
                    moduleViewAddfieldCommunityInfo.mOtherTextView.setCompoundDrawables(null, null, null, null);
                    final int finalI = i;
                    moduleViewAddfieldCommunityInfo.mBackLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (category.getAttributes().get(finalI).getOptions().size() > 0) {
                                showAttributesDialog(moduleViewAddfieldCommunityInfo.mBackTextView,category.getAttributes().get(finalI).getOptions(),
                                        category.getAttributes().get(finalI).getId(),
                                        moduleViewAddfieldCommunityInfo.mOtherLinearLayout,
                                        moduleViewAddfieldCommunityInfo.mOtherTextView,
                                        moduleViewAddfieldCommunityInfo.mOtherEditText);
                            }
                        }
                    });
                    for (int j = 0; j < attributes.size(); j++) {
                        if (attributes.get(j).getId() == category.getAttributes().get(i).getId()) {
                            if (attributes.get(j).getOptions().size() > 0) {
                                ArrayList<FieldAddfieldAttributesModel> optionsList = attributes.get(j).getOptions();
                                for (int k = 0; k < optionsList.size(); k++) {
                                    if (category.getAttributes().get(i).getOptions().size() > 0) {
                                        for (int l = 0; l < category.getAttributes().get(i).getOptions().size(); l++) {
                                            if (optionsList.get(k).getOption_id() - category.getAttributes().get(i).getOptions().get(l).getId() == 0) {
                                                attributeRadioChooseMap.put(category.getAttributes().get(i).getId(),
                                                        category.getAttributes().get(i).getOptions().get(l).getId());
                                                attributeRadioChooseNameMap.put(category.getAttributes().get(i).getId(),
                                                        category.getAttributes().get(i).getOptions().get(l).getName());
                                                moduleViewAddfieldCommunityInfo.mBackTextView.setText(category.getAttributes().get(i).getOptions().get(l).getName());
                                                if (category.getAttributes().get(i).getOptions().get(l).getIs_input() == 1) {
                                                    moduleViewAddfieldCommunityInfo.mOtherLinearLayout.setVisibility(View.VISIBLE);
                                                    moduleViewAddfieldCommunityInfo.mOtherTextView.setText(category.getAttributes().get(i).getOptions().get(l).getName());
                                                    if (optionsList.get(k).getMark() != null) {
                                                        moduleViewAddfieldCommunityInfo.mOtherEditText.setText(optionsList.get(k).getMark());
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (category.getAttributes().get(i).getType() == 2 &&
                category.getAttributes().get(i).getOptions().size() > 0) {
                    if (category.getAttributes().get(i).getAll_inputs() == 1) {
                        final ModuleViewAddfieldCommunityInfo multipleChoiceTwo = new ModuleViewAddfieldCommunityInfo(this,8);
                        mViewList.add(multipleChoiceTwo);
                        multipleChoiceTwo.mTextView.setText(category.getAttributes().get(i).getName());
                        if (category.getAttributes().get(i).getRequired() == 0) {
                            mAddCommunityInfoPerfectLL.setVisibility(View.VISIBLE);
                            multipleChoiceTwo.mTextView.setCompoundDrawables(null, null, null, null);
                            mAddfieldDynamicCategoryOptionsLL.addView(multipleChoiceTwo);
                        } else {
                            mAddfieldDynamicCategoryLL.addView(multipleChoiceTwo);
                        }
                        HashMap<Integer,Boolean> optionMap = new HashMap<>();
                        HashMap<Integer,String> optionMarkMap = new HashMap<>();
                        attributeMultipleIsInputChoice.put(category.getAttributes().get(i).getId(),optionMap);
                        attributeMultipleIsInputEditMap.put(category.getAttributes().get(i).getId(),optionMarkMap);
                        for (int j = 0; j < category.getAttributes().get(i).getOptions().size(); j++) {
                            optionMap.put(category.getAttributes().get(i).getOptions().get(j).getId(),false);
                            optionMarkMap.put(category.getAttributes().get(i).getOptions().get(j).getId(),"");
                            attributeMultipleIsInputChoice.put(category.getAttributes().get(i).getId(),optionMap);
                            attributeMultipleIsInputEditMap.put(category.getAttributes().get(i).getId(),optionMarkMap);
                            for (int k = 0; k < attributes.size(); k++) {
                                if (attributes.get(k).getId() == category.getAttributes().get(i).getId()) {
                                    if (attributes.get(k).getOptions().size() > 0) {
                                        ArrayList<FieldAddfieldAttributesModel> optionsList = attributes.get(k).getOptions();
                                        for (int l = 0; l < optionsList.size(); l++) {
                                            if (optionsList.get(l).getOption_id() - category.getAttributes().get(i).getOptions().get(j).getId() == 0) {
                                                optionMap.put(category.getAttributes().get(i).getOptions().get(j).getId(),true);
                                                if (category.getAttributes().get(i).getOptions().get(j).getIs_input() == 1 &&
                                                        optionsList.get(l).getMark() != null) {
                                                    optionMarkMap.put(category.getAttributes().get(i).getOptions().get(j).getId(),optionsList.get(l).getMark());
                                                }
                                                attributeMultipleIsInputChoice.put(category.getAttributes().get(i).getId(),optionMap);
                                                attributeMultipleIsInputEditMap.put(category.getAttributes().get(i).getId(),optionMarkMap);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        AddfieldCommunityInfoAdapter addfieldCommunityInfoAdapter = new AddfieldCommunityInfoAdapter(R.layout.module_recycle_item_community_info,
                                category.getAttributes().get(i).getOptions(),this,
                                this,category.getAttributes().get(i).getId());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        multipleChoiceTwo.mRecyclerView.setLayoutManager(linearLayoutManager);
                        multipleChoiceTwo.mRecyclerView.setAdapter(addfieldCommunityInfoAdapter);
                    } else {
                        final ModuleViewAddfieldCommunityInfo multipleChoiceOne = new ModuleViewAddfieldCommunityInfo(this,6);
                        mViewList.add(multipleChoiceOne);
                        if (category.getAttributes().get(i).getRequired() == 0) {
                            mAddCommunityInfoPerfectLL.setVisibility(View.VISIBLE);
                            multipleChoiceOne.mTextView.setCompoundDrawables(null, null, null, null);
                            mAddfieldDynamicCategoryOptionsLL.addView(multipleChoiceOne);
                        } else {
                            mAddfieldDynamicCategoryLL.addView(multipleChoiceOne);
                        }
                        multipleChoiceOne.mTextView.setText(category.getAttributes().get(i).getName());
                        showMultiSelect(multipleChoiceOne.mRelativeLayout,multipleChoiceOne.mOtherLinearLayout,
                                multipleChoiceOne.mOtherEditText,category.getAttributes().get(i).getId(),category);
                    }
                } else if (category.getAttributes().get(i).getType() == 3) {
                    final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,3);
                    mViewList.add(moduleViewAddfieldCommunityInfo);
                    if (category.getAttributes().get(i).getRequired() == 0) {
                        mAddCommunityInfoPerfectLL.setVisibility(View.VISIBLE);
                        moduleViewAddfieldCommunityInfo.mTextView.setCompoundDrawables(null, null, null, null);
                        mAddfieldDynamicCategoryOptionsLL.addView(moduleViewAddfieldCommunityInfo);
                    } else {
                        mAddfieldDynamicCategoryLL.addView(moduleViewAddfieldCommunityInfo);
                    }
                    moduleViewAddfieldCommunityInfo.mTextView.setText(category.getAttributes().get(i).getName());
                    String hint = " ";
                    if (category.getAttributes().get(i).getMin_length() != null) {
                        hint = getResources().getString(R.string.module_addfield_community_info_input_min) +
                                String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length()) +
                                getResources().getString(R.string.module_addfield_community_info_input_error_msg);
                    }
                    if (category.getAttributes().get(i).getMax_length() != null) {
                        hint = hint + " " + getResources().getString(R.string.module_addfield_community_info_input_max) +
                                String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length()) +
                                getResources().getString(R.string.module_addfield_community_info_input_error_msg);
                        if (category.getAttributes().get(i).getMax_length() > 0) {
                            moduleViewAddfieldCommunityInfo.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(category.getAttributes().get(i).getMax_length())});
                        }
                    }
                    moduleViewAddfieldCommunityInfo.mEditText.setHint(category.getAttributes().get(i).getHint());
                    for (int k = 0; k < attributes.size(); k++) {
                        if (attributes.get(k).getId() == category.getAttributes().get(i).getId()) {
                            if (attributes.get(k).getOptions().size() > 0) {
                                if (attributes.get(k).getOptions().get(0).getMark() != null) {
                                    moduleViewAddfieldCommunityInfo.mEditText.setText(attributes.get(k).getOptions().get(0).getMark());
                                }
                            }
                        }
                    }

                } else if (category.getAttributes().get(i).getType() == 4) {
                    final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,4);
                    mViewList.add(moduleViewAddfieldCommunityInfo);
                    if (category.getAttributes().get(i).getRequired() == 0) {
                        mAddCommunityInfoPerfectLL.setVisibility(View.VISIBLE);
                        moduleViewAddfieldCommunityInfo.mTextView.setCompoundDrawables(null, null, null, null);
                        mAddfieldDynamicCategoryOptionsLL.addView(moduleViewAddfieldCommunityInfo);
                    } else {
                        mAddfieldDynamicCategoryLL.addView(moduleViewAddfieldCommunityInfo);
                    }
                    moduleViewAddfieldCommunityInfo.mTextView.setText(category.getAttributes().get(i).getName());
                    moduleViewAddfieldCommunityInfo.mBackTextView.setHint(category.getAttributes().get(i).getHint());
                    for (int k = 0; k < attributes.size(); k++) {
                        if (attributes.get(k).getId() == category.getAttributes().get(i).getId()) {
                            if (attributes.get(k).getOptions().size() > 0) {
                                if (attributes.get(k).getOptions().get(0).getMark() != null) {
                                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(attributes.get(k).getOptions().get(0).getMark());
                                    attributeEditMap.put(category.getAttributes().get(i).getId(),
                                            attributes.get(k).getOptions().get(0).getMark());
                                }
                            }
                        }
                    }
                    final int finalI1 = i;
                    moduleViewAddfieldCommunityInfo.mOtherLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FieldAddfieldCommunityInfoActivity.this,FieldAddfieldEditTextActivity.class);
                            mAttributeTestMoreTV = moduleViewAddfieldCommunityInfo.mBackTextView;
                            intent.putExtra("attribute_id",category.getAttributes().get(finalI1).getId());
                            if (category.getAttributes().get(finalI1).getMax_length() != null &&
                                    category.getAttributes().get(finalI1).getMax_length() > 0) {
                                intent.putExtra("str_size",category.getAttributes().get(finalI1).getMax_length());
                            }
                            if (attributeEditMap.get(category.getAttributes().get(finalI1).getId()) != null) {
                                intent.putExtra("edit_str",attributeEditMap.get(category.getAttributes().get(finalI1).getId()));
                            }
                            startActivityForResult(intent,2);
                        }
                    });
                }
            }
            //类目保存
            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory(category);
        }
    }
    //banner
    private void updataBanner() {
        if (mPicList != null) {
            mPicList.clear();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size() > 0) {
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size(); i++) {
                    if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url().length() > 0) {
                        mPicList.add(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url()
                                + Config.Linhui_Max_Watermark);
                    }
                }
            }
        }

        if (mPicList != null && mPicList.size() > 0) {
            mAddfieldBannerDefaultLL.setVisibility(View.GONE);
            mAddfieldMainBanner.setVisibility(View.VISIBLE);
            bannershow();
        } else {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mAddfieldBannerRL.getLayoutParams();
            //设置图片显示高度
            linearParams.height = height;
            linearParams.width = width;
            mAddfieldBannerRL.setLayoutParams(linearParams);
            mAddfieldMainBanner.setVisibility(View.INVISIBLE);
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 0) {
                mAddfieldBannerNoLL.setVisibility(View.VISIBLE);
                mAddfieldBannerDefaultLL.setVisibility(View.GONE);
            } else {
                mAddfieldBannerNoLL.setVisibility(View.GONE);
                mAddfieldBannerDefaultLL.setVisibility(View.VISIBLE);
            }
        }
    }
    private void bannershow() {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mAddfieldBannerRL.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mAddfieldBannerRL.setLayoutParams(linearParams);
        //设置图片加载器
        mAddfieldMainBanner.setImageLoader(mAddfieldMainImgLoader);
        //设置图片集合
        mAddfieldMainBanner.setImages(mPicList);
        //设置banner动画效果
        mAddfieldMainBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mAddfieldMainBanner.isAutoPlay(false);
        //设置指示器位置（当banner模式中有指示器时）
        mAddfieldMainBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1 &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 0) {
            mAddfieldMainBanner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent addfield_three = new Intent(FieldAddfieldCommunityInfoActivity.this, Field_AddField_UploadingPictureActivity.class);
                    addfield_three.putExtra("click_position",position - 1);
                    addfield_three.putExtra("picture_type",1);
                    startActivityForResult(addfield_three,1);
                }
            });
        }
        mAddfieldMainBanner.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                updataBanner();
                break;
            case 2:
                if (data != null &&
                        data.getExtras() != null && data.getExtras().get("edit_str") != null) {
                    if (data.getExtras().get("common_type") != null && data.getExtras().getInt("common_type") > -1) {
                        if (data.getExtras().getInt("common_type") == 1) {
                            mDescriptionStr = data.getExtras().get("edit_str").toString();
                            mCommunityDescriptionTV.setText(mDescriptionStr);
                        }
                    } else {
                        if (data.getExtras().get("attribute_id") != null) {
                            attributeEditMap.put(data.getExtras().getInt("attribute_id"),
                                    data.getExtras().get("edit_str").toString());
                            mAttributeTestMoreTV.setText(data.getExtras().get("edit_str").toString());
                        }
                    }
                }
                break;
            case SEARCH_CITY_RESULT_INT:
                if (data.getExtras() != null &&
                        data.getExtras().get("id") != null &&
                        data.getExtras().get("name") != null) {
                    mCommunityCityTV.setText(data.getExtras().get("name").toString());
                    mCityId = Integer.parseInt(data.getExtras().get("id").toString());
                    mDistrictList = new ArrayList<>();
                    if (Field_AddResourcesModel.getInstance().getOptions().getCity() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                        for (Field_AddResourceCreateItemModel model : Field_AddResourcesModel.getInstance().getOptions().getCity()) {
                            if (model.getId() - mCityId == 0) {
                                mDistrictList = model.getDistricts();
                                mProvinceId = model.getProvince_id();
                                break;
                            }
                        }
                    }
                    mDistrictId = null;
                    mCommunityDistrictTV.setText("");
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showChooseDialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        if (list == null || list.size() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        final WheelView mwheelview= (WheelView) textEntryView.findViewById(R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        int clickPosition = -1;
        if (type != 4) {
            for (int i = 0; i < list.size(); i ++) {
                if (type == 1) {
                    wheelview_list.add(list.get(i).getName());
                    if (mCityId != null &&
                            mCityId == list.get(i).getId()) {
                        clickPosition = i;
                    }
                } else if (type == 2) {
                    wheelview_list.add(list.get(i).getName());
                    if (mDistrictId != null &&
                            mDistrictId == list.get(i).getId()) {
                        clickPosition = i;
                    }
                }
            }
            mwheelview.setOffset(2);
            mwheelview.setItems(wheelview_list);
            if (clickPosition > -1) {
                wheelviewSelectInt = clickPosition;
                mwheelview.setSeletion(clickPosition);
            } else {
                if (wheelview_list.size() > 2) {
                    wheelviewSelectInt = 2;
                    mwheelview.setSeletion(2);
                } else if (wheelview_list.size() > 1) {
                    wheelviewSelectInt = 1;
                    mwheelview.setSeletion(1);
                } else {
                    wheelviewSelectInt = 0;
                    mwheelview.setSeletion(0);
                }
            }
        } else {
            mwheelview.setOffset(2);
            mwheelview.setItems(Field_AddResourcesModel.getInstance().getOptions().getBuilding_years());
            for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getBuilding_years().size(); i ++) {
                if (textView.getText().toString().equals(
                                Field_AddResourcesModel.getInstance().getOptions().getBuilding_years().get(i).toString())) {
                    clickPosition = i;
                    break;
                }
            }
            if (clickPosition > -1) {
                wheelviewSelectInt = clickPosition;
                mwheelview.setSeletion(clickPosition);
            } else {
                wheelviewSelectInt = 0;
                mwheelview.setSeletion(0);
            }
        }
        mwheelview.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelviewSelectInt = selectedIndex - 2;
            }
        });
        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    textView.setText(list.get(wheelviewSelectInt).getName());
                    mCityId = list.get(wheelviewSelectInt).getId();
                } else if (type == 2) {
                    textView.setText(list.get(wheelviewSelectInt).getName());
                    mDistrictId = list.get(wheelviewSelectInt).getId();
                } else if (type == 4) {
                    textView.setText(Field_AddResourcesModel.getInstance().getOptions().getBuilding_years().get(wheelviewSelectInt).toString());
                }
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mDialog);
    }
    private void showAttributesDialog(final TextView textView, final ArrayList<FieldAddfieldAttributesModel> list, final int attribute_id,
                                      final LinearLayout other_ll, final TextView other_tv, EditText other_et) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        final WheelView mwheelview= (WheelView) textEntryView.findViewById(R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (list.get(i).getName() == null) {
                if (list.get(i).getName() != null) {
                    wheelview_list.add(list.get(i).getName());
                } else {
                    wheelview_list.add("");
                }
            } else {
                wheelview_list.add(list.get(i).getName());
            }
        }

        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (list.size() > 2) {
            wheelviewSelectInt = 2;
            mwheelview.setSeletion(2);
        } else if (list.size() > 1) {
            wheelviewSelectInt = 1;
            mwheelview.setSeletion(1);
        } else {
            wheelviewSelectInt = 0;
            mwheelview.setSeletion(0);
        }
        mwheelview.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelviewSelectInt = selectedIndex - 2;
            }
        });
        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(wheelviewSelectInt).getName() != null) {
                    textView.setText(list.get(wheelviewSelectInt).getName());
                } else {
                    if (list.get(wheelviewSelectInt).getName() != null) {
                        textView.setText(list.get(wheelviewSelectInt).getName());
                    } else {
                        textView.setText("");
                    }
                }

                attributeRadioChooseMap.put(attribute_id,list.get(wheelviewSelectInt).getId());
                attributeRadioChooseNameMap.put(attribute_id,list.get(wheelviewSelectInt).getName());
                if (list.get(wheelviewSelectInt).getIs_input() == 1) {
                    other_ll.setVisibility(View.VISIBLE);
                    other_tv.setText(list.get(wheelviewSelectInt).getName());
                } else {
                    other_ll.setVisibility(View.GONE);
                }
                mDialog.dismiss();
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mDialog);
    }

    private void chooseDate(LinearLayout linearLayout,final TextView textView) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();//当前时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);//设置当前日期
                calendar.add(Calendar.DAY_OF_MONTH,0);//天数加一，为-1的话是天数减1
                final DatePickerDialog datedialog;
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker,
                                                  int year, int month, int dayOfMonth) {

                            }
                        };
                datedialog = new DatePickerDialog(FieldAddfieldCommunityInfoActivity.this,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datedialog.getDatePicker();
                datePicker.setMinDate(calendar.getTimeInMillis());
                datedialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                        DatePicker datePicker = datedialog.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        textView.setText(year + "年" +
                                (month + 1) + "月" + day + "日");
//                begin_year = datePicker.getYear();
//                begin_month = datePicker.getMonth()+1;
//                begin_day = datePicker.getDayOfMonth();
                    }
                });
                //取消按钮，如果不需要直接不设置即可
                datedialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datedialog.dismiss();
                    }
                });
                datedialog.show();
            }
        });
    }
    private void showMultiSelect(RelativeLayout relativeLayout, final LinearLayout linearLayout,
                                 EditText editText, final int attribute_id,
                                 FieldAddfieldAttributesModel category) {
        ArrayList<FieldAddfieldAttributesModel> options = null;
        int textListSize = 0;
        if (category != null && category.getAttributes() != null &&
                category.getAttributes().size() > 0) {
            for (int i = 0; i < category.getAttributes().size(); i++) {
                if (attribute_id == category.getAttributes().get(i).getId()) {
                    if (category.getAttributes().get(i).getOptions() != null &&
                            category.getAttributes().get(i).getOptions().size() > 0) {
                        textListSize = category.getAttributes().get(i).getOptions().size();
                        options = category.getAttributes().get(i).getOptions();
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
                        return;
                    }
                    break;
                }
            }

        }
        if (options != null && options.size() > 0) {
            final TextView[] contraband_textViewlist = new TextView[textListSize];
            final HashMap<String,Boolean> contraband_clickmap = new HashMap<>();
            final HashMap<Integer,String> contraband_viewsetidmap = new HashMap<>();

            //多项选择
            float contraband_width = 0;
            float contraband_widthtmp = 0;
            int contraband_hightnum = 0;
            for (int i = 0; i < options.size(); i++) {
                final TextView textView = new TextView(this);
                textView.setText("  " + options.get(i).getName().toString() + "  ");
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(8, 0, 8, 0);
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                textView.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                textView.setId(i);
                textView.setTag(i);
                contraband_width = Constants.getTextWidth(this, options.get(i).getName(), 16) + 16;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, 64);
                if (contraband_width > (width - contraband_widthtmp - 28)) {
                    contraband_hightnum = contraband_hightnum + 1;
                    contraband_widthtmp = 0;
                    params.setMargins((int) contraband_widthtmp + 28, contraband_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    relativeLayout.addView(textView);
                } else {
                    params.setMargins((int) contraband_widthtmp + 28, contraband_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    relativeLayout.addView(textView);
                }
                contraband_widthtmp = contraband_width + contraband_widthtmp + 28;
                contraband_viewsetidmap.put(i, options.get(i).getName().toString());
                contraband_clickmap.put(options.get(i).getName().toString(), false);
                //选中之前选中的item
                for (int j = 0; j < attributes.size(); j++) {
                    if (attributes.get(j).getId() == attribute_id) {
                        if (attributes.get(j).getOptions().size() > 0) {
                            ArrayList<FieldAddfieldAttributesModel> optionsList = attributes.get(j).getOptions();
                            for (int k = 0; k < optionsList.size(); k++) {
                                if (optionsList.get(k).getOption_id() - options.get(i).getId() == 0) {
                                    contraband_clickmap.put(options.get(i).getName(), true);
                                    attributeMultipleChoice.put(attribute_id,contraband_clickmap);
                                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
                                    if (options.get(i).getIs_input() == 1 &&
                                            optionsList.get(k).getMark() != null) {
                                        editText.setText(optionsList.get(k).getMark());
                                        linearLayout.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        }
                    }
                }
                contraband_textViewlist[i] = textView;
            }

            if (contraband_textViewlist != null && contraband_textViewlist.length >0) {
                final ArrayList<FieldAddfieldAttributesModel> finalOptions = options;
                for (int j = 0; j < contraband_textViewlist.length; j++) {
                    final int finalJ = j;
                    contraband_textViewlist[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contraband_clickmap.get(contraband_viewsetidmap.get(v.getId()))) {
                                contraband_clickmap.put(contraband_viewsetidmap.get(v.getId()),false);
                                attributeMultipleChoice.put(attribute_id,contraband_clickmap);
                                contraband_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                                contraband_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                                if (finalOptions.get(finalJ).getIs_input() == 1) {
                                    linearLayout.setVisibility(View.GONE);
                                }
                            } else {
                                contraband_clickmap.put(contraband_viewsetidmap.get(v.getId()),true);
                                attributeMultipleChoice.put(attribute_id,contraband_clickmap);
                                contraband_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
                                contraband_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.default_bluebg));
                                if (finalOptions.get(finalJ).getIs_input() == 1) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        }
    }
    private void showCategoriesDialog(final ArrayList<AddfieldCommunityCategoriesModel> list) {
        if (list == null || list.size() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View textEntryView = layoutInflater.inflate(R.layout.module_dialog_addfield_community_categories, null);
        wheelView1 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories1);
        wheelView2 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories2);
        wheelView3 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories3);
        wheelView4 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories4);
        TextView mBtnConfirm= (TextView) textEntryView.findViewById(R.id.btn_confirm);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        final String[] wheelList1 = new String[list.size()];
        for (int i = 0; i < list.size(); i ++) {
            wheelList1[i] = (list.get(i).getName());
        }
        // 添加change事件
        wheelView1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt1 = newValue;
                showwheel(2,list);
                showwheel(3,list);
                showwheel(4,list);
            }
        });
        wheelView2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt2 = newValue;
                showwheel(3,list);
                showwheel(4,list);
            }
        });
        wheelView3.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt3 = newValue;
                showwheel(4,list);
            }
        });
        wheelView4.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt4 = newValue;
            }
        });
        wheelviewSelectInt1 = 0;
        wheelviewSelectInt2 = 0;
        wheelviewSelectInt3 = 0;
        wheelviewSelectInt4 = 0;
        showwheel(1,list);
        showwheel(2,list);
        showwheel(3,list);
        showwheel(4,list);
        // 设置可见条目数量
        wheelView1.setVisibleItems(11);
        wheelView2.setVisibleItems(11);
        wheelView3.setVisibleItems(11);
        wheelView4.setVisibleItems(11);

        // 添加onclick事件
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categories3 = 0;
                String categoriesstr3 = "";
                if (wheelviewSelectInt4 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getCategories().get(wheelviewSelectInt4).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getCategories().get(wheelviewSelectInt4).getId();
                } else if (wheelviewSelectInt3 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getId();
                } else if (wheelviewSelectInt2 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getId();
                } else if (wheelviewSelectInt1 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getName();
                    categories3 = list.get(wheelviewSelectInt1).getId();
                }
                mCategoryId = categories3;
                mCommunityCategoryTV.setText(categoriesstr3);
                mPresenter.getAttributes(categories3);
                mDialog.dismiss();
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mDialog);
    }
    private boolean checkInput() {
        boolean result = false;
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img() == null ||
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.mPic_map_prompt));
            return result;
        }
        if (mCityId == null || mCommunityCityTV.getText().toString().length() == 0 ||
                mProvinceId == null) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_city_hint));
            return result;
        }
        if (mDistrictId == null || mCommunityDistrictTV.getText().toString().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_district_hint));
            return result;
        }
        if (mCommunityNameET.getText().toString().trim().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_name_hint));
            return result;
        }
        if (mCommunityAddressET.getText().toString().trim().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_address_hint));
            return result;
        }
        if (mCommunityBuileYearTV.getText().toString().trim().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_build_year_hint));
            return result;
        }
        if (mDescriptionStr == null || mDescriptionStr.length() == 0) {
            BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.txt_description_hint_prompt));
            return result;
        }
        if (mCommunityCategoryTV.getText().toString().trim().length() == 0 ||
                mCategoryId == null) {
            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_chooscofieldtype_hinttxt));
            return result;
        }
        if (mViewList.size() > 0) {
            for (int i = 0; i < mViewList.size(); i++) {
                if (mFieldAddfieldAttributesModel.getAttributes().get(i).getRequired() == 1) {
                    if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 1 &&
                            mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions().size() > 0) {
                        if (attributeRadioChooseMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) == null) {
                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_choose_textview_hinttext) +
                                    mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                            return result;
                        }
                    } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 2 &&
                            mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions().size() > 0) {
                        if (mFieldAddfieldAttributesModel.getAttributes().get(i).getAll_inputs() == 1) {
                            if (attributeMultipleIsInputChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) == null ||
                                    attributeMultipleIsInputChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()).size() == 0) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_choose_textview_hinttext) +
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                                return result;
                            } else {
                                Map<Integer, Boolean> map = attributeMultipleIsInputChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                                boolean isChoosed = false;
                                for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
                                    if (entry.getKey() != null &&
                                            entry.getValue()) {
                                        isChoosed = true;
                                        break;
                                    }
                                }
                                if (!isChoosed) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_choose_textview_hinttext) +
                                            mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                                    return result;
                                }
                            }
                        } else {
                            if (attributeMultipleChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) == null ||
                                    attributeMultipleChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()).size() == 0) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_choose_textview_hinttext) +
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                                return result;
                            } else {
                                Map<String, Boolean> map = attributeMultipleChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                                boolean isChoosed = false;
                                for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                                    if (entry.getKey() != null &&
                                            entry.getValue()) {
                                        isChoosed = true;
                                        break;
                                    }
                                }
                                if (!isChoosed) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_choose_textview_hinttext) +
                                            mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                                    return result;
                                }
                            }
                        }
                    } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 3) {
                        if (mViewList.get(i).mEditText.getText().toString().trim().length() == 0) {
                            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_info_input_hint) +
                                    mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                            return result;
                        } else {
                            if (mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length() != null &&
                                    mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length() > 0) {
                                if (mViewList.get(i).mEditText.getText().toString().trim().length() <
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length()) {
                                    BaseMessageUtils.showToast(mFieldAddfieldAttributesModel.getAttributes().get(i).getName() +
                                            getResources().getString(R.string.module_addfield_community_info_input_min) +
                                            String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length()) +
                                                    getResources().getString(R.string.module_addfield_community_info_input_error_msg));
                                    return result;
                                }
                            }
                            if (mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length() != null &&
                                    mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length() > 0) {
                                if (mViewList.get(i).mEditText.getText().toString().trim().length() >
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length()) {
                                    BaseMessageUtils.showToast(mFieldAddfieldAttributesModel.getAttributes().get(i).getName() +
                                            getResources().getString(R.string.module_addfield_community_info_input_max) +
                                            String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length()) +
                                            getResources().getString(R.string.module_addfield_community_info_input_error_msg));
                                    return result;
                                }
                            }
                        }
                    } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 4) {
                        if (attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) == null ||
                                attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()).trim().length() == 0) {
                            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_info_input_hint) +
                                    mFieldAddfieldAttributesModel.getAttributes().get(i).getName());
                            return result;
                        } else {
                            if (mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length() != null) {
                                if (attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()).trim().length() <
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length()) {
                                    BaseMessageUtils.showToast(mFieldAddfieldAttributesModel.getAttributes().get(i).getName() +
                                            getResources().getString(R.string.module_addfield_community_info_input_min) +
                                            String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMin_length()) +
                                            getResources().getString(R.string.module_addfield_community_info_input_error_msg));
                                    return result;
                                }
                            }
                            if (mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length() != null) {
                                if (attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()).trim().length() >
                                        mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length()) {
                                    BaseMessageUtils.showToast(mFieldAddfieldAttributesModel.getAttributes().get(i).getName() +
                                            getResources().getString(R.string.module_addfield_community_info_input_max) +
                                            String.valueOf(mFieldAddfieldAttributesModel.getAttributes().get(i).getMax_length()) +
                                            getResources().getString(R.string.module_addfield_community_info_input_error_msg));
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    private void saveData() {
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity_id(mCityId);
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setProvince_id(mProvinceId);
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict_id(mDistrictId);
        Field_AddResourceCreateItemModel cityModel = new Field_AddResourceCreateItemModel();
        cityModel.setName(mCommunityCityTV.getText().toString());
        if (mCityId != null) {
            cityModel.setId(mCityId);
        }
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity(cityModel);
        Field_AddResourceCreateItemModel districtModel = new Field_AddResourceCreateItemModel();
        districtModel.setName(mCommunityDistrictTV.getText().toString());
        if (mDistrictId != null) {
            cityModel.setId(mDistrictId);
        }
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict(districtModel);
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setName(mCommunityNameET.getText().toString().trim());
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setAddress(mCommunityAddressET.getText().toString().trim());
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory_id(mCategoryId);
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setBuild_year(mCommunityBuileYearTV.getText().toString());
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDescription(mDescriptionStr);
        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setBuilding_area(mCommunityAreaET.getText().toString().trim());
        if (mViewList.size() > 0) {
            ArrayList<FieldAddfieldAttributesModel> attributes = new ArrayList<>();
            for (int i = 0; i < mViewList.size(); i++) {
                if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 1 &&
                        mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions().size() > 0) {
                    if (attributeRadioChooseMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) != null) {
                        ArrayList<FieldAddfieldAttributesModel> options = new ArrayList<>();
                        FieldAddfieldAttributesModel option = new FieldAddfieldAttributesModel();
                        option.setId(attributeRadioChooseMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()));
                        option.setOption_id(attributeRadioChooseMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()));
                        option.setName(attributeRadioChooseNameMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()));
                        if (mViewList.get(i).mOtherLinearLayout.getVisibility() == View.VISIBLE) {
                            option.setMark(mViewList.get(i).mOtherEditText.getText().toString());
                        }
                        option.setType(mFieldAddfieldAttributesModel.getAttributes().get(i).getType());
                        options.add(option);
                        FieldAddfieldAttributesModel attribute = new FieldAddfieldAttributesModel();
                        attribute.setId(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                        attribute.setOptions(options);
                        attributes.add(attribute);
                    }
                } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 2 &&
                        mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions().size() > 0) {
                    if (mFieldAddfieldAttributesModel.getAttributes().get(i).getAll_inputs() == 1) {
                        if (attributeMultipleIsInputChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) != null &&
                                attributeMultipleIsInputEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) != null) {
                            HashMap<Integer,Boolean> map = attributeMultipleIsInputChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                            HashMap<Integer,String> editMap = attributeMultipleIsInputEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                            ArrayList<FieldAddfieldAttributesModel> options = mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions();
                            ArrayList<FieldAddfieldAttributesModel> chooseOptions = new ArrayList<>();
                            for (int j = 0; j < options.size(); j++) {
                                if (map.get(options.get(j).getId()) != null &&
                                        map.get(options.get(j).getId())) {
                                    FieldAddfieldAttributesModel option = new FieldAddfieldAttributesModel();
                                    option.setId(options.get(j).getId());
                                    option.setName(options.get(j).getName());
                                    option.setOption_id(options.get(j).getId());
                                    option.setType(mFieldAddfieldAttributesModel.getAttributes().get(i).getType());
                                    if (editMap.get(options.get(j).getId()) != null) {
                                        option.setMark(editMap.get(options.get(j).getId()) );
                                    }
                                    chooseOptions.add(option);
                                }
                            }
                            if (chooseOptions.size() > 0) {
                                FieldAddfieldAttributesModel attribute = new FieldAddfieldAttributesModel();
                                attribute.setId(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                                attribute.setOptions(chooseOptions);
                                attributes.add(attribute);
                            }
                        }
                    } else {
                        if (attributeMultipleChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) != null) {
                            HashMap<String,Boolean> map = attributeMultipleChoice.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                            ArrayList<FieldAddfieldAttributesModel> options = mFieldAddfieldAttributesModel.getAttributes().get(i).getOptions();
                            ArrayList<FieldAddfieldAttributesModel> chooseOptions = new ArrayList<>();
                            for (int j = 0; j < options.size(); j++) {
                                if (map.get(options.get(j).getName()) != null &&
                                        map.get(options.get(j).getName())) {
                                    FieldAddfieldAttributesModel option = new FieldAddfieldAttributesModel();
                                    option.setId(options.get(j).getId());
                                    option.setName(options.get(j).getName());
                                    option.setOption_id(options.get(j).getId());
                                    option.setType(mFieldAddfieldAttributesModel.getAttributes().get(i).getType());
                                    if (options.get(j).getIs_input() == 1) {
                                        option.setMark(mViewList.get(i).mOtherEditText.getText().toString());
                                    }
                                    chooseOptions.add(option);
                                }
                            }
                            if (chooseOptions.size() > 0) {
                                FieldAddfieldAttributesModel attribute = new FieldAddfieldAttributesModel();
                                attribute.setId(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                                attribute.setOptions(chooseOptions);
                                attributes.add(attribute);
                            }
                        }
                    }
                } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 3) {
                    FieldAddfieldAttributesModel attribute = new FieldAddfieldAttributesModel();
                    attribute.setId(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                    ArrayList<FieldAddfieldAttributesModel> chooseOptions = new ArrayList<>();
                    FieldAddfieldAttributesModel option = new FieldAddfieldAttributesModel();
                    option.setId(0);
                    option.setOption_id(0);
                    option.setType(mFieldAddfieldAttributesModel.getAttributes().get(i).getType());
                    option.setMark(mViewList.get(i).mEditText.getText().toString());
                    option.setName(mViewList.get(i).mTextView.getText().toString());
                    chooseOptions.add(option);
                    attribute.setOptions(chooseOptions);
                    attributes.add(attribute);
                } else if (mFieldAddfieldAttributesModel.getAttributes().get(i).getType() == 4) {
                    if (attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()) != null) {
                        FieldAddfieldAttributesModel attribute = new FieldAddfieldAttributesModel();
                        attribute.setId(mFieldAddfieldAttributesModel.getAttributes().get(i).getId());
                        ArrayList<FieldAddfieldAttributesModel> chooseOptions = new ArrayList<>();
                        FieldAddfieldAttributesModel option = new FieldAddfieldAttributesModel();
                        option.setId(0);
                        option.setOption_id(0);
                        option.setType(mFieldAddfieldAttributesModel.getAttributes().get(i).getType());
                        option.setMark(attributeEditMap.get(mFieldAddfieldAttributesModel.getAttributes().get(i).getId()));
                        option.setName(mViewList.get(i).mTextView.getText().toString());
                        chooseOptions.add(option);
                        attribute.setOptions(chooseOptions);
                        attributes.add(attribute);
                    }
                }
            }
            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setAttributes(attributes);
        }
    }
    private void showData() {
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity_id() != null) {
            mCityId = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity_id();
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity().getName() != null) {
                mCommunityCityTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity().getName());
            }
            for (Field_AddResourceCreateItemModel model : Field_AddResourcesModel.getInstance().getOptions().getCity()) {
                if (model.getId() -
                        mCityId == 0) {
                    mProvinceId = model.getProvince_id();
                    mDistrictList = new ArrayList<>();
                    mDistrictList = model.getDistricts();
                    mCommunityCityTV.setText(model.getName());
                    break;
                }
            }
        } else {
            if (LoginManager.getInstance().getTrackcityid() != null &&
                LoginManager.getInstance().getTrackcityid().length() > 0 &&
                LoginManager.getInstance().getTrackCityName() != null &&
                LoginManager.getInstance().getTrackCityName().length() > 0) {
                mCityId = Integer.parseInt(LoginManager.getInstance().getTrackcityid());
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getCity().size(); i++) {
                    if (Field_AddResourcesModel.getInstance().getOptions().getCity().get(i).getId() -
                            mCityId == 0) {
                        mProvinceId = Field_AddResourcesModel.getInstance().getOptions().getCity().get(i).getProvince_id();
                        mDistrictList = new ArrayList<>();
                        mDistrictList = Field_AddResourcesModel.getInstance().getOptions().getCity().get(i).getDistricts();
                        mCommunityCityTV.setText(Field_AddResourcesModel.getInstance().getOptions().getCity().get(i).getName());
                        break;
                    }
                }
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getProvince_id() != null) {
            mProvinceId = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getProvince_id();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict_id() != null) {
            mDistrictId = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict_id();
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict().getName() != null) {
                mCommunityDistrictTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict().getName());
            }
        }

        mCommunityNameET.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getName());
        mCommunityAddressET.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAddress());
        mCommunityBuileYearTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuild_year());
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory_id() != null) {
            mCategoryId = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory_id();
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getName() != null) {
                mCommunityCategoryTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getName());
                mPresenter.getAttributes(mCategoryId);
            }
        }
        mDescriptionStr = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDescription();
        mCommunityDescriptionTV.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDescription());
        mCommunityAreaET.setText(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuilding_area());
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().size() > 0) {
            attributes = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory() != null) {
            mFieldAddfieldAttributesModel = Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory();
            addView(mFieldAddfieldAttributesModel);
        }
    }
    private void showwheel(int position,ArrayList<AddfieldCommunityCategoriesModel> list) {
        if (position == 1) {
            if (list != null && list.size() > 0) {
                final String[] wheelList1 = new String[list.size()];
                for (int i = 0; i < list.size(); i ++) {
                    wheelList1[i] = (list.get(i).getName());
                }
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, wheelList1));
                wheelviewSelectInt1 = 0;
                wheelView1.setVisibility(View.VISIBLE);
            } else {
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt1 = -1;
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView1.setVisibility(View.GONE);
                wheelView2.setVisibility(View.GONE);
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 2) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt1 < list.size() &&
                    list.get(wheelviewSelectInt1).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().size(); i ++) {
                    wheelList1[i] = list.get(wheelviewSelectInt1).getCategories().get(i).getName();
                }
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, wheelList1));
                wheelviewSelectInt2 = 0;
                wheelView2.setVisibility(View.VISIBLE);
            } else {
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView2.setVisibility(View.GONE);
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 3) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt2 > -1 &&
                    wheelviewSelectInt2 < list.get(wheelviewSelectInt1).getCategories().size() &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size(); i ++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(i).getName());
                }
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, wheelList1));
                wheelviewSelectInt3 = 0;
                wheelView3.setVisibility(View.VISIBLE);
            } else {
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 4) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt2 > -1 && wheelviewSelectInt3 > -1 &&
                    wheelviewSelectInt3 < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size() &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size(); i ++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().get(i).getName());
                }
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, wheelList1));
                wheelviewSelectInt4 = 0;
                wheelView4.setVisibility(View.VISIBLE);
            } else {
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddfieldCommunityInfoActivity.this, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView4.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSearchCommunitySuccess(ArrayList<AddfieldSearchCommunityModule> data) {

    }

    @Override
    public void onSearchCommunityFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onAttributesSuccess(FieldAddfieldAttributesModel data) {
        mFieldAddfieldAttributesModel = data;
        addView(mFieldAddfieldAttributesModel);
    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIsSearchChoose() == 1) {
                Intent intent = new Intent();
                setResult(1,intent);
            }
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1 &&
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getIs_not_check() == 0) {
                if (checkInput()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                    saveData();
                }
                finish();
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void showMsgDialog(final int position) {
        if (mOrderOperationDialog == null || !mOrderOperationDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mOrderOperationDialog.dismiss();
                        Intent searchResIntent = new Intent(FieldAddfieldCommunityInfoActivity.this,Field_FieldListActivity.class);
                        searchResIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        searchResIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        searchResIntent.putExtra("editor_agen",1);
                        searchResIntent.putExtra("id",mPhyResList.getPhysical_data().get(position).getSelling_resource_id());
                        startActivity(searchResIntent);
                    } else if (i == R.id.btn_cancel) {
                        mOrderOperationDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(FieldAddfieldCommunityInfoActivity.this);
            mOrderOperationDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_msg_tv,getResources().getString(R.string.module_addfield_community_repeated_release))
                    .setText(R.id.dialog_title_textview,getResources().getString(R.string.module_addfield_community_repeated_release_hint))
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.module_addfield_community_repeated_release_jump_edit_res))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.confirm))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(FieldAddfieldCommunityInfoActivity.this,mOrderOperationDialog);
            mOrderOperationDialog.show();
        }
    }
}
