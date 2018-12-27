package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.AreaListViewAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/19.
 */
public class MapSearchResourceTypeActivity extends BaseMvpActivity {
    private ArrayList<HashMap<String, String>> mfieldtype_map;
    private ArrayList<HashMap<String, String>> madvertisingtype_map;
    @InjectView(R.id.search_resource_typelist)
    ListView msearch_resource_typelist;
    private String keywords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsearchresourcetype);
        initView();
    }
    private void initView() {
        ButterKnife.inject(this);
        gettupelist();
        TitleBarUtils.showBackImg(this,true);
        final Intent mapsearchintent = getIntent();
        if (mapsearchintent != null) {
            if (mapsearchintent.getExtras().size() != 0) {
                keywords = mapsearchintent.getStringExtra("keywords");
                if (mapsearchintent.getStringExtra("resourcetype").equals("1")) {
                    TitleBarUtils.setTitleText(this,getResources().getString(R.string.map_fieldtype_text));
                    msearch_resource_typelist.setAdapter(new AreaListViewAdapter(MapSearchResourceTypeActivity.this, mfieldtype_map, 0,-1));
                } else if (mapsearchintent.getStringExtra("resourcetype").equals("2")) {
                    TitleBarUtils.setTitleText(this,getResources().getString(R.string.map_advertisingtype_text));
                    msearch_resource_typelist.setAdapter(new AreaListViewAdapter(MapSearchResourceTypeActivity.this, madvertisingtype_map, 0,-1));
                }
            }
        }
        msearch_resource_typelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ImageView nowView = (ImageView) view.findViewById(R.id.area_choose_img);
                nowView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        Intent intent = new Intent();
                        if (mapsearchintent.getStringExtra("resourcetype").equals("1")) {
                            intent.putExtra("typecode", mfieldtype_map.get(position).get("id"));
                            intent.putExtra("keywords",keywords);
                        } else  if (mapsearchintent.getStringExtra("resourcetype").equals("2")) {
                            intent.putExtra("typecode",madvertisingtype_map.get(position).get("id"));
                            intent.putExtra("keywords",keywords);
                        }
                        MapSearchResourceTypeActivity.this.setResult(1, intent);
                        MapSearchResourceTypeActivity.this.finish();
                    }
                }, 200);

            }
        });
    }
    private void gettupelist() {
        String typelist = LoginManager.getfieldtype();
        String advertisingtypelist = LoginManager.getAd_type();
        com.alibaba.fastjson.JSONArray array = JSON.parseArray(typelist);
        com.alibaba.fastjson.JSONArray advettisignarray = JSON.parseArray(advertisingtypelist);
        mfieldtype_map = new ArrayList<>();
        madvertisingtype_map =  new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonobject = array.getJSONObject(i);
            String key = jsonobject.getString("display_name");
            String value = jsonobject.getString("field_type_id");
            HashMap<String,String> map = new HashMap<>();
            map.put("id",value);
            map.put("name",key);
            mfieldtype_map.add(map);
        }
        for (int i = 0; i < advettisignarray.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonobject = advettisignarray.getJSONObject(i);
            String key = jsonobject.getString("type");
            String value = jsonobject.getString("code");
            HashMap<String,String> map = new HashMap<>();
            map.put("id",value);
            map.put("name",key);
            madvertisingtype_map.add(map);
        }
    }
}
