package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.SearchFieldAreaAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.SearchLatelyHistoryModel;
import com.linhuiba.business.mvppresenter.SearchKeyWordsMvpPresenter;
import com.linhuiba.business.mvpview.SearchKeyWordsMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SearchFieldAreaActivity extends BaseMvpActivity implements SearchKeyWordsMvpView {
    @InjectView(R.id.edit_search_field)
    EditText medit_search_field;
    @InjectView(R.id.search_cancel_txt)
    TextView msearch_cancel_txt;
    @InjectView(R.id.search_hotsearch_layout)
    RelativeLayout msearch_hotsearch_layout;
    @InjectView(R.id.search_lately_search_listview)
    MyListView msearch_lately_search_listview;
    @InjectView(R.id.search_lately_search_layout)
    LinearLayout msearch_lately_search_layout;
    @InjectView(R.id.search_hotsearch_alllayout)
    LinearLayout msearch_hotsearch_alllayout;
    @InjectView(R.id.searchfieldarea_delete_history_text)
    ImageButton msearchfieldarea_delete_history_text;
    @InjectView(R.id.search_resource_listview)
    MyListView mSearchWordsLV;

    private ArrayList<HashMap<String,String>> arealist = new ArrayList<>();
    private int searchtype = 0;//跳转来的标志
    private final int Intent_SearchlistInt = 3;//列表跳转来的
    private final int Intent_HomeInt = 2;//首页跳转来的
    private int cityname_code;//城市id
    private int mResourceType;//资源类型
    private ArrayList<String> mHotWordsList = new ArrayList<>();//热词 list
    private ArrayList<String> mLatelyWordsList = new ArrayList<>();//最近搜索 list
    private List<SearchLatelyHistoryModel> mLatelyHistoryModelList = new ArrayList<>();//本地最近搜索存储处理后的 list
    private int mShowLatelyHitoryListInt;//最近搜索对应的当前城市和资源类型的list的列表顺序
    private SearchKeyWordsMvpPresenter mSearchPresenter;
    public ArrayList<Call> mCallsList = new ArrayList<>();
    private ArrayList<String> mSearchWordsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfieldarea);
        ButterKnife.inject(this);
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            mSearchPresenter.detachView();
        }
    }

    private void initview() {
        mSearchPresenter = new SearchKeyWordsMvpPresenter();
        mSearchPresenter.attachView(this);
        medit_search_field.addTextChangedListener(textWatcher);
        medit_search_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                     /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    //搜索处理：gridview显示场地信息
                    //medit_search_field.setText("监听到了完成键");
                    search_fieldlist();
                }
                return false;
            }
        });
        Intent searchgetintent = getIntent();
        searchtype = searchgetintent.getExtras().getInt("search");
        if (searchtype == Intent_HomeInt) {
            mResourceType = 1;
        } else if (searchtype == Intent_SearchlistInt) {
            mResourceType = searchgetintent.getExtras().getInt("res_type_id");
        }
        if (searchgetintent.getExtras().get("cityname_code") != null &&
                searchgetintent.getExtras().get("cityname_code").toString().length() > 0) {
            cityname_code = Integer.parseInt(searchgetintent.getExtras().getString("cityname_code"));
        } else {
            cityname_code = Integer.parseInt(LoginManager.getTrackcityid());
        }
        if (arealist != null) {
            arealist.clear();
        }
        if (searchgetintent.getExtras().get("ksywords") != null) {
            medit_search_field.setText(searchgetintent.getExtras().getString("ksywords"));
        }
        addlatelysearchview();
        mSearchWordsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //搜索处理：gridview显示场地信息
                //medit_search_field.setText("监听到了完成键");
                Intent intent = new Intent();
                if (searchtype == Intent_HomeInt) {
                    intent.putExtra("back", mSearchWordsList.get(position));
                    setResult(2, intent);
                    finish();
                } else if (searchtype == Intent_SearchlistInt) {
                    intent.putExtra("back", mSearchWordsList.get(position));
                    setResult(3, intent);
                    finish();
                }
                search_fieldlist();
            }
        });
    }
    private void getHotwordlist() {
        msearch_hotsearch_alllayout.setVisibility(View.GONE);
        showProgressDialog();
        UserApi.getHot_word(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),HotwordHandler,String.valueOf(cityname_code),String.valueOf(mResourceType));
    }
    @OnClick({
            R.id.search_cancel_txt_layout,
            R.id.home_search_cancel,
            R.id.searchfieldarea_delete_history_text,
            R.id.searchedit_area_back_textview
    })
    public void searchareaclick (View view) {
        switch (view.getId()) {
            case  R.id.search_cancel_txt_layout:
                medit_search_field.setText("");
                msearch_cancel_txt.setVisibility(View.GONE);
                break;
            case R.id.home_search_cancel:
                search_fieldlist();
                break;
            case R.id.searchfieldarea_delete_history_text:
                List<String> hotword_list = new ArrayList<>();
                mLatelyHistoryModelList.get(mShowLatelyHitoryListInt).setHotword_list(hotword_list);
                LoginManager.getInstance().setSearch_lately_history(JSONArray.toJSONString(mLatelyHistoryModelList,true));
                msearch_lately_search_layout.setVisibility(View.GONE);
                break;
            case R.id.searchedit_area_back_textview:
                finish();
                break;
            default:
                break;
        }
    }
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                mSearchWordsLV.setVisibility(View.VISIBLE);
                msearch_cancel_txt.setVisibility(View.VISIBLE);
                msearch_hotsearch_alllayout.setVisibility(View.GONE);
                msearch_lately_search_layout.setVisibility(View.GONE);
                if (mSearchWordsList != null) {
                    mSearchWordsList.clear();
                }
                mSearchWordsLV.setAdapter(new SearchFieldAreaAdapter(SearchFieldAreaActivity.this, mSearchWordsList, 0,-1));
                cancelCalls();
                if (isOnCancel()) {
                    mCallsList.clear();
                    mSearchPresenter.getFastSearchList(SearchFieldAreaActivity.this,
                            s.toString().trim(),String.valueOf(cityname_code),String.valueOf(mResourceType));
                }
            } else {
                mSearchWordsLV.setVisibility(View.GONE);
                msearch_cancel_txt.setVisibility(View.GONE);
                if (mHotWordsList != null && mHotWordsList.size() > 0) {
                    msearch_hotsearch_alllayout.setVisibility(View.VISIBLE);
                }
                if (mLatelyWordsList != null && mLatelyWordsList.size() > 0) {
                    msearch_lately_search_layout.setVisibility(View.VISIBLE);
                }
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
    private void addhotsearchview() {
        if (mHotWordsList != null && mHotWordsList.size() > 0) {
            msearch_hotsearch_layout.removeAllViews();
            DisplayMetrics metric = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width_new = metric.widthPixels;     // 屏幕宽度（像素）
            float width = 0;
            float widthtmp = 0;
            int hightnum = 0;
            for (int i = 0; i < mHotWordsList.size(); i++) {
                final TextView textView = new TextView(this);
                textView.setText("  " + mHotWordsList.get(i).toString() + "  ");
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(8, 0, 8, 0);
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_search_field_area_tv_bg));
                textView.setTextColor(getResources().getColor(R.color.headline_tv_color));
                textView.setId(i);
                textView.setTag(i);
                width = Constants.getTextWidth(this, mHotWordsList.get(i).toString(), 16) + 16;
                RelativeLayout.LayoutParams params;
                if (width < 120) {
                    width = 120;
                    params = new RelativeLayout.LayoutParams(120, 64);
                } else {
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 64);

                }
                if (width > (width_new - widthtmp - 28)) {
                    hightnum = hightnum + 1;
                    widthtmp = 0;
                    params.setMargins((int) widthtmp + 28, hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    msearch_hotsearch_layout.addView(textView);
                } else {
                    params.setMargins((int) widthtmp + 28, hightnum * 84, 0, 0);
                    textView.setLayoutParams(params);
                    msearch_hotsearch_layout.addView(textView);
                }
                widthtmp = width + widthtmp + 20;
                textView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        medit_search_field.setText(textView.getText().toString().trim());
                        InputMethodManager imm = (InputMethodManager) v
                                .getContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        //搜索处理：gridview显示场地信息
                        //medit_search_field.setText("监听到了完成键");
                        Intent intent = new Intent();
                        if (searchtype == Intent_HomeInt) {
                            intent.putExtra("back", medit_search_field.getText().toString().trim());
                            setResult(2, intent);
                            finish();
                        } else if (searchtype == Intent_SearchlistInt) {
                            intent.putExtra("back", medit_search_field.getText().toString());
                            setResult(3, intent);
                            finish();
                        }
                    }
                });
            }
            msearch_hotsearch_alllayout.setVisibility(View.VISIBLE);
        } else {
            msearch_hotsearch_alllayout.setVisibility(View.GONE);
        }
    }
    private void addlatelysearchview() {
        msearch_lately_search_layout.setVisibility(View.GONE);
        if (mResourceType > 0 && cityname_code > 0) {
            getHotwordlist();
            if (LoginManager.getInstance().getSearch_lately_history() != null &&
                    LoginManager.getInstance().getSearch_lately_history().length() > 0) {
                if (mLatelyHistoryModelList != null) {
                    mLatelyHistoryModelList.clear();
                }
                if (mLatelyWordsList != null) {
                    mLatelyWordsList.clear();
                }
                mLatelyHistoryModelList = JSON.parseArray(LoginManager.getInstance().getSearch_lately_history(), SearchLatelyHistoryModel.class);
                if (mLatelyHistoryModelList != null && mLatelyHistoryModelList.size() > 0) {
                    for (int i = 0; i < mLatelyHistoryModelList.size(); i++) {
                        if (mLatelyHistoryModelList.get(i).getCity_id() == cityname_code &&
                                mLatelyHistoryModelList.get(i).getResources_type() == mResourceType) {
                            mShowLatelyHitoryListInt = i;
                            mLatelyWordsList.addAll(mLatelyHistoryModelList.get(i).getHotword_list());
                        }
                    }
                    if (mLatelyWordsList != null && mLatelyWordsList.size() > 0) {
                        msearch_lately_search_listview.setAdapter(new SearchFieldAreaAdapter(SearchFieldAreaActivity.this, mLatelyWordsList, 0,-1));
                        msearch_lately_search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                medit_search_field.setText(mLatelyWordsList.get(position).toString());
                                //搜索处理：gridview显示场地信息
                                //medit_search_field.setText("监听到了完成键");
                                Intent intent = new Intent();
                                if (searchtype == Intent_HomeInt) {
                                    intent.putExtra("back", medit_search_field.getText().toString());
                                    setResult(2, intent);
                                    finish();
                                } else if (searchtype == Intent_SearchlistInt) {
                                    intent.putExtra("back", medit_search_field.getText().toString());
                                    setResult(3, intent);
                                    finish();
                                }
                                search_fieldlist();
                            }
                        });
                        msearch_lately_search_layout.setVisibility(View.VISIBLE);
                    } else {
                        msearch_lately_search_layout.setVisibility(View.GONE);
                    }
                } else {
                    msearch_lately_search_layout.setVisibility(View.GONE);
                }
            } else {
                msearch_lately_search_layout.setVisibility(View.GONE);
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
            this.finish();
        }
    }
    private LinhuiAsyncHttpResponseHandler HotwordHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null ) {
                JSONArray jsonarray = JSONArray.parseArray(data.toString());
                if (jsonarray != null && jsonarray.size() > 0) {
                    if (mHotWordsList != null) {
                        mHotWordsList.clear();
                    }
                    for (int i = 0; i < jsonarray.size(); i++) {
                        mHotWordsList.add(jsonarray.get(i).toString());
                    }
                    addhotsearchview();
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    private void search_fieldlist() {
        Intent intent = new Intent();
        if (searchtype == Intent_HomeInt) {
            intent.putExtra("back", medit_search_field.getText().toString());
            setResult(2, intent);
            finish();
        } else if (searchtype == Intent_SearchlistInt) {
            intent.putExtra("back", medit_search_field.getText().toString());
            setResult(3, intent);
            finish();
        }
        if (medit_search_field.getText().toString().trim().length() > 0) {
            if (mLatelyWordsList != null && mLatelyWordsList.size() > 0) {
                for (int i = 0; i < mLatelyWordsList.size(); i++) {
                    if (medit_search_field.getText().toString().equals(mLatelyWordsList.get(i).toString())) {
                        return;
                    }
                }
                if (mLatelyWordsList.size() > 9) {
                    mLatelyWordsList.remove(mLatelyWordsList.size() - 1);
                }
                mLatelyWordsList.add(0,medit_search_field.getText().toString());

                if (mLatelyHistoryModelList.get(mShowLatelyHitoryListInt).getHotword_list() != null) {
                    mLatelyHistoryModelList.get(mShowLatelyHitoryListInt).getHotword_list().clear();
                }
                mLatelyHistoryModelList.get(mShowLatelyHitoryListInt).getHotword_list().addAll(mLatelyWordsList);
            } else {
                SearchLatelyHistoryModel searchLatelyHistoryModel = new SearchLatelyHistoryModel();
                searchLatelyHistoryModel.setCity_id(cityname_code);
                searchLatelyHistoryModel.setResources_type(mResourceType);
                mLatelyWordsList.add(medit_search_field.getText().toString());
                searchLatelyHistoryModel.setHotword_list(mLatelyWordsList);
                mLatelyHistoryModelList.add(searchLatelyHistoryModel);
            }
            LoginManager.setSearch_lately_history(JSONArray.toJSONString(mLatelyHistoryModelList,true));
        }
    }

    @Override
    public void onSearchWordsSuccess(ArrayList<String> list) {
        if (mSearchWordsList != null) {
            mSearchWordsList.clear();
        }
        mSearchWordsList = list;
        mSearchWordsLV.setAdapter(new SearchFieldAreaAdapter(SearchFieldAreaActivity.this, mSearchWordsList, 0,-1));
    }

    @Override
    public void onSearchWordsFailure(boolean superresult, Throwable error) {

    }
    private void cancelCalls() {
        for (int i = 0; i < mCallsList.size(); i++) {
            if (!mCallsList.get(i).isCanceled()) {
                mCallsList.get(i).cancel();
            }
        }
    }
    private boolean isOnCancel() {
        boolean uncancel = true;
        for (int i = 0; i < mCallsList.size(); i++) {
            if (!mCallsList.get(i).isCanceled()) {
                uncancel = false;
                break;
            }
        }
        return uncancel;
    }
}
