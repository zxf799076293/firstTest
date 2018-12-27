package com.linhuiba.linhuifield.fieldactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldadapter.FieldBannerImageLoader;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.AddfieldSearchCommunityModule;
import com.linhuiba.linhuifield.fieldmodel.AddressContactModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldAddFieldMainMvpPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldMainMvpView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
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
import java.util.HashMap;

import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/5/2.
 */

public class FieldAddFieldMainActivity extends FieldBaseMvpActivity
        implements FieldAddFieldMainMvpView {
    private RelativeLayout maddfield_main_fieldinfo_layout;
    private RelativeLayout maddfield_main_fieldprice_layout;
    private RelativeLayout maddfield_main_fieldrules_layout;
    private RelativeLayout maddfield_main_fieldservices_layout;
    private RelativeLayout mAddfieldMainActivityInfoLL;
    private TextView maddfield_main_fieldinfo_full_text;
    private TextView mAddfieldMainActivityInfoFullTV;
    private TextView maddfield_main_fieldprice_full_text;
    private TextView mAddfieldCommunityInfoPerfectTV;
    private TextView maddfield_main_fieldservices_full_text;
    private ScrollView maddfield_main_scrollview;
    private TextView maddfield_main_fieldinfo_text;
    private RelativeLayout maddfield_other_contact_layout;
    private TextView maddfield_main_contact_full_text;
    private LinearLayout mAddfieldMainSuccessLL;
    private Button mAddfieldMainSuccessConTinueBtn;
    private Button mAddfieldMainSuccessBackListBtn;
    private Button mAddfieldMainSaveBtn;
    private LinearLayout mAddfieldMainSaveBtnLL;
    private boolean save_btn_isclick = true;
    private Drawable mFinishDrawable;
    private FieldAddFieldMainMvpPresenter mPresenter;
    private final int SEARCH_CHOOSE_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_main);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("is_search_community") != null &&
                intent.getExtras().getInt("is_search_community") == 1) {
            //设置场地选择后的界面属性 可编辑
            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setIsSearchChoose(1);
            if (intent.getExtras() != null && intent.getExtras().get("model") != null) {
                AddfieldSearchCommunityModule mPhyResList = (AddfieldSearchCommunityModule) intent.getExtras().get("model");
                if (mPhyResList.getCommunity_data().getEditable() == 1) {
                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setIs_not_check(1);
                }
                Intent addfield = new Intent(FieldAddFieldMainActivity.this, FieldAddfieldCommunityInfoActivity.class);
                addfield.putExtra("model", (Serializable)mPhyResList);
                startActivityForResult(addfield,SEARCH_CHOOSE_CODE);
            }
        }
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.review_error_text));
            finish();
        }
        int unfinished = 0;
        if (Field_AddResourcesModel.getInstance().getIs_hava_field_info() == 1) {
            maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            unfinished ++;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getIs_hava_activity_info() == 1) {
                mAddfieldMainActivityInfoFullTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
                mAddfieldMainActivityInfoFullTV.setCompoundDrawables(null, null, mFinishDrawable, null);
                mAddfieldMainActivityInfoFullTV.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
            } else {
                mAddfieldMainActivityInfoFullTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
                mAddfieldMainActivityInfoFullTV.setCompoundDrawables(null, null, null, null);
                mAddfieldMainActivityInfoFullTV.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
                unfinished ++;
            }
        }
        if (Field_AddResourcesModel.getInstance().getIs_hava_field_price() == 1 &&
                Field_AddResourcesModel.getInstance().getIs_hava_field_price_standard() == 1) {
            maddfield_main_fieldprice_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_fieldprice_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_fieldprice_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            maddfield_main_fieldprice_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_fieldprice_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_fieldprice_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            unfinished ++;
        }
        if (Field_AddResourcesModel.getInstance().getIs_hava_field_services() == 1) {
            maddfield_main_fieldservices_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_fieldservices_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_fieldservices_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            maddfield_main_fieldservices_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_fieldservices_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_fieldservices_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            unfinished ++;
        }
        if (Field_AddResourcesModel.getInstance().getIs_hava_field_contact() == 1) {
            maddfield_main_contact_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_contact_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_contact_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            maddfield_main_contact_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_contact_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_contact_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            unfinished ++;
        }
        if (Field_AddResourcesModel.getInstance().getIs_hava_community_info() == 1) {
            mAddfieldCommunityInfoPerfectTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            mAddfieldCommunityInfoPerfectTV.setCompoundDrawables(null, null, mFinishDrawable, null);
            mAddfieldCommunityInfoPerfectTV.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            mAddfieldCommunityInfoPerfectTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            mAddfieldCommunityInfoPerfectTV.setCompoundDrawables(null, null, null, null);
            mAddfieldCommunityInfoPerfectTV.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            unfinished ++;
        }
        if (unfinished > 0) {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.addfield_main_title_onetext) +
                    String.valueOf(unfinished) +getResources().getString(R.string.addfield_main_title_twotext));
        } else {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.addfield_main_title_threeext));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.delete_picture_file();
        mPresenter.detachView();
    }

    private void initview() {
        mPresenter = new FieldAddFieldMainMvpPresenter();
        mPresenter.attachView(this);
        mPresenter.getDefaultAddress();
        maddfield_main_fieldinfo_layout = (RelativeLayout)findViewById(R.id.addfield_main_fieldinfo_layout);
        mAddfieldMainActivityInfoLL = (RelativeLayout) findViewById(R.id.addfield_main_activityinfo_layout);
        maddfield_main_fieldprice_layout = (RelativeLayout)findViewById(R.id.addfield_main_fieldprice_layout);
        maddfield_main_fieldrules_layout = (RelativeLayout)findViewById(R.id.addfield_main_fieldrules_layout);
        maddfield_main_fieldservices_layout = (RelativeLayout)findViewById(R.id.addfield_main_fieldservices_layout);
        maddfield_main_scrollview = (ScrollView) findViewById(R.id.addfield_main_scrollview);
        maddfield_main_fieldinfo_full_text = (TextView) findViewById(R.id.addfield_main_fieldinfo_full_text);
        mAddfieldMainActivityInfoFullTV = (TextView) findViewById(R.id.addfield_main_activityinfo_full_text);
        maddfield_main_fieldprice_full_text = (TextView) findViewById(R.id.addfield_main_fieldprice_full_text);
        mAddfieldCommunityInfoPerfectTV = (TextView) findViewById(R.id.addfield_main_fieldreles_full_text);
        maddfield_main_fieldservices_full_text = (TextView) findViewById(R.id.addfield_main_fieldservices_full_text);
        maddfield_main_fieldinfo_text = (TextView)findViewById(R.id.addfield_main_fieldinfo_text);
        maddfield_other_contact_layout = (RelativeLayout)findViewById(R.id.addfield_other_contact_layout);
        maddfield_main_contact_full_text = (TextView) findViewById(R.id.addfield_main_fields_contact_full_text);
        mAddfieldMainSuccessLL = (LinearLayout) findViewById(R.id.addfield_main_success_ll);
        mAddfieldMainSuccessConTinueBtn = (Button) findViewById(R.id.addfield_main_success_continue_btn);
        mAddfieldMainSuccessBackListBtn = (Button) findViewById(R.id.addfield_main_success_back_list_btn);
        mAddfieldMainSaveBtn = (Button) findViewById(R.id.addfield_main_btn);
        mAddfieldMainSaveBtnLL = (LinearLayout) findViewById(R.id.addfield_main_btn_ll);
        mFinishDrawable = getResources().getDrawable(R.drawable.ic_yiwancheng_three_six);
        mFinishDrawable.setBounds(0, 0, mFinishDrawable.getMinimumWidth(), mFinishDrawable.getMinimumHeight());
        mAddfieldMainSuccessConTinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchResIntent = new Intent(FieldAddFieldMainActivity.this,FieldAddFieldChooseResOptionsActivity.class);
                searchResIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                searchResIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(searchResIntent);
            }
        });
        mAddfieldMainSuccessBackListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchResIntent = new Intent(FieldAddFieldMainActivity.this,Field_FieldListActivity.class);
                searchResIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                searchResIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(searchResIntent);
            }
        });
        mAddfieldMainSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_btn_isclick) {
                    if (Field_AddResourcesModel.getInstance().getIs_hava_field_info() == 1 &&
                            Field_AddResourcesModel.getInstance().getIs_hava_field_price_standard() == 1 &&
                            Field_AddResourcesModel.getInstance().getIs_hava_field_price() == 1 &&
                            Field_AddResourcesModel.getInstance().getIs_hava_field_services() == 1 &&
                            Field_AddResourcesModel.getInstance().getIs_hava_field_contact() == 1 &&
                            Field_AddResourcesModel.getInstance().getIs_hava_community_info() == 1) {
                        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                            if (Field_AddResourcesModel.getInstance().getIs_hava_activity_info() == 1) {
                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                        Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),0)) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_start_tiem_error_remind_text));
                                    return;
                                }
                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0 &&
                                        Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline(),0)) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_finish_tiem_error_remind_text));
                                    return;
                                }
                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                                    if (Constants.compare_start_finish_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline())) {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_time_prompt));
                                        return;
                                    } else {
                                        if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_start_date_conflict_text));
                                            return;
                                        }
                                    }
                                }
                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_info_full_hinttext));
                                return;
                            }
                        }
                        save_btn_isclick = false;
