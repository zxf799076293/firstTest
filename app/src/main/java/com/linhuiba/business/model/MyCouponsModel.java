package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyCouponsModel implements Serializable {
    //用户优惠券
    private int id;
    private String title;//优惠券名称
    private String min_goods_amount;//满多少（1000）减
    private String amount;//优惠金额 （减 30）
    private String hint;//优惠券提示语
    private String effective_start_time;//生效开始时间
    private String effective_end_time;//生效结束时间
    private int quantity;//优惠券数量
    private int expired;//是否即将过期 （0：否   1：是）
    private int scope;//优惠券适用范围 （1：全场通用，scope=1时 field、value 均为null）（1：全场通用  2：限场地  3：限场地类目  4：限标签）
    private String field;//场地列表筛选字段 （community_type_ids：类目id，label_ids：标签id，physical_resource_id：展位id）
    private ArrayList<Integer> value = new ArrayList<>();//场地列表筛选字段 （community_type_ids：类目id，label_ids：标签id，physical_resource_id：展位id）
    //新人礼包
    private int coupon_rule_id;//优惠券ID
    //积分兑换优惠券
    private Integer point;//积分兑换时所需积分数
    private int method;//兑换方式 （1：用户领取  3：积分兑换）
    private int coupon_id;//优惠券id （积分兑换时使用此id领取优惠券）
    //使用优惠券
    private int receive;//是否已领取 （0：未领取  1：已领取）
    private String relative_time;
    //领券中心
    private Integer account_limit;//用户限领数量 （null：表示无限）
    private int user_coupons_count;//用户已领取数量
    private String shelf_time;//上架时间
    private int coupon_remind;//是否设置了优惠券提醒  1/是 0/否
    private int status;//优惠券状态 1/进行中 2/即将结束 3/即将开始 4/已领完
    private long remind_time = 100000;//倒计时时间
    ArrayList<MyCouponsModel> coupons = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMin_goods_amount() {
        return min_goods_amount;
    }

    public void setMin_goods_amount(String min_goods_amount) {
        this.min_goods_amount = min_goods_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getEffective_start_time() {
        return effective_start_time;
    }

    public void setEffective_start_time(String effective_start_time) {
        this.effective_start_time = effective_start_time;
    }

    public String getEffective_end_time() {
        return effective_end_time;
    }

    public void setEffective_end_time(String effective_end_time) {
        this.effective_end_time = effective_end_time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public void setValue(ArrayList<Integer> value) {
        this.value = value;
    }

    public int getCoupon_rule_id() {
        return coupon_rule_id;
    }

    public void setCoupon_rule_id(int coupon_rule_id) {
        this.coupon_rule_id = coupon_rule_id;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public String getRelative_time() {
        return relative_time;
    }

    public void setRelative_time(String relative_time) {
        this.relative_time = relative_time;
    }

    public Integer getAccount_limit() {
        return account_limit;
    }

    public void setAccount_limit(Integer account_limit) {
        this.account_limit = account_limit;
    }

    public int getUser_coupons_count() {
        return user_coupons_count;
    }

    public void setUser_coupons_count(int user_coupons_count) {
        this.user_coupons_count = user_coupons_count;
    }

    public String getShelf_time() {
        return shelf_time;
    }

    public void setShelf_time(String shelf_time) {
        this.shelf_time = shelf_time;
    }

    public int getCoupon_remind() {
        return coupon_remind;
    }

    public void setCoupon_remind(int coupon_remind) {
        this.coupon_remind = coupon_remind;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(long remind_time) {
        this.remind_time = remind_time;
    }

    public ArrayList<MyCouponsModel> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<MyCouponsModel> coupons) {
        this.coupons = coupons;
    }
}
