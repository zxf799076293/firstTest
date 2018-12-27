package com.linhuiba.business.connector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.ActivityCaseActivity;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.CommunityInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.InviteActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.MyCouponsActivity;
import com.linhuiba.business.activity.MyWalletActivity;
import com.linhuiba.business.activity.MyselfInfo_CompanyActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.activity.SearchAdvListActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.activity.SelfSupportShopActivity;
import com.linhuiba.business.activity.TheFirstRegisterCouponsActivity;
import com.linhuiba.business.adapter.WalletApplyPasswordAdapter;
import com.linhuiba.business.fieldmodel.WalletFingerprintPayModel;
import com.linhuiba.business.fieldview.Field_NewGalleryView;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.view.NewGalleryView;
import com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuifield.fragment.Field_HomeFragment;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlManager;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuifield.sqlite.DBManager;
import com.linhuiba.linhuipublic.callbackmodel.ConfigInfo;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Constants {
    static Constants sInstance;
    public String[] mProvinceDatas;
    public String[] mcityDatas;
    public String[] DistrictNames;
    public Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    public Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    public Map<String, String> mCitisDatasidMap = new HashMap<String, String>();
    public Map<Integer, String> mCitisDeliveryFeeMap = new HashMap<Integer, String>();
    public Map<String, String> mDistrictDatasidMap = new HashMap<String, String>();
    public Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    public String[] mProvinceDatas_tmp;
    public String[] mcityDatas_tmp;
    public String[] DistrictNames_tmp;
    public Map<String, String[]> mCitisDatasMap_tmp = new HashMap<String, String[]>();
    public Map<String, String[]> mDistrictDatasMap_tmp = new HashMap<String, String[]>();
    public Map<String, String> mCitisDatasidMap_tmp = new HashMap<String, String>();
    public Map<String, String> mDistrictDatasidMap_tmp = new HashMap<String, String>();
    public Map<String, String> mZipcodeDatasMap_tmp = new HashMap<String, String>();
    public Map<Integer, String> mCitisDeliveryFeeMapTmp = new HashMap<Integer, String>();
    public static Context mcontext;
    public static Activity mactivity;
    //推送判断跳转界面
    private static final int OrderInfoInt = 1;
    private static final int OrdersManageInt = 2;
    private static final int InvoiceInfoInt = 3;
    private static final int WalletsInt = 4;
    private static final int PointsInfoInt = 5;
    private static final int COMMENTS = 6;
    private static final int DEMAND = 7;
    private static final int ORDER_ITEM_INFO = 8;
    private static final int DEMAND_INFO = 9;
    private static final int ENQUIRY_ORDER_INFO = 10;
    private static final int COUPON_INT = 11;
    private static final int PROPERTY_FIELDS = 12;
    private static final int PROPERTY_ACTIVITIES = 13;
    private static final int HELP_WEB = 14;
    private static final int CARTS = 15;
    public static final int RELEASE_PERMISSIONS = 16;
    private static final int FIELD_INFO = 17;//展位详情
    private static final int FIELD_LIST = 18;//场地列表
    private static final int ADV_LIST = 19;//广告列表
    private static final int CASE_LIST = 20;//案例列表
    private static final int SERVICE_LIST = 21;//服务商列表
    private static final int COMMUNITY_INFO = 22;//场地详情
    private static final int ACTIVITY_LIST = 23;//活动列表
    private static final int THEME_INFO = 24;//专题详情 theme
    private static final int WEAL_LIST = 25;//新人礼包 weal

    public static final String picture_file_str = Environment.getExternalStorageDirectory() + "/linhuiba/";
    public static final File picture_file = new File(Environment.getExternalStorageDirectory() + "/linhuiba/");
    public static int newPosition;//预览删除图片的position
    public static int mImageList_size;//预览图片listsize
    public static int bunneritem;//banner自动循环的position
    public static int previousPointEnale;//banner图片的上一次的position
    public static final int PhotoRequestCode = 1000;//相册选择返回值
    public static final int CameraRequestCode = 2;//拍照返回值
    public static final int CutOutPicturesRequestCode = 3;//裁剪图片返回值
    public static final int PermissionRequestCode = 100;//相机等权限返回值
    // 微信APP_ID AppSecret
    public static final String APP_ID = "wx1c6cfbe75b359364";
    public static final String AppSecret = "a64d40a2ff8f2626d342419a1126dc1d";
    public static final String WX_MINI_ID = "gh_aa69bc25d761";
    private static int pixel_width;//图片宽
    private static int pixel_height;//图片高
    private static IUiListener mIUiListener;
    private static String mQQPicUrl;
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    public Constants(Context context) {
        this.mcontext = context;
    }
    public Constants(Context context,
                     String pic_url) {
        this.mcontext = context;
        if (pic_url != null && pic_url.length() > 0) {
            this.mQQPicUrl = pic_url;
        } else {
            this.mQQPicUrl = Config.LINHUIBA_LOGO_URL;
        }

        IUiListener listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };
        this.mIUiListener = listener;
    }



    public Constants(Context context, Activity activity, int newPosition, int mImageList_size) {
        this.mcontext = context;
        this.mactivity = activity;
        this.newPosition = newPosition;
        this.mImageList_size = mImageList_size;
    }

    public Constants(Context context, Activity activity, int newPosition, int mImageList_size, int previousPointEnale, int bunneritem,
                     int pixel_width, int pixel_height) {
        this.mcontext = context;
        this.mactivity = activity;
        this.newPosition = newPosition;
        this.mImageList_size = mImageList_size;
        this.previousPointEnale = previousPointEnale;
        this.bunneritem = bunneritem;
        this.pixel_width = pixel_width;
        this.pixel_height = pixel_height;
    }

    Constants() {
    }

    public Constants(Context mcontext, Activity activity) {
        getInstance().mcontext = mcontext;
        getInstance().mactivity = activity;
    }

    public static Constants getInstance() {
        if (sInstance == null) {
            sInstance = new Constants();
        }
        return sInstance;
    }

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

    //判断是否在后台
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
//				Log.i(context.getPackageName(), "此appimportace ="
//						+ appProcess.importance
//						+ ",context.getClass().getName()="
//						+ context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//					Log.i(context.getPackageName(), "处于后台"
//							+ appProcess.processName);
                    return true;
                } else {
//					Log.i(context.getPackageName(), "处于前台"
//							+ appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //舍整数返回字符串
    public static String get_double_integral(Double price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#");
        price_df.setRoundingMode(RoundingMode.DOWN);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(price * unit));
        return pricestr;
    }

    //舍整数返回字符串
    public static String getfieldinfo_integral(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#");
        price_df.setRoundingMode(RoundingMode.DOWN);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(Double.parseDouble(price) * unit));
        return pricestr;
    }

    //四舍五入整数返回字符串
    public static String getorderdoublepricestring(Double price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(price * unit));
        return pricestr;
    }

    //四舍五入两位小数返回字符串  参数：小数
    public static String getdoublepricestring(Double price, double unit) {
        DecimalFormat price_df = new DecimalFormat("###############0.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(price * unit));
        return pricestr;
    }

    //四舍五入两位小数返回字符串 参数：字符串
    public static String getpricestring(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("###############0.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        double double_minfieldprice = -1;
        double_minfieldprice = Double.parseDouble(price);
        pricestr = subZeroAndDot(price_df.format(double_minfieldprice * unit));
        return pricestr;
    }

    //四舍五入两位小数返回字符串 参数：字符串
    public static String getSearchPriceStr(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("###############0.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        double double_minfieldprice = -1;
        double_minfieldprice = Double.parseDouble(price);
        pricestr = price_df.format(double_minfieldprice * unit);
        return pricestr;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @str
     */
    public static String getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datastr = chineseDateFormat.format(calendar.getTime());
        return datastr;
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @str
     */
    public static String getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datastr = chineseDateFormat.format(calendar.getTime());
        return datastr;
    }

    //选择规格 判断日期是否在选择区间
    public static boolean date_interval(String datestr, int reserve_days, boolean isActivity) {
        Date itemDate = null;
        Date maxdate = null;
        Calendar rightNow = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        try {
            Date data = new Date();
            String nowdata = sdf.format(data);
            Date currentdate = sdf.parse(nowdata);
            rightNow = Calendar.getInstance();
            rightNow.setTime(currentdate);
            rightNow.add(Calendar.DAY_OF_YEAR, reserve_days - 1);//日期减1天
            if (!isActivity) {
                Calendar endrightNow = Calendar.getInstance();
                endrightNow.setTime(rightNow.getTime());
                endrightNow.add(Calendar.DAY_OF_YEAR, 90);//日期加90天
                maxdate = endrightNow.getTime();
            }
            itemDate = sdf.parse(datestr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型
        if (!isActivity) {
            if (itemDate.after(maxdate) || itemDate.before(rightNow.getTime()) || itemDate.equals(rightNow.getTime())) {
                return false;
            } else {
                return true;
            }
        } else {
            if (itemDate.before(rightNow.getTime()) || itemDate.equals(rightNow.getTime())) {
                return false;
            } else {
                return true;
            }
        }
    }

    //选择规格 判断日期是否在截止时间
    public static boolean deadline_date_interval(String datestr, String deadline_date) {
        Date itemDate = null;
        Date maxdate = null;
        Calendar rightNow = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        try {
            Date currentdate = sdf.parse(deadline_date);
            rightNow = Calendar.getInstance();
            rightNow.setTime(currentdate);
            rightNow.add(Calendar.DAY_OF_YEAR, 1);//日期加1天

            Calendar endrightNow = Calendar.getInstance();
            endrightNow.setTime(rightNow.getTime());
//			endrightNow.add(Calendar.MONTH, 2);//日期加2个月
            maxdate = endrightNow.getTime();
            itemDate = sdf.parse(datestr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型

        if (itemDate != null &&
                maxdate != null &&
                itemDate.before(maxdate)) {
            return true;
        } else {
            return false;
        }
    }

    //获取text的宽度
    public static float getTextWidth(Context Context, String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = Context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }

    //活动 判断日期是否在截止时间
    public static boolean activity_overdue(String deadline_date, int advance_day) {
        Date itemDate = null;
        Date maxdate = null;
        Calendar rightNow = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date currentdate = sdf.parse(deadline_date);
            rightNow = Calendar.getInstance();
            rightNow.setTime(currentdate);
            rightNow.add(Calendar.DAY_OF_YEAR, advance_day);//日期减1天

            Calendar endrightNow = Calendar.getInstance();
            endrightNow.setTime(rightNow.getTime());
//			endrightNow.add(Calendar.MONTH, 2);//日期加2个月
            maxdate = endrightNow.getTime();
            itemDate = sdf.parse(sdf.format(new Date()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型
        return itemDate.before(maxdate);
    }

    //选择规格时 周月年 的结束时间
    public static Date getchoose_enddate(Date startdate, int datenum_unit) {
        Date enddate = null;
        Calendar endcalendar = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endcalendar = Calendar.getInstance();
        endcalendar.setTime(startdate);
        endcalendar.add(Calendar.DAY_OF_YEAR, datenum_unit);//日期加1天

        Calendar endrightNow = Calendar.getInstance();
        endrightNow.setTime(endcalendar.getTime());
//			endrightNow.add(Calendar.MONTH, 2);//日期加2个月
        enddate = endrightNow.getTime();
        return enddate;
    }

    /**
     * 选择规格返回活动大于1天起订的data
     * @param startTime 点击的时间相当于开始时间
     * @param endTime 结束日期
     * @param minimum_order_quantity 起订天数
     * @param closing_dates 不能选择的时间
     * @param mWeekLeaseTermPriceMap 周一到周日对应的价格
     * @param mWeekLeaseTermMap 0-6代表周一到七 对应id
     * @return
     */
    public static ArrayList<Date> getChooseActivityDate(String startTime, String endTime,int minimum_order_quantity,
                                       ArrayList<String> closing_dates,HashMap<Integer,String> mWeekLeaseTermPriceMap,
                                                        HashMap<Integer,Integer> mWeekLeaseTermMap) {
        // 返回的日期集合
        ArrayList<Date> days = new ArrayList<Date>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            int size = 0;//需要的日期数
            while (tempStart.before(tempEnd)) {
                int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(dateFormat.format(tempStart.getTime()));
                if (weekInt > -1) {
                    if (mWeekLeaseTermPriceMap.get(
                            mWeekLeaseTermMap.get(weekInt)) != null) {
                        if (Integer.parseInt(mWeekLeaseTermPriceMap.get(
                                mWeekLeaseTermMap.get(weekInt))) > 0) {
                            if (closing_dates != null && closing_dates.size() > 0) {
                                if (!closing_dates.contains(dateFormat.format(tempStart.getTime()))) {
                                    days.add(dateFormat.parse(dateFormat.format(tempStart.getTime())));
                                    size ++;
                                }
                                if (size == minimum_order_quantity) {
                                    break;
                                }
                            } else {
                                days.add(dateFormat.parse(dateFormat.format(tempStart.getTime())));
                                size ++;
                                if (size == minimum_order_quantity) {
                                    break;
                                }
                            }
                        }
                    }
                }
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    //判断是否在选择的时间的范围内
    public static boolean ischoosedate(Date startdate, Date enddate, Date tmpdate) {
        if ((tmpdate.before(enddate) && tmpdate.after(startdate)) || tmpdate.equals(startdate) ||
                tmpdate.equals(enddate)) {
            return true;
        } else {
            return false;
        }
    }

    //判断时间字符串是否在选择范围内
    public static boolean isdeadline_date(String startdatestr, String enddatestr, String tmpdatestr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate = null;
        Date enddate = null;
        Date tmpdate = null;
        boolean isdeadline_date;
        try {
            startdate = sdf.parse(startdatestr);
            enddate = sdf.parse(enddatestr);
            tmpdate = sdf.parse(tmpdatestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tmpdate != null && enddate != null && startdate != null &&
                ((tmpdate.before(enddate) && tmpdate.after(startdate)) || tmpdate.equals(startdate) ||
                tmpdate.equals(enddate))) {
            isdeadline_date = true;
        } else {
            isdeadline_date = false;
        }
        return isdeadline_date;
    }

    //获取配置接口 linhuifield 里也有改的时候要一起
    public void getConfig() {
        LoginManager.readConfig();
        UserApi.getconfigurations(MyAsyncHttpClient.MyAsyncHttpClient4(), ConfigHandler, LoginManager.getversion());
    }

    //获取配置接口回调
    private LinhuiAsyncHttpResponseHandler ConfigHandler = new LinhuiAsyncHttpResponseHandler(ConfigInfo.class) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            LoginManager.setConfig_updatetime();
            if (data != null) {
                final ConfigInfo configInfo = (ConfigInfo) data;
                if (Integer.parseInt(configInfo.getVersion()) > Integer.parseInt(LoginManager.getversion())) {
                    LoginManager.getInstance().setcontext(mcontext);
                    LoginManager.getInstance().updateConfigInfo(configInfo);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ConfigSqlManager.addCityParameter(mcontext,configInfo.getCitylist());
                            Intent intent = new Intent();
                            intent.setAction("ConfigBroadcast");
                            mcontext.sendBroadcast(intent);
                        }
                    }).start();

                } else {
                    if (!LoginManager.getInstance().isIs_insert_config()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ConfigSqlManager.addCityParameter(mcontext,LoginManager.getcitylist());
                            }
                        }).start();
                    }
                }
            } else {
                if (!LoginManager.getInstance().isIs_insert_config()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ConfigSqlManager.addCityParameter(mcontext,LoginManager.getcitylist());
                        }
                    }).start();
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!LoginManager.getInstance().isIs_insert_config()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConfigSqlManager.addCityParameter(mcontext,LoginManager.getcitylist());
                    }
                }).start();
            }
        }
    };

    //获取定位好的城市的客服电话
    public static String getService_Phone(Context context) {
        String servicephone = "";
        List<ConfigCityParameterModel> cities = ConfigSqlOperation.selectSQL(6,0,context);
        if (cities != null &&
                cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (LoginManager.getInstance().getTrackcityid() != null) {
                    if (LoginManager.getInstance().getTrackcityid().equals(String.valueOf(cities.get(i).getCity_id()))) {
                        if (cities.get(i).getService_phone() != null) {
                            servicephone = cities.get(i).getService_phone();
                        }
                    }
                }
            }
        } else {
            ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
            if (citylist != null && citylist.size() > 0) {
                for (int i = 0; i < citylist.size(); i++) {
                    if (LoginManager.getInstance().getTrackcityid() != null) {
                        if (LoginManager.getInstance().getTrackcityid().equals(String.valueOf(citylist.get(i).getCity_id()))) {
                            if (citylist.get(i).getService_phone() != null) {
                                servicephone = citylist.get(i).getService_phone();
                            }
                        }
                    }
                }
            }
        }
        return servicephone;
    }

    //获取定位好的城市
    public static String getDefault_cityid(Context context) {
        String Default_city = "";
        List<ConfigCityParameterModel> configCityParameterModels = ConfigSqlOperation.selectSQL(6,0,context);
        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
            for (int i = 0; i < configCityParameterModels.size(); i++) {
                String city = configCityParameterModels.get(i).getCity();
                String cityid = String.valueOf(configCityParameterModels.get(i).getCity_id());
                String default_city = String.valueOf(configCityParameterModels.get(i).getDefault_city());
                if (default_city.equals("1")) {
                    Default_city = cityid;
                    break;
                }
            }

        }
        return Default_city;
    }
    //根据id获取城市名称
    public static String getCityName(String id,Context context) {
        String cityName = "";
        List<ConfigCityParameterModel> cities = ConfigSqlOperation.selectSQL(6,0,context);
        if (cities != null &&
                cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                String city = cities.get(i).getCity();
                String cityid = String.valueOf(cities.get(i).getCity_id());
                if (cityid.equals(id)) {
                    cityName = city;
                    break;
                }
            }
        } else {
            ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
            if (citylist != null && citylist.size() > 0) {
                for (int i = 0; i < citylist.size(); i++) {
                    String city = citylist.get(i).getCity();
                    String cityid = String.valueOf(citylist.get(i).getCity_id());
                    if (cityid.equals(id)) {
                        cityName = city;
                        break;
                    }
                }
            }
        }
        if (cityName.length() > 0) {
            cityName =  cityName.substring(0,cityName.length() - 1);
        }
        return cityName;
    }
    //根据城市id获取省份id
    public static int getProvinceid(Context context, int city_id) {
        int province_id = 0;
        List<ConfigCityParameterModel> configCityParameterModels = ConfigSqlOperation.selectSQL(6,0,context);
        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
            for (int i = 0; i < configCityParameterModels.size(); i++) {
                int cityid = configCityParameterModels.get(i).getCity_id();
                if (city_id == cityid) {
                    province_id = configCityParameterModels.get(i).getProvince_id();
                    break;
                }
            }
        }
        return province_id;
    }
    //获取收货地址的省市区联动列表
    public void getdistrictconfig(String citylist) {
        if (getInstance().mCitisDatasMap.size() > 0) {
            getInstance().mCitisDatasMap.clear();
        }
        if (getInstance().mDistrictDatasMap.size() > 0) {
            getInstance().mDistrictDatasMap.clear();
        }
        if (getInstance().mDistrictDatasidMap.size() > 0) {
            getInstance().mDistrictDatasidMap.clear();
        }
        if (getInstance().mZipcodeDatasMap.size() > 0) {
            getInstance().mZipcodeDatasMap.clear();
        }
        if (getInstance().mCitisDatasidMap.size() > 0) {
            getInstance().mCitisDatasidMap.clear();
        }
        if (getInstance().mCitisDeliveryFeeMap.size() > 0) {
            getInstance().mCitisDeliveryFeeMap.clear();
        }
        String districtstr = "";
        if (citylist != null && citylist.length() > 0) {
            districtstr = citylist;

        } else {
            districtstr = com.linhuiba.linhuifield.connector.Constants.readAssetsFileString(mactivity,"district").trim();
        }
        if (districtstr != null &&
                districtstr.length() > 0) {
            JSONArray ProvinceArray = JSON.parseArray(districtstr);
            if (ProvinceArray != null && ProvinceArray.size() > 0) {
                getInstance().mProvinceDatas = new String[ProvinceArray.size()];
                for (int i = 0; i < ProvinceArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject Provincejsonobject = ProvinceArray.getJSONObject(i);
                    getInstance().mProvinceDatas[i] = Provincejsonobject.getString("name");
                    com.alibaba.fastjson.JSONArray Cityarray = JSON.parseArray(Provincejsonobject.getString("city"));
                    getInstance().mcityDatas = new String[Cityarray.size()];
                    for (int j = 0; j < Cityarray.size(); j++) {
                        com.alibaba.fastjson.JSONObject Cityjsonobject = Cityarray.getJSONObject(j);
                        getInstance().mcityDatas[j] = Cityjsonobject.getString("name");
                        com.alibaba.fastjson.JSONArray Districtarray = JSON.parseArray(Cityjsonobject.getString("district"));
                        getInstance().DistrictNames = new String[Districtarray.size()];
                        for (int k = 0; k < Districtarray.size(); k++) {
                            com.alibaba.fastjson.JSONObject Districtjsonobject = Districtarray.getJSONObject(k);
                            getInstance().DistrictNames[k] = Districtjsonobject.getString("name");
                            getInstance().mZipcodeDatasMap.put(Districtjsonobject.getString("name"), Districtjsonobject.getString("id"));
                        }
                        getInstance().mDistrictDatasidMap.put(Cityjsonobject.getString("name"), Cityjsonobject.getString("id"));
                        getInstance().mDistrictDatasMap.put(Cityjsonobject.getString("name"), getInstance().DistrictNames);
                        getInstance().mCitisDeliveryFeeMap.put(Cityjsonobject.getInteger("id"), Cityjsonobject.get("delivery_fee").toString());
                    }
                    getInstance().mCitisDatasidMap.put(Provincejsonobject.getString("name"), Provincejsonobject.getString("id"));
                    getInstance().mCitisDatasMap.put(Provincejsonobject.getString("name"), getInstance().mcityDatas);
                }
                getInstance().mProvinceDatas_tmp = getInstance().mProvinceDatas;
                getInstance().mcityDatas_tmp = getInstance().mcityDatas;
                getInstance().DistrictNames_tmp = getInstance().DistrictNames;
                getInstance().mCitisDatasMap_tmp.putAll(getInstance().mCitisDatasMap);
                getInstance().mDistrictDatasMap_tmp.putAll(getInstance().mDistrictDatasMap);
                getInstance().mCitisDatasidMap_tmp.putAll(getInstance().mCitisDatasidMap);
                getInstance().mDistrictDatasidMap_tmp.putAll(getInstance().mDistrictDatasidMap);
                getInstance().mZipcodeDatasMap_tmp.putAll(getInstance().mZipcodeDatasMap);
                getInstance().mCitisDeliveryFeeMapTmp.putAll(getInstance().mCitisDeliveryFeeMap);
            }
        }
    }
    //分享dialog显示
    public static void share_showPopupWindow(final Activity activity, View myView, final Dialog pw, final IWXAPI api,
                                             final String shareurl, final String title, final String description,
                                             final Bitmap ShareBitmap) {
        if (pw == null || !pw.isShowing()) {
            pw.show();
            Window window = pw.getWindow();
            window.setContentView(myView);
            window.setWindowAnimations(R.style.ShareDialogStyle); // 添加动画
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            pw.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            pw.getWindow().setAttributes(lp);
            LinearLayout mshare_weixin = (LinearLayout) myView.findViewById(R.id.share_weixin);
            LinearLayout mshare_penyouquan = (LinearLayout) myView.findViewById(R.id.share_penyouquan);
            LinearLayout shareQQLL = (LinearLayout) myView.findViewById(R.id.share_qq_ll);
            LinearLayout shareQzoneLL = (LinearLayout) myView.findViewById(R.id.share_qzone_ll);
            Button mcancel_share_btn = (Button) myView.findViewById(R.id.cancel_share_btn);
            mshare_weixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!api.isWXAppInstalled()) {
                        MessageUtils.showToast(activity, activity.getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                    wechatShare(0, shareurl, title, description,
                            ShareBitmap, activity, api);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            mshare_penyouquan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!api.isWXAppInstalled()) {
                        MessageUtils.showToast(activity, activity.getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                    wechatShare(1, shareurl, title, description,
                            ShareBitmap, activity, api);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            shareQQLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WXMiniShare(2, shareurl, title, description,
                            ShareBitmap, activity, api, shareurl,ShareBitmap,title);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            shareQzoneLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WXMiniShare(3, shareurl, title, description,
                            ShareBitmap, activity, api, shareurl,ShareBitmap,title);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            mcancel_share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
        }
    }
    public static void shareWXMiniPopupWindow(final Activity activity, View myView, final Dialog pw, final IWXAPI api,
                                              final String shareurl, final String title, final String description,
                                              final Bitmap ShareBitmap, final String sharewxMinShareLinkUrl, final Bitmap miniShareBitmap,
                                              final String pyqTitle) {
        if (pw == null || !pw.isShowing()) {
            pw.show();
            Window window = pw.getWindow();
            window.setContentView(myView);
            window.setWindowAnimations(R.style.ShareDialogStyle); // 添加动画
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            pw.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            pw.getWindow().setAttributes(lp);
            LinearLayout mshare_weixin = (LinearLayout) myView.findViewById(R.id.share_weixin);
            LinearLayout mshare_penyouquan = (LinearLayout) myView.findViewById(R.id.share_penyouquan);
            LinearLayout shareQQLL = (LinearLayout) myView.findViewById(R.id.share_qq_ll);
            LinearLayout shareQzoneLL = (LinearLayout) myView.findViewById(R.id.share_qzone_ll);

            Button mcancel_share_btn = (Button) myView.findViewById(R.id.cancel_share_btn);
            mshare_weixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!api.isWXAppInstalled()) {
                        MessageUtils.showToast(activity, activity.getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                    WXMiniShare(0, shareurl, title, description,
                            ShareBitmap, activity, api, sharewxMinShareLinkUrl,miniShareBitmap,pyqTitle);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            mshare_penyouquan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!api.isWXAppInstalled()) {
                        MessageUtils.showToast(activity, activity.getResources().getString(R.string.commoditypay_weixinapp_toast));
                        return;
                    }
                    WXMiniShare(1, shareurl, title, description,
                            ShareBitmap, activity, api, sharewxMinShareLinkUrl,miniShareBitmap,pyqTitle);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            shareQQLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WXMiniShare(2, shareurl, title, description,
                            ShareBitmap, activity, api, sharewxMinShareLinkUrl,miniShareBitmap,pyqTitle);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            shareQzoneLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WXMiniShare(3, shareurl, title, description,
                            ShareBitmap, activity, api, sharewxMinShareLinkUrl,miniShareBitmap,pyqTitle);
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });

            mcancel_share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
            });
        }
    }

    //分享dialog 页面点击事件
    private static void wechatShare(int flag, String shareurl, String title, String description,
                                    Bitmap ShareBitmap, Activity activity, IWXAPI api) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareurl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (ShareBitmap == null) {
            ShareBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sharelogo);
        }
        msg.setThumbImage(ShareBitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }
    //小程序分享和朋友圈分享
    private static void WXMiniShare(int flag, String shareurl, String title, String description,
                                    Bitmap ShareBitmap, Activity activity, IWXAPI api,
                                    String sharewxMinShareLinkUrl, Bitmap miniShareBitmap, String pyqTitle) {
        if (flag == 0) {
            WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
            miniProgramObj.webpageUrl = shareurl; // 兼容低版本的网页链接
            int miniprogramType;
            if (com.linhuiba.business.config.Config.BASE_API_URL_PHP.equals(com.linhuiba.business.config.Config.BASE_API_URL_PHP_PE)) {
                miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
            } else {
                miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;
            }
            miniProgramObj.miniprogramType = miniprogramType;// 正式版:0，测试版:1，体验版:2
            miniProgramObj.userName = WX_MINI_ID;    // 小程序原始id
            miniProgramObj.path = sharewxMinShareLinkUrl;            //小程序页面路径
            WXMediaMessage miniMsg = new WXMediaMessage(miniProgramObj);
            miniMsg.title = title;                    // 小程序消息title
            miniMsg.description = description;               // 小程序消息desc
            if (ShareBitmap == null) {
                ShareBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_pic_big);
            }
            miniMsg.thumbData = com.linhuiba.linhuifield.connector.Constants.Bitmap2Bytes(ShareBitmap);// 小程序消息封面图片，小于128k
            SendMessageToWX.Req miniReq = new SendMessageToWX.Req();
            miniReq.transaction = buildTransaction("webpage");
            miniReq.message = miniMsg;
            miniReq.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
            api.sendReq(miniReq);
        } else if (flag == 1) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = shareurl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = pyqTitle;
            msg.description = description;
            if (miniShareBitmap == null) {
                miniShareBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sharelogo);
            }
            msg.setThumbImage(miniShareBitmap);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            api.sendReq(req);
        } else if (flag == 2) {//qq
            Tencent  mTencent = Tencent.createInstance(com.linhuiba.linhuipublic.config.Config.TENGXUN_QQ_APPID,mcontext);
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  description);
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,shareurl);// 内容地址
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mQQPicUrl);
            mTencent.shareToQQ(activity, params, mIUiListener);
        } else if (flag == 3) {//qzone
            Tencent  mTencent = Tencent.createInstance(com.linhuiba.linhuipublic.config.Config.TENGXUN_QQ_APPID,mcontext);
            Bundle params = new Bundle();
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, pyqTitle);// 标题
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);// 摘要
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,shareurl);// 内容地址
            ArrayList<String> imgUrlList = new ArrayList<>();
            imgUrlList.add(mQQPicUrl);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);// 图片地址
            // 分享操作要在主线程中完成
            mTencent.shareToQzone(activity, params,mIUiListener);
        }
    }


    //transaction字段用于唯一标识一个请求
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //普通dialog显示
    public static void show_dialog(View myView, final Dialog pw) {
        if (pw == null || !pw.isShowing()) {
            pw.show();
            Window window = pw.getWindow();
            window.setContentView(myView);
//		window.setWindowAnimations(R.style.ShareDialogStyle); // 添加动画
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            pw.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
            pw.getWindow().setAttributes(lp);
        }
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
    //快递单号复制功能
    public void onClickCopy(TextView v, String msg) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(v.getText().toString());
        MessageUtils.showToast(msg);
    }
    //解析url获取所需要的数组
    public static ArrayList<Integer> getUrlArray(String urlstr) {
        ArrayList<Integer> list = new ArrayList<>();
        if (urlstr.indexOf(",") != -1) {
            String[] strArray = urlstr.split(",");
            for (int i = 0; i < strArray.length; i++) {
                list.add(Integer.parseInt(strArray[i]));
            }
        } else {
            list.add(Integer.parseInt(urlstr));
        }
        return list;
    }
    ////解析url获取所需要的key字段
    public static String getnotices_urlstring(String url, String urlstr) {
        String url_division = "-";
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String key = "";
                String value = "";
                try {
                    key = URLDecoder.decode(pair[0], "UTF-8");
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }
                    if (key.equals(urlstr)) {
                        url_division = value;
                        break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url_division;
    }
    //获取详情url 中的id
    public static String getInfoid_url_jsonobject(String url_link) {
        String url = "";
        String resource_id = "";
        String[] urlParts = url_link.split("\\?");
        if (urlParts.length > 0) {
            url = urlParts[0];
        }
        String[] d = url.split("/");
        resource_id = d[d.length - 1];
        return resource_id;
    }

    //获取列表url 中的resource_type
    public static String getresource_type_url_jsonobject(String url_link) {
        String url = "";
        String resource_type = "";
        String[] urlParts = url_link.split("\\?");
        if (urlParts.length > 0) {
            url = urlParts[0];
        }
        if (url.indexOf("fields") != -1) {
            resource_type = "1";
        } else if (url.indexOf("advs") != -1) {
            resource_type = "2";
        } else if (url.indexOf("activities") != -1) {
            resource_type = "3";
        } else {
            resource_type = "0";
        }
        return resource_type;
    }

    //banner url转为json字符串
    public static String getlisturl_jsonobject(String url_link,int type) {//type  1:pc,2:wep
        String url = "";
        String[] urlParts = url_link.split("\\?");
        if (urlParts.length > 1) {
            url = urlParts[1];
        } else {
            return url;
        }
        String jsonobjectstr = "";
//        int sun = 0;// 计算出字符串里有多少个&符号
//        int count = 0;// 计算出字符串里有多少个,符号
        String data = "";
        String attributesJsonStr = "";
//        StringBuffer sb = new StringBuffer();
//        StringBuffer sb2 = new StringBuffer();
//        // 计算出字符串里有多少个&符号
//        for (int i = 0; i < url.length(); i++) {
//            if ("&".equals(url.substring(i, i + 1))) {
//                sun++;
//            }
//        }
        String a[] = url.split("[&]");//以&符号分隔
        boolean isFirstAttributes = false;
        for (int i = 0; i < a.length; i++) {
            String[] p = a[i].split("=");
            if (p.length > 0) {
                if (p[0].equals("city_ids") ||
                        p[0].equals("district_ids") ||
                        p[0].equals("trading_area_ids") ||
                        p[0].equals("subway_station_ids") ||
                        p[0].equals("community_type_ids") ||
                        p[0].equals("label_ids") ||
                        p[0].equals("field_cooperation_type_ids") ||
                        p[0].equals("activity_type_ids") ||
                        p[0].equals("age_level_ids") ||
                        p[0].equals("indoor") ||
                        p[0].equals("facilities") ||
                        p[0].equals("developer_ids") ||
                        p[0].equals("attributes[]")||
                        p[0].equals("attributes")
                        ) {
                    if (i == 0) {
                        if (p.length == 2) {
                            if (p[0].equals("facilities")) {
                                String[] facilities_str = p[1].split(",");
                                String facilities_datastr = "";
                                if (facilities_str.length > 0) {
                                    for (int j = 0; j < facilities_str.length; j++) {
                                        if (j == 0) {
                                            facilities_datastr += "\"" + facilities_str[j] + "\"";
                                        } else {
                                            facilities_datastr += "," + "\"" + facilities_str[j] + "\"";
                                        }
                                    }
                                } else {
                                    facilities_datastr = p[1];
                                }
                                data += "\"" + p[0] + "\"" + ":" + "[" + facilities_datastr + "]";
                            } else if (p[0].equals("attributes[]") && type == 1) {
                                isFirstAttributes = true;
                                if (attributesJsonStr.length() > 0) {
                                    attributesJsonStr = attributesJsonStr + ",";
                                }
                                attributesJsonStr = attributesJsonStr + p[1];
                            } else if (p[0].equals("attributes") && type == 2) {
                                isFirstAttributes = true;
                                if (attributesJsonStr.length() > 0) {
                                    attributesJsonStr = attributesJsonStr + ",";
                                }
                                attributesJsonStr = attributesJsonStr + p[1];
                            } else {
                                data += "\"" + p[0] + "\"" + ":" + "[" + p[1] + "]";
                            }
                        } else if (p.length == 1) {
                            data += "\"" + p[0] + "\"" + ":" + "[" + "" + "]";
                        }
                    } else {
                        if (p.length == 2) {
                            if (p[0].equals("facilities")) {
                                String[] facilities_str = p[1].split(",");
                                String facilities_datastr = "";
                                if (facilities_str.length > 0) {
                                    for (int j = 0; j < facilities_str.length; j++) {
                                        if (j == 0) {
                                            facilities_datastr += "\"" + facilities_str[j] + "\"";
                                        } else {
                                            facilities_datastr += "," + "\"" + facilities_str[j] + "\"";
                                        }
                                    }
                                } else {
                                    facilities_datastr = p[1];
                                }
                                data += "," + "\"" + p[0] + "\"" + ":" + "[" + facilities_datastr + "]";
                            } else if (p[0].equals("attributes[]") && type == 1) {
                                if (attributesJsonStr.length() > 0) {
                                    attributesJsonStr = attributesJsonStr + ",";
                                }
                                attributesJsonStr = attributesJsonStr + p[1];
                            } else if (p[0].equals("attributes") && type == 2) {
                                if (attributesJsonStr.length() > 0) {
                                    attributesJsonStr = attributesJsonStr + ",";
                                }
                                attributesJsonStr = attributesJsonStr + p[1];
                            } else {
                                data += "," + "\"" + p[0] + "\"" + ":" + "[" + p[1] + "]";
                            }
                        } else if (p.length == 1) {
                            data += "," + "\"" + p[0] + "\"" + ":" + "[" + "" + "]";
                        }
                    }
                } else {
                    if (i == 0) {
                        if (p.length == 2) {
                            if (p[0].equals("min_price") ||
                                    p[0].equals("max_price")) {
                                data += "\"" + p[0] + "\"" + ":" + "\"" + getpricestring(p[1],100) + "\"";
                            } else {
                                data += "\"" + p[0] + "\"" + ":" + "\"" + p[1] + "\"";
                            }
                        } else if (p.length == 1) {
                            data += "\"" + p[0] + "\"" + ":" + "\"" + "" + "\"";
                        }
                    } else {
                        if (p.length == 2) {
                            if (p[0].equals("min_price") ||
                                    p[0].equals("max_price")) {
                                data += "," + "\"" + p[0] + "\"" + ":" + "\"" + getpricestring(p[1],100) + "\"";
                            } else {
                                data += "," + "\"" + p[0] + "\"" + ":" + "\"" + p[1] + "\"";
                            }
                        } else if (p.length == 1) {
                            data += "," + "\"" + p[0] + "\"" + ":" + "\"" + "" + "\"";
                        }
                    }
                }
            }
        }
        if (attributesJsonStr.length() > 0) {
            if (type == 1) {
                if (isFirstAttributes) {
                    data = "\"" + "attributes" + "\"" + ":" + "[" + attributesJsonStr + "]" + "," + data;
                } else {
                    data += "," + "\"" + "attributes" + "\"" + ":" + "[" + attributesJsonStr + "]";
                }
            } else if (type == 2) {
                if (isFirstAttributes) {
                    data = "\"" + "attributes" + "\"" + ":" + attributesJsonStr +"," + data;
                } else {
                    data += "," + "\"" + "attributes" + "\"" + ":" + attributesJsonStr;
                }
            }
        }
        //拼接json格式
        jsonobjectstr = "{" + data + "}";
        return jsonobjectstr;
    }

    //判断是否开启指纹支付
    public static boolean is_open_fingerprint() {
        boolean is_open_fingerprint = false;
        ArrayList<WalletFingerprintPayModel> walletFingerprintPaydatalist = new ArrayList<>();
        if (LoginManager.getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
            walletFingerprintPaydatalist = (ArrayList<WalletFingerprintPayModel>) JSONArray.parseArray(LoginManager.getInstance().getFingerprint_validation(), WalletFingerprintPayModel.class);
            boolean cycle_mark = false;
            for (int i = 0; i < walletFingerprintPaydatalist.size(); i++) {
                if (walletFingerprintPaydatalist.get(i).getUid().equals(LoginManager.getUid())) {
                    if (walletFingerprintPaydatalist.get(i).isOpen_fingerprint_pay()) {
                        is_open_fingerprint = true;
                    } else {
                        is_open_fingerprint = false;
                    }
                    cycle_mark = true;
                    break;
                }
            }
            if (!cycle_mark) {
                is_open_fingerprint = false;
            }
        }

        return is_open_fingerprint;
    }

    //获取指纹支付密码
    public static String get_fingerprint_pw() {
        String fingerprint_pw = "";
        ArrayList<WalletFingerprintPayModel> walletFingerprintPaydatalist = new ArrayList<>();
        if (LoginManager.getInstance().getFingerprint_validation() != null && LoginManager.getInstance().getFingerprint_validation().length() > 0) {
            walletFingerprintPaydatalist = (ArrayList<WalletFingerprintPayModel>) JSONArray.parseArray(LoginManager.getInstance().getFingerprint_validation(), WalletFingerprintPayModel.class);
            boolean cycle_mark = false;
            for (int i = 0; i < walletFingerprintPaydatalist.size(); i++) {
                if (walletFingerprintPaydatalist.get(i).getUid().equals(LoginManager.getUid())) {
                    if (walletFingerprintPaydatalist.get(i).isOpen_fingerprint_pay()) {
                        if (walletFingerprintPaydatalist.get(i).getWallet_pw() != null &&
                                walletFingerprintPaydatalist.get(i).getWallet_pw().length() == 6) {
                            fingerprint_pw = walletFingerprintPaydatalist.get(i).getWallet_pw();
                        } else {
                            fingerprint_pw = "";
                        }
                    } else {
                        fingerprint_pw = "";
                    }
                    cycle_mark = true;
                    break;
                }
            }
            if (!cycle_mark) {
                fingerprint_pw = "";
            }
        }
        return fingerprint_pw;
    }

    //转为钱数字格式
    public static String addComma(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#,###.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        DecimalFormat decimalprice_df = new DecimalFormat("###############0.00");
        decimalprice_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        double double_minfieldprice = -1;
        double_minfieldprice = Double.parseDouble(price);
        if (double_minfieldprice * unit < 1) {
            pricestr = subZeroAndDot(decimalprice_df.format(double_minfieldprice * unit));
        } else {
            pricestr = price_df.format(double_minfieldprice * unit);
        }

        return pricestr;
    }

    //绑定推送设备接口
    public void binding_devices() {
        if (LoginManager.isLogin()) {
            if (LoginManager.getInstance().getDevice_token() != null &&
                    LoginManager.getInstance().getDevice_token().length() > 0) {
                UserApi.binding_devices(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),
                        devicesHandler, LoginManager.getUid(), LoginManager.getInstance().getDevice_token());
            }
        }
    }

    private LinhuiAsyncHttpResponseHandler devicesHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
