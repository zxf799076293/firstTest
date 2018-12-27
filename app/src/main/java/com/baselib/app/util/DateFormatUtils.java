package com.baselib.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by snowd on 15/4/5.
 */
public class DateFormatUtils {

    public static String formatUnixTime(long time) {
        return formatTime(time * 1000);
    }

    public static String formatTime(long time) {
        return formatTime(time, "yyyy-M-d HH:mm");
    }

    public static String formatTime(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    public static Date parseTime(String str, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            return f.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNeturalAgeString(int birthTimestamp) {
        Calendar calbirth = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calbirth.setTime(parseTime(formatUnixTime(birthTimestamp), "yyyy-M-d HH:mm"));

        Calendar calnow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calnow.setTimeInMillis(System.currentTimeMillis());

        int[] age = getNeturalAge(calbirth, calnow);
        if (age != null && age.length > 2) {
            return age[0] + "岁" + age[1] + "个月" + age[2] + "天";
        }
        return null;
    }

    public static int[] getNeturalAge(Calendar calendarBirth,Calendar calendarNow) {
        int diffYears = 0, diffMonths, diffDays;
        int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH);
        int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH);
        if (dayOfBirth <= dayOfNow) {
            diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
            diffDays = dayOfNow - dayOfBirth;
            if (diffMonths == 0)
                diffDays++;
        } else {
            if (isEndOfMonth(calendarBirth)) {
                if (isEndOfMonth(calendarNow)) {
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = 0;
                } else {
                    calendarNow.add(Calendar.MONTH, -1);
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = dayOfNow + 1;
                }
            } else {
                if (isEndOfMonth(calendarNow)) {
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
                    diffDays = 0;
                } else {
                    calendarNow.add(Calendar.MONTH, -1);// 上个月
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
// 获取上个月最大的一天
                    int maxDayOfLastMonth = calendarNow         .getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (maxDayOfLastMonth > dayOfBirth) {
                        diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow;
                    } else {
                        diffDays = dayOfNow;
                    }
                }
            }
        }
// 计算月份时，没有考虑年
        diffYears = diffMonths / 12;
        diffMonths = diffMonths % 12;
        return new int[] { diffYears, diffMonths, diffDays };
    }

    /**
     * 获取两个日历的月份之差
     *
     * @param calendarBirth
     * @param calendarNow
     * @return
     */
    public static int getMonthsOfAge(Calendar calendarBirth,
                                     Calendar calendarNow) {
        return (calendarNow.get(Calendar.YEAR) - calendarBirth
                .get(Calendar.YEAR))* 12+ calendarNow.get(Calendar.MONTH)
                - calendarBirth.get(Calendar.MONTH);
    }

    /**
     * 判断这一天是否是月底
     *
     * @param calendar
     * @return
     */
    public static boolean isEndOfMonth(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }

    public static String getZodicaFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int yearoffset = year - 1900;
        int zodica = yearoffset % 12;
        switch (zodica) {
            case 0:
                return "鼠";
            case 1:
                return "牛";
            case 2:
                return "虎";
            case 3:
                return "兔";
            case 4:
                return "龙";
            case 5:
                return "蛇";
            case 6:
                return "马";
            case 7:
                return "羊";
            case 8:
                return "猴";
            case 9:
                return "鸡";
            case 10:
                return "狗";
            case 11:
                return "猪";
        }
        return "鼠";
    }

}
