package com.linhuiba.linhuifield.fieldactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleColor;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleParams;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodlePath;
import com.linhuiba.linhuifield.cn.hzw.doodle.core.IDoodleItem;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
import com.linhuiba.linhuifield.connector.OnMultiClickListener;
import com.linhuiba.linhuifield.fieldadapter.Field_ChoosePictureGridViewAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldbusiness.Field_FieldApi;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Response;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.forward.androids.utils.ImageUtils;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleBitmap;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleOnTouchGestureListener;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodlePen;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleShape;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleText;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleTouchDetector;
import com.linhuiba.linhuifield.cn.hzw.doodle.DoodleView;
import com.linhuiba.linhuifield.cn.hzw.doodle.IDoodleListener;
import com.linhuiba.linhuifield.cn.hzw.doodle.core.IDoodle;
import com.linhuiba.linhuifield.cn.hzw.doodle.core.IDoodlePen;
import com.linhuiba.linhuifield.cn.hzw.doodle.core.IDoodleSelectableItem;
import com.linhuiba.linhuifield.cn.hzw.doodle.core.IDoodleTouchDetector;
import com.linhuiba.linhuifield.cn.hzw.doodle.dialog.DialogController;

import cn.forward.androids.utils.Util;
import okhttp3.Call;
import okhttp3.Callback;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/2/1.
 */
public class Field_AddField_UploadingPictureActivity extends FieldBaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdFieldChoosePictureCall,Field_AddFieldChoosePictureCallBack.AdField_deleteChoosePictureCall,Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall {

    private FieldMyGridView recycler;
    private Field_ChoosePictureGridViewAdapter adapter;
    private ArrayList<String> path = new ArrayList<>();//需要裁剪的从相册选择的图片地址列表
    private ArrayList<String> addfieldimg_str = new ArrayList<>();//最终上传到服务器的url列表
    private ArrayList<String> show_addfielding_str = new ArrayList<>();//相册选择的图片的list
    private ArrayList<String> show_addfielding_str_tmp = new ArrayList<>();//相册上一次选择的图片的list
    private ArrayList<String> choose_filepicture_editor = new ArrayList<>();//上传修改的图片的file
    private ArrayList<String> choose_filepicture_url = new ArrayList<>();//上传修改的图片的file以外的网络url（之前就有的url）
    private int addfieldimgsize;
    private boolean upload = true;
    private int mImageList_size;
    private int newPosition;
    private ArrayList<String> pathtmp = new ArrayList<>();//预览大图时的imglist

