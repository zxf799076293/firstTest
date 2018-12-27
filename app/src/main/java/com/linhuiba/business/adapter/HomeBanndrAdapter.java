package com.linhuiba.business.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class HomeBanndrAdapter extends PagerAdapter {
    private List<ImageView> imageList = new ArrayList<>();
    public HomeBanndrAdapter(List<ImageView> imageList) {
        this.imageList = imageList;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 对ViewPager页号求模取出View列表中要显示的项
        position %= imageList.size();
        if (position < 0) {
            position = imageList.size() + position;
        }
        ImageView view = imageList.get(position);
        // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        // add listeners here if necessary
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
