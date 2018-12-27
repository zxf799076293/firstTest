package com.linhuiba.business.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class FieldinfoAllResourceInfoViewPagerAdapter extends PagerAdapter {
    public List<View> mListViews = null;

    public FieldinfoAllResourceInfoViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        ((ViewPager) container).removeView(mListViews.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }
}
