package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/3/7.
 */
public class LoginInfoModel {

    private String account; //": "1",  //用户ID
    private String apikey; //": "201502101507461695157"  //凭证

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getApikey() {
        return apikey;
    }
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

}
