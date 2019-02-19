package com.linhuiba.linhuifield.fieldactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.fieldadapter.CommunityExclusiveResourcesAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.View_StickyGridHeaders.StickyGridHeadersGridView;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.CommunityResourcesItemsModel;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/14.
 */
public class CommunityResourcesActivity extends FieldBaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener,Field_MyScrollview.OnScrollListener{
    private StickyGridHeadersGridView mcommunity_pullscrollview;
    private SwipeRefreshLayout mcommunity_swipe_refresh;
    private RelativeLayout mlay_no_resources;
    private FieldMyGridView mcommumity_esclusive_listview;
    private TextView mcommunityresources_loadmore_nulldata_text;
    private LinearLayout mcommumity_esclusive_field_layout;
    private TextView mcommumity_esclusive_activity_text;
    private TextView mcommumity_esclusive_field_text;
    private LinearLayout mcommumity_esclusive_activity_alllayout;
    private LinearLayout mcommumity_esclusive_fields_alllayout;
    private TextView mcommumity_esclusive_activity_type_text;
    private TextView mcommumity_esclusive_activity_num_text;
    private TextView mcommumity_esclusive_fields_type_text;
    private TextView mcommumity_esclusive_fields_num_text;
    private RelativeLayout mback_layout_top;
    private TextView mtitle;
    private RelativeLayout maction_layout_top;
    private Dialog mShareDialog;
    private IWXAPI api;
    private Bitmap ShareBitmap = null;
    private String share_description = "";
    private String share_title = "";
    private Field_MyScrollview myscrollview;
    private LinearLayout mcommunity_resources_type_layout_tmp;
    private LinearLayout mcommunity_resources_type_layout;
    private LinearLayout mcommumity_esclusive_loadmore_layout;
    private TextView mexclusive_resources_propertyname;
    private int mcommunity_resources_type_layout_height;
    private CommunityExclusiveResourcesAdapter[] communityExclusiveResourcesAdapters = new CommunityExclusiveResourcesAdapter[2];
    private ArrayList<CommunityResourcesItemsModel>[] mDataList = new ArrayList[2];
    private int[] page= new int[2];
    private int resource_type = 1;
    private int currIndex;// 当前页卡编号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_communityresources);
        initview();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
    }
    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.commumity_esclusive_field_layout) {
                resource_type = 1;
                currIndex = 0;
                mcommumity_esclusive_field_text.setTextColor(getResources().getColor(R.color.white));
                mcommumity_esclusive_activity_text.setTextColor(getResources().getColor(R.color.addfield_main_picture_bg));
                mcommumity_esclusive_fields_type_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_fields_num_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_activity_type_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                mcommumity_esclusive_activity_num_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                showProgressDialog();
                initdata();
            } else if (v.getId() == R.id.commumity_esclusive_activity_text) {
                if (LoginManager.isRight_to_publish() || LoginManager.isIs_supplier()) {
                    resource_type = 3;
                    currIndex = 1;
                    mcommumity_esclusive_field_text.setTextColor(getResources().getColor(R.color.addfield_main_picture_bg));
                    mcommumity_esclusive_activity_text.setTextColor(getResources().getColor(R.color.white));
                    mcommumity_esclusive_activity_type_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                    mcommumity_esclusive_activity_num_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                    mcommumity_esclusive_fields_type_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                    mcommumity_esclusive_fields_num_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                    showProgressDialog();
                    initdata();
                }
            } else if (v.getId() == R.id.commumity_esclusive_activity_alllayout) {
                mcommumity_esclusive_field_text.setTextColor(getResources().getColor(R.color.addfield_main_picture_bg));
                mcommumity_esclusive_activity_text.setTextColor(getResources().getColor(R.color.white));
                mcommumity_esclusive_activity_type_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_activity_num_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_fields_type_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                mcommumity_esclusive_fields_num_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                resource_type = 3;
                currIndex = 1;
                showProgressDialog();
                initdata();
            } else if (v.getId() == R.id.commumity_esclusive_fields_alllayout) {
                mcommumity_esclusive_field_text.setTextColor(getResources().getColor(R.color.white));
                mcommumity_esclusive_activity_text.setTextColor(getResources().getColor(R.color.addfield_main_picture_bg));
                mcommumity_esclusive_fields_type_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_fields_num_text.setTextColor(getResources().getColor(R.color.default_bluebg));
                mcommumity_esclusive_activity_type_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                mcommumity_esclusive_activity_num_text.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                resource_type = 1;
                currIndex = 0;
                showProgressDialog();
                initdata();
            } else if (v.getId() == R.id.back_layout_top) {
                finish();
            }  else if (v.getId() == R.id.action_layout_top) {

            } else if (v.getId() == R.id.lay_no_resources) {
                showProgressDialog();
                initdata();
            }
        }
    }
    private void initview() {
        mcommunity_pullscrollview = (StickyGridHeadersGridView)findViewById(R.id.community_pullscrollview);
        mcommunity_swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.community_swipe_refresh);
        mlay_no_resources = (RelativeLayout)findViewById(R.id.lay_no_resources);
        myscrollview = (Field_MyScrollview)findViewById(R.id.scrollview);
        mcommunity_resources_type_layout_tmp = (LinearLayout)findViewById(R.id.community_resources_type_layout_tmp);
        mcommunity_resources_type_layout = (LinearLayout)findViewById(R.id.community_resources_type_layout);
        mcommumity_esclusive_listview = (FieldMyGridView)findViewById(R.id.commumity_esclusive_listview);
        mcommunityresources_loadmore_nulldata_text = (TextView)findViewById(R.id.communityresources_loadmore_nulldata_text);
        mcommumity_esclusive_field_layout = (LinearLayout) findViewById(R.id.commumity_esclusive_field_layout);
        mcommumity_esclusive_activity_text = (TextView)findViewById(R.id.commumity_esclusive_activity_text);
        mcommumity_esclusive_field_text = (TextView)findViewById(R.id.commumity_esclusive_field_text);
        mcommumity_esclusive_fields_alllayout = (LinearLayout) findViewById(R.id.commumity_esclusive_fields_alllayout);
        mcommumity_esclusive_activity_alllayout = (LinearLayout) findViewById(R.id.commumity_esclusive_activity_alllayout);
        mcommumity_esclusive_activity_type_text = (TextView)findViewById(R.id.commumity_esclusive_activity_type_text);
        mcommumity_esclusive_activity_num_text = (TextView)findViewById(R.id.commumity_esclusive_activity_num_text);
        mcommumity_esclusive_fields_type_text = (TextView)findViewById(R.id.commumity_esclusive_fields_type_text);
        mcommumity_esclusive_fields_num_text = (TextView)findViewById(R.id.commumity_esclusive_fields_num_text);
        mcommumity_esclusive_loadmore_layout = (LinearLayout)findViewById(R.id.commumity_esclusive_loadmore_layout);
        mexclusive_resources_propertyname = (TextView)findViewById(R.id.exclusive_resources_propertyname);
        mback_layout_top = (RelativeLayout)findViewById(R.id.community_resources_back_layout);
        mtitle = (TextView)findViewById(R.id.community_resources_title_text);
        maction_layout_top = (RelativeLayout)findViewById(R.id.community_resources_share_layout);
        mcommumity_esclusive_field_layout.setOnClickListener(new OnClickListener());
        mcommumity_esclusive_activity_text.setOnClickListener(new OnClickListener());
        mcommumity_esclusive_activity_alllayout.setOnClickListener(new OnClickListener());
        mcommumity_esclusive_fields_alllayout.setOnClickListener(new OnClickListener());
        mlay_no_resources.setOnClickListener(new OnClickListener());
        findViewById(R.id.all_communityresource_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                onScroll(myscrollview.getScrollY());
                System.out.println(myscrollview.getScrollY());
            }
        });
        myscrollview.setOnScrollListener(this);
        myscrollview.setOnScrollToBottomLintener(new Field_MyScrollview.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && mlay_no_resources.getVisibility() == View.GONE && mDataList[currIndex] != null &&
                        mDataList[currIndex].size() > 0) {
                    if (mcommunityresources_loadmore_nulldata_text.getVisibility() != View.VISIBLE && mcommumity_esclusive_loadmore_layout.getVisibility() == View.GONE) {
                        page[currIndex] = page[currIndex] + 1;
                        mcommumity_esclusive_loadmore_layout.setVisibility(View.VISIBLE);
                        Field_FieldApi.getexclusive_resources(MyAsyncHttpClient.MyAsyncHttpClient2(),getusershopMoreHandler,resource_type,page[currIndex]);
                    }
                }
            }
        });
        mcommumity_esclusive_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resourceinfo = new Intent("com.business.aboutActivity");
                resourceinfo.putExtra("type",Config.RESOURCE_INFO_WEB_INT);
                resourceinfo.putExtra("resourceid", Integer.parseInt(mDataList[currIndex].get(position).getResource_id())) ;
                resourceinfo.putExtra("resources_type", resource_type);
                startActivity(resourceinfo);
            }
        });

        mback_layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtitle.setText(getResources().getString(R.string.myselfinfo_exclusive_txt));
        mcommunity_swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mcommunity_swipe_refresh.setOnRefreshListener(this);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
            resource_type = 3;
            mcommumity_esclusive_field_layout.setVisibility(View.GONE);
            mcommumity_esclusive_fields_alllayout.setVisibility(View.GONE);
        }
        showProgressDialog();
        initdata();
    }
    private void initdata() {
        page[currIndex] = 1;
        mcommunityresources_loadmore_nulldata_text.setVisibility(View.GONE);
        if (mDataList[currIndex] != null) {
            mDataList[currIndex].clear();
        }
        mlay_no_resources.setVisibility(View.GONE);
        if (mcommunity_resources_type_layout_height > 0) {
            myscrollview.fullScroll(ScrollView.FOCUS_UP);
        }
        Field_FieldApi.getexclusive_resources(MyAsyncHttpClient.MyAsyncHttpClient2(),getusershopHandler,resource_type,page[currIndex]);
    }
    @Override
    public void onRefresh() {
        myscrollview.fullScroll(ScrollView.FOCUS_UP);
        mcommunity_pullscrollview.setVisibility(View.GONE);
        initdata();
    }
    private LinhuiAsyncHttpResponseHandler getusershopHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if(mcommunity_swipe_refresh.isShown()){
                mcommunity_swipe_refresh.setRefreshing(false);
            }
            if (data != null && JSONObject.parseObject(data.toString()) != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("name") != null && jsonObject.get("name").toString().length() > 0) {
                    mexclusive_resources_propertyname.setVisibility(View.VISIBLE);
                    mexclusive_resources_propertyname.setText(jsonObject.get("name").toString());
                } else {
                    mexclusive_resources_propertyname.setVisibility(View.GONE);
                }
                int resources_num = 0;
                if (jsonObject.get("activity_num") != null && jsonObject.get("activity_num").toString().length() > 0) {
                    mcommumity_esclusive_activity_num_text.setText(jsonObject.get("activity_num").toString());
                    resources_num = resources_num + jsonObject.getInteger("activity_num");
                }
                if (jsonObject.get("field_num") != null && jsonObject.get("field_num").toString().length() > 0) {
                    mcommumity_esclusive_fields_num_text.setText(jsonObject.get("field_num").toString());
                    resources_num = resources_num + jsonObject.getInteger("field_num");
                }
                if (resources_num > 0) {
                    share_title = String.valueOf(resources_num);
                }
                if (resource_type == 1) {
                    if (jsonObject.get("fields") == null || jsonObject.get("fields").toString().length() == 0) {
                        mlay_no_resources.setVisibility(View.VISIBLE);
                        mcommumity_esclusive_listview.setVisibility(View.GONE);
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.GONE);
                        mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
                        return;
                    }
                } else if (resource_type == 3) {
                    if (jsonObject.get("activities") == null || jsonObject.get("activities").toString().length() == 0) {
                        mlay_no_resources.setVisibility(View.VISIBLE);
                        mcommumity_esclusive_listview.setVisibility(View.GONE);
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.GONE);
                        mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
                        return;
                    }
                }
                if (resource_type == 1) {
                    mDataList[currIndex] = (ArrayList<CommunityResourcesItemsModel>) JSONArray.parseArray(jsonObject.get("fields").toString(),CommunityResourcesItemsModel.class);
                } else if (resource_type == 3) {
                    mDataList[currIndex] = (ArrayList<CommunityResourcesItemsModel>) JSONArray.parseArray(jsonObject.get("activities").toString(),CommunityResourcesItemsModel.class);
                }
                if (mDataList[currIndex] == null || mDataList[currIndex].size() == 0) {
                    mlay_no_resources.setVisibility(View.VISIBLE);
                    mcommumity_esclusive_listview.setVisibility(View.GONE);
                    mcommunityresources_loadmore_nulldata_text.setVisibility(View.GONE);
                    mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
                    return;
                }
                mlay_no_resources.setVisibility(View.GONE);
                mcommumity_esclusive_listview.setVisibility(View.VISIBLE);
                communityExclusiveResourcesAdapters[currIndex] = new CommunityExclusiveResourcesAdapter(CommunityResourcesActivity.this,
                        CommunityResourcesActivity.this,mDataList[currIndex],currIndex);
                mcommumity_esclusive_listview.setAdapter(communityExclusiveResourcesAdapters[currIndex]);
                if (mDataList[currIndex].size() < 10) {
                    mcommunityresources_loadmore_nulldata_text.setVisibility(View.VISIBLE);
                }
            } else {
                mlay_no_resources.setVisibility(View.VISIBLE);
                mcommumity_esclusive_listview.setVisibility(View.GONE);
                mcommunityresources_loadmore_nulldata_text.setVisibility(View.GONE);
                mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
            }
            //分享
