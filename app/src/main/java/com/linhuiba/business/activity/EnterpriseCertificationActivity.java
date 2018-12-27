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
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.linhuiba.business.model.Enterprise_infoModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldactivity.PropertyDataStatisticalActivity;
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
import com.yancy.imageselector.bean.Image;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Administrator on 2016/11/28.
 */

public class EnterpriseCertificationActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall{
    private TimeCount mTime;
    private EditText edit_enterprise_companyname;
    private EditText edit_enterprise_certification_representative;
    private EditText edit_enterprise_certification_idcard;
    private EditText edit_enterprise_certification_company_code;
    private TextView text_enterprise_certification_uploadlicense;
    private RelativeLayout layout_enterprise_certification_choose_city;
    private EditText edit_enterprise_certification_choose_address;
    public TextView mregion_textview;
    private EditText edit_enterprise_certification_applicant;
    private EditText edit_enterprise_certification_phonenumber;
    private EditText edit_enterprise_certification_captcha;
    private Button btn_enterprise_certification_captchabtn;
    private Button btn_certification_btn;
    private Field_MyGridView mygridview;
    private RelativeLayout mcertification_check_layout;
    private RelativeLayout mrefuse_reason_layout;
    private TextView mrefuse_reason_text;
    private TextView mrefuse_reason_remind_close;
    private ScrollView scrollView_enterprise;
    public String province_id = "";
    public String city_id = "";
    public String district_id = "";

