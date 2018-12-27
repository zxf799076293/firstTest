package com.linhuiba.business.basemvp;

import android.content.Context;

/**
 * Created by Administrator on 2018/2/5.
 */

public interface BaseView {
    /**
     * 显示正在加载view
     */
    void showLoading();

    /**
     * 关闭正在加载view
     */
    void hideLoading();
    /**
     * 获取上下文
     * @return 上下文
     */
    Context getContext();
    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
}
