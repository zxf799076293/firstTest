package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.mvppresenter.InvoiceInfoMvpPresenter;
import com.linhuiba.business.mvpview.InvoiceInfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/7/11.
 */
public class InvoiceInformationActivity extends BaseMvpActivity implements
        InvoiceInfoMvpView{
    @InjectView(R.id.invoice_price_tv)  TextView mInvoiceBtnPriceTV;
    @InjectView(R.id.invoiceinfomation_name)  EditText minvoiceinfomation_name;
    @InjectView(R.id.invoiceinfomation_mobile)  EditText minvoiceinfomation_mobile;
    @InjectView(R.id.invoiceinfomation_address)  TextView minvoiceinfomation_address;
    @InjectView(R.id.invoice_confirm_receipt_email_layout) RelativeLayout minvoice_confirm_receipt_email_layout;
    @InjectView(R.id.invoice_confirm_receipt_email_edit) EditText minvoice_confirm_receipt_email_edit;
    @InjectView(R.id.applyforinvoice_confirm_receipt_address_layout) LinearLayout mapplyforinvoice_confirm_receipt_address_layout;
    @InjectView(R.id.invoive_send_invoice_type_layout)
    RelativeLayout minvoive_send_invoice_type_layout;
    @InjectView(R.id.invoiceinfomation_submitbtn) Button minvoiceinfomation_submitbtn;
    @InjectView(R.id.invoice_price_textview) TextView minvoice_price_freetax_textview;

    @InjectView(R.id.invoice_show_info_ll) LinearLayout mInvoiceShowInfoLL;
    @InjectView(R.id.invoiceinfo_companyname_edit)
    TextView invoiceinfo_companyname_edit;
    @InjectView(R.id.invoiceinfomation_submit_layout)
    RelativeLayout mInvoiceInfoPayBtnRL;
    @InjectView(R.id.incoicce_info_show_all_tv)
    TextView mInvoiceShowAllListTV;
    @InjectView(R.id.invoice_info_remark_et)
    EditText mInvoiceRemarkET;
    @InjectView(R.id.invoice_paper_type_tv)
    TextView mInvoicePaperTypeTV;
    @InjectView(R.id.invoice_information_type_ll)
    LinearLayout mInvoiceTypeLL;
    @InjectView(R.id.invoice_type_tv)
    TextView mInvoiceTypeTV;

    private InvoiceInfomationModel invoiceInfomationModeldata = new InvoiceInfomationModel();
    private ArrayList<HashMap<Object,Object>> checkedlist = new ArrayList<>();
    private AddressContactModel Addressdata;
    private String address_id;
    private ArrayList<Field_AddResourceCreateItemModel> InvoiceContentDataList = new ArrayList<>();
    public String province_id = "";
    public String city_id = "";
    public String district_id = "";
    public boolean isShowAllInvoiceNameList;
    private Drawable mShowAllUpDrawable;//查看场地信息
    private Drawable mShowAllDownDrawable;//查看场地信息
    private InvoiceInfoMvpPresenter mInvoiceInfoMvpPresenter;
    private ArrayList<HashMap<String,String>> mInvoiceList;
    private Dialog mTaxDialog;
    private int mTaxSelectInt = -1;
    public  TextView mInvoiceCompanyEareTV;
    private ArrayList<Field_AddResourceCreateItemModel> mInvoiceChoosePaperList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoiceinfomation);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.invoiceinfo_title_tv_str));
        TitleBarUtils.showBackImg(this,true);
        initview();
    }
    private void initview() {
        mInvoiceInfoMvpPresenter = new InvoiceInfoMvpPresenter();
        mInvoiceInfoMvpPresenter.attachView(this);
        for (int i = 0; i < 2; i ++) {
            Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
            field_addResourceCreateItemModel.setId(i);
            if (i == 1) {
                field_addResourceCreateItemModel.setName(getResources().getString(R.string.applyforinvoice_paper_invoive_text));
            } else {
                field_addResourceCreateItemModel.setName(getResources().getString(R.string.applyforinvoice_electron_invoive_text));
            }
            mInvoiceChoosePaperList.add(field_addResourceCreateItemModel);
        }
        Intent invoiceintent = getIntent();
        if (invoiceintent.getExtras().get("checkedlist") != null &&
                invoiceintent.getExtras().get("invoice_list") != null) {
            checkedlist = (ArrayList<HashMap<Object,Object>>) invoiceintent.getSerializableExtra("checkedlist");
            mInvoiceList = (ArrayList<HashMap<String,String>>)invoiceintent.getSerializableExtra("invoice_list");
        } else {
            if (invoiceintent.getExtras().get("id") != null &&
                    invoiceintent.getExtras().get("id").toString().length() > 0 &&
                    invoiceintent.getExtras().get("price") != null &&
                    invoiceintent.getExtras().get("price").toString().length() > 0) {
                checkedlist = new ArrayList<>();
                HashMap<Object,Object> map = new HashMap<>();
                map.put("order_item_id",Integer.parseInt(invoiceintent.getExtras().get("id").toString()));
                checkedlist.add(map);
                mInvoiceList = new ArrayList<>();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("real_fee",Constants.getpricestring(invoiceintent.getExtras().get("price").toString(),100));
                mInvoiceList.add(hashMap);
            } else {
                MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                finish();
                return;
            }
        }
        if (checkedlist == null || checkedlist.size() == 0) {
            MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
            finish();
            return;
        }
        if (mInvoiceList != null && mInvoiceList.size() > 0) {
            double  total_fee = 0;
            for (int j = 0; j < mInvoiceList.size(); j++) {
                total_fee = total_fee +
                        Double.parseDouble(mInvoiceList.get(j).get("real_fee").toString());
            }
            mInvoiceBtnPriceTV.setText(Constants.getSearchPriceStr(String.valueOf(Double.parseDouble(String.valueOf(total_fee))), 0.01));
        } else {
            MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
            finish();
            return;
        }
        //2018/3/5 获取抬头信息
        showProgressDialog();
        mInvoiceInfoMvpPresenter.getInvoiceInfo();
        mShowAllUpDrawable = getResources().getDrawable(R.drawable.ic_close_green);
        mShowAllUpDrawable.setBounds(0, 0, mShowAllUpDrawable.getMinimumWidth(), mShowAllUpDrawable.getMinimumHeight());
        mShowAllDownDrawable = getResources().getDrawable(R.drawable.ic_into_green);
        mShowAllDownDrawable.setBounds(0, 0, mShowAllDownDrawable.getMinimumWidth(), mShowAllDownDrawable.getMinimumHeight());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInvoiceInfoMvpPresenter != null) {
            mInvoiceInfoMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.chooseaddress_layout,
            R.id.invoiceinfomation_submitbtn,
            R.id.incoicce_info_show_all_ll,
            R.id.invoive_send_invoice_type_layout,
            R.id.chooseinfomation_layout
    })
    public void invoiceinfomation (View view) {
        switch (view.getId()) {
            case R.id.chooseaddress_layout:
                Intent chooseContactaddress = new Intent(InvoiceInformationActivity.this,InvoiceInfoActivity.class);
                startActivityForResult(chooseContactaddress, 1);
                break;
            case R.id.invoiceinfomation_submitbtn:
                if (invoiceInfomationModeldata == null ||
                        invoiceInfomationModeldata.getId() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_choose_title));
                    return;
                }
                //
                if (invoiceInfomationModeldata.getIs_paper() == 1) {
                    if (address_id != null && address_id.length() > 0) {
                        invoiceInfomationModeldata.setAddress_id(Integer.parseInt(address_id));
                    }
                    if (!(minvoiceinfomation_address.getText().toString().length() > 0)) {
                        MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_address_errormsg));
                        return;
                    }
                    if (!(minvoiceinfomation_name.getText().toString().length() > 0)) {
                        MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_account_errormsg));
                        return;
                    }
                    if (!(minvoiceinfomation_mobile.getText().toString().length() > 0)) {
                        MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_account_errormsg));
                        return;
                    }
                    if (invoiceInfomationModeldata.getAddress_id() == 0) {
                        MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_account_errormsg));
                        return;
                    }

                    //2018/3/5 保存地址id

                } else {
                    if (!(minvoice_confirm_receipt_email_edit.getText().toString().length() > 0)) {
                        MessageUtils.showToast(getResources().getString(R.string.applyforinvoice_confirm_receipt_email_hinttext));
                        return;
                    }
                    if (!CommonTool.isEmail(minvoice_confirm_receipt_email_edit.getText().toString())) {
                        MessageUtils.showToast(getResources().getString(R.string.applyforrelease_email_errortxt));
                        return;
                    }
                    invoiceInfomationModeldata.setEmail(minvoice_confirm_receipt_email_edit.getText().toString());
                }
                invoiceInfomationModeldata.setTicket_remarks(mInvoiceRemarkET.getText().toString().trim());
                if (invoiceInfomationModeldata.getInvoice_content_id() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_invoice_hinttxt));
                    return;
                }
                if (invoiceInfomationModeldata.getTotal_fee() != null &&
                        invoiceInfomationModeldata.getTotal_fee().length() > 0 &&
                        Double.parseDouble(invoiceInfomationModeldata.getTotal_fee())*0.01 < 10) {
                    return;
                }
                mInvoiceInfoMvpPresenter.addInvoice(checkedlist,
                        invoiceInfomationModeldata.getId(),invoiceInfomationModeldata.getIs_paper(),
                        invoiceInfomationModeldata.getInvoice_content_id(),invoiceInfomationModeldata.getAddress_id(),
                        invoiceInfomationModeldata.getEmail(),invoiceInfomationModeldata.getTicket_remarks());                break;
            case R.id.incoicce_info_show_all_ll:
                if ((mTaxDialog != null && !mTaxDialog.isShowing()) ||
                        mTaxDialog == null) {
                    showTaxContentDialog(mInvoiceShowAllListTV,InvoiceContentDataList,1);
                }
                break;
            case R.id.invoive_send_invoice_type_layout:
                if ((mTaxDialog != null && !mTaxDialog.isShowing()) ||
                        mTaxDialog == null) {
                    showTaxContentDialog(mInvoicePaperTypeTV,mInvoiceChoosePaperList,2);
                }
                break;
            case R.id.chooseinfomation_layout:
                Intent titleListIntent = new Intent(InvoiceInformationActivity.this,InvoiceTitleListActivity.class);
                titleListIntent.putExtra("operation_type",1);
                startActivityForResult(titleListIntent,2);
                break;
            default:
                break;
        }
    }
    private void getInvoiveContentList(String data) {
        String liststr = data;
        if (liststr != null && liststr.length() > 0) {
            InvoiceContentDataList = (ArrayList<Field_AddResourceCreateItemModel>) JSONArray.parseArray(liststr,Field_AddResourceCreateItemModel.class);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data != null && data.getExtras() !=  null) {
                    if (data.getExtras().get("AddressContactModel") != null) {
                        Addressdata = (AddressContactModel)data.getSerializableExtra("AddressContactModel");
                        if (Addressdata.getId() != null) {
                            if (Addressdata.getId().length() > 0) {
                                address_id = Addressdata.getId();
                            }
                        }
                        if (Addressdata.getAddress() != null && Addressdata.getProvince() != null&& Addressdata.getCity() != null) {
                            if (Addressdata.getAddress().length() >0 && Addressdata.getProvince().length() >0 && Addressdata.getCity() .length() >0) {
                                minvoiceinfomation_address.setText(Addressdata.getProvince() + Addressdata.getCity() +
                                        Addressdata.getDistrict()+ Addressdata.getAddress());
                            } else {
                                minvoiceinfomation_address.setText("");
                            }
                        } else {
                            minvoiceinfomation_address.setText("");
                        }
                        if (Addressdata.getMobile() != null) {
                            minvoiceinfomation_mobile.setText(Addressdata.getMobile());
                        } else {
                            minvoiceinfomation_mobile.setText("");
                        }
                        if (Addressdata.getName() != null) {
                            minvoiceinfomation_name.setText(Addressdata.getName());
                        } else {
                            minvoiceinfomation_name.setText("");
                        }
                    }
                }
                break;
            case 2:
                if (data != null && data.getExtras() !=  null &&
                        data.getExtras().get("title_model") != null) {
                    InvoiceInfomationModel model = (InvoiceInfomationModel) data.getExtras().get("title_model");

                    if (invoiceInfomationModeldata == null) {
                        invoiceInfomationModeldata = new  InvoiceInfomationModel();
                    }
                    invoiceInfomationModeldata.setTitle(model.getTitle());
                    invoiceInfomationModeldata.setIs_default(model.getIs_default());
                    invoiceInfomationModeldata.setId(model.getId());
                    invoiceInfomationModeldata.setInvoice_type(model.getInvoice_type());
                    if (invoiceInfomationModeldata.getTitle() != null) {
                        invoiceinfo_companyname_edit.setText(invoiceInfomationModeldata.getTitle());
                    }
                    if (invoiceInfomationModeldata.getInvoice_type() > 0) {
                        mInvoiceTypeLL.setVisibility(View.VISIBLE);
                        showInvoiceType();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onInvoiceTaxPointSuccess(String data) {
        getInvoiveContentList(data);
        if (InvoiceContentDataList != null && InvoiceContentDataList.size() > 0) {
            int chooseItemInt = -1;
            if (invoiceInfomationModeldata != null &&
                    invoiceInfomationModeldata.getInvoice_content_id() > 0) {
                for (int i = 0; i < InvoiceContentDataList.size(); i++) {
                    if (InvoiceContentDataList.get(i).getId() -
                            invoiceInfomationModeldata.getInvoice_content_id() == 0) {
                        chooseItemInt = i;
                        break;
                    }
                }
            }
            if (chooseItemInt > -1) {
                mInvoiceShowAllListTV.setText(InvoiceContentDataList.get(chooseItemInt).getName());
            } else {
                mInvoiceShowAllListTV.setText(InvoiceContentDataList.get(0).getName());
                invoiceInfomationModeldata.setInvoice_content_id(InvoiceContentDataList.get(0).getId());
                invoiceInfomationModeldata.setInvoice_content(InvoiceContentDataList.get(0).getName());
            }
        }
    }

    @Override
    public void onInvoiceTaxPointFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onInvoiceInfoSuccess(InvoiceInfomationModel data) {
        mInvoiceInfoMvpPresenter.getInvoiceInfoTaxPoitn();
        invoiceInfomationModeldata = data;
        if (invoiceInfomationModeldata != null) {
            if (invoiceInfomationModeldata.getTitle() != null) {
                invoiceinfo_companyname_edit.setText(invoiceInfomationModeldata.getTitle());
            }
            if (invoiceInfomationModeldata.getInvoice_type() > 0) {
                mInvoiceTypeLL.setVisibility(View.VISIBLE);
                showInvoiceType();
            }
            if (invoiceInfomationModeldata.getArea() != null && invoiceInfomationModeldata.getArea().length() > 0) {
                minvoiceinfomation_address.setText(invoiceInfomationModeldata.getArea());
            }
            if (invoiceInfomationModeldata.getAddress_id() > 0) {
                address_id = String.valueOf(invoiceInfomationModeldata.getAddress_id());
            }
            if (invoiceInfomationModeldata.getMobile() != null) {
                minvoiceinfomation_mobile.setText(getResources().getString(R.string.txt_field_addfield_phone_prompt) +
                        invoiceInfomationModeldata.getMobile());
            }
            if (invoiceInfomationModeldata.getName() != null) {
                minvoiceinfomation_name.setText(getResources().getString(R.string.module_invoice_title_direction) +
                        invoiceInfomationModeldata.getName());
            }
            if (invoiceInfomationModeldata.getEmail() != null &&
                    invoiceInfomationModeldata.getEmail().length() > 0) {
                minvoice_confirm_receipt_email_edit.setText(invoiceInfomationModeldata.getEmail());
            }
            if (invoiceInfomationModeldata.getInvoice_content_id() > 0 &&
                    invoiceInfomationModeldata.getInvoice_content() != null &&
                    invoiceInfomationModeldata.getInvoice_content().length() > 0) {
                mInvoiceShowAllListTV.setText(invoiceInfomationModeldata.getInvoice_content());
            }
        } else {
            mInvoiceInfoMvpPresenter.getInvoiceInfoTaxPoitn();
            invoiceInfomationModeldata = new InvoiceInfomationModel();
            Constants.delete_picture_file();
        }
    }

    @Override
    public void onAddInvoiceSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.applyforinvoice_access_txt));
        Intent applyforinvoice = new Intent();
        setResult(1,applyforinvoice);
        finish();
    }

    @Override
    public void onAddInvoiceFailure(boolean superresult, Throwable error) {
        mInvoiceInfoMvpPresenter.getInvoiceInfoTaxPoitn();
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }

    private void showTaxContentDialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(com.linhuiba.linhuifield.R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.btn_confirm);
        final com.linhuiba.linhuifield.fieldview.WheelView mwheelview= (com.linhuiba.linhuifield.fieldview.WheelView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            wheelview_list.add(list.get(i).getName());
        }
        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (list.size() > 2) {
            mTaxSelectInt = 2;
            mwheelview.setSeletion(2);
        } else if (list.size() > 1) {
            mTaxSelectInt = 1;
            mwheelview.setSeletion(1);
        } else {
            mTaxSelectInt = 0;
            mwheelview.setSeletion(0);
        }
        mwheelview.setOnWheelViewListener(new com.linhuiba.linhuifield.fieldview.WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mTaxSelectInt = selectedIndex - 2;
            }
        });
        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(list.get(mTaxSelectInt).getName());
                if (type == 1) {
                    invoiceInfomationModeldata.setInvoice_content(list.get(mTaxSelectInt).getName());
                    invoiceInfomationModeldata.setInvoice_content_id(list.get(mTaxSelectInt).getId());
                } else if (type == 2) {
                    invoiceInfomationModeldata.setIs_paper(list.get(mTaxSelectInt).getId());
                    if (list.get(mTaxSelectInt).getId() == 1) {
                        mapplyforinvoice_confirm_receipt_address_layout.setVisibility(View.VISIBLE);
                        minvoice_confirm_receipt_email_layout.setVisibility(View.GONE);
                    } else if (list.get(mTaxSelectInt).getId() == 0) {
                        mapplyforinvoice_confirm_receipt_address_layout.setVisibility(View.GONE);
                        minvoice_confirm_receipt_email_layout.setVisibility(View.VISIBLE);
                    }
                }
                if (mTaxDialog != null) {
                    mTaxDialog.dismiss();
                }
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaxDialog != null) {
                    mTaxDialog.dismiss();
                }
            }
        });

        mTaxDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mTaxDialog);
    }
    private void showInvoiceType() {
        if (invoiceInfomationModeldata.getInvoice_type() == 1) {
            mInvoiceTypeTV.setText(getResources().getString(R.string.module_invoice_title_invoice_receipt_type));
            minvoive_send_invoice_type_layout.setVisibility(View.GONE);
            invoiceInfomationModeldata.setIs_paper(1);
            mapplyforinvoice_confirm_receipt_address_layout.setVisibility(View.VISIBLE);
            minvoice_confirm_receipt_email_layout.setVisibility(View.GONE);
        } else if (invoiceInfomationModeldata.getInvoice_type() == 2) {
            minvoive_send_invoice_type_layout.setVisibility(View.VISIBLE);
            invoiceInfomationModeldata.setIs_paper(0);
            mapplyforinvoice_confirm_receipt_address_layout.setVisibility(View.GONE);
            minvoice_confirm_receipt_email_layout.setVisibility(View.VISIBLE);
            mInvoicePaperTypeTV.setText(getResources().getString(R.string.applyforinvoice_electron_invoive_text));
            mInvoiceTypeTV.setText(getResources().getString(R.string.module_invoice_title_invoice_common_type));
        } else if (invoiceInfomationModeldata.getInvoice_type() == 3) {
            minvoive_send_invoice_type_layout.setVisibility(View.GONE);
            invoiceInfomationModeldata.setIs_paper(1);
            mapplyforinvoice_confirm_receipt_address_layout.setVisibility(View.VISIBLE);
            minvoice_confirm_receipt_email_layout.setVisibility(View.GONE);
            mInvoiceTypeTV.setText(getResources().getString(R.string.module_invoice_title_invoice_dedicated_type));
        }
    }

}
