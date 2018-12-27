package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldadapter.FieldBannerImageLoader;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldview.WheelView;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FieldAddfieldActivityInfoActivity extends FieldBaseMvpActivity {
    private EditText mActivityNameET;
    private LinearLayout mActivityTypeLL;
    private TextView mActivityTypeTV;
    private TextView mtxt_activity_timebegin;
    private TextView mtxt_activity_timefinish;
    private LinearLayout madd_activity_layout;
    private Banner mAddfieldActivityBanner;
    private LinearLayout mAddfieldBannerDefaultLL;
    private LinearLayout mAddfieldBannerNoLL;
    private RelativeLayout mAddfieldBannerRL;
    private LinearLayout mAddfieldActivityBtnLL;
    private Button mAddfieldActivityBtn;
    private TextView mPhyResDescriptionsET;
    private LinearLayout mPhyResDescriptionsLL;
    private String begin_date = "";
    private String finish_date = "";
    private int begin_year;
    private int begin_month;
    private int begin_day;
    private int finish_year;
    private int finish_month;
    private int finish_day;
    private int mActivityTypeId;
    private int wheelview_select_int = -1;
    private Dialog newdialog;
    private DisplayMetrics mDisplayMetrics;
    private FieldBannerImageLoader mAddfieldMainImgLoader;
    private int height;
    private int width;
    private ArrayList<String> mPicList = new ArrayList<>();
    private String mDescriptionStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_field_activity_activityinfo);
        initView();
    }
    private void initView() {
        mActivityNameET =(EditText)findViewById(R.id.activity_title);
        mActivityTypeLL =(LinearLayout)findViewById(R.id.addfield_activity_type_ll);
        mActivityTypeTV =(TextView)findViewById(R.id.addfield_activity_type_tv);
        mtxt_activity_timebegin = (TextView)findViewById(R.id.txt_activity_timebegin);
        mtxt_activity_timefinish = (TextView)findViewById(R.id.txt_activity_timefinish);
        madd_activity_layout = (LinearLayout)findViewById(R.id.add_activity_layout);
        mAddfieldActivityBanner = (Banner)findViewById(R.id.addfield_activityinfo_banner);
        mActivityTypeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(mActivityTypeTV,Field_AddResourcesModel.getInstance().getOptions().getActivity_type(),1);
            }
        });
        onDateClick();
        //banner
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        height = width * 300 / 375;
        mAddfieldActivityBanner = (Banner)findViewById(R.id.addfield_activityinfo_banner);
        mAddfieldBannerDefaultLL = (LinearLayout) findViewById(R.id.addfield_activityinfo_default_pic_ll);
        mAddfieldBannerNoLL = (LinearLayout) findViewById(R.id.addfield_activityinfo_no_pic_ll);
        mAddfieldBannerRL = (RelativeLayout) findViewById(R.id.addfield_activityinfo_banner_ll);
        mAddfieldActivityBtn = (Button) findViewById(R.id.addfield_activityinfo_btn);
        mAddfieldActivityBtnLL = (LinearLayout) findViewById(R.id.addfield_activityinfo_btn_ll);
        mPhyResDescriptionsET =(TextView)findViewById(R.id.activityinfo_description_et);
        mPhyResDescriptionsLL =(LinearLayout)findViewById(R.id.activityinfo_description_ll);
        mAddfieldMainImgLoader = new FieldBannerImageLoader(FieldAddfieldActivityInfoActivity.this,375,300);
        mAddfieldBannerDefaultLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2018/11/1 活动图片跳转
                Intent addfield_three = new Intent(FieldAddfieldActivityInfoActivity.this, Field_AddField_UploadingPictureActivity.class);
                addfield_three.putExtra("picture_type",3);
                startActivityForResult(addfield_three,1);
            }
        });
        mPhyResDescriptionsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FieldAddfieldActivityInfoActivity.this,FieldAddfieldEditTextActivity.class);
                intent.putExtra("common_type",1);
                intent.putExtra("str_size",255);
                if (mDescriptionStr != null) {
                    intent.putExtra("edit_str",mDescriptionStr);
                }
                startActivityForResult(intent,2);
            }
        });

        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_addfield_activity_title));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_activity_info(1);
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_activity_info(0);
                }
                saveDataActivityInfo();
                finish();
            }
        });
        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
            TitleBarUtils.shownextTextView(this, "",
                    17,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkInput()) {
                                Field_AddResourcesModel.getInstance().setIs_hava_activity_info(1);
                                saveDataActivityInfo();
                                finish();
                            }
                        }
                    }
            );
            mAddfieldActivityBtnLL.setVisibility(View.GONE);
        } else {
            mAddfieldActivityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_activity_info(1);
                        saveDataActivityInfo();
                        Intent addfour = new Intent(FieldAddfieldActivityInfoActivity.this,Field_AddField_FieldPricesActivity.class);
                        startActivity(addfour);
                    }
                }
            });
        }
        showData();
        //显示图片
        updataBanner();

    }
    private void onDateClick() {
        mtxt_activity_timebegin.setOnClickListener(new View.OnClickListener() {
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
                datedialog = new DatePickerDialog(FieldAddfieldActivityInfoActivity.this,
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
                        begin_year = datePicker.getYear();
                        begin_month = datePicker.getMonth()+1;
                        begin_day = datePicker.getDayOfMonth();
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null) {
                            if (Constants.compare_date(year + "-" + (month + 1) + "-" + day,Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_reservation_date_conflict_text));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                activity_data_conflict_refresh();
                                return;
                            }
                        }
                        if (mtxt_activity_timefinish.getText().toString().length()  == 0) {
                            mtxt_activity_timebegin.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            begin_date = year + "-" + (month + 1) + "-" + day;
                        } else {
                            if (begin_year > finish_year) {
                                BaseMessageUtils.showToast(getContext(),FieldAddfieldActivityInfoActivity.this.getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month > finish_month) {
                                BaseMessageUtils.showToast(getContext(),FieldAddfieldActivityInfoActivity.this.getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month == finish_month && begin_day > finish_day) {
                                BaseMessageUtils.showToast(getContext(),FieldAddfieldActivityInfoActivity.this.getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (!checkInput_activity_data_conflict(year + "-" +
                                    (month + 1) + "-" + day,finish_date)) {
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                activity_data_conflict_refresh();
                                return;
                            }
                            mtxt_activity_timebegin.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            begin_date = year + "-" + (month + 1) + "-" + day;
                        }

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
        mtxt_activity_timefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();//当前时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);//设置当前日期
                calendar.add(Calendar.DAY_OF_MONTH, 0);//天数加
                final DatePickerDialog datedialog;
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker,
                                                  int year, int month, int dayOfMonth) {

                            }
                        };
                datedialog = new DatePickerDialog(FieldAddfieldActivityInfoActivity.this,
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
                        finish_year = datePicker.getYear();
                        finish_month = datePicker.getMonth()+1;
                        finish_day = datePicker.getDayOfMonth();
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() == null) {

                        } else {
                            if (Constants.compare_date(year + "-" + (month + 1) + "-" + day,Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_reservation_date_conflict_text));
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                activity_data_conflict_refresh();
                                return;
                            }
                        }
                        if (mtxt_activity_timebegin.getText().toString().length()  == 0) {
                            mtxt_activity_timefinish.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            finish_date = year + "-" + (month + 1) + "-" + day;
                        } else {
                            if (begin_year > finish_year) {
                                BaseMessageUtils.showToast(getContext(),getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month > finish_month) {
                                BaseMessageUtils.showToast(getContext(),getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month == finish_month && begin_day > finish_day) {
                                BaseMessageUtils.showToast(getContext(),getResources().getString(R.string.toast_time_prompt));
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                return;
                            }
                            if (!checkInput_activity_data_conflict(begin_date,year + "-" +
                                    (month + 1) + "-" + day)) {
                                mtxt_activity_timebegin.setText("");
                                begin_date = "";
                                mtxt_activity_timefinish.setText("");
                                finish_date = "";
                                activity_data_conflict_refresh();
                                return;
                            }
                            mtxt_activity_timefinish.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            finish_date = year + "-" + (month + 1) + "-" + day;
                        }

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
    private void activity_data_conflict_refresh() {
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDays_in_advance(null);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days() != null) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setMinimum_booking_days(1);
        }
        Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
        Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
        Field_AddResourcesModel.getInstance().setIs_hava_field_services(0);
    }
    private  boolean checkInput_activity_data_conflict(String start_data,String finish_date) {
        boolean result = true;
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (start_data != null && finish_date != null && start_data.length() > 0 &&
                    finish_date.length() > 0) {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days() != null) {
                    if (Constants.two_date_discrepancy(start_data, finish_date) < Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days()) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_data_conflict_error_text));
                        return false;
                    }
                }
                HashMap<Object, Object> map = new HashMap<>();
                for (int i = 0; i< Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size(); i++) {
                    map.put(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId(),
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getPeriod());
                }
                if ( Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0
                        && map.size() > 0) {
                    int minimum_order_quantity = Constants.two_date_discrepancy(start_data, finish_date);
                    for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                        if (map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) != null &&
                                map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()).toString().length() > 0 &&
                                Integer.parseInt(map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()).toString()) != 1 &&
                                Integer.parseInt(map.get(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()).toString())>
                                        minimum_order_quantity) {
                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_data_conflict_specification_error_text));
                            return false;
                        }
                    }
                }
            }
        }
        return result;
    }
    private boolean checkInput() {
        boolean result = false;
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img() == null ||
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_activity_info_picture_hint));
                return result;
            }

            if (mActivityNameET.getText().toString().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_phy_res_activity_name_hint));
                return result;
            }
            if (TextUtils.getTrimmedLength(mtxt_activity_timebegin.getText()) == 0 || begin_date.length() == 0) {
                BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.activity_time_begin_prompt));
                return result;
            }
            if (TextUtils.getTrimmedLength(mtxt_activity_timefinish.getText()) == 0 || finish_date.length() == 0) {
                BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.activity_time_finish_prompt));
                return result;
            }
            if (mtxt_activity_timebegin.getText().toString().length() > 0 &&
                    begin_date.length() > 0 &&
                    Constants.iseditoractivitydate(begin_date,0)) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_start_tiem_error_remind_text));
                return result;
            }
            if (mtxt_activity_timefinish.getText().toString().length() > 0 && finish_date.length() > 0 &&
                    Constants.iseditoractivitydate(finish_date,0)) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldinfo_finish_tiem_error_remind_text));
                return result;
            }
            if (begin_date != null &&
                    finish_date != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                    begin_date.length() > 0 &&
                    finish_date.length() > 0) {
                if (Constants.compare_start_finish_date(begin_date,finish_date)) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_activity_time_prompt));
                    return result;
                } else {
                    if (Constants.compare_date(begin_date,Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance())) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_start_date_conflict_text));
                        return result;
                    }
                }
            }
            if (mActivityTypeId == 0) {
                BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.addfield_activity_type_hint_str));
                return result;
            }
            if (TextUtils.getTrimmedLength(mActivityTypeTV.getText()) == 0) {
                BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.addfield_activity_type_hint_str));
                return result;
            }

        }
        return true;
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
            if (type == 1) {
                if (mActivityTypeId == list.get(i).getId()) {
                    clickPosition = i;
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
                if (type == 1) {
                    mActivityTypeId = list.get(wheelview_select_int).getId();
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
    private void saveDataActivityInfo() {
        if (mActivityTypeId > 0) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_type_id(mActivityTypeId);
            Field_AddResourceCreateItemModel activity_type = new Field_AddResourceCreateItemModel();
            activity_type.setDisplay_name(mActivityTypeTV.getText().toString());
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_type(activity_type);
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_type_id(null);
        }
        if (begin_date != null && begin_date.length() > 0) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date(begin_date);
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setActivity_start_date(null);
        }
        if (finish_date != null && finish_date.length() > 0) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline(finish_date);
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDeadline(null);
        }
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setCustom_name(mActivityNameET.getText().toString());
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setDescription(mDescriptionStr);
    }
    private void showData() {
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0) {
                begin_date = Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date();
                String[] datebeginstr = Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().split("-");
                mtxt_activity_timebegin.setText(datebeginstr[0] + "年" +
                        datebeginstr[1] + "月" + datebeginstr[2] + "日");
                begin_year = Integer.parseInt(datebeginstr[0]);
                begin_month = Integer.parseInt(datebeginstr[1]);
                begin_day = Integer.parseInt(datebeginstr[2]);
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                finish_date = Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline();
                String[] datefinishstr = Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().split("-");
                mtxt_activity_timefinish.setText(datefinishstr[0] + "年" +
                        datefinishstr[1] + "月" + datefinishstr[2] + "日");
                finish_year = Integer.parseInt(datefinishstr[0]);
                finish_month = Integer.parseInt(datefinishstr[1]);
                finish_day = Integer.parseInt(datefinishstr[2]);
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getCustom_name() != null) {
                mActivityNameET.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getCustom_name());
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type_id() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type_id() > 0) {
                mActivityTypeId = Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type_id();
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type() != null &&
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type().getDisplay_name() != null) {
                    mActivityTypeTV.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_type().getDisplay_name());
                }
            }
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDescription() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDescription().length() > 0) {
                mPhyResDescriptionsET.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDescription());
                mDescriptionStr = Field_AddResourcesModel.getInstance().getResource().getResource_data().getDescription();
            }
        }
    }
    //banner
    private void updataBanner() {
        if (mPicList != null) {
            mPicList.clear();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size() > 0) {
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size(); i++) {
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url().length() > 0) {
                        mPicList.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url()
                                + Config.Linhui_Max_Watermark);
                    }
                }
            }
        }

        if (mPicList != null && mPicList.size() > 0) {
            mAddfieldBannerDefaultLL.setVisibility(View.GONE);
            mAddfieldActivityBanner.setVisibility(View.VISIBLE);
            bannershow();
        } else {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mAddfieldBannerRL.getLayoutParams();
            //设置图片显示高度
            linearParams.height = height;
            linearParams.width = width;
            mAddfieldBannerRL.setLayoutParams(linearParams);
            mAddfieldActivityBanner.setVisibility(View.INVISIBLE);
            mAddfieldBannerNoLL.setVisibility(View.GONE);
            mAddfieldBannerDefaultLL.setVisibility(View.VISIBLE);
        }
    }
    private void bannershow() {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mAddfieldBannerRL.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mAddfieldBannerRL.setLayoutParams(linearParams);
        //设置图片加载器
        mAddfieldActivityBanner.setImageLoader(mAddfieldMainImgLoader);
        //设置图片集合
        mAddfieldActivityBanner.setImages(mPicList);
        //设置banner动画效果
        mAddfieldActivityBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mAddfieldActivityBanner.isAutoPlay(false);
        //设置指示器位置（当banner模式中有指示器时）
        mAddfieldActivityBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mAddfieldActivityBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent addfield_three = new Intent(FieldAddfieldActivityInfoActivity.this, Field_AddField_UploadingPictureActivity.class);
                addfield_three.putExtra("click_position",position - 1);
                addfield_three.putExtra("picture_type",3);
                startActivityForResult(addfield_three,1);
            }
        });

        mAddfieldActivityBanner.start();
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            if (checkInput()) {
                Field_AddResourcesModel.getInstance().setIs_hava_activity_info(1);
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_activity_info(0);
            }
            saveDataActivityInfo();
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}
