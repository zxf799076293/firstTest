package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.fragment.SearchAdvListFragment;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.view.MyGridview;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ResourcesScreeningNewAdapter extends BaseAdapter {
    private ArrayList<HashMap<Object,Object>> data = new ArrayList<HashMap<Object,Object>>();
    private Context mcontext;
    private SearchListFragment mactivity;
    private BaiduMapActivity baiduMapActivity;
    private LayoutInflater mInflater = null;
    private int mAdapterType;
    private int showall = 0;
    private SearchAdvListFragment mAdvListActivity;
    private FieldInfoActivity fieldInfoActivity;
    private static HashMap<String,String> edittextmap = new HashMap<String, String>();
    public ResourcesScreeningNewAdapter(Context context, SearchListFragment activity, ArrayList<HashMap<Object, Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.mAdapterType = invoicetype;
    }
    public ResourcesScreeningNewAdapter(Context context, BaiduMapActivity activity, ArrayList<HashMap<Object, Object>> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.baiduMapActivity = activity;
        this.data = datas;
        this.mAdapterType = invoicetype;
    }
    public ResourcesScreeningNewAdapter(Context context, FieldInfoActivity activity,
                                        ArrayList<HashMap<Object, Object>> datas, int type) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.data = datas;
        this.mAdapterType = type;
        this.fieldInfoActivity = activity;
    }

    public ResourcesScreeningNewAdapter(Context context, SearchAdvListFragment activity, ArrayList<HashMap<Object, Object>> datas, int type) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.data = datas;
        this.mAdapterType = type;
        this.mAdvListActivity = activity;
    }

    @Override
    public int getCount() {
        if(data != null){
            return data.size();
        } else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_resourcesscreeningnew_gridviewitem, null);
            holder.mscreening_listtype_layout = (RelativeLayout) convertView.findViewById(R.id.screening_listtype_layout);
            holder.mresourcesscreening_item_textview = (TextView) convertView.findViewById(R.id.resourcesscreening_item_textview);
            holder.mresourcesscreening_item_gridview = (MyGridview) convertView.findViewById(R.id.resourcesscreening_item_gridview);
            holder.mresourcesscreening_item_imageview = (CheckBox) convertView.findViewById(R.id.resourcesscreening_item_imageview);
            holder.mscreening_othertype_layout = (LinearLayout) convertView.findViewById(R.id.screening_othertype_layout);
            holder.mminimum_edit = (EditText) convertView.findViewById(R.id.minimum_edit);
            holder.mscreening_typeother_layout = (LinearLayout) convertView.findViewById(R.id.screening_typeother_layout);
            holder.mmaximum_edit = (EditText) convertView.findViewById(R.id.maximum_edit);
            holder.mScreenTopView = (View) convertView.findViewById(R.id.search_screen_top_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mScreenTopView.setVisibility(View.GONE);
        if (data.get(position).get("type").toString().equals("field_labels")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_field_labels_txt));
        } else if (data.get(position).get("type").toString().equals("deposit")) {//详情筛选
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.orderconfirm_deposit_text));
        } else if (data.get(position).get("type").toString().equals("lease_term_type_id")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.txt_fourdenominatedunit_titletxt));
        } else if (data.get(position).get("type").toString().equals("size")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.txt_addfieldfour_fieldsizetxt));
        } else if (data.get(position).get("type").toString().equals("customDimension")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.fieldinfo_screen_custom_dimension_adapter_str));
        } else if (data.get(position).get("type").toString().equals("develops")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.module_res_screening_develop));
        } else if (data.get(position).get("type").toString().equals("category")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_resourcefieldtype));
        } else if (data.get(position).get("type").toString().indexOf("attributes") !=-1) {
            holder.mresourcesscreening_item_textview.setText(data.get(position).get("name").toString());
        } else if (data.get(position).get("type").toString().equals("activity_type_id")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_activity_type_str));
        } else if (data.get(position).get("type").toString().equals("ad_type_id")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_advertisingtype));
        } else if (data.get(position).get("type").toString().equals("location_types")) {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.module_screening_location_type_hint));
        } else {
            holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (mAdapterType == 0) {
            final ArrayList<HashMap<Object,Object>> datatemp = new ArrayList<>();
            datatemp.addAll(data);
            final ResourcesScreeningItemAdapter adapter;
            if (baiduMapActivity == null && mactivity != null) {
                adapter = new ResourcesScreeningItemAdapter(mcontext,mactivity,(ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")),0);
            } else {
                adapter = new ResourcesScreeningItemAdapter(baiduMapActivity,baiduMapActivity,(ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")),0);
            }
            boolean itemlist_showall = false;
            if (data.get(position).get("itemtype") != null && ((Integer)data.get(position).get("itemtype") == 1 ||
                    (Integer)data.get(position).get("itemtype") == 2)) {//输入框 1是输入最大值最小值 2 是输入面积
                holder.mscreening_othertype_layout.setVisibility(View.VISIBLE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.GONE);
                holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                holder.mminimum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"min"));
                if ((Integer)data.get(position).get("itemtype") == 2) {
                    holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_minimum_area_txt));
                    SpannableString areahint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                    holder.mminimum_edit.setHint(areahint);
                    mineditwatch(holder.mminimum_edit,data.get(position).get("type").toString());
                    holder.mscreening_typeother_layout.setVisibility(View.GONE);
                } else {
                    mineditwatch(holder.mminimum_edit,data.get(position).get("type").toString());
                    maxeditwatch(holder.mmaximum_edit,data.get(position).get("type").toString());
                    holder.mmaximum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"max"));
                    holder.mscreening_typeother_layout.setVisibility(View.VISIBLE);
                    if (data.get(position).get("type").toString().equals("year")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.review_year_text));
                        SpannableString minyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_yearedit_hint));
                        holder.mminimum_edit.setHint(minyearhint);
                        SpannableString maxyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_yearedit_hint));
                        holder.mmaximum_edit.setHint(maxyearhint);
                    } else if (data.get(position).get("type").toString().equals("area")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_minimum_area_txt));
                        SpannableString minyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                        holder.mminimum_edit.setHint(minyearhint);
                        SpannableString maxyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_peoplesedit_hint));
                        holder.mmaximum_edit.setHint(maxyearhint);
                    } else if (data.get(position).get("type").toString().equals("numper_of_people")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.review_label_text));
                        SpannableString minnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                        holder.mminimum_edit.setHint(minnumper_of_peoplehint);
                        SpannableString maxnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_peoplesedit_hint));
                        holder.mmaximum_edit.setHint(maxnumper_of_peoplehint);
                    } else if (data.get(position).get("type").toString().equals("price")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_pricetype));
                        SpannableString minnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_pricetype_lowPriceedit_hint));
                        holder.mminimum_edit.setHint(minnumper_of_peoplehint);
                        SpannableString maxnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_pricetype_highPriceedit_hint));
                        holder.mmaximum_edit.setHint(maxnumper_of_peoplehint);
                    }

                }
            } else {
                holder.mscreening_othertype_layout.setVisibility(View.GONE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.VISIBLE);

                holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                if (((ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist"))) != null &&
                        ((ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist"))).size() > 4
                        ) {
                    holder.mresourcesscreening_item_imageview.setVisibility(View.VISIBLE);
                } else {
                    holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                }
                if (data.get(position).get("type").toString().equals("field_labels")) {
                    if (baiduMapActivity == null && mactivity != null &&
                            mactivity.field_labels_int == 1) {
                        itemlist_showall = true;
                    } else if (baiduMapActivity != null && mactivity == null &&
                            baiduMapActivity.field_labels_int == 1) {
                        itemlist_showall = true;
                    }
                } else if (data.get(position).get("type").toString().equals("location_types")) {//2018/12/11 位置类型
                    if (baiduMapActivity == null && mactivity != null &&
                            mactivity.mLocationTypeChooseInt == 1) {
                        itemlist_showall = true;
                    }
                } else if (data.get(position).get("type").toString().equals("category")) {
                    if (baiduMapActivity == null && mactivity != null &&
                            mactivity.category_id_int == 1) {
                        itemlist_showall = true;
                    } else if (baiduMapActivity != null && mactivity == null &&
                            baiduMapActivity.category_id_int == 1) {
                        itemlist_showall = true;
                    }
                } else if (data.get(position).get("type").toString().indexOf("attributes") !=-1) {
                    if (baiduMapActivity == null && mactivity != null &&
                            mactivity.attributesChooseMap.get(data.get(position).get("type").toString()) == 1) {
                        itemlist_showall = true;
                    } else if (baiduMapActivity != null && mactivity == null &&
                            baiduMapActivity.attributesChooseMap.get(data.get(position).get("type").toString()) == 1) {
                        itemlist_showall = true;
                    }
                }
                holder.mresourcesscreening_item_imageview.setChecked(itemlist_showall);
                adapter.notifyDataSetChanged(itemlist_showall,
                        (ArrayList<HashMap<Object,Object>>)(datatemp.get(position).get("datalist")));
            }

            holder.mresourcesscreening_item_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean finalItemlist_showall = false;
                    if (data.get(position).get("type").toString().equals("field_labels")) {
                        if (baiduMapActivity == null && mactivity != null &&
                                mactivity.field_labels_int == 1) {
                            finalItemlist_showall = true;
                        } else if (baiduMapActivity != null && mactivity == null &&
                                baiduMapActivity.field_labels_int == 1) {
                            finalItemlist_showall = true;
                        }
                    } else if (data.get(position).get("type").toString().equals("location_types")) {//2018/12/11 位置类型
                        if (baiduMapActivity == null && mactivity != null &&
                                mactivity.mLocationTypeChooseInt == 1) {
                            finalItemlist_showall = true;
                        }
                    } else if (data.get(position).get("type").toString().equals("category")) {
                        if (baiduMapActivity == null && mactivity != null &&
                                mactivity.category_id_int == 1) {
                            finalItemlist_showall = true;
                        } else if (baiduMapActivity != null && mactivity == null &&
                                baiduMapActivity.category_id_int == 1) {
                            finalItemlist_showall = true;
                        }
                    } else if (data.get(position).get("type").toString().indexOf("attributes") !=-1) {
                        if (baiduMapActivity == null && mactivity != null &&
                                mactivity.attributesChooseMap.get(data.get(position).get("type").toString()) == 1) {
                            finalItemlist_showall = true;
                        } else if (baiduMapActivity != null && mactivity == null &&
                                baiduMapActivity.attributesChooseMap.get(data.get(position).get("type").toString()) == 1) {
                            finalItemlist_showall = true;
                        }
                    }
                    if (finalItemlist_showall) {
                        adapter.notifyDataSetChanged(false,
                                (ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")));
                    } else {
                        adapter.notifyDataSetChanged(true,
                                (ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")));
                    }
                    if (data.get(position).get("type").toString().equals("field_labels")) {
                        if (!finalItemlist_showall) {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.field_labels_int = 1;
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.field_labels_int = 1;
                            }
                        } else {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.field_labels_int = 0;
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.field_labels_int = 0;
                            }
                        }
                    } else if (data.get(position).get("type").toString().equals("location_types")) {//2018/12/11 位置类型
                        if (!finalItemlist_showall) {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.mLocationTypeChooseInt = 1;
                            }
                        } else {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.mLocationTypeChooseInt = 0;
                            }
                        }
                    } else if (data.get(position).get("type").toString().equals("category")) {
                        if (!finalItemlist_showall) {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.category_id_int = 1;
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.category_id_int = 1;
                            }
                        } else {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.category_id_int = 0;
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.category_id_int = 0;
                            }
                        }
                    } else if (data.get(position).get("type").toString().indexOf("attributes") !=-1) {
                        if (!finalItemlist_showall) {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.attributesChooseMap.put(data.get(position).get("type").toString(),1);
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.attributesChooseMap.put(data.get(position).get("type").toString(),1);
                            }
                        } else {
                            if (baiduMapActivity == null && mactivity != null) {
                                mactivity.attributesChooseMap.put(data.get(position).get("type").toString(),0);
                            } else if (baiduMapActivity != null && mactivity == null) {
                                baiduMapActivity.attributesChooseMap.put(data.get(position).get("type").toString(),0);
                            }
                        }
                    }
                }
            });
        } else if (mAdapterType == 1) {
            if (data.get(position).get("itemtype") != null && ((Integer)data.get(position).get("itemtype") == 1)) {//输入框 1是输入最大值最小值 2 是输入面积
                holder.mscreening_othertype_layout.setVisibility(View.VISIBLE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.GONE);
                holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                holder.mminimum_edit.setText(edittextmap.get(data.get(position).get("type").toString() + "min"));
                mineditwatch(holder.mminimum_edit, data.get(position).get("type").toString());
                maxeditwatch(holder.mmaximum_edit, data.get(position).get("type").toString());
                holder.mmaximum_edit.setText(edittextmap.get(data.get(position).get("type").toString() + "max"));
                holder.mscreening_typeother_layout.setVisibility(View.VISIBLE);
                if (data.get(position).get("type").toString().equals("price")) {
                    holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.search_price_type_txt));
                    SpannableString minnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_pricetype_lowPriceedit_hint));
                    holder.mminimum_edit.setHint(minnumper_of_peoplehint);
                    SpannableString maxnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_pricetype_highPriceedit_hint));
                    holder.mmaximum_edit.setHint(maxnumper_of_peoplehint);
                }
                mineditwatch(holder.mminimum_edit,data.get(position).get("type").toString());
                maxeditwatch(holder.mmaximum_edit,data.get(position).get("type").toString());
                holder.mmaximum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"max"));
                holder.mminimum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"min"));
            } else {
                holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                holder.mscreening_othertype_layout.setVisibility(View.GONE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.VISIBLE);
                ResourcesScreeningItemAdapter adapter = null;
                if (data.get(position).get("type").toString().equals("deposit")) {//详情筛选
                    holder.mresourcesscreening_item_gridview.setNumColumns(4);
                    adapter = new ResourcesScreeningItemAdapter(mcontext,fieldInfoActivity,(ArrayList<HashMap<Object,Object>>) (data.get(position).get("datalist")),1,4);
                    holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                } else if (data.get(position).get("type").toString().equals("lease_term_type_id")) {
                    holder.mresourcesscreening_item_gridview.setNumColumns(2);
                    adapter = new ResourcesScreeningItemAdapter(mcontext,fieldInfoActivity,(ArrayList<HashMap<Object,Object>>) (data.get(position).get("datalist")),1,2);
                    holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                } else if (data.get(position).get("type").toString().equals("size")) {
                    holder.mresourcesscreening_item_gridview.setNumColumns(2);
                    adapter = new ResourcesScreeningItemAdapter(mcontext,fieldInfoActivity,(ArrayList<HashMap<Object,Object>>) (data.get(position).get("datalist")),1,2);
                    holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                } else if (data.get(position).get("type").toString().equals("customDimension")) {
                    holder.mresourcesscreening_item_gridview.setNumColumns(3);
                    adapter = new ResourcesScreeningItemAdapter(mcontext,fieldInfoActivity,(ArrayList<HashMap<Object,Object>>) (data.get(position).get("datalist")),1,3);
                    holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged(true,
                        (ArrayList<HashMap<Object,Object>>)(data.get(position).get("datalist")));
            }
        } else if (mAdapterType == 3) {
            final ArrayList<HashMap<Object,Object>> datatemp = new ArrayList<>();
            datatemp.addAll(data);
            final ResourcesScreeningItemAdapter adapter;
            adapter = new ResourcesScreeningItemAdapter(mcontext,mAdvListActivity,(ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")),0);

            if (data.get(position).get("itemtype") != null && ((Integer)data.get(position).get("itemtype") == 1 ||
                    (Integer)data.get(position).get("itemtype") == 2)) {//输入框 1是输入最大值最小值 2 是输入面积
                holder.mscreening_othertype_layout.setVisibility(View.VISIBLE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.GONE);
                holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                holder.mminimum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"min"));
                if ((Integer)data.get(position).get("itemtype") == 2) {
                    holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_minimum_area_txt));
                    SpannableString areahint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                    holder.mminimum_edit.setHint(areahint);
                    mineditwatch(holder.mminimum_edit,data.get(position).get("type").toString());
                    holder.mscreening_typeother_layout.setVisibility(View.GONE);
                } else {
                    mineditwatch(holder.mminimum_edit,data.get(position).get("type").toString());
                    maxeditwatch(holder.mmaximum_edit,data.get(position).get("type").toString());
                    holder.mmaximum_edit.setText(edittextmap.get(data.get(position).get("type").toString()+"max"));
                    holder.mscreening_typeother_layout.setVisibility(View.VISIBLE);
                    if (data.get(position).get("type").toString().equals("year")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.review_year_text));
                        SpannableString minyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_yearedit_hint));
                        holder.mminimum_edit.setHint(minyearhint);
                        SpannableString maxyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_yearedit_hint));
                        holder.mmaximum_edit.setHint(maxyearhint);
                    } else if (data.get(position).get("type").toString().equals("area")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.resourcesscreening_minimum_area_txt));
                        SpannableString minyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                        holder.mminimum_edit.setHint(minyearhint);
                        SpannableString maxyearhint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_peoplesedit_hint));
                        holder.mmaximum_edit.setHint(maxyearhint);
                    } else if (data.get(position).get("type").toString().equals("numper_of_people")) {
                        holder.mresourcesscreening_item_textview.setText(mcontext.getResources().getString(R.string.review_label_text));
                        SpannableString minnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_minimum_peoplesedit_hint));
                        holder.mminimum_edit.setHint(minnumper_of_peoplehint);
                        SpannableString maxnumper_of_peoplehint = new SpannableString(mcontext.getString(R.string.resourcesscreening_maximum_peoplesedit_hint));
                        holder.mmaximum_edit.setHint(maxnumper_of_peoplehint);
                    }

                }
            } else {
                holder.mscreening_othertype_layout.setVisibility(View.GONE);
                holder.mresourcesscreening_item_gridview.setVisibility(View.VISIBLE);
                holder.mresourcesscreening_item_gridview.setAdapter(adapter);
                if (((ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist"))) != null &&
                        ((ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist"))).size() > 4
                        ) {
                    holder.mresourcesscreening_item_imageview.setVisibility(View.VISIBLE);
                } else {
                    holder.mresourcesscreening_item_imageview.setVisibility(View.GONE);
                }
                boolean itemlist_showall = false;
                if (data.get(position).get("type").toString().equals("activity_type_id")) {
                    if (mAdvListActivity.activity_type_id_int == 1) {
                        itemlist_showall = true;
                    }
                }
                holder.mresourcesscreening_item_imageview.setChecked(itemlist_showall);
                adapter.notifyDataSetChanged(itemlist_showall,
                        (ArrayList<HashMap<Object,Object>>)(datatemp.get(position).get("datalist")));
            }
            holder.mresourcesscreening_item_imageview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        adapter.notifyDataSetChanged(true,
                                (ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")));

                    } else {
                        adapter.notifyDataSetChanged(false,
                                (ArrayList<HashMap<Object,Object>>) (datatemp.get(position).get("datalist")));
                    }
                    if (data.get(position).get("type").toString().equals("activity_type_id")) {
                        if (isChecked) {
                            mAdvListActivity.activity_type_id_int = 1;
                        } else {
                            mAdvListActivity.activity_type_id_int = 0;
                        }
                    }
                }
            });
        }

        return convertView;
    }
    static class ViewHolder
    {
        public RelativeLayout mscreening_listtype_layout;
        public TextView mresourcesscreening_item_textview;
        public MyGridview mresourcesscreening_item_gridview;
        public CheckBox mresourcesscreening_item_imageview;
        public LinearLayout mscreening_othertype_layout;
        public EditText mminimum_edit;
        public LinearLayout mscreening_typeother_layout;
        public EditText mmaximum_edit;
        private View mScreenTopView;
    }
    private void mineditwatch(final EditText edittext, String type) {
        if (type.equals("year")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("yearmin",edittext.getText().toString());
                    }
                }
            });
        } else if (type.equals("area")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("areamin",edittext.getText().toString());
                    }
                }
            });
        } else if (type.equals("price")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("pricemin",edittext.getText().toString());
                    }
                }
            });
        } else if (type.equals("numper_of_people")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("numper_of_peoplemin",edittext.getText().toString());
                    }
                }
            });
        }

    }
    private void maxeditwatch(final EditText edittext, String type) {
        if (type.equals("year")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("yearmax",edittext.getText().toString());
                    }
                }
            });

        } else if (type.equals("area")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("areamax",edittext.getText().toString());
                    }
                }
            });
        } else if (type.equals("price")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("pricemax",edittext.getText().toString());
                    }
                }
            });
        } else if (type.equals("numper_of_people")) {
            edittext.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        // 此处为得到焦点时的处理内容
                    } else {
                        // 此处为失去焦点时的处理内容
                        edittextmap.put("numper_of_peoplemax",edittext.getText().toString());
                    }
                }
            });
        }
    }

    public  static HashMap<String,String> getedittextmap() {
        return edittextmap;
    }

    public  static void seedittextmap(HashMap<String,String> isSelected) {
        ResourcesScreeningNewAdapter.edittextmap = isSelected;
    }
    public  static void clear_edittextmap() {
        if (edittextmap != null) {
            edittextmap.clear();
        }
    }
}