//            if (share_title != null &&  share_title.length() > 0) {
//                share_title = getResources().getString(R.string.community_resources_share_title_one) + share_title + getResources().getString(R.string.community_resources_share_title_two);
//                share_description = getResources().getString(R.string.community_resources_share_description);
//                maction_layout_top.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //分享
//                        if (ShareBitmap == null) {
//                            Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
//                            ShareBitmap = Constants.zoomImage(bmp,90,90);
//                            if(bmp != null) {
//                                bmp.recycle();
//                            }
//                        }
//                        if (share_description.length() > 0 && share_title.length() > 0) {
//                            showPopupWindow();
//                        }
//                    }
//                });
//
//            }


        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mcommunity_swipe_refresh.isShown()){
                mcommunity_swipe_refresh.setRefreshing(false);
            }
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getusershopMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
            if (resource_type == 1) {
                if (data != null && JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("fields") != null) {
                    ArrayList<CommunityResourcesItemsModel> tmp = (ArrayList<CommunityResourcesItemsModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("fields").toString(), CommunityResourcesItemsModel.class);
                    if ((tmp == null || tmp.isEmpty())) {
                        page[currIndex] = page[currIndex] - 1;
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.VISIBLE);
                        return;
                    }
                    for (CommunityResourcesItemsModel order : tmp) {
                        mDataList[currIndex].add(order);
                    }
                    communityExclusiveResourcesAdapters[currIndex].notifyDataSetChanged();
                    if (tmp.size() < 10) {
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.VISIBLE);
                    }
                }
            } else if (resource_type == 3) {
                if (data != null && JSONObject.parseObject(data.toString()) != null && JSONObject.parseObject(data.toString()).get("activities") != null) {
                    ArrayList<CommunityResourcesItemsModel> tmp = (ArrayList<CommunityResourcesItemsModel>) JSONArray.parseArray(JSONObject.parseObject(data.toString()).get("activities").toString(), CommunityResourcesItemsModel.class);
                    if ((tmp == null || tmp.isEmpty())) {
                        page[currIndex] = page[currIndex] - 1;
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.VISIBLE);
                        return;
                    }
                    for (CommunityResourcesItemsModel order : tmp) {
                        mDataList[currIndex].add(order);
                    }
                    communityExclusiveResourcesAdapters[currIndex].notifyDataSetChanged();
                    if (tmp.size() < 10) {
                        mcommunityresources_loadmore_nulldata_text.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            mcommumity_esclusive_loadmore_layout.setVisibility(View.GONE);
            page[currIndex] = page[currIndex] - 1;
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };

    @Override
    public void onScroll(int scrollY) {
        if (scrollY > 0 && scrollY < 100 && mcommunity_resources_type_layout_height < 50) {
            mcommunity_resources_type_layout_height = Math.max(scrollY, mcommunity_resources_type_layout_tmp.getTop());
        }
        int mBuyLayout2ParentTop = Math.max(scrollY, mcommunity_resources_type_layout_tmp.getTop());
        mcommunity_resources_type_layout.layout(0, mBuyLayout2ParentTop, mcommunity_resources_type_layout.getWidth(), mBuyLayout2ParentTop + mcommunity_resources_type_layout.getHeight());
        if (mcommunity_resources_type_layout_height > 0) {
            if (scrollY < mcommunity_resources_type_layout_height) {
                mcommunity_resources_type_layout.setVisibility(View.GONE);
                mcommunity_resources_type_layout_tmp.setVisibility(View.GONE);
            } else {
                mcommunity_resources_type_layout_tmp.setVisibility(View.VISIBLE);
                mcommunity_resources_type_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
