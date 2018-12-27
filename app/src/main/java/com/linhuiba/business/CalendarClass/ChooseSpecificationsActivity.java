package com.linhuiba.business.CalendarClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.LoginActivity;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MarqueTextView;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ChooseSpecificationsActivity extends BaseMvpActivity implements View.OnClickListener,Field_AddFieldChoosePictureCallBack.CalendarClickCall,Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall {
    /** 当前的年月，现在日历顶端 */
    @InjectView(R.id.choosepecifications_currentMonth)
    TextView currentMonth;
    /** 上个月 */
    @InjectView(R.id.choosepecifications_prevMonth)
    ImageButton prevMonth;
    /** 下个月 */
    @InjectView(R.id.choosepecifications_nextMonth) ImageButton nextMonth;
    @InjectView(R.id.fieldsize_txt_gridview) MyGridview mfieldsize_txt_gridview;
    @InjectView(R.id.fieldprice_txt_gridview) MyGridview mfieldprice_txt_gridview;
    @InjectView(R.id.custom_dimension_mygridview) MyGridview mcustom_dimension_mygridview;
    @InjectView(R.id.viewflipperlayout) LinearLayout viewflipperlayout;
    @InjectView(R.id.choosefieldlayout) LinearLayout mchoosefieldlayout;
    @InjectView(R.id.chooseadvertisinglayout) LinearLayout mchooseadvertisinglayout;
    @InjectView(R.id.advertising_number) TextView madvertising_framenumber;
    @InjectView(R.id.advertising_price) TextView madvertising_price;
    @InjectView(R.id.advertising_mtw_down) TextView madvertising_mtw_down;
    @InjectView(R.id.advertising_mtw_num) TextView madvertising_mtw_num;
    @InjectView(R.id.advertising_mtw_add) TextView madvertising_mtw_add;
    @InjectView(R.id.calendarclick_remind) TextView mcalendarclick_remind;
    @InjectView(R.id.calendarclick_remind_layout) RelativeLayout mcalendarclick_remind_layout;
    @InjectView(R.id.field_custom_dimension_layout) LinearLayout mfield_custom_dimension_layout;
    @InjectView(R.id.choosespecifications_buttombtn)
    Button mchoosespecifications_buttombtn;
    @InjectView(R.id.choosespecifications_paid_order_layout)
    LinearLayout mchoosespecifications_paid_order_layout;
    @InjectView(R.id.choose_specifications_carts_btn)
    Button mchoose_specifications_carts_btn;
    @InjectView(R.id.choose_specifications_paid_order_btn)
    Button mchoose_specifications_paid_order_btn;
    @InjectView(R.id.choosespecifications_choose_size_textview) TextView mchoosespecifications_choose_size_textview;
    @InjectView(R.id.choosespecifications_deposit_remind_layout)
    RelativeLayout mchoosespecifications_deposit_remind_layout;
    @InjectView(R.id.choosespecifications_deposit_remind_textview)
    MarqueTextView mchoosespecifications_deposit_remind_textview;
    
    public GestureDetector gestureDetector = null;
    private ChooseSpecificationsAdapter calV = null;
    private ViewFlipper flipper = null;
    private MyGridview gridView = null;
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    /** 每次添加gridview到viewflipper中时给的标记 */
    private int gvFlag = 0;
    public ArrayList<Date> mdatelist = new ArrayList<Date>();//选择的日期
    public ArrayList<Date> mdatelisttmp = new ArrayList<Date>();
    private SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ChooseSpecificationsMygridviewAdapter chooseSpecificationsMygridviewAdapter_fielesize;
    private ChooseSpecificationsMygridviewAdapter chooseSpecificationsMygridviewAdapter_fieldpriceunit;
    private ChooseSpecificationsMygridviewAdapter choose_custom_dimensionid_adapter;
    private ArrayList<HashMap<String,String>> demensions = new ArrayList();
    private HashMap<String,String> term_typestr = new HashMap<>();
    private ArrayList<String> datapriceunit = new ArrayList<>();
    public HashMap<Object,Object> term_types;
    private ArrayList<String> sizelist = new ArrayList<>();
    private ArrayList<String> custom_dimension_list = new ArrayList<>();
    private ArrayList<String> FieldDateStringList = new ArrayList<>();
    public boolean termtype_workday = false;//天的规格中是否有工作日
    public boolean termtype_weekkday = false;//天的规格中是否有周末
    public String size = "";//详情页中保存的规格大小单位
    public String custom_dimension = "";//详情页中保存的特殊规格单位
    public String lease_term_type = "";//详情页中保存的计价单位
    public String adaptersizeid = "";//上一次选中的场地大小
    public String adaptertermtypeid = "";//上一次选中的计价单位
    public String adaptercustom_dimensionid = "";//上一次选中的特殊品类
    public ArrayList<String> getClosing_dates = new ArrayList<>();
    public String weatherstr = "";
    public int resourcetype;
    private String advertisingprice = "";
    private int advertsing_mtwnum = 0;
    private int advertsing_mtwnumtmp = 0;
    private String count_of_frame = "";
    private int operationtype = -1;
    public String deadline_date = "";
    public String activity_start_date = "";
    private HashMap<String,String> term_type_days_map;
    public Date checkedstart_date;
    public Date checkedend_date;
    public boolean iscustom_dimension;//是否选中特殊规格
    private Dialog newdialog;
    public int reserve_days = 0;//提前几天预定
    private int minimum_order_quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosespecifications);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.choosespecifications_titletxt));
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.shownextOtherButton(this, getResources().getString(R.string.confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < sizelist.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().get(sizelist.get(i).toString())) {
                        size = sizelist.get(i).toString();
                    }
                }
                for (int i = 0; i < datapriceunit.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().get(datapriceunit.get(i).toString())) {
                        lease_term_type = term_types.get(datapriceunit.get(i).toString()).toString() ;
                    }
                }
                for (int i = 0; i < custom_dimension_list.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().get(custom_dimension_list.get(i).toString())) {
                        custom_dimension = custom_dimension_list.get(i).toString();
                    }
                }

                if (FieldDateStringList != null) {
                    FieldDateStringList.clear();
                }
                for(int i = 0; i < mdatelist.size(); i++) {
                    String str=chineseDateFormat.format(mdatelist.get(i));
                    FieldDateStringList.add(str);
                }
                if (resourcetype == 1 || resourcetype == 3) {
                    if (mdatelist.size() > 0 && size.length() > 0 && lease_term_type.length() > 0) {
                        if (resourcetype == 3) {
                            if (mdatelist.size() < minimum_order_quantity) {
                                MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                        String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
                                return;
                            }
                        }
                        Intent fieldinfo = new Intent();
                        fieldinfo.putExtra("arraylist_date",(Serializable) mdatelist);
                        fieldinfo.putExtra("jsonarraydatastr",(Serializable) FieldDateStringList);
                        fieldinfo.putExtra("lease_term_type", lease_term_type);
                        fieldinfo.putExtra("size", size);
                        if (custom_dimension!= null) {
                            if (iscustom_dimension == false) {
                                fieldinfo.putExtra("custom_dimension", "");
                            } else {
                                fieldinfo.putExtra("custom_dimension", custom_dimension);
                            }
                        }
                        if (checkedstart_date != null) {
                            fieldinfo.putExtra("checkedstart_date", checkedstart_date);
                        }
                        if (checkedend_date != null) {
                            fieldinfo.putExtra("checkedend_date", checkedend_date);
                        }

                        if (operationtype == 1) {
                            ChooseSpecificationsActivity.this.setResult(1, fieldinfo);
                        } else if (operationtype == 2) {
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                        } else if (operationtype == 3) {
                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                        }
                        ChooseSpecificationsActivity.this.finish();
                    } else {
                        if (mdatelist.size() == 0) {
                            MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                        }
                    }
                } else if(resourcetype == 2) {
                    if (size.length() > 0 && lease_term_type.length() > 0) {
                        Intent fieldinfo = new Intent();
                        fieldinfo.putExtra("lease_term_type", lease_term_type);
                        fieldinfo.putExtra("size", size);
                        fieldinfo.putExtra("cont",advertsing_mtwnum);
                        if (custom_dimension!= null && custom_dimension.length() > 0) {
                            fieldinfo.putExtra("custom_dimension", custom_dimension);
                        }

                        if (operationtype == 1) {
                            ChooseSpecificationsActivity.this.setResult(1, fieldinfo);
                        } else if (operationtype == 2) {
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                        } else if (operationtype == 3) {
                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                        }
                        ChooseSpecificationsActivity.this.finish();
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                    }
                }

            }
        });
        initview();

    }
    private void initview() {
        Intent fieldinfo = getIntent();
        if (fieldinfo.getExtras() != null) {
            if (fieldinfo.getExtras().get("days_in_advance") != null) {
                reserve_days = fieldinfo.getExtras().getInt("days_in_advance");
            }
            if (fieldinfo.getExtras().get("minimum_order_quantity") != null) {
                minimum_order_quantity = fieldinfo.getExtras().getInt("minimum_order_quantity");
            }
            if (fieldinfo.getExtras().get("demensions") != null && fieldinfo.getExtras().get("term_typestr") != null) {
                if (fieldinfo.getExtras().get("checkedstart_date") != null) {
                    checkedstart_date = (Date) fieldinfo.getExtras().get("checkedstart_date");
                }
                if (fieldinfo.getExtras().get("checkedend_date") != null) {
                    checkedend_date = (Date) fieldinfo.getExtras().get("checkedend_date");
                }
                if (fieldinfo.getExtras().get("operationtype") != null) {
                    operationtype = fieldinfo.getExtras().getInt("operationtype");
                }
                if (operationtype == 2 ||operationtype == 3) {
                    mchoosespecifications_buttombtn.setVisibility(View.VISIBLE);
                    mchoosespecifications_paid_order_layout.setVisibility(View.GONE);
                } else {
                    mchoosespecifications_buttombtn.setVisibility(View.GONE);
                    mchoosespecifications_paid_order_layout.setVisibility(View.VISIBLE);
                    if (minimum_order_quantity > 1) {
                        mchoose_specifications_carts_btn.setVisibility(View.GONE);
                    }
                }
                if (fieldinfo.getExtras().get("size") != null) {
                    if (fieldinfo.getExtras().get("size").toString().length() > 0) {
                        size = fieldinfo.getExtras().get("size").toString();
                    }
                }
                if (fieldinfo.getExtras().get("lease_term_type") != null) {
                    if (fieldinfo.getExtras().get("lease_term_type").toString().length() > 0) {
                        if (fieldinfo.getExtras().get("lease_term_type").toString().equals("1") ||
                                fieldinfo.getExtras().get("lease_term_type").toString().equals("2")) {
                            lease_term_type = "-1";
                        } else {
                            lease_term_type = fieldinfo.getExtras().get("lease_term_type").toString();
                        }
                    }
                }
                if (fieldinfo.getExtras().get("custom_dimension") != null) {
                    if (fieldinfo.getExtras().get("custom_dimension").toString().length() > 0) {
                        custom_dimension = fieldinfo.getExtras().get("custom_dimension").toString();
                        iscustom_dimension = true;
                    }
                }

                if (fieldinfo.getExtras().get("term_type_days_map") != null) {
                    if (((HashMap<String,String>)fieldinfo.getExtras().get("term_type_days_map")).size() > 0) {
                        term_type_days_map = (HashMap<String,String>)fieldinfo.getExtras().get("term_type_days_map");
                    }
                }
                if (fieldinfo.getExtras().get("cont") != null) {
                    if (fieldinfo.getExtras().getInt("cont") > 0) {
                        advertsing_mtwnum = fieldinfo.getExtras().getInt("cont");
                        advertsing_mtwnumtmp = fieldinfo.getExtras().getInt("cont");
                    }
                }
                if (fieldinfo.getExtras().get("deadline") != null) {
                    if (fieldinfo.getExtras().getString("deadline").length() > 0) {
                        deadline_date = fieldinfo.getExtras().getString("deadline");
                    }
                }
                if (fieldinfo.getExtras().get("activity_start_date") != null) {
                    if (fieldinfo.getExtras().getString("activity_start_date").length() > 0) {
                        activity_start_date = fieldinfo.getExtras().getString("activity_start_date");
                    }
                }

                if (fieldinfo.getExtras().get("resourcetype") != null) {
                    resourcetype = fieldinfo.getExtras().getInt("resourcetype");
                }
                if (resourcetype == 1 || resourcetype == 3) {
                    mchoosefieldlayout.setVisibility(View.VISIBLE);
                    mchooseadvertisinglayout.setVisibility(View.GONE);
                    mchoosespecifications_choose_size_textview.setText(getResources().getString(R.string.order_listitem_capacity_sizetxt));
                } else if (resourcetype == 2) {
                    mchoosespecifications_choose_size_textview.setText(getResources().getString(R.string.order_listitem_advertising_sizetxt));
                    mchoosefieldlayout.setVisibility(View.GONE);
                    mchooseadvertisinglayout.setVisibility(View.VISIBLE);
                }
                if (fieldinfo.getExtras().get("term_typestr") != null) {
                    term_typestr = (HashMap<String, String>) fieldinfo.getExtras().get("term_typestr");
                    if (term_typestr != null && term_typestr.size() > 0) {
                        if (resourcetype == 1 || resourcetype == 3) {
                            term_types = new HashMap<>();
                            boolean deletetermtypeitem = true;
                            term_types.put("天","-1");
                            for (Map.Entry<String, String> entry : term_typestr.entrySet()) {
                                String key = entry.getKey();
                                String value = (String) entry.getValue();
                                if (key.equals("1")) {
                                    deletetermtypeitem = false;
                                    termtype_workday = true;
                                }
                                if (key.equals("2")){
                                    deletetermtypeitem = false;
                                    termtype_weekkday = true;
                                }
                                if (!key.equals("1") && !key.equals("2")) {
                                    term_types.put(value, key);
                                }
                            }
                            if (deletetermtypeitem == true) {
                                term_types.remove("天");
                            }
                        } else if (resourcetype == 2){
                            term_types = new HashMap<>();
                            for (Map.Entry<String, String> entry : term_typestr.entrySet()) {
                                String key = entry.getKey();
                                String value = (String) entry.getValue();
                                term_types.put(value, key);
                            }
                        }
                    }
                }
                if (fieldinfo.getExtras().get("sizelist") != null) {
                    if (fieldinfo.getExtras().get("sizelist").toString().length() > 0) {
                        sizelist = (ArrayList<String>) fieldinfo.getExtras().getSerializable("sizelist");
                    }
                }
                if (fieldinfo.getExtras().get("custom_dimension_list") != null) {
                    if (fieldinfo.getExtras().get("custom_dimension_list").toString().length() > 0) {
                        custom_dimension_list = (ArrayList<String>) fieldinfo.getExtras().getSerializable("custom_dimension_list");
                    }
                }

                demensions = (ArrayList<HashMap<String,String>>) fieldinfo.getSerializableExtra("demensions");
                JSONArray demensionsjsonarray = JSONArray.parseArray(JSON.toJSONString(demensions,true));
                List<Map.Entry<Object, Object>> term_type_list = new ArrayList<Map.Entry<Object, Object>>(
                        term_types.entrySet());
                //排序
                Collections.sort(term_type_list, new Comparator<Map.Entry<Object, Object>>() {
                    public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
                        return (o1.getValue().toString().compareTo(o2.getValue().toString()));
                    }
                });
                for (int i = 0; i < term_type_list.size(); i++) {
                    String name = term_type_list.get(i).toString();
                    datapriceunit.add(name.substring(0, name.indexOf("=")));
                }
                for (int i = 0; i <sizelist.size(); i++) {
                    if (size != null) {
                        if (size.length() > 0) {
                            if (sizelist.get(i).toString().equals(size)) {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),true);
                                adaptersizeid = sizelist.get(i).toString();
                            } else {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),false);
                            }
                        } else {
                            if (i == 0) {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),true);
                                adaptersizeid = sizelist.get(i).toString();
                            } else {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),false);
                            }
                        }
                    } else {
                        if (i == 0) {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),true);
                            adaptersizeid = sizelist.get(i).toString();
                        } else {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().put(sizelist.get(i).toString(),false);
                        }
                    }

                }
                chooseSpecificationsMygridviewAdapter_fielesize = new ChooseSpecificationsMygridviewAdapter(this,this,sizelist,0);
                mfieldsize_txt_gridview.setAdapter(chooseSpecificationsMygridviewAdapter_fielesize);
                for (int i = 0; i <datapriceunit.size(); i++) {
                    if (lease_term_type != null) {
                        if (lease_term_type.length() > 0) {
                            if (term_types.get(datapriceunit.get(i).toString()).toString().equals(lease_term_type)) {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),true);
                                adaptertermtypeid = term_types.get(datapriceunit.get(i).toString()).toString();
                            } else {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),false);
                            }
                        } else {
                            if (i == 0) {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),true);
                                adaptertermtypeid = term_types.get(datapriceunit.get(i).toString()).toString();
                            } else {
                                ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),false);
                            }
                        }
                    } else {
                        if (i == 0) {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),true);
                            adaptertermtypeid = term_types.get(datapriceunit.get(i).toString()).toString();
                        } else {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().put(datapriceunit.get(i).toString(),false);
                        }
                    }

                }
                chooseSpecificationsMygridviewAdapter_fieldpriceunit = new ChooseSpecificationsMygridviewAdapter(this,this,datapriceunit,1);
                mfieldprice_txt_gridview.setAdapter(chooseSpecificationsMygridviewAdapter_fieldpriceunit);
                for (int i = 0; i <custom_dimension_list.size(); i++) {
                    if (custom_dimension != null && custom_dimension.length() > 0) {
                        if (custom_dimension_list.get(i).toString().equals(custom_dimension)) {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().put(custom_dimension_list.get(i).toString(),true);
                            adaptercustom_dimensionid = custom_dimension_list.get(i).toString();
                        } else {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().put(custom_dimension_list.get(i).toString(),false);
                        }
                    } else {
                        if (i == 0) {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().put(custom_dimension_list.get(i).toString(),false);
                        } else {
                            ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().put(custom_dimension_list.get(i).toString(),false);
                        }
                    }
                }
                if (custom_dimension_list != null && custom_dimension_list.size() > 0) {
                    mfield_custom_dimension_layout.setVisibility(View.VISIBLE);
                    choose_custom_dimensionid_adapter = new ChooseSpecificationsMygridviewAdapter(ChooseSpecificationsActivity.this,ChooseSpecificationsActivity.this,custom_dimension_list,2);
                    mcustom_dimension_mygridview.setAdapter(choose_custom_dimensionid_adapter);
                } else {
                    mfield_custom_dimension_layout.setVisibility(View.GONE);
                }
            }
        }
        if (resourcetype == 1 || resourcetype == 3) {
            if (minimum_order_quantity > 1) {
                mchoosespecifications_deposit_remind_layout.setVisibility(View.VISIBLE);
                mchoosespecifications_deposit_remind_textview.setText(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
            }
            if (fieldinfo.getExtras().get("arraylist_date") != null) {
                mdatelist = (ArrayList<Date>) fieldinfo.getSerializableExtra("arraylist_date");
                if (mdatelisttmp != null) {
                    mdatelisttmp.clear();
                }
                mdatelisttmp.addAll(mdatelist);
            }
            if (resourcetype == 1 || resourcetype == 3) {
                if (fieldinfo.getExtras().get("weatherstr") != null) {
                    weatherstr = fieldinfo.getExtras().get("weatherstr").toString();
                }
            }
            if (fieldinfo.getExtras().get("Closing_dates") != null) {
                if (fieldinfo.getExtras().get("Closing_dates").toString().length() > 0) {
                    getClosing_dates = (ArrayList<String>) fieldinfo.getSerializableExtra("Closing_dates");
                }
            }

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
            currentDate = sdf.format(date); // 当期日期
            year_c = Integer.parseInt(currentDate.split("-")[0]);
            month_c = Integer.parseInt(currentDate.split("-")[1]);
            day_c = Integer.parseInt(currentDate.split("-")[2]);
            setListener();
            gestureDetector = new GestureDetector(this, new MyGestureListener());
            flipper = (ViewFlipper) findViewById(R.id.choosepecifications_flipper);
            flipper.removeAllViews();
            ChooseSpecificationsAdapter.clear_mapitemclick();
            ChooseSpecificationsAdapter.clear_mapitemprice();
            ChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
            ChooseSpecificationsAdapter.clear_mapitemweather();
            calV = new ChooseSpecificationsAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,demensions,this,1);
            addGridView();
            gridView.setAdapter(calV);
            flipper.addView(gridView, 0);
            addTextToTopTextView(currentMonth);
        } else if (resourcetype == 2) {
            if (fieldinfo.getExtras().get("arraylist_date") != null) {
                mdatelist = (ArrayList<Date>) fieldinfo.getSerializableExtra("arraylist_date");
                if (mdatelisttmp != null) {
                    mdatelisttmp.clear();
                }
                mdatelisttmp.addAll(mdatelist);
            }

            getadvertisingshow(true);
            final Drawable drawable_down = getResources().getDrawable(R.drawable.down_img);
            drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
            final Drawable drawable_down_pressed = getResources().getDrawable(R.drawable.down_img_pressed);
            drawable_down_pressed.setBounds(0, 0, drawable_down_pressed.getMinimumWidth(), drawable_down_pressed.getMinimumHeight());
            final Drawable drawable_add = getResources().getDrawable(R.drawable.add_img);
            drawable_add.setBounds(0, 0, drawable_add.getMinimumWidth(), drawable_add.getMinimumHeight());
            final Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.add_img_presseed);
            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
            if (advertsing_mtwnum > 1 && advertsing_mtwnum < Integer.parseInt(count_of_frame)) {
                madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
            } else if (advertsing_mtwnum == 1 || advertsing_mtwnum < 1){
                madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
                madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
            } else if (advertsing_mtwnum == Integer.parseInt(count_of_frame) || advertsing_mtwnum > Integer.parseInt(count_of_frame)){
                madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
            }
            madvertising_mtw_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advertsing_mtwnum > 1) {
                        madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
                        advertsing_mtwnum = advertsing_mtwnum - 1;
                        if (advertsing_mtwnum == 1) {
                            madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
                        } else {
                            madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);

                        }
                        madvertising_price.setText(getadvertising(advertisingprice,advertsing_mtwnum));
                        madvertising_mtw_num.setText(String.valueOf(advertsing_mtwnum));
                    } else {
                        madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
                        madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
                    }
                }
            });
            madvertising_mtw_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advertsing_mtwnum > 0) {
                        madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                        if (count_of_frame != null) {
                            if (count_of_frame.length() != 0) {
                                if (advertsing_mtwnum < Integer.parseInt(count_of_frame)) {
                                    advertsing_mtwnum = advertsing_mtwnum + 1;
                                    madvertising_price.setText(getadvertising(advertisingprice, advertsing_mtwnum));
                                    madvertising_mtw_num.setText(String.valueOf(advertsing_mtwnum));
                                    if (advertsing_mtwnum < Integer.parseInt(count_of_frame)) {
                                        madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
                                    } else {
                                        madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                                    }
                                } else {
                                    madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                                }
                            } else {
                                madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                            }
                        } else {
                            madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                        }
                    } else {
                        madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
                        madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                    }
                }
            });

        }
        if (resourcetype == 1 || resourcetype == 3) {
            if (adaptertermtypeid.equals("-1")) {
                if (termtype_workday == true && termtype_weekkday == false) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day_work));
                } else if (termtype_weekkday == true && termtype_workday == false) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day_week));
                } else if (termtype_weekkday == true && termtype_workday == true) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day));
                }
            } else {
                mcalendarclick_remind_layout.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (resourcetype == 1 || resourcetype == 3) {
            gestureDetector.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            float a = (e1.getX() - e2.getX());
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }
//    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
//            float a = (e1.getX() - e2.getX());
//            if (e1.getX() - e2.getX() > 20) {
//                // 像左滑动
//                enterNextMonth(gvFlag);
//                return true;
//            } else if (e1.getX() - e2.getX() < -20) {
//                // 向右滑动
//                enterPrevMonth(gvFlag);
//                return true;
//            }
//            return false;
//        }
//    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        ChooseSpecificationsAdapter.clear_mapitemclick();
        ChooseSpecificationsAdapter.clear_mapitemprice();
        ChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
        ChooseSpecificationsAdapter.clear_mapitemweather();
        calV = new ChooseSpecificationsAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,demensions,this,1);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        ChooseSpecificationsAdapter.clear_mapitemclick();
        ChooseSpecificationsAdapter.clear_mapitemprice();
        ChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
        ChooseSpecificationsAdapter.clear_mapitemweather();
        calV = new ChooseSpecificationsAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,demensions,this,1);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        gridView = new MyGridview(this);
        gridView.setBackgroundColor(getResources().getColor(R.color.calendar_bg));
        gridView.setNumColumns(7);
        gridView.setColumnWidth(width / 7);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        gridView.setGravity(Gravity.CENTER);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setLayoutParams(params);
    }
    @OnClick({
            R.id.calendarclick_remind_close,
            R.id.choosespecifications_buttombtn,
            R.id.choosepecifications_choosetime_instruction,
            R.id.choose_specifications_carts_btn,
            R.id.choose_specifications_paid_order_btn,
            R.id.choosespecifications_deposit_remind_close
    })
    public void remindclick(View v) {
        switch (v.getId()) {
            case R.id.calendarclick_remind_close:
                mcalendarclick_remind_layout.setVisibility(View.GONE);
                break;
            case R.id.choosespecifications_buttombtn:
                for (int i = 0; i < sizelist.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().get(sizelist.get(i).toString())) {
                        size = sizelist.get(i).toString();
                    }
                }
                for (int i = 0; i < datapriceunit.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().get(datapriceunit.get(i).toString())) {
                        lease_term_type =term_types.get(datapriceunit.get(i).toString()).toString() ;
                    }
                }
                for (int i = 0; i < custom_dimension_list.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().get(custom_dimension_list.get(i).toString())) {
                        custom_dimension = custom_dimension_list.get(i).toString() ;
                    }
                }

                if (FieldDateStringList != null) {
                    FieldDateStringList.clear();
                }
                for(int i = 0; i < mdatelist.size(); i++) {
                    String str=chineseDateFormat.format(mdatelist.get(i));
                    FieldDateStringList.add(str);
                }
                if (resourcetype == 1 || resourcetype == 3) {
                    if (mdatelist.size() > 0 && size.length() > 0 && lease_term_type.length() > 0) {
                        if (resourcetype == 3) {
                            if (mdatelist.size() < minimum_order_quantity) {
                                MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                        String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
                                return;
                            }
                        }
                        Intent fieldinfo = new Intent();
                        fieldinfo.putExtra("arraylist_date",(Serializable) mdatelist);
                        fieldinfo.putExtra("jsonarraydatastr",(Serializable) FieldDateStringList);
                        fieldinfo.putExtra("lease_term_type", lease_term_type);
                        fieldinfo.putExtra("size", size);
                        if (custom_dimension!= null) {
                            if (iscustom_dimension == false) {
                                fieldinfo.putExtra("custom_dimension", "");
                            } else {
                                fieldinfo.putExtra("custom_dimension", custom_dimension);
                            }
                        }
                        if (checkedstart_date != null) {
                            fieldinfo.putExtra("checkedstart_date", checkedstart_date);
                        }
                        if (checkedend_date != null) {
                            fieldinfo.putExtra("checkedend_date", checkedend_date);
                        }

                        if (operationtype == 1) {
                            ChooseSpecificationsActivity.this.setResult(1, fieldinfo);
                        } else if (operationtype == 2) {
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                        } else if (operationtype == 3) {
                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                        }
                        ChooseSpecificationsActivity.this.finish();
                    } else {
                        if (mdatelist.size() == 0) {
                            MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                        }
                    }
                } else if(resourcetype == 2) {
                    if (size.length() > 0 && lease_term_type.length() > 0) {
                        Intent fieldinfo = new Intent();
                        fieldinfo.putExtra("lease_term_type", lease_term_type);
                        fieldinfo.putExtra("size", size);
                        fieldinfo.putExtra("cont",advertsing_mtwnum);
                        if (custom_dimension!= null && custom_dimension.length() > 0) {
                            fieldinfo.putExtra("custom_dimension", custom_dimension);
                        }

                        if (operationtype == 1) {
                            ChooseSpecificationsActivity.this.setResult(1, fieldinfo);
                        } else if (operationtype == 2) {
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                        } else if (operationtype == 3) {
                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                        }
                        ChooseSpecificationsActivity.this.finish();
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                    }
                }
                break;
            case R.id.choosepecifications_choosetime_instruction:
                show_choosesize_reminddialog();
                break;
            case R.id.choose_specifications_carts_btn:
                for (int i = 0; i < sizelist.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().get(sizelist.get(i).toString())) {
                        size = sizelist.get(i).toString();
                    }
                }
                for (int i = 0; i < datapriceunit.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().get(datapriceunit.get(i).toString())) {
                        lease_term_type =term_types.get(datapriceunit.get(i).toString()).toString() ;
                    }
                }
                for (int i = 0; i < custom_dimension_list.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().get(custom_dimension_list.get(i).toString())) {
                        custom_dimension = custom_dimension_list.get(i).toString() ;
                    }
                }

                if (FieldDateStringList != null) {
                    FieldDateStringList.clear();
                }
                for(int i = 0; i < mdatelist.size(); i++) {
                    String str=chineseDateFormat.format(mdatelist.get(i));
                    FieldDateStringList.add(str);
                }
                if (resourcetype == 1 || resourcetype == 3) {
                    if (mdatelist.size() > 0 && size.length() > 0 && lease_term_type.length() > 0) {
                        if (LoginManager.isLogin()) {
                            if (resourcetype == 3) {
                                if (mdatelist.size() < minimum_order_quantity) {
                                    MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                            String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
                                    return;
                                }
                            }
                            Intent fieldinfo = new Intent();
                            fieldinfo.putExtra("arraylist_date",(Serializable) mdatelist);
                            fieldinfo.putExtra("jsonarraydatastr",(Serializable) FieldDateStringList);
                            fieldinfo.putExtra("lease_term_type", lease_term_type);
                            fieldinfo.putExtra("size", size);
                            if (custom_dimension!= null) {
                                if (iscustom_dimension == false) {
                                    fieldinfo.putExtra("custom_dimension", "");
                                } else {
                                    fieldinfo.putExtra("custom_dimension", custom_dimension);
                                }
                            }

                            if (checkedstart_date != null) {
                                fieldinfo.putExtra("checkedstart_date", checkedstart_date);
                            }
                            if (checkedend_date != null) {
                                fieldinfo.putExtra("checkedend_date", checkedend_date);
                            }
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                            ChooseSpecificationsActivity.this.finish();
                        } else {
                            Intent loginintent = new Intent(ChooseSpecificationsActivity.this, LoginActivity.class);
                            startActivity(loginintent);
                        }
                    } else {
                        if (mdatelist.size() == 0) {
                            MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                        }
                    }
                } else if(resourcetype == 2) {
                    if (size.length() > 0 && lease_term_type.length() > 0) {
                        if (LoginManager.isLogin()) {
                            Intent fieldinfo = new Intent();
                            fieldinfo.putExtra("lease_term_type", lease_term_type);
                            fieldinfo.putExtra("size", size);
                            fieldinfo.putExtra("cont",advertsing_mtwnum);
                            if (custom_dimension!= null && custom_dimension.length() > 0) {
                                fieldinfo.putExtra("custom_dimension", custom_dimension);
                            }
                            ChooseSpecificationsActivity.this.setResult(2, fieldinfo);
                            ChooseSpecificationsActivity.this.finish();
                        } else {
                            Intent loginintent = new Intent(ChooseSpecificationsActivity.this, LoginActivity.class);
                            startActivity(loginintent);
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                    }
                }
                break;
            case R.id.choose_specifications_paid_order_btn:
                for (int i = 0; i < sizelist.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_fieldsize().get(sizelist.get(i).toString())) {
                        size = sizelist.get(i).toString();
                    }
                }
                for (int i = 0; i < datapriceunit.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_priceunit().get(datapriceunit.get(i).toString())) {
                        lease_term_type =term_types.get(datapriceunit.get(i).toString()).toString() ;
                    }
                }
                for (int i = 0; i < custom_dimension_list.size(); i++) {
                    if (ChooseSpecificationsMygridviewAdapter.getIsSelected_custom_dimension().get(custom_dimension_list.get(i).toString())) {
                        custom_dimension = custom_dimension_list.get(i).toString() ;
                    }
                }

                if (FieldDateStringList != null) {
                    FieldDateStringList.clear();
                }
                for(int i = 0; i < mdatelist.size(); i++) {
                    String str=chineseDateFormat.format(mdatelist.get(i));
                    FieldDateStringList.add(str);
                }
                if (resourcetype == 1 || resourcetype == 3) {
                    if (mdatelist.size() > 0 && size.length() > 0 && lease_term_type.length() > 0) {
                        if (LoginManager.isLogin()) {
                            if (resourcetype == 3) {
                                if (mdatelist.size() < minimum_order_quantity) {
                                    MessageUtils.showToast(String.valueOf(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                            String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity)));
                                    return;
                                }
                            }
                            Intent fieldinfo = new Intent();
                            fieldinfo.putExtra("arraylist_date",(Serializable) mdatelist);
                            fieldinfo.putExtra("jsonarraydatastr",(Serializable) FieldDateStringList);
                            fieldinfo.putExtra("lease_term_type", lease_term_type);
                            fieldinfo.putExtra("size", size);
                            if (custom_dimension!= null) {
                                if (iscustom_dimension == false) {
                                    fieldinfo.putExtra("custom_dimension", "");
                                } else {
                                    fieldinfo.putExtra("custom_dimension", custom_dimension);
                                }
                            }

                            if (checkedstart_date != null) {
                                fieldinfo.putExtra("checkedstart_date", checkedstart_date);
                            }
                            if (checkedend_date != null) {
                                fieldinfo.putExtra("checkedend_date", checkedend_date);
                            }
                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                            ChooseSpecificationsActivity.this.finish();
                        } else {
                            Intent loginintent = new Intent(ChooseSpecificationsActivity.this, LoginActivity.class);
                            startActivity(loginintent);
                        }
                    } else {
                        if (mdatelist.size() == 0) {
                            MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                        }
                    }
                } else if(resourcetype == 2) {
                    if (size.length() > 0 && lease_term_type.length() > 0) {
                        if (LoginManager.isLogin()) {
                            Intent fieldinfo = new Intent();
                            fieldinfo.putExtra("lease_term_type", lease_term_type);
                            fieldinfo.putExtra("size", size);
                            fieldinfo.putExtra("cont",advertsing_mtwnum);
                            if (custom_dimension!= null && custom_dimension.length() > 0) {
                                fieldinfo.putExtra("custom_dimension", custom_dimension);
                            }

                            ChooseSpecificationsActivity.this.setResult(3, fieldinfo);
                            ChooseSpecificationsActivity.this.finish();
                        } else {
                            Intent loginintent = new Intent(ChooseSpecificationsActivity.this, LoginActivity.class);
                            startActivity(loginintent);
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.choose_errormessage));
                    }
                }
                break;
            case R.id.choosespecifications_deposit_remind_close:
                mchoosespecifications_deposit_remind_layout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosepecifications_nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.choosepecifications_prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            default:
                break;
        }
    }
    @Override
    public void CalendarClick(int position) {
        // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
        if (position == -1) {

        } else {
            int startPosition = calV.getStartPositon();
            int endPosition = calV.getEndPosition();
            String[] datestrarray = ChooseSpecificationsAdapter.dayNumber[position].split("\\.");
            Date choosedate = null;
            try {
                choosedate = chineseDateFormat.parse(datestrarray[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startPosition <= position + 7 && position <= endPosition - 7) {
                String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                // String scheduleLunarDay =
                // calV.getDateByClickItem(position).split("\\.")[1];
                // //这一天的阴历
                String scheduleYear = calV.getShowYear();
                String scheduleMonth = calV.getShowMonth();
                Date clickdate = null;
                try {
                    clickdate = chineseDateFormat.parse(scheduleYear + "-" + scheduleMonth + "-" + scheduleDay);
                    if (adaptertermtypeid.equals("-1")) {
                        if (mdatelist.contains(clickdate)) {
                            mdatelist.remove(clickdate);
                            if (adaptertermtypeid.equals("-1")) {
                                if (position % 7 == 0 || position % 7 == 6) {
                                    ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 2);
                                } else {
                                    ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 1);
                                }
                            }
                            calV.notifyDataSetChanged();
                        } else {
                            if (adaptertermtypeid.equals("-1")) {
                                mdatelist.add(clickdate);
                                ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 3);
                            }
                            calV.notifyDataSetChanged();
                        }
                    } else {
                        if (mdatelist != null) {
                            mdatelist.clear();
                        }
                        checkedstart_date = clickdate;
                        checkedend_date = Constants.getchoose_enddate(clickdate,Integer.parseInt(term_type_days_map.get(adaptertermtypeid)) - 1);
                        for (int i = 0; i < ChooseSpecificationsAdapter.dayNumber.length; i++) {
                            String[] strarray = ChooseSpecificationsAdapter.dayNumber[i].split("\\.");
                            if (i < ChooseSpecificationsAdapter.daysOfMonth + ChooseSpecificationsAdapter.dayOfWeek && i >= ChooseSpecificationsAdapter.dayOfWeek) {
                                if (Constants.date_interval(strarray[3],reserve_days,false) ) {
                                    Date datetmp = chineseDateFormat.parse(strarray[3]);
                                    if (Constants.ischoosedate(checkedstart_date,checkedend_date,datetmp)) {
                                        if (!mdatelist.contains(datetmp)) {
                                            mdatelist.add(datetmp);
                                        }
                                        ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 3);
                                    } else {
                                        if (deadline_date.length() > 0 && activity_start_date.length() > 0 &&
                                                !Constants.isdeadline_date(activity_start_date,deadline_date,strarray[3])) {
                                                ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 4);
                                        } else {
                                            if (i % 7 == 0 || i % 7 == 6) {
                                                ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 2);
                                            } else {
                                                ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 1);
                                            }
                                        }
                                    }
                                } else {
                                    if (mdatelist.size() > 0) {
                                        Date datetmp = null;
                                        try {
                                            datetmp = chineseDateFormat.parse(strarray[3]);
                                            if (Constants.ischoosedate(checkedstart_date,checkedend_date,datetmp)) {
                                                if (!mdatelist.contains(datetmp)) {
                                                    mdatelist.add(datetmp);
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (mdatelist.contains(datetmp)) {
                                            ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 3);
                                        } else {
                                            ChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 4);
                                        }
                                    }
                                }
                            }
                        }
                        calV.notifyDataSetChanged();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    public void getfieldsize_pricenuit(int position,String str) {
        if (resourcetype == 1 || resourcetype == 3) {
            if (mdatelist != null) {
                mdatelist.clear();
            }
            if (position == 0) {
                adaptersizeid = str;
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    mdatelist.addAll(mdatelisttmp);
                }
            } else if (position == 1) {
                adaptertermtypeid = term_types.get(str).toString();
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type)&& adaptercustom_dimensionid.equals(custom_dimension)) {
                    mdatelist.addAll(mdatelisttmp);
                }
            } else if (position == 2) {
                iscustom_dimension = true;
                adaptercustom_dimensionid = str;
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    mdatelist.addAll(mdatelisttmp);
                }

            } else if (position == -2) {
                adaptercustom_dimensionid = "";
                iscustom_dimension = false;
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    mdatelist.addAll(mdatelisttmp);
                }
            }
            ChooseSpecificationsAdapter.clear_mapitemclick();
            ChooseSpecificationsAdapter.clear_mapitemprice();
            ChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
            ChooseSpecificationsAdapter.clear_mapitemweather();
            calV = new ChooseSpecificationsAdapter(ChooseSpecificationsActivity.this, ChooseSpecificationsActivity.this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,mdatelist,demensions,ChooseSpecificationsActivity.this,1);
            gridView.setAdapter(calV);
        } else {
            if (position == 0) {
                adaptersizeid = str;
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    advertsing_mtwnum = advertsing_mtwnumtmp;
                    getadvertisingshow(false);
                } else {
                    getadvertisingshow(true);
                }

            } else if (position == 1) {
                adaptertermtypeid = term_types.get(str).toString();
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    advertsing_mtwnum = advertsing_mtwnumtmp;
                    getadvertisingshow(false);
                } else {
                    getadvertisingshow(true);
                }
            } else if (position == 2) {
                adaptercustom_dimensionid = str;
                if (adaptersizeid.equals(size) && adaptertermtypeid.equals(lease_term_type) && adaptercustom_dimensionid.equals(custom_dimension)) {
                    mdatelist.addAll(mdatelisttmp);
                }
            } else if (position == -2) {
                iscustom_dimension = false;
            }

        }
        if (resourcetype == 1 || resourcetype == 3) {
            if (adaptertermtypeid.equals("-1")) {
                if (termtype_workday == true && termtype_weekkday == false) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day_work));
                } else if (termtype_weekkday == true && termtype_workday == false) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day_week));
                } else if (termtype_weekkday == true && termtype_workday == true) {
                    mcalendarclick_remind_layout.setVisibility(View.GONE);
                    mcalendarclick_remind.setText(getResources().getString(R.string.choose_remind_day));
                }
            } else {
                mcalendarclick_remind_layout.setVisibility(View.GONE);
            }
        }
    }
    public void getconfigurate(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.CalendarClicklisten(this);
    }
    public void getselectfieldsize(int type,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(type,str);
        test.getfieldsize_pricenuit(this);
    }
    private String getadvertising(String price,int advertsing_mtwnum) {
        String advertisingprice = "";
        advertisingprice = Constants.getdoublepricestring(Double.parseDouble(price)*advertsing_mtwnum,1);
        return advertisingprice;
    }
    private void show_choosesize_reminddialog() {
        LayoutInflater factory = LayoutInflater.from(ChooseSpecificationsActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_choose_specifications_dialog, null);
        ImageButton mrefunt_price_detailed_btn = (ImageButton) textEntryView.findViewById(R.id.choosespecifications_dialog_closed_btn);
        newdialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,newdialog);
        mrefunt_price_detailed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
    }
    private void getadvertisingshow(boolean newmtwnum) {
        final Drawable drawable_down = getResources().getDrawable(R.drawable.down_img);
        drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
        final Drawable drawable_down_pressed = getResources().getDrawable(R.drawable.down_img_pressed);
        drawable_down_pressed.setBounds(0, 0, drawable_down_pressed.getMinimumWidth(), drawable_down_pressed.getMinimumHeight());
        final Drawable drawable_add = getResources().getDrawable(R.drawable.add_img);
        drawable_add.setBounds(0, 0, drawable_add.getMinimumWidth(), drawable_add.getMinimumHeight());
        final Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.add_img_presseed);
        drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());

        for (int i = 0; i < demensions.size(); i++) {
            if (demensions.get(i).get("size").equals(adaptersizeid) &&
                    demensions.get(i).get("lease_term_type_id").equals(adaptertermtypeid)) {
                if (newmtwnum) {
                    //2017/11/2 媒体位赋值
                    advertsing_mtwnum = Integer.parseInt(demensions.get(i).get("count_of_frame"));
                }
                if (advertsing_mtwnum > 1) {
                    madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down_pressed, null);
                } else {
                    madvertising_mtw_down.setCompoundDrawables(null, null, drawable_down, null);
                }

                madvertising_mtw_num.setText(String.valueOf(advertsing_mtwnum));
                advertisingprice = Constants.getpricestring(demensions.get(i).get("price"), 0.01);
                madvertising_price.setText(getadvertising(advertisingprice, advertsing_mtwnum));
                count_of_frame =  demensions.get(i).get("count_of_frame");
                madvertising_framenumber.setText(getResources().getString(R.string.advertding_framenum_txt)+demensions.get(i).get("count_of_frame")
                        +getResources().getString(R.string.advertding_framenum_other_txt));
                if (advertsing_mtwnum < Integer.parseInt(demensions.get(i).get("count_of_frame"))) {
                    madvertising_mtw_add.setCompoundDrawables(drawable_add_pressed,null,null, null);
                } else {
                    madvertising_mtw_add.setCompoundDrawables(drawable_add, null,null, null);
                }
                break;
            }
        }
    }
}
