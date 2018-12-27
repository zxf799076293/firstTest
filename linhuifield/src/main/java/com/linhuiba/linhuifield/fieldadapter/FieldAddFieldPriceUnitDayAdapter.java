package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.Field_AddField_FiveEditerPriceActivity;
import com.linhuiba.linhuifield.fieldmodel.AddfieldEditPriceWeekChooseModel;
import com.linhuiba.linhuifield.fieldview.FieldMyGridView;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class FieldAddFieldPriceUnitDayAdapter extends BaseExpandableListAdapter {
    private List<AddfieldEditPriceWeekChooseModel> mGroupDataList;
    private List<List<AddfieldEditPriceWeekChooseModel>> mChildDataList;
    private LayoutInflater mInflater = null;
    private Context mContext;
    private Field_AddField_FiveEditerPriceActivity mActivity;
    private int mAdapterType;//1是常规规格 2是特殊规格
    public FieldAddFieldPriceUnitDayAdapter(Context context, Field_AddField_FiveEditerPriceActivity activity,
                                            List<AddfieldEditPriceWeekChooseModel> groupList, List<List<AddfieldEditPriceWeekChooseModel>> childList,
                                            int type){
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mActivity = activity;
        this.mGroupDataList = groupList;
        this.mChildDataList = childList;
        this.mAdapterType = type;
    }
    @Override
    public int getGroupCount() {
        if (mGroupDataList != null &&
                mGroupDataList.size() > 0) {
            return mGroupDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mChildDataList.get(groupPosition).size() > 0) {
            return mChildDataList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildDataList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.field_activity_addfield_unit_day_item, null);
            holder.mAddfieldEditPriceGroupLL = (LinearLayout) convertView.findViewById(R.id.addfield_edit_price_day_item_group);
            holder.mAddfieldEditPriceChildLL = (LinearLayout) convertView.findViewById(R.id.addfield_edit_price_day_item_child);
            holder.mAddfieldEditPriceSizeTV = (TextView) convertView.findViewById(R.id.addfield_edit_price_unit_day_size_tv);
            holder.mAddfieldEditPriceSpecificationTV = (TextView) convertView.findViewById(R.id.addfield_edit_price_unit_day_specification_tv);
            holder.mAddfieldEditPriceCustomDimensionTV = (TextView) convertView.findViewById(R.id.addfield_edit_price_custom_dimension_tv);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder)convertView.getTag();
        }
        holder.mAddfieldEditPriceGroupLL.setVisibility(View.VISIBLE);
        holder.mAddfieldEditPriceChildLL.setVisibility(View.GONE);
        if (mGroupDataList.get(groupPosition).getSize() != null &&
                mGroupDataList.get(groupPosition).getSize().length() > 0) {
            holder.mAddfieldEditPriceSizeTV.setText(mGroupDataList.get(groupPosition).getSize());
        } else {
            holder.mAddfieldEditPriceSizeTV.setText(mContext.getResources().getString(R.string.fieldinfo_no_parameter_message));
        }
        if (mAdapterType == 1) {
            holder.mAddfieldEditPriceSpecificationTV.setText(mContext.getResources().getString(R.string.addfield_five_addfieldprice_txt));
        } else if (mAdapterType == 2) {
            holder.mAddfieldEditPriceSpecificationTV.setText(mContext.getResources().getString(R.string.addfield_five_specialprice_txt));
        }
        if (mAdapterType == 1) {
            holder.mAddfieldEditPriceCustomDimensionTV.setVisibility(View.INVISIBLE);
        } else if (mAdapterType == 2) {
            if (mGroupDataList.get(groupPosition).getCustom_dimension() != null && mGroupDataList.get(groupPosition).getCustom_dimension().length() > 0) {
                holder.mAddfieldEditPriceCustomDimensionTV.setText("  " + mGroupDataList.get(groupPosition).getCustom_dimension() + "  ");
                holder.mAddfieldEditPriceCustomDimensionTV.setVisibility(View.VISIBLE);
            } else {
                holder.mAddfieldEditPriceCustomDimensionTV.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.field_activity_addfield_unit_day_item, null);
            holder.mAddfieldEditPriceGroupLL = (LinearLayout) convertView.findViewById(R.id.addfield_edit_price_day_item_group);
            holder.mAddfieldEditPriceChildLL = (LinearLayout) convertView.findViewById(R.id.addfield_edit_price_day_item_child);
            holder.mAddfieldEditPriceGV = (FieldMyGridView) convertView.findViewById(R.id.addfield_edit_price_week_gv);
            holder.mAddfieldEditPriceET = (EditText) convertView.findViewById(R.id.addfield_edit_price_et);
            holder.mAddfieldEditPriceCB = (CheckBox) convertView.findViewById(R.id.addfield_edit_price_cb);
            holder.mChildLastView = (View) convertView.findViewById(R.id.addfield_price_child_last_view);
            holder.mGroupLastView = (View) convertView.findViewById(R.id.addfield_price_group_last_view);
            holder.mDepositLL = (LinearLayout) convertView.findViewById(R.id.addfield_price_item_deposit_ll);
            holder.mAddfieldDepositET = (EditText) convertView.findViewById(R.id.addfield_price_deposit_item_et);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder)convertView.getTag();
        }
        holder.mAddfieldEditPriceET.clearFocus();
        holder.mAddfieldEditPriceET.setCursorVisible(false);// 内容清空后将编辑框1的光标隐藏，提升用户的体验度
        holder.mAddfieldDepositET.clearFocus();
        holder.mAddfieldDepositET.setCursorVisible(false);// 内容清空后将编辑框1的光标隐藏，提升用户的体验度

        if ((groupPosition == mGroupDataList.size() || groupPosition < mGroupDataList.size())
        && (childPosition == mChildDataList.get(groupPosition).size() ||
                childPosition < mChildDataList.get(groupPosition).size())) {
            if (mChildDataList.get(groupPosition).get(childPosition).getHiteView() == 1) {
                holder.mAddfieldEditPriceGroupLL.setVisibility(View.GONE);
                holder.mAddfieldEditPriceChildLL.setVisibility(View.GONE);
            } else {
                holder.mAddfieldEditPriceGroupLL.setVisibility(View.GONE);
                holder.mAddfieldEditPriceChildLL.setVisibility(View.VISIBLE);
                addGVAdapter(holder.mAddfieldEditPriceGV,groupPosition,childPosition);
                if (mChildDataList.get(groupPosition).get(childPosition).getPrice() != null &&
                        mChildDataList.get(groupPosition).get(childPosition).getPrice().toString().length() > 0) {
                    holder.mAddfieldEditPriceET.setText(mChildDataList.get(groupPosition).get(childPosition).getPrice().toString());
                } else {
                    holder.mAddfieldEditPriceET.setText("");
                }
                //2018/11/12 发布时押金显示
                if (mActivity.isChooseDeposit) {
                    holder.mDepositLL.setVisibility(View.VISIBLE);
                    if (mChildDataList.get(groupPosition).get(childPosition).getDeposit() != null &&
                            mChildDataList.get(groupPosition).get(childPosition).getDeposit().toString().length() > 0 &&
                            Double.parseDouble(mChildDataList.get(groupPosition).get(childPosition).getDeposit()) > 0) {
                        holder.mAddfieldDepositET.setText(mChildDataList.get(groupPosition).get(childPosition).getDeposit().toString());
                    } else {
                        holder.mAddfieldDepositET.setText("");
                    }
                } else {
                    holder.mDepositLL.setVisibility(View.GONE);
                }

                if (mChildDataList.get(groupPosition).get(childPosition).getItemChoose() == 1) {
                    holder.mAddfieldEditPriceCB.setChecked(true);
                } else {
                    holder.mAddfieldEditPriceCB.setChecked(false);
                }
                onTouchListener(holder.mAddfieldEditPriceET);
                onItemClick(holder.mAddfieldEditPriceGV,holder.mAddfieldEditPriceET,groupPosition,childPosition);
                setOnClickListener(holder.mAddfieldEditPriceCB,groupPosition,childPosition,holder.mAddfieldEditPriceET);
                setListener(holder.mAddfieldEditPriceET,groupPosition,childPosition,true);
                onTouchListener(holder.mAddfieldDepositET);
                setListener(holder.mAddfieldDepositET,groupPosition,childPosition,false);
            }
            if (childPosition != mChildDataList.get(groupPosition).size() - 1) {
                holder.mChildLastView.setVisibility(View.VISIBLE);
                holder.mGroupLastView.setVisibility(View.GONE);
            } else {
                holder.mChildLastView.setVisibility(View.GONE);
                holder.mGroupLastView.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class GroupViewHolder {
        public LinearLayout mAddfieldEditPriceGroupLL;
        public LinearLayout mAddfieldEditPriceChildLL;
        public TextView mAddfieldEditPriceSizeTV;
        public TextView mAddfieldEditPriceSpecificationTV;
        public TextView mAddfieldEditPriceCustomDimensionTV;
    }
    static class ChildViewHolder {
        public LinearLayout mAddfieldEditPriceGroupLL;
        public LinearLayout mAddfieldEditPriceChildLL;
        public FieldMyGridView mAddfieldEditPriceGV;
        public EditText mAddfieldEditPriceET;
        public CheckBox mAddfieldEditPriceCB;
        public View mChildLastView;
        public View mGroupLastView;
        public LinearLayout mDepositLL;
        public EditText mAddfieldDepositET;
    }
    //价格输入框操作
    public void setListener(final EditText edit, final int groupPosition, final int childPosition, final boolean is_price){
        //2018/1/4 会卡 要修复
        edit.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    edit.addTextChangedListener(new TextWatcher(){

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                            if (s.toString().contains(".")) {
                                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                    s = s.toString().subSequence(0,
                                            s.toString().indexOf(".") + 3);
                                    edit.setText(s);
                                    edit.setSelection(s.length());
                                }
                            }
                            if (s.toString().trim().substring(0).equals(".")) {
                                s = "0" + s;
                                edit.setText(s);
                                edit.setSelection(2);
                            }

                            if (s.toString().startsWith("0")
                                    && s.toString().trim().length() > 1) {
                                if (!s.toString().substring(1, 2).equals(".")) {
                                    edit.setText(s.subSequence(0, 1));
                                    edit.setSelection(1);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (edit.getText().toString().length() > 0 &&
                                    mChildDataList.get(groupPosition).get(childPosition).getItemChoose() == 0
                                    && checkChildWeekChoose(groupPosition,childPosition) && is_price) {
                                AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                                if (mAdapterType == 1) {
                                    mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                                    mActivity.mCommonChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                                } else if (mAdapterType == 2) {
                                    mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                                    mActivity.mSpecialChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                                }
                                notifyDataSetChanged();
                            }
                        }

                    });

                } else {
                    // 此处为失去焦点时的处理内容
                    if (is_price) {
                        mChildDataList.get(groupPosition).get(childPosition).setPrice(edit.getText().toString().trim());
                        if (mAdapterType == 1) {
                            mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setPrice(edit.getText().toString().trim());
                        } else if (mAdapterType == 2) {
                            mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setPrice(edit.getText().toString().trim());
                        }
                    } else {
                        mChildDataList.get(groupPosition).get(childPosition).setDeposit(edit.getText().toString().trim());
                        if (mAdapterType == 1) {
                            mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setDeposit(edit.getText().toString().trim());
                        } else if (mAdapterType == 2) {
                            mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setDeposit(edit.getText().toString().trim());
                        }
                    }
                }
            }
        });
    }
    //checkbox 点击事件
    private void setOnClickListener(final CheckBox checkbox, final int groupPosition, final int childPosition,final EditText editText) {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2018/1/4 增加一行默认的
                if (checkbox.isChecked()) {
                    if (editText.getText().toString().trim().length() > 0 &&
                            checkChildWeekChoose(groupPosition,childPosition)) {
                        AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                        if (mAdapterType == 1) {
                            mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                            mActivity.mCommonChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                        } else if (mAdapterType == 2) {
                            mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                            mActivity.mSpecialChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                        }
                        notifyDataSetChanged();
                    } else {
                        if (mAdapterType == 1) {
                            mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setItemChoose(0);
                        } else if (mAdapterType == 2) {
                            mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setItemChoose(0);
                        }
                        notifyDataSetChanged();
                        BaseMessageUtils.showToast(mContext.getResources().getString(R.string.addfield_five_addfield_special_pointtxt));
                    }

                } else {
                    if (mAdapterType == 1) {
                        mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setHiteView(1);
                    } else if (mAdapterType == 2) {
                        mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setHiteView(1);
                    }
                    mChildDataList.get(groupPosition).get(childPosition).setHiteView(1);
                    removeGroupWeek(groupPosition,childPosition);
                    notifyDataSetChanged();
                }
            }
        });

    }
    //周几点击事件
    private void onItemClick(final FieldMyGridView gv, final EditText et,final int groupPosition, final int childPosition) {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //2018/1/4 判断是否满足条件增加一行
                if (mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(position) != null &&
                        mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(position) == 1) {
                    mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,0);
                    mGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,0);
                    if (mAdapterType == 1) {
                        mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,0);
                        mActivity.mCommonGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,0);
                    } else if (mAdapterType == 2) {
                        mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,0);
                        mActivity.mSpecialGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,0);
                    }
                    addGVAdapter(gv,groupPosition,childPosition);
                } else {
                    if (mGroupDataList.get(groupPosition).getGroupWeekChooseList().get(position) != null &&
                            mGroupDataList.get(groupPosition).getGroupWeekChooseList().get(position) == 1) {
                        BaseMessageUtils.showToast("重复了！");
                    } else {
                        if (et.getText().toString().trim().length() > 0
                                && (mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(position) == null ||
                                mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(position) == 0) &&
                                mChildDataList.get(groupPosition).get(childPosition).getItemChoose() == 0) {
                            if (mAdapterType == 1) {
                                mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                            } else if (mAdapterType == 2) {
                                mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).setItemChoose(1);
                            }
                            AddfieldEditPriceWeekChooseModel addfieldEditPriceWeekChooseModel = new AddfieldEditPriceWeekChooseModel();
                            if (mAdapterType == 1) {
                                mActivity.mCommonChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                            } else if (mAdapterType == 2) {
                                mActivity.mSpecialChildDataList.get(groupPosition).add(addfieldEditPriceWeekChooseModel);
                            }
                            mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                            mGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            if (mAdapterType == 1) {
                                mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mCommonGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            } else if (mAdapterType == 2) {
                                mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mSpecialGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            }
                            notifyDataSetChanged();
                        } else {
                            mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                            mGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            if (mAdapterType == 1) {
                                mActivity.mCommonChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mCommonGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            } else if (mAdapterType == 2) {
                                mActivity.mSpecialChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().put(position,1);
                                mActivity.mSpecialGroupDataList.get(groupPosition).getGroupWeekChooseList().put(position,1);
                            }
                            addGVAdapter(gv,groupPosition,childPosition);
                        }
                    }
                }
            }
        });

    }
    //周几list显示和选中显示 adapter
    private void addGVAdapter(FieldMyGridView gv,final int groupPosition, final int childPosition) {
        ArrayList<HashMap<Object,Object>> weekDataList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            HashMap<Object,Object> map = new HashMap<>();
            if (mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList() != null &&
                    mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) != null &&
                    mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) == 1) {
                map.put("weekChoosed",1);
            } else {
                map.put("weekChoosed",0);
            }
            if (mActivity.mWeekLeaseTermNameMap.get(i) != null) {
                map.put("weekStr", mActivity.mWeekLeaseTermNameMap.get(i));
            }
            weekDataList.add(map);
        }
        Field_AddField_FourDenominatedUnitAdapter weekAdapter = new Field_AddField_FourDenominatedUnitAdapter(mContext,mActivity,weekDataList,2);
        gv.setAdapter(weekAdapter);
    }
    //周几是否有选中
    private boolean checkChildWeekChoose(final int groupPosition, final int childPosition) {
        boolean checked = false;
        for (int i = 0; i < 7; i++) {
            if (mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) != null &&
                    mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) == 1) {
                checked = true;
                break;
            }
        }
        return checked;
    }
    //删除选中的天（周几）
    private void removeGroupWeek(final int groupPosition, final int childPosition) {
        for (int i = 0; i < 7; i++) {
            if (mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) != null &&
                    mChildDataList.get(groupPosition).get(childPosition).getChildWeekChooseList().get(i) == 1) {
                    mGroupDataList.get(groupPosition).getGroupWeekChooseList().put(i,0);
                    if (mAdapterType == 1) {
                        mActivity.mCommonGroupDataList.get(groupPosition).getGroupWeekChooseList().put(i,0);
                    } else if (mAdapterType == 2) {
                        mActivity.mSpecialGroupDataList.get(groupPosition).getGroupWeekChooseList().put(i,0);
                    }
            }
        }

    }
    private void onTouchListener(final EditText edit) {
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    edit.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
    }
}