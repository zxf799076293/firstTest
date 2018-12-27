package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldview.WheelView;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class FieldAddFieldServicesItemsActivity extends FieldBaseMvpActivity {
    private EditText edit_point;
    private LinearLayout minvoice_layout;
    private CheckBox btn_chair;
    private CheckBox btn_electricity;
    private CheckBox btn_materials;
    private CheckBox btn_Propaganda;
    private Switch btnToggleButton_ticket;
    private CheckBox btnToggleButton_tent;
    private TextView mfieldrules_reserve_down;
    private EditText mfieldrules_reserve_days;
    private TextView mfieldrules_reserve_add;
    private TextView mfieldrules_activity_reserve_down;
    private EditText mfieldrules_activity_reserve_days;
    private TextView mfieldrules_activity_reserve_add;
    private LinearLayout mfieldrules_activity_reserve_day_layout;
    private Button mAddfieldServicesBtn;
    private LinearLayout mAddfieldServicesBtnLL;
    //场地守则
    private RelativeLayout mcontraband_layout;
    private LinearLayout mcontraband_list_layout;
    private RelativeLayout mproperty_requirement_layout;
    private LinearLayout mproperty_requirement_list_layout;
    private EditText mtxt_contraband;
    private EditText meditproperty_requirement;
    private LinearLayout mServicePhyLL;
    private LinearLayout mActivityChangeDateLL;
    private TextView mActivityChangeDateTV;
    private LinearLayout mActivityChangeDateDayLL;
    private EditText mActivityChangeDateDayET;
    private TextView[] contraband_textViewlist;
    private HashMap<String,Boolean> contraband_clickmap = new HashMap<>();
    private HashMap<Integer,String> contraband_viewsetidmap = new HashMap<>();
    private TextView[] property_requirement_textViewlist;
    private HashMap<String,Boolean> property_requirement_clickmap = new HashMap<>();
    private HashMap<Integer,String> property_requirement_viewsetidmap = new HashMap<>();
    private ArrayList<FieldAddfieldAttributesModel> contraband_ids = new ArrayList<>();
    private ArrayList<FieldAddfieldAttributesModel> requirement_ids = new ArrayList<>();
    private ArrayList<Field_AddResourceCreateItemModel> contraband = new ArrayList<>();
    private ArrayList<Field_AddResourceCreateItemModel> requirement = new ArrayList<>();

    private int reserve_day = 1;
    private int reserve_day_tmp = 1;
    private int activity_reserve_day = 1;
    private int activity_reserve_day_tmp = 1;
    private Drawable drawable_down_pressed;
    private Drawable drawable_down;
    private Drawable drawable_add;
    private Drawable drawable_add_pressed;
    private  String str_electricity = "0",str_chair = "0",str_materials ="0",str_Propaganda = "0",str_invoice = "0",
            str_tent = "0";
    private Dialog mDialog;
    private int wheelviewSelectInt = -1;
    private int isChangeDate = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_servicesitems);
        initView();
    }
    private void initView() {
        mfieldrules_reserve_down = (TextView)findViewById(R.id.fieldrules_reserve_down);
        mfieldrules_reserve_days = (EditText)findViewById(R.id.fieldrules_reserve_days);
        mfieldrules_reserve_add = (TextView)findViewById(R.id.fieldrules_reserve_add);
        mfieldrules_activity_reserve_down = (TextView)findViewById(R.id.fieldrules_activity_reserve_down);
        mfieldrules_activity_reserve_days = (EditText)findViewById(R.id.fieldrules_reserve_activity_days);
        mfieldrules_activity_reserve_add = (TextView)findViewById(R.id.fieldrules_activity_reserve_add);
        mfieldrules_activity_reserve_day_layout = (LinearLayout)findViewById(R.id.fieldrules_activity_reserve_day_layout);
        mAddfieldServicesBtn = (Button) findViewById(R.id.addfield_services_next_btn);
        mAddfieldServicesBtnLL = (LinearLayout) findViewById(R.id.addfield_services_next_btn_ll);

        mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
        mfieldrules_activity_reserve_days.setText(String.valueOf(activity_reserve_day));

        edit_point = (EditText)findViewById(R.id.edit_point);
        minvoice_layout = (LinearLayout)findViewById(R.id.invoice_layout);
        btn_chair = (CheckBox) findViewById(R.id.ToggleButton_chair) ;
        btn_electricity=(CheckBox)findViewById(R.id.ToggleButton_electricity);
        btn_materials = (CheckBox )findViewById(R.id.ToggleButton_materials) ;
        btn_Propaganda =(CheckBox)findViewById (R.id.ToggleButton_Propaganda);
        btnToggleButton_ticket=  (Switch)findViewById(R.id.ToggleButton_ticket);
        btnToggleButton_tent=  (CheckBox)findViewById(R.id.ToggleButton_tent);
        drawable_down = getResources().getDrawable(R.drawable.down_img);
        drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
        drawable_down_pressed = getResources().getDrawable(R.drawable.down_img_pressed);
        drawable_down_pressed.setBounds(0, 0, drawable_down_pressed.getMinimumWidth(), drawable_down_pressed.getMinimumHeight());
        drawable_add = getResources().getDrawable(R.drawable.add_img);
        drawable_add.setBounds(0, 0, drawable_add.getMinimumWidth(), drawable_add.getMinimumHeight());
        drawable_add_pressed = getResources().getDrawable(R.drawable.add_img_presseed);
        drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
        //场地守则
        mcontraband_layout = (RelativeLayout)findViewById(R.id.contraband_layout);
        mcontraband_list_layout = (LinearLayout)findViewById(R.id.contraband_list_layout);
        mproperty_requirement_layout = (RelativeLayout)findViewById(R.id.property_requirement_layout);
        mproperty_requirement_list_layout = (LinearLayout)findViewById(R.id.property_requirement_list_layout);
        mtxt_contraband = (EditText)findViewById(R.id.txt_contraband);
        meditproperty_requirement = (EditText)findViewById(R.id.editproperty_requirement);
        mServicePhyLL = (LinearLayout)findViewById(R.id.addfield_service_phy_ll);
        //改期
        mActivityChangeDateLL = (LinearLayout)findViewById(R.id.addfield_services_negotiate_rescheduled_ll);
        mActivityChangeDateTV = (TextView)findViewById(R.id.addfield_services_negotiate_rescheduled_tv);
        mActivityChangeDateDayLL = (LinearLayout)findViewById(R.id.addfield_services_negotiate_rescheduled_day_ll);
        mActivityChangeDateDayET = (EditText)findViewById(R.id.addfield_services_negotiate_rescheduled_day_tv);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.addfield_main_fieldservices_text));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back_checkInput()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_services(1);
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_services(0);
                }
                savebtn();
                finish();
            }
        });

        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
            TitleBarUtils.shownextTextView(this, "", 14, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_services(1);
                        savebtn();
                        finish();
                    }
                }
            });
            mAddfieldServicesBtnLL.setVisibility(View.GONE);
        } else {
            mAddfieldServicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_services(1);
                        savebtn();
                        Intent addfield_main_intent = new Intent(FieldAddFieldServicesItemsActivity.this,FieldAddFieldOtherContactAccountActivity.class);
                        startActivity(addfield_main_intent);
                    }
                }
            });
        }
        mfieldrules_reserve_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserve_day_tmp = reserve_day;
                if (reserve_day > 0) {
                    reserve_day = reserve_day -1;
                    mfieldrules_reserve_add.setCompoundDrawables(drawable_down_pressed, null,null, null);
                    mfieldrules_reserve_add.setCompoundDrawables(drawable_add_pressed, null,null, null);
                    mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
                } else {
                    mfieldrules_reserve_down.setCompoundDrawables(null, null, drawable_down, null);
                    mfieldrules_reserve_add.setCompoundDrawables(drawable_add_pressed, null,null, null);
                }            }
        });
        mfieldrules_reserve_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserve_day_tmp = reserve_day;
                if (reserve_day > -1 && reserve_day < 100) {
                    reserve_day = reserve_day + 1;
                    mfieldrules_reserve_add.setCompoundDrawables(null, null, drawable_add_pressed, null);
                    mfieldrules_reserve_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                    mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
                } else {
                    mfieldrules_reserve_add.setCompoundDrawables(drawable_add, null,null, null);
                    mfieldrules_reserve_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                }
            }
        });
        mfieldrules_reserve_days.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                } else {
                    if (mfieldrules_reserve_days.getText().toString().length() > 0) {
                        if (Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim()) > -1 && Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim()) < 100) {
                            if (Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim()) > 1) {
                                reserve_day = Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim());
                                if (reserve_day == 100) {
                                    mfieldrules_reserve_add.setCompoundDrawables(drawable_add, null,null, null);
                                }
                            } else {
                                reserve_day = Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim());
                            }
                        } else {
                            mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
                        }
                    }
                }
            }
        });
        mfieldrules_reserve_days.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                reserve_day_tmp = reserve_day;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mfieldrules_reserve_days.clearFocus();
                } else {

                }
            }
        });
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            mfieldrules_activity_reserve_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity_reserve_day_tmp = activity_reserve_day;
                    if (activity_reserve_day > 1) {
                        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                                activity_reserve_day = activity_reserve_day - 1;
                                mfieldrules_activity_reserve_add.setCompoundDrawables(drawable_down_pressed, null, null, null);
                                mfieldrules_activity_reserve_add.setCompoundDrawables(drawable_add_pressed, null, null, null);
                                mfieldrules_activity_reserve_days.setText(String.valueOf(activity_reserve_day));
                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_minimum_order_quantity_choose_activitytime_text));
                            }
                        }
                    } else {
                        mfieldrules_activity_reserve_down.setCompoundDrawables(null, null, drawable_down, null);
                        mfieldrules_activity_reserve_add.setCompoundDrawables(drawable_add_pressed, null, null, null);
                    }
                }

            });
            mfieldrules_activity_reserve_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                            activity_reserve_day_tmp = activity_reserve_day;
                            activity_reserve_day = activity_reserve_day + 1;
                            mfieldrules_activity_reserve_add.setCompoundDrawables(drawable_down_pressed, null, null, null);
                            mfieldrules_activity_reserve_add.setCompoundDrawables(drawable_add_pressed, null, null, null);
                            mfieldrules_activity_reserve_days.setText(String.valueOf(activity_reserve_day));
                        } else {
                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_minimum_order_quantity_choose_activitytime_text));
                        }
                    }
                }
            });
            mfieldrules_activity_reserve_days.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {

                    } else {
                        if (mfieldrules_activity_reserve_days.getText().toString().length() > 0) {
                            if (Integer.parseInt(mfieldrules_activity_reserve_days.getText().toString().trim()) > 0) {
                                if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                                        activity_reserve_day = Integer.parseInt(mfieldrules_activity_reserve_days.getText().toString().trim());
                                    } else {
                                        mfieldrules_activity_reserve_days.setText(String.valueOf(activity_reserve_day));
                                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_main_fieldrules_minimum_order_quantity_choose_activitytime_text));
                                    }
                                }
                            } else {
                                mfieldrules_activity_reserve_days.setText(String.valueOf(activity_reserve_day));
                            }
                        }
                    }
                }
            });
            mfieldrules_activity_reserve_days.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    activity_reserve_day_tmp = activity_reserve_day;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > 0) {
                        mfieldrules_activity_reserve_days.clearFocus();
                    } else {

                    }
                }
            });
        }
        mActivityChangeDateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Field_AddResourceCreateItemModel> list = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
                    field_addResourceCreateItemModel.setId(i);
                    if (i== 0) {
                        field_addResourceCreateItemModel.setName(getResources().getString(R.string.addfield_optional_info_nohave_restaurant_text));
                    } else {
                        field_addResourceCreateItemModel.setName(getResources().getString(R.string.addfield_optional_info_have_restaurant_text));
                    }
                    list.add(field_addResourceCreateItemModel);
                }
                showChooseDialog(mActivityChangeDateTV,list,1);
            }
        });

        //是否开发票等togglebutton的点击事件
        getToggleButton_value();
        showData();
    }
    private void getToggleButton_value() {
        btn_electricity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_electricity = "1";
                } else {
                    str_electricity = "0";
                }
            }
        });
        btn_chair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_chair = "1";
                } else {
                    str_chair = "0";
                }
            }
        });
        btn_materials.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_materials = "1";
                } else {
                    str_materials = "0";
                }
            }
        });
        btn_Propaganda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_Propaganda = "1";
                } else {
                    str_Propaganda = "0";
                }
            }
        });
        btnToggleButton_ticket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_invoice = "1";
                    minvoice_layout.setVisibility(View.VISIBLE);
                } else {
                    str_invoice = "0";
                    minvoice_layout.setVisibility(View.GONE);
                    edit_point.setText("");
                }
            }
        });
        btnToggleButton_tent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_tent = "1";
                } else {
                    str_tent = "0";
                }
            }
        });
    }

    private boolean checkInput() {
        boolean result = false;
        if (mfieldrules_reserve_days.getText().toString().trim().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldrules_null_text));
            return result;
        }
        if (isChangeDate == -1) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_services_change_date_hint));
            return result;
        }
        if (isChangeDate == 1) {
            if (mActivityChangeDateDayET.getText().toString().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_services_change_date_day_hint));
                return result;
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0) {
                if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),
                        Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim()))) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldrules_error_text));
                    return result;
                }
            }
            if (mfieldrules_activity_reserve_days.getText().toString().trim().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_minimum_order_quantity_null_text));
                return result;
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                if (Constants.two_date_discrepancy(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline()) < Integer.parseInt(mfieldrules_activity_reserve_days.getText().toString().trim())) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_minimum_order_quantity_activity_time_error_text));
                    return result;
                }
            }
        }
        String string = edit_point.getText().toString();
        double num = 0.00;
        int newnum;
        if (str_invoice.equals("1")) {
            if (TextUtils.getTrimmedLength(edit_point.getText()) == 0) {
                {
                    BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.tax_prompt));
                }
                return result;
            }
            if (TextUtils.getTrimmedLength(edit_point.getText()) != 0) {
                num = Double.valueOf(string.toString());
                newnum = (int) num;
                if ((newnum < 0 || newnum > 100)) {
                    BaseMessageUtils.showToast(getContext(), getResources().getString(R.string.tax_range_prompt));
                    edit_point.setText("");
                    return result;
                }
            }
        }
        return true;
    }
    private boolean back_checkInput() {
        boolean result = false;
        if (mfieldrules_reserve_days.getText().toString().trim().length() == 0) {
            return result;
        }
        if (isChangeDate == -1) {
            BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_services_change_date_hint));
            return result;
        }
        if (isChangeDate == 1) {
            if (mActivityChangeDateDayET.getText().toString().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_services_change_date_day_hint));
                return result;
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0) {
                if (Constants.compare_date(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),
                        Integer.parseInt(mfieldrules_reserve_days.getText().toString().trim()))) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_fieldrules_error_text));
                    return result;
                }
            }
        }
        if (mfieldrules_activity_reserve_days.getText().toString().trim().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_minimum_order_quantity_null_text));
            return result;
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline().length() > 0) {
                if (Constants.two_date_discrepancy(Field_AddResourcesModel.getInstance().getResource().getResource_data().getActivity_start_date(),
                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDeadline()) < Integer.parseInt(mfieldrules_activity_reserve_days.getText().toString().trim())) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_minimum_order_quantity_activity_time_error_text));
                    return result;
                }
            }
        }
        String string = edit_point.getText().toString();
        double num = 0.00;
        int newnum;
        if (str_invoice.equals("1")) {
            if (TextUtils.getTrimmedLength(edit_point.getText()) == 0) {
                return result;
            }
            if (TextUtils.getTrimmedLength(edit_point.getText()) != 0) {
                num = Double.valueOf(string.toString());
                newnum = (int) num;
                if ((newnum < 0 || newnum > 100)) {
                    return result;
                }
            }
        }
        return true;
    }

    private void savebtn() {
        if (mfieldrules_reserve_days.getText().toString().trim().length() > 0) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDays_in_advance(Integer.parseInt(mfieldrules_reserve_days.getText().toString()));
        }
        if (mfieldrules_activity_reserve_days.getText().toString().trim().length() > 0) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setMinimum_booking_days(Integer.parseInt(mfieldrules_activity_reserve_days.getText().toString()));
        }
        if (isChangeDate == 1) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setSupport_rescheduling(isChangeDate);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setReschedule_in_advance(Integer.parseInt(mActivityChangeDateDayET.getText().toString()));
        } else if (isChangeDate == 2) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setSupport_rescheduling(isChangeDate);
        }
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setHas_power(Integer.parseInt(str_electricity));
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setHas_chair(Integer.parseInt(str_chair));
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setOvernight_material(Integer.parseInt(str_materials));
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setLeaflet(Integer.parseInt(str_Propaganda));
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setInvoice(Integer.parseInt(str_invoice));
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setHas_tent(Integer.parseInt(str_tent));
        if (str_invoice.equals("1")) {
            if (edit_point.getText().toString() != null) {
                if (! (edit_point.getText().toString().equals(""))) {
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setTax_point(Constants.getpricestring(edit_point.getText().toString(),1));
                } else {
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setTax_point("0");
                }
            } else {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setTax_point("0");
            }
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setTax_point("0");
        }
        //场地守则
        if (contraband_ids != null) {
            contraband_ids.clear();
        }
        if (contraband_textViewlist != null && contraband_textViewlist.length > 0) {
            for (int j = 0; j < contraband_textViewlist.length; j++) {
                if (contraband_clickmap.get(contraband_viewsetidmap.get(j))) {
                    if (Field_AddResourcesModel.getInstance().getOptions().getContraband() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getContraband().size() > 0) {
                        if (!contraband_textViewlist[j].getText().toString().trim().equals(getResources().getString(R.string.addfield_optional_info_else_text))) {
                            FieldAddfieldAttributesModel fieldAddfieldAttributesModel = new FieldAddfieldAttributesModel();
                            fieldAddfieldAttributesModel.setId(Field_AddResourcesModel.getInstance().getOptions().getContraband().get(j).getId());
                            contraband_ids.add(fieldAddfieldAttributesModel);
                        }
                    }

                }

            }
        }
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setContraband(contraband_ids);
        if (mtxt_contraband.getVisibility() == View.VISIBLE && mtxt_contraband.getText().toString().trim().length() > 0)  {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setOther_contraband(mtxt_contraband.getText().toString());
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setOther_contraband(null);
        }
        if (requirement_ids != null) {
            requirement_ids.clear();
        }
        if (property_requirement_textViewlist != null && property_requirement_textViewlist.length > 0) {
            for (int j = 0; j < property_requirement_textViewlist.length; j++) {
                if (property_requirement_clickmap.get(property_requirement_viewsetidmap.get(j))) {
                    if (Field_AddResourcesModel.getInstance().getOptions().getRequirement() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getRequirement().size() > 0) {
                        if (!property_requirement_textViewlist[j].getText().toString().trim().equals(getResources().getString(R.string.addfield_optional_info_else_text))) {
                            FieldAddfieldAttributesModel fieldAddfieldAttributesModel = new FieldAddfieldAttributesModel();
                            fieldAddfieldAttributesModel.setId(Field_AddResourcesModel.getInstance().getOptions().getRequirement().get(j).getId());
                            requirement_ids.add(fieldAddfieldAttributesModel);
                        }
                    }

                }

            }
        }
        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setRequirement(requirement_ids);
        if (meditproperty_requirement.getVisibility() == View.VISIBLE && meditproperty_requirement.getText().toString().trim().length() > 0)  {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setProperty_requirement(meditproperty_requirement.getText().toString());
        } else {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setProperty_requirement(null);
        }
    }
    private void showData() {
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            mfieldrules_activity_reserve_day_layout.setVisibility(View.VISIBLE);
        } else {
            mfieldrules_activity_reserve_day_layout.setVisibility(View.GONE);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance()  > -1) {
            mfieldrules_reserve_days.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance()));
            reserve_day = Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance();
            reserve_day_tmp = Field_AddResourcesModel.getInstance().getResource().getResource_data().getDays_in_advance();
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days()  > -1) {
            mfieldrules_activity_reserve_days.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days()));
            activity_reserve_day = Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days();
            activity_reserve_day_tmp = Field_AddResourcesModel.getInstance().getResource().getResource_data().getMinimum_booking_days();
        }
        //场地守则
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getContraband() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getContraband().size() > 0) {
            contraband_ids= Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getContraband();
        }
        //选中的禁摆品类处理
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_contraband() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_contraband().length() > 0) {
            mtxt_contraband.setVisibility(View.VISIBLE);
            mcontraband_list_layout.setVisibility(View.VISIBLE);
            mtxt_contraband.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_contraband());
        } else {
            mtxt_contraband.setVisibility(View.GONE);
            mcontraband_list_layout.setVisibility(View.GONE);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getRequirement() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getRequirement().size() > 0) {
            requirement_ids= Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getRequirement();
        }
        //选中的物业要求处理

        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getProperty_requirement() != null &&
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getProperty_requirement().length() > 0) {
            meditproperty_requirement.setVisibility(View.VISIBLE);
            mproperty_requirement_list_layout.setVisibility(View.VISIBLE);
            meditproperty_requirement.setText(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getProperty_requirement());
        } else {
            meditproperty_requirement.setVisibility(View.GONE);
            mproperty_requirement_list_layout.setVisibility(View.GONE);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getEditable() == 1) {
            showFieldRulesView();
            mServicePhyLL.setVisibility(View.VISIBLE);
        } else {
            mServicePhyLL.setVisibility(View.GONE);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_power() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_power() == 1) {
                str_electricity = "1";
                btn_electricity.setChecked(true);
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_chair() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_chair() == 1) {
                str_chair = "1";
                btn_chair.setChecked(true);
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getOvernight_material() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getOvernight_material() == 1) {
                str_materials = "1";
                btn_materials.setChecked(true);
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getLeaflet() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getLeaflet() == 1) {
                str_Propaganda = "1";
                btn_Propaganda.setChecked(true);
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getInvoice() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getInvoice() == 1) {
            minvoice_layout.setVisibility(View.VISIBLE);
            str_invoice = "1";
            btnToggleButton_ticket.setChecked(true);
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getTax_point() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getTax_point().length() > 0) {
                edit_point.setText(Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getTax_point(), 1));
            } else {
                edit_point.setText("0");
            }
        } else {
            minvoice_layout.setVisibility(View.GONE);
            str_invoice = "0";
            btnToggleButton_ticket.setChecked(false);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_tent() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getHas_tent() == 1) {
                str_tent = "1";
                btnToggleButton_tent.setChecked(true);
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getSupport_rescheduling() != null) {
            isChangeDate = Field_AddResourcesModel.getInstance().getResource().getResource_data().getSupport_rescheduling();
            if (isChangeDate == 1) {
                mActivityChangeDateTV.setText(getResources().getString(R.string.addfield_optional_info_have_restaurant_text));
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReschedule_in_advance() != null) {
                    mActivityChangeDateDayLL.setVisibility(View.VISIBLE);
                    mActivityChangeDateDayET.setText(String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getReschedule_in_advance()));
                }
            } else {
                mActivityChangeDateTV.setText(getResources().getString(R.string.addfield_optional_info_nohave_restaurant_text));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 4) {
            mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
        } else {
            reserve_day = reserve_day_tmp;
            mfieldrules_reserve_days.setText(String.valueOf(reserve_day));
        }
        if (reserve_day == 0) {
            mfieldrules_reserve_down.setCompoundDrawables(drawable_down, null,null, null);
        } else {
            mfieldrules_reserve_down.setCompoundDrawables(drawable_down_pressed, null,null, null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            if (back_checkInput()) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_services(1);
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_services(0);
            }
            savebtn();
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void showFieldRulesView() {
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width_new = metric.widthPixels;     // 屏幕宽度（像素）
        float contraband_width = 0;
        float contraband_widthtmp = 0;
        int contraband_hightnum = 0;
        if (Field_AddResourcesModel.getInstance().getOptions() != null && Field_AddResourcesModel.getInstance().getOptions().getContraband() != null
                && Field_AddResourcesModel.getInstance().getOptions().getContraband().size() > 0) {
            contraband.addAll(Field_AddResourcesModel.getInstance().getOptions().getContraband());
            Field_AddResourceCreateItemModel contrabandmodel = new Field_AddResourceCreateItemModel();
            contrabandmodel.setDisplay_name(getResources().getString(R.string.addfield_optional_info_else_text));
            contraband.add(contrabandmodel);
            contraband_textViewlist = new TextView[contraband.size()];
            for (int i = 0; i < contraband.size(); i++) {
                final TextView textView = new TextView(this);
                textView.setText("  " + contraband.get(i).getDisplay_name().toString() + "  ");
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(8, 0, 8, 0);
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_show_all_resource_info_bg));
                textView.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                textView.setId(i);
                textView.setTag(i);
                contraband_width = Constants.getTextWidth(this, contraband.get(i).getDisplay_name(), 16) + 16;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, Constants.Dp2Px(this,24));
                if (contraband_width > (width_new - contraband_widthtmp - 28)) {
                    contraband_hightnum = contraband_hightnum + 1;
                    contraband_widthtmp = 0;
                    params.setMargins((int) contraband_widthtmp + 28, contraband_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    mcontraband_layout.addView(textView);
                } else {
                    params.setMargins((int) contraband_widthtmp + 28, contraband_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    mcontraband_layout.addView(textView);
                }
                contraband_widthtmp = contraband_width + contraband_widthtmp + 28;
                contraband_viewsetidmap.put(i, contraband.get(i).getDisplay_name().toString());
                contraband_clickmap.put(contraband.get(i).getDisplay_name().toString(), false);
                if (contraband.get(i).getDisplay_name().toString().trim().equals(getResources().getString(R.string.addfield_optional_info_else_text))) {
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_contraband() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getOther_contraband().length() > 0) {
                        contraband_clickmap.put(contraband.get(i).getDisplay_name().toString(), true);
                        textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                    }
                }

                for (int j = 0; j < contraband_ids.size(); j++) {
                    if (contraband_ids.get(j).getId() == contraband.get(i).getId()) {
                        contraband_clickmap.put(contraband.get(i).getDisplay_name(), true);
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                        textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                    }
                }
                contraband_textViewlist[i] = textView;
            }
            if (contraband_textViewlist != null && contraband_textViewlist.length >0) {
                for (int j = 0; j < contraband_textViewlist.length; j++) {
                    contraband_textViewlist[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contraband_clickmap.get(contraband_viewsetidmap.get(v.getId()))) {
                                contraband_clickmap.put(contraband_viewsetidmap.get(v.getId()),false);
                                contraband_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                                contraband_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                            } else {
                                contraband_clickmap.put(contraband_viewsetidmap.get(v.getId()),true);
                                contraband_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
                                contraband_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.default_bluebg));
                            }
                        }
                    });
                }
            }
        } else {
            mtxt_contraband.setVisibility(View.VISIBLE);
            mcontraband_list_layout.setVisibility(View.VISIBLE);
        }
        float property_requirement_width = 0;
        float property_requirement_widthtmp = 0;
        int property_requirement_hightnum = 0;
        if (Field_AddResourcesModel.getInstance().getOptions() != null && Field_AddResourcesModel.getInstance().getOptions().getRequirement() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getRequirement().size() > 0) {
            requirement.addAll(Field_AddResourcesModel.getInstance().getOptions().getRequirement());
            Field_AddResourceCreateItemModel requirementmodel = new Field_AddResourceCreateItemModel();
            requirementmodel.setDisplay_name(getResources().getString(R.string.addfield_optional_info_else_text));
            requirement.add(requirementmodel);
            property_requirement_textViewlist = new TextView[requirement.size()];
            for (int i = 0; i < requirement.size(); i++) {
                final TextView textView = new TextView(this);
                textView.setText("  " + requirement.get(i).getDisplay_name().toString() + "  ");
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(8, 0, 8, 0);
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_show_all_resource_info_bg));
                textView.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                textView.setId(i);
                textView.setTag(i);
                property_requirement_width = Constants.getTextWidth(this, requirement.get(i).getDisplay_name().toString(), 16) + 16;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, Constants.Dp2Px(this,24));
                if (property_requirement_width > (width_new - property_requirement_widthtmp - 28)) {
                    property_requirement_hightnum = property_requirement_hightnum + 1;
                    property_requirement_widthtmp = 0;
                    params.setMargins((int) property_requirement_widthtmp + 28, property_requirement_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    mproperty_requirement_layout.addView(textView);
                } else {
                    params.setMargins((int) property_requirement_widthtmp + 28, property_requirement_hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    mproperty_requirement_layout.addView(textView);
                }
                property_requirement_widthtmp = property_requirement_width + property_requirement_widthtmp + 28;
                property_requirement_viewsetidmap.put(i, requirement.get(i).getDisplay_name().toString());
                property_requirement_clickmap.put(requirement.get(i).getDisplay_name().toString(), false);
                if (requirement.get(i).getDisplay_name().toString().trim().equals(getResources().getString(R.string.addfield_optional_info_else_text))) {
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getProperty_requirement() != null &&
                            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getProperty_requirement().length() > 0) {
                        property_requirement_clickmap.put(requirement.get(i).getDisplay_name().toString(), true);
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                        textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                    }
                }
                for (int j = 0; j < requirement_ids.size(); j++) {
                    if (requirement_ids.get(j).getId() != null) {
                        if (requirement_ids.get(j).getId() == requirement.get(i).getId()) {
                            property_requirement_clickmap.put(requirement.get(i).getDisplay_name(), true);
                            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_shopcard_adv_occupancy_rate_tv_bg));
                            textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                        }
                    }
                }
                property_requirement_textViewlist[i] = textView;
            }
            if (property_requirement_textViewlist != null && property_requirement_textViewlist.length >0) {
                for (int j = 0; j < property_requirement_textViewlist.length; j++) {
                    property_requirement_textViewlist[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (property_requirement_clickmap.get(property_requirement_viewsetidmap.get(v.getId()))) {
                                property_requirement_clickmap.put(property_requirement_viewsetidmap.get(v.getId()),false);
                                property_requirement_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                                property_requirement_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                            } else {
                                property_requirement_clickmap.put(property_requirement_viewsetidmap.get(v.getId()),true);
                                property_requirement_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_searchlist_item_edittext_clicked_bg));
                                property_requirement_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.default_bluebg));
                            }
                        }
                    });
                }
            }
        } else {
            meditproperty_requirement.setVisibility(View.VISIBLE);
            mproperty_requirement_list_layout.setVisibility(View.VISIBLE);
        }
    }
    private void showChooseDialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(R.id.btn_confirm);
        final WheelView mwheelview= (WheelView) textEntryView.findViewById(R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (type == 1) {
                wheelview_list.add(list.get(i).getName());
            }
        }
        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (isChangeDate == 1) {
            wheelviewSelectInt = 1;
            mwheelview.setSeletion(1);
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
                    if (list.get(wheelviewSelectInt).getId() == 0) {
                        isChangeDate = 2;
                    } else {
                        isChangeDate = list.get(wheelviewSelectInt).getId();
                    }
                    if (isChangeDate == 1) {
                        mActivityChangeDateDayLL.setVisibility(View.VISIBLE);
                    } else {
                        mActivityChangeDateDayLL.setVisibility(View.GONE);
                    }
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

}
