package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.Industry_ChooseAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.model.IndustriesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/10/14.
 */
public class Industry_ChooseActivity extends BaseMvpActivity {
    @InjectView(R.id.industry_listview_child)
    ListView mindustry_listview_child;
    @InjectView(R.id.industry_listview)
    ListView mindustry_listview;
    private Industry_ChooseAdapter industry_chooseAdapter;
    private Industry_ChooseAdapter industry_chooseAdapter_child;
    private ArrayList<IndustriesModel> Industriedata = new ArrayList<>();
    private ArrayList<IndustriesModel> Industriechilddata= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_choose);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_industry_choose_titletext));
        TitleBarUtils.showBackImg(this, true);
        initview();
        initdata();
    }
    private void initview() {
        mindustry_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < Industriedata.size(); i++) {
                    if (position == i) {
                        Industry_ChooseAdapter.getisSelected_industry().put(Industriedata.get(i).getId(),true);
                    } else {
                        Industry_ChooseAdapter.getisSelected_industry().put(Industriedata.get(i).getId(),false);
                    }
                }
                industry_chooseAdapter.notifyDataSetChanged();
                if (Industriechilddata != null) {
                    Industriechilddata.clear();
                }
                if (Industriedata.get(position).getSelected_children() != null) {
                    Industriechilddata.addAll(Industriedata.get(position).getSelected_children());
                }
                industry_chooseAdapter_child = new Industry_ChooseAdapter(Industry_ChooseActivity.this.getContext(), Industriechilddata, 1);
                mindustry_listview_child.setAdapter(industry_chooseAdapter_child);
            }
        });
        mindustry_listview_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myselfinfo_company = new Intent();
                myselfinfo_company.putExtra("industry_id",Industriechilddata.get(position).getId());
                myselfinfo_company.putExtra("display_name", Industriechilddata.get(position).getDisplay_name());
                setResult(5, myselfinfo_company);
                Industry_ChooseActivity.this.finish();
            }
        });

    }
    private void initdata() {
        showProgressDialog();
        UserApi.getindustries(MyAsyncHttpClient.MyAsyncHttpClient2(), IndustriesHandler);
    }
    private LinhuiAsyncHttpResponseHandler IndustriesHandler = new LinhuiAsyncHttpResponseHandler(IndustriesModel.class,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            Industriedata = (ArrayList<IndustriesModel>) data;
            if (Industriedata != null) {
                if (Industriedata.size() > 0) {
                    for (int i = 0; i < Industriedata.size(); i++) {
                        if (i == 0) {
                            Industry_ChooseAdapter.getisSelected_industry().put(Industriedata.get(i).getId(),true);
                        } else {
                            Industry_ChooseAdapter.getisSelected_industry().put(Industriedata.get(i).getId(),false);
                        }
                    }
                }
            }
            industry_chooseAdapter = new Industry_ChooseAdapter(Industry_ChooseActivity.this,Industriedata,0);
            mindustry_listview.setAdapter(industry_chooseAdapter);
            if (Industriechilddata != null) {
                Industriechilddata.clear();
            }
            if (Industriedata.get(0).getSelected_children() != null) {
                Industriechilddata.addAll(Industriedata.get(0).getSelected_children());
            }
            industry_chooseAdapter_child = new Industry_ChooseAdapter(Industry_ChooseActivity.this.getContext(), Industriechilddata, 1);
            mindustry_listview_child.setAdapter(industry_chooseAdapter_child);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
}
