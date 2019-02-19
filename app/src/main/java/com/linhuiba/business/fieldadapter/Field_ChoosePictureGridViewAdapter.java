package com.linhuiba.business.fieldadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.ApplyforReleaseActivity;
import com.linhuiba.business.activity.EnterpriseCertificationActivity;
import com.linhuiba.business.activity.InvoiceInformationActivity;
import com.linhuiba.business.activity.InvoiceTitleEditActivity;
import com.linhuiba.business.activity.OrderConfirmUploadVoucherActivity;
import com.linhuiba.business.activity.MyselfInfo_CompanyActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.activity.TrackRemarksActivity;
import com.linhuiba.business.network.Request;
import com.linhuiba.linhuifield.connector.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Field_ChoosePictureGridViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Bitmap> result_tmp;
    private ArrayList<String> result;
    private TrackRemarksActivity remarksActivity;
    private PublishReviewActivity publishReviewActivity;
    private MyselfInfo_CompanyActivity myselfInfo_companyActivity;
    private OrderConfirmUploadVoucherActivity orderConfirm_uploadVoucherActivity;
    private EnterpriseCertificationActivity enterpriseCertificationActivity;
    private ApplyforReleaseActivity applyforReleaseActivity;
    private InvoiceInformationActivity invoiceInformationActivity;
    private InvoiceTitleEditActivity mInvoiceTitleEditActivity;
    private int type;
    private int myself_company_upload_type;
    private int width;
    private int height;
    public Field_ChoosePictureGridViewAdapter(Context context, TrackRemarksActivity activity, ArrayList<String> pathlist,int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.remarksActivity = activity;
        this.result = pathlist;
        this.type = type;
    }
    public Field_ChoosePictureGridViewAdapter(Context context, PublishReviewActivity activity, ArrayList<String> pathlist,int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.publishReviewActivity = activity;
        this.result = pathlist;
        this.type = type;
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
    }
    public Field_ChoosePictureGridViewAdapter(Context context, MyselfInfo_CompanyActivity activity, ArrayList<String> pathlist,int type,int upload_type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.myselfInfo_companyActivity = activity;
        this.result = pathlist;
        this.type = type;
        this.myself_company_upload_type = upload_type;
    }
    public Field_ChoosePictureGridViewAdapter(Context context, OrderConfirmUploadVoucherActivity activity, ArrayList<String> pathlist,int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.orderConfirm_uploadVoucherActivity = activity;
        this.result = pathlist;
        this.type = type;
    }

    public Field_ChoosePictureGridViewAdapter(Context context, EnterpriseCertificationActivity activity, ArrayList<String> pathlist, int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.enterpriseCertificationActivity = activity;
        this.result = pathlist;
        this.type = type;
    }
    public Field_ChoosePictureGridViewAdapter(Context context, ApplyforReleaseActivity activity, ArrayList<String> pathlist, int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.applyforReleaseActivity = activity;
        this.result = pathlist;
        this.type = type;
    }
    public Field_ChoosePictureGridViewAdapter(Context context, InvoiceInformationActivity activity, ArrayList<String> pathlist, int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.invoiceInformationActivity = activity;
        this.result = pathlist;
        this.type = type;//7
    }
    public Field_ChoosePictureGridViewAdapter(Context context, InvoiceTitleEditActivity activity, ArrayList<String> pathlist, int type) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mInvoiceTitleEditActivity = activity;
        this.result = pathlist;
        this.type = type;//8
    }

    @Override
    public int getCount() {
        if(result != null){
            return result.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mLayoutInflater.inflate(R.layout.field_adapter_choosepicture_item, null);
            holder.gridview_img = (ImageView)convertView.findViewById(R.id.choosepicture_img);
            holder.mchoosepicture_relativelayout = (RelativeLayout)convertView.findViewById(R.id.choosepicture_relativelayout);
            holder.mChooseInvoiceProveLL = (LinearLayout)convertView.findViewById(R.id.invoice_prove_ll);
            holder.mChooseInvoiceProveImgV = (ImageView)convertView.findViewById(R.id.invoice_prove_imgv);
            holder.mChooseInvoiceProveUrlImgV = (ImageView)convertView.findViewById(R.id.invoice_prove_url_imgv);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mChooseInvoiceProveLL.setVisibility(View.GONE);
        if (type == 2) {
            RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams((width -
                    Constants.Dp2Px(context,70))/4,
                    (width -
                            Constants.Dp2Px(context,70))/4);
            holder.gridview_img.setLayoutParams(layoutParams);
        }
        if (result != null) {
            if (result.size() > 0) {
                if (result.get(position).toString().equals("firstgridviewitem")) {
                    if (type == 7 || type == 8) {
                        holder.mChooseInvoiceProveLL.setVisibility(View.VISIBLE);
                        holder.mChooseInvoiceProveImgV.setVisibility(View.VISIBLE);
                        holder.mChooseInvoiceProveUrlImgV.setVisibility(View.GONE);
                        if (type == 7) {
                            holder.mChooseInvoiceProveLL.setBackgroundColor(context.getResources().getColor(R.color.search_choose_lable_unselect_bg_color));
                            holder.mChooseInvoiceProveImgV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_three_one));
                        } else if (type == 8) {
                            holder.mChooseInvoiceProveImgV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_shangchuang_three_six));
                            holder.mChooseInvoiceProveLL.setBackgroundColor(context.getResources().getColor(R.color.white));
                        }

                    } else if (type == 2) {
                        Glide.with(context)
                                .load(R.drawable.ic_sahngchuangtupian)
                                .centerCrop()
                                .into(holder.gridview_img);
                    } else {
                        Glide.with(context)
                                .load(R.drawable.ic_add_upload_pictures)
                                .centerCrop()
                                .into(holder.gridview_img);
                    }
                } else {
                    if (type == 7 || type == 8) {
                        holder.mChooseInvoiceProveLL.setVisibility(View.VISIBLE);
                        holder.mChooseInvoiceProveImgV.setVisibility(View.GONE);
                        holder.mChooseInvoiceProveUrlImgV.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(result.get(position))
                                .centerCrop()
                                .into(holder.mChooseInvoiceProveUrlImgV);
                    } else {
                        Glide.with(context)
                                .load(result.get(position))
                                .centerCrop()
                                .into(holder.gridview_img);
                    }
                }
                if (type == 1) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            remarksActivity.showpreviewpicture(-1);
                                        } else {
                                            remarksActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 2) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            publishReviewActivity.showpreviewpicture(-1);
                                        } else {
                                            publishReviewActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 3) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            myselfInfo_companyActivity.upload_picture_type = myself_company_upload_type;
                                            myselfInfo_companyActivity.showpreviewpicture(-1);
                                        } else {
                                            myselfInfo_companyActivity.upload_picture_type = myself_company_upload_type;
                                            myselfInfo_companyActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 4) {
                    holder.mchoosepicture_relativelayout.setBackgroundColor(orderConfirm_uploadVoucherActivity.getResources().getColor(R.color.tab_gb));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            orderConfirm_uploadVoucherActivity.showpreviewpicture(-1);
                                        } else {
                                            orderConfirm_uploadVoucherActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 5) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            enterpriseCertificationActivity.showpreviewpicture(-1);
                                        } else {
                                            enterpriseCertificationActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 6) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            applyforReleaseActivity.showpreviewpicture(-1);
                                        } else {
                                            applyforReleaseActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                } else if (type == 7) {

                } else if (type == 8) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result != null) {
                                if (result.size() > 0) {
                                    if (position < result.size()) {
                                        if (result.get(position).toString().equals("firstgridviewitem")) {
                                            mInvoiceTitleEditActivity.showpreviewpicture(-1);
                                        } else {
                                            mInvoiceTitleEditActivity.showpreviewpicture(position);
                                        }
                                    }
                                }
                            }
                        }
                    });

                }

            }

        }

        return convertView;
    }
    static class ViewHolder
    {
        public ImageView gridview_img;
        public RelativeLayout mchoosepicture_relativelayout;
        public LinearLayout mChooseInvoiceProveLL;
        public ImageView mChooseInvoiceProveImgV;
        public ImageView mChooseInvoiceProveUrlImgV;
    }
}
