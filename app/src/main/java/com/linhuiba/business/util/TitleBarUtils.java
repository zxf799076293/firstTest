package com.linhuiba.business.util;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.linhuiba.business.R;

/**
 * Created by Administrator on 2016/1/29.
 */
public class TitleBarUtils  {
    public static void showBackButton(final Activity activity,boolean show) {
        if (activity != null && show) {
            TextView backButton = (TextView)activity.findViewById(R.id.txt_back_top);
            backButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }
        }
    }
    public static void showBackCloseImage(final Activity activity,boolean show,View.OnClickListener l) {
        if (activity != null && show) {
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.back_close_layout);
            backButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
            }
        }
    }

    public static void showBackImg(final Activity activity,boolean show) {
        if (activity != null && show) {
            ImageView backImgButton = (ImageView)activity.findViewById(R.id.back_button_top);
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.back_layout_top);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }

            if (backImgButton != null) {
                backImgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }

        }
    }
    public static void showBackImgclick(final Activity activity,boolean show,View.OnClickListener l) {
        if (activity != null && show) {
            ImageView backImgButton = (ImageView)activity.findViewById(R.id.back_button_top);
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.back_layout_top);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
                backImgButton.setOnClickListener(l);
            }
        }
    }
    public static void showBackImg(final View View,final Activity activity,boolean show) {
        if (View != null && show) {
            ImageView backImgButton = (ImageView)View.findViewById(R.id.back_button_top);
            RelativeLayout backButton = (RelativeLayout)View.findViewById(R.id.back_layout_top);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });

                backImgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }
        }
    }
    public static void showActionImg(final Activity activity,boolean show,Drawable img_src,View.OnClickListener l) {
        if (activity != null && show) {
            ImageView backImgButton = (ImageView)activity.findViewById(R.id.action_img_top);
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.action_layout_top);
            backImgButton.setBackgroundDrawable(img_src);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
                backImgButton.setOnClickListener(l);
            }
        }
    }
    public static void show_below_ActionImg(final Activity activity,boolean show,Drawable img_src,View.OnClickListener l) {
        if (activity != null && show) {
            ImageView backImgButton = (ImageView)activity.findViewById(R.id.below_action_img_top);
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.below_action_layout_top);
            backImgButton.setBackgroundDrawable(img_src);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
                backImgButton.setOnClickListener(l);
            }
        }
    }

    public static void showActionImg(final View activity,boolean show,Drawable img_src,View.OnClickListener l) {
        ImageView backImgButton = (ImageView)activity.findViewById(R.id.action_img_top);
        RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.action_layout_top);
        if (activity != null && show) {
            backImgButton.setBackgroundDrawable(img_src);
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
                backImgButton.setOnClickListener(l);
            }
        } else {
            backButton.setVisibility(View.GONE);
            backImgButton.setVisibility(View.GONE);
        }
    }
    public static void showBackButton(final Activity activity,boolean show,String string) {
        if (activity != null && show) {
            TextView backButton = (TextView)activity.findViewById(R.id.txt_back_top);
            backButton.setText(string);
            backButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }
        }
    }
    public static void showBack_ObjectiveButton (final Activity activity, String string, View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.txt_back_top);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            //ImageView actionButton = (ImageView) activity.findViewById(R.id.action_button_top);
            //actionButton.setImageResource(iconRes);
            //actionButton.setVisibility(View.VISIBLE);
            if (actionbtn != null) {
                actionbtn.setOnClickListener(l);
            }
        }
    }
    public static void setTitleText(Activity activity, String title) {
        if (activity != null) {
            TextView titletext = (TextView)activity.findViewById(R.id.title);
            titletext.setVisibility(View.VISIBLE);
            titletext.setText(title);
        }
    }
    public static void setTitleText(View view, String title) {
        if (view != null) {
            TextView titleText = (TextView) view.findViewById(R.id.title);
            titleText.setVisibility(View.VISIBLE);
            titleText.setText(title);
        }
    }
    public static void setTitleImg(Activity activity, int resorce) {
        if (activity != null) {
            ImageView titleImgV = (ImageView) activity.findViewById(R.id.title_bar_title_imgv);
            titleImgV.setVisibility(View.VISIBLE);
            titleImgV.setImageResource(resorce);
        }
    }
    public static void setTitleImg(View view, int resorce) {
        if (view != null) {
            ImageView titleImgV = (ImageView) view.findViewById(R.id.title_bar_title_imgv);
            titleImgV.setVisibility(View.VISIBLE);
            titleImgV.setImageResource(resorce);
        }
    }

    //    public static void showActionButton (Activity activity, String string, View.OnClickListener l) {