    private File photosavafile;//裁剪后保存图片的file
    private String photosavafilestr;//裁剪之前定义的所要保存的file的url
    private int tailorsiae_tmp;//要裁剪的图片个数
    private boolean booleancamera = false;//是否选择的是拍照
    private UploadManager uploadManager = new UploadManager();//七牛上传的回调
    private String uploadtoken;//七牛上传的token
    private ArrayList<String> Pic_urllist = new ArrayList<>();//编辑之前服务器保存的图片的url
    private Dialog mZoomPictureDialog;
    private List<ImageView> mImageViewList = new ArrayList<>();//显示图片的imageview list
    private boolean mIsRefreshZoomImageview = true;
    private TextView mShowPictureSizeTV;
    private ViewPager mZoomViewPage;
    private Constants mConstants;
    private View mView;
    private CustomDialog mCustomDialog;
    private LinearLayout stickerLL;
    private SeekBar mDoodleSeekBar;
    private ImageButton mDoodleRevocationTextImgv;
    private ImageButton mDoodleRevocationBitmapImgv;
    private ImageView mDoodleDotAddImgv;//添加点
    private LinearLayout mDoodleDotAddLL;
    private RadioGroup mDoodleOperatingTypeRG;
    //2019/1/4 涂鸦
    private FrameLayout mDoodleContainerFL;
    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    private int stickerImgInt;
    private int stickerWidth;
    private int stickerHeight;
    private int pictireType;//1场地；2展位
    private LinearLayout mDoodleLineTypeLL;
    private LinearLayout mDoodleTextTypeLL;
    private LinearLayout mDoodleBitmapTypeLL;
    private LinearLayout mDoodleTextOrDotTypeLL;
    private EditText mDoodleTextEditText;
    private TextView mDoodleDeleteImgv;
    // FIXME: 2019/1/4 涂鸦
    private IDoodle mDoodle;
    private DoodleView mDoodleView;
    private DoodleOnTouchGestureListener mTouchGestureListener;
    private List<HashMap<String,Float>> mDotCoordinateList = new ArrayList<>();
    private int mDoodleCheckedId;
    private int mDotPosition = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_three_resourceinfo);
        initView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PhotoRequestCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            ArrayList<String> pathList_tmp = new ArrayList<>();
            pathList_tmp.addAll(show_addfielding_str_tmp);
            booleancamera = false;
            if (path != null) {
                path.clear();
            }
            if (booleancamera == false) {
                if (show_addfielding_str != null) {
                    show_addfielding_str.clear();
                }
            }
            show_addfielding_str.addAll(getIntersection(pathList_tmp, pathList));
            if (compare(show_addfielding_str,pathList)) {
                TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                        String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));
                if (show_addfielding_str.size() < 12) {
                    show_addfielding_str.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
            } else {
                if (show_addfielding_str != null) {
                    if (show_addfielding_str.size() > 0) {
                        path.addAll(getAnd_set(pathList,show_addfielding_str));
                    } else {
                        path.addAll(pathList);
                    }
                } else {
                    path.addAll(pathList);
                }
                String time = "" + System.currentTimeMillis();
                tailorsiae_tmp = 0;
                if (Constants.SDCardState()) {
                    photosavafilestr = Constants.picture_file + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                    photosavafile = new File(photosavafilestr);
                    startPhotoZoom(Uri.fromFile(new File(path.get(tailorsiae_tmp))),Uri.fromFile(photosavafile));
                }
            }


        } else if (requestCode == Constants.PhotoRequestCode) {
            if (booleancamera == false) {
                if (show_addfielding_str != null) {
                    show_addfielding_str.clear();
                }
            }
            show_addfielding_str.addAll(show_addfielding_str_tmp);
            TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                    String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

            if (show_addfielding_str.size() < 12) {
                show_addfielding_str.add("firstgridviewitem");
            }
            adapter.notifyDataSetChanged();
        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                booleancamera = true;
                photosavafile = new File(photosavafilestr);
                if (!photosavafile.exists()) {
                    try {
                        //在指定的文件夹中创建文件
                        photosavafile.createNewFile();
                    } catch (Exception e) {

                    }
                }
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
                    for (int i = 0; i < show_addfielding_str.size(); i++) {
                        if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                            show_addfielding_str.remove(i);
                        }
                    }
                    show_addfielding_str.add(photosavafilestr);
                    if (booleancamera == false) {
                        if (tailorsiae_tmp < path.size()-1) {
                            tailorsiae_tmp = tailorsiae_tmp +1;
                            String time = "" + System.currentTimeMillis();
                            photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                            photosavafile = new File(photosavafilestr);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startPhotoZoom(Uri.fromFile(new File(path.get(tailorsiae_tmp))), Uri.fromFile(photosavafile));
                                }
                            }, 700);
                        } else if (tailorsiae_tmp == path.size()-1){
                            TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                    String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

                            if (show_addfielding_str.size() < 12) {
                                show_addfielding_str.add("firstgridviewitem");
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

                        if (show_addfielding_str.size() < 12) {
                            show_addfielding_str.add("firstgridviewitem");
                        }
                        adapter.notifyDataSetChanged();
                        booleancamera = false;
                    }
                } else {
                    for (int i = 0; i < show_addfielding_str.size(); i++) {
                        if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                            show_addfielding_str.remove(i);
                        }
                    }
                    if (booleancamera == false) {
                        if (tailorsiae_tmp < path.size()-1) {
                            tailorsiae_tmp = tailorsiae_tmp +1;
                            String time = "" + System.currentTimeMillis();
                            photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                            photosavafile = new File(photosavafilestr);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startPhotoZoom(Uri.fromFile(new File(path.get(tailorsiae_tmp))), Uri.fromFile(photosavafile));
                                }
                            }, 300);
                        } else if (tailorsiae_tmp == path.size()-1){
                            TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                    String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

                            if (show_addfielding_str.size() < 12) {
                                show_addfielding_str.add("firstgridviewitem");
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

                        if (show_addfielding_str.size() < 12) {
                            show_addfielding_str.add("firstgridviewitem");
                        }
                        adapter.notifyDataSetChanged();
                        booleancamera = false;
                    }
                }
                mIsRefreshZoomImageview = true;
                break;
            case 4:
                if (show_addfielding_str != null) {
                    show_addfielding_str.clear();
                }
                // list（场地或展位）
                if (pictireType == 1) {
                    if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img() != null) {
                        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size() > 0) {
                            for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size(); i++) {
                                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url().length() > 0) {
                                    show_addfielding_str.add(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url());
                                    Pic_urllist.add(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url());
                                }
                            }
                        }
                    }
                } else if (pictireType == 2) {
                    if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img() != null) {
                        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size() > 0) {
                            for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size(); i++) {
                                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url() != null &&
                                        Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url().length() > 0) {
                                    show_addfielding_str.add(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url());
                                    Pic_urllist.add(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url());
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < show_addfielding_str.size(); i++) {
                    if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                        show_addfielding_str.remove(i);
                    }
                }
                TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                        String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));

                if (show_addfielding_str.size() < 12) {
                    show_addfielding_str.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
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
        intent.putExtra("outputX", 1000);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }
    //保存所要上传的资源的字段信息
    private void getPreviousDtata() {
        ArrayList<FieldAddfieldAttributesModel> field_imgs = new ArrayList<>();
        for (int i = 0; i < addfieldimg_str.size(); i++) {
            if (addfieldimg_str.get(i) != null &&
                    addfieldimg_str.get(i).length() > 0) {
                FieldAddfieldAttributesModel fieldAddfieldAttributesModel = new FieldAddfieldAttributesModel();
                fieldAddfieldAttributesModel.setSeq(i);
                fieldAddfieldAttributesModel.setPic_url(addfieldimg_str.get(i));
                field_imgs.add(fieldAddfieldAttributesModel);
            }
        }
        //设置需要显示的list（场地或展位）
        if (pictireType == 1) {
            Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setResource_img(field_imgs);
        } else if (pictireType == 2) {
            Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setResource_img(field_imgs);
        } else if (pictireType == 3) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setResource_img(field_imgs);
        }
        hideProgressDialog();
        Intent intent = new Intent();
        setResult(1,intent);
        finish();
    }
    //选择图片的功能
    private void choosepicture() {
        if (show_addfielding_str != null) {
            for (int i = 0; i < show_addfielding_str.size(); i++) {
                if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                    show_addfielding_str.remove(i);
                }
            }
        }
        if (show_addfielding_str_tmp != null) {
            show_addfielding_str_tmp.clear();
        }
        show_addfielding_str_tmp.addAll(show_addfielding_str);
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
                .mutiSelectMaxSize(12)
                        // 已选择的图片路径
                .pathList(show_addfielding_str)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(Constants.PhotoRequestCode)
                .build();

        ImageSelector.open(Field_AddField_UploadingPictureActivity.this, imageConfig);   // 开启图片选择器
    }

    public void Intentpayorder(ArrayList<String> path) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(path);
        test.MyChoosePicture(this);
    }
    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }
    @Override
    public void addfieldchoosepicture(ArrayList<String> path) {
        AndPermission.with(Field_AddField_UploadingPictureActivity.this)
                .requestCode(Constants.PermissionRequestCode)
                .permission(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .start();
    }

    @Override
    public void addfield_deletechoosepicture(int item_num) {

    }
    @Override
    public void AdField_showPreviewImg(int position) {
        if (pathtmp != null) {
            pathtmp.clear();
        }
        pathtmp .addAll(show_addfielding_str);
        for (int i = 0; i < pathtmp.size(); i++) {
            if (pathtmp.get(i).toString().equals("firstgridviewitem")) {
                pathtmp.remove(i);
            }
        }
        mConstants = new Constants(Field_AddField_UploadingPictureActivity.this,Field_AddField_UploadingPictureActivity.this,newPosition,mImageList_size,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mZoomPictureDialog,mImageViewList,pathtmp,mIsRefreshZoomImageview,
                position,true);
        mIsRefreshZoomImageview = false;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                hideProgressDialog();
                showStickerView(1);
            } else if (msg.what == 0) {
                hideProgressDialog();
                BaseMessageUtils.showToast(getResources().getString(R.string.myselfinfo_upload_picture_error_str));
            }
        }
    };
    private void uploadimg() {
        for (int i = 0; i < show_addfielding_str.size(); i++) {
            if (show_addfielding_str.get(i).toString().equals("firstgridviewitem")) {
                show_addfielding_str.remove(i);
            }
        }
        if (choose_filepicture_editor != null) {
            choose_filepicture_editor.clear();
        }
        choose_filepicture_editor.addAll(show_addfielding_str);
        if (choose_filepicture_url != null) {
            choose_filepicture_url.clear();
        }
        if (Pic_urllist != null) {
            for (int i = 0; i < Pic_urllist.size(); i++) {
                if (choose_filepicture_editor.contains(Pic_urllist.get(i).toString())) {
                    choose_filepicture_editor.remove(Pic_urllist.get(i).toString());
                    choose_filepicture_url.add(Pic_urllist.get(i).toString());
                }
            }
        }
        if (choose_filepicture_editor.size() > 0) {
            Field_FieldApi.getuptoken(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
        } else {
            if (addfieldimg_str != null) {
                addfieldimg_str.clear();
            }
            for (int i = 0; i < choose_filepicture_url.size(); i++) {
                addfieldimg_str.add(choose_filepicture_url.get(i));
            }
            getPreviousDtata();
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
                BaseMessageUtils.showToast(Field_AddField_UploadingPictureActivity.this,getResources().getString(R.string.field_tupesize_errortoast));
            }


        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                BaseMessageUtils.showToast(error.getMessage());
            checkAccess(error);
        }
    };
    private UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, org.json.JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                try {
                    addfieldimgsize ++;
                    addfieldimg_str.add(Config.qiniu_domain + jsonObject.getString("key").toString());
                    if (addfieldimgsize == choose_filepicture_editor.size() && upload == true) {
                        for (int i = 0; i < choose_filepicture_url.size(); i++) {
                            addfieldimg_str.add(choose_filepicture_url.get(i));
                        }
                        getPreviousDtata();
                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        BaseMessageUtils.showToast(Field_AddField_UploadingPictureActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                BaseMessageUtils.showToast(Field_AddField_UploadingPictureActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

        }
    };
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

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //2017/8/16 检查逻辑
            if (mZoomPictureDialog.isShowing()) {
                mZoomPictureDialog.dismiss();

            } else {
                BaseMessageUtils.showDialog(getContext(), getResources().getString(R.string.dialog_prompt),
                        getResources().getString(R.string.addfield_optional_info_savedata_dialog_text),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                uploadimg();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initView() {
        Intent resIntent = getIntent();
        if (resIntent.getExtras() != null && resIntent.getExtras().get("picture_type") != null) {
            pictireType = resIntent.getExtras().getInt("picture_type");
        }
        initPreviewZoomView();
        recycler  = (FieldMyGridView)findViewById(R.id.field_recycler);
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseMessageUtils.showDialog(getContext(), getResources().getString(R.string.dialog_prompt),
                        getResources().getString(R.string.addfield_optional_info_savedata_dialog_text),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                uploadimg();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

            }
        });
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.mTxt_save_fieldinfo),
                17, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        uploadimg();
                    }
                });

        //删除保存在本地裁剪的照片
        Constants.delete_picture_file();
        //设置需要显示的list（场地或展位）
        if (pictireType == 1) {
            if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img() != null) {
                if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size() > 0) {
                    for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().size(); i++) {
                        if (Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url().length() > 0) {
                            show_addfielding_str.add(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url());
                            Pic_urllist.add(Field_AddResourcesModel.getInstance().getResource().getCommunity_data().getResource_img().get(i).getPic_url());
                        }

                    }
                }
            } else {
                ArrayList<FieldAddfieldAttributesModel> field_imgs = new ArrayList<>();
                Field_AddResourcesModel.getInstance().getResource().getCommunity_data().setResource_img(field_imgs);
            }
        } else if (pictireType == 2) {
            if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img() != null) {
                if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size() > 0) {
                    for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().size(); i++) {
                        if (Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url().length() > 0) {
                            show_addfielding_str.add(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url());
                            Pic_urllist.add(Field_AddResourcesModel.getInstance().getResource().getPhysical_data().getResource_img().get(i).getPic_url());
                        }

                    }
                }
            } else {
                ArrayList<FieldAddfieldAttributesModel> field_imgs = new ArrayList<>();
                Field_AddResourcesModel.getInstance().getResource().getPhysical_data().setResource_img(field_imgs);
            }
        } else if (pictireType == 3) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img() != null) {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size() > 0) {
                    for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().size(); i++) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url().length() > 0) {
                            show_addfielding_str.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url());
                            Pic_urllist.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getResource_img().get(i).getPic_url());
                        }

                    }
                }
            } else {
                ArrayList<FieldAddfieldAttributesModel> field_imgs = new ArrayList<>();
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setResource_img(field_imgs);
            }
        }
        TitleBarUtils.setTitleText(this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));
        if (show_addfielding_str.size() < 12) {
            show_addfielding_str.add("firstgridviewitem");
        }
        adapter = new Field_ChoosePictureGridViewAdapter(this,Field_AddField_UploadingPictureActivity.this,show_addfielding_str,0);
        recycler.setAdapter(adapter);
        final Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().get("click_position") != null &&
                intent.getExtras().get("click_position").toString().length() > 0) {
            if (pathtmp != null) {
                pathtmp.clear();
            }
            pathtmp .addAll(show_addfielding_str);
            for (int i = 0; i < pathtmp.size(); i++) {
                if (pathtmp.get(i).toString().equals("firstgridviewitem")) {
                    pathtmp.remove(i);
                }
            }
            mConstants = new Constants(Field_AddField_UploadingPictureActivity.this,Field_AddField_UploadingPictureActivity.this,newPosition,mImageList_size,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
            mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                    mZoomPictureDialog,mImageViewList,pathtmp,mIsRefreshZoomImageview,
                    Integer.parseInt(intent.getExtras().get("click_position").toString()),true);
            mIsRefreshZoomImageview = false;
        }
    }
    private void initPreviewZoomView() {
        mZoomPictureDialog = new AlertDialog.Builder(this).create();
        mZoomPictureDialog.setOnKeyListener(onKeylistener);
        mView = getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mShowPictureSizeTV = (TextView)mView.findViewById(R.id.showpicture_dialog_sizetxt);
        TextView mBackTv = (TextView)mView.findViewById(R.id.showpicture_dialog_back);
        mZoomViewPage = (ViewPager)mView.findViewById(R.id.zoom_dialog_viewpage);
        stickerLL = (LinearLayout) mView.findViewById(R.id.sticker_ll);
        //2019/1/4 涂鸦
        mDoodleContainerFL = (FrameLayout) mView.findViewById(R.id.doodle_container);
        final LinearLayout stickerAllLL = (LinearLayout) mView.findViewById(R.id.sticker_operate_ll);
        final RelativeLayout allRL = (RelativeLayout) mView.findViewById(R.id.show_picture_all_rl);

        TextView stickerSaveBtn = (TextView) mView.findViewById(R.id.sticker_save_btn);
        TextView stickerTitleTV = (TextView) mView.findViewById(R.id.sticker_title_tv);
        // FIXME: 2019/1/5 涂鸦
        mDoodleDotAddImgv = (ImageView) mView.findViewById(R.id.doodle_dot_add_imgv);
        mDoodleDotAddLL = (LinearLayout) mView.findViewById(R.id.doodle_dot_add_ll);
        mDoodleSeekBar = (SeekBar) mView.findViewById(R.id.doodle_seekbar);
        mDoodleRevocationTextImgv = (ImageButton) mView.findViewById(R.id.doodle_revocation_text);
        mDoodleRevocationBitmapImgv = (ImageButton) mView.findViewById(R.id.doodle_revocation_bitmap);

        mDoodleOperatingTypeRG = (RadioGroup) mView.findViewById(R.id.doodle_operating_type);
        RadioButton doodle_delete_btn = (RadioButton) mView.findViewById(R.id.doodle_delete_item);
        final RadioButton doodle_dot_btn = (RadioButton) mView.findViewById(R.id.doodle_dot);
        RadioGroup doodle_color_group = (RadioGroup) mView.findViewById(R.id.doodle_color_group);
        TextView doodle_text_confirm = (TextView) mView.findViewById(R.id.doodle_text_confirm);
        mDoodleTextEditText = (EditText) mView.findViewById(R.id.doodle_text_edit);
        final TextView mDoodleTextEditDeleteTV = (TextView) mView.findViewById(R.id.doodle_text_delete_tv);
        mDoodleTextTypeLL = (LinearLayout) mView.findViewById(R.id.doodle_text_type_ll);
        mDoodleLineTypeLL = (LinearLayout) mView.findViewById(R.id.doodle_line_type_ll);
        ImageView doodle_pic_add_img = (ImageView) mView.findViewById(R.id.doodle_pic_add_img);
        mDoodleBitmapTypeLL = (LinearLayout) mView.findViewById(R.id.doodle_bitmap_type_ll);
        mDoodleTextOrDotTypeLL = (LinearLayout) mView.findViewById(R.id.doodle_text_or_dot_type_ll);
        mDoodleDeleteImgv = (TextView) mView.findViewById(R.id.doodle_dot_delete_imgv);

        mBackTv.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                back();
            }
        });
        mDoodleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 0) {
                    mDoodleSeekBar.setProgress(1);
                    return;
                }
                if ((int)mDoodle.getSize() == progress) {
                    return;
                }
                mDoodle.setSize(progress);
                List<IDoodleItem> items = mDoodle.getAllItem();
                if (items != null && items.size() > 0) {
                    for (int i = items.size() - 1; i >= 0; i--) {
                        IDoodleItem elem = items.get(i);
                        if (elem.getPen().equals(DoodlePen.DOT) &&
                                elem.getShape().equals(DoodleShape.LINE)) {
                            elem.setSize(progress);
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mDoodleRevocationTextImgv.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                List<IDoodleItem> items = mDoodle.getAllItem();
                if (items != null && items.size() > 0) {
                    for (int i = items.size() - 1; i >= 0; i--) {
                        IDoodleItem elem = items.get(i);
                        if (elem.getPen().equals(DoodlePen.TEXT)) {
                            mDoodle.removeItem(elem);
                            break;
                        }
                    }
                }
                setHasIDoodleItemStatus();
            }
        });
        mDoodleRevocationBitmapImgv.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                List<IDoodleItem> items = mDoodle.getAllItem();
                if (items != null && items.size() > 0) {
                    for (int i = items.size() - 1; i >= 0; i--) {
                        IDoodleItem elem = items.get(i);
                        if (elem.getPen().equals(DoodlePen.BITMAP)) {
                            mDoodle.removeItem(elem);
                            break;
                        }
                    }
                }
                setHasIDoodleItemStatus();
            }
        });

        doodle_color_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.doodle_color_red) {
                    setDoodleColor(R.color.module_addfield_upload_pic_red_color);
                } else if (checkedId == R.id.doodle_color_purple) {
                    setDoodleColor(R.color.module_addfield_upload_pic_purple_color);
                } else if (checkedId == R.id.doodle_color_yellow) {
                    setDoodleColor(R.color.module_addfield_upload_pic_yellow_color);
                } else if (checkedId == R.id.doodle_color_green) {
                    setDoodleColor(R.color.module_addfield_upload_pic_green_color);
                } else if (checkedId == R.id.doodle_color_blue) {
                    setDoodleColor(R.color.default_bluebg);
                } else if (checkedId == R.id.doodle_color_black) {
                    setDoodleColor(R.color.title_bar_txtcolor);
                } else if (checkedId == R.id.doodle_color_white) {
                    setDoodleColor(R.color.white);
                }
            }
        });
        mDoodleOperatingTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doodle_dot_btn.setOnClickListener(null);
                mDoodleCheckedId = checkedId;
                if (mTouchGestureListener != null) {
                    mTouchGestureListener.setSelectedItem(null);
                }
                if (mDoodleCheckedId > 0 &&
                        mDoodleCheckedId != R.id.doodle_delete_item) {
                    getPictureBitmap();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            doodle_dot_btn.setOnClickListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    if (mDoodleCheckedId == R.id.doodle_dot &&
                                            doodle_dot_btn.isChecked() && mDoodle != null) {
                                        if (mDoodleSeekBar.getProgress() > 0) {
                                            mDoodle.setSize(mDoodleSeekBar.getProgress());
                                        } else {
                                            mDoodle.setSize(3);
                                        }
                                        float unit = mDoodle.getUnitSize();
                                        float xx = mDoodleView.toX(stickerWidth/2);
                                        float yy = mDoodleView.toY(stickerHeight/2);
                                        float addX = 80 * unit;
                                        float addY = 40 * unit;
                                        List<HashMap<String,Float>> mDotCoordinateList = new ArrayList<>();
                                        HashMap<String,Float> map = new HashMap<>();
                                        map.put("x",xx - addX);
                                        map.put("y",yy - addY);
                                        mDotCoordinateList.add(map);
                                        HashMap<String,Float> map1 = new HashMap<>();
                                        map1.put("x",xx + addX);
                                        map1.put("y",yy - addY);
                                        mDotCoordinateList.add(map1);
                                        HashMap<String,Float> map2 = new HashMap<>();
                                        map2.put("x",xx + addX);
                                        map2.put("y",yy + addY);
                                        mDotCoordinateList.add(map2);
                                        HashMap<String,Float> map3 = new HashMap<>();
                                        map3.put("x",xx - addX);
                                        map3.put("y",yy + addY);
                                        mDotCoordinateList.add(map3);
                                        Path path1 = new Path();
                                        for (int i = 0; i < mDotCoordinateList.size(); i++) {
                                            if (i == 0) {
                                                path1.moveTo(mDotCoordinateList.get(i).get("x"),
                                                        mDotCoordinateList.get(i).get("y"));
                                            } else {
                                                path1.lineTo(mDotCoordinateList.get(i).get("x"),
                                                        mDotCoordinateList.get(i).get("y"));
                                            }
                                            mDoodle.setShape(DoodleShape.FILL_CIRCLE);
                                            IDoodleSelectableItem iDoodleSelectableItem =
                                                    DoodlePath.toShape(mDoodle,
                                                            mDotCoordinateList.get(i).get("x"),
                                                            mDotCoordinateList.get(i).get("y"),
                                                            mDotCoordinateList.get(i).get("x"),
                                                            mDotCoordinateList.get(i).get("y"),
                                                            Field_AddField_UploadingPictureActivity.this);
                                            iDoodleSelectableItem.setPosition(mDotPosition);
                                            mDoodle.addItem(iDoodleSelectableItem);
                                        }
                                        mDoodle.setShape(DoodleShape.LINE);
                                        path1.close();//封闭
                                        IDoodleSelectableItem iDoodleItem = DoodlePath.toShape(mDoodle,
                                                path1,Field_AddField_UploadingPictureActivity.this);
                                        iDoodleItem.setPosition(mDotPosition);
                                        mDoodle.addItem(iDoodleItem);
                                        mDoodle.refresh();
                                        mDotPosition ++;
                                    }
                                }
                            });
                        }
                    }, 200);
                }
            }
        });
        doodle_delete_btn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                new AlertDialog.Builder(Field_AddField_UploadingPictureActivity.this)
                        .setTitle(getResources().getString(R.string.delete_prompt))
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                pathtmp.remove(newPosition);
                                show_addfielding_str.remove(newPosition);
                                mImageViewList.remove(newPosition);
                                int addpictureimg_num = 0;
                                for (int i = 0; i < show_addfielding_str.size(); i++) {
                                    if (show_addfielding_str.get(i).equals("firstgridviewitem")) {
                                        addpictureimg_num ++;
                                    }
                                }
                                TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                        String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));
                                if (addpictureimg_num == 0) {
                                    show_addfielding_str.add("firstgridviewitem");
                                }
                                adapter.notifyDataSetChanged();
                                if (pathtmp != null) {
                                    if (pathtmp.size() != 0) {
                                        if (pathtmp.size() > newPosition + 1 || pathtmp.size() == newPosition + 1) {
                                            mConstants.showPreviewZoomAdapter(mImageViewList,mZoomViewPage,mShowPictureSizeTV,newPosition);
                                        } else {
                                            mConstants.showPreviewZoomAdapter(mImageViewList,mZoomViewPage,mShowPictureSizeTV,newPosition - 1);
                                        }
                                    } else {
                                        mZoomPictureDialog.dismiss();
                                    }
                                } else {
                                    mZoomPictureDialog.dismiss();
                                }
                                clearDoodleViewData();
                            }
                        }).show();
            }
        });
        doodle_text_confirm.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mDoodleTextEditText.getText().toString().trim().length() > 0) {
                    hideKeyboard();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mDoodle.setPen(DoodlePen.TEXT);
                            mDoodle.setSize(20 * mDoodle.getUnitSize());
                            if (mTouchGestureListener.getSelectedItem() instanceof DoodleText) {
                                ((DoodleText) mTouchGestureListener.getSelectedItem()).setText(mDoodleTextEditText.getText().toString().trim());
                            } else {
                                IDoodleSelectableItem item = new DoodleText(mDoodle, mDoodleTextEditText.getText().toString().trim(), mDoodle.getSize(), mDoodle.getColor().copy(), mDoodleView.toX(stickerWidth/2), mDoodleView.toY(stickerHeight/2),Field_AddField_UploadingPictureActivity.this);
                                mDoodle.addItem(item);
                            }
                            mDoodle.refresh();
                            mDoodleRevocationTextImgv.setImageResource(R.drawable.ic_next_thr_ten);
                            mDoodleTextEditText.setText("");
                        }
                    }, 500);
                } else {
                    BaseMessageUtils.showToast(getResources().getString(R.string.module_addfield_community_info_input_hint));
                }
            }
        });
        doodle_pic_add_img.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mDoodle.setPen(DoodlePen.BITMAP);
                mDoodle.setSize(80 * mDoodle.getUnitSize());
                createDoodleBitmap(null, mDoodleView.toX(stickerWidth/2), mDoodleView.toY(stickerHeight/2));//添加贴图
                mDoodleRevocationBitmapImgv.setImageResource(R.drawable.ic_next_thr_ten);
            }
        });
        mDoodleTextEditText.addTextChangedListener(new TextWatcher() {
            int selectionStart;
            int selectionEnd;
            CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mDoodleTextEditDeleteTV.setVisibility(View.VISIBLE);
                } else {
                    mDoodleTextEditDeleteTV.setVisibility(View.GONE);
                }
                selectionStart = mDoodleTextEditText.getSelectionStart();
                selectionEnd = mDoodleTextEditText.getSelectionEnd();
                if (temp.length() > 10) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.module_upload_pic_doodle_text_remind));
                    s.delete(selectionStart-1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDoodleTextEditText.setText(s);
                    mDoodleTextEditText.setSelection(tempSelection);
                }
            }
        });
        mDoodleTextEditDeleteTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mDoodleTextEditText.setText("");
            }
        });
        stickerSaveBtn.setVisibility(View.VISIBLE);
        if (pictireType == 2) {
            stickerAllLL.setVisibility(View.VISIBLE);
            stickerTitleTV.setVisibility(View.VISIBLE);
        }
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
//        stickerCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (stickerLL.getVisibility() == View.VISIBLE) {
////                    stickerView.removeAllStickers();
//                    // FIXME: 2019/1/7 涂鸦清空
//                    mDoodle.clear();
//                    stickerLL.setVisibility(View.GONE);
//                }
//            }
//        });
        stickerSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2018/3/19 保存贴纸的操作
                // FIXME: 2019/1/7 mDoodle.save(); 涂鸦保存
                if (stickerLL.getVisibility() == (View.VISIBLE)) {
                    // FIXME: 2019/1/18 删除点
                    List<IDoodleItem> items = mDoodle.getAllItem();
                    if (items != null && items.size() > 0) {
                        for (int i = items.size() - 1; i >= 0; i--) {
                            IDoodleItem elem = items.get(i);
                            if (elem.getPen().equals(DoodlePen.DOT) &&
                                    elem.getShape().equals(DoodleShape.FILL_CIRCLE)) {
                                mDoodle.removeItem(elem);
                            }
                        }
                    }
                    mDoodle.save();
                }
            }
        });
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
                            int i = view.getId();
                            if (i == R.id.upload_photo_ll) {
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
                                }
                            } else if (i == R.id.upload_picture_ll) {
                                mCustomDialog.dismiss();
                                choosepicture();
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(Field_AddField_UploadingPictureActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(Field_AddField_UploadingPictureActivity.this,mCustomDialog);
                    mCustomDialog.show();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                BaseMessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    private void showStickerView(int type) {
        stickerLL.setVisibility(View.VISIBLE);
        // FIXME: 2019/1/4 涂鸦显示
        if (type == 1) {
            getDooldle(Constants.picture_file_str  +
                    String.valueOf(stickerImgInt - 1) + "sticker.jpg");
        } else if (type == 2) {
            getDooldle(pathtmp.get(newPosition));
        }
        stickerWidth = mDisplayMetrics.widthPixels;
        stickerHeight = stickerWidth * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setDoodleStype();
            }
        }, 500);
    }
    private void getPictureBitmap() {
        if (stickerLL.getVisibility() == View.GONE) {
            if (pathtmp.get(newPosition).toString().indexOf("http") != -1) {
                showProgressDialog();
                Field_FieldApi.downloadPic(MyAsyncHttpClient.MyAsyncHttpClient(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        hideProgressDialog();
                        BaseMessageUtils.showToast(getResources().getString(R.string.myselfinfo_upload_picture_error_str));
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        Bitmap bitmap= BitmapFactory.decodeStream(response.body().byteStream());
                        if (bitmap != null) {

                            File dcimFile = Constants.getDCIMFile(
                                    String.valueOf(stickerImgInt++) + "sticker.jpg");
                            FileOutputStream ostream = null;
                            try {
                                ostream = new FileOutputStream(dcimFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                ostream.close();
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                                if (bitmap != null) {
                                    bitmap.recycle();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }

                    }
                },pathtmp.get(newPosition).toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Max_Watermark);
            } else {
                showStickerView(2);
            }
        } else {
            setDoodleStype();
        }
    }

    // FIXME: 2019/1/4 涂鸦功能界面
    private void getDooldle(String imgPath) {
         mDoodle = mDoodleView = new DoodleView(this, ImageUtils.createBitmapFromPath(imgPath, this), new IDoodleListener() {
            /*
            called when save the doodled iamge.
            保存涂鸦图像时调用
             */
            @Override
            public void onSaved(IDoodle doodle, Bitmap bitmap, Runnable callback) {
                //do something
                if (stickerLL.getVisibility() == View.VISIBLE) {
                    String time = "" + System.currentTimeMillis();
                    File file = Constants.getDCIMFile(time + ".jpg");
                    String url = Constants.picture_file_str + time + ".jpg";
                    if (file != null) {
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            pathtmp.remove(newPosition);
                            pathtmp.add(newPosition,url);
                            show_addfielding_str.remove(newPosition);
                            show_addfielding_str.add(newPosition,url);
                            mImageViewList.remove(newPosition);
                            ZoomImageView imageView = new ZoomImageView(
                                    Field_AddField_UploadingPictureActivity.this);

                            Glide.with(Field_AddField_UploadingPictureActivity.this)
                                    .load(url)
                                    .placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big)
                                    .override(stickerWidth, stickerHeight).centerCrop()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(imageView);
                            mImageViewList.add(newPosition,imageView);
                            int addpictureimg_num = 0;
                            for (int i = 0; i < show_addfielding_str.size(); i++) {
                                if (show_addfielding_str.get(i).equals("firstgridviewitem")) {
                                    addpictureimg_num ++;
                                }
                            }
                            TitleBarUtils.setTitleText(Field_AddField_UploadingPictureActivity.this, Field_AddField_UploadingPictureActivity.this.getResources().getString(R.string.addfield_upload_picture_title_onetext) +
                                    String.valueOf(show_addfielding_str.size()) +getResources().getString(R.string.addfield_upload_picture_title_twotext));
                            if (addpictureimg_num == 0) {
                                show_addfielding_str.add("firstgridviewitem");
                            }
                            adapter.notifyDataSetChanged();
                            if (pathtmp != null) {
                                if (pathtmp.size() != 0) {
                                    if (pathtmp.size() > newPosition + 1 || pathtmp.size() == newPosition + 1) {
                                        mConstants.showPreviewZoomAdapter(mImageViewList,mZoomViewPage,mShowPictureSizeTV,newPosition);
                                    } else {
                                        mConstants.showPreviewZoomAdapter(mImageViewList,mZoomViewPage,mShowPictureSizeTV,newPosition - 1);
                                    }
                                } else {
                                    mZoomPictureDialog.dismiss();
                                }
                            } else {
                                mZoomPictureDialog.dismiss();
                            }
                            clearDoodleViewData();
                        } catch (Exception e) {
                            e.printStackTrace();

                        } finally {
                            Util.closeQuietly(outputStream);
                        }
                        clearDoodleViewData();
                    } else {
                        BaseMessageUtils.showToast("没有文件");
                    }
                }
            }

            /*
             called when it is ready to doodle because the view has been measured. Now, you can set size, color, pen, shape, etc.
             此时view已经测量完成，涂鸦前的准备工作已经完成，在这里可以设置大小、颜色、画笔、形状等。
             */
            @Override
            public void onReady(IDoodle doodle) {
                //do something
                // 设置初始值
                mDoodle.setSize(3);
                // 选择画笔
                mDoodle.setPen(DoodlePen.NULL);
                //设置直线
                mDoodle.setShape(DoodleShape.LINE);
                mDoodle.setColor(new DoodleColor(getResources().getColor(R.color.white)));
            }
        });

        mTouchGestureListener = new DoodleOnTouchGestureListener(mDoodleView, new DoodleOnTouchGestureListener.ISelectionListener() {
            /*
             called when the item(such as text, texture) is selected/unselected.
             item（如文字，贴图）被选中或取消选中时回调
             */
            @Override
            public void onSelectedItem(IDoodle doodle, final IDoodleSelectableItem selectableItem, boolean selected) {
                //do something
                if (selected) {
                    if (selectableItem.getPen().equals(DoodlePen.DOT) &&
                            (selectableItem.getShape().equals(DoodleShape.LINE) ||
                                    selectableItem.getShape().equals(DoodleShape.FILL_CIRCLE))) {
                        mDoodleSeekBar.setProgress((int) selectableItem.getSize());//线的粗细
                        if (selectableItem.getShape().equals(DoodleShape.LINE)) {
                            mDoodleDeleteImgv.setVisibility(View.VISIBLE);
                            mDoodleDeleteImgv.setOnClickListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    List<IDoodleItem> items = mDoodle.getAllItem();
                                    if (items != null && items.size() > 0) {
                                        for (int i = items.size() - 1; i >= 0; i--) {
                                            IDoodleItem elem = items.get(i);
                                            if (elem.getPen().equals(DoodlePen.DOT) &&
                                                    selectableItem.getPosition() ==
                                                            ((IDoodleSelectableItem)elem).getPosition()) {
                                                mDoodle.removeItem(elem);
                                            }
                                        }
                                    }
                                    mDoodleDeleteImgv.setVisibility(View.GONE);
                                    mDoodleDotAddImgv.setImageResource(R.drawable.ic_add_dian_thr_ten);
                                    mDoodleDotAddLL.setOnClickListener(null);
                                }
                            });
                        }
                        mDoodleDotAddImgv.setImageResource(R.drawable.ic_add_dian_select_thr_ten);
                        mDoodleDotAddLL.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                // FIXME: 2019/1/18 增加点
                                List<IDoodleItem> items = mDoodle.getAllItem();
                                if (items != null && items.size() > 0) {
                                    List<HashMap<String,Float>> mDotCoordinateList = new ArrayList<>();
                                    for (int i = 0; i < items.size(); i++) {
                                        IDoodleItem elem = items.get(i);
                                        if (elem.getPen().equals(DoodlePen.DOT) &&
                                                elem.getShape().equals(DoodleShape.FILL_CIRCLE) &&
                                                ((IDoodleSelectableItem)elem).getPosition() == selectableItem.getPosition()) {
                                            HashMap<String,Float> map = new HashMap<>();
                                            map.put("x",elem.getPivotX());
                                            map.put("y",elem.getPivotY());
                                            mDotCoordinateList.add(map);
                                        }
                                    }
                                    for (int i = items.size() - 1; i >= 0; i--) {
                                        IDoodleItem elem = items.get(i);
                                        if (elem.getPen().equals(DoodlePen.DOT) &&
                                                elem.getShape().equals(DoodleShape.FILL_CIRCLE) &&
                                                ((IDoodleSelectableItem)elem).getPosition() == selectableItem.getPosition()) {
                                            mDoodle.removeItem(elem);
                                        } else if (elem.getPen().equals(DoodlePen.DOT) &&
                                                elem.getShape().equals(DoodleShape.LINE) &&
                                                ((IDoodleSelectableItem)elem).getPosition() == selectableItem.getPosition()) {
                                            mDoodle.removeItem(elem);
                                        }
                                    }
                                    mDoodle.setShape(DoodleShape.FILL_CIRCLE);
                                    if (mDotCoordinateList != null &&
                                            mDotCoordinateList.size() > 0) {
                                        float x = mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("x") -
                                                (mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("x") -
                                                        mDotCoordinateList.get(mDotCoordinateList.size() - 1).get("x")) / 2;
                                        float y = ((mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") -
                                                mDotCoordinateList.get(mDotCoordinateList.size() - 1).get("y")) / 2 == 0 ?
                                                - 50 * mDoodle.getUnitSize():
                                                (mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") -
                                                        mDotCoordinateList.get(mDotCoordinateList.size() - 1).get("y")) / 2);
                                        HashMap<String,Float> map = new HashMap<>();
                                        if (Math.abs(y) < 10 * mDoodle.getUnitSize()) {
                                            if (y > 0) {
                                                y = mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") +
                                                        (y + 10 * mDoodle.getUnitSize());
                                            } else if (y < 0){
                                                y = mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") -
                                                        (y - 10 * mDoodle.getUnitSize());
                                            }
                                        } else {
                                            if ((mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") -
                                                    mDotCoordinateList.get(mDotCoordinateList.size() - 1).get("y")) == 0) {
                                                y = mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y") + 50 * mDoodle.getUnitSize();
                                            } else {
                                                y = mDotCoordinateList.get(mDotCoordinateList.size() - 2).get("y");
                                            }
                                        }

                                        map.put("x",x);
                                        map.put("y",y);
                                        mDotCoordinateList.add(mDotCoordinateList.size()- 1,map);
                                        //重新连线
                                        Path path1 = new Path();
                                        for (int i = 0; i < mDotCoordinateList.size(); i++) {
                                            if (i == 0) {
                                                path1.moveTo(mDotCoordinateList.get(i).get("x"),
                                                        mDotCoordinateList.get(i).get("y"));
                                            } else {
                                                path1.lineTo(mDotCoordinateList.get(i).get("x"),
                                                        mDotCoordinateList.get(i).get("y"));
                                            }
                                            mDoodle.setShape(DoodleShape.FILL_CIRCLE);
                                            IDoodleSelectableItem iDoodleSelectableItem =
                                                    DoodlePath.toShape(mDoodle,
                                                            mDotCoordinateList.get(i).get("x"),
                                                            mDotCoordinateList.get(i).get("y"),
                                                            mDotCoordinateList.get(i).get("x"),
                                                            mDotCoordinateList.get(i).get("y"),
                                                            Field_AddField_UploadingPictureActivity.this);
                                            iDoodleSelectableItem.setPosition(selectableItem.getPosition());
                                            mDoodle.addItem(iDoodleSelectableItem);
                                        }
                                        mDoodle.setShape(DoodleShape.LINE);
                                        path1.close();//封闭
                                        IDoodleSelectableItem iDoodleItem = DoodlePath.toShape(mDoodle,
                                                path1,Field_AddField_UploadingPictureActivity.this);
                                        iDoodleItem.setPosition(selectableItem.getPosition());
                                        mDoodle.addItem(iDoodleItem);
                                        mDoodle.refresh();
                                        mDoodleDotAddImgv.setImageResource(R.drawable.ic_add_dian_thr_ten);
                                        mDoodleDotAddLL.setOnClickListener(null);
                                    }
                                }                            }
                        });
                    } else if (mDoodle.getPen().equals(DoodlePen.TEXT)) {
                        mDoodleTextEditText.setText(((DoodleText)selectableItem).getText());
                    }
                } else {
                    if (mDoodle.getPen().equals(DoodlePen.TEXT)) {
                        mDoodleTextEditText.setText("");
                    }
                    mDoodleDeleteImgv.setVisibility(View.GONE);
                    mDoodleDotAddImgv.setImageResource(R.drawable.ic_add_dian_thr_ten);
                    mDoodleDotAddLL.setOnClickListener(null);
                }
            }

            /*
             called when you click the view to create a item(such as text, texture).
             点击View中的某个点创建可选择的item（如文字，贴图）时回调
             */
            @Override
            public void onCreateSelectableItem(IDoodle doodle, float x, float y) {
                //do something
//                if (mDoodle.getPen() == DoodlePen.TEXT) {
//                    createDoodleText(null, x, y);
//                } else if (mDoodle.getPen() == DoodlePen.BITMAP) {
//                    createDoodleBitmap(null, x, y);//添加贴图
//                }
                if (mDoodle.getPen().equals(DoodlePen.TEXT)) {
                    mDoodleTextEditText.setText("");
                }
            }

            @Override
            public void onDownXY(float x, float y) {

            }
        },Field_AddField_UploadingPictureActivity.this);

        // create touch detector, which dectects the gesture of scoll, scale, single tap, etc.
        // 创建手势识别器，识别滚动，缩放，点击等手势
        IDoodleTouchDetector detector = new DoodleTouchDetector(getApplicationContext(), mTouchGestureListener);
        mDoodleView.setDefaultTouchDetector(detector);
        mDoodleContainerFL.addView(mDoodleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDoodle.setDoodleMinScale(0.25f);
        mDoodle.setDoodleMaxScale(4f);

    }
    // 添加文字
    private void createDoodleText(final DoodleText doodleText, final float x, final float y) {
        if (isFinishing()) {
            return;
        }

        DialogController.showInputTextDialog(this, doodleText == null ? null : doodleText.getText(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (v.getTag() + "").trim();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                if (doodleText == null) {
                    IDoodleSelectableItem item = new DoodleText(mDoodle, text, mDoodle.getSize(), mDoodle.getColor().copy(), x, y,Field_AddField_UploadingPictureActivity.this);
                    mDoodle.addItem(item);
                    mTouchGestureListener.setSelectedItem(item);//选中涂鸦可以移动操作
                } else {
                    doodleText.setText(text);
                }
                mDoodle.refresh();
            }
        }, null);
        if (doodleText == null) {
//            mSettingsPanel.removeCallbacks(mHideDelayRunnable);//隐藏选择的工具框
        }
    }
    // 添加贴图
    private void createDoodleBitmap(final DoodleBitmap doodleBitmap, final float x, final float y) {
        Bitmap bitmap = ImageUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_xiaohongqi));

        if (doodleBitmap == null) {
            IDoodleSelectableItem item = new DoodleBitmap(mDoodle, bitmap, mDoodle.getSize(), x, y,Field_AddField_UploadingPictureActivity.this);
            mDoodle.addItem(item);
            mTouchGestureListener.setSelectedItem(item);
        } else {
            doodleBitmap.setBitmap(bitmap);
        }
        mDoodle.refresh();
    }
    private void setDoodleColor(int color) {
        mDoodle.setColor(new DoodleColor(getResources().getColor(color)));
        // FIXME: 2019/1/14 如果是线就全部设为指定颜色
        List<IDoodleItem> items = mDoodle.getAllItem();
        if (items != null && items.size() > 0) {
            for (int i = items.size() - 1; i >= 0; i--) {
                IDoodleItem elem = items.get(i);
               if (mDoodleCheckedId == R.id.doodle_dot) {
                   if (elem.getPen().equals(DoodlePen.DOT)) {
                       elem.setColor(new DoodleColor(getResources().getColor(color)));
                   }
                } else if (mDoodleCheckedId == R.id.doodle_text) {
                   if (elem.getPen().equals(DoodlePen.TEXT)) {
                       elem.setColor(new DoodleColor(getResources().getColor(color)));
                   }
               }
            }
        }
    }
    private void setDoodleStype() {
        if (mDoodleCheckedId > 0 &&
                (((RadioButton)mView.findViewById(mDoodleCheckedId)) != null &&
                        ((RadioButton) mView.findViewById(mDoodleCheckedId)).isChecked())) {
            if (mDoodleCheckedId == R.id.doodle_pic) {
                mDoodle.setPen(DoodlePen.BITMAP);
                mDoodle.setSize(80 * mDoodle.getUnitSize());
                mDoodleTextOrDotTypeLL.setVisibility(View.GONE);
                mDoodleBitmapTypeLL.setVisibility(View.VISIBLE);
            } else if (mDoodleCheckedId == R.id.doodle_dot) {
                mDoodleTextTypeLL.setVisibility(View.GONE);
                mDoodleLineTypeLL.setVisibility(View.VISIBLE);
                mDoodleTextOrDotTypeLL.setVisibility(View.VISIBLE);
                mDoodleBitmapTypeLL.setVisibility(View.GONE);
                mDoodle.setPen(DoodlePen.DOT);
                mDoodle.setShape(DoodleShape.FILL_CIRCLE);
                mDoodleView.setEditMode(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (mDoodleCheckedId == R.id.doodle_dot && mDoodle != null &&
                                !isHasIDoodleItem()) {
                            if (mDoodleSeekBar.getProgress() > 0) {
                                mDoodle.setSize(mDoodleSeekBar.getProgress());
                            } else {
                                mDoodle.setSize(3);
                            }
                            float unit = mDoodle.getUnitSize();
                            float xx = mDoodleView.toX(stickerWidth/2);
                            float yy = mDoodleView.toY(stickerHeight/2);
                            float addX = 80 * unit;
                            float addY = 40 * unit;
                            List<HashMap<String,Float>> mDotCoordinateList = new ArrayList<>();
                            HashMap<String,Float> map = new HashMap<>();
                            map.put("x",xx - addX);
                            map.put("y",yy - addY);
                            mDotCoordinateList.add(map);
                            HashMap<String,Float> map1 = new HashMap<>();
                            map1.put("x",xx + addX);
                            map1.put("y",yy - addY);
                            mDotCoordinateList.add(map1);
                            HashMap<String,Float> map2 = new HashMap<>();
                            map2.put("x",xx + addX);
                            map2.put("y",yy + addY);
                            mDotCoordinateList.add(map2);
                            HashMap<String,Float> map3 = new HashMap<>();
                            map3.put("x",xx - addX);
                            map3.put("y",yy + addY);
                            mDotCoordinateList.add(map3);
                            Path path1 = new Path();
                            for (int i = 0; i < mDotCoordinateList.size(); i++) {
                                if (i == 0) {
                                    path1.moveTo(mDotCoordinateList.get(i).get("x"),
                                            mDotCoordinateList.get(i).get("y"));
                                } else {
                                    path1.lineTo(mDotCoordinateList.get(i).get("x"),
                                            mDotCoordinateList.get(i).get("y"));
                                }
                                mDoodle.setShape(DoodleShape.FILL_CIRCLE);
                                IDoodleSelectableItem iDoodleSelectableItem =
                                        DoodlePath.toShape(mDoodle,
                                                mDotCoordinateList.get(i).get("x"),
                                                mDotCoordinateList.get(i).get("y"),
                                                mDotCoordinateList.get(i).get("x"),
                                                mDotCoordinateList.get(i).get("y"),
                                                Field_AddField_UploadingPictureActivity.this);
                                iDoodleSelectableItem.setPosition(mDotPosition);
                                mDoodle.addItem(iDoodleSelectableItem);
                            }
                            mDoodle.setShape(DoodleShape.LINE);
                            path1.close();//封闭
                            IDoodleSelectableItem iDoodleItem = DoodlePath.toShape(mDoodle,
                                    path1,Field_AddField_UploadingPictureActivity.this);
                            iDoodleItem.setPosition(mDotPosition);
                            mDoodle.addItem(iDoodleItem);
                            mDoodle.refresh();
                            mDotPosition ++;
                        }
                    }
                }, 500);
            } else if (mDoodleCheckedId == R.id.doodle_text) {
                mDoodleTextTypeLL.setVisibility(View.VISIBLE);
                mDoodleLineTypeLL.setVisibility(View.GONE);
                mDoodle.setPen(DoodlePen.TEXT);
                mDoodle.setSize(20 * mDoodle.getUnitSize());
                mDoodleTextOrDotTypeLL.setVisibility(View.VISIBLE);
                mDoodleBitmapTypeLL.setVisibility(View.GONE);
            }
        }
    }
    private boolean isHasIDoodleItem() {
        boolean isHasIDoodleItem = false;
        if (mDoodle != null) {
            List<IDoodleItem> items = mDoodle.getAllItem();
            if (items != null && items.size() > 0) {
                for (int i = items.size() - 1; i >= 0; i--) {
                    IDoodleItem elem = items.get(i);
                    if (mDoodleCheckedId == R.id.doodle_pic) {
                        if (elem.getPen().equals(DoodlePen.BITMAP)) {
                            isHasIDoodleItem = true;
                        }
                    } else if (mDoodleCheckedId == R.id.doodle_dot) {
                        if (elem.getPen().equals(DoodlePen.DOT)) {
                            isHasIDoodleItem = true;
                        }
                    } else if (mDoodleCheckedId == R.id.doodle_text) {
                        if (elem.getPen().equals(DoodlePen.TEXT)) {
                            isHasIDoodleItem = true;
                        }
                    }
                }
            }
        }
        return isHasIDoodleItem;
    }
    private void setHasIDoodleItemStatus() {
        if (isHasIDoodleItem()) {
            if (mDoodleCheckedId == R.id.doodle_text) {
                mDoodleRevocationTextImgv.setImageResource(R.drawable.ic_next_thr_ten);
            } else if (mDoodleCheckedId == R.id.doodle_pic) {
                mDoodleRevocationBitmapImgv.setImageResource(R.drawable.ic_next_thr_ten);
            }
        } else {
            if (mDoodleCheckedId == R.id.doodle_text) {
                mDoodleRevocationTextImgv.setImageResource(R.drawable.ic_unnext_thr_ten);
            } else if (mDoodleCheckedId == R.id.doodle_pic) {
                mDoodleRevocationBitmapImgv.setImageResource(R.drawable.ic_unnext_thr_ten);
            }
        }
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mDoodleTextEditText.getWindowToken(), 0);
        }
    }
    DialogInterface.OnKeyListener onKeylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                back();
                return false;
            } else {
                return true;
            }
        }
    };
    private void clearDoodleViewData() {
        mDoodleOperatingTypeRG.clearCheck();
        if (mDoodle != null) {
            mDoodle.clear();
        }
        mDoodleBitmapTypeLL.setVisibility(View.GONE);
        mDoodleTextOrDotTypeLL.setVisibility(View.GONE);
        mDoodleCheckedId = 0;
        mDoodleRevocationTextImgv.setImageResource(R.drawable.ic_unnext_thr_ten);
        mDoodleRevocationBitmapImgv.setImageResource(R.drawable.ic_unnext_thr_ten);
        stickerLL.setVisibility(View.GONE);
    }
    private void back() {
        if (isHasIDoodleItem()) {
            new AlertDialog.Builder(Field_AddField_UploadingPictureActivity.this)
                    .setTitle(getResources().getString(R.string.module_addfield_upload_pic_abandon_save_pic))
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (mZoomPictureDialog != null && mZoomPictureDialog.isShowing()) {
                                mZoomPictureDialog.dismiss();
                                clearDoodleViewData();
                            }
                        }
                    }).show();
        } else {
            mZoomPictureDialog.dismiss();
            clearDoodleViewData();
        }
    }
}

