package com.linhuiba.business.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.ManageContactAdapter;
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
 * Created by Administrator on 2016/7/8.
 */
public class ManageContactActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,Field_AddFieldChoosePictureCallBack.EditorAddress{
    @InjectView(R.id.managecontact_listview)
    LoadMoreListView mmanagecontact_listview;
    @InjectView(R.id.managecontact_swipe_refresh)
    SwipeRefreshLayout mswipList;
    @InjectView(R.id.lay_no_manafecontact)
    RelativeLayout mlay_no_manafecontact;
    private ManageContactAdapter manageContactAdapter;
    private int pagesize;
    private ArrayList<AddressContactModel> Addressdata;
    private int listitem;
    private String defaultaddressid = "";
    private int listitemposition;
    private boolean no_addresslist;//联系人列表是否为0
    public int mGetIntentInt;
    private static final int ADD_ACCOUNT_TYPE = 4;
    public static final int EDIT_ACCOUNT_TYPE = 3;
    public static final int ORDER_CHOOSE_ACCOUNT_TYPE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managecontact);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.managecontact_title_txt));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!no_addresslist && Addressdata != null && Addressdata.size() == 1 &&
                        mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
                    Intent intent = new Intent();
                    intent.putExtra("AddressContactModel",(Serializable)Addressdata.get(0));
                    setResult(ORDER_CHOOSE_ACCOUNT_TYPE,intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.myself_account_addtxt),15, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editoraddress = new Intent(ManageContactActivity.this, InvoiceInfoEditorAddress.class);
                editoraddress.putExtra("Operationtype", ADD_ACCOUNT_TYPE);
                if (mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
                    editoraddress.putExtra("order_choose", ORDER_CHOOSE_ACCOUNT_TYPE);
                }
                startActivityForResult(editoraddress,ADD_ACCOUNT_TYPE);
            }
        });
        initview();
        showProgressDialog();
        initdata();
    }
    private void initview() {
        mswipList.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mmanagecontact_listview.setLoadMoreListen(this);
        mswipList.setOnRefreshListener(this);
        mlay_no_manafecontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                initdata();
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("intentType") != null) {
            mGetIntentInt = intent.getIntExtra("intentType",0);
        }
    }
    private void initdata() {
        if (Addressdata != null) {
            Addressdata.clear();
        }
        pagesize = 1;
        mmanagecontact_listview.set_refresh();
        FieldApi.getaddresscontactlist(MyAsyncHttpClient.MyAsyncHttpClient(), AddresscontactlistHandler, String.valueOf(pagesize), "10");
    }

    @Override
    public void loadMore() {
        if (Addressdata != null) {
            if (Addressdata.size() != 0) {
                pagesize = pagesize + 1;
                FieldApi.getaddresscontactlist(MyAsyncHttpClient.MyAsyncHttpClient(), AddresscontactlistMoreHandler, String.valueOf(pagesize), "10");
            } else {
                mmanagecontact_listview.onLoadComplete();
            }
        } else {
            mmanagecontact_listview.onLoadComplete();
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
                        no_addresslist = true;
                        mlay_no_manafecontact.setVisibility(View.VISIBLE);
                        if(mswipList.isShown()){
                            mswipList.setRefreshing(false);
                        }
                        if (mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
                            Intent editoraddress = new Intent(ManageContactActivity.this, InvoiceInfoEditorAddress.class);
                            editoraddress.putExtra("Operationtype", ADD_ACCOUNT_TYPE);
                            if (mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
                                editoraddress.putExtra("order_choose", ORDER_CHOOSE_ACCOUNT_TYPE);
                            }
                            startActivityForResult(editoraddress,ADD_ACCOUNT_TYPE);
                        }
                        return;
                    }
                    ManageContactAdapter.clear_isSelectedlist_invoice();
                    mlay_no_manafecontact.setVisibility(View.GONE);
                    for (int i = 0; i < Addressdata.size(); i++) {
                        if (Addressdata.get(i).getIs_default() == 1) {
                            ManageContactAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),true);
                        } else {
                            ManageContactAdapter.getIsSelected_invoice().put(Addressdata.get(i).getId().toString(),false);
                        }

                    }
                    manageContactAdapter = new ManageContactAdapter(ManageContactActivity.this,ManageContactActivity.this,Addressdata,0);
                    mmanagecontact_listview.setAdapter(manageContactAdapter);
                    if (Addressdata.size() < 10) {
                        mmanagecontact_listview.set_loaded();
                    }
                    if(mswipList.isShown()){
                        mswipList.setRefreshing(false);
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mswipList.isShown()){
                mswipList.setRefreshing(false);
            }
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }

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
                        pagesize = pagesize-1;
                        mmanagecontact_listview.set_loaded();
                        return;
                    }
                    for( AddressContactModel order: tmp ){
                        Addressdata.add(order);
                    }
                    for (int i = 0; i < tmp.size(); i++) {
                        if (tmp.get(i).getIs_default() == 1) {
                            ManageContactAdapter.getIsSelected_invoice().put(tmp.get(i).getId().toString(),true);
                        } else {
                            ManageContactAdapter.getIsSelected_invoice().put(tmp.get(i).getId().toString(),false);
                        }

                    }

                    manageContactAdapter.notifyDataSetChanged();
                    mmanagecontact_listview.onLoadComplete();
                    if (tmp.size() < 10 ) {
                        mmanagecontact_listview.set_loaded();
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mmanagecontact_listview.onLoadComplete();
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess(error);
        }
    };
    @Override
    public void getfieldsize_pricenuit(int position, final String str) {
        listitem = position;
        final String addressid = str;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        FieldApi.deleteaddresscontact(ManageContactActivity.this,MyAsyncHttpClient.MyAsyncHttpClient(),DeleteaddressHandler,addressid);
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
            manageContactAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    public void editoraddress(AddressContactModel address,int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(address,position);
        test.edotoraddressinfo(this);
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
                            data.getMobile(),data.getProvince_id(),data.getCity_id(),data.getDistrict_id(),data.getAddress(),1);

                } else {
                    MessageUtils.showToast(this.getResources().getString(R.string.field_tupesize_errortoast));
                }
            } else {
                MessageUtils.showToast(this.getResources().getString(R.string.field_tupesize_errortoast));
            }
        }
    }
    private LinhuiAsyncHttpResponseHandler EditoraddressHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            initdata();
            mmanagecontact_listview.setSelection(listitemposition);

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == ADD_ACCOUNT_TYPE ||
                resultCode == EDIT_ACCOUNT_TYPE) &&
                        mGetIntentInt != ORDER_CHOOSE_ACCOUNT_TYPE) {
            initdata();
        } else if ((resultCode == ADD_ACCOUNT_TYPE ||
                resultCode == EDIT_ACCOUNT_TYPE ||
                resultCode == ORDER_CHOOSE_ACCOUNT_TYPE) &&
                        mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
            if (data != null && data.getExtras() != null &&
                    data.getExtras().get("AddressContactModel") != null &&
                    data.getExtras().get("AddressContactModel").toString().length() > 0) {
                AddressContactModel addressdata = (AddressContactModel) data.getSerializableExtra("AddressContactModel");
                Intent orderAccountIntent = new Intent();
                orderAccountIntent.putExtra("AddressContactModel", (Serializable) addressdata);
                setResult(ORDER_CHOOSE_ACCOUNT_TYPE, orderAccountIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (!no_addresslist && Addressdata != null && Addressdata.size() == 1 &&
                    mGetIntentInt == ORDER_CHOOSE_ACCOUNT_TYPE) {
                Intent intent = new Intent();
                intent.putExtra("AddressContactModel",(Serializable)Addressdata.get(0));
                setResult(ORDER_CHOOSE_ACCOUNT_TYPE,intent);
                finish();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
