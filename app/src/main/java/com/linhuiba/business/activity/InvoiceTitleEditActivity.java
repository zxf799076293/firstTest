package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
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
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.fieldview.Field_MyGridView;
import com.linhuiba.business.mvppresenter.InvoiceTitleMvpPresenter;
import com.linhuiba.business.mvpview.InvoiceTitleMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.CommonTool;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
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
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/18.
 */

public class InvoiceTitleEditActivity extends BaseMvpActivity implements
        Field_AddFieldChoosePictureCallBack.AdField_showPreviewImgCall,
        InvoiceTitleMvpView {
    @InjectView(R.id.invoiceinfo_change_to_common_invoive_checkbox)
    CheckBox mInvoiceCommonCheckBox;
    @InjectView(R.id.invoiceinfo_change_to_dedicated_invoive_checkbox) CheckBox mInvoiceDedicatedCheckBox;
    @InjectView(R.id.invoiceinfo_change_to_receipt_checkbox) CheckBox mInvoiceReceiptCheckBox;
    @InjectView(R.id.invoive_checkbox_tmp)
    CheckBox mInvoiceTmpCB;
    @InjectView(R.id.invoiceinfo_companyname_edit)
    EditText invoiceinfo_companyname_edit;
    @InjectView(R.id.invoice_company_tax_number_edit)
    EditText minvoice_company_tax_number_edit;
    @InjectView(R.id.invoice_companyaddress_edit)
    EditText minvoice_companyaddress_edit;
    @InjectView(R.id.invoice_bankname_edit)
    EditText minvoice_bankname_edit;
    @InjectView(R.id.invoice_bankaccount_number_edit)
    EditText minvoice_bankaccount_number_edit;
    @InjectView(R.id.invoice_prove_gv)
    Field_MyGridView mInvoiceProveGv;
    @InjectView(R.id.invoice_show_info_ll)
    LinearLayout mInvoiceShowInfoLL;
    @InjectView(R.id.invoice_company_tax_number_layout)
    RelativeLayout minvoice_company_tax_number_layout;
    @InjectView(R.id.invoice_infomation_all_ll)
    LinearLayout mInvoiceInfoMationAllLL;
    @InjectView(R.id.invoice_companytelephone_edit)
    EditText minvoice_companytelephone_edit;
    @InjectView(R.id.invoiceinfo_dedicated_invoive_layout)
    LinearLayout minvoiceinfo_dedicated_invoive_layout;
    @InjectView(R.id.invoice_title_btn)
    Button mInvoiceTitleBtn;
    @InjectView(R.id.invoice_title_default_cb)
    CheckBox mDefaultCheckBox;
    private Dialog mInvoiceChooseAreaDialog;
    public String province_id = "";
    public String city_id = "";
    public String district_id = "";
    public TextView mInvoiceCompanyEareTV;
    //选择图片显示图片
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
    private String voucher_image_url = "";
    private Dialog zoom_picture_dialog;
    private List<ImageView> imageList;
    private CustomDialog mCustomDialog;//选择图片dialog
    private InvoiceInfomationModel invoiceInfomationModeldata;//抬头信息model
    private int invoice_type;//开票类型
    private final int receipt = 1;//收据
    private final int common_invoive = 2;//普通发票
    private final int dedicated_invoive = 3;//专用发票
    private InvoiceTitleMvpPresenter mInvoiceTitleMvpPresenter;
    private int mGetIntentInt;//抬头操作类型：详情，新增，编辑
    private final int INFO_TITLE_INT = 1;//详情
    private final int ADD_TITLE_INT = 2;//新增
    private final int EDIT_TITLE_INT = 3;//编辑
    private int isDefault;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_invoice_title);
        initView();
    }
    private void initView() {
        ButterKnife.inject(this);
        mInvoiceTitleMvpPresenter = new InvoiceTitleMvpPresenter();
        mInvoiceTitleMvpPresenter.attachView(this);
        mInvoiceCompanyEareTV = (TextView)findViewById(R.id.invoice_company_eare_edit);
        TitleBarUtils.showBackImg(this,true);
        //2018/7/18 编辑时获取以前的信息
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            if (intent.getExtras().get("title_model") != null) {
                invoiceInfomationModeldata = (InvoiceInfomationModel) intent.getExtras().get("title_model");
            }
            if (intent.getExtras().get("show_type") != null) {
                mGetIntentInt = intent.getExtras().getInt("show_type");
            }
            if (mGetIntentInt == INFO_TITLE_INT) {
                TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_invoice_title_info_titlebar_str));
            } else if (mGetIntentInt == ADD_TITLE_INT) {
                TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_invoice_title_add_titlebar_str));
            } else if (mGetIntentInt == EDIT_TITLE_INT) {
                TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_invoice_title_edit_titlebar_str));
            }
        }
        if (invoiceInfomationModeldata != null) {
            if (mGetIntentInt == INFO_TITLE_INT) {
                com.linhuiba.linhuifield.connector.Constants.disableSubControls(mInvoiceInfoMationAllLL);
                mInvoiceTitleBtn.setVisibility(View.GONE);
            }
            invoice_type = invoiceInfomationModeldata.getInvoice_type();
            if (invoiceInfomationModeldata.getInvoice_type() == 1) {
                mInvoiceCommonCheckBox.setVisibility(View.GONE);
                mInvoiceDedicatedCheckBox.setVisibility(View.GONE);
                mInvoiceTmpCB.setVisibility(View.INVISIBLE);
                mInvoiceReceiptCheckBox.setChecked(true);
                mInvoiceReceiptCheckBox.setEnabled(false);
                if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                    mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                }
                minvoiceinfo_dedicated_invoive_layout.setVisibility(View.GONE);
                minvoice_company_tax_number_layout.setVisibility(View.GONE);
            } else if (invoiceInfomationModeldata.getInvoice_type() == 2) {
                mInvoiceCommonCheckBox.setChecked(true);
                mInvoiceCommonCheckBox.setEnabled(false);
                mInvoiceDedicatedCheckBox.setVisibility(View.GONE);
                mInvoiceReceiptCheckBox.setVisibility(View.GONE);
                mInvoiceTmpCB.setVisibility(View.INVISIBLE);
                if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                    mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                }
                minvoice_company_tax_number_layout.setVisibility(View.VISIBLE);
                minvoiceinfo_dedicated_invoive_layout.setVisibility(View.GONE);
            } else if (invoiceInfomationModeldata.getInvoice_type() == 3) {
                mInvoiceCommonCheckBox.setVisibility(View.GONE);
                mInvoiceDedicatedCheckBox.setChecked(true);
                mInvoiceDedicatedCheckBox.setEnabled(false);
                mInvoiceReceiptCheckBox.setVisibility(View.GONE);
                mInvoiceTmpCB.setVisibility(View.INVISIBLE);
                if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                    mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                }
                minvoiceinfo_dedicated_invoive_layout.setVisibility(View.VISIBLE);
                minvoice_company_tax_number_layout.setVisibility(View.VISIBLE);
            }
            if (invoiceInfomationModeldata.getTitle() != null) {
                invoiceinfo_companyname_edit.setText(invoiceInfomationModeldata.getTitle());
            }

            if (invoiceInfomationModeldata.getTax_number() != null) {
                minvoice_company_tax_number_edit.setText(invoiceInfomationModeldata.getTax_number());
            }
            if (invoiceInfomationModeldata.getCompany_region() != null) {
                mInvoiceCompanyEareTV.setText(invoiceInfomationModeldata.getCompany_region());
            }
            if (invoiceInfomationModeldata.getCompany_address() != null) {
                minvoice_companyaddress_edit.setText(invoiceInfomationModeldata.getCompany_address());
            }
            if (invoiceInfomationModeldata.getCompany_mobile() != null) {
                minvoice_companytelephone_edit.setText(invoiceInfomationModeldata.getCompany_mobile());
            }

            if (invoiceInfomationModeldata.getBank() != null) {
                minvoice_bankname_edit.setText(invoiceInfomationModeldata.getBank());
            }
            if (invoiceInfomationModeldata.getAccount() != null) {
                minvoice_bankaccount_number_edit.setText(invoiceInfomationModeldata.getAccount());
            }
            if (invoiceInfomationModeldata.getGeneral_taxpayer_qualification() != null &&
                    invoiceInfomationModeldata.getGeneral_taxpayer_qualification().length() > 0) {
                choose_filepicture.add(invoiceInfomationModeldata.getGeneral_taxpayer_qualification());
                voucher_image_url = invoiceInfomationModeldata.getGeneral_taxpayer_qualification();
            }            Constants.delete_picture_file();
            if (choose_filepicture.size() < 1) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter = new Field_ChoosePictureGridViewAdapter(InvoiceTitleEditActivity.this, InvoiceTitleEditActivity.this, choose_filepicture, 8);
            mInvoiceProveGv.setAdapter(adapter);
            mInvoiceInfoMationAllLL.setVisibility(View.VISIBLE);
            if (invoiceInfomationModeldata.getIs_default() == 1) {
                mDefaultCheckBox.setChecked(true);
                isDefault = 1;
            }
        } else {
            invoiceInfomationModeldata = new InvoiceInfomationModel();
            Constants.delete_picture_file();
            if (choose_filepicture.size() < 1) {
                choose_filepicture.add("firstgridviewitem");
            }
            adapter = new Field_ChoosePictureGridViewAdapter(InvoiceTitleEditActivity.this, InvoiceTitleEditActivity.this, choose_filepicture, 8);
            mInvoiceProveGv.setAdapter(adapter);
        }
        Constants.addTextSpace(invoiceinfo_companyname_edit);
        Constants.addTextSpace(minvoice_company_tax_number_edit);
        Constants.addTextSpace(minvoice_companyaddress_edit);
        Constants.addTextSpace(minvoice_bankname_edit);
        Constants.addTextSpace(minvoice_bankaccount_number_edit);
        Constants.addTextSpace(minvoice_companytelephone_edit);
        mDefaultCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDefault == 1) {
                    mDefaultCheckBox.setChecked(true);
                } else {
                    mDefaultCheckBox.setChecked(true);
                    isDefault = 1;
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInvoiceTitleMvpPresenter != null) {
            mInvoiceTitleMvpPresenter.detachView();
        }
    }
    @OnClick({
            R.id.invoiceinfo_change_to_common_invoive_checkbox,
            R.id.invoiceinfo_change_to_dedicated_invoive_checkbox,
            R.id.invoiceinfo_change_to_receipt_checkbox,
            R.id.invoice_company_eare_layout,
            R.id.invoice_title_btn
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.invoiceinfo_change_to_common_invoive_checkbox:
                if (mInvoiceCommonCheckBox.isChecked()) {
                    invoice_type = common_invoive;
                    mInvoiceCommonCheckBox.setChecked(true);
                    mInvoiceDedicatedCheckBox.setChecked(false);
                    mInvoiceReceiptCheckBox.setChecked(false);
                    if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                        mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                    }
                    minvoice_company_tax_number_layout.setVisibility(View.VISIBLE);
                    minvoiceinfo_dedicated_invoive_layout.setVisibility(View.GONE);
                } else {
                    mInvoiceCommonCheckBox.setChecked(true);
                }
                break;
            case R.id.invoiceinfo_change_to_dedicated_invoive_checkbox:
                if (mInvoiceDedicatedCheckBox.isChecked()) {
                    invoice_type = dedicated_invoive;
                    mInvoiceCommonCheckBox.setChecked(false);
                    mInvoiceDedicatedCheckBox.setChecked(true);
                    mInvoiceReceiptCheckBox.setChecked(false);
                    if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                        mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                    }
                    minvoiceinfo_dedicated_invoive_layout.setVisibility(View.VISIBLE);
                    minvoice_company_tax_number_layout.setVisibility(View.VISIBLE);
                } else {
                    mInvoiceDedicatedCheckBox.setChecked(true);
                }
                break;
            case R.id.invoiceinfo_change_to_receipt_checkbox:
                if (mInvoiceReceiptCheckBox.isChecked()) {
                    invoice_type = receipt;
                    mInvoiceCommonCheckBox.setChecked(false);
                    mInvoiceDedicatedCheckBox.setChecked(false);
                    mInvoiceReceiptCheckBox.setChecked(true);

                    if (mInvoiceShowInfoLL.getVisibility() == View.GONE) {
                        mInvoiceShowInfoLL.setVisibility(View.VISIBLE);
                    }
                    minvoiceinfo_dedicated_invoive_layout.setVisibility(View.GONE);
                    minvoice_company_tax_number_layout.setVisibility(View.GONE);
                } else {
                    mInvoiceReceiptCheckBox.setChecked(true);
                }
                break;
            case R.id.invoice_company_eare_layout:
                LayoutInflater factory = LayoutInflater.from(this);
                final View textEntryView = factory.inflate(R.layout.activity_districtselect, null);
                mInvoiceChooseAreaDialog = new AlertDialog.Builder(this).create();
                DistrictSelectActivity districtSelectActivity = new DistrictSelectActivity(InvoiceTitleEditActivity.this,textEntryView,mInvoiceChooseAreaDialog,4);
                break;
            case R.id.invoice_title_btn:
                btnClick();
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
            AndPermission.with(InvoiceTitleEditActivity.this)
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
    private void show_zoom_picture_dialog(final int position, ArrayList<String> mPicList) {
        View myView = InvoiceTitleEditActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        zoom_picture_dialog = new AlertDialog.Builder(InvoiceTitleEditActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
        zoom_picture_dialog.setOnKeyListener(onKeylistener);
        final TextView mshowpicture_dialog_sizetxt = (TextView)myView.findViewById(R.id.showpicture_dialog_sizetxt);
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.showpicture_dialog_back);
        final ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.zoom_dialog_viewpage);
        ImageButton mshowpicture_dialog_deletebtn = (ImageButton)myView.findViewById(R.id.showpicture_dialog_deletebtn);
        if (mGetIntentInt != INFO_TITLE_INT) {
            mshowpicture_dialog_deletebtn.setVisibility(View.VISIBLE);
        }
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
        com.linhuiba.linhuifield.connector.Constants constants = new com.linhuiba.linhuifield.connector.Constants(InvoiceTitleEditActivity.this,InvoiceTitleEditActivity.this,newPosition,0);
        constants.show_preview_zoom(imageList,mzoom_viewpage,mshowpicture_dialog_sizetxt,newPosition);
    }
    DialogInterface.OnKeyListener onKeylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return false;
            } else {
                return true;
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
                                    }
                                    break;
                                case R.id.upload_picture_ll:
                                    mCustomDialog.dismiss();
                                    choosepicture();
                            }
                        }
                    };
                    CustomDialog.Builder builder = new CustomDialog.Builder(InvoiceTitleEditActivity.this);
                    mCustomDialog = builder
                            .cancelTouchout(true)
                            .view(R.layout.app_upload_picture_dialog)
                            .addViewOnclick(R.id.upload_photo_ll,uploadListener)
                            .addViewOnclick(R.id.upload_picture_ll,uploadListener)
                            .build();
                    com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(InvoiceTitleEditActivity.this,mCustomDialog);
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

        ImageSelector.open(InvoiceTitleEditActivity.this, imageConfig);   // 开启图片选择器
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
           
            default:
                break;
        }
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
            } else {
                this.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            //2018/1/2 请求支付接口
            invoiceInfomationModeldata.setGeneral_taxpayer_qualification(addfieldimg_str.get(0));
            showProgressDialog();
            if (invoiceInfomationModeldata.getId() > 0) {
                mInvoiceTitleMvpPresenter.editInvoiceTitle(invoiceInfomationModeldata);
            } else {
                mInvoiceTitleMvpPresenter.addInvoiceTitle(invoiceInfomationModeldata);
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
                    addfieldimgsize ++;
                    addfieldimg_str.add(Config.qiniu_domain_creats + jsonObject.getString("key").toString());
                    if (addfieldimgsize == choose_filepicture_editor.size() && upload == true) {
                        for (int i = 0; i < choose_filepicture_url.size(); i++) {
                            addfieldimg_str.add(choose_filepicture_url.get(i));
                        }
                        //2018/1/2 请求支付接口上传数据
                        invoiceInfomationModeldata.setGeneral_taxpayer_qualification(addfieldimg_str.get(0));
                        showProgressDialog();
                        if (invoiceInfomationModeldata.getId() > 0) {
                            mInvoiceTitleMvpPresenter.editInvoiceTitle(invoiceInfomationModeldata);
                        } else {
                            mInvoiceTitleMvpPresenter.addInvoiceTitle(invoiceInfomationModeldata);
                        }
                    } else if (addfieldimgsize < choose_filepicture_editor.size() && upload == true) {
                        uploadManager.put(choose_filepicture_editor.get(addfieldimgsize), null, uploadtoken,
                                upCompletionHandler, null);

                    } else {
                        if (addfieldimg_str != null) {
                            addfieldimg_str.clear();
                        }
                        MessageUtils.showToast(getResources().getString(R.string.txt_upload_fieldimg));
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
                MessageUtils.showToast(getResources().getString(R.string.txt_upload_fieldimg));
                hideProgressDialog();
            }
        }
    };
    private void btnClick() {
        if (invoice_type > 0) {
            if (!(invoiceinfo_companyname_edit.getText().toString().trim().length() > 0)) {
                MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_firstname_hinttxt));
                return;
            }
            if (invoice_type == common_invoive || invoice_type == dedicated_invoive) {
                if (!(minvoice_company_tax_number_edit.getText().toString().trim().length() > 0)) {
                    MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_companycode_hinttxt));
                    return;
                } else {
                    if (minvoice_company_tax_number_edit.getText().toString().trim().length() == 15 ||
                            minvoice_company_tax_number_edit.getText().toString().trim().length() == 18 ||
                            minvoice_company_tax_number_edit.getText().toString().trim().length() == 20) {

                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_invoice_title_tax_num_error_msg));
                        return;
                    }
                    if (!CommonTool.istaxNum(minvoice_company_tax_number_edit.getText().toString().trim())) {
                        MessageUtils.showToast(getResources().getString(R.string.module_invoice_title_tax_format_error_msg));
                        return;
                    }
                }
            }
            if (invoice_type == dedicated_invoive) {
                if (mInvoiceCompanyEareTV.getText().toString().trim().length() == 0) {
                    MessageUtils.showToast(getResources().getString(R.string.txt_field_area_hint));
                    return;
                }
                if (!(minvoice_companyaddress_edit.getText().toString().trim().length() > 0)) {
                    MessageUtils.showToast(getResources().getString(R.string.invoice_companyaddress_hinttext));
                    return;
                }
                if (!(minvoice_companytelephone_edit.getText().toString().trim().length() > 0)) {
                    MessageUtils.showToast(getResources().getString(R.string.invoice_companytelephone_hinttext));
                    return;
                }
                if (!CommonTool.isTelephone(minvoice_companytelephone_edit.getText().toString())) {
                    MessageUtils.showToast(getResources().getString(R.string.mTxt_telephone_prompt));
                    return;
                }
                if (!(minvoice_bankname_edit.getText().toString().trim().length() > 0)) {
                    MessageUtils.showToast(getResources().getString(R.string.module_applyforrelease_bankname_hint));
                    return;
                }
                if (!(minvoice_bankaccount_number_edit.getText().toString().trim().length() > 0)) {
                    MessageUtils.showToast(getResources().getString(R.string.applyforrelease_bankaccount_number_hinttxt));
                    return;
                }
                if (choose_filepicture != null && choose_filepicture.size() > 0 &&
                        choose_filepicture.get(0).length() > 0 &&
                        !choose_filepicture.get(0).equals("firstgridviewitem")) {

                } else {
                    MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_invoice_prove_hint_str));
                    return;
                }
            }
            invoiceInfomationModeldata.setInvoice_type(invoice_type);
            invoiceInfomationModeldata.setTitle(invoiceinfo_companyname_edit.getText().toString());
            invoiceInfomationModeldata.setIs_default(isDefault);
            if (invoice_type == common_invoive || invoice_type == dedicated_invoive) {
                invoiceInfomationModeldata.setTax_number(minvoice_company_tax_number_edit.getText().toString());
            }
            if (invoice_type == dedicated_invoive) {
                invoiceInfomationModeldata.setCompany_region(mInvoiceCompanyEareTV.getText().toString());
                invoiceInfomationModeldata.setCompany_address(minvoice_companyaddress_edit.getText().toString());
                invoiceInfomationModeldata.setCompany_mobile(minvoice_companytelephone_edit.getText().toString());
                invoiceInfomationModeldata.setBank(minvoice_bankname_edit.getText().toString());
                invoiceInfomationModeldata.setAccount(minvoice_bankaccount_number_edit.getText().toString());
            }
            if (invoice_type == dedicated_invoive) {
                if (choose_filepicture.size() > 0 || !choose_filepicture.contains("firstgridviewitem")) {
                    uploadimg();
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_invoice_prove_hint_str));
                }
            } else {
                showProgressDialog();
                if (invoiceInfomationModeldata.getId() > 0) {
                    mInvoiceTitleMvpPresenter.editInvoiceTitle(invoiceInfomationModeldata);
                } else {
                    mInvoiceTitleMvpPresenter.addInvoiceTitle(invoiceInfomationModeldata);
                }
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.invoiceinfo_type_choose_remiand_txt));
        }

    }

    @Override
    public void onInvoiceTitleListSuccess(ArrayList<InvoiceInfomationModel> data) {

    }

    @Override
    public void onInvoiceTitleListMoreSuccess(ArrayList<InvoiceInfomationModel> data) {

    }

    @Override
    public void onInvoiceTitleListMoreFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onInvoiceTitleInfoSuccess(InvoiceInfomationModel data) {

    }

    @Override
    public void onInvoiceTitleAddSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.save_success_prompt));
        Intent intent = new Intent();
        setResult(mGetIntentInt,intent);
        finish();
    }

    @Override
    public void onInvoiceTitleEditSuccess() {
        MessageUtils.showToast(getResources().getString(R.string.save_success_prompt));
        Intent intent = new Intent();
        setResult(mGetIntentInt,intent);
        finish();
    }

    @Override
    public void onInvoiceTitleDeleteSuccess() {

    }

    @Override
    public void onInvoiceTitleDefaultSuccess() {

    }

    @Override
    public void onInvoiceTitleFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

}
