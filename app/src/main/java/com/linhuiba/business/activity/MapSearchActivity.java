package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.MapSearchAdapter;
import com.linhuiba.business.adapter.ResourceMapSearchTypeAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MapSearchActivity extends BaseMvpActivity {
    @InjectView(R.id.mapsearch_list)
    ListView mmapsearch_list;
    @InjectView(R.id.map_edit_search)
    EditText mmap_edit_search;
    @InjectView(R.id.search_cancel_txt)
    TextView msearch_cancel_txt;
    @InjectView(R.id.mapsearch_history_layout)
    LinearLayout mmapsearch_history_layout;
    @InjectView(R.id.mapsearch_address_list)
    ListView mmapsearch_address_list;
    @InjectView(R.id.map_search_delete_img)
    ImageView img_map_search_delete;
    private String cityname;
    private PoiSearch poiSearch;//poi检索
    private ArrayList<Field_AddResourceCreateItemModel> HistoryList = new ArrayList<>();
    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            hideProgressDialog();
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                MessageUtils.showToast(MapSearchActivity.this, "未找到结果");
                return;
            }
            if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                final ArrayList<HashMap<String, Object>> map_address_list = new ArrayList<>();
                for (int i = 0; i < poiResult.getAllPoi().size(); i++) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", poiResult.getAllPoi().get(i).name);
                    map.put("city", poiResult.getAllPoi().get(i).city);
                    if (poiResult.getAllPoi().get(i).location != null) {
                        map.put("location", poiResult.getAllPoi().get(i).location);
                    } else {
                        continue;
                    }
                    if (poiResult.getAllPoi().get(i).location.latitude > 0) {
                        map.put("latitude", poiResult.getAllPoi().get(i).location.latitude);
                    } else {
                        continue;
                    }
                    if (poiResult.getAllPoi().get(i).location.longitude > 0) {
                        map.put("longitude", poiResult.getAllPoi().get(i).location.longitude);
                    } else {
                        continue;
                    }
                    map.put("address", poiResult.getAllPoi().get(i).address);
                    map_address_list.add(map);
                }
                mmapsearch_address_list.setAdapter(new MapSearchAdapter(MapSearchActivity.this.getContext(), map_address_list, 1, -1));
                mmapsearch_address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("map_marker_info", (Serializable) map_address_list.get(position));
                        setResult(1, intent);
                        MapSearchActivity.this.finish();
                        setMapSearchListHistory(map_address_list.get(position).get("name").toString(), map_address_list.get(position));
                    }
                });
                mmapsearch_address_list.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            hideProgressDialog();
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            hideProgressDialog();
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mmap_edit_search.getText().toString())) {
                msearch_cancel_txt.setVisibility(View.VISIBLE);
                if (cityname != null && cityname.length() > 0) {
                    poiSearch.searchInCity((new PoiCitySearchOption())
                            .city(cityname)
                            .keyword(mmap_edit_search.getText().toString().trim())
                            .pageCapacity(50));
                }
            } else {
                msearch_cancel_txt.setVisibility(View.GONE);
                mmapsearch_address_list.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsearch);
        ButterKnife.inject(this);
        mmap_edit_search.addTextChangedListener(textWatcher);
        Intent getsearchtypeintent = getIntent();
        cityname = getsearchtypeintent.getExtras().getString("cityname");
        initView();
    }

    private void setMapSearchListHistory(String str, HashMap<String, Object> Search_map_address_back_map) {
        if (HistoryList.size() > 0) {
            for (int i = 0; i < HistoryList.size(); i++) {
                if (str.equals(HistoryList.get(i).getName())) {
                    return;
                }
            }
            if (HistoryList.size() > 9) {
                HistoryList.remove(HistoryList.size() - 1);
            }
            Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
            field_addResourceCreateItemModel.setName(str);
            field_addResourceCreateItemModel.setSearch_map_address_back_map(Search_map_address_back_map);
            HistoryList.add(0, field_addResourceCreateItemModel);
        } else {
            Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
            field_addResourceCreateItemModel.setName(str);
            field_addResourceCreateItemModel.setSearch_map_address_back_map(Search_map_address_back_map);
            HistoryList.add(0, field_addResourceCreateItemModel);
        }
        LoginManager.getInstance().setMapSearchhistory(JSON.toJSONString(HistoryList, true));
    }

    private void initView() {
        poiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        poiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
        getMapSearchListHistory();
        mmap_edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showProgressDialog();
                    poiSearch.searchInCity((new PoiCitySearchOption())
                            .city(cityname)
                            .keyword(mmap_edit_search.getText().toString().trim())
                            .pageCapacity(50));
                }
                return false;
            }
        });
        //自动弹出软键盘
        mmap_edit_search.setFocusable(true);
        mmap_edit_search.setFocusableInTouchMode(true);
        mmap_edit_search.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) MapSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mmap_edit_search, 0);
            }
        }, 500);

    }

    @OnClick({
            R.id.map_search_map_address_textview,
            R.id.search_cancel_txt_layout,
            R.id.map_search_back_textview,
            R.id.map_search_delete_img
    })
    public void mapsearchintent(View view) {
        switch (view.getId()) {
            case R.id.map_search_map_address_textview:
                finish();
                break;
            case R.id.search_cancel_txt_layout:
                mmap_edit_search.setText("");
                msearch_cancel_txt.setVisibility(View.GONE);
                break;
            case R.id.map_search_back_textview:
                this.finish();
                break;
            case R.id.map_search_delete_img:
                mmapsearch_history_layout.setVisibility(View.GONE);
                LoginManager.getInstance().setMapSearchhistory("");
                break;
            default:
                break;
        }
    }

    private void getMapSearchListHistory() {
        mmapsearch_history_layout.setVisibility(View.GONE);
        if (HistoryList != null) {
            HistoryList.clear();
        }
        if (LoginManager.getInstance().getMapSearchhistory() != null &&
                LoginManager.getInstance().getMapSearchhistory().length() > 0) {
            HistoryList = (ArrayList<Field_AddResourceCreateItemModel>) JSON.parseArray(LoginManager.getInstance().getMapSearchhistory(), Field_AddResourceCreateItemModel.class);
        }
        if (HistoryList.size() > 0) {
            mmapsearch_list.setAdapter(new ResourceMapSearchTypeAdapter(MapSearchActivity.this.getContext(), HistoryList, 1, -1));
            mmapsearch_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //返回参数关闭activity
                    Intent intent = new Intent();
                    intent.putExtra("map_marker_info", (HashMap<String, Object>) HistoryList.get(position).getSearch_map_address_back_map());
                    setResult(1, intent);
                    MapSearchActivity.this.finish();
                }
            });
            mmapsearch_history_layout.setVisibility(View.VISIBLE);
        }
    }

}
