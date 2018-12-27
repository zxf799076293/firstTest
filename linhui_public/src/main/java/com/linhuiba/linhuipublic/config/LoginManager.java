package com.linhuiba.linhuipublic.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.linhuiba.linhuipublic.callbackmodel.ConfigInfo;
import com.linhuiba.linhuipublic.callbackmodel.LoginInfo;
import com.securepreferences.SecurePreferences;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by chentefu on 15/12/22.
 */
public class LoginManager {

    static LoginManager sInstance;
    private LoginInfo mLoginInfo;
    private ConfigInfo mConfigInfo;
    private static Context context;
   // private List<JoinStatus> mJoinStatus;

    private static SecurePreferences sp;
    public static String ServicephoneNumber = "4001028028";
    public static String Servicetime = "09:00--18:00";
    public static String Banner = "[{\"link\":\"https://hz.linhuiba.com/fields/index?mode=imagelist&minimum_households=&maximum_households=&lowPrice=&highPrice=&minimum_peoples=&maximum_peoples=&minimum_property_costs=&maximum_property_costs=&minimum_build_year=&maximum_build_year=&minimum_house_price=&maximum_house_price=&minimum_area=&keywords=&field_type_id=12\",\"pic_url\":\"https://banner.linhuiba.com/Fm3oRadOzCVLcO79y_5NBHUeZHqj\"},{\"link\":\"http://mp.weixin.qq.com/s/_ezJpS6tT6nZlt-wR8w3RA\",\"pic_url\":\"https://banner.linhuiba.com/FnyHmnB-XM4zrdTkZkUbWbXPN7YW\"},{\"link\":\"https://bj.linhuiba.com/fields/index?is_mobile=1\",\"pic_url\":\"https://banner.linhuiba.com/FjpHIcljU7TFIkZBGVctI0QeXiNd\"},{\"link\":\"http://m.linhuiba.com/fields/view/11581\",\"pic_url\":\"https://banner.linhuiba.com/FnEfX0dI2aERjG9Ic83s7wmaqEnN\"},{\"link\":\"http://m.linhuiba.com/fields/view/4486\",\"pic_url\":\"https://banner.linhuiba.com/FnFLEt2wlVCmDdmyJdP9X3Z1IAa6\"},{\"link\":\"https://m.linhuiba.com/activities/index\",\"pic_url\":\"https://banner.linhuiba.com/FifasVl8Fp1zeN99SEMcddTccy-i\"},{\"link\":\"http://mp.weixin.qq.com/s/5MTrFdOeblwy_mCmuqppMg\",\"pic_url\":\"https://banner.linhuiba.com/Fo2YMHjVS1kJo74UfMCx3txm4SIA\"}]";
    public static String Citylist;
    public static String Fieldsort = "[{\"default_sort\":1,\"display_name\":\"默认排序\",\"display_short_name\":\"默认\",\"order\":\"desc\",\"order_by\":\"weight_score\"},{\"default_sort\":0,\"display_name\":\"距离从近到远\",\"display_short_name\":\"距离\",\"order\":\"asc\",\"order_by\":\"distance\"},{\"default_sort\":0,\"display_name\":\"销量从高到低\",\"display_short_name\":\"销量\",\"order\":\"desc\",\"order_by\":\"order_quantity\"},{\"default_sort\":0,\"display_name\":\"建筑年份从近到远\",\"display_short_name\":\"建筑年份\",\"order\":\"desc\",\"order_by\":\"build_year\"},{\"default_sort\":0,\"display_name\":\"建筑年份从远到近\",\"display_short_name\":\"建筑年份\",\"order\":\"asc\",\"order_by\":\"build_year\"}]";
    public static String Avdsort = "[{\"default_sort\":1,\"display_name\":\"综合排序\",\"display_short_name\":\"综合\",\"order\":\"desc\",\"order_by\":\"default_sort\"},{\"default_sort\":0,\"display_name\":\"性价比(人流量/价格)优先\",\"display_short_name\":\"性价比\",\"order\":\"desc\",\"order_by\":\"cost_performance\"},{\"default_sort\":0,\"display_name\":\"人流量优先\",\"display_short_name\":\"人流量\",\"order\":\"desc\",\"order_by\":\"number_of_people\"},{\"default_sort\":0,\"display_name\":\"最新发布\",\"display_short_name\":\"最新\",\"order\":\"desc\",\"order_by\":\"created_at\"},{\"default_sort\":0,\"display_name\":\"最早发布\",\"display_short_name\":\"最早\",\"order\":\"asc\",\"order_by\":\"created_at\"},{\"default_sort\":0,\"display_name\":\"销量优先\",\"display_short_name\":\"销量\",\"order\":\"desc\",\"order_by\":\"number_of_order\"}]";
    public static String Activitysort = "[{\"default_sort\":0,\"display_name\":\"性价比(人流量/价格)优先\",\"display_short_name\":\"性价比\",\"order\":\"desc\",\"order_by\":\"cost_performance\"},{\"default_sort\":0,\"display_name\":\"人流量优先\",\"display_short_name\":\"人流量\",\"order\":\"desc\",\"order_by\":\"number_of_people\"},{\"default_sort\":1,\"display_name\":\"最新发布\",\"display_short_name\":\"最新\",\"order\":\"desc\",\"order_by\":\"activity_start_date\"},{\"default_sort\":0,\"display_name\":\"最早发布\",\"display_short_name\":\"最早\",\"order\":\"asc\",\"order_by\":\"activity_start_date\"},{\"default_sort\":0,\"display_name\":\"销量优先\",\"display_short_name\":\"销量\",\"order\":\"desc\",\"order_by\":\"number_of_order\"}]";
    public static String Fieldtype = "[{\"display_name\":\"住宅\",\"field_type_id\":1},{\"display_name\":\"写字楼\",\"field_type_id\":2},{\"display_name\":\"园区\",\"field_type_id\":14},{\"display_name\":\"商场\",\"field_type_id\":10},{\"display_name\":\"超市\",\"field_type_id\":3},{\"display_name\":\"影院\",\"field_type_id\":8},{\"display_name\":\"地铁\",\"field_type_id\":12},{\"display_name\":\"餐厅\",\"field_type_id\":5},{\"display_name\":\"学校\",\"field_type_id\":4},{\"display_name\":\"广场\",\"field_type_id\":6},{\"display_name\":\"大企业\",\"field_type_id\":11},{\"display_name\":\"交通枢纽\",\"field_type_id\":13},{\"display_name\":\"步行街\",\"field_type_id\":15},{\"display_name\":\"景区\",\"field_type_id\":17},{\"display_name\":\"众创空间\",\"field_type_id\":19},{\"display_name\":\"其他\",\"field_type_id\":9}]";
    public static String Version = "1";
    public static String Ad_types = "[{\"code\":8,\"type\":\"其他\"},{\"code\":7,\"type\":\"大牌\"},{\"code\":6,\"type\":\"电梯横媒体\"},{\"code\":5,\"type\":\"微信打印机\"},{\"code\":4,\"type\":\"墙壁框架\"},{\"code\":2,\"type\":\"公告栏\"},{\"code\":1,\"type\":\"电梯\"}]";
    public static String Term_types = "[{\"display_name\":\"天(工作日)\",\"m_lease_term_type_id\":1},{\"display_name\":\"天(周末)\",\"m_lease_term_type_id\":2},{\"display_name\":\"周\",\"m_lease_term_type_id\":3},{\"display_name\":\"月\",\"m_lease_term_type_id\":4},{\"display_name\":\"年\",\"m_lease_term_type_id\":5},{\"display_name\":\"半月\",\"m_lease_term_type_id\":6},{\"display_name\":\"半年\",\"m_lease_term_type_id\":7},{\"display_name\":\"三个月\",\"m_lease_term_type_id\":8}]";
    public static String Invoice_content = "[{\"invoice_content_id\":5,\"name\":\"信息服务费\"},{\"invoice_content_id\":6,\"name\":\"技术服务费\"},{\"invoice_content_id\":7,\"name\":\"代订场地服务费\"},{\"invoice_content_id\":8,\"name\":\"技术咨询服务费\"},{\"invoice_content_id\":9,\"name\":\"场地信息服务费\"}]";
    public static String Field_review_tags = "[{\"display_name\":\"参与度高\",\"tag_id\":1},{\"display_name\":\"摆摊位置好\",\"tag_id\":2},{\"display_name\":\"物业态度好\",\"tag_id\":3},{\"display_name\":\"适合下载APP\",\"tag_id\":4},{\"display_name\":\"适合公众号关注\",\"tag_id\":5},{\"display_name\":\"适合售卖类销售\",\"tag_id\":6}]";
    public static String Facility_tags = "[{\"dispaly_name\":\"有电\",\"tag\":\"hasPower\"},{\"dispaly_name\":\"提供桌椅\",\"tag\":\"hasChair\"},{\"dispaly_name\":\"提供帐篷\",\"tag\":\"has_tent\"},{\"dispaly_name\":\"有车位\",\"tag\":\"has_carport\"},{\"dispaly_name\":\"物料过夜\",\"tag\":\"goodsHelp\"},{\"dispaly_name\":\"传单\",\"tag\":\"leaflet\"},{\"dispaly_name\":\"免税开票\",\"tag\":\"zero_tax_point\"}]";
    public static String Community_types = "[{\"display_name\":\"住宅\",\"id\":1},{\"display_name\":\"写字楼\",\"id\":2},{\"display_name\":\"超市\",\"id\":3},{\"display_name\":\"学校\",\"id\":4},{\"display_name\":\"餐厅\",\"id\":5},{\"display_name\":\"广场\",\"id\":6},{\"display_name\":\"影院\",\"id\":8},{\"display_name\":\"其他\",\"id\":9},{\"display_name\":\"商场\",\"id\":10},{\"display_name\":\"大企业\",\"id\":11},{\"display_name\":\"地铁\",\"id\":12},{\"display_name\":\"交通枢纽\",\"id\":13},{\"display_name\":\"园区\",\"id\":14},{\"display_name\":\"步行街\",\"id\":15},{\"display_name\":\"景区\",\"id\":16},{\"display_name\":\"众创空间\",\"id\":17}]";
    public static String Field_labels = "[{\"display_name\":\"邻汇测评\",\"id\":28,\"name\":\"\"},{\"display_name\":\"绿城物业\",\"id\":50,\"name\":\"\"},{\"display_name\":\"保利物业社区\",\"id\":52,\"name\":\"\"},{\"display_name\":\"彩生活物业\",\"id\":57,\"name\":\"\"},{\"display_name\":\"滨江物业\",\"id\":141,\"name\":\"\"},{\"display_name\":\"金地物业\",\"id\":142,\"name\":\"\"},{\"display_name\":\"远洋物业\",\"id\":143,\"name\":\"\"},{\"display_name\":\"场地认证\",\"id\":161,\"name\":\"\"},{\"display_name\":\"北方物业\",\"id\":168,\"name\":\"\"},{\"display_name\":\"我要获客\",\"id\":179,\"name\":\"\"},{\"display_name\":\"我要获客\",\"id\":180,\"name\":\"\"},{\"display_name\":\"我要获客\",\"id\":181,\"name\":\"\"},{\"display_name\":\"我要获客\",\"id\":182,\"name\":\"\"},{\"display_name\":\"消费能力高\",\"id\":183,\"name\":\"\"},{\"display_name\":\"消费能力高\",\"id\":184,\"name\":\"\"},{\"display_name\":\"消费能力高\",\"id\":185,\"name\":\"\"},{\"display_name\":\"消费能力高\",\"id\":186,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":187,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":188,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":189,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":190,\"name\":\"\"},{\"display_name\":\"我要卖货\",\"id\":191,\"name\":\"\"},{\"display_name\":\"我要卖货\",\"id\":192,\"name\":\"\"},{\"display_name\":\"我要卖货\",\"id\":193,\"name\":\"\"},{\"display_name\":\"我要卖货\",\"id\":194,\"name\":\"\"},{\"display_name\":\"高人气场地\",\"id\":195,\"name\":\"\"},{\"display_name\":\"高人气场地\",\"id\":196,\"name\":\"\"},{\"display_name\":\"高人气场地\",\"id\":197,\"name\":\"\"},{\"display_name\":\"高人气场地\",\"id\":198,\"name\":\"\"},{\"display_name\":\"金融理财\",\"id\":199,\"name\":\"\"},{\"display_name\":\"金融理财\",\"id\":200,\"name\":\"\"},{\"display_name\":\"金融理财\",\"id\":201,\"name\":\"\"},{\"display_name\":\"金融理财\",\"id\":202,\"name\":\"\"},{\"display_name\":\"银行\",\"id\":203,\"name\":\"\"},{\"display_name\":\"银行\",\"id\":204,\"name\":\"\"},{\"display_name\":\"银行\",\"id\":205,\"name\":\"\"},{\"display_name\":\"银行\",\"id\":206,\"name\":\"\"},{\"display_name\":\"大面积场地\",\"id\":207,\"name\":\"\"},{\"display_name\":\"大面积场地\",\"id\":208,\"name\":\"\"},{\"display_name\":\"大面积场地\",\"id\":209,\"name\":\"\"},{\"display_name\":\"大面积场地\",\"id\":210,\"name\":\"\"},{\"display_name\":\"特卖场地\",\"id\":211,\"name\":\"\"},{\"display_name\":\"特卖场地\",\"id\":212,\"name\":\"\"},{\"display_name\":\"特卖场地\",\"id\":213,\"name\":\"\"},{\"display_name\":\"特卖场地\",\"id\":214,\"name\":\"\"},{\"display_name\":\"机器设备\",\"id\":215,\"name\":\"\"},{\"display_name\":\"机器设备\",\"id\":216,\"name\":\"\"},{\"display_name\":\"机器设备\",\"id\":217,\"name\":\"\"},{\"display_name\":\"机器设备\",\"id\":218,\"name\":\"\"},{\"display_name\":\"机器设备\",\"id\":219,\"name\":\"\"},{\"display_name\":\"特卖场地\",\"id\":220,\"name\":\"\"},{\"display_name\":\"我要卖货\",\"id\":221,\"name\":\"\"},{\"display_name\":\"金融理财\",\"id\":222,\"name\":\"\"},{\"display_name\":\"房展\",\"id\":225,\"name\":\"\"},{\"display_name\":\"房展\",\"id\":226,\"name\":\"\"},{\"display_name\":\"房展\",\"id\":227,\"name\":\"\"},{\"display_name\":\"房展\",\"id\":228,\"name\":\"\"},{\"display_name\":\"房展\",\"id\":229,\"name\":\"\"},{\"display_name\":\"车展\",\"id\":230,\"name\":\"\"},{\"display_name\":\"车展\",\"id\":231,\"name\":\"\"},{\"display_name\":\"车展\",\"id\":232,\"name\":\"\"},{\"display_name\":\"车展\",\"id\":233,\"name\":\"\"},{\"display_name\":\"车展\",\"id\":234,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":235,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":236,\"name\":\"\"},{\"display_name\":\"教育培训\",\"id\":237,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":238,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":239,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":240,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":241,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":242,\"name\":\"\"},{\"display_name\":\"美食饮料\",\"id\":244,\"name\":\"\"},{\"display_name\":\"美食饮料\",\"id\":245,\"name\":\"\"},{\"display_name\":\"美食饮料\",\"id\":246,\"name\":\"\"},{\"display_name\":\"美食饮料\",\"id\":247,\"name\":\"\"},{\"display_name\":\"健康医疗\",\"id\":248,\"name\":\"\"},{\"display_name\":\"健康医疗\",\"id\":249,\"name\":\"\"},{\"display_name\":\"健康医疗\",\"id\":250,\"name\":\"\"},{\"display_name\":\"健康医疗\",\"id\":251,\"name\":\"\"},{\"display_name\":\"健康医疗\",\"id\":252,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":253,\"name\":\"\"},{\"display_name\":\"零食货架\",\"id\":254,\"name\":\"\"},{\"display_name\":\"美食饮料\",\"id\":255,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":256,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":257,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":258,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":259,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":260,\"name\":\"\"},{\"display_name\":\"品牌快闪\",\"id\":261,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":262,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":263,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":264,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":265,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":266,\"name\":\"\"},{\"display_name\":\"品牌展厅\",\"id\":267,\"name\":\"\"},{\"display_name\":\"我要获客\",\"id\":268,\"name\":\"\"},{\"display_name\":\"专题-金融理财专场\",\"id\":269,\"name\":\"专题-金融理财专场\"},{\"display_name\":\"专题-房产展示必选\",\"id\":270,\"name\":\"专题-房产展示必选\"},{\"display_name\":\"专题-冬日暖心写字楼\",\"id\":271,\"name\":\"专题-冬日暖心写字楼\"},{\"display_name\":\"专题-特卖场地荟萃\",\"id\":272,\"name\":\"专题-特卖场地荟萃\"},{\"display_name\":\"专题-高性价比写字楼场地\",\"id\":273,\"name\":\"专题-高性价比写字楼场地\"},{\"display_name\":\"专题-高端优质住宅场地\",\"id\":274,\"name\":\"专题-高端优质住宅场地\"},{\"display_name\":\"专题-特卖场地TOP\",\"id\":275,\"name\":\"专题-特卖场地TOP\"},{\"display_name\":\"专题-打包场地更划算\",\"id\":276,\"name\":\"专题-打包场地更划算\"},{\"display_name\":\"专题-火热销量场地系列之新零售篇\",\"id\":277,\"name\":\"专题-火热销量场地系列之新零售篇\"},{\"display_name\":\"专题-人气特卖场地抢购\",\"id\":278,\"name\":\"专题-人气特卖场地抢购\"},{\"display_name\":\"专题-路演活动必选场地私荐\",\"id\":279,\"name\":\"专题-路演活动必选场地私荐\"},{\"display_name\":\"专题-车展精选宝地\",\"id\":280,\"name\":\"专题-车展精选宝地\"},{\"display_name\":\"专题-商场中庭活动专区\",\"id\":281,\"name\":\"专题-商场中庭活动专区\"},{\"display_name\":\"专题-超市包月推广专区\",\"id\":282,\"name\":\"专题-超市包月推广专区\"},{\"display_name\":\"专题-写字楼大面积售卖专区\",\"id\":283,\"name\":\"专题-写字楼大面积售卖专区\"},{\"display_name\":\"专题-医疗美容专区\",\"id\":284,\"name\":\"专题-医疗美容专区\"},{\"display_name\":\"专题-室内人气展位\",\"id\":285,\"name\":\"专题-室内人气展位\"},{\"display_name\":\"专题-CBD热门商圈\",\"id\":286,\"name\":\"专题-CBD热门商圈\"},{\"display_name\":\"专题-鹏城超高人气社区\",\"id\":287,\"name\":\"专题-鹏城超高人气社区\"},{\"display_name\":\"专题-高端写字楼精英聚集地 \",\"id\":288,\"name\":\"专题-高端写字楼精英聚集地 \"},{\"display_name\":\"专题-都市商务精英专区\",\"id\":291,\"name\":\"专题-都市商务精英专区\"},{\"display_name\":\"专题-市民之家专区\",\"id\":292,\"name\":\"专题-市民之家专区\"},{\"display_name\":\"专题-火爆人气商圈专区\",\"id\":293,\"name\":\"专题-火爆人气商圈专区\"},{\"display_name\":\"专题-高校联盟专区\",\"id\":294,\"name\":\"专题-高校联盟专区\"},{\"display_name\":\"我要卖货\",\"id\":295,\"name\":\"我要卖货\"},{\"display_name\":\"我要获客\",\"id\":296,\"name\":\"我要获客\"},{\"display_name\":\"品牌快闪\",\"id\":297,\"name\":\"品牌快闪\"},{\"display_name\":\"我要卖货\",\"id\":298,\"name\":\"我要卖货\"},{\"display_name\":\"我要获客\",\"id\":299,\"name\":\"我要获客\"},{\"display_name\":\"车展\",\"id\":300,\"name\":\"车展\"},{\"display_name\":\"房展\",\"id\":301,\"name\":\"房展\"},{\"display_name\":\"大型特卖\",\"id\":302,\"name\":\"大型特卖\"},{\"display_name\":\"品牌快闪\",\"id\":303,\"name\":\"品牌快闪\"},{\"display_name\":\"零食货架\",\"id\":304,\"name\":\"零食货架\"},{\"display_name\":\"信用卡\",\"id\":305,\"name\":\"信用卡\"}]";
    public static String is_outdoor = "[{\"display_name\":\"室外\",\"tag\":0},{\"display_name\":\"室内\",\"tag\":1}]";
    public static String activity_types = "[{\"code\":3,\"type\":\"峰会\"},{\"code\":2,\"type\":\"公益\"},{\"code\":1,\"type\":\"市集\"}]";
    public static String hotCotyList = "[{\"domain_name\":\"bj.lanhanba.net\",\"en_name\":\"beijingshi\",\"id\":1,\"name\":\"北京市\",\"province_id\":1,\"province_name\":\"北京市\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"sh.lanhanba.net\",\"en_name\":\"shanghaishi\",\"id\":75,\"name\":\"上海市\",\"province_id\":9,\"province_name\":\"上海市\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"nj.lanhanba.net\",\"en_name\":\"nanjingshi\",\"id\":77,\"name\":\"南京市\",\"province_id\":10,\"province_name\":\"江苏省\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"su.lanhanba.net\",\"en_name\":\"suzhoushi\",\"id\":81,\"name\":\"苏州市\",\"province_id\":10,\"province_name\":\"江苏省\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"hz.lanhanba.net\",\"en_name\":\"hangzhoushi\",\"id\":90,\"name\":\"杭州市\",\"province_id\":11,\"province_name\":\"浙江省\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"gz.lanhanba.net\",\"en_name\":\"guangzhoushi\",\"id\":200,\"name\":\"广州市\",\"province_id\":19,\"province_name\":\"广东省\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"sz.lanhanba.net\",\"en_name\":\"shenzhenshi\",\"id\":202,\"name\":\"深圳市\",\"province_id\":19,\"province_name\":\"广东省\",\"service_phone\":[\"4001028028\"]},{\"domain_name\":\"cd.lanhanba.net\",\"en_name\":\"chengdushi\",\"id\":240,\"name\":\"成都市\",\"province_id\":23,\"province_name\":\"四川省\",\"service_phone\":[\"4001028028\"]}]";
    public static String DEVELOPER = "[{\"display_name\":\"恒大地产\",\"id\":1,\"seq\":49},{\"display_name\":\"万科\",\"id\":2,\"seq\":62},{\"display_name\":\"保利\",\"id\":3,\"seq\":63},{\"display_name\":\"绿地\",\"id\":4,\"seq\":64},{\"display_name\":\"绿城\",\"id\":5,\"seq\":65},{\"display_name\":\"其他\",\"id\":6,\"seq\":66},{\"display_name\":\"万达电影\",\"id\":7,\"seq\":67},{\"display_name\":\"中影星美\",\"id\":8,\"seq\":68},{\"display_name\":\"联和院线\",\"id\":9,\"seq\":69},{\"display_name\":\"大地影院\",\"id\":10,\"seq\":70},{\"display_name\":\"中影南方\",\"id\":11,\"seq\":61},{\"display_name\":\"金逸影视\",\"id\":12,\"seq\":60},{\"display_name\":\"横店影视\",\"id\":13,\"seq\":59},{\"display_name\":\"中影数字\",\"id\":14,\"seq\":50},{\"display_name\":\"新影联\",\"id\":15,\"seq\":51},{\"display_name\":\"时代院线\",\"id\":16,\"seq\":52},{\"display_name\":\"比高\",\"id\":17,\"seq\":53},{\"display_name\":\"UME\",\"id\":18,\"seq\":54},{\"display_name\":\"太平洋院线\",\"id\":19,\"seq\":55},{\"display_name\":\"幸福蓝海OMNIJOI\",\"id\":20,\"seq\":56},{\"display_name\":\"奥斯卡院线OSCAR\",\"id\":21,\"seq\":57},{\"display_name\":\"保利影院\",\"id\":22,\"seq\":58},{\"display_name\":\"橙天嘉禾\",\"id\":23,\"seq\":71},{\"display_name\":\"中影北方THEATRES\",\"id\":24,\"seq\":72},{\"display_name\":\"博纳影业BONA\",\"id\":25,\"seq\":85},{\"display_name\":\"恒大影城\",\"id\":26,\"seq\":86},{\"display_name\":\"世纪环球\",\"id\":27,\"seq\":87},{\"display_name\":\"紫荆\",\"id\":28,\"seq\":88},{\"display_name\":\"耀莱成龙\",\"id\":29,\"seq\":89},{\"display_name\":\"沃尔玛\",\"id\":30,\"seq\":90},{\"display_name\":\"家乐福\",\"id\":31,\"seq\":91},{\"display_name\":\"苏宁易购\",\"id\":32,\"seq\":92},{\"display_name\":\"国美\",\"id\":33,\"seq\":93},{\"display_name\":\"华润万家\",\"id\":34,\"seq\":84},{\"display_name\":\"大润发\",\"id\":35,\"seq\":83},{\"display_name\":\"百联\",\"id\":36,\"seq\":82},{\"display_name\":\"永辉超市\",\"id\":37,\"seq\":73},{\"display_name\":\"物美\",\"id\":38,\"seq\":74},{\"display_name\":\"中百\",\"id\":39,\"seq\":75},{\"display_name\":\"天虹\",\"id\":40,\"seq\":76},{\"display_name\":\"麦德龙\",\"id\":41,\"seq\":77},{\"display_name\":\"步步高\",\"id\":42,\"seq\":78},{\"display_name\":\"华联超市\",\"id\":43,\"seq\":79},{\"display_name\":\"王府井百货\",\"id\":44,\"seq\":80},{\"display_name\":\"苏果\",\"id\":45,\"seq\":81},{\"display_name\":\"红旗连锁\",\"id\":46,\"seq\":94},{\"display_name\":\"大商\",\"id\":47,\"seq\":3},{\"display_name\":\"银座\",\"id\":48,\"seq\":16},{\"display_name\":\"人人乐\",\"id\":49,\"seq\":17},{\"display_name\":\"欧亚商都\",\"id\":51,\"seq\":18},{\"display_name\":\"重庆百货\",\"id\":52,\"seq\":19},{\"display_name\":\"茂业\",\"id\":53,\"seq\":20},{\"display_name\":\"农工商超市\",\"id\":54,\"seq\":21},{\"display_name\":\"五星电器\",\"id\":55,\"seq\":22},{\"display_name\":\"卜蜂莲花\",\"id\":56,\"seq\":23},{\"display_name\":\"春天百货\",\"id\":57,\"seq\":24},{\"display_name\":\"欧尚\",\"id\":58,\"seq\":15},{\"display_name\":\"西单商场\",\"id\":59,\"seq\":14},{\"display_name\":\"AEON永旺\",\"id\":60,\"seq\":13},{\"display_name\":\"塔吉特\",\"id\":61,\"seq\":5},{\"display_name\":\"华联\",\"id\":62,\"seq\":4},{\"display_name\":\"新华百货\",\"id\":63,\"seq\":6},{\"display_name\":\"新华都\",\"id\":64,\"seq\":7},{\"display_name\":\"合肥百货\",\"id\":65,\"seq\":8},{\"display_name\":\"COSTCO好市多\",\"id\":66,\"seq\":9},{\"display_name\":\"三江购物\",\"id\":67,\"seq\":10},{\"display_name\":\"Coop\",\"id\":68,\"seq\":11},{\"display_name\":\"\\t 家家悦\",\"id\":69,\"seq\":12},{\"display_name\":\"新一佳\",\"id\":70,\"seq\":25},{\"display_name\":\"碧桂园\",\"id\":71,\"seq\":26},{\"display_name\":\"融创\",\"id\":72,\"seq\":39},{\"display_name\":\"中海\",\"id\":73,\"seq\":40},{\"display_name\":\"龙湖\",\"id\":74,\"seq\":41},{\"display_name\":\"华润\",\"id\":75,\"seq\":42},{\"display_name\":\"华夏幸福\",\"id\":76,\"seq\":43},{\"display_name\":\"金地\",\"id\":77,\"seq\":44},{\"display_name\":\"万达\",\"id\":78,\"seq\":45},{\"display_name\":\"新城地产\",\"id\":79,\"seq\":46},{\"display_name\":\"招商蛇口 \",\"id\":80,\"seq\":47},{\"display_name\":\"世茂\",\"id\":81,\"seq\":38},{\"display_name\":\"\\t 富力\",\"id\":82,\"seq\":37},{\"display_name\":\"旭辉 \",\"id\":83,\"seq\":36},{\"display_name\":\"泰禾\",\"id\":84,\"seq\":27},{\"display_name\":\"首开\",\"id\":85,\"seq\":28},{\"display_name\":\"阳光城\",\"id\":86,\"seq\":29}]";
    LoginManager() {}
    public static void init(Context appContext) {
        sp = new SecurePreferences(appContext);
        getInstance().context = appContext;
        Citylist = readAssetsFileString(context,"citylist").trim();
        String appversion = sp.getString("app2.0", "");
        if (sp.getString("app2.0", "") ==null || sp.getString("app2.0", "").length() ==0 || !sp.getString("app2.0","").equals("3.6.1.1")) {
            sp.edit()
                    .putString(CommonValue.Servicetime, Servicetime)
                    .putString(CommonValue.ServicephoneNumber, ServicephoneNumber)
                    .putString(CommonValue.Fieldtype, Fieldtype)
                    .putString(CommonValue.Fieldsort, Fieldsort)
                    .putString(CommonValue.Avdsort, Avdsort)
                    .putString(CommonValue.Activitysort, Activitysort)
                    .putString(CommonValue.Citylist, Citylist)
                    .putString(CommonValue.Banner, Banner)
                    .putString(CommonValue.Version, Version)
                    .putString(CommonValue.Ad_types, Ad_types)
                    .putString(CommonValue.Term_types,Term_types)
                    .putString(CommonValue.Invoice_content, Invoice_content)
                    .putString(CommonValue.Field_review_tags,Field_review_tags)
                    .putString(CommonValue.Facility_tags,Facility_tags)
                    .putString(CommonValue.Community_types,Community_types)
                    .putString(CommonValue.Field_labels,Field_labels)
                    .putString(CommonValue.IS_OUTDOOR,is_outdoor)
                    .putString(CommonValue.ACTIVITY_TYPES,activity_types)
                    .putString(CommonValue.HOT_CITYLIST,hotCotyList)
                    .putString(CommonValue.DEVELOPER,DEVELOPER)
                    .putString("app2.0", "3.6.1.1")
                    .putString("currentcitycode", "")
                    .putString("currentcity", "")
                    .putLong("Config_updatetime", 0)
                    .apply();
        }
    }

