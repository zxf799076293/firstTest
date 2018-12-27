package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.fieldadapter.ChildAccountManagementEditorAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;
import com.linhuiba.linhuifield.fieldmvppresenter.FieldAddFieldContactMvpPresenter;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldContactMvpViewl;
import com.linhuiba.linhuifield.fieldview.FieldMyListView;
import com.linhuiba.linhuifield.util.CommonTool;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22.
 */

public class FieldAddFieldOtherContactAccountActivity extends FieldBaseMvpActivity implements
        Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall, FieldAddFieldContactMvpViewl {
    private EditText medit_resource_respons_name;
    private EditText medit_resource_respons_mobile;
    private EditText medit_financial_name;
    private EditText medit_financial_mobile;
    private EditText maddfield_account_text;
    private EditText mTxt_mobile;
    private Button mAddfieldOptionalinfoBtn;
    private LinearLayout mAddfieldOptionalinfoBtnLL;
    private ChildAccountManagementEditorAdapter mAccountAdapter;
    private FieldMyListView mAccountLV;
    private LinearLayout mAccountListLL;
    private TextView mAccountAddTV;
    private LinearLayout mAccountAliRemindLL;
    private TextView mAccountAliRemindTV;
    private TextView mAccountMoneyRemindTV;
    private TextView mAddfieldAccountUnfloadTV;
    private ArrayList<ReceiveAccountModel> mAccountList = new ArrayList<>();

    private int payType;
    private String payAccount;
    private String openingBank;
    private String account_owner;
    private int payAccountId;
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private boolean isUnfload;
    private FieldAddFieldContactMvpPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_optionalinfo_othercontact);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        medit_resource_respons_name = (EditText)findViewById(R.id.edit_resource_respons_name);
        medit_resource_respons_mobile = (EditText)findViewById(R.id.edit_resource_respons_mobile);
        medit_financial_name = (EditText)findViewById(R.id.edit_financial_name);
        medit_financial_mobile = (EditText)findViewById(R.id.edit_financial_mobile);
        mTxt_mobile = (EditText)findViewById(R.id.txt_mobile);
        maddfield_account_text = (EditText)findViewById(R.id.addfield_account_text);
        mAddfieldOptionalinfoBtn = (Button) findViewById(R.id.addfield_optionalinfo_btn);
        mAddfieldOptionalinfoBtnLL = (LinearLayout) findViewById(R.id.addfield_optionalinfo_btn_ll);
        mAccountLV = (FieldMyListView) findViewById(R.id.account_listview);
        mAccountListLL = (LinearLayout) findViewById(R.id.account_listview_ll);
        mAccountAddTV = (TextView) findViewById(R.id.account_add_tv);
        mAccountAliRemindLL = (LinearLayout) findViewById(R.id.account_choose_account_type_ll);
        mAccountAliRemindTV = (TextView) findViewById(R.id.account_choose_account_type_tv);
        mAccountMoneyRemindTV = (TextView) findViewById(R.id.account_money_remind_tv);
        mAddfieldAccountUnfloadTV = (TextView) findViewById(R.id.addfield_choose_account_unfold_tv);
        mSortGreyUpDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_check);
        mSortGreyUpDrawable.setBounds(0, 0, mSortGreyUpDrawable.getMinimumWidth(), mSortGreyUpDrawable.getMinimumHeight());
        mSortGreyDownDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_uncheck);
        mSortGreyDownDrawable.setBounds(0, 0, mSortGreyDownDrawable.getMinimumWidth(), mSortGreyDownDrawable.getMinimumHeight());
        mPresenter = new FieldAddFieldContactMvpPresenter();
        mPresenter.attachView(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.addfield_optional_info_other_contact_title_text));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_contact(1);
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_contact(0);
                }
                savedata();
                FieldAddFieldOtherContactAccountActivity.this.onBackPressed();
            }
        });
        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            TitleBarUtils.setnextViewText(this,getResources().getString(R.string.mTxt_save_fieldinfo));
            TitleBarUtils.shownextTextView(this, getResources().getString(R.string.myselfinfo_save_pw), 14, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        savedata();
                        Intent addfield_main_intent = new Intent(FieldAddFieldOtherContactAccountActivity.this,FieldAddFieldMainActivity.class);
                        startActivity(addfield_main_intent);
                        Field_AddResourcesModel.getInstance().setIs_hava_field_contact(1);
                    }
                }
            });
            mAddfieldOptionalinfoBtnLL.setVisibility(View.GONE);
        } else {
            mAddfieldOptionalinfoBtnLL.setVisibility(View.VISIBLE);
            mAddfieldOptionalinfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInput()) {
                        savedata();
                        Intent addfield_main_intent = new Intent(FieldAddFieldOtherContactAccountActivity.this,FieldAddFieldMainActivity.class);
                        startActivity(addfield_main_intent);
                        Field_AddResourcesModel.getInstance().setIs_hava_field_contact(1);
                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setIsSearchChoose(0);
                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setIsSearchChoose(0);
                    }
                }
            });
        }
        mAccountAddTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.business.aboutActivity");
                intent.putExtra("type", Config.ADD_ACCOUNT_INT);
                startActivityForResult(intent,Config.ADD_ACCOUNT_INT);
            }
        });
        mAddfieldAccountUnfloadTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccountList.size() > 0) {
                    if (mAccountList.size() < 4) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_res_account_is_all));
                    } else {
                        if (isUnfload) {
                            mAddfieldAccountUnfloadTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                            mAccountAdapter.notifyDataSetChanged(false,mAccountList);
                            isUnfload = false;
                        } else {
                            mAddfieldAccountUnfloadTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                            mAccountAdapter.notifyDataSetChanged(true,mAccountList);
                            isUnfload = true;
                        }
                    }
                }
            }
        });
        showData();
        mPresenter.getBeneficiary();
    }
    private void showData() {
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getManager() != null) {
            medit_resource_respons_name.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getManager());
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getManager_mobile() != null) {
            medit_resource_respons_mobile.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getManager_mobile());
        }

        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getAccountant() != null) {
            medit_financial_name.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getAccountant());
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getAccountant_mobile() != null) {
            medit_financial_mobile.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getAccountant_mobile());
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter().length() > 0) {
            maddfield_account_text.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter());
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile().length() > 0) {
            mTxt_mobile.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getConnecter_mobile());
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().size() > 0) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getPay_type().length() > 0 &&
                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getUser_recevice_id() != null) {
                payAccountId = Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getUser_recevice_id();
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount() != null) {
                    payAccount = Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount();
                }
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount_owner() != null) {
                    account_owner = Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getAccount_owner();
                }
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getOpening_bank() != null) {
                    openingBank = Field_AddResourcesModel.getInstance().getResource().getResource_data().getReceivables_information().get(0).getOpening_bank();
                }
            }
        }
    }
    private void savedata() {
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setManager(medit_resource_respons_name.getText().toString().trim());
        if (medit_resource_respons_mobile.getText().toString().trim().length() > 0) {
            if (!CommonTool.isTelephone(medit_resource_respons_mobile.getText().toString())) {
                BaseMessageUtils.showToast(getResources().getString(R.string.mTxt_telephone_prompt));
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setManager_mobile("");
            } else {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setManager_mobile(medit_resource_respons_mobile.getText().toString().trim());
            }
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setManager_mobile("");
        }
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setAccountant(medit_financial_name.getText().toString().trim());
        if (medit_financial_mobile.getText().toString().trim().length() > 0) {
            if (!CommonTool.isTelephone(medit_financial_mobile.getText().toString())) {
                BaseMessageUtils.showToast(getContext(), FieldAddFieldOtherContactAccountActivity.this.getResources().getString(R.string.mTxt_telephone_prompt));
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setAccountant_mobile("");
            } else {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setAccountant_mobile(medit_financial_mobile.getText().toString().trim());
            }
        } else {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setAccountant_mobile("");
        }
        ArrayList<ReceiveAccountModel> list = new ArrayList<>();
        ReceiveAccountModel receiveAccountModel = new ReceiveAccountModel();
        receiveAccountModel.setPay_type(String.valueOf(payType));
        receiveAccountModel.setAccount(payAccount);
        receiveAccountModel.setAccount_owner(account_owner);
        receiveAccountModel.setOpening_bank(openingBank);
        receiveAccountModel.setUser_recevice_id(payAccountId);
        list.add(receiveAccountModel);
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setReceivables_information(list);
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setConnecter(maddfield_account_text.getText().toString().trim());
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setConnecter_mobile(mTxt_mobile.getText().toString().trim());
    }
    private boolean checkInput() {
        boolean result = false;
        if (maddfield_account_text.getText().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_optional_info_resource_responsible_name_hinttext));
            return result;
        }
        if (mTxt_mobile.getText().length() == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.fieldinfo_diolog_contactmobile));
            return result;
        }
        if (!CommonTool.isTelephone(mTxt_mobile.getText().toString())) {
            BaseMessageUtils.showToast(getResources().getString(R.string.mTxt_telephone_prompt));
            return result;
        }
        if (medit_resource_respons_mobile.getText().toString().trim().length() > 0) {
            if (!CommonTool.isTelephone(medit_resource_respons_mobile.getText().toString())) {
                BaseMessageUtils.showToast(getContext(), FieldAddFieldOtherContactAccountActivity.this.getResources().getString(R.string.mTxt_telephone_prompt));
                return result;
            }
        }
        if (medit_financial_mobile.getText().toString().trim().length() > 0) {
            if (!CommonTool.isTelephone(medit_financial_mobile.getText().toString())) {
                BaseMessageUtils.showToast(getContext(), FieldAddFieldOtherContactAccountActivity.this.getResources().getString(R.string.mTxt_telephone_prompt));
                return result;
            }
        }
        for (int j = 0; j < mAccountList.size(); j++) {
            if (ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().get(String.valueOf(mAccountList.get(j).getId()))) {
                payType = Integer.parseInt(mAccountList.get(j).getPay_type());
                payAccountId = mAccountList.get(j).getId();
                openingBank = mAccountList.get(j).getOpening_bank();
                account_owner = mAccountList.get(j).getAccount_owner();
                break;
            }
        }

        if (payType == 0) {
            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_other_account_remind_str));
            return result;
        } else {
            if (payType != 1) {
                if (payAccount == null || payAccount.length() == 0) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_other_account_remind_str));
                    return result;
                }
                if (payType == 2) {
                    if (openingBank == null || openingBank.length() == 0) {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_other_account_remind_str));
                        return result;
                    }
                }
            }
        }

        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            if (checkInput()) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_contact(1);
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_contact(0);
            }
            savedata();
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void showPayAccountView() {
        if (mAccountList != null && mAccountList.size() > 0) {
            int position = 0;
            for (int j = 0; j < mAccountList.size(); j++) {
                if (payAccountId == mAccountList.get(j).getId()) {
                    position = j;
                    ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(String.valueOf(mAccountList.get(j).getId()),true);
                } else {
                    ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(String.valueOf(mAccountList.get(j).getId()),false);
                }
            }
            mAccountList.add(0,mAccountList.get(position));
            mAccountList.remove(position + 1);
            mAccountAdapter = new ChildAccountManagementEditorAdapter(FieldAddFieldOtherContactAccountActivity.this,FieldAddFieldOtherContactAccountActivity.this,mAccountList,1);
            mAccountLV.setAdapter(mAccountAdapter);
            mAccountAdapter.notifyDataSetChanged(false,mAccountList);
        } else {
            if (Field_AddResourcesModel.getInstance().getOptions() != null &&
                    Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations() != null &&
                    Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().size() > 0) {
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().size(); i++) {
                    if (Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().get(i).getPay_type() != null
                            && Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().get(i).getPay_type().length() > 0 &&
                            Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().get(i).getAccount() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().get(i).getAccount().length() > 0) {
                        mAccountList.add(Field_AddResourcesModel.getInstance().getOptions().getUser_receivables_informations().get(i));
                    }
                }
            } else {
                mAccountList = new ArrayList<>();
            }
            mAccountAdapter = new ChildAccountManagementEditorAdapter(FieldAddFieldOtherContactAccountActivity.this,FieldAddFieldOtherContactAccountActivity.this,mAccountList,1);
            mAccountLV.setAdapter(mAccountAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.ADD_ACCOUNT_INT:
                //收款账号刷新
                mPresenter.getBeneficiary();
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void setClick(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }
    @Override
    public void AdField_showPreviewImg(int position) {
        for (int j = 0; j < mAccountList.size(); j++) {
            if (j == position) {
                if (mAccountList.get(j).getAccount_owner() == null ||
                        mAccountList.get(j).getAccount() == null ||
                        mAccountList.get(j).getPay_type() == null ||
                        mAccountList.get(j).getAccount_owner().length() == 0 ||
                        mAccountList.get(j).getAccount().length() == 0 ||
                        mAccountList.get(j).getPay_type().length() == 0) {
                    ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(String.valueOf(mAccountList.get(j).getId()),false);
                    BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_phy_res_other_account_hint));
                    return;
                }
                ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(String.valueOf(mAccountList.get(j).getId()),true);
                mAccountList.add(0,mAccountList.get(position));
                mAccountList.remove(position + 1);
                payAccount = mAccountList.get(j).getAccount();
                if (mAccountList.get(j).getOpening_bank() != null) {
                    openingBank = mAccountList.get(j).getOpening_bank();
                }
                account_owner = mAccountList.get(j).getOpening_bank();
            } else {
                ChildAccountManagementEditorAdapter.getIsSelected_editorchildaccount().put(String.valueOf(mAccountList.get(j).getId()),false);
            }
        }
        mAccountAdapter.notifyDataSetChanged(isUnfload,mAccountList);
    }

    @Override
    public void onFieldAddContactSuccess(ArrayList<ReceiveAccountModel> list) {
        ArrayList<ReceiveAccountModel> models = list;
        if (models != null && models.size() > 0) {
            for (int i = 0; i < models.size(); i++) {
                if ( models.get(i).getPay_type() != null &&
                        models.get(i).getPay_type().length() > 0) {
                    if (models.get(i).getPay_type().equals(getResources().getString(R.string.addfield_other_account_money_tv_str))) {
                        models.get(i).setPay_type("1");
                    } else if (models.get(i).getPay_type().equals(getResources().getString(R.string.addfield_other_account_weixin_tv_str))) {
                        models.get(i).setPay_type("3");
                    } else if (models.get(i).getPay_type().equals(getResources().getString(R.string.addfield_other_account_alipay_tv_str))) {
                        models.get(i).setPay_type("4");
                    } else if (models.get(i).getPay_type().equals(getResources().getString(R.string.addfield_other_account_bank_tv_str))) {
                        models.get(i).setPay_type("2");
                    } else if (models.get(i).getPay_type().equals(getResources().getString(R.string.addfield_other_account_business_to_business))) {
                        models.get(i).setPay_type("5");
                    } else {
                        models.get(i).setPay_type("0");
                    }
                } else {
                    models.get(i).setPay_type("1");
                }
            }
        }
        mAccountList = models;
        ChildAccountManagementEditorAdapter.clear_isSelectedlist_editorchildaccount();
        showPayAccountView();

    }

    @Override
    public void onFieldAddContactFailure(boolean superresult, Throwable error) {
        if (!superresult)
            BaseMessageUtils.showToast(error.getMessage());
        checkAccess(error);
    }
}
