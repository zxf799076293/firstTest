package com.linhuiba.linhuifield.fieldadapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.fieldactivity.FieldAddfieldCommunityInfoActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;

import java.util.ArrayList;
import java.util.List;

public class AddfieldCommunityInfoAdapter extends BaseQuickAdapter<FieldAddfieldAttributesModel, BaseViewHolder>  {
    private Context mContext;
    private FieldAddfieldCommunityInfoActivity mActivity;
    private List<FieldAddfieldAttributesModel> mDatas;
    private int attribute_id;
    public AddfieldCommunityInfoAdapter(int layoutResId, @Nullable List<FieldAddfieldAttributesModel> data, Context mContext, FieldAddfieldCommunityInfoActivity activity, int attribute_id) {
        super(layoutResId, data);
        this.mDatas = data;
        this.mContext = mContext;
        this.mActivity = activity;
        this.attribute_id = attribute_id;
    }

    @Override
    protected void convert(BaseViewHolder helper, final FieldAddfieldAttributesModel item) {
        final CheckBox checkBox = (CheckBox) helper.getView(R.id.addfield_community_dynamic_cb);
        final EditText editText = (EditText) helper.getView(R.id.addfield_community_dynamic_et);
        TextView textView = (TextView) helper.getView(R.id.addfield_community_dynamic_tv);

        if (mActivity.attributeMultipleIsInputChoice.get(attribute_id).get(item.getId())) {
            checkBox.setChecked(true);
            editText.setVisibility(View.VISIBLE);
        } else {
            checkBox.setChecked(false);
            editText.setVisibility(View.GONE);
        }
        if (mActivity.attributeMultipleIsInputEditMap.get(attribute_id).get(item.getId()) != null) {
            editText.setText(mActivity.attributeMultipleIsInputEditMap.get(attribute_id).get(item.getId()));
        }
        helper.setText(R.id.addfield_community_dynamic_tv, item.getName());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    mActivity.attributeMultipleIsInputChoice.get(attribute_id).put(item.getId(),false);
                    editText.setVisibility(View.GONE);
                } else {
                    mActivity.attributeMultipleIsInputChoice.get(attribute_id).put(item.getId(),true);
                    editText.setVisibility(View.VISIBLE);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mActivity.attributeMultipleIsInputEditMap.get(attribute_id).put(item.getId(),editText.getText().toString());
            }
        });
    }
}