    public static LoginManager getInstance() {
        if (sInstance == null) {
            sInstance = new LoginManager();
        }
        return sInstance;
    }

    public synchronized static void saveLogin() {
        if (isLogin()) {
            LoginInfo login = getInstance().mLoginInfo;
            sp.edit()
                    .putString(CommonValue.GROUP, login.getRole_id())
                    .putString(CommonValue.UID, login.getId())
                    .putString(CommonValue.UNAME, login.getUser_name())
                    .putString(CommonValue.PROJECTNAME, login.getProject_name())
                    .putString(CommonValue.MOBILE, login.getMobile())
                    .putString(CommonValue.CONTACT, login.getContact())
                    .putString(CommonValue.EMAIL, login.getEmail())
                    .putString(CommonValue.NAME, login.getName())
                    .putString(CommonValue.ADDRESS, login.getAddress())
                    .putString(CommonValue.ACCESSTOKEN, login.getApikey())
                    .putString(CommonValue.INVITE, login.getInvite_code())
                    .putString(CommonValue.COMPANYNAME, login.getCompany())
                    .putString(CommonValue.PRODUCTNAME, login.getProduct())
                    .putString(CommonValue.CONSUMPTION_POINT, login.getConsumption_point())
                    .putInt(CommonValue.ENTERPRISE_AUTHORISE_STATUS, login.getEnterprise_authorize_status())
                    .putInt(CommonValue.ENTERPRISE_ROLE, login.getEnterprise_role())
                    .putString(CommonValue.AVATAR, login.getAvatar())
                    .putBoolean(CommonValue.WECHAT_BOUND,login.isWechat_bound())
                    .putBoolean(CommonValue.HAS_CONTACTS,login.isHas_contracts())
                    .putInt(CommonValue.RELATION_ID, login.getRelation_id())
                    .putInt(CommonValue.ORDERING_NOTICED, login.getOrdering_noticed())
                    .apply();
        }
    }
    public synchronized static void saveUserProfile(LoginInfo loginInfo) {
        if (isLogin()) {
            LoginInfo login = loginInfo;
            sp.edit()
                    .putString(CommonValue.MOBILE, login.getMobile())
                    .putString(CommonValue.EMAIL, login.getEmail())
                    .putString(CommonValue.NAME, login.getName())
                    .putString(CommonValue.COMPANYNAME, login.getCompany())
                    .putString(CommonValue.PRODUCTNAME, login.getProduct())
                    .putString(CommonValue.AVATAR, login.getAvatar())
                    .putBoolean(CommonValue.HAS_CONTACTS,login.isHas_contracts())
                    .putInt(CommonValue.RELATION_ID, login.getRelation_id())
                    .putString(CommonValue.MEMBERSHIP_LEVEL, login.getMembership_level())
                    .putInt(CommonValue.ENTERPRISE_AUTHORISE_STATUS, login.getEnterprise_authorize_status())
                    .putString(CommonValue.GROUP, login.getRole_id())
                    .putBoolean(CommonValue.RIGHT_TO_PUBLISH,login.isRight_to_publish())
                    .putBoolean(CommonValue.IS_SUPPLIER,login.isIs_supplier())
                    .putBoolean(CommonValue.WECHAT_BOUND,login.isWechat_bound())
                    .putBoolean(CommonValue.QQ_BOUND,login.isQq_bound())
                    .apply();
            getInstance().mLoginInfo.setMobile(login.getMobile());
            getInstance().mLoginInfo.setEmail(login.getEmail());
            getInstance().mLoginInfo.setName(login.getName());
            getInstance().mLoginInfo.setCompany(login.getCompany());
            getInstance().mLoginInfo.setProduct(login.getProduct());
            getInstance().mLoginInfo.setAvatar(login.getAvatar());
            getInstance().mLoginInfo.setHas_contracts(login.isHas_contracts());
            getInstance().mLoginInfo.setRelation_id(login.getRelation_id());
            getInstance().mLoginInfo.setMembership_level(login.getMembership_level());
            getInstance().mLoginInfo.setEnterprise_authorize_status(login.getEnterprise_authorize_status());
            getInstance().mLoginInfo.setRole_id(login.getRole_id());
            getInstance().mLoginInfo.setRight_to_publish(login.isRight_to_publish());
            getInstance().mLoginInfo.setIs_supplier(login.isIs_supplier());
            getInstance().mLoginInfo.setWechat_bound(login.isWechat_bound());
            getInstance().mLoginInfo.setQq_bound(login.isQq_bound());
        }
    }

