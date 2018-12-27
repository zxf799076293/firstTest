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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fieldactivity.Field_GlideLoader;
import com.linhuiba.business.fieldadapter.Field_ChoosePictureGridViewAdapter;
import com.linhuiba.business.fieldbusiness.Field_FieldApi;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_MyGridView;
import com.linhuiba.business.fieldview.Field_NewGalleryView;
import com.linhuiba.business.model.CompanyConfigInfoModel;
import com.linhuiba.business.model.CompanyModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.WheelView;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
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
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MyselfInfo_CompanyActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall {
    @InjectView(R.id.addfield_gallery)
    Field_NewGalleryView mCoverFlow;

    @InjectView(R.id.remarks_addpicture)
    Field_MyGridView mremarks_addpicturegridview;
    @InjectView(R.id.food_safety_license_addpicture_gridview)
    Field_MyGridView mfood_safety_license_addpicture_gridview;
    @InjectView(R.id.productname_textview)
    EditText mproductname_textview;
    @InjectView(R.id.industryname)
    TextView mindustryname;
    @InjectView(R.id.promotion_way_text)
    TextView mpromotion_way_text;
    @InjectView(R.id.license_addpicture_gridview)
    Field_MyGridView mlicense_addpicture_gridview;
    @InjectView(R.id.company_user_consumption_level_layout)
    RelativeLayout mcompany_user_consumption_level_layout;
    @InjectView(R.id.company_user_age_group_layout)
    RelativeLayout mcompany_user_age_group_layout;
    @InjectView(R.id.myselfinfo_demand_area_edittext)
    EditText mmyselfinfo_demand_area_edittext;
    @InjectView(R.id.companyinfo_frequency_text)
    TextView mcompanyinfo_frequency_text;
    @InjectView(R.id.myselfinfo_acceptable_minimum_price_edittext)
    EditText mmyselfinfo_acceptable_minimum_price_edittext;
    @InjectView(R.id.myselfinfo_acceptable_maximum_price_edittext)
    EditText mmyselfinfo_acceptable_maximum_price_edittext;
    @InjectView(R.id.myselfinfo_feature_description_edittext)
    EditText mmyselfinfo_feature_description_edittext;
    @InjectView(R.id.food_safety_license_addpicture_layout)
    LinearLayout mfood_safety_license_addpicture_layout;
    private Field_ChoosePictureGridViewAdapter adapter;//地推照片adapter
    private Field_ChoosePictureGridViewAdapter license_adapter;//营业执照adapter
    private Field_ChoosePictureGridViewAdapter food_safety_license_adapter;//营业执照adapter
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
    private int mImageList_size;//预览图片listsize
    private ArrayList<String> addfieldimg_str = new ArrayList<>();//七牛上传的list
    private ArrayList<String> food_safety_license_addfieldimg_str = new ArrayList<>();//食品安全凭证七牛上传的list
    private UploadManager uploadManager = new UploadManager();
    private String uploadtoken;//地推照片七牛上传的凭证
    private String license_uploadtoken;//营业执照七牛上传的凭证
    private int addfieldimgsize;//要上传图片的position
    private boolean upload = true;//上传到七牛的图片成功的标志
    private int food_safety_license_addfieldimgsize;//食品安全凭证要上传图片的position
    private boolean food_safety_license_upload = true;//食品安全凭证上传到七牛的图片成功的标志
    private int industry_id = -1;//行业id
    private CompanyModel companyModel;//公司信息的model
    private Dialog newdialog;//行业选择界面
    private String wheelview_select_str;//行业选择的item的str
    private ArrayList<String> license_choose_filepicture = new ArrayList<>();//营业执照选取之前的list
    private ArrayList<String> license_galleryviewlist = new ArrayList<>();//营业执照预览图片的list
    private ArrayList<String> license_choose_filepicture_tmp = new ArrayList<>();//营业执照之前已选择图片的过渡list(用来判断)
    private ArrayList<String> license_interceptionpathlist = new ArrayList<>();//营业执照需要截取的图片urllist
    private String license_upload_img_url = "";//营业执照上传图片url
    private int license_newPosition;//营业执照预览删除图片的position
    private int license_mImageList_size;//营业执照预览图片listsize
    public int upload_picture_type;//选择照片的功能 2：营业执照，1：地推照片 3：食品安全
    private final int upload_license_img_type = 2;//营业执照选择照片功能
    private final int upload_field_img_type = 1;//地推照片选择照片功能
    private final int upload_food_safety_license_type = 3;//食品安全许可证选择照片功能
    private ArrayList<String> upload_food_safety_license_filepicture = new ArrayList<>();//食品安全许可证选取之前的list
    private ArrayList<String> upload_food_safety_license_galleryviewlist = new ArrayList<>();//食品安全许可证预览图片的list
    private ArrayList<String> upload_food_safety_license_choose_filepicture_tmp = new ArrayList<>();//食品安全许可证之前已选择图片的过渡list(用来判断)
    private ArrayList<String> upload_food_safety_license_interceptionpathlist = new ArrayList<>();//食品安全许可证需要截取的图片urllist
    private ArrayList<String> upload_food_safety_license_filepicture_editor = new ArrayList<>();//食品安全许上传修改的图片的file
    private ArrayList<String> upload_food_safety_license_filepicture_url = new ArrayList<>();//食品安全许上传修改的图片的file以外的网络url
    private String upload_food_safety_license_upload_img_url = "";//食品安全许可证上传图片url
    private int upload_food_safety_license_newPosition;//食品安全许可证预览删除图片的position
    private int upload_food_safety_license_mImageList_size;//食品安全许可证预览图片listsize

    private HashMap<String,Boolean> user_consumption_level_clickmap = new HashMap<>();
    private HashMap<Integer,String> user_consumption_level_viewsetidmap = new HashMap<>();
    private TextView[] user_consumption_level_textViewlists;
    private HashMap<String,Integer> user_consumption_level_contentMap = new HashMap<>();
    private HashMap<String,Boolean> user_age_level_clickmap = new HashMap<>();
    private HashMap<Integer,String> user_age_level_viewsetidmap = new HashMap<>();
    private TextView[] user_age_level_textViewlists;
    private HashMap<String,Integer> user_age_level_contentMap = new HashMap<>();
    private Dialog zoom_picture_dialog;
    private List<ImageView> imageList;
    private CompanyConfigInfoModel companyConfigInfoModel;
    private ArrayList<Integer> consumption_level_id = new ArrayList<>();
    private ArrayList<Integer> age_level_id = new ArrayList<>();
    private int wheelview_select_int = -1;
    private int pushing_frequency_level_id = -1;
    private Dialog pushing_frequency_level_dialog;//行业选择界面
    private boolean click_save_btn = true;
    private int spread_way_id = -1;
    private CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myselfinfo_company);
        ButterKnife.inject(this);
        initview();
        initdata();
    }
    private void initdata() {
        showProgressDialog();
        UserApi.getcompany_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),CompanyinfoHandler);
    }
    private void initview() {
        Constants.delete_picture_file();
        Constants.textchangelistener(mmyselfinfo_demand_area_edittext);
        Constants.textchangelistener(mmyselfinfo_acceptable_minimum_price_edittext);
        Constants.textchangelistener(mmyselfinfo_acceptable_maximum_price_edittext);
        addTextChangedListener(mproductname_textview);
        addTextChangedListener(mmyselfinfo_feature_description_edittext);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_intention_str));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextTextView(this, getResources().getString(R.string.mTxt_save_fieldinfo), 15,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mproductname_textview.getText().toString().length() == 0) {
                            MessageUtils.showToast(getResources().getString(R.string.applyforrelease_productname_hinttxt));
                            return;
                        }
                        if (industry_id == -1) {
                            MessageUtils.showToast(getResources().getString(R.string.applyforrelease_industry_hinttxt));
                            return;
                        }
                        if (spread_way_id == -1) {
                            MessageUtils.showToast(getResources().getString(R.string.myselfinfo_promotion_way_hint_str));
                            return;
                        }
                        if (mmyselfinfo_acceptable_minimum_price_edittext.getText().toString().length() > 0 &&
                                mmyselfinfo_acceptable_maximum_price_edittext.getText().toString().length() > 0) {
                            if (Double.parseDouble(mmyselfinfo_acceptable_minimum_price_edittext.getText().toString()) >
                                    Double.parseDouble(mmyselfinfo_acceptable_maximum_price_edittext.getText().toString())) {
                                MessageUtils.showToast(getResources().getString(R.string.myselfinfo_company_acceptable_price_error_text));
                                return;
                            }
                        }
                        if (LoginManager.isLogin() && click_save_btn == true) {
                            click_save_btn = false;
                            uploadimg();
                        } else {
                            LoginManager.getInstance().clearLoginInfo();
                            MyselfInfo_CompanyActivity.this.finish();
                        }
                    }
                });
    }
    private LinhuiAsyncHttpResponseHandler company_config_infoHandler = new LinhuiAsyncHttpResponseHandler(CompanyConfigInfoModel.class,false) {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            companyConfigInfoModel = (CompanyConfigInfoModel) data;
            if (companyConfigInfoModel != null) {
                if (companyConfigInfoModel.getConsumptionlevel() != null && companyConfigInfoModel.getConsumptionlevel().size() > 0) {
                    add_layout(companyConfigInfoModel.getConsumptionlevel(),mcompany_user_consumption_level_layout,
                            user_consumption_level_contentMap,user_consumption_level_viewsetidmap,user_consumption_level_clickmap,1);
                }
                if (companyConfigInfoModel.getAgelevel() != null && companyConfigInfoModel.getAgelevel().size() > 0) {
                    add_layout(companyConfigInfoModel.getAgelevel(),mcompany_user_age_group_layout,
                            user_age_level_contentMap,user_age_level_viewsetidmap,user_age_level_clickmap,2);
                }

                if (companyConfigInfoModel.getCatering_industry_id() != null && companyConfigInfoModel.getCatering_industry_id().size() > 0
                        && industry_id > -1 && companyConfigInfoModel.getCatering_industry_id().contains(industry_id)) {
                    mfood_safety_license_addpicture_layout.setVisibility(View.VISIBLE);
                } else {
                    mfood_safety_license_addpicture_layout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private LinhuiAsyncHttpResponseHandler CompanyinfoHandler = new LinhuiAsyncHttpResponseHandler(CompanyModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            companyModel = (CompanyModel)data;
            if (companyModel.getProduct()!= null) {
                mproductname_textview.setText(companyModel.getProduct().trim());
                LoginManager.setProduct(companyModel.getProduct().trim());
            }  else {
                LoginManager.setProduct("");
            }
            if (companyModel.getIndustry_name()!= null) {
                mindustryname.setText(companyModel.getIndustry_name().trim());
            }
            if (companyModel.getIndustry_id() != null && companyModel.getIndustry_id().length() > 0) {
                industry_id = Integer.parseInt(companyModel.getIndustry_id().trim());
            }
            if (companyModel.getSpread_way() != null && companyModel.getSpread_way().getId() > 0 &&
                    companyModel.getSpread_way().getSpread_way() != null) {
                spread_way_id = companyModel.getSpread_way().getId();
                mpromotion_way_text.setText(companyModel.getSpread_way().getSpread_way().trim());
            }
            if (companyModel.getImages()!= null) {
                if (companyModel.getImages().size() > 0) {
                    choose_filepicture.addAll(companyModel.getImages());
                }
            }
            if (choose_filepicture.size() < 3) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, choose_filepicture, 3,upload_field_img_type);
            mremarks_addpicturegridview.setAdapter(adapter);
            if (companyModel.getCert_url() != null && companyModel.getCert_url().length() > 0) {
                license_choose_filepicture.add(companyModel.getCert_url().trim());
                license_upload_img_url = companyModel.getCert_url().trim();
            }
            if (license_choose_filepicture.size() < 1) {
                license_choose_filepicture.add("firstgridviewitem");
            }
            license_adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, license_choose_filepicture, 3,upload_license_img_type);
            mlicense_addpicture_gridview.setAdapter(license_adapter);
            //食品安全许可证图片
            if (companyModel.getFood_safety_license()!= null) {
                if (companyModel.getFood_safety_license().size() > 0) {
                    upload_food_safety_license_filepicture.addAll(companyModel.getFood_safety_license());
                }
            }
            if (upload_food_safety_license_filepicture.size() < 3) {
                upload_food_safety_license_filepicture.add("firstgridviewitem");
            }
            food_safety_license_adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, upload_food_safety_license_filepicture, 3,upload_food_safety_license_type);
            mfood_safety_license_addpicture_gridview.setAdapter(food_safety_license_adapter);
            if (companyModel.getDemand_area() != null) {
                mmyselfinfo_demand_area_edittext.setText(String.valueOf(companyModel.getDemand_area()));
            }
            if (companyModel.getPushing_frequency_level() != null
                    && companyModel.getPushing_frequency_level().getId() > 0 &&
                    companyModel.getPushing_frequency_level().getDisplay_name() != null) {
                mcompanyinfo_frequency_text.setText(companyModel.getPushing_frequency_level().getDisplay_name());
                pushing_frequency_level_id = companyModel.getPushing_frequency_level().getId();
            }
            if (companyModel.getAcceptable_minimum_price() != null) {
                mmyselfinfo_acceptable_minimum_price_edittext.setText(Constants.getdoublepricestring(companyModel.getAcceptable_minimum_price(),0.01));
            }
            if (companyModel.getAcceptable_maximum_price() != null) {
                mmyselfinfo_acceptable_maximum_price_edittext.setText(Constants.getdoublepricestring(companyModel.getAcceptable_maximum_price(),0.01));
            }
            if (companyModel.getConsumption_level() != null && companyModel.getConsumption_level().size() > 0) {
                for (int i = 0; i < companyModel.getConsumption_level().size(); i++) {
                    if (companyModel.getConsumption_level().get(i).getId() > 0) {
                        consumption_level_id.add(companyModel.getConsumption_level().get(i).getId());
                    }
                }
            }
            if (companyModel.getAge_level() != null && companyModel.getAge_level().size() > 0) {
                for (int i = 0; i < companyModel.getAge_level().size(); i++) {
                    if (companyModel.getAge_level().get(i).getId() > 0) {
                        age_level_id.add(companyModel.getAge_level().get(i).getId());
                    }
                }
            }
            if (companyModel.getFeature_description() != null &&
                    companyModel.getFeature_description().length() > 0) {
                mmyselfinfo_feature_description_edittext.setText(companyModel.getFeature_description());
            }
            UserApi.getcompany_config_info(MyAsyncHttpClient.MyAsyncHttpClient(),company_config_infoHandler);
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (choose_filepicture.size() < 3) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, choose_filepicture, 3,upload_field_img_type);
            mremarks_addpicturegridview.setAdapter(adapter);

            if (license_choose_filepicture.size() < 3) {
                license_choose_filepicture.add("firstgridviewitem");
            }
            license_adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, license_choose_filepicture, 3,upload_license_img_type);
            mlicense_addpicture_gridview.setAdapter(license_adapter);
            if (upload_food_safety_license_filepicture.size() < 3) {
                upload_food_safety_license_filepicture.add("firstgridviewitem");
            }
            food_safety_license_adapter = new Field_ChoosePictureGridViewAdapter(MyselfInfo_CompanyActivity.this, MyselfInfo_CompanyActivity.this, upload_food_safety_license_filepicture, 3,upload_food_safety_license_type);
            mfood_safety_license_addpicture_gridview.setAdapter(food_safety_license_adapter);
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    @OnClick({
            R.id.industry_choose_layout,
            R.id.promotion_way_layout,
            R.id.myselfinfo_company_frequency_layout,
            R.id.company_user_age_group_layout,
    })
    public void publishreview(View view) {
        switch (view.getId()) {
            case R.id.industry_choose_layout:
                Intent industry_choose_intent = new Intent(MyselfInfo_CompanyActivity.this,Industry_ChooseActivity.class);
                startActivityForResult(industry_choose_intent, 5);
                break;
            case R.id.promotion_way_layout:
                if (companyConfigInfoModel.getSpreadway() != null &&
                        companyConfigInfoModel.getSpreadway().size() > 0) {
                    show_pushfrequency_dialog(mpromotion_way_text,companyConfigInfoModel.getSpreadway(),3);
                }
                break;
            case R.id.myselfinfo_company_frequency_layout:
                if (companyConfigInfoModel.getPushfrequency() != null &&
                        companyConfigInfoModel.getPushfrequency().size() > 0) {
                    show_pushfrequency_dialog(mcompanyinfo_frequency_text,companyConfigInfoModel.getPushfrequency(),1);
                }
                break;
            case R.id.company_user_age_group_layout:
                if (companyConfigInfoModel.getAgelevel() != null &&
                        companyConfigInfoModel.getAgelevel().size() > 0) {

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
                AndPermission.with(MyselfInfo_CompanyActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            } else {
                if (upload_picture_type == upload_field_img_type) {
                    if (galleryviewlist != null) {
                        galleryviewlist.clear();
                    }
                    galleryviewlist.addAll(choose_filepicture);
                    for (int i = 0; i < galleryviewlist.size(); i++) {
                        if (galleryviewlist.get(i).toString().equals("firstgridviewitem")) {
                            galleryviewlist.remove(i);
                        }
                    }
                    show_zoom_picture_dialog(position,galleryviewlist);
                } else if (upload_picture_type == upload_license_img_type) {
                    if (license_galleryviewlist != null) {
                        license_galleryviewlist.clear();
                    }
                    license_galleryviewlist .addAll(license_choose_filepicture);
                    for (int i = 0; i < license_galleryviewlist.size(); i++) {
                        if (license_galleryviewlist.get(i).toString().equals("firstgridviewitem")) {
                            license_galleryviewlist.remove(i);
                        }
                    }
                    license_newPosition = 0;
                    license_mImageList_size = 0;
                    show_zoom_picture_dialog(position,license_galleryviewlist);
                } else if (upload_picture_type == upload_food_safety_license_type) {
                    if (upload_food_safety_license_galleryviewlist != null) {
                        upload_food_safety_license_galleryviewlist.clear();
                    }
                    upload_food_safety_license_galleryviewlist.addAll(upload_food_safety_license_filepicture);
                    for (int i = 0; i < upload_food_safety_license_galleryviewlist.size(); i++) {
                        if (upload_food_safety_license_galleryviewlist.get(i).toString().equals("firstgridviewitem")) {
                            upload_food_safety_license_galleryviewlist.remove(i);
                        }
                    }
                    upload_food_safety_license_newPosition = 0;
                    upload_food_safety_license_mImageList_size = 0;
                    show_zoom_picture_dialog(position,upload_food_safety_license_galleryviewlist);
                }
            }
    }
    private void choosepicture(ArrayList<String> choose_filepicture,ArrayList<String> choose_filepicture_tmp,
                               int mutiSelectMaxSize) {
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
                .mutiSelectMaxSize(mutiSelectMaxSize)
                        // 已选择的图片路径
                .pathList(choose_filepicture)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                        // 开启拍照功能 （默认开启）
//                .showCamera()
                .requestCode(Constants.PhotoRequestCode)
                .build();

        ImageSelector.open(MyselfInfo_CompanyActivity.this, imageConfig);   // 开启图片选择器
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PhotoRequestCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            ArrayList<String> pathList_tmp = new ArrayList<>();
            if (upload_picture_type == upload_license_img_type) {
                pathList_tmp.addAll(license_choose_filepicture_tmp);
                booleancamera = false;
                if (license_interceptionpathlist != null) {
                    license_interceptionpathlist.clear();
                }
                if (booleancamera == false) {
                    if (license_choose_filepicture != null) {
                        license_choose_filepicture.clear();
                    }
                }
                license_choose_filepicture.addAll(getIntersection(pathList_tmp, pathList));
                if (compare(license_choose_filepicture,pathList)) {
                    if (license_choose_filepicture.size() < 1) {
                        license_choose_filepicture.add("firstgridviewitem");
                    }
                    license_adapter.notifyDataSetChanged();
                } else {
                    if (license_choose_filepicture != null) {
                        if (license_choose_filepicture.size() > 0) {
                            license_interceptionpathlist.addAll(getAnd_set(pathList,license_choose_filepicture));
                        } else {
                            license_interceptionpathlist.addAll(pathList);
                        }
                    } else {
                        license_interceptionpathlist.addAll(pathList);
                    }
                    for (int i = 0; i < license_choose_filepicture.size(); i++) {
                        if (license_choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                            license_choose_filepicture.remove(i);
                        }
                    }
                    for (int i = 0; i < license_interceptionpathlist.size(); i++) {
                        license_choose_filepicture.add(license_interceptionpathlist.get(i));
                    }
                    if (license_choose_filepicture.size() < 1) {
                        license_choose_filepicture.add("firstgridviewitem");
                    }
                    license_adapter.notifyDataSetChanged();
                }

            } else if (upload_picture_type == upload_field_img_type) {
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
                    for (int i = 0; i < interceptionpathlist.size(); i++) {
                        choose_filepicture.add(interceptionpathlist.get(i));
                    }
                    if (choose_filepicture.size() < 3) {
                        choose_filepicture.add("firstgridviewitem");
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (upload_picture_type == upload_food_safety_license_type) {
                pathList_tmp.addAll(upload_food_safety_license_choose_filepicture_tmp);
                booleancamera = false;
                if (upload_food_safety_license_interceptionpathlist != null) {
                    upload_food_safety_license_interceptionpathlist.clear();
                }
                if (booleancamera == false) {
                    if (upload_food_safety_license_filepicture != null) {
                        upload_food_safety_license_filepicture.clear();
                    }
                }
                upload_food_safety_license_filepicture.addAll(getIntersection(pathList_tmp, pathList));
                if (compare(upload_food_safety_license_filepicture,pathList)) {
                    if (upload_food_safety_license_filepicture.size() < 3) {
                        upload_food_safety_license_filepicture.add("firstgridviewitem");
                    }
                    food_safety_license_adapter.notifyDataSetChanged();
                } else {
                    if (upload_food_safety_license_filepicture != null) {
                        if (upload_food_safety_license_filepicture.size() > 0) {
                            upload_food_safety_license_interceptionpathlist.addAll(getAnd_set(pathList,upload_food_safety_license_filepicture));
                        } else {
                            upload_food_safety_license_interceptionpathlist.addAll(pathList);
                        }
                    } else {
                        upload_food_safety_license_interceptionpathlist.addAll(pathList);
                    }
                    for (int i = 0; i < upload_food_safety_license_interceptionpathlist.size(); i++) {
                        upload_food_safety_license_filepicture.add(upload_food_safety_license_interceptionpathlist.get(i));
                    }
                    if (upload_food_safety_license_filepicture.size() < 3) {
                        upload_food_safety_license_filepicture.add("firstgridviewitem");
                    }
                    food_safety_license_adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == Constants.PhotoRequestCode) {
            if (upload_picture_type == upload_field_img_type) {
                if (choose_filepicture!= null) {
                    choose_filepicture.clear();
                }
                choose_filepicture.addAll(choose_filepicture_tmp);
                if (choose_filepicture.size() < 3) {
                    choose_filepicture.add("firstgridviewitem");
                }
                adapter.notifyDataSetChanged();
            } else if (upload_picture_type == upload_license_img_type) {
                if (license_choose_filepicture!= null) {
                    license_choose_filepicture.clear();
                }
                license_choose_filepicture.addAll(license_choose_filepicture_tmp);
                if (license_choose_filepicture.size() < 1) {
                    license_choose_filepicture.add("firstgridviewitem");
                }
                license_adapter.notifyDataSetChanged();
            } else if (upload_picture_type == upload_food_safety_license_type) {
                if (upload_food_safety_license_filepicture!= null) {
                    upload_food_safety_license_filepicture.clear();
                }
                upload_food_safety_license_filepicture.addAll(upload_food_safety_license_choose_filepicture_tmp);
                if (upload_food_safety_license_filepicture.size() < 3) {
                    upload_food_safety_license_filepicture.add("firstgridviewitem");
                }
                food_safety_license_adapter.notifyDataSetChanged();
            }

        }
        switch (requestCode) {
            // 如果是调用相机拍照时
            case Constants.CameraRequestCode:
                booleancamera = true;
                if (upload_picture_type == upload_field_img_type) {
                    for (int i = 0; i < choose_filepicture.size(); i++) {
                        if (choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                            choose_filepicture.remove(i);
                        }
                    }
                    choose_filepicture.add(photosavafilestr);
                    if (choose_filepicture.size() < 3) {
                        choose_filepicture.add("firstgridviewitem");
                    }
                    adapter.notifyDataSetChanged();
                } else if (upload_picture_type == upload_license_img_type) {
                    for (int i = 0; i < license_choose_filepicture.size(); i++) {
                        if (license_choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                            license_choose_filepicture.remove(i);
                        }
                    }
                    license_choose_filepicture.add(photosavafilestr);
                    if (license_choose_filepicture.size() < 3) {
                        license_choose_filepicture.add("firstgridviewitem");
                    }
                    license_adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;

        }
        switch (resultCode) {
            case 5:
                if(data != null) {
                    if (data.getExtras().get("industry_id") != null) {
                        industry_id = data.getExtras().getInt("industry_id");
                        if (companyConfigInfoModel.getCatering_industry_id() != null &&
                                companyConfigInfoModel.getCatering_industry_id().size() > 0 &&
                                companyConfigInfoModel.getCatering_industry_id().contains(industry_id)) {
                            mfood_safety_license_addpicture_layout.setVisibility(View.VISIBLE);
                        } else {
                            mfood_safety_license_addpicture_layout.setVisibility(View.GONE);
                        }
                    }
                    if (data.getExtras().get("display_name") != null) {
                        mindustryname.setText(data.getExtras().getString("display_name"));
                    }
                }

                break;
            default:
                break;


        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //获取相同的 交集
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
    //不同的
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
    //相同的
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
        for (int i = 0; i < license_choose_filepicture.size(); i++) {
            if (license_choose_filepicture.get(i).toString().equals("firstgridviewitem")) {
                license_choose_filepicture.remove(i);
            }
        }
        for (int i = 0; i < upload_food_safety_license_filepicture.size(); i++) {
            if (upload_food_safety_license_filepicture.get(i).toString().equals("firstgridviewitem")) {
                upload_food_safety_license_filepicture.remove(i);
            }
        }
        if (choose_filepicture_editor != null) {
            choose_filepicture_editor.clear();
        }
        choose_filepicture_editor.addAll(choose_filepicture);
        if (choose_filepicture_url != null) {
            choose_filepicture_url.clear();
        }
        if (companyModel != null) {
            if (companyModel.getImages() != null) {
                for (int i = 0; i < companyModel.getImages().size(); i++) {
                    if (choose_filepicture_editor.contains(companyModel.getImages().get(i))) {
                        choose_filepicture_editor.remove(companyModel.getImages().get(i));
                        choose_filepicture_url.add(companyModel.getImages().get(i));
                    }
                }
            }
        }
        if (upload_food_safety_license_filepicture_editor != null) {
            upload_food_safety_license_filepicture_editor.clear();
        }
        upload_food_safety_license_filepicture_editor.addAll(upload_food_safety_license_filepicture);
        if (upload_food_safety_license_filepicture_url != null) {
            upload_food_safety_license_filepicture_url.clear();
        }
        if (companyModel != null) {
            if (companyModel.getFood_safety_license() != null) {
                for (int i = 0; i < companyModel.getFood_safety_license().size(); i++) {
                    if (upload_food_safety_license_filepicture_editor.contains(companyModel.getFood_safety_license().get(i))) {
                        upload_food_safety_license_filepicture_editor.remove(companyModel.getFood_safety_license().get(i));
                        upload_food_safety_license_filepicture_url.add(companyModel.getFood_safety_license().get(i));
                    }
                }
            }
        }
        if (choose_filepicture_editor.size() > 0 ||upload_food_safety_license_filepicture_editor.size() > 0 ||
                (license_choose_filepicture.size() > 0 &&
                !license_choose_filepicture.get(0).equals(license_upload_img_url))) {
            if (choose_filepicture_editor.size() > 0) {
                showProgressDialog();
                Field_FieldApi.getuptoken_comment(MyAsyncHttpClient.MyAsyncHttpClient(), UptokenHandler);
            } else {
                if ((license_choose_filepicture.size() > 0 &&
                        !license_choose_filepicture.get(0).equals(license_upload_img_url)) ||
                        upload_food_safety_license_filepicture_editor.size() > 0) {
                    showProgressDialog();
                    Field_FieldApi.getuptoken_certs(MyAsyncHttpClient.MyAsyncHttpClient(), Up_license_tokenHandler);
                }
            }
        } else {
            if (addfieldimg_str != null) {
                addfieldimg_str.clear();
            }
            for (int i = 0; i < choose_filepicture_url.size(); i++) {
                addfieldimg_str.add(choose_filepicture_url.get(i));
            }
            showProgressDialog();
            //保存按钮的操作功能
            saveCompanyinfo();
            UserApi.saveCompany_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),Company_infoHandler,companyModel);
        }
    }
    private LinhuiAsyncHttpResponseHandler Up_license_tokenHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            if (data != null) {
                license_uploadtoken = data.toString();
                if (upload_food_safety_license_filepicture_editor.size() > 0) {
                    if (food_safety_license_addfieldimg_str != null) {
                        food_safety_license_addfieldimg_str.clear();
                    }
                    food_safety_license_addfieldimgsize = 0;

                    food_safety_license_upload = true;
                    uploadManager.put(upload_food_safety_license_filepicture_editor.get(food_safety_license_addfieldimgsize), null, license_uploadtoken,
                            up_food_safety_licenseHandler, null);
                } else {
                    if (license_choose_filepicture.size() > 0 &&
                            !license_choose_filepicture.get(0).equals(license_upload_img_url)) {
                        uploadManager.put(license_choose_filepicture.get(0), null, license_uploadtoken,
                                license_upCompletionHandler, null);

                    } else {

                    }
                }
            } else {
                MessageUtils.showToast(MyselfInfo_CompanyActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
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
                MessageUtils.showToast(MyselfInfo_CompanyActivity.this, getResources().getString(R.string.field_tupesize_errortoast));
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
    private UpCompletionHandler license_upCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                //保存按钮的操作功能
                try {
                    license_upload_img_url = (Config.qiniu_domain_creats + jsonObject.getString("key").toString());
                    saveCompanyinfo();
                    UserApi.saveCompany_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),Company_infoHandler,companyModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MessageUtils.showToast(MyselfInfo_CompanyActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }
        }
    };
    private UpCompletionHandler up_food_safety_licenseHandler = new UpCompletionHandler() {
        @Override
        public void complete(String s, ResponseInfo responseInfo, org.json.JSONObject jsonObject) {
            if (responseInfo.statusCode == 200) {
                try {
                    food_safety_license_addfieldimgsize ++;
                    food_safety_license_addfieldimg_str.add(Config.qiniu_domain_creats + jsonObject.getString("key").toString());
                    if (food_safety_license_addfieldimgsize == upload_food_safety_license_filepicture_editor.size() && food_safety_license_upload == true) {
                        for (int i = 0; i < upload_food_safety_license_filepicture_url.size(); i++) {
                            food_safety_license_addfieldimg_str.add(upload_food_safety_license_filepicture_url.get(i));
                        }
                        if (license_choose_filepicture.size() > 0 &&
                                !license_choose_filepicture.get(0).equals(license_upload_img_url)) {
                            uploadManager.put(license_choose_filepicture.get(0), null, license_uploadtoken,
                                    license_upCompletionHandler, null);
                        } else {
                            saveCompanyinfo();
                            UserApi.saveCompany_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),Company_infoHandler,companyModel);                        }

                        //保存按钮的操作功能
                    } else if (food_safety_license_addfieldimgsize < upload_food_safety_license_filepicture_editor.size() && food_safety_license_upload == true) {
                        uploadManager.put(upload_food_safety_license_filepicture_editor.get(food_safety_license_addfieldimgsize), null, license_uploadtoken,
                                up_food_safety_licenseHandler, null);

                    } else {
                        if (food_safety_license_addfieldimg_str != null) {
                            food_safety_license_addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(MyselfInfo_CompanyActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                        hideProgressDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                food_safety_license_addfieldimgsize ++;
                food_safety_license_upload = false;

                //删除七牛刚上传的图片  addfieldimg_str

                if (food_safety_license_addfieldimg_str != null) {
                    food_safety_license_addfieldimg_str.clear();
                }
                MessageUtils.showToast(MyselfInfo_CompanyActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

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
                        for (int i = 0; i < choose_filepicture_url.size(); i++) {
                            addfieldimg_str.add(choose_filepicture_url.get(i));
                        }
                        //保存按钮的操作功能
                        if ((license_choose_filepicture.size() > 0 &&
                                !license_choose_filepicture.get(0).equals(license_upload_img_url)) ||
                                upload_food_safety_license_filepicture_editor.size() > 0) {
                            Field_FieldApi.getuptoken_certs(MyAsyncHttpClient.MyAsyncHttpClient(), Up_license_tokenHandler);
                        } else {
                            saveCompanyinfo();
                            UserApi.saveCompany_info(MyAsyncHttpClient.MyAsyncHttpClient_version_three(),Company_infoHandler,companyModel);
                        }
                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(MyselfInfo_CompanyActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
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
                MessageUtils.showToast(MyselfInfo_CompanyActivity.this,getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }

        }
    };

    private LinhuiAsyncHttpResponseHandler Company_infoHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            MessageUtils.showToast(getResources().getString(R.string.save_success_prompt));
            LoginManager.setProduct(mproductname_textview.getText().toString());
            LoginManager.getInstance().setIndustry_name(mindustryname.getText().toString());
            MyselfInfo_CompanyActivity.this.finish();
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    //用户消费水平 自定义控件组件 代码生成
    private void add_layout(ArrayList<Field_AddResourceCreateItemModel> list,
                            RelativeLayout mcompany_user_consumption_level_layout,
                            HashMap<String,Integer> user_consumption_level_contentMap,
                            final HashMap<Integer,String> user_consumption_level_viewsetidmap,
                            final HashMap<String,Boolean> user_consumption_level_clickmap, final int type) {
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width_new = metric.widthPixels;     // 屏幕宽度（像素）
        float width = 0;
        float widthtmp = 0;
        int hightnum = 0;
        TextView[] user_consumption_level_textViewlist = new TextView[list.size()];
        if (type == 1) {
            user_consumption_level_textViewlists = new TextView[list.size()];
            user_consumption_level_textViewlist = user_consumption_level_textViewlists;
        } else if (type == 2) {
            user_age_level_textViewlists = new TextView[list.size()];
            user_consumption_level_textViewlist = user_age_level_textViewlists;
        }

        for (int i = 0; i < list.size(); i++) {
            String name = "";
            if (list.get(i).getDisplay_name() != null) {
                name = list.get(i).getDisplay_name();
            }
            int district_id = list.get(i).getId();
            user_consumption_level_contentMap.put(name,district_id);
            final TextView textView = new TextView(MyselfInfo_CompanyActivity.this);
            textView.setText("  " +  list.get(i).getDisplay_name().toString() + "  ");
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            textView.setPadding(8, 0, 8, 0);
            if (type == 1) {
                if (consumption_level_id != null && consumption_level_id.size() > 0 &&
                        consumption_level_id.contains(list.get(i).getId())) {
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                    textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                    user_consumption_level_clickmap.put(list.get(i).getDisplay_name().toString(), true);
                } else {
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                    textView.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                    user_consumption_level_clickmap.put(list.get(i).getDisplay_name().toString(), false);
                }
            } else if (type == 2){
                if (age_level_id != null && age_level_id.size() > 0 &&
                        age_level_id.contains(list.get(i).getId())) {
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                    textView.setTextColor(getResources().getColor(R.color.default_bluebg));
                    user_consumption_level_clickmap.put(list.get(i).getDisplay_name().toString(), true);
                } else {
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                    textView.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                    user_consumption_level_clickmap.put(list.get(i).getDisplay_name().toString(), false);
                }
            }
            textView.setId(i);
            textView.setTag(i);
            width = Constants.getTextWidth(this, list.get(i).getDisplay_name().toString(), 16) + 16;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, 64);
            if (width > (width_new - widthtmp - 28)) {
                hightnum = hightnum + 1;
                widthtmp = 0;
                params.setMargins((int) widthtmp + 28, hightnum * 84, 0, 0);
                textView.setLayoutParams(params);
                mcompany_user_consumption_level_layout.addView(textView);
            } else {
                params.setMargins((int) widthtmp + 28, hightnum * 84, 0, 0);
                textView.setLayoutParams(params);
                mcompany_user_consumption_level_layout.addView(textView);
            }
            widthtmp = width + widthtmp + 28;
            user_consumption_level_viewsetidmap.put(i, list.get(i).getDisplay_name().toString());
            user_consumption_level_textViewlist[i] = textView;
        }
        for (int i = 0; i < user_consumption_level_textViewlist.length; i++) {
            final TextView[] finalUser_consumption_level_textViewlist = user_consumption_level_textViewlist;
            user_consumption_level_textViewlist[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (user_consumption_level_clickmap.get(user_consumption_level_viewsetidmap.get(v.getId()))) {
                        user_consumption_level_clickmap.put(user_consumption_level_viewsetidmap.get(v.getId()),false);
                        finalUser_consumption_level_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.orderlist_cancelorder_btnbg));
                        finalUser_consumption_level_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                    } else {
                        user_consumption_level_clickmap.put(user_consumption_level_viewsetidmap.get(v.getId()),true);
                        finalUser_consumption_level_textViewlist[v.getId()].setBackgroundDrawable(getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                        finalUser_consumption_level_textViewlist[v.getId()].setTextColor(getResources().getColor(R.color.default_bluebg));
                    }
                }
            });

        }
    }
    //保存按钮点击 保存公司信息到model
    private void saveCompanyinfo() {
        companyModel.setIndustry_id(String.valueOf(industry_id));
        companyModel.setProduct(mproductname_textview.getText().toString().trim());
        companyModel.setSpread_way_id(spread_way_id);
        if (mmyselfinfo_demand_area_edittext.getText().toString().trim().length() > 0) {
            companyModel.setDemand_area(Double.parseDouble(Constants.getpricestring(mmyselfinfo_demand_area_edittext.getText().toString(),1)));
        } else {
            companyModel.setDemand_area(null);
        }
        if (mmyselfinfo_acceptable_minimum_price_edittext.getText().toString().trim().length() > 0) {
            companyModel.setAcceptable_minimum_price(Double.parseDouble(Constants.getpricestring(mmyselfinfo_acceptable_minimum_price_edittext.getText().toString(),1)));
        } else {
            companyModel.setAcceptable_minimum_price(null);
        }
        if (mmyselfinfo_acceptable_maximum_price_edittext.getText().toString().trim().length() > 0) {
            companyModel.setAcceptable_maximum_price(Double.parseDouble(Constants.getpricestring(mmyselfinfo_acceptable_maximum_price_edittext.getText().toString(),1)));
        } else {
            companyModel.setAcceptable_maximum_price(null);
        }
        companyModel.setPushing_frequency_level_id(pushing_frequency_level_id);
        if (consumption_level_id != null) {
            consumption_level_id.clear();
        }
        if (companyConfigInfoModel.getConsumptionlevel() != null && companyConfigInfoModel.getConsumptionlevel().size() > 0) {
            for (int i = 0; i < companyConfigInfoModel.getConsumptionlevel().size(); i++) {
                if (user_consumption_level_clickmap.get(companyConfigInfoModel.getConsumptionlevel().get(i).getDisplay_name())) {
                    consumption_level_id.add(companyConfigInfoModel.getConsumptionlevel().get(i).getId());
                }
            }
        }
        companyModel.setConsumption_level_id(JSONArray.toJSONString(consumption_level_id,true));
        if (age_level_id != null) {
            age_level_id.clear();
        }
        if (companyConfigInfoModel.getAgelevel() != null && companyConfigInfoModel.getAgelevel().size() > 0) {
            for (int i = 0; i < companyConfigInfoModel.getAgelevel().size(); i++) {
                if (user_age_level_clickmap.get(companyConfigInfoModel.getAgelevel().get(i).getDisplay_name())) {
                    age_level_id.add(companyConfigInfoModel.getAgelevel().get(i).getId());
                }
            }
        }
        companyModel.setAge_level_id(JSONArray.toJSONString(age_level_id,true));
        companyModel.setFeature_description(mmyselfinfo_feature_description_edittext.getText().toString().trim());
        companyModel.setCert_url(license_upload_img_url.trim());
        if (food_safety_license_addfieldimg_str.size() > 0) {
            companyModel.setFood_safety_license(food_safety_license_addfieldimg_str);
        } else {
            companyModel.setFood_safety_license(upload_food_safety_license_filepicture_url);
        }
        if (addfieldimg_str.size() > 0) {
            companyModel.setImages(addfieldimg_str);
        } else {
            companyModel.setImages(choose_filepicture_url);
        }
    }
    private void show_pushfrequency_dialog(final TextView textView, final ArrayList<Field_AddResourceCreateItemModel> list, final int type) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(com.linhuiba.linhuifield.R.layout.field_activity_field_addfield_optionalinfo_dialog, null);
        TextView mbtn_cancel = (TextView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.btn_cancel);
        TextView mbtn_confirm = (TextView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.btn_confirm);
        final com.linhuiba.linhuifield.fieldview.WheelView mwheelview= (com.linhuiba.linhuifield.fieldview.WheelView) textEntryView.findViewById(com.linhuiba.linhuifield.R.id.promotion_way_wheelview);
        final List<String> wheelview_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            wheelview_list.add(list.get(i).getDisplay_name());
        }
        mwheelview.setOffset(2);
        mwheelview.setItems(wheelview_list);
        if (list.size() > 2) {
            wheelview_select_int = 2;
            mwheelview.setSeletion(2);
        } else if (list.size() > 1) {
            wheelview_select_int = 1;
            mwheelview.setSeletion(1);
        } else {
            wheelview_select_int = 0;
            mwheelview.setSeletion(0);
        }
        mwheelview.setOnWheelViewListener(new com.linhuiba.linhuifield.fieldview.WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelview_select_int = selectedIndex - 2;
            }
        });
        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(list.get(wheelview_select_int).getDisplay_name());
                if (type == 1) {
                    pushing_frequency_level_id = list.get(wheelview_select_int).getId();
                } else if (type == 3) {
                    spread_way_id = list.get(wheelview_select_int).getId();
                }
                if (pushing_frequency_level_dialog != null) {
                    pushing_frequency_level_dialog.dismiss();
                }
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushing_frequency_level_dialog != null) {
                    pushing_frequency_level_dialog.dismiss();
                }
            }
        });

        pushing_frequency_level_dialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,pushing_frequency_level_dialog);
    }

    private void show_zoom_picture_dialog(final int position, ArrayList<String> mPicList) {
        View myView = MyselfInfo_CompanyActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        zoom_picture_dialog = new AlertDialog.Builder(MyselfInfo_CompanyActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
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
                new AlertDialog.Builder(MyselfInfo_CompanyActivity.this)
                    .setTitle(getResources().getString(R.string.delete_prompt))
                    .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (upload_picture_type == upload_field_img_type) {
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
                                if (galleryviewlist != null && galleryviewlist.size() > 0) {
                                    if (galleryviewlist.size() > newPosition+1 || galleryviewlist.size() == newPosition+1) {

                                    } else {
                                        newPosition = newPosition -1;
                                    }
                                    mshowpicture_dialog_sizetxt.setText(String.valueOf(newPosition%imageList.size()+1)+"/" + String.valueOf(imageList.size()));
                                    com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(MyselfInfo_CompanyActivity.this,MyselfInfo_CompanyActivity.this,newPosition,0);
                                    constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,newPosition);
                                } else {
                                    zoom_picture_dialog.dismiss();
                                }
                            } else if (upload_picture_type == upload_license_img_type) {
                                license_galleryviewlist.remove(license_newPosition%imageList.size());
                                license_choose_filepicture.remove(license_newPosition%imageList.size());
                                license_upload_img_url = "";
                                imageList.remove(license_newPosition%imageList.size());
                                int addpictureimg_num = 0;
                                for (int i = 0; i < license_choose_filepicture.size(); i++) {
                                    if (license_choose_filepicture.get(i).equals("firstgridviewitem")) {
                                        addpictureimg_num++;
                                    }
                                }
                                if (addpictureimg_num == 0) {
                                    license_choose_filepicture.add("firstgridviewitem");
                                }
                                license_adapter.notifyDataSetChanged();
                                zoom_picture_dialog.dismiss();
                            } else if (upload_picture_type == upload_food_safety_license_type) {
                                upload_food_safety_license_galleryviewlist.remove(upload_food_safety_license_newPosition%imageList.size());
                                upload_food_safety_license_filepicture.remove(upload_food_safety_license_newPosition%imageList.size());
                                imageList.remove(upload_food_safety_license_newPosition%imageList.size());
                                int addpictureimg_num = 0;
                                for (int i = 0; i < upload_food_safety_license_filepicture.size(); i++) {
                                    if (upload_food_safety_license_filepicture.get(i).equals("firstgridviewitem")) {
                                        addpictureimg_num++;
                                    }
                                }
                                if (addpictureimg_num == 0) {
                                    upload_food_safety_license_filepicture.add("firstgridviewitem");
                                }
                                food_safety_license_adapter.notifyDataSetChanged();
                                if (upload_food_safety_license_galleryviewlist != null && upload_food_safety_license_galleryviewlist.size() > 0) {
                                    if (upload_food_safety_license_galleryviewlist.size() > upload_food_safety_license_newPosition+1 || upload_food_safety_license_galleryviewlist.size() == upload_food_safety_license_newPosition+1) {

                                    } else {
                                        upload_food_safety_license_newPosition = upload_food_safety_license_newPosition -1;
                                    }
                                    mshowpicture_dialog_sizetxt.setText(String.valueOf(upload_food_safety_license_newPosition%imageList.size()+1)+"/" + String.valueOf(imageList.size()));
                                    com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(MyselfInfo_CompanyActivity.this,MyselfInfo_CompanyActivity.this,upload_food_safety_license_newPosition,0);
                                    constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,upload_food_safety_license_newPosition);
                                } else {
                                    zoom_picture_dialog.dismiss();
                                }
                            }
                        }

                    })
                    .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();

            }
        });
        if (upload_picture_type == upload_field_img_type) {
            newPosition = position;
            com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(MyselfInfo_CompanyActivity.this,MyselfInfo_CompanyActivity.this,newPosition,0);
            constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,newPosition);
        } else if (upload_picture_type == upload_license_img_type) {
            license_newPosition = position;
            com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(MyselfInfo_CompanyActivity.this,MyselfInfo_CompanyActivity.this,license_newPosition,0);
            constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,license_newPosition);
        } else if (upload_picture_type == upload_food_safety_license_type) {
            upload_food_safety_license_newPosition = position;
            com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(MyselfInfo_CompanyActivity.this,MyselfInfo_CompanyActivity.this,upload_food_safety_license_newPosition,0);
            constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,upload_food_safety_license_newPosition);
        }
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
                                            startActivityForResult(intent, Constants.CameraRequestCode);
                                        }
                                    }                               break;
                                case R.id.upload_picture_ll:
                                    mCustomDialog.dismiss();
                                    if (upload_picture_type == upload_field_img_type) {
                                        choosepicture(choose_filepicture,choose_filepicture_tmp,3);
                                    } else if (upload_picture_type == upload_license_img_type) {
                                        choosepicture(license_choose_filepicture,license_choose_filepicture_tmp,1);
                                    } else if (upload_picture_type == upload_food_safety_license_type) {
                                        choosepicture(upload_food_safety_license_filepicture,upload_food_safety_license_choose_filepicture_tmp,3);
                                    }
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(MyselfInfo_CompanyActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(MyselfInfo_CompanyActivity.this,mCustomDialog);
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
    private void addTextChangedListener(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    et.setText(str1);
                    et.setSelection(start);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
