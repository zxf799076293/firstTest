package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldactivity.Field_GlideLoader;
import com.linhuiba.business.fieldadapter.Field_ChoosePictureGridViewAdapter;
import com.linhuiba.business.fieldbusiness.Field_FieldApi;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_MyGridView;
import com.linhuiba.business.fieldview.Field_NewGalleryView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/20.
 */
public class OrderConfirmUploadVoucherActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall{
    private Field_ChoosePictureGridViewAdapter adapter;
    private ArrayList<String> choose_filepicture = new ArrayList<>();//之前已选择的图片list不用重新截图
    private ArrayList<String> choose_filepicture_editor = new ArrayList<>();//上传修改的图片的file
    private ArrayList<String> choose_filepicture_url = new ArrayList<>();//上传修改的图片的file以外的网络url
    private ArrayList<String> choose_filepicture_tmp = new ArrayList<>();//之前已选择图片的过渡list(用来判断)
    private String photosavafilestr;//存到choose_filepicture中的String变量
    private File photosavafile;//截取图后保存到手机的file
    private boolean booleancamera = false;//是否是选择拍照
    private ArrayList<String> interceptionpathlist = new ArrayList<>();//需要截取的图片urllist
    private ArrayList<String> galleryviewlist = new ArrayList<>();//预览图片的list
    private int newPosition;//预览删除图片的position
    private ArrayList<String> addfieldimg_str = new ArrayList<>();//七牛上传的list
    private UploadManager uploadManager = new UploadManager();
    private String uploadtoken;//七牛上传的凭证
    private int addfieldimgsize;//要上传图片的position
    private boolean upload = true;//上传到七牛的图片成功的标志
    @InjectView(R.id.remarks_addpicture)
    Field_MyGridView mremarks_addpicturegridview;
    @InjectView(R.id.account_pay_button)
    Button maccount_pay_button;
    @InjectView(R.id.order_number)
    TextView morder_number;
    @InjectView(R.id.remittance_remind_close_imagebutton)
    ImageButton mremittance_remind_close_imagebutton;
    @InjectView(R.id.remittance_remind_layout)
    RelativeLayout mremittance_remind_layout;
    @InjectView(R.id.upload_voucher_name_edit)
    TextView mupload_voucher_name_edit;
    @InjectView(R.id.upload_voucher_bankname_edit)
    TextView mupload_voucher_bankname_edit;
    @InjectView(R.id.upload_voucher_payment_account_edit)
    TextView mupload_voucher_payment_account_edit;
    @InjectView(R.id.account_pay_layout)
    LinearLayout maccount_pay_layout;
    @InjectView(R.id.calendarclick_remind)
    TextView tv_calendarclick_remind;
    private String order_id = "";
    private String voucher_image_url = "";//凭证url
    private int type;//adapter 中点击查看和上传按钮的操作1:adapter跳转进来的
    private int remittance_type;// == 1是下单 == 2是钱包充值 3是拼团
    private final int IntentTypeOrderInt = 1;//订单
    private final int IntentTypeWalletInt = 2;//钱包
    private final int IntentTypeGroupOrderInt = 3;//拼团
    private final int IntentAdapterItem = 1;//adapter跳转进来的
    private String payment_price = "";//充值金额
    private Dialog zoom_picture_dialog;
    private List<ImageView> imageList;
    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder_brought_to_account);
        ButterKnife.inject(this);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.close), 17, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == IntentAdapterItem) {
                    Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, OrderListNewActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);
                    bundle_ordertype1.putInt("showdialog", 2);
                    orderpayintent.putExtras(bundle_ordertype1);
                    orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(orderpayintent);
                    OrderConfirmUploadVoucherActivity.this.finish();
                } else {
                    if (remittance_type == IntentTypeOrderInt) {
                        Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, OrderListNewActivity.class);
                        Bundle bundle_ordertype1 = new Bundle();
                        bundle_ordertype1.putInt("order_type", 1);
                        bundle_ordertype1.putInt("showdialog", 2);
                        orderpayintent.putExtras(bundle_ordertype1);
                        orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(orderpayintent);
                        OrderConfirmUploadVoucherActivity.this.finish();
                    } else if (remittance_type == IntentTypeGroupOrderInt) {
                        Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, GroupBookingMainActivity.class);
                        Bundle bundle_ordertype1 = new Bundle();
                        bundle_ordertype1.putInt("order_type", 1);
                        bundle_ordertype1.putInt("showdialog", 2);
                        orderpayintent.putExtras(bundle_ordertype1);
                        startActivity(orderpayintent);
                        OrderConfirmUploadVoucherActivity.this.finish();
                    } else {
                        Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, WalletRechargeParticularsActivity.class);
                        orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        orderpayintent.putExtra("type",1);
                        startActivity(orderpayintent);
                        OrderConfirmUploadVoucherActivity.this.finish();
                    }
                }
            }
        });
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.confirmorder_account));
        Intent orderconfirintent = getIntent();
        if (orderconfirintent.getExtras() != null) {
            if (orderconfirintent.getExtras().get("order_id")!= null) {
                order_id =orderconfirintent.getExtras().get("order_id").toString();
            }
            if (orderconfirintent.getExtras().get("voucher_image_url")!= null) {
                voucher_image_url = orderconfirintent.getExtras().get("voucher_image_url").toString();
                choose_filepicture.add(orderconfirintent.getExtras().get("voucher_image_url").toString());
            }
            if (orderconfirintent.getExtras().get("type")!= null) {
                type = orderconfirintent.getExtras().getInt("type");
            }

            if (orderconfirintent.getExtras().get("remittance") != null &&
                    orderconfirintent.getExtras().getInt("remittance") == 1) {
                if (orderconfirintent.getExtras().get("order_num")!= null) {
                    morder_number.setText(getResources().getString(R.string.order_confirm_ordernum_text)+
                            orderconfirintent.getExtras().getString("order_num"));
                } else {
                    morder_number.setText(getResources().getString(R.string.order_confirm_ordernum_text)+
                            getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                tv_calendarclick_remind.setText(getResources().getString(R.string.order_confirm_transfer_order_number_remind_str));
                if (orderconfirintent.getExtras().get("isGroupOrder") != null &&
                        orderconfirintent.getExtras().getInt("isGroupOrder") == 1) {
                    remittance_type = IntentTypeGroupOrderInt;
                } else {
                    remittance_type = IntentTypeOrderInt;
                }
            } else if (orderconfirintent.getExtras().get("remittance") != null &&
                    orderconfirintent.getExtras().getInt("remittance") == 2){
                if (orderconfirintent.getExtras().get("payment_price") != null &&
                        orderconfirintent.getExtras().get("payment_price").toString().length() > 0) {
                    payment_price = orderconfirintent.getExtras().get("payment_price").toString();
                }
                if (orderconfirintent.getExtras().get("order_num")!= null) {
                    morder_number.setText(getResources().getString(R.string.wallet_mywallet_recharge_serial_number_text)+
                            orderconfirintent.getExtras().getString("order_num"));
                } else {
                    morder_number.setText(getResources().getString(R.string.wallet_mywallet_recharge_serial_number_text)+
                            getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                tv_calendarclick_remind.setText(getResources().getString(R.string.order_confirm_transfer_remind_text));
                remittance_type = IntentTypeWalletInt;
                if (orderconfirintent.getExtras().get("voucher_confirmed") != null &&
                        orderconfirintent.getExtras().getInt("voucher_confirmed") == 1) {
                    maccount_pay_layout.setVisibility(View.GONE);
                }
            }

        }

        initview();
    }
    private void initview() {
        Constants.delete_picture_file();
        if (choose_filepicture.size() < 1) {
            choose_filepicture.add("firstgridviewitem");
        }
        adapter = new Field_ChoosePictureGridViewAdapter(OrderConfirmUploadVoucherActivity.this, OrderConfirmUploadVoucherActivity.this, choose_filepicture, 4);
        mremarks_addpicturegridview.setAdapter(adapter);
        if (remittance_type == IntentTypeOrderInt || remittance_type == IntentTypeGroupOrderInt) {
            UserApi.getpayment_orders(MyAsyncHttpClient.MyAsyncHttpClient(),payment_orderHandler,order_id);
        }
    }
    @OnClick({
            R.id.account_pay_button,
            R.id.remittance_remind_close_imagebutton
    })
    public void publishreview(View view) {
        switch (view.getId()) {
            case R.id.account_pay_button:
                for (int i = 0; i < choose_filepicture.size(); i++) {
                    if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                        choose_filepicture.remove(i);
                    }
                }
                if (choose_filepicture.size() > 0) {
                    uploadimg();
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.mPic_map_prompt));
                }
                break;
            case R.id.remittance_remind_close_imagebutton:
                mremittance_remind_layout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    private LinhuiAsyncHttpResponseHandler upload_voucherHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (type == IntentAdapterItem) {
                MessageUtils.showToast(getResources().getString(R.string.order_refuse_offline_pay_upload_success));
                OrderConfirmUploadVoucherActivity.this.setResult(1, new Intent());
                OrderConfirmUploadVoucherActivity.this.finish();
            } else {
                if (remittance_type == IntentTypeOrderInt || remittance_type == IntentTypeGroupOrderInt) {
                    Intent intent = new Intent();
                    OrderConfirmUploadVoucherActivity.this.setResult(2, new Intent());
                    OrderConfirmUploadVoucherActivity.this.finish();
                } else if (remittance_type == IntentTypeWalletInt) {
                    OrderConfirmUploadVoucherActivity.this.finish();
                    Intent maintabintent = new Intent(OrderConfirmUploadVoucherActivity.this, WalletRechargeSuccessActivity.class);
                    maintabintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    maintabintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    maintabintent.putExtra("payment_mode", 0);
                    maintabintent.putExtra("payment_price", payment_price);
                    startActivity(maintabintent);
                }
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
    private LinhuiAsyncHttpResponseHandler payment_orderHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            mupload_voucher_name_edit.setText(JSONObject.parseObject(data.toString()).get("account_holder").toString());
            mupload_voucher_bankname_edit.setText(JSONObject.parseObject(data.toString()).get("opening_bank").toString());
            mupload_voucher_payment_account_edit.setText(JSONObject.parseObject(data.toString()).get("beneficiary_account_number").toString());
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }

    @Override
    public void AdField_showPreviewImg(int position) {
        if (position == -1) {
            AndPermission.with(OrderConfirmUploadVoucherActivity.this)
                    .requestCode(Constants.PermissionRequestCode)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(listener)
                    .start();
        } else {
            if (galleryviewlist != null) {
                galleryviewlist.clear();
            }
            galleryviewlist .addAll(choose_filepicture);
            for (int i = 0; i < galleryviewlist.size(); i++) {
                if (galleryviewlist.get(i).toString().equals("firstgridviewitem")) {
                    galleryviewlist.remove(i);
                }
            }
            show_zoom_picture_dialog(position,galleryviewlist);
            maccount_pay_button.setVisibility(View.GONE);
        }

    }
    private void show_zoom_picture_dialog(final int position, ArrayList<String> mPicList) {
        View myView = OrderConfirmUploadVoucherActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        zoom_picture_dialog = new AlertDialog.Builder(OrderConfirmUploadVoucherActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
        zoom_picture_dialog.setOnKeyListener(onKeylistener);
        final TextView mshowpicture_dialog_sizetxt = (TextView)myView.findViewById(R.id.showpicture_dialog_sizetxt);
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.showpicture_dialog_back);
        final ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.zoom_dialog_viewpage);
        ImageButton mshowpicture_dialog_deletebtn = (ImageButton)myView.findViewById(R.id.showpicture_dialog_deletebtn);
        mshowpicture_dialog_deletebtn.setVisibility(View.VISIBLE);
        mshowpicture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_picture_dialog.dismiss();
                maccount_pay_button.setVisibility(View.VISIBLE);
            }
        });
        imageList = new ArrayList<>();
        for (int i = 0; i < mPicList.size(); i++) {
            ZoomImageView imageView = new ZoomImageView(
                    getApplicationContext());
            Glide.with(this)
                    .load(mPicList.get(i).toString())
                    .into(imageView);
            imageList.add(imageView);
        }
        mshowpicture_dialog_deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryviewlist.remove(newPosition%imageList.size());
                choose_filepicture.remove(newPosition%imageList.size());
                imageList.remove(newPosition%imageList.size());
                int addpictureimg_num = 0;
                for (int i = 0; i < choose_filepicture.size(); i++) {
                    if (choose_filepicture.get(i).equals("firstgridviewitem")) {
                        addpictureimg_num++;
                    }
                }
                if (addpictureimg_num == 0) {
                    choose_filepicture.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
                zoom_picture_dialog.dismiss();
                maccount_pay_button.setVisibility(View.VISIBLE);

            }
        });
        newPosition = position;
        com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(OrderConfirmUploadVoucherActivity.this,OrderConfirmUploadVoucherActivity.this,newPosition,0);
        constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,newPosition);
    }
    private void choosepicture() {
        if (choose_filepicture != null) {
            for (int i = 0; i < choose_filepicture.size(); i++) {
                if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                    choose_filepicture.remove(i);
                }
            }
        }
        if (choose_filepicture_tmp != null) {
            choose_filepicture_tmp.clear();
        }
        choose_filepicture_tmp.addAll(choose_filepicture);
        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // Field_GlideLoader 可用自己用的缓存库
                new Field_GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.default_bluebg))
                        // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.default_bluebg))
                        // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                        // 开启多选   （默认为多选）  (单选 为 singleSelect)
