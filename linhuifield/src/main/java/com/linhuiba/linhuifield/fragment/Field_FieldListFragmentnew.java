package com.linhuiba.linhuifield.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldMainActivity;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldSearchResActivity;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.Field_Field_ShelvesActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_FieldlistModel;
import com.linhuiba.linhuifield.fieldview.FieldLoadMoreListView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/15.
 */
public class Field_FieldListFragmentnew extends FieldBaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener, FieldLoadMoreListView.OnLoadMore {
    private ViewPager mFieldListListViewPager;
    private ArrayList<View> mListViews;
    private SwipeRefreshLayout[] mSwipList= new SwipeRefreshLayout[5];
    private FieldLoadMoreListView[] mOrderList = new FieldLoadMoreListView[5];
    private RelativeLayout[] mFieldListNullLL = new RelativeLayout[5];
    private FeildListAdapter[] mFeildListAdapter= new FeildListAdapter[5];
    private ArrayList<Field_FieldlistModel>[] mDataList= new ArrayList[5];
    private int mCurrIndex = 0;// 当前页卡编号
    private int mCheckItemInt;
    private int[] mListPageSizeInt = new int[5];
    private InputMethodManager mManager;
    private View mMainContent;
    private ImageButton mFieldListRemindCloseImgBtn;
    private RelativeLayout mFieldListRemindRL;
    private TabLayout mFieldListTL;
    private int shopcardrestart = -1;
    private int mResTypeId;
    private int mTabTextViewInt[] = {R.string.fieldlist_resources_type_check_text, R.string.btn_maintab_field_list,
            R.string.fieldlist_resources_type_close_text,R.string.order_cancel_toast, R.string.myselfinfo_refused};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_fragment_field_list_test, container, false);
            TitleBarUtils.setTitleText(mMainContent, getResources().getString(R.string.field_maintab_fieldlist_title));
            TitleBarUtils.showBackButton(mMainContent, Field_FieldListFragmentnew.this.getActivity(),true,
                    "",
                    Field_FieldListFragmentnew.this.getActivity().getResources().getColor(R.color.default_bluebg),14);
            if (!(LoginManager.isLogin())) {
                LoginManager.getInstance().clearLoginInfo();
                Intent intent = new Intent("com.business.loginActivity");
                startActivity(intent);
                Field_FieldListFragmentnew.this.getActivity().finish();
            } else {
                initView();
                showProgressDialog();
                initData();
            }
        }
        return mMainContent;
    }
    @Override
    public void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager) Field_FieldListFragmentnew.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            if (Constants.isBackground(Field_FieldListFragmentnew.this.getContext())) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }

        } else if (screen == false) {
            shopcardrestart = 0;
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        //屏幕亮灭
        PowerManager pm = (PowerManager) Field_FieldListFragmentnew.this.getActivity().getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if (screen == true) {
            //是否处于后台
            if (Constants.isBackground(Field_FieldListFragmentnew.this.getContext())) {
                shopcardrestart = 0;
            } else {
                shopcardrestart = 1;
            }
        } else if (screen == false) {
            shopcardrestart = 0;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        StatService.onResume(this);
        if (shopcardrestart == 1) {
            showProgressDialog();
            initData();
        }
    }
    private void initView() {
        if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
            mResTypeId = 3;
            TitleBarUtils.setTitleText(mMainContent, getResources().getString(R.string.field_maintab_fieldlist_activity_title));
        }
        mFieldListTL = (TabLayout)mMainContent.findViewById(R.id.field_list_tablayout);
        mFieldListRemindCloseImgBtn = (ImageButton)mMainContent.findViewById(R.id.fieldlist_perfect_remind_close);
        mFieldListRemindRL = (RelativeLayout)mMainContent.findViewById(R.id.fieldlist_remind_layout);
        mFieldListRemindCloseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFieldListRemindRL.setVisibility(View.GONE);
            }
        });
        TitleBarUtils.shownextOtherButton(mMainContent, getResources().getString(R.string.txt_childaccount_titleaddtxt),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoginManager.getRole_id() != null && LoginManager.getRole_id().length() > 0) {

                        }
                    }
                });
        mManager = (InputMethodManager) Field_FieldListFragmentnew.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        hideKeyboard();
        mFieldListListViewPager = (ViewPager) mMainContent.findViewById(R.id.friend_viewpager);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = Field_FieldListFragmentnew.this.getActivity().getLayoutInflater();

        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mListViews.add(inflater.inflate(R.layout.field_loadmorelist_viewpager, null));
        mFieldListListViewPager.setAdapter(new OrderPagerAdapter(mListViews));
        for( int i=0 ;i< mListViews.size(); i++ ) {

            mSwipList[i] = (SwipeRefreshLayout) mListViews.get(i).findViewById(R.id.field_swipe_refresh);
            mOrderList[i]  = (FieldLoadMoreListView) mListViews.get(i).findViewById(R.id.field_order_list);
            mSwipList[i].setOnRefreshListener(this);
            mOrderList[i].setLoadMoreListen(this);
            mOrderList[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrIndex == 1) {
                        Intent resourceinfo = new Intent("com.business.aboutActivity");
                        resourceinfo.putExtra("type", com.linhuiba.linhuifield.config.Config.RESOURCE_INFO_WEB_INT);
                        resourceinfo.putExtra("resourceid", Integer.parseInt(mDataList[mCurrIndex].get(position).getResource_id())) ;
                        resourceinfo.putExtra("resources_type", Integer.parseInt(mDataList[mCurrIndex].get(position).getResource_type_id()));
                        startActivityForResult(resourceinfo, 1);
                    }

                }
            });
            mSwipList[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mFieldListNullLL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.lay_no_order);
            mFieldListNullLL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    initData();
                }
            });
        }
        mFieldListListViewPager.setOnPageChangeListener(new PagerChangeListener());
        mFieldListListViewPager.setCurrentItem(mCurrIndex);
        mFieldListTL.setupWithViewPager(mFieldListListViewPager);
        for (int i = 0; i < mFieldListTL.getTabCount(); i++) {
            mFieldListTL.getTabAt(i).setText(getResources().getString(mTabTextViewInt[i]));
        }

    }
    public class OrderPagerAdapter extends PagerAdapter {
        public List<View> mListViews = null;

        public OrderPagerAdapter(List<View> mListViews) {
            super();
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object)
        {
            ((ViewPager) container).removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mListViews.get(position), 0);
//			setPagerInfo(0);
            return mListViews.get(position);
        }

    }

    @Override
    public void loadMore() {
        String status = "0";
        if (mCurrIndex == 0 ) {
            status = "waiting_for_review";
        } else if(mCurrIndex == 1 ) {
            status = "ready_for_sale";
        } else if(mCurrIndex == 2 ) {
            if (mResTypeId == 3) {
                status = "closed";
            } else {
                status = "removed_from_sale";
            }
        } else if(mCurrIndex == 3 ) {
            if (mResTypeId == 3) {
                status = "removed_from_sale";
            } else {
                status = "rejected";
            }
        } else if (mCurrIndex == 4) {
            status = "rejected";
        }
        mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex] +1;
        if(!mDataList[mCurrIndex].isEmpty()){
            String fieldId = mDataList[mCurrIndex].get(mDataList[mCurrIndex].size()-1).getResource_id();
            Field_FieldApi.getresourceslist(MyAsyncHttpClient.MyAsyncHttpClient3(), getMoreFieldListHandler,mResTypeId, status,
                    String.valueOf(mListPageSizeInt[mCurrIndex]), "10");
        }

    }

    @Override
    public void onRefresh() {
        initData();
    }
    private void setPageView() {
        showProgressDialog();
        initData();
    }
    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrIndex = position;
            switch (position) {
                case 0:
                    setPageView();
                    break;
                case 1:
                    setPageView();
                    break;
                case 2:
                    setPageView();
                    break;
                case 3:
                    setPageView();
                    break;
                case 4:
                    setPageView();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private class FeildListAdapter extends BaseAdapter {
        private ArrayList<Field_FieldlistModel> mFeildList;
        private LayoutInflater mInflater = null;
        private int type;
        public FeildListAdapter(Context context , ArrayList<Field_FieldlistModel> list, int type) {
           if (context != null) {
               this.mFeildList = list;
               this.mInflater = LayoutInflater.from(context);
               this.type = type;
           }
        }
        @Override
        public int getCount() {
            if(mFeildList != null){
                return mFeildList.size();
            }else{
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.fragment_fieldlist_item, null);
                holder.img = (ImageView)convertView.findViewById(R.id.imageView);
                holder.price = (TextView)convertView.findViewById(R.id.field_price_value);
                holder.title = (TextView)convertView.findViewById(R.id.field_title_value);
                holder.address = (TextView)convertView.findViewById(R.id.field_address_value);
                holder.mfield_editorbtn = (TextView)convertView.findViewById(R.id.field_editorbtn);
                holder.mfieldlist_item_resourcestype_laber = (TextView)convertView.findViewById(R.id.fieldlist_item_resourcestype_laber);
                holder.btnshelves  = (TextView)convertView.findViewById(R.id.field_shelves);
                holder.btnadded = (TextView)convertView.findViewById(R.id.field_added);
                holder.mfieldlist_item_field_operation_layout = (LinearLayout)convertView.findViewById(R.id.fieldlist_item_field_operation_layout);
                holder.mfieldlist_item_view = (View)convertView.findViewById(R.id.fieldlist_item_view);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            if (position == mFeildList.size() - 1) {
                holder.mfieldlist_item_view.setVisibility(View.GONE);
            } else {
                holder.mfieldlist_item_view.setVisibility(View.VISIBLE);
            }
            if(mFeildList.get(position).getPic_url() !=null){
                if (mFeildList.get(position).getPic_url().length() != 0) {
                    Picasso.with(Field_FieldListFragmentnew.this.getActivity())
                            .load(mFeildList.get(position).getPic_url().toString() + Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .resize(160, 160).into(holder.img);

                } else {
                    Picasso.with(Field_FieldListFragmentnew.this.getActivity())
                            .load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                            .into(holder.img);
                }
            } else {
                Picasso.with(Field_FieldListFragmentnew.this.getActivity())
                        .load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small)
                        .into(holder.img);
            }
            if (mFeildList.get(position).getOrigin_price() > 0) {
                holder.price.setText(Constants.getPriceUnitStr(Field_FieldListFragmentnew.this.getContext(),(getResources().getString(R.string.order_listitem_price_unit_text)
                        +Constants.getdoublepricestring(mFeildList.get(position).getOrigin_price(),0.01)),10));
            }
            if (mFeildList.get(position).getField_type() != null &&
                    mFeildList.get(position).getField_type().length() > 0) {
                holder.mfieldlist_item_resourcestype_laber.setVisibility(View.VISIBLE);
                holder.mfieldlist_item_resourcestype_laber.setText(mFeildList.get(position).getField_type());
            } else {
                holder.mfieldlist_item_resourcestype_laber.setVisibility(View.GONE);
            }
            if (mFeildList.get(position).getResource_type_id() != null && mFeildList.get(position).getResource_type_id().length() > 0) {
                if (!mFeildList.get(position).getResource_type_id().equals("2")) {
                    holder.mfieldlist_item_field_operation_layout.setVisibility(View.VISIBLE);
                    holder.mfield_editorbtn.setVisibility(View.VISIBLE);
                    holder.mfield_editorbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //编辑场地
                            Field_AddResourcesModel.getnewInstance();
                            showProgressDialog();
                            Field_FieldApi.getresources(MyAsyncHttpClient.MyAsyncHttpClient2(),getresourcesHandler,mFeildList.get(position).getResource_id());
                        }
                    });
                    if (!mFeildList.get(position).getResource_type_id().equals("3")) {
                        if (type == 1) {
                            holder.btnshelves.setVisibility(View.VISIBLE);
                            holder.btnshelves.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent fieldshelvesintent = new Intent(Field_FieldListFragmentnew.this.getActivity(), Field_Field_ShelvesActivity.class);
                                    fieldshelvesintent.putExtra("fieldId",mFeildList.get(position).getResource_id());
                                    mCheckItemInt = position;
                                    startActivityForResult(fieldshelvesintent, 2);
                                }
                            });
                        } else if (type ==2) {
                            holder.btnadded.setVisibility(View.VISIBLE);
                            holder.btnadded.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(Field_FieldListFragmentnew.this.getActivity())
                                            .setTitle("继续执行？")
                                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                    mCheckItemInt = position;
                                                    Field_FieldApi.editFieldStatusthrough(MyAsyncHttpClient.MyAsyncHttpClient3(), editFieldStatus_addedHandler, mFeildList.get(position).getResource_id());

                                                }
                                            })
                                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.dismiss();

                                                }
                                            }).show();
                                }
                            });
                        }
                    }
                } else {
                    holder.mfieldlist_item_field_operation_layout.setVisibility(View.GONE);
                    holder.mfield_editorbtn.setVisibility(View.GONE);
                    holder.btnshelves.setVisibility(View.GONE);
                    holder.btnadded.setVisibility(View.GONE);
                }
                holder.title.setText((String)mFeildList.get(position).getName());
                holder.address.setText((String)mFeildList.get(position).getAddress());
            }
            return convertView;
        }
    }
    static class ViewHolder
    {
        public ImageView img;
        public TextView price;
        public TextView title;
        public TextView address;
        public TextView mfield_editorbtn;
        public TextView mfieldlist_item_resourcestype_laber;
        public TextView btnshelves;
        public TextView btnadded;
        public LinearLayout mfieldlist_item_field_operation_layout;
        public View mfieldlist_item_view;
    }
    private void initData() {
        String status = "0";
        if (mCurrIndex == 0 ) {
            status = "waiting_for_review";
        } else if(mCurrIndex == 1 ) {
            status = "ready_for_sale";
        } else if(mCurrIndex == 2 ) {
            if (mResTypeId == 3) {
                status = "closed";
            } else {
                status = "removed_from_sale";
            }
        } else if(mCurrIndex == 3 ) {
            if (mResTypeId == 3) {
                status = "removed_from_sale";
            } else {
                status = "rejected";
            }
        } else if (mCurrIndex == 4) {
            status = "rejected";
        }
        mListPageSizeInt[mCurrIndex] = 1;
        mOrderList[mCurrIndex].set_refresh();
        Field_FieldApi.getresourceslist(MyAsyncHttpClient.MyAsyncHttpClient3(),
                getFieldListHandler,mResTypeId,
                status, String.valueOf(mListPageSizeInt[mCurrIndex]), "10");
    }
    private LinhuiAsyncHttpResponseHandler getFieldListHandler = new LinhuiAsyncHttpResponseHandler(Field_FieldlistModel.class ,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if(mSwipList[mCurrIndex].isShown()){
                mSwipList[mCurrIndex].setRefreshing(false);
            }
            mDataList[mCurrIndex] = (ArrayList<Field_FieldlistModel>) data;
            if( mDataList[mCurrIndex] == null ||  mDataList[mCurrIndex].isEmpty()) {
                mFieldListNullLL[mCurrIndex].setVisibility(View.VISIBLE);
            } else {
                mFieldListNullLL[mCurrIndex].setVisibility(View.GONE);
                mFeildListAdapter[mCurrIndex] = new FeildListAdapter(Field_FieldListFragmentnew.this.getActivity(), mDataList[mCurrIndex],mCurrIndex);
                mOrderList[mCurrIndex].setAdapter(mFeildListAdapter[mCurrIndex]);
                if (mDataList[mCurrIndex].size() < 10) {
                    mOrderList[mCurrIndex].set_loaded();
                }
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode,okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(mSwipList[mCurrIndex].isShown()){
                mSwipList[mCurrIndex].setRefreshing(false);
            }
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getMoreFieldListHandler = new LinhuiAsyncHttpResponseHandler(Field_FieldlistModel.class ,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            ArrayList<Field_FieldlistModel> tmp = (ArrayList<Field_FieldlistModel>) data;
            if (data != null && tmp.size() > 0) {
                for( Field_FieldlistModel fieldDetail: tmp ){
                    mDataList[mCurrIndex].add(fieldDetail);
                }
                mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
                mOrderList[mCurrIndex].onLoadComplete();
                if (tmp.size() < 10 ) {
                    mOrderList[mCurrIndex].set_loaded();
                }
            } else {
                mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex]-1;
                mOrderList[mCurrIndex].set_loaded();
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mOrderList[mCurrIndex].onLoadComplete();
            mListPageSizeInt[mCurrIndex] = mListPageSizeInt[mCurrIndex]-1;
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler editFieldStatus_shelvesHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mDataList[mCurrIndex].remove(mCheckItemInt);
            mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
            BaseMessageUtils.showToast(getContext(), Field_FieldListFragmentnew.this.getResources().getString(R.string.field_shelves_toast));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler editFieldStatus_addedHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mDataList[mCurrIndex].remove(mCheckItemInt);
            mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
            BaseMessageUtils.showToast(getContext(), Field_FieldListFragmentnew.this.getResources().getString(R.string.field_added_toast));

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (Field_FieldListFragmentnew.this.getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (Field_FieldListFragmentnew.this.getActivity().getCurrentFocus() != null)
                mManager.hideSoftInputFromWindow(Field_FieldListFragmentnew.this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data.getExtras() != null) {
                    if (data.getExtras().getInt("updata") == 1) {
                        showProgressDialog();
                        initData();
                    }
                }

                break;
            case 2:
                if (data.getExtras() != null) {
                    if (data.getExtras().getInt("updata") == 1) {
                        mDataList[mCurrIndex].remove(mCheckItemInt);
                        mFeildListAdapter[mCurrIndex].notifyDataSetChanged();
                        BaseMessageUtils.showToast(getContext(), Field_FieldListFragmentnew.this.getResources().getString(R.string.field_shelves_toast));
                    }
                }

                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private LinhuiAsyncHttpResponseHandler getresourcesHandler = new LinhuiAsyncHttpResponseHandler(Field_AddResourcesModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            Field_AddResourcesModel.getnewInstance((Field_AddResourcesModel) data);
            Intent addfieldintent = new Intent(Field_FieldListFragmentnew.this.getActivity(),FieldAddFieldMainActivity.class);
            startActivity(addfieldintent);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
}
