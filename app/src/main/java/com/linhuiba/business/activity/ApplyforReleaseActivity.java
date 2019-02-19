package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
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
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_UploadingPictureActivity;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
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

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ApplyforReleaseActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall {
    @InjectView(R.id.applyforrelease_savebtn)Button mapplyforrelease_savebtn;
    @InjectView(R.id.applyforrelease_bankname_edit)
    EditText mapplyforrelease_bankname_edit;
    @InjectView(R.id.applyforrelease_bankname_layout)
    RelativeLayout mapplyforrelease_bankname_layout;
    @InjectView(R.id.applyforrelease_name_layout)
    RelativeLayout mapplyforrelease_name_layout;
    @InjectView(R.id.applyforrelease_payment_account_edit)
    EditText mapplyforrelease_payment_account_edit;
    @InjectView(R.id.applyforrelease_companyneme_edit)
    EditText mApplyforreleaseMobileET;

    @InjectView(R.id.applyforrelease_name_edit)
    EditText mapplyforrelease_name_edit;
    @InjectView(R.id.applyforrelease_payment_account_layout)
    RelativeLayout mapplyforrelease_payment_account_layout;
    @InjectView(R.id.applyforrelease_contect_edit)
    EditText mapplyforrelease_contect_edit;
    @InjectView(R.id.comoany_contact_relativelayout)
    RelativeLayout mcomoany_contact_relativelayout;
    @InjectView(R.id.comoanyname_relativelayout)
    RelativeLayout mApplyforreleaseMobileRL;
    @InjectView(R.id.applyforrelease_license_addpicture_gridview)
    Field_MyGridView mapplyforrelease_license_addpicture_gridview;
    @InjectView(R.id.calendarclick_remind_layout) RelativeLayout mcalendarclick_remind_layout;

    @InjectView(R.id.applyforrelease_upload_business_license_layout)
    LinearLayout mapplyforrelease_upload_business_license_layout;

    private Field_ChoosePictureGridViewAdapter license_adapter;//营业执照adapter
    private File photosavafile;
    private String photosavafilestr;
    private ArrayList<String> show_addfielding_str = new ArrayList<>();
    private ArrayList<String> show_addfielding_str_tmp = new ArrayList<>();
    private UploadManager uploadManager = new UploadManager();
    private String uploadtoken;
    private String imgurl;
    private int license_newPosition;//营业执照预览删除图片的position
    private int license_mImageList_size;//营业执照预览图片listsize
    private Dialog mZoomPictureDialog;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private boolean mIsRefreshZoomImageview = true;
    private TextView mShowPictureSizeTV;
    private ViewPager mZoomViewPage;
    private com.linhuiba.linhuifield.connector.Constants mConstants;
    private ImageButton mImgBtn;
    private View mView;
    private TextView mShowPictureBackTV;
    private int mImageList_size;
    private int newPosition;
    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyforrelease);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_applyforrelease_txt));
        TitleBarUtils.showBackImg(this, true);
        initview();
    }
    private void initview() {
        initPreviewZoomView();
        Intent accountinfointent = getIntent();
        if (accountinfointent != null && accountinfointent.getExtras() != null && accountinfointent.getExtras().get("role_id") != null
                && accountinfointent.getExtras().getInt("role_id") == 2) {
            showProgressDialog();
            UserApi.getbeneficiary_info(MyAsyncHttpClient.MyAsyncHttpClient2(), Property_applicationsHandler, LoginManager.getUid());
            mcomoany_contact_relativelayout.setVisibility(View.GONE);
            mApplyforreleaseMobileRL.setVisibility(View.GONE);
            mapplyforrelease_savebtn.setVisibility(View.GONE);
            mapplyforrelease_upload_business_license_layout.setVisibility(View.GONE);
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_company_acountinfo));
            TitleBarUtils.shownextTextView(this, getResources().getString(R.string.mTxt_save_fieldinfo), 17, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserApi.savebeneficiary_info(MyAsyncHttpClient.MyAsyncHttpClient2(),savebeneficiary_infoHandler,mapplyforrelease_bankname_edit.getText().toString(),
                            mapplyforrelease_name_edit.getText().toString(),mapplyforrelease_payment_account_edit.getText().toString(), LoginManager.getUid());
                }
            });

        } else {
            showProgressDialog();
            UserApi.getproperty_applications(MyAsyncHttpClient.MyAsyncHttpClient2(), Property_applicationsHandler);
            mApplyforreleaseMobileET.addTextChangedListener(textWatcher);
            mapplyforrelease_payment_account_layout.setVisibility(View.GONE);
            mapplyforrelease_bankname_layout.setVisibility(View.GONE);
            mapplyforrelease_name_layout.setVisibility(View.GONE);
            mapplyforrelease_contect_edit.addTextChangedListener(textWatcher);
            mapplyforrelease_savebtn.setEnabled(false);
            mapplyforrelease_savebtn.setBackgroundColor(getResources().getColor(R.color.btn_notclickstate_bg));
            Constants.delete_picture_file();
        }

    }
    private void initPreviewZoomView() {
        mZoomPictureDialog = new AlertDialog.Builder(this).create();
        mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mImgBtn = (ImageButton)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_deletebtn);
        mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        mShowPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);

        mImgBtn.setVisibility(View.VISIBLE);
        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ApplyforReleaseActivity.this)
                        .setTitle("确定删除？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                show_addfielding_str.remove(newPosition);
                                show_addfielding_str.add("firstgridviewitem");
                                license_adapter.notifyDataSetChanged();
                                mZoomPictureDialog.dismiss();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();

                            }
                        }).show();

            }
        });
        mZoomViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                newPosition = position % mImageViewList.size();
            }

            @Override
            public void onPageSelected(int position) {
                mShowPictureSizeTV.setText(String.valueOf(position % mImageViewList.size()+1)+"/" + String.valueOf(mImageViewList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({
            R.id.calendarclick_remind_close
    })
    public void remindclick(View v) {
        switch (v.getId()) {
            case R.id.calendarclick_remind_close:
                mcalendarclick_remind_layout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mApplyforreleaseMobileET.getText().toString())
                    && !TextUtils.isEmpty(mapplyforrelease_contect_edit.getText().toString())
                    ) {
                mapplyforrelease_savebtn.setEnabled(true);
                mapplyforrelease_savebtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
            } else {
                mapplyforrelease_savebtn.setBackgroundColor(getResources().getColor(R.color.btn_notclickstate_bg));
                mapplyforrelease_savebtn.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };
    @OnClick({
            R.id.applyforrelease_savebtn,
    })
    public void applyforreleaseclick(View view) {
        switch (view.getId()) {
            case R.id.applyforrelease_savebtn:
                uploadimg();
                break;
            default:
                break;
        }
    }
    private void choosepicture() {
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
                .pathList(show_addfielding_str)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(Constants.PhotoRequestCode)
                .build();

        ImageSelector.open(ApplyforReleaseActivity.this, imageConfig);   // 开启图片选择器
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PhotoRequestCode && resultCode == RESULT_OK && data != null) {
            if (show_addfielding_str != null) {
                show_addfielding_str.clear();
            }
            ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            show_addfielding_str.addAll(pathList);
            if (show_addfielding_str.size() < 1) {
                show_addfielding_str.add("firstgridviewitem");
            }
            license_adapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(mApplyforreleaseMobileET.getText().toString())
                    && !TextUtils.isEmpty(mapplyforrelease_contect_edit.getText().toString())
                    ) {
                mapplyforrelease_savebtn.setEnabled(true);
                mapplyforrelease_savebtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
            } else {
                mapplyforrelease_savebtn.setBackgroundColor(getResources().getColor(R.color.btn_notclickstate_bg));
                mapplyforrelease_savebtn.setEnabled(false);
            }
        } else if (requestCode == Constants.PhotoRequestCode) {
            if (show_addfielding_str != null) {
                show_addfielding_str.clear();
            }
            show_addfielding_str.addAll(show_addfielding_str_tmp);
            if (show_addfielding_str.size() < 1) {
                show_addfielding_str.add("firstgridviewitem");
            }
            license_adapter.notifyDataSetChanged();
        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                if (show_addfielding_str != null) {
                    show_addfielding_str.clear();
                }
                show_addfielding_str.add(photosavafilestr);
                if (show_addfielding_str.size() < 1) {
                    show_addfielding_str.add("firstgridviewitem");
                }
                license_adapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(mApplyforreleaseMobileET.getText().toString())
                        && !TextUtils.isEmpty(mapplyforrelease_contect_edit.getText().toString())
                        ) {
                    mapplyforrelease_savebtn.setEnabled(true);
                    mapplyforrelease_savebtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.loginbtn_bg));
                } else {
                    mapplyforrelease_savebtn.setBackgroundColor(getResources().getColor(R.color.btn_notclickstate_bg));
                    mapplyforrelease_savebtn.setEnabled(false);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private LinhuiAsyncHttpResponseHandler applyreleasepermissionsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getContext(), getResources().getString(R.string.myselfinfo_applyforrelease_successtxt));
            ApplyforReleaseActivity.this.finish();
            Constants.delete_picture_file();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    private void uploadimg() {
        for (int i = 0; i < show_addfielding_str.size(); i++) {
            if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                show_addfielding_str.remove(i);
            }
        }
        if (show_addfielding_str.size() > 0 &&
                !show_addfielding_str.get(0).equals(imgurl)) {
            showProgressDialog();
            Field_FieldApi.getuptoken_certs(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
        } else {
            showProgressDialog();
//            FieldApi.applyreleasepermissions(MyAsyncHttpClient.MyAsyncHttpClient2(), applyreleasepermissionsHandler,
//                    mapplyforrelease_companyneme_edit.getText().toString(), mapplyforrelease_contect_edit.getText().toString(),
//                    mapplyforrelease_email_edit.getText().toString(), imgurl, mapplyforrelease_bankname_edit.getText().toString(),
//                    mapplyforrelease_name_edit.getText().toString(), mapplyforrelease_payment_account_edit.getText().toString());

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
                uploadManager.put(new File(show_addfielding_str.get(0)), null, uploadtoken,
                        upCompletionHandler, null);
            } else {
                MessageUtils.showToast(getString(R.string.field_tupesize_errortoast));
            }


        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            MessageUtils.showToast(getString(R.string.field_tupesize_errortoast));
            checkAccess_new(error);
        }
    };
    private UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, org.json.JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                try {
                    imgurl = Config.qiniu_domain_creats + jsonObject.getString("key").toString();
                    showProgressDialog();
//                    FieldApi.applyreleasepermissions(MyAsyncHttpClient.MyAsyncHttpClient2(),applyreleasepermissionsHandler,
//                            mapplyforrelease_companyneme_edit.getText().toString(),mapplyforrelease_contect_edit.getText().toString(),
//                            mapplyforrelease_email_edit.getText().toString(),imgurl ,mapplyforrelease_bankname_edit.getText().toString(),
//                            mapplyforrelease_name_edit.getText().toString(),mapplyforrelease_payment_account_edit.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //删除七牛刚上传的图片  addfieldimg_str
                MessageUtils.showToast(getResources().getString(R.string.txt_upload_fieldimg));
            }

        }
    };
    private LinhuiAsyncHttpResponseHandler savebeneficiary_infoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getContext(), getResources().getString(R.string.save_success_prompt));
            Intent intent = new Intent();
            setResult(4, intent);
            ApplyforReleaseActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
    private LinhuiAsyncHttpResponseHandler Property_applicationsHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                if (data instanceof JSONObject) {
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    if (jsonObject != null) {
                        if (jsonObject.get("company") != null) {
                            mApplyforreleaseMobileET.setText(jsonObject.get("company").toString());
                            mcalendarclick_remind_layout.setVisibility(View.VISIBLE);
                        }
                        if (jsonObject.get("contact") != null) {
                            mapplyforrelease_contect_edit.setText(jsonObject.get("contact").toString());
                            mcalendarclick_remind_layout.setVisibility(View.VISIBLE);
                        }

                        if (jsonObject.get("cert_url") != null && jsonObject.get("cert_url").toString().length() > 0) {
                            imgurl = (jsonObject.get("cert_url").toString());
                            show_addfielding_str.add(imgurl);
                            license_adapter = new Field_ChoosePictureGridViewAdapter(ApplyforReleaseActivity.this, ApplyforReleaseActivity.this, show_addfielding_str, 6);
                            mapplyforrelease_license_addpicture_gridview.setAdapter(license_adapter);
                        } else {
                            show_addfielding_str.add("firstgridviewitem");
                            license_adapter = new Field_ChoosePictureGridViewAdapter(ApplyforReleaseActivity.this, ApplyforReleaseActivity.this, show_addfielding_str, 6);
                            mapplyforrelease_license_addpicture_gridview.setAdapter(license_adapter);
                        }
                        if (jsonObject.get("opening_bank") != null) {
                            mapplyforrelease_bankname_edit.setText(jsonObject.get("opening_bank").toString());
                        }
                        if (jsonObject.get("account_holder") != null) {
                            mapplyforrelease_name_edit.setText(jsonObject.get("account_holder").toString());
                        }
                        if (jsonObject.get("beneficiary_account_number") != null) {
                            mapplyforrelease_payment_account_edit.setText(jsonObject.get("beneficiary_account_number").toString());
                        }
                    }
                }
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            show_addfielding_str.add("firstgridviewitem");
            license_adapter = new Field_ChoosePictureGridViewAdapter(ApplyforReleaseActivity.this, ApplyforReleaseActivity.this, show_addfielding_str, 6);
            mapplyforrelease_license_addpicture_gridview.setAdapter(license_adapter);
            checkAccess_new(error);
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
                                        photosavafilestr = Constants.picture_file_str + "applyforrelease" + time + "_" + ".jpg";
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
                                    for (int i = 0; i < show_addfielding_str.size(); i++) {
                                        if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                                            show_addfielding_str.remove(i);
                                        }
                                    }
                                    if (show_addfielding_str_tmp != null) {
                                        show_addfielding_str_tmp.clear();
                                    }
                                    if (show_addfielding_str != null && show_addfielding_str.size() > 0) {
                                        show_addfielding_str_tmp.addAll(show_addfielding_str);
                                    }
                                    choosepicture();
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(ApplyforReleaseActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(ApplyforReleaseActivity.this,mCustomDialog);
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
    public void doFailSomething(){
        MessageUtils.showToast("应用没有权限，请授权！");
    }
    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }

    @Override
    public void AdField_showPreviewImg(int position) {
        if (position == -1) {
            AndPermission.with(ApplyforReleaseActivity.this)
                    .requestCode(Constants.PermissionRequestCode)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(listener)
                    .start();
        } else {
            mConstants = new com.linhuiba.linhuifield.connector.Constants(ApplyforReleaseActivity.this,ApplyforReleaseActivity.this,newPosition,mImageList_size,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                            com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
            mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                    mZoomPictureDialog,mImageViewList,show_addfielding_str,mIsRefreshZoomImageview,
                    position,false);
            mIsRefreshZoomImageview = false;
        }
    }
}
