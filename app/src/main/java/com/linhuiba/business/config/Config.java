package com.linhuiba.business.config;


import com.linhuiba.business.BuildConfig;

/**
 * Created by snowd on 15/3/27.
 */
public class Config {
    /* 真实环境  PE*/
    public static final String BASE_API_URL_PHP_PE = "https://api.linhuiba.com/api/";
    public static final String Domain_Name_PE = "https://m.linhuiba.com";
    /* 预演环境  PE_TEST*/
    public static final String Domain_Name_PE_TEST = "http://m.lanhanba.com";
    public static final String BASE_API_URL_PHP_PE_TEST = "http://hz.lanhanba.com/api/";
    /* SE测试环境 */
    public static final String BASE_API_URL_PHP_SE = "https://hz.lanhanba.net/api/";
    public static final String Domain_Name_SE = "https://m.lanhanba.net";
  /* IE */
//    private final String BASE_API_URL_PHP_IE = "https://192.168.0.196:443/api/";
//    private final String Domain_Name_IE = "https://192.168.0.196";
    public static final String BASE_API_URL_PHP_IE = "http://192.168.0.196:80/api/";
    public static final String Domain_Name_IE = "https://m.lanhanba.net";
    /* DE */
    public static final String BASE_API_URL_PHP_DE = "http://192.168.0.104:8009/api/";
    public static final String Domain_Name_DE = "https://m.lanhanba.net";
    public static final String BASE_API_URL_PHP_DETESTLL = "http://192.168.0.109:8039/api/";
    public static final String BASE_API_URL_PHP_DETESTZR = "http://gitlhbzxr.vaiwan.com:8081/api/";
    public static final String BASE_API_URL_PHP_DETESTJLP = "http://192.168.0.123/api/";
    public static final String BASE_API_URL_PHP_DETEWAP = "http://192.168.0.121:9000";
    //api请求网络头url 和 web加载网络头url
    public static String BASE_API_URL_PHP = com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP =
            BuildConfig.DEBUG?BASE_API_URL_PHP_SE:BASE_API_URL_PHP_PE;
    public static String Domain_Name = com.linhuiba.linhuifield.config.Config.Domain_Name =
            BuildConfig.DEBUG?Domain_Name_SE:Domain_Name_PE;
    //加载页面
    public static String FIELDINFO_ADV_URL ="/advs/view/";//广告详情
    public static String FIELDINFO_END_URL = "?is_mobile=1&is_app=1";
    public static String RESOURCESINFO_END_URL = "?is_mobile=1&is_app=1&only_view=1";
    public static String TRANSACTION_VOUCHER_URL = "/#/app_page/orders_info/";//商家端获取交易凭证
    public static String ORDER_INFO_URL = "/#/appPropertyOrderDet/";//物业订单详情加载
    //需求定制
    public static String ADD_DEMAND_WEB_URL = "/#/appDemand";
    //优质服务商
    public static String FACILITATOR_LIST_URL = "/#/appFacilitatorlist";
    //优质服务商详情
    public static String FACILITATOR_INFO_URL = "/#/appFacilitatorDetail/";
    //优质服务商案例详情
    public static String FACILITATOR_CASE_INFO_URL = "/#/appCaseDetail/";
    //特色主题展
    public static String THEMATIC_URL = "/#/appThematic";
    //特色主题展详情
    public static String THEMATIC_INFO_URL = "/#/appThematicDetail/";
    //查看拼团流程详情
    public static String GROUP_DESC_URL = "/#/appGroupDesc";
    //拼团列表分享
    public static String SHARE_GROUP_LIST_URL = "/#/group";
    //小程序拼团列表分享
    public static String WX_MINI_SHARE_GROUP_LIST_URL = "pages/groupPurchase/groupPurchase";
    //拼团详情分享
    public static String SHARE_GROUP_INFO_URL = "/#/group/groupDetail/";//+ID 拼团id + city_id
    //小程序拼团详情分享
    public static String WX_MINI_SHARE_GROUP_INFO_URL = "pages/groupPurchaseDetail/groupPurchaseDetail";//+ID 拼团id + city_id
    //已开票据详情
    public static String INVOICE_INFO_URL = "/#/appInvoiceDetail/";//+id？+key
    //场地详情web
    public static String FIELD_INFO_URL = "/#/appFields/"; //+ id + ?res_type_id=1";//
    //物业端查看订单详情 保持原来的链接 包括物业的支付凭证
    //会员等级查看
    public static String UEER_GRADE_URL = "/#/appLevelDesc";//+key
    //我的需求
    public static String MY_DEMAND_URL = "/#/appMyDemand";//+key
    //其他状态大订单
    public static String BIG_ORDER_INFO_URL = "/#/appBigorderDetail/";//+ID?+key
    //待支付大订单
    public static String SUBMITTED_ORDER_INFO_WEB_URL = "/#/appSubmittedorderDetail/";//+ID?+key
    //订单场地详情 订单行
    public static String ORDER_ITEM_INFO_URL = "/#/appSmallorderDetail/";//+ID?+key
    //查看交易凭证
    public static String DEAL_VOUCHER_URL = "/#/appDealVoucher/";//+ID?+key
    //关于我们
    public static String ABOUT_US_URL = "/#/appAbout";
    //帮助中心
    public static String HELP_URL = "/#/appHelp";//
    //邀请好友
    public static String INVITE_URL = "/#/appInvite";//+KEY
    //分享邀请码界面
    public static String SHARE_INVITE_CODE_URL = "/#/invited?invite_code=";//+code邀请码
    //小程序分享邀请码界面
    public static final String WX_MINI_SHARE_INVITE_CODE_URL = "invitePages/pages/invited/invited?invite_code=";//+code邀请码
    //场地列表分享
    public static String SHARE_FIELDS_LIST_URL = "/#/fields?";
    //小程序场地列表分享
    public static final String WX_MINI_SHARE_FIELDS_LIST_URL = "pages/fields/fields?";
    //展位详情分享
    public static String SHARE_FIELDS_INFO_URL = "/#/fields/";//+ID 场地id
    //展位详情小程序分享
    public static final String WX_MINI_SHARE_FIELDS_INFO_URL = "pages/fieldsDetail/fieldsDetail?is_app=1&BackKey=1&id=";//+ID 场地id
    //供给详情小程序分享
    public static final String WX_MINI_SHARE_ACTIVITY_INFO_URL = "pages/sizeDetail/sizeDetail?is_app=1&BackKey=1&id=";//+ID 供给id
    //活动列表分享
    public static String SHARE_ACTIVITIES_LIST_URL = "/#/activities?is_app=1&BackKey=1&city_id=";
    //小程序活动列表分享
    public static final String WX_MINI_SHARE_ACTIVITIES_LIST_URL = "pages/activities/activities?is_app=1&BackKey=1&city=";
    //活动供给详情分享
    public static String SHARE_ACTIVITIES_INFO_URL = "/#/activities/";//+ID 活动id
    //广告列表分享
    //广告详情分享
    //商家信息详情  暂时没找到
    public static String BUSSINESS_COMPANY_URL = "/#/appBussinessInfo?";
    //获取积分列表
    public static String INVITE_WEB_URL = "/#/appPoint?";//+KEY
    //通知保安
    public static String INFORM_GUARD_WEB_URL = "/#/appArticle/64";
    //积分使用规则
    public static String INTEGRA_RULE_WEB_URL = "/#/appArticle/65";
    //钱包说明
    public static String WALLET_EXPLAIN_WEB_URL = "/#/appArticle/72";
    //人流量展示方向说明
    public static String NUMBER_OF_PEOPLE_WEB_URL = "/#/appArticle/73";
    //注册协议
    public static String AGREEMENT_URL = "/#/appRegisterProtocol";//用户协议
    //分享链接
    //特色主题展分享链接
    public static String SHARE_THEMATIC_URL = "/#/thematic/thematicDetail/";
    //优质服务商分享链接
    public static String SHARE_FACILITATOR_URL = "/#/facilitatorlist/facilitatorDetail/";//+ID
    //优质服务商案例分享链接
    public static String SHARE_FACILITATOR_CASE_URL = "/#/facilitatorlist/caseDetail/";//+ID+city
    //需求定制分享链接
    public static String SHARE_DEMAND_URL = "/#/demand";
    //案例朋友圈分享链接
    public static String SHARE_CASE_LIST_PYQ_URL = "/#/classicCase?BackKey=1&is_app=1&";
    //案例小程序分享链接
    public static final String SHARE_CASE_LIST_WXMINI_URL = "otherPages/pages/case/case?BackKey=1&is_app=1&";
    //案例详情朋友圈分享链接
    public static String SHARE_CASE_INFO_PYQ_URL = "/#/classicCaseDetial?BackKey=1&is_app=1&caseId=";
    //案例详情小程序分享链接
    public static final String SHARE_CASE_INFO_WXMINI_URL = "otherPages/pages/caseDetail/caseDetail?BackKey=1&is_app=1&id=";
    //场地详情微信分享
    public static String SHARE_COMMUNITY_INFO_URL = "/#/community/";
    //场地详情小程序分享
    public static final String WX_MINI_SHARE_COMMUNITY_INFO_URL = "pages/fieldsDetail/fieldsDetail?type=community&is_app=1&BackKey=1&id=";
    //礼包小程序分享
    public static final String WX_MINI_SHARE_GIFT_URL = "pages/couponPages/pages/gift/gift";
    //礼包微信分享
    public static String WX_SHARE_GIFT_URL = "/#/newCoupon";
    //专题小程序分享
    public static final String WX_MINI_SHARE_THEME_URL = "themePages/pages/themeDetail/themeDetail?is_app=1&id=";
    //专题分享
    public static String WX_SHARE_THEME_URL = "/#/specialTopic/";
    //分享链接

