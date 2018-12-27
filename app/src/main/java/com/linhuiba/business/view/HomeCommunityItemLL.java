package com.linhuiba.business.view;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.business.R;

public class HomeCommunityItemLL extends LinearLayout {
    public TextView mTextView;
    public TabLayout mInvoiceTitleTL;
    public ViewPager mInvoiceTitleVP;
    public TextView mShowAllTextView;
    public HomeCommunityItemLL(Context context) {
        super(context);
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.module_ll_item_home_dynamic_community, this);
        mTextView = (TextView) view.findViewById(R.id.home_community_item_title);
        mInvoiceTitleTL = (TabLayout) view.findViewById(R.id.home_tablayout);
        mInvoiceTitleVP = (ViewPager) view.findViewById(R.id.home_vp);
        mShowAllTextView = (TextView) view.findViewById(R.id.home_community_show_all_tv);
    }
}
