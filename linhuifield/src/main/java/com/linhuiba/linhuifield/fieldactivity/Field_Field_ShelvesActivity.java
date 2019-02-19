package com.linhuiba.linhuifield.fieldactivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/13.
 */
public class Field_Field_ShelvesActivity extends FieldBaseMvpActivity {
    private TextView mchoose_date_textview;
    private EditText mfield_shelves_reason_edit;
    private String fieldId = "";
    private String strdate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_fieldshelves);
        mchoose_date_textview = (TextView)findViewById(R.id.choose_date_textview);
        mfield_shelves_reason_edit = (EditText)findViewById(R.id.field_shelves_reason_edit);
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.fieldlist_shelves_title_tv_str));
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.mTxt_save_fieldinfo), 17, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldId.length() > 0) {
                    if (TextUtils.isEmpty(mfield_shelves_reason_edit.getText().toString())) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.field_shelves_reasonhint));
                        return;
                    }
                    showProgressDialog();
                    Field_FieldApi.editFieldStatusRefused(Field_Field_ShelvesActivity.this, MyAsyncHttpClient.MyAsyncHttpClient3(), editFieldStatus_shelvesHandler, fieldId,
                            mfield_shelves_reason_edit.getText().toString(),strdate);
                }
            }
        });
        onChooseDateClick();
        initview();
    }
    private void initview() {
        Intent fieldlistintent = getIntent();
        if (fieldlistintent.getExtras()!= null) {
            if (fieldlistintent.getExtras().get("fieldId") != null) {
                if (Integer.parseInt(fieldlistintent.getExtras().getString("fieldId")) > 0) {
                    fieldId = fieldlistintent.getExtras().getString("fieldId");
                }
            }

        }
    }
    private void onChooseDateClick() {
        mchoose_date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();//当前时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);//设置当前日期
                calendar.add(Calendar.DAY_OF_MONTH, 1);//天数加一，为-1的话是天数减1
                final DatePickerDialog datedialog;
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker,
                                                  int year, int month, int dayOfMonth) {

                            }
                        };
                datedialog = new DatePickerDialog(Field_Field_ShelvesActivity.this,
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
                        mchoose_date_textview.setText(year + "年" +
                                (month + 1) + "月" + day + "日");
                        strdate = year + "-" + (month + 1) + "-" + day;

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
    private LinhuiAsyncHttpResponseHandler editFieldStatus_shelvesHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            Intent fieldlist = new Intent();
            fieldlist.putExtra("updata",1);
            setResult(2,fieldlist);
            Field_Field_ShelvesActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };

}
