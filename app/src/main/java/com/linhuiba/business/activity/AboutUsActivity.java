package com.linhuiba.business.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.fragment.SelfSupportShopFragment;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.internal.http2.Header;
/**
 * Created by Administrator on 2016/3/17.
 */
public class AboutUsActivity extends BaseMvpActivity {
    @InjectView(R.id.about_WebView)
    BridgeWebView mabout_WebView;
    @InjectView(R.id.about_title_layout)
    LinearLayout mabout_title_layout;
    @InjectView(R.id.about_notices_back_imgbtn)
    ImageButton mabout_notices_back_imgbtn;
    @InjectView(R.id.about_line_view)
    View mAboutLineView;
    @InjectView(R.id.about_facilitator_ll)
    LinearLayout mFacilitatorLL;
    private Handler mRecommendHandler = new Handler();
    private Dialog pw;
    private IWXAPI mIWXAPI;
    private String notices_url = "";
    private Dialog mShareDialog;
    private String mShareLinkurl;
    private String mShareTitleStr;
    private String mShareDescriptionStr;
    private String cityId;
    private boolean isDemand;//判断是否是需求定制进来的
    //需求定制选择图片交互
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 1;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private int intentTypeInt;
    private CustomDialog mCustomDialog;
    private String mServicePhone;
    private boolean isAddfile;//判断是否需要上传图片
    private Handler mDialogHandler = new Handler();
    private CustomDialog mDeleteEnquiryDialog;
    private int mGroupOrderInt;
    private final int CALL_PHONE_CODE = 110;
    private String callPhoneStr = "";
    private String orderItemId;
    private ArrayList<Integer> mCommunityIds = new ArrayList<>();//询价下单的场地id数组
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String shareWXLinkurl = "";//微信分享的url
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private String mThemeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDemand) {
            MobclickAgent.onPageStart(getResources().getString(R.string.demand_activity_name_str));
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isDemand) {
            MobclickAgent.onPageEnd(getResources().getString(R.string.demand_activity_name_str));
            MobclickAgent.onPause(this);
        }
    }

    private void initView() {
        mIWXAPI = WXAPIFactory.createWXAPI(AboutUsActivity.this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        ButterKnife.inject(this);
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mabout_WebView.canGoBack()
                        && intentTypeInt != Config.ENQUIRY_LIST_WEB_INT) {
                    mabout_WebView.goBack();// 返回前一个页面
                } else {
                    AboutUsActivity.this.finish();
                }
            }
        });
        if (LoginManager.isLogin()) {
            String RandomStr = com.linhuiba.linhuifield.connector.Constants.getRandomString(10);
            // 2018/7/16 Cooking 测试设置
//            syncCookie(Config.Domain_Name_COOKING_IE_TEST,RandomStr.substring(0, 2) + LoginManager.getAccessToken() + RandomStr.substring(2));
            syncCookie(Config.Domain_Name,RandomStr.substring(0, 2) + LoginManager.getAccessToken() + RandomStr.substring(2));
        }
        mabout_WebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mabout_WebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mabout_WebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mabout_WebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mabout_WebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mabout_WebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mabout_WebView.getSettings().setAppCacheEnabled(false);//是否使用缓存
        mabout_WebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        mabout_WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mabout_WebView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
