package com.nexchanges.hailyo.model;

import java.util.ArrayList;

/**
 * Created by AbhishekWork on 27/06/15.
 */
public class VisitData {

   private String user_name, thumbnailUrl,specCode,loc,dealingRoom;
    private int visitdate;
    private double prop_count;
    private ArrayList<String> genre;

    public VisitData() {
    }

    public VisitData(String name, String thumbnailUrl, int year, double rating,String spec_code, String location, String dealing_room
                 ) {
        this.user_name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.visitdate = year;
        this.prop_count = rating;
        this.specCode=spec_code;
        this.loc=location;
        this.dealingRoom = dealing_room;

    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public String getDealingRoom() {
        return dealingRoom;
    }

    public void setDealingRoom(String dealing_room) {
        this.dealingRoom = dealing_room;
    }


    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String spec_code) {
        this.specCode = spec_code;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getVisitDate() {
        return visitdate;
    }

    public void setVisitDate(int year) {
        this.visitdate = year;
    }

    public double getPropsCount() {
        return prop_count;
    }

    public void setPropsCount(double rating) {
        this.prop_count = rating;
    }


    public String getLocation() {
        return loc;
    }

    public void setLocation(String location) {
        this.loc = location;
    }


}
