package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuifield.fieldadapter.Field_AddField_FieldPriceAdapter;
import com.linhuiba.linhuifield.fieldadapter.Field_AddField_FourSpecialAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldSellResDimensionsModel;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldadapter.Addfield_fourlisviewadapter;
import com.linhuiba.linhuifield.fieldadapter.Field_AddField_FourDenominatedUnitAdapter;
import com.linhuiba.linhuifield.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.linhuifield.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.util.TitleBarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Administrator on 2016/7/16.
 */
public class Field_AddField_FieldPricesActivity extends FieldBaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AddfieldFourCall,Field_AddFieldChoosePictureCallBack.AddfieldFourSpecialCall{
    private FieldMyGridView maddfieldfour_denominated_gridview;
    public FieldMyGridView maddfield_fieldsizelistview;
    public LinearLayout mAddfieldCustomSizeLL;
    public FieldMyGridView maddfield_special_fieldsizelistview;
    public RelativeLayout maddfield_special_relativelayout;
    public FieldMyGridView maddfield_default_special_fieldsizelistview;
    private Button mAddfieldEditorPriceBtn;
   private Field_AddField_FourDenominatedUnitAdapter addfieldfour_denominated_adapter;
    public int GridviewNumColumns = 0;
    private Addfield_fourlisviewadapter fieldsizeadapter;
    private Field_AddField_FieldPriceAdapter field_addField_fieldPriceAdapter;
    private ArrayList<HashMap<String, String>> customsizelist;//固定的摊位大小的list
    public ArrayList<HashMap<String,String>> datefieldsizelist = new ArrayList<>();
    private ArrayList<HashMap<Object,Object>> data;//时间单位
    private ArrayList<String> lease_term_type_id = new ArrayList<>();
    private ArrayList<String> custom_dimension_list = new ArrayList<>();
    private ArrayList<String> sizelist = new ArrayList<>();
    public boolean editorsizetxt = true;
    private Field_AddField_FourSpecialAdapter field_addField_fourSpecialAdapter;
    private ArrayList<HashMap<String,String>> datefieldSpecialList = new ArrayList<>();//自定义特殊规格
    private ArrayList<HashMap<String,String>> default_custom_dimensionList = new ArrayList<>();//默认的固定特殊规格
    public boolean editor_special_txt = true;

