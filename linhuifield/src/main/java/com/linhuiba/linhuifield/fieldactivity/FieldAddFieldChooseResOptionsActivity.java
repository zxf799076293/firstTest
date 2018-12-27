package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.OnMultiClickListener;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldCommunityCategoriesModel;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldFieldListPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldFieldListMvpView;
import com.linhuiba.linhuifield.fieldview.WheelView;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.OnWheelChangedListener;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class FieldAddFieldChooseResOptionsActivity extends FieldBaseMvpActivity
        implements FieldFieldListMvpView {
    private TextView mAddfieldCity;
    private TextView mAddfieldChooseResTypeTV;
    private TextView mAddfieldDistrictsTV;
    private Button mAddfieldAgentBtn;
    private LinearLayout mAddfieldCityLL;
    private LinearLayout mAddfieldChooseResTypeLL;
    private LinearLayout mAddfieldDistrictsLL;
    private LinearLayout maddfield_main_all_layout;
    private TextView maddfield_main_network_refresh_text;
    private TextView mAddfieldChooseOptionsAddTV;
    private LinearLayout mAddfieldChooseBannerLL;
    private TextView mAddfieldChooseBannerTitleTV;
    private TextView mAddfieldChooseBannerDescriptionTV;
    private Dialog mDialog;
    private int wheelviewSelectInt = -1;
    private int mCityId;
    private int mDistrictId;
    private int mResTypeId;
    private List<ConfigCitiesModel> mCityList = new ArrayList<>();
    private FieldFieldListPresenter mFieldFieldListPresenter;
    private int wheelviewSelectInt1 = -1;
    private int wheelviewSelectInt2 = -1;
    private int wheelviewSelectInt3 = -1;
    private int wheelviewSelectInt4 = -1;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView1;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView2;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView3;
    private com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView4;
    private List<Field_AddResourceCreateItemModel> mDistrictsList = new ArrayList<>();
    private static final int SEARCH_CITY_RESULT_INT = 1;
    private int mProvinceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfield_agency);
        initView();
    }

    private void initView() {
        mFieldFieldListPresenter = new FieldFieldListPresenter();
        mFieldFieldListPresenter.attachView(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_addfield_choose_res_options_titlebar));
        TitleBarUtils.showBackImg(this, true);
        mAddfieldAgentBtn = (Button) findViewById(R.id.addfield_agent_btn);
        mAddfieldCity = (TextView) findViewById(R.id.addfield_choose_ooption_city_tv);
        mAddfieldChooseResTypeTV = (TextView) findViewById(R.id.addfield_choose_ooption_res_type_tv);
        mAddfieldDistrictsTV  = (TextView) findViewById(R.id.addfield_choose_ooption_districts_tv);
        mAddfieldCityLL = (LinearLayout) findViewById(R.id.addfield_choose_ooption_city_ll);
        mAddfieldChooseResTypeLL = (LinearLayout) findViewById(R.id.addfield_choose_ooption_res_type_ll);
        mAddfieldDistrictsLL = (LinearLayout) findViewById(R.id.addfield_choose_ooption_districts_ll);
        maddfield_main_all_layout = (LinearLayout) findViewById(R.id.addfield_main_all_layout);
        maddfield_main_network_refresh_text = (TextView) findViewById(R.id.addfield_main_network_refresh_text);
        mAddfieldChooseOptionsAddTV = (TextView) findViewById(R.id.addfield_choose_options_add_res_tv);
        mAddfieldChooseBannerLL = (LinearLayout) findViewById(R.id.addfield_choose_res_banner_ll);
        mAddfieldChooseBannerTitleTV = (TextView) findViewById(R.id.addfield_choose_res_banner_title_tv);
        mAddfieldChooseBannerDescriptionTV = (TextView) findViewById(R.id.addfield_choose_res_banner_description_tv);
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            mAddfieldAgentBtn.setText(getResources().getString(R.string.module_addfield_choose_res_to_search_activity_res));
            mAddfieldChooseOptionsAddTV.setText(getResources().getString(R.string.module_addfield_choose_res_no_data_activity_second));
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_addfield_choose_res_options_activity_titlebar));
            mAddfieldChooseBannerLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_fabuhuodong_three_six));
            mAddfieldChooseBannerTitleTV.setText(getResources().getString(R.string.module_addfield_choose_res_options_activity_banner_title));
            mAddfieldChooseBannerDescriptionTV.setText(getResources().getString(R.string.module_addfield_choose_res_options_activity_banner_description));
        }
        mAddfieldAgentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchResIntent = new Intent(FieldAddFieldChooseResOptionsActivity.this, FieldAddFieldSearchResActivity.class);
                searchResIntent.putExtra("category_id", mResTypeId);
                searchResIntent.putExtra("category_name", mAddfieldChooseResTypeTV.getText().toString());
                searchResIntent.putExtra("city_id", mCityId);
                searchResIntent.putExtra("city_name", mAddfieldCity.getText().toString());
                searchResIntent.putExtra("district_id", mDistrictId);
                searchResIntent.putExtra("district_name", mAddfieldDistrictsTV.getText().toString());
                startActivity(searchResIntent);
            }
        });
        mAddfieldChooseOptionsAddTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查 设置可编辑
                int res_type_id = Field_AddResourcesModel.getInstance().getResource().getRes_type_id();
                Field_AddResourcesModel.getnewInstance();
                if (LoginManager.getInstance().getAddfield_resourcesmodel() != null && LoginManager.getInstance().getAddfield_resourcesmodel().length() > 0) {
                    Field_AddResourcesModel.getnewInstance(JSON.parseObject(LoginManager.getInstance().getAddfield_resourcesmodel(), Field_AddResourcesModel.class));
                }
                Field_AddResourcesModel.getInstance().getResource().setRes_type_id(res_type_id);
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setEditable(1);
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setIsSearchChoose(0);
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setIs_not_check(0);
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setEditable(1);
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIsSearchChoose(0);
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIs_not_check(0);
                if (mCityId > 0 && mAddfieldCity.getText().toString().length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setCity_id(mCityId);
                    model.setName(mAddfieldCity.getText().toString());
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity_id(mCityId);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setProvince_id(mProvinceId);
                }
                if (mDistrictId > 0 && mAddfieldDistrictsTV.getText().toString().length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setName(mAddfieldDistrictsTV.getText().toString());
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict_id(mDistrictId);
                }
                if (mResTypeId > 0 && mAddfieldChooseResTypeTV.getText().toString().length() > 0) {
                    FieldAddfieldAttributesModel model = new FieldAddfieldAttributesModel();
                    model.setName(mAddfieldChooseResTypeTV.getText().toString());
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory_id(mResTypeId);
                }
                Intent intent = new Intent(FieldAddFieldChooseResOptionsActivity.this, FieldAddFieldMainActivity.class);
                startActivity(intent);
            }
        });
        mAddfieldCityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity()!= null &&
                        Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                    Intent searchCityIntent = new Intent("com.business.SearchCityActivity");
                    searchCityIntent.putExtra("addfield_searchcity_list",(Serializable) Field_AddResourcesModel.getInstance().getOptions().getCity());
                    searchCityIntent.putExtra("name",mAddfieldCity.getText().toString().trim());
                    searchCityIntent.putExtra("id",mCityId);
                    startActivityForResult(searchCityIntent,SEARCH_CITY_RESULT_INT);
                }
            }
        });
        mAddfieldDistrictsLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mCityId == 0 && mAddfieldCity.getText().toString().length() == 0) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_city_hint));
                    return;
                } else {
                    ArrayList<Field_AddResourceCreateItemModel> list = new ArrayList<>();
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setName(getResources().getString(R.string.module_addfield_search_city_unlimited));
                    model.setId(0);
                    list.add(model);
                    if (!mAddfieldCity.getText().toString().equals(getResources().getString(R.string.module_addfield_search_city_unlimited))) {
                        if (mDistrictsList != null && mDistrictsList.size() > 0) {
                            list.addAll(mDistrictsList);
                        }
                    }
                    showDialog(mAddfieldDistrictsTV, list, 2);
                }

            }
        });
        mAddfieldChooseResTypeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new AlertDialog.Builder(FieldAddFieldChooseResOptionsActivity.this).create();
                if (Field_AddResourcesModel.getInstance().getOptions() != null) {
                    showCategoriesDialog(Field_AddResourcesModel.getInstance().getOptions().getCategories(), mAddfieldChooseResTypeTV);
                }
            }
        });
        if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getCity() != null && Field_AddResourcesModel.getInstance().getOptions().getField_type() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getActivity_type() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getCustom_dimension() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getContraband() != null && Field_AddResourcesModel.getInstance().getOptions().getRequirement() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0 && Field_AddResourcesModel.getInstance().getOptions().getField_type().size() > 0 &&
                Field_AddResourcesModel.getInstance().getOptions().getActivity_type().size() > 0 &&
                Field_AddResourcesModel.getInstance().getOptions().getCustom_dimension().size() > 0 &&
                Field_AddResourcesModel.getInstance().getOptions().getContraband().size() > 0 && Field_AddResourcesModel.getInstance().getOptions().getRequirement().size() > 0) {
            maddfield_main_all_layout.setVisibility(View.GONE);
        } else {
            showProgressDialog();
            mFieldFieldListPresenter.getFieldResCreate();
        }
        maddfield_main_network_refresh_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                mFieldFieldListPresenter.getFieldResCreate();
            }
        });
    }

    private void showDialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        if (list == null || list.size() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
            return;
        }
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        final WheelView mwheelview = (WheelView) textEntryView.findViewById(R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        int clickPosition = -1;
        for (int i = 0; i < list.size(); i++) {
            if (type == 1 || type == 2) {
                wheelview_list.add(list.get(i).getName());
                if (mCityId == list.get(i).getId()) {
                    clickPosition = i;
                }
            } else {
                wheelview_list.add(list.get(i).getDisplay_name());
            }
        }
        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (clickPosition > -1) {
            wheelviewSelectInt = clickPosition;
            mwheelview.setSeletion(clickPosition);
        } else {
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
                if (type == 1 || type == 2) {
                    textView.setText(list.get(wheelviewSelectInt).getName());
                } else {
                    textView.setText(list.get(wheelviewSelectInt).getDisplay_name());
                }
                if (type == 1) {
                    mCityId = list.get(wheelviewSelectInt).getId();
                    mDistrictId = 0;
                    mAddfieldDistrictsTV.setText("");
                    mDistrictsList = new ArrayList<>();
                    mDistrictsList = Constants.getAddfieldDistrictsList(mCityId,list);
                } else if (type == 2) {
                    mDistrictId = list.get(wheelviewSelectInt).getId();
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
        Constants.show_dialog(textEntryView, mDialog);
    }

    private void showCategoriesDialog(final ArrayList<AddfieldCommunityCategoriesModel> list, final TextView textView) {
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
        TextView mBtnConfirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        final String[] wheelList1 = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            wheelList1[i] = (list.get(i).getName());
        }
        // 添加change事件
        wheelView1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt1 = newValue;
                showwheel(2, list);
                showwheel(3, list);
                showwheel(4, list);
            }
        });
        wheelView2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt2 = newValue;
                showwheel(3, list);
                showwheel(4, list);
            }
        });
        wheelView3.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt3 = newValue;
                showwheel(4, list);
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
        showwheel(1, list);
        showwheel(2, list);
        showwheel(3, list);
        showwheel(4, list);
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
                textView.setText(categoriesstr3);
                mResTypeId = categories3;
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
        Constants.show_dialog(textEntryView, mDialog);
    }

    private void showwheel(int position, ArrayList<AddfieldCommunityCategoriesModel> list) {
        if (position == 1) {
            if (list != null && list.size() > 0) {
                final String[] wheelList1 = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    wheelList1[i] = (list.get(i).getName());
                }
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, wheelList1));
                wheelviewSelectInt1 = 0;
                wheelView1.setVisibility(View.VISIBLE);
            } else {
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt1 = -1;
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
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
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().size(); i++) {
                    wheelList1[i] = list.get(wheelviewSelectInt1).getCategories().get(i).getName();
                }
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, wheelList1));
                wheelviewSelectInt2 = 0;
                wheelView2.setVisibility(View.VISIBLE);
            } else {
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
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
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size(); i++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(i).getName());
                }
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, wheelList1));
                wheelviewSelectInt3 = 0;
                wheelView3.setVisibility(View.VISIBLE);
            } else {
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
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
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size(); i++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().get(i).getName());
                }
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, wheelList1));
                wheelviewSelectInt4 = 0;
                wheelView4.setVisibility(View.VISIBLE);
            } else {
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(FieldAddFieldChooseResOptionsActivity.this, new String[]{""}));
                wheelviewSelectInt4 = -1;
                wheelView4.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFieldListSuccess(ArrayList<Field_FieldlistModel> data) {

    }

    @Override
    public void onFieldListFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onFieldListMoreSuccess(ArrayList<Field_FieldlistModel> data) {

    }

    @Override
    public void onFieldListMoreFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onFieldInfoSuccess(Field_AddResourcesInfoModel data) {

    }

    @Override
    public void onFieldInfoFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onFieldThroughSuccess() {

    }

    @Override
    public void onFieldResCreateSuccess(Field_AddResourceCreateModel data) {
        Field_AddResourceCreateModel field_addResourceCreateModel = data;
        if (field_addResourceCreateModel.getCity() != null && field_addResourceCreateModel.getField_type() != null &&
                field_addResourceCreateModel.getActivity_type() != null &&
                field_addResourceCreateModel.getCustom_dimension() != null &&
                field_addResourceCreateModel.getContraband() != null && field_addResourceCreateModel.getRequirement() != null &&
                field_addResourceCreateModel.getCategories() != null && field_addResourceCreateModel.getBuilding_years() != null &&
                field_addResourceCreateModel.getLocation_type() != null &&
                field_addResourceCreateModel.getCity().size() > 0 && field_addResourceCreateModel.getField_type().size() > 0 &&
                field_addResourceCreateModel.getActivity_type().size() > 0 &&
                field_addResourceCreateModel.getCustom_dimension().size() > 0 &&
                field_addResourceCreateModel.getContraband().size() > 0 && field_addResourceCreateModel.getRequirement().size() > 0 &&
                field_addResourceCreateModel.getCategories().size() > 0 && field_addResourceCreateModel.getBuilding_years().size() > 0 &&
                field_addResourceCreateModel.getLocation_type().size() > 0) {
            maddfield_main_all_layout.setVisibility(View.GONE);
            Field_AddResourcesModel.getInstance().setOptions(field_addResourceCreateModel);
        } else {
            maddfield_main_all_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFieldResCreateFailure(boolean superresult, Throwable error) {
        maddfield_main_all_layout.setVisibility(View.VISIBLE);
        TitleBarUtils.setTitleText(FieldAddFieldChooseResOptionsActivity.this,
                "");

        if (!superresult)
            BaseMessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SEARCH_CITY_RESULT_INT:
                if (data.getExtras() != null &&
                        data.getExtras().get("id") != null &&
                        data.getExtras().get("name") != null) {
                    mAddfieldCity.setText(data.getExtras().get("name").toString());
                    mCityId = Integer.parseInt(data.getExtras().get("id").toString());
                    mDistrictId = 0;
                    mAddfieldDistrictsTV.setText("");
                    mDistrictsList = new ArrayList<>();
                    if (Field_AddResourcesModel.getInstance().getOptions().getCity() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getCity().size() > 0) {
                        for (Field_AddResourceCreateItemModel model : Field_AddResourcesModel.getInstance().getOptions().getCity()) {
                            if (model.getId() - mCityId == 0) {
                                mDistrictsList = model.getDistricts();
                                mProvinceId = model.getProvince_id();
                                break;
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
