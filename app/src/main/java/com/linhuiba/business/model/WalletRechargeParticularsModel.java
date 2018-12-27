package com.linhuiba.business.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/18.
 */

public class WalletRechargeParticularsModel {
    private int id;
    private String out_trade_no;
    private Double amount;
    private int payment_mode;//支付方式(0:线下支付 1：微信app支付 2:微信公众号支付 3:微信原生扫码支付)
    private String voucher_image_url;
    private int confirmed;
    private String paid_at;
    private String created_at;
    private int transaction_type;
    private String name;
    private HashMap<String,String> invoice_order = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(int payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getVoucher_image_url() {
        return voucher_image_url;
    }

    public void setVoucher_image_url(String voucher_image_url) {
        this.voucher_image_url = voucher_image_url;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getInvoice_order() {
        return invoice_order;
    }

    public void setInvoice_order(HashMap<String, String> invoice_order) {
        this.invoice_order = invoice_order;
    }
}
