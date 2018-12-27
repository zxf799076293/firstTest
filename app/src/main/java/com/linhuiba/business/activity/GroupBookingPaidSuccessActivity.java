package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoOtherResourcesAdapter;
import com.linhuiba.business.adapter.FieldinfoRecommendResourcesAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/29.
 */

public class GroupBookingPaidSuccessActivity extends BaseMvpActivity {
    @InjectView(R.id.groupbooking_success_gridview)
    MyGridview mGroupBookingSuccessGV;
    @InjectView(R.id.other_group_list_ll)
    LinearLayout mOtherGroupListLL;
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataList = new ArrayList<>();
    private FieldinfoOtherResourcesAdapter fieldinfoRecommendResources_adapter;
    private String mGroupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupbooking_paid_success);
        ButterKnife.inject(this);
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.shownextButton(this,getResources().getString(R.string.confirmorder_back_homepage),
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintabintent = new Intent(GroupBookingPaidSuccessActivity.this, MainTabActivity.class);
                maintabintent.putExtra("new_tmpintent", "goto_homepage");
                startActivity(maintabintent);
            }
        });
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.group_paid_success_activity_name_str));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.group_paid_success_activity_name_str));
        MobclickAgent.onPause(this);
    }
    @OnClick ({
            R.id.group_paid_success_orderlist_btn,
            R.id.group_paid_success_invite_btn
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.group_paid_success_orderlist_btn:
                Intent orderpayintent = new Intent(GroupBookingPaidSuccessActivity.this, GroupBookingMainActivity.class);
                Bundle bundle_ordertype1 = new Bundle();
                bundle_ordertype1.putInt("order_type", 2);
                orderpayintent.putExtras(bundle_ordertype1);
                startActivity(orderpayintent);
                break;
            case R.id.group_paid_success_invite_btn:
                Intent inviteintent = new Intent(GroupBookingPaidSuccessActivity.this,InviteActivity.class);
                startActivity(inviteintent);
                finish();
                break;
            default:
                break;
        }
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("group_id") != null) {
            mGroupId = intent.getExtras().get("group_id").toString();
        }
        if (LoginManager.isLogin()) {
            //其他火爆拼团
            if (mGroupId != null && mGroupId.length() > 0) {
                FieldApi.getGroupBookingList(MyAsyncHttpClient.MyAsyncHttpClient(), mOtherResHandler, 1, LoginManager.getInstance().getTrackcityid(),6,Integer.parseInt(mGroupId));
            }
        }
    }
    private LinhuiAsyncHttpResponseHandler mOtherResHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    ArrayList<GroupBookingListModel> mHomeRecentActivityList = (ArrayList<GroupBookingListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingListModel.class);
                    if( mHomeRecentActivityList == null ||  mHomeRecentActivityList.isEmpty()) {
                        return;
                    }
                    mOtherGroupListLL.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mHomeRecentActivityList.size(); i++) {
                        ResourceSearchItemModel resourceSearchItemModel = new ResourceSearchItemModel();
                        resourceSearchItemModel.setName(mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getName());
                        resourceSearchItemModel.setActivity_start_date(mHomeRecentActivityList.get(i).getActivity_start());
                        resourceSearchItemModel.setDeadline(mHomeRecentActivityList.get(i).getActivity_end());
                        if (mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url") != null) {
                            resourceSearchItemModel.setPic_url(mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url").toString());
                        }
                        if (mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price() != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price") != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price").toString().length() > 0) {
                            resourceSearchItemModel.setPrice(mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price").toString());
                        }
                        resourceSearchItemModel.setNumber_of_group_purchase_now(mHomeRecentActivityList.get(i).getNumber_of_group_purchase_now());
                        resourceSearchItemModel.setResource_id(mHomeRecentActivityList.get(i).getId());
                        mFieldInfoOtherDataList.add(resourceSearchItemModel);
                    }
                    fieldinfoRecommendResources_adapter = new FieldinfoOtherResourcesAdapter(GroupBookingPaidSuccessActivity.this,GroupBookingPaidSuccessActivity.this,mFieldInfoOtherDataList,1);
                    mGroupBookingSuccessGV.setAdapter(fieldinfoRecommendResources_adapter);
                    mGroupBookingSuccessGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent fieldinfo = new Intent(GroupBookingPaidSuccessActivity.this, GroupBookingInfoActivity.class);
                            fieldinfo.putExtra("ResId", String.valueOf(mFieldInfoOtherDataList.get(position).getResource_id()));
                            startActivity(fieldinfo);
                            finish();

                        }
                    });
                }
            }
        }
        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

}
