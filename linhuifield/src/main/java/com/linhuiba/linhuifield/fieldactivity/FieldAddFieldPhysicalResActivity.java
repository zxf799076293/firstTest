package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.connector.OnMultiClickListener;
import com.linhuiba.linhuifield.fieldadapter.FieldBannerImageLoader;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;
import com.linhuiba.linhuifield.fieldview.WheelView;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class FieldAddFieldPhysicalResActivity extends FieldBaseMvpActivity {
    private TextView mSpinner;
    private EditText mTxt_people;
    private TextView time_begin;
    private TextView time_finish;
    private TextView mAddfieldResTypeTV;
    private TextView mtxt_desp_choose_text;
    private TextView mAddfieldPhyLocatonTypeTV;
    private EditText mAddfieldPhyOtherLocatonTypeET;
    private LinearLayout mAddfieldPhyOtherLocatonTypeLL;
    private EditText mresources_name;
    private EditText mTxt_title;
    private ScrollView mscrollView;
    private TextView mPhyResDescriptionsET;
    private LinearLayout mPhyResDescriptionsLL;
    private TextView mtxt_timebegin;
    private TextView mtxt_timefinish;
    private EditText mtxt_field_allsize;
//    private TextView mtxt_activity_timebegin;
//    private TextView mtxt_activity_timefinish;
//    private LinearLayout madd_activity_layout;
    private TextView maddfield_number_of_people_show_direction_text;
    private LinearLayout mAddfieldResInfoLL;
    private Button mAddfieldNextBtn;
    //banner
    private Banner mAddfieldMainBanner;
    private LinearLayout mAddfieldBannerDefaultLL;
    private LinearLayout mAddfieldBannerNoLL;
    private RelativeLayout mAddfieldBannerRL;
    private LinearLayout mAddfieldChoosell;
    private LinearLayout mAddfieldAddll;
    private TextView mAddfieldChooseName;
    private TextView mAddfieldChooseDoLocation;
    private TextView mAddfieldChooseType;
    private TextView mAddfieldChooseArea;
    private TextView mAddfieldChooseTime;
    private TextView mAddfieldChooseNumOfPeople;
    private TextView mAddfieldChooseFacade;
//    private EditText mPhyResActivityNameET;
    private LinearLayout mPhyResFirstLL;
    private LinearLayout mPhyResLastLL;
//    private LinearLayout mActivityTypeLL;
//    private TextView mActivityTypeTV;
    private ArrayList<String> mPicList = new ArrayList<>();
    private DisplayMetrics mDisplayMetrics;
    private FieldBannerImageLoader mAddfieldMainImgLoader;
    private int height;
    private int width;
    private TimePickerDialog time_begindialog;
    private int begin_hour = 8,begin_minute = 0,finish_hour = 18,finish_minute = 0;
    private InputMethodManager mManager;
    private int Field_type_id;
    private String begin_date = "";
    private String finish_date = "";
    private int begin_year;
    private int begin_month;
    private int begin_day;
    private int finish_year;
    private int finish_month;
    private int finish_day;
    private Dialog newdialog;
    private int wheelview_select_int = -1;
    private int facade;//展示方向(可选值1~4)
    private String mDescriptionStr;
    private int isAddfieldMainIntent;
    private int mLocationTypeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_fragment_add_field);
        initview();
    }
    private void initview() {
        mAddfieldResInfoLL = (LinearLayout)findViewById(R.id.fragmentaddfield_all_layout);
        mAddfieldNextBtn = (Button)findViewById(R.id.addfield_next_btn);
        mSpinner = (TextView)findViewById(R.id.spinner);
        mTxt_people =(EditText)findViewById(R.id.txt_people);
        time_begin = (TextView)findViewById(R.id.txt_timebegin);
        time_finish =  (TextView)findViewById(R.id.txt_timefinish);
        mAddfieldResTypeTV = (TextView)findViewById(R.id.addfield_res_type_tv);
        mtxt_desp_choose_text = (TextView)findViewById(R.id.txt_desp_choose_text);
        mAddfieldPhyLocatonTypeTV = (TextView)findViewById(R.id.addfield_physical_location_type_tv);
        mAddfieldPhyOtherLocatonTypeET = (EditText)findViewById(R.id.addfield_physical_other_location_type_et);
        mAddfieldPhyOtherLocatonTypeLL = (LinearLayout)findViewById(R.id.addfield_physical_other_location_type_ll);
        mresources_name =(EditText)findViewById(R.id.txt_title);
        mTxt_title =(EditText)findViewById(R.id.txt_desp);
        mscrollView =(ScrollView)findViewById(R.id.scrollView) ;

        mtxt_field_allsize =(EditText)findViewById(R.id.txt_field_allsize) ;
        mtxt_timebegin = (TextView)findViewById(R.id.txt_timebegin);
        mtxt_timefinish = (TextView)findViewById(R.id.txt_timefinish);
        maddfield_number_of_people_show_direction_text = (TextView)findViewById(R.id.addfield_number_of_people_show_direction_text);
        mAddfieldChoosell = (LinearLayout)findViewById(R.id.addfeield_phy_res_choose_info_ll);
        mAddfieldAddll = (LinearLayout)findViewById(R.id.field_add_phy_res_ll);
        mAddfieldChooseName = (TextView)findViewById(R.id.addfeield_phy_res_choose_info_name);
        mAddfieldChooseDoLocation = (TextView)findViewById(R.id.addfeield_phy_res_choose_info_docation);
        mAddfieldChooseType = (TextView)findViewById(R.id.addfeield_phy_res_choose_info_type);
        mAddfieldChooseArea = (TextView)findViewById(R.id.addfeield_phy_res_choose_info_area);
        mAddfieldChooseTime = (TextView)findViewById(R.id.addfeield_phy_res_choose_info_time);
        mAddfieldChooseNumOfPeople= (TextView)findViewById(R.id.addfeield_phy_res_choose_info_num_of_people);
        mAddfieldChooseFacade = (TextView)findViewById(R.id.addfeield_phy_res_choose_facade);
        mPhyResDescriptionsET =(TextView)findViewById(R.id.phy_res_description_et);
        mPhyResDescriptionsLL =(LinearLayout)findViewById(R.id.phy_res_description_ll);
        mPhyResFirstLL =(LinearLayout)findViewById(R.id.addfield_phy_res_first_ll);
        mPhyResLastLL =(LinearLayout)findViewById(R.id.addfield_phy_res_last_ll);
        Constants.textchangelistener(mtxt_field_allsize);
        onFunctionClick();
        mscrollView.setVerticalScrollBarEnabled(false);
        mscrollView.setHorizontalScrollBarEnabled(false);
        mManager = (InputMethodManager) FieldAddFieldPhysicalResActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //banner
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        height = width * 300 / 375;
        mAddfieldMainBanner = (Banner)findViewById(R.id.addfield_phy_res_banner);
        mAddfieldBannerDefaultLL = (LinearLayout) findViewById(R.id.addfield_phy_res_default_pic_ll);
        mAddfieldBannerNoLL = (LinearLayout) findViewById(R.id.addfield_phy_res_no_pic_ll);
        mAddfieldBannerRL = (RelativeLayout) findViewById(R.id.addfield_phy_res_banner_ll);
        mAddfieldMainImgLoader = new FieldBannerImageLoader(FieldAddFieldPhysicalResActivity.this,375,300);
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1) {
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIs_not_check() == 1) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                    } else {
                        if (checkInput()) {
                            Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                        } else {
                            Field_AddResourcesModel.getInstance().setIs_hava_field_info(0);
                        }
                        saveData();
                    }
                    finish();
                } else {
                    finish();
                }
            }
        });
        mSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBoard();
                showdialog(mSpinner,Field_AddResourcesModel.getInstance().getOptions().getField_type(),2);
            }
        });
        maddfield_number_of_people_show_direction_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Field_AddResourceCreateItemModel> list = new ArrayList<Field_AddResourceCreateItemModel>();
                for (int i = 0 ; i < 4; i++) {
                    Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
                    field_addResourceCreateItemModel.setDisplay_name(String.valueOf(i+1)+getResources().getString(R.string.addfield_optional_info_facade_text));
                    field_addResourceCreateItemModel.setId(i + 1);
                    list.add(field_addResourceCreateItemModel);
                }
                showdialog(maddfield_number_of_people_show_direction_text,list,5);
            }
        });
        mtxt_desp_choose_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Field_AddResourceCreateItemModel> list = new ArrayList<Field_AddResourceCreateItemModel>();
                for (int i = 0 ; i < 2; i++) {
                    Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
                    if ( i== 0) {
                        field_addResourceCreateItemModel.setDisplay_name(getResources().getString(R.string.addfield_do_location_indoor_text));
                    } else if (i== 1) {
                        field_addResourceCreateItemModel.setDisplay_name(getResources().getString(R.string.addfield_do_location_outdoor_text));
                    }
                    list.add(field_addResourceCreateItemModel);
                }
                showdialog(mtxt_desp_choose_text,list,3);
            }
        });
        mAddfieldPhyLocatonTypeTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                showdialog(mAddfieldPhyLocatonTypeTV,Field_AddResourcesModel.getInstance().getOptions().getLocation_type(),1);
            }
        });
        mSpinner.setText("");
        mAddfieldNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1 &&
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIsSearchChoose() == 0) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                        saveData();
                        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 1) {
                            Intent addfour = new Intent(FieldAddFieldPhysicalResActivity.this,Field_AddField_FieldPricesActivity.class);
                            startActivity(addfour);
                        } else {
                            Intent addfour = new Intent(FieldAddFieldPhysicalResActivity.this,FieldAddfieldActivityInfoActivity.class);
                            startActivity(addfour);
                        }
                    }
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                    if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 1) {
                        Intent addfour = new Intent(FieldAddFieldPhysicalResActivity.this,Field_AddField_FieldPricesActivity.class);
                        startActivity(addfour);
                    } else {
                        Intent addfour = new Intent(FieldAddFieldPhysicalResActivity.this,FieldAddfieldActivityInfoActivity.class);
                        startActivity(addfour);
                    }
                }
            }
        });
        mPhyResDescriptionsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FieldAddFieldPhysicalResActivity.this,FieldAddfieldEditTextActivity.class);
                intent.putExtra("common_type",1);
                intent.putExtra("str_size",255);
                if (mDescriptionStr != null) {
                    intent.putExtra("edit_str",mDescriptionStr);
                }
                startActivityForResult(intent,2);
            }
        });
        mAddfieldBannerDefaultLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1 &&
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIsSearchChoose() == 0) {
                    Intent addfield_three = new Intent(FieldAddFieldPhysicalResActivity.this, Field_AddField_UploadingPictureActivity.class);
                    addfield_three.putExtra("picture_type",2);
                    startActivityForResult(addfield_three,1);
                }
            }
        });
        Intent intent =  getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("is_radect") != null) {
            isAddfieldMainIntent = intent.getExtras().getInt("is_radect");
        }
        //编辑或添加页面的显示
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1 &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIsSearchChoose() == 0) {
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 1) {
                TitleBarUtils.setTitleText(this, FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.module_addfield_phy_res_title));
            }
            if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
                TitleBarUtils.shownextTextView(this, "",
                        17,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkInput()) {
                                    Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                                    saveData();
                                    finish();
                                }
                            }
                        }
                );
                mAddfieldNextBtn.setVisibility(View.GONE);
            }
            mAddfieldNextBtn.setText(getResources().getString(R.string.addfield_rules_next_btn_str));
            mAddfieldChoosell.setVisibility(View.GONE);
            mAddfieldAddll.setVisibility(View.VISIBLE);
        } else {
            TitleBarUtils.setTitleText(this, FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.module_addfield_main_phy_res_info));
            if (isAddfieldMainIntent == 1) {
                mAddfieldNextBtn.setVisibility(View.GONE);
            } else {
                mAddfieldNextBtn.setVisibility(View.VISIBLE);
                mAddfieldNextBtn.setText(getResources().getString(R.string.addfield_rules_next_btn_str));
            }
            mAddfieldChoosell.setVisibility(View.VISIBLE);
            mAddfieldAddll.setVisibility(View.GONE);
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName() != null) {
                mAddfieldChooseName.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location() != null) {
                mAddfieldChooseDoLocation.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name() != null) {
                mAddfieldChooseType.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area() != null) {
                mAddfieldChooseArea.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish().length() > 0) {
                mAddfieldChooseTime.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin() + "--" +
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getNumber_of_people() != null) {
                mAddfieldChooseNumOfPeople.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getNumber_of_people()));
            }
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade() != null) {
                mAddfieldChooseFacade.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade()) +
                        getResources().getString(R.string.addfield_optional_info_facade_text));
            }
        }
        showData();
        //显示图片
        updataBanner();
    }
    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
    }
    private  void closeBoard() {
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(this.findViewById(R.id.fragmentaddfield_all_layout).getWindowToken(), 0); //强制隐藏键盘
    }
    //时间选择后的处理
    private void getbegindialog() {
        Calendar calendar = Calendar.getInstance();
        time_begindialog=new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        begin_hour = hourOfDay;
                        begin_minute = minute;
                        String hourOfDaytmp = String.valueOf(hourOfDay);
                        String minutetmp = String.valueOf(minute);
                        if (hourOfDay < 10) {
                            hourOfDaytmp = "0"+String.valueOf(hourOfDay);
                        }
                        if (minute < 10) {
                            minutetmp = "0"+String.valueOf(minute);
                        }
                        if (time_finish.getText().toString().equals("")) {
                            time_begin.setText(hourOfDaytmp + ":" + minutetmp);
                        } else {
                            if (!(hourOfDay < finish_hour)) {
                                BaseMessageUtils.showToast(getContext(),FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.toast_time_prompt));
                                time_begin.setText("");
                                time_finish.setText("");
                            } else if (hourOfDay == finish_hour) {
                                if (!(minute < finish_hour)) {
                                    BaseMessageUtils.showToast(getContext(),FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.toast_time_prompt));
                                    time_begin.setText("");
                                    time_finish.setText("");
                                } else {
                                    time_begin.setText(hourOfDaytmp + ":" + minutetmp);
                                }
                            } else {
                                time_begin.setText(hourOfDaytmp + ":" + minutetmp);
                            }
                        }
                    }
                },8,0,true);
        time_begindialog.show();
    }
    //地推时间选择控件的点击事件及处理
    private void getfinishdialog() {
        Calendar calendar = Calendar.getInstance();
        time_begindialog=new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        finish_hour = hourOfDay;
                        finish_minute = minute;
                        String hourOfDaytmp = String.valueOf(hourOfDay);
                        String minutetmp = String.valueOf(minute);
                        if (hourOfDay < 10) {
                            hourOfDaytmp = "0"+String.valueOf(hourOfDay);
                        }
                        if (minute < 10) {
                            minutetmp = "0"+String.valueOf(minute);
                        }
                        if(time_begin.getText().toString().equals("")){
                            time_finish.setText(hourOfDaytmp + ":" + minutetmp);
                        } else {
                            if (hourOfDay < begin_hour ) {
                                BaseMessageUtils.showToast(getContext(),FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.toast_time_prompt));
                                time_begin.setText("");
                                time_finish.setText("");
                            } else if (hourOfDay == begin_hour) {
                                if (minute < begin_minute) {
                                    BaseMessageUtils.showToast(getContext(),FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.toast_time_prompt));
                                    time_begin.setText("");
                                    time_finish.setText("");
                                } else {
                                    time_finish.setText(hourOfDaytmp + ":" + minutetmp);
                                }
                            } else {
                                time_finish.setText(hourOfDaytmp + ":" + minutetmp);
                            }
                        }
                    }
                },18,0,true);
        time_begindialog.show();
    }
    //活动时间选择控件的点击事件及处理
    private void onFunctionClick() {
        mtxt_timebegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getbegindialog();
            }
        });
        mtxt_timefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getfinishdialog();
            }
        });
    }
    private void saveData() {
        if (Field_type_id > 0) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setField_type_id(Field_type_id);
            Field_AddResourceCreateItemModel fieldtype = new Field_AddResourceCreateItemModel();
            fieldtype.setDisplay_name(mSpinner.getText().toString());
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setField_type(fieldtype);
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setField_type_id(null);
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setField_type(null);
        }
        if (mLocationTypeId > 0) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setLocation_type_id(mLocationTypeId);
            Field_AddResourceCreateItemModel locationType = new Field_AddResourceCreateItemModel();
            locationType.setDisplay_name(mAddfieldPhyLocatonTypeTV.getText().toString());
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setLocation_type(locationType);
            if (mAddfieldPhyLocatonTypeTV.getText().toString().equals(
                    getResources().getString(R.string.addfield_optional_info_else_text)) &&
                    mAddfieldPhyOtherLocatonTypeET.getText().toString().length() > 0) {
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setOther_location_type(mAddfieldPhyOtherLocatonTypeET.getText().toString());
            } else {
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setOther_location_type(null);
            }
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setLocation_type_id(null);
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setLocation_type(null);
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setOther_location_type(null);
        }
        if (mtxt_desp_choose_text.getText().toString().equals(getResources().getString(R.string.addfield_do_location_indoor_text))) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIndoor(1);
        } else if (mtxt_desp_choose_text.getText().toString().equals(getResources().getString(R.string.addfield_do_location_outdoor_text))) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIndoor(0);
        }
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setName(mresources_name.getText().toString().trim());//标题
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setDo_location(mTxt_title.getText().toString().trim());//详细位置
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setTotal_area(mtxt_field_allsize.getText().toString().trim());
        if (mTxt_people.getText().toString().length() > 0) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setNumber_of_people(Integer.parseInt(mTxt_people.getText().toString().trim()));
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setNumber_of_people(null);
        }
        if (facade > 0) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setFacade(facade);
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setFacade(null);
        }
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setDo_begin(time_begin.getText().toString().trim());
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setDo_finish(time_finish.getText().toString().trim());
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setDescription(mDescriptionStr);
    }

    private boolean checkInput() {
        boolean result = false;
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img() == null ||
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.mPic_map_prompt));
            return result;
        }
        if (mtxt_desp_choose_text.getText().toString().length() == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.addfield_desp_type_prompt));
            return result;
        }
        if (TextUtils.getTrimmedLength(mAddfieldPhyLocatonTypeTV.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.module_addfield_phy_location_type_hint));
            return result;
        }
        if (mLocationTypeId  == 0) {
            BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.module_addfield_phy_location_type_hint));
            return result;
        }
        if (mAddfieldPhyLocatonTypeTV.getText().toString().equals(
                getResources().getString(R.string.addfield_optional_info_else_text))) {
            if (mAddfieldPhyOtherLocatonTypeET.getText().toString().length() == 0) {
                BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.module_addfield_phy_other_location_type_hint));
                return result;
            }
        }
        if (TextUtils.getTrimmedLength(mresources_name.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.mTxt_title_prompt));
            return result;
        }
        if (TextUtils.getTrimmedLength(mTxt_title.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.module_addfield_phy_res_info_specific_location_hint));
            return result;
        }
        if (TextUtils.getTrimmedLength(mSpinner.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.txt_field_type_hint));
            return result;
        }
        if (Field_type_id  == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.txt_field_type_hint));
            return result;
        }
        if (TextUtils.getTrimmedLength(mtxt_field_allsize.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.txt_field_total_area_hint));
            return result;
        }
        if (TextUtils.getTrimmedLength(time_begin.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.time_begin_prompt));
            return result;
        }
        if (TextUtils.getTrimmedLength(time_finish.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.time_finish_prompt));
            return result;
        }
        if (mDescriptionStr == null || mDescriptionStr.length() == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.mTxt_desp_prompt));
            return result;
        }
        if (TextUtils.getTrimmedLength(mTxt_people.getText()) == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.mTxt_people_prompt));
            return result;
        }
        if (facade == 0) {
            BaseMessageUtils.showToast(getContext(), FieldAddFieldPhysicalResActivity.this.getResources().getString(R.string.addfield_optional_info_facade_hinttext));
            return result;
        }
        return true;
    }
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (FieldAddFieldPhysicalResActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (FieldAddFieldPhysicalResActivity.this.getCurrentFocus() != null)
                mManager.hideSoftInputFromWindow(FieldAddFieldPhysicalResActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void showdialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        final WheelView mwheelview= (WheelView) textEntryView.findViewById(R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        int clickPosition = -1;
        for (int i = 0; i < list.size(); i ++) {
            wheelview_list.add(list.get(i).getDisplay_name());
            if (type == 2) {
                if (Field_type_id == list.get(i).getId()) {
                    clickPosition = i;
                }
            } else if (type == 5) {
                if (facade == list.get(i).getId()) {
                    clickPosition = i;
                }
            } else if (type == 1) {
                if (mLocationTypeId == list.get(i).getId()) {
                    clickPosition = i;
                }
            } else if (type == 3) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() == 1) {
                    clickPosition = 0;
                } else {
                    clickPosition = 1;
                }
            }
        }
        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (clickPosition > -1) {
            wheelview_select_int = clickPosition;
            mwheelview.setSeletion(clickPosition);
        } else {
            if (list.size() > 2) {
                wheelview_select_int = 2;
                mwheelview.setSeletion(2);
            } else if (list.size() > 1) {
                wheelview_select_int = 1;
                mwheelview.setSeletion(1);
            } else {
                wheelview_select_int = 0;
                mwheelview.setSeletion(0);
            }
        }
        mwheelview.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelview_select_int = selectedIndex - 2;
            }
        });
        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(list.get(wheelview_select_int).getDisplay_name());
                if (type == 2) {
                    Field_type_id = list.get(wheelview_select_int).getId();
                } else if (type == 5) {
                    facade = list.get(wheelview_select_int).getId();
                } else if (type == 1) {
                    mLocationTypeId = list.get(wheelview_select_int).getId();
                    if (list.get(wheelview_select_int).getDisplay_name().equals(
                            getResources().getString(R.string.addfield_optional_info_else_text))) {
                        mAddfieldPhyOtherLocatonTypeLL.setVisibility(View.VISIBLE);
                    } else {
                        mAddfieldPhyOtherLocatonTypeLL.setVisibility(View.GONE);
                    }
                }
                if (newdialog != null) {
                    newdialog.dismiss();
                }
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog != null) {
                    newdialog.dismiss();
                }
            }
        });

        newdialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,newdialog);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIs_not_check() == 1) {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                } else {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_info(1);
                    } else {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_info(0);
                    }
                    saveData();
                }
                finish();
            } else {
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    //banner
    private void updataBanner() {
        if (mPicList != null) {
            mPicList.clear();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size() > 0) {
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size(); i++) {
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url().length() > 0) {
                        mPicList.add(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url()
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
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 0) {
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
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1 &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIsSearchChoose() == 0) {
            mAddfieldMainBanner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent addfield_three = new Intent(FieldAddFieldPhysicalResActivity.this, Field_AddField_UploadingPictureActivity.class);
                    addfield_three.putExtra("click_position",position - 1);
                    addfield_three.putExtra("picture_type",2);
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
                            mPhyResDescriptionsET.setText(mDescriptionStr);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showData() {
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type_id() != null ) {
            Field_type_id = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type_id();
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name() != null) {
                mSpinner.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getField_type().getDisplay_name());
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type_id() != null ) {
            mLocationTypeId = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type_id();
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name() != null) {
                mAddfieldPhyLocatonTypeTV.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name());
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getLocation_type().getDisplay_name().equals(
                        getResources().getString(R.string.addfield_optional_info_else_text))) {
                    mAddfieldPhyOtherLocatonTypeLL.setVisibility(View.VISIBLE);
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_location_type() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_location_type().length() > 0) {
                        mAddfieldPhyOtherLocatonTypeET.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_location_type());
                    }
                }
            }
        }

        //摆摊位置下拉框实现
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() == 1) {
                mtxt_desp_choose_text.setText(getResources().getString(R.string.addfield_do_location_indoor_text));
            } else if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getIndoor() == 0) {
                mtxt_desp_choose_text.setText(getResources().getString(R.string.addfield_do_location_outdoor_text));
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName().length() > 0) {
            mresources_name.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getName());//标题
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location().length() > 0) {
            mTxt_title.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_location());//详细位置
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area().length() > 0) {
            mtxt_field_allsize.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getTotal_area()));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getNumber_of_people() != null) {
            mTxt_people.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getNumber_of_people()));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade()  > 0) {
            facade = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getFacade();
            maddfield_number_of_people_show_direction_text.setText(String.valueOf(facade)+getResources().getString(R.string.addfield_optional_info_facade_text));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin() != null) {
            time_begin.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin());
            String[] timebeginstr = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_begin().split(":");
            begin_hour = Integer.parseInt(Constants.subZeroAndDot(timebeginstr[0]));
            begin_minute = Integer.parseInt(Constants.subZeroAndDot(timebeginstr[1]));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish() != null) {
            time_finish.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish());
            String[] timebeginstr = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDo_finish().split(":");
            finish_hour = Integer.parseInt(Constants.subZeroAndDot(timebeginstr[0]));
            finish_minute = Integer.parseInt(Constants.subZeroAndDot(timebeginstr[1]));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDescription() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDescription().length() > 0) {
            mPhyResDescriptionsET.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDescription());
            mDescriptionStr = Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getDescription();
        }
    }
}
