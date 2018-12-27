package com.linhuiba.business.model;

/**
 * Created by snowd on 15/4/21.
 */
public class Version {

    private int vid; //": "1",
    private String num; //": "1.0",
    private String url; //": "",//版本更新地址
    private String platformname; //": "android",
    private String newfeature; //": "产品 v1.0新特性：\r\n-新增评价功能\r\n
    private int force_update;//是否需要强制更新
    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatformname() {
        return platformname;
    }

    public void setPlatformname(String platformname) {
        this.platformname = platformname;
    }

    public String getNewfeature() {
        return newfeature;
    }

    public void setNewfeature(String newfeature) {
        this.newfeature = newfeature;
    }

    public int getForce_update() {
        return force_update;
    }

    public void setForce_update(int force_update) {
        this.force_update = force_update;
    }
}
