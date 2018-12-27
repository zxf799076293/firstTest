package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldactivity.Field_GlideLoader;
import com.linhuiba.business.fieldbusiness.Field_FieldApi;
import com.linhuiba.business.fragment.GroupBookingListFragment;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.mvppresenter.ModifyUserInfoMvpPressenter;
import com.linhuiba.business.mvpview.ModifyUserInfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.XCRoundImageView;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;
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
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/3/10.
 */
public class MyselfInfoActivity extends BaseMvpActivity implements ModifyUserInfoMvpView {
    @InjectView(R.id.txt_account_information)
    TextView mtxt_account_information;
    @InjectView(R.id.seflinfo_username_remiand_tv)
    TextView mUserNameRemindTV;
    @InjectView(R.id.username_set_iv)
    ImageView mUserNameSetIV;
    @InjectView(R.id.username_rl)
    RelativeLayout mUserNameRL;

    @InjectView(R.id.txt_phone_number)
    TextView mtxt_phone_number;
    @InjectView(R.id.txt_companyname)
    TextView mtxt_companyname;
    @InjectView(R.id.prompt_companyname)
    TextView mprompt_companyname;
    @InjectView(R.id.txt_contactname)
    TextView mtxt_contactname;
    @InjectView(R.id.txt_mailbox)
    TextView mtxt_mailbox;
    @InjectView(R.id.txt_invitecode)
    TextView mtxt_invitecode;
    @InjectView(R.id.applyfor_releasepermissions_layout)
    LinearLayout mapplyfor_releasepermissions_layout;
    @InjectView(R.id.myself_receive_account_tv)
    TextView myselfReceiveAccountTV;
    @InjectView(R.id.myselfinfo_integral_textview)
    TextView mmyselfinfo_integral_textview;
    @InjectView(R.id.layout_myselfinfo_integral)
    RelativeLayout mlayout_myselfinfo_integral;
    @InjectView(R.id.txt_myselfname)
    TextView mtxt_myselfname;
    @InjectView(R.id.txt_account_photo_img)
    XCRoundImageView mtxt_account_photo_img;
    @InjectView(R.id.myselfinfo_user_grade_tv)
    TextView mUserGradeTV;
    @InjectView(R.id.myselfinfo_enterprise_info_ll)
    RelativeLayout mCompanyInfoLL;
    private String photosavafilestr;//存到choose_filepicture中的String变量
    private File photosavafile;//截取图后保存到手机的file
    private ArrayList<String> choose_picture_list = new ArrayList<>();//选择或拍照返回的url
    private ArrayList<String> cutout_picture_list = new ArrayList<>();//裁剪返回的url
    private ArrayList<String> upload_picture_list = new ArrayList<>();//上传的url
    private boolean booleancamera;//是否是拍照
    private int tailorsiae_tmp;//截取图片的position
    private UploadManager uploadManager = new UploadManager();
    private String uploadtoken;//七牛上传的凭证
    private CustomDialog mCustomDialog;
    private ModifyUserInfoMvpPressenter mPressenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myselfinfo);
        ButterKnife.inject(this);
        mPressenter = new ModifyUserInfoMvpPressenter();
        mPressenter.attachView(this);
        TitleBarUtils.showBackImg(this, true);
        if (LoginManager.isLogin()) {
            initview();
        } else {
            mtxt_account_photo_img.setBackgroundResource(R.drawable.image_touxiang_three_two);
            mlayout_myselfinfo_integral.setEnabled(false);
            LoginManager.getInstance().clearLoginInfo();
            MyselfInfoActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPressenter != null) {
            mPressenter.detachView();
        }
    }

    @OnClick({
            R.id.selfinfo_grade_ll,
            R.id.layout_phone_number,
            R.id.layout_companyname,
            R.id.layout_contactname,
            R.id.layout_mailbox,
            R.id.applyfor_releasepermissions_layout,
            R.id.layout_myselfinfo_integral,
            R.id.layout_myselfinfo_name,
            R.id.layout_third_party_account,
            R.id.layout_account_photo,
            R.id.myselfinfo_enterprise_info_ll,
            R.id.username_rl,
            R.id.myself_info_invoice_title_rl
    })
    public void myselfinfo_click(View view) {
        switch (view.getId()) {
            case R.id.selfinfo_grade_ll:
                Intent gradeIntent = new Intent(MyselfInfoActivity.this, AboutUsActivity.class);
                gradeIntent.putExtra("type", Config.GRADE_INT);
                startActivity(gradeIntent);
                break;
            //场地意向（公司信息查看编辑）
            case R.id.layout_companyname:
                Intent intentionIntent = new Intent(MyselfInfoActivity.this,AboutUsActivity.class);
                intentionIntent.putExtra("type", Config.SITE_INTENNTION_INT);
                startActivityForResult(intentionIntent,4);
                break;
            //联系人管理功能
            case R.id.layout_contactname:
                Intent contactname = new Intent(MyselfInfoActivity.this,ManageContactActivity.class);
                startActivity(contactname);
                break;
            //查看编辑邮箱
            case R.id.layout_mailbox:
                Intent mailbox = new Intent(MyselfInfoActivity.this,ModifyUserInfoActivity.class);
                mailbox.putExtra("modifytype_text",getResources().getString(R.string.myselfinfo_mailbox));
                startActivityForResult(mailbox, 4);
                break;
            //申请发布权限
            case R.id.applyfor_releasepermissions_layout:
                if(LoginManager.getEnterprise_authorize_status() == 1 && LoginManager.isRight_to_publish()) {
                    Intent applyforreleasepermissions = new Intent(this,AboutUsActivity.class);
                    applyforreleasepermissions.putExtra("type", Config.RECEIVE_ACCOUNT_INT);
                    startActivityForResult(applyforreleasepermissions, 4);
                } else {
                    Intent applyforreleasepermissions = new Intent(this,AboutUsActivity.class);
                    applyforreleasepermissions.putExtra("type", Config.APPLY_FOR_RELEASE_INT);
                    startActivityForResult(applyforreleasepermissions, 4);
                }
                break;
            //查看积分
            case R.id.layout_myselfinfo_integral:
                Intent about_intrgral = new Intent(this,AboutUsActivity.class);
                about_intrgral.putExtra("type", Config.POINT_INFO_WEB_INT);
                startActivity(about_intrgral);
                break;
            //查看编辑姓名
            case R.id.layout_myselfinfo_name:
                Intent name_intent = new Intent(MyselfInfoActivity.this,ModifyUserInfoActivity.class);
                name_intent.putExtra("modifytype_text",getResources().getString(R.string.myselfinfo_username));
                startActivityForResult(name_intent, 4);
                break;
            //我的地三方账号
            case R.id.layout_third_party_account:
                Intent bindingweixin_intent = new Intent(this,BinDingWeixinActivity.class);
                startActivity(bindingweixin_intent);
                break;
            //头像编辑
            case R.id.layout_account_photo:
                AndPermission.with(MyselfInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
                break;
            case R.id.layout_phone_number:
                // 2018/3/22 修改手机号
                if (!mtxt_phone_number.getText().toString().equals(getResources().getString(R.string.myselfinfo_unperfect_text))) {
                    Intent intent = new Intent(MyselfInfoActivity.this,AboutUsActivity.class);
                    intent.putExtra("type", Config.MODIFY_MOBILE_INT);
                    startActivityForResult(intent,4);
                }
                break;
            case R.id.myselfinfo_enterprise_info_ll:
                //2018/3/15 企业信息
                Intent companyIntent = new Intent(this,AboutUsActivity.class);
                companyIntent.putExtra("type", Config.COMPANY_INFO_INT);
                startActivityForResult(companyIntent, 4);
                break;
            case R.id.username_rl:
                if (LoginManager.getUser_name() != null && LoginManager.getUser_name().length() > 0) {
                    MessageUtils.showToast(getResources().getString(R.string.myselfinfo_dot_set_username_hint_str));
                } else {
                    Intent setUsetNameIntent = new Intent(MyselfInfoActivity.this,ModifyUserInfoActivity.class);
                    setUsetNameIntent.putExtra("modifytype_text",getResources().getString(R.string.myselfinfo_username_text));
                    startActivityForResult(setUsetNameIntent, 4);
                }
                break;
            case R.id.myself_info_invoice_title_rl:
                Intent titleListIntent = new Intent(MyselfInfoActivity.this,InvoiceTitleListActivity.class);
                startActivity(titleListIntent);
                break;
            default:
                break;
        }
    }
    private void initview() {
        if (LoginManager.getAvatar() != null && LoginManager.getAvatar().length() > 0) {
            Picasso.with(MyselfInfoActivity.this).load(LoginManager.getAvatar()+"?imageView2/0/w/" + "70" + "/h/" + "70").resize(70, 70).into(mtxt_account_photo_img);
        } else {
            mtxt_account_photo_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_touxiang_three_two));
        }
        if (LoginManager.getMembership_level() != null) {
            mUserGradeTV.setText(LoginManager.getMembership_level());
        }
        if (LoginManager.getEnterprise_authorize_status() == 1) {
            mCompanyInfoLL.setVisibility(View.VISIBLE);
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_company_title_str));
        } else {
            mCompanyInfoLL.setVisibility(View.GONE);
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_titletxt));
        }
        mtxt_invitecode.setText(LoginManager.getinvitecode());
        mmyselfinfo_integral_textview.setText(LoginManager.getConsumption_point());
        mlayout_myselfinfo_integral.setEnabled(true);
        if (LoginManager.getEnterprise_authorize_status() == 1) {
            mapplyfor_releasepermissions_layout.setVisibility(View.VISIBLE);
            if (LoginManager.isRight_to_publish()) {
                myselfReceiveAccountTV.setText(getResources().getString(R.string.applyforrelease_payment_account_txt));
            }
        } else {
            mapplyfor_releasepermissions_layout.setVisibility(View.GONE);
        }
        if (LoginManager.isHas_contacts()) {
            mtxt_contactname.setVisibility(View.GONE);
        } else {
            mtxt_contactname.setVisibility(View.VISIBLE);
        }
        if (LoginManager.getUser_name() != null && LoginManager.getUser_name().length() > 0) {
            mtxt_account_information.setText(LoginManager.getUser_name());
            mUserNameRemindTV.setVisibility(View.INVISIBLE);
            mUserNameSetIV.setVisibility(View.INVISIBLE);
        } else {
            mUserNameRemindTV.setVisibility(View.VISIBLE);
            mUserNameSetIV.setVisibility(View.VISIBLE);
        }
        //判断手机号是否完善
        if (LoginManager.getMobile() != null && LoginManager.getMobile().length() > 0) {
            mtxt_phone_number.setText(LoginManager.getMobile());
        } else {
            mtxt_phone_number.setText(getResources().getString(R.string.myselfinfo_unperfect_text));
        }
        //判断公司信息是否完善
        if (LoginManager.getInstance().getIndustry_name() != null &&
                LoginManager.getInstance().getIndustry_name().length() > 0) {
            mprompt_companyname.setVisibility(View.GONE);
        } else {
            mprompt_companyname.setVisibility(View.VISIBLE);
        }
        //判断邮箱是否完善
        if (LoginManager.geteEmail() != null && LoginManager.geteEmail().length() > 0) {
            mtxt_mailbox.setText(LoginManager.geteEmail());
        } else {
            mtxt_mailbox.setText(getResources().getString(R.string.myselfinfo_unperfect_text));
        }
        //判断姓名是否完善
        if (LoginManager.getName() != null && LoginManager.getName().length() > 0) {
            mtxt_myselfname.setText(LoginManager.getName());
        } else {
            mtxt_myselfname.setText(getResources().getString(R.string.myselfinfo_unperfect_text));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1002 && resultCode == -1 && data != null) {
            choose_picture_list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            String time = "" + System.currentTimeMillis();
            tailorsiae_tmp = 0;
            if (Constants.SDCardState()) {
                photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                photosavafile = new File(photosavafilestr);
                startPhotoZoom(Uri.fromFile(new File(choose_picture_list.get(tailorsiae_tmp))),Uri.fromFile(photosavafile));
            }
        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                booleancamera = true;
                startPhotoZoom(Uri.fromFile(photosavafile), Uri.fromFile(photosavafile));
                break;
            // 取得裁剪后的图片
            case Constants.CutOutPicturesRequestCode:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException
                 *
                 */
                if (data != null) {
                    if (cutout_picture_list != null) {
                        cutout_picture_list.clear();
                    }
                    cutout_picture_list.add(photosavafilestr);
                    if (booleancamera == false) {
                        if (tailorsiae_tmp < choose_picture_list.size() - 1) {
                            tailorsiae_tmp = tailorsiae_tmp + 1;
                            String time = "" + System.currentTimeMillis();
                            photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                            photosavafile = new File(photosavafilestr);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startPhotoZoom(Uri.fromFile(new File(choose_picture_list.get(tailorsiae_tmp))), Uri.fromFile(photosavafile));
                                }
                            }, 700);
                        } else if (tailorsiae_tmp == choose_picture_list.size() - 1) {
                            //
                            showProgressDialog();
                            Field_FieldApi.getuptoken_comment(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
                        }
                    } else {
                        booleancamera = false;
                        //
                        showProgressDialog();
                        Field_FieldApi.getuptoken_comment(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);

                    }
                } else {
                    booleancamera = false;
                }
                break;
            case 4:
                if (LoginManager.isLogin()) {
                    initview();
                } else {
                    mtxt_account_photo_img.setBackgroundResource(R.drawable.image_touxiang_three_two);
                    mlayout_myselfinfo_integral.setEnabled(false);
                    LoginManager.getInstance().clearLoginInfo();
                    MyselfInfoActivity.this.finish();
                }
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
                .pathList(choose_picture_list)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(1002)
                .build();
        ImageSelector.open(MyselfInfoActivity.this, imageConfig);   // 开启图片选择器
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri,Uri file_uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 4);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }
    private LinhuiAsyncHttpResponseHandler UptokenHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, final Object data) {
            if (data != null) {
                uploadtoken = data.toString();
                if (upload_picture_list != null) {
                    upload_picture_list.clear();
                }
                uploadManager.put(cutout_picture_list.get(0), null, uploadtoken,
                        upCompletionHandler, null);
            } else {
                MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
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
                    upload_picture_list.add(Config.qiniu_domain_comment + jsonObject.getString("key").toString());
                    //上传照片到服务器 上传成功后 显示头像
                    mPressenter.modifyUserInfo(3,upload_picture_list.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MessageUtils.showToast(getResources().getString(R.string.txt_upload_fieldimg));
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
                                    if (choose_picture_list != null) {
                                        choose_picture_list.clear();
                                    }
                                    String time = "" + System.currentTimeMillis();
                                    if (com.linhuiba.business.connector.Constants.SDCardState()) {
                                        photosavafilestr = com.linhuiba.business.connector.Constants.picture_file_str + "addfield" + time + "_" + ".jpg";
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
                                            startActivityForResult(intent, Constants.CameraRequestCode);
                                        }
                                    }                               break;
                                case R.id.upload_picture_ll:
                                    mCustomDialog.dismiss();
                                    if (choose_picture_list != null) {
                                        choose_picture_list.clear();
                                    }
                                    choosepicture();
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(MyselfInfoActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MyselfInfoActivity.this,mCustomDialog);
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

    @Override
    public void onModifyUserInfoSuccess(String msg) {
        if (upload_picture_list.get(0) != null && upload_picture_list.get(0).length() > 0) {
            Picasso.with(MyselfInfoActivity.this).load(upload_picture_list.get(0)+"?imageView2/0/w/" + "70" + "/h/" + "70").resize(70, 70).into(mtxt_account_photo_img);
            LoginManager.setAvatar(upload_picture_list.get(0));
        }
    }

    @Override
    public void onModifyUserInfoFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onSetUserNameSuccess(String msg) {

    }
}
