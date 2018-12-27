package com.linhuiba.linhuipublic.callbackmodel;

/**
 * Created by snowd on 15/3/26.
 */



public class LoginInfo {
    private String id;
    private String role_id;// 1:管理员 2.物业 3.商家
    private String user_name;
    private String project_name;
    private String mobile;
    private String contact;
    private String email;
    private String address;
    private String apikey;
    private String invite_code;
    private String password;
    private String company;
    private String product;
    private String consumption_point;//积分
    private int enterprise_authorize_status;// 0:待审核 1:认证通过 2:认证失败 3:未提交认证
    private int enterprise_role;//用户在企业中的角色:0.没加入任何企业1.管理员2.普通员工
    private String name;
    private String avatar;
    private boolean wechat_bound;
    private boolean has_contracts;
    private int relation_id = -1;//1.自营2.签约3.代理4.注册
    private String membership_level;//"V2"
    private String industry_name;
    private boolean right_to_publish;//是否可以发布资源
    private boolean is_supplier;//是否是供应商
    private boolean qq_bound;
    private int ordering_noticed;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getConsumption_point() {
        return consumption_point;
    }

    public void setConsumption_point(String consumption_point) {
        this.consumption_point = consumption_point;
    }

    public int getEnterprise_authorize_status() {
        return enterprise_authorize_status;
    }

    public void setEnterprise_authorize_status(int enterprise_authorize_status) {
        this.enterprise_authorize_status = enterprise_authorize_status;
    }

    public int getEnterprise_role() {
        return enterprise_role;
    }

    public void setEnterprise_role(int enterprise_role) {
        this.enterprise_role = enterprise_role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isWechat_bound() {
        return wechat_bound;
    }

    public void setWechat_bound(boolean wechat_bound) {
        this.wechat_bound = wechat_bound;
    }

    public boolean isHas_contracts() {
        return has_contracts;
    }

    public void setHas_contracts(boolean has_contracts) {
        this.has_contracts = has_contracts;
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }

    public String getMembership_level() {
        return membership_level;
    }

    public void setMembership_level(String membership_level) {
        this.membership_level = membership_level;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public boolean isRight_to_publish() {
        return right_to_publish;
    }

    public void setRight_to_publish(boolean right_to_publish) {
        this.right_to_publish = right_to_publish;
    }

    public boolean isIs_supplier() {
        return is_supplier;
    }

    public void setIs_supplier(boolean is_supplier) {
        this.is_supplier = is_supplier;
    }

    public boolean isQq_bound() {
        return qq_bound;
    }

    public void setQq_bound(boolean qq_bound) {
        this.qq_bound = qq_bound;
    }

    public int getOrdering_noticed() {
        return ordering_noticed;
    }

    public void setOrdering_noticed(int ordering_noticed) {
        this.ordering_noticed = ordering_noticed;
    }
}
