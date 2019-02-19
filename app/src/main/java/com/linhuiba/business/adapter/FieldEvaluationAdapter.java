package com.linhuiba.business.adapter;

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
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.view.MyGridview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
            holder.mtxt_number_of_people = (TextView)convertView.findViewById(R.id.comment_number_of_people);
            holder.mevaluation_txt = (TextView)convertView.findViewById(R.id.evaluation_txt);
            holder.mreview_imggridview = (MyGridview)convertView.findViewById(R.id.review_imggridview);
            holder.mreview_label_relativelayout = (RelativeLayout)convertView.findViewById(R.id.review_label_relativelayout);
            holder.mfield_review_grade_layout = (LinearLayout)convertView.findViewById(R.id.field_review_grade_layout);
            holder.mSizeTV = (TextView) convertView.findViewById(R.id.review_res_size_tv);
            holder.mCompositeTV = (TextView) convertView.findViewById(R.id.comment_composite_score_tv);
            holder.mPropertymatchingTV = (TextView) convertView.findViewById(R.id.comment_item_propertymatching_tv);
            holder.mGoalcompletionTV = (TextView) convertView.findViewById(R.id.comment_item_goalcompletion_tv);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (mFeildList != null) {
            if (mFeildList.size() != 0) {
                if (position == 0 && mFeildList.get(position).getDetailScore() != null) {
                    if (mFeildList.get(position).getDetailScore().getComposite_score() != null) {
                        holder.mCompositeTV.setText(Constants.getdoublepricestring(mFeildList.get(position).getDetailScore().getComposite_score(),1));
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_propertymatching() != null) {
                        holder.mPropertymatchingTV.setText(Constants.getdoublepricestring(mFeildList.get(position).getDetailScore().getScore_of_propertymatching(),1));
                    }
                    if (mFeildList.get(position).getDetailScore().getScore_of_goalcompletion() != null) {
                        holder.mGoalcompletionTV.setText(Constants.getdoublepricestring(mFeildList.get(position).getDetailScore().getScore_of_goalcompletion(),1));
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
                holder.mfield_user.setText(mFeildList.get(position).getName());
                holder.mfieldtime.setText(mFeildList.get(position).getReviewed_at());
                if (mFeildList.get(position).getContent() != null &&
                        mFeildList.get(position).getContent().length() > 0) {
                    holder.mevaluation_txt.setVisibility(View.VISIBLE);
                    holder.mevaluation_txt.setText(mFeildList.get(position).getContent());
                } else {
                    holder.mevaluation_txt.setVisibility(View.GONE);
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
                //2018/12/14 size 和 industry
                String sizeStr = "";
                if (mFeildList.get(position).getSize() != null &&
                        mFeildList.get(position).getSize().length() > 0) {
                    sizeStr = (context.getResources().getString(R.string.module_comment_centre_item_size) +
                            mFeildList.get(position).getSize());
                }
                if (mFeildList.get(position).getIndustry() != null &&
                        mFeildList.get(position).getIndustry().length() > 0) {
                    if (sizeStr.length() > 0) {
                        sizeStr = sizeStr + "    ";
                    }
                    sizeStr = sizeStr + (context.getResources().getString(R.string.module_reviewed_industry) +
                            mFeildList.get(position).getIndustry());
                }
                if (sizeStr.length() > 0) {
                    holder.mSizeTV.setVisibility(View.VISIBLE);
                    holder.mSizeTV.setText(sizeStr);
                } else {
                    holder.mSizeTV.setVisibility(View.GONE);
                }
                //2019/1/10 推广目的推广形式
                String promotion_purposes = "";
                if (mFeildList.get(position).getPromotion_purposes() != null &&
                        mFeildList.get(position).getPromotion_purposes().size() > 0) {
                    for (int i = 0; i < mFeildList.get(position).getPromotion_purposes().size(); i++) {
                        if (mFeildList.get(position).getPromotion_purposes().get(i).getDisplay_name() != null &&
                                mFeildList.get(position).getPromotion_purposes().get(i).getDisplay_name().length() > 0) {
                            if (promotion_purposes.length() > 0) {
                                promotion_purposes = promotion_purposes + "、";
                            }
                            promotion_purposes = promotion_purposes + mFeildList.get(position).getPromotion_purposes().get(i).getDisplay_name();
                        }

                    }
                }
                if (promotion_purposes.length() > 0) {
                    promotion_purposes = (context.getResources().getString(R.string.module_comment_promotion_purpose) +
                            promotion_purposes);
                }
                String spread_ways = "";
                if (mFeildList.get(position).getSpread_ways() != null &&
                        mFeildList.get(position).getSpread_ways().size() > 0) {
                    for (int i = 0; i < mFeildList.get(position).getSpread_ways().size(); i++) {
                        if (mFeildList.get(position).getSpread_ways().get(i).getSpread_way() != null &&
                                mFeildList.get(position).getSpread_ways().get(i).getSpread_way().length() > 0) {
                            if (spread_ways.length() > 0) {
                                spread_ways = spread_ways + "、";
                            }
                            spread_ways = spread_ways + mFeildList.get(position).getSpread_ways().get(i).getSpread_way();
                        }

                    }
                }
                if (spread_ways.length() > 0) {
                    spread_ways  = (context.getResources().getString(R.string.module_comment_spread_way) +
                            spread_ways);
                }
                String people_flow = "";
                if (mFeildList.get(position).getPeople_flow() != null) {
                    if (mFeildList.get(position).getPeople_flow().length() > 0 &&
                            Integer.parseInt(mFeildList.get(position).getPeople_flow()) > 0) {
                        if (mFeildList.get(position).getType() == 2) {
                            people_flow = context.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                    (mFeildList.get(position).getPeople_flow());
                        } else {
                            if (mFeildList.get(position).getScore_of_visitorsflowrate() != null &&
                                    mFeildList.get(position).getScore_of_visitorsflowrate() >= 4) {
                                people_flow = context.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                        (context.getResources().getString(R.string.module_publish_review_number_of_people_greater_than) +
                                                mFeildList.get(position).getPeople_flow());
                            } else if (mFeildList.get(position).getScore_of_visitorsflowrate() != null &&
                                    mFeildList.get(position).getScore_of_visitorsflowrate() > 1) {
                                people_flow = context.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                        (context.getResources().getString(R.string.module_publish_review_number_of_people_equality) +
                                                mFeildList.get(position).getPeople_flow());
                            } else if (mFeildList.get(position).getScore_of_visitorsflowrate() != null &&
                                    mFeildList.get(position).getScore_of_visitorsflowrate() > 0) {
                                people_flow = context.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                        (context.getResources().getString(R.string.module_publish_review_number_of_people_less_than) +
                                                mFeildList.get(position).getPeople_flow());
                            } else {
                                people_flow = context.getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                        (mFeildList.get(position).getPeople_flow() +
                                                context.getResources().getString(R.string.numberofpeople_righttxt));
                            }
                        }
                    }
                }
                String str = promotion_purposes;
                if (spread_ways.length() > 0)  {
                    if (str.length() > 0) {
                        str = str + "    ";
                    }
                    str = str + spread_ways;
                }
                if (people_flow.length() > 0)  {
                    if (str.length() > 0) {
                        str = str + "    ";
                    }
                    str = str + people_flow;
                }
                if (str.length() > 0) {
                    holder.mtxt_number_of_people.setVisibility(View.VISIBLE);
                    holder.mtxt_number_of_people.setText(str);
                } else {
                    holder.mtxt_number_of_people.setVisibility(View.GONE);
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
        public TextView mevaluation_txt;
        public MyGridview mreview_imggridview;
        public RelativeLayout mreview_label_relativelayout;
        public LinearLayout mfield_review_grade_layout;
        public TextView mSizeTV;
        public TextView mCompositeTV;
        public TextView mPropertymatchingTV;
        public TextView mGoalcompletionTV;

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
