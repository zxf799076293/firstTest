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
import com.linhuiba.business.activity.FieldEvaluationActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.EvaluationModel_new;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.linhuipublic.config.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.InjectView;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FieldEvaluationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ReviewModel> mFeildList;
    private LayoutInflater mInflater = null;
    private FieldEvaluationActivity mactivity;

    public FieldEvaluationAdapter(Context context ,ArrayList<ReviewModel> list,FieldEvaluationActivity activity) {
        this.mInflater = LayoutInflater.from(context);
        this.mFeildList =list;
        this.mactivity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(mFeildList != null){
            return mFeildList.size();
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
            holder.mfield_review_grade_layout = (LinearLayout)convertView.findViewById(R.id.field_review_grade_layout);
            holder.mSizeTV = (TextView) convertView.findViewById(R.id.review_res_size_tv);
            holder.mIndustryTV = (TextView) convertView.findViewById(R.id.review_res_industry_tv);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (mFeildList != null) {
            if (mFeildList.size() != 0) {
                if (position == 0) {
                    ImageView[] synthesize_imglist = new ImageView[5];
                    ImageView[] number_of_people_imglist = new ImageView[5];
                    ImageView[] property_cooperate_imglist = new ImageView[5];
                    ImageView[] user_participation_imglist = new ImageView[5];
                    ImageView[] complete_target_imglist = new ImageView[5];
                    for (int i = 0; i < 5; i++) {
                        int id = context.getResources().getIdentifier("review_img_" + i, "id", context.getPackageName());
                        ImageView imageview = (ImageView)convertView.findViewById(id);
                        synthesize_imglist[i]= (imageview);
                        int number_of_people_id = context.getResources().getIdentifier("review_number_of_people_img_" + i, "id", context.getPackageName());
                        ImageView number_of_people_imageview = (ImageView) convertView.findViewById(number_of_people_id);
                        number_of_people_imglist[i]= (number_of_people_imageview);
                        int property_cooperate_id = context.getResources().getIdentifier("review_property_cooperate_img_" + i, "id", context.getPackageName());
                        ImageView property_cooperate_imageview = (ImageView) convertView.findViewById(property_cooperate_id);
                        property_cooperate_imglist[i]= (property_cooperate_imageview);
                        int user_participation_id = context.getResources().getIdentifier("review_user_participation_img_" + i, "id", context.getPackageName());
                        ImageView user_participation_imageview = (ImageView) convertView.findViewById(user_participation_id);
                        user_participation_imglist[i]= (user_participation_imageview);
                        int complete_target_id = context.getResources().getIdentifier("review_complete_target_img_" + i, "id", context.getPackageName());
                        ImageView complete_target_imageview = (ImageView) convertView.findViewById(complete_target_id);
                        complete_target_imglist[i]= (complete_target_imageview);
                    }
                    final int pic_light[] = new int[5];
                    final int pic_dark[] = new int[5];
                    for (int i = 0; i < 5; i ++) {
                        pic_light[i] = R.drawable.ic_xingxing_click;
                        pic_dark[i] = R.drawable.ic_xingxing_normal;
                    }
                    if (mFeildList.get(position).getDetailScore().getComposite_score() != null) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < mFeildList.get(position).getDetailScore().getComposite_score()) {
                                synthesize_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_light[j]));
                            } else {
                                synthesize_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_dark[j]));
                            }
                        }
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_visitorsflowrate() != null) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < mFeildList.get(position).getDetailScore().getScore_of_visitorsflowrate()) {
                                number_of_people_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_light[j]));
                            } else {
                                number_of_people_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_dark[j]));
                            }
                        }
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_propertymatching() != null) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < mFeildList.get(position).getDetailScore().getScore_of_propertymatching()) {
                                property_cooperate_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_light[j]));
                            } else {
                                property_cooperate_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_dark[j]));
                            }
                        }
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_userparticipation() != null) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < mFeildList.get(position).getDetailScore().getScore_of_userparticipation()) {
                                user_participation_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_light[j]));
                            } else {
                                user_participation_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_dark[j]));
                            }
                        }
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_goalcompletion() != null) {
                        for(int j = 0;j < 5; j ++) {
                            if(j < mFeildList.get(position).getDetailScore().getScore_of_goalcompletion()) {
                                complete_target_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_light[j]));
                            } else {
                                complete_target_imglist[j].setBackgroundDrawable(context.getResources().getDrawable(pic_dark[j]));
                            }
                        }
                    }
                    holder.mfield_review_grade_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.mfield_review_grade_layout.setVisibility(View.GONE);
                }
                if (mFeildList.get(position).getImages() != null) {
                    if (mFeildList.get(position).getImages().size() > 0) {
                        Mygridviewadapter mygridviewadapter = new Mygridviewadapter(mactivity, mFeildList.get(position).getImages(),1,mactivity);
                        holder.mreview_imggridview.setAdapter(mygridviewadapter);
                        holder.mreview_imggridview.setVisibility(View.VISIBLE);
                    } else {
                        holder.mreview_imggridview.setVisibility(View.GONE);
                    }
                } else {
                    holder.mreview_imggridview.setVisibility(View.GONE);
                }

                if (mFeildList.get(position).getTags() != null) {
                    if (mFeildList.get(position).getTags().size() > 0) {
                        holder.mreview_label_relativelayout.setVisibility(View.VISIBLE);
                        DisplayMetrics metric = new DisplayMetrics();
                        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
                        int width_new = metric.widthPixels;     // 屏幕宽度（像素）
                        float width = 0;
                        float widthtmp = 0;
                        int hightnum = 0;
                        for(int i=0;i<mFeildList.get(position).getTags().size();i++){
                            final TextView textView = new TextView(mactivity);
                            textView.setText("  "+mFeildList.get(position).getTags().get(i).get("display_name").toString()+"  ");
                            textView.setGravity(Gravity.CENTER);
                            textView.setTextSize(13);
                            textView.setPadding(8, 0, 8, 0);
                            textView.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.choosespecifications_pricetxtselect));
                            textView.setTextColor(mactivity.getResources().getColor(R.color.default_bluebg));
                            textView.setId(i);
                            textView.setTag(i);
                            width = Constants.getTextWidth(mactivity, mFeildList.get(position).getTags().get(i).get("display_name").toString(), 16) + 16;
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

                    } else {
                        holder.mreview_label_relativelayout.setVisibility(View.GONE);
                    }
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
                for(int i = 0;i < 5; i ++)
                {
                    if(i < Integer.parseInt(mFeildList.get(position).getScore())) {
                        img[i].setImageResource(pic_light[i]);
                    } else {
                        img[i].setImageResource(pic_dark[i]);
                    }
                }
                // FIXME: 2018/12/14 size 和 industry
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
            }
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView mfield_user;
        public TextView mfieldtime;
        public TextView mtxt_number_of_people;
        public LinearLayout mNumberOfPeopleLL;
        public TextView mevaluation_txt;
        public MyGridview mreview_imggridview;
        public RelativeLayout mreview_label_relativelayout;
        public LinearLayout mfield_review_grade_layout;
        public TextView mSizeTV;
        public TextView mIndustryTV;
    }
    /** 列表适配器 */
    private class Mygridviewadapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> griddata;
        private LayoutInflater mInflater = null;
        private FieldEvaluationActivity fieldEvaluationActivity;
        private int type;

        public Mygridviewadapter(Context context ,ArrayList<String> list , int type,FieldEvaluationActivity fieldEvaluationActivity){
            this.griddata = list;
            this.mInflater = LayoutInflater.from(context);
            this.type = type;
            this.mContext = context;
            this.fieldEvaluationActivity = fieldEvaluationActivity;
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
            DisplayMetrics metric = new DisplayMetrics();
            mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width/4 -
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)), (width/4 -
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)));
            holder.gridviewlayout.setLayoutParams(paramgroups);
            if (griddata != null) {
                if (griddata.size() > 0) {
                    Picasso.with(mContext).load(griddata.get(position).toString()+ "?imageView2/0/w/120/h/120").placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize((width / 4 -
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15)), (width / 4 -
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(context,15))).into(holder.gridviewimg);
                } else {
                    Picasso.with(mContext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.gridviewimg);
                }
            } else {
                Picasso.with(mContext).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(holder.gridviewimg);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fieldEvaluationActivity.showpreviewpicture(griddata,position);
                }
            });
            return convertView;
        }

    }
    static class GridviewViewHolder
    {
        public ImageView gridviewimg;
        public LinearLayout gridviewlayout;

    }
}