    public void updateConfigInfo(ConfigInfo config) {
        mConfigInfo = config;
        sp.edit()
                .putString(CommonValue.Servicetime, config.getServicetime())
                .putString(CommonValue.ServicephoneNumber, config.getServicephoneNumber())
                .putString(CommonValue.Fieldtype, config.getFieldtype())
                .putString(CommonValue.Fieldsort, config.getResource_sort())
                .putString(CommonValue.Avdsort, config.getAvd_sort())
                .putString(CommonValue.Activitysort, config.getActivity_sort())
                .putString(CommonValue.Citylist, config.getCitylist())
                .putString(CommonValue.Banner, config.getBanner())
                .putString(CommonValue.Version, config.getVersion())
                .putString(CommonValue.Ad_types, config.getAd_types())
                .putString(CommonValue.Term_types,config.getTerm_types())
                .putString(CommonValue.Invoice_content, config.getInvoice_content())
                .putString(CommonValue.Field_review_tags,config.getField_review_tags())
                .putString(CommonValue.Facility_tags,config.getFacility_tags())
                .putString(CommonValue.Community_types,config.getCommunity_type())
                .putString(CommonValue.Field_labels,config.getField_labels())
                .putString(CommonValue.IS_OUTDOOR,config.getIs_outdoor())
                .putString(CommonValue.ACTIVITY_TYPES,config.getActivity_types())
                .putString(CommonValue.HOT_CITYLIST,config.getHot_citylist())
                .putString(CommonValue.DEVELOPER,config.getDeveloper())
                .apply();
    }
    public static AsyncTask readLoginAsync(final Runnable callback) {
        return readLoginAsync(callback, 0);
    }

