package com.linhuiba.linhuifield.fieldactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.connector.MyAsyncHttpClient;
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
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerIconEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;
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
    private ImageButton mImgBtn;
    private View mView;
    private TextView mShowPictureBackTV;
    private CustomDialog mCustomDialog;
    private ImageView stickerIV;
    private StickerView stickerView;
    private LinearLayout stickerLL;
    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    private int stickerImgInt;
    private int stickerWidth;
    private int stickerHeight;
    private int firstX;
    private int firstY;
    private int firstleft;
    private int firsttop;
    private int firstright;
    private int firstbottom;
    private int lastX;
    private int lastY;
    private int maxRight;
    private int maxBottom;
    private int pictireType;//1场地；2展位
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
        intent.putExtra("outputX", com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH);
        intent.putExtra("outputY", com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }

    private Bitmap decodeUriAsBitmap(Uri uri){

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;

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
    private void loadSticker() {
        Drawable drawable =
                ContextCompat.getDrawable(this, R.drawable.ic_xiaohongqi);
        stickerView.addSticker(new DrawableSticker(drawable));
    }

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
        mView = getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mImgBtn = (ImageButton)mView.findViewById(R.id.showpicture_dialog_deletebtn);
        mShowPictureSizeTV = (TextView)mView.findViewById(R.id.showpicture_dialog_sizetxt);
        mShowPictureBackTV = (TextView)mView.findViewById(R.id.showpicture_dialog_back);
        mZoomViewPage = (ViewPager)mView.findViewById(R.id.zoom_dialog_viewpage);
        stickerView = (StickerView) mView.findViewById(R.id.sticker_view);
        stickerIV = (ImageView) mView.findViewById(R.id.sticker_image_view);
        stickerLL = (LinearLayout) mView.findViewById(R.id.sticker_ll);
        final LinearLayout stickerAllLL = (LinearLayout) mView.findViewById(R.id.sticker_operate_ll);
        final ImageView stickerClickImaV = (ImageView) mView.findViewById(R.id.sticker_show_imgv);
        final ImageView stickerClickTmpImaV = (ImageView) mView.findViewById(R.id.sticker_show_tmp_imgv);
        final RelativeLayout allRL = (RelativeLayout) mView.findViewById(R.id.show_picture_all_rl);

        Button stickerCancelBtn = (Button) mView.findViewById(R.id.sticker_cancel_btn);
        Button stickerSaveBtn = (Button) mView.findViewById(R.id.sticker_save_btn);
        TextView stickerTitleTV = (TextView) mView.findViewById(R.id.sticker_title_tv);


        mImgBtn.setVisibility(View.VISIBLE);
        if (pictireType == 2) {
            stickerAllLL.setVisibility(View.VISIBLE);
            stickerClickTmpImaV.setVisibility(View.VISIBLE);
            stickerTitleTV.setVisibility(View.VISIBLE);
        }
        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Field_AddField_UploadingPictureActivity.this)
                        .setTitle(getResources().getString(R.string.delete_prompt))
                        .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                                stickerView.removeAllStickers();
                                stickerLL.setVisibility(View.GONE);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
        stickerClickTmpImaV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //得到事件的坐标
                int eventX = (int) event.getRawX();
                int eventY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //得到父视图的right/bottom
                        if(maxRight==0) {//保证只赋一次值
                            maxRight = allRL.getRight();
                            maxBottom = allRL.getBottom();
                        }
                        //第一次记录lastX/lastY
                        firstX = eventX;
                        firstY = eventY;
                        firstleft = stickerClickTmpImaV.getLeft();
                        firsttop = stickerClickTmpImaV.getTop();
                        firstright = stickerClickTmpImaV.getRight();
                        firstbottom = stickerClickTmpImaV.getBottom();
                        lastX =eventX;
                        lastY = eventY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算事件的偏移
                        int dx = eventX-lastX;
                        int dy = eventY-lastY;
                        //根据事件的偏移来移动imageView
                        int left = stickerClickTmpImaV.getLeft()+dx;
                        int top = stickerClickTmpImaV.getTop()+dy;
                        int right = stickerClickTmpImaV.getRight()+dx;
                        int bottom = stickerClickTmpImaV.getBottom()+dy;
                        //限制left >=0
                        if(left<0) {
                            right += -left;
                            left = 0;
                        }
                        //限制top
                        if(top<0) {
                            bottom += -top;
                            top = 0;
                        }
                        //限制right <=maxRight
                        if(right>maxRight) {
                            left -= right-maxRight;
                            right = maxRight;
                        }
                        //限制bottom <=maxBottom
                        if(bottom>maxBottom) {
                            top -= bottom-maxBottom;
                            bottom = maxBottom;
                        }
                        stickerClickTmpImaV.layout(left, top, right, bottom);
                        //再次记录lastX/lastY
                        lastX = eventX;
                        lastY = eventY;
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((firstY - lastY > 470 && firstY - lastY < 1070)
                                || ((firstX - lastX < 3 && firstX - lastX > -3) ||
                                (firstY - lastY > -3 && firstY - lastY < 3))) {
                            getPictureBitmap();
                        }
                        stickerClickTmpImaV.layout(firstleft, firsttop, firstright, firstbottom);
                        break;
                    default:
                        break;
                }
                return true;//所有的motionEvent都交给imageView处理
            }
        });
        stickerCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stickerLL.getVisibility() == View.VISIBLE) {
                    stickerView.removeAllStickers();
                    stickerLL.setVisibility(View.GONE);
                }
            }
        });
        stickerSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2018/3/19 保存贴纸的操作
                if (stickerLL.getVisibility() == View.VISIBLE) {
                    String time = "" + System.currentTimeMillis();
                    File file = Constants.getDCIMFile(time + ".jpg");
                    String url = Constants.picture_file_str + time + ".jpg";
                    if (file != null) {
                        stickerView.save(file);
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
                        stickerView.removeAllStickers();
                        stickerLL.setVisibility(View.GONE);
                    } else {
                        BaseMessageUtils.showToast("没有文件");
                    }
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
                                        Field_AddField_UploadingPictureActivity.this.startActivityForResult(intent, 2);
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
        stickerWidth = mDisplayMetrics.widthPixels;
        stickerHeight = stickerWidth * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        LinearLayout.LayoutParams paramdata = new LinearLayout.LayoutParams(stickerWidth, stickerHeight);
        stickerView.setLayoutParams(paramdata);
        if (type == 1) {
            Glide.with(Field_AddField_UploadingPictureActivity.this).load(Constants.picture_file_str  +
                    String.valueOf(stickerImgInt - 1) + "sticker.jpg").placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).override(stickerWidth, stickerHeight).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(stickerIV);
        } else if (type == 2) {
            Glide.with(Field_AddField_UploadingPictureActivity.this).load(pathtmp.get(newPosition)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).override(stickerWidth, stickerHeight).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(stickerIV);
        }
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(Field_AddField_UploadingPictureActivity.this,
                R.drawable.ic_close),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new StickerDeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(Field_AddField_UploadingPictureActivity.this,
                R.drawable.ic_fangdasuoxiao),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon));
        stickerView.setLocked(false);//固定贴纸不能操作
        stickerView.setConstrained(true);
        loadSticker();
    }
    private class StickerDeleteIconEvent  implements StickerIconEvent {

        @Override
        public void onActionDown(StickerView stickerView, MotionEvent event) {

        }

        @Override
        public void onActionMove(StickerView stickerView, MotionEvent event) {

        }

        @Override
        public void onActionUp(StickerView stickerView, MotionEvent event) {
            stickerView.removeCurrentSticker();
            if (stickerView.getStickerCount() == 0) {
                stickerLL.setVisibility(View.GONE);
            }
        }
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
            loadSticker();
        }
    }
}

