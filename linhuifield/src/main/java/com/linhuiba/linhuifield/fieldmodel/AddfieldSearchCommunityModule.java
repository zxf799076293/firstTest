package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/15.
 */

public class AddfieldSearchCommunityModule implements Serializable {
    private FieldAddfieldCommunityModel community_data = new FieldAddfieldCommunityModel();
    private ArrayList<FieldAddfieldPhyResModel> physical_data = new ArrayList<>();

    public FieldAddfieldCommunityModel getCommunity_data() {
        return community_data;
    }

    public void setCommunity_data(FieldAddfieldCommunityModel community_data) {
        this.community_data = community_data;
    }

    public ArrayList<FieldAddfieldPhyResModel> getPhysical_data() {
        return physical_data;
    }

    public void setPhysical_data(ArrayList<FieldAddfieldPhyResModel> physical_data) {
        this.physical_data = physical_data;
    }
}
