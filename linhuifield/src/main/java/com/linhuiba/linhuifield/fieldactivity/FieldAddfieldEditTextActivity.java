package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.util.TitleBarUtils;

public class FieldAddfieldEditTextActivity extends FieldBaseMvpActivity {
    private EditText mEditText;
    private TextView mTextView;
    private int attribute_id;
    private int commonType = -1;//1 描述
    private int mStrSize = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_field_activity_addfield_edittext);
        initView();
    }
    private void initView() {
        mEditText = (EditText) findViewById(R.id.addfield_text_et);
        mTextView = (TextView) findViewById(R.id.addfield_text_tv);
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("attribute_id") != null) {
            attribute_id = intent.getExtras().getInt("attribute_id");
        }
        if (intent.getExtras() != null &&
                intent.getExtras().get("common_type") != null) {
            commonType = intent.getExtras().getInt("common_type");
        }
        if (intent.getExtras() != null &&
                intent.getExtras().get("edit_str") != null) {
            mEditText.setText(intent.getExtras().get("edit_str").toString());
        }
        if (intent.getExtras() != null &&
                intent.getExtras().get("str_size") != null) {
            mStrSize = (intent.getExtras().getInt("str_size"));
        }
        mTextView.setText("0/"+String.valueOf(mStrSize));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.mTxt_save_fieldinfo),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        intent.putExtra("common_type",commonType);
                        intent.putExtra("attribute_id",attribute_id);
                        intent.putExtra("edit_str",mEditText.getText().toString());
                        setResult(2,intent);
                        finish();
                    }
                });

        mEditText.addTextChangedListener(new TextWatcher() {
            boolean islMaxCount;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence e, int start, int before, int count) {
                Editable editable = mEditText.getText();
                int len = editable.length();
                if(len > mStrSize) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0,mStrSize);
                    mEditText.setText(newStr);
                    editable = mEditText.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                } else {
                    int detailLength = e.length();
                    mTextView.setText(detailLength + "/" + String.valueOf(mStrSize));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
