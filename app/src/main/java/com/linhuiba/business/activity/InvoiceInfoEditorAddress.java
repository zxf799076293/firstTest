package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/5.
 */
public class InvoiceInfoEditorAddress extends BaseMvpActivity {
    @InjectView(R.id.invoiceinfo_address_areatxt)
    public TextView mareatxt;
    @InjectView(R.id.invoicename)  EditText minvoicename;
    @InjectView(R.id.invoicemobile)  EditText minvoicemobile;
    @InjectView(R.id.invoiceaddress)  EditText minvoiceaddress;
    @InjectView(R.id.invoicepostcode)  EditText minvoicepostcode;
    @InjectView(R.id.invoicedefault)  CheckBox minvoicedefault;
    @InjectView(R.id.defaultaddress_layout)
    LinearLayout mdefaultaddress_layout;
    @InjectView(R.id.invoiceinfo_address_arearelativelayout)
    RelativeLayout minvoiceinfo_address_arearelativelayout;
    @InjectView(R.id.address_layout)
    LinearLayout maddress_layout;
    @InjectView(R.id.address_layout_view)
    View maddress_layout_view;
    @InjectView(R.id.invoicepostcode_layout)
    LinearLayout minvoicepostcode_layout;
    @InjectView(R.id.invoicepostcode_view)
    View minvoicepostcode_view;
    @InjectView(R.id.contectname)
    TextView mcontectname;
    public String address_id;
    public String province_id;
    public String city_id;
    public String district_id;
    public String provinceStr;
    public String cityStr;
    public String districtStr;
    private int strdefault;
    private int Operationtype;//0:add地址;1:editor地址;2:详情；4管理联系人add;3管理联系人editor
    private AddressContactModel Addressdata;
    private Dialog choose_area_dialog;
    private static final int add_invoice_address = 0;//开票地址新增功能标识
    private static final int editor_invoice_address = 1;//开票地址编辑功能标识
    private static final int add_account = 4;//联系人新增功能标识
    private static final int editor_account = 3;//联系人编辑功能标识
    private static final int information_account = 2;//显示地址详情功能标识
    private static final int addAccountOrder = 5;//order联系人add功能标识
    private static final int ORDER_CHOOSE_ACCOUNT_TYPE = 1;
    private int mOrderChooseType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoiceeditoraddress);
        ButterKnife.inject(this);
        TitleBarUtils.showBackImg(this,true);
        initview();
    }
    private void initview() {
        minvoicedefault.setChecked(false);
        Intent it = getIntent();
        Operationtype = it.getExtras().getInt("Operationtype");
        if (it.getExtras().get("AddressContactModel") != null) {
            Addressdata = (AddressContactModel)it.getSerializableExtra("AddressContactModel");
        }
        if (it.getExtras().get("order_choose") != null) {
            mOrderChooseType = it.getExtras().getInt("order_choose",0);
        }
        if (Operationtype == add_invoice_address || Operationtype == editor_invoice_address) {
            if (Operationtype == add_invoice_address) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.add_address_txt));
            } else {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.editor_address_txt));
            }
            if (Addressdata != null) {
                if (Addressdata.getId() != null) {
                    if (Addressdata.getId().length() > 0) {
                        address_id = Addressdata.getId();
                    }
                }
                if (Addressdata.getName() != null) {
                    minvoicename.setText(Addressdata.getName());
                }
                if (Addressdata.getMobile() != null) {
                    minvoicemobile.setText(Addressdata.getMobile());
                }
                if (Addressdata.getDistrict() != null &&
                        Addressdata.getCity() != null && Addressdata.getProvince() != null &&
                        Addressdata.getDistrict_id() != null &&
                        Addressdata.getCity_id() != null && Addressdata.getProvince_id() != null) {
                    mareatxt.setText(Addressdata.getProvince()+Addressdata.getCity()+
                            Addressdata.getDistrict());
                    districtStr = Addressdata.getDistrict();
                    cityStr = Addressdata.getCity();
                    provinceStr = Addressdata.getProvince();
                }
                if (Addressdata.getAddress() != null) {
                    minvoiceaddress.setText(Addressdata.getAddress());
                }
                if (Addressdata.getPostcode() != null) {
                    minvoicepostcode.setText(Addressdata.getPostcode());
                }
                if (Addressdata.getIs_default() == 0) {
                    mdefaultaddress_layout.setVisibility(View.VISIBLE);
                    minvoicedefault.setChecked(false);
                } else if (Addressdata.getIs_default() == 1) {
                    mdefaultaddress_layout.setVisibility(View.GONE);
                }
                if (Addressdata.getProvince_id() != null) {
                    province_id = Addressdata.getProvince_id();
                }

                if (Addressdata.getCity() != null) {
                    city_id = Addressdata.getCity_id();
                }
                if (Addressdata.getDistrict_id() != null) {
                    district_id = Addressdata.getDistrict_id();
                }

            }

        } else if (Operationtype == editor_account || Operationtype == add_account ||
                Operationtype == addAccountOrder){
            if (Operationtype == editor_account) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.managecontacteditor_title_txt));
            } else {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.managecontactadd_title_txt));
            }
            mcontectname.setText(getResources().getString(R.string.myselfinfo_contactname));
            minvoicedefault.setText(getResources().getString(R.string.myselfinfo_defaultaccount_txt));
            if (Addressdata != null) {
                if (Addressdata.getId() != null) {
                    if (Addressdata.getId().length() > 0) {
                        address_id = Addressdata.getId();
                    }
                }
                if (Addressdata.getName() != null) {
                    minvoicename.setText(Addressdata.getName());
                }
                if (Addressdata.getMobile() != null) {
                    minvoicemobile.setText(Addressdata.getMobile());
                }
                if (Addressdata.getDistrict() != null &&
                        Addressdata.getCity() != null && Addressdata.getProvince() != null) {
                    mareatxt.setText(Addressdata.getProvince()+Addressdata.getCity()+
                            Addressdata.getDistrict());
                }
                if (Addressdata.getAddress() != null) {
                    minvoiceaddress.setText(Addressdata.getAddress());
                }
                if (Addressdata.getPostcode() != null) {
                    minvoicepostcode.setText(Addressdata.getPostcode());
                }
                if (Addressdata.getIs_default() == 0) {
                    mdefaultaddress_layout.setVisibility(View.VISIBLE);
                    minvoicedefault.setChecked(false);
                } else if (Addressdata.getIs_default() == 1) {
                    mdefaultaddress_layout.setVisibility(View.GONE);
                }
                if (Addressdata.getProvince_id() != null) {
                    province_id = Addressdata.getProvince_id();
                }

                if (Addressdata.getCity() != null) {
                    city_id = Addressdata.getCity_id();
                }
                if (Addressdata.getDistrict_id() != null) {
                    district_id = Addressdata.getDistrict_id();
                }
            }
        }
       if (Operationtype == information_account) {
            minvoicename.setEnabled(false);
            minvoicemobile.setEnabled(false);
            minvoiceaddress.setEnabled(false);
            minvoicepostcode.setEnabled(false);
            minvoicedefault.setEnabled(false);
            minvoiceinfo_address_arearelativelayout.setEnabled(false);
        }
    }
    @OnClick({
            R.id.invoiceinfo_address_arearelativelayout,
            R.id.invoiceeditor_savebtn,
            R.id.invoicedefault
    })
    public void onFunctionClick(View view) {
        switch (view.getId()) {
            case R.id.invoiceinfo_address_arearelativelayout:
                LayoutInflater factory = LayoutInflater.from(this);
                final View textEntryView = factory.inflate(R.layout.activity_districtselect, null);
                choose_area_dialog = new AlertDialog.Builder(this).create();
                DistrictSelectActivity districtSelectActivity = new DistrictSelectActivity(InvoiceInfoEditorAddress.this,textEntryView,choose_area_dialog,1);
                break;
            case R.id.invoiceeditor_savebtn:
                if (TextUtils.isEmpty(minvoicename.getText())) {
                    MessageUtils.showToast(getContext(), InvoiceInfoEditorAddress.this.getResources().getString(R.string.fieldinfo_diolog_contactname));
                    return;
                }
                if (!CommonTool.isMobileNO(minvoicemobile.getText().toString())) {
                    MessageUtils.showToast(getContext(), InvoiceInfoEditorAddress.this.getResources().getString(R.string.mTxt_mobile_prompt));
                    return;
                }
                if (Operationtype == editor_invoice_address || Operationtype == editor_account) {
                    if (mOrderChooseType != ORDER_CHOOSE_ACCOUNT_TYPE ||
                            Operationtype == add_invoice_address || Operationtype == editor_invoice_address) {
                        if (TextUtils.isEmpty(minvoiceaddress.getText())) {
                            MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_address_errormsg));
                            return;
                        }
                        if (province_id == null || city_id == null || district_id == null) {
                            MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_address_errormsg));
                            return;
                        }
                    }
                    showProgressDialog();
                    FieldApi.editoraddresscontact(MyAsyncHttpClient.MyAsyncHttpClient(),EditoraddressHandler,address_id,minvoicename.getText().toString(),
                            minvoicemobile.getText().toString(),province_id,city_id,district_id,minvoiceaddress.getText().toString(),
                           strdefault);

                } else if (Operationtype == add_invoice_address || Operationtype == add_account ||
                 Operationtype == addAccountOrder) {
                    if (mOrderChooseType != ORDER_CHOOSE_ACCOUNT_TYPE ||
                            Operationtype == add_invoice_address || Operationtype == editor_invoice_address) {
                        if (TextUtils.isEmpty(minvoiceaddress.getText())) {
                            MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_address_errormsg));
                            return;
                        }
                        if (province_id == null || city_id == null || district_id == null) {
                            MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_address_errormsg));
                            return;
                        }
                    }
                    showProgressDialog();
                    FieldApi.Addaddresscontact(MyAsyncHttpClient.MyAsyncHttpClient(),EditoraddressHandler,minvoicename.getText().toString(),
                            minvoicemobile.getText().toString(),province_id,city_id,district_id,minvoiceaddress.getText().toString(),
                            strdefault);
                }
                break;
            case R.id.invoicedefault:
                if (minvoicedefault.isChecked()) {
                    minvoicedefault.setChecked(true);
                    strdefault = 1;
                } else {
                    minvoicedefault.setChecked(false);
                    strdefault = 0;
                }
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler EditoraddressHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null &&
                    data.toString().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("address_id") != null &&
                        jsonObject.get("address_id").toString().length() > 0) {
                    address_id = jsonObject.get("address_id").toString();
                }
            }
            Intent orderAccountIntent = new Intent();
            if (mOrderChooseType == ORDER_CHOOSE_ACCOUNT_TYPE ||
                    Operationtype == add_invoice_address || Operationtype == editor_invoice_address) {
                AddressContactModel addressdata = new AddressContactModel();
                addressdata.setProvince(provinceStr);
                addressdata.setCity(cityStr);
                addressdata.setDistrict(districtStr);
                addressdata.setAddress(minvoiceaddress.getText().toString());
                addressdata.setName(minvoicename.getText().toString());
                addressdata.setMobile(minvoicemobile.getText().toString());
                addressdata.setCity_id(city_id);
                addressdata.setProvince_id(province_id);
                addressdata.setDistrict_id(district_id);
                addressdata.setId(address_id);
                orderAccountIntent.putExtra("AddressContactModel",(Serializable)addressdata);
            }
            setResult(Operationtype,orderAccountIntent);
            InvoiceInfoEditorAddress.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
}
