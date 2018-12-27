package com.linhuiba.linhuifield.fieldactivity;

import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linhuiba.linhuifield.R;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuifield.fieldadapter.AddfieldViewPageAdapter;
import com.linhuiba.linhuifield.fieldbasemvp.FieldBaseMvpActivity;
import com.linhuiba.linhuipublic.config.BaseMessageUtils;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FieldAddFieldGuideActivity extends FieldBaseMvpActivity {
    private TextView mAddfieldSearchResJumpTV;
    private ViewPager mAddfieldSearchResVP;
    private LinearLayout mAddfieldSearchResPointLL;
    private int[] imageView = { R.drawable.image_one_three_six, R.drawable.image_two_three_six,
            R.drawable.image_three_three_six};
    private List<View> mPictureList;
    private RelativeLayout mGuideCarouselRL;
    private RelativeLayout mGuideImgvClickRL;
    private ImageView mGuideClickImgv;
    private int showType; //1:发布场地，2:地图,3:首页，4：列表，5：展位详情
    private int[] mapImg = { R.drawable.ic_onboarding_three_six_one, R.drawable.ic_onboarding_three_six_two,
            R.drawable.ic_onboarding_three_six_three};
    private int[] homeImg = { R.drawable.ic_shouye_three_six_one, R.drawable.ic_shouyeone_three_six_one};
    private int[] communitylistImg = { R.drawable.ic_list_three_six_one};
    private int[] phyinfoImg = { R.drawable.ic_xiangqingye_three_six_one};
    private int mImgClickPage;
    private boolean isClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_addfield_search_resource);
        initView();
    }
    private void initView() {
        if (Constants.hasNavBar(this)) {
            hideBottomUIMenu();
        }
        mGuideCarouselRL = (RelativeLayout) findViewById(R.id.guide_carousel_rl);
        mGuideImgvClickRL = (RelativeLayout) findViewById(R.id.guide_imgv_click_rl);
        mGuideClickImgv = (ImageView) findViewById(R.id.guidu_click_imgv);
        mAddfieldSearchResJumpTV = (TextView) findViewById(R.id.addfield_search_res_jump_tv);
        mAddfieldSearchResVP = (ViewPager) findViewById(R.id.addfield_search_resource_vp);
        mAddfieldSearchResPointLL = (LinearLayout) findViewById(R.id.addfield_search_res_start_page_point_ll);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().get("show_type") != null) {
                showType = intent.getExtras().getInt("show_type");
            }
        }
        if (showType == 1) {
            mGuideImgvClickRL.setVisibility(View.GONE);
            mGuideCarouselRL.setVisibility(View.VISIBLE);
            initoper();
            addView();
            addPoint();
        } else if (showType == 2) {
            mGuideImgvClickRL.setVisibility(View.VISIBLE);
            mGuideCarouselRL.setVisibility(View.GONE);
            mGuideClickImgv.setImageResource(mapImg[mImgClickPage]);
            mGuideClickImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClicked) {
                        isClicked = true;
                        mImgClickPage ++;
                        if (mImgClickPage == mapImg.length) {
                            LoginManager.getInstance().setMap_show_guide(1);
                            finish();
                        } else {
                            if (mImgClickPage < mapImg.length) {
                                mGuideClickImgv.setImageResource(mapImg[mImgClickPage]);
                            }
                            isClicked = false;
                        }
                    }
                }
            });
        } else if (showType == 3) {
            mGuideImgvClickRL.setVisibility(View.VISIBLE);
            mGuideCarouselRL.setVisibility(View.GONE);
            mGuideClickImgv.setImageResource(homeImg[mImgClickPage]);
            mGuideClickImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClicked) {
                        isClicked = true;
                        mImgClickPage ++;
                        if (mImgClickPage == homeImg.length) {
                            LoginManager.getInstance().setHome_show_guide(1);
                            finish();
                        } else {
                            if (mImgClickPage < homeImg.length) {
                                mGuideClickImgv.setImageResource(homeImg[mImgClickPage]);
                            }
                            isClicked = false;
                        }
                    }
                }
            });
        } else if (showType == 4) {
            mGuideImgvClickRL.setVisibility(View.VISIBLE);
            mGuideCarouselRL.setVisibility(View.GONE);
            mGuideClickImgv.setImageResource(communitylistImg[mImgClickPage]);
            mGuideClickImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClicked) {
                        isClicked = true;
                        mImgClickPage ++;
                        if (mImgClickPage == communitylistImg.length) {
                            LoginManager.getInstance().setCommunity_list_show_guide(1);
                            finish();
                        } else {
                            if (mImgClickPage < communitylistImg.length) {
                                mGuideClickImgv.setImageResource(communitylistImg[mImgClickPage]);
                            }
                            isClicked = false;
                        }
                    }

                }
            });
        } else if (showType == 5) {
            mGuideImgvClickRL.setVisibility(View.VISIBLE);
            mGuideCarouselRL.setVisibility(View.GONE);
            mGuideClickImgv.setImageResource(phyinfoImg[mImgClickPage]);
            mGuideClickImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImgClickPage ++;
                    if (mImgClickPage == phyinfoImg.length) {
                        LoginManager.getInstance().setPhyinfo_show_guide(1);
                        finish();
                    } else {
                        if (mImgClickPage < phyinfoImg.length) {
                            mGuideClickImgv.setImageResource(phyinfoImg[mImgClickPage]);
                        }
                    }
                }
            });
        }
    }
    private void initoper() {
        // 进入按钮
        mAddfieldSearchResJumpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseTypeIntent = new Intent(FieldAddFieldGuideActivity.this,FieldAddFieldChooseResOptionsActivity.class);
                startActivity(chooseTypeIntent);
                LoginManager.getInstance().setAddfield_show_guide(1);
                finish();
            }
        });

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        mAddfieldSearchResVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }
    /**
     * 添加图片到view
     */
    private void addView() {
        mPictureList = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER);
            //小米审核没通过时的修改
//            iv.setImageBitmap(readBitMap(this,imageView[i]));
            iv.setImageResource(imageView[i]);
//            String a = "https://banner.linhuiba.com/FiGCHi3ubPC4VXe-Cuka_mZl7Ry6";
//            Picasso.with(this).load(a).resize(450, 200).into(iv);
            mPictureList.add(iv);
        }
        // 加入适配器
        mAddfieldSearchResVP.setAdapter(new AddfieldViewPageAdapter(mPictureList));

    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imageView.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    Constants.Dp2Px(this,7), Constants.Dp2Px(this,7));
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(Constants.Dp2Px(this,24), 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.activity_splash_screen_unselected_circle_bg);
            mAddfieldSearchResPointLL.addView(iv);
        }
        mAddfieldSearchResPointLL.getChildAt(0).setBackgroundResource(R.drawable.activity_splash_screen_selected_circle_bg);
    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        for (int i = 0; i < imageView.length; i++) {
            if (i == position) {
                mAddfieldSearchResPointLL.getChildAt(position).setBackgroundResource(
                        R.drawable.activity_splash_screen_selected_circle_bg);
            } else {
                mAddfieldSearchResPointLL.getChildAt(i).setBackgroundResource(
                        R.drawable.activity_splash_screen_unselected_circle_bg);
            }
        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imageView.length - 1) {
            mAddfieldSearchResJumpTV.setVisibility(View.VISIBLE);
        } else {
            mAddfieldSearchResJumpTV.setVisibility(View.GONE);
        }
    }
}