    //需求详情
    public static String MY_DEMAND_INFO_WEB_URL = "/#/appDemandDetail/";//+ID?+key
    //询价加载web
    public static String ADD_ENQUIRY = "/#/appEnquire/";//+ID?+key
    //询价列表加载web
    public static String ENQUIRY_LIST = "/#/appEnquirelist";//+key
    //询价单详情加载web
    public static String ENQUIRY_OINFO = "/#/appEnquireDetail/";//ID+key
    //注册
    public static String REGISTER_URL = "/#/appRegistered?channel=android";
    //收款账号
    public static String RECEIVE_ACCOUNT_URL = "/#/appReceivingAccount";
    //申请发布权限
    public static String APPLY_FOR_RELEASE_URL = "/#/appPublishingPermissions?publishingMobile=";
    // 企业信息
    public static String COMPANY_INFO_URL = "/#/appCompanyInfo";
    // 新增收款账号
    public static String ADD_ACCOUNT_URL = "/#/appAddAccount";
    // 修改手机号
    public static String MODIFY_MOBILE_URL = "/#/appChangeMobile?oldMobile=";
    // 企业认证
    public static String ENTERPRISE_CERTIFICATE_URL = "/#/appEnterpriseCertificate?user_id=";
    // 场地意向
    public static String SITE_INTENNTION_URL = "/#/appSiteIntention";
    //人流量展示方向说明
    public static String APP_HTML_LOAD_URL = "/#/appArticle/";//+id
    //优惠券使用规则
    public static String COUPON_REGULATION_URL = "/#/appArticle/128";
    //积分兑换优惠券
    public static String INTEGRAL_EXCHANGE_COUPON_REGULATION_URL = "/#/appArticle/128";
    //新人礼包活动规则
    public static String NEW_GIFT_BAG_REGULATION_URL = "/#/appArticle/128";
    //专题详情加载web
    public static String THEME_INFO_URL = "/#/appSpecialTopic/";//ID
    //询价成功加载web
    public static String ENQUIRY_SUCCESS_URL = "/#/appEnquireSuccess?id=";//+ID?+key
    //询价成功加载web
    public static String DEMAND_HALL_URL = "/#/appDemandHall?";//
    //七牛token
    public static final String QINIU_TOKEN = "https://api.linhuiba.com/qiniu/app-token";//普通发布场地
//    public static final String QINIU_TOKEN_FIELDS = "https://www.linhuiba.com/qiniu/app-token";//资源发布token
    public static final String QINIU_TOKEN_CERTS = "https://api.linhuiba.com/qiniu/app-token/linhuiba-certs";//认证token/申请发布权限/企业认证/对公打款/专票
    public static final String QINIU_TOKEN_COMMENT = "https://api.linhuiba.com/qiniu/app-token/linhuiba-fields";//评论token/评论/轨迹/公司信息
    public static final String qiniu_domain_creats = "https://cert.linhuiba.com/";//认证token/申请发布权限/企业认证/对公打款/专票
    public static final String qiniu_domain_comment = "https://comment.linhuiba.com/";//评论token/评论/轨迹/公司信息/个人头像
    //加载html自适应图片和文字
    public static final String WEBVIEW_URL_CSS = "<html> \n<head> \n <style type=\"text/css\"> \n body {font-size:60px;}\n </style> \n </head> \n <body> <script type='text/javascript'> window.onload = function(){\n var $img = document.getElementsByTagName('img');\n for(var p in  $img){\n  $img[p].style.width = '100%';\n $img[p].style.height ='auto'\n }\n } </script> </body> </html>";
    public static final String qiniu_domain = "https://img.linhuiba.com/";//资源发布token
    //邻汇吧下载地址
    public static final String DOWNLOAD_LINHUIBA_URL = "https://www.linhuiba.com/b";
    //服务商我要入住
    public static final String SERVICE_PROVIDER_SETTLED_URL = "https://hz.linhuiba.com/activities/activity-page";
    //图文详情url
    public static String FIELDINFO_PIC_WORD_URL = "/#/appImgDescription";
    //加载web显示各种页面的判断
    public static final int ABOUT_WEB_INT = 0;//关于我们
    public static final int HELP_WEB_INT = 1;//帮助中心
    public static final int BUSINESS_COMPANY_WEB_INT = 2;//商家公司信息
    public static final int POINT_INFO_WEB_INT = 3;//积分详情
    public static final int POINT_RULE_WEB_INT = 4;//积分规则
    public static final int GUARD_WEB_INT = 5;//通知保安
    public static final int RESOURCE_INFO_WEB_INT = 6;//资源详情
    public static final int REGISTER_AGREEMENT_WEB_INT = 7;//注册协议
    public static final int REPORT_WEB_INT = 8;//首页通知 报告
    public static final int COMMON_WEB_INT = 9;//普通直接加载web
    public static final int INVOICE_INFO_WEB_INT = 10;//票据详情
    public static final int WALLET_PLAIN_WEB_INT = 11;//钱包说明
    public static final int HOME_NEW_SIGN_INT = 12;//合作案例
    public static final int NUMBER_OF_PEOPLE_SHOW_DIRECTION_INT = 13;//人流展示方向
    public static final int ADD_DEMAND_WEB_INT = 14;//发布需求
    public static final int DEMAND_LIST_WEB_INT = 15;//需求列表
    public static final int TRANSACTION_VOUCHER_INT = 16;//邻汇吧交易凭证
    public static final int ORDER_INFO_INT = 17;//物业订单详情
    public static final int FACILITATOR_INT = 19;//优质服务商
    public static final int THEMATIC_INT = 20;//特色主题展
    public static final int GROUP_EDSC_INT = 21;//查看拼团流程详情
    public static final int GRADE_INT = 22;//会员等级
    public static final int BIG_ORDER_INFO_INT = 23;//不是待支付大订单详情
    public static final int SMALL_ORDER_INFO_INT = 24;//订单行详情
    public static final int DEAL_VOUCHER_INT = 25;//查看交易凭证
    public static final int SUBMITTED_BIG_ORDER_INFO_INT = 26;//待支付大订单详情
    public static final int DEMAND_INFO_WEB_INT = 27;//需求详情
    public static final int THEMATIC_INFO_INT = 28;//特色主题展详情
    public static final int FACILITATOR_INFO_INT = 29;//优质服务商详情
    public static final int FACILITATOR_CASE_INFO_INT = 30;//优质服务案例商详情
    public static final int ENQUIRY_WEB_INT = 31;//询价
    public static final int ENQUIRY_LIST_WEB_INT = 32;//询价列表
    public static final int ENQUIRY_INFO_WEB_INT = 33;//询价详情
    public static final int RECEIVE_ACCOUNT_INT = 34;//收款账号
    public static final int APPLY_FOR_RELEASE_INT = 35;//申请发布权限
    public static final int COMPANY_INFO_INT = 36;//企业信息
    public static final int ADD_ACCOUNT_INT = 37;//新增收款账号
    public static final int MODIFY_MOBILE_INT = 38;//修改手机号
    public static final int ENTERPRISE_CERTIFICATE_INT = 39;//企业认证
    public static final int SITE_INTENNTION_INT = 40;//场地意向
    public static final int HTML_LOAD_INT = 41;//html 加载 动态和经典案例
    public static final int COUPON_REGULATION_WEB_INT = 42;//优惠券规则
    public static final int INTEGRAL_EXCHANGE_COUPON_REGULATION_WEB_INT = 43;//积分兑换优惠券规则
    public static final int NEW_GIFT_BAG_REGULATION_WEB_INT = 44;//新人礼包活动规则
    public static final int THEME_WEB_INT = 45;//专题详情
    public static final int ENQUIRY_SUCCESS_WEB_INT = 46;//询价成功
    public static final int PANORAMA_WEB_INT = 47;//全景加载
    public static final int DEMAND_HALL_WEB_INT = 48;//需求大厅
    //跳转详情类型
    public static final String JUMP_COMMUNITY_RES = "community_resource";//场地
    public static final String JUMP_PHYSICAL_RES = "physical_resource";//展位
    public static final String JUMP_SELLING_RES = "selling_resource";//供给
    public static void setDefaultConfigUrl() {
        BASE_API_URL_PHP = BuildConfig.DEBUG?BASE_API_URL_PHP_SE:BASE_API_URL_PHP_PE;
        Domain_Name = BuildConfig.DEBUG?Domain_Name_SE:Domain_Name_PE;
        com.linhuiba.linhuifield.config.Config.BASE_API_URL_PHP = BASE_API_URL_PHP;
        com.linhuiba.linhuifield.config.Config.Domain_Name = Domain_Name;
    }
}
