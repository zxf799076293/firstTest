package com.linhuiba.business.connector;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.model.CompanyModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.linhuifield.BuildConfig;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/3/7.
 */
public class UserApi {

    public static void editCheckingUser(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        String account, String apikey, String userId, String status) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("apikey", apikey);
        paramsMap.put("userId", userId);
        paramsMap.put("status", status);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP, "user/editCheckingUserApi",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getCheckingUserlist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                           String account, String apikey, String userId,String role) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("apikey", apikey);
        paramsMap.put("userId", userId);
        paramsMap.put("role", role);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "user/getCheckingUserApi",
                paramsMap,1));
        call.enqueue(handler);
    }
    //2.0API
    public static void apiauthlogin(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                     String account, String password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("password", password);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/login",
                paramsMap,1));
        call.enqueue(handler);
    }
    //usage:验证码的用途(1:注册 2:快速登录（根据需求已改为3 ） 3:重设密码(单纯发送验证码不考虑账号是否存在))
    public static void apiauthcaptcha(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                    String account, String usage) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("usage", usage);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/app_captcha",
                paramsMap, 1));
        call.enqueue(handler);
    }
    public static void apiauthfast_login(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                      String account, String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("captcha", captcha);
        paramsMap.put("channel", "android");
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/fast_login",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void apiauthregister(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                         String account, String captcha,String password,String invite_code,String city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("captcha", captcha);
        paramsMap.put("password", password);
        paramsMap.put("channel", "android");
        if (city_id != null && city_id.length() > 0) {
            paramsMap.put("city_id", city_id);
        }
        if (invite_code != null && invite_code.length() > 0) {
            paramsMap.put("invite_code", invite_code);
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/register",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void apiauth_reset_password(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                       String account, String captcha,String password,String password_confirmation) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        paramsMap.put("captcha", captcha);
        paramsMap.put("password", password);
        paramsMap.put("password_confirmation", password_confirmation);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/reset_password",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取app配置信息接口 3.4 如果修改还需修改linhuifield
    public static void getconfigurations(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String version) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (version == null || (version != null && version.length() == 0)) {
            paramsMap.put("version", "0");
        } else {
            paramsMap.put("version", version);
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "configurations",
                paramsMap,4));
        call.enqueue(handler);
    }
    // type 1修改邮箱 2修改姓名
    public static void modifyUserInfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, int type,String email) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (type == 1) {
            paramsMap.put("email", email);
        } else if (type == 2) {
            paramsMap.put("name", email);
        } else if (type == 3) {
            paramsMap.put("avatar",email);
        }
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "profile",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void editUserInfo_pw(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String old_password,
                                       String password,String confirm_password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("old_password", old_password);
        paramsMap.put("password", password);
        paramsMap.put("password_confirmation", confirm_password);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/password",
                paramsMap,1));
        call.enqueue(handler);
    }
    //v2.1
    //获取行业列表接口 2.1
    public static void getindustries(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "industries",
                paramsMap,2));
        call.enqueue(handler);
    }
    //保存当前用户的公司信息 2.6
    public static void saveCompany_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, CompanyModel companyModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (companyModel.getProduct() != null) {
            paramsMap.put("product", companyModel.getProduct());
        }
        if (companyModel.getSpread_way_id() > -1) {
            paramsMap.put("spread_way_id", String.valueOf(companyModel.getSpread_way_id()));
        }
        if (companyModel.getDemand_area() != null) {
            paramsMap.put("demand_area", String.valueOf(companyModel.getDemand_area()));
        }
        if (companyModel.getAcceptable_minimum_price() != null) {
            paramsMap.put("acceptable_minimum_price", Constants.getdoublepricestring(companyModel.getAcceptable_minimum_price(),100));
        }
        if (companyModel.getAcceptable_maximum_price() != null) {
            paramsMap.put("acceptable_maximum_price", Constants.getdoublepricestring(companyModel.getAcceptable_maximum_price(),100));
        }
        if(companyModel.getPushing_frequency_level_id() > -1) {
            paramsMap.put("pushing_frequency_level_id", String.valueOf(companyModel.getPushing_frequency_level_id()));
        }
        paramsMap.put("consumption_level_id", companyModel.getConsumption_level_id());
        paramsMap.put("age_level_id", companyModel.getAge_level_id());
        paramsMap.put("feature_description", companyModel.getFeature_description());
        if (companyModel.getIndustry_id() != null && companyModel.getIndustry_id().length() > 0 &&
                Integer.parseInt(companyModel.getIndustry_id()) > -1) {
            paramsMap.put("industry_id", String.valueOf(companyModel.getIndustry_id()));
        }

        paramsMap.put("images", JSONArray.toJSONString(companyModel.getImages(),true));
        paramsMap.put("food_safety_license", JSONArray.toJSONString(companyModel.getFood_safety_license(),true));
        paramsMap.put("cert_url", companyModel.getCert_url());
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/company_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取当前用户的公司信息 2.6
    public static void getcompany_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/company_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取公司的地推频率等信息列表 2.6
    public static void getcompany_config_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "company_information",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取用户个人资料的完善状态 2.1
    public static void getprofile_status(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/profile/status",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取当前用户权限申请的资料 2.1
    public static void getproperty_applications(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "property_applications/current",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取收款人信息接口 2.3
    public static void getbeneficiary_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String uid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/"+uid+"/beneficiary_info",
                paramsMap,2));
        call.enqueue(handler);
    }
    //保存收款人信息接口 2.3
    public static void savebeneficiary_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String opening_bank,String account_holder,String beneficiary_account_number,String uid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (opening_bank.length() > 0) {
            paramsMap.put("opening_bank", opening_bank);
        }
        if (account_holder.length() > 0) {
            paramsMap.put("account_holder", account_holder);
        }
        if (beneficiary_account_number.length() > 0) {
            paramsMap.put("beneficiary_account_number", beneficiary_account_number);
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/"+uid+"/beneficiary_info",
                paramsMap,2));
        call.enqueue(handler);
    }
    //企业认证接口 2.3
    public static void auth_enterprise(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String registration_no,String name,String corporation,
                                       String citizen_id,String cert_url,String province_id,
                                       String city_id,String district_id,String address,String contact,
                                       String mobile,String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("registration_no",registration_no);
        paramsMap.put("name",name);
        paramsMap.put("corporation",corporation);
        paramsMap.put("citizen_id",citizen_id);
        paramsMap.put("cert_url",cert_url);
        paramsMap.put("province_id",province_id);
        paramsMap.put("city_id",city_id);
        paramsMap.put("district_id",district_id);
        paramsMap.put("address",address);
        paramsMap.put("contact",contact);
        paramsMap.put("mobile",mobile);
        paramsMap.put("captcha",captcha);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/auth_enterprise",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取子账号列表 2.3
    public static void getenterprise_info (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/enterprise_info",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取子账号列表 2.3
    public static void getchild_accounts (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "child_accounts",
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除子账号item 2.3
    public static void deletechild_accounts (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "child_accounts/"+id,
                paramsMap,2));
        call.enqueue(handler);
    }
    //子账号 更换管理员接口
    public static void child_accounts_change_admin (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String mobile,
                                                    String captcha, String user_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("captcha", captcha);
        paramsMap.put("user_id", user_id);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "child_accounts/change_admin",
                paramsMap,2));
        call.enqueue(handler);
    }
    //版本更新
    public static void version(OkHttpClient client,
                               LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "config/app_latest_version/android",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取当前用户提醒标记接口/3.0
    public static void getbadge_info(OkHttpClient client,
                               LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/badge_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取商家报表接口/2.3
    public static void getbusiness_report(OkHttpClient client,
                                     LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/business_report",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取当前用户信息  获取积分接口 3.0
    public static void getuserprofile(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/profile",
                paramsMap,3));
        call.enqueue(handler);
    }
    //发送邀请子账号邮件接口 2.3
    public static void send_invitation(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String email) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "mails/send_invitation",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取加入企业邀请页面的链接地址的接口 2.3
    public static void child_accounts_share_link(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "child_accounts/share_link",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取公告列表（默认按时间倒序排列) 2.4.1
    public static void get_notices(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "notices",
                paramsMap,3));
        call.enqueue(handler);
    }
    //绑定用户的设备 2.4.2
    public static void binding_devices(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String uid,
                                       String device_token) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("device_token", device_token);
        paramsMap.put("os", "android");
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/"+uid+"/devices",
                paramsMap,2));
        call.enqueue(handler);
    }
    //退出登录接口 2.4.2
    public static void logout(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (LoginManager.getInstance().getDevice_token() != null &&
                LoginManager.getInstance().getDevice_token().length() > 0) {
            paramsMap.put("device_token", LoginManager.getInstance().getDevice_token());
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/logout",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取用户推送消息列表 2.4.2
    public static void getuser_msg_list(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                              String uid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/"+ uid +"/user_messages/",
                paramsMap,1));
        call.enqueue(handler);
    }
    //开通钱包功能 2.4.2
    public static void dredgewallets(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                     String password,String password_confirmation,
                                     String mobile,String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password",password);
        if (password_confirmation.length() > 0) {
            paramsMap.put("password_confirmation",password_confirmation);
        }
        paramsMap.put("mobile",mobile);
        paramsMap.put("captcha",captcha);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "wallets",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取钱包信息接口 2.4.2
    public static void getwalletsinfo(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "wallets",
                paramsMap,2));
        call.enqueue(handler);
    }
    //钱包充值接口 2.4.2
    public static void wallet_recharge(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                       String amount,int payment_mode,String openid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("amount",amount);
        paramsMap.put("payment_mode",String.valueOf(payment_mode));
        if (openid.length() > 0) {
            paramsMap.put("openid",openid);
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "transactions",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取交易明细列表接口 2.4.2(type：1:充值交易明细 2:消费交易明细)
    public static void gettransactions(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                      int type, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", String.valueOf(type));
        paramsMap.put("page", String.valueOf(page));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "transactions",
                paramsMap,2));
        call.enqueue(handler);
    }
    //验证钱包密码 2.4.2(重置密码)
    public static void validate_password(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                         String password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password", password);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "wallets/validate_password",
                paramsMap,2));
        call.enqueue(handler);
    }
    //修改钱包信息接口 2.4.2(重置密码)
    public static void modifywallets_pw(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                     String password,String password_confirmation,
                                     String mobile,String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password",password);
        if (password_confirmation.length() > 0) {
            paramsMap.put("password_confirmation",password_confirmation);
        }
        paramsMap.put("mobile",mobile);
        paramsMap.put("captcha",captcha);
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "wallets",
                paramsMap,2));
        call.enqueue(handler);
    }
    //取消付款接口(充值对公打款相关）2.4.2
    public static void cancel_transactions(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                           String id,Context context) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "transactions/" + id,
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取交易详情2.4.2
    public static void gettransactionsinfo(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                       String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "transactions/" + id,
                paramsMap,2));
        call.enqueue(handler);
    }
    //版本更新
    public static void version(OkHttpClient client,
                               LinhuiAsyncHttpResponseHandler handler, String version) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("version",version);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "version_info/android",
                paramsMap,1));
        call.enqueue(handler);
    }
    //微信第三方登录
    public static void third_party_wechat_login(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                         String code,int province_id,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("code", code);
        if (province_id > 0) {
            paramsMap.put("province_id", String.valueOf(province_id));
        }
        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        }
        paramsMap.put("channel", "android");
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/third_party_wechat_login",
                paramsMap,1));
        call.enqueue(handler);
    }
    //qq登录 3.7
    public static void qqLogin(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                                String unionid,int province_id,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("unionid", unionid);
        if (province_id > 0) {
            paramsMap.put("province_id", String.valueOf(province_id));
        }
        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        }
        paramsMap.put("channel", "android");
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/QQ_login",
                paramsMap,1));
        call.enqueue(handler);
    }
    //绑定微信号
    public static void bind_wechat(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                   String code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("code", code);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/bind_wechat",
                paramsMap,1));
        call.enqueue(handler);
    }
    //解绑微信号
    public static void unbind_wechat(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/unbind_wechat",
                paramsMap,1));
        call.enqueue(handler);
    }
    //绑定手机号
    public static void bind_mobile(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                                String mobile,String captcha,String token) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("captcha", captcha);
        paramsMap.put("v", BuildConfig.VERSION_NAME);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Config.BASE_API_URL_PHP + "auth/bind_mobile")
                .addHeader("Accept","application/vnd.linhuiba.v1+json")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("client","android"+ ":" + BuildConfig.VERSION_NAME)
                .post(Request.getRequestBody(paramsMap))
                .build();

        Call call = client.newCall(request);
        call.enqueue(handler);
    }
    public static void bind_mobile(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                   String mobile,String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("captcha", captcha);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/bind_mobile",
                paramsMap,1));
        call.enqueue(handler);
    }

    //获取搜索热词
    public static void getHot_word(OkHttpClient client,
                               LinhuiAsyncHttpResponseHandler handler, String city_id,String res_type_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",city_id);
        paramsMap.put("res_type_id",res_type_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "hot_words",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取公司信息的推广方式 2.5
    public static void getcompany_spread_way(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "track/company/spread_way",
                paramsMap,1));
        call.enqueue(handler);
    }
    //换绑手机号
    public static void change_mobile(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                   String old_mobile_captcha,String new_mobile, String new_mobile_captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("old_mobile_captcha", old_mobile_captcha);
        paramsMap.put("new_mobile", new_mobile);
        paramsMap.put("new_mobile_captcha", new_mobile_captcha);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/change_mobile",
                paramsMap,1));
        call.enqueue(handler);
    }
    //验证短信验证码有效性
    public static void validate_captcha(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                     String mobile,String captcha) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("captcha", captcha);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/validate_captcha",
                paramsMap,1));
        call.enqueue(handler);
    }
    //banner图片获取
    public static void getbanners(OkHttpClient client,
                                   LinhuiAsyncHttpResponseHandler handler, String city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page","1");
        paramsMap.put("type","home");
        paramsMap.put("pageSize",String.valueOf(Integer.MAX_VALUE));
        if (city_id != null && city_id.length() > 0) {
            paramsMap.put("city_id",city_id);
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "banners",
                paramsMap,1));
        call.enqueue(handler);
    }
    //根据订单id获取收款信息 3.0
    public static void getpayment_orders(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "payment/orders/"+id,
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取最新签约列表
    public static void getsigned(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page","1");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/signed",
                paramsMap,1));
        call.enqueue(handler);
    }
    //设置用户名 3.4
    public static void setUsetName(String username,
                                   OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", username);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/set_name",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取首页弹窗
    public static void getMessageNotices(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "message_notices",
                paramsMap,1));
        call.enqueue(handler);
    }
    //删除首页弹窗
    public static void deleteMessageNotices(String id, OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "message_notices/" + id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //绑定qq号
    public static void bindQQ(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                   String unionid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("unionid", unionid);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/bind_qq",
                paramsMap,1));
        call.enqueue(handler);
    }
    //解绑qq号
    public static void unbindQQ(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "auth/unbind_qq",
                paramsMap,1));
        call.enqueue(handler);
    }
    /**
     * 获取用户消息列表 3.8
     * @param client
     * @param handler
     * @param id 用户id
     * @param type 消息类型（1：系统消息，2：邻汇精选，默认是全部）
     * @param page
     */
    public static void getUserMsg(OkHttpClient client,
                                  LinhuiAsyncHttpResponseHandler handler,
                                  String id,int type,int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (type > 0) {
            paramsMap.put("type",String.valueOf(type));
        }
        paramsMap.put("page_size","10");
        paramsMap.put("page",String.valueOf(page));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/" + id + "/user_messages",
                paramsMap,2));
        call.enqueue(handler);
    }

    /**
     * 批量删除我的消息 3.8
     * @param client
     * @param handler
     * @param ids 消息ID数组
     */
    public static void deleteUserMsgs(OkHttpClient client,
                                  LinhuiAsyncHttpResponseHandler handler,
                                  ArrayList<Integer> ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            paramsMap.put("ids[" + String.valueOf(i) + "]", String.valueOf(ids.get(i)));
        }
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "users/user_messages",
                paramsMap,2));
        call.enqueue(handler);
    }

    /**
     * 标记消息为已读(msg_ids为','分隔的id列表) 3.8
     * @param client
     * @param handler
     * @param uid 用户id
     * @param ids 消息ID数组
     */
    public static void setMsgsRead(OkHttpClient client,
                                      LinhuiAsyncHttpResponseHandler handler,
                                      String uid,
                                      ArrayList<Integer> ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        String msg_ids = "";
        for (int i = 0; i < ids.size(); i++) {
            if (msg_ids.length() > 0) {
                msg_ids = msg_ids + "," + String.valueOf(ids.get(i));
            } else {
                msg_ids = msg_ids + String.valueOf(ids.get(i));
            }
        }
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "users/" + uid +"/user_messages/" + msg_ids + "/read",
                paramsMap,2));
        call.enqueue(handler);
    }

    /**
     * 消息全部已读 3.8
     * @param client
     * @param handler
     */
    public static void setAllMsgsRead(OkHttpClient client,
                                   LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "user_messages/read",
                paramsMap,2));
        call.enqueue(handler);
    }
    public static void sendBrowseHistories(OkHttpClient client,
                                      LinhuiAsyncHttpResponseHandler handler,
                                           String view, String parameter,String cur_city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("view",view);
        if (parameter != null && parameter.length() > 0) {
            paramsMap.put("parameters",parameter);
        }
        paramsMap.put("source","1");
        if (cur_city_id != null &&
                cur_city_id.length() > 0) {
            paramsMap.put("cur_city_id",cur_city_id);
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "browse-histories",
                paramsMap,1));
        call.enqueue(handler);
    }

}
