package com.linhuiba.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.InvoiceTitleEditActivity;
import com.linhuiba.business.activity.InvoiceTitleListActivity;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class InvoiceTitleAdapter extends BaseQuickAdapter<InvoiceInfomationModel, BaseViewHolder> {
    private Context mContext;
    private InvoiceTitleListActivity mInvoiceTitleListActivity;
    private List<InvoiceInfomationModel> mDatas;
    public InvoiceTitleAdapter(int layoutResId, @Nullable List<InvoiceInfomationModel> data, Context context,
                               InvoiceTitleListActivity activity) {
        super(layoutResId, data);
        this.mContext = context;
        this.mInvoiceTitleListActivity = activity;
        this.mDatas = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final InvoiceInfomationModel item) {
        helper.setText(R.id.invoice_title_item_title_tv,item.getTitle());
        if (item.getInvoice_type() == 1) {
            helper.getView(R.id.invoice_title_item_tax_num_ll).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.invoice_title_item_tax_num_ll).setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.invoice_title_item_tax_num_tv,item.getTax_number());
        ImageView imageView = (ImageView) helper.getView(R.id.invoice_title_item_imgv);
        if (item.getInvoice_type() == 1) {
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_shou_three_six));
        } else if (item.getInvoice_type() == 2) {
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_pu_three_six));
        } else if (item.getInvoice_type() == 3) {
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_zhuan_three_six));
        }
        helper.getView(R.id.invoice_title_edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent titleEditIntent = new Intent(mInvoiceTitleListActivity,InvoiceTitleEditActivity.class);
                titleEditIntent.putExtra("show_type",mInvoiceTitleListActivity.EDIT_TITLE_INT);
                titleEditIntent.putExtra("title_model",(Serializable) item);
                mInvoiceTitleListActivity.startActivityForResult(titleEditIntent,mInvoiceTitleListActivity.EDIT_TITLE_INT);
            }
        });
        helper.getView(R.id.invoice_title_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInvoiceTitleListActivity.showMsgDialog(0,item.getId());
            }
        });
        final CheckBox checkBox = (CheckBox) helper.getView(R.id.invoice_title_set_default_cb);
        if (item.getIs_default() == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_default() == 0) {
                    mInvoiceTitleListActivity.mInvoiceTitleMvpPresenter.setInvoiceTitleDefault(item.getId());
                } else {
                    checkBox.setChecked(true);
                }
            }
        });

    }
}