//        if (activity != null) {
//            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_tops);
//            actionbtn.setVisibility(View.VISIBLE);
//            actionbtn.setText(string);
//            //ImageView actionButton = (ImageView) activity.findViewById(R.id.action_button_top);
//            //actionButton.setImageResource(iconRes);
//            //actionButton.setVisibility(View.VISIBLE);
//            if (actionbtn != null) {
//                actionbtn.setOnClickListener(l);
//            }
//        }
//    }
//    public static void showActionButton (View activity, String string, View.OnClickListener l) {
//        if (activity != null) {
//            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_tops);
//            actionbtn.setVisibility(View.VISIBLE);
//            actionbtn.setText(string);
//            //ImageView actionButton = (ImageView) activity.findViewById(R.id.action_button_top);
//            //actionButton.setImageResource(iconRes);
//            //actionButton.setVisibility(View.VISIBLE);
//            if (actionbtn != null) {
//                actionbtn.setOnClickListener(l);
//            }
//        }
//    }
    public static void shownextButton (Activity activity, String string, View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout);
            actionbtn_layout.setVisibility(View.VISIBLE);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            if (actionbtn_layout != null && actionbtn != null) {
                actionbtn_layout.setOnClickListener(l);
            }
        }
    }
    public static void shownextOtherButton (Activity activity, String string, View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top_other);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout_other);
            actionbtn_layout.setVisibility(View.VISIBLE);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            //ImageView actionButton = (ImageView) activity.findViewById(R.id.action_button_top);
            //actionButton.setImageResource(iconRes);
            //actionButton.setVisibility(View.VISIBLE);
            if (actionbtn != null) {
                actionbtn.setOnClickListener(l);
            }
        }
    }
    public static void shownextTextView(Activity activity, String string,int textsize, View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top_other);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout_other);
            actionbtn_layout.setVisibility(View.VISIBLE);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            actionbtn.setTextSize(textsize);
            if (actionbtn != null) {
                actionbtn.setOnClickListener(l);
            }
        }
    }
    public static void showRegisterText(Activity activity, String string,int textsize, int color,
                                        View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top_other);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout_other);
            actionbtn_layout.setVisibility(View.VISIBLE);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            actionbtn.setTextColor(color);
            actionbtn.setTextSize(textsize);
            if (actionbtn != null) {
                actionbtn.setOnClickListener(l);
            }
        }
    }
    public static void showRegisterText(Activity activity, String string,int textsize, int color,
                                        Drawable drawable,
                                        View.OnClickListener l) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top_other);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout_other);
            actionbtn_layout.setVisibility(View.VISIBLE);
            actionbtn.setVisibility(View.VISIBLE);
            actionbtn.setText(string);
            actionbtn.setTextColor(color);
            actionbtn.setTextSize(textsize);
            actionbtn.setCompoundDrawables(null, null, drawable, null);
            if (actionbtn != null) {
                actionbtn.setOnClickListener(l);
            }
        }
    }

    public static void showTitleBarRightCard(final Activity activity,boolean show,Integer img_src,View.OnClickListener l) {
        if (activity != null && show) {
            ImageView backImgButton = (ImageView)activity.findViewById(R.id.business_titlebar_right_card_img);
            RelativeLayout backButton = (RelativeLayout)activity.findViewById(R.id.business_titlebar_right_rl);
            if (img_src != null) {
                backImgButton.setImageResource(img_src);
            }
            backButton.setVisibility(View.VISIBLE);
            backImgButton.setVisibility(View.VISIBLE);
            if (backButton != null) {
                backButton.setOnClickListener(l);
                backImgButton.setOnClickListener(l);
            }
        }
    }
    public static void setNextViewText(Activity activity, String string) {
        if (activity != null) {
            TextView actionbtn = (TextView)activity.findViewById(R.id.action_text_top_other);
            RelativeLayout actionbtn_layout = (RelativeLayout)activity.findViewById(R.id.action_text_top_layout_other);
            if (string.equals("null")) {
                actionbtn_layout.setVisibility(View.GONE);
            } else {
                actionbtn_layout.setVisibility(View.VISIBLE);
                actionbtn.setVisibility(View.VISIBLE);
                actionbtn.setText(string);
            }


        }
    }

}

