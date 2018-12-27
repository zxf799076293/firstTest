package com.baselib.app.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日历工具类
 */
public class CalendarUtil {

    private static  int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化

    public  static void setWeeks(boolean flg)
    {
        if(flg)
        {
            weeks ++;
        }
        else
        {
            weeks--;
        }
    }

    public static String getNowData(int i) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7*weeks+i);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }
    public static String getStringdata ()
    {
        Date curDate = new Date();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String stringdata = myFormatter.format(curDate);
        return stringdata;
    }
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月-dd日");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    public static Date datawork(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    public  static String isYeaterday(String a) throws ParseException {

        Date newTime = new Date();

        Date oldTime = CalendarUtil.datawork(a);
        //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);
        //昨天 86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
            return "昨天";
        } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
            return "今天";
        } else { //至少是前天
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy年MM月dd日");
            String stringdata = myFormatter.format(oldTime);
            return stringdata;
        }
    }


    public static String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 4);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }

    
    public String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    
    public static String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }
    
    public static String getPreviousWeekSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks +4);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }

    
    public static String getPreviousWeekday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }

    
    public static String getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7*weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }

    
    public static String getNextSunday() {

        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 4);
        Date monday = currentDate.getTime();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = myFormatter.format(monday);
        return preMonday;
    }
}