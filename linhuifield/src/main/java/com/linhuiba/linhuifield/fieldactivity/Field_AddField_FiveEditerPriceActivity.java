package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;
import com.linhuiba.linhuifield.FieldBaseActivity;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldadapter.FieldAddFieldPriceUnitDayAdapter;
import com.linhuiba.linhuifield.fieldadapter.Field_AddField_FiveEditerPriceAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldEditPriceWeekChooseModel;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldSellResDimensionsModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.fieldview.FieldLoadMoreExpandablelistView;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuifield.util.TitleBarUtils;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/19.
 */
public class Field_AddField_FiveEditerPriceActivity extends FieldBaseMvpActivity {
    public FieldMyGridView maddfield_specialpricelistciew;
    private LinearLayout mspecial_listview_layout;
    private LinearLayout maddfield_editprice_cash_pledge_layout;
    private Switch maddfield_editprice_cash_pledge_checkbox;
    private Button mAddfieldEditorPriceBtn;
    private LinearLayout mAddfieldEditorPriceBtnLL;
    public FieldLoadMoreExpandablelistView mAddfieldEditPriceCommonDayGV;
    public FieldLoadMoreExpandablelistView mAddfieldEditPriceSpecialDayLV;
    private ArrayList<FieldAddfieldSellResDimensionsModel> dimensionslist = new ArrayList<>();//上传的规格的list
    private ArrayList<Map<String,String>> editorprice_special_list;//有特殊品类时的上一页组合的规格list
    private boolean onclick = false;
    private Field_AddField_FiveEditerPriceAdapter special_editorpriceadapter;
    private ArrayList<String> fieldsizelist;//上一页规格中大小list
    private ArrayList<Map<String,String>> denominatedunitlist;//上一页规格中时间list
    private ArrayList<Map<String,String>> custom_dimensionlist;//上一页规格中特殊品类list
    private FieldAddFieldPriceUnitDayAdapter unitDayAdapter;
    private FieldAddFieldPriceUnitDayAdapter unitDaySpecialAdapter;
    public List<AddfieldEditPriceWeekChooseModel> mCommonGroupDataList = new ArrayList<>();
    //由于mCommonChildDataList 和 mCommonGroupDataList的size一样（一个group只有一个child）所以就用
    //mCommonChildDataList循环了（少做一个grouplist的判断） 另外一个特殊天的也是 （在循环检查价格和提交规格数组时）
    public List<List<AddfieldEditPriceWeekChooseModel>> mCommonChildDataList = new ArrayList<>();
    public List<AddfieldEditPriceWeekChooseModel> mSpecialGroupDataList = new ArrayList<>();
    public List<List<AddfieldEditPriceWeekChooseModel>> mSpecialChildDataList = new ArrayList<>();
    private HashMap<Integer,Integer> mWeekLeaseTermMap = new HashMap<Integer,Integer>();//周几对应id
    public HashMap<Integer,String> mWeekLeaseTermNameMap = new HashMap<Integer,String>();//周几对应界面显示文字
    public List<String> mWeekLeaseTermIdList = new ArrayList<>();
    public boolean isChooseDeposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity_field_addfield_five_editerprice);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_editorpricetxt));
        TitleBarUtils.showBackImgclick(this, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput_info_full()) {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                    savadata();
                } else {
                    Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
                }
                Field_AddField_FiveEditerPriceActivity.this.finish();
            }
        });
        showProgressDialog();
        maddfield_specialpricelistciew = (FieldMyGridView)findViewById(R.id.addfield_specialpricelistciew);
        mspecial_listview_layout = (LinearLayout)findViewById(R.id.special_listview_layout);
        maddfield_editprice_cash_pledge_layout = (LinearLayout)findViewById(R.id.addfield_editprice_cash_pledge_layout);
        maddfield_editprice_cash_pledge_checkbox = (Switch)findViewById(R.id.addfield_editprice_cash_pledge_checkbox);
        mAddfieldEditorPriceBtn = (Button)findViewById(R.id.addfield_price_next_btn);
        mAddfieldEditorPriceBtnLL = (LinearLayout)findViewById(R.id.addfield_price_next_btn_ll);

        //2018/ 周几listView
        mAddfieldEditPriceCommonDayGV = (FieldLoadMoreExpandablelistView) findViewById(R.id.addfield_edit_price_unit_day_gv);
        mAddfieldEditPriceSpecialDayLV = (FieldLoadMoreExpandablelistView)findViewById(R.id.addfield_edit_price_unit_day_special_lv);
        maddfield_editprice_cash_pledge_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (!isChecked) {
                    checkInput_info_full();
                    savadata();
                }
                showProgressDialog();
                initData();
                if (isChecked) {
                    isChooseDeposit = true;
                } else {
                    isChooseDeposit = false;
                }
            }
        });
        fieldsizelist = new ArrayList<String>();
        denominatedunitlist = new ArrayList<Map<String,String>>();
        custom_dimensionlist = new ArrayList<Map<String,String>>();
        editorprice_special_list = new ArrayList<Map<String,String>>();
        Intent getintent = getIntent();
        fieldsizelist = (ArrayList<String>)getintent.getSerializableExtra("fieldsizelist");
        denominatedunitlist = (ArrayList<Map<String,String>>)getintent.getSerializableExtra("denominatedunitlist");
        custom_dimensionlist= (ArrayList<Map<String,String>>)getintent.getSerializableExtra("custom_dimension");
        mWeekLeaseTermMap = (HashMap<Integer, Integer>) getintent.getSerializableExtra("WeekLeaseTermMap");
        mWeekLeaseTermNameMap = (HashMap<Integer, String>) getintent.getSerializableExtra("WeekLeaseTermNameMap");
        mWeekLeaseTermIdList = (List<String>) getintent.getSerializableExtra("WeekLeaseTermIdList");
        Field_AddResourcesModel.getInstance().setSizelist(fieldsizelist);
        Field_AddResourcesModel.getInstance().setLease_term_type_id_list(denominatedunitlist);
        Field_AddResourcesModel.getInstance().setCustom_dimension_field_list(custom_dimensionlist);
        //由于数据处理慢所以要先处理完数据在加载完成按钮的点击事件
        initview();
    }
    private void initview() {
        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            TitleBarUtils.setnextViewText(Field_AddField_FiveEditerPriceActivity.this,getResources().getString(R.string.myselfinfo_save_pw));
            TitleBarUtils.shownextTextView(Field_AddField_FiveEditerPriceActivity.this,"",
                    17, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onclick == false) {
                                if (dimensionslist != null) {
                                    dimensionslist.clear();
                                }
                                mAddfieldEditPriceCommonDayGV.clearFocus();
                                mAddfieldEditPriceSpecialDayLV.clearFocus();
                                if (editorprice_special_list.size() > 0) {
                                    boolean price_null = false;
                                    for (int i = 0; i < maddfield_specialpricelistciew.getChildCount(); i++) {
                                        LinearLayout layout = (LinearLayout) maddfield_specialpricelistciew.getChildAt(i);// 获得子item的layout
                                        EditText et = (EditText) layout.findViewById(R.id.editprice_special_itemedit);//价格
                                        EditText depositET = (EditText) layout.findViewById(R.id.addfield_deposit_item_edit);//押金

                                        if ((isFloathString(et.getText().toString()) == 0 || isNumberString(et.getText().toString()) == 0) && et.getText().toString().length() > 0) {
                                            if ((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100)).length() > 10) {
//                                                        et.setText("");
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
//                                                        et.setText("");
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            //2018/1/4 double判断价格
                                            if (Double.parseDouble(et.getText().toString()) > 0) {
                                                price_null = true;
                                            }
                                            if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                                                    Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                                for (int k = 0; k < 7; k++) {
                                                    FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                    fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                    fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                                    fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                    fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                                                    fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                                    if (editorprice_special_list.get(i).get("id") != null &&
                                                            editorprice_special_list.get(i).get("id").length() > 0) {
                                                        fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                                                    } else {
                                                        fieldAddfieldSellResDimensionsModel.setId(0);
                                                    }
                                                    if (depositET.getText().toString().length() > 0) {
                                                        fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                                    } else {
                                                        fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                    }
                                                    dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                                }
                                            } else {
                                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                                                fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                                if (editorprice_special_list.get(i).get("id") != null &&
                                                        editorprice_special_list.get(i).get("id").length() > 0) {
                                                    fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                                                } else {
                                                    fieldAddfieldSellResDimensionsModel.setId(0);
                                                }
                                                if (depositET.getText().toString().length() > 0) {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                                } else {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                }
                                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                            }
                                        } else {
                                            if (et.getText().toString().length() == 0) {
                                                if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                                                        Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                                    for (int k = 0; k < 7; k++) {
                                                        FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                        fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                        fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                                        fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                        fieldAddfieldSellResDimensionsModel.setPrice("0");
                                                        if (depositET.getText().toString().length() > 0) {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                        }
                                                        fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                                        if (editorprice_special_list.get(i).get("id") != null &&
                                                                editorprice_special_list.get(i).get("id").length() > 0) {
                                                            fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setId(0);
                                                        }
                                                        dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                                    }
                                                } else {
                                                    FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                    fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                    fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                                    fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                                                    fieldAddfieldSellResDimensionsModel.setPrice("0");
                                                    if (depositET.getText().toString().length() > 0) {
                                                        fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                                    } else {
                                                        fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                    }
                                                    fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                                    if (editorprice_special_list.get(i).get("id") != null &&
                                                            editorprice_special_list.get(i).get("id").length() > 0) {
                                                        fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                                                    } else {
                                                        fieldAddfieldSellResDimensionsModel.setId(0);
                                                    }
                                                    dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (!price_null) {
                                        boolean isWeek = false;
                                        if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                                            for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                                for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                                    if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                            mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                                            mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                        price_null = true;
                                                        if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                            return;
                                                        }
                                                        if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                            return;
                                                        }
                                                        if (checkChildWeekChoose(i,j,1)&&
                                                                mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                            isWeek = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                                            for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                                for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                                    if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                            mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                                            mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                        if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                            return;
                                                        }
                                                        if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                            return;
                                                        }
                                                        price_null = true;
                                                        if (checkChildWeekChoose(i,j,2)&&
                                                                mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                            isWeek = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!price_null) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                                            return;
                                        }
                                        if (!isWeek) {
                                            BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                                            return;
                                        }
                                    }
                                } else {
                                    boolean isPrice = false;
                                    boolean isWeek = false;
                                    if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                                        for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                            for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                                if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                        mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                                        mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                    isPrice = true;
                                                    if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                        return;
                                                    }
                                                    if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                        return;
                                                    }
                                                    if (checkChildWeekChoose(i,j,1)&&
                                                            mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                        isWeek = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                                        for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                            for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                                if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                        mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                                        mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                    if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                        return;
                                                    }
                                                    if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                        return;
                                                    }
                                                    isPrice = true;
                                                    if (checkChildWeekChoose(i,j,2)&&
                                                            mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                        isWeek = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!isPrice) {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                                        return;
                                    }
                                    if (!isWeek) {
                                        BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                                        return;
                                    }
                                }
                                //2018/1/5 周几规格整合 提交
                                if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                                    for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                        for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                            if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                    mCommonChildDataList.get(i).get(j).getItemChoose() == 1 &&
                                                    mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                                    mCommonChildDataList.get(i).get(j).getPrice().length() > 0 &&
                                                    checkChildWeekChoose(i,j,1)) {
                                                for (int k = 0; k < 7; k++) {
                                                    if (mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                                            mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                                        FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                        fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                        fieldAddfieldSellResDimensionsModel.setSize(mCommonGroupDataList.get(i).getSize());
                                                        fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                        fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100));
                                                        if (mCommonChildDataList.get(i).get(j).getDeposit() != null &&
                                                                mCommonChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit(mCommonChildDataList.get(i).get(j).getDeposit());
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                        }

                                                        fieldAddfieldSellResDimensionsModel.setCustom_dimension(mCommonGroupDataList.get(i).getCustom_dimension());
                                                        if (mCommonChildDataList.get(i).get(j).getChild_week_id().get(k) != null) {
                                                            fieldAddfieldSellResDimensionsModel.setId(mCommonChildDataList.get(i).get(j).getChild_week_id().get(k));
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setId(0);
                                                        }
                                                        dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                                if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                                    for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                        for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                            if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                    mSpecialChildDataList.get(i).get(j).getItemChoose() == 1 &&
                                                    mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                                    mSpecialChildDataList.get(i).get(j).getPrice().length() > 0 &&
                                                    checkChildWeekChoose(i,j,2)) {
                                                for (int k = 0; k < 7; k++) {
                                                    if (mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                                            mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                                        FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                        fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                        fieldAddfieldSellResDimensionsModel.setSize(mSpecialGroupDataList.get(i).getSize());
                                                        fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                        fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100));
                                                        if (mSpecialChildDataList.get(i).get(j).getDeposit() != null &&
                                                                mSpecialChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit(mSpecialChildDataList.get(i).get(j).getDeposit());
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                        }
                                                        fieldAddfieldSellResDimensionsModel.setCustom_dimension(mSpecialGroupDataList.get(i).getCustom_dimension());
                                                        if (mSpecialChildDataList.get(i).get(j).getChild_week_id().get(k) != null) {
                                                            fieldAddfieldSellResDimensionsModel.setId(mSpecialChildDataList.get(i).get(j).getChild_week_id().get(k));
                                                        } else {
                                                            fieldAddfieldSellResDimensionsModel.setId(0);
                                                        }
                                                        dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensionslist);
                                    Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                                    Intent addfield_main_intent = new Intent(Field_AddField_FiveEditerPriceActivity.this,FieldAddFieldMainActivity.class);
                                    startActivity(addfield_main_intent);
                                } else {
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensionslist);
                                    Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                                    Intent serviceitem_intent = new Intent(Field_AddField_FiveEditerPriceActivity.this,FieldAddFieldServicesItemsActivity.class);
                                    startActivity(serviceitem_intent);
                                }
                            }
                        }
                    });
            mAddfieldEditorPriceBtnLL.setVisibility(View.GONE);
        } else {
            mAddfieldEditorPriceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclick == false) {
                        if (dimensionslist != null) {
                            dimensionslist.clear();
                        }
                        mAddfieldEditPriceCommonDayGV.clearFocus();
                        mAddfieldEditPriceSpecialDayLV.clearFocus();
                        if (editorprice_special_list.size() > 0) {
                            boolean price_null = false;
                            for (int i = 0; i < maddfield_specialpricelistciew.getChildCount(); i++) {
                                LinearLayout layout = (LinearLayout) maddfield_specialpricelistciew.getChildAt(i);// 获得子item的layout
                                EditText et = (EditText) layout.findViewById(R.id.editprice_special_itemedit);//
                                EditText depositET = (EditText) layout.findViewById(R.id.addfield_deposit_item_edit);//押金
                                if ((isFloathString(et.getText().toString()) == 0 || isNumberString(et.getText().toString()) == 0) && et.getText().toString().length() > 0) {
                                    if ((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100)).length() > 10) {
//                                                        et.setText("");
                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                        return;
                                    }
                                    if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
//                                                        et.setText("");
                                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                        return;
                                    }
                                    if (Double.parseDouble(et.getText().toString()) > 0) {
                                        price_null = true;
                                    }
                                    if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                                            Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                        for (int k = 0; k < 7; k++) {
                                            FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                            fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                            fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                            fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                            fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                                            if (depositET.getText().toString().length() > 0) {
                                                fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                            } else {
                                                fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                            }
                                            fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                            fieldAddfieldSellResDimensionsModel.setId(null);
                                            dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                        }
                                    } else {
                                        FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                        fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                        fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                        fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                                        fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                                        if (depositET.getText().toString().length() > 0) {
                                            fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                        } else {
                                            fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                        }
                                        fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                        fieldAddfieldSellResDimensionsModel.setId(null);
                                        dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                    }
                                } else {
                                    if (et.getText().toString().length() == 0) {
                                        if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                                                Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                            for (int k = 0; k < 7; k++) {
                                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                fieldAddfieldSellResDimensionsModel.setPrice("0");
                                                if (depositET.getText().toString().length() > 0) {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                                } else {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                }                                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                                fieldAddfieldSellResDimensionsModel.setId(null);
                                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                            }
                                        } else {
                                            FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                            fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                            fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                            fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                                            fieldAddfieldSellResDimensionsModel.setPrice("0");
                                            if (depositET.getText().toString().length() > 0) {
                                                fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                            } else {
                                                fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                            }
                                            fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                            fieldAddfieldSellResDimensionsModel.setId(null);
                                            dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            if (!price_null) {
                                boolean isWeek = false;
                                if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                                    for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                        for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                            if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                    mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                                    mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                price_null = true;
                                                if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                    return;
                                                }
                                                if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                    return;
                                                }
                                                if (checkChildWeekChoose(i,j,1)&&
                                                        mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                    isWeek = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                                    for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                        for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                            if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                    mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                                    mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                                                if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                    return;
                                                }
                                                if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                    return;
                                                }
                                                price_null = true;
                                                if (checkChildWeekChoose(i,j,2)&&
                                                        mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                    isWeek = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!price_null) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                                    return;
                                }
                                if (!isWeek) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                                    return;
                                }
                            }
                        } else {
                            boolean isPrice = false;
                            boolean isWeek = false;
                            if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                                for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                    for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                        if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                                mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                                            isPrice = true;
                                            if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            if (checkChildWeekChoose(i,j,1)&&
                                                    mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                isWeek = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                                for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                    for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                        if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                                mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                                mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                                            if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                                return;
                                            }
                                            isPrice = true;
                                            if (checkChildWeekChoose(i,j,2)&&
                                                    mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                                isWeek = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!isPrice) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                                return;
                            }
                            if (!isWeek) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                                return;
                            }
                        }
                        //2018/1/5 周几规格整合 提交
                        if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                            for (int i = 0; i < mCommonChildDataList.size(); i++) {
                                for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                                    if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                            mCommonChildDataList.get(i).get(j).getItemChoose() == 1 &&
                                            mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                            mCommonChildDataList.get(i).get(j).getPrice().length() > 0 &&
                                            checkChildWeekChoose(i,j,1)) {
                                        for (int k = 0; k < 7; k++) {
                                            if (mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                                    mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                fieldAddfieldSellResDimensionsModel.setSize(mCommonGroupDataList.get(i).getSize());
                                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100));
                                                if (mCommonChildDataList.get(i).get(j).getDeposit() != null &&
                                                        mCommonChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit(mCommonChildDataList.get(i).get(j).getDeposit());
                                                } else {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                }
                                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(mCommonGroupDataList.get(i).getCustom_dimension());
                                                fieldAddfieldSellResDimensionsModel.setId(null);
                                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                            for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                                for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                                    if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                            mSpecialChildDataList.get(i).get(j).getItemChoose() == 1 &&
                                            mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                            mSpecialChildDataList.get(i).get(j).getPrice().length() > 0 &&
                                            checkChildWeekChoose(i,j,2)) {
                                        for (int k = 0; k < 7; k++) {
                                            if (mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                                    mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                                fieldAddfieldSellResDimensionsModel.setSize(mSpecialGroupDataList.get(i).getSize());
                                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                                fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100));
                                                if (mSpecialChildDataList.get(i).get(j).getDeposit() != null &&
                                                        mSpecialChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit(mSpecialChildDataList.get(i).get(j).getDeposit());
                                                } else {
                                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                                }
                                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(mSpecialGroupDataList.get(i).getCustom_dimension());
                                                fieldAddfieldSellResDimensionsModel.setId(null);
                                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensionslist);
                            Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                            Intent addfield_main_intent = new Intent(Field_AddField_FiveEditerPriceActivity.this,FieldAddFieldMainActivity.class);
                            startActivity(addfield_main_intent);
                        } else {
                            Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensionslist);
                            Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                            Intent serviceitem_intent = new Intent(Field_AddField_FiveEditerPriceActivity.this,FieldAddFieldServicesItemsActivity.class);
                            startActivity(serviceitem_intent);
                        }
                    }
                }
            });
        }
        initData();
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
            return -1;
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
    private void savadata() {
        Field_AddResourcesModel.getInstance().getResource().getResource_data().setDimensions(dimensionslist);
    }
    private boolean checkInput_info_full() {
        boolean result = false;
        if (dimensionslist != null) {
            dimensionslist.clear();
        }
        mAddfieldEditPriceCommonDayGV.clearFocus();
        mAddfieldEditPriceSpecialDayLV.clearFocus();
        if (editorprice_special_list.size() > 0) {
            boolean price_null = false;
            for (int i = 0; i < maddfield_specialpricelistciew.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) maddfield_specialpricelistciew.getChildAt(i);// 获得子item的layout
                EditText et = (EditText) layout.findViewById(R.id.editprice_special_itemedit);//
                EditText depositET = (EditText) layout.findViewById(R.id.addfield_deposit_item_edit);//
                if ((isFloathString(et.getText().toString()) == 0 || isNumberString(et.getText().toString()) == 0) && et.getText().toString().length() > 0) {
                    if ((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100)).length() > 10) {
//                                                        et.setText("");
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                        return result;
                    }
                    if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
//                                                        et.setText("");
                        BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                        return result;
                    }
                    if (Double.parseDouble(et.getText().toString()) > 0) {
                        price_null = true;
                    }
                    if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                            Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                        for (int k = 0; k < 7; k++) {
                            FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                            fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                            fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                            fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                            fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                            fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                            if (editorprice_special_list.get(i).get("id") != null &&
                                    editorprice_special_list.get(i).get("id").length() > 0) {
                                fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                            }
                            if (depositET.getText().toString().length() > 0) {
                                fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                            } else {
                                fieldAddfieldSellResDimensionsModel.setDeposit("0");
                            }

                            dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                        }
                    } else {
                        FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                        fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                        fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                        fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                        fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(et.getText().toString(),1),100));
                        fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                        if (editorprice_special_list.get(i).get("id") != null &&
                                editorprice_special_list.get(i).get("id").length() > 0) {
                            fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                        } else {
                            fieldAddfieldSellResDimensionsModel.setId(0);
                        }
                        if (depositET.getText().toString().length() > 0) {
                            fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                        } else {
                            fieldAddfieldSellResDimensionsModel.setDeposit("0");
                        }
                        dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                    }
                } else {
                    if (et.getText().toString().length() == 0) {
                        if (editorprice_special_list.get(i).get("m_lease_term_type_id").toString().equals("-1") &&
                                Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                            for (int k = 0; k < 7; k++) {
                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                fieldAddfieldSellResDimensionsModel.setPrice("0");
                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                                if (editorprice_special_list.get(i).get("id") != null &&
                                        editorprice_special_list.get(i).get("id").length() > 0) {
                                    fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setId(0);
                                }
                                if (depositET.getText().toString().length() > 0) {
                                    fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                }
                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                            }
                        } else {
                            FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                            fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                            fieldAddfieldSellResDimensionsModel.setSize(editorprice_special_list.get(i).get("fieldsize").toString());
                            fieldAddfieldSellResDimensionsModel.setLease_term_type_id(editorprice_special_list.get(i).get("m_lease_term_type_id").toString());
                            fieldAddfieldSellResDimensionsModel.setPrice("0");
                            fieldAddfieldSellResDimensionsModel.setCustom_dimension(editorprice_special_list.get(i).get("custom_dimension"));
                            if (editorprice_special_list.get(i).get("id") != null &&
                                    editorprice_special_list.get(i).get("id").length() > 0) {
                                fieldAddfieldSellResDimensionsModel.setId(Integer.parseInt(editorprice_special_list.get(i).get("id")));
                            } else {
                                fieldAddfieldSellResDimensionsModel.setId(0);
                            }
                            if (depositET.getText().toString().length() > 0) {
                                fieldAddfieldSellResDimensionsModel.setDeposit(depositET.getText().toString());
                            } else {
                                fieldAddfieldSellResDimensionsModel.setDeposit("0");
                            }
                            dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                        }
                    } else {
                        return result;
                    }
                }
            }
            if (!price_null) {
                boolean isWeek = false;
                if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                    for (int i = 0; i < mCommonChildDataList.size(); i++) {
                        for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                            if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                    mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                    mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                                price_null = true;
                                if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                    return result;
                                }
                                if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                    return result;
                                }
                                if (checkChildWeekChoose(i,j,1)&&
                                        mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                    isWeek = true;
                                }
                            }
                        }
                    }
                }
                if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                    for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                        for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                            if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                    mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                    mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                                if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                    return result;
                                }
                                if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                    return result;
                                }
                                price_null = true;
                                if (checkChildWeekChoose(i,j,2)&&
                                        mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                    isWeek = true;
                                }
                            }
                        }
                    }
                }
                if (!price_null) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                    return result;
                }
                if (!isWeek) {
                    BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                    return result;
                }
            }
        } else {
            boolean isPrice = false;
            boolean isWeek = false;
            if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
                for (int i = 0; i < mCommonChildDataList.size(); i++) {
                    for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                        if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                                mCommonChildDataList.get(i).get(j).getPrice() != null &&
                                mCommonChildDataList.get(i).get(j).getPrice().length() > 0) {
                            isPrice = true;
                            if ((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                return result;
                            }
                            if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                return result;
                            }
                            if (checkChildWeekChoose(i,j,1)&&
                                    mCommonChildDataList.get(i).get(j).getItemChoose() == 1) {
                                isWeek = true;
                            }
                        }
                    }
                }
            }
            if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
                for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                    for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                        if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                                mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                                mSpecialChildDataList.get(i).get(j).getPrice().length() > 0) {
                            if ((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100)).length() > 10) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                return result;
                            }
                            if (Double.parseDouble((Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100))) > Double.parseDouble(String.valueOf(Integer.MAX_VALUE))) {
                                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_beyondprice_prompt));
                                return result;
                            }
                            isPrice = true;
                            if (checkChildWeekChoose(i,j,2)&&
                                    mSpecialChildDataList.get(i).get(j).getItemChoose() == 1) {
                                isWeek = true;
                            }
                        }
                    }
                }
            }
            if (!isPrice) {
                BaseMessageUtils.showToast(getResources().getString(R.string.txt_field_addfieldfive_minprice_prompt));
                return result;
            }
            if (!isWeek) {
                BaseMessageUtils.showToast(getResources().getString(R.string.addfield_edit_price_week_click_error_str));
                return result;
            }
        }
        //2018/1/5 周几规格整合 提交
        if (mCommonChildDataList != null && mCommonChildDataList.size() > 0) {
            for (int i = 0; i < mCommonChildDataList.size(); i++) {
                for (int j = 0; j < mCommonChildDataList.get(i).size(); j++) {
                    if (mCommonChildDataList.get(i).get(j).getHiteView() != 1 &&
                            mCommonChildDataList.get(i).get(j).getItemChoose() == 1 &&
                            mCommonChildDataList.get(i).get(j).getPrice() != null &&
                            mCommonChildDataList.get(i).get(j).getPrice().length() > 0 &&
                            checkChildWeekChoose(i,j,1)) {
                        for (int k = 0; k < 7; k++) {
                            if (mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                    mCommonChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                fieldAddfieldSellResDimensionsModel.setSize(mCommonGroupDataList.get(i).getSize());
                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mCommonChildDataList.get(i).get(j).getPrice(),1),100));
                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(mCommonGroupDataList.get(i).getCustom_dimension());
                                if (mCommonChildDataList.get(i).get(j).getChild_week_id().get(k) != null) {
                                    fieldAddfieldSellResDimensionsModel.setId(mCommonChildDataList.get(i).get(j).getChild_week_id().get(k));
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setId(0);
                                }
                                if (mCommonChildDataList.get(i).get(j).getDeposit() != null &&
                                        mCommonChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                    fieldAddfieldSellResDimensionsModel.setDeposit(mCommonChildDataList.get(i).get(j).getDeposit());
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                }
                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                            }
                        }

                    }
                }
            }
        }
        if (mSpecialChildDataList != null && mSpecialChildDataList.size() > 0) {
            for (int i = 0; i < mSpecialChildDataList.size(); i++) {
                for (int j = 0; j < mSpecialChildDataList.get(i).size(); j++) {
                    if (mSpecialChildDataList.get(i).get(j).getHiteView() != 1 &&
                            mSpecialChildDataList.get(i).get(j).getItemChoose() == 1 &&
                            mSpecialChildDataList.get(i).get(j).getPrice() != null &&
                            mSpecialChildDataList.get(i).get(j).getPrice().length() > 0 &&
                            checkChildWeekChoose(i,j,2)) {
                        for (int k = 0; k < 7; k++) {
                            if (mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) != null &&
                                    mSpecialChildDataList.get(i).get(j).getChildWeekChooseList().get(k) == 1) {
                                FieldAddfieldSellResDimensionsModel fieldAddfieldSellResDimensionsModel = new FieldAddfieldSellResDimensionsModel();
                                fieldAddfieldSellResDimensionsModel.setCount_of_frame(1);
                                fieldAddfieldSellResDimensionsModel.setSize(mSpecialGroupDataList.get(i).getSize());
                                fieldAddfieldSellResDimensionsModel.setLease_term_type_id(String.valueOf(mWeekLeaseTermMap.get(k)));
                                fieldAddfieldSellResDimensionsModel.setPrice(Constants.getpricestring(Constants.getpricestring(mSpecialChildDataList.get(i).get(j).getPrice(),1),100));
                                fieldAddfieldSellResDimensionsModel.setCustom_dimension(mSpecialGroupDataList.get(i).getCustom_dimension());
                                if (mSpecialChildDataList.get(i).get(j).getChild_week_id().get(k) != null) {
                                    fieldAddfieldSellResDimensionsModel.setId(mSpecialChildDataList.get(i).get(j).getChild_week_id().get(k));
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setId(0);
                                }
                                if (mSpecialChildDataList.get(i).get(j).getDeposit() != null &&
                                        mSpecialChildDataList.get(i).get(j).getDeposit().length() > 0) {
                                    fieldAddfieldSellResDimensionsModel.setDeposit(mSpecialChildDataList.get(i).get(j).getDeposit());
                                } else {
                                    fieldAddfieldSellResDimensionsModel.setDeposit("0");
                                }
                                dimensionslist.add(fieldAddfieldSellResDimensionsModel);
                            }
                        }

                    }
                }
            }
        }
        if (Field_AddResourcesModel.getInstance().getResource().getIsRedact() == 1) {
            Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
        } else {
            Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            if (checkInput_info_full()) {
                Field_AddResourcesModel.getInstance().setIs_hava_field_price(1);
                savadata();
            } else {
                Field_AddResourcesModel.getInstance().setIs_hava_field_price(0);
            }
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    private int getWeekKey(String weekName) {
        int key = -1;
        if (weekName != null) {
            for (int i = 0; i < Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().size(); i++) {
                if (Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getPeriod() == 1) {
                    if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                    Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Mon")) {
                        key = 0;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Tue")) {
                        key = 1;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Wed")) {
                        key = 2;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Thu")) {
                        key = 3;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Fri")) {
                        key = 4;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Sat")) {
                        key = 5;
                    } else if (weekName.equals(String.valueOf(Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getId())) &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName() != null &&
                            Field_AddResourcesModel.getInstance().getOptions().getLease_term_type().get(i).getName().equals("Sun")) {
                        key = 6;
                    }
                }
            }
        }
        return key;
    }
    //周几是否有选中
    private boolean checkChildWeekChoose(final int groupPosition, final int childPosition, int type) {
        boolean checked = false;
        for (int i = 0; i < 7; i++) {
            if (type == 1) {
                if (mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) != null &&
                        mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) == 1) {
                    checked = true;
                    break;
                }
            } else if (type == 2) {
                if (mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) != null &&
                        mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) == 1) {
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    private void initData() {
        mCommonGroupDataList = new ArrayList<>();
        mCommonChildDataList = new ArrayList<>();
        editorprice_special_list = new ArrayList<>();
        mSpecialGroupDataList = new ArrayList<>();
        mSpecialChildDataList = new ArrayList<>();
        //场地常规规格数据格式整理
        for (int i = 0; i < fieldsizelist.size(); i++) {
            for (int l = 0; l < denominatedunitlist.size(); l++) {
                if (denominatedunitlist.get(l).get("m_lease_term_type_id").toString().equals("-1")
                        && Field_AddResourcesModel.getInstance().getResource().getRes_type_id() != 3) {
                    Map<String, String> map = new HashMap<String, String>();
                    AddfieldEditPriceWeekChooseModel groupModel = new AddfieldEditPriceWeekChooseModel();
                    List<AddfieldEditPriceWeekChooseModel> childModelList = new ArrayList<>();
                    groupModel.setSize(fieldsizelist.get(i).toString());
                    groupModel.setLease_term_type_name(denominatedunitlist.get(l).get("denominatedunit").toString());
                    groupModel.setLease_term_type_id(Integer.parseInt(denominatedunitlist.get(l).get("m_lease_term_type_id")));
                    groupModel.setPrice("");
                    groupModel.setCustom_dimension("");
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                        List<String> priceList = new ArrayList<>();
                        List<Map<String,String>> weekPriceList = new ArrayList<>();
                        for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                            if ((mWeekLeaseTermIdList != null && mWeekLeaseTermIdList.size() > 0 &&
                                    mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                    fieldsizelist.get(i).toString().equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() == 0)) {
                                int weekKey = getWeekKey(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                if (weekKey > -1) {
                                    groupModel.getGroupWeekChooseList().put(weekKey,1);
                                }
                                if (priceList.size() == 0 ||
                                        !priceList.contains(Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01))) {
                                    priceList.add(Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                }
                                Map<String, String> weekPriceMap = new HashMap<String, String>();
                                weekPriceMap.put("price",Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                weekPriceMap.put("lease_term_type_id",Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit() != null
                                        && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                    weekPriceMap.put("deposit",Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString());
                                    if (isChooseDeposit == false) {
                                        isChooseDeposit = true;
                                        maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                    }
                                } else {
                                    weekPriceMap.put("deposit","0");
                                }
                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                    weekPriceMap.put("id",String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                }
                                weekPriceList.add(weekPriceMap);
                            }
                        }
                        if (priceList != null && priceList.size() > 0 &&
                                weekPriceList != null && weekPriceList.size() > 0) {
                            for (int k = 0; k < priceList.size(); k++) {
                                //2018/11/12 价格一样押金不一样要分开
                                List<String> depositList = new ArrayList<>();
                                for (int n = 0; n < weekPriceList.size(); n++) {
                                    if (weekPriceList.get(n).get("price").equals(priceList.get(k))) {
                                        if (depositList.size() == 0 ||
                                                !depositList.contains(weekPriceList.get(n).get("deposit"))) {
                                            depositList.add(weekPriceList.get(n).get("deposit"));
                                        }
                                    }
                                }
                                if (depositList.size() > 0) {
                                    if (isChooseDeposit == false) {
                                        isChooseDeposit = true;
                                        maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                    }
                                    for (int m = 0; m < depositList.size(); m++) {
                                        AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                                        addfieldEditPriceWeekChooseModel.setPrice(priceList.get(k));
                                        addfieldEditPriceWeekChooseModel.setDeposit(depositList.get(m));
                                        addfieldEditPriceWeekChooseModel.setItemChoose(1);
                                        for (int n = 0; n < weekPriceList.size(); n++) {
                                            if (weekPriceList.get(n).get("price").equals(priceList.get(k)) &&
                                                    weekPriceList.get(n).get("deposit").equals(depositList.get(m))) {
                                                int weekKey = getWeekKey(weekPriceList.get(n).get("lease_term_type_id"));
                                                if (weekKey > -1) {
                                                    addfieldEditPriceWeekChooseModel.getChildWeekChooseList().put(weekKey,1);
                                                    if (weekPriceList.get(n).get("id") != null) {
                                                        addfieldEditPriceWeekChooseModel.getChild_week_id().put(weekKey,Integer.parseInt(weekPriceList.get(n).get("id")));
                                                    }
                                                }
                                            }
                                        }
                                        childModelList.add(addfieldEditPriceWeekChooseModel);
                                    }
                                } else {
                                    AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                                    addfieldEditPriceWeekChooseModel.setPrice(priceList.get(k));
                                    addfieldEditPriceWeekChooseModel.setDeposit("0");
                                    addfieldEditPriceWeekChooseModel.setItemChoose(1);
                                    for (int n = 0; n < weekPriceList.size(); n++) {
                                        if (weekPriceList.get(n).get("price").equals(priceList.get(k))) {
                                            int weekKey = getWeekKey(weekPriceList.get(n).get("lease_term_type_id"));
                                            if (weekKey > -1) {
                                                addfieldEditPriceWeekChooseModel.getChildWeekChooseList().put(weekKey,1);
                                                if (weekPriceList.get(n).get("id") != null) {
                                                    addfieldEditPriceWeekChooseModel.getChild_week_id().put(weekKey,Integer.parseInt(weekPriceList.get(n).get("id")));
                                                }
                                            }
                                        }
                                    }
                                    childModelList.add(addfieldEditPriceWeekChooseModel);
                                }
                            }
                        }
                    }
                    AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                    childModelList.add(addfieldEditPriceWeekChooseModel);
                    mCommonGroupDataList.add(groupModel);
                    mCommonChildDataList.add(childModelList);
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("fieldsize", fieldsizelist.get(i).toString());
                    map.put("denominatedunit", denominatedunitlist.get(l).get("denominatedunit").toString());
                    map.put("m_lease_term_type_id", denominatedunitlist.get(l).get("m_lease_term_type_id").toString());
                    map.put("price", "");
                    map.put("deposit", "0");
                    map.put("custom_dimension", "");
                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                        for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                            if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                if (denominatedunitlist.get(l).get("m_lease_term_type_id").equals("-1")) {
                                    if (mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                            fieldsizelist.get(i).toString().equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() == 0) {
                                        map.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString().length() > 0) {
                                            map.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString());
                                            if (isChooseDeposit == false) {
                                                isChooseDeposit = true;
                                                maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                            }
                                        }
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                            map.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                        }
                                        break;
                                    }
                                } else {
                                    if ((denominatedunitlist.get(l).get("m_lease_term_type_id").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                            fieldsizelist.get(i).toString().equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() == 0) {
                                        map.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString() != null &&
                                                Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                            map.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit());
                                            if (isChooseDeposit == false) {
                                                isChooseDeposit = true;
                                                maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                            }
                                        }
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                            map.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                        }
                                        break;
                                    }
                                }
                            } else {
                                if ((denominatedunitlist.get(l).get("m_lease_term_type_id").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                        fieldsizelist.get(i).toString().equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension().toString().length() == 0) {
                                    map.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString() != null &&
                                            Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                        map.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit());
                                        if (isChooseDeposit == false) {
                                            isChooseDeposit = true;
                                            maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                        }
                                    }
                                    if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                        map.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    editorprice_special_list.add(map);
                }
            }
        }
        //场地特殊规格数据格式整理
        for (int i = 0; i < fieldsizelist.size(); i++) {
            for (int l = 0; l < denominatedunitlist.size(); l++) {
                if (custom_dimensionlist != null && custom_dimensionlist.size() > 0) {
                    for (int k = 0; k < custom_dimensionlist.size(); k++) {
                        if (denominatedunitlist.get(l).get("m_lease_term_type_id").toString().equals("-1")
                                && Field_AddResourcesModel.getInstance().getResource().getRes_type_id() != 3) {
                            Map<String, String> map = new HashMap<String, String>();
                            AddfieldEditPriceWeekChooseModel groupModel = new AddfieldEditPriceWeekChooseModel();
                            List<AddfieldEditPriceWeekChooseModel> childModelList = new ArrayList<>();
                            groupModel.setSize(fieldsizelist.get(i).toString());
                            groupModel.setLease_term_type_name(denominatedunitlist.get(l).get("denominatedunit").toString());
                            groupModel.setLease_term_type_id(Integer.parseInt(denominatedunitlist.get(l).get("m_lease_term_type_id")));
                            groupModel.setPrice("");
                            groupModel.setCustom_dimension(custom_dimensionlist.get(k).get("custom_dimension").toString());
                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                                List<String> priceList = new ArrayList<>();
                                List<Map<String,String>> weekPriceList = new ArrayList<>();
                                for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                                    if ((mWeekLeaseTermIdList != null && mWeekLeaseTermIdList.size() > 0 &&
                                            mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                            fieldsizelist.get(i).toString().equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                            (custom_dimensionlist.get(k).get("custom_dimension").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension()))) {
                                        int weekKey = getWeekKey(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                        if (weekKey > -1) {
                                            groupModel.getGroupWeekChooseList().put(weekKey,1);
                                        }
                                        if (priceList.size() == 0 ||
                                                !priceList.contains(Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01))) {
                                            priceList.add(Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                        }
                                        Map<String, String> weekPriceMap = new HashMap<String, String>();
                                        weekPriceMap.put("price",Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                        weekPriceMap.put("lease_term_type_id",Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString());
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit() != null) {
                                            weekPriceMap.put("deposit",Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().toString());
                                        } else {
                                            weekPriceMap.put("deposit","0");
                                        }
                                        if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                            weekPriceMap.put("id",String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                        }
                                        weekPriceList.add(weekPriceMap);
                                    }
                                }
                                if (priceList != null && priceList.size() > 0 &&
                                        weekPriceList != null && weekPriceList.size() > 0) {
                                    for (int m = 0; m < priceList.size(); m++) {
                                        //2018/11/12 价格一样押金不一样要分开
                                        List<String> depositList = new ArrayList<>();
                                        for (int n = 0; n < weekPriceList.size(); n++) {
                                            if (weekPriceList.get(n).get("price").equals(priceList.get(m))) {
                                                if (depositList.size() == 0 ||
                                                        !depositList.contains(weekPriceList.get(n).get("deposit"))) {
                                                    depositList.add(weekPriceList.get(n).get("deposit"));
                                                }
                                            }
                                        }
                                        if (depositList.size() > 0) {
                                            if (isChooseDeposit == false) {
                                                isChooseDeposit = true;
                                                maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                            }
                                            for (int o = 0; o < depositList.size(); o++) {
                                                AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                                                addfieldEditPriceWeekChooseModel.setPrice(priceList.get(m));
                                                addfieldEditPriceWeekChooseModel.setDeposit(depositList.get(o));
                                                addfieldEditPriceWeekChooseModel.setItemChoose(1);
                                                if (weekPriceList != null && weekPriceList.size() > 0) {
                                                    for (int n = 0; n < weekPriceList.size(); n++) {
                                                        if (weekPriceList.get(n).get("price").equals(priceList.get(m)) &&
                                                                weekPriceList.get(n).get("deposit").equals(depositList.get(o))) {
                                                            int weekKey = getWeekKey(weekPriceList.get(n).get("lease_term_type_id"));
                                                            if (weekKey > -1) {
                                                                addfieldEditPriceWeekChooseModel.getChildWeekChooseList().put(weekKey,1);
                                                                if (weekPriceList.get(n).get("id") != null) {
                                                                    addfieldEditPriceWeekChooseModel.getChild_week_id().put(weekKey,Integer.parseInt(weekPriceList.get(n).get("id")));
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                                childModelList.add(addfieldEditPriceWeekChooseModel);
                                            }
                                        } else {
                                            AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                                            addfieldEditPriceWeekChooseModel.setPrice(priceList.get(m));
                                            addfieldEditPriceWeekChooseModel.setDeposit("0");
                                            addfieldEditPriceWeekChooseModel.setItemChoose(1);
                                            if (weekPriceList != null && weekPriceList.size() > 0) {
                                                for (int n = 0; n < weekPriceList.size(); n++) {
                                                    if (weekPriceList.get(n).get("price").equals(priceList.get(m))) {
                                                        int weekKey = getWeekKey(weekPriceList.get(n).get("lease_term_type_id"));
                                                        if (weekKey > -1) {
                                                            addfieldEditPriceWeekChooseModel.getChildWeekChooseList().put(weekKey,1);
                                                            if (weekPriceList.get(n).get("id") != null) {
                                                                addfieldEditPriceWeekChooseModel.getChild_week_id().put(weekKey,Integer.parseInt(weekPriceList.get(n).get("id")));
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                            childModelList.add(addfieldEditPriceWeekChooseModel);
                                        }
                                    }
                                }
                            }
                            AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                            childModelList.add(addfieldEditPriceWeekChooseModel);
                            mSpecialGroupDataList.add(groupModel);
                            mSpecialChildDataList.add(childModelList);
                        } else {
                            Map<String, String> custommap = new HashMap<String, String>();
                            custommap.put("fieldsize", fieldsizelist.get(i).toString());
                            custommap.put("denominatedunit", denominatedunitlist.get(l).get("denominatedunit").toString());
                            custommap.put("m_lease_term_type_id", denominatedunitlist.get(l).get("m_lease_term_type_id").toString());
                            custommap.put("price", "");
                            custommap.put("deposit", "0");
                            custommap.put("custom_dimension", custom_dimensionlist.get(k).get("custom_dimension").toString());
                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions() != null && Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size() > 0) {
                                for (int j = 0; j < Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().size(); j++) {
                                    if (Field_AddResourcesModel.getInstance().getResource().getRes_type_id() == 3) {
                                        if (denominatedunitlist.get(l).get("m_lease_term_type_id").equals("-1")) {
                                            if (mWeekLeaseTermIdList.contains(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                                    (fieldsizelist.get(i).toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                                    (custom_dimensionlist.get(k).get("custom_dimension").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension())) {
                                                custommap.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit() != null &&
                                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                                    custommap.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit());
                                                    if (isChooseDeposit == false) {
                                                        isChooseDeposit = true;
                                                        maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                                    }
                                                }
                                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                                    custommap.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                                }
                                                break;
                                            }
                                        } else {
                                            if ((denominatedunitlist.get(l).get("m_lease_term_type_id").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                                    (fieldsizelist.get(i).toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                                    (custom_dimensionlist.get(k).get("custom_dimension").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension())) {
                                                custommap.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit() != null &&
                                                        Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                                    custommap.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit());
                                                    if (isChooseDeposit == false) {
                                                        isChooseDeposit = true;
                                                        maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                                    }
                                                }
                                                if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                                    custommap.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                                }
                                                break;
                                            }
                                        }
                                    } else {
                                        if ((denominatedunitlist.get(l).get("m_lease_term_type_id").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getLease_term_type_id().toString()) &&
                                                (fieldsizelist.get(i).toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getSize()) &&
                                                (custom_dimensionlist.get(k).get("custom_dimension").toString()).equals(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getCustom_dimension())) {
                                            custommap.put("price", Constants.getpricestring(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getPrice().toString(), 0.01));
                                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit() != null &&
                                                    Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit().length() > 0) {
                                                custommap.put("deposit", Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getDeposit());
                                                if (isChooseDeposit == false) {
                                                    isChooseDeposit = true;
                                                    maddfield_editprice_cash_pledge_checkbox.setChecked(true);
                                                }
                                            }
                                            if (Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId() != null) {
                                                custommap.put("id", String.valueOf(Field_AddResourcesModel.getInstance().getResource().getResource_data().getDimensions().get(j).getId()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            editorprice_special_list.add(custommap);
                        }
                    }
                }
            }
        }

        //2018/1/5 天规格显示
        //天常规的规格
        unitDayAdapter = new FieldAddFieldPriceUnitDayAdapter(this,this,mCommonGroupDataList,mCommonChildDataList,1);
        mAddfieldEditPriceCommonDayGV.setAdapter(unitDayAdapter);
        mAddfieldEditPriceCommonDayGV.setGroupIndicator(null);
        for (int i = 0; i < mCommonGroupDataList.size(); i++) {
            mAddfieldEditPriceCommonDayGV.expandGroup(i);
        }
        //天以外的规格
        special_editorpriceadapter= new Field_AddField_FiveEditerPriceAdapter(Field_AddField_FiveEditerPriceActivity.this,Field_AddField_FiveEditerPriceActivity.this,editorprice_special_list,2);
        maddfield_specialpricelistciew.setAdapter(special_editorpriceadapter);
        //天特殊的规格
        unitDaySpecialAdapter = new FieldAddFieldPriceUnitDayAdapter(this,this,mSpecialGroupDataList,mSpecialChildDataList,2);
        mAddfieldEditPriceSpecialDayLV.setAdapter(unitDaySpecialAdapter);
        mAddfieldEditPriceSpecialDayLV.setGroupIndicator(null);
        for (int i = 0; i < mSpecialGroupDataList.size(); i++) {
            mAddfieldEditPriceSpecialDayLV.expandGroup(i);
        }
        hideProgressDialog();
    }
}
