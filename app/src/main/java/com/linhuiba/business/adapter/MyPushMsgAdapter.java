package com.linhuiba.business.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.MySelfPushMessageActivity;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.MyPushMsgModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.SwipeItemView;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPushMsgAdapter extends BaseQuickAdapter<MyPushMsgModel, BaseViewHolder> {
    private Context mContext;
    private MySelfPushMessageActivity mMySelfPushMessageActivity;
    private DisplayMetrics mDisplayMetrics;
    private static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public MyPushMsgAdapter(int layoutResId, @Nullable List<MyPushMsgModel> data, Context context,
                            MySelfPushMessageActivity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.mMySelfPushMessageActivity = activity;
        mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyPushMsgModel item) {
        TextView pushMsgRemindTV = (TextView) helper.getView(R.id.push_msg_item_remind_tv);
        TextView pushMsgTitleTV = (TextView) helper.getView(R.id.push_msg_item_title_tv);
        TextView pushMsgContentTV = (TextView) helper.getView(R.id.push_msg_item_content_tv);
        TextView pushMsgTimeTV = (TextView) helper.getView(R.id.push_msg_item_time_tv);
        TextView pushMsgShowInfoTV = (TextView) helper.getView(R.id.push_msg_item_show_info_tv);
        CheckBox pushMsgCB = (CheckBox) helper.getView(R.id.push_msg_item_cb);
        TextView pushMsgReadSignTV = (TextView) helper.getView(R.id.push_msg_item_read_sign_tv);
        TextView pushMsgDeleteTV = (TextView) helper.getView(R.id.push_msg_item_delete_tv);
        SwipeItemView swipeItemView = (SwipeItemView) helper.getView(R.id.push_msg_item_swipe_view);
        pushMsgReadSignTV.setText(mContext.getResources().getString(R.string.module_my_push_msg_read_sign));
        pushMsgDeleteTV.setText(mContext.getResources().getString(R.string.invoiceinfo_delete_address_txt));

        if (item.getContent() != null && item.getContent().length() > 0) {
            int contentTvWidth = mDisplayMetrics.widthPixels - Constants.Dp2Px(mContext,60);
            float strWidth = Constants.getTextWidth(mContext, item.getContent(), 12);
            float tvCount = 0;
            if (strWidth > contentTvWidth) {
                tvCount = strWidth / contentTvWidth;
            }
            float remainder = strWidth - contentTvWidth * (int)tvCount;
            int count;
            if (remainder > 0) {
                count = (int)tvCount +1;
            } else {
                count = (int)tvCount;
            }
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Constants.Dp2Px(mContext,77 + 17*count));
            swipeItemView.setLayoutParams(paramgroups);
        } else {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Constants.Dp2Px(mContext,94));
            swipeItemView.setLayoutParams(paramgroups);
        }

        if (item.getIs_read() == 1) {
            pushMsgRemindTV.setVisibility(View.GONE);
            pushMsgReadSignTV.setVisibility(View.GONE);
            pushMsgTitleTV.setTextColor(mContext.getResources().getColor(R.color.register_edit_color));
        } else {
            pushMsgRemindTV.setVisibility(View.VISIBLE);
            pushMsgReadSignTV.setVisibility(View.VISIBLE);
            pushMsgTitleTV.setTextColor(mContext.getResources().getColor(R.color.headline_tv_color));
        }
        if (item.getTitle() != null && item.getTitle().length() > 0) {
            pushMsgTitleTV.setText(item.getTitle());
        } else {
            pushMsgTitleTV.setText("");
        }
        if (item.getContent() != null && item.getContent().length() > 0) {
            pushMsgContentTV.setText(item.getContent());
        } else {
            pushMsgContentTV.setText("");
        }
        if (item.getCreated_at() != null && item.getCreated_at().length() > 0) {
            pushMsgTimeTV.setText(item.getCreated_at());
        } else {
            pushMsgTimeTV.setText("");
        }
        if (isSelected.get(item.getId())) {
            pushMsgCB.setChecked(true);
        } else {
            pushMsgCB.setChecked(false);
        }
        if (item.getUrl() != null && item.getUrl().length() > 0) {
            if (com.linhuiba.business.connector.Constants.getpush_msg_type(item.getUrl()) == com.linhuiba.business.connector.Constants.RELEASE_PERMISSIONS) {
                pushMsgShowInfoTV.setVisibility(View.GONE);
            } else {
                pushMsgShowInfoTV.setVisibility(View.VISIBLE);
            }
        } else {
            pushMsgShowInfoTV.setVisibility(View.GONE);
        }
        pushMsgCB.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isSelected.get(item.getId())) {
                    isSelected.put(item.getId(),false);
                } else {
                    isSelected.put(item.getId(),true);
                }
                notifyDataSetChanged();
                mMySelfPushMessageActivity.setBottomTVText();
            }
        });
        pushMsgShowInfoTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //2018/11/5 msg info 点击
                mMySelfPushMessageActivity.onInfoClick(item.getUrl(),item.getId(),helper.getLayoutPosition());
            }
        });
        pushMsgReadSignTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(item.getId());
                mMySelfPushMessageActivity.showProgressDialog();
                mMySelfPushMessageActivity.mMyPushMsgMvpPresenter.setMsgsRead(LoginManager.getUid(),ids);
            }
        });
        pushMsgDeleteTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(item.getId());
                mMySelfPushMessageActivity.mMyPushMsgMvpPresenter.deleteUserMsgs(ids);
            }
        });
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyPushMsgAdapter.isSelected = isSelected;
    }
    public static void clearIsSelected() {
        if (isSelected != null) {
            isSelected.clear();
        }
    }
    public static Boolean isNoChoose(List<MyPushMsgModel> data) {
        boolean isNoChoose = true;
        for (int i = 0; i < data.size(); i++) {
            if (isSelected.get(data.get(i).getId())) {
                isNoChoose = false;
                break;
            }
        }
        return isNoChoose;
    }
}
