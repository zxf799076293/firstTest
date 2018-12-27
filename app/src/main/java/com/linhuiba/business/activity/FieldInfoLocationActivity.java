package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldInfoLocationAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.model.ResourceInfoCommunityInfoResourcesModel;
import com.linhuiba.linhuifield.util.TitleBarUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/5/16.
 */

public class FieldInfoLocationActivity extends BaseMvpActivity {
    @InjectView(R.id.fieldinfo_location_listview)
    ListView mfieldinfo_location_listview;
    private FieldInfoLocationAdapter fieldInfoLocationAdapter;
    private ArrayList<ResourceInfoCommunityInfoResourcesModel> location_list = new ArrayList<>();
    private int resource_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldinfolocation);
        ButterKnife.inject(this);
        initView();
        initData();
    }
    private void initView() {
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.fieldinfo_location_text));
        TitleBarUtils.showBackImg(this,true);
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("resources") != null) {
            location_list = (ArrayList<ResourceInfoCommunityInfoResourcesModel>) intent.getExtras().get("resources");
        }
        if (intent.getExtras() != null && intent.getExtras().get("resource_id") != null) {
            resource_id = intent.getExtras().getInt("resource_id");
        }
        if (location_list != null && location_list.size() > 0) {
            initData();
        } else {
            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
        }
    }
    private void initData() {
        for (int i = 0; i < location_list.size(); i++) {
            if (resource_id == location_list.get(i).getId()) {
                FieldInfoLocationAdapter.getIsLocationSelected().put(location_list.get(i).getId(),true);
            } else {
                FieldInfoLocationAdapter.getIsLocationSelected().put(location_list.get(i).getId(),false);
            }
        }
        fieldInfoLocationAdapter = new FieldInfoLocationAdapter(FieldInfoLocationActivity.this,FieldInfoLocationActivity.this,location_list);
        mfieldinfo_location_listview.setAdapter(fieldInfoLocationAdapter);
    }
}
