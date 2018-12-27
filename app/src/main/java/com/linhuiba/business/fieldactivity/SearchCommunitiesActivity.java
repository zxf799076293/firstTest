package com.linhuiba.business.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.AreaListViewAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.CharacterParser;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.ClearEditText;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.PinyinComparator;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.SideBar;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.SortAdapter;
import com.linhuiba.business.fieldactivity.SearchCommunityClass.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/26.
 */
public class SearchCommunitiesActivity extends BaseMvpActivity {
    @InjectView(R.id.filter_edit)
    ClearEditText mClearEditText;
    private ArrayList<HashMap<String, String>> communitylist = new ArrayList<>();
    private AreaListViewAdapter searchcommunities_adapter;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcommunities);
        ButterKnife.inject(this);
        Intent addfieldintent = getIntent();
        if (addfieldintent.getExtras()!= null) {
            if (addfieldintent.getExtras().get("communitylist") != null) {
                communitylist = (ArrayList<HashMap<String, String>>) addfieldintent.getExtras().get("communitylist");
            }
            if (addfieldintent.getExtras().get("communityname") != null) {
                if (addfieldintent.getExtras().get("communityname").toString().length() > 0) {
                    mClearEditText.setText(addfieldintent.getExtras().get("communityname").toString());
                }
            }

        }
        initViews();
    }
    @OnClick({
            R.id.map_search_backimg,

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_search_backimg:
               this.finish();
                break;
            default:
                break;
        }
    }
    private void initViews() {
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                mClearEditText.setText("");
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent addfield = new Intent();
                addfield.putExtra("communitiesid", ((SortModel) adapter.getItem(position)).getCommunityid());
                addfield.putExtra("communitiesname", ((SortModel) adapter.getItem(position)).getName());
                setResult(5, addfield);
                SearchCommunitiesActivity.this.finish();
            }
        });

        SourceDateList = filledData(communitylist);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * ListView
     * @param date
     * @return
     */
    private List<SortModel> filledData(ArrayList<HashMap<String, String>> date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.size(); i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).get("name"));
            sortModel.setCommunityid(date.get(i).get("id"));
            String pinyin = characterParser.getSelling(date.get(i).get("name"));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     *
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
