package com.linhuiba.business.connector;

import android.app.Activity;

import com.linhuiba.business.adapter.GroupBookingListAdapter;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.adapter.OrderExpandableListAdapter;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.model.MyCouponsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class OnCouponTimerTask extends TimerTask {
    private List<MyCouponsModel> datas;
    private MyCouponsAdapter mMyCouponsAdapter;
    private Activity mActivity;
    private int mOtherTypeActivityInt;//1：订单列表 2 拼团
    private List<Map<String, Object>> mData;
    private ArrayList<GroupBookingListModel> mGroupBookingListModels;
    private OrderExpandableListAdapter mOrderExpandableListAdapter;
    private GroupBookingListAdapter mGroupBookingListAdapter;
    private int startNum;//开始倒计时的position 加载时
    public OnCouponTimerTask(List<MyCouponsModel> datas,
                             MyCouponsAdapter myCouponsAdapter,
                             Activity activity,int startNum){
        this.datas = datas;
        this.mMyCouponsAdapter = myCouponsAdapter;
        this.mActivity = activity;
        this.startNum = startNum;
    }
    public OnCouponTimerTask(List<Map<String, Object>> datas,
                             OrderExpandableListAdapter adapter,
                             Activity activity,int type,int startNum) {
        this.mData = datas;
        this.mOrderExpandableListAdapter = adapter;
        this.mActivity = activity;
        this.mOtherTypeActivityInt = type;
        this.startNum = startNum;
    }
    public OnCouponTimerTask(ArrayList<GroupBookingListModel> datas,
                             GroupBookingListAdapter adapter,
                             Activity activity,int type,int startNum) {
        this.mGroupBookingListModels = datas;
        this.mGroupBookingListAdapter = adapter;
        this.mActivity = activity;
        this.mOtherTypeActivityInt = type;
        this.startNum = startNum;
    }

    @Override
    public void run() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mOtherTypeActivityInt == 1) {
                    for (int i = startNum; i < mData.size(); i++) {
                        if (mData.get(i).get("remind_time") != null &&
                                mData.get(i).get("remind_time").toString().length() > 0) {
                            long useTime = Long.parseLong(mData.get(i).get("remind_time").toString());
                            if (useTime > 1000) {
                                useTime -= 1000;
                                mData.get(i).put("remind_time",useTime);
                                if (useTime <= 1000 ) {
                                    mOrderExpandableListAdapter.notifyDataSetChanged();
                                } else {
                                    mOrderExpandableListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                } else if (mOtherTypeActivityInt == 2) {
                    for (int i = startNum; i < mGroupBookingListModels.size(); i++) {
                        if (mGroupBookingListModels.get(i).getTime_left() != null) {
                            long useTime = Long.parseLong(String.valueOf(mGroupBookingListModels.get(i).getTime_left()* 1000));
                            if (useTime > 1000) {
                                useTime -= 1000;
                                mGroupBookingListModels.get(i).setTime_left(new Double((useTime*0.001)).intValue());
                                if (useTime <= 1000 ) {
                                    mGroupBookingListAdapter.notifyDataSetChanged();
                                } else {
                                    mGroupBookingListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                } else {
                    for (int i = startNum; i < datas.size(); i++) {
                        long useTime =  Long.parseLong(String.valueOf(datas.get(i).getRemind_time()* 1000));
                        if (useTime > 1000) {
                            useTime -= 1000;
                            datas.get(i).setRemind_time( new Double((useTime*0.001)).longValue());
                            if (useTime <= 1000 ) {
                                mMyCouponsAdapter.notifyItemChanged(i);
                            } else {
                                mMyCouponsAdapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
            }
        });
    }
}
