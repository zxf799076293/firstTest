package com.baselib.app.util;


import android.text.TextUtils;

public class NumberUtils {
    static final String TAG = "NumberUtils";

    public static Double parseDouble(String string) {
        return parseDouble(string, null);
    }

    public static Double parseDouble(String string, Double defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }

    public static double parseDoubleValue(String string) {
        return parseDoubleValue(string, 0d);
    }

    public static double parseDoubleValue(String string, double defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }

    public static Long parseLong(String string) {
        return parseLong(string, null);
    }

    public static Long parseLong(String string, Long defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }

    public static long parseLongValue(String string) {
        return parseLongValue(string, 0L);
    }

    public static long parseLongValue(String string, long defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }

    public static Integer parseInteger(String string) {
        return parseInteger(string, null);
    }

    public static Integer parseInteger(String string, Integer defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }

    public static int parseInt(String string) {
        return parseInt(string, 0);
    }

    public static int parseInt(String string, int defaultValue) {
        if (TextUtils.isEmpty(string)) {
            System.out.println("parse number '" + string + "' failed");
            return defaultValue;
        }
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("parse number '" + string + "' failed : " + e.getMessage());
            return defaultValue;
        }
    }
    
    /**
     * 判断double是否为0
     * 
     * @param v
     * @return
     */
    public static boolean doubuleIsZero(double v) {
        return v <= 0.0000001;
    }
}
