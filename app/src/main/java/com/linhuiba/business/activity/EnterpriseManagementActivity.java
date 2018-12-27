package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.model.Business_reportModel;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/12.
 */

public class EnterpriseManagementActivity extends BaseMvpActivity {
    @InjectView(R.id.enterprise_business_total_text)
    TextView menterprise_business_total_text;
    @InjectView(R.id.enterprise_business_today_stall)
    TextView menterprise_business_today_stall;
    @InjectView(R.id.enterprise_business_total_stall)
    TextView menterprise_business_total_stall;
    private Business_reportModel business_reportModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_management);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_enterprise_management_text));
        TitleBarUtils.showBackImg(this,true);
        ButterKnife.inject(this);
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginManager.getEnterprise_role() == 2) {
            this.finish();
        }
    }

    private void initview() {
        UserApi.getbusiness_report(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),business_reportHandler);
    }
    @OnClick({
            R.id.childaccount_chagemanage_relativelayout,
            R.id.childaccount_manage_relativelayout
    })
    public void myselfinfo_click(View view) {
        switch (view.getId()) {
            case R.id.childaccount_manage_relativelayout:
                Intent childaccount_manage = new Intent(EnterpriseManagementActivity.this, ChildAccountActivity.class);
                startActivity(childaccount_manage);
                break;
            case R.id.childaccount_chagemanage_relativelayout:
                Intent ReplaceAdministrator = new Intent(EnterpriseManagementActivity.this, ChildAccountReplaceAdministratorActivity.class);
                startActivity(ReplaceAdministrator);
                break;
            default:
                break;
        }
    }
    private com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler business_reportHandler = new com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler(Business_reportModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, com.linhuiba.business.network.Response response, Object data) {
            hideProgressDialog();
            business_reportModel = (Business_reportModel)data;
            if (business_reportModel != null) {
                menterprise_business_total_text.setText(Constants.getdoublepricestring(business_reportModel.getTotal_consumption(),0.01));
                menterprise_business_today_stall.setText(String.valueOf(business_reportModel.getCount_of_today()));
                menterprise_business_total_text.setText(String.valueOf(business_reportModel.getFinished_days()));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
}
