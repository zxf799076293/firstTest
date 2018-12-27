package com.linhuiba.business.fieldmodel;

/**
 * Created by Administrator on 2017/2/25.
 */

public class WalletFingerprintPayModel {
    private String uid;
    private String wallet_pw;
    private boolean open_fingerprint_pay;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWallet_pw() {
        return wallet_pw;
    }

    public void setWallet_pw(String wallet_pw) {
        this.wallet_pw = wallet_pw;
    }

    public boolean isOpen_fingerprint_pay() {
        return open_fingerprint_pay;
    }

    public void setOpen_fingerprint_pay(boolean open_fingerprint_pay) {
        this.open_fingerprint_pay = open_fingerprint_pay;
    }
}
