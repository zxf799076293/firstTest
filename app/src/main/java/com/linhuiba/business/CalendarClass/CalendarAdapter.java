package com.linhuiba.business.CalendarClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.model.ScheduleCalendarModel;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author Vincent Lee
 * 
 */
public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private Context context;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
	// private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null;
	private Drawable drawable = null;

	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1; // 用于标记当天
	private int[] schDateTagFlag = null; // 存储当月所有的日程日期

	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	private ArrayList<Date> mdatelist = new ArrayList<>();
    private int  jumpMonth;
    private int  jumpYear;
    private int  year_c;
    private int  month_c;
    private int  day_c;
    private ScheduleCalendarActivity mactivity;
	private int type;
	private static HashMap<Integer, Integer> isSelected_calendar = new HashMap<Integer, Integer>();//value:0其他月/1本月普通背景/2本月周末背景/3选中的本月的背景
	private HashMap<String,ScheduleCalendarModel> maplistdata = new HashMap<>();
	public CalendarAdapter() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];

	}

	public CalendarAdapter(Context context, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c ,ArrayList<Date> datelist,ScheduleCalendarActivity activity,HashMap<String,ScheduleCalendarModel> maplist,int type) {
        this();
        this.context = context;
		this.mdatelist = datelist;
        this.jumpMonth = jumpMonth;
        this.jumpYear = jumpYear;
        this.year_c = year_c;
        this.month_c = month_c;
        this.day_c = day_c;
        this.mactivity = activity;
		this.maplistdata = maplist;
		this.type = type;
        sc = new SpecialCalendar();
        lc = new LunarCalendar();
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

        currentYear = String.valueOf(stepYear); // 得到当前的年份
        currentMonth = String.valueOf(stepMonth); // 得到本月
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
		for (int i = 0; i < dayNumber.length; i++) {
			if ( i < daysOfMonth + dayOfWeek && i >= dayOfWeek) {
				isSelected_calendar.put(i,1);
				if (i % 7 == 0 || i % 7 == 6) {
					// 当前月信息显示
					isSelected_calendar.put(i,2);
				}
			} else {
				isSelected_calendar.put(i,0);
			}
		}
		setisSelected_calendar(isSelected_calendar);
	}

    @Override
	public int getCount() {
		return dayNumber.length;
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
//        firstfunction();
		ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
            holder.datetxt = (TextView)convertView.findViewById(R.id.tvtext);
            holder.imagelayout = (LinearLayout)convertView.findViewById(R.id.imagebutton_linearlayout);
            holder.fieldicon = (ImageButton)convertView.findViewById(R.id.fieldicon);
            holder.activityicon = (ImageButton)convertView.findViewById(R.id.activityicon);
            holder.advertisingicon = (ImageButton)convertView.findViewById(R.id.advertisingicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
		convertView.setBackgroundColor(mactivity.getResources().getColor(R.color.white));
        DisplayMetrics metric = new DisplayMetrics();
		if (type == 0) {
			mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		} else if (type == 1) {

		}

        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        RelativeLayout.LayoutParams imagebtn_params = new RelativeLayout.LayoutParams(
                GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        imagebtn_params.width = width/7;
		String[] strarray=dayNumber[position].split("\\.");

		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			holder.datetxt.setText(strarray[0]);
			if (date_interval(strarray[3])) {
				if (strarray[2] != null) {
					if (strarray[2].equals("1")) {
						if (type == 0) {
							//显示 广告/场地/活动 的标记
							if (maplistdata.get(strarray[3]) != null) {
								if (maplistdata.get(strarray[3]).getActive() > 0) {
									holder.activityicon.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.calendar_date_activitybg));
									holder.activityicon.setVisibility(View.VISIBLE);
								}
								if (maplistdata.get(strarray[3]).getAd() > 0) {
									holder.advertisingicon.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.calendar_date_advertisingbg));
									holder.advertisingicon.setVisibility(View.VISIBLE);
								}
								if (maplistdata.get(strarray[3]).getField() > 0) {
									holder.fieldicon.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.textview_bg));
									holder.fieldicon.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				}
			}
		}
		if (isSelected_calendar.get(position) == 1) {
			holder.datetxt.setTextColor(mactivity.getResources().getColor(R.color.title_bar_txtcolor));
			holder.datetxt.setBackgroundColor(mactivity.getResources().getColor(R.color.white));
		} else if (isSelected_calendar.get(position) == 2) {
			holder.datetxt.setTextColor(mactivity.getResources().getColor(R.color.title_bar_txtcolor));
			holder.datetxt.setBackgroundColor(mactivity.getResources().getColor(R.color.white));
		} else if (isSelected_calendar.get(position) == 3) {
			holder.datetxt.setTextColor(mactivity.getResources().getColor(R.color.white));
			holder.datetxt.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.calendar_date_activitybg));
		} else {
			holder.datetxt.setBackgroundColor(mactivity.getResources().getColor(R.color.white));
		}
		if (currentFlag == position) {
			// 设置当天的背景
			if (isSelected_calendar.get(position) != 3) {
				holder.datetxt.setTextColor(mactivity.getResources().getColor(R.color.default_bluebg));
				TextPaint tp = holder.datetxt .getPaint();
				tp.setFakeBoldText(true);
			}
		}
		setonclick(convertView, holder.datetxt,position);
		convertView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector
            public boolean onTouch(View v, MotionEvent event) {
                return mactivity.gestureDetector.onTouchEvent(event);
            }
        });
		return convertView;
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek + "  =========   " + lastDaysOfMonth);
		getweek(year, month);
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";

		// 得到当前月的所有日程日期(这些日期需要标记)

		for (int i = 0; i < dayNumber.length; i++) {
			// 周一
			// if(i<7){
			// dayNumber[i]=week[i]+"."+" ";
			// }
			if (i < dayOfWeek) { // 前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				lunarDay = lc.getLunarDate(year, month - 1, temp + i, false,mdatelist);
				dayNumber[i] = (temp + i) + "." + lunarDay;

			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
				lunarDay = lc.getLunarDate(year, month, i - dayOfWeek + 1, false,mdatelist);
				dayNumber[i] = i - dayOfWeek + 1 + "." + lunarDay;
				// 对于当前月才去标记当前日期
				if (sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)) {
					// 标记当前日期
					currentFlag = i;
				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(lc.animalsYear(year));
				setLeapMonth(lc.leapMonth == 0 ? "" : String.valueOf(lc.leapMonth));
				setCyclical(lc.cyclical(year));
			} else { // 下一个月
				lunarDay = lc.getLunarDate(year, month + 1, j, false,mdatelist);
				dayNumber[i] = j + "." + lunarDay;
				j++;
			}
		}

		String abc = "";
		for (int i = 0; i < dayNumber.length; i++) {
			abc = abc + dayNumber[i] + ":";
		}
		Log.d("DAYNUMBER", abc);

	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek + 7;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
    static class ViewHolder
    {
        public TextView datetxt;
        public LinearLayout imagelayout;
        public ImageButton fieldicon;
        public ImageButton activityicon;
        public ImageButton advertisingicon;

    }

    private void firstfunction() {
        Date date = new Date();
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
        sc = new SpecialCalendar();
        lc = new LunarCalendar();
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

        currentYear = String.valueOf(stepYear); // 得到当前的年份
        currentMonth = String.valueOf(stepMonth); // 得到本月
        // （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c); // 得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
    }
	private void setonclick(final View view, final TextView textview, final int positions) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (positions < daysOfMonth + dayOfWeek && positions >= dayOfWeek) {
					String[] strarray=dayNumber[positions].split("\\.");
					if (type == 0) {
						for (int i = 0; i < dayNumber.length; i++) {
							if ( i < daysOfMonth + dayOfWeek && i >= dayOfWeek) {
								isSelected_calendar.put(i,1);
								if (i == positions) {
									isSelected_calendar.put(i,3);
								} else {
									if (i % 7 == 0 || i % 7 == 6) {
										// 当前月信息显示
										isSelected_calendar.put(i,2);
									}
								}
							}
						}
						setisSelected_calendar(isSelected_calendar);
						notifyDataSetChanged();
						mactivity.getconfigurate(positions,strarray[3]);
					}

				}
			}
		});
	}
	public static HashMap<Integer, Integer> getisSelected_calendar() {
		return isSelected_calendar;
	}

	public static void setisSelected_calendar(HashMap<Integer, Integer> isSelected) {
		CalendarAdapter.isSelected_calendar = isSelected;
	}
	public static void clear_isSelected_calendar() {
		if (isSelected_calendar != null) {
			isSelected_calendar.clear();
		}
	}
	private boolean date_interval(String datestr) {
		Date itemDate = null;
		Date maxdate = null;
		try {
			Date data = new Date();
			String nowdata = sdf.format(data);
			Date currentdate = sdf.parse(nowdata);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(currentdate);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日期加1天

			Calendar endrightNow = Calendar.getInstance();
			endrightNow.setTime(rightNow.getTime());
			endrightNow.add(Calendar.MONTH, 3);//日期加2个月
			maxdate = endrightNow.getTime();
			itemDate = sdf.parse(datestr);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		//显示各个日期的类型

		if (itemDate.after(maxdate)) {
			return false;
		} else {
			return true;
		}
	}

}
