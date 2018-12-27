package com.linhuiba.business.activity;

import android.os.Bundle;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;

/**
 * Created by Administrator on 2017/10/11.
 */

public class GroupBookingConfirmOrderActivity extends BaseMvpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder_success_popupwindow);
    }
}
