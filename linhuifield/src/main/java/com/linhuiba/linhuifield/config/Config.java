package com.linhuiba.linhuifield.config;

/**
 * Created by snowd on 15/3/27.
 */
public class Config {
    /* 真实环境  PE*/
    private static final String Domain_Name_PE = "https://m.linhuiba.com";
    private static final String BASE_API_URL_PHP_PE = "https://api.linhuiba.com/api/";

    /* 预演环境  PE_TEST*/
    private static final String Domain_Name_PE_TEST = "http://m.lanhanba.com";
    private static final String BASE_API_URL_PHP_PE_TEST = "http://hz.lanhanba.com/api/";

    /* SE测试环境 */
    private static final String BASE_API_URL_PHP_SE = "https://hz.lanhanba.net/api/";
    private static final String Domain_Name_SE = "https://m.lanhanba.net";
    /* IE */
//    private final String BASE_API_URL_PHP_IE = "https://192.168.0.196:443/api/";
//    private final String Domain_Name_IE = "https://192.168.0.196";
    private static final String BASE_API_URL_PHP_IE = "http://192.168.0.196:80/api/";
    private static final String Domain_Name_IE = "https://m.lanhanba.net";
    private static final String Domain_Name_IE_TEST = "http://wap.vaiwan.com:8081";
    private static final String Domain_Name_COOKING_IE_TEST = "http://wap.vaiwan.com";
    /* DE */
    private static final String BASE_API_URL_PHP_DE = "http://192.168.0.110:8009/api/";
    private static final String Domain_Name_DE = "http://m.lanhanba.net";
    private static final String BASE_API_URL_PHP_DETEST = "http://192.168.0.109/api/";
    private static final String BASE_API_URL_PHP_DETESTLL = "http://192.168.0.109:8039/api/";
    private static final String BASE_API_URL_PHP_DETESTZR = "http://gitlhbzxr.vaiwan.com:8081/api/";
    private static final String BASE_API_URL_PHP_DETESTJLP = "http://192.168.0.123/api/";
    //api请求网络头url 和 web加载网络头url
    public static final String BASE_API_URL_PHP = com.linhuiba.linhuipublic.config.Config.DEBUG?BASE_API_URL_PHP_SE:BASE_API_URL_PHP_PE;
    public static final String Domain_Name = com.linhuiba.linhuipublic.config.Config.DEBUG?Domain_Name_SE:Domain_Name_PE;
    public static final String INVITE_WEBVIEW_URL = Domain_Name+"/admin/profile/invite?&is_mobile=1&is_app=1&invite_code=";
    public static final String INVITE_SHARE_URL = Domain_Name+"/admin/profile/invited?&is_mobile=1&is_app=1&invite_code=";
    public static final String FIELDINFO_FIELD_URL =Domain_Name+ "/fields/view/";
    public static final String FIELDINFO_ADV_URL = Domain_Name+"/advs/view/";
    public static final String FIELDINFO_ACTIVITY_URL = Domain_Name+"/activities/view/";
    public static final String FIELDINFO_END_URL = "?is_mobile=1&is_app=1";
    public static final String COMMUNITY_SHARE_URL = Domain_Name+"/shop/index/";
    public static final String COMMUNITY_RESOURCES_SHARE_URL = Domain_Name+"/communities/";
    public static final String COMMUNITY_RESOURCES_SHARE_URL_OTHER ="/resources";
    public static final String ABOUT_WEBVIEW_URL = Domain_Name+"/company/about?is_mobile=1&is_app=1";
    public static final String HELP_WEBVIEW_URL = Domain_Name+"/company/help?is_mobile=1&is_app=1";
    public static final String BUSSINESS_COMPANY_URL = Domain_Name+"/admin/users/view?";
    public static final String QINIU_TOKEN = "https://api.linhuiba.com/qiniu/app-token";
    public static final String qiniu_domain = "https://img.linhuiba.com/";
    public static final String PropertyDataStatistical = Domain_Name + "/#/appStatistics";
    public static final String PropertyDataStatisticalSignSuccess =Domain_Name + "/app_page/relation_success" + FIELDINFO_END_URL;


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
