package com.linhuiba.business.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.mvppresenter.SubmitDemandMvpPresenter;
import com.linhuiba.business.mvpview.SubmitDemandMvpView;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SubmitDemandActivity extends BaseMvpActivity implements SubmitDemandMvpView {
    @InjectView(R.id.submit_demand_product_name_et)
    EditText mSubmitDemandProductET;
    @InjectView(R.id.submit_demand_min_area_et)
    EditText mSubmitDemandMinAreaET;
    @InjectView(R.id.submit_demand_max_area_et)
    EditText mSubmitDemandMaxAreaET;
    @InjectView(R.id.submit_demand_price_et)
    EditText mSubmitDemandPriceET;
    @InjectView(R.id.submit_demand_end_et)
    TextView mSubmitDemandEndTV;
    @InjectView(R.id.submit_demand_start_et)
    TextView mSubmitDemandStartTV;
    @InjectView(R.id.submit_demand_name_et)
    EditText mSubmitDemandNameET;
    @InjectView(R.id.submit_demand_mobile_et)
    EditText mSubmitDemandMobileET;
    private SubmitDemandMvpPresenter mSubmitDemandMvpPresenter;
    private String begin_date = "";
    private String finish_date = "";
    private int begin_year;
    private int begin_month;
    private int begin_day;
    private int finish_year;
    private int finish_month;
    private int finish_day;
    private String mCommunityResId;
    private String mPhysicalResId;
    private ArrayList<Integer> mCityIds;
    private ArrayList<Integer> mCommunityTypeIds;
    private static final int DEMAND_RESULTCODE = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_submit_demand);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubmitDemandMvpPresenter != null) {
            mSubmitDemandMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.submit_demand_btn
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_demand_btn:
                if (checkInput()) {
                    showProgressDialog();
                    mSubmitDemandMvpPresenter.appealDemand(
                            mSubmitDemandProductET.getText().toString().trim(),
                            mSubmitDemandMinAreaET.getText().toString().trim(),
                            mSubmitDemandMaxAreaET.getText().toString().trim(),
                            mSubmitDemandPriceET.getText().toString().trim(),
                            begin_date,finish_date,mSubmitDemandNameET.getText().toString().trim(),
                            mSubmitDemandMobileET.getText().toString().trim(),
                            mCommunityResId,mPhysicalResId,mCityIds,mCommunityTypeIds);
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (intent.getExtras().get("physical_res_id") != null) {
            mPhysicalResId = intent.getStringExtra("physical_res_id");
        }
        if (intent.getExtras().get("community_res_id") != null) {
            mCommunityResId = intent.getStringExtra("community_res_id");
        }
        if (intent.getExtras().get("city_ids") != null) {
            mCityIds = (ArrayList<Integer>) intent.getExtras().get("city_ids");
        }
        if (intent.getExtras().get("community_type_ids") != null) {
            mCommunityTypeIds = (ArrayList<Integer>) intent.getExtras().get("community_type_ids");
        }
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_submit_demand_title));
        TitleBarUtils.showBackImg(this,true);
        mSubmitDemandMvpPresenter = new SubmitDemandMvpPresenter();
        mSubmitDemandMvpPresenter.attachView(this);
        Constants.textchangelistener(mSubmitDemandMinAreaET);
        Constants.textchangelistener(mSubmitDemandMaxAreaET);
        Constants.textchangelistener(mSubmitDemandPriceET);
        onDateClick();
    }

    @Override
    public void onSubmitDemandSuccess() {
        Intent intent = new Intent();
        setResult(6,intent);
        finish();
    }

    @Override
    public void onSubmitDemandFailure(boolean superresult, Throwable error) {
        if (!superresult) {
            MessageUtils.showToast(error.getMessage());
        }
        checkAccess_new(error);
    }
    private void onDateClick() {
        mSubmitDemandStartTV.setOnClickListener(new View.OnClickListener() {
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
                datedialog = new DatePickerDialog(SubmitDemandActivity.this,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datedialog.getDatePicker();
                datePicker.setMinDate(calendar.getTimeInMillis());
                datedialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.btn_complete),
                        new DialogInterface.OnClickListener() {
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
                        if (mSubmitDemandEndTV.getText().toString().length()  == 0) {
                            mSubmitDemandStartTV.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            begin_date = year + "-" + (month + 1) + "-" + day;
                        } else {
                            if (begin_year > finish_year) {
                               MessageUtils.showToast(getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month > finish_month) {
                                MessageUtils.showToast(getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month == finish_month && begin_day > finish_day) {
                                MessageUtils.showToast(getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            mSubmitDemandStartTV.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            begin_date = year + "-" + (month + 1) + "-" + day;
                        }

                    }
                });
                //取消按钮，如果不需要直接不设置即可
                datedialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datedialog.dismiss();
                    }
                });
                datedialog.show();
            }
        });
        mSubmitDemandEndTV.setOnClickListener(new View.OnClickListener() {
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
                datedialog = new DatePickerDialog(SubmitDemandActivity.this,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datedialog.getDatePicker();
                datePicker.setMinDate(calendar.getTimeInMillis());
                datedialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.btn_complete),
                        new DialogInterface.OnClickListener() {
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
                        if (mSubmitDemandStartTV.getText().toString().length()  == 0) {
                            mSubmitDemandEndTV.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            finish_date = year + "-" + (month + 1) + "-" + day;
                        } else {
                            if (begin_year > finish_year) {
                                MessageUtils.showToast(getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month > finish_month) {
                                MessageUtils.showToast(getContext(),getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            if (begin_year == finish_year && begin_month == finish_month && begin_day > finish_day) {
                                MessageUtils.showToast(getContext(),getResources().getString(R.string.toast_time_prompt));
                                mSubmitDemandStartTV.setText("");
                                begin_date = "";
                                mSubmitDemandEndTV.setText("");
                                finish_date = "";
                                return;
                            }
                            mSubmitDemandEndTV.setText(year + "年" +
                                    (month + 1) + "月" + day + "日");
                            finish_date = year + "-" + (month + 1) + "-" + day;
                        }

                    }
                });
                //取消按钮，如果不需要直接不设置即可
                datedialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datedialog.dismiss();
                    }
                });
                datedialog.show();
            }
        });
    }
    private boolean checkInput() {
        if (mSubmitDemandProductET.getText().toString().trim().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_product_name_hint));
            return false;
        }
        if (mSubmitDemandMinAreaET.getText().toString().trim().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_min_area_remind));
            return false;
        }
        if (mSubmitDemandMaxAreaET.getText().toString().trim().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_max_area_remind));
            return false;
        }
        if (Double.parseDouble(mSubmitDemandMinAreaET.getText().toString()) >
                Double.parseDouble(mSubmitDemandMaxAreaET.getText().toString())) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_area_error_msg));
            return false;
        }
        if (mSubmitDemandPriceET.getText().toString().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_budget_hint));
            return false;
        }
        if (TextUtils.getTrimmedLength(mSubmitDemandStartTV.getText()) == 0 || begin_date.length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.activity_time_begin_prompt));
            return false;
        }
        if (TextUtils.getTrimmedLength(mSubmitDemandEndTV.getText()) == 0 || finish_date.length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.activity_time_finish_prompt));
            return false;
        }
        if (mSubmitDemandNameET.getText().toString().trim().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.applyforrelease_contect_hinttxt));
            return false;
        }

        if (mSubmitDemandMobileET.getText().toString().trim().length() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.module_submit_demand_mobile_hint));
            return false;
        }
        if (!CommonTool.isTelephone(mSubmitDemandMobileET.getText().toString())) {
            MessageUtils.showToast(getResources().getString(R.string.mTxt_telephone_prompt));
            return false;
        }
        return true;
    }

}
