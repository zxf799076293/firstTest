package com.linhuiba.linhuifield.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.FieldBaseFragment;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.ChildAccountManagementActivity;
import com.linhuiba.linhuifield.fieldactivity.CommunityResourcesActivity;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.Property_reportModel;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

/**
 * Created by Administrator on 2016/11/24.
 */

public class Field_MyseflFragment extends FieldBaseMvpFragment {
    private View mMainContent;
    private RelativeLayout mfield_exclusive_relativelayout;
    private RelativeLayout mfield_childaccount_relativelayout;
    private RelativeLayout mfield_childaccount_manage_relativelayout;
    private RelativeLayout mfield_payment_account_relativelayout;
    private RelativeLayout mfield_enterprise_certification_relativelayout;
    private RelativeLayout mfield_childaccount_chagemanage_relativelayout;
    private RelativeLayout mfield_helper_relativelayout;
    private LinearLayout menterprise_management_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_activity_fieldmyselffragment, container, false);
            TitleBarUtils.setTitleText(mMainContent,Field_MyseflFragment.this.getActivity().getResources().getString(R.string.field_myselffragment_title_text));
            if (!(LoginManager.isLogin())) {
                LoginManager.getInstance().clearLoginInfo();
                Intent intent = new Intent("com.business.loginActivity");
                startActivity(intent);
                Field_MyseflFragment.this.getActivity().finish();
            } else {
                if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
                    TitleBarUtils.showBackButton(mMainContent, Field_MyseflFragment.this.getActivity(),true,
                            Field_MyseflFragment.this.getActivity().getResources().getString(R.string.back),
                            Field_MyseflFragment.this.getActivity().getResources().getColor(R.color.default_bluebg),14);
                }
                mfield_exclusive_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_exclusive_relativelayout);
                mfield_childaccount_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_childaccount_relativelayout);
                mfield_childaccount_manage_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_childaccount_manage_relativelayout);
                mfield_payment_account_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_payment_account_relativelayout);
                menterprise_management_layout = (LinearLayout)mMainContent.findViewById(R.id.enterprise_management_layout);
                mfield_enterprise_certification_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_enterprise_certification_relativelayout);
                mfield_childaccount_chagemanage_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_childaccount_chagemanage_relativelayout);
                mfield_helper_relativelayout = (RelativeLayout)mMainContent.findViewById(R.id.field_helper_relativelayout);
                mfield_exclusive_relativelayout.setVisibility(View.VISIBLE);
                mfield_payment_account_relativelayout.setVisibility(View.VISIBLE);
                mfield_helper_relativelayout.setVisibility(View.VISIBLE);
                initview();
            }
        }
        return mMainContent;
    }
    private void initview() {
        if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
            mfield_childaccount_manage_relativelayout.setVisibility(View.GONE);
            mfield_childaccount_chagemanage_relativelayout.setVisibility(View.GONE);
            mfield_enterprise_certification_relativelayout.setVisibility(View.GONE);
            mfield_childaccount_relativelayout.setVisibility(View.GONE);
        } else {
            mfield_childaccount_relativelayout.setVisibility(View.VISIBLE);
            if (LoginManager.getEnterprise_authorize_status() == 1) {
                mfield_enterprise_certification_relativelayout.setVisibility(View.GONE);
            } else {
                mfield_enterprise_certification_relativelayout.setVisibility(View.VISIBLE);
            }
            if (LoginManager.getEnterprise_authorize_status() == 1) {
                mfield_childaccount_manage_relativelayout.setVisibility(View.VISIBLE);
                mfield_childaccount_chagemanage_relativelayout.setVisibility(View.VISIBLE);
            } else {
                mfield_childaccount_manage_relativelayout.setVisibility(View.GONE);
                mfield_childaccount_chagemanage_relativelayout.setVisibility(View.GONE);
            }
        }
        mfield_helper_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent("com.business.aboutActivity");
                help.putExtra("type", Config.HELP_WEB_INT);
                startActivity(help);
            }
        });
        mfield_exclusive_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent communityresource = new Intent(Field_MyseflFragment.this.getActivity(), CommunityResourcesActivity.class);
                startActivity(communityresource);
            }
        });
        mfield_childaccount_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent(Field_MyseflFragment.this.getActivity(), ChildAccountManagementActivity.class);
                startActivity(childaccount);
            }
        });
        mfield_childaccount_manage_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent("com.business.childAccountActivity");
                startActivity(childaccount);
            }
        });
        mfield_enterprise_certification_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enterpriseCertification = new Intent("com.business.enterpriseCertificationActivity");
                enterpriseCertification.putExtra("type", Config.ENTERPRISE_CERTIFICATE_INT);
                startActivity(enterpriseCertification);
            }
        });
        mfield_payment_account_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountinfointent = new Intent("com.business.aboutActivity");
                accountinfointent.putExtra("type", Config.RECEIVE_ACCOUNT_INT);
                startActivity(accountinfointent);
            }
        });
        mfield_childaccount_chagemanage_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childaccount = new Intent("com.business.childAccountReplaceAdministratorActivity");
                startActivityForResult(childaccount,1);
            }
        });
        menterprise_management_layout.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (LoginManager.getEnterprise_role() == 2) {
                Field_MyseflFragment.this.getActivity().finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
