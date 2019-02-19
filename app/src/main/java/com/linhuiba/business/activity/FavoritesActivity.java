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
import com.linhuiba.business.adapter.FavoritesAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_LoadMoreListView;
import com.linhuiba.business.model.FavoritesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FavoritesActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall{
    @InjectView(R.id.favorites_listview)
    LoadMoreListView mfavorites_listview;
    @InjectView(R.id.favorites_swipe_refresh)
    SwipeRefreshLayout mswipeRefreshLayout;
    @InjectView(R.id.favorites_lay_no_review)
    RelativeLayout mlay_no_review;
    private FavoritesAdapter favoritesAdapter;
    private ArrayList<FavoritesModel> favoriteslistdata;
    private int reviewlistpagesize;
    private int deleteitem_position;
    private String deleteitem_id= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.favorires_titletxt));
        TitleBarUtils.showBackImg(this, true);
        initview();
        initdata(1);
    }
    private void initview() {
        mswipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mfavorites_listview.setLoadMoreListen(this);
        mswipeRefreshLayout.setOnRefreshListener(this);
        mlay_no_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdata(1);
            }
        });
    }
    private void initdata(int type) {
        if (type == 1) {
            showProgressDialog();
        }
        reviewlistpagesize = 1;
        mfavorites_listview.set_refresh();
        FieldApi.getfavoriteslistitem(MyAsyncHttpClient.MyAsyncHttpClient2(),GetfavoriteslistitemHandler,String.valueOf(reviewlistpagesize), "10");
    }

    @Override
    public void loadMore() {
        if (favoriteslistdata != null) {
            if (favoriteslistdata.size() != 0) {
                reviewlistpagesize = reviewlistpagesize + 1;
                FieldApi.getfavoriteslistitem(MyAsyncHttpClient.MyAsyncHttpClient2(), GetfavoriteslistitemMoreHandler, String.valueOf(reviewlistpagesize), "10");
            } else {
                mfavorites_listview.onLoadComplete();
            }
        } else {
            mfavorites_listview.onLoadComplete();
        }
    }

    @Override
    public void onRefresh() {
        initdata(0);
    }
    private LinhuiAsyncHttpResponseHandler GetfavoriteslistitemHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("data") != null) {
                    ArrayList<FavoritesModel> list = (ArrayList<FavoritesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                            FavoritesModel.class);
                    favoriteslistdata = list;
                    if (favoriteslistdata == null || favoriteslistdata.isEmpty()) {
                        mlay_no_review.setVisibility(View.VISIBLE);
                        if(mswipeRefreshLayout.isShown()){
                            mswipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }
                    mlay_no_review.setVisibility(View.GONE);
                    favoritesAdapter = new FavoritesAdapter(FavoritesActivity.this,FavoritesActivity.this,favoriteslistdata);
                    mfavorites_listview.setAdapter(favoritesAdapter);
                    if (favoriteslistdata.size() <10) {
                        mfavorites_listview.set_loaded();
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
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler GetfavoriteslistitemMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("data") != null) {
                    ArrayList<FavoritesModel> list = (ArrayList<FavoritesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),
                            FavoritesModel.class);
                    ArrayList<FavoritesModel> tmp = list;
                    if( (tmp == null || tmp.isEmpty())){
                        reviewlistpagesize = reviewlistpagesize-1;
                        mfavorites_listview.set_loaded();
                        return;
                    }
                    for( FavoritesModel order: tmp ){
                        favoriteslistdata.add(order);
                    }
                    favoritesAdapter.notifyDataSetChanged();
                    mfavorites_listview.onLoadComplete();
                    if (tmp.size() < 10 ) {
                        mfavorites_listview.set_loaded();
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mfavorites_listview.onLoadComplete();
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                initdata(1);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        deleteitem_position = position;
        deleteitem_id = str;
        MessageUtils.showDialog(this, getResources().getString(R.string.dialog_prompt),
                getResources().getString(R.string.delete_prompt),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressDialog();
                        FieldApi.deletefavoriteslistitem(FavoritesActivity.this, MyAsyncHttpClient.MyAsyncHttpClient(), FavoriteslistitemHandler, deleteitem_id);                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }
    public void delete_favorite(int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position,str);
        test.getfieldsize_pricenuit(this);
    }
    private LinhuiAsyncHttpResponseHandler FavoriteslistitemHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.myselfinfo_deletefocuson_txt));
            favoriteslistdata.remove(deleteitem_position);
            favoritesAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess_new(error);
        }
    };

}
