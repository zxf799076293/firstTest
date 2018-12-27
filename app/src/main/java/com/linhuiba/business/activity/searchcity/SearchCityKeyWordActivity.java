package com.linhuiba.business.activity.searchcity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.github.promeg.pinyinhelper.Pinyin;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.SearchFieldAreaAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/10.
 */

public class SearchCityKeyWordActivity extends BaseMvpActivity{
    @InjectView(R.id.searchcity_keyword_search_et)
    EditText mSearchET;
    @InjectView(R.id.searchcity_keyword_lv)
    ListView mkeyWordCityLV;
    @InjectView(R.id.searchcity_keyword_close_tv)
    TextView mDeleteTV;
    @InjectView(R.id.searchcity_no_list_tv)
    TextView mNoListTV;
    private List<MeiTuanBean> mBodyDatasDefault = new ArrayList<>();
    private List<MeiTuanBean> mBodyDatas = new ArrayList<>();
    private static final int RESULT_INT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_searchcity_keyword);
        ButterKnife.inject(this);
        initView();
    }
    private void initView() {
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("citylist") != null) {
            mBodyDatasDefault = (List<MeiTuanBean>) intent.getExtras().get("citylist");
        }
        mSearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    String str1 = "";
                    for (int j = 0; j < str.length; j++) {
                        str1 += str[i];
                    }
                    mSearchET.setText(str1);
                    mSearchET.setSelection(i);
                }
                if (charSequence.toString().trim().length() > 0) {
                    if (mBodyDatas != null) {
                        mBodyDatas.clear();
                    }
                    mBodyDatas = search(mSearchET.getText().toString().trim(),mBodyDatasDefault,mBodyDatas);
                    if (mBodyDatas.size() > 0) {
                        ArrayList<String> cityList = new ArrayList<>();
                        for (int j = 0; j < mBodyDatas.size(); j++) {
                            cityList.add(mBodyDatas.get(j).getName());
                        }
                        mkeyWordCityLV.setVisibility(View.VISIBLE);
                        mNoListTV.setVisibility(View.GONE);
                        mkeyWordCityLV.setAdapter(new SearchFieldAreaAdapter(SearchCityKeyWordActivity.this, cityList, 0,-1));
                    } else {
                        mkeyWordCityLV.setVisibility(View.GONE);
                        mNoListTV.setVisibility(View.VISIBLE);
                    }
                } else {
                    mkeyWordCityLV.setVisibility(View.GONE);
                    mNoListTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mDeleteTV.setVisibility(View.VISIBLE);
                } else {
                    mDeleteTV.setVisibility(View.GONE);
                }
            }
        });
        mkeyWordCityLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBodyDatas.get(position) != null &&
                        mBodyDatas.get(position).getId() != null &&
                        mBodyDatas.get(position).getName() != null &&
                        mBodyDatas.get(position).getId().length() > 0 &&
                        mBodyDatas.get(position).getName().length() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("id",mBodyDatas.get(position).getId());
                    intent.putExtra("name",mBodyDatas.get(position).getName());
                    setResult(RESULT_INT,intent);
                    finish();
                }

            }
        });
    }
    @OnClick({
            R.id.searchcity_keyword_cancel_tv,
            R.id.searchcity_keyword_close_rl
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.searchcity_keyword_cancel_tv:
                    finish();
                 break;
            case R.id.searchcity_keyword_close_rl:
                mSearchET.setText("");
                break;
            default:
                break;
        }
    }
    //搜索结果
    public static List<MeiTuanBean> search(String str,
                                           List<MeiTuanBean> allContacts, List<MeiTuanBean> contactList) {
        if (contactList != null) {
            contactList.clear();
        }
        for (MeiTuanBean contact : allContacts) {
            // 先将输入的字符串转换为拼音
            if (contains(contact, str)) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public static boolean contains(MeiTuanBean contact, String search) {
        if (TextUtils.isEmpty(contact.getTarget())) {
            return false;
        }
        boolean flag = false;
        boolean isChiness = false;
        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        for (int i = 0; i < search.length(); i++) {
            if (Pinyin.isChinese(search.charAt(i))) {
                isChiness = true;
                break;
            }
        }
        if (isChiness) {
            if (contact.getName().contains(search)) {
                flag = true;
            }
        } else {
            if (search.length() < 5) {
                // 不区分大小写
                Pattern firstLetterMatcher = Pattern.compile(search,
                        Pattern.CASE_INSENSITIVE);
                flag = firstLetterMatcher.matcher(contact.getBaseIndexAllTag()).find();
            }

            if (!flag) { // 如果简拼已经找到了，就不使用全拼了
                // 不区分大小写
                Pattern pattern2 = Pattern
                        .compile(search, Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = pattern2.matcher(contact.getBaseIndexPinyin());
                flag = matcher2.find();
            }
        }
        return flag;
    }
}