    private FieldMyGridView maddfield_price_default_size_gridview;
    private LinearLayout mAddfieldFieldPriceUnitLL;
    private LinearLayout mAddfieldFieldPriceEnquiryLL;
    private RelativeLayout mAddfieldFieldPriceRL;
    private Switch mAddfieldFieldPriceSwitch;
    private EditText mAddfieldPriceEnquiryET;
    private EditText mAddfieldPriceEnquiryMaxPriceET;
    private EditText mAddfieldPriceEnquiryMinPriceET;
    private ArrayList<Map<String, String>> denominatedunitlist = new ArrayList<Map<String, String>>();//规格中的时间的list
    private ArrayList<String> sizestrlist_tmp = new ArrayList<String>();//规格中的摊位大小的list
    private ArrayList<Map<String, String>> custom_dimensionlist = new ArrayList<Map<String, String>>();//特殊品类的list
    private HashMap<Integer,Integer> mWeekLeaseTermMap = new HashMap<Integer,Integer>();
    public HashMap<Integer,String> mWeekLeaseTermNameMap = new HashMap<Integer,String>();
    public List<String> mWeekLeaseTermIdList = new ArrayList<>();
    private int mIsEnquiryInt;
    private CheckBox[] checkBoxList = new CheckBox[5];
    private int[] checkBoxInt = new int[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfeld_four_denominatedunit);
        initview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }
    private boolean checkInput_info_full() {
        boolean result = false;
        if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
        mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
            if (mAddfieldPriceEnquiryMinPriceET.getText().toString().trim().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_txt));
                return result;
            }
            if (mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim().length() == 0) {
                BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_txt));
                return result;
            }
            if (mAddfieldPriceEnquiryMinPriceET.getText().toString().trim().length() > 0 &&
                    mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim().length() > 0) {
                if (Double.parseDouble(mAddfieldPriceEnquiryMinPriceET.getText().toString().trim()) >
                        Double.parseDouble(mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim())) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_price_acceptable_price_error_text));
                    return result;
                }
            } else if ((mAddfieldPriceEnquiryMinPriceET.getText().toString().trim().length() > 0 &&
                    mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim().length() == 0) ||
                    (mAddfieldPriceEnquiryMinPriceET.getText().toString().trim().length() == 0 &&
                            mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim().length() > 0)) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_info_full_hinttext));
                return result;
            }
        } else {
            ArrayList<Map<String, String>> fieldsizelist = new ArrayList<Map<String, String>>();//输入的场地大小的list
            ArrayList<Map<String, String>> denominatedunitlist = new ArrayList<Map<String, String>>();//规格中的时间的list
            ArrayList<Map<String, String>> custom_dimensionlist = new ArrayList<Map<String, String>>();//特殊品类的list

            for (int i = 0; i < customsizelist.size(); i++) {
                if (Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().get(customsizelist.get(i).get("fieldsize_str"))) {
                    fieldsizelist.add(customsizelist.get(i));
                }
            }
            for (int i = 0; i < default_custom_dimensionList.size(); i++) {
                if (Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().get(default_custom_dimensionList.get(i).get("custom_dimension"))) {
                    custom_dimensionlist.add(default_custom_dimensionList.get(i));
                }
            }

            for (int i = 0; i < maddfield_fieldsizelistview.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) maddfield_fieldsizelistview.getChildAt(i);// 获得子item的layout
                EditText et = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_left);// 从layout中获得控件,根据其id
                EditText et_right = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_right);// 从layout中获得控件,根据其id
                if (et.getVisibility() == View.VISIBLE) {
                    if (Addfield_fourlisviewadapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                        if (et.getText().toString().length() > 0 && et_right.getText().toString().length() > 0) {
                            if (isFloathString(et.getText().toString()) == 0 && isFloathString(et_right.getText().toString()) == 0) {
                                boolean size_repeat = false;//检查是否有重复的size
                                for (int j = 0; j < fieldsizelist.size(); j++) {
                                    if ((Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_left"),1).equals(Constants.getpricestring(et.getText().toString(),1))&&
                                            Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_right"),1).equals(Constants.getpricestring(et_right.getText().toString(),1))) ||
                                            (Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_left"),1).equals(Constants.getpricestring(et_right.getText().toString(),1))&&
                                                    Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_right"),1).equals(Constants.getpricestring(et.getText().toString(),1)))) {
                                        hideProgressDialog();
                                        size_repeat = true;
                                        BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_repeat_txt));
                                        return result;
                                    }
                                }
                                if (size_repeat == false ) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("fieldsize_left", et.getText().toString());
                                    map.put("fieldsize_right", et_right.getText().toString());
                                    fieldsizelist.add(map);
                                } else {
                                    hideProgressDialog();
                                    fieldsizelist.clear();
                                    return result;
                                }
                            } else {
                                hideProgressDialog();
                                fieldsizelist.clear();
                                BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_correct_txt));
                                return result;
                            }
                        } else {
                            hideProgressDialog();
                            fieldsizelist.clear();
                            BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_correct_txt));
                            return result;
                        }
                    }
                }
            }
            for (int i = 0; i < maddfield_special_fieldsizelistview.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) maddfield_special_fieldsizelistview.getChildAt(i);// 获得子item的layout
                EditText et = (EditText) layout.findViewById(R.id.addfield_denominated_special_edittext);// 从layout中获得控件,根据其id
                if (et.getVisibility() == View.VISIBLE) {
                    if (Field_AddField_FourSpecialAdapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                        if (et.getText().toString().length() > 0) {
                            boolean size_repeat = false;
                            for (int j = 0; j < custom_dimensionlist.size(); j++) {
                                if (custom_dimensionlist.get(j).get("custom_dimension").trim().equals(et.getText().toString().trim())) {
                                    hideProgressDialog();
                                    size_repeat = true;
                                    BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_repeat_txt));
                                    return result;
                                }
                            }
                            if (size_repeat == false ) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("custom_dimension", et.getText().toString());
                                custom_dimensionlist.add(map);
                            } else {
                                hideProgressDialog();
                                custom_dimensionlist.clear();
                                return result;
                            }
                        } else {
                            hideProgressDialog();
                            custom_dimensionlist.clear();
                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_five_addfield_special_hinttxt));
                            return result;
                        }
                    }
                }
            }

            for (int i = 0; i < data.size(); i++) {
                if (Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().get(data.get(i).get("denominatedunit").toString())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("denominatedunit", data.get(i).get("denominatedunit").toString());
                    map.put("m_lease_term_type_id", data.get(i).get("m_lease_term_type_id").toString());
                    denominatedunitlist.add(map);
                }
            }
            if (custom_dimensionlist.size() > 0) {
                if (custom_dimensionlist.size() * denominatedunitlist.size() +custom_dimensionlist.size() * denominatedunitlist.size() * fieldsizelist.size() > 50) {
                    hideProgressDialog();
                    BaseMessageUtils.showToast(Field_AddField_FieldPricesActivity.this.getResources().getString(R.string.addfield_four_addfield_morethan_max_specifications_hinttxt));
                    return result;
                }
            } else {
                if (denominatedunitlist.size() * fieldsizelist.size() > 50) {
                    hideProgressDialog();
                    BaseMessageUtils.showToast(Field_AddField_FieldPricesActivity.this.getResources().getString(R.string.addfield_four_addfield_morethan_max_specifications_hinttxt));
                    return result;
                }
            }

            if (denominatedunitlist.size() > 0 && fieldsizelist.size() > 0) {
                ArrayList<String> fieldsize_default_tmp = new ArrayList<String>();
                ArrayList<String> sizestrlist = new ArrayList<String>();
                for (int i = 0; i < customsizelist.size(); i++) {
                    if (Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().get(customsizelist.get(i).get("fieldsize_str"))) {
                        fieldsize_default_tmp.add(customsizelist.get(i).get("fieldsize_str"));
                        sizestrlist.add(customsizelist.get(i).get("fieldsize_str"));
                    }
                }
                for (int i = 0; i < fieldsize_default_tmp.size(); i++) {
                    for (int j = 0; j < fieldsizelist.size(); j++) {
                        if (fieldsizelist.get(j).get("fieldsize_str") != null) {
                            if (fieldsize_default_tmp.get(i).equals(fieldsizelist.get(j).get("fieldsize_str"))) {
                                fieldsizelist.remove(j);
                                break;
                            }
                        }
                    }
                }
                for (int i = 0; i < fieldsizelist.size(); i++) {
                    sizestrlist.add(Constants.getpricestring(fieldsizelist.get(i).get("fieldsize_left").toString(), 1) + "*" + Constants.getpricestring(fieldsizelist.get(i).get("fieldsize_right").toString(), 1));
                }
                if (sizestrlist.size() > 0) {
                    if ((Field_AddResourcesModel.getInstance().getSizelist() != null &&
                            Field_AddResourcesModel.getInstance().getSizelist().size() > 0)) {
                        if (sizestrlist.size() == Field_AddResourcesModel.getInstance().getSizelist().size()) {
                            for (int i = 0; i < sizestrlist.size(); i++) {
                                if (Field_AddResourcesModel.getInstance().getSizelist().contains(sizestrlist.get(i))) {

                                } else {
                                    //size和保存的不一样
//                            BaseMessageUtils.showToast("2");
                                    return result;
                                }
                            }
                        } else {
//                        BaseMessageUtils.showToast("12");
                            return result;
                        }

                    } else {
                        //size和保存的不一样
//                    BaseMessageUtils.showToast("3");
                        return result;
                    }
                } else {
                    if ((Field_AddResourcesModel.getInstance().getSizelist() == null ||
                            Field_AddResourcesModel.getInstance().getSizelist().size() == 0)) {

                    } else {
                        //size和保存的不一样
//                    BaseMessageUtils.showToast("4");
                        return result;
                    }
                }
                if (denominatedunitlist.size() > 0) {
                    if ((Field_AddResourcesModel.getInstance().getLease_term_type_id_list() != null ||
                            Field_AddResourcesModel.getInstance().getLease_term_type_id_list().size() > 0)) {
                        if (denominatedunitlist.size() == Field_AddResourcesModel.getInstance().getLease_term_type_id_list().size()) {
                            for (int i = 0; i < denominatedunitlist.size(); i++) {
                                if (Field_AddResourcesModel.getInstance().getLease_term_type_id_list().contains(denominatedunitlist.get(i))) {

                                } else {
                                    //价格单位和保存的不一样
//                            BaseMessageUtils.showToast("5");
                                    return result;
                                }
                            }
                        } else {
//                        BaseMessageUtils.showToast("15");
                            return result;
                        }
                    } else {
                        //价格单位和保存的不一样
//                    BaseMessageUtils.showToast("6");
                        return result;
                    }
                } else {
                    if ((Field_AddResourcesModel.getInstance().getLease_term_type_id_list() == null ||
                            Field_AddResourcesModel.getInstance().getLease_term_type_id_list().size() == 0)) {

                    } else {
                        //价格单位和保存的不一样
//                    BaseMessageUtils.showToast("7");
                        return result;
                    }
                }
                if (custom_dimensionlist.size() > 0) {
                    if ((Field_AddResourcesModel.getInstance().getCustom_dimension_field_list() != null &&
                            Field_AddResourcesModel.getInstance().getCustom_dimension_field_list().size() > 0)) {
                        if (custom_dimensionlist.size() == Field_AddResourcesModel.getInstance().getCustom_dimension_field_list().size()) {
                            for (int i = 0; i < custom_dimensionlist.size(); i++) {
                                if (Field_AddResourcesModel.getInstance().getCustom_dimension_field_list().contains(custom_dimensionlist.get(i))) {

                                } else {
                                    //特殊规格和保存的不一样
//                            BaseMessageUtils.showToast("8");
                                    return result;
                                }
                            }
                        } else {
//                        BaseMessageUtils.showToast("18");
                            return result;
                        }
                    } else {
                        //特殊规格和保存的不一样
//                    BaseMessageUtils.showToast("9");
                        return result;
                    }
                } else {
                    if ((Field_AddResourcesModel.getInstance().getCustom_dimension_field_list() == null ||
                            Field_AddResourcesModel.getInstance().getCustom_dimension_field_list().size() == 0)) {

                    } else {
                        //特殊规格和保存的不一样
//                    BaseMessageUtils.showToast("10");
                        return result;
                    }
                }
            } else {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_five_addfield_special_pointtxt));
                return result;
            }
        }
        return true;
    }

    private void initview() {
        maddfield_price_default_size_gridview = (FieldMyGridView)findViewById(R.id.addfield_price_default_size_gridview);
        maddfieldfour_denominated_gridview = (FieldMyGridView) findViewById(R.id.addfieldfour_denominated_gridview);
        maddfield_fieldsizelistview = (FieldMyGridView) findViewById(R.id.addfield_fieldsizelistview);
        mAddfieldCustomSizeLL = (LinearLayout) findViewById(R.id.addfield_price_custom_size_ll);
        maddfield_special_fieldsizelistview = (FieldMyGridView) findViewById(R.id.addfield_special_fieldsizelistview);
        maddfield_special_relativelayout = (RelativeLayout)findViewById(R.id.addfield_special_relativelayout);
        maddfield_default_special_fieldsizelistview = (FieldMyGridView)findViewById(R.id.addfield_default_special_fieldsizelistview);
        mAddfieldEditorPriceBtn = (Button)findViewById(R.id.addfield_editor_price_btn);
        mAddfieldFieldPriceUnitLL = (LinearLayout)findViewById(R.id.addfield_field_price_unit_ll);
        mAddfieldFieldPriceEnquiryLL = (LinearLayout)findViewById(R.id.addfield_enquiry_price_ll);
        mAddfieldFieldPriceRL = (RelativeLayout)findViewById(R.id.addfield_price_enquary_rl);
        mAddfieldFieldPriceSwitch = (Switch)findViewById(R.id.addfield_price_enquary_switch);
        mAddfieldPriceEnquiryET = (EditText)findViewById(R.id.addfield_price_enquiry_edit);
        mAddfieldPriceEnquiryMinPriceET = (EditText)findViewById(R.id.addfield_price_enquiry_min_price_et);
        mAddfieldPriceEnquiryMaxPriceET = (EditText)findViewById(R.id.addfield_price_enquiry_max_price_et);

        mAddfieldFieldPriceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsEnquiryInt = 0;
                    mAddfieldFieldPriceEnquiryLL.setVisibility(View.GONE);
                    mAddfieldFieldPriceUnitLL.setVisibility(View.VISIBLE);
                    mAddfieldEditorPriceBtn.setText(getResources().getString(R.string.addfield_next_btn_str));
                    mAddfieldEditorPriceBtn.setVisibility(View.VISIBLE);
                    TitleBarUtils.setnextViewText(Field_AddField_FieldPricesActivity.this,"null");
                } else {
                    mIsEnquiryInt = 1;
                    mAddfieldFieldPriceEnquiryLL.setVisibility(View.VISIBLE);
                    mAddfieldFieldPriceUnitLL.setVisibility(View.GONE);
                    mAddfieldEditorPriceBtn.setText(getResources().getString(R.string.addfield_service_next_btn_str));
                    if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                        TitleBarUtils.setnextViewText(Field_AddField_FieldPricesActivity.this,getResources().getString(R.string.myselfinfo_save_pw));
                        TitleBarUtils.shownextTextView(Field_AddField_FieldPricesActivity.this, "", 14, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                                        && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
                                        mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
                                    if (checkInput_info_full()) {
                                        Intent serciceIntent = new Intent(Field_AddField_FieldPricesActivity.this,
                                                FieldAddFieldMainActivity.class);
                                        startActivity(serciceIntent);
                                        Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                                        Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                                        savadata();
                                    }
                                }
                            }
                        });
                        mAddfieldEditorPriceBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getIs_enquiry() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getIs_enquiry() == 1) {
            mAddfieldFieldPriceSwitch.setChecked(false);
            mIsEnquiryInt = 1;
            mAddfieldFieldPriceEnquiryLL.setVisibility(View.VISIBLE);
            mAddfieldFieldPriceUnitLL.setVisibility(View.GONE);
            mAddfieldEditorPriceBtn.setText(getResources().getString(R.string.addfield_service_next_btn_str));
            if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                TitleBarUtils.setnextViewText(this,getResources().getString(R.string.myselfinfo_save_pw));
                TitleBarUtils.shownextTextView(this, "", 14, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                                && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
                                mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
                            if (checkInput_info_full()) {
                                Intent serciceIntent = new Intent(Field_AddField_FieldPricesActivity.this,
                                        FieldAddFieldMainActivity.class);
                                startActivity(serciceIntent);
                                Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                                Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                                savadata();
                            }
                        }
                    }
                });
                mAddfieldEditorPriceBtn.setVisibility(View.GONE);
            }
        } else {
            mAddfieldFieldPriceSwitch.setChecked(true);
            mIsEnquiryInt = 0;
            mAddfieldFieldPriceEnquiryLL.setVisibility(View.GONE);
            mAddfieldFieldPriceUnitLL.setVisibility(View.VISIBLE);
            mAddfieldEditorPriceBtn.setText(getResources().getString(R.string.addfield_next_btn_str));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
            mAddfieldFieldPriceRL.setVisibility(View.GONE);
            mAddfieldFieldPriceSwitch.setChecked(true);
            mIsEnquiryInt = 0;
            mAddfieldFieldPriceEnquiryLL.setVisibility(View.GONE);
            mAddfieldFieldPriceUnitLL.setVisibility(View.VISIBLE);
            mAddfieldEditorPriceBtn.setText(getResources().getString(R.string.addfield_next_btn_str));
        } else {
            mAddfieldFieldPriceRL.setVisibility(View.VISIBLE);
        }
        initEnquiryView();
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        if ((width -40)/4 > 174 ||(width -40)/4 == 174 || (width -40)/4 > 166) {
            GridviewNumColumns = 4;
            maddfield_price_default_size_gridview.setNumColumns(4);
        } else  {
            GridviewNumColumns = 3;
            maddfield_price_default_size_gridview.setNumColumns(3);
        }
        addWeekData();
        data = new ArrayList<>();//资源规格整理为list
        if (Field_AddResourcesModel.getInstance().getOptions() != null && Field_AddResourcesModel.getInstance().getOptions().getLease_term_type() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size() > 0) {
            HashMap<Object,Object> map = new HashMap<Object,Object>();
            map.put("denominatedunit",getResources().getString(R.string.addfield_edit_price_day_unit_str));
            map.put("m_lease_term_type_id",-1);
            data.add(map);
            //场地判断是否选中计价单位
            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() != 3) {
                for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size(); i++) {
                    if (mWeekLeaseTermIdList != null &&
                            mWeekLeaseTermIdList.size() > 0 &&
                            mWeekLeaseTermIdList.contains(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()))) {
                        continue;
                    }
                    HashMap<Object,Object> dayMap = new HashMap<Object,Object>();
                    dayMap.put("denominatedunit",Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                    dayMap.put("m_lease_term_type_id",Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                    data.add(dayMap);
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                            for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                                if (mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                                    if (lease_term_type_id.size() == 0 || (lease_term_type_id.size() > 0
                                            && !lease_term_type_id.contains("-1"))) {
                                        lease_term_type_id.add("-1");
                                        break;
                                    } else if (lease_term_type_id.size() > 0 && lease_term_type_id.contains("-1")) {
                                        break;
                                    }
                                } else {
                                    if (String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                                        lease_term_type_id.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //活动判断是否选中计价单位
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null &&
                    Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                    for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                        if (mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                            if (lease_term_type_id.size() == 0 || (lease_term_type_id.size() > 0
                                    && !lease_term_type_id.contains("-1"))) {
                                lease_term_type_id.add("-1");
                                break;
                            } else if (lease_term_type_id.size() > 0 && lease_term_type_id.contains("-1")) {
                                break;
                            }
                        }
                    }
                }
            }

        }
        if (Field_AddResourcesModel.getInstance().getDenominatedunitlist() != null &&
                Field_AddResourcesModel.getInstance().getDenominatedunitlist().size() > 0) {
            for (int i = 0; i <data.size(); i++) {
                if (Field_AddResourcesModel.getInstance().getDenominatedunitlist().contains(data.get(i).get("m_lease_term_type_id").toString())) {
                    Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().put(data.get(i).get("denominatedunit").toString(),true);
                } else {
                    Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().put(data.get(i).get("denominatedunit").toString(),false);
                }
            }
        } else {
            for (int i = 0; i <data.size(); i++) {
                if (lease_term_type_id.contains(data.get(i).get("m_lease_term_type_id").toString())) {
                    Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().put(data.get(i).get("denominatedunit").toString(),true);
                } else {
                    Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().put(data.get(i).get("denominatedunit").toString(),false);
                }
            }
        }
        addfieldfour_denominated_adapter = new Field_AddField_FourDenominatedUnitAdapter(this,this,data,1);
        maddfieldfour_denominated_gridview.setAdapter(addfieldfour_denominated_adapter);
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null) {
            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                if (sizelist != null && custom_dimension_list != null) {
                    for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize() != null &&
                                !(sizelist.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize().toString()))) {
                            sizelist.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize().toString());
                        }
                        if (!(custom_dimension_list.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString()))) {
                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() > 0) {
                                custom_dimension_list.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString());
                            }
                        }
                    }
                }
                ArrayList<String> sizestrlist = new ArrayList<String>();
                ArrayList<Map<String, String>> denominatedunitlist = new ArrayList<Map<String, String>>();//规格中的时间的list
                ArrayList<Map<String, String>> custom_dimensionlist = new ArrayList<Map<String, String>>();//特殊品类的list
                ArrayList<String> mLeaseTermTypeIdList = new ArrayList<>();
                ArrayList<String> mCustomDimensionList = new ArrayList<>();
                for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize() != null &&
                            (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize().toString().length() > 0)) {
                        if (!sizestrlist.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize().toString())) {
                            sizestrlist.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize().toString());
                        }
                    }
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getId() > 0) {
                        //编辑的时候会返回id + name
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type().toString().length() > 0 &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString().length() > 0) {
                            if (!mLeaseTermTypeIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                                mLeaseTermTypeIdList.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                JSONObject jsonObject = JSONObject.parseObject(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type().toString());
                                if (jsonObject.get("display_name") != null &&
                                        jsonObject.get("display_name").toString().length() > 0) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("denominatedunit", jsonObject.get("display_name").toString());
                                    map.put("m_lease_term_type_id", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                    denominatedunitlist.add(map);
                                }
                            }
                        }
                    } else {
                        //新增的时候只有id 需要循环找出 id + name
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id() != null &&
                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString().length() > 0) {
                            if (!mLeaseTermTypeIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                                mLeaseTermTypeIdList.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                for (int k = 0; k < data.size(); k++) {
                                    if (data.get(k).get("m_lease_term_type_id").toString().
                                            equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString())) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("denominatedunit", data.get(k).get("denominatedunit").toString());
                                        map.put("m_lease_term_type_id", data.get(k).get("m_lease_term_type_id").toString());
                                        denominatedunitlist.add(map);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension() != null) {
                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() > 0) {
                            if (!mCustomDimensionList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString())) {
                                mCustomDimensionList.add(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString());
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("custom_dimension", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString());
                                custom_dimensionlist.add(map);
                            }
                        }
                    }
                }
                Field_AddResourcesModel.getInstance().setSizelist(sizestrlist);
                Field_AddResourcesModel.getInstance().setLease_term_type_id_list(denominatedunitlist);
                Field_AddResourcesModel.getInstance().setCustom_dimension_field_list(custom_dimensionlist);
            }
        }
