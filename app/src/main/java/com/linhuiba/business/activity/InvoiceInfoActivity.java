package com.linhuiba.business.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.InvoiceInfoAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_LoadMoreListView;
import com.linhuiba.business.model.AddressContactModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/7/5.
 */
public class InvoiceInfoActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,Field_AddFieldChoosePictureCallBack.EditorAddress{
    @InjectView(R.id.invoiceinfo_listview)
    LoadMoreListView minvoiceinfo_listview;
    @InjectView(R.id.invoiceinfo_swipe_refresh) SwipeRefreshLayout mswipeRefreshLayout;
    @InjectView(R.id.lay_no_review)
    RelativeLayout mlay_no_review;
    private InvoiceInfoAdapter invoiceInfoAdapter;
    private ArrayList<AddressContactModel> Addressdata;
    private int reviewlistpagesize;
    private int listitem;//删除的item的id
    private int listitemposition;//删除的item的position
    private String defaultaddressid = "";//默认地址的id
    private static final int ADD_INVOICE_ADDRESS = 0;
    public static final int EDIT_INVOICE_ADDRESS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoiceinfo);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.invoiceinfo_txt));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.invoiceinfo_add_address_txt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editoraddress = new Intent(InvoiceInfoActivity.this, InvoiceInfoEditorAddress.class);
                editoraddress.putExtra("Operationtype", ADD_INVOICE_ADDRESS);
                editoraddress.putExtra("order_choose", EDIT_INVOICE_ADDRESS);
                startActivityForResult(editoraddress,ADD_INVOICE_ADDRESS);
            }
        });
        initview();
        showProgressDialog();
        initdata();
    }
    private void initview() {
        mswipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        minvoiceinfo_listview.setLoadMoreListen(this);
        mswipeRefreshLayout.setOnRefreshListener(this);
        mlay_no_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                initdata();
            }
        });
    }
    private void initdata() {
        if (Addressdata != null) {
            Addressdata.clear();
        }
        reviewlistpagesize = 1;
        minvoiceinfo_listview.set_refresh();
        FieldApi.getaddresscontactlist(MyAsyncHttpClient.MyAsyncHttpClient(), AddresscontactlistHandler, String.valueOf(reviewlistpagesize), "10");
    }

    @Override
    public void loadMore() {
        if (Addressdata != null) {
            if (Addressdata.size() != 0) {
                reviewlistpagesize = reviewlistpagesize + 1;
                FieldApi.getaddresscontactlist(MyAsyncHttpClient.MyAsyncHttpClient(), AddresscontactlistMoreHandler, String.valueOf(reviewlistpagesize), "10");
            } else {
                minvoiceinfo_listview.onLoadComplete();
            }
        } else {
            minvoiceinfo_listview.onLoadComplete();
        }
    }

    @Override
    public void onRefresh() {
        initdata();
    }
    private LinhuiAsyncHttpResponseHandler AddresscontactlistHandler= new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data.toString().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("data") != null) {
                    ArrayList<AddressContactModel> list = (ArrayList<AddressContactModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                            AddressContactModel.class);
                    Addressdata = list;
                    if (Addressdata == null || Addressdata.isEmpty()) {
                        mlay_no_review.setVisibility(View.VISIBLE);
                        if(mswipeRefreshLayout.isShown()){
                            mswipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }
                    InvoiceInfoAdapter.clear_isSelectedlist_invoice();
                    mlay_no_review.setVisibility(View.GONE);
                    for (int i = 0; i < Addressdata.size(); i++) {
                        if (Addressdata.get(i).getIs_default() == 1) {
                            InvoiceInfoAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),true);
                        } else {
                            InvoiceInfoAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),false);
                        }

                    }
                    invoiceInfoAdapter = new InvoiceInfoAdapter(InvoiceInfoActivity.this,InvoiceInfoActivity.this,Addressdata,0);
                    minvoiceinfo_listview.setAdapter(invoiceInfoAdapter);
                    if (Addressdata.size() < 10) {
                        minvoiceinfo_listview.set_loaded();
                    }
                    if(mswipeRefreshLayout.isShown()){
                        mswipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mswipeRefreshLayout.isShown()){
                mswipeRefreshLayout.setRefreshing(false);
            }
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess_new(error);

        }
    };
    private LinhuiAsyncHttpResponseHandler AddresscontactlistMoreHandler= new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data.toString().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("data") != null) {
                    ArrayList<AddressContactModel> list = (ArrayList<AddressContactModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                            AddressContactModel.class);
                    ArrayList<AddressContactModel> tmp = list;
                    if( (tmp == null || tmp.isEmpty())){
                        reviewlistpagesize = reviewlistpagesize-1;
                        minvoiceinfo_listview.set_loaded();
                        return;
                    }
                    for( AddressContactModel order: tmp ){
                        Addressdata.add(order);
                    }
                    for (int i = 0; i < tmp.size(); i++) {
                        if (tmp.get(i).getIs_default() == 1) {
                            InvoiceInfoAdapter.getIsSelected_invoice().put(tmp.get(i).getId().toString(),true);
                        } else {
                            InvoiceInfoAdapter.getIsSelected_invoice().put(tmp.get(i).getId().toString(),false);
                        }

                    }

                    invoiceInfoAdapter.notifyDataSetChanged();
                    minvoiceinfo_listview.onLoadComplete();
                    if (tmp.size() < 10 ) {
                        minvoiceinfo_listview.set_loaded();
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            minvoiceinfo_listview.onLoadComplete();
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess_new(error);
        }
    };

    @Override
    public void getfieldsize_pricenuit(int position, final String str) {
        listitem = position;
        final String deleteid = str;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        FieldApi.deleteaddresscontact(InvoiceInfoActivity.this, MyAsyncHttpClient.MyAsyncHttpClient(), DeleteaddressHandler, deleteid);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


    }
    public void deleteinvoicelistitem(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    private LinhuiAsyncHttpResponseHandler DeleteaddressHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(response.msg);
            Addressdata.remove(listitem);
            invoiceInfoAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ADD_INVOICE_ADDRESS ||
                resultCode == EDIT_INVOICE_ADDRESS) {
            if (data != null && data.getExtras() != null &&
                    data.getExtras().get("AddressContactModel") != null &&
                    data.getExtras().get("AddressContactModel").toString().length() > 0) {
                AddressContactModel addressdata = (AddressContactModel) data.getSerializableExtra("AddressContactModel");
                Intent orderAccountIntent = new Intent();
                orderAccountIntent.putExtra("AddressContactModel", (Serializable) addressdata);
                setResult(EDIT_INVOICE_ADDRESS, orderAccountIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void editor(AddressContactModel data, int position) {
        if (data != null) {
            listitemposition = position;
            showProgressDialog();
            defaultaddressid = data.getId();
            if (defaultaddressid != null) {
                if (defaultaddressid.length() > 0) {
                    FieldApi.editoraddresscontact(MyAsyncHttpClient.MyAsyncHttpClient(),EditoraddressHandler,data.getId(),data.getName(),
                            data.getMobile(),data.getProvince_id(),data.getCity_id(),data.getDistrict_id(),data.getAddress(),
                            1);

                } else {
                    MessageUtils.showToast(this.getResources().getString(R.string.field_tupesize_errortoast));
                }
            } else {
                MessageUtils.showToast(this.getResources().getString(R.string.field_tupesize_errortoast));
            }
        }
    }
    public void editoraddress(AddressContactModel address,int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(address,position);
        test.edotoraddressinfo(this);
    }
    private LinhuiAsyncHttpResponseHandler EditoraddressHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            for (int i = 0; i < Addressdata.size(); i++) {
                if (Addressdata.get(i).getId().equals(defaultaddressid)) {
                    InvoiceInfoAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),true);
                } else {
                    InvoiceInfoAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),false);
                }

            }
            initdata();
            minvoiceinfo_listview.setSelection(listitemposition);
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
