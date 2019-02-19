package com.linhuiba.linhuifield.connector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONPObject;
import com.bumptech.glide.Glide;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldChooseResOptionsActivity;
import com.linhuiba.linhuifield.fieldactivity.FieldAddfieldCommunityInfoActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_UploadingPictureActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldCommunityCategoriesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.OnWheelChangedListener;
import com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlManager;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuipublic.callbackmodel.ConfigInfo;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Constants {
    private Context mcontext;

    public Constants(Context context) {
        this.mcontext = context;
    }

    public static SpannableString msp;
    public static final String picture_file_str = Environment.getExternalStorageDirectory() + "/linhuiba/";
    public static final File picture_file = new File(Environment.getExternalStorageDirectory() + "/linhuiba/");
    public static final int PhotoRequestCode = 1000;//相册选择返回值
    public static final int CameraRequestCode = 2;//拍照返回值
    public static final int CutOutPicturesRequestCode = 3;//裁剪图片返回值
    public static final int PermissionRequestCode = 100;//相机等权限返回值
    public static final int SHOW_IMG_PIXEL_WIDTH = 600;//显示照片的宽的像素
    public static final int SHOW_IMG_PIXEL_HEIGHT = 480;//显示照片的高的像素
    public static int newPosition;//预览删除图片的position
    public static int mImageList_size;//预览图片listsize
    private static int pixel_width;//图片宽
    private static int pixel_height;//图片高
    // 微信APP_ID AppSecret APP_ID
    public static final String APP_ID = "wx1c6cfbe75b359364";
    public static final String AppSecret = "a64d40a2ff8f2626d342419a1126dc1d";
    public static Activity mactivity;
    private DisplayMetrics mDisplayMetrics;
    private static int wheelviewSelectInt1 = -1;
    private static int wheelviewSelectInt2 = -1;
    private static int wheelviewSelectInt3 = -1;
    private static int wheelviewSelectInt4 = -1;
    private static com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView1;
    private static com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView2;
    private static com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView3;
    private static com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheelView4;

    public Constants(Context context, Activity activity, int newPosition, int mImageList_size) {
        this.mcontext = context;
        this.mactivity = activity;
        this.newPosition = newPosition;
        this.mImageList_size = mImageList_size;
    }

    public Constants(Context context, Activity activity, int newPosition, int mImageList_size, int width, int height) {
        this.mcontext = context;
        this.mactivity = activity;
        this.newPosition = newPosition;
        this.mImageList_size = mImageList_size;
        this.pixel_width = width;
        this.pixel_height = height;
        mDisplayMetrics = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    public Constants(Context context, Activity activity, int mImageList_size) {
        this.mcontext = context;
        this.mactivity = activity;
        this.mImageList_size = mImageList_size;
    }

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

    //判断app是否在后台
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

    //价格显示转换
    public static String getfieldinfo_integral(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#");
        price_df.setRoundingMode(RoundingMode.DOWN);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(Double.parseDouble(price) * unit));
        return pricestr;
    }

    public static String getorderdoublepricestring(Double price, double unit) {
        DecimalFormat price_df = new DecimalFormat("#");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(price * unit));
        return pricestr;
    }

    public static String getdoublepricestring(Double price, double unit) {
        DecimalFormat price_df = new DecimalFormat("###############0.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        pricestr = subZeroAndDot(price_df.format(price * unit));
        return pricestr;
    }

    public static String getpricestring(String price, double unit) {
        DecimalFormat price_df = new DecimalFormat("###############0.00");
        price_df.setRoundingMode(RoundingMode.HALF_UP);
        String pricestr = "";
        double double_minfieldprice = -1;
        double_minfieldprice = Double.parseDouble(price);
        pricestr = subZeroAndDot(price_df.format(double_minfieldprice * unit));
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

    //选择规格日期区间
    public static boolean date_interval(String datestr) {
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
            rightNow.add(Calendar.DAY_OF_YEAR, 1);//日期加1天

            Calendar endrightNow = Calendar.getInstance();
            endrightNow.setTime(rightNow.getTime());
            endrightNow.add(Calendar.MONTH, 2);//日期加2个月
            maxdate = endrightNow.getTime();
            itemDate = sdf.parse(datestr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型

        if (itemDate.after(maxdate) || itemDate.before(rightNow.getTime()) || itemDate.equals(rightNow.getTime())) {
            return false;
        } else {
            return true;
        }
    }

    //判断当前日期加几 与日期的大小
    public static boolean compare_date(String startdatestr, int add_day) {
        Date nowdate = null;
        Date startdate = null;
        Date min_reservation_date = null;
        Calendar rightNow = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data = new Date();
            String nowdata = sdf.format(data);
            nowdate = sdf.parse(nowdata);
            rightNow = Calendar.getInstance();
            rightNow.setTime(data);
            rightNow.add(Calendar.DAY_OF_YEAR, add_day);//日期加几天
            Calendar endrightNow = Calendar.getInstance();
            endrightNow.setTime(rightNow.getTime());
            min_reservation_date = sdf.parse(sdf.format(endrightNow.getTime()));

            startdate = sdf.parse(startdatestr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型
        return startdate.before(min_reservation_date);
    }

    //判断两个日期大小
    public static boolean compare_start_finish_date(String startdatestr, String finishdatestr) {
        Date finishdate = null;
        Date startdate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        try {
            startdate = sdf.parse(startdatestr);
            finishdate = sdf.parse(finishdatestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //显示各个日期的类型
        return finishdate.before(startdate);
    }

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

        if (itemDate.before(maxdate)) {
            return true;
        } else {
            return false;
        }
    }

    //获取字符串的长度
    public static float getTextWidth(Context Context, String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = Context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }

    //判断日期是周几
    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String dayForWeekstr = "";
        try {
            c.setTime(format.parse(pTime));
            int dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            if (dayForWeek == 1) {
                dayForWeekstr = "星期一";
            } else if (dayForWeek == 2) {
                dayForWeekstr = "星期二";
            } else if (dayForWeek == 3) {
                dayForWeekstr = "星期三";
            } else if (dayForWeek == 4) {
                dayForWeekstr = "星期四";
            } else if (dayForWeek == 5) {
                dayForWeekstr = "星期五";
            } else if (dayForWeek == 6) {
                dayForWeekstr = "星期六";
            } else if (dayForWeek == 7) {
                dayForWeekstr = "星期日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayForWeekstr;
    }

    //活动日期区间
    public static boolean activity_overdue(String deadline_date) {
        Date itemDate = null;
        Date maxdate = null;
        Calendar rightNow = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        try {
            Date currentdate = sdf.parse(deadline_date);
            rightNow = Calendar.getInstance();
            rightNow.setTime(currentdate);
            rightNow.add(Calendar.DAY_OF_YEAR, -1);//日期加1天

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

    public void getConfig() {
        LoginManager.readConfig();
        Field_FieldApi.getconfigurations(MyAsyncHttpClient.MyAsyncHttpClient4(), ConfigHandler, LoginManager.getversion());
    }

    private LinhuiAsyncHttpResponseHandler ConfigHandler = new LinhuiAsyncHttpResponseHandler(ConfigInfo.class) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            LoginManager.setConfig_updatetime();
            if (data != null) {
                final ConfigInfo configInfo = (ConfigInfo) data;
                if (Integer.parseInt(configInfo.getVersion()) > Integer.parseInt(LoginManager.getversion())) {
                    LoginManager.getInstance().setcontext(mcontext);
                    LoginManager.getInstance().updateConfigInfo((ConfigInfo) data);
                    //2018/7/25 测试城市数据库
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ConfigSqlManager.addCityParameter(mcontext, configInfo.getCitylist());
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
                                ConfigSqlManager.addCityParameter(mcontext, LoginManager.getcitylist());
                            }
                        }).start();
                    }
                }
            } else {
                if (!LoginManager.getInstance().isIs_insert_config()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ConfigSqlManager.addCityParameter(mcontext, LoginManager.getcitylist());
                        }
                    }).start();
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            //2018/7/25 城市数据库
            if (!LoginManager.getInstance().isIs_insert_config()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConfigSqlManager.addCityParameter(mcontext, LoginManager.getcitylist());
                    }
                }).start();

            }
        }
    };

    public static boolean iseditoractivitydate(String datestr, int add_day) {
        boolean isactivitydate = false;
        Date date = new Date();//当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DAY_OF_MONTH, add_day);//天数加一，为-1的话是天数减1
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String correctdate = format.format(calendar.getTime());
            if (format.parse(datestr).before(format.parse(correctdate))) {
                isactivitydate = true;
            } else {
                isactivitydate = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isactivitydate;
    }

    //string中将钱的单位变小
    public static SpannableString getPriceUnitStr(Context context, String pricestr, int textsize) {
        SpannableString msp = new SpannableString(pricestr);
        msp.setSpan(new AbsoluteSizeSpan(com.linhuiba.linhuifield.connector.Constants.Dp2Px(context, textsize)), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return msp;
    }

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

    public void showPicture(final View myView, final TextView mshowpicture_sizetxtview, final ViewPager mzoom_viewpage,
                            final Dialog zoom_picture_dialog, final List<ImageView> mImageViewList,
                            ArrayList<String> mPicList, boolean mIsRefreshZoomImageview,
                            int ClickedPosition, boolean isWaterMark) {
        mImageList_size = mPicList.size();
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width * pixel_height / pixel_width;
        Constants.show_dialog(myView, zoom_picture_dialog);
        if (mactivity != null && mactivity instanceof Field_AddField_UploadingPictureActivity) {

        } else {
            final TextView mshowpicture_back = (TextView) myView.findViewById(R.id.showpicture_dialog_back);
            mshowpicture_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoom_picture_dialog.dismiss();
                }
            });
        }
        if (mIsRefreshZoomImageview) {
            if (mImageViewList != null) {
                mImageViewList.clear();
            }
            for (int i = 0; i < mPicList.size(); i++) {
                ZoomImageView imageView = new ZoomImageView(
                        mcontext);
                if (mPicList.get(i).indexOf("http") != -1) {
                    if (mPicList.get(i).indexOf(Config.Linhui_Max_Watermark) != -1) {
                        Picasso.with(mcontext).load(mPicList.get(i)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width, height).into(imageView);
                    } else {
                        if (isWaterMark) {
                            Picasso.with(mcontext).load(mPicList.get(i) + Config.Linhui_Max_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width, height).into(imageView);
                        } else {
                            Picasso.with(mcontext).load(mPicList.get(i)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width, height).into(imageView);
                        }
                    }
                } else {
                    Picasso.with(mcontext).load(new File(mPicList.get(i))).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width, height).into(imageView);
                }
                mImageViewList.add(imageView);
            }
        }
        if (mImageViewList != null && mImageViewList.size() > 0) {
            showPreviewZoomAdapter(mImageViewList, mzoom_viewpage, mshowpicture_sizetxtview, ClickedPosition);
        }
    }

    public static boolean SDCardState() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {//表示SDCard存在并且可以读写
            return true;
        } else {
            return false;
        }
    }

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
        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    public static void showPreviewZoomAdapter(final List<ImageView> imageList, ViewPager mViewPager, final TextView textView,
                                              int Position) {
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 对ViewPager页号求模取出View列表中要显示的项
                position %= imageList.size();
                if (position < 0) {
                    position = imageList.size() + position;
                }
                ImageView view = imageList.get(position);
                // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);
                // add listeners here if necessary
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return imageList.size();
            }
        });
        mViewPager.setCurrentItem(Position);
        textView.setText(String.valueOf(Position % imageList.size() + 1) + "/" + String.valueOf(imageList.size()));
    }
    public static void show_preview_zoom(final List<ImageView> imageList, final ViewPager mViewPager, final TextView textView,
                                         int Position) {
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 对ViewPager页号求模取出View列表中要显示的项
                position %= imageList.size();
                if (position < 0) {
                    position = imageList.size() + position;
                }
                ImageView view = imageList.get(position);
                // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);
                // add listeners here if necessary
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return imageList.size();
            }
        });
        mViewPager.setCurrentItem(Position);
        textView.setText(String.valueOf(Position % imageList.size() + 1) + "/" + String.valueOf(imageList.size()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(String.valueOf(position % imageList.size() + 1) + "/" + String.valueOf(imageList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public static void showFieldinfoPic(final List<ImageView> imageList, final ViewPager mViewPager,
                                        final int Position, final TextView numTV, final TextView phyTV, final TextView caseTV,
                                        final int phyEnd, final Context context,
                                        final LinearLayout mShowPicstatementLL) {
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 对ViewPager页号求模取出View列表中要显示的项
                position %= imageList.size();
                if (position < 0) {
                    position = imageList.size() + position;
                }
                ImageView view = imageList.get(position);
                // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);
                // add listeners here if necessary
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return imageList.size();
//                return Integer.MAX_VALUE;
            }
        });
//        mViewPager.setCurrentItem(Position);
        mViewPager.setCurrentItem(Position + 1 , false);
//        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        numTV.setText(String.valueOf(Position % imageList.size() + 1));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (currentPosition == 0 || currentPosition == imageList.size() - 1) {

                } else {
                    mShowPicstatementLL.setVisibility(View.GONE);
                    numTV.setText(String.valueOf(position % imageList.size()));
                    if (position % imageList.size() > phyEnd) {
                        caseTV.setTextColor(context.getResources().getColor(R.color.white));
                        caseTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
                        phyTV.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                        phyTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
                    } else {
                        phyTV.setTextColor(context.getResources().getColor(R.color.white));
                        phyTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
                        caseTV.setTextColor(context.getResources().getColor(R.color.headline_tv_color));
                        caseTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;

                // 当视图在第一个时，将页面号设置为图片的最后一张。
                if (currentPosition == 0) {
                    mViewPager.setCurrentItem(imageList.size() - 2, false);

                } else if (currentPosition == imageList.size() - 1) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    mViewPager.setCurrentItem(1, false);
                }

            }
        });
        phyTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (Integer.parseInt(numTV.getText().toString()) > phyEnd) {
                    mViewPager.setCurrentItem(1);
                }
            }
        });
        caseTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (Integer.parseInt(numTV.getText().toString()) > phyEnd) {

                } else {
                    mViewPager.setCurrentItem(phyEnd + 1);
                }
            }
        });

    }

    //输入小数时的edittext监听
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
    //transaction字段用于唯一标识一个请求
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * dp转换成px,代码写的是像素,而XML中写的是单位密度
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转换成dp,代码写的是像素,而XML中(dp)写的是单位密度
     *
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    //设置tablayout 中下面横线的长度距两边的距离
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
    public static boolean IsCurrentActivity(Context context, String mActivityName) {
        boolean isCurrentActivity = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
                .getClassName();
        if (mActivityName.equals(runningActivity)) {
            isCurrentActivity = true;
        }
        return isCurrentActivity;
    }

    //禁止编辑
    public static void disableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                } else if (v instanceof ListView) {
                    ((ListView) v).setClickable(false);
                    ((ListView) v).setEnabled(false);
                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(false);
                ((EditText) v).setClickable(false);
            } else if (v instanceof Button) {
                ((Button) v).setEnabled(false);
            } else if (v instanceof TextView) {
                ((TextView) v).setEnabled(false);
            } else if (v instanceof RelativeLayout) {
                ((RelativeLayout) v).setEnabled(false);
            } else if (v instanceof LinearLayout) {
                ((LinearLayout) v).setEnabled(false);
            }
        }
    }

    public static int getWeatherStr(int code) {
        int mDrawable = R.string.fieldinfo_no_parameter_message;
        if (code == 100 || code == 201) {
            mDrawable = R.string.module_schedule_sun;
        } else if (code == 101 || code == 102
                || code == 103 || code == 104) {
            mDrawable = R.string.module_schedule_cloudy;
        } else if (code == 300 || code == 301
                || code == 303 || code == 304) {
            mDrawable = R.string.module_schedule_shower_rain;
        } else if (code == 305 || code == 309) {
            mDrawable = R.string.module_schedule_light_rain;
        } else if (code == 306) {
            mDrawable = R.string.module_schedule_moderate_rain;
        } else if (code == 307 || code == 308
                || code == 310 || code == 311
                || code == 312 || code == 313) {
            mDrawable = R.string.module_schedule_heavy_rain;
        } else if (code > 399 &&
                code < 408) {
            mDrawable = R.string.module_schedule_snow;
        } else if (code > 499 &&
                code < 503) {
            mDrawable = R.string.module_schedule_foggy;
        } else if (code > 199 &&
                code < 214 && code != 201) {
            mDrawable = R.string.module_schedule_windy;
        } else if (code > 502 &&
                code < 509) {
            mDrawable = R.string.module_schedule_sand;
        }
        return mDrawable;
    }

    public static int getWeatherImg(int code) {
        int mDrawable = R.drawable.bunner_round_white;
        if (code == 100 || code == 201) {
            mDrawable = R.drawable.ic_sun_three;
        } else if (code == 101 || code == 102
                || code == 103 || code == 104) {
            mDrawable = R.drawable.ic_cloudy_three;
        } else if (code == 300 || code == 301
                || code == 303 || code == 304) {
            mDrawable = R.drawable.ic_shower_rain_three;
        } else if (code == 305 || code == 309) {
            mDrawable = R.drawable.ic_light_rain_three;
        } else if (code == 306) {
            mDrawable = R.drawable.ic_moderate_rain_three;
        } else if (code == 307 || code == 308
                || code == 310 || code == 311
                || code == 312 || code == 313) {
            mDrawable = R.drawable.ic_heavy_rain_three;
        } else if (code > 399 &&
                code < 408) {
            mDrawable = R.drawable.ic_snow_three;
        } else if (code > 499 &&
                code < 503) {
            mDrawable = R.drawable.ic_foggy_three;
        } else if (code > 199 &&
                code < 214 && code != 201) {
            mDrawable = R.drawable.ic_wind_three;
        } else if (code > 502 &&
                code < 509) {
            mDrawable = R.drawable.ic_sand_three;
        }
        return mDrawable;
    }


    public static int getMySelfGradeDrawable(String code) {
        int mDrawable = 0;
        if (code != null) {
            if (code.equals("V0")) {
                mDrawable = R.drawable.ic_memberv0_three;
            } else if (code.equals("V1")) {
                mDrawable = R.drawable.ic_memberv1_three;
            } else if (code.equals("V2")) {
                mDrawable = R.drawable.ic_memberv2_three;
            } else if (code.equals("V3")) {
                mDrawable = R.drawable.ic_memberv3_three;
            } else if (code.equals("V4")) {
                mDrawable = R.drawable.ic_memberv4_three;
            } else if (code.equals("V5")) {
                mDrawable = R.drawable.ic_memberv5_three;
            }
        }
        return mDrawable;
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char) result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }

        }
        return sb.toString();
    }

    public static Bitmap addWaterMark(Bitmap src, String water, Activity context) {
        Bitmap tarBitmap = src.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        int w = tarBitmap.getWidth();
        int h = tarBitmap.getHeight();
        Canvas canvas = new Canvas(tarBitmap);
//        Paint p = new Paint();
////        p.setColor(Color.BLACK);// 设置灰色
//        p.setStyle(Paint.Style.FILL);//设置填满
////        p.setAlpha(200);
//        LinearGradient lg=new LinearGradient(0,h*0.7f,0,h,new int[]{R.color.grid_state_pressed,Color.BLACK},new float[]{0f,1.0f}, Shader.TileMode.CLAMP);  //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像//刚才已经讲到Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变，代码如下：
//        p.setShader(lg);
//        canvas.drawRect(0, h*0.7f,w , h, p);// 长方形
        View myView = context.getLayoutInflater().inflate(R.layout.module_share_bg, null);
        Bitmap viewBitmap = convertViewToBitmap(myView);
        if (viewBitmap != null) {
            canvas.drawBitmap(viewBitmap, 0, h * 0.6f, null);
        }
        //启用抗锯齿和使用设备的文本字距
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setAntiAlias(true);       // 设置抗锯齿
        //字体的相关设置
        textPaint.setTextSize(17.0f);//字体大小
//        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.WHITE);
        StaticLayout layout = new StaticLayout(water, textPaint, w,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate((float) (w * 0.03), (float) (h * 0.75));//从20，20开始画
        layout.draw(canvas);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        if (viewBitmap != null) {
            viewBitmap.recycle();
        }
        return tarBitmap;
    }
    public static Bitmap addWaterMark(View view, Context mContext) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();


        if(view == null){
            return null ;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(
                    view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(bitmap));
            return bitmap;
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 把网络资源图片转化成bitmap
     *
     * @param url 网络资源图片
     * @return Bitmap
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩 微信分享小于128
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options < 0 ||
                    options == 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        image.recycle();
        return bitmap;
    }

    //Bitmap → byte[]
    public static byte[] Bitmap2Bytes(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static void hideUploadPictureLine(Context context, Dialog dialog) {
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(divierId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 控制dialog点击后是否能够关闭
     *
     * @param dialog      想要控制的dialog
     * @param isCloseAble true表示可以关闭 false表示不能关闭
     */
    public static void setDialogCloseAbility(DialogInterface dialog, boolean isCloseAble) {
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, isCloseAble);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断是否是json
    public static boolean isGoodJson(String json) {
        try {
            new JSONPObject().addParameter(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    //本地文件解析为str
    public static String readAssetsFileString(Activity activity, String fileName) {
        String str = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //判断日期是周几详情使用
    public static int getDayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int dayForWeekstr = -1;
        try {
            c.setTime(format.parse(pTime));
            int dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            dayForWeekstr = dayForWeek - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayForWeekstr;
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public static File getDCIMFile(String imageName) {
        if (SDCardState()) {
            if (!picture_file.exists()) {
                try {
                    //按照指定的路径创建文件夹
                    picture_file.mkdirs();
                } catch (Exception e) {

                }
            }
            File file = new File(picture_file_str + imageName);
            if (!file.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file.createNewFile();
                } catch (Exception e) {
                }
            }
            return file;
        } else {
            return null;
        }
    }

    public static List<ConfigCitiesModel> getCityList(Context context) {
        List<ConfigCitiesModel> configCityParameterModels;
        configCityParameterModels = ConfigSqlOperation.selectCitySQL(context);
        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {

        } else {
            configCityParameterModels = ConfigurationsModel.getInstance().getCitylist();
        }
        return configCityParameterModels;
    }

    /**
     * 配置文件中的城市获取区域
     * @param city_id 城市id
     * @param context
     * @return
     */
    public static List<Field_AddResourceCreateItemModel> getDistrictsList(int city_id, Context context) {
        List<Field_AddResourceCreateItemModel> configCitiesModels = new ArrayList<>();
        List<ConfigCityParameterModel> configCityParameterModels = new ArrayList<>();
        List<ConfigCityParameterModel> districts = ConfigSqlOperation.selectSQL(1,city_id,context);
        if (districts != null && districts.size() > 0) {
            configCityParameterModels.addAll(districts);
        } else {
            List<ConfigCityParameterModel> districtsModels = ConfigurationsModel.getInstance().getDistrictsList(city_id);
            configCityParameterModels.addAll(districtsModels);
        }
        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
            for (int i = 0; i < configCityParameterModels.size(); i++) {
                Field_AddResourceCreateItemModel configCitiesModel = new Field_AddResourceCreateItemModel();
                configCitiesModel.setId(configCityParameterModels.get(i).getDistrict_id());
                configCitiesModel.setName(configCityParameterModels.get(i).getName());
                configCitiesModel.setCity_id(configCityParameterModels.get(i).getCity_id());
                configCitiesModels.add(configCitiesModel);
            }
        }
        return configCitiesModels;
    }

    /**
     * 发布场地时获取城市的区域
     * @param city_id
     * @param cityList
     * @return
     */
    public static List<Field_AddResourceCreateItemModel> getAddfieldDistrictsList(int city_id, List<Field_AddResourceCreateItemModel> cityList) {
        List<Field_AddResourceCreateItemModel> configCitiesModels = new ArrayList<>();
        if (cityList != null && cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getId() - city_id == 0) {
                    configCitiesModels = cityList.get(i).getDistricts();
                    break;
                }
            }
        }
        return configCitiesModels;
    }


    public static List<ConfigCitiesModel> getResTypeList() {
        List<ConfigCitiesModel> configCityParameterModels = new ArrayList<>();
        String resType = LoginManager.getfieldtype();
        if (resType != null &&
                resType.length() > 0) {
            configCityParameterModels = JSON.parseArray(resType, ConfigCitiesModel.class);
        }
        return configCityParameterModels;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    public static boolean isCouponsExpired(String startdatestr, int add_day) {
        Date nowdate = null;
        Date startdate = null;
        Date min_reservation_date = null;
        Calendar rightNow = null;
        boolean isBigger;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data = new Date();
            String nowdata = sdf.format(data);
            nowdate = sdf.parse(nowdata);
            rightNow = Calendar.getInstance();
            rightNow.setTime(data);
            rightNow.add(Calendar.DAY_OF_YEAR, add_day);//日期加几天
            Calendar endrightNow = Calendar.getInstance();
            endrightNow.setTime(rightNow.getTime());
            min_reservation_date = sdf.parse(sdf.format(endrightNow.getTime()));

            startdate = sdf.parse(startdatestr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (startdate.getTime() >= min_reservation_date.getTime()) {
            isBigger = true;
        } else {
            isBigger = false;
        }
        return isBigger;
    }
    public static void showWheelViewDialog(final ArrayList<AddfieldCommunityCategoriesModel> list, final Context context,
                                           final Dialog mDialog, final int view_size, final TextView textView) {
        if (list == null || list.size() == 0) {
            BaseMessageUtils.showToast(context.getResources().getString(R.string.review_error_text));
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View textEntryView = layoutInflater.inflate(R.layout.module_dialog_addfield_community_categories, null);
        wheelView1 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories1);
        wheelView2 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories2);
        wheelView3 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories3);
        wheelView4 = (com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView) textEntryView.findViewById(R.id.categories4);
        TextView mBtnConfirm= (TextView) textEntryView.findViewById(R.id.btn_confirm);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(R.id.btn_cancel);
        final String[] wheelList1 = new String[list.size()];
        for (int i = 0; i < list.size(); i ++) {
            wheelList1[i] = (list.get(i).getName());
        }
        // 添加change事件
        wheelView1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt1 = newValue;
                showwheel(2,list,context);
                if (view_size == 3) {
                    showwheel(3,list,context);
                }
                if (view_size == 4) {
                    showwheel(4,list,context);
                }

            }
        });
        wheelView2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt2 = newValue;
                if (view_size > 2) {
                    showwheel(3,list,context);
                    showwheel(4,list,context);
                }

            }
        });
        wheelView3.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt3 = newValue;
                showwheel(4,list,context);
            }
        });
        wheelView4.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(com.linhuiba.linhuifield.fieldview.kankan.wheel.widget.WheelView wheel, int oldValue, int newValue) {
                wheelviewSelectInt4 = newValue;
            }
        });
        wheelviewSelectInt1 = 0;
        wheelviewSelectInt2 = 0;
        wheelviewSelectInt3 = 0;
        wheelviewSelectInt4 = 0;
        showwheel(1,list,context);
        showwheel(2,list,context);
        showwheel(3,list,context);
        showwheel(4,list,context);
        // 设置可见条目数量
        wheelView1.setVisibleItems(11);
        wheelView2.setVisibleItems(11);
        wheelView3.setVisibleItems(11);
        wheelView4.setVisibleItems(11);

        // 添加onclick事件
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categories3 = 0;
                String categoriesstr3 = "";
                if (wheelviewSelectInt4 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getCategories().get(wheelviewSelectInt4).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getCategories().get(wheelviewSelectInt4).getId();
                } else if (wheelviewSelectInt3 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).
                            getCategories().get(wheelviewSelectInt3).getId();
                } else if (wheelviewSelectInt2 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getName();
                    categories3 = list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getId();
                } else if (wheelviewSelectInt1 != -1) {
                    categoriesstr3 = list.get(wheelviewSelectInt1).getName();
                    categories3 = list.get(wheelviewSelectInt1).getId();
                }
