package com.linhuiba.business.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseFragment;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.GroupBookingInfoActivity;
import com.linhuiba.business.activity.GroupBookingMainActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.adapter.GroupBookingListAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.OnCouponTimerTask;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/19.
 */

public class GroupBookingListFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(R.id.groupbooking_list_lv)
    MyListView mGroupBookingListLV;
    @InjectView(R.id.groupbooking_list_slv)
    Field_MyScrollview mScrollView;
    @InjectView(R.id.grouplist_loadmore_layout)
    LinearLayout mGroupListLoadMoreLL;
    @InjectView(R.id.grouplist_loadmore_nulldata_text)
    TextView mGroupListNullTV;
    @InjectView(R.id.groupbooking_list_swipe)
    SwipeRefreshLayout mGroupBookingListSwipe;
    private View mMainContentView;
    private ArrayList<GroupBookingListModel> mGroupList;
    private GroupBookingListAdapter mAdapter;
    private int mPagePosition;
    private String mCityIdStr;
    private String ShareTitleStr = "";//分享的标题
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private String share_linkurl = "";//分享的url
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private Dialog mShareDialog;
    private IWXAPI api;
    private View mNoDataRL;
    private ImageView mNoDataImg;
    private TextView mNoDataTV;
    private Button mNoDataBtn;
    private String mTitleBarCityName = "";
    private Timer mTimers;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContentView == null) {
            mMainContentView = inflater.inflate(R.layout.fragment_groupbooking_list, container , false);
            ButterKnife.inject(this, mMainContentView);
            initView();
            showProgressDialog();
            initData();
        }
        return mMainContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.groupbooking_list_fragment_name_str));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.groupbooking_list_fragment_name_str));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
        if (miniShareBitmap != null) {
            miniShareBitmap.recycle();
        }
        if (mTimers != null) {
            mTimers.cancel();
        }
    }

    private void initView() {
        mNoDataRL = (View) mMainContentView.findViewById(R.id.no_data_view);
        mNoDataImg = (ImageView) mNoDataRL.findViewById(R.id.no_data_img);
        mNoDataTV = (TextView) mNoDataRL.findViewById(R.id.no_data_tv);
        mNoDataBtn = (Button) mNoDataRL.findViewById(R.id.no_data_btn);
        mNoDataImg.setImageResource(R.drawable.emptystates_grouppurchase);
        mNoDataTV.setText(getResources().getString(R.string.groupbooking_list_no_data_tv_str));
        mNoDataBtn.setText(getResources().getString(R.string.confirmorder_back_homepage));
        mNoDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintabintent = new Intent(GroupBookingListFragment.this.getActivity(), MainTabActivity.class);
                maintabintent.putExtra("new_tmpintent", "goto_homepage");
                startActivity(maintabintent);
            }
        });
        mNoDataRL.setVisibility(View.GONE);
        TitleBarUtils.showBackImg(mMainContentView,GroupBookingListFragment.this.getActivity(),true);
        mGroupBookingListSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mGroupBookingListSwipe.setOnRefreshListener(this);
        GroupBookingMainActivity activity = (GroupBookingMainActivity)GroupBookingListFragment.this.getActivity();
        mCityIdStr = activity.mCityIdStr;
        new Thread(new Runnable() {
            @Override
            public void run() {
                api = WXAPIFactory.createWXAPI(GroupBookingListFragment.this.getContext(), Constants.APP_ID);
                api.registerApp(Constants.APP_ID);
                mTitleBarCityName = Constants.getCityName(mCityIdStr,GroupBookingListFragment.this.getContext());
                mGetTitleCityNameHandler.sendEmptyMessage(1);
            }
        }).start();

        mGroupBookingListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent fieldinfo = new Intent(GroupBookingListFragment.this.getActivity(), GroupBookingInfoActivity.class);
                fieldinfo.putExtra("ResId", String.valueOf(mGroupList.get(position).getId()));
                startActivity(fieldinfo);
            }
        });
        mScrollView.setOnScrollToBottomLintener(new Field_MyScrollview.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom && LoginManager.isLogin()) {
                    if (mGroupListNullTV.getVisibility() != View.VISIBLE && mGroupListLoadMoreLL.getVisibility() == View.GONE) {
                        mGroupListLoadMoreLL.setVisibility(View.VISIBLE);
                        mPagePosition ++;
                        FieldApi.getGroupBookingList(MyAsyncHttpClient.MyAsyncHttpClient(), getPublicActivityListMoreHandler, mPagePosition,mCityIdStr,0,0);
                        browseHistories();
                    }
                }
            }
        });
    }
    private void initData() {
        mPagePosition = 1;
        if (mCityIdStr != null && mCityIdStr.length() > 0) {
            FieldApi.getGroupBookingList(MyAsyncHttpClient.MyAsyncHttpClient(), getPublicActivityListHandler, mPagePosition,mCityIdStr,0,0);
            browseHistories();
        } else {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
            GroupBookingListFragment.this.getActivity().finish();
        }
    }
    @OnClick({
            R.id.groupbooking_see_flow_ll
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.groupbooking_see_flow_ll:
                //2017/9/19 加载拼团流程web
                Intent groupIntent = new Intent(GroupBookingListFragment.this.getActivity(), AboutUsActivity.class);
                groupIntent.putExtra("type", Config.GROUP_EDSC_INT);
                startActivity(groupIntent);
                break;
            default:
                break;
        }
    }
            
    private LinhuiAsyncHttpResponseHandler getPublicActivityListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    if(mGroupBookingListSwipe.isShown()){
                        mGroupBookingListSwipe.setRefreshing(false);
                    }
                    mGroupListNullTV.setVisibility(View.GONE);
                    mGroupList = (ArrayList<GroupBookingListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingListModel.class);
                    if( mGroupList == null ||  mGroupList.isEmpty()) {
                        mNoDataRL.setVisibility(View.VISIBLE);
                        return;
                    }
                    mNoDataRL.setVisibility(View.GONE);
                    if (mGroupList.size() < 10) {
                        mGroupListNullTV.setVisibility(View.VISIBLE);
                    }
                    mAdapter = new GroupBookingListAdapter(GroupBookingListFragment.this.getContext(),GroupBookingListFragment.this.getActivity(),mGroupList);
                    mGroupBookingListLV.setAdapter(mAdapter);
                    if (isAdded()) {
                        TitleBarUtils.showActionImg(mMainContentView,true,getResources().getDrawable(R.drawable.tab_ic_share), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareGroupLists();
                            }
                        });
                    }
                    //2018/12/12 倒计时
                    if (mTimers == null) {
                        mTimers = new Timer();
                    }
                    mTimers.schedule(new OnCouponTimerTask(mGroupList,mAdapter,
                            GroupBookingListFragment.this.getActivity(),2,0),0,1000);
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mGroupBookingListSwipe.isShown()){
                mGroupBookingListSwipe.setRefreshing(false);
            }
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
        }
    };
    private LinhuiAsyncHttpResponseHandler getPublicActivityListMoreHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    mGroupListLoadMoreLL.setVisibility(View.GONE);
                    ArrayList<GroupBookingListModel> tmp = (ArrayList<GroupBookingListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingListModel.class);
                    if( tmp == null ||  tmp.isEmpty()) {
                        mPagePosition = mPagePosition - 1;
                        mGroupListNullTV.setVisibility(View.VISIBLE);
                        return;
                    }
                    int startNum = mGroupList.size();
                    for( GroupBookingListModel listitem: tmp ){
                        mGroupList.add(listitem);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (tmp.size() < 10) {
                        mGroupListNullTV.setVisibility(View.VISIBLE);
                    }
                    mTimers.schedule(new OnCouponTimerTask(mGroupList,mAdapter,
                            GroupBookingListFragment.this.getActivity(),2,startNum),0,1000);
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mGroupListLoadMoreLL.setVisibility(View.GONE);
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
        }
    };
    private void shareGroupLists() {
        if (ShareBitmap == null || miniShareBitmap == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_grouplist));//压缩Bitmap
                    miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            View myView = GroupBookingListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            mShareDialog = new AlertDialog.Builder(GroupBookingListFragment.this.getActivity()).create();
            sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_GROUP_LIST_URL+ "?city_id=" + mCityIdStr;
            share_linkurl = Config.SHARE_GROUP_LIST_URL + "?city_id=" + mCityIdStr;
            Constants constants = new Constants(GroupBookingListFragment.this.getActivity(),
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(GroupBookingListFragment.this.getActivity(),myView,mShareDialog,api,share_linkurl,
                    ShareTitleStr,
                    SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,ShareTitleStr);
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    final View myView = GroupBookingListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mShareDialog = new AlertDialog.Builder(GroupBookingListFragment.this.getActivity()).create();

                    share_linkurl =  Config.SHARE_GROUP_LIST_URL + "?city_id=" + mCityIdStr;
                    sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_GROUP_LIST_URL+ "?city_id=" + mCityIdStr;
                    if (mShareDialog!= null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                    }
                    Constants constants = new Constants(GroupBookingListFragment.this.getActivity(),
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(GroupBookingListFragment.this.getActivity(),myView,mShareDialog,api,share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,ShareTitleStr);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onRefresh() {
        if (mTimers != null) {
            mTimers.cancel();
            mTimers = null;
        }
        initData();
    }
    private Handler mGetTitleCityNameHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ShareTitleStr = getResources().getString(R.string.groupbooking_info_share_title_first_str) +
                            mTitleBarCityName +
                            getResources().getString(R.string.groupbooking_info_share_title_str) +
                            getResources().getString(R.string.groupbooding_list_share_title_str);
                    SharedescriptionStr = getResources().getString(R.string.groupbooding_list_share_desc_str);
                    TitleBarUtils.setTitleText(mMainContentView,mTitleBarCityName +
                            getResources().getString(R.string.groupbooding_list_title_str));
                    break;
                default:
                    break;
            }
        }
    };
    private HashMap<String,String> getBrowseHistoriesUrl(int page,String city_id,int pageSize,int group_purchase_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",city_id);
        if (group_purchase_id > 0) {
            paramsMap.put("group_purchase_id",String.valueOf(group_purchase_id));
        }
        return paramsMap;
    }
    private void browseHistories() {
        //浏览记录
        if (LoginManager.isLogin()) {
            try {
                String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl(mPagePosition,mCityIdStr,0,0));
                LoginMvpModel.sendBrowseHistories("group_list",parameter,mCityIdStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
