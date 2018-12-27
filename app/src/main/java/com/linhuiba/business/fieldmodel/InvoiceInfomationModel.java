package com.linhuiba.business.fieldmodel;

import com.linhuiba.business.model.InvoicesModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/4.
 */
public class InvoiceInfomationModel implements Serializable{
    private ArrayList<InvoicesModel> order_info;
    private int id;
    private int invoice_type;
    private String title = "";
    private String tax_number = "";
    private String company_region = "";
    private String company_address = "";
    private String company_mobile = "";
    private String bank = "";
    private String account = "";
    private String general_taxpayer_qualification = "";
    private int is_default;
    private String invoice_content;
    private String total_fee;
    private String Area;
    private int address_id;
    private String name;
    private String mobile;
    private int ticket_type;

    private int is_paper;

    private int invoice_content_id;
    private String email;
    private Double total_tax;
    private Double total_special_tax;
    private Double delivery_fee;

    private Integer consignee_addresses_id;
    private int freight_collect;
    private String ticket_remarks;
    private int material;

    public ArrayList<InvoicesModel> getOrder_info() {
        return order_info;
    }

    public void setOrder_info(ArrayList<InvoicesModel> order_info) {
        this.order_info = order_info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(int invoice_type) {
        this.invoice_type = invoice_type;
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

    public String getCompany_region() {
        return company_region;
    }

    public void setCompany_region(String company_region) {
        this.company_region = company_region;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_mobile() {
        return company_mobile;
    }

    public void setCompany_mobile(String company_mobile) {
        this.company_mobile = company_mobile;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGeneral_taxpayer_qualification() {
        return general_taxpayer_qualification;
    }

    public void setGeneral_taxpayer_qualification(String general_taxpayer_qualification) {
        this.general_taxpayer_qualification = general_taxpayer_qualification;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(int ticket_type) {
        this.ticket_type = ticket_type;
    }

    public int getIs_paper() {
        return is_paper;
    }

    public void setIs_paper(int is_paper) {
        this.is_paper = is_paper;
    }

    public int getInvoice_content_id() {
        return invoice_content_id;
    }

    public void setInvoice_content_id(int invoice_content_id) {
        this.invoice_content_id = invoice_content_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(Double total_tax) {
        this.total_tax = total_tax;
    }

    public Double getTotal_special_tax() {
        return total_special_tax;
    }

    public void setTotal_special_tax(Double total_special_tax) {
        this.total_special_tax = total_special_tax;
    }

    public Double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(Double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public Integer getConsignee_addresses_id() {
        return consignee_addresses_id;
    }

    public void setConsignee_addresses_id(Integer consignee_addresses_id) {
        this.consignee_addresses_id = consignee_addresses_id;
    }

    public int getFreight_collect() {
        return freight_collect;
    }

    public void setFreight_collect(int freight_collect) {
        this.freight_collect = freight_collect;
    }

    public String getTicket_remarks() {
        return ticket_remarks;
    }

    public void setTicket_remarks(String ticket_remarks) {
        this.ticket_remarks = ticket_remarks;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }
}