//                mCategoryId = categories3;
                textView.setText(categoriesstr3);

                mDialog.dismiss();
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        Constants.show_dialog(textEntryView,mDialog);
    }
    private static void showwheel(int position,ArrayList<AddfieldCommunityCategoriesModel> list,
                           Context context) {
        if (position == 1) {
            if (list != null && list.size() > 0) {
                final String[] wheelList1 = new String[list.size()];
                for (int i = 0; i < list.size(); i ++) {
                    wheelList1[i] = (list.get(i).getName());
                }
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(context, wheelList1));
                wheelviewSelectInt1 = 0;
                wheelView1.setVisibility(View.VISIBLE);
            } else {
                wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt1 = -1;
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView1.setVisibility(View.GONE);
                wheelView2.setVisibility(View.GONE);
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 2) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt1 < list.size() &&
                    list.get(wheelviewSelectInt1).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().size(); i ++) {
                    wheelList1[i] = list.get(wheelviewSelectInt1).getCategories().get(i).getName();
                }
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(context, wheelList1));
                wheelviewSelectInt2 = 0;
                wheelView2.setVisibility(View.VISIBLE);
            } else {
                wheelView2.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt2 = -1;
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView2.setVisibility(View.GONE);
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 3) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt2 > -1 &&
                    wheelviewSelectInt2 < list.get(wheelviewSelectInt1).getCategories().size() &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size(); i ++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(i).getName());
                }
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(context, wheelList1));
                wheelviewSelectInt3 = 0;
                wheelView3.setVisibility(View.VISIBLE);
            } else {
                wheelView3.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt3 = -1;
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView3.setVisibility(View.GONE);
                wheelView4.setVisibility(View.GONE);
            }
        } else if (position == 4) {
            if (wheelviewSelectInt1 > -1 && wheelviewSelectInt2 > -1 && wheelviewSelectInt3 > -1 &&
                    wheelviewSelectInt3 < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().size() &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories() != null &&
                    list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size() > 0) {
                String[] wheelList1 = new String[list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size()];
                for (int i = 0; i < list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().size(); i ++) {
                    wheelList1[i] = (list.get(wheelviewSelectInt1).getCategories().get(wheelviewSelectInt2).getCategories().get(wheelviewSelectInt3).getCategories().get(i).getName());
                }
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(context, wheelList1));
                wheelviewSelectInt4 = 0;
                wheelView4.setVisibility(View.VISIBLE);
            } else {
                wheelView4.setViewAdapter(new ArrayWheelAdapter<String>(context, new String[] { "" }));
                wheelviewSelectInt4 = -1;
                wheelView4.setVisibility(View.GONE);
            }
        }
    }
    public static boolean isNotificationsEnabled(Context mContext) {
        if (Build.VERSION.SDK_INT >= 24) {
            return NotificationManagerCompat.from(mContext).areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOps =
                    (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = mContext.getApplicationInfo();
            String pkg = mContext.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE,
                        Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
                        == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException e) {
                return true;
            }
        } else {
            return true;
        }
    }
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static void saveToSystemGallery(Context context, String url) {
        // 首先保存图片
        Bitmap bmp = returnBitMap(url);
        if (Constants.SDCardState()) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "linhuiba");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            if (file != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    if (bmp != null) {
                        bmp.recycle();
                    }
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                file.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param ms
     * @param showSize 1 只显示最大单位 3 显示时分秒 显示天时分秒
     * @return
     */
    public static String getFormatTime(Long ms,int showSize) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = ms / dd;
        if (showSize == 3) {
            day = Long.parseLong("0");
        }
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
            if (showSize == 1) {
                return sb.toString();
            }
        }
        if(hour > 0) {
            sb.append(hour+"小时");
            if (showSize == 1) {
                return sb.toString();
            }
        }
        if(minute > 0) {
            sb.append(minute+"分");
            if (showSize == 1) {
                return sb.toString();
            }
        }
        if(second > 0) {
            sb.append(second+"秒");
            if (showSize == 1) {
                return sb.toString();
            }
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//            if (showSize == 1) {
//                return sb.toString();
//            }
//        }
        return sb.toString();
    }
}