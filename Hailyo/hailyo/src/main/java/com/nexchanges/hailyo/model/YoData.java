package com.nexchanges.hailyo.model;

import java.util.ArrayList;

/**
 * Created by AbhishekWork on 27/06/15.
 */
public class YoData {

   private String user_name, thumbnailUrl,specCode, user_type;
    private int visit_count;
    private int rating;

    public YoData() {
    }

    public YoData(String name, String thumbnailUrl, int rating, String spec_code, int visitCount, String userType
    ) {
        this.user_name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.specCode=spec_code;
        this.visit_count = visitCount;
        this.rating = rating;
        this.user_type=userType;

    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }


    public String getUserType() {
        return user_type;
    }

    public void setUserType(String userType) {
        this.user_type = userType;
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

    public int getVisitCount() {
        return visit_count;
    }

    public void setVisitCount(int visitCount) {
        this.visit_count = visitCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }




}
