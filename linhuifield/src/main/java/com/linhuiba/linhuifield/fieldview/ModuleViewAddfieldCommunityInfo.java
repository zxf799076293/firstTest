package com.linhuiba.linhuifield.fieldview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;

public class ModuleViewAddfieldCommunityInfo extends LinearLayout {
    public TextView mTextView;
    public LinearLayout mLinearLayout;
    public EditText mEditText;
    public RecyclerView mRecyclerView;
    public RelativeLayout mRelativeLayout;
    public TextView mOtherTextView;
    public LinearLayout mOtherLinearLayout;
    public EditText mOtherEditText;
    public TextView mBackTextView;
    public LinearLayout mBackLL;
    //type: 3:单文本类型,4多文本类型,,5:单向选择,6:多项选择，7日期选择,8多项选择 list竖 (1:数字型(可以为小数)2:占比型) 9:发布场地展位不能编辑详情
    //10:场地和展位详情
    public ModuleViewAddfieldCommunityInfo(Context context, int type) {
        super(context);
        //加载需要的属性，加载方法一的子Layout
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.module_addfield_community_info_dynamic_view, this);
        //在此你可以封装很多方法
        int tv_id = getResources().getIdentifier("tv" + String.valueOf(type), "id", context.getPackageName());
        int ll_id = getResources().getIdentifier("ll" + String.valueOf(type), "id", context.getPackageName());
        int et_id = getResources().getIdentifier("et" + String.valueOf(type), "id", context.getPackageName());
        int rv_id = getResources().getIdentifier("rv" + String.valueOf(type), "id", context.getPackageName());
        int rl_id = getResources().getIdentifier("rl" + String.valueOf(type), "id", context.getPackageName());
        int othertv_id = getResources().getIdentifier("other_tv" + String.valueOf(type), "id", context.getPackageName());
        int otherll_id = getResources().getIdentifier("other_ll" + String.valueOf(type), "id", context.getPackageName());
        int otheret_id = getResources().getIdentifier("other_et" + String.valueOf(type), "id", context.getPackageName());
        int backtv_id = getResources().getIdentifier("back_tv" + String.valueOf(type), "id", context.getPackageName());
        int backll_id = getResources().getIdentifier("back_ll" + String.valueOf(type), "id", context.getPackageName());
        mTextView = (TextView) view.findViewById(tv_id);
        mLinearLayout = (LinearLayout) view.findViewById(ll_id);
        mEditText = (EditText) view.findViewById(et_id);
        mLinearLayout.setVisibility(VISIBLE);
        if (type == 4 || type == 5 || type == 7 || type == 9 ||
                type == 10) {
            mBackTextView = (TextView) view.findViewById(backtv_id);
        }
        if (type == 3) {
            Constants.textchangelistener(mEditText);
        } else if (type == 4) {
            mOtherLinearLayout = (LinearLayout) view.findViewById(otherll_id);
        } else if (type == 5) {
            mBackLL = (LinearLayout) view.findViewById(backll_id);
            mOtherTextView = (TextView) view.findViewById(othertv_id);
            mOtherLinearLayout = (LinearLayout) view.findViewById(otherll_id);
            mOtherEditText = (EditText) view.findViewById(otheret_id);
        } else if (type == 6) {
            mRelativeLayout = (RelativeLayout) view.findViewById(rl_id);
            mOtherLinearLayout = (LinearLayout) view.findViewById(otherll_id);
            mOtherEditText = (EditText) view.findViewById(otheret_id);
        } else if (type == 7) {
            mOtherLinearLayout = (LinearLayout) view.findViewById(otherll_id);
        } else if (type == 8) {
            mRecyclerView = (RecyclerView) view.findViewById(rv_id);
        }
    }
}
