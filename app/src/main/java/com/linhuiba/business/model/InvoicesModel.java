package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
public class  InvoicesModel implements Serializable {
    private String id;
    private String sum;
    private String created_at;
    private String status;
    private String title;
    private String tax_number;
    private String ticket_type;
    private String content;
    private String contact;
    private String mobile;
    private String address;

    private String field_order_item_id;
    private String name;
    private String pic_url;
    private Map<String,String> field;
    private String community;
    private String price;
    private String start;
    private String size;
    private String lease_term_type;
    private String res_type;
    private String ticket;
    private String deliver;
    private String delivery_num;
    private String field_type;
    private Map<String,String> field_first_img;
    private String actual_fee;
    //是否过期是否能开发票
    private Boolean issue_invoice;
    //新加的字段
    private String paid_at;
    private GroupBookingOrderSellResModel selling_resource = new GroupBookingOrderSellResModel();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTax_number() {
        return tax_number;
    }

    public void setTax_number(String tax_number) {
        this.tax_number = tax_number;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getField_order_item_id() {
        return field_order_item_id;
    }

    public void setField_order_item_id(String field_order_item_id) {
        this.field_order_item_id = field_order_item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Map<String, String> getField() {
        return field;
    }

    public void setField(Map<String, String> field) {
        this.field = field;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLease_term_type() {
        return lease_term_type;
    }

    public void setLease_term_type(String lease_term_type) {
        this.lease_term_type = lease_term_type;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getDelivery_num() {
        return delivery_num;
    }

    public void setDelivery_num(String delivery_num) {
        this.delivery_num = delivery_num;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public Map<String, String> getField_first_img() {
        return field_first_img;
    }

    public void setField_first_img(Map<String, String> field_first_img) {
        this.field_first_img = field_first_img;
    }

    public String getActual_fee() {
        return actual_fee;
    }

    public void setActual_fee(String actual_fee) {
        this.actual_fee = actual_fee;
    }

    public Boolean getIssue_invoice() {
        return issue_invoice;
    }

    public void setIssue_invoice(Boolean issue_invoice) {
        this.issue_invoice = issue_invoice;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public GroupBookingOrderSellResModel getSelling_resource() {
        return selling_resource;
    }

    public void setSelling_resource(GroupBookingOrderSellResModel selling_resource) {
        this.selling_resource = selling_resource;
    }
}
