package com.linhuiba.linhuifield.fieldmodel;

/**
 * Created by Administrator on 2017/5/25.
 */

public class Field_AddResourcesInfoModel {
    private FieldAddfieldCommunityModel community_data = new FieldAddfieldCommunityModel();
    private FieldAddfieldPhyResModel physical_data = new FieldAddfieldPhyResModel();
    private FieldAddfieldSellResModel resource_data = new FieldAddfieldSellResModel();
    private int res_type_id;//资源类型 详情中有 发布时要复制到resource_data中 1场地3活动
    private int isRedact;//是否是编辑
    public FieldAddfieldCommunityModel getCommunity_data() {
        return community_data;
    }

    public void setCommunity_data(FieldAddfieldCommunityModel community_data) {
        this.community_data = community_data;
    }

    public FieldAddfieldPhyResModel getPhysical_data() {
        return physical_data;
    }

    public void setPhysical_data(FieldAddfieldPhyResModel physical_data) {
        this.physical_data = physical_data;
    }

    public FieldAddfieldSellResModel getResource_data() {
        return resource_data;
    }

    public void setResource_data(FieldAddfieldSellResModel resource_data) {
        this.resource_data = resource_data;
    }

    public int getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(int res_type_id) {
        this.res_type_id = res_type_id;
    }

    public int getIsRedact() {
        return isRedact;
    }

    public void setIsRedact(int isRedact) {
        this.isRedact = isRedact;
    }

}