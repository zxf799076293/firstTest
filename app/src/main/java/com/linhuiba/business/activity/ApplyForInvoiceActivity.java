package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.ApplyForInvoiceAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.InvoicesModel;
import com.linhuiba.business.mvppresenter.ApplyForInvoiceMvpPresenter;
import com.linhuiba.business.mvpview.ApplyForInvoiceMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuifield.connector.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ApplyForInvoiceActivity extends BaseMvpActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,
        Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,
        ApplyForInvoiceMvpView {
    @InjectView(R.id.clecked_invoicenum_relativelayout)
    RelativeLayout mCleckedInvoiceNumRL;
    @InjectView(R.id.applyforinvoicesize_txt) TextView mApplyForInvoiceSizeTV;
    @InjectView(R.id.invoice_next_btn)
    Button mInvoiceNextBtn;
    @InjectView(R.id.apply_invoice_chooseall_checkbox)
    CheckBox mApplyForInvoiceChooseAllCB;
    @InjectView(R.id.applyforinvoice_tablayout)
    TabLayout mApplyForInvoiceTL;
    @InjectView(R.id.applyforinvoice_total_fee_tv)
    TextView mApplyForInvoiceTotalFeeTV;
    @InjectView(R.id.applyforinvoice_remind_ll)
    RelativeLayout mApplyForInvoiceRemindRL;
    @InjectView(R.id.applyforinvoice_remind_imgBtn)
    ImageButton mApplyForInvoiceRemindImgBtn;
    private ViewPager mViewPager;
    private int mCurrIndex = 0;// 当前页卡编号
    private ArrayList<View> mListViews;
    private LoadMoreListView[] mInvoiceLV = new LoadMoreListView[2];
    private RelativeLayout[] mLayNoInvoiceRL = new RelativeLayout[2];
    private SwipeRefreshLayout[] swipList= new SwipeRefreshLayout[2];
    private ApplyForInvoiceAdapter[] mInvoiceAdapter = new ApplyForInvoiceAdapter[2];
    private ArrayList<InvoicesModel>[] Invoicelistdata = new ArrayList[2];
    private int[] mListPage = new int[2];
    private ArrayList<HashMap<Object,Object>> mCheckedInvoiceList = new ArrayList<>();
    private ArrayList<Integer> mCheckedInvoiceIdList = new ArrayList<>();
    private int mTabTextViewList[] = {R.string.applyforinvoice_tablayout_one_tv_str, R.string.applyforinvoice_tablayout_two_tv_str};
    private int mTabTextViewSupplementStrList[] = {R.string.applyforinvoice_common_invoive_text, R.string.applyforinvoice_dedicated_invoive_text};
    public boolean isSupplement;//补开发票显示界面判断
    private ApplyForInvoiceMvpPresenter mPresenter;
    private int checkOrderItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyforinvoice);
        initview();
        showProgressDialog();
        initdata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void initview() {
        ButterKnife.inject(this);
        mPresenter = new ApplyForInvoiceMvpPresenter();
        mPresenter.attachView(this);
        Intent mSupplementIntent = getIntent();
        if (mSupplementIntent.getExtras() != null &&
                mSupplementIntent.getExtras().get("isSupplement") != null) {
            isSupplement = mSupplementIntent.getBooleanExtra("isSupplement",false);
        }
        if (mSupplementIntent.getExtras() != null &&
                mSupplementIntent.getExtras().get("id") != null &&
                mSupplementIntent.getExtras().get("id").toString().length() > 0) {
            checkOrderItemId = Integer.parseInt(mSupplementIntent.getExtras().get("id").toString());
        }
        if (mSupplementIntent.getExtras() != null &&
                mSupplementIntent.getExtras().get("id") != null &&
                mSupplementIntent.getExtras().get("id").toString().length() > 0) {
            checkOrderItemId = Integer.parseInt(mSupplementIntent.getExtras().get("id").toString());
            if (mSupplementIntent.getExtras() != null &&
                    mSupplementIntent.getExtras().get("price") != null &&
                    mSupplementIntent.getExtras().get("price").toString().length() > 0) {
                checkOrderItemId = Integer.parseInt(mSupplementIntent.getExtras().get("id").toString());
                mApplyForInvoiceTotalFeeTV.setText(com.linhuiba.business.connector.Constants.getSpannableAllStr(ApplyForInvoiceActivity.this,(getResources().getString(R.string.order_listitem_price_unit_text) +
                                mSupplementIntent.getExtras().get("price").toString()), 12,0,1,false,0,0,
                        12, mSupplementIntent.getExtras().get("price").toString().length() + 1 - 2,
                        mSupplementIntent.getExtras().get("price").toString().length() + 1,
                        false,0,0));
            }
        }

        TitleBarUtils.showBackImg(this,true);
        if (isSupplement) {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.applyforinvoice_supplement_title_tv_str));
        } else {
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.applyforinvoice_title_tv_str));
            //以后再显示 没有理顺
