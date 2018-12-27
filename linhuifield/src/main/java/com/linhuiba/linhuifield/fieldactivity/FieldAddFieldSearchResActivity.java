package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldadapter.FieldAddFieldSearchResAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldAddfieldChooseResOptionsMvpPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddfieldChooseResOptionsaMvpView;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/10.
 */

public class FieldAddFieldSearchResActivity extends FieldBaseMvpActivity
        implements FieldAddfieldChooseResOptionsaMvpView {
    private RelativeLayout msearch_delete_txt_layout;
    private EditText mtemplate_search_resource_edit;
    private ListView mtemplate_search_resource_listview;
    private LinearLayout mSearchResListLL;
    private LinearLayout mSearchResNoLL;
    private LinearLayout mSearchResAddLL;
    private ImageView mSearchResCloseImgv;
    public ArrayList<Call> callslist = new ArrayList<>();
    private ArrayList<AddfieldSearchCommunityModule> templateResourceModels = new ArrayList<>();
    private FieldAddFieldSearchResAdapter field_addField_templateResourcesAdapter;
    private FieldAddfieldChooseResOptionsMvpPresenter mPresenter;
    private int mCityId;
    private int mCategoryId;
    private int mDistrictId;
    private String mCityName = "";
    private String mCategoryName = "";
    private String mDistrictName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_agent_resources);
        initView();
    }
    private void initView() {
        mPresenter = new FieldAddfieldChooseResOptionsMvpPresenter();
        mPresenter.attachView(this);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("city_id") != null ) {
            mCityId = intent.getExtras().getInt("city_id");
        }
        if (intent.getExtras() != null && intent.getExtras().get("city_name") != null ) {
            mCityName = intent.getExtras().getString("city_name");
        }
        if (intent.getExtras() != null && intent.getExtras().get("district_id") != null) {
            mDistrictId = intent.getExtras().getInt("district_id");
        }
        if (intent.getExtras() != null && intent.getExtras().get("district_name") != null ) {
            mDistrictName = intent.getExtras().getString("district_name");
        }

        if (intent.getExtras() != null && intent.getExtras().get("category_id") != null) {
            mCategoryId = intent.getExtras().getInt("category_id");
        }
        if (intent.getExtras() != null && intent.getExtras().get("category_name") != null ) {
            mCategoryName = intent.getExtras().getString("category_name");
        }

        msearch_delete_txt_layout = (RelativeLayout)findViewById(R.id.addfield_search_src_keyword_close_rl);
        mtemplate_search_resource_edit = (EditText)findViewById(R.id.template_search_resource_edit);
        mtemplate_search_resource_listview = (ListView)findViewById(R.id.template_search_resource_listview);
        mSearchResListLL = (LinearLayout)findViewById(R.id.addfield_search_res_list_ll);
        mSearchResNoLL = (LinearLayout)findViewById(R.id.addfield_search_res_no_ll);
        mSearchResAddLL = (LinearLayout)findViewById(R.id.addfield_search_res_add_new_ll);
        mSearchResCloseImgv = (ImageView) findViewById(R.id.addfield_search_close_imgv);
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() > 0) {
            mtemplate_search_resource_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().trim().length() > 0) {
                        msearch_delete_txt_layout.setVisibility(View.VISIBLE);
                        mtemplate_search_resource_listview.setVisibility(View.VISIBLE);
                        cancelCalls();
                        if (isOnCancel()) {
                            callslist.clear();
                            mPresenter.searchCommunities(mCityId,mDistrictId,mCategoryId,
                                    Field_AddResourcesModel.getInstance().getResource().getRes_type_id(),
                                    mtemplate_search_resource_edit.getText().toString().trim(),
                                    FieldAddFieldSearchResActivity.this);
                        }
                    } else {
                        mSearchResListLL.setVisibility(View.GONE);
                        mSearchResNoLL.setVisibility(View.GONE);
                        msearch_delete_txt_layout.setVisibility(View.INVISIBLE);
                        mtemplate_search_resource_listview.setVisibility(View.GONE);
                    }
                }
            });

        }
        mSearchResCloseImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msearch_delete_txt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msearch_delete_txt_layout.setVisibility(View.INVISIBLE);
                mtemplate_search_resource_listview.setVisibility(View.GONE);
                mtemplate_search_resource_edit.setText("");
            }
        });
        mtemplate_search_resource_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Field_AddResourcesModel.getInstance().getResource().setCommunity_data(templateResourceModels.get(position).getCommunity_data());
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setEditable(0);
                Intent addfield = new Intent(FieldAddFieldSearchResActivity.this, FieldAddFieldMainActivity.class);
                addfield.putExtra("is_search_community",1);
                addfield.putExtra("model", (Serializable)templateResourceModels.get(position));
                startActivity(addfield);
            }
        });
        mSearchResNoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置能编辑
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
                if (mCityId > 0 && mCityName.length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setCity_id(mCityId);
                    model.setName(mCityName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity_id(mCityId);
                }
                if (mDistrictId > 0 && mDistrictName.length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setName(mDistrictName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict_id(mDistrictId);
                }
                if (mCategoryId > 0 && mCategoryName.length() > 0) {
                    FieldAddfieldAttributesModel model = new FieldAddfieldAttributesModel();
                    model.setName(mCategoryName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory_id(mCategoryId);
                }
                Intent addfield = new Intent(FieldAddFieldSearchResActivity.this, FieldAddFieldMainActivity.class);
                startActivity(addfield);
            }
        });
        mSearchResAddLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置能编辑
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
                if (mCityId > 0 && mCityName.length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setCity_id(mCityId);
                    model.setName(mCityName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCity_id(mCityId);
                }
                if (mDistrictId > 0 && mDistrictName.length() > 0) {
                    Field_AddResourceCreateItemModel model = new Field_AddResourceCreateItemModel();
                    model.setName(mDistrictName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setDistrict_id(mDistrictId);
                }
                if (mCategoryId > 0 && mCategoryName.length() > 0) {
                    FieldAddfieldAttributesModel model = new FieldAddfieldAttributesModel();
                    model.setName(mCategoryName);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory(model);
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCategory_id(mCategoryId);
                }
                Intent addfield = new Intent(FieldAddFieldSearchResActivity.this, FieldAddFieldMainActivity.class);
                startActivity(addfield);
            }
        });

    }
    private void cancelCalls() {
        for (int i = 0; i < callslist.size(); i++) {
            if (!callslist.get(i).isCanceled()) {
                callslist.get(i).cancel();
            }
        }
    }
    private boolean isOnCancel() {
        boolean uncancel = true;
        for (int i = 0; i < callslist.size(); i++) {
            if (!callslist.get(i).isCanceled()) {
                uncancel = false;
                break;
            }
        }
        return uncancel;
    }
    @Override
    public void onSearchCommunitySuccess(ArrayList<AddfieldSearchCommunityModule> data) {
        templateResourceModels = data;
        if (templateResourceModels != null && templateResourceModels.size() > 0) {
            field_addField_templateResourcesAdapter = new FieldAddFieldSearchResAdapter(FieldAddFieldSearchResActivity.this,templateResourceModels);
            mtemplate_search_resource_listview.setAdapter(field_addField_templateResourcesAdapter);
            mSearchResListLL.setVisibility(View.VISIBLE);
            mSearchResNoLL.setVisibility(View.GONE);
        } else {
            mSearchResListLL.setVisibility(View.GONE);
            mSearchResNoLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSearchCommunityFailure(boolean superresult, Throwable error) {
        if (!superresult)
            BaseMessageUtils.showToast(error.getMessage());
        checkAccess(error);
        mSearchResListLL.setVisibility(View.GONE);
        mSearchResNoLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttributesSuccess(FieldAddfieldAttributesModel data) {

    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {

    }
}
