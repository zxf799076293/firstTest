package com.linhuiba.business.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.business.R;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.ArticleslistModel;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/25.
 */

public class HomeFragmentCooperationCaseAdapter extends BaseAdapter {
    private ArrayList<ArticleslistModel> data = new ArrayList<ArticleslistModel>();
    private Context mcontext;
    private HomeFragment mactivity;
    private LayoutInflater mInflater = null;
    private int type;
    private int width;
    private int height;
    public HomeFragmentCooperationCaseAdapter(Context context, HomeFragment activity, ArrayList<ArticleslistModel> datas, int invoicetype) {
        if (context != null && activity != null) {
            this.mcontext = context;
            this.mactivity = activity;
            this.mInflater = LayoutInflater.from(context);
            this.data = datas;
            this.type = invoicetype;
            DisplayMetrics metric = new DisplayMetrics();
            mactivity.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;     // 屏幕宽度（像素）
            height = metric.heightPixels;
        }
    }
    @Override
    public int getCount() {
        if(data != null){
            return data.size();
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_home_cooperation_case_item, null);
            holder.mimage_layout = (RelativeLayout)convertView.findViewById(R.id.home_themes_item_image_layout);
            holder.mhome_activity_img = (OvalImageView)convertView.findViewById(R.id.home_themes_item_imageview);
            holder.mhome_thenes_name = (TextView)convertView.findViewById(R.id.home_themes_item_textview);
            holder.mHomeCaseItemLL = (LinearLayout)convertView.findViewById(R.id.home_case_item_ll);
            holder.mHomeCaseNameTV = (TextView)convertView.findViewById(R.id.home_case_item_name_tv);
            holder.mHomeCaseDigestTV = (TextView)convertView.findViewById(R.id.home_case_item_digest_tv);
            holder.mHomeSiteFirstView = (View)convertView.findViewById(R.id.home_site_type_first_view);
            holder.mHomeSiteEndView = (View)convertView.findViewById(R.id.home_site_type_end_view);
            holder.mHomeCaseCutOffSiteView = (View)convertView.findViewById(R.id.home_case_cut_off_rule_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (type == 5) {
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) +
                    Constants.Dp2Px(mcontext,10),
                    (width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480);

            LinearLayout.LayoutParams param= new LinearLayout.LayoutParams((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH)+
                    Constants.Dp2Px(mcontext,10),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.mimage_layout.setLayoutParams(paramgroups);
            holder.mHomeCaseItemLL.setLayoutParams(param);
            if (data.get(position).getCover_pic_url() != null) {
                if (data.get(position).getCover_pic_url().length() != 0) {
                    Picasso.with(mcontext).load(data.get(position).getCover_pic_url().toString() + "?imageView2/0/w/" + String.valueOf(((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH))) + "/h/" +
                            String.valueOf(((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480))).resize(((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH)),
                            ((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.mhome_activity_img);
                } else {
                    Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                            ((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.mhome_activity_img);
                }
            } else {
                Picasso.with(mcontext).load(R.drawable.ic_no_pic_big).resize((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH),
                        ((width* 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480)).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).into(holder.mhome_activity_img);
            }
            if (data.get(position).getTitle().length() > 0) {
                holder.mHomeCaseNameTV.setVisibility(View.VISIBLE);
                holder.mHomeCaseNameTV.setText(data.get(position).getTitle());
            } else {
                holder.mHomeCaseNameTV.setVisibility(View.GONE);
                holder.mHomeCaseNameTV.setText("");
            }
            /* 取消显示描述
            if (data.get(position).getDigest() != null &&
                    data.get(position).getDigest().length() > 0) {
                holder.mHomeCaseDigestTV.setVisibility(View.VISIBLE);
                holder.mHomeCaseDigestTV.setText(data.get(position).getDigest());
            } else {
                holder.mHomeCaseDigestTV.setText("");
                holder.mHomeCaseDigestTV.setVisibility(View.GONE);
            }
            **/
            holder.mHomeCaseDigestTV.setVisibility(View.GONE);
            if (position == 0) {
                holder.mHomeSiteFirstView.setVisibility(View.VISIBLE);
                holder.mHomeCaseCutOffSiteView.setVisibility(View.GONE);
            } else {
                holder.mHomeSiteFirstView.setVisibility(View.GONE);
                holder.mHomeCaseCutOffSiteView.setVisibility(View.VISIBLE);
            }
            if (position == data.size() - 1) {
                holder.mHomeSiteEndView.setVisibility(View.VISIBLE);
            } else {
                holder.mHomeSiteEndView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    static class ViewHolder
    {
        public RelativeLayout mimage_layout;
        public OvalImageView mhome_activity_img;
        public TextView mhome_thenes_name;
        public LinearLayout mHomeCaseItemLL;
        public TextView mHomeCaseNameTV;
        public TextView mHomeCaseDigestTV;
        public View mHomeSiteFirstView;
        public View mHomeSiteEndView;
        public View mHomeCaseCutOffSiteView;
    }
}
