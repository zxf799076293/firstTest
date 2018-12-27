package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MapCommunitySearchListModel implements Serializable {
    private ArrayList<MapCommunityInfoModel> communities;
    private ArrayList<MapCommunityInfoModel> trading_areas;

    public ArrayList<MapCommunityInfoModel> getCommunities() {
        return communities;
    }

    public void setCommunities(ArrayList<MapCommunityInfoModel> communities) {
        this.communities = communities;
    }

    public ArrayList<MapCommunityInfoModel> getTrading_areas() {
        return trading_areas;
    }

    public void setTrading_areas(ArrayList<MapCommunityInfoModel> trading_areas) {
        this.trading_areas = trading_areas;
    }
}
