package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldadapter.Field_OrderRefuseAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/8/31.
 */
public class Field_OrderRefuseActivity extends FieldBaseMvpActivity {

    private FieldMyGridView mfieldrefused_mygridview;
    private Button mrefuse_agreebtn;
    private Button mrefuse_cancelbtn;
    private ArrayList<HashMap<String,String>> datelist = new ArrayList<>();
    private Field_OrderRefuseAdapter field_orderRefuseAdapter;
    public EditText mrefused_edit;
    private String approvedid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_ordersrefused);
        mfieldrefused_mygridview = (FieldMyGridView)findViewById(R.id.fieldview_refused_mygridview);
        mrefuse_agreebtn = (Button)findViewById(R.id.refuse_agreebtn);
        mrefuse_cancelbtn = (Button)findViewById(R.id.refuse_cancelbtn);
        resourcecalendarclick();
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.order_refuse_titletxt));
        TitleBarUtils.showBackImg(this, true);
        initview();
    }
    private void initview() {
        Intent field_orderintent = getIntent();
        if (field_orderintent.getExtras() != null) {
            approvedid = field_orderintent.getExtras().getString("approvedid");
        }
        mrefused_edit = (EditText)findViewById(R.id.refused_edittext);
        HashMap<String,String> map = new HashMap<>();
        map.put("reason", "档期已满");
        datelist.add(map);
        HashMap<String,String> maptwo = new HashMap<>();
        maptwo.put("reason", "商家品类冲突");
        datelist.add(maptwo);
        HashMap<String,String> mapthree = new HashMap<>();
        mapthree.put("reason", "订单已取消");
        datelist.add(mapthree);
        HashMap<String,String> mapfour = new HashMap<>();
        mapfour.put("reason", "其他");
        datelist.add(mapfour);
        for (int i = 0; i < datelist.size(); i++) {
            Field_OrderRefuseAdapter.getisSelected_addfield_orderrefused().put(datelist.get(i).get("reason").toString(),false);
        }
        field_orderRefuseAdapter = new Field_OrderRefuseAdapter(this,this,datelist,0);
        mfieldrefused_mygridview.setAdapter(field_orderRefuseAdapter);
    }
    private void resourcecalendarclick() {
        mrefuse_agreebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < datelist.size(); i++) {
                    if (Field_OrderRefuseAdapter.getisSelected_addfield_orderrefused().get(datelist.get(i).get("reason").toString())) {
                        if (datelist.get(i).get("reason").toString().equals("其他")) {
                            showProgressDialog();
                            if (mrefused_edit.getText().toString().trim().length() > 0) {
                                Field_FieldApi.fieldorderlistitemRefused(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), OrderapprovedHandler, Field_OrderRefuseActivity.this, approvedid, mrefused_edit.getText().toString());
                            } else {
                                Field_FieldApi.fieldorderlistitemRefused(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), OrderapprovedHandler, Field_OrderRefuseActivity.this, approvedid, datelist.get(i).get("reason").toString());
                            }
                            break;
                        } else {
                            showProgressDialog();
                            Field_FieldApi.fieldorderlistitemRefused(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), OrderapprovedHandler, Field_OrderRefuseActivity.this, approvedid, datelist.get(i).get("reason").toString());
                            break;
                        }

                    }
                }

            }
        });
        mrefuse_cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Field_OrderRefuseActivity.this.finish();
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler OrderapprovedHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            Intent it = new Intent();
            it.putExtra("refused",1);
            setResult(1, it);
            Field_OrderRefuseActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };

}
