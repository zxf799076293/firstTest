package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class FieldAddfieldSellResModel implements Serializable {
    private Integer days_in_advance;//提前预定天数，非询价必传
    private Integer invoice;//是否开发票(0/物业不支持给我们开发票，1/物业支持给我们开发票)
    private Integer has_power;//是否供电(0/不供电,1/供电)
    private Integer has_chair;//是否提供桌椅(0/不提供桌椅,1/提供桌椅)
    private Integer overnight_material;//物料过夜(0/不提供物料过夜,1/提供物料过夜)
    private Integer leaflet;//能否发传单(0/不可发传单,1/可发传单)
    private Integer has_tent;//是否提供帐篷(0/不提供帐篷,1/提供帐篷)
    private Integer support_rescheduling;//是否支持改期(1/支持改期,2/不支持改期)
    private Integer reschedule_in_advance;//提前改期的天数
    private String connecter;//场地对接人姓名
    private String connecter_mobile;//场地对接人联系电话(必须为有效的手机号码或座机)
    private String manager;//场地负责人姓名
    private String manager_mobile;//场地负责人联系电话（必须为有效的手机号码或座机)
    private String accountant;//财务联系人姓名
    private String accountant_mobile;//财务联系电话（必须为有效的手机号码或座机)
    private Integer is_enquiry;//是否为询价场地（1：是）【如果不是询价场地就不要传，传了会影响其它验证】
    private Integer refer_min_price;//参考价（低，分），询价必传
    private Integer refer_max_price;//参考价（高，分），询价必传
    private ArrayList<ReceiveAccountModel> receivables_information;//收款信息id
    private ArrayList<FieldAddfieldSellResDimensionsModel> dimensions = new ArrayList<>();//规格及价格,非询价必传
    private int id;
    private String custom_name;//自定义名称
    private int res_type_id;//供给类型ID 1/场地 2/活动
    private Integer activity_type_id;//活动类型ID
    private String activity_start_date;
    private String deadline;
    private ArrayList<FieldAddfieldAttributesModel> resource_img = new ArrayList<>();//活动图片
    private String description;//活动描述
    private Integer minimum_booking_days;
    private String tax_point;
    //询价
    private int goal;
    private int process;
    private int effect_pic;
    private int license;
    private int copying_of_id_card;
    private String m_else;
    private Field_AddResourceCreateItemModel activity_type;
    public Integer getDays_in_advance() {
        return days_in_advance;
    }

    public void setDays_in_advance(Integer days_in_advance) {
        this.days_in_advance = days_in_advance;
    }

    public Integer getInvoice() {
        return invoice;
    }

    public void setInvoice(Integer invoice) {
        this.invoice = invoice;
    }

    public Integer getHas_power() {
        return has_power;
    }

    public void setHas_power(Integer has_power) {
        this.has_power = has_power;
    }

    public Integer getHas_chair() {
        return has_chair;
    }

    public void setHas_chair(Integer has_chair) {
        this.has_chair = has_chair;
    }

    public Integer getOvernight_material() {
        return overnight_material;
    }

    public void setOvernight_material(Integer overnight_material) {
        this.overnight_material = overnight_material;
    }

    public Integer getLeaflet() {
        return leaflet;
    }

    public void setLeaflet(Integer leaflet) {
        this.leaflet = leaflet;
    }

    public Integer getHas_tent() {
        return has_tent;
    }

    public void setHas_tent(Integer has_tent) {
        this.has_tent = has_tent;
    }

    public Integer getSupport_rescheduling() {
        return support_rescheduling;
    }

    public void setSupport_rescheduling(Integer support_rescheduling) {
        this.support_rescheduling = support_rescheduling;
    }

    public Integer getReschedule_in_advance() {
        return reschedule_in_advance;
    }

    public void setReschedule_in_advance(Integer reschedule_in_advance) {
        this.reschedule_in_advance = reschedule_in_advance;
    }

    public String getConnecter() {
        return connecter;
    }

    public void setConnecter(String connecter) {
        this.connecter = connecter;
    }

    public String getConnecter_mobile() {
        return connecter_mobile;
    }

    public void setConnecter_mobile(String connecter_mobile) {
        this.connecter_mobile = connecter_mobile;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManager_mobile() {
        return manager_mobile;
    }

    public void setManager_mobile(String manager_mobile) {
        this.manager_mobile = manager_mobile;
    }

    public String getAccountant() {
        return accountant;
    }

    public void setAccountant(String accountant) {
        this.accountant = accountant;
    }

    public String getAccountant_mobile() {
        return accountant_mobile;
    }

    public void setAccountant_mobile(String accountant_mobile) {
        this.accountant_mobile = accountant_mobile;
    }

    public Integer getIs_enquiry() {
        return is_enquiry;
    }

    public void setIs_enquiry(Integer is_enquiry) {
        this.is_enquiry = is_enquiry;
    }

    public Integer getRefer_min_price() {
        return refer_min_price;
    }

    public void setRefer_min_price(Integer refer_min_price) {
        this.refer_min_price = refer_min_price;
    }

    public Integer getRefer_max_price() {
        return refer_max_price;
    }

    public void setRefer_max_price(Integer refer_max_price) {
        this.refer_max_price = refer_max_price;
    }

    public ArrayList<ReceiveAccountModel> getReceivables_information() {
        return receivables_information;
    }

    public void setReceivables_information(ArrayList<ReceiveAccountModel> receivables_information) {
        this.receivables_information = receivables_information;
    }

    public ArrayList<FieldAddfieldSellResDimensionsModel> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<FieldAddfieldSellResDimensionsModel> dimensions) {
        this.dimensions = dimensions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public int getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(int res_type_id) {
        this.res_type_id = res_type_id;
    }

    public Integer getActivity_type_id() {
        return activity_type_id;
    }

    public void setActivity_type_id(Integer activity_type_id) {
        this.activity_type_id = activity_type_id;
    }

    public String getActivity_start_date() {
        return activity_start_date;
    }

    public void setActivity_start_date(String activity_start_date) {
        this.activity_start_date = activity_start_date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public ArrayList<FieldAddfieldAttributesModel> getResource_img() {
        return resource_img;
    }

    public void setResource_img(ArrayList<FieldAddfieldAttributesModel> resource_img) {
        this.resource_img = resource_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinimum_booking_days() {
        return minimum_booking_days;
    }

    public void setMinimum_booking_days(Integer minimum_booking_days) {
        this.minimum_booking_days = minimum_booking_days;
    }

    public String getTax_point() {
        return tax_point;
    }

    public void setTax_point(String tax_point) {
        this.tax_point = tax_point;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getEffect_pic() {
        return effect_pic;
    }

    public void setEffect_pic(int effect_pic) {
        this.effect_pic = effect_pic;
    }

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public int getCopying_of_id_card() {
        return copying_of_id_card;
    }

    public void setCopying_of_id_card(int copying_of_id_card) {
        this.copying_of_id_card = copying_of_id_card;
    }

    public String getM_else() {
        return m_else;
    }

    public void setM_else(String m_else) {
        this.m_else = m_else;
    }

    public Field_AddResourceCreateItemModel getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Field_AddResourceCreateItemModel activity_type) {
        this.activity_type = activity_type;
    }
}