    public static AsyncTask readLoginAsync(final Runnable callback, final long time) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long start = System.currentTimeMillis();
                readLogin();
                long cost = System.currentTimeMillis() - start;
                if (cost < time) {
                    SystemClock.sleep(time - cost);
                } else {
                    SystemClock.sleep(10);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) callback.run();
            }
        };
        task.execute();
        return task;
    }

    public static AsyncTask readConfigAsync(final Runnable callback) {
        return readLoginAsync(callback, 0);
    }

    public static AsyncTask readConfigAsync(final Runnable callback, final long time) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long start = System.currentTimeMillis();
                readConfig();
                long cost = System.currentTimeMillis() - start;
                if (cost < time) {
                    SystemClock.sleep(time - cost);
                } else {
                    SystemClock.sleep(10);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) callback.run();
            }
        };
        task.execute();
        return task;
    }

    public synchronized static void readConfig() {
        if (getInstance().mConfigInfo == null) {
            getInstance().mConfigInfo = new ConfigInfo();
        }
        getInstance().mConfigInfo.setServicetime(sp.getString(CommonValue.Servicetime, ""));
        getInstance().mConfigInfo.setServicephoneNumber(sp.getString(CommonValue.ServicephoneNumber, ""));
        //默认物业端
        getInstance().mConfigInfo.setFieldtype(sp.getString(CommonValue.Fieldtype, ""));
        getInstance().mConfigInfo.setResource_sort(sp.getString(CommonValue.Fieldsort, ""));
        getInstance().mConfigInfo.setAvd_sort(sp.getString(CommonValue.Avdsort, ""));
        getInstance().mConfigInfo.setActivity_sort(sp.getString(CommonValue.Activitysort, ""));


        getInstance().mConfigInfo.setCitylist(sp.getString(CommonValue.Citylist, ""));
        getInstance().mConfigInfo.setBanner(sp.getString(CommonValue.Banner, ""));
        getInstance().mConfigInfo.setVersion(sp.getString(CommonValue.Version, ""));
        getInstance().mConfigInfo.setAd_types(sp.getString(CommonValue.Ad_types, ""));
        getInstance().mConfigInfo.setTerm_types(sp.getString(CommonValue.Term_types, ""));
        getInstance().mConfigInfo.setInvoice_content(sp.getString(CommonValue.Invoice_content, ""));
        getInstance().mConfigInfo.setField_review_tags(sp.getString(CommonValue.Field_review_tags, ""));
        getInstance().mConfigInfo.setFacility_tags(sp.getString(CommonValue.Facility_tags, ""));
        getInstance().mConfigInfo.setCommunity_type(sp.getString(CommonValue.Community_types, ""));
        getInstance().mConfigInfo.setField_labels(sp.getString(CommonValue.Field_labels, ""));
        getInstance().mConfigInfo.setIs_outdoor(sp.getString(CommonValue.IS_OUTDOOR, ""));
        getInstance().mConfigInfo.setActivity_types(sp.getString(CommonValue.ACTIVITY_TYPES, ""));
        getInstance().mConfigInfo.setHot_citylist(sp.getString(CommonValue.HOT_CITYLIST, ""));
        getInstance().mConfigInfo.setDeveloper(sp.getString(CommonValue.DEVELOPER, ""));
    }
    public synchronized static void readLogin() {
        if (getInstance().mLoginInfo == null) {
            getInstance().mLoginInfo = new LoginInfo();
        }
        //商家端
        getInstance().mLoginInfo.setRole_id(sp.getString(CommonValue.GROUP, ""));
        getInstance().mLoginInfo.setId(sp.getString(CommonValue.UID, ""));
        getInstance().mLoginInfo.setUser_name(sp.getString(CommonValue.UNAME, ""));
        getInstance().mLoginInfo.setProject_name(sp.getString(CommonValue.PROJECTNAME, ""));
        getInstance().mLoginInfo.setMobile(sp.getString(CommonValue.MOBILE, ""));
        getInstance().mLoginInfo.setContact(sp.getString(CommonValue.CONTACT, ""));
        getInstance().mLoginInfo.setEmail(sp.getString(CommonValue.EMAIL, ""));
        getInstance().mLoginInfo.setName(sp.getString(CommonValue.NAME, ""));
        getInstance().mLoginInfo.setAddress(sp.getString(CommonValue.ADDRESS, ""));
        getInstance().mLoginInfo.setApikey(sp.getString(CommonValue.ACCESSTOKEN, ""));
        getInstance().mLoginInfo.setInvite_code(sp.getString(CommonValue.INVITE, ""));
        getInstance().mLoginInfo.setCompany(sp.getString(CommonValue.COMPANYNAME, ""));
        getInstance().mLoginInfo.setProduct(sp.getString(CommonValue.PRODUCTNAME, ""));
        getInstance().mLoginInfo.setConsumption_point(sp.getString(CommonValue.CONSUMPTION_POINT, ""));
        getInstance().mLoginInfo.setEnterprise_authorize_status(sp.getInt(CommonValue.ENTERPRISE_AUTHORISE_STATUS, 0));
        getInstance().mLoginInfo.setEnterprise_role(sp.getInt(CommonValue.ENTERPRISE_ROLE, 0));
        getInstance().mLoginInfo.setAvatar(sp.getString(CommonValue.AVATAR, ""));
        getInstance().mLoginInfo.setWechat_bound(sp.getBoolean(CommonValue.WECHAT_BOUND, false));
        getInstance().mLoginInfo.setHas_contracts(sp.getBoolean(CommonValue.HAS_CONTACTS, false));
        getInstance().mLoginInfo.setRelation_id(sp.getInt(CommonValue.RELATION_ID, 0));
        getInstance().mLoginInfo.setOrdering_noticed(sp.getInt(CommonValue.ORDERING_NOTICED, 0));
        getInstance().mLoginInfo.setMembership_level(sp.getString(CommonValue.MEMBERSHIP_LEVEL, ""));
        getInstance().mLoginInfo.setRight_to_publish(sp.getBoolean(CommonValue.RIGHT_TO_PUBLISH, false));
        getInstance().mLoginInfo.setIs_supplier(sp.getBoolean(CommonValue.IS_SUPPLIER, false));
        getInstance().mLoginInfo.setQq_bound(sp.getBoolean(CommonValue.QQ_BOUND, false));
    }

    public void updateLoginInfo(LoginInfo loginInfo) {
        mLoginInfo = loginInfo;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                saveLogin();
                return null;
            }
        }.execute();
    }
    public void updateUserProfile(final LoginInfo loginInfo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                saveUserProfile(loginInfo);
                return null;
            }
        }.execute();
    }

    public static boolean isLogin() {
        if (getInstance().mLoginInfo == null) {
            if (TextUtils.isEmpty(getAccessToken())) {
                return false;
            } else {
                return true;
            }
        }
        if (TextUtils.isEmpty(getAccessToken())) return false;
        return true;

    }
    public static boolean isConfig() {
        if (getInstance().mConfigInfo == null) return false;
        if (TextUtils.isEmpty(getversion())) return false;//这里要修改为版本号
        return true;
    }
    public static LoginInfo getLoginInfo() {
        return getInstance().mLoginInfo;
    }

    public static String getServicetime() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getServicetime();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getServicetime();
        }
    }


    public static String getUid() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getId();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getId();
        }
    }
    public static String getUser_name() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getUser_name();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getUser_name();
        }
    }
    public static String getId() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getId();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getId();
        }
    }

    public static String getRole_id() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getRole_id();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getRole_id();
        }
    }
    public static String getAccessToken() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getApikey();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getApikey();
        }
    }


    public static String getProject_name() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getProject_name();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getProject_name();
        }
    }
    public static String getProduct() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getProduct();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getProduct();
        }
    }
    public static void setIndustry_name(String industry_name) {
        sp.edit().putString("industry_name", industry_name)
                .apply();
    }
    public static String getIndustry_name() {
        return sp.getString("industry_name", "");
    }

    public static String getCompany_name() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getCompany();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getCompany();
        }
    }

    public static String getMobile() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getMobile();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getMobile();
        }
    }
    public static String getContact() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getContact();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getContact();
        }
    }
    public static String geteEmail() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getEmail();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getEmail();
        }
    }
    public static String getName() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getName();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getName();
        }
    }
    public static String getAddress() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getAddress();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getAddress();
        }
    }

    public static String getinvitecode() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getInvite_code();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getInvite_code();
        }
    }
    public static void setConsumption_point(String consumption_point) {
        if (consumption_point != null) {
            getInstance().mLoginInfo.setConsumption_point(consumption_point);
            sp.edit()
                    .putString(CommonValue.CONSUMPTION_POINT, consumption_point)
                    .apply();
        }
    }
    public static String getConsumption_point() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getConsumption_point();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getConsumption_point();
        }
    }
    public static void setEnterprise_role(int enterprise_role) {
        getInstance().mLoginInfo.setEnterprise_role(enterprise_role);
        sp.edit()
                .putInt(CommonValue.ENTERPRISE_ROLE, enterprise_role)
                .apply();
    }
    public static int getEnterprise_role() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getEnterprise_role();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getEnterprise_role();
        }
    }
    public static void setRelation_id(int relation_id) {
        getInstance().mLoginInfo.setRelation_id(relation_id);
        sp.edit()
                .putInt(CommonValue.RELATION_ID, relation_id)
                .apply();
    }
    public static int getRelation_id() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getRelation_id();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getRelation_id();
        }
    }
    public static void setOrdering_noticed(int ordering_noticed) {
        getInstance().mLoginInfo.setOrdering_noticed(ordering_noticed);
        sp.edit()
                .putInt(CommonValue.ORDERING_NOTICED, ordering_noticed)
                .apply();
    }

    public static int getOrdering_noticed() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getOrdering_noticed();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getOrdering_noticed();
        }
    }
    public static void setAvatar(String avatar) {
        getInstance().mLoginInfo.setAvatar(avatar);
        sp.edit()
                .putString(CommonValue.AVATAR, avatar)
                .apply();
    }
    public static String getAvatar() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getAvatar();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getAvatar();
        }
    }
    public static void setMembership_level(String membership_level) {
        getInstance().mLoginInfo.setMembership_level(membership_level);
        sp.edit()
                .putString(CommonValue.MEMBERSHIP_LEVEL, membership_level)
                .apply();
    }
    public static String getMembership_level() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getMembership_level();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getMembership_level();
        }
    }
    public static void setWechat_bound(boolean wechat_bound) {
        getInstance().mLoginInfo.setWechat_bound(wechat_bound);
        sp.edit()
                .putBoolean(CommonValue.WECHAT_BOUND, wechat_bound)
                .apply();
    }
    public static boolean isWechat_bound() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.isWechat_bound();
        } else {
            readLogin();
            return getInstance().mLoginInfo.isWechat_bound();
        }
    }
    public static void setRight_to_publish(boolean right_to_publish) {
        getInstance().mLoginInfo.setRight_to_publish(right_to_publish);
        sp.edit()
                .putBoolean(CommonValue.RIGHT_TO_PUBLISH, right_to_publish)
                .apply();
    }
    public static boolean isRight_to_publish() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.isRight_to_publish();
        } else {
            readLogin();
            return getInstance().mLoginInfo.isRight_to_publish();
        }
    }
    public static void setIs_supplier(boolean is_supplier) {
        getInstance().mLoginInfo.setIs_supplier(is_supplier);
        sp.edit()
                .putBoolean(CommonValue.IS_SUPPLIER, is_supplier)
                .apply();
    }
    public static boolean isIs_supplier() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.isIs_supplier();
        } else {
            readLogin();
            return getInstance().mLoginInfo.isIs_supplier();
        }
    }
    public static void setQq_bound(boolean qq_bound) {
        getInstance().mLoginInfo.setQq_bound(qq_bound);
        sp.edit()
                .putBoolean(CommonValue.QQ_BOUND, qq_bound)
                .apply();
    }
    public static boolean isQq_bound() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.isQq_bound();
        } else {
            readLogin();
            return getInstance().mLoginInfo.isQq_bound();
        }
    }
    public static void setHas_contacts(boolean has_contacts) {
        getInstance().mLoginInfo.setHas_contracts(has_contacts);
        sp.edit()
                .putBoolean(CommonValue.HAS_CONTACTS, has_contacts)
                .apply();
    }
    public static boolean isHas_contacts() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.isHas_contracts();
        } else {
            readLogin();
            return getInstance().mLoginInfo.isHas_contracts();
        }
    }
    public static void setEnterprise_name(String enterprise_name) {
        sp.edit().putString("enterprise_name", enterprise_name)
                .apply();
    }
    public static String getEnterprise_name() {
        return sp.getString("enterprise_name", "");
    }
    public static void setEnterprise_authorize_status(int enterprise_authorize_status) {
        getInstance().mLoginInfo.setEnterprise_authorize_status(enterprise_authorize_status);
        sp.edit()
                .putInt(CommonValue.ENTERPRISE_AUTHORISE_STATUS, enterprise_authorize_status)
                .apply();
    }
    public static int getEnterprise_authorize_status() {
        if (getInstance().mLoginInfo != null) {
            return getInstance().mLoginInfo.getEnterprise_authorize_status();
        } else {
            readLogin();
            return getInstance().mLoginInfo.getEnterprise_authorize_status();
        }
    }
    public static void setcontext(Context context) {
        getInstance().context = context;
    }
    public static String getServicephoneNumber() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getServicephoneNumber();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getServicephoneNumber();
        }
    }
    public static String getfieldtype() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getFieldtype();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getFieldtype();
        }
    }
    public static String getfieldsort() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getResource_sort();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getResource_sort();
        }
    }
    public static String getAvd_sort() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getAvd_sort();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getAvd_sort();
        }
    }
    public static String getActivity_sort() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getActivity_sort();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getActivity_sort();
        }
    }
    public static String getcitylist() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getCitylist();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getCitylist();
        }
    }
    public static String getbanner() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getBanner();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getBanner();
        }
    }
    public static String getversion() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getVersion();
        } else {
            readConfig();
            return getInstance().mConfigInfo.getVersion();
        }
    }
    public static void setversion(String version) {
        getInstance().mConfigInfo.setVersion(version);
        sp.edit()
                .putString(CommonValue.Version, version)
                .apply();
    }

    public static String getAd_type() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getAd_types();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getAd_types();
        }
    }

    public static String getTerm_types() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getTerm_types();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getTerm_types();
        }
    }
    public static String getInvoice_content() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getInvoice_content();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getInvoice_content();
        }
    }
    public static String getField_review_tags() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getField_review_tags();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getField_review_tags();
        }
    }
    public static String getFacility_tags() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getFacility_tags();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getFacility_tags();
        }
    }
    public static String getCommunity_type() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getCommunity_type();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getCommunity_type();
        }
    }
    public static String getField_labels() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getField_labels();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getField_labels();
        }
    }
    public static String getIs_outdoor() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getIs_outdoor();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getIs_outdoor();
        }
    }
    public static String getActivity_types() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getActivity_types();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getActivity_types();
        }
    }
    public static String getHot_citylist() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getHot_citylist();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getHot_citylist();
        }
    }
    public static String getDeveloper() {
        if (getInstance().mConfigInfo != null) {
            return getInstance().mConfigInfo.getDeveloper();
        }else {
            readConfig();
            return getInstance().mConfigInfo.getDeveloper();
        }
    }

    public static void setUid(String uid) {
        if (!(TextUtils.isEmpty(uid))) {
            getInstance().mLoginInfo.setUser_name(uid);
        }
    }
    public static void  setMobile(String mobile) {
        if (!(TextUtils.isEmpty(mobile))) {
            getInstance().mLoginInfo.setMobile(mobile);
            sp.edit()
                    .putString(CommonValue.MOBILE, mobile)
                    .apply();
        }
    }

    public static void  setinvitecode(String incite) {
        if (!(TextUtils.isEmpty(incite))) {
            getInstance().mLoginInfo.setInvite_code(incite);
            sp.edit()
                    .putString(CommonValue.INVITE, incite)
                    .apply();
        }
    }
    public static void  setProduct(String product) {
        getInstance().mLoginInfo.setProduct(product);
        sp.edit()
                .putString(CommonValue.PRODUCTNAME, product)
                .apply();
    }
    public static void  setCompany_name(String company_name) {
        getInstance().mLoginInfo.setCompany(company_name);
        sp.edit()
                .putString(CommonValue.COMPANYNAME, company_name)
                .apply();
    }

    public static void  setContact(String contact) {
        if (!(TextUtils.isEmpty(contact))) {
            getInstance().mLoginInfo.setContact(contact);
            sp.edit()
                    .putString(CommonValue.CONTACT, contact)
                    .apply();
        }
    }
    public static void  setEmail(String email) {
        if (!(TextUtils.isEmpty(email))) {
            getInstance().mLoginInfo.setEmail(email);
            sp.edit()
                    .putString(CommonValue.EMAIL, email)
                    .apply();
        }
    }
    public static void  setName(String name) {
        if (!(TextUtils.isEmpty(name))) {
            getInstance().mLoginInfo.setName(name);
            sp.edit()
                    .putString(CommonValue.NAME, name)
                    .apply();
        }
    }
    public static void  setUser_name(String name) {
        if (!(TextUtils.isEmpty(name))) {
            getInstance().mLoginInfo.setUser_name(name);
            sp.edit()
                    .putString(CommonValue.UNAME, name)
                    .apply();
        }
    }

    public static void  setRole_id(String role_id) {
        if (!(TextUtils.isEmpty(role_id))) {
            getInstance().mLoginInfo.setRole_id(role_id);
            sp.edit()
                    .putString(CommonValue.GROUP, role_id)
                    .apply();
        }
    }

    public void clearLoginInfo() {
        mLoginInfo = null;
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
                sp.edit()
                        .remove(CommonValue.UID)
                        .remove(CommonValue.UNAME)
                        .remove(CommonValue.ACCESSTOKEN)
                        .remove(CommonValue.GROUP)
                        .remove(CommonValue.STATUS)
                        .remove(CommonValue.MOBILE)
                        .remove(CommonValue.PROJECTNAME)
                        .remove(CommonValue.CONTACT)
                        .remove(CommonValue.EMAIL)
                        .remove(CommonValue.NAME)
                        .remove(CommonValue.REGION)
                        .remove(CommonValue.INVITE)
                        .remove(CommonValue.COMPANYNAME)
                        .remove(CommonValue.PRODUCTNAME)
                        .remove(CommonValue.CONSUMPTION_POINT)
                        .remove(CommonValue.ENTERPRISE_AUTHORISE_STATUS)
                        .remove(CommonValue.ENTERPRISE_ROLE)
                        .remove(CommonValue.PASSWORD)
                        .remove(CommonValue.AVATAR)
                        .remove(CommonValue.WECHAT_BOUND)
                        .remove(CommonValue.HAS_CONTACTS)
                        .remove(CommonValue.RELATION_ID)
                        .remove(CommonValue.ORDERING_NOTICED)
                        .remove(CommonValue.MEMBERSHIP_LEVEL)
                        .remove(CommonValue.RIGHT_TO_PUBLISH)
                        .remove(CommonValue.IS_SUPPLIER)
                        .remove(CommonValue.QQ_BOUND)
                        .commit();
//                return null;
//            }
//        }.execute();
        setIndustry_name("");
    }
    public void clearConfigInfo() {
        mConfigInfo = null;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                sp.edit()
                        .remove(CommonValue.Servicetime)
                        .remove(CommonValue.ServicephoneNumber)
                        .remove(CommonValue.Fieldtype)
                        .remove(CommonValue.Fieldsort)
                        .remove(CommonValue.Avdsort)
                        .remove(CommonValue.Activitysort)
                        .remove(CommonValue.Citylist)
                        .remove(CommonValue.Banner)
                        .remove(CommonValue.Version)
                        .remove(CommonValue.Ad_types)
                        .remove(CommonValue.Term_types)
                        .remove(CommonValue.Invoice_content)
                        .remove(CommonValue.Field_review_tags)
                        .remove(CommonValue.Facility_tags)
                        .remove(CommonValue.Community_types)
                        .remove(CommonValue.Field_labels)
                        .remove(CommonValue.IS_OUTDOOR)
                        .remove(CommonValue.ACTIVITY_TYPES)
                        .remove(CommonValue.HOT_CITYLIST)
                        .remove(CommonValue.DEVELOPER)
                        .apply();
                return null;
            }
        }.execute();
    }
    public static void setConfig_updatetime() {
        sp.edit().putLong("Config_updatetime", System.currentTimeMillis())
                .apply();
    }
    public static Long getConfig_updatetime() {
        return sp.getLong("Config_updatetime", 0L);
    }
    public static void setCurrentCity(String currentcity) {
        sp.edit().putString("currentcity", currentcity)
                .apply();
    }
    public static String getCurrentCity() {
        return sp.getString("currentcity", "");
    }
    //首页刚进去定位后显示的城市（确认切换就变）
    public static void setCurrentCitycode(String currentcity) {
        sp.edit().putString("currentcitycode", currentcity)
                .apply();
    }
    public static String getCurrentCitycode() {
        return sp.getString("currentcitycode", "");
    }
    //首页显示的城市id（选择不同的城市会变）
    public static void setTrackcityid(String currentcity) {
        sp.edit().putString("rackcityid", currentcity)
                .apply();
    }
    public static String getTrackcityid() {
        return sp.getString("rackcityid", "");
    }
    public static void setTrackCityName(String trackCityName) {
        sp.edit().putString("trackCityName", trackCityName)
                .apply();
    }
    public static String getTrackCityName() {
        return sp.getString("trackCityName", "");
    }
    public static void setTrackProvinceName(String trackProvinceName) {
        sp.edit().putString("trackProvinceName", trackProvinceName)
                .apply();
    }
    public static String getTrackProvinceName() {
        return sp.getString("trackProvinceName", "");
    }
    public static void setNoticescount (int count) {
        sp.edit().putInt("noticescount", count)
                .apply();
    }
    public static int getNoticescount() {
        return sp.getInt("noticescount", 0);
    }
    public static void setNoticesid (int id) {
        sp.edit().putInt("noticesid", id)
                .apply();
    }
    public static int getNoticesid() {
        return sp.getInt("noticesid", 0);
    }

    public static void setNoticesTitle (String title) {
        sp.edit().putString("noticestitle", title)
                .apply();
    }
    public static String getNoticesTitle() {
        return sp.getString("noticestitle", "");
    }
    public static void setNoticesUrl (String url) {
        sp.edit().putString("noticesurl", url)
                .apply();
    }
    public static String getNoticesUrl() {
        return getInstance().sp.getString("noticesurl", "");
    }
    public static void setNoticesshow (int show) {
        getInstance().sp.edit().putInt("noticesshow", show)
                .apply();
    }
    public static int getNoticesshow() {
        return sp.getInt("noticesshow", 0);
    }
    //友盟推送没启动app时跳转的标记
    public static void setUMmsg_start_app (String UMmsg_start_app) {
        sp.edit().putString("UMmsg_start_app", UMmsg_start_app)
                .apply();
    }
    public static String getUMmsg_start_app() {
        return sp.getString("UMmsg_start_app", "");
    }
    public static void setWallet_stauts(int wallet_stauts) {
        sp.edit().putInt("wallet_stauts", wallet_stauts)
                .apply();
    }
    public static int getWallet_stauts() {
        return sp.getInt("wallet_stauts", 0);
    }
    //fingerprint_validation
    public static void setFingerprint_validation (String fingerprint_model) {
        sp.edit().putString("fingerprint_model", fingerprint_model)
                .apply();
    }
    public static String getFingerprint_validation() {
        return sp.getString("fingerprint_model", "");
    }
    //Device_token
    public static void setDevice_token (String device_token) {
        sp.edit().putString("device_token", device_token)
                .apply();
    }
    public static String getDevice_token() {
        return sp.getString("device_token", "");
    }

    //定位的城市id（位置中没有符合城市默认杭州）
    public static void setRegisterCurrentCityid(String currentcity) {
        sp.edit().putString("RegisterCurrentCityid", currentcity)
                .apply();
    }
    public static String getRegisterCurrentCityid() {
        return sp.getString("RegisterCurrentCityid", "");
    }
    //引导页显示判断  否则显示启动页
    public static void setShowStartPages(boolean showStartPages) {
        sp.edit().putBoolean("ShowStartPages", showStartPages)
                .apply();
    }
    public static boolean isShowStartPages() {
        return sp.getBoolean("ShowStartPages", false);
    }
    public static void setSearch_lately_history(String Search_lately_history) {
        sp.edit().putString("Search_lately_history", Search_lately_history)
                .apply();
    }
    public static String getSearch_lately_history() {
        return sp.getString("Search_lately_history", "");
    }
    public static void setFieldexit(int isfieldexit) {
        sp.edit().putInt("isfieldexit", isfieldexit)
                .apply();
    }
    public static int getFieldexit() {
        return sp.getInt("isfieldexit", -1);
    }

    public static void setAddfield_resourcesmodel(String addfield_resourcesmodel) {
        sp.edit().putString("addfield_resourcesmodel", addfield_resourcesmodel)
                .apply();
    }
    public static String getAddfield_resourcesmodel() {
        return sp.getString("addfield_resourcesmodel", "");
    }
    public void setMapSearchhistory(String mapsearchhistorylist) {
        sp.edit().putString("mapsearchhistorylist", mapsearchhistorylist)
                .apply();
    }
    public String getMapSearchhistory() {
        return sp.getString("mapsearchhistorylist", "");
    }

    public void setupdatatime() {
        sp.edit().putLong("last_check", System.currentTimeMillis())
                .apply();
    }
    public Long getuodatatime() {
        return sp.getLong("last_check", 0L);
    }

    public void setHome_review_show_time(long home_review_show_time) {
        if (home_review_show_time == 0) {
            sp.edit().putLong("home_review_show_time", 0)
                    .apply();

        } else if (home_review_show_time == 1) {
            sp.edit().putLong("home_review_show_time", System.currentTimeMillis())
                    .apply();
        }
    }
    public Long getHome_review_show_time() {
        return sp.getLong("home_review_show_time", 0L);
    }
    public static void setCertificate (int certificate) {
        sp.edit().putInt("certificate", certificate)
                .apply();
    }
    public static int getCertificate() {
        return sp.getInt("certificate", 0);
    }
    public static void setFieldinfoGuidance (int fieldinfoGuidance) {
        sp.edit().putInt("fieldinfoGuidance_3.2", fieldinfoGuidance)
                .apply();
    }
    public static int getFieldinfoGuidance() {
        return sp.getInt("fieldinfoGuidance_3.2", 0);
    }
    public static void setSplashScreenPicUrl (String splashScreenPicUrl) {
        sp.edit().putString("splashScreenPicUrl", splashScreenPicUrl)
                .apply();
    }
    public static String getSplashScreenPicUrl() {
        return sp.getString("splashScreenPicUrl", "");
    }
    public static void setSplashScreenJumpUrl (String splashScreenJumpUrl) {
        sp.edit().putString("splashScreenJumpUrl", splashScreenJumpUrl)
                .apply();
    }
    public static String getSplashScreenJumpUrl() {
        return sp.getString("splashScreenJumpUrl", "");
    }
    public static void setSplashScreenStart (String splashScreenStart) {
        sp.edit().putString("splashScreenStart", splashScreenStart)
                .apply();
    }
    public static String getSplashScreenStart() {
        return sp.getString("splashScreenStart", "");
    }
    public static void setSplashScreenend (String splashScreenend) {
        sp.edit().putString("splashScreenend", splashScreenend)
                .apply();
    }
    public static String getSplashScreenend() {
        return sp.getString("splashScreenend", "");
    }
    private static String readAssetsFileString(Context activity, String fileName) {
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
    public static void setProvinceListVersion (int provinceListVersion) {
        sp.edit().putInt("provinceListVersion", provinceListVersion)
                .apply();
    }
    public static int getProvinceListVersion() {
        return sp.getInt("provinceListVersion", 0);
    }
    public static void setProvinceList (String provinceList) {
        sp.edit().putString("provinceList", provinceList)
                .apply();
    }
    public static String getProvinceList() {
        return sp.getString("provinceList", "");
    }
    public boolean isIs_insert_config() {
        return sp.getBoolean("is_insert_config", false);
    }

    public void setIs_insert_config(boolean is_insert_config) {
        sp.edit().putBoolean("is_insert_config", is_insert_config)
                .apply();
    }
    public static void setAddfield_show_guide (int addfield_show_guide) {
        sp.edit().putInt("addfield_show_guide", addfield_show_guide)
                .apply();
    }
    public static int getAddfield_show_guide() {
        return sp.getInt("addfield_show_guide", 0);
    }
    public static void setMap_show_guide (int map_show_guide) {
        sp.edit().putInt("map_show_guide", map_show_guide)
                .apply();
    }
    public static int getMap_show_guide() {
        return sp.getInt("map_show_guide", 0);
    }
    public static void setHome_show_guide (int home_show_guide) {
        sp.edit().putInt("home_show_guide", home_show_guide)
                .apply();
    }
    public static int getHome_show_guide() {
        return sp.getInt("home_show_guide", 0);
    }
    public static void setCommunity_list_show_guide (int community_list_show_guide) {
        sp.edit().putInt("community_list_show_guide", community_list_show_guide)
                .apply();
    }
    public static int getCommunity_list_show_guide() {
        return sp.getInt("community_list_show_guide", 0);
    }
    public static void setPhyinfo_show_guide (int phyinfo_show_guide) {
        sp.edit().putInt("phyinfo_show_guide", phyinfo_show_guide)
                .apply();
    }
    public static int getPhyinfo_show_guide() {
        return sp.getInt("phyinfo_show_guide", 0);
    }
}
