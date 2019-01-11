package com.linhuiba.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.GroupBookingInfoActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Fieldinfo_ReviewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ReviewModel> mFeildList;
    private LayoutInflater mInflater = null;
    private FieldInfoActivity mactivity;
    private AdvertisingInfoActivity advertisingInfoActivity;
    private GroupBookingInfoActivity mGroupBookingInfoActivity;
    private DisplayMetrics metrictmp = new DisplayMetrics();
    public Fieldinfo_ReviewAdapter(Context context ,ArrayList<ReviewModel> list,FieldInfoActivity activity) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mFeildList =list;
        this.mactivity = activity;
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metrictmp);

    }
    public Fieldinfo_ReviewAdapter(Context context ,ArrayList<ReviewModel> list,AdvertisingInfoActivity advertisingInfoActivity) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mFeildList =list;
        this.advertisingInfoActivity = advertisingInfoActivity;
        advertisingInfoActivity.getWindowManager().getDefaultDisplay().getMetrics(metrictmp);
    }
    public Fieldinfo_ReviewAdapter(Context context ,ArrayList<ReviewModel> list,GroupBookingInfoActivity groupBookingInfoActivity) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mFeildList =list;
        this.mGroupBookingInfoActivity = groupBookingInfoActivity;
        mGroupBookingInfoActivity.getWindowManager().getDefaultDisplay().getMetrics(metrictmp);
    }

    @Override
    public int getCount() {
        if(mFeildList != null){
            return mFeildList.size();
        } else{
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null) {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.activity_fieldevaluation_item, null);
            holder.mfield_user = (TextView)convertView.findViewById(R.id.field_user_txt);
            holder.mfieldtime = (TextView)convertView.findViewById(R.id.txt_time);
            holder.mtxt_number_of_people = (TextView)convertView.findViewById(R.id.txt_number_of_people);
            holder.mNumberOfPeopleLL = (LinearLayout)convertView.findViewById(R.id.txt_number_of_people_ll);
            holder.mevaluation_txt = (TextView)convertView.findViewById(R.id.evaluation_txt);
            holder.mreview_imggridview = (MyGridview)convertView.findViewById(R.id.review_imggridview);
            holder.mreview_label_relativelayout = (RelativeLayout)convertView.findViewById(R.id.review_label_relativelayout);
            holder.mfield_review_layout = (LinearLayout)convertView.findViewById(R.id.field_review_layout);
            holder.mfield_review_alllayout = (LinearLayout)convertView.findViewById(R.id.field_review_alllayout);
            holder.mSizeTV = (TextView) convertView.findViewById(R.id.review_res_size_tv);
            holder.mIndustryTV = (TextView) convertView.findViewById(R.id.review_res_industry_tv);
            holder.mPromotionPurposeTV = (TextView) convertView.findViewById(R.id.comment_item_promotion_purpose_tv);
            holder.mSpreadWayTV = (TextView) convertView.findViewById(R.id.comment_item_spread_way_tv);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        int widthview = metrictmp.widthPixels;     // 屏幕宽度（像素）
        int height = metrictmp.heightPixels;
        if (position == 2) {
            convertView.setVisibility(View.GONE);
        } else {
            if (mFeildList != null) {
                if (mFeildList.size() != 0) {
                    if (mFeildList.get(position).getField_order_item_id() != null) {
                        LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (widthview/3-56));
                        if (mFeildList.get(position).getImages() != null &&
                                mFeildList.get(position).getImages().size() > 0) {
                            Mygridviewadapter mygridviewadapter = null;
                            if (mactivity != null && advertisingInfoActivity == null && mGroupBookingInfoActivity == null) {
                                mygridviewadapter = new Mygridviewadapter(mactivity, mFeildList.get(position).getImages(),1,mactivity);
                            } else if (mactivity == null && advertisingInfoActivity != null && mGroupBookingInfoActivity == null) {
                                mygridviewadapter = new Mygridviewadapter(advertisingInfoActivity, mFeildList.get(position).getImages(),1,advertisingInfoActivity);
                            } else if (mactivity == null && advertisingInfoActivity == null && mGroupBookingInfoActivity != null) {
                                mygridviewadapter = new Mygridviewadapter(mGroupBookingInfoActivity, mFeildList.get(position).getImages(),1,mGroupBookingInfoActivity);
                            }
                            holder.mreview_imggridview.setAdapter(mygridviewadapter);
                            holder.mreview_imggridview.setLayoutParams(paramgroups);
                            holder.mreview_imggridview.setVisibility(View.VISIBLE);
                        } else {
                            holder.mreview_imggridview.setVisibility(View.GONE);
                        }
                        if (mFeildList.get(position).getTags() != null &&
                                mFeildList.get(position).getTags().size() > 0) {
                            int width_new = metrictmp.widthPixels;     // 屏幕宽度（像素）
                            float width = 0;
                            float widthtmp = 0;
                            int hightnum = 0;
                            for(int i=0;i<mFeildList.get(position).getTags().size();i++){
                                final TextView textView = new TextView(context);
                                textView.setText("  "+mFeildList.get(position).getTags().get(i).get("display_name").toString()+"  ");
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextSize(13);
                                textView.setPadding(8, 0, 8, 0);
                                textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                                textView.setTextColor(context.getResources().getColor(R.color.default_bluebg));
                                textView.setId(i);
                                textView.setTag(i);
                                width = Constants.getTextWidth(context, mFeildList.get(position).getTags().get(i).get("display_name").toString(), 16) + 16;
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,55);
                                if (width > (width_new - widthtmp - 28)) {
                                    hightnum = hightnum +1;
                                    widthtmp = 0;
                                    params.setMargins((int) widthtmp+28, hightnum*75, 0, 0);
                                    textView.setLayoutParams(params);
                                    holder.mreview_label_relativelayout.addView(textView);
                                } else {
                                    params.setMargins((int) widthtmp+28, hightnum*75, 0, 0);
                                    textView.setLayoutParams(params);
                                    holder.mreview_label_relativelayout.addView(textView);
                                }
                                widthtmp = width + widthtmp+28;
                            }
                            holder.mreview_label_relativelayout.setVisibility(View.VISIBLE);
                        } else {
                            holder.mreview_label_relativelayout.setVisibility(View.GONE);
                        }
                        holder.mfield_user.setText((String)mFeildList.get(position).getName());
                        holder.mfieldtime.setText((String)mFeildList.get(position).getReviewed_at());
                        holder.mevaluation_txt.setText((String)mFeildList.get(position).getContent());
                        if (mFeildList.get(position).getPeople_flow() != null) {
                            if (mFeildList.get(position).getPeople_flow().length() > 0 &&
                                    Integer.parseInt(mFeildList.get(position).getPeople_flow()) > 0) {
                                holder.mNumberOfPeopleLL.setVisibility(View.VISIBLE);
                                holder.mtxt_number_of_people.setVisibility(View.VISIBLE);
                                holder.mtxt_number_of_people.setText(mFeildList.get(position).getPeople_flow()+"左右");
                            } else {
                                holder.mNumberOfPeopleLL.setVisibility(View.GONE);
                            }
                        } else {
                            holder.mNumberOfPeopleLL.setVisibility(View.GONE);
                        }

                        int pic_light[] = new int[5];
                        int pic_dark[] = new int[5];
                        ImageView img[] = new ImageView[5];
                        img[0] = (ImageView)convertView.findViewById(R.id.score_img_one);
                        img[1] = (ImageView)convertView.findViewById(R.id.score_img_two);
                        img[2] = (ImageView)convertView.findViewById(R.id.score_img_three);
                        img[3] = (ImageView)convertView.findViewById(R.id.score_img_four);
                        img[4] = (ImageView)convertView.findViewById(R.id.score_img_five);
                        for (int i = 0; i < 5; i ++) {
                            pic_light[i] = R.drawable.ic_xingxing_click;
                            pic_dark[i] = R.drawable.ic_xingxing_normal;
                        }
                        for(int i = 0;i < 5; i ++) {
                            if(i < Integer.parseInt(mFeildList.get(position).getScore())) {
                                img[i].setImageResource(pic_light[i]);
                            } else {
                                img[i].setImageResource(pic_dark[i]);
                            }
                        }
                        //2018/12/14 size 和 industry
                        if (mFeildList.get(position).getSize() != null &&
                                mFeildList.get(position).getSize().length() > 0) {
                            holder.mSizeTV.setText(context.getResources().getString(R.string.order_listitem_sizetxt) +
                                    mFeildList.get(position).getSize());
                            holder.mSizeTV.setVisibility(View.VISIBLE);
                        } else {
                            holder.mSizeTV.setVisibility(View.GONE);
                        }
                        if (mFeildList.get(position).getIndustry() != null &&
                                mFeildList.get(position).getIndustry().length() > 0) {
                            holder.mIndustryTV.setText(context.getResources().getString(R.string.module_reviewed_industry) +
                                    mFeildList.get(position).getIndustry());
                            holder.mIndustryTV.setVisibility(View.VISIBLE);
                        } else {
                            holder.mIndustryTV.setVisibility(View.GONE);
                        }
                        // FIXME: 2019/1/10 推广目的推广形式

                    } else {
                        convertView.setVisibility(View.GONE);
                    }
                }
            }
        }

        return convertView;
    }
    static class ViewHolder
    {
        public TextView mfield_user;
        public TextView mfieldtime;
        public TextView mevaluation_txt;
        public MyGridview mreview_imggridview;
        public LinearLayout mNumberOfPeopleLL;
        public TextView mtxt_number_of_people;
        public RelativeLayout mreview_label_relativelayout;
        public LinearLayout mfield_review_layout;
        public LinearLayout mfield_review_alllayout;
        public TextView mSizeTV;
        public TextView mIndustryTV;
        public TextView mPromotionPurposeTV;
        public TextView mSpreadWayTV;
    }
    /** 列表适配器 */
    private class Mygridviewadapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> griddata;
        private LayoutInflater mInflater = null;
        private FieldInfoActivity fieldEvaluationActivity;
        private AdvertisingInfoActivity advertisingInfoActivity;
        private GroupBookingInfoActivity groupBookingInfoActivity;
        private int type;

        public Mygridviewadapter(Context context ,ArrayList<String> list , int type,FieldInfoActivity fieldEvaluationActivity){
            this.griddata = list;
            this.mInflater = LayoutInflater.from(context);
            this.type = type;
            this.mContext = context;
            this.fieldEvaluationActivity = fieldEvaluationActivity;
        }
        public Mygridviewadapter(Context context ,ArrayList<String> list , int type,AdvertisingInfoActivity advertisingInfoActivity){
            this.griddata = list;
            this.mInflater = LayoutInflater.from(context);
            this.type = type;
            this.mContext = context;
            this.advertisingInfoActivity = advertisingInfoActivity;
        }
        public Mygridviewadapter(Context context ,ArrayList<String> list , int type,GroupBookingInfoActivity groupBookingInfoActivity){
            this.griddata = list;
            this.mInflater = LayoutInflater.from(context);
            this.type = type;
            this.mContext = context;
            this.groupBookingInfoActivity = groupBookingInfoActivity;
        }
        @Override
        public int getCount() {
            // How many items are in the data set represented by this Adapter.(在此适配器中所代表的数据集中的条目数)
            if(griddata != null){
                return griddata.size();
            }else{
                return 0;
            }

        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GridviewViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null) {
                holder = new GridviewViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.activity_fieldevaluationitemgridview, null);
                holder.gridviewimg = (ImageView)convertView.findViewById(R.id.gridview_img);
                holder.gridviewlayout = (LinearLayout)convertView.findViewById(R.id.review_gridviewlayout);

                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (GridviewViewHolder)convertView.getTag();
            }
            int width = metrictmp.widthPixels;     // 屏幕宽度（像素）
            int height = metrictmp.heightPixels;
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width / 4 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)), (width / 4 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)));
            holder.gridviewlayout.setLayoutParams(paramgroups);
            if (griddata != null) {
                if (griddata.size() > 0) {
                    Picasso.with(mContext).load(griddata.get(position).toString()+ "?imageView2/0/w/120/h/120").placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize((width / 4 -
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)), (width / 4 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15))).into(holder.gridviewimg);
                } else {
                    Picasso.with(context).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.gridviewimg);
                }
            } else {
                Picasso.with(context).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.gridviewimg);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (advertisingInfoActivity == null && fieldEvaluationActivity != null &&
                            groupBookingInfoActivity == null) {
                        fieldEvaluationActivity.showpreviewpicture(griddata,position);
                    } else if (advertisingInfoActivity != null && fieldEvaluationActivity == null &&
                            groupBookingInfoActivity == null) {
                        advertisingInfoActivity.showpreviewpicture(griddata,position);
                    } else if (advertisingInfoActivity == null && fieldEvaluationActivity == null &&
                            groupBookingInfoActivity != null) {
                        groupBookingInfoActivity.showpreviewpicture(griddata,position);
                    }

                }
            });
            return convertView;
        }
    }
    static class GridviewViewHolder {
        public ImageView gridviewimg;
        public LinearLayout gridviewlayout;
    }
}