//
//                //handler.cancel(); 默认的处理方式，WebView变成空白页
//                //handler.process();接受证书
//                handler.proceed();
//                //handleMessage(Message msg); 其他处理
//            }
//        });
        mabout_WebView.registerHandler("clickHiddenKeyboard", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        //返回首页
        mabout_WebView.registerHandler("clickBackHomePage", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Intent maintabintent = new Intent(AboutUsActivity.this, MainTabActivity.class);
                maintabintent.putExtra("new_tmpintent", "goto_homepage");
                startActivity(maintabintent);
            }
        });
        //返回
        mabout_WebView.registerHandler("back_home", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (intentTypeInt == Config.ENQUIRY_WEB_INT) {
                    Intent maintabintent = new Intent(AboutUsActivity.this, MainTabActivity.class);
                    maintabintent.putExtra("new_tmpintent", "goto_homepage");
                    startActivity(maintabintent);
                } else {
                    finish();
                }
            }
        });
        //联系客服
        mabout_WebView.registerHandler("contactLinHuiServer", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                AndPermission.with(AboutUsActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            }
        });
        //评价
        mabout_WebView.registerHandler("commentsOrder", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Intent publidhreview = new Intent(AboutUsActivity.this, PublishReviewActivity.class);
                publidhreview.putExtra("orderid", data);
                startActivityForResult(publidhreview, 1);
            }
        });
        //删除订单
        mabout_WebView.registerHandler("deleteOrder", new BridgeHandler() {

            @Override
            public void handler(final String data, CallBackFunction function) {
                MessageUtils.showDialog(AboutUsActivity.this, getResources().getString(R.string.dialog_prompt),
                        getResources().getString(R.string.delete_prompt),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                FieldApi.deleteordersitemlist(AboutUsActivity.this, MyAsyncHttpClient.MyAsyncHttpClient(), deleteorderHandler, data);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        });
        //支付订单
        mabout_WebView.registerHandler("payOrder", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                Intent choosepayway_intent = new Intent(AboutUsActivity.this, OrderConfirmChoosePayWayActivity.class);
                if (mGroupOrderInt == 1) {
                    choosepayway_intent.putExtra("payment_option", 3);
                } else {
                    choosepayway_intent.putExtra("payment_option", 1);
                }
                choosepayway_intent.putExtra("order_id", json.get("order_id").toString());
                choosepayway_intent.putExtra("order_num", json.get("order_num").toString());
                choosepayway_intent.putExtra("order_price", json.get("totalPrice").toString());
                startActivity(choosepayway_intent);
                finish();
            }
        });
        //联系我们
        mabout_WebView.registerHandler("telUs", new BridgeHandler() {
            @Override
            public void handler(final String data, CallBackFunction function) {
                if (intentTypeInt == Config.BIG_ORDER_INFO_INT ||
                        intentTypeInt == Config.SMALL_ORDER_INFO_INT ||
                        intentTypeInt == Config.SUBMITTED_BIG_ORDER_INFO_INT) {
                    mServicePhone = "";
                    if (data != null && data.toString().length() > 0) {
                        JSONObject jsonpObject = JSON.parseObject(data.toString());
                        if (jsonpObject != null &&
                                jsonpObject.get("id") != null &&
                                jsonpObject.get("telphonenumber") != null &&
                                jsonpObject.get("id").toString().length() > 0 &&
                                jsonpObject.get("telphonenumber").toString().length() > 0) {
                            mServicePhone = jsonpObject.get("telphonenumber").toString();
                            showProgressDialog();
                            FieldApi.getVirtualNumber(MyAsyncHttpClient.MyAsyncHttpClient(),
                                    virtualNumberHandler, jsonpObject.get("id").toString());
                        }
                    }
                } else {
                    if (data != null && data.toString().length() > 0) {
                        if (intentTypeInt == Config.FACILITATOR_INT ||
                                intentTypeInt == Config.FACILITATOR_INFO_INT) {
                           if (LoginManager.isLogin()) {
                               if (mDeleteEnquiryDialog == null || !mDeleteEnquiryDialog.isShowing()) {
                                   View.OnClickListener uploadListener = new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           switch (view.getId()) {
                                               case R.id.btn_perfect:
                                                   mDeleteEnquiryDialog.dismiss();
                                                   Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                                           + data));
                                                   startActivity(intent_mobile);
                                                   break;
                                               default:
                                                   break;
                                           }
                                       }
                                   };
                                   CustomDialog.Builder builder = new CustomDialog.Builder(AboutUsActivity.this);
                                   mDeleteEnquiryDialog = builder
                                           .cancelTouchout(true)
                                           .view(R.layout.field_activity_field_orders_success_dialog)
                                           .addViewOnclick(R.id.btn_perfect,uploadListener)
                                           .setText(R.id.dialog_title_textview,
                                                   getResources().getString(R.string.about_faciltator_dialog_str))
                                           .setText(R.id.btn_perfect,
                                                   getResources().getString(R.string.about_faciltator_dialog_btn_str))
                                           .showView(R.id.btn_cancel,View.GONE)
                                           .showView(R.id.linhuiba_logo_imgv,View.GONE)
                                           .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                                           .build();
                                   com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(AboutUsActivity.this,mDeleteEnquiryDialog);
                                   mDeleteEnquiryDialog.show();
                               }
                           } else {
                               Intent loginIntent = new Intent(AboutUsActivity.this,LoginActivity.class);
                               if (intentTypeInt == Config.FACILITATOR_INT) {
                                   startActivityForResult(loginIntent,Config.FACILITATOR_INT);
                               } else {
                                   startActivity(loginIntent);
                               }
                           }
                        } else {
                            Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                    + data));
                            startActivity(intent_mobile);
                        }
                    }
                }

            }
        });
        //主题展分享
        mabout_WebView.registerHandler("shareThematic", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                String mShareUrl = "";
                if (json.get("origin") != null &&
                        json.get("origin").toString().length() > 0) {
                    mShareUrl = json.get("origin").toString();
                } else {
                    if (json.get("id") != null &&
                            json.get("id").toString().length() > 0) {
                        mShareUrl = Config.SHARE_THEMATIC_URL + json.get("id").toString();
                    }
                }
                showShareDialog(mShareUrl,
                        json.get("pic_url").toString(),
                        json.get("name").toString(),
                        getResources().getString(R.string.about_theme_share_desc_str));
            }
        });
        //优质服务商分享
        mabout_WebView.registerHandler("shareFacilitator", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                showShareDialog(Config.SHARE_FACILITATOR_URL + json.get("id").toString(),
                        json.get("pic_url").toString(),
                        json.get("title").toString(),
                        json.get("desc").toString());
            }
        });
        //优质服务商案例分享
        mabout_WebView.registerHandler("shareCaseDetail", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                showShareDialog(Config.SHARE_FACILITATOR_CASE_URL + json.get("case_id").toString() +
                                "?city_id=" + cityId,
                        json.get("pic_url").toString(),
                        json.get("title").toString(),
                        json.get("desc").toString());
            }
        });
        //需求定制分享
        mabout_WebView.registerHandler("shareDemand", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                showShareDialog(Config.SHARE_DEMAND_URL,
                        "demand",
                        getResources().getString(R.string.about_demand_share_title_str),
                        getResources().getString(R.string.about_demand_share_desc_str));
            }
        });
        //跳转到详情
        mabout_WebView.registerHandler("gotoFieldDetail", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null) {
                    if (data.toString().indexOf(":") != -1) {
                        if (com.linhuiba.linhuifield.connector.Constants.isGoodJson(data.toString())) {
                            JSONObject json = JSONObject.parseObject(data.toString());
                            if (json != null) {
                                if (json.get("res_type_id") != null &&
                                        json.get("res_type_id").toString().length() > 0) {
                                    if (json.get("res_type_id").toString().equals("4")) {
                                        if (json.get("group_purchase_id") != null &&
                                                json.get("group_purchase_id").toString().length() > 0) {
                                            Intent fieldinfoIntent = new Intent(AboutUsActivity.this, GroupBookingInfoActivity.class);
                                            fieldinfoIntent.putExtra("ResId", json.get("group_purchase_id").toString());
                                            startActivity(fieldinfoIntent);
                                        }
                                    } else {
                                        if (json.get("selling_resource_id") != null &&
                                                json.get("selling_resource_id").toString().length() > 0 &&
                                                json.get("res_type_id") != null &&
                                                json.get("res_type_id").toString().length() > 0) {
                                            Intent fieldinfoIntent;
                                            if (!json.get("res_type_id").toString().equals("2")) {
                                                fieldinfoIntent = new Intent(AboutUsActivity.this, FieldInfoActivity.class);
                                                if (json.get("res_type_id").toString().equals("3")) {
                                                    fieldinfoIntent.putExtra("sell_res_id", json.get("selling_resource_id").toString());
                                                    fieldinfoIntent.putExtra("is_sell_res", true);
                                                } else {
                                                    fieldinfoIntent.putExtra("fieldId", json.get("selling_resource_id").toString());
                                                }
                                            } else {
                                                fieldinfoIntent = new Intent(AboutUsActivity.this, AdvertisingInfoActivity.class);
                                                fieldinfoIntent.putExtra("fieldId", json.get("selling_resource_id").toString());
                                                fieldinfoIntent.putExtra("good_type", Integer.parseInt(json.get("res_type_id").toString()));
                                            }
                                            startActivity(fieldinfoIntent);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (data != null &&
                                data.toString().length() > 0) {
                            Intent fieldinfoIntent;
                            fieldinfoIntent = new Intent(AboutUsActivity.this, FieldInfoActivity.class);
                            fieldinfoIntent.putExtra("fieldId", data.toString());
                            fieldinfoIntent.putExtra("good_type", 1);
                            startActivity(fieldinfoIntent);
                        }
                    }
                }
            }
        });
        //跳转到订单行详情
        mabout_WebView.registerHandler("gotoDetail", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                if (json != null) {
                    if (json.get("group_purchase_id") != null &&
                            json.get("group_purchase_id").toString().length() > 0) {
                        Intent fieldinfoIntent = new Intent(AboutUsActivity.this, GroupBookingInfoActivity.class);
                        fieldinfoIntent.putExtra("ResId", json.get("group_purchase_id").toString());
                        startActivity(fieldinfoIntent);
                    } else {
                        if (json.get("field_order_item_id") != null &&
                                json.get("field_order_item_id").toString().length() > 0) {
                            Intent orderinfo = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                            orderinfo.putExtra("id", json.get("field_order_item_id").toString());
                            orderinfo.putExtra("type", com.linhuiba.business.config.Config.SMALL_ORDER_INFO_INT);
                            startActivity(orderinfo);
                        }
                    }
                }
            }
        });
        //跳转到大订单详情
        mabout_WebView.registerHandler("gotoBigOrder", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    Intent orderinfo = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                    orderinfo.putExtra("id", data.toString());
                    orderinfo.putExtra("type", com.linhuiba.business.config.Config.BIG_ORDER_INFO_INT);
                    startActivity(orderinfo);
                }
            }
        });
        //2017/12/19 开票跳转
        mabout_WebView.registerHandler("gotoApplyNote", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null && data.length() > 0) {
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    if (jsonObject != null) {
                        if (jsonObject.get("id") != null &&
                                jsonObject.get("id").toString().length() > 0 &&
                                jsonObject.get("price") != null &&
                                jsonObject.get("price").toString().length() > 0) {
                            Intent InvoiceInfoEntry = new Intent(AboutUsActivity.this, ApplyForInvoiceActivity.class);
                            InvoiceInfoEntry.putExtra("id",jsonObject.get("id").toString());
                            InvoiceInfoEntry.putExtra("price",jsonObject.get("price").toString());
                            startActivityForResult(InvoiceInfoEntry,Config.SMALL_ORDER_INFO_INT);
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                            if (orderItemId != null && orderItemId.length() > 0) {
                                mabout_WebView.loadUrl(Config.ORDER_ITEM_INFO_URL + orderItemId);
                            }
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                        if (orderItemId != null && orderItemId.length() > 0) {
                            mabout_WebView.loadUrl(Config.ORDER_ITEM_INFO_URL + orderItemId);
                        }
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                    if (orderItemId != null && orderItemId.length() > 0) {
                        mabout_WebView.loadUrl(Config.ORDER_ITEM_INFO_URL + orderItemId);
                    }
                }

            }
        });
        //2018/1/11 地址地图跳转
        mabout_WebView.registerHandler("goMap", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject json = JSONObject.parseObject(data.toString());
                if (json != null) {
                    if (json.get("address") != null &&
                            json.get("resource_name") != null &&
                            json.get("lat") != null &&
                            json.get("lng") != null) {
                        Intent mapinfo_intent = new Intent(AboutUsActivity.this, FieldinfoMapinfoActivity.class);
                        if (json.get("lng").toString().length() > 0) {
                            mapinfo_intent.putExtra("longitude", Double.parseDouble(json.get("lng").toString()));
                        }
                        if (json.get("lat").toString().length() > 0) {
                            mapinfo_intent.putExtra("latitude", Double.parseDouble(json.get("lat").toString()));
                        }
                        mapinfo_intent.putExtra("resourcename", json.get("resource_name").toString());
                        mapinfo_intent.putExtra("address", json.get("address").toString());
                        startActivity(mapinfo_intent);
                    }
                }
            }
        });
        //2017/12/19 询价返回
        mabout_WebView.registerHandler("backOneStep", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish();
            }
        });
        //2017/12/19 询价下单
        mabout_WebView.registerHandler("orderEnquire", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null && data.toString().length() > 0) {
                    JSONArray array = JSONArray.parseArray(data.toString());
                    if (array != null && array.size() > 0) {
                        ArrayList<HashMap<String, Object>> getJsonArray = getOrderConfirmJson(array);
                        if (getJsonArray != null && getJsonArray.size() > 0) {
                            //2018/1/16 询价下单功能
                            Intent OrderConfirm = new Intent(AboutUsActivity.this, OrderConfirmActivity.class);
                            OrderConfirm.putExtra("community_ids",(Serializable) mCommunityIds);
                            OrderConfirm.putExtra("ordertype", 1);
                            OrderConfirm.putExtra("submitorderlist", (Serializable) getJsonArray);
                            if (array.getJSONObject(0).get("deposit") != null &&
                                    array.getJSONObject(0).get("deposit").toString().length() > 0 &&
                                    Double.parseDouble(array.getJSONObject(0).get("deposit").toString()) > 0) {
                                OrderConfirm.putExtra("deposit", Double.parseDouble(array.getJSONObject(0).get("deposit").toString()));
                            } else {
                                OrderConfirm.putExtra("deposit", 0);
                            }
                            if (array.getJSONObject(0).get("service_fee") != null &&
                                    array.getJSONObject(0).get("service_fee").toString().length() > 0 &&
                                    Double.parseDouble(array.getJSONObject(0).get("service_fee").toString()) > 0) {
                                OrderConfirm.putExtra("service_fee", Double.parseDouble(array.getJSONObject(0).get("service_fee").toString()));
                            } else {
                                OrderConfirm.putExtra("service_fee", 0);
                            }
                            OrderConfirm.putExtra("inquiry", 1);
                            startActivity(OrderConfirm);
                        }

                    }
                }
            }
        });
        //跳转询价详情
        mabout_WebView.registerHandler("goEnquireDetail", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    Intent enquiryIntent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                    enquiryIntent.putExtra("type", Config.ENQUIRY_INFO_WEB_INT);
                    enquiryIntent.putExtra("id", data.toString());
                    startActivityForResult(enquiryIntent,Config.ENQUIRY_LIST_WEB_INT);
                }
            }
        });
        //跳转询价订单详情
        mabout_WebView.registerHandler("viewEnquireOrder", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    JSONObject jsonpObject = JSONObject.parseObject(data.toString());
                    if (jsonpObject != null &&
                            jsonpObject.get("id") != null &&
                            jsonpObject.get("order_status") != null &&
                            jsonpObject.get("id").toString().length() > 0 &&
                            jsonpObject.get("order_status").toString().length() > 0) {
                        Intent orderinfoIntent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                        orderinfoIntent.putExtra("id", jsonpObject.get("id").toString());
                        if (jsonpObject.get("order_status").toString().equals("submitted")) {
                            orderinfoIntent.putExtra("type", com.linhuiba.business.config.Config.SUBMITTED_BIG_ORDER_INFO_INT);
                            startActivity(orderinfoIntent);
                        } else {
                            orderinfoIntent.putExtra("type", com.linhuiba.business.config.Config.BIG_ORDER_INFO_INT);
                            startActivity(orderinfoIntent);
                        }
                    }
                }
            }
        });
        //跳转到发布需求
        mabout_WebView.registerHandler("goDemand", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Intent AddDemandIntent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                AddDemandIntent.putExtra("type", Config.ADD_DEMAND_WEB_INT);
                startActivity(AddDemandIntent);
            }
        });
        //询价详情设置点击文档预览事件
        mabout_WebView.registerHandler("viewFileLink", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    String url = data.toString();
                    String pdfStr = ".pdf";
                    if (data.toUpperCase().indexOf(pdfStr.toUpperCase()) != -1) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                    } else {
                        String officeLineStr = "https://view.officeapps.live.com/op/view.aspx?src=";
                        mabout_WebView.loadUrl(officeLineStr + url);
                    }
                }
            }
        });
        //取消询价
        mabout_WebView.registerHandler("deleteEnquire", new BridgeHandler() {
            @Override
            public void handler(final String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    if (mDeleteEnquiryDialog == null || !mDeleteEnquiryDialog.isShowing()) {
                        View.OnClickListener uploadListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()){
                                    case R.id.btn_perfect:
                                        mDeleteEnquiryDialog.dismiss();
                                        showProgressDialog();
                                        FieldApi.deleteEnquiry(MyAsyncHttpClient.MyAsyncHttpClient(), deleteEnquiryHandler, data.toString());
                                        break;
                                    case R.id.btn_cancel:
                                        mDeleteEnquiryDialog.dismiss();
                                }
                            }
                        };
                        CustomDialog.Builder builder = new CustomDialog.Builder(AboutUsActivity.this);
                        mDeleteEnquiryDialog = builder
                                .cancelTouchout(false)
                                .view(R.layout.field_activity_field_orders_success_dialog)
                                .addViewOnclick(R.id.btn_cancel,uploadListener)
                                .addViewOnclick(R.id.btn_perfect,uploadListener)
                                .setText(R.id.dialog_title_textview,
                                        getResources().getString(R.string.enquiry_dialog_title_str))
                                .setText(R.id.btn_cancel,
                                        getResources().getString(R.string.cancel))
                                .setText(R.id.btn_perfect,
                                        getResources().getString(R.string.confirm))
                                .showView(R.id.linhuiba_logo_imgv,View.GONE)
                                .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                                .build();
                        com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(AboutUsActivity.this,mDeleteEnquiryDialog);
                        mDeleteEnquiryDialog.show();
                    }
                }
            }
        });
        //跳转询价订单详情
        mabout_WebView.registerHandler("demandGoOrder", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null &&
                        data.toString().length() > 0) {
                    JSONObject jsonpObject = JSONObject.parseObject(data.toString());
                    if (jsonpObject != null &&
                            jsonpObject.get("id") != null &&
                            jsonpObject.get("status") != null &&
                            jsonpObject.get("id").toString().length() > 0 &&
                            jsonpObject.get("status").toString().length() > 0) {
                        Intent orderinfoIntent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
                        orderinfoIntent.putExtra("id", jsonpObject.get("id").toString());
                        if (jsonpObject.get("status").toString().equals("submitted")) {
                            orderinfoIntent.putExtra("type", com.linhuiba.business.config.Config.SUBMITTED_BIG_ORDER_INFO_INT);
                            startActivity(orderinfoIntent);
                        } else {
                            orderinfoIntent.putExtra("type", com.linhuiba.business.config.Config.BIG_ORDER_INFO_INT);
                            startActivity(orderinfoIntent);
                        }
                    }
                }
            }
        });
        //新增收款账号
        mabout_WebView.registerHandler("addReceiptMoneyAccountSuccess", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Intent backIntet = new Intent();
                setResult(Config.ADD_ACCOUNT_INT,backIntet);
                finish();
            }
        });
        //申请发布权限
        mabout_WebView.registerHandler("applyForBublishSuccess", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //2018/3/22 申请发布权限成功处理
                Intent backIntet = new Intent();
                setResult(Config.APPLY_FOR_RELEASE_INT,backIntet);
                finish();
            }
        });
        //修改手机号
        mabout_WebView.registerHandler("modifyMobileSuccess", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //2018/3/22 修改手机号
                LoginManager.setMobile(data);
                Intent backIntet = new Intent();
                setResult(4,backIntet);
                finish();
            }
        });
        //企业认证
        mabout_WebView.registerHandler("enterpriseCertificateSuccess", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                // 2018/3/22 企业认证
                LoginManager.setEnterprise_role(1);
                LoginManager.setEnterprise_authorize_status(1);
                finish();
            }
        });
        //场地意向
        // 2018/4/23 场地意向
        mabout_WebView.registerHandler("saveSiteIntention", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                // 2018/3/22 企业认证
                if (data != null && data.toString().length() > 0) {
                    LoginManager.getInstance().setIndustry_name(data.toString());
                }
                finish();
            }
        });
        //场地意向返回键
        //2018/4/23 场地意向返回键
        mabout_WebView.registerHandler("siteIntentionBack", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish();
            }
        });
        //2018/11/16 主题页面已加载返回分享需要的参数
        mabout_WebView.registerHandler("specialTopic", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //2018/11/16 专题分享获取分享的各种参数
                if (data != null && data.length() > 0) {
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    if (jsonObject != null && jsonObject.get("type") != null) {
                        if (jsonObject.get("type").toString().equals("1")) {
                            if (jsonObject.get("title") != null &&
                                    jsonObject.get("description") != null &&
                                    jsonObject.get("pic_url") != null &&
                                    jsonObject.get("title").toString().length() > 0 &&
                                    jsonObject.get("description").toString().length() > 0 &&
                                    jsonObject.get("pic_url").toString().length() > 0) {
                                ShareIconStr = jsonObject.get("pic_url").toString() + "?imageView2/0/w/300/h/240";
                                mShareTitleStr = jsonObject.get("title").toString();
                                TitleBarUtils.setTitleText(AboutUsActivity.this,mShareTitleStr);
                                mShareDescriptionStr = jsonObject.get("description").toString();
                                sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_THEME_URL + mThemeId;
                                shareWXLinkurl = Config.THEME_INFO_URL + mThemeId;
                                TitleBarUtils.showActionImg(AboutUsActivity.this, true, getResources().getDrawable(R.drawable.popup_ic_share), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        themeShare();
                                    }
                                });
                            }
                        } else if (jsonObject.get("type").toString().equals("2")) {
                            if (jsonObject.get("link") != null &&
                                    jsonObject.get("link").toString().length() > 0) {
                                Constants.pushUrlJumpActivity(jsonObject.get("link").toString(),AboutUsActivity.this,false);
                            }
                        } else if (jsonObject.get("type").toString().equals("3")) {
                            if (jsonObject.get("physical_resource_id") != null &&
                                    jsonObject.get("physical_resource_id").toString().length() > 0) {
                                Intent fieldinfoIntent = new Intent(AboutUsActivity.this,FieldInfoActivity.class);
                                fieldinfoIntent.putExtra("fieldId", jsonObject.get("physical_resource_id").toString());
                                startActivity(fieldinfoIntent);
                            }
                        }
                    }
                }
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("type") != null) {
            if (intent.getExtras().getInt("type") == Config.ABOUT_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_about));
                mabout_WebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading
                            (WebView view, String url) {
                        //判断用户单击的是哪个超连接
                        if (url.contains("tel:")) {
                            Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                            startActivity(intent_mobile);
                            //这个超连接,java已经处理了，webview不要处理了
                            return true;
                        }
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                });
                // 2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.ABOUT_US_URL);
            } else if (intent.getExtras().getInt("type") == Config.HELP_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_helpcenter_txt));
                mabout_WebView.loadUrl(Config.HELP_URL);
            } else if (intent.getExtras().getInt("type") == Config.BUSINESS_COMPANY_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_business_info));
                if (intent.getExtras().get("urlstr") != null && intent.getExtras().getString("urlstr").length() > 0) {
                    //2017/11/8 上线修改
                    mabout_WebView.loadUrl(Config.BUSSINESS_COMPANY_URL + intent.getExtras().getString("urlstr"));
                }
            } else if (intent.getExtras().getInt("type") == Config.POINT_INFO_WEB_INT) {
                if (LoginManager.isLogin()) {
                    TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_business_points_info));
                    mabout_WebView.loadUrl(Config.INVITE_WEB_URL);

                } else {
                    LoginManager.getInstance().clearLoginInfo();
                    LoginActivity.BaesActivityreloadLogin(this);
                    this.finish();
                }
            } else if (intent.getExtras().getInt("type") == Config.POINT_RULE_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.aboutactivity_integer_sule_text));
                mabout_WebView.loadUrl(Config.INTEGRA_RULE_WEB_URL);
            } else if (intent.getExtras().getInt("type") == Config.GUARD_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.aboutactivity_inform_guard_text));
                mabout_WebView.loadUrl(Config.INFORM_GUARD_WEB_URL);
            } else if (intent.getExtras().getInt("type") == Config.RESOURCE_INFO_WEB_INT) {
                if (intent.getExtras().getInt("resources_type") == 1) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.FIELD_INFO_URL + intent.getExtras().getInt("resourceid") + "?res_type_id=1");
                } else if (intent.getExtras().getInt("resources_type") == 2) {
                    TitleBarUtils.setTitleText(this, getResources().getString(R.string.fieldinfo_advertising_title_text));
                    mabout_WebView.loadUrl(Config.FIELDINFO_ADV_URL + intent.getExtras().getInt("resourceid") + Config.RESOURCESINFO_END_URL);
                } else if (intent.getExtras().getInt("resources_type") == 3) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.FIELD_INFO_URL + intent.getExtras().getInt("resourceid") + "?res_type_id=3");
                }
            } else if (intent.getExtras().getInt("type") == Config.REGISTER_AGREEMENT_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.register_agreement_title_text));
                //2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.AGREEMENT_URL);

            } else if (intent.getExtras().getInt("type") == Config.REPORT_WEB_INT) {
                setTransparentStatusBar();
                mabout_notices_back_imgbtn.setVisibility(View.VISIBLE);
                if (intent.getExtras().get("url") != null && intent.getExtras().get("url").toString().length() > 0) {
                    notices_url = intent.getExtras().get("url").toString();
                    mabout_WebView.loadUrl(intent.getExtras().get("url").toString());
                    mabout_WebView.addJavascriptInterface(new AboutUsActivity.DemoJavaScriptInterface(AboutUsActivity.this), "app");
                    LoginManager.getInstance().setNoticescount(Integer.MAX_VALUE);
                }
            } else if (intent.getExtras().getInt("type") == Config.COMMON_WEB_INT) {
                if (intent.getExtras().get("web_url") != null && intent.getExtras().getString("web_url").length() > 0) {
                    mabout_WebView.loadUrl(intent.getExtras().getString("web_url"));
                }
            } else if (intent.getExtras().getInt("type") == Config.INVOICE_INFO_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.invoiceinfomation_titletxt));
                TextView mTitleTV = (TextView) findViewById(R.id.common_title_bar).findViewById(R.id.title);
                RelativeLayout mTitleRL = (RelativeLayout) findViewById(R.id.common_title_bar).findViewById(R.id.about_title);
                ImageView mTitleImgV = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.back_button_top);
                mTitleRL.setBackgroundColor(getResources().getColor(R.color.default_bluebg));
                mTitleTV.setTextColor(getResources().getColor(R.color.white));
                mTitleImgV.setImageResource(R.drawable.whtie_back);
                mAboutLineView.setVisibility(View.GONE);
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    mabout_WebView.loadUrl(Config.INVOICE_INFO_URL + intent.getExtras().get("id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.WALLET_PLAIN_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.wallet_mywallet_explain_text));
                mabout_WebView.loadUrl(Config.WALLET_EXPLAIN_WEB_URL);
            } else if (intent.getExtras().getInt("type") == Config.HOME_NEW_SIGN_INT) {
                TitleBarUtils.setTitleText(this, intent.getExtras().getString("title"));
                if (intent.getExtras().get("web_url") != null) {
                    mabout_WebView.loadUrl(intent.getExtras().getString("web_url"));
                } else if (intent.getExtras().get("web_html_content") != null) {
//                    mabout_WebView.getSettings().setJavaScriptEnabled(true);
//                    mabout_WebView.loadDataWithBaseURL("", Config.WEBVIEW_URL_CSS + intent.getExtras().getString("web_html_content"), "text/html", "UTF-8", "");
                    if (intent.getExtras().get("id") != null) {
                        mabout_WebView.loadUrl(Config.APP_HTML_LOAD_URL+ intent.getExtras().get("id").toString());
                    }
                }
            } else if (intent.getExtras().getInt("type") == Config.NUMBER_OF_PEOPLE_SHOW_DIRECTION_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.addfield_number_of_people_show_direction_text));
                mabout_WebView.loadUrl(Config.NUMBER_OF_PEOPLE_WEB_URL);
            } else if (intent.getExtras().getInt("type") == Config.ADD_DEMAND_WEB_INT) {
                //需求定制
                if (LoginManager.isLogin()) {
                    isDemand = true;
                    isAddfile = true;
                    setTransparentStatusBar();
                    mFacilitatorLL.setVisibility(View.VISIBLE);
                    mabout_WebView.loadUrl(Config.ADD_DEMAND_WEB_URL);
                    AndPermission.with(AboutUsActivity.this)
                            .requestCode(Constants.PermissionRequestCode)
                            .permission(Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .callback(listener)
                            .start();

                } else {
                    LoginManager.getInstance().clearLoginInfo();
                    LoginActivity.BaesActivityreloadLogin(this);
                    this.finish();
                }
            } else if (intent.getExtras().getInt("type") == Config.DEMAND_LIST_WEB_INT) {
                if (LoginManager.isLogin()) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.MY_DEMAND_URL);
                } else {
                    LoginManager.getInstance().clearLoginInfo();
                    LoginActivity.BaesActivityreloadLogin(this);
                    this.finish();
                }
            } else if (intent.getExtras().getInt("type") == Config.TRANSACTION_VOUCHER_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_title_transaction_voucher_str));
                Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("authorization", "bearer" + LoginManager.getAccessToken());
                mabout_WebView.loadUrl(Config.TRANSACTION_VOUCHER_URL + intent.getExtras().get("id") + Config.FIELDINFO_END_URL, extraHeaders);
            } else if (intent.getExtras().getInt("type") == Config.ORDER_INFO_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_title_order_info_str));
                Map<String, String> extraHeaders = new HashMap<String, String>();
                extraHeaders.put("authorization", "bearer" + LoginManager.getAccessToken());
                //2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.ORDER_INFO_URL + intent.getExtras().get("id") + Config.FIELDINFO_END_URL, extraHeaders);
            } else if (intent.getExtras().getInt("type") == Config.FACILITATOR_INT) {
                // 优质服务商
                intentTypeInt = Config.FACILITATOR_INT;
                if (intent.getExtras().get("city_id") != null) {
                    setTransparentStatusBar();
                    mFacilitatorLL.setVisibility(View.VISIBLE);
                    cityId = intent.getExtras().get("city_id").toString();
                    int is_login = 0;
                    if (LoginManager.isLogin()) {
                        is_login = 1;
                    }
                    mabout_WebView.loadUrl(Config.FACILITATOR_LIST_URL + "?city_ids=" +
                            intent.getExtras().get("city_id").toString() + "&is_login=" +
                    String.valueOf(is_login));
                }
            } else if (intent.getExtras().getInt("type") == Config.FACILITATOR_INFO_INT) {
                // 优质服务商详情
                intentTypeInt = Config.FACILITATOR_INFO_INT;
                if (intent.getExtras().get("resource_id") != null && intent.getExtras().get("resource_id").toString().length() > 0) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.FACILITATOR_INFO_URL +
                            intent.getExtras().get("resource_id").toString() + "?redirect_url=home" );
                }
            } else if (intent.getExtras().getInt("type") == Config.FACILITATOR_CASE_INFO_INT) {
                //优质服务商案例详情
                if (intent.getExtras().get("resource_id") != null && intent.getExtras().get("resource_id").toString().length() > 0) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.FACILITATOR_CASE_INFO_URL +
                            intent.getExtras().get("resource_id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.THEMATIC_INT) {
                //主题展
                setTransparentStatusBar();
                mabout_WebView.loadUrl(Config.THEMATIC_URL);
            } else if (intent.getExtras().getInt("type") == Config.THEMATIC_INFO_INT) {
                //主题展详情
                if (intent.getExtras().get("resource_id") != null && intent.getExtras().get("resource_id").toString().length() > 0) {
                    setTransparentStatusBar();
                    mabout_WebView.loadUrl(Config.THEMATIC_INFO_URL +
                            intent.getExtras().get("resource_id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.GROUP_EDSC_INT) {
                mabout_WebView.loadUrl(Config.GROUP_DESC_URL);
            } else if (intent.getExtras().getInt("type") == Config.GRADE_INT) {
                mabout_WebView.loadUrl(Config.UEER_GRADE_URL);
            } else if (intent.getExtras().getInt("type") == Config.BIG_ORDER_INFO_INT) {
                intentTypeInt = intent.getExtras().getInt("type");
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_title_order_info_str));
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    mabout_WebView.loadUrl(Config.BIG_ORDER_INFO_URL + intent.getExtras().get("id").toString());
                }

            } else if (intent.getExtras().getInt("type") == Config.SMALL_ORDER_INFO_INT) {
                intentTypeInt = intent.getExtras().getInt("type");
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_title_order_item_info_str));
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    orderItemId = intent.getExtras().get("id").toString();
                    mabout_WebView.loadUrl(Config.ORDER_ITEM_INFO_URL + intent.getExtras().get("id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.DEAL_VOUCHER_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_voucher_title_tv_str));
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    mabout_WebView.loadUrl(Config.DEAL_VOUCHER_URL + intent.getExtras().get("id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.SUBMITTED_BIG_ORDER_INFO_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_title_order_info_str));
                intentTypeInt = intent.getExtras().getInt("type");
                if (intent.getExtras().get("group_order") != null) {
                    mGroupOrderInt =intent.getExtras().getInt("group_order");
                }
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    mabout_WebView.loadUrl(Config.SUBMITTED_ORDER_INFO_WEB_URL + intent.getExtras().get("id").toString());
                }

            } else if (intent.getExtras().getInt("type") == Config.DEMAND_INFO_WEB_INT) {
                setTransparentStatusBar();
                if (intent.getExtras().get("id") != null && intent.getExtras().get("id").toString().length() > 0) {
                    mabout_WebView.loadUrl(Config.MY_DEMAND_INFO_WEB_URL + intent.getExtras().get("id").toString() + "?is_back=1");
                }
            } else if (intent.getExtras().getInt("type") == Config.ENQUIRY_WEB_INT) {
                setTransparentStatusBar();
                if (intent.getExtras().get("id") != null &&
                        intent.getExtras().get("id").toString().length() > 0) {
                    intentTypeInt = intent.getExtras().getInt("type");
                    isAddfile = true;
                    mabout_WebView.loadUrl(Config.ADD_ENQUIRY + intent.getExtras().get("id").toString());
                    AndPermission.with(AboutUsActivity.this)
                            .requestCode(Constants.PermissionRequestCode)
                            .permission(Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .callback(listener)
                            .start();
                } else {
                    finish();
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                }
            } else if (intent.getExtras().getInt("type") == Config.ENQUIRY_LIST_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_enquiry_order_list_str));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.ENQUIRY_LIST);
            } else if (intent.getExtras().getInt("type") == Config.ENQUIRY_INFO_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.about_enquiry_order_info_str));
                if (intent.getExtras().get("id") != null &&
                        intent.getExtras().get("id").toString().length() > 0) {
                    intentTypeInt = intent.getExtras().getInt("type");
                    mabout_WebView.loadUrl(Config.ENQUIRY_OINFO + intent.getExtras().get("id").toString());
                }
            } else if (intent.getExtras().getInt("type") == Config.RECEIVE_ACCOUNT_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.applyforrelease_payment_account_txt));
                Drawable drawable = getResources().getDrawable(R.drawable.ic_xinzen);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                TitleBarUtils.showRegisterText(this, getResources().getString(R.string.about_add_reveive_account_str),
                        16,
                        getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor),
                        drawable,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent companyIntent = new Intent(AboutUsActivity.this,AboutUsActivity.class);
                                companyIntent.putExtra("type", Config.ADD_ACCOUNT_INT);
                                startActivityForResult(companyIntent, Config.ADD_ACCOUNT_INT);
                            }
                        });

                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.RECEIVE_ACCOUNT_URL);
            } else if (intent.getExtras().getInt("type") == Config.APPLY_FOR_RELEASE_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_applyforrelease_txt));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.APPLY_FOR_RELEASE_URL + LoginManager.getMobile());
            } else if (intent.getExtras().getInt("type") == Config.COMPANY_INFO_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_enterprise_str));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.COMPANY_INFO_URL);
            } else if (intent.getExtras().getInt("type") == Config.ADD_ACCOUNT_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.applyforrelease_add_payment_account_txt));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.ADD_ACCOUNT_URL);
            } else if (intent.getExtras().getInt("type") == Config.MODIFY_MOBILE_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_modifymobile_title_text));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.MODIFY_MOBILE_URL + LoginManager.getMobile() + "&enterprise_role=" +
                String.valueOf(LoginManager.getEnterprise_role()));
                isAddfile = true;
                AndPermission.with(AboutUsActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            } else if (intent.getExtras().getInt("type") == Config.ENTERPRISE_CERTIFICATE_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.enterprise_certification_title_text));
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.ENTERPRISE_CERTIFICATE_URL + LoginManager.getId());
                isAddfile = true;
                AndPermission.with(AboutUsActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            } else if (intent.getExtras().getInt("type") == Config.SITE_INTENNTION_INT) {
//                setTransparentStatusBar();
                mabout_title_layout.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                intentTypeInt = intent.getExtras().getInt("type");
                mabout_WebView.loadUrl(Config.SITE_INTENNTION_URL);
                isAddfile = true;//判断是否是上传照片
                AndPermission.with(AboutUsActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            } else if (intent.getExtras().getInt("type") == Config.COUPON_REGULATION_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_coupons_first_register_activity_regulation));
                //2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.COUPON_REGULATION_URL);
            } else if (intent.getExtras().getInt("type") == Config.INTEGRAL_EXCHANGE_COUPON_REGULATION_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_coupons_first_register_activity_regulation));
                //2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.INTEGRAL_EXCHANGE_COUPON_REGULATION_URL);

            } else if (intent.getExtras().getInt("type") == Config.NEW_GIFT_BAG_REGULATION_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_coupons_first_register_activity_regulation));
                //2017/11/8 上线修改
                mabout_WebView.loadUrl(Config.NEW_GIFT_BAG_REGULATION_URL);
            } else if (intent.getExtras().getInt("type") == Config.THEME_WEB_INT) {
                TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_theme_title));
                if (intent.getExtras().get("id") != null &&
                        intent.getExtras().get("id").toString().length() > 0) {
                    intentTypeInt = intent.getExtras().getInt("type");
                    mThemeId = intent.getExtras().get("id").toString();
                    mabout_WebView.loadUrl(Config.THEME_INFO_URL + intent.getExtras().get("id").toString());
                }
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
            this.finish();
        }
        mabout_notices_back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mabout_WebView.canGoBack()
                        && intentTypeInt != Config.ENQUIRY_LIST_WEB_INT) {
                    mabout_WebView.goBack();// 返回前一个页面
                } else {
                    AboutUsActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
        if (miniShareBitmap != null) {
            miniShareBitmap.recycle();
        }
    }

    // 这是他定义由 addJavascriptInterface 提供的一个Object
    public class DemoJavaScriptInterface {
        private Context context;

        private DemoJavaScriptInterface(Context context) {
            super();
            this.context = context;
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * 这不是呼吁界面线程。发表一个运行调用
         * loadUrl on the UI thread.
         * loadUrl在UI线程。
         */
        @JavascriptInterface
        public String _get_platform() {
            String android = "android";
            return android;
        }

        @JavascriptInterface
        public void _invite() {        // 注意这里的名称。(),注意，注意，严重注意
            mRecommendHandler.post(new Runnable() {
                public void run() {
                    share_show(Constants.getnotices_urlstring(notices_url, "item_count"), Constants.getnotices_urlstring(notices_url, "most_expensive_field_price"));
                }
            });
        }
    }

    private void share_show(String description_one, String description_two) {
        mIWXAPI = WXAPIFactory.createWXAPI(AboutUsActivity.this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        pw = new AlertDialog.Builder(this).create();
        View myView = AboutUsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
        Constants constants = new Constants(AboutUsActivity.this,
                "");
        constants.share_showPopupWindow(AboutUsActivity.this, myView, pw, mIWXAPI, notices_url,
                getResources().getString(R.string.aboutus_notices_title_text), getResources().getString(R.string.track_share_description_one_text) + description_one +
                        getResources().getString(R.string.home_share_show_description_twotext) +
                        description_two + getResources().getString(R.string.term_types_unit_txt), ShareBitmap);
    }

    private void setTransparentStatusBar() {
        mabout_title_layout.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                mabout_WebView != null &&
                mabout_WebView.canGoBack()
                && intentTypeInt != Config.ENQUIRY_LIST_WEB_INT) {
            mabout_WebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == Constants.PermissionRequestCode) {
                if (isAddfile) {
                    mabout_WebView.setWebChromeClient(new WebChromeClient() {
                        // For 3.0+ Devices (Start)
                        // onActivityResult attached before constructor
                        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                            Log.i("web上传图片","3.0+");
                            mUploadMessage = uploadMsg;
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("*/*");
                            AboutUsActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                        }

                        // For Lollipop 5.0+ Devices
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                            Log.i("web上传图片","5.0+");
                            if (uploadMessage != null) {
                                uploadMessage.onReceiveValue(null);
                                uploadMessage = null;
                            }
                            uploadMessage = filePathCallback;
                            try {
                                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                i.addCategory(Intent.CATEGORY_OPENABLE);
                                i.setType("*/*");
                                startActivityForResult(Intent.createChooser(i, "File Chooser"), REQUEST_SELECT_FILE);
                            } catch (ActivityNotFoundException e) {
                                uploadMessage = null;
                                MessageUtils.showToast(getBaseContext(), "Cannot Open File Chooser");
                                return false;
                            }
                            return true;
                        }

                        //For Android 4.1 only
                        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                            Log.i("web上传图片","4.1");
                            mUploadMessage = uploadMsg;
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            AboutUsActivity.this.startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
                        }

                        //for Android <3.0
                        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                            Log.i("web上传图片","<3.0");
                            mUploadMessage = uploadMsg;
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("*/*");
                            AboutUsActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                        }
                    });

                } else {
                    MQConfig.init(AboutUsActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
                        @Override
                        public void onSuccess(String clientId) {
                            if (LoginManager.isLogin()) {
                                HashMap<String, String> clientInfo = new HashMap<>();
                                clientInfo.put("name", LoginManager.getCompany_name());
                                clientInfo.put("email", LoginManager.geteEmail());
                                if (LoginManager.getRole_id().equals("2")) {
                                    clientInfo.put("comment", getResources().getString(R.string.module_myself_role_property_str));
                                } else if (LoginManager.getRole_id().equals("3")) {
                                    clientInfo.put("comment", getResources().getString(R.string.module_myself_role_business_str));
                                } else if (LoginManager.getRole_id().equals("1")) {
                                    clientInfo.put("comment", getResources().getString(R.string.module_myself_role_admin_str));
                                }
                                clientInfo.put("avatar", "https://banner.linhuiba.com/o_1b555h2jjoj6u1716tr12dl2rg7.jpg");
                                clientInfo.put("tel", LoginManager.getMobile());
                                // 相同的 id 会被识别为同一个顾客
                                Intent intent = new MQIntentBuilder(AboutUsActivity.this)
                                        .setClientInfo(clientInfo)
                                        .setCustomizedId(LoginManager.getUid())
                                        .build();
                                startActivityForResult(intent, 10);
                            } else {
                                Intent intent = new MQIntentBuilder(AboutUsActivity.this)
                                        .build();
                                startActivityForResult(intent, 10);
                            }

                        }

                        @Override
                        public void onFailure(int code, String message) {
                            MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                        }
                    });
                }
            }  else if (requestCode == CALL_PHONE_CODE) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + callPhoneStr));
                if (ActivityCompat.checkSelfPermission(AboutUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            } else if (requestCode == CALL_PHONE_CODE) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_rationale));
            }
        }
    };
    private LinhuiAsyncHttpResponseHandler deleteorderHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.cancel_success));
            finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler deleteEnquiryHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (intentTypeInt == Config.ENQUIRY_LIST_WEB_INT) {
                mabout_WebView.loadUrl(Config.ENQUIRY_LIST);
            } else {
                Intent  enquiryIntent = new Intent();
                AboutUsActivity.this.setResult(Config.ENQUIRY_LIST_WEB_INT,enquiryIntent);
                finish();
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };

    private LinhuiAsyncHttpResponseHandler virtualNumberHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, final Object data) {
            hideProgressDialog();
            if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                View.OnClickListener uploadListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_perfect:
                                mCustomDialog.dismiss();
                                if (data != null && data.toString().length() > 0) {
                                    callPhoneStr = data.toString();
                                    AndPermission.with(AboutUsActivity.this)
                                            .requestCode(CALL_PHONE_CODE)
                                            .permission(
                                                    Manifest.permission.CALL_PHONE,
                                                    Manifest.permission.READ_PHONE_STATE)
                                            .callback(listener)
                                            .start();
                                }

                                break;
                            case R.id.btn_cancel:
                                mCustomDialog.dismiss();
                        }
                    }
                };
                CustomDialog.Builder builder = new CustomDialog.Builder(AboutUsActivity.this);
                mCustomDialog = builder
                        .cancelTouchout(false)
                        .view(R.layout.field_activity_field_orders_success_dialog)
                        .addViewOnclick(R.id.btn_cancel, uploadListener)
                        .addViewOnclick(R.id.btn_perfect, uploadListener)
                        .setText(R.id.dialog_title_textview,
                                getResources().getString(R.string.order_call_service_phone_str))
                        .setText(R.id.btn_cancel,
                                getResources().getString(R.string.cancel))
                        .setText(R.id.btn_perfect,
                                getResources().getString(R.string.confirm))
                        .showView(R.id.linhuiba_logo_imgv, View.GONE)
                        .build();
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(AboutUsActivity.this, mCustomDialog);
                mCustomDialog.show();
                mDialogHandler.removeMessages(0);
                mDialogHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                    }
                }, 30000);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (mServicePhone != null && mServicePhone.toString().length() > 0) {
                callPhoneStr = mServicePhone;
                AndPermission.with(AboutUsActivity.this)
                        .requestCode(CALL_PHONE_CODE)
                        .permission(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE)
                        .callback(listener)
                        .start();
            }
        }
    };

    private void showShareDialog(final String share_linkurl, final String ShareIconStr, final String ShareTitleStr, final String SharedescriptionStr) {
        if (ShareIconStr != null &&
                ShareIconStr.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShareLinkurl = share_linkurl;
                    mShareTitleStr = ShareTitleStr;
                    mShareDescriptionStr = SharedescriptionStr;

                    if (ShareIconStr.equals("demand")) {
                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_demand);
                    } else {
                        ShareBitmap = Bitmap.createScaledBitmap(com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr), 120, 120, true);//压缩Bitmap
                        if (ShareBitmap == null) {
                            ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
                        }
                    }
                    mShareHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            if (mShareDialog == null) {
                ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
                View myView = AboutUsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                mShareDialog = new AlertDialog.Builder(AboutUsActivity.this).create();
                Constants constants = new Constants(AboutUsActivity.this,
                        "");
                constants.share_showPopupWindow(AboutUsActivity.this, myView, mShareDialog, mIWXAPI, share_linkurl,
                        ShareTitleStr,
                        SharedescriptionStr, ShareBitmap);
            } else {
                if (mShareDialog!= null || !mShareDialog.isShowing()) {
                    mShareDialog.show();
                }
            }
        }
    }

    private ArrayList<HashMap<String, Object>> getOrderConfirmJson(JSONArray JsonArray) {
        ArrayList<HashMap<String, Object>> jsonArray = new ArrayList<HashMap<String, Object>>();
        if (mCommunityIds != null) {
            mCommunityIds.clear();
        }
        for (int i = 0; i < JsonArray.size(); i++) {
            JSONObject jsonpObject = JsonArray.getJSONObject(i);
            if (jsonpObject != null) {
                HashMap<String, Object> json = new HashMap<String, Object>();
                if (jsonpObject.get("id") != null &&
                        jsonpObject.get("id").toString().length() > 0) {
                    json.put("id", Integer.parseInt(jsonpObject.get("id").toString()));
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("community_id") != null &&
                        jsonpObject.get("community_id").toString().length() > 0) {
                    json.put("community_id", Integer.parseInt(jsonpObject.get("community_id").toString()));
                    if (mCommunityIds.size() > 0 && mCommunityIds.contains(Integer.parseInt(jsonpObject.get("community_id").toString()))) {

                    } else {
                        mCommunityIds.add(Integer.parseInt(jsonpObject.get("community_id").toString()));
                    }
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("start") != null &&
                        jsonpObject.get("start").toString().length() > 0) {
                    json.put("date", jsonpObject.get("start").toString());
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("size") != null &&
                        jsonpObject.get("size").toString().length() > 0) {
                    json.put("size", jsonpObject.get("size").toString());
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("count") != null &&
                        jsonpObject.get("count").toString().length() > 0) {
                    json.put("count", Integer.parseInt(jsonpObject.get("count").toString()));
                } else {
                    json.put("count", 1);
                }
                if (jsonpObject.get("custom_dimension") != null &&
                        jsonpObject.get("custom_dimension").toString().length() > 0) {
                    json.put("custom_dimension", jsonpObject.get("custom_dimension").toString());
                } else {
                    json.put("custom_dimension", "");
                }
                if (jsonpObject.get("count_of_time_unit") != null &&
                        jsonpObject.get("count_of_time_unit").toString().length() > 0) {
                    json.put("count_of_time_unit", Integer.parseInt(jsonpObject.get("count_of_time_unit").toString()));
                } else {
                    json.put("count_of_time_unit", 1);
                }
                if (jsonpObject.get("lease_term_type_name") != null &&
                        jsonpObject.get("lease_term_type_name").toString().length() > 0) {
                    json.put("lease_term_type", jsonpObject.get("lease_term_type_name").toString());
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("price") != null &&
                        jsonpObject.get("price").toString().length() > 0) {
                    if (jsonpObject.get("discount_price") != null &&
                            jsonpObject.get("discount_price").toString().length() > 0) {

                        json.put("price",
                                Constants.getdoublepricestring(Double.parseDouble(jsonpObject.get("price").toString()) +
                                        Double.parseDouble(jsonpObject.get("discount_price").toString()), 1));
                    } else {
                        json.put("price", jsonpObject.get("price").toString());
                    }
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("discount_price") != null &&
                        jsonpObject.get("discount_price").toString().length() > 0) {
                    json.put("subsidy_fee", jsonpObject.get("discount_price").toString());
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("lease_term_type_id") != null &&
                        jsonpObject.get("lease_term_type_id").toString().length() > 0) {
                    json.put("lease_term_type_id", jsonpObject.get("lease_term_type_id").toString());
                } else {
                    return jsonArray;
                }
                if (jsonpObject.get("name") != null &&
                        jsonpObject.get("name").toString().length() > 0) {
                    json.put("resourcename", jsonpObject.get("name").toString());
                } else {
                    json.put("resourcename", "");
                }
                if (jsonpObject.get("pic_url") != null &&
                        jsonpObject.get("pic_url").toString().length() > 0) {
                    json.put("imagepath", jsonpObject.get("pic_url").toString());
                } else {
                    json.put("imagepath", "");
                }
                if (jsonpObject.get("field_type") != null &&
                        jsonpObject.get("field_type").toString().length() > 0) {
                    json.put("field_type", jsonpObject.get("field_type").toString());
                } else {
                    json.put("field_type", "");
                }
                if (jsonpObject.get("discount_rate") != null &&
                        jsonpObject.get("discount_rate").toString().length() > 0) {
                    json.put("discount_rate", jsonpObject.get("discount_rate").toString());
                } else {
                    json.put("discount_rate", "0");
                }
                if (jsonpObject.get("enquiry_id") != null &&
                        jsonpObject.get("enquiry_id").toString().length() > 0) {
                    json.put("enquiry_id", jsonpObject.get("enquiry_id").toString());
                }
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_FILE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (uploadMessage == null)
                        return;
//                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    uploadMessage = null;
                    onActivityResultAboveL(requestCode, resultCode, data);
                }
                break;
            case FILECHOOSER_RESULTCODE:
                if (null == mUploadMessage)
                    return;
                // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
                // Use RESULT_OK only if you're implementing WebView inside an Activity
                Uri result = data == null || resultCode != AboutUsActivity.RESULT_OK ? null : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
                break;
            case Config.FACILITATOR_INT:
                int is_login = 0;
                if (LoginManager.isLogin()) {
                    is_login = 1;
                    mabout_WebView.loadUrl(Config.FACILITATOR_LIST_URL + "?city_id=" +
                            cityId + "&is_login=" +
                            String.valueOf(is_login));
                }
                break;
            case Config.SMALL_ORDER_INFO_INT:
                if (orderItemId != null && orderItemId.length() > 0) {
                    mabout_WebView.loadUrl(Config.ORDER_ITEM_INFO_URL + orderItemId);
                }
                break;
        }
        if (resultCode == Config.ENQUIRY_LIST_WEB_INT) {
            Intent enquiryIntent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
            enquiryIntent.putExtra("type",Config.ENQUIRY_LIST_WEB_INT);
            startActivity(enquiryIntent);
            finish();
        } else if (resultCode == Config.ADD_ACCOUNT_INT) {
            mabout_WebView.loadUrl(Config.RECEIVE_ACCOUNT_URL);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Handler mShareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //不包含小程序分享
                    if (mShareDialog == null) {
                        View myView = AboutUsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                        mShareDialog = new AlertDialog.Builder(AboutUsActivity.this).create();
                        Constants constants = new Constants(AboutUsActivity.this,
                                "");
                        constants.share_showPopupWindow(AboutUsActivity.this, myView, mShareDialog, mIWXAPI, mShareLinkurl,
                                mShareTitleStr,
                                mShareDescriptionStr, ShareBitmap);
                    } else {
                        if (mShareDialog!= null || !mShareDialog.isShowing()) {
                            mShareDialog.show();
                        }
                    }
                    break;
                case 1:
                    //包含小程序分享
                    hideProgressDialog();
                    final View myView = AboutUsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    pw = new AlertDialog.Builder(AboutUsActivity.this).create();
                    if (pw!= null && pw.isShowing()) {
                        pw.dismiss();
                    }
                    Constants constants = new Constants(AboutUsActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(AboutUsActivity.this,myView,pw,mIWXAPI,shareWXLinkurl,
                            mShareTitleStr,
                            mShareDescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mShareTitleStr);
                    break;
                default:
                    break;
            }
        }

    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_SELECT_FILE || uploadMessage == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessage.onReceiveValue(results);
        uploadMessage = null;
    }
    public boolean syncCookie(String url,String key) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, "LINHUIBA_KEY="+key);
        String newCookie = cookieManager.getCookie(url);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(AboutUsActivity.this);
            cookieSyncManager.sync();
        }
        return TextUtils.isEmpty(newCookie) ? false : true;
    }
    /**
     * 专题分享
     */
    private void themeShare() {
        //2017/9/23 分享功能
        //2018/7/4 title设置 朋友圈 和小程序
        if (ShareBitmap == null || miniShareBitmap == null) {
            showProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 2018/7/4 判断bitmap
                    if (ShareIconStr != null && ShareIconStr.length() > 0) {
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                        ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 300, 220, true);//压缩Bitmap
                    }
                    if (ShareBitmap == null) {
                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                    }
                    if (miniShareBitmap == null) {
                        miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
                    }
                    mShareHandler.sendEmptyMessage(1);
                }
            }).start();
        } else {
            final View myView = AboutUsActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            pw = new AlertDialog.Builder(AboutUsActivity.this).create();
            if (pw!= null && pw.isShowing()) {
                pw.dismiss();
            }
            Constants constants = new Constants(AboutUsActivity.this,
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(AboutUsActivity.this,myView,pw,mIWXAPI,shareWXLinkurl,
                    mShareTitleStr,
                    mShareDescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mShareTitleStr);
        }
    }

}