//            TitleBarUtils.shownextButton(this, getResources().getString(R.string.applyforinvoice_title_right_tv_str),new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent isSupplementIntent = new Intent(ApplyForInvoiceActivity.this,ApplyForInvoiceActivity.class);
//                    isSupplementIntent.putExtra("isSupplement",true);
//                    startActivity(isSupplementIntent);
//                }
//            });
        }
        mViewPager = (ViewPager) this.findViewById(R.id.applyforinvoice_viewpage);
        mListViews = new ArrayList<View>();
        LayoutInflater inflater = this.getLayoutInflater();
        mListViews.add(inflater.inflate(R.layout.viewpager_invoicelist, null));
        mListViews.add(inflater.inflate(R.layout.viewpager_invoicelist, null));
        mViewPager.setAdapter(new OrderPagerAdapter(mListViews));
        //未申请界面点击title操作
        for( int i = 0 ;i < mListViews.size(); i++ ) {
            mInvoiceLV[i]  = (LoadMoreListView) mListViews.get(i).findViewById(R.id.order_list);
            mInvoiceLV[i].setDividerHeight(0);
            swipList[i] = (SwipeRefreshLayout)  mListViews.get(i).findViewById(R.id.swipe_refresh);
            mInvoiceLV[i].setLoadMoreListen(this);
            swipList[i].setOnRefreshListener(this);
            swipList[i].setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mLayNoInvoiceRL[i] = (RelativeLayout) mListViews.get(i).findViewById(R.id.lay_no_order);
            mLayNoInvoiceRL[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    initdata();
                }
            });
        }
        mViewPager.setOnPageChangeListener(new OrderPagerChangeListener());
        mViewPager.setCurrentItem(mCurrIndex);
        mApplyForInvoiceTL.post(new Runnable() {
            @Override
            public void run() {
                Constants.setIndicator(mApplyForInvoiceTL,40,40);
            }
        });
        mApplyForInvoiceTL.setupWithViewPager(mViewPager);
        for (int i = 0; i < mApplyForInvoiceTL.getTabCount(); i++) {
            if (isSupplement) {
                mApplyForInvoiceTL.getTabAt(i).setText(getResources().getString(mTabTextViewSupplementStrList[i]));
            } else {
                mApplyForInvoiceTL.getTabAt(i).setText(getResources().getString(mTabTextViewList[i]));
            }
        }
    }
    private void initdata() {
        mApplyForInvoiceSizeTV.setText("0");
        mInvoiceNextBtn.setEnabled(false);
        mInvoiceNextBtn.setBackgroundColor(getResources().getColor(R.color.checkbox_managecontact_txtcolor));
        mApplyForInvoiceChooseAllCB.setEnabled(false);
        mApplyForInvoiceChooseAllCB.setChecked(false);
        mInvoiceLV[mCurrIndex].set_refresh();
        if (mCurrIndex == 0) {
            mListPage[mCurrIndex] = 1;
            String mTaxTypeStr = "";
            if (isSupplement) {
                mTaxTypeStr = String.valueOf(mCurrIndex + 2);
            } else {
                mTaxTypeStr = String.valueOf(mCurrIndex + 1);
            }
            mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]), "10",mTaxTypeStr,false);
        } else {
            if (isSupplement) {
                mListPage[mCurrIndex] = 1;
                String mTaxTypeStr = String.valueOf(mCurrIndex + 2);
                mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]), "10",mTaxTypeStr,false);
            } else {
                mListPage[mCurrIndex] = 1;
                mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]), "10","",true);
            }
        }
    }
    @OnClick({
            R.id.invoice_next_btn,
            R.id.apply_invoice_chooseall_layout,
            R.id.applyforinvoice_remind_imgBtn
    })
    public void invoiceonclick(View view) {
        switch (view.getId()) {
            case R.id.invoice_next_btn:
                if (mCheckedInvoiceList != null) {
                    mCheckedInvoiceList.clear();
                }
                if (mCheckedInvoiceIdList != null) {
                    mCheckedInvoiceIdList.clear();
                }
                ArrayList<HashMap<String,String>> mInvoiceList = new ArrayList<>();
                if (Invoicelistdata[mCurrIndex] != null) {
                    if (Invoicelistdata[mCurrIndex].size() > 0) {
                        if (isSupplement) {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                if (ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().get(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id())) {
                                    HashMap<Object,Object> map = new HashMap<>();
                                    map.put("order_item_id",Integer.parseInt(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id()));
                                    mCheckedInvoiceList.add(map);
                                    mCheckedInvoiceIdList.add(Integer.parseInt(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id()));
                                    HashMap<String,String> invoiceMap = new HashMap<>();
                                    invoiceMap.put("real_fee",Invoicelistdata[mCurrIndex].get(i).getActual_fee());
                                    mInvoiceList.add(invoiceMap);
                                }
                            }

                        } else {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                if (ApplyForInvoiceAdapter.getIsSelected_invoice().get(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id())) {
                                    HashMap<Object,Object> map = new HashMap<>();
                                    map.put("order_item_id",Integer.parseInt(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id()));
                                    mCheckedInvoiceList.add(map);
                                    mCheckedInvoiceIdList.add(Integer.parseInt(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id()));
                                    HashMap<String,String> invoiceMap = new HashMap<>();
                                    invoiceMap.put("real_fee",Invoicelistdata[mCurrIndex].get(i).getActual_fee());
                                    mInvoiceList.add(invoiceMap);
                                }
                            }

                        }
                    }
                }
                if (mCheckedInvoiceList != null && mCheckedInvoiceIdList != null) {
                    if (mCheckedInvoiceList.size() > 0 && mCheckedInvoiceIdList.size() > 0) {
                        Intent invoiceintent = new Intent(this,InvoiceInformationActivity.class);
                        invoiceintent.putExtra("checkedlist",(Serializable) mCheckedInvoiceList);
                        invoiceintent.putExtra("checkedidlist",(Serializable) mCheckedInvoiceIdList);
                        invoiceintent.putExtra("invoice_list",(Serializable) mInvoiceList);
                        invoiceintent.putExtra("invoice_type",mCurrIndex);
                        invoiceintent.putExtra("isSupplement",isSupplement);
                        if (isSupplement) {
                            invoiceintent.putExtra("invoice_type",mCurrIndex+2);
                        } else {
                            invoiceintent.putExtra("invoice_type",mCurrIndex+1);
                        }
                        startActivityForResult(invoiceintent, 1);
                    }
                }
                break;
            case R.id.apply_invoice_chooseall_layout:
                if (Invoicelistdata[mCurrIndex].size() > 0) {
                    if (!mApplyForInvoiceChooseAllCB.isChecked()) {
                        mApplyForInvoiceChooseAllCB.setChecked(true);
                        if (isSupplement) {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                if (Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != null &&
                                        Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != false) {
                                    ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),true);
                                }
                            }

                        } else {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                if (Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != null &&
                                        Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != false) {
                                    ApplyForInvoiceAdapter.getIsSelected_invoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),true);
                                }
                            }
                        }
                        mInvoiceAdapter[mCurrIndex].notifyDataSetChanged();
                        getnextbtn_state();
                    } else {
                        mApplyForInvoiceChooseAllCB.setChecked(false);
                        mInvoiceNextBtn.setBackgroundColor(getResources().getColor(R.color.checkbox_managecontact_txtcolor));
                        if (isSupplement) {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),false);
                            }
                        } else {
                            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                                ApplyForInvoiceAdapter.getIsSelected_invoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),false);
                            }
                        }
                        mInvoiceAdapter[mCurrIndex].notifyDataSetChanged();
                        getnextbtn_state();
                    }
                } else {
                    mApplyForInvoiceChooseAllCB.setChecked(false);
                }
                break;
            case R.id.applyforinvoice_remind_imgBtn:
                mApplyForInvoiceRemindRL.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    @Override
    public void loadMore() {
        mListPage[mCurrIndex] = mListPage[mCurrIndex] +1;
        if(!Invoicelistdata[mCurrIndex].isEmpty()){
            if (mCurrIndex == 0) {
                String mTaxTypeStr = "";
                if (isSupplement) {
                    mTaxTypeStr = String.valueOf(mCurrIndex + 2);
                } else {
                    mTaxTypeStr = String.valueOf(mCurrIndex + 1);
                }
                mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]), "10",mTaxTypeStr,false);
            } else {
                if (isSupplement) {
                    String mTaxTypeStr = String.valueOf(mCurrIndex + 2);
                    mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]), "10",mTaxTypeStr,false);
                } else {
                    mPresenter.getOnInvoiceData(String.valueOf(mListPage[mCurrIndex]),"10","",true);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        initdata();
    }
    public void getInvoice_clicksize_price(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        mApplyForInvoiceSizeTV.setText(String.valueOf(position));
        mApplyForInvoiceTotalFeeTV.setText(com.linhuiba.business.connector.Constants.getSpannableAllStr(ApplyForInvoiceActivity.this,(getResources().getString(R.string.order_listitem_price_unit_text) +
                        str), 12,0,1,false,0,0,
                12, str.length() + 1 - 2,
                str.length() + 1,
                false,0,0));
    }
    /**
     * ViewPager适配器
     * */
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
    /**
     * 页卡切换监听
     * */
    public class OrderPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            mCurrIndex = arg0;
            switch (arg0) {
                case 0:
                    setPageView();
                    break;
                case 1:
                    setPageView();
                    break;
                default:
                    break;

            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

    }
    private void setPageView() {
        showProgressDialog();
        initdata();

    }
    public void getnextbtn_state() {
        int num = 0;
        int issueInvoiceNum = 0;
        if (isSupplement) {
            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                if (ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().get(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id())) {
                    num ++;
                }
                if (Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != null &&
                        Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != false) {
                    issueInvoiceNum ++;
                }

            }
        } else {
            for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                if (ApplyForInvoiceAdapter.getIsSelected_invoice().get(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id())) {
                    num ++;
                }
                if (Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != null &&
                        Invoicelistdata[mCurrIndex].get(i).getIssue_invoice() != false) {
                    issueInvoiceNum ++;
                }
            }
        }
        if (num > 0) {
            mInvoiceNextBtn.setEnabled(true);
            mInvoiceNextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressedbg));
            mApplyForInvoiceSizeTV.setText(String.valueOf(num));
            if (num == issueInvoiceNum) {
                mApplyForInvoiceChooseAllCB.setChecked(true);
            } else {
                mApplyForInvoiceChooseAllCB.setChecked(false);
            }
        } else {
            mInvoiceNextBtn.setEnabled(false);
            mInvoiceNextBtn.setBackgroundColor(getResources().getColor(R.color.checkbox_managecontact_txtcolor));
            mApplyForInvoiceSizeTV.setText("0");
            mApplyForInvoiceChooseAllCB.setChecked(false);
        }
    }
    @Override
    public void onNoInvoiceListSuccess(Object data) {
        if(swipList[mCurrIndex].isShown()){
            swipList[mCurrIndex].setRefreshing(false);
        }
        if (data != null) {
            if (isSupplement) {
                if (data instanceof JSONObject) {
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    if (jsonObject != null && jsonObject.get("data") != null) {
                        Invoicelistdata[mCurrIndex] = (ArrayList<InvoicesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),InvoicesModel.class);
                    }
                }

            } else {
                if (mCurrIndex == 0) {
                    if (data instanceof JSONObject) {
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        if (jsonObject != null && jsonObject.get("data") != null) {
                            Invoicelistdata[mCurrIndex] = (ArrayList<InvoicesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),InvoicesModel.class);
                        }
                    }
                } else {
                    if (data instanceof JSONArray) {
                        Invoicelistdata[mCurrIndex] =  (ArrayList<InvoicesModel>) JSONArray.parseArray(data.toString(),InvoicesModel.class);
                    }
                }
            }
        }

        if( Invoicelistdata[mCurrIndex] == null ||  Invoicelistdata[mCurrIndex].isEmpty()) {
            mLayNoInvoiceRL[mCurrIndex].setVisibility(View.VISIBLE);
        } else {
            mLayNoInvoiceRL[mCurrIndex].setVisibility(View.GONE);
            if (isSupplement) {
                ApplyForInvoiceAdapter.clearIsSelectedSupplementInvoice();
                for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                    if (Invoicelistdata[mCurrIndex].get(i).getField_order_item_id() != null &&
                            Invoicelistdata[mCurrIndex].get(i).getField_order_item_id().length() > 0) {
                        if (Invoicelistdata[mCurrIndex].get(i).getField_order_item_id().equals(String.valueOf(checkOrderItemId))) {
                            ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(), true);

                        } else {
                            ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(), false);

                        }
                    }
                }
            } else {
                if (mCurrIndex == 0) {
                    ApplyForInvoiceAdapter.clear_isSelectedlist_invoice();
                    for (int i = 0; i < Invoicelistdata[mCurrIndex].size(); i++) {
                        if (Invoicelistdata[mCurrIndex].get(i).getField_order_item_id() != null &&
                                Invoicelistdata[mCurrIndex].get(i).getField_order_item_id().length() > 0) {
                            if (Invoicelistdata[mCurrIndex].get(i).getField_order_item_id().equals(String.valueOf(checkOrderItemId))) {
                                ApplyForInvoiceAdapter.getIsSelected_invoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),true);
                            } else {
                                ApplyForInvoiceAdapter.getIsSelected_invoice().put(Invoicelistdata[mCurrIndex].get(i).getField_order_item_id(),false);
                            }
                        }
                    }
                }
            }
            mInvoiceLV[mCurrIndex].setVisibility(View.VISIBLE);
            if (isSupplement) {
                mInvoiceAdapter[mCurrIndex] = new ApplyForInvoiceAdapter(ApplyForInvoiceActivity.this,ApplyForInvoiceActivity.this,Invoicelistdata[mCurrIndex],0);
            } else {
                mInvoiceAdapter[mCurrIndex] = new ApplyForInvoiceAdapter(ApplyForInvoiceActivity.this,ApplyForInvoiceActivity.this,Invoicelistdata[mCurrIndex],mCurrIndex);
            }
            mInvoiceLV[mCurrIndex].setAdapter(mInvoiceAdapter[mCurrIndex]);
            if (isSupplement) {
                mCleckedInvoiceNumRL.setVisibility(View.VISIBLE);
                getnextbtn_state();
            } else {
                if (mCurrIndex == 0) {
                    mCleckedInvoiceNumRL.setVisibility(View.VISIBLE);
                    getnextbtn_state();
                } else {
                    mCleckedInvoiceNumRL.setVisibility(View.GONE);
                }
            }
            if (Invoicelistdata[mCurrIndex].size() < 10) {
                mInvoiceLV[mCurrIndex].set_loaded();
            }
        }
    }
    @Override
    public void onNoInvoiceListFailure(boolean superresult, Throwable error) {
        if(swipList[mCurrIndex].isShown()){
            swipList[mCurrIndex].setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
        checkAccess(error);
    }
    @Override
    public void onNoInvoiceListMoreSuccess(Object data) {
        ArrayList<InvoicesModel> tmp = new ArrayList<>();
        if (data != null) {
            if (isSupplement) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    tmp = (ArrayList<InvoicesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),InvoicesModel.class);
                }
            } else {
                if (mCurrIndex == 0) {
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    if (jsonObject != null && jsonObject.get("data") != null) {
                        tmp = (ArrayList<InvoicesModel>) JSONArray.parseArray(jsonObject.get("data").toString(),InvoicesModel.class);
                    }
                } else {
                    tmp =  (ArrayList<InvoicesModel>) JSONArray.parseArray(data.toString(),InvoicesModel.class);
                }
            }
        }
        if (isSupplement) {
            if (data != null && tmp != null && tmp.size() != 0) {
                for( InvoicesModel fieldDetail: tmp ){
                    ApplyForInvoiceAdapter.getIsSelectedSupplementInvoice().put(fieldDetail.getField_order_item_id(),false);
                    Invoicelistdata[mCurrIndex].add(fieldDetail);
                }
                mInvoiceAdapter[mCurrIndex].notifyDataSetChanged();
                mInvoiceLV[mCurrIndex].onLoadComplete();
                if (tmp.size() < 10 ) {
                    mInvoiceLV[mCurrIndex].set_loaded();
                }
                mApplyForInvoiceChooseAllCB.setChecked(false);
            } else {
                mListPage[mCurrIndex] = mListPage[mCurrIndex]-1;
                mInvoiceLV[mCurrIndex].set_loaded();
            }
        } else {
            if (data != null && tmp != null && tmp.size() != 0 && mCurrIndex == 0) {
                for( InvoicesModel fieldDetail: tmp ){
                    ApplyForInvoiceAdapter.getIsSelected_invoice().put(fieldDetail.getField_order_item_id(),false);
                    Invoicelistdata[mCurrIndex].add(fieldDetail);
                }
                mInvoiceAdapter[mCurrIndex].notifyDataSetChanged();
                mInvoiceLV[mCurrIndex].onLoadComplete();
                if (tmp.size() < 10 ) {
                    mInvoiceLV[mCurrIndex].set_loaded();
                }
                mApplyForInvoiceChooseAllCB.setChecked(false);
            } else {
                mListPage[mCurrIndex] = mListPage[mCurrIndex]-1;
                mInvoiceLV[mCurrIndex].set_loaded();
            }
        }
    }

    @Override
    public void onNoInvoiceListMoreFailure(boolean superresult, Throwable error) {
        mInvoiceLV[mCurrIndex].onLoadComplete();
        mListPage[mCurrIndex] = mListPage[mCurrIndex]-1;
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
        checkAccess(error);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                initdata();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        initdata();
    }
}
