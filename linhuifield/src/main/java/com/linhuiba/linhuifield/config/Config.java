package com.linhuiba.linhuifield.config;

/**
 * Created by snowd on 15/3/27.
 */
public class Config {
    /* 真实环境  PE*/
    private static final String Domain_Name_PE = "https://m.linhuiba.com";
    public static final String BASE_API_URL_PHP_PE = "https://api.linhuiba.com/api/";
    //api请求网络头url 和 web加载网络头url
    public static String BASE_API_URL_PHP;
    public static String Domain_Name;
    public static final String QINIU_TOKEN = "https://api.linhuiba.com/qiniu/app-token";
    public static final String qiniu_domain = "https://img.linhuiba.com/";
    public static String PropertyDataStatistical = "/#/appStatistics";


    public static final int GUARD_WEB_INT = 5;
    public static final int ABOUT_WEB_INT = 0;
    public static final int HELP_WEB_INT = 1;
    public static final int BUSINESS_COMPANY_WEB_INT = 2;//商家公司信息
    public static final int POINT_INFO_WEB_INT = 3;//积分详情
    public static final int RESOURCE_INFO_WEB_INT = 6;//资源详情
    public static final int COMMON_WEB_INT = 9;//普通直接加载web
    public static final int INVOICE_INFO_WEB_INT = 10;//票据详情
    public static final int Number_of_people_Show_Direction = 13;
    public static final int ORDER_INFO_INT = 17;//物业订单详情
    public static final int DEMAND_INFO_INT = 18;//需求详情
    public static final int BIG_ORDER_INFO_INT = 23;//不是待支付大订单详情
    public static final int RECEIVE_ACCOUNT_INT = 34;//收款账号
    public static final int ADD_ACCOUNT_INT = 37;//新增收款账号
    public static final int ENTERPRISE_CERTIFICATE_INT = 39;//企业认证
    //友盟点击事件命名
    public static final String UM_ACCOUNT_LOGIN = "li_click_login";
    public static final String UM_WECHAT_LOGIN = "li_click_wx_login";
    public static final String UM_WECHAT_FAST_LOGIN = "li_click_fast_login";
    public static final String UM_HOME_FIELD_CLICK = "hp_click_field";
    public static final String UM_HOME_MAP_CLICK = "hp_click_lookMap";
    public static final String UM_HOME_GROUP_CLICK = "hp_click_groupBuy";
    public static final String UM_HOME_SELF_SUPPORT_SHOP_CLICK = "hp_click_selfSupportShop";
    public static final String UM_ADV_CLICK = "hp_click_ad";
    public static final String UM_REGISTER_CLICK = "re_click_register";

}