//                .singleSelect()
                .mutiSelect()
                .crop()
                        // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(1)
                        // 已选择的图片路径
                .pathList(choose_filepicture)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(Constants.PhotoRequestCode)
                .build();

        ImageSelector.open(OrderConfirmUploadVoucherActivity.this, imageConfig);   // 开启图片选择器
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PhotoRequestCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            ArrayList<String> pathList_tmp = new ArrayList<>();
            pathList_tmp.addAll(choose_filepicture_tmp);
            booleancamera = false;
            if (interceptionpathlist != null) {
                interceptionpathlist.clear();
            }
            if (booleancamera == false) {
                if (choose_filepicture != null) {
                    choose_filepicture.clear();
                }
            }
            choose_filepicture.addAll(getIntersection(pathList_tmp, pathList));
            if (compare(choose_filepicture,pathList)) {
                if (choose_filepicture.size() < 1) {
                    choose_filepicture.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
            } else {
                if (choose_filepicture != null) {
                    if (choose_filepicture.size() > 0) {
                        interceptionpathlist.addAll(getAnd_set(pathList,choose_filepicture));
                    } else {
                        interceptionpathlist.addAll(pathList);
                    }
                } else {
                    interceptionpathlist.addAll(pathList);
                }
                for (int i = 0; i < choose_filepicture.size(); i++) {
                    if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                        choose_filepicture.remove(i);
                    }
                }
                for (int i = 0; i < interceptionpathlist.size(); i++) {
                    choose_filepicture.add(interceptionpathlist.get(i));
                }
                if (choose_filepicture.size() < 1) {
                    choose_filepicture.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
            }


        } else if (requestCode == Constants.PhotoRequestCode) {
            if (choose_filepicture!= null) {
                choose_filepicture.clear();
            }
            choose_filepicture.addAll(choose_filepicture_tmp);
            if (choose_filepicture.size() < 1) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter.notifyDataSetChanged();
        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                booleancamera = true;
                for (int i = 0; i < choose_filepicture.size(); i++) {
                    if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                        choose_filepicture.remove(i);
                    }
                }
                choose_filepicture.add(photosavafilestr);
                if (choose_filepicture.size() < 1) {
                    choose_filepicture.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public static ArrayList<String> getIntersection(ArrayList<String> list1,
                                                    ArrayList<String> list2) {
        ArrayList<String> result = new ArrayList<String>();
        for (String String : list2) {//遍历list1
            if (list1.contains(String)) {//如果存在这个数
                result.add(String);//放进一个list里面，这个list就是交集
            }
        }
        return result;
    }
    public static ArrayList<String> getAnd_set(ArrayList<String> list2,
                                               ArrayList<String> list1) {
        ArrayList<String> result = new ArrayList<String>();
        for (String String : list2) {//遍历list1
            if (!(list1.contains(String))) {//如果存在这个数
                result.add(String);//放进一个list里面，这个list就是交集
            }
        }
        return result;
    }
    public static <T extends Comparable<T>> boolean compare(ArrayList<T> a, ArrayList<T> b) {
        if(a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (zoom_picture_dialog != null && zoom_picture_dialog.isShowing()) {
                zoom_picture_dialog.dismiss();
                maccount_pay_button.setVisibility(View.VISIBLE);
            } else {
                if (type == IntentAdapterItem) {
                    Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, OrderListNewActivity.class);
                    Bundle bundle_ordertype1 = new Bundle();
                    bundle_ordertype1.putInt("order_type", 1);
                    bundle_ordertype1.putInt("showdialog", 2);
                    orderpayintent.putExtras(bundle_ordertype1);
                    orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(orderpayintent);
                    OrderConfirmUploadVoucherActivity.this.finish();
                } else {
                    if (remittance_type == IntentTypeOrderInt) {
                        Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, OrderListNewActivity.class);
                        Bundle bundle_ordertype1 = new Bundle();
                        bundle_ordertype1.putInt("order_type", 1);
                        bundle_ordertype1.putInt("showdialog", 2);
                        orderpayintent.putExtras(bundle_ordertype1);
                        orderpayintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        orderpayintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(orderpayintent);
                        OrderConfirmUploadVoucherActivity.this.finish();
                    } else if (remittance_type == IntentTypeGroupOrderInt) {
                        Intent orderpayintent = new Intent(OrderConfirmUploadVoucherActivity.this, GroupBookingMainActivity.class);
                        Bundle bundle_ordertype1 = new Bundle();
                        bundle_ordertype1.putInt("order_type", 1);
                        bundle_ordertype1.putInt("showdialog", 2);
                        orderpayintent.putExtras(bundle_ordertype1);
                        startActivity(orderpayintent);
                        OrderConfirmUploadVoucherActivity.this.finish();
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    DialogInterface.OnKeyListener onKeylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                maccount_pay_button.setVisibility(View.VISIBLE);
                return false;
            } else {
                return true;
            }
        }
    };

    private void uploadimg() {
        for (int i = 0; i < choose_filepicture.size(); i++) {
            if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                choose_filepicture.remove(i);
            }
        }
        if (choose_filepicture_editor != null) {
            choose_filepicture_editor.clear();
        }
        choose_filepicture_editor.addAll(choose_filepicture);
        if (choose_filepicture_url != null) {
            choose_filepicture_url.clear();
        }
        if (voucher_image_url.length() > 0) {
            if (choose_filepicture_editor.contains(voucher_image_url)) {
                choose_filepicture_editor.remove(voucher_image_url);
                choose_filepicture_url.add(voucher_image_url);
            }
        }
        if (choose_filepicture_editor.size() > 0) {
            showProgressDialog();
            Field_FieldApi.getuptoken_certs(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
        } else {
            if (addfieldimg_str != null) {
                addfieldimg_str.clear();
            }
            for (int i = 0; i < choose_filepicture_url.size(); i++) {
                addfieldimg_str.add(choose_filepicture_url.get(i));
            }
            showProgressDialog();
            //保存按钮的操作功能
            if (remittance_type == IntentTypeOrderInt || remittance_type == IntentTypeGroupOrderInt) {
                FieldApi.upload_voucher(MyAsyncHttpClient.MyAsyncHttpClient2(), upload_voucherHandler,order_id, addfieldimg_str.get(0));
            } else if (remittance_type == IntentTypeWalletInt) {
                FieldApi.transactions_upload_voucher(MyAsyncHttpClient.MyAsyncHttpClient2(), upload_voucherHandler,order_id, addfieldimg_str.get(0));
            }
        }
    }
    private LinhuiAsyncHttpResponseHandler UptokenHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, final Object data) {
//        data = <File对象、或 文件路径、或 字节数组>
//        String key = <指定七牛服务上的文件名，或 null>;
//        String token = <从服务端SDK获取>;
            if (data != null) {
                uploadtoken = data.toString();
                if (addfieldimg_str != null) {
                    addfieldimg_str.clear();
                }
                addfieldimgsize = 0;

                upload = true;
                uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                        upCompletionHandler, null);
            } else {
                MessageUtils.showToast(OrderConfirmUploadVoucherActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
            }


        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, org.json.JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                try {
                    addfieldimgsize ++;
                    addfieldimg_str.add(Config.qiniu_domain_creats + jsonObject.getString("key").toString());
                    if (addfieldimgsize == choose_filepicture_editor.size() && upload == true) {
                        for (int i = 0; i < choose_filepicture_url.size(); i++) {
                            addfieldimg_str.add(choose_filepicture_url.get(i));
                        }
                        //保存按钮的操作功能
                        if (remittance_type == IntentTypeOrderInt || remittance_type == IntentTypeGroupOrderInt) {
                            FieldApi.upload_voucher(MyAsyncHttpClient.MyAsyncHttpClient2(), upload_voucherHandler,order_id, addfieldimg_str.get(0));
                        } else if (remittance_type == IntentTypeWalletInt) {
                            FieldApi.transactions_upload_voucher(MyAsyncHttpClient.MyAsyncHttpClient2(), upload_voucherHandler,order_id, addfieldimg_str.get(0));
                        }                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(OrderConfirmUploadVoucherActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                        hideProgressDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                addfieldimgsize ++;
                upload = false;

                //删除七牛刚上传的图片  addfieldimg_str

                if (addfieldimg_str != null) {
                    addfieldimg_str.clear();
                }
                MessageUtils.showToast(OrderConfirmUploadVoucherActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

        }
    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                    View.OnClickListener uploadListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()){
                                case R.id.upload_photo_ll:
                                    mCustomDialog.dismiss();
                                    String time = "" + System.currentTimeMillis();
                                    if (Constants.SDCardState()) {
                                        photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + ".jpg";
                                        photosavafile = new File(photosavafilestr);
                                        if (!photosavafile.exists()) {
                                            try {
                                                //在指定的文件夹中创建文件
                                                photosavafile.createNewFile();
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (photosavafile != null) {
                                            Intent intent = new Intent(
                                                    MediaStore.ACTION_IMAGE_CAPTURE);
                                            //下面这句指定调用相机拍照后的照片存储的路径
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                                    .fromFile(photosavafile));
                                            startActivityForResult(intent, 2);
                                        }
                                    }                               break;
                                case R.id.upload_picture_ll:
                                    mCustomDialog.dismiss();
                                    choosepicture();
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(OrderConfirmUploadVoucherActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(OrderConfirmUploadVoucherActivity.this,mCustomDialog);
                    mCustomDialog.show();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
}