    private Field_ChoosePictureGridViewAdapter adapter;
    private ArrayList<String> choose_filepicture = new ArrayList<>();//之前已选择的图片list不用重新截图
    private ArrayList<String> choose_filepicture_editor = new ArrayList<>();//上传修改的图片的file
    private ArrayList<String> choose_filepicture_url = new ArrayList<>();//上传修改的图片的file以外的网络url
    private ArrayList<String> choose_filepicture_tmp = new ArrayList<>();//之前已选择图片的过渡list(用来判断)
    private String photosavafilestr;//存到choose_filepicture中的String变量
    private File photosavafile;//截取图后保存到手机的file
    private boolean booleancamera = false;//是否是选择拍照
    private ArrayList<String> interceptionpathlist = new ArrayList<>();//需要截取的图片urllist
    private int tailorsiae_tmp;//截取图片的position
    private ArrayList<String> galleryviewlist = new ArrayList<>();//预览图片的list
    private int newPosition;//预览删除图片的position
    private int mImageList_size;//预览图片listsize
    private ArrayList<String> addfieldimg_str = new ArrayList<>();//七牛上传的list
    private UploadManager uploadManager = new UploadManager();
    private String uploadtoken;//七牛上传的凭证
    private int addfieldimgsize;//要上传图片的position
    private boolean upload = true;//上传到七牛的图片成功的标志
    private String voucher_image_url = "";
    private Enterprise_infoModel enterprise_infoModel;
    private Dialog choose_area_dialog;
    private Dialog zoom_picture_dialog;
    private List<ImageView> imageList;
    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprisecertification);
        edit_enterprise_companyname = (EditText)findViewById(R.id.enterprise_companyname);
        edit_enterprise_certification_representative = (EditText)findViewById(R.id.enterprise_certification_representative);
        edit_enterprise_certification_idcard = (EditText)findViewById(R.id.enterprise_certification_idcard);
        edit_enterprise_certification_company_code = (EditText)findViewById(R.id.enterprise_certification_company_code);
        text_enterprise_certification_uploadlicense = (TextView)findViewById(R.id.enterprise_certification_uploadlicense);
        layout_enterprise_certification_choose_city = (RelativeLayout)findViewById(R.id.enterprise_certification_choose_city);
        edit_enterprise_certification_choose_address = (EditText)findViewById(R.id.enterprise_certification_choose_address);
        mregion_textview = (TextView)findViewById(R.id.region_textview);
        edit_enterprise_certification_applicant = (EditText)findViewById(R.id.enterprise_certification_applicant);
        edit_enterprise_certification_phonenumber= (EditText)findViewById(R.id.enterprise_certification_phonenumber);
        edit_enterprise_certification_captcha = (EditText)findViewById(R.id.enterprise_certification_captcha);
        btn_enterprise_certification_captchabtn = (Button)findViewById(R.id.enterprise_certification_captchabtn);
        btn_certification_btn = (Button)findViewById(R.id.certification_btn);
        mygridview = (Field_MyGridView)findViewById(R.id.enterprisecetification_gridview);
        mcertification_check_layout = (RelativeLayout)findViewById(R.id.certification_check_layout);
        mrefuse_reason_layout = (RelativeLayout)findViewById(R.id.refuse_reason_layout);
        mrefuse_reason_text = (TextView)findViewById(R.id.refuse_reason_text);
        mrefuse_reason_remind_close = (TextView)findViewById(R.id.refuse_reason_remind_close);
        scrollView_enterprise = (ScrollView)findViewById(R.id.enterprise_scrollview);
        TitleBarUtils.showBackImg(this,true);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.enterprise_certification_title_text));
        initview();
    }
    private void initview() {
        btn_enterprise_certification_captchabtn.setClickable(false);
        btn_enterprise_certification_captchabtn.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
        edit_enterprise_certification_phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn_enterprise_certification_captchabtn.setClickable(true);
                    btn_enterprise_certification_captchabtn.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    btn_enterprise_certification_captchabtn.setClickable(false);
                    btn_enterprise_certification_captchabtn.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
                }
            }
        });
        Intent myself = getIntent();
        if (myself != null && myself.getExtras() != null && myself.getExtras().get("enterprise_authorize_status") != null) {
            if (myself.getExtras().getInt("enterprise_authorize_status") == 0) {
                mcertification_check_layout.setVisibility(View.VISIBLE);
                btn_certification_btn.setVisibility(View.GONE);
                scrollView_enterprise.setVisibility(View.GONE);
            } else {
                btn_certification_btn.setVisibility(View.VISIBLE);
                mcertification_check_layout.setVisibility(View.GONE);
                if (myself.getExtras().getInt("enterprise_authorize_status") == 2) {
                    UserApi.getenterprise_info(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),enterprise_infoHandler);
                }
            }
        } else {
            btn_certification_btn.setVisibility(View.VISIBLE);
            mcertification_check_layout.setVisibility(View.GONE);
        }
        Constants.delete_picture_file();
        if (choose_filepicture.size() < 1) {
            choose_filepicture.add("firstgridviewitem");
        }
        adapter = new Field_ChoosePictureGridViewAdapter(EnterpriseCertificationActivity.this, EnterpriseCertificationActivity.this, choose_filepicture, 5);
        mygridview.setAdapter(adapter);
        mTime = new EnterpriseCertificationActivity.TimeCount(60000, 1000);
        Onclick();
    }
    private LinhuiAsyncHttpResponseHandler enterprise_infoHandler = new LinhuiAsyncHttpResponseHandler(Enterprise_infoModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            enterprise_infoModel = (Enterprise_infoModel)data;
            edit_enterprise_certification_company_code.setText(enterprise_infoModel.getRegistration_no());
            edit_enterprise_companyname.setText(enterprise_infoModel.getName());
            edit_enterprise_certification_representative.setText(enterprise_infoModel.getCorporation());
            edit_enterprise_certification_idcard.setText(enterprise_infoModel.getCitizen_id());
            province_id = enterprise_infoModel.getProvince_id();
            city_id = enterprise_infoModel.getCity_id();
            district_id = enterprise_infoModel.getDistrict_id();
            edit_enterprise_certification_choose_address.setText(enterprise_infoModel.getAddress());
            mregion_textview.setText(enterprise_infoModel.getDetail_district());
            edit_enterprise_certification_applicant.setText(enterprise_infoModel.getContact());
            edit_enterprise_certification_phonenumber.setText(enterprise_infoModel.getMobile());
            for (int i = 0; i < choose_filepicture.size(); i++) {
                if (choose_filepicture.get(i).equals("firstgridviewitem")) {
                    choose_filepicture.remove(i);
                }
            }
            choose_filepicture.add(enterprise_infoModel.getCert_url());
            voucher_image_url = enterprise_infoModel.getCert_url();
            if (choose_filepicture.size() < 1) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter = new Field_ChoosePictureGridViewAdapter(EnterpriseCertificationActivity.this, EnterpriseCertificationActivity.this, choose_filepicture, 5);
            mygridview.setAdapter(adapter);
            mrefuse_reason_layout.setVisibility(View.VISIBLE);
            mrefuse_reason_text.setText(getResources().getString(R.string.enterprise_refuse_reason_text)+
            enterprise_infoModel.getCause_of_failure()+getResources().getString(R.string.enterprise_correct_content_text));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler getLoginCodeeHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                mTime.start();
            } else {
                MessageUtils.showToast(getContext(),getResources().getString(R.string.txt_register_send_codeb_error_remind));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler auth_enterprise = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            mcertification_check_layout.setVisibility(View.VISIBLE);
            btn_certification_btn.setVisibility(View.GONE);
            LoginManager.setEnterprise_authorize_status(0);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btn_enterprise_certification_captchabtn.setText(R.string.txt_register_codebtn_txt);
            btn_enterprise_certification_captchabtn.setClickable(true);
            btn_enterprise_certification_captchabtn.setTextColor(getResources().getColor(R.color.default_bluebg));
        }
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            String str = getResources().getString(R.string.btn_securitycode_reget);
            btn_enterprise_certification_captchabtn.setClickable(false);
            btn_enterprise_certification_captchabtn.setText(str+"("+(millisUntilFinished / 1000)+")");
            btn_enterprise_certification_captchabtn.setTextColor(getResources().getColor(R.color.field_chair_textcolor));
        }
    }
    private void Onclick () {
        layout_enterprise_certification_choose_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(EnterpriseCertificationActivity.this);
                final View textEntryView = factory.inflate(R.layout.activity_districtselect, null);
                choose_area_dialog = new AlertDialog.Builder(EnterpriseCertificationActivity.this).create();
                DistrictSelectActivity districtSelectActivity = new DistrictSelectActivity(EnterpriseCertificationActivity.this,textEntryView,choose_area_dialog,2);
            }
        });
        btn_enterprise_certification_captchabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.getTrimmedLength(edit_enterprise_certification_phonenumber.getText().toString()) == 0) {
                    MessageUtils.showToast(EnterpriseCertificationActivity.this, getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (!CommonTool.isMobileNO(edit_enterprise_certification_phonenumber.getText().toString())) {
                    MessageUtils.showToast(EnterpriseCertificationActivity.this, getResources().getString(R.string.txt_correctphonenumber_hint));
                    return;
                }
                showProgressDialog(EnterpriseCertificationActivity.this.getResources().getString(R.string.txt_waiting));
                UserApi.apiauthcaptcha(MyAsyncHttpClient.MyAsyncHttpClient(), getLoginCodeeHandler,
                        edit_enterprise_certification_phonenumber.getText().toString(),"3");
            }
        });
        btn_certification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_enterprise_companyname.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.applyforrelease_companyneme_hinttxt));
                    return;
                }
                if (province_id.length() == 0 || city_id.length() == 0 || district_id.length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.mTxt_rear_prompt));
                    return;
                }
                if (edit_enterprise_certification_choose_address.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.mTxt_address_prompt));
                    return;
                }
                if (edit_enterprise_certification_applicant.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.enterprise_certification_applicant_hinttext));
                    return;
                }
                if (edit_enterprise_certification_phonenumber.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_phonenumber_hint));
                    return;
                }
                if (edit_enterprise_certification_captcha.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_code));
                    return;
                }
                if (edit_enterprise_certification_representative.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.enterprise_certification_representative_hinttext));
                    return;
                }
                if (edit_enterprise_certification_idcard.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.enterprise_certification_idcard_hinttext));
                    return;
                }
                if (!CommonTool.isIdcard(edit_enterprise_certification_idcard.getText().toString().trim())) {
                    MessageUtils.showToast(getResources().getString(R.string.enterprise_certification_correct_idcard_hinttext));
                    return;
                }

                if (edit_enterprise_certification_company_code.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.enterprise_certification_company_code_hinttext));
                    return;
                }
                if (edit_enterprise_companyname.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.applyforrelease_companyneme_hinttxt));
                    return;
                }
                if (choose_filepicture.size() > 0 && !choose_filepicture.contains("firstgridviewitem")) {
                    uploadimg();
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.mPic_map_prompt));
                }
            }
        });
        mrefuse_reason_remind_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mrefuse_reason_layout.setVisibility(View.GONE);
            }
        });
    }

    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }

    @Override
    public void AdField_showPreviewImg(int position) {
        if (position == -1) {
            AndPermission.with(EnterpriseCertificationActivity.this)
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
        }

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

        ImageSelector.open(EnterpriseCertificationActivity.this, imageConfig);   // 开启图片选择器
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
                btn_certification_btn.setVisibility(View.VISIBLE);
            } else {
                this.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    DialogInterface.OnKeyListener onKeylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                btn_certification_btn.setVisibility(View.VISIBLE);
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
            //进行认证
            UserApi.auth_enterprise(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),auth_enterprise,edit_enterprise_certification_company_code.getText().toString(),
                    edit_enterprise_companyname.getText().toString(),edit_enterprise_certification_representative.getText().toString(),
                    edit_enterprise_certification_idcard.getText().toString(),addfieldimg_str.get(0),
                    province_id,city_id,district_id,edit_enterprise_certification_choose_address.getText().toString(),
                    edit_enterprise_certification_applicant.getText().toString(),edit_enterprise_certification_phonenumber.getText().toString(),
                    edit_enterprise_certification_captcha.getText().toString());

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
                MessageUtils.showToast(EnterpriseCertificationActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
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
                        //进行认证
                        UserApi.auth_enterprise(MyAsyncHttpClient.MyAsyncHttpClient_version_two(),auth_enterprise,edit_enterprise_certification_company_code.getText().toString(),
                                edit_enterprise_companyname.getText().toString(),edit_enterprise_certification_representative.getText().toString(),
                                edit_enterprise_certification_idcard.getText().toString(),addfieldimg_str.get(0),
                                province_id,city_id,district_id,edit_enterprise_certification_choose_address.getText().toString(),
                                edit_enterprise_certification_applicant.getText().toString(),edit_enterprise_certification_phonenumber.getText().toString(),
                                edit_enterprise_certification_captcha.getText().toString());
                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(EnterpriseCertificationActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                MessageUtils.showToast(EnterpriseCertificationActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

        }
    };
    private void show_zoom_picture_dialog(final int position, ArrayList<String> mPicList) {
        View myView = EnterpriseCertificationActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        zoom_picture_dialog = new AlertDialog.Builder(EnterpriseCertificationActivity.this).create();
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

            }
        });
        newPosition = position;
        com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(EnterpriseCertificationActivity.this,EnterpriseCertificationActivity.this,newPosition,0);
        constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,newPosition);
    }
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
                    CustomDialog.Builder builder = new CustomDialog.Builder(EnterpriseCertificationActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(EnterpriseCertificationActivity.this,mCustomDialog);
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
