package com.linhuiba.linhuifield.fieldadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldOtherContactAccountActivity;
import com.linhuiba.linhuifield.fieldmodel.ReceiveAccountModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ChildAccountManagementEditorAdapter extends BaseAdapter {
    private ArrayList<ReceiveAccountModel> data = new ArrayList<>();
    private Context mcontext;
    private Activity mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private static HashMap<String, Boolean> isSelected_editorchildaccount = new HashMap<String, Boolean>();
    private static HashMap<String, Boolean> isSelected_invoiceinfoentry = new HashMap<String, Boolean>();
    private FieldAddFieldOtherContactAccountActivity chooseAccountActivity;
    public ChildAccountManagementEditorAdapter(Context context, Activity activity, ArrayList<ReceiveAccountModel> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.mactivity = activity;
        this.data = datas;
        this.type = invoicetype;
    }
    public ChildAccountManagementEditorAdapter(Context context, FieldAddFieldOtherContactAccountActivity activity, ArrayList<ReceiveAccountModel> datas, int invoicetype) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        this.chooseAccountActivity = activity;
        this.type = invoicetype;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
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
            convertView = mInflater.inflate(R.layout.field_activity_childaccountmanagementeditor_listviewitem, null);
            holder.mfieldnametextview = (CheckBox) convertView.findViewById(R.id.fieldnametextview);
            holder.mBottomView = (View) convertView.findViewById(R.id.item_bottom_view);
            holder.mFieldAccountLL = (LinearLayout) convertView.findViewById(R.id.field_addfield_account_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == 0) {
            if (data.get(position).getAccount() != null && data.get(position).getAccount().length() > 0) {
                holder.mfieldnametextview.setText(data.get(position).getAccount());
                holder.mfieldnametextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSelected_editorchildaccount.get(String.valueOf(data.get(position).getId()))) {
                            isSelected_editorchildaccount.put(String.valueOf(data.get(position).getId()),false);
                            setIsSelected_editorchildaccount(isSelected_editorchildaccount);
                            notifyDataSetChanged();
                        } else {
                            isSelected_editorchildaccount.put(String.valueOf(data.get(position).getId()),true);
                            setIsSelected_editorchildaccount(isSelected_editorchildaccount);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        } else if (type == 1) {
            if (isSelected_editorchildaccount.get(String.valueOf(data.get(position).getId()))) {
                holder.mfieldnametextview.setChecked(true);
            } else {
                holder.mfieldnametextview.setChecked(false);
            }
            holder.mfieldnametextview.setButtonDrawable(mcontext.getResources().getDrawable(R.drawable.activity_invoiceinfo_content_checkbox_bg));
            holder.mfieldnametextview.setTextColor(mcontext.getResources().getColor(R.color.headline_tv_color));
            holder.mfieldnametextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelected_editorchildaccount.get(String.valueOf(data.get(position).getId()))) {
                        isSelected_editorchildaccount.put(String.valueOf(data.get(position).getId()),true);
                        notifyDataSetChanged();
                    } else {
                        chooseAccountActivity.setClick(position);
                    }
                }
            });
            if (data.get(position).getAccount() != null && data.get(position).getAccount().length() > 0) {
                String pay_type = "";
                String account = data.get(position).getAccount();
                String account_owner = "";
                if (data.get(position).getAccount_owner() != null && data.get(position).getAccount_owner().length() > 0) {
                    account_owner = data.get(position).getAccount_owner();
                }
                if (data.get(position).getPay_type() != null &&
                        data.get(position).getPay_type().length() > 0) {
                    if (Integer.parseInt(data.get(position).getPay_type()) == 2) {
                        pay_type = mcontext.getResources().getString(R.string.addfield_other_account_bank_tv_str);
                    } else if (Integer.parseInt(data.get(position).getPay_type()) == 3) {
                        pay_type = mcontext.getResources().getString(R.string.addfield_other_account_weixin_tv_str);
                    } else if (Integer.parseInt(data.get(position).getPay_type()) == 4) {
                        pay_type = mcontext.getResources().getString(R.string.addfield_other_account_alipay_tv_str);
                    } else if (Integer.parseInt(data.get(position).getPay_type()) == 1) {
                        pay_type = mcontext.getResources().getString(R.string.addfield_other_account_money_tv_str);
                    }
                }
                if (account != null && pay_type.length() > 0 && account.length() > 0 &&
                        account_owner.length() > 0) {
                    holder.mFieldAccountLL.setVisibility(View.VISIBLE);
                    holder.mfieldnametextview.setEnabled(true);
                    holder.mfieldnametextview.setText(pay_type + "  " + account + "  " +
                            "（" + account_owner + "）");
                } else {
                    holder.mfieldnametextview.setEnabled(false);
                    holder.mfieldnametextview.setText("");
                    holder.mFieldAccountLL.setVisibility(View.GONE);
                }

            } else {
                holder.mfieldnametextview.setText("");
                holder.mfieldnametextview.setEnabled(false);
                holder.mFieldAccountLL.setVisibility(View.GONE);
            }
        }
        if (isSelected_editorchildaccount.get(String.valueOf(data.get(position).getId()))) {
            holder.mfieldnametextview.setChecked(true);
        } else {
            holder.mfieldnametextview.setChecked(false);
        }
        if (position == data.size() - 1) {
            holder.mBottomView.setVisibility(View.GONE);
        } else {
            holder.mBottomView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        public CheckBox mfieldnametextview;
        public View mBottomView;
        public LinearLayout mFieldAccountLL;
    }
    public static HashMap<String, Boolean> getIsSelected_editorchildaccount() {
        return isSelected_editorchildaccount;
    }

    public static void setIsSelected_editorchildaccount(HashMap<String, Boolean> isSelected) {
        ChildAccountManagementEditorAdapter.isSelected_editorchildaccount = isSelected;
    }
    public static void clear_isSelectedlist_editorchildaccount() {
        if (isSelected_editorchildaccount != null) {
            isSelected_editorchildaccount.clear();
        }
    }
    public static HashMap<String, Boolean> getisSelected_invoiceinfoentry() {
        return isSelected_invoiceinfoentry;
    }

    public static void setisSelected_invoiceinfoentry(HashMap<String, Boolean> isSelected) {
        ChildAccountManagementEditorAdapter.isSelected_invoiceinfoentry = isSelected;
    }
    public static void clear_isSelected_invoiceinfoentry() {
        if (isSelected_invoiceinfoentry != null) {
            isSelected_invoiceinfoentry.clear();
        }
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public void notifyDataSetChanged(boolean isUnfold,
                                     ArrayList<ReceiveAccountModel> tempData) {
        if (tempData == null || 0 == tempData.size()) {
            return;
        }
        ArrayList<ReceiveAccountModel> tempdata = new ArrayList<>();
        tempdata.addAll(tempData);
        data.clear();
        // 如果是展开的，则加入全部data，反之则只显示3条
        if (isUnfold) {
            data.addAll(tempdata);
        } else {
            for (int i = 0; i < 3; i++ ) {
                if (tempdata.size() > i) {
                    data.add(tempdata.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}