package com.linhuiba.business.CalendarClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.connector.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ChooseSpecificationsAdapter  extends BaseAdapter {
    private boolean isLeapyear = false; // 是否为闰年
    public static int daysOfMonth = 0; // 某月的天数
    public static int dayOfWeek = 0; // 具体某一天是星期几
    public static int lastDaysOfMonth = 0; // 上一个月的总天数
    private Context context;
    public static String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
    private SpecialCalendar sc = null;
    private LunarCalendar lc = null;
    private Resources res = null;

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
    private ChooseSpecificationsActivity choosespecifucationsactivity;
    private int type;
    private SimpleDateFormat newsdf=new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<HashMap<String,String>> dimensionslist = new ArrayList<>();
    private static HashMap<Integer, Object> mapitemprice = new HashMap<>();
    private static HashMap<Integer, Object> mapitemweather = new HashMap<>();
    private static HashMap<Integer, Object> mapitemtxt_itembg = new HashMap<>();//0：其他月；1：本月普通；2：周末；3：选中；4：不可用灰色
    private static HashMap<Integer, Object> mapitemclick = new HashMap<>();
    public ChooseSpecificationsAdapter() {
        Date date = new Date();
        sysDate = sdf.format(date); // 当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];

    }
    public ChooseSpecificationsAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c ,ArrayList<Date> datelist,ArrayList<HashMap<String,String>> mdimensionslist,ChooseSpecificationsActivity activity,int type) {
        this();
        this.context = context;
        this.res = rs;
        this.mdatelist = datelist;
        this.jumpMonth = jumpMonth;
        this.jumpYear = jumpYear;
        this.year_c = year_c;
        this.month_c = month_c;
        this.day_c = day_c;
        this.dimensionslist = mdimensionslist;
        this.choosespecifucationsactivity = activity;
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
        String itemworkprice = "";
        String itemweekprice = "";
        String itemotherprice = "";
        if (choosespecifucationsactivity.adaptertermtypeid.length() > 0 && choosespecifucationsactivity.adaptersizeid.length() > 0) {
            if (choosespecifucationsactivity.adaptertermtypeid.equals("-1")) {
                if (choosespecifucationsactivity.termtype_workday == true &&
                        choosespecifucationsactivity.termtype_weekkday == false) {
                    for (int i = 0; i < dimensionslist.size(); i++) {
                        if (dimensionslist.get(i).get("size").equals(choosespecifucationsactivity.adaptersizeid) &&
                                dimensionslist.get(i).get("lease_term_type_id").equals("1") &&
                                dimensionslist.get(i).get("custom_dimension").equals(choosespecifucationsactivity.adaptercustom_dimensionid)) {
                            if (dimensionslist.get(i).get("price") != null && dimensionslist.get(i).get("price").toString().length() > 0) {
                                if (Integer.parseInt(dimensionslist.get(i).get("price").toString()) > 0) {
                                    itemworkprice =choosespecifucationsactivity.getResources().getString(R.string.order_listitem_price_unit_text)
                                            + Constants.getpricestring(dimensionslist.get(i).get("price"),0.01);
                                } else {
                                    itemworkprice = choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose);
                                }
                                break;
                            }
                        }
                    }
                } else if (choosespecifucationsactivity.termtype_weekkday == true &&
                        choosespecifucationsactivity.termtype_workday == false) {
                    for (int i = 0; i < dimensionslist.size(); i++) {
                        if (dimensionslist.get(i).get("size").equals(choosespecifucationsactivity.adaptersizeid) &&
                                dimensionslist.get(i).get("lease_term_type_id").equals("2") &&
                                dimensionslist.get(i).get("custom_dimension").equals(choosespecifucationsactivity.adaptercustom_dimensionid)) {
                            if (Integer.parseInt(dimensionslist.get(i).get("price").toString()) > 0) {
                                itemweekprice =choosespecifucationsactivity.getResources().getString(R.string.order_listitem_price_unit_text)
                                        + Constants.getpricestring(dimensionslist.get(i).get("price"),0.01);
                            } else {
                                itemweekprice = choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose);
                            }
                            break;
                        }
                    }

                } else if (choosespecifucationsactivity.termtype_weekkday == true &&
                        choosespecifucationsactivity.termtype_workday == true) {
                    for (int i = 0; i < dimensionslist.size(); i++) {
                        if (dimensionslist.get(i).get("size").equals(choosespecifucationsactivity.adaptersizeid) &&
                                dimensionslist.get(i).get("lease_term_type_id").equals("1") &&
                                dimensionslist.get(i).get("custom_dimension").equals(choosespecifucationsactivity.adaptercustom_dimensionid)) {
                            if (Integer.parseInt(dimensionslist.get(i).get("price").toString()) > 0) {
                                itemworkprice =choosespecifucationsactivity.getResources().getString(R.string.order_listitem_price_unit_text)
                                        + Constants.getpricestring(dimensionslist.get(i).get("price"),0.01);
                            } else {
                                itemworkprice = choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose);
                            }
                        }
                        if (dimensionslist.get(i).get("size").equals(choosespecifucationsactivity.adaptersizeid) &&
                                dimensionslist.get(i).get("lease_term_type_id").equals("2") &&
                                dimensionslist.get(i).get("custom_dimension").equals(choosespecifucationsactivity.adaptercustom_dimensionid)) {
                            if (Integer.parseInt(dimensionslist.get(i).get("price").toString()) > 0) {
                                itemweekprice =choosespecifucationsactivity.getResources().getString(R.string.order_listitem_price_unit_text)
                                        + Constants.getpricestring(dimensionslist.get(i).get("price"),0.01);
                            } else {
                                itemweekprice = choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose);
                            }
                        }
                    }

                }
            } else {
                for (int i = 0; i < dimensionslist.size(); i++) {
                    if (dimensionslist.get(i).get("size").equals(choosespecifucationsactivity.adaptersizeid) &&
                            dimensionslist.get(i).get("lease_term_type_id").equals(choosespecifucationsactivity.adaptertermtypeid)&&
                            dimensionslist.get(i).get("custom_dimension").equals(choosespecifucationsactivity.adaptercustom_dimensionid)) {
                        if (Integer.parseInt(dimensionslist.get(i).get("price").toString()) > 0) {
                            itemotherprice =choosespecifucationsactivity.getResources().getString(R.string.order_listitem_price_unit_text)
                                    + Constants.getpricestring(dimensionslist.get(i).get("price"),0.01);
                        } else {
                            itemotherprice = choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose);
                        }
                        break;
                    }
                }

            }
        }
        ArrayList<String> weatherlist = new ArrayList<>();
        HashMap<String,String> weathermap = new HashMap<>();
        if (choosespecifucationsactivity.weatherstr != null && choosespecifucationsactivity.weatherstr.length() > 0) {
            MessageUtils.showToast(choosespecifucationsactivity.weatherstr);
//            for (Map.Entry<String, Object> entry : JSONObject.parseObject(choosespecifucationsactivity.weatherstr).entrySet()) {
//                String key = entry.getKey();
//                String value = (String) entry.getValue();
//                weatherlist.add(key);
//                weathermap.put(key,value);
//            }

        }
        getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

        for (int i = 0; i < dayNumber.length; i++) {
            if ( i < daysOfMonth + dayOfWeek && i >= dayOfWeek) {
                String[] strarray=dayNumber[i].split("\\.");
                mapitemprice.put(i,"");
                if (weatherlist.contains(strarray[3])) {
                    if (Constants.date_interval(strarray[3],activity.reserve_days,false)) {
                        mapitemweather.put(i,weathermap.get(strarray[3]));
                        if (choosespecifucationsactivity.deadline_date.length() > 0) {
                            if (!Constants.deadline_date_interval(strarray[3],choosespecifucationsactivity.deadline_date)) {
                                mapitemweather.put(i,"");
                            }
                        }
                    } else {
                        mapitemweather.put(i,"");
                    }
                } else {
                    mapitemweather.put(i,"");
                }
                mapitemtxt_itembg.put(i,0);
                mapitemclick.put(i,false);
                if (choosespecifucationsactivity.adaptertermtypeid.length() > 0 && choosespecifucationsactivity.adaptersizeid.length() > 0) {
                    if (choosespecifucationsactivity.adaptertermtypeid.equals("-1")) {
                        if (Constants.date_interval(strarray[3],activity.reserve_days,false)) {
                            if (choosespecifucationsactivity.termtype_workday == true &&
                                    choosespecifucationsactivity.termtype_weekkday == false) {
                                if (i % 7 == 0 || i % 7 == 6) {
                                    // 当前月周末信息显示
                                    mapitemprice.put(i,"");

                                    mapitemtxt_itembg.put(i,4);
                                    mapitemclick.put(i,false);
                                } else {
                                    if (itemworkprice.length() > 0) {
                                        mapitemprice.put(i,itemworkprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    if (strarray[2].equals("1")) {
                                        mapitemtxt_itembg.put(i,3);
                                    } else {
                                        mapitemtxt_itembg.put(i,1);
                                    }
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                }

                            } else if (choosespecifucationsactivity.termtype_weekkday == true &&
                                    choosespecifucationsactivity.termtype_workday == false) {
                                if (i % 7 == 0 || i % 7 == 6) {
                                    // 当前月周末信息显示
                                    if (itemweekprice.length() > 0) {
                                        mapitemprice.put(i,itemweekprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    if (strarray[2].equals("1")) {
                                        mapitemtxt_itembg.put(i,3);
                                    } else {
                                        mapitemtxt_itembg.put(i,2);
                                    }
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                } else {
                                    mapitemprice.put(i,"");

                                    mapitemtxt_itembg.put(i,4);
                                    mapitemclick.put(i,false);
                                }

                            } else if (choosespecifucationsactivity.termtype_weekkday == true &&
                                    choosespecifucationsactivity.termtype_workday == true) {
                                if (i % 7 == 0 || i % 7 == 6) {
                                    // 当前月周末信息显示
                                    if (itemweekprice.length() > 0) {
                                        mapitemprice.put(i,itemweekprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    if (strarray[2].equals("1")) {
                                        mapitemtxt_itembg.put(i,3);
                                    } else {
                                        mapitemtxt_itembg.put(i,2);
                                    }
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                } else {
                                    if (itemworkprice.length() > 0) {
                                        mapitemprice.put(i,itemworkprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    if (strarray[2].equals("1")) {
                                        mapitemtxt_itembg.put(i,3);
                                    } else {
                                        mapitemtxt_itembg.put(i,1);
                                    }
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                }
                            }
                            if (choosespecifucationsactivity.deadline_date.length() > 0 && choosespecifucationsactivity.activity_start_date.length() > 0) {
                                if (!Constants.isdeadline_date(choosespecifucationsactivity.activity_start_date,choosespecifucationsactivity.deadline_date,strarray[3])) {
                                    mapitemprice.put(i,"");
                                    try {
                                        if (choosespecifucationsactivity.mdatelist.contains(newsdf.parse(strarray[3]) )) {
                                            mapitemtxt_itembg.put(i,3);
                                        } else {
                                            mapitemtxt_itembg.put(i,4);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mapitemclick.put(i,false);
                                }
                            }
                        } else {
                            mapitemprice.put(i,"");

                            mapitemtxt_itembg.put(i,4);
                            mapitemclick.put(i,false);
                        }

                    } else {
                        if (Constants.date_interval(strarray[3],activity.reserve_days,false)) {
                            if (choosespecifucationsactivity.mdatelist.size() > 0) {
                                Date datetmp = null;
                                try {
                                    datetmp = newsdf.parse(strarray[3]);
                                    if (choosespecifucationsactivity.checkedstart_date != null &&
                                            choosespecifucationsactivity.checkedend_date != null &&
                                            Constants.ischoosedate(choosespecifucationsactivity.checkedstart_date,choosespecifucationsactivity.checkedend_date,datetmp)) {
                                        if (!mdatelist.contains(datetmp)) {
                                            mdatelist.add(datetmp);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (choosespecifucationsactivity.mdatelist.contains(datetmp)) {
                                    if (itemotherprice.length() > 0) {
                                        mapitemprice.put(i,itemotherprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    mapitemtxt_itembg.put(i,3);
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                } else {
                                    if (i % 7 == 0 || i % 7 == 6) {
                                        // 当前月周末信息显示
                                        if (itemotherprice.length() > 0) {
                                            mapitemprice.put(i,itemotherprice);
                                        } else {
                                            mapitemprice.put(i,"");
                                        }

                                        mapitemtxt_itembg.put(i,2);
                                        if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                            mapitemclick.put(i,false);
                                            mapitemtxt_itembg.put(i,4);
                                        } else {
                                            mapitemclick.put(i,true);
                                        }
                                    } else {
                                        if (itemotherprice.length() > 0) {
                                            mapitemprice.put(i,itemotherprice);
                                        } else {
                                            mapitemprice.put(i,"");
                                        }

                                        mapitemtxt_itembg.put(i,1);
                                        if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                            mapitemclick.put(i,false);
                                            mapitemtxt_itembg.put(i,4);
                                        } else {
                                            mapitemclick.put(i,true);
                                        }
                                    }
//                                    if (itemotherprice.length() > 0) {
//                                        mapitemprice.put(i,itemotherprice);
//                                    } else {
//                                        mapitemprice.put(i,"");
//                                    }
//
//                                    mapitemtxt_itembg.put(i,4);
//                                    mapitemclick.put(i,false);
                                }

                            } else {
                                if (i % 7 == 0 || i % 7 == 6) {
                                    // 当前月周末信息显示
                                    if (itemotherprice.length() > 0) {
                                        mapitemprice.put(i,itemotherprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    mapitemtxt_itembg.put(i,2);
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                } else {
                                    if (itemotherprice.length() > 0) {
                                        mapitemprice.put(i,itemotherprice);
                                    } else {
                                        mapitemprice.put(i,"");
                                    }

                                    mapitemtxt_itembg.put(i,1);
                                    if (mapitemprice.get(i).equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                                        mapitemclick.put(i,false);
                                        mapitemtxt_itembg.put(i,4);
                                    } else {
                                        mapitemclick.put(i,true);
                                    }
                                }

                            }
//                            if (choosespecifucationsactivity.deadline_date.length() > 0) {
//                                if (!Constants.deadline_date_interval(strarray[3],choosespecifucationsactivity.deadline_date)) {
//                                    mapitemprice.put(i,"");
//                                    mapitemtxt_itembg.put(i,4);
//                                    mapitemclick.put(i,false);
//                                }
//                            }
                            if (choosespecifucationsactivity.deadline_date.length() > 0 && choosespecifucationsactivity.activity_start_date.length() > 0) {
                                if (!Constants.isdeadline_date(choosespecifucationsactivity.activity_start_date,choosespecifucationsactivity.deadline_date,strarray[3])) {
                                    mapitemprice.put(i,"");
                                    try {
                                        if (choosespecifucationsactivity.mdatelist.contains(newsdf.parse(strarray[3]) )) {
                                            mapitemtxt_itembg.put(i,3);
                                        } else {
                                            mapitemtxt_itembg.put(i,4);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mapitemclick.put(i,false);
                                }
                            }

                        } else {
                            mapitemprice.put(i,"");
                            mapitemweather.put(i,"");
                            if (choosespecifucationsactivity.mdatelist.size() > 0) {
                                Date datetmp = null;
                                try {
                                    datetmp = newsdf.parse(strarray[3]);
                                    if (choosespecifucationsactivity.checkedstart_date != null &&
                                            choosespecifucationsactivity.checkedend_date != null &&
                                            Constants.ischoosedate(choosespecifucationsactivity.checkedstart_date,choosespecifucationsactivity.checkedend_date,datetmp)) {
                                        if (!mdatelist.contains(datetmp)) {
                                            mdatelist.add(datetmp);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (choosespecifucationsactivity.mdatelist.contains(datetmp)) {
                                    mapitemtxt_itembg.put(i,3);
                                } else {
                                    mapitemtxt_itembg.put(i,4);
                                }
                            } else {
                                mapitemtxt_itembg.put(i,4);
                            }
                            mapitemclick.put(i,false);
                        }
                    }
                    if (choosespecifucationsactivity.getClosing_dates.contains(strarray[3])) {
                        mapitemprice.put(i,"");
                        mapitemweather.put(i,"");
                        mapitemtxt_itembg.put(i,4);
                        mapitemclick.put(i,false);
                    }

                }
            } else {
                mapitemprice.put(i,"");
                mapitemweather.put(i,"");
                mapitemtxt_itembg.put(i,0);
                mapitemclick.put(i,false);
            }
        }
        this.mdatelist = choosespecifucationsactivity.mdatelist;
        setmapitemprice(mapitemprice);
        setmapitemweather(mapitemweather);
        setmapitemtxt_itembg(mapitemtxt_itembg);
        setmapitemclick(mapitemclick);

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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_choosespecifications_calendaritem, null);
            holder.datetxt = (TextView)convertView.findViewById(R.id.choosepecifications_tvtext);
            holder.mChooseSizeWertherImg = (ImageView) convertView.findViewById(R.id.choosepecifications_weather_img);
            holder.pricetxt = (TextView)convertView.findViewById(R.id.choosepecifications_price_tvtext);
            holder.mchoosepecifications_tvtext_layout = (LinearLayout)convertView.findViewById(R.id.choosepecifications_tvtext_layout);
            holder.mchoosepecifications_tvtext_item_right_layout = (LinearLayout)convertView.findViewById(R.id.choosepecifications_tvtext_item_right_layout);
            holder.mchoosepecifications_tvtext_item_left_layout = (LinearLayout)convertView.findViewById(R.id.choosepecifications_tvtext_item_left_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        DisplayMetrics metric = new DisplayMetrics();
        String[] strarray=dayNumber[position].split("\\.");

        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 当前月信息显示
            holder.datetxt.setText(strarray[0]);
        }
        if (mapitemweather.get(position).toString().length() > 0) {
            holder.mChooseSizeWertherImg.setVisibility(View.VISIBLE);
            holder.mChooseSizeWertherImg.setImageResource(com.linhuiba.linhuifield.connector.Constants.getWeatherImg(Integer.valueOf(mapitemweather.get(position).toString())));
        } else {
            holder.mChooseSizeWertherImg.setVisibility(View.GONE);
        }
        if (mapitemprice.get(position).toString().length() > 0) {
            if (mapitemprice.get(position) != null && mapitemprice.get(position).toString().length() > 0) {
                if (!mapitemprice.get(position).toString().equals(choosespecifucationsactivity.getResources().getString(R.string.choosespecifications_no_choose))) {
                    holder.pricetxt.setText(Constants.getPriceUnitStr(context,mapitemprice.get(position).toString(),7));
                } else {
                    holder.pricetxt.setText(mapitemprice.get(position).toString());
                }
            } else {
                holder.pricetxt.setText("");
            }
            holder.pricetxt.setVisibility(View.VISIBLE);
        } else {
            holder.pricetxt.setVisibility(View.GONE);
        }
        if (Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) == 0) {
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.datetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.pricetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) == 1) {
            holder.pricetxt.setTextColor(context.getResources().getColor(R.color.default_bluebg));
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
            holder.pricetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.datetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) == 2) {
            holder.pricetxt.setTextColor(context.getResources().getColor(R.color.default_bluebg));
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.top_title_center_txt_color));
            holder.pricetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.datetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) == 3) {
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.pricetxt.setTextColor(context.getResources().getColor(R.color.default_bluebg));
            holder.pricetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            if (choosespecifucationsactivity.adaptertermtypeid.equals("-1")) {
                holder.datetxt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_choosespecifications_selected_datebg));
                holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                if (choosespecifucationsactivity.checkedstart_date != null &&
                        choosespecifucationsactivity.checkedend_date != null &&
                        strarray[3].equals(newsdf.format(choosespecifucationsactivity.checkedstart_date))) {
                    holder.datetxt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_choosespecifications_selected_datebg));
                    holder.mchoosepecifications_tvtext_layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_choosespecifications_selected_start_groupdatebg));
                    holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                } else if (strarray[3].equals(newsdf.format(choosespecifucationsactivity.checkedend_date))) {
                    holder.datetxt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_choosespecifications_selected_datebg));
                    holder.mchoosepecifications_tvtext_layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_choosespecifications_selected_end_groupdatebg));
                    holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.datetxt.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                    holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.default_bluebg));
                }
            }
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) == 4) {
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.field_chair_textcolor));
            holder.pricetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.datetxt.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_right_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mchoosepecifications_tvtext_item_left_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (currentFlag == position && Integer.parseInt(String.valueOf(mapitemtxt_itembg.get(position))) != 3) {
            // 设置当天的背景
            holder.datetxt.setTextColor(context.getResources().getColor(R.color.default_bluebg));
            holder.datetxt.setText(context.getResources().getString(R.string.fieldinfo_choose_date_today_tv_str));
            TextPaint tp = holder.datetxt .getPaint();
            tp.setFakeBoldText(true);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if ((boolean)mapitemclick.get(position)) {
                choosespecifucationsactivity.getconfigurate(position);
            }
            }
        });
//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            // 将gridview中的触摸事件回传给gestureDetector
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return choosespecifucationsactivity.gestureDetector.onTouchEvent(event);
//            }
//        });
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
        private ImageView mChooseSizeWertherImg;
        public TextView pricetxt;
        public LinearLayout mchoosepecifications_tvtext_layout;
        private LinearLayout mchoosepecifications_tvtext_item_right_layout;
        private LinearLayout mchoosepecifications_tvtext_item_left_layout;
    }
    public static HashMap<Integer, Object> getmapitemprice() {
        return mapitemprice;
    }

    public static void setmapitemprice(HashMap<Integer, Object> isSelected) {
        ChooseSpecificationsAdapter.mapitemprice = isSelected;
    }
    public static void clear_mapitemprice() {
        if ( mapitemprice != null) {
            mapitemprice.clear();
        }
    }

    public static HashMap<Integer, Object> getmapitemweather() {
        return mapitemweather;
    }

    public static void setmapitemweather(HashMap<Integer, Object> isSelected) {
        ChooseSpecificationsAdapter.mapitemweather = isSelected;
    }
    public static void clear_mapitemweather() {
        if ( mapitemweather != null) {
            mapitemweather.clear();
        }
    }

    public static HashMap<Integer, Object> getmapitemtxt_itembg() {
        return mapitemtxt_itembg;
    }

    public static void setmapitemtxt_itembg(HashMap<Integer, Object> isSelected) {
        ChooseSpecificationsAdapter.mapitemtxt_itembg = isSelected;
    }
    public static void clear_mapitemtxt_itembg() {
        if ( mapitemtxt_itembg != null) {
            mapitemtxt_itembg.clear();
        }
    }

    public static HashMap<Integer, Object> getmapitemclick() {
        return mapitemclick;
    }

    public static void setmapitemclick(HashMap<Integer, Object> isSelected) {
        ChooseSpecificationsAdapter.mapitemclick = isSelected;
    }
    public static void clear_mapitemclick() {
        if ( mapitemclick != null) {
            mapitemclick.clear();
        }
    }
}

