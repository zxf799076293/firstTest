
package com.baselib.app.view;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {

    public T mFragment;

    private final Activity mActivity;

    private final String mTag;

    /**
     * Constructor used each time a new tab is created.
     * 
     * @param activity The host Activity, used to instantiate the fragment
     * @param tag The identifier tag for the fragment
     * @param clz The fragment's Class, used to instantiate the fragment
     */
    public TabListener(Activity activity, String tag, T fragment) {
        mActivity = activity;
        mTag = tag;
        mFragment = fragment;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(android.R.id.content, mFragment, mTag);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}
