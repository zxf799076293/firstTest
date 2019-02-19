package com.linhuiba.linhuifield.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.FieldBaseFragment;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.fieldactivity.Field_OrderRefuseActivity;
import com.linhuiba.linhuifield.fieldadapter.AreaListViewAdapter;
import com.linhuiba.linhuifield.fieldadapter.ResourceCalendarAdapter;
import com.linhuiba.linhuifield.fieldadapter.ResourceCalendarMyListviewAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpFragment;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.ScheduleCalendarModel;
import com.linhuiba.linhuifield.fieldmodel.ScheduleCalendarlistModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuifield.fieldview.FieldMyListView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;

import org.apache.http.Header;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/7/21.
 */
public class Field_ResourceCalendarFragment extends FieldBaseMvpFragment implements Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,Field_AddFieldChoosePictureCallBack.FieldOrderApproved{
    private TextView mresourcecalendar_currentMonth;
    private ViewFlipper flipper;
    private FieldMyListView mresourcecalendarlistview;
    private Switch mToggleButton;
    private LinearLayout mall_no_linearlayout;
    private ImageButton mresourcecalendar_prevMonth;
    private ImageButton mresourcecalendar_nextMonth;
    private TextView mtesid_textview;
    private FieldMyGridView gridView = null;
    private View mChooseResourcePwView;
    public GestureDetector gestureDetector = null;
    private ResourceCalendarAdapter calV = null;
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int gvFlag = 0;
    private String currentDate = "";
    private ArrayList<Date> munuserdatelist = new ArrayList<>();//不可用日期
    private ArrayList<Date> muntreateddatelist = new ArrayList<>();//未处理日期
    private SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ResourceCalendarMyListviewAdapter resourceCalendarMyListviewAdapter;
    private HashMap<String,ScheduleCalendarModel> maplistdata = new HashMap<>();
    private ArrayList<HashMap<String,String>> mapreslist = new ArrayList<>();
    private boolean slidingright;//右滑true
    private SupportPopupWindow pw;
    private int parameter_year;
    private int parameter_month;
    private String res_id = "";
    private String clickdate = "";
    private ArrayList<ScheduleCalendarlistModel> datas;
    private int ResourceCalendarMyListviewAdapter_position;
    private View mMainContent;
    private CustomDialog mOrderOperationDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.field_activity_resourcecalendar, container, false);
            TitleBarUtils.setTitleText(mMainContent, getResources().getString(R.string.field_resourcescalendar_titletxt));
            if (!(LoginManager.isLogin())) {
                LoginManager.getInstance().clearLoginInfo();
                Intent intent = new Intent("com.business.loginActivity");
                startActivity(intent);
                Field_ResourceCalendarFragment.this.getActivity().finish();
            } else {
                if (!LoginManager.isRight_to_publish() && !LoginManager.isIs_supplier()) {
                    TitleBarUtils.showBackButton(mMainContent, Field_ResourceCalendarFragment.this.getActivity(),true,
                            Field_ResourceCalendarFragment.this.getActivity().getResources().getString(R.string.back),
                            Field_ResourceCalendarFragment.this.getActivity().getResources().getColor(R.color.default_bluebg),14);
                }
                mresourcecalendar_currentMonth = (TextView)mMainContent.findViewById(R.id.resourcecalendar_currentMonth);
                flipper = (ViewFlipper)mMainContent.findViewById(R.id.resourcecalendar_flipper);
                mresourcecalendarlistview = (FieldMyListView) mMainContent.findViewById(R.id.resourcecalendarlistview);
                mToggleButton = (Switch)mMainContent.findViewById(R.id.calendar_notuser_btn);
                mall_no_linearlayout = (LinearLayout)mMainContent.findViewById(R.id.all_no_linearlayout);
                mresourcecalendar_prevMonth = (ImageButton)mMainContent.findViewById(R.id.resourcecalendar_prevMonth);
                mresourcecalendar_nextMonth = (ImageButton)mMainContent.findViewById(R.id.resourcecalendar_nextMonth);
                mtesid_textview = (TextView) mMainContent.findViewById(R.id.tesid_textview);
                mChooseResourcePwView = (View) mMainContent.findViewById(R.id.choose_resource_pe_view);
                initview();
            }
        }
        return mMainContent;
    }

    private void initview() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        parameter_year = year_c;
        parameter_month = month_c;
        mToggleButton.setVisibility(View.GONE);
        Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleHandler, "", 1,
                "all", Constants.getFirstDayOfMonth(year_c, month_c - 1), Constants.getLastDayOfMonth(year_c, month_c - 1));
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mToggleButton.isChecked()) {
                    if (res_id.length() > 0) {
                        showProgressDialog();
                        Field_FieldApi.setcalendarnouser(MyAsyncHttpClient.MyAsyncHttpClient(),setcalendarnouserHandler,res_id,clickdate);
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_chooscoresources_hinttxt));
                        mToggleButton.setChecked(true);
                    }
                } else {
                    if (res_id.length() > 0) {
                        showProgressDialog();
                        Field_FieldApi.deletecalendarnouser(MyAsyncHttpClient.MyAsyncHttpClient(), deletecalendarnouserHandler, Field_ResourceCalendarFragment.this.getActivity(), res_id, clickdate);
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_chooscoresources_hinttxt));
                        mToggleButton.setChecked(false);
                    }
                }
            }
        });
        resourcecalendarclick();
    }
    private LinhuiAsyncHttpResponseHandler setcalendarnouserHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mToggleButton.setChecked(false);
            for (int i = 0; i < ResourceCalendarAdapter.dayNumber.length; i++) {
                String[] strarray = ResourceCalendarAdapter.dayNumber[i].split("\\.");
                if (i < ResourceCalendarAdapter.daysOfMonth + ResourceCalendarAdapter.dayOfWeek && i >= ResourceCalendarAdapter.dayOfWeek) {
                    if (strarray[3].equals(clickdate)) {
                        if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 1) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 4);
                        } else if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 2) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 5);
                        } else if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 0) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 3);
                        }
                    }
                }
            }
            calV.notifyDataSetChanged();
            BaseMessageUtils.showToast(getResources().getString(R.string.setcalendarnouserHandler));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mToggleButton.setChecked(true);
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler deletecalendarnouserHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mToggleButton.setChecked(true);
            for (int i = 0; i < ResourceCalendarAdapter.dayNumber.length; i++) {
                String[] strarray = ResourceCalendarAdapter.dayNumber[i].split("\\.");
                if (i < ResourceCalendarAdapter.daysOfMonth + ResourceCalendarAdapter.dayOfWeek && i >= ResourceCalendarAdapter.dayOfWeek) {
                    if (strarray[3].equals(clickdate)) {
                        if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 4) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 1);
                        } else if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 5) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 2);
                        } else if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(i) == 3) {
                            ResourceCalendarAdapter.getisSelected_calendar_datetype().put(i, 0);
                        }
                    }
                }
            }
            calV.notifyDataSetChanged();
            BaseMessageUtils.showToast(getResources().getString(R.string.deletecalendarnouser));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mToggleButton.setChecked(false);
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private void resourcecalendarclick() {
        mresourcecalendar_prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPrevMonth(gvFlag);
            }
        });
        mresourcecalendar_nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextMonth(gvFlag);
            }
        });
        mtesid_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupWindow(mtesid_textview,mChooseResourcePwView,mapreslist);
            }
        });
    }
    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        slidingright = true;
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        parameter_year = stepYear;
        parameter_month = stepMonth;
        mToggleButton.setVisibility(View.GONE);
        Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleOtherHandler,res_id, 0,
                "all", Constants.getFirstDayOfMonth(stepYear, stepMonth - 1), Constants.getLastDayOfMonth(stepYear, stepMonth - 1));

    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        slidingright = false;
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        parameter_year = stepYear;
        parameter_month = stepMonth;
        mToggleButton.setVisibility(View.GONE);
        Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleOtherHandler, res_id, 0,
                "all", Constants.getFirstDayOfMonth(stepYear, stepMonth - 1), Constants.getLastDayOfMonth(stepYear, stepMonth - 1));
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }
    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        if (ResourceCalendarAdapter.getisSelected_calendar_datetype().get(position) == 0 ||
                ResourceCalendarAdapter.getisSelected_calendar_datetype().get(position) == 1 ||
                ResourceCalendarAdapter.getisSelected_calendar_datetype().get(position) == 2) {
            mToggleButton.setVisibility(View.VISIBLE);
            mToggleButton.setChecked(true);
        } else {
            mToggleButton.setVisibility(View.VISIBLE);
            mToggleButton.setChecked(false);
        }
        clickdate = str;
        datas = new ArrayList<>();
        resourceCalendarMyListviewAdapter= new ResourceCalendarMyListviewAdapter(Field_ResourceCalendarFragment.this.getActivity(),Field_ResourceCalendarFragment.this,datas,0);
        mresourcecalendarlistview.setVisibility(View.VISIBLE);
        mresourcecalendarlistview.setAdapter(resourceCalendarMyListviewAdapter);
        mresourcecalendarlistview.setFocusable(false);
        if (maplistdata != null && mapreslist.size() > 0 &&
                maplistdata.get(str) != null && maplistdata.get(str) != null) {
            ScheduleCalendarModel scheduleCalendarModel = (ScheduleCalendarModel)maplistdata.get(str);
            if (scheduleCalendarModel.getInfo() != null) {
                if (scheduleCalendarModel.getInfo().size() > 0) {
                    datas.addAll((ArrayList<ScheduleCalendarlistModel>)scheduleCalendarModel.getInfo());
                    if (datas != null && datas.size() >0) {
                        resourceCalendarMyListviewAdapter = new ResourceCalendarMyListviewAdapter(Field_ResourceCalendarFragment.this.getActivity(), Field_ResourceCalendarFragment.this, datas, 0);
                        mresourcecalendarlistview.setAdapter(resourceCalendarMyListviewAdapter);
                        mresourcecalendarlistview.setFocusable(false);
                    }
                }
            }
        }
    }

    public void getconfigurate(int posittion,String datestr) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(posittion,datestr);
        test.getfieldsize_pricenuit(this);
    }

    public void getconfiguratenew(int state,int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(state,position,str);
        test.editorOrderApproved(this);
    }
    private LinhuiAsyncHttpResponseHandler OrderapprovedHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            datas.remove(ResourceCalendarMyListviewAdapter_position);
            resourceCalendarMyListviewAdapter.notifyDataSetChanged();
            Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleOther_new_Handler, res_id, 0,
                    "all", Constants.getFirstDayOfMonth(parameter_year, parameter_month - 1), Constants.getLastDayOfMonth(parameter_year, parameter_month - 1));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if(!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getscheduleOther_new_Handler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.data.toString().length() > 0) {
                if (response.data instanceof JSONArray) {
                    if (JSONArray.parseArray(response.data.toString()).size() > 0) {

                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else if (response.data instanceof JSONObject){
                    JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
//                        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("resList").toString());
//                        if (jsonArray != null) {
//                            if (jsonArray.size() > 0) {
//                                for (int i = 0; i < jsonArray.size(); i++) {
//                                    JSONObject mapjsonObject = jsonArray.getJSONObject(i);
//                                    for (Map.Entry<String, Object> entry : mapjsonObject.entrySet()) {
//                                        String key = entry.getKey();
//                                        String value = entry.getValue().toString();
//                                        resList.add(value);
//                                        mapreslist.put(value,key);
//                                    }
//                                }
//                            }
//                        }
                    JSONArray exp_datejsonArray = JSONArray.parseArray(jsonObject.get("exp_date").toString());
                    if (exp_datejsonArray != null) {
                        if (exp_datejsonArray.size() > 0) {
                            if (muntreateddatelist != null) {
                                muntreateddatelist.clear();
                            }
                            if (munuserdatelist!= null) {
                                munuserdatelist.clear();
                            }
                            for (int i = 0; i > exp_datejsonArray.size(); i++) {
                                JSONObject exp_datejsonObject = exp_datejsonArray.getJSONObject(i);
                                String date = exp_datejsonObject.get("date").toString();
                                if (date != null) {
                                    if (date.length() > 0) {
                                        try {
                                            Date NextDate = chineseDateFormat.parse(date);
                                            munuserdatelist.add(NextDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (jsonObject.get("orderInfo").toString().length() > 0) {
                        if (response.data instanceof JSONArray) {
                            if (JSONArray.parseArray(response.data.toString()).size() > 0 ) {

                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                            }
                        } else if (response.data instanceof JSONObject){
                            JSONObject orderInfojsonObject = JSONObject.parseObject(jsonObject.get("orderInfo").toString());
                            if (maplistdata != null) {
                                maplistdata.clear();
                            }
                            if (muntreateddatelist != null) {
                                muntreateddatelist.clear();
                            }
                            for (Map.Entry<String, Object> entry : orderInfojsonObject.entrySet()) {
                                String key = entry.getKey();
                                ScheduleCalendarModel value = (ScheduleCalendarModel) JSON.parseObject(entry.getValue().toString(), ScheduleCalendarModel.class);
                                try {
                                    Date NextDate = chineseDateFormat.parse(key);
                                    muntreateddatelist.add(NextDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                maplistdata.put(key, value);
                            }
                        }
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else {
                    BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }
            } else {
                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    @Override
    public void postOrderApproved(int state, int position, String str) {
        final String orderid = str;
        ResourceCalendarMyListviewAdapter_position = position;
        showDialog(state,orderid);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        gestureDetector.onTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
//    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            float a = (e1.getX() - e2.getX());
            if (e1.getX() - e2.getX() > 2) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -2) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }
    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = Field_ResourceCalendarFragment.this.getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        Field_ResourceCalendarFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        gridView = new FieldMyGridView(Field_ResourceCalendarFragment.this.getActivity());
        gridView.setBackgroundColor(getResources().getColor(R.color.app_samelinearlayout_bg));
        gridView.setNumColumns(7);
        gridView.setColumnWidth(width / 7);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        gridView.setGravity(Gravity.CENTER);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(2);
        gridView.setHorizontalSpacing(2);
        gridView.setLayoutParams(params);
    }
    private LinhuiAsyncHttpResponseHandler getscheduleHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.data.toString().length() > 0) {
                if (response.data instanceof JSONArray) {
                    if (JSONArray.parseArray(response.data.toString()).size() > 0) {

                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else if (response.data instanceof JSONObject){
                    JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
                    if (jsonObject.get("resList") != null) {
                        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("resList").toString());
                        if (jsonArray != null) {
                            if (jsonArray.size() > 0) {
                                if (mapreslist != null) {
                                    mapreslist.clear();
                                }
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject mapjsonObject = jsonArray.getJSONObject(i);
                                    String key = mapjsonObject.get("id").toString();
                                    String value = mapjsonObject.get("name").toString();
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("id",key);
                                    map.put("name",value);
                                    mapreslist.add(map);
                                }
                                res_id = mapreslist.get(0).get("id");
                                mtesid_textview.setText(mapreslist.get(0).get("name"));
                            }
                        }
                    }
                    if (jsonObject.get("exp_date")!= null) {
                        JSONArray exp_datejsonArray = JSONArray.parseArray(jsonObject.get("exp_date").toString());
                        if (exp_datejsonArray != null) {
                            if (exp_datejsonArray.size() > 0) {
                                if (muntreateddatelist != null) {
                                    muntreateddatelist.clear();
                                }
                                if (munuserdatelist!= null) {
                                    munuserdatelist.clear();
                                }
                                for (int i = 0; i < exp_datejsonArray.size(); i++) {
                                    JSONObject exp_datejsonObject = exp_datejsonArray.getJSONObject(i);
                                    String date = exp_datejsonObject.get("date").toString();
                                    if (date != null) {
                                        if (date.length() > 0) {
                                            try {
                                                Date NextDate = chineseDateFormat.parse(date);
                                                munuserdatelist.add(NextDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (jsonObject.get("orderInfo").toString().length() > 0) {
                        if (response.data instanceof JSONArray) {
                            if (JSONArray.parseArray(response.data.toString()).size() > 0 ) {

                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                            }

                        } else if (response.data instanceof JSONObject){
                            JSONObject orderInfojsonObject = JSONObject.parseObject(jsonObject.get("orderInfo").toString());
                            if (maplistdata != null) {
                                maplistdata.clear();
                            }
                            if (muntreateddatelist != null) {
                                muntreateddatelist.clear();
                            }
                            for (Map.Entry<String, Object> entry : orderInfojsonObject.entrySet()) {
                                String key = entry.getKey();
                                ScheduleCalendarModel value = (ScheduleCalendarModel) JSON.parseObject(entry.getValue().toString(), ScheduleCalendarModel.class);
                                try {
                                    Date NextDate = chineseDateFormat.parse(key);
                                    muntreateddatelist.add(NextDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                maplistdata.put(key,value);
                            }
                        }

                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else {
                    BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }
            } else {
                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            }

            gestureDetector = new GestureDetector(Field_ResourceCalendarFragment.this.getActivity(), new MyGestureListener());
            flipper.removeAllViews();
            ResourceCalendarAdapter.clear_isSelected_calendar();
            ResourceCalendarAdapter.clear_isSelected_calendar_datetype();
            calV = new ResourceCalendarAdapter(Field_ResourceCalendarFragment.this.getActivity(), Field_ResourceCalendarFragment.this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,munuserdatelist,muntreateddatelist,maplistdata,Field_ResourceCalendarFragment.this,0);
            addGridView();
            gridView.setAdapter(calV);
            flipper.addView(gridView, 0);
            addTextToTopTextView(mresourcecalendar_currentMonth);
            mall_no_linearlayout.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getscheduleOtherHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.data.toString().length() > 0) {
                if (response.data instanceof JSONArray) {
                    if (JSONArray.parseArray(response.data.toString()).size() > 0) {

                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else if (response.data instanceof JSONObject){
                    JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
//                        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("resList").toString());
//                        if (jsonArray != null) {
//                            if (jsonArray.size() > 0) {
//                                for (int i = 0; i < jsonArray.size(); i++) {
//                                    JSONObject mapjsonObject = jsonArray.getJSONObject(i);
//                                    for (Map.Entry<String, Object> entry : mapjsonObject.entrySet()) {
//                                        String key = entry.getKey();
//                                        String value = entry.getValue().toString();
//                                        resList.add(value);
//                                        mapreslist.put(value,key);
//                                    }
//                                }
//                            }
//                        }
                    JSONArray exp_datejsonArray = JSONArray.parseArray(jsonObject.get("exp_date").toString());
                    if (exp_datejsonArray != null) {
                        if (exp_datejsonArray.size() > 0) {
                            if (muntreateddatelist != null) {
                                muntreateddatelist.clear();
                            }
                            if (munuserdatelist!= null) {
                                munuserdatelist.clear();
                            }
                            for (int i = 0; i < exp_datejsonArray.size(); i++) {
                                JSONObject exp_datejsonObject = exp_datejsonArray.getJSONObject(i);
                                String date = exp_datejsonObject.get("date").toString();
                                if (date != null) {
                                    if (date.length() > 0) {
                                        try {
                                            Date NextDate = chineseDateFormat.parse(date);
                                            munuserdatelist.add(NextDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (jsonObject.get("orderInfo").toString().length() > 0) {
                        if (response.data instanceof JSONArray) {
                            if (JSONArray.parseArray(response.data.toString()).size() > 0 ) {

                            } else {
                                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                            }
                        } else if (response.data instanceof JSONObject){
                            JSONObject orderInfojsonObject = JSONObject.parseObject(jsonObject.get("orderInfo").toString());
                            if (maplistdata != null) {
                                maplistdata.clear();
                            }
                            if (muntreateddatelist != null) {
                                muntreateddatelist.clear();
                            }
                            for (Map.Entry<String, Object> entry : orderInfojsonObject.entrySet()) {
                                String key = entry.getKey();
                                ScheduleCalendarModel value = (ScheduleCalendarModel) JSON.parseObject(entry.getValue().toString(), ScheduleCalendarModel.class);
                                try {
                                    Date NextDate = chineseDateFormat.parse(key);
                                    muntreateddatelist.add(NextDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                maplistdata.put(key, value);
                            }
                        }
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                    }
                } else {
                    BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }
            } else {
                BaseMessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            }
            int gvFlag = 0;
            ResourceCalendarAdapter.clear_isSelected_calendar();
            ResourceCalendarAdapter.clear_isSelected_calendar_datetype();
            calV = new ResourceCalendarAdapter(Field_ResourceCalendarFragment.this.getActivity(), Field_ResourceCalendarFragment.this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,munuserdatelist,muntreateddatelist,maplistdata,Field_ResourceCalendarFragment.this,0);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(mresourcecalendar_currentMonth); // 移动到上一月后，将当月显示在头标题中
            flipper.addView(gridView, gvFlag);
            if (slidingright == true) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(Field_ResourceCalendarFragment.this.getActivity(), R.anim.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(Field_ResourceCalendarFragment.this.getActivity(), R.anim.push_left_out));
            } else if (slidingright == false) {
                flipper.setInAnimation(AnimationUtils.loadAnimation(Field_ResourceCalendarFragment.this.getActivity(), R.anim.push_right_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(Field_ResourceCalendarFragment.this.getActivity(), R.anim.push_right_out));
            }

            flipper.showPrevious();
            flipper.removeViewAt(0);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private void ShowPopupWindow(final TextView textView,View view, final ArrayList<HashMap<String,String>> list) {
        View myView = Field_ResourceCalendarFragment.this.getActivity().getLayoutInflater().inflate(R.layout.popupwindow_layout, null);
        DisplayMetrics metric = new DisplayMetrics();
        Field_ResourceCalendarFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        //通过view 和宽·高，构造PopopWindow
        pw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        pw.getBackground().setAlpha(155);
        //设置焦点为可点击
        pw.setFocusable(true);//可以试试设为false的结果
        //将window视图显示在myButton下面
        pw.showAsDropDown(view);
        ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw.isShowing()) {
                    pw.dismiss();
                }
            }
        });
        lv.setAdapter(new AreaListViewAdapter(Field_ResourceCalendarFragment.this.getContext(), list, 0,-1));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView nowView = (ImageView) view.findViewById(R.id.area_choose_img);
                nowView.setVisibility(View.VISIBLE);
                textView.setText(list.get(position).get("name"));
                res_id = list.get(position).get("id");
                mToggleButton.setVisibility(View.GONE);
                showProgressDialog();
                mresourcecalendarlistview.setVisibility(View.GONE);
                if (munuserdatelist != null) {
                    munuserdatelist.clear();
                }
                if (muntreateddatelist != null) {
                    muntreateddatelist.clear();
                }
                if (maplistdata != null) {
                    maplistdata.clear();
                }
                Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleHandler,res_id, 0,
                        "all", Constants.getFirstDayOfMonth(parameter_year, parameter_month - 1), Constants.getLastDayOfMonth(parameter_year, parameter_month - 1));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pw.dismiss();
                    }
                }, 200);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data != null) {
                    if (data.getExtras().get("refused")!= null) {
                        if (data.getExtras().getInt("refused") == 1) {
                            datas.remove(ResourceCalendarMyListviewAdapter_position);
                            resourceCalendarMyListviewAdapter.notifyDataSetChanged();
                            showProgressDialog();
                            Field_FieldApi.getresourcecalendar_order_items(MyAsyncHttpClient.MyAsyncHttpClient2(), getscheduleOther_new_Handler, res_id, 0,
                                    "all", Constants.getFirstDayOfMonth(parameter_year, parameter_month - 1), Constants.getLastDayOfMonth(parameter_year, parameter_month - 1));
                        }
                    }
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showDialog(final int type, final String orderid) {//type: 0同意；1：拒绝
        String msg = "";
        if (type == 0) {
            if (datas.get(ResourceCalendarMyListviewAdapter_position).getNumber_of_reserve() > 0) {
                msg = getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_first) +
                        String.valueOf(datas.get(ResourceCalendarMyListviewAdapter_position).getNumber_of_reserve()) +
                        getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_second);
            } else {
                msg = getResources().getString(R.string.moduld_field_order_pproved_order_dialog_msg_third);
            }
        } else if (type == 1) {
            msg = getResources().getString(R.string.moduld_field_order_deny_order_dialog_msg);
        }
        if (mOrderOperationDialog == null || !mOrderOperationDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.btn_perfect) {
                        mOrderOperationDialog.dismiss();
                        if (type == 1) {
                            Intent orderrefused = new Intent(Field_ResourceCalendarFragment.this.getActivity(), Field_OrderRefuseActivity.class);
                            orderrefused.putExtra("approvedid",orderid);
                            startActivityForResult(orderrefused, 1);
                        } else if (type == 0) {
                            showProgressDialog();
                            Field_FieldApi.fieldorderlistitemapproved(MyAsyncHttpClient.MyAsyncHttpClient2(), OrderapprovedHandler, orderid);
                        }
                    } else if (i == R.id.btn_cancel) {
                        mOrderOperationDialog.dismiss();
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(Field_ResourceCalendarFragment.this.getActivity());
            mOrderOperationDialog = builder
                    .cancelTouchout(false)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .addViewOnclick(R.id.btn_perfect,uploadListener)
                    .addViewOnclick(R.id.btn_cancel,uploadListener)
                    .setText(R.id.dialog_title_textview,msg)
                    .setText(R.id.btn_perfect,
                            getResources().getString(R.string.confirm))
                    .setText(R.id.btn_cancel,getResources().getString(R.string.cancel))
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_ResourceCalendarFragment.this.getActivity(),mOrderOperationDialog);
            mOrderOperationDialog.show();
        }
    }

}