//        代理规则
//        if (Field_AddResourcesModel.getInstance().getResource().getCooperation_type_id() != null &&
//                Field_AddResourcesModel.getInstance().getResource().getCooperation_type_id() == 13) {
//            customsizelist = new ArrayList<HashMap<String, String>>();//输入的场地大小的list
//            if (Field_AddResourcesModel.getInstance().getResource().getSize() != null &&
//                    Field_AddResourcesModel.getInstance().getResource().getSize().size() > 0) {
//                for (int i = 0; i < Field_AddResourcesModel.getInstance().getResource().getSize().size(); i++) {
//                    String[] size = Field_AddResourcesModel.getInstance().getResource().getSize().get(i).toString().split("\\*");
//                    HashMap<String, String> map_one = new HashMap<String, String>();
//                    map_one.put("fieldsize_left", size[0]);
//                    map_one.put("fieldsize_right", size[1]);
//                    map_one.put("denominatedunit", size[0] + "×" + size[1]);
//                    map_one.put("fieldsize_str", Field_AddResourcesModel.getInstance().getResource().getSize().get(i).toString());
//                    customsizelist.add(map_one);
//                }
//            }
//            if (Field_AddResourcesModel.getInstance().getCustomsizelist() != null &&
//                    Field_AddResourcesModel.getInstance().getCustomsizelist().size() > 0) {
//                for (int i = 0; i < customsizelist.size(); i++) {
//                    if (Field_AddResourcesModel.getInstance().getCustomsizelist().contains(customsizelist.get(i).get("fieldsize_str"))) {
//                        Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),true);
//                    } else {
//                        Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),false);
//                    }
//                }
//
//            } else {
//                for (int i = 0; i < customsizelist.size(); i++) {
//                    if (sizelist.size() > 0 && sizelist.contains(customsizelist.get(i).get("fieldsize_str"))) {
//                        Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),true);
//                    } else {
//                        Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),false);
//                    }
//                }
//            }
//            field_addField_fieldPriceAdapter = new Field_AddField_FieldPriceAdapter(Field_AddField_FieldPricesActivity.this,Field_AddField_FieldPricesActivity.this,customsizelist,0);
//            maddfield_price_default_size_gridview.setAdapter(field_addField_fieldPriceAdapter);
//            mAddfieldCustomSizeLL.setVisibility(View.GONE);
//        }
        customsizelist = new ArrayList<HashMap<String, String>>();//输入的场地大小的list
        HashMap<String, String> map_one = new HashMap<String, String>();
        map_one.put("fieldsize_left", "1");
        map_one.put("fieldsize_right", "2");
        map_one.put("denominatedunit", "1×2");
        map_one.put("fieldsize_str", "1*2");
        HashMap<String, String> map_two = new HashMap<String, String>();
        map_two.put("fieldsize_left", "2");
        map_two.put("fieldsize_right", "2");
        map_two.put("denominatedunit", "2×2");
        map_two.put("fieldsize_str", "2*2");
        HashMap<String, String> map_three = new HashMap<String, String>();
        map_three.put("fieldsize_left", "3");
        map_three.put("fieldsize_right", "3");
        map_three.put("denominatedunit", "3×3");
        map_three.put("fieldsize_str", "3*3");
        HashMap<String, String> map_four = new HashMap<String, String>();
        map_four.put("fieldsize_left", "5");
        map_four.put("fieldsize_right", "5");
        map_four.put("denominatedunit", "5×5");
        map_four.put("fieldsize_str", "5*5");
        customsizelist.add(map_one);
        customsizelist.add(map_two);
        customsizelist.add(map_three);
        customsizelist.add(map_four);
        ArrayList<String> default_sizelist_tmp = new ArrayList<>();//默认的场地大小list
        if (Field_AddResourcesModel.getInstance().getCustomsizelist() != null &&
                Field_AddResourcesModel.getInstance().getCustomsizelist().size() > 0) {
            for (int i = 0; i < customsizelist.size(); i++) {
                if ( Field_AddResourcesModel.getInstance().getCustomsizelist().contains(customsizelist.get(i).get("fieldsize_str"))) {
                    Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),true);
                    default_sizelist_tmp.add(customsizelist.get(i).get("fieldsize_str"));
                } else {
                    Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),false);
                }
            }
        } else {
            for (int i = 0; i < customsizelist.size(); i++) {
                if (sizelist.size() > 0 && sizelist.contains(customsizelist.get(i).get("fieldsize_str"))) {
                    Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),true);
                    default_sizelist_tmp.add(customsizelist.get(i).get("fieldsize_str"));
                } else {
                    Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().put(customsizelist.get(i).get("fieldsize_str"),false);
                }
            }
        }
        field_addField_fieldPriceAdapter = new Field_AddField_FieldPriceAdapter(Field_AddField_FieldPricesActivity.this,Field_AddField_FieldPricesActivity.this,customsizelist,0);
        maddfield_price_default_size_gridview.setAdapter(field_addField_fieldPriceAdapter);

        mAddfieldCustomSizeLL.setVisibility(View.VISIBLE);
        if (Field_AddResourcesModel.getInstance().getFieldsizelist() != null &&
                Field_AddResourcesModel.getInstance().getFieldsizelist().size() > 0) {
            datefieldsizelist = Field_AddResourcesModel.getInstance().getFieldsizelist();
        } else {
            if (sizelist != null) {
                for (int i = 0; i < sizelist.size(); i++) {
                    if (!default_sizelist_tmp.contains(sizelist.get(i))) {
                        HashMap<String,String> map = new HashMap<>();
                        String[] size =sizelist.get(i).toString().split("\\*");
                        map.put("edittext_left", size[0]);
                        map.put("edittext_right", size[1]);
                        datefieldsizelist.add(map);
                    }
                }
            }
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("edittext_left", "");
        map.put("edittext_right", "");
        datefieldsizelist.add(map);
        for (int i = 0; i <datefieldsizelist.size(); i++) {
            if (i == datefieldsizelist.size() - 1) {
                Addfield_fourlisviewadapter.getIsSelected_addfield_fieldsize().put(i,3);
            } else {
                Addfield_fourlisviewadapter.getIsSelected_addfield_fieldsize().put(i,1);
            }
        }
        fieldsizeadapter = new Addfield_fourlisviewadapter(this,this,datefieldsizelist,0);
        maddfield_fieldsizelistview.setAdapter(fieldsizeadapter);

        HashMap<String, String> custom_dimension_map_one = new HashMap<String, String>();
        custom_dimension_map_one.put("custom_dimension", getResources().getString(R.string.addfield_default_custom_dimension_one_text));
        HashMap<String, String> custom_dimension_map_two = new HashMap<String, String>();
        custom_dimension_map_two.put("custom_dimension", getResources().getString(R.string.addfield_default_custom_dimension_two_text));
        HashMap<String, String> custom_dimension_map_three = new HashMap<String, String>();
        custom_dimension_map_three.put("custom_dimension", getResources().getString(R.string.addfield_default_custom_dimension_three_text));
        default_custom_dimensionList.add(custom_dimension_map_one);
        default_custom_dimensionList.add(custom_dimension_map_two);
        default_custom_dimensionList.add(custom_dimension_map_three);

        ArrayList<String> default_custom_dimension_list_tmp = new ArrayList<>();//默认的固定特殊规格
        if (Field_AddResourcesModel.getInstance().getDefault_dimensionlist() != null &&
                Field_AddResourcesModel.getInstance().getDefault_dimensionlist().size() > 0) {
            for (int i = 0; i < default_custom_dimensionList.size(); i++) {
                if (Field_AddResourcesModel.getInstance().getDefault_dimensionlist().contains(default_custom_dimensionList.get(i).get("custom_dimension"))) {
                    Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().put(default_custom_dimensionList.get(i).get("custom_dimension"),true);
                    default_custom_dimension_list_tmp.add(default_custom_dimensionList.get(i).get("custom_dimension"));
                } else {
                    Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().put(default_custom_dimensionList.get(i).get("custom_dimension"),false);
                }
            }
        } else {
            for (int i = 0; i < default_custom_dimensionList.size(); i++) {
                if (custom_dimension_list.size() > 0 && custom_dimension_list.contains(default_custom_dimensionList.get(i).get("custom_dimension"))) {
                    Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().put(default_custom_dimensionList.get(i).get("custom_dimension"),true);
                    default_custom_dimension_list_tmp.add(default_custom_dimensionList.get(i).get("custom_dimension"));
                } else {
                    Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().put(default_custom_dimensionList.get(i).get("custom_dimension"),false);
                }
            }

        }
        field_addField_fieldPriceAdapter = new Field_AddField_FieldPriceAdapter(Field_AddField_FieldPricesActivity.this,Field_AddField_FieldPricesActivity.this,default_custom_dimensionList,1);
        maddfield_default_special_fieldsizelistview.setAdapter(field_addField_fieldPriceAdapter);
        if (Field_AddResourcesModel.getInstance().getCustom_dimensionlist() != null &&
                Field_AddResourcesModel.getInstance().getCustom_dimensionlist().size() > 0) {
            datefieldSpecialList = Field_AddResourcesModel.getInstance().getCustom_dimensionlist();
        } else {
            if (custom_dimension_list != null) {
                for (int i = 0; i < custom_dimension_list.size(); i++) {
                    if (!default_custom_dimension_list_tmp.contains(custom_dimension_list.get(i))) {
                        HashMap<String,String> custom_dimension_map = new HashMap<>();
                        custom_dimension_map.put("edittext_special", custom_dimension_list.get(i));
                        datefieldSpecialList.add(custom_dimension_map);
                    }
                }
            }

        }
        HashMap<String,String> specialmap = new HashMap<>();
        specialmap.put("edittext_special", "");
        datefieldSpecialList.add(specialmap);
        for (int i = 0; i <datefieldSpecialList.size(); i++) {
            if (i == datefieldSpecialList.size() - 1) {
                Field_AddField_FourSpecialAdapter.getIsSelected_addfield_fieldsize().put(i,3);
            } else {
                Field_AddField_FourSpecialAdapter.getIsSelected_addfield_fieldsize().put(i,1);
            }
        }
        field_addField_fourSpecialAdapter = new Field_AddField_FourSpecialAdapter(this,this,datefieldSpecialList,0);
        maddfield_special_fieldsizelistview.setAdapter(field_addField_fourSpecialAdapter);

        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_fourdenominatedunit_titletxt));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
                if (checkInput_info_full()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                    if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                            && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
                            mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
                        Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                    }
                    savadata();
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
                    Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
                    savadata();
                }
                finish();
            }
        });
        mAddfieldEditorPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                        && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
                        mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
                    if (checkInput_info_full()) {
                        Intent serciceIntent = new Intent(Field_AddField_FieldPricesActivity.this,
                                FieldAddFieldServicesItemsActivity.class);
                        startActivity(serciceIntent);
                        Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                        Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                        savadata();
                    }
                } else {
                    //跳到编辑价格|检查输入的规格是否符合
                    ArrayList<Map<String, String>> fieldsizelist = new ArrayList<Map<String, String>>();//输入的场地大小的list
                    ArrayList<Map<String, String>> denominatedunitlist = new ArrayList<Map<String, String>>();//规格中的时间的list
                    ArrayList<Map<String, String>> custom_dimensionlist = new ArrayList<Map<String, String>>();//特殊品类的list

                    for (int i = 0; i < customsizelist.size(); i++) {
                        if (Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().get(customsizelist.get(i).get("fieldsize_str"))) {
                            fieldsizelist.add(customsizelist.get(i));
                        }
                    }
                    for (int i = 0; i < default_custom_dimensionList.size(); i++) {
                        if (Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().get(default_custom_dimensionList.get(i).get("custom_dimension"))) {
                            custom_dimensionlist.add(default_custom_dimensionList.get(i));
                        }
                    }

                    for (int i = 0; i < maddfield_fieldsizelistview.getChildCount(); i++) {
                        LinearLayout layout = (LinearLayout) maddfield_fieldsizelistview.getChildAt(i);// 获得子item的layout
                        EditText et = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_left);// 从layout中获得控件,根据其id
                        EditText et_right = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_right);// 从layout中获得控件,根据其id
                        if (et.getVisibility() == View.VISIBLE) {
                            if (Addfield_fourlisviewadapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                                if ((et.getText().toString().length() > 0 && et_right.getText().toString().length() > 0) &&
                                        (Double.parseDouble(et.getText().toString().trim()) > 0 &&
                                                Double.parseDouble(et_right.getText().toString().trim()) > 0)) {
                                    if ((isFloathString(et.getText().toString()) == 0 && isFloathString(et_right.getText().toString()) == 0)) {
                                        boolean size_repeat = false;//检查是否有重复的size
                                        for (int j = 0; j < fieldsizelist.size(); j++) {
                                            if ((Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_left"),1).equals(Constants.getpricestring(et.getText().toString(),1))&&
                                                    Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_right"),1).equals(Constants.getpricestring(et_right.getText().toString(),1))) ||
                                                    (Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_left"),1).equals(Constants.getpricestring(et_right.getText().toString(),1))&&
                                                            Constants.getpricestring(fieldsizelist.get(j).get("fieldsize_right"),1).equals(Constants.getpricestring(et.getText().toString(),1)))) {
                                                hideProgressDialog();
//                                                    et.setText("");
//                                                    et_right.setText("");
                                                size_repeat = true;
                                                BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_repeat_txt));
                                                return;
                                            }
                                        }
                                        if (size_repeat == false ) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("fieldsize_left", et.getText().toString());
                                            map.put("fieldsize_right", et_right.getText().toString());
                                            fieldsizelist.add(map);
                                        } else {
                                            hideProgressDialog();
                                            fieldsizelist.clear();
                                            return;
                                        }
                                    } else {
                                        hideProgressDialog();
//                                            et.setText("");
//                                            et_right.setText("");
                                        fieldsizelist.clear();
                                        BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_correct_txt));
                                        return;
                                    }
                                } else {
                                    hideProgressDialog();
                                    fieldsizelist.clear();
                                    BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_correct_txt));
                                    return;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < maddfield_special_fieldsizelistview.getChildCount(); i++) {
                        LinearLayout layout = (LinearLayout) maddfield_special_fieldsizelistview.getChildAt(i);// 获得子item的layout
                        EditText et = (EditText) layout.findViewById(R.id.addfield_denominated_special_edittext);// 从layout中获得控件,根据其id
                        if (et.getVisibility() == View.VISIBLE) {
                            if (Field_AddField_FourSpecialAdapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                                if (et.getText().toString().length() > 0) {
                                    boolean size_repeat = false;
                                    for (int j = 0; j < custom_dimensionlist.size(); j++) {
                                        if (custom_dimensionlist.get(j).get("custom_dimension").trim().equals(et.getText().toString().trim())) {
                                            hideProgressDialog();
//                                                et.setText("");
                                            size_repeat = true;
                                            BaseMessageUtils.showToast(getResources().getString(R.string.search_fieldlist_size_repeat_txt));
                                            return;
                                        }
                                    }
                                    if (size_repeat == false ) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("custom_dimension", et.getText().toString());
                                        custom_dimensionlist.add(map);
                                    } else {
                                        hideProgressDialog();
                                        custom_dimensionlist.clear();
                                        return;
                                    }
                                } else {
                                    hideProgressDialog();
                                    custom_dimensionlist.clear();
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_five_addfield_special_hinttxt));
                                    return;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < data.size(); i++) {
                        if (Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().get(data.get(i).get("denominatedunit").toString())) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("denominatedunit", data.get(i).get("denominatedunit").toString());
                            map.put("m_lease_term_type_id", data.get(i).get("m_lease_term_type_id").toString());
                            denominatedunitlist.add(map);
                        }
                    }
                    if (custom_dimensionlist.size() > 0) {
                        if (custom_dimensionlist.size() * denominatedunitlist.size() +custom_dimensionlist.size() * denominatedunitlist.size() * fieldsizelist.size() > 50) {
                            hideProgressDialog();
                            BaseMessageUtils.showToast(Field_AddField_FieldPricesActivity.this.getResources().getString(R.string.addfield_four_addfield_morethan_max_specifications_hinttxt));
                            return;
                        }
                    } else {
                        if (denominatedunitlist.size() * fieldsizelist.size() > 50) {
                            hideProgressDialog();
                            BaseMessageUtils.showToast(Field_AddField_FieldPricesActivity.this.getResources().getString(R.string.addfield_four_addfield_morethan_max_specifications_hinttxt));
                            return;
                        }
                    }

                    if (denominatedunitlist.size() > 0 && fieldsizelist.size() > 0) {
                        ArrayList<String> fieldsize_default_tmp = new ArrayList<String>();
                        ArrayList<String> sizestrlist = new ArrayList<String>();
                        for (int i = 0; i < customsizelist.size(); i++) {
                            if (Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().get(customsizelist.get(i).get("fieldsize_str"))) {
                                fieldsize_default_tmp.add(customsizelist.get(i).get("fieldsize_str"));
                                sizestrlist.add(customsizelist.get(i).get("fieldsize_str"));
                            }
                        }
                        for (int i = 0; i < fieldsize_default_tmp.size(); i++) {
                            for (int j = 0; j < fieldsizelist.size(); j++) {
                                if (fieldsizelist.get(j).get("fieldsize_str") != null) {
                                    if (fieldsize_default_tmp.get(i).equals(fieldsizelist.get(j).get("fieldsize_str"))) {
                                        fieldsizelist.remove(j);
                                        break;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < fieldsizelist.size(); i++) {
                            sizestrlist.add(Constants.getpricestring(fieldsizelist.get(i).get("fieldsize_left").toString(), 1) + "*" + Constants.getpricestring(fieldsizelist.get(i).get("fieldsize_right").toString(), 1));
                        }

                        Intent it = new Intent(Field_AddField_FieldPricesActivity.this, Field_AddField_FiveEditerPriceActivity.class);
                        it.putExtra("fieldsizelist", (Serializable) sizestrlist);
                        it.putExtra("denominatedunitlist", (Serializable) denominatedunitlist);
                        if (custom_dimensionlist.size() > 0) {
                            it.putExtra("custom_dimension", (Serializable) custom_dimensionlist);
                        }
                        it.putExtra("WeekLeaseTermMap", (Serializable) mWeekLeaseTermMap);
                        it.putExtra("WeekLeaseTermNameMap", (Serializable) mWeekLeaseTermNameMap);
                        it.putExtra("WeekLeaseTermIdList", (Serializable) mWeekLeaseTermIdList);
                        startActivity(it);
                        Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                        savadata();
                    } else {
                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_five_addfield_special_pointtxt));
                    }
                }
            }
        });
    }
    @Override
    public void getAddfieldFouritemcall(int position) {
        editorsizetxt = false;
        if (position == -1) {
            HashMap<String,String> map = new HashMap<>();
            map.put("edittext_left", "");
            map.put("edittext_right", "");
            datefieldsizelist.add(map);
            fieldsizeadapter.notifyDataSetChanged();
        } else if (position != -1) {

        }
    }
    public void getconfigurate(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.AddfieldFouritemcall(this);
    }

    // 判断某个字符串是否是整数字符串，若是数字字符串返回0，若不是则返回-1
    private int isNumberString(String testString)
    {
        String numAllString="0123456789";
        if(testString.length()<=0)
            return -1;
        for(int i=0;i<testString.length();i++)
        {
            String charInString=testString.substring(i, i+1);
            if(!numAllString.contains(charInString))
                return -1;
        }
        return 0;
    }
    // 判断某个字符串是否是float字符串，若是返回0，若不是则返回-1
    public int isFloathString(String testString)
    {
        if(!testString.contains("."))
        {
            return isNumberString(testString);
        }
        else
        {
            String[] floatStringPartArray=testString.split("\\.");
            if(floatStringPartArray.length==2)
            {
                if(0==isNumberString(floatStringPartArray[0])&&0==isNumberString(floatStringPartArray[1]))
                    return 0;
                else
                    return -1;
            }
            else
                return -1;

        }

    }


    @Override
    public void getAddfieldFourSpecialcall(int position) {
        editor_special_txt = false;
        if (position == -1) {
            HashMap<String,String> specialmap = new HashMap<>();
            specialmap.put("edittext_special","");
            datefieldSpecialList.add(specialmap);
            field_addField_fourSpecialAdapter.notifyDataSetChanged();
        }
    }
    public void add_speciallistitem(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.getAddfieldFourSpecialcall(this);
    }
    private void savadata() {
        if (mIsEnquiryInt == 1 && !mAddfieldFieldPriceSwitch.isChecked()
                && mAddfieldFieldPriceEnquiryLL.getVisibility() == View.VISIBLE &&
                mAddfieldFieldPriceUnitLL.getVisibility() == View.GONE) {
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setIs_enquiry(1);
            if (mAddfieldPriceEnquiryMinPriceET.getText().toString().trim().length() > 0) {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setRefer_min_price(Integer.parseInt(Constants.getpricestring(mAddfieldPriceEnquiryMinPriceET.getText().toString().trim(),100)));
            } else {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setRefer_min_price(null);
            }
            if (mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim().length() > 0) {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setRefer_max_price(Integer.parseInt(Constants.getpricestring(mAddfieldPriceEnquiryMaxPriceET.getText().toString().trim(),100)));
            } else {
                Field_AddResourcesModel.getInstance().getResource().getResource_data().setRefer_max_price(null);
            }
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setProcess(checkBoxInt[0]);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setGoal(checkBoxInt[1]);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setEffect_pic(checkBoxInt[2]);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setLicense(checkBoxInt[3]);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setCopying_of_id_card(checkBoxInt[4]);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setM_else(mAddfieldPriceEnquiryET.getText().toString().trim());
            ArrayList<FieldAddfieldSellResDimensionsModel> dimensions = new ArrayList<>();
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensions);
        } else {
            List<String> customsizelist_tmp = new ArrayList<>();
            for (int i = 0; i < customsizelist.size(); i++) {
                if (Field_AddField_FieldPriceAdapter.getIsSelected_addfield_custom_size().get(customsizelist.get(i).get("fieldsize_str"))) {
                    customsizelist_tmp.add(customsizelist.get(i).get("fieldsize_str").toString());
                }
            }
            Field_AddResourcesModel.getInstance().setCustomsizelist(customsizelist_tmp);
            List<String> default_custom_dimensionList_tmp = new ArrayList<>();
            for (int i = 0; i < default_custom_dimensionList.size(); i++) {
                if (Field_AddField_FieldPriceAdapter.getIsSelected_custom_dimension().get(default_custom_dimensionList.get(i).get("custom_dimension"))) {
                    default_custom_dimensionList_tmp.add(default_custom_dimensionList.get(i).get("custom_dimension"));
                }
            }
            Field_AddResourcesModel.getInstance().setDefault_dimensionlist(default_custom_dimensionList_tmp);
            ArrayList<HashMap<String,String>> fieldsizelist = new ArrayList<>();
            for (int i = 0; i < maddfield_fieldsizelistview.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) maddfield_fieldsizelistview.getChildAt(i);// 获得子item的layout
                EditText et = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_left);// 从layout中获得控件,根据其id
                EditText et_right = (EditText) layout.findViewById(R.id.addfield_price_denominated_edittext_right);// 从layout中获得控件,根据其id
                if (et.getVisibility() == View.VISIBLE) {
                    if (Addfield_fourlisviewadapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("edittext_left", et.getText().toString());
                        map.put("edittext_right", et_right.getText().toString());
                        fieldsizelist.add(map);
                    }
                }
            }
            Field_AddResourcesModel.getInstance().setFieldsizelist(fieldsizelist);
            ArrayList<HashMap<String,String>> custom_dimensionlist_tmp = new ArrayList<>();
            for (int i = 0; i < maddfield_special_fieldsizelistview.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) maddfield_special_fieldsizelistview.getChildAt(i);// 获得子item的layout
                EditText et = (EditText) layout.findViewById(R.id.addfield_denominated_special_edittext);// 从layout中获得控件,根据其id
                if (et.getVisibility() == View.VISIBLE) {
                    if (Field_AddField_FourSpecialAdapter.getIsSelected_addfield_fieldsize().get(i) == 1) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("edittext_special", et.getText().toString());
                        custom_dimensionlist_tmp.add(map);
                    }
                }
            }
            Field_AddResourcesModel.getInstance().setCustom_dimensionlist(custom_dimensionlist_tmp);
            List<String> denominatedunitlist = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (Field_AddField_FourDenominatedUnitAdapter.getIsSelected_addfielddenominatedunit().get(data.get(i).get("denominatedunit").toString())) {
                    denominatedunitlist.add(data.get(i).get("m_lease_term_type_id").toString());
                }
            }
            Field_AddResourcesModel.getInstance().setDenominatedunitlist(denominatedunitlist);
            Field_AddResourcesModel.getInstance().getResource().getResource_data().setIs_enquiry(0);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            if (checkInput_info_full()) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(1);
                savadata();
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_price_standard(0);
            }
            savadata();
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    //周几的配置 id name
    private void addWeekData() {
        if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type() != null &&
                Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size() > 0) {
            for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size(); i++) {
                if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getPeriod() == 1) {
                    if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Mon")) {
                        mWeekLeaseTermMap.put(0,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(0,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Tue")) {
                        mWeekLeaseTermMap.put(1,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(1,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Wed")) {
                        mWeekLeaseTermMap.put(2,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(2,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Thu")) {
                        mWeekLeaseTermMap.put(3,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(3,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Fri")) {
                        mWeekLeaseTermMap.put(4,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(4,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Sat")) {
                        mWeekLeaseTermMap.put(5,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(5,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    } else if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Sun")) {
                        mWeekLeaseTermMap.put(6,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId());
                        mWeekLeaseTermNameMap.put(6,Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getDisplay_name());
                        mWeekLeaseTermIdList.add(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId()));
                    }
                }
            }
        }
    }
    private void initEnquiryView() {
        for (int i = 0; i < checkBoxList.length; i++) {
            int id = getResources().getIdentifier("addfield_enquiry_label" + String.valueOf(i), "id", getPackageName());
            CheckBox checkBox = (CheckBox) findViewById(id);
            checkBoxList[i]= (checkBox);
            checkBoxInt[i] = 0;
        }
        checkBoxList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxList[0].isChecked()) {
                    checkBoxInt[0] = 1;
                } else {
                    checkBoxInt[0] = 0;
                }
            }
        });
        checkBoxList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxList[1].isChecked()) {
                    checkBoxInt[1] = 1;
                } else {
                    checkBoxInt[1] = 0;
                }
            }
        });
        checkBoxList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxList[2].isChecked()) {
                    checkBoxInt[2] = 1;
                } else {
                    checkBoxInt[2] = 0;
                }
            }
        });
        checkBoxList[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxList[3].isChecked()) {
                    checkBoxInt[3] = 1;
                } else {
                    checkBoxInt[3] = 0;
                }
            }
        });
        checkBoxList[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxList[4].isChecked()) {
                    checkBoxInt[4] = 1;
                } else {
                    checkBoxInt[4] = 0;
                }
            }
        });

        Constants.textchangelistener(mAddfieldPriceEnquiryMinPriceET);
        Constants.textchangelistener(mAddfieldPriceEnquiryMaxPriceET);
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_min_price() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_min_price() > 0) {
            mAddfieldPriceEnquiryMinPriceET.setText(Constants.getpricestring(
                    String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_min_price()),0.01));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_max_price() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_max_price() > 0) {
            mAddfieldPriceEnquiryMaxPriceET.setText(Constants.getpricestring(
                    String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getRefer_max_price()),0.01));
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getProcess() == 1) {
            checkBoxInt[0] = 1;
            checkBoxList[0].setChecked(true);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getGoal() == 1) {
            checkBoxInt[1] = 1;
            checkBoxList[1].setChecked(true);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getEffect_pic() == 1) {
            checkBoxInt[2] = 1;
            checkBoxList[2].setChecked(true);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getLicense() == 1) {
            checkBoxInt[3] = 1;
            checkBoxList[3].setChecked(true);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getCopying_of_id_card() == 1) {
            checkBoxInt[4] = 1;
            checkBoxList[4].setChecked(true);
        }
        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getM_else() != null &&
                Field_AddResourcesModel.getInstance().getResource().getResource_data().getM_else().length() > 0) {
            mAddfieldPriceEnquiryET.setText(Field_AddResourcesModel.getInstance().getResource().getResource_data().getM_else());
        }
    }
}