//                        Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date());
//                        Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline());
//                        Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_type_id(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getActivity_type_id());
//                        Field_AddResourcesModel.getInstance().getResource().getResource_data().setCustom_name(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getCustom_name());
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().setRes_type_id(Field_AddResourcesModel.getInstance().getResource().getRes_type_id());
                        TitleBarUtils.set_nexttext_enable(FieldAddFieldMainActivity.this,false);
                        showProgressDialog();
                        mPresenter.addCommunities();
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_info_full_hinttext));
                    }
                }
            }
        });
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddfieldMainSuccessLL.getVisibility() == View.GONE) {
                    BaseMessageUtils.showDialog(getContext(), getResources().getString(R.string.dialog_prompt),
                            getResources().getString(R.string.addfield_optional_info_savedata_dialog_text),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                                Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),0)) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_start_tiem_error_remind_text));
                                            return;
                                        }
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0 &&
                                                Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline(),0)) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_finish_tiem_error_remind_text));
                                            return;
                                        }
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                                            if (Constants.compare_start_finish_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline())) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_time_prompt));
                                                return;
                                            } else {
                                                if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_start_date_conflict_text));
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCommunity_id(null);
                                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setPhysical_id(null);
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setId(0);
                                    Field_AddResourcesModel.getInstance().getResource().setIsRedact(0);
                                    LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
                                    finish();
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //场地信息清空 设置资源类型
                                    int res_type_id =  Field_AddResourcesModel.getInstance().getResource().getRes_type_id();
                                    Field_AddResourcesInfoModel resource = new Field_AddResourcesInfoModel();
                                    Field_AddResourcesModel.getInstance().setResource(resource);
                                    Field_AddResourcesModel.getInstance().setPriceConfigNull();
                                    Field_AddResourcesModel.getInstance().getResource().setRes_type_id(res_type_id);
                                    LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
                                    finish();
                                }
                            });
                } else {
                    finish();
                }

            }
        });
        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
            TitleBarUtils.shownextTextView(this, "", 14, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (save_btn_isclick) {
                        if (Field_AddResourcesModel.getInstance().getIs_hava_field_info() == 1 &&
                                Field_AddResourcesModel.getInstance().getIs_hava_field_price_standard() == 1 &&
                                Field_AddResourcesModel.getInstance().getIs_hava_field_price() == 1 &&
                                Field_AddResourcesModel.getInstance().getIs_hava_field_services() == 1 &&
                                Field_AddResourcesModel.getInstance().getIs_hava_field_contact() == 1 &&
                                Field_AddResourcesModel.getInstance().getIs_hava_community_info() == 1) {
                            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                if (Field_AddResourcesModel.getInstance().getIs_hava_activity_info() == 1) {
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                            Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),0)) {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_start_tiem_error_remind_text));
                                        return;
                                    }
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0 &&
                                            Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline(),0)) {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_finish_tiem_error_remind_text));
                                        return;
                                    }
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                                        if (Constants.compare_start_finish_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline())) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_time_prompt));
                                            return;
                                        } else {
                                            if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_start_date_conflict_text));
                                                return;
                                            }
                                        }
                                    }
                                } else {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_info_full_hinttext));
                                    return;
                                }
                            }
                            save_btn_isclick = false;
                            TitleBarUtils.set_nexttext_enable(FieldAddFieldMainActivity.this,false);
                            showProgressDialog();
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().setRes_type_id(Field_AddResourcesModel.getInstance().getResource().getRes_type_id());
                            mPresenter.updateCommunities(Field_AddResourcesModel.getInstance().getResource().getResource_data().getId());
                        } else {
                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_info_full_hinttext));
                        }
                    }


                }
            });
            mAddfieldMainSaveBtnLL.setVisibility(View.GONE);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1) {
            if (checkInput_fieldifno()) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
                maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
                maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_info(0);
                maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
                maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, null, null);
                maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            }
        } else {
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 1) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
                maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
                maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_info(0);
                maddfield_main_fieldinfo_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
                maddfield_main_fieldinfo_full_text.setCompoundDrawables(null, null, null, null);
                maddfield_main_fieldinfo_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (checkInputActivityInfo()) {
                Field_AddResourcesModel.getInstance().setIs_hava_activity_info(1);
                mAddfieldMainActivityInfoFullTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
                mAddfieldMainActivityInfoFullTV.setCompoundDrawables(null, null, mFinishDrawable, null);
                mAddfieldMainActivityInfoFullTV.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_activity_info(0);
                mAddfieldMainActivityInfoFullTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
                mAddfieldMainActivityInfoFullTV.setCompoundDrawables(null, null, null, null);
                mAddfieldMainActivityInfoFullTV.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            }
        }

        if (checkInput_fieldprice()) {
            Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
            Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
            maddfield_main_fieldprice_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_fieldprice_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_fieldprice_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
            Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
            maddfield_main_fieldprice_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_fieldprice_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_fieldprice_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
        }
        if (checkInput_serviceitem()) {
            Field_AddResourcesModel.getInstance().setIs_hava_field_services(1);
            maddfield_main_fieldservices_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_fieldservices_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_fieldservices_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            Field_AddResourcesModel.getInstance().setIs_hava_field_services(0);
            maddfield_main_fieldservices_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_fieldservices_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_fieldservices_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
        }
        if (checkInputFieldContact()) {
            Field_AddResourcesModel.getInstance().setIs_hava_field_contact(1);
            maddfield_main_contact_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            maddfield_main_contact_full_text.setCompoundDrawables(null, null, mFinishDrawable, null);
            maddfield_main_contact_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        } else {
            Field_AddResourcesModel.getInstance().setIs_hava_field_contact(0);
            maddfield_main_contact_full_text.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
            maddfield_main_contact_full_text.setCompoundDrawables(null, null, null, null);
            maddfield_main_contact_full_text.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getEditable() == 1) {
            if (checkInputCommunityInfo()) {
                Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
                mAddfieldCommunityInfoPerfectTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
                mAddfieldCommunityInfoPerfectTV.setCompoundDrawables(null, null, mFinishDrawable, null);
                mAddfieldCommunityInfoPerfectTV.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_community_info(0);
                mAddfieldCommunityInfoPerfectTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_not_fill_in_text));
                mAddfieldCommunityInfoPerfectTV.setCompoundDrawables(null, null, null, null);
                mAddfieldCommunityInfoPerfectTV.setTextColor(getResources().getColor(R.color.module_addfield_main_unfinish_color));
            }
        } else {
            Field_AddResourcesModel.getInstance().setIs_hava_community_info(1);
            mAddfieldCommunityInfoPerfectTV.setText(getResources().getString(R.string.addfield_main_fieldinfo_fill_in_text));
            mAddfieldCommunityInfoPerfectTV.setCompoundDrawables(null, null, mFinishDrawable, null);
            mAddfieldCommunityInfoPerfectTV.setTextColor(getResources().getColor(R.color.module_addfield_main_finish_color));
        }
        if (!checkInput_activity_date()) {
            save_initial_data_state();
        }

        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            mAddfieldMainActivityInfoLL.setVisibility(View.VISIBLE);
        } else {
            mAddfieldMainActivityInfoLL.setVisibility(View.GONE);
        }
        maddfield_main_fieldinfo_layout.setOnClickListener(new MyClickListener());
        mAddfieldMainActivityInfoLL.setOnClickListener(new MyClickListener());
        maddfield_main_fieldprice_layout.setOnClickListener(new MyClickListener());
        maddfield_main_fieldrules_layout.setOnClickListener(new MyClickListener());
        maddfield_main_fieldservices_layout.setOnClickListener(new MyClickListener());
        maddfield_other_contact_layout.setOnClickListener(new MyClickListener());
    }

    @Override
    public void onFieldAddCommunitySuccess() {
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getId() > 0) {
            finish();
        } else {
            mAddfieldMainSuccessLL.setVisibility(View.VISIBLE);
            TitleBarUtils.setTitleText(FieldAddFieldMainActivity.this,getResources().getString(R.string.txt_addfield_succeed));
        }
        save_btn_isclick = true;
        TitleBarUtils.set_nexttext_enable(FieldAddFieldMainActivity.this,true);
        TitleBarUtils.setNextTextViewVisibility(FieldAddFieldMainActivity.this,View.GONE);
        Constants.delete_picture_file();
        //场地信息清空 设置资源类型
        int res_type_id =  Field_AddResourcesModel.getInstance().getResource().getRes_type_id();
        Field_AddResourcesInfoModel resource = new Field_AddResourcesInfoModel();
        Field_AddResourcesModel.getInstance().setResource(resource);
        Field_AddResourcesModel.getInstance().setPriceConfigNull();
        Field_AddResourcesModel.getInstance().getResource().setRes_type_id(res_type_id);
        LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
    }

    @Override
    public void onFieldAddCommunityFailure(boolean superresult, Throwable error) {
        save_btn_isclick = true;
        TitleBarUtils.set_nexttext_enable(FieldAddFieldMainActivity.this,true);
        if (!superresult)
            BaseMessageUtils.showToast(getContext(), error.getMessage());
        checkAccess(error);
    }

    @Override
    public void onDefaultAddressSuccess(AddressContactModel addressContactModel) {
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter().length() > 0) {
            return;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile().length() > 0) {
            return;
        }
        if (addressContactModel != null) {
            if (addressContactModel.getName() != null && addressContactModel.getName().length() > 0) {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setConnecter(addressContactModel.getName());
            }
            if (addressContactModel.getMobile() != null && addressContactModel.getMobile().length() > 0) {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setConnecter_mobile(addressContactModel.getMobile());
            }
        }
    }
    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.addfield_main_fieldinfo_layout) {
                Intent supplement_intent = new Intent(FieldAddFieldMainActivity.this,FieldAddFieldPhysicalResActivity.class);
                supplement_intent.putExtra("is_radect",1);
                startActivity(supplement_intent);
            } else if (v.getId() == R.id.addfield_main_activityinfo_layout) {
                Intent supplement_intent = new Intent(FieldAddFieldMainActivity.this,FieldAddfieldActivityInfoActivity.class);
                startActivity(supplement_intent);
            } else if (v.getId() == R.id.addfield_main_fieldprice_layout) {
                Intent supplement_intent = new Intent(FieldAddFieldMainActivity.this,Field_AddField_FieldPricesActivity.class);
                startActivity(supplement_intent);
            } else if (v.getId() == R.id.addfield_main_fieldrules_layout) {
                Intent supplement_intent = new Intent(FieldAddFieldMainActivity.this,FieldAddfieldCommunityInfoActivity.class);
                supplement_intent.putExtra("is_radect",1);
                startActivity(supplement_intent);
            } else if (v.getId() == R.id.addfield_main_fieldservices_layout) {
                if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {

                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_minimum_order_quantity_choose_activitytime_text));
                        return;
                    }
                }
                Intent supplement_intent = new Intent(FieldAddFieldMainActivity.this,FieldAddFieldServicesItemsActivity.class);
                startActivity(supplement_intent);
            } else if (v.getId() == R.id.addfield_other_contact_layout) {
                Intent characteristic_intent = new Intent(FieldAddFieldMainActivity.this,FieldAddFieldOtherContactAccountActivity.class);
                startActivity(characteristic_intent);
            }
        }
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (mAddfieldMainSuccessLL.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                BaseMessageUtils.showDialog(getContext(), getResources().getString(R.string.dialog_prompt),
                        getResources().getString(R.string.addfield_optional_info_savedata_dialog_text),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setCommunity_id(null);
                                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setPhysical_id(null);
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().setId(0);
                                Field_AddResourcesModel.getInstance().getResource().setIsRedact(0);
                                LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
                                finish();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //场地信息清空 设置资源类型
                                int res_type_id =  Field_AddResourcesModel.getInstance().getResource().getRes_type_id();
                                Field_AddResourcesInfoModel resource = new Field_AddResourcesInfoModel();
                                Field_AddResourcesModel.getInstance().setResource(resource);
                                Field_AddResourcesModel.getInstance().setPriceConfigNull();
                                Field_AddResourcesModel.getInstance().getResource().setRes_type_id(res_type_id);
                                LoginManager.getInstance().setAddfield_resourcesmodel(JSON.toJSONString(Field_AddResourcesModel.getInstance(),false));
                                finish();
                            }
                        });
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private boolean checkInput_fieldifno() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img() == null ||
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size() == 0) {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() != null) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location().length() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type_id() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type_id() > 0 &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name().length() > 0 ) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type_id() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type_id() > 0 &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name().length() > 0 ) {
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name().equals(
                    getResources().getString(R.string.addfield_optional_info_else_text))) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_location_type() == null ||
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_location_type().length() == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area().length() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getNumber_of_people() != null) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin().length() > 0) {

        } else {
            return false;
        }
        if(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish().length() > 0) {

        } else {
            return false;
        }
        return result;
    }
    private boolean checkInputActivityInfo() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img() == null ||
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size() == 0) {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),0)) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_start_tiem_error_remind_text));
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date("");
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline("");
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0 &&
                    Constants.iseditoractivitydate(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline(),0)) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_finish_tiem_error_remind_text));
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date("");
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline("");
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                if (Constants.compare_start_finish_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline())) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_time_prompt));
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date("");
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline("");
                    return false;
                }
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0) {

            } else {
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {

            } else {
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type_id() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type_id() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type().getDisplay_name() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type().getDisplay_name().length() > 0) {

            } else {
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getCustom_name() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getCustom_name().length() > 0) {

            } else {
                return false;
            }
        }
        return result;
    }

    private boolean checkInputFieldContact() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter().length() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile().length() > 0) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().size() > 0) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount_owner() == null ||
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount_owner().length() == 0) {
                return false;
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type() == null ||
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type().length() == 0) {
                return false;
            } else {
                if (Integer.parseInt(Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type()) != 1) {
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount() == null ||
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount().length() == 0) {
                        return false;
                    }
                    if (Integer.parseInt(Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type()) == 2) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getOpening_bank() == null ||
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getOpening_bank().length() == 0) {
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return result;
    }

    private boolean checkInput_fieldprice() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getIs_enquiry() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getIs_enquiry() == 1) {
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                return false;
            } else {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_max_price()!= null &&
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_min_price() != null) {

                } else {
                    return false;
                }
            }
        } else {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions()!= null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {

            } else {
                return false;
            }
        }
        return result;
    }
    private boolean checkInput_serviceitem() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() > -1) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_power() != null) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_chair() != null) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getOvernight_material() != null) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getLeaflet() != null) {

        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_tent() != null) {

        } else {
            return false;
        }

        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getInvoice() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getInvoice() == 1) {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getTax_point() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getTax_point().length() > 0) {

                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getSupport_rescheduling() == null) {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getSupport_rescheduling() == 1) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReschedule_in_advance() == null) {
                return false;
            }
        }
        return result;
    }
    private boolean checkInputCommunityInfo() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img() == null ||
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size() == 0) {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity_id() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity().getName() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCity().getName().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict_id() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict().getName() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDistrict().getName().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getName() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getName().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAddress() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAddress().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuild_year() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getBuild_year().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory_id() != null) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDescription() != null &&
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getDescription().length() > 0) {
        } else {
            return false;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getAttributes().size() > 0) {
            for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getAttributes().size(); i++) {
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getAttributes().get(i).getRequired() == 1) {
                    if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().size() > 0) {
                        boolean isChose = false;
                        for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().size(); j++) {
                            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getAttributes().get(j).getId() ==
                                    Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getCategory().getAttributes().get(i).getId()) {
                                isChose = true;
                                break;
                            }
                        }
                        if (!isChose) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        //属性判断
        return result;
    }

    private  boolean checkInput_activity_date() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null) {
                    if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                        return false;
                    }
                }
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days() != null) {
                    if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                        if (Constants.two_date_discrepancy(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline()) < Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days()) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return result;
    }
    private  boolean checkInput_activity_priceunit() {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                HashMap<Object, Object> map = new HashMap<>();
                for (int i = 0; i< Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size(); i++) {
                    map.put(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId(),
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getPeriod());
                }
                if ( Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0
                        && map.size() > 0) {
                    for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                        if (map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) != null &&
                                map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()).toString().length() > 0 &&
                                Integer.parseInt(map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()).toString()) != 1) {
                            return false;
                        }
                    }
                }

            } else {
                return false;
            }
        }
        return result;
    }
    //活动时间冲突后设置信息
    private void save_initial_data_state() {
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date("");
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline("");
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDays_in_advance(null);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days() != null) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setMinimum_booking_days(1);
        }
        Field_AddResourcesModel.getInstance().setIs_hava_field_info(0);
        Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
        Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
        Field_AddResourcesModel.getInstance().setIs_hava_field_services(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SEARCH_CHOOSE_CODE:
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
