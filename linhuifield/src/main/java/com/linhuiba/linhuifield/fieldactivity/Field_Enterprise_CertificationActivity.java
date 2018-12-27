package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.Property_reportModel;
import com.linhuiba.linhuifield.fragment.Field_MyseflFragment;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

/**
 * Created by Administrator on 2017/6/3.
 */

public class Field_Enterprise_CertificationActivity extends FieldBaseMvpActivity {
    private LinearLayout menterprise_management_layout;
    private TextView mfield_myself_fieldnumber;
    private TextView mfield_myself_activitynumber;
    private TextView mfield_myself_today_order_number;
    private TextView mfield_myself_cumulative_deal_number;
    private TextView mfield_myself_income_text;

    private RelativeLayout mfield_childaccount_chagemanage_relativelayout;
    private RelativeLayout mfield_childaccount_manage_relativelayout;
    private Property_reportModel property_reportModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_fieldmyselffragment);
        initView();
        initData();
    }
    private void initView () {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_term_management_text));
        TitleBarUtils.showBackImg(this,true);
        mfield_myself_fieldnumber = (TextView)findViewById(R.id.field_myself_fieldnumber);
        mfield_myself_activitynumber = (TextView)findViewById(R.id.field_myself_activitynumber);
        mfield_myself_today_order_number = (TextView)findViewById(R.id.field_myself_today_order_number);
        mfield_myself_cumulative_deal_number = (TextView)findViewById(R.id.field_myself_cumulative_deal_number);
        mfield_myself_income_text = (TextView)findViewById(R.id.field_myself_income_text);
        menterprise_management_layout = (LinearLayout)findViewById(R.id.enterprise_management_layout);
        mfield_childaccount_chagemanage_relativelayout = (RelativeLayout)findViewById(R.id.field_childaccount_chagemanage_relativelayout);
        mfield_childaccount_manage_relativelayout = (RelativeLayout)findViewById(R.id.field_childaccount_manage_relativelayout);
        if (LoginManager.getEnterprise_role() == 1) {
            mfield_childaccount_manage_relativelayout.setVisibility(View.VISIBLE);
            mfield_childaccount_chagemanage_relativelayout.setVisibility(View.VISIBLE);
        } else {
            mfield_childaccount_manage_relativelayout.setVisibility(View.GONE);
            mfield_childaccount_chagemanage_relativelayout.setVisibility(View.GONE);
        }
        mfield_childaccount_manage_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent("com.business.childAccountActivity");
                startActivity(childaccount);
            }
        });
        mfield_childaccount_chagemanage_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent("com.business.childAccountReplaceAdministratorActivity");
                startActivityForResult(childaccount,1);
            }
        });
    }
    private void initData() {
        Field_FieldApi.getproperty_report(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),property_reportHandler);
    }
    private LinhuiAsyncHttpResponseHandler property_reportHandler = new LinhuiAsyncHttpResponseHandler(Property_reportModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            property_reportModel = (Property_reportModel)data;
            mfield_myself_fieldnumber.setText(String.valueOf(property_reportModel.getCount_of_field()));
            mfield_myself_activitynumber.setText(String.valueOf(property_reportModel.getCount_of_activity()));
            mfield_myself_today_order_number.setText(String.valueOf(property_reportModel.getCount_of_order_today()));
            mfield_myself_cumulative_deal_number.setText(String.valueOf(property_reportModel.getCount_of_order()));
            mfield_myself_income_text.setText(Constants.getdoublepricestring(property_reportModel.getIncome(),0.01));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (LoginManager.getEnterprise_role() == 2) {
                Field_Enterprise_CertificationActivity.this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
