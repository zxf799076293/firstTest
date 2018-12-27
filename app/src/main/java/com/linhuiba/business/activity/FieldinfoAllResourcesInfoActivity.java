package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.model.FieldInfoPhysicalResourceModel;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/9/28.
 */

public class FieldinfoAllResourcesInfoActivity extends BaseMvpActivity {
    @InjectView(R.id.fieldinfo_all_res_info_tablayout)
    TabLayout mFieldinfoAllResInfoTL;
    @InjectView(R.id.fieldinfo_all_res_info_viewpager)
    ViewPager mFieldinfoAllResInfoVP;
    private LinearLayout[] mFieldinfoAllResCommunityLL = new LinearLayout[2];
    private LinearLayout[] mFieldinfoAllResInfoLL = new LinearLayout[2];
    private TextView[] mtxt_stall_time = new TextView[2];
    private TextView[] mtxt_doLocation_text = new TextView[2];
    private TextView[] mtxt_number_of_people = new TextView[2];
    private LinearLayout[] mtxt_number_of_people_layout = new LinearLayout[2];
    private TextView[] mtxt_number_of_people_peak_time = new TextView[2];
    private LinearLayout[] mtxt_number_of_people_peak_time_layout = new LinearLayout[2];
    private TextView[] mtxt_number_of_people_facade = new TextView[2];
    private LinearLayout[] mtxt_number_of_people_facade_layout = new LinearLayout[2];
    private TextView[] mtxt_total_area = new TextView[2];
    private LinearLayout[] mtotal_area_layout = new LinearLayout[2];
    private TextView[] mtxt_description = new TextView[2];
    private LinearLayout[] mtxt_description_ll = new LinearLayout[2];
    private TextView[] mtxt_property_requirement = new TextView[2];
    private LinearLayout[] mtxt_property_requirement_layout = new LinearLayout[2];
    private TextView[] mtxt_contraband = new TextView[2];
    private LinearLayout[] mtxt_contraband_ll = new LinearLayout[2];
    private TextView[] mtxt_sales_volume = new TextView[2];
    private LinearLayout[] mtxt_sales_volume_layout = new LinearLayout[2];
    private TextView[] mtxt_facilities = new TextView[2];
    private LinearLayout[] mtxt_facilities_layout = new LinearLayout[2];
    private TextView[] mtxt_restaurant = new TextView[2];
    private LinearLayout[] mtxt_restaurant_layout = new LinearLayout[2];
    private TextView[] mtxt_park = new TextView[2];
    private LinearLayout[] mtxt_park_layout = new LinearLayout[2];
    private TextView[] mtxt_participation = new TextView[2];
    private LinearLayout[] mparticipation = new LinearLayout[2];
    private TextView[] mtxt_gender_ratio = new TextView[2];
    private LinearLayout[] mgender_ratio = new LinearLayout[2];
    private TextView[] mtxt_age_group = new TextView[2];
    private LinearLayout[] mage_group = new LinearLayout[2];
    private TextView[] mtxt_consumption_level = new TextView[2];
    private LinearLayout[] mtxt_consumption_level_ll = new LinearLayout[2];
    private TextView[] mtxt_linhuievaluation = new TextView[2];
    private LinearLayout[] mtxt_linhuievaluation_lauout = new LinearLayout[2];
    private View[] mtxt_linhuievaluation_view = new View[2];
    private TextView[] mcommunity_name_text = new TextView[2];
    private LinearLayout[] mcommunity_name_layout = new LinearLayout[2];
    private TextView[] mcommunity_address_text = new TextView[2];
    private LinearLayout[] mcommunity_address_layout = new LinearLayout[2];
    private TextView[] mcommunity_type_text = new TextView[2];
    private LinearLayout[] mcommunity_type_layout = new LinearLayout[2];
    private TextView[] mcommunity_grade_text = new TextView[2];
    private LinearLayout[] mcommunity_grade_layout = new LinearLayout[2];
    private TextView[] mcommunity_business_district_text = new TextView[2];
    private LinearLayout[] mcommunity_business_district_layout = new LinearLayout[2];
    private TextView[] mcommunity_area_text = new TextView[2];
    private LinearLayout[] mcommunity_area_layout = new LinearLayout[2];
    private TextView[] mfieldinfo_community_buildyear_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_buildyear_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_occupancy_rate_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_occupancy_rate_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_number_of_enterprises_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_number_of_enterprises_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_rent_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_rent_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_number_of_households_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_number_of_households_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_house_price_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_house_price_ll = new LinearLayout[2];
    private TextView[] mfieldinfo_community_property_costs_text = new TextView[2];
    private LinearLayout[] mfieldinfo_community_property_costs_ll = new LinearLayout[2];
    private TextView[] mcommunity_total_number_of_people_text = new TextView[2];
    private LinearLayout[] mcommunity_total_number_of_people_layout = new LinearLayout[2];
    private TextView[] mtxt_enterprises_type = new TextView[2];
    private LinearLayout[] mtxt_enterprises_type_layout = new LinearLayout[2];
    private TextView[] mtxt_supermarket_type = new TextView[2];
    private LinearLayout[] mtxt_supermarket_type_layout = new LinearLayout[2];
    private TextView[] mtxt_shopping_mall_type = new TextView[2];
    private LinearLayout[] mtxt_shopping_mall_type_layout = new LinearLayout[2];
    private TextView[] mnumber_of_commercial_textview = new TextView[2];
    private LinearLayout[] mnumber_of_commercial_layout = new LinearLayout[2];
    private TextView[] mhousing_attribute_textview = new TextView[2];
    private LinearLayout[] mhousing_attribute_layout = new LinearLayout[2];
    private TextView[] maveragea_ticket_price_textivew = new TextView[2];
    private LinearLayout[] maveragea_ticket_price_layout = new LinearLayout[2];
    private ArrayList<View> mListViews;
    private int mPageInt;
    private int mTabTextViewList[] = {R.string.fieldinfo_fieldinfo_particular_str, R.string.fieldinfo_resourcesinfo_text};
    private FieldInfoPhysicalResourceModel mAllResModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldinfo_all_resource_info);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        Intent FieldInfoIntent = getIntent();
        if (FieldInfoIntent.getExtras().get("FieldInfoModel") !=null) {
            mAllResModel = (FieldInfoPhysicalResourceModel) FieldInfoIntent.getExtras().get("FieldInfoModel");
        }
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.fieldinfo_all_resources_info_str));
        TitleBarUtils.showBackImg(this,true);
        mFieldinfoAllResInfoTL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mFieldinfoAllResInfoTL,50,50);
            }
        });
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = this.getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.activity_fieldinfo_all_resource_info_viewpage_item, null));
        mListViews.add(inflater.inflate(R.layout.activity_fieldinfo_all_resource_info_viewpage_item, null));
        for (int i = 0; i < mListViews.size(); i++) {
            mFieldinfoAllResCommunityLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_all_res_community_info_ll);
            mFieldinfoAllResInfoLL[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_all_resource_ll);
            mtxt_stall_time[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_stall_time);
            mtxt_doLocation_text[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_doLocation_text);
            mtxt_number_of_people[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_number_of_people);
            mtxt_number_of_people_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_number_of_people_layout);
            mtxt_number_of_people_peak_time[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_number_of_people_peak_time);
            mtxt_number_of_people_peak_time_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_number_of_people_peak_time_layout);
            mtxt_number_of_people_facade[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_number_of_people_facade);
            mtxt_number_of_people_facade_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_number_of_people_facade_layout);
            mtxt_total_area[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_total_area);
            mtotal_area_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.total_area_layout);
            mtxt_description[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_description);
            mtxt_description_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_description_tablerow);
            mtxt_property_requirement[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_property_requirement);
            mtxt_property_requirement_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_property_requirement_layout);
            mtxt_contraband[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_contraband);
            mtxt_contraband_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_contraband_tablerow);
            mtxt_sales_volume[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_sales_volume);
            mtxt_sales_volume_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_sales_volume_layout);
            mtxt_facilities[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_facilities);
            mtxt_facilities_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_facilities_layout);
            mtxt_restaurant[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_restaurant);
            mtxt_restaurant_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_restaurant_layout);
            mtxt_park[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_park);
            mtxt_park_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_park_layout);
            mtxt_participation[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_participation);
            mparticipation[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.participation);
            mtxt_gender_ratio[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_gender_ratio);
            mgender_ratio[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.gender_ratio);
            mtxt_age_group[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_age_group);
            mage_group[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.age_group);
            mtxt_consumption_level[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_consumption_level);
            mtxt_consumption_level_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_consumption_level_ll);
            mtxt_linhuievaluation[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_linhuievaluation);
            mtxt_linhuievaluation_lauout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_linhuievaluation_lauout);
            mtxt_linhuievaluation_view[i] = (View) mListViews.get(i).findViewById(R.id.txt_linhuievaluation_view);
            mcommunity_name_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_name_text);
            mcommunity_name_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_name_layout);
            mcommunity_address_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_address_text);
            mcommunity_address_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_address_layout);
            mcommunity_type_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_type_text);
            mcommunity_type_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_type_layout);
            mcommunity_grade_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_grade_text);
            mcommunity_grade_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_grade_layout);
            mcommunity_business_district_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_business_district_text);
            mcommunity_business_district_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_business_district_layout);
            mcommunity_area_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_area_text);
            mcommunity_area_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_area_layout);
            mfieldinfo_community_buildyear_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_buildyear_text);
            mfieldinfo_community_buildyear_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_buildyear_ll);
            mfieldinfo_community_occupancy_rate_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_occupancy_rate_text);
            mfieldinfo_community_occupancy_rate_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_occupancy_rate_ll);
            mfieldinfo_community_number_of_enterprises_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_number_of_enterprises_text);
            mfieldinfo_community_number_of_enterprises_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_number_of_enterprises_ll);
            mfieldinfo_community_rent_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_rent_text);
            mfieldinfo_community_rent_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_rent_ll);
            mfieldinfo_community_number_of_households_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_number_of_households_text);
            mfieldinfo_community_number_of_households_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_number_of_households_ll);
            mfieldinfo_community_house_price_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_house_price_text);
            mfieldinfo_community_house_price_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_house_price_ll);
            mfieldinfo_community_property_costs_text[i] = (TextView)mListViews.get(i).findViewById(R.id.fieldinfo_community_property_costs_text);
            mfieldinfo_community_property_costs_ll[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.fieldinfo_community_property_costs_ll);
            mcommunity_total_number_of_people_text[i] = (TextView)mListViews.get(i).findViewById(R.id.community_total_number_of_people_text);
            mcommunity_total_number_of_people_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.community_total_number_of_people_layout);
            mtxt_enterprises_type[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_enterprises_type);
            mtxt_enterprises_type_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_enterprises_type_layout);
            mtxt_supermarket_type[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_supermarket_type);
            mtxt_supermarket_type_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_supermarket_type_layout);
            mtxt_shopping_mall_type[i] = (TextView)mListViews.get(i).findViewById(R.id.txt_shopping_mall_type);
            mtxt_shopping_mall_type_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.txt_shopping_mall_type_layout);
            mnumber_of_commercial_textview[i] = (TextView)mListViews.get(i).findViewById(R.id.number_of_commercial_textview);
            mnumber_of_commercial_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.number_of_commercial_layout);
            mhousing_attribute_textview[i] = (TextView)mListViews.get(i).findViewById(R.id.housing_attribute_textview);
            mhousing_attribute_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.housing_attribute_layout);
            maveragea_ticket_price_textivew[i] = (TextView)mListViews.get(i).findViewById(R.id.averagea_ticket_price_textivew);
            maveragea_ticket_price_layout[i] = (LinearLayout) mListViews.get(i).findViewById(R.id.averagea_ticket_price_layout);



            initData(i);
        }
        mFieldinfoAllResInfoVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
        setAllInfoView(mPageInt);//或取数据填充ui
        mFieldinfoAllResInfoVP.setCurrentItem(mPageInt);
        mFieldinfoAllResInfoVP.setOnPageChangeListener(new PagerChangeListener());
        mFieldinfoAllResInfoTL.setupWithViewPager(mFieldinfoAllResInfoVP);
        for (int i=0;i<mFieldinfoAllResInfoTL.getTabCount();i++) {
            mFieldinfoAllResInfoTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
        }
    }
    private void initData(int i) {
        if (mAllResModel.getDo_begin() != null &&
                mAllResModel.getDo_finish() != null &&
                mAllResModel.getDo_begin().length() > 0 &&
                mAllResModel.getDo_finish().length() > 0) {
            mtxt_stall_time[i].setText(mAllResModel.getDo_begin() + "  --  " + mAllResModel.getDo_finish());
        }
        if (mAllResModel.getDoLocation() != null &&
                mAllResModel.getDoLocation().length() > 0) {
            mtxt_doLocation_text[i].setText(mAllResModel.getDoLocation());
        }
        if (mAllResModel.getNumber_of_people() != null) {
            if (mAllResModel.getNumber_of_people() > 0) {
                if (mAllResModel.getNumber_of_people().equals("0")) {
                    mtxt_number_of_people[i].setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                } else {
                    mtxt_number_of_people[i].setText(mAllResModel.getNumber_of_people().toString());
                }
            } else {
                mtxt_number_of_people_layout[i].setVisibility(View.GONE);
            }
        } else {
            mtxt_number_of_people_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getPeak_time() != null &&
                mAllResModel.getPeak_time().getDisplay_name() != null &&
                mAllResModel.getPeak_time().getDisplay_name().length() > 0) {
            mtxt_number_of_people_peak_time_layout[i].setVisibility(View.VISIBLE);
            mtxt_number_of_people_peak_time[i].setText(mAllResModel.getPeak_time().getDisplay_name());
        } else {
            mtxt_number_of_people_peak_time_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getFacade() != null && mAllResModel.getFacade() > 0) {
            mtxt_number_of_people_facade_layout[i].setVisibility(View.VISIBLE);
            mtxt_number_of_people_facade[i].setText(String.valueOf(mAllResModel.getFacade()) +"面");
        } else {
            mtxt_number_of_people_facade_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getTotal_area() != null &&
                mAllResModel.getTotal_area() > 0) {
            mtotal_area_layout[i].setVisibility(View.VISIBLE);
            mtxt_total_area[i].setText(String.valueOf(mAllResModel.getTotal_area())+"m²");
        } else {
            mtotal_area_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getDescription() != null &&
                mAllResModel.getDescription().length() > 0) {
            mtxt_description_ll[i].setVisibility(View.VISIBLE);
            mtxt_description[i].setText(mAllResModel.getDescription());
        } else {
            mtxt_description_ll[i].setVisibility(View.GONE);
        }
        if ((mAllResModel.getRequirements() != null && mAllResModel.getRequirements().size() > 0) ||
                (mAllResModel.getProperty_requirement() != null && mAllResModel.getProperty_requirement().length() > 0)) {
            mtxt_property_requirement_layout[i].setVisibility(View.VISIBLE);
            String property_requirement = "";
            if (mAllResModel.getRequirements() != null && mAllResModel.getRequirements().size() > 0) {
                for (int j = 0; j < mAllResModel.getRequirements().size(); j++) {
                    if (mAllResModel.getRequirements().get(j).getDisplay_name() != null &&
                            mAllResModel.getRequirements().get(j).getDisplay_name().length() > 0) {
                        if (j != 0) {
                            property_requirement = property_requirement + "、" + mAllResModel.getRequirements().get(j).getDisplay_name();
                        } else {
                            property_requirement = property_requirement + mAllResModel.getRequirements().get(j).getDisplay_name();
                        }
                    }
                }
            }
            if (mAllResModel.getProperty_requirement() != null && mAllResModel.getProperty_requirement().length() > 0) {
                if (mAllResModel.getRequirements() != null && mAllResModel.getRequirements().size() > 0) {
                    property_requirement = property_requirement + "、" + mAllResModel.getProperty_requirement();
                } else {
                    property_requirement = property_requirement + mAllResModel.getProperty_requirement();
                }
            }
            mtxt_property_requirement[i].setText(property_requirement);
        } else {
            mtxt_property_requirement_layout[i].setVisibility(View.GONE);
        }
        if ((mAllResModel.getContraband() != null && mAllResModel.getContraband().size() > 0) ||
                (mAllResModel.getOther_contraband() != null && mAllResModel.getOther_contraband().length() > 0)) {
            mtxt_contraband_ll[i].setVisibility(View.VISIBLE);
            String contraband_str = "";
            if (mAllResModel.getContraband() != null && mAllResModel.getContraband().size() > 0) {
                for (int j = 0; j < mAllResModel.getContraband().size(); j++) {
                    if (mAllResModel.getContraband().get(j).getDisplay_name() != null &&
                            mAllResModel.getContraband().get(j).getDisplay_name().length() > 0) {
                        if (j != 0) {
                            contraband_str = contraband_str + "、" + mAllResModel.getContraband().get(j).getDisplay_name();
                        } else {
                            contraband_str = contraband_str + mAllResModel.getContraband().get(j).getDisplay_name();
                        }
                    }
                }
            }
            if (mAllResModel.getOther_contraband() != null && mAllResModel.getOther_contraband().length() > 0) {
                if (mAllResModel.getContraband() != null && mAllResModel.getContraband().size() > 0) {
                    contraband_str = contraband_str + "、" + mAllResModel.getOther_contraband();
                } else {
                    contraband_str = contraband_str + mAllResModel.getOther_contraband();
                }
            }
            mtxt_contraband[i].setText(contraband_str);

        } else {
            mtxt_contraband_ll[i].setVisibility(View.GONE);
        }
        //历史单量字段确认
        if (mAllResModel.getInformation() != null &&
                mAllResModel.getInformation().length() > 0) {
            mtxt_sales_volume[i].setText(mAllResModel.getInformation());
        } else {
            mtxt_sales_volume_layout[i].setVisibility(View.GONE);
        }
        // 配套设施
        if (mAllResModel.getCommunity().getFacilities() != null) {
            if (mAllResModel.getCommunity().getFacilities().length() >0) {
                mtxt_facilities_layout[i].setVisibility(View.VISIBLE);
                mtxt_facilities[i].setText(mAllResModel.getCommunity().getFacilities().toString());
            } else {
                mtxt_facilities_layout[i].setVisibility(View.GONE);
            }
        } else {
            mtxt_facilities_layout[i].setVisibility(View.GONE);
        }
        //配套餐厅
        if (mAllResModel.getCommunity().getRestaurant() == 1 &&
                mAllResModel.getCommunity().getNumber_of_seat() != null) {
            mtxt_restaurant_layout[i].setVisibility(View.VISIBLE);
            mtxt_restaurant[i].setText(getResources().getString(R.string.fieldinfo_restaurant_text) +
                    String.valueOf(mAllResModel.getCommunity().getNumber_of_seat()) +
                    getResources().getString(R.string.fieldinfo_restaurant_unit_text));
        } else {
            mtxt_restaurant_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getHas_carport() == 1) {
            mtxt_park[i].setText(getResources().getString(R.string.fieldinfo_carport_text));
            if (mAllResModel.getCharging_standard() != null && mAllResModel.getCharging_standard().length() > 0) {
                mtxt_park[i].setText(getResources().getString(R.string.fieldinfo_carport_text) +","+
                        mAllResModel.getCharging_standard());
            }
        } else {
            mtxt_park_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getParticipation_level() != null &&
                mAllResModel.getParticipation_level().getDisplay_name() != null &&
                mAllResModel.getParticipation_level().getDisplay_name().length() > 0) {
            mparticipation[i].setVisibility(View.VISIBLE);
            mtxt_participation[i].setText(mAllResModel.getParticipation_level().getDisplay_name());
        } else {
            mparticipation[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getMale_proportion() != null &&
                mAllResModel.getMale_proportion() > 0) {
            mtxt_gender_ratio[i].setText(getResources().getString(R.string.fieldinfo_man_proportion_text) +
                    String.valueOf(mAllResModel.getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text) +
                    "," +getResources().getString(R.string.fieldinfo_woman_proportion_text) +
                    String.valueOf(100 - mAllResModel.getMale_proportion()) +getResources().getString(R.string.fieldinfo_man_proportion_unit_text));

        } else {
            mgender_ratio[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getAge_level() != null &&
                mAllResModel.getAge_level().getDisplay_name() != null &&
                mAllResModel.getAge_level().getDisplay_name().length() > 0) {
            mtxt_age_group[i].setText(mAllResModel.getAge_level().getDisplay_name());
        } else {
            mage_group[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getConsumption_level() != null &&
                mAllResModel.getConsumption_level().getDisplay_name() != null &&
                mAllResModel.getConsumption_level().getDisplay_name().length() > 0) {
            mtxt_consumption_level_ll[i].setVisibility(View.VISIBLE);
            mtxt_consumption_level[i].setText(mAllResModel.getConsumption_level().getDisplay_name());
        } else {
            mtxt_consumption_level_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCompany_comment() != null) {
            if (mAllResModel.getCompany_comment().length() >0) {
                mtxt_linhuievaluation[i].setText(mAllResModel.getCompany_comment().toString());
            } else {
                mtxt_linhuievaluation_lauout[i].setVisibility(View.GONE);
                mtxt_linhuievaluation_view[i].setVisibility(View.GONE);
            }
        } else {
            mtxt_linhuievaluation_lauout[i].setVisibility(View.GONE);
            mtxt_linhuievaluation_view[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getName() != null && mAllResModel.getCommunity().getName().length() > 0) {
            mcommunity_name_text[i].setText(mAllResModel.getCommunity().getName());
        } else {
            mcommunity_name_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getDetailed_address() != null &&
                mAllResModel.getCommunity().getDetailed_address().length() > 0) {
            mcommunity_address_layout[i].setVisibility(View.VISIBLE);
            mcommunity_address_text[i].setText(mAllResModel.getCommunity().getDetailed_address());
        } else {
            mcommunity_address_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getCommunity_type() != null &&
                mAllResModel.getCommunity().getCommunity_type().getDisplay_name() != null &&
                mAllResModel.getCommunity().getCommunity_type().getDisplay_name().length() > 0) {
            mcommunity_type_layout[i].setVisibility(View.VISIBLE);
            mcommunity_type_text[i].setText(mAllResModel.getCommunity().getCommunity_type().getDisplay_name());
        } else {
            mcommunity_type_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getConstruction_class() != null &&
                mAllResModel.getCommunity().getConstruction_class().getDisplay_name() != null &&
                mAllResModel.getCommunity().getConstruction_class().getDisplay_name().length() > 0) {
            mcommunity_grade_layout[i].setVisibility(View.VISIBLE);
            mcommunity_grade_text[i].setText(mAllResModel.getCommunity().getConstruction_class().getDisplay_name());
        } else {
            mcommunity_grade_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getTradingarea() != null &&
                mAllResModel.getCommunity().getTradingarea().getName() != null &&
                mAllResModel.getCommunity().getTradingarea().getName().length() > 0) {
            mcommunity_business_district_layout[i].setVisibility(View.VISIBLE);
            mcommunity_business_district_text[i].setText(mAllResModel.getCommunity().getTradingarea().getName());
        } else {
            mcommunity_business_district_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getBuilding_area() > 0) {
            mcommunity_area_text[i].setText(String.valueOf(mAllResModel.getCommunity().getBuilding_area()) + "㎡");
        } else {
            mcommunity_area_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getBuild_year() != null && mAllResModel.getCommunity().getBuild_year().length() > 0) {
            mfieldinfo_community_buildyear_text[i].setText(String.valueOf(mAllResModel.getCommunity().getBuild_year()));
        } else {
            mfieldinfo_community_buildyear_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getOccupancy_rate() != null &&
                mAllResModel.getCommunity().getOccupancy_rate().length() > 0) {
            mfieldinfo_community_occupancy_rate_text[i].setText(mAllResModel.getCommunity().getOccupancy_rate());
        } else {
            mfieldinfo_community_occupancy_rate_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getNumber_of_enterprises() > 0) {
            mfieldinfo_community_number_of_enterprises_text[i].setText(String.valueOf(mAllResModel.getCommunity().getNumber_of_enterprises()).toString());
        } else {
            mfieldinfo_community_number_of_enterprises_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getRent() > 0) {
            mfieldinfo_community_rent_text[i].setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mAllResModel.getCommunity().getRent()), 0.01) +
                    getResources().getString(R.string.fieldinfo_house_price_unit_text));
        } else {
            mfieldinfo_community_rent_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getNumber_of_households() > 0) {
            mfieldinfo_community_number_of_households_text[i].setText(String.valueOf(mAllResModel.getCommunity().getNumber_of_households()).toString());
        } else {
            mfieldinfo_community_number_of_households_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getHouse_price() > 0) {
            mfieldinfo_community_house_price_text[i].setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mAllResModel.getCommunity().getHouse_price()), 0.01) +
                    getResources().getString(R.string.fieldinfo_house_price_unit_text));
        } else {
            mfieldinfo_community_house_price_ll[i].setVisibility(View.GONE);
        }
        if (String.valueOf(mAllResModel.getCommunity().getProperty_costs()) != null) {
            mfieldinfo_community_property_costs_text[i].setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mAllResModel.getCommunity().getProperty_costs()), 0.01) +
                    getResources().getString(R.string.fieldinfo_property_costs_unit_text));
        } else {
            mfieldinfo_community_property_costs_ll[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getTotal_number_of_people() > 0) {
            mcommunity_total_number_of_people_text[i].setText(String.valueOf(mAllResModel.getCommunity().getTotal_number_of_people()));
        } else {
            mcommunity_total_number_of_people_layout[i].setVisibility(View.GONE);
        }
        //企业类型
        if (mAllResModel.getCommunity().getEnterprise_type() != null &&
                mAllResModel.getCommunity().getEnterprise_type().getDisplay_name() != null &&
                mAllResModel.getCommunity().getEnterprise_type().getDisplay_name().length() > 0) {
            mtxt_enterprises_type_layout[i].setVisibility(View.VISIBLE);
            mtxt_enterprises_type[i].setText(mAllResModel.getCommunity().getEnterprise_type().getDisplay_name());
        } else {
            mtxt_enterprises_type_layout[i].setVisibility(View.GONE);
        }
        //超市定位
        if (mAllResModel.getCommunity().getSupermarket_type() != null &&
                mAllResModel.getCommunity().getSupermarket_type().getDisplay_name() != null &&
                mAllResModel.getCommunity().getSupermarket_type().getDisplay_name().length() > 0) {
            mtxt_supermarket_type_layout[i].setVisibility(View.VISIBLE);
            mtxt_supermarket_type[i].setText(mAllResModel.getCommunity().getSupermarket_type().getDisplay_name());
        } else {
            mtxt_supermarket_type_layout[i].setVisibility(View.GONE);
        }
        //商场定位
        if (mAllResModel.getCommunity().getShopping_mall_type() != null &&
                mAllResModel.getCommunity().getShopping_mall_type().getDisplay_name() != null &&
                mAllResModel.getCommunity().getShopping_mall_type().getDisplay_name().length() > 0) {
            mtxt_shopping_mall_type_layout[i].setVisibility(View.VISIBLE);
            mtxt_shopping_mall_type[i].setText(mAllResModel.getCommunity().getShopping_mall_type().getDisplay_name());
        } else {
            mtxt_shopping_mall_type_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getNumber_of_households() != 0) {
            mnumber_of_commercial_textview[i].setText(String.valueOf(mAllResModel.getCommunity().getNumber_of_households()).toString());
            mnumber_of_commercial_layout[i].setVisibility(View.VISIBLE);
        } else {
            mnumber_of_commercial_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getHouse_attribute() != null &&
                mAllResModel.getCommunity().getHouse_attribute().getDisplay_name() != null &&
                mAllResModel.getCommunity().getHouse_attribute().getDisplay_name().length() > 0) {
            mhousing_attribute_textview[i].setText(mAllResModel.getCommunity().getHouse_attribute().getDisplay_name());
            mhousing_attribute_layout[i].setVisibility(View.VISIBLE);
        } else {
            mhousing_attribute_layout[i].setVisibility(View.GONE);
        }
        if (mAllResModel.getCommunity().getAverage_fare() != null &&
                mAllResModel.getCommunity().getAverage_fare().length() > 0) {
            maveragea_ticket_price_textivew[i].setText(mAllResModel.getCommunity().getAverage_fare());
            maveragea_ticket_price_layout[i].setVisibility(View.VISIBLE);
        } else {
            maveragea_ticket_price_layout[i].setVisibility(View.GONE);
        }
    }
    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPageInt = position;
            switch (position) {
                case 0:
                    setAllInfoView(mPageInt);//或取数据填充ui
                    break;
                case 1:
                    setAllInfoView(mPageInt);//或取数据填充ui
                    break;
                default:
                    break;
            }

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void setAllInfoView(int page) {
        if (page == 0) {
            mFieldinfoAllResInfoLL[page].setVisibility(View.VISIBLE);
            mFieldinfoAllResCommunityLL[page].setVisibility(View.GONE);
        } else if (page == 1) {
            mFieldinfoAllResInfoLL[page].setVisibility(View.GONE);
            mFieldinfoAllResCommunityLL[page].setVisibility(View.VISIBLE);
        }
    }
}