//            Log.i("友盟上传token", LoginManager.getInstance().getDevice_token());
        }
        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    //string中颜色和大小不同的设置
    public static SpannableString getSpannableAllStr(Context context, String pricestr, int textsize, int SizeStart, int SizeEnd,
                                                     boolean StrikeThrough, int StrikeThroughStart, int StrikeThroughEnd,
                                                     Integer OtherTvSize, int OtherSizeStart, int OtherSizeEnd,
                                                     boolean blod, int BlodStart, int BlodEnd) {
        SpannableString String = null;
        if (pricestr.indexOf(".00") != -1) {
            String[] strArray= pricestr.split("[.]");
            String = new SpannableString(strArray[0]);
            if (blod) {
                String.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), BlodStart, strArray[0].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (StrikeThrough) {
                String.setSpan(new StrikethroughSpan(), StrikeThroughStart, StrikeThroughEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (textsize > 0) {
                String.setSpan(new AbsoluteSizeSpan(com.linhuiba.linhuifield.connector.Constants.Dp2Px(context, textsize)), SizeStart, SizeEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        } else {
            String = new SpannableString(pricestr);
            if (textsize > 0) {
                String.setSpan(new AbsoluteSizeSpan(com.linhuiba.linhuifield.connector.Constants.Dp2Px(context, textsize)), SizeStart, SizeEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (OtherTvSize != null && OtherTvSize > 0) {
                String.setSpan(new AbsoluteSizeSpan(com.linhuiba.linhuifield.connector.Constants.Dp2Px(context, textsize)), OtherSizeStart, OtherSizeEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (blod) {
                String.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), BlodStart, BlodEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (StrikeThrough) {
                String.setSpan(new StrikethroughSpan(), StrikeThroughStart, StrikeThroughEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

        }
        return String;
    }

    //string中将钱的单位变小
    public static SpannableString getPriceUnitStr(Context context, String pricestr, int textsize) {
        SpannableString msp = new SpannableString(pricestr);
        msp.setSpan(new AbsoluteSizeSpan(com.linhuiba.linhuifield.connector.Constants.Dp2Px(context, textsize)), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return msp;
    }
    public static SpannableStringBuilder getDifferentColorStr(String str, int start,int end,int color) {
        SpannableStringBuilder mSpannable = new SpannableStringBuilder(str);
        mSpannable.setSpan(new ForegroundColorSpan(color),
                start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return mSpannable;
    }

    //推送列表url解析
    public static int getpush_msg_type(String url_link) {
        int banner_intetnt = -1;//1 列表；2 详情；3web
        String url = url_link;
        if (url != null && url.length() > 0) {
            if (url.indexOf("admin/orders/view") != -1) {
                banner_intetnt = OrderInfoInt;
            } else if (url.indexOf("admin/orders/manage") != -1) {
                banner_intetnt = OrdersManageInt;
            } else if (url.indexOf("admin/invoices/view") != -1) {
                banner_intetnt = InvoiceInfoInt;
            } else if (url.indexOf("admin/wallets/index") != -1) {
                banner_intetnt = WalletsInt;
            } else if (url.indexOf("admin/points/index") != -1) {
                banner_intetnt = PointsInfoInt;
            } else if (url.indexOf("admin/comments/edit") != -1) {
                banner_intetnt = COMMENTS;
            } else if (url.indexOf("app_page/appeal") != -1) {
                banner_intetnt = DEMAND;
            } else if (url.indexOf("admin/orders/order-items") != -1) {
                banner_intetnt = ORDER_ITEM_INFO;
            } else if (url.indexOf("admin/requirements/details") != -1) {
                banner_intetnt = DEMAND_INFO;
            } else if (url.indexOf("admin/enquiry/view") != -1) {
                banner_intetnt = ENQUIRY_ORDER_INFO;
            } else if (url.indexOf("admin/coupon/index") != -1) {
                banner_intetnt = COUPON_INT;
            } else if (url.indexOf("admin/fields/index") != -1) {
                banner_intetnt = PROPERTY_FIELDS;
            } else if (url.indexOf("admin/activities/index") != -1) {
                banner_intetnt = PROPERTY_ACTIVITIES;
            } else if (url.indexOf("company/help") != -1) {
                banner_intetnt = HELP_WEB;
            } else if (url.indexOf("cart/index") != -1) {
                banner_intetnt = CARTS;
            } else if (url.indexOf("admin/fields/edit") != -1) {
                banner_intetnt = RELEASE_PERMISSIONS;
            } else if ((url.indexOf("fields/list") != -1 || url.indexOf("fieldslist") != -1 ||
                    url.indexOf("advs/index") != -1)) {
                if (url.indexOf("advs/index") != -1) {
                    banner_intetnt = ADV_LIST;//广告列表
                } else {
                    banner_intetnt = FIELD_LIST;//场地列表
                }
            } else if (url.indexOf("fields") != -1 || url.indexOf("advs/view") != -1 ||
                    url.indexOf("activities") != -1) {
                banner_intetnt = FIELD_INFO;//展位详情 / 供给详情
            } else if (url.indexOf("theme/case") != -1) {
                banner_intetnt = CASE_LIST;//案例列表
            } else if (url.indexOf("service/index") != -1) {
                banner_intetnt = SERVICE_LIST;//服务商
            } else if (url.indexOf("community") != -1) {
                banner_intetnt = COMMUNITY_INFO;//场地详情
            } else if (url.indexOf("activities/proprietary") != -1) {
                banner_intetnt = ACTIVITY_LIST;//活动列表
            } else if (url.indexOf("theme") != -1) {
                banner_intetnt = THEME_INFO;//专题详情
            } else if (url.indexOf("company/weal") != -1) {
                banner_intetnt = WEAL_LIST;//新人礼包
            }
        }
        return banner_intetnt;
    }

    //获取详情url 中的id
    public static String getpush_msg_id(String url_link, String delimiter) {
        String url = url_link;
        String resource_id = "";
        String[] d = url.split(delimiter);
        resource_id = d[d.length - 1];
        return resource_id;
    }

    //关闭软键盘
    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void delete_picture_file() {
        if (!picture_file.exists()) {
            try {
                //按照指定的路径创建文件夹
                picture_file.mkdirs();
            } catch (Exception e) {

            }
        }
        try {
            //按照指定的路径创建文件夹
            File files[] = picture_file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.exists()) {
                        f.delete();
                    } else {

                    }
                }
            }
        } catch (Exception e) {

        }

    }

    //轮播图显示
    public static void showbanner(final Field_NewGalleryView mCoverFlow, final ViewGroup pointsGroup, final ArrayList<Integer> galleryviewlist, final TextView textView) {
        DisplayMetrics metric = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mCoverFlow.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mCoverFlow.setLayoutParams(linearParams);
        mImageList_size = galleryviewlist.size();
        final BannerImageAdapter imageAdapter = new BannerImageAdapter(mcontext, galleryviewlist, pointsGroup);
        mCoverFlow.setAdapter(imageAdapter);//自定义图片的填充方式
        if (mImageList_size > 1) {
            mCoverFlow.setSelection(0);
        }
        mCoverFlow.setAnimationDuration(1500);
        mCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newPosition = position % mImageList_size;
                bunneritem = position;
                if (position == mImageList_size - 1) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }

//				// 消除上一次的状态点
//				pointsGroup.getChildAt(previousPointEnale).setEnabled(false);
//				// 设置当前的状态点“点”
//				pointsGroup.getChildAt(newPosition).setEnabled(true);
                // 记录位置
                previousPointEnale = newPosition;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static class BannerImageAdapter extends BaseAdapter {
        private ArrayList<ImageView> mImages = new ArrayList<ImageView>();        // 保存倒影图片的数组
        private Context mContext;
        private ArrayList<Integer> galleryviewlist;
        private ViewGroup pointsGroup;

        public BannerImageAdapter(Context c, ArrayList<Integer> galleryviewlist, ViewGroup pointsGroup) {
            this.mContext = c;
            this.galleryviewlist = galleryviewlist;
            this.pointsGroup = pointsGroup;
            if (galleryviewlist != null && galleryviewlist.size() > 0) {
                createReflectedImages();
            } else {
                ImageView imageView = new ImageView(mContext);
                DisplayMetrics metric = new DisplayMetrics();
                mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width_new = metric.widthPixels;     // 屏幕宽度（像素）
                int height_new = metric.heightPixels;
                imageView.setLayoutParams(new NewGalleryView.LayoutParams((int) (width_new),
                        (int) (height_new)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Picasso.with(mactivity).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(imageView);
                mImages.add(imageView);
            }

        }

        /**
         * 反射倒影
         */
        public boolean createReflectedImages() {
            int index = 0;
//			pointsGroup.removeAllViews();
            for (int i = 0; i < galleryviewlist.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                DisplayMetrics metric = new DisplayMetrics();
                mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width_new = metric.widthPixels;     // 屏幕宽度（像素）
                int height_new = metric.heightPixels;
                imageView.setLayoutParams(new NewGalleryView.LayoutParams((int) (width_new),
                        (int) (width_new * pixel_height / pixel_width)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                if (galleryviewlist.get(index).toString().length() > 0) {
                    Picasso.with(mactivity).load(galleryviewlist.get(index++)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, width_new * pixel_height / pixel_width).centerCrop().into(imageView);
                } else {
                    Picasso.with(mactivity).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(imageView);
                    index++;
                }
                mImages.add(imageView);
                final int finalIndex = index;
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),mPicList.get((finalIndex -1)),Toast.LENGTH_LONG).show();
//                    }
//                });
                ImageView pointimageView = new ImageView(mContext);
                pointimageView = new ImageView(mcontext);
                pointimageView.setBackgroundResource(R.drawable.point_background);
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setMargins(15, 0, 0, 0);
                pointimageView.setLayoutParams(layout);
                pointimageView.setEnabled(false);
//				pointsGroup.addView(pointimageView);
            }

            return true;
        }

        @Override
        public int getCount() {
            if (mImages.size() > 1) {
                return mImages.size();
            } else {
                return mImages.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mImages.isEmpty()) {
                return null;
            } else {
                return mImages.get(position);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mImages.isEmpty()) {
                return null;
            } else {
                if (mImages.size() > 1) {
                    return mImages.get(position % mImages.size());
                } else {
                    return mImages.get(position);
                }
            }
        }

        public float getScale(boolean focused, int offset) {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }
    }

    //判断SDCard存在并且可以读写
    public static boolean SDCardState() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {//表示SDCard存在并且可以读写
            return true;
        } else {
            return false;
        }
    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName 应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    //判断两个日期（字符串）相差多少天
    public static int two_date_discrepancy(String start_datestr, String finish_datestr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal_start = Calendar.getInstance();
        Calendar cal_finish = Calendar.getInstance();
        try {
            cal_start.setTime(sdf.parse(start_datestr));
            cal_finish.setTime(sdf.parse(finish_datestr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = cal_start.getTimeInMillis();
        long time2 = cal_finish.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }
    /**
     * 输入小数时的监听只能有两位小数
     * @param editText
     */
    public static void textchangelistener(final EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }

            }
        };
        editText.addTextChangedListener(textWatcher);
    }
    public static void addTextSpace(final EditText accountET) {
        accountET.addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    accountET.setText(str1);
                    accountET.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * 登录注册用户名输入判断
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String registerStringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字 下划线
//        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        String   regEx  =  "[^a-zA-Z0-9_]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    /**
     * 用户名输入框判断规则
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String userNameStringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字 下划线 汉字
//        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        String   regEx  =  "[^a-zA-Z0-9_\u4E00-\u9FA5]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    /**
     * 判断是否是正常点击不是快速点击
     * @return true
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    /**
     *  推送url跳转
     * @param data url 链接
     * @param context
     * @param isApplicationContext 是否是Application
     */
    public static void pushUrlJumpActivity(String data,Context context,boolean isApplicationContext) {
        if (data != null && data.length() > 0) {
            if (getpush_msg_type(data) == OrderInfoInt) {
                String id = getpush_msg_id(data,"/");
                if (id != null && id.length() > 0) {
                    //2017/10/14 跳转到商家的订单详情（加载web）
                    Intent orderInfoIntent = new Intent(context, AboutUsActivity.class);
                    orderInfoIntent.putExtra("id", id);
                    orderInfoIntent.putExtra("type", com.linhuiba.business.config.Config.BIG_ORDER_INFO_INT);
                    if (isApplicationContext) {
                        orderInfoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(orderInfoIntent);
                }
            } else if (getpush_msg_type(data) == OrdersManageInt) {
                String status = getnotices_urlstring(data,"status");
                int OrderFragment_currIndex = -1;
                if ( status!= null && status.length() > 0) {
                    if(status.equals("paid")) {
                        OrderFragment_currIndex = 0;
                    } else if(status.equals("approved")) {
                        OrderFragment_currIndex = 1;
                    } else if(status.equals("finished")) {
                        OrderFragment_currIndex = 2;
                    } else if(status.equals("reviewed")) {
                        OrderFragment_currIndex = 3;
                    } else if(status.equals("canceled")) {
                        OrderFragment_currIndex = 4;
                    }
                    Intent fieldorderlist = new Intent(context, FieldMainTabActivity.class);
                    if (isApplicationContext) {
                        if (com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(context,"com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity")) {
                            fieldorderlist.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }
                        fieldorderlist.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    fieldorderlist.putExtra("new_tmpintent", "fieldorder");
                    fieldorderlist.putExtra("OrderFragment_currIndex", OrderFragment_currIndex);
                    context.startActivity(fieldorderlist);
                }
            } else if (getpush_msg_type(data) == InvoiceInfoInt) {
                String id = getpush_msg_id(data,"/");
                if (id != null && id.length() > 0) {
                    Intent invoiceinfo = new Intent(context,AboutUsActivity.class);
                    invoiceinfo.putExtra("type", com.linhuiba.business.config.Config.INVOICE_INFO_WEB_INT);
                    invoiceinfo.putExtra("id",id);
                    if (isApplicationContext) {
                        invoiceinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(invoiceinfo);
                }

            } else if (getpush_msg_type(data) == WalletsInt) {
                Intent walletapply_intent = new Intent(context, MyWalletActivity.class);
                if (isApplicationContext) {
                    if (com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(context,"com.linhuiba.business.activity.MyWalletActivity")) {
                        walletapply_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    walletapply_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(walletapply_intent);
            } else if (getpush_msg_type(data) == PointsInfoInt) {
                Intent about_intrgral = new Intent(context,AboutUsActivity.class);
                about_intrgral.putExtra("type", com.linhuiba.business.config.Config.POINT_INFO_WEB_INT);
                if (isApplicationContext) {
                    about_intrgral.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(about_intrgral);
            } else if (getpush_msg_type(data) == COMMENTS) {
                String id = getpush_msg_id(data,"/");
                if (id != null && id.length() > 0) {
                    Intent publidhreview = new Intent(context, PublishReviewActivity.class);
                    if (isApplicationContext) {
                        if (com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(context,"com.linhuiba.business.activity.PublishReviewActivity")) {
                            publidhreview.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }
                        publidhreview.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    publidhreview.putExtra("orderid", id);
                    context.startActivity(publidhreview);
                }
            } else if(getpush_msg_type(data) == ORDER_ITEM_INFO) {
                String id = getpush_msg_id(data,"/");
                if (id != null && id.length() > 0) {
                    Intent orderinfo = new Intent(context, AboutUsActivity.class);
                    orderinfo.putExtra("type", com.linhuiba.business.config.Config.ORDER_INFO_INT);
                    orderinfo.putExtra("id",id);
                    if (isApplicationContext) {
                        orderinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(orderinfo);
                }
            } else if(getpush_msg_type(data) == DEMAND_INFO) {
                String id = getpush_msg_id(data,"/");
                if (id != null && id.length() > 0) {
                    Intent orderinfo = new Intent(context, AboutUsActivity.class);
                    orderinfo.putExtra("type", com.linhuiba.business.config.Config.DEMAND_INFO_WEB_INT);
                    orderinfo.putExtra("id",id);
                    if (isApplicationContext) {
                        orderinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(orderinfo);
                }
            } else if(getpush_msg_type(data) == ENQUIRY_ORDER_INFO) {
                String enquiryId = getpush_msg_id(data, "/");
                if (enquiryId != null && enquiryId.length() > 0) {
                    Intent enquiryIntent = new Intent(context, AboutUsActivity.class);
                    enquiryIntent.putExtra("type", com.linhuiba.business.config.Config.ENQUIRY_INFO_WEB_INT);
                    enquiryIntent.putExtra("id", enquiryId);
                    if (isApplicationContext) {
                        enquiryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(enquiryIntent);
                }
            } else if (getpush_msg_type(data) == COUPON_INT) {
                Intent enquiryIntent = new Intent(context, MyCouponsActivity.class);
                if (isApplicationContext) {
                    enquiryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(enquiryIntent);
            } else if (getpush_msg_type(data) == PROPERTY_FIELDS) {
                String status = getnotices_urlstring(data,"status");
                Intent field_intent = new Intent(context,Field_FieldListActivity.class);
                field_intent.putExtra("res_type_id",1);
                if ( status!= null && status.length() > 0) {
                    field_intent.putExtra("status",status);
                }
                if (isApplicationContext) {
                    if (com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(context,"com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity")) {
                        field_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    field_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(field_intent);
            } else if (getpush_msg_type(data) == PROPERTY_ACTIVITIES) {
                String status = getnotices_urlstring(data,"status");
                Intent field_intent = new Intent(context,Field_FieldListActivity.class);
                field_intent.putExtra("res_type_id",3);
                if ( status!= null && status.length() > 0) {
                    field_intent.putExtra("status",status);
                }
                if (isApplicationContext) {
                    if (com.linhuiba.linhuifield.connector.Constants.IsCurrentActivity(context,"com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity")) {
                        field_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                    field_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(field_intent);
            } else if (getpush_msg_type(data) == HELP_WEB) {
                Intent help = new Intent(context, AboutUsActivity.class);
                help.putExtra("type", com.linhuiba.business.config.Config.HELP_WEB_INT);
                if (isApplicationContext) {
                    help.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(help);
            } else if (getpush_msg_type(data) == CARTS) {
                Intent cartsIntent = new Intent(context, MainTabActivity.class);
                cartsIntent.putExtra("new_tmpintent", "goto_cartitems");
                if (isApplicationContext) {
                    cartsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(cartsIntent);
            } if (getpush_msg_type(data) == FIELD_LIST) {
                String link_jsonobject = getlisturl_jsonobject(data,1);
                ApiResourcesModel apiResourcesModel = (ApiResourcesModel) JSONObject.parseObject(link_jsonobject,ApiResourcesModel.class);
                if (apiResourcesModel.getCity_ids() != null &&
                        apiResourcesModel.getCity_ids().size() > 0) {
                    ArrayList<Integer> cityIds = new ArrayList<>();
                    if (apiResourcesModel.getCity_ids().get(0) == 0) {
                        cityIds.add(Integer.parseInt(LoginManager.getTrackcityid()));
                    } else {
                        cityIds.add(apiResourcesModel.getCity_ids().get(0));
                    }
                    apiResourcesModel.setCity_ids(cityIds);
                } else {
                    ArrayList<Integer> cityList = new ArrayList();
                    cityList.add(Integer.parseInt(LoginManager.getTrackcityid()));
                    apiResourcesModel.setCity_ids(cityList);
                }
                Intent searchintent = new Intent(context, SearchListActivity.class);
                if (isApplicationContext) {
                    searchintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                searchintent.putExtra("is_home_page",4);
                searchintent.putExtra("ApiResourcesModel",(Serializable)apiResourcesModel);
                context.startActivity(searchintent);
            } else if (getpush_msg_type(data) == FIELD_INFO) {
                String resourcetype = getresource_type_url_jsonobject(data);
                if (Integer.parseInt(resourcetype) > 0) {
                    String resourceid = getInfoid_url_jsonobject(data);
                    Intent fieldinfo = null;
                    if (Integer.parseInt(resourcetype) != 2) {
                        fieldinfo = new Intent(context, FieldInfoActivity.class);
                        if (isApplicationContext) {
                            fieldinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        if (Integer.parseInt(resourcetype) == 3) {
                            fieldinfo.putExtra("sell_res_id", resourceid);
                            fieldinfo.putExtra("is_sell_res", true);
                        } else {
                            fieldinfo.putExtra("fieldId", resourceid);
                        }
                        String link_jsonobject = getlisturl_jsonobject(data,1);
                        if (link_jsonobject.length() > 0) {
                            ApiResourcesModel apiResourcesModel = (ApiResourcesModel)JSONObject.parseObject(link_jsonobject,ApiResourcesModel.class);
                            fieldinfo.putExtra("model", (Serializable) apiResourcesModel);
                        }
                    } else {
                        fieldinfo = new Intent(context, AdvertisingInfoActivity.class);
                        if (isApplicationContext) {
                            fieldinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        fieldinfo.putExtra("fieldId", resourceid);
                    }
                    context.startActivity(fieldinfo);
                }
            } else if (getpush_msg_type(data) == CASE_LIST) {
                Intent caseIntent = new Intent(context,ActivityCaseActivity.class);
                if (isApplicationContext) {
                    caseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (!getnotices_urlstring(data,"cur_city_id").equals("-")) {
                    caseIntent.putExtra("city_id",Integer.parseInt(getnotices_urlstring(data,"cur_city_id")));
                }
                if (!getnotices_urlstring(data,"city_ids").equals("-")) {
                    caseIntent.putExtra("city_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"city_ids")));
                }
                if (!getnotices_urlstring(data,"industry_ids").equals("-")) {
                    caseIntent.putExtra("industry_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"industry_ids")));
                }
                if (!getnotices_urlstring(data,"spread_way_ids").equals("-")) {
                    caseIntent.putExtra("spread_way_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"spread_way_ids")));
                }
                if (!getnotices_urlstring(data,"promotion_purpose_ids").equals("-")) {
                    caseIntent.putExtra("promotion_purpose_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"promotion_purpose_ids")));
                }

                if (!getnotices_urlstring(data,"label_ids").equals("-")) {
                    caseIntent.putExtra("case_label_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"label_ids")));
                }
                if (!getnotices_urlstring(data,"community_type_ids").equals("-")) {
                    caseIntent.putExtra("field_type_ids",
                            (Serializable) getUrlArray(getnotices_urlstring(data,"community_type_ids")));
                }
                context.startActivity(caseIntent);
            } else if (getpush_msg_type(data) == SERVICE_LIST) {
                String[] urlParts = data.split("\\?");
                String str = "";
                if (urlParts.length > 1) {
                    str = urlParts[1];
                }
                if (str.length() > 0) {
                    int is_login = 0;
                    if (LoginManager.isLogin()) {
                        is_login = 1;
                    }
                    Intent resources_web = new Intent(context, AboutUsActivity.class);
                    if (isApplicationContext) {
                        resources_web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    resources_web.putExtra("type", com.linhuiba.business.config.Config.COMMON_WEB_INT);
                    resources_web.putExtra("web_url",com.linhuiba.business.config.Config.FACILITATOR_LIST_URL + "?is_app=1" +
                            "&is_login=" +
                            String.valueOf(is_login) + "&" + str);
                    context.startActivity(resources_web);
                }
            } else if (getpush_msg_type(data) == COMMUNITY_INFO) {
                String resourceid = getInfoid_url_jsonobject(data);
                Intent fieldinfo = new Intent(context, CommunityInfoActivity.class);
                if (isApplicationContext) {
                    fieldinfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                fieldinfo.putExtra("city_id",Integer.parseInt(LoginManager.getInstance().getTrackcityid()));
                fieldinfo.putExtra("id", Integer.parseInt(resourceid));
                context.startActivity(fieldinfo);
            } else if (getpush_msg_type(data) == ACTIVITY_LIST) {
                Intent maintabintent = new Intent(context, SelfSupportShopActivity.class);
                if (isApplicationContext) {
                    maintabintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                maintabintent.putExtra("res_type_id",3);
                context.startActivity(maintabintent);
            } else if (getpush_msg_type(data) == ADV_LIST) {
                String link_jsonobject = getlisturl_jsonobject(data,1);
                if (link_jsonobject.length() > 0) {
                    ApiAdvResourcesModel apiResourcesModel = (ApiAdvResourcesModel) JSONObject.parseObject(link_jsonobject,ApiAdvResourcesModel.class);
                    Intent searchintent = new Intent(context, SearchAdvListActivity.class);
                    if (isApplicationContext) {
                        searchintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    searchintent.putExtra("is_home_page",4);
                    searchintent.putExtra("ApiResourcesModel",(Serializable)apiResourcesModel);
                    context.startActivity(searchintent);
                } else {
                    Intent AdvertisinglistIntent = new Intent(context, SearchAdvListActivity.class);
                    if (isApplicationContext) {
                        AdvertisinglistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    AdvertisinglistIntent.putExtra("cityname_code",LoginManager.getInstance().getTrackcityid());
                    AdvertisinglistIntent.putExtra("good_type",2);
                    context.startActivity(AdvertisinglistIntent);
                }
            } else if (getpush_msg_type(data) == THEME_INFO) {
                String resourceid = getInfoid_url_jsonobject(data);
                Intent resources_web = new Intent(context, AboutUsActivity.class);
                if (isApplicationContext) {
                    resources_web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                resources_web.putExtra("type", com.linhuiba.business.config.Config.THEME_WEB_INT);
                resources_web.putExtra("id", resourceid);
                context.startActivity(resources_web);
            } else if (getpush_msg_type(data) == WEAL_LIST) {
                Intent firstRegisterCouponsIntent = new Intent(context, TheFirstRegisterCouponsActivity.class);
                if (isApplicationContext) {
                    firstRegisterCouponsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(firstRegisterCouponsIntent);
            } else {
                if (Constants.getpush_msg_type(data) != RELEASE_PERMISSIONS) {
                    Intent resources_web = new Intent(context, AboutUsActivity.class);
                    resources_web.putExtra("type", com.linhuiba.business.config.Config.COMMON_WEB_INT);
                    resources_web.putExtra("web_url",data);
                    if (isApplicationContext) {
                        resources_web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(resources_web);
                }
            }
        }
    }
}
