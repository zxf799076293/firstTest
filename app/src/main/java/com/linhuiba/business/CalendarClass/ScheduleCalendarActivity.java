package com.linhuiba.business.CalendarClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.OrderListNewActivity;
import com.linhuiba.business.activity.TrackActivity;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldadapter.SchedulAdapter;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.ScheduleCalendarModel;
import com.linhuiba.business.model.ScheduleCalendarlistModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 日历显示activity
 * 
 * @author Vincent Lee
 * 
 */
public class ScheduleCalendarActivity extends BaseMvpActivity implements View.OnClickListener,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,
		Field_AddFieldChoosePictureCallBack.FieldOrderApproved{
    /** 当前的年月，现在日历顶端 */
    @InjectView(R.id.currentMonth) TextView currentMonth;
    /** 上个月 */
    @InjectView(R.id.prevMonth) ImageButton prevMonth;
    /** 下个月 */
    @InjectView(R.id.nextMonth) ImageButton nextMonth;
    @InjectView(R.id.schedule_listview) MyListView mschedule_listview;
	@InjectView(R.id.all_no_linearlayout) LinearLayout mall_no_linearlayout;
	public GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private ViewFlipper flipper = null;
	private GridView gridView = null;
	private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	/** 每次添加gridview到viewflipper中时给的标记 */
	private int gvFlag = 0;
	private ArrayList<Date> mdatelist = new ArrayList<>();
	private SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private HashMap<String,ScheduleCalendarModel> maplistdata = new HashMap<String,ScheduleCalendarModel>();
	private HashMap<String,ScheduleCalendarModel> maplistdatatmp = new HashMap<>();
	private ArrayList<ScheduleCalendarlistModel> listviewdata = new ArrayList<>();
	private SchedulAdapter schedulAdapter;
	private boolean slidingright;
	private String mServicePhone;
	private CustomDialog mCustomDialog;
	private Handler mDialogHandler = new Handler();
	private final int CALL_PHONE_CODE = 110;
	private String callPhoneStr = "";
	public ScheduleCalendarActivity() {
//		try {
//			Date NextDate = chineseDateFormat.parse("2016-7-6");
//			Date NextDate1 = chineseDateFormat.parse("2016-7-12");
//			Date NextDate2 = chineseDateFormat.parse("2016-8-12");
//			mdatelist.add(NextDate);
//			mdatelist.add(NextDate1);
//			mdatelist.add(NextDate2);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当前日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		ButterKnife.inject(this);
//		mschedule_listview.setEnabled(false);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_schedule_txt));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.myselfinfo_footprint_txt), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (LoginManager.isLogin()) {
					Intent footprint_intent = new Intent(ScheduleCalendarActivity.this, TrackActivity.class);
					startActivity(footprint_intent);
				} else {
					Intent myselfinfo_intent = new Intent(ScheduleCalendarActivity.this, LoginActivity.class);
					startActivityForResult(myselfinfo_intent, 1);
				}
			}
		});
		FieldApi.getschedule_order_items(MyAsyncHttpClient.MyAsyncHttpClient(), getscheduleHandler,
				"all", Constants.getFirstDayOfMonth(year_c, month_c - 1), Constants.getLastDayOfMonth(year_c, month_c - 1));
	}
	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
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

	/**
	 * 移动到下一个月
	 * 
	 * @param gvFlag
	 */
	private void enterNextMonth(int gvFlag) {
		mschedule_listview.setVisibility(View.GONE);
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
		showProgressDialog();
		FieldApi.getschedule_order_items(MyAsyncHttpClient.MyAsyncHttpClient(), getscheduleOtherHandler,
				"all", Constants.getFirstDayOfMonth(stepYear, stepMonth - 1), Constants.getLastDayOfMonth(stepYear, stepMonth - 1));
	}

	/**
	 * 移动到上一个月
	 * 
	 * @param gvFlag
	 */
	private void enterPrevMonth(int gvFlag) {
		mschedule_listview.setVisibility(View.GONE);
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
		showProgressDialog();
		FieldApi.getschedule_order_items(MyAsyncHttpClient.MyAsyncHttpClient(), getscheduleOtherHandler,
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

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
		gridView = new GridView(this);
        gridView.setBackgroundColor(getResources().getColor(R.color.calendar_bg));
		gridView.setNumColumns(7);
		gridView.setColumnWidth(width / 7);
		// gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

		gridView.setGravity(Gravity.CENTER);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 去除gridView边框
		gridView.setVerticalSpacing(0);
		gridView.setHorizontalSpacing(0);
		gridView.setLayoutParams(params);
	}

	private void setListener() {
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextMonth: // 下一个月
			enterNextMonth(gvFlag);
			break;
		case R.id.prevMonth: // 上一个月
			enterPrevMonth(gvFlag);
			break;
		default:
			break;
		}
	}
	@Override
	public void getfieldsize_pricenuit(int position, String str) {
		mschedule_listview.setVisibility(View.GONE);
		if (maplistdata.get(str) != null) {
			if (listviewdata != null) {
				listviewdata.clear();
			}
			listviewdata.addAll((ArrayList<ScheduleCalendarlistModel>)maplistdata.get(str).getInfo());
			if (listviewdata.size() > 0) {
				schedulAdapter = new SchedulAdapter(this,listviewdata,0,this,maplistdata.get(str).getWeather());
				mschedule_listview.setAdapter(schedulAdapter);
				mschedule_listview.setVisibility(View.VISIBLE);
//				mschedule_listview.setEnabled(false);
			} else {

			}
		}
	}
    public void getconfigurate(int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position,str);
        test.getfieldsize_pricenuit(this);
    }
	public void setVirtualNumber(int state,int position,String str) {
		Field_MyAllCallBack test = new Field_MyAllCallBack(state,position,str);
		test.editorOrderApproved(this);
	}
	@Override
	public void postOrderApproved(int state, int position, String str) {
		if (state == -1) {
			if (position > 0 && str != null && str.length() > 0) {
				mServicePhone = str;
				showProgressDialog();
				FieldApi.getVirtualNumber(MyAsyncHttpClient.MyAsyncHttpClient(),
						virtualNumberHandler, String.valueOf(position));
			}
		}

	}
	private LinhuiAsyncHttpResponseHandler getscheduleHandler = new LinhuiAsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
			hideProgressDialog();
			if (mdatelist != null) {
				mdatelist.clear();
			}
			if (maplistdata != null) {
				maplistdata.clear();
			}
			if (response.data.toString().length() > 0) {
				if (response.data instanceof JSONArray) {
					if (!(JSONArray.parseArray(response.data.toString()).size() > 0 )) {
						MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
					}
				} else if (response.data instanceof JSONObject) {
					for (Map.Entry<String, Object> entry : JSONObject.parseObject(response.data.toString()).entrySet()) {
						String key = entry.getKey();
						ScheduleCalendarModel value = (ScheduleCalendarModel)JSON.parseObject(entry.getValue().toString(), ScheduleCalendarModel.class);
						try {
							Date date = chineseDateFormat.parse(key);
							mdatelist.add(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						maplistdata.put(key,value);
					}
				} else {
					MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
				}
			} else {
				MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
			}
			setListener();
			gestureDetector = new GestureDetector(ScheduleCalendarActivity.this, new MyGestureListener());
			flipper = (ViewFlipper) findViewById(R.id.flipper);
			flipper.removeAllViews();
			CalendarAdapter.clear_isSelected_calendar();
			calV = new CalendarAdapter(ScheduleCalendarActivity.this, jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,ScheduleCalendarActivity.this,maplistdata,0);
			addGridView();
			gridView.setAdapter(calV);
			flipper.addView(gridView, 0);
			addTextToTopTextView(currentMonth);
			mall_no_linearlayout.setVisibility(View.GONE);
		}

		@Override
		public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
			hideProgressDialog();
			if (!superresult)
				MessageUtils.showToast(error.getMessage());
			checkAccess(error);
		}
	};
	private LinhuiAsyncHttpResponseHandler getscheduleOtherHandler = new LinhuiAsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
			hideProgressDialog();
			if (mdatelist != null) {
				mdatelist.clear();
			}
			if (maplistdata != null) {
				maplistdata.clear();
			}
			if (response.data.toString().length() > 0) {
				if (response.data instanceof JSONArray) {
					if (JSONArray.parseArray(response.data.toString()).size() > 0 ) {

					} else {
						MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
					}
				} else if (response.data instanceof JSONObject) {
					for (Map.Entry<String, Object> entry : JSONObject.parseObject(response.data.toString()).entrySet()) {
						String key = entry.getKey();
						ScheduleCalendarModel value = (ScheduleCalendarModel)JSON.parseObject(entry.getValue().toString(), ScheduleCalendarModel.class);
						try {
							Date date = chineseDateFormat.parse(key);
							mdatelist.add(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						maplistdata.put(key,value);
					}
				} else {
					MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
				}
			} else {
				MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
			}
			int gvFlag = 0;
			CalendarAdapter.clear_isSelected_calendar();
			calV = new CalendarAdapter(ScheduleCalendarActivity.this, jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,ScheduleCalendarActivity.this,maplistdata,0);
			gridView.setAdapter(calV);
			addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
			gvFlag++;
			flipper.addView(gridView, gvFlag);
			if (slidingright == true) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(ScheduleCalendarActivity.this, R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(ScheduleCalendarActivity.this, R.anim.push_left_out));
			} else if (slidingright == false) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(ScheduleCalendarActivity.this, R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(ScheduleCalendarActivity.this, R.anim.push_right_out));
			}
			flipper.showNext();
			flipper.removeViewAt(0);
		}

		@Override
		public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
			hideProgressDialog();
			if (!superresult)
				MessageUtils.showToast(error.getMessage());
			checkAccess(error);
		}
	};
	private LinhuiAsyncHttpResponseHandler virtualNumberHandler = new LinhuiAsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, final Object data) {
			hideProgressDialog();
			if (mCustomDialog == null || !mCustomDialog.isShowing()) {
				View.OnClickListener uploadListener = new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						switch (view.getId()){
							case R.id.btn_perfect:
								mCustomDialog.dismiss();
								if (data != null && data.toString().length() > 0) {
									callPhoneStr = data.toString();
									AndPermission.with(ScheduleCalendarActivity.this)
											.requestCode(CALL_PHONE_CODE)
											.permission(
													Manifest.permission.CALL_PHONE,
													Manifest.permission.READ_PHONE_STATE)
											.callback(listener)
											.start();
								}

								break;
							case R.id.btn_cancel:
								mCustomDialog.dismiss();
						}
					}
				};
				CustomDialog.Builder builder = new CustomDialog.Builder(ScheduleCalendarActivity.this);
				mCustomDialog = builder
						.cancelTouchout(false)
						.view(R.layout.field_activity_field_orders_success_dialog)
						.addViewOnclick(R.id.btn_cancel,uploadListener)
						.addViewOnclick(R.id.btn_perfect,uploadListener)
						.setText(R.id.dialog_title_textview,
								getResources().getString(R.string.order_call_service_phone_str))
						.setText(R.id.btn_cancel,
								getResources().getString(R.string.cancel))
						.setText(R.id.btn_perfect,
								getResources().getString(R.string.confirm))
						.showView(R.id.linhuiba_logo_imgv,View.GONE)
						.build();
				com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(ScheduleCalendarActivity.this,mCustomDialog);
				mCustomDialog.show();
				mDialogHandler.removeMessages(0);
				mDialogHandler.postDelayed(new Runnable() {
					public void run() {
						if (mCustomDialog.isShowing()) {
							mCustomDialog.dismiss();
						}
					}
				}, 30000);
			}
		}

		@Override
		public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
			hideProgressDialog();
			if (mServicePhone != null && mServicePhone.toString().length() > 0) {
				callPhoneStr = mServicePhone.toString();
				AndPermission.with(ScheduleCalendarActivity.this)
						.requestCode(CALL_PHONE_CODE)
						.permission(
								Manifest.permission.CALL_PHONE,
								Manifest.permission.READ_PHONE_STATE)
						.callback(listener)
						.start();
			}
		}
	};
	private PermissionListener listener = new PermissionListener() {
		@Override
		public void onSucceed(int requestCode, List<String> grantedPermissions) {
			// Successfully.
			if (requestCode == CALL_PHONE_CODE) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ callPhoneStr));
				if (ActivityCompat.checkSelfPermission(ScheduleCalendarActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					return;
				}
				startActivity(intent);
			}
		}

		@Override
		public void onFailed(int requestCode, List<String> deniedPermissions) {
			// Failure.
			if (requestCode == CALL_PHONE_CODE) {
				MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_rationale));
			}
		}
	};

}