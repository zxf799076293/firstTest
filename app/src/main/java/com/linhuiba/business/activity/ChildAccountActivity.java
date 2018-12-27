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
import com.linhuiba.business.adapter.ChildAccountAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.ChildAccountModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ChildAccountActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore ,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall{
    private LoadMoreListView childaccountlistview;
    private SwipeRefreshLayout childaccount_swipe_refresh;
    private RelativeLayout mchildaccount_lay_no_review;
    private ArrayList<ChildAccountModel> childAccountlist = new ArrayList<>();
    private ChildAccountAdapter childAccountAdapter;
    private int pagesize;
    private int delete_position;
    private String delete_id;
    public int changeadmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childaccount);
        initview();
        showProgressDialog();
        initdata();
    }
    private void initview() {
        childaccountlistview = (LoadMoreListView)findViewById(R.id.childaccount_listview);
        childaccount_swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.child_swipe_refresh);
        mchildaccount_lay_no_review = (RelativeLayout)findViewById(R.id.childaccount_lay_no_review);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.childaccount_title_text));
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.childaccount_invitation_newaccount), 14,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent childaccountinvitation = new Intent(ChildAccountActivity.this,ChildAccountInvitationActivity.class);
                        startActivity(childaccountinvitation);
                    }
                });
        Intent changgeadmin = getIntent();
        if (changgeadmin.getExtras() != null && changgeadmin.getExtras().get("changeadmin") != null) {
            changeadmin = changgeadmin.getExtras().getInt("changeadmin");
        }
        childaccount_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        childaccountlistview.setLoadMoreListen(this);
        childaccount_swipe_refresh.setOnRefreshListener(this);
        OnClick();
    }
    private void initdata() {
        childaccountlistview.set_refresh();
        if (childAccountlist != null) {
            childAccountlist.clear();
        }
        pagesize = 1;
        UserApi.getchild_accounts(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),child_accountsHandler,String.valueOf(pagesize));
    }
    private void OnClick() {
        mchildaccount_lay_no_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                initdata();
            }
        });
    }
    @Override
    public void onRefresh() {
        initdata();
    }

    @Override
    public void loadMore() {
        pagesize = pagesize + 1;
        UserApi.getchild_accounts(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),child_accountsMoreHandler,String.valueOf(pagesize));
    }
    private LinhuiAsyncHttpResponseHandler child_accountsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if(childaccount_swipe_refresh.isShown()){
                childaccount_swipe_refresh.setRefreshing(false);
            }
            if (data != null && data.toString().length() > 0 && data instanceof JSONObject &&
                    JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("data") != null) {
                childAccountlist =  (ArrayList<ChildAccountModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString(),ChildAccountModel.class);
                if( childAccountlist == null ||  childAccountlist.isEmpty()) {
                    mchildaccount_lay_no_review.setVisibility(View.VISIBLE);
                } else {
                    if (childAccountlist.size() < 10) {
                        childaccountlistview.set_loaded();
                    }
                    mchildaccount_lay_no_review.setVisibility(View.GONE);
                    childAccountAdapter = new ChildAccountAdapter(ChildAccountActivity.this,ChildAccountActivity.this,childAccountlist);
                    childaccountlistview.setAdapter(childAccountAdapter);
                }
            } else {
                mchildaccount_lay_no_review.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(childaccount_swipe_refresh.isShown()){
                childaccount_swipe_refresh.setRefreshing(false);
            }
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler child_accountsMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null && data.toString().length() > 0 && data instanceof JSONObject &&
                    JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("data") != null) {
                ArrayList<ChildAccountModel> tmp = (ArrayList<ChildAccountModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("data").toString(),ChildAccountModel.class);
                if( (tmp == null || tmp.isEmpty())){
                    childaccountlistview.set_loaded();
                    pagesize = pagesize-1;
                    childaccountlistview.onLoadComplete();
                    return;
                }
                for( ChildAccountModel order: tmp ){
                    childAccountlist.add(order);
                }
                childAccountAdapter.notifyDataSetChanged();
                childaccountlistview.onLoadComplete();
                if (tmp.size() < 10 ) {
                    childaccountlistview.set_loaded();                }
            } else {
                childaccountlistview.set_loaded();
                pagesize = pagesize-1;
                childaccountlistview.onLoadComplete();
                return;
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            childaccountlistview.onLoadComplete();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        delete_position = position;
        delete_id = str;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        UserApi.deletechild_accounts(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), deletechildaccount, delete_id);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }
    public void delete_childaccount(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    private LinhuiAsyncHttpResponseHandler deletechildaccount = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            childAccountlist.remove(delete_position);
            childAccountAdapter.notifyDataSetChanged();
            MessageUtils.showToast(getResources().getString(R.string.childaccount_delete_success_text));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
}
