package com.linhuiba.business.activity.searchcity;

import com.linhuiba.business.activity.searchcity.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * 介绍：美团里的城市bean
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/28.
 */

public class MeiTuanBean extends BaseIndexPinyinBean implements Serializable {
    private String name;//城市名字
    private String id;
    public MeiTuanBean() {
    }
    public String getName() {
        return name;
    }

    public MeiTuanBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public MeiTuanBean setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getTarget() {
        return name;
    }
}
