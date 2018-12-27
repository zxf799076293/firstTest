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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldactivity.Field_GlideLoader;
import com.linhuiba.business.fieldadapter.Field_ChoosePictureGridViewAdapter;
import com.linhuiba.business.fieldbusiness.Field_FieldApi;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_MyGridView;
import com.linhuiba.business.fieldview.Field_NewGalleryView;
import com.linhuiba.business.model.TrackModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
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

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TrackRemarksActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall {
    @InjectView(R.id.remarks_addpicture)
    Field_MyGridView mremarks_addpicturegridview;
    @InjectView(R.id.remarks_numberofpeople)  TextView mremarks_numberofpeople;
    @InjectView(R.id.remarks_sales)  TextView mremarks_sales;
    @InjectView(R.id.remarks_focus)  TextView mremarks_focus;
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
    private int tracktype;
    private TrackModel trackModel;
    private int editorposition;
    private ArrayList<HashMap<String, String>> Pic_urlMaplist = new ArrayList<>();//修改之前的图片map
    private ArrayList<String> Pic_urllist = new ArrayList<>();//修改之前的图片urllist
    private Dialog mZoomPictureDialog;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private boolean mIsRefreshZoomImageview = true;
    private TextView mShowPictureSizeTV;
    private ViewPager mZoomViewPage;
    private com.linhuiba.linhuifield.connector.Constants mConstants;
    private ImageButton mImgBtn;
    private View mView;
    private TextView mShowPictureBackTV;
    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprintremarks);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.footprint_remark_txt));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackRemarksActivity.this.finish();
            }
        });
        initView();
    }
    private void initView() {
        initPreviewZoomView();
        Intent trackintent = getIntent();
        if (trackintent.getExtras()!= null) {
            tracktype = trackintent.getExtras().getInt("trackitemtype");
            trackModel = (TrackModel)trackintent.getSerializableExtra("trackModel");
            editorposition = trackintent.getExtras().getInt("editorposition");
        }
        if (tracktype == 1) {
            if (trackModel.getNumber_of_people() != null) {
                mremarks_numberofpeople.setText(trackModel.getNumber_of_people());
            }
            if (trackModel.getSale() != null) {
                mremarks_sales.setText(trackModel.getSale());
            }

            if (trackModel.getNumber_of_fans() != null) {
                mremarks_focus.setText(trackModel.getNumber_of_fans());
            }
            if (trackModel.getPic_url() != null) {
                Pic_urlMaplist.addAll(trackModel.getPic_url());
                for (int i = 0; i < trackModel.getPic_url().size(); i++) {
                    choose_filepicture.add(trackModel.getPic_url().get(i).get("url"));
                    Pic_urllist.add(trackModel.getPic_url().get(i).get("url"));
                }
            }
        }

        if (choose_filepicture.size() < 3) {
            choose_filepicture.add("firstgridviewitem");
        }
        adapter = new Field_ChoosePictureGridViewAdapter(this,this,choose_filepicture,1);
        mremarks_addpicturegridview.setAdapter(adapter);
        Constants.delete_picture_file();
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
                new AlertDialog.Builder(TrackRemarksActivity.this)
                        .setTitle(getResources().getString(R.string.delete_prompt))
                        .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mImageViewList.remove(newPosition);
                                galleryviewlist.remove(newPosition);
                                choose_filepicture.remove(newPosition);
                                int addpictureimg_num = 0;
                                for (int i = 0; i < choose_filepicture.size(); i++) {
                                    if (choose_filepicture.get(i).equals("firstgridviewitem")) {
                                        addpictureimg_num ++;
                                    }
                                }
                                if (addpictureimg_num == 0) {
                                    choose_filepicture.add("firstgridviewitem");
                                }
                                adapter.notifyDataSetChanged();

                                if (galleryviewlist != null) {
                                    if (galleryviewlist.size() != 0) {
                                        if (galleryviewlist.size() > newPosition + 1 || galleryviewlist.size() == newPosition + 1) {
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
    }

    @OnClick({
            R.id.footprintremark_savebtn
    })
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.footprintremark_savebtn:
                uploadimg();
                break;
            default:
                break;
        }

    }
    @Override
    public void AdField_showPreviewImg(int position) {
        if (position == -1) {
            AndPermission.with(TrackRemarksActivity.this)
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
            mConstants = new com.linhuiba.linhuifield.connector.Constants(TrackRemarksActivity.this,TrackRemarksActivity.this,newPosition,mImageList_size,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
            mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                    mZoomPictureDialog,mImageViewList,galleryviewlist,mIsRefreshZoomImageview,
                    position,false);
            mIsRefreshZoomImageview = false;
        }
    }
    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
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
                .mutiSelectMaxSize(3)
                        // 已选择的图片路径
                .pathList(choose_filepicture)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(Constants.PhotoRequestCode)
                .build();

        ImageSelector.open(TrackRemarksActivity.this, imageConfig);   // 开启图片选择器
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
                if (choose_filepicture.size() < 3) {
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
                String time = "" + System.currentTimeMillis();
                tailorsiae_tmp = 0;
                if (Constants.SDCardState()) {
                    photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                    photosavafile = new File(photosavafilestr);
                    startPhotoZoom(Uri.fromFile(new File(interceptionpathlist.get(tailorsiae_tmp))),Uri.fromFile(photosavafile));
                }
            }
        } else if (requestCode == Constants.PhotoRequestCode) {
            if (choose_filepicture!= null) {
                choose_filepicture.clear();
            }
            choose_filepicture.addAll(choose_filepicture_tmp);
            if (choose_filepicture.size() < 3) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter.notifyDataSetChanged();
        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                booleancamera = true;
                startPhotoZoom(Uri.fromFile(photosavafile),Uri.fromFile(photosavafile));
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
                    for (int i = 0; i < choose_filepicture.size(); i++) {
                        if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                            choose_filepicture.remove(i);
                        }
                    }
                    choose_filepicture.add(photosavafilestr);
                    if (booleancamera == false) {
                        if (tailorsiae_tmp < interceptionpathlist.size()-1) {
                            tailorsiae_tmp = tailorsiae_tmp +1;
                            String time = "" + System.currentTimeMillis();
                            photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                            photosavafile = new File(photosavafilestr);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startPhotoZoom(Uri.fromFile(new File(interceptionpathlist.get(tailorsiae_tmp))), Uri.fromFile(photosavafile));
                                }
                            }, 700);
                        } else if (tailorsiae_tmp == interceptionpathlist.size()-1){
                            if (choose_filepicture.size() < 3) {
                                choose_filepicture.add("firstgridviewitem");
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        if (choose_filepicture.size() < 3) {
                            choose_filepicture.add("firstgridviewitem");
                        }
                        adapter.notifyDataSetChanged();
                        booleancamera = false;
                    }
                } else {
                    for (int i = 0; i < choose_filepicture.size(); i++) {
                        if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                            choose_filepicture.remove(i);
                        }
                    }
                    if (booleancamera == false) {
                        if (tailorsiae_tmp < interceptionpathlist.size()-1) {
                            tailorsiae_tmp = tailorsiae_tmp +1;
                            String time = "" + System.currentTimeMillis();
                            photosavafilestr = Constants.picture_file_str + "addfield" + time + "_" + (tailorsiae_tmp) + ".jpg";
                            photosavafile = new File(photosavafilestr);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    startPhotoZoom(Uri.fromFile(new File(interceptionpathlist.get(tailorsiae_tmp))), Uri.fromFile(photosavafile));
                                }
                            }, 300);
                        } else if (tailorsiae_tmp == interceptionpathlist.size()-1){
                            if (choose_filepicture.size() < 3) {
                                choose_filepicture.add("firstgridviewitem");
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        if (choose_filepicture.size() < 3) {
                            choose_filepicture.add("firstgridviewitem");
                        }
                        adapter.notifyDataSetChanged();
                        booleancamera = false;
                    }
                }
                mIsRefreshZoomImageview = true;
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
        if (trackModel != null) {
            if (trackModel.getPic_url() != null) {
                for (int i = 0; i < trackModel.getPic_url().size(); i++) {
                    if (choose_filepicture_editor.contains(trackModel.getPic_url().get(i).get("url"))) {
                        choose_filepicture_editor.remove(trackModel.getPic_url().get(i).get("url"));
                        choose_filepicture_url.add(trackModel.getPic_url().get(i).get("url"));
                    }
                }
            }
        }
        if (choose_filepicture_editor.size() > 0) {
            showProgressDialog();
            Field_FieldApi.getuptoken_comment(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
        } else {
            if (addfieldimg_str != null) {
                addfieldimg_str.clear();
            }
            for (int i = 0; i < choose_filepicture_url.size(); i++) {
                addfieldimg_str.add(choose_filepicture_url.get(i));
            }
            if (tracktype == 0) {
                showProgressDialog();
                FieldApi.addtractremarks(MyAsyncHttpClient.MyAsyncHttpClient(), TrackHandler, trackModel.getField_order_item_id(), mremarks_numberofpeople.getText().toString(),
                        mremarks_sales.getText().toString(), mremarks_focus.getText().toString(), JSON.parseArray(JSON.toJSONString(addfieldimg_str,true)));
            } else {
                showProgressDialog();
                FieldApi.editortractremarks(MyAsyncHttpClient.MyAsyncHttpClient(), TrackHandler, trackModel.getTrack_id(),trackModel.getField_order_item_id(), mremarks_numberofpeople.getText().toString(),
                        mremarks_sales.getText().toString(), mremarks_focus.getText().toString(), JSON.parseArray(JSON.toJSONString(addfieldimg_str, true)));
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
                MessageUtils.showToast(TrackRemarksActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
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
    private UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, org.json.JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                try {
                    addfieldimgsize ++;
                    addfieldimg_str.add(Config.qiniu_domain_comment + jsonObject.getString("key").toString());
                    if (addfieldimgsize == choose_filepicture_editor.size() && upload == true) {
                        //上传备注所需信息
                        for (int i = 0; i < choose_filepicture_url.size(); i++) {
                            addfieldimg_str.add(choose_filepicture_url.get(i));
                        }
                        if (tracktype == 0) {
                            FieldApi.addtractremarks(MyAsyncHttpClient.MyAsyncHttpClient(), TrackHandler, trackModel.getField_order_item_id(), mremarks_numberofpeople.getText().toString(),
                                    mremarks_sales.getText().toString(), mremarks_focus.getText().toString(), JSON.parseArray(JSON.toJSONString(addfieldimg_str,true)));
                        } else {
                            FieldApi.editortractremarks(MyAsyncHttpClient.MyAsyncHttpClient(), TrackHandler, trackModel.getTrack_id(),trackModel.getField_order_item_id(), mremarks_numberofpeople.getText().toString(),
                                    mremarks_sales.getText().toString(), mremarks_focus.getText().toString(), JSON.parseArray(JSON.toJSONString(addfieldimg_str, true)));
                        }

                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(TrackRemarksActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                MessageUtils.showToast(TrackRemarksActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

        }
    };
    private LinhuiAsyncHttpResponseHandler TrackHandler = new LinhuiAsyncHttpResponseHandler(TrackModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(response.msg);
            TrackModel mtrackModel = (TrackModel)data;
            Intent it = new Intent();
            it.putExtra("trackmodel",(Serializable)mtrackModel);
            it.putExtra("editorposition", editorposition);
            setResult(1,it);
            TrackRemarksActivity.this.finish();
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
                    CustomDialog.Builder builder = new CustomDialog.Builder(TrackRemarksActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(TrackRemarksActivity.this,mCustomDialog);
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
