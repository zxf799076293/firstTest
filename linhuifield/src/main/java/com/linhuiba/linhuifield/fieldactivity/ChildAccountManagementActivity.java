package com.linhuiba.linhuifield.fieldactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldadapter.ChildAccountManagementAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.ChildAccountManagementAllModel;
import com.linhuiba.linhuifield.fieldmodel.ChildAccountManagementModel;
import com.linhuiba.linhuifield.fieldview.FieldLoadMoreListView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ChildAccountManagementActivity extends FieldBaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, FieldLoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall{
    private FieldLoadMoreListView mchildaccount_listview;
    private SwipeRefreshLayout mchildaccount_swipe_refresh;
    private RelativeLayout mLay_no_childaccount;
    private LinearLayout mNoticeGuardLL;
    private ChildAccountManagementAdapter childAccountManagementAdapter;
    private int listpagesize;
    private ArrayList<ChildAccountManagementModel> childAccountlistdata;
    private ChildAccountManagementAllModel childAccountManagementAllModel;
    public ArrayList<HashMap<String,String>> resListmap = new ArrayList<>();
    private int deletelistposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_childaccountmanagement);
        mchildaccount_listview = (FieldLoadMoreListView)findViewById(R.id.childaccount_loadmorelistview);
        mchildaccount_swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.childaccount_swipe_refresh);
        mLay_no_childaccount = (RelativeLayout)findViewById(R.id.lay_no_childaccount);
        mNoticeGuardLL = (LinearLayout)findViewById(R.id.field_notice_guard_ll);

        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_childaccount_titletxt));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextOtherButton(ChildAccountManagementActivity.this, getResources().getString(R.string.txt_childaccount_titleaddtxt), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editchildaccountintent = new Intent(ChildAccountManagementActivity.this, ChildAccountManagementEditorActivity.class);
                editchildaccountintent.putExtra("type",0);
                editchildaccountintent.putExtra("resListmap",(Serializable)resListmap);
                startActivityForResult(editchildaccountintent, 0);
            }
        });

        initview();
        initdata();
    }
    private void initview() {
        mchildaccount_listview.setLoadMoreListen(this);
        mchildaccount_swipe_refresh.setOnRefreshListener(this);
        mchildaccount_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mLay_no_childaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                initdata();
            }
        });
        mNoticeGuardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent("com.business.aboutActivity");
                account.putExtra("type", Config.GUARD_WEB_INT);
                startActivity(account);
            }
        });
    }
    private void initdata() {
        listpagesize = 1;
        mchildaccount_listview.set_refresh();
        Field_FieldApi.getmessageuserlistitems(MyAsyncHttpClient.MyAsyncHttpClient(),getmessageHandler,String.valueOf(listpagesize),"10");
    }
    private LinhuiAsyncHttpResponseHandler getmessageHandler = new LinhuiAsyncHttpResponseHandler(ChildAccountManagementAllModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if(mchildaccount_swipe_refresh.isShown()){
                mchildaccount_swipe_refresh.setRefreshing(false);
            }
            childAccountManagementAllModel = (ChildAccountManagementAllModel)data;
            if (childAccountManagementAllModel.getMessage() != null) {
                childAccountlistdata =  childAccountManagementAllModel.getMessage();
                if(childAccountlistdata == null ||  childAccountlistdata.isEmpty()) {
                    mLay_no_childaccount.setVisibility(View.VISIBLE);
                    mNoticeGuardLL.setVisibility(View.VISIBLE);
                } else {
                    mLay_no_childaccount.setVisibility(View.GONE);
                    mNoticeGuardLL.setVisibility(View.GONE);
                    childAccountManagementAdapter = new ChildAccountManagementAdapter(ChildAccountManagementActivity.this,ChildAccountManagementActivity.this,childAccountlistdata,0);
                    mchildaccount_listview.setAdapter(childAccountManagementAdapter);
                    if (childAccountlistdata.size() < 10) {
                        mchildaccount_listview.set_loaded();
                    }
                }
            }
            if (childAccountManagementAllModel.getResList() != null) {
                if (childAccountManagementAllModel.getResList().size() > 0) {
                    resListmap = childAccountManagementAllModel.getResList();
                } else {
                    mLay_no_childaccount.setVisibility(View.VISIBLE);
                    mNoticeGuardLL.setVisibility(View.VISIBLE);
                }
            } else {
                mLay_no_childaccount.setVisibility(View.VISIBLE);
                mNoticeGuardLL.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mchildaccount_swipe_refresh.isShown()){
                mchildaccount_swipe_refresh.setRefreshing(false);
            }
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getmessageMoreHandler = new LinhuiAsyncHttpResponseHandler(ChildAccountManagementAllModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            childAccountManagementAllModel = (ChildAccountManagementAllModel)data;
            if (childAccountManagementAllModel.getMessage() != null) {
                ArrayList<ChildAccountManagementModel> tmp = childAccountManagementAllModel.getMessage();
                if (data != null && tmp.size() !=0) {
                    for( ChildAccountManagementModel fieldDetail: tmp ){
                        childAccountlistdata.add(fieldDetail);
                    }
                    childAccountManagementAdapter.notifyDataSetChanged();
                    mchildaccount_listview.onLoadComplete();
                    if (tmp.size() < 10 ) {
                        mchildaccount_listview.set_loaded();
                    }
                } else {
                    mchildaccount_listview.set_loaded();
                    listpagesize = listpagesize-1;
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mchildaccount_listview.onLoadComplete();
            listpagesize = listpagesize-1;
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };

    @Override
    public void loadMore() {
        listpagesize = listpagesize +1;
        if(!childAccountlistdata.isEmpty()){
            Field_FieldApi.getmessageuserlistitems(MyAsyncHttpClient.MyAsyncHttpClient(),getmessageMoreHandler,String.valueOf(listpagesize),"10");
        }
    }

    @Override
    public void onRefresh() {
        initdata();
    }
    //删除通知item的回调函数
    public void deleteinvoicelistitem(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        final String id = str;
        deletelistposition = position;
        BaseMessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        Field_FieldApi.deletemessageuser(MyAsyncHttpClient.MyAsyncHttpClient(), deletemessageuserHandler, ChildAccountManagementActivity.this, id);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }
    private LinhuiAsyncHttpResponseHandler deletemessageuserHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (childAccountlistdata != null && childAccountlistdata.size() == 1) {
                mchildaccount_listview.onLoadComplete();
            }
            childAccountlistdata.remove(deletelistposition);
            childAccountManagementAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initdata();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
