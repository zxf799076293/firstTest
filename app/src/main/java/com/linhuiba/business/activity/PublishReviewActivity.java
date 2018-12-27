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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldactivity.Field_GlideLoader;
import com.linhuiba.business.fieldadapter.Field_ChoosePictureGridViewAdapter;
import com.linhuiba.business.fieldbusiness.Field_FieldApi;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_MyGridView;
import com.linhuiba.business.model.ReviewFieldInfoModel;
import com.linhuiba.business.mvppresenter.PublishReviewMvpPresenter;
import com.linhuiba.business.mvpview.PublishReviewMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuipublic.config.LoginManager;
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
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/14.
 */
public class PublishReviewActivity  extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall,
        PublishReviewMvpView {
    @InjectView(R.id.review_edittext)
    EditText mreview_edittext;
    @InjectView(R.id.ToggleButton_review_Competence)
    ToggleButton mToggleButton_review_Competence;
    @InjectView(R.id.pulish_review_sv)
    ScrollView mPulishReview;

    @InjectView(R.id.scoretxt)
    TextView mscoretxt;
    @InjectView(R.id.number_of_people_scoretxt)
    TextView mnumber_of_people_scoretxt;
    @InjectView(R.id.property_cooperate_scoretxt)
    TextView mproperty_cooperate_scoretxt;
    @InjectView(R.id.user_participation_scoretxt)
    TextView muser_participation_scoretxt;
    @InjectView(R.id.complete_target_scoretxt)
    TextView mcomplete_target_scoretxt;
    @InjectView(R.id.remarks_addpicture)
    Field_MyGridView mremarks_addpicturegridview;

    @InjectView(R.id.numberofpeople_edit)
    EditText mnumberofpeople_edit;
    @InjectView(R.id.publishreview_fieldname_textview)
    TextView mpublishreview_fieldname_textview;
    @InjectView(R.id.publishreview_price_textview)
    TextView mpublishreview_price_textview;
    @InjectView(R.id.publishreview_imageview)
    ImageView mpublishreview_imageview;
    @InjectView(R.id.review_confirm_btn)
    Button mReviewBtn;
    @InjectView(R.id.publish_review_show_ll)
    LinearLayout mPublishReviewShowLL;
    private int anonymous = 0;
    private String orderid;

    private Field_ChoosePictureGridViewAdapter adapter;
    private ArrayList<String> choose_filepicture = new ArrayList<>();//之前已选择的图片list不用重新截图
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
    private ImageView[] synthesize_imglist = new ImageView[5];
    private ImageView[] number_of_people_imglist = new ImageView[5];
    private ImageView[] property_cooperate_imglist = new ImageView[5];
    private ImageView[] user_participation_imglist = new ImageView[5];
    private ImageView[] complete_target_imglist = new ImageView[5];
    private int review_fraction = 5;
    private int number_of_people_review_fraction = 5;
    private int property_cooperate_review_fraction = 5;
    private int user_participation_review_fraction = 5;
    private int complete_target_review_fraction = 5;
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
    private PublishReviewMvpPresenter mPublishReviewMvpPresenter;
    private ReviewFieldInfoModel reviewFieldInfoModel;
    private int pic_light[] = new int[5];
    private int pic_dark[] = new int[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishreview);
        ButterKnife.inject(this);
        mPublishReviewMvpPresenter = new PublishReviewMvpPresenter();
        mPublishReviewMvpPresenter.attachView(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.review_title_text));
        TitleBarUtils.showBackImg(this, true);
        Intent orderadpterintent = getIntent();
        orderid = orderadpterintent.getStringExtra("orderid");
        ToggleButtonClick();
        initView();
        showProgressDialog();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPublishReviewMvpPresenter != null) {
            mPublishReviewMvpPresenter.detachView();
        }
    }

    private void initData () {
        mPulishReview.setVisibility(View.GONE);
        mReviewBtn.setVisibility(View.GONE);
        mPublishReviewMvpPresenter.getReviewInfo(orderid);
    }
    private void ToggleButtonClick() {
        mToggleButton_review_Competence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    anonymous = 1;
                } else {
                    anonymous = 0;
                }
            }
        });

    }
    private void initView() {
        initPreviewZoomView();
        choose_filepicture.add("firstgridviewitem");
        adapter = new Field_ChoosePictureGridViewAdapter(this,this,choose_filepicture,2);
        mremarks_addpicturegridview.setAdapter(adapter);
        Constants.delete_picture_file();
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("review_img_" + i, "id", getPackageName());
            ImageView imageview = (ImageView) findViewById(id);
            synthesize_imglist[i]= (imageview);
            int number_of_people_id = getResources().getIdentifier("review_number_of_people_img_" + i, "id", getPackageName());
            ImageView number_of_people_imageview = (ImageView) findViewById(number_of_people_id);
            number_of_people_imglist[i]= (number_of_people_imageview);
            int property_cooperate_id = getResources().getIdentifier("review_property_cooperate_img_" + i, "id", getPackageName());
            ImageView property_cooperate_imageview = (ImageView) findViewById(property_cooperate_id);
            property_cooperate_imglist[i]= (property_cooperate_imageview);
            int user_participation_id = getResources().getIdentifier("review_user_participation_img_" + i, "id", getPackageName());
            ImageView user_participation_imageview = (ImageView) findViewById(user_participation_id);
            user_participation_imglist[i]= (user_participation_imageview);
            int complete_target_id = getResources().getIdentifier("review_complete_target_img_" + i, "id", getPackageName());
            ImageView complete_target_imageview = (ImageView) findViewById(complete_target_id);
            complete_target_imglist[i]= (complete_target_imageview);
        }
        final int[] synthesize_strlist = new int[5];
        final int[] number_of_people_strlist = new int[5];
        final int[] complete_target_strlist = new int[5];
        synthesize_strlist[0] = R.string.score_one_txt;
        synthesize_strlist[1] = R.string.score_two_txt;
        synthesize_strlist[2] = R.string.score_three_txt;
        synthesize_strlist[3] = R.string.score_four_txt;
        synthesize_strlist[4] = R.string.score_five_txt;
        number_of_people_strlist[0] = R.string.number_of_people_score_one_txt;
        number_of_people_strlist[1] = R.string.number_of_people_score_two_txt;
        number_of_people_strlist[2] = R.string.number_of_people_score_three_txt;
        number_of_people_strlist[3] = R.string.number_of_people_score_four_txt;
        number_of_people_strlist[4] = R.string.number_of_people_score_five_txt;
        complete_target_strlist[0] = R.string.complete_target_score_one_txt;
        complete_target_strlist[1] = R.string.complete_target_score_two_txt;
        complete_target_strlist[2] = R.string.complete_target_score_three_txt;
        complete_target_strlist[3] = R.string.complete_target_score_four_txt;
        complete_target_strlist[4] = R.string.complete_target_score_five_txt;

        for (int i = 0; i < 5; i ++) {
            pic_light[i] = R.drawable.publish_review_img;
            pic_dark[i] = R.drawable.publish_review_grey_img;
        }
        for (int i = 0; i < 5; i++) {
            final int score = i;
            synthesize_imglist[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewFieldInfoModel != null &&
                            !reviewFieldInfoModel.isIs_reviewed()) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < score + 1) {
                                synthesize_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                            } else {
                                synthesize_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                            }
                        }
                        review_fraction = score+1;
                        mscoretxt.setText(getResources().getString(synthesize_strlist[score]));
                    }
                }
            });
            number_of_people_imglist[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewFieldInfoModel != null &&
                            !reviewFieldInfoModel.isIs_reviewed()) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < score + 1) {
                                number_of_people_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                            } else {
                                number_of_people_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                            }
                        }
                        number_of_people_review_fraction = score+1;
                        mnumber_of_people_scoretxt.setText(getResources().getString(number_of_people_strlist[score]));
                    }
                }
            });

            property_cooperate_imglist[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewFieldInfoModel != null &&
                            !reviewFieldInfoModel.isIs_reviewed()) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < score + 1) {
                                property_cooperate_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                            } else {
                                property_cooperate_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                            }
                        }
                        property_cooperate_review_fraction = score+1;
                        mproperty_cooperate_scoretxt.setText(getResources().getString(synthesize_strlist[score]));
                    }
                }
            });

            user_participation_imglist[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewFieldInfoModel != null &&
                            !reviewFieldInfoModel.isIs_reviewed()) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < score + 1) {
                                user_participation_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                            } else {
                                user_participation_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                            }
                        }
                        user_participation_review_fraction = score+1;
                        muser_participation_scoretxt.setText(getResources().getString(synthesize_strlist[score]));
                    }
                }
            });
            complete_target_imglist[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewFieldInfoModel != null &&
                            !reviewFieldInfoModel.isIs_reviewed()) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < score + 1) {
                                complete_target_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                            } else {
                                complete_target_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                            }
                        }
                        complete_target_review_fraction = score+1;
                        mcomplete_target_scoretxt.setText(getResources().getString(complete_target_strlist[score]));
                    }
                }
            });
        }

    }
    @OnClick({
            R.id.review_confirm_btn
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.review_confirm_btn:
                if (LoginManager.isLogin()) {
                    if (mreview_edittext.getText().toString().trim().length() == 0) {
                        MessageUtils.showToast(getResources().getString(R.string.publishreview_review_no_message_text));
                        return;
                    }
                    uploadimg();
                } else {
                    LoginManager.getInstance().clearLoginInfo();
                    Intent orderlistintent = new Intent();
                    setResult(1, orderlistintent);
                    PublishReviewActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }
    public void showpreviewpicture(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.MyshowPreviewPicture(this);
    }

    @Override
    public void AdField_showPreviewImg(int position) {
        if (position == -1) {
            if (reviewFieldInfoModel != null &&
                    !reviewFieldInfoModel.isIs_reviewed()) {
                AndPermission.with(PublishReviewActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            }
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
            newPosition = 0;
            mImageList_size = 0;
            mConstants = new com.linhuiba.linhuifield.connector.Constants(PublishReviewActivity.this,PublishReviewActivity.this,newPosition,mImageList_size,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
            if (reviewFieldInfoModel != null &&
                    reviewFieldInfoModel.isIs_reviewed()) {
                mImgBtn.setVisibility(View.GONE);
            } else {
                mImgBtn.setVisibility(View.VISIBLE);
            }
            mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                    mZoomPictureDialog,mImageViewList,galleryviewlist,mIsRefreshZoomImageview,
                    position,false);
            mIsRefreshZoomImageview = false;
        }

    }
    private void initPreviewZoomView() {
        mZoomPictureDialog = new AlertDialog.Builder(this).create();
        mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mImgBtn = (ImageButton)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_deletebtn);
        mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        mShowPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);
        if (reviewFieldInfoModel != null &&
                reviewFieldInfoModel.isIs_reviewed()) {
            mImgBtn.setVisibility(View.GONE);
        } else {
            mImgBtn.setVisibility(View.VISIBLE);
        }
        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PublishReviewActivity.this)
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

        ImageSelector.open(PublishReviewActivity.this, imageConfig);   // 开启图片选择器
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
        if (choose_filepicture.size() > 0) {
            showProgressDialog();
            Field_FieldApi.getuptoken_comment(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
        } else {
            showProgressDialog();
            mPublishReviewMvpPresenter.confirmReview(orderid,review_fraction,
                    number_of_people_review_fraction,user_participation_review_fraction,property_cooperate_review_fraction,
                    complete_target_review_fraction,mreview_edittext.getText().toString(),
                    anonymous, null, JSON.toJSONString(addfieldimg_str,true), mnumberofpeople_edit.getText().toString());
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
                uploadManager.put(choose_filepicture.get(addfieldimgsize), null, uploadtoken,
                        upCompletionHandler, null);
            } else {
                MessageUtils.showToast(PublishReviewActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
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
                    addfieldimg_str.add(Config.qiniu_domain_comment + jsonObject.getString("key").toString());
                    if (addfieldimgsize == choose_filepicture.size() && upload == true) {
                        mPublishReviewMvpPresenter.confirmReview(orderid,review_fraction,
                                number_of_people_review_fraction,user_participation_review_fraction,property_cooperate_review_fraction,
                                complete_target_review_fraction,mreview_edittext.getText().toString(),
                                anonymous, null, JSON.toJSONString(addfieldimg_str,true), mnumberofpeople_edit.getText().toString());
                    } else if (addfieldimgsize < choose_filepicture.size() && upload == true) {
                        uploadManager.put(choose_filepicture.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(PublishReviewActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                MessageUtils.showToast(PublishReviewActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                                    }                                break;
                                case R.id.upload_picture_ll:
                                    mCustomDialog.dismiss();
                                    choosepicture();
                                    break;
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(PublishReviewActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(PublishReviewActivity.this,mCustomDialog);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Intent orderadpterintent = getIntent();
        orderid = orderadpterintent.getStringExtra("orderid");
        showProgressDialog();
        initData();
    }

    @Override
    public void onReviewInfoSuccess(ReviewFieldInfoModel mReviewFieldInfoModel) {
        reviewFieldInfoModel = mReviewFieldInfoModel;
        if (reviewFieldInfoModel != null) {
            mPulishReview.setVisibility(View.VISIBLE);
            mReviewBtn.setVisibility(View.VISIBLE);
            if (reviewFieldInfoModel.getName() != null &&
                    reviewFieldInfoModel.getName().length() > 0) {
                mpublishreview_fieldname_textview.setText(reviewFieldInfoModel.getName());
            }
            if (reviewFieldInfoModel.getPrice() != null &&
                    reviewFieldInfoModel.getPrice().length() > 0) {
                mpublishreview_price_textview.setText(Constants.getPriceUnitStr(PublishReviewActivity.this,(getResources().getString(R.string.order_listitem_price_unit_text)+Constants.getpricestring(reviewFieldInfoModel.getPrice(),0.01)),12));
            }
            if (reviewFieldInfoModel.getPic_url() != null) {
                if (reviewFieldInfoModel.getPic_url().length() != 0) {
                    Picasso.with(PublishReviewActivity.this).load(reviewFieldInfoModel.getPic_url().toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(160, 160).into(mpublishreview_imageview);
                } else {
                    Picasso.with(PublishReviewActivity.this).load(R.drawable.ic_no_pic_small).resize(160, 160).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(mpublishreview_imageview);
                }
            } else {
                Picasso.with(PublishReviewActivity.this).load(R.drawable.ic_no_pic_small).resize(160, 160).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(mpublishreview_imageview);
            }
            if (reviewFieldInfoModel.isIs_reviewed()) {
                mReviewBtn.setEnabled(false);
                mReviewBtn.setText(getResources().getString(R.string.module_publish_reviewed_btn_str));
                mReviewBtn.setBackgroundColor(getResources().getColor(R.color.gray_normal));
                com.linhuiba.linhuifield.connector.Constants.disableSubControls(mPublishReviewShowLL);
                if (reviewFieldInfoModel.getReview_images() != null &&
                        reviewFieldInfoModel.getReview_images().size() > 0) {
                    if (choose_filepicture != null) {
                        choose_filepicture.clear();
                    }
                    for (int i = 0; i < reviewFieldInfoModel.getReview_images().size(); i++) {
                        choose_filepicture.add(reviewFieldInfoModel.getReview_images().get(i));
                    }
                    if (choose_filepicture.size() < 3) {
                        choose_filepicture.add("firstgridviewitem");
                    }
                    adapter.notifyDataSetChanged();
                }
                for(int j = 0; j < 5; j ++) {
                    if (j < reviewFieldInfoModel.getScore()) {
                        synthesize_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                    } else {
                        synthesize_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                    }
                    if( j < reviewFieldInfoModel.getNumber_of_people()) {
                        number_of_people_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                    } else {
                        number_of_people_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                    }
                    if( j < reviewFieldInfoModel.getScore_of_propertymatching() + 1) {
                        property_cooperate_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                    } else {
                        property_cooperate_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                    }
                    if( j < reviewFieldInfoModel.getScore_of_userparticipation() + 1) {
                        user_participation_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                    } else {
                        user_participation_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                    }
                    if( j < reviewFieldInfoModel.getScore_of_goalcompletion() + 1) {
                        complete_target_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_light[j]));
                    } else {
                        complete_target_imglist[j].setBackgroundDrawable(getResources().getDrawable(pic_dark[j]));
                    }
                }
                if (reviewFieldInfoModel.getAnonymity() == 0) {
                    mToggleButton_review_Competence.setChecked(false);
                } else {
                    mToggleButton_review_Competence.setChecked(true);
                }
                mnumberofpeople_edit.setText(String.valueOf(reviewFieldInfoModel.getNumber_of_people()));
                mreview_edittext.setText(reviewFieldInfoModel.getContent());
            }
        }
    }
    @Override
    public void onReviewSuccess() {
        //评价成功
        Intent orderlistintent = new Intent();
        setResult(1, orderlistintent);
        PublishReviewActivity.this.finish();
    }

    @Override
    public void onReviewFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        checkAccess_new(error);
    }
}
