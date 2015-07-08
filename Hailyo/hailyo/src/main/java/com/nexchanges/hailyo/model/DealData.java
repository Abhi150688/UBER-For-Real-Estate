package com.nexchanges.hailyo.model;

import java.util.ArrayList;

/**
 * Created by AbhishekWork on 03/07/15.
 */
public class DealData {
    private String user_name, thumbnailUrl,apartment_Name, offer_date, agr_start_date;
    private int rent_Amount, deposit_Amount;

    public DealData() {
    }

    public DealData(String name, String thumbnailUrl, String offer_date, int rent, int deposit, String apartment_name, String Ag_Start_Date
                    ) {
        this.user_name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.offer_date = offer_date;
        this.apartment_Name=apartment_name;
        this.rent_Amount=rent;
        this.deposit_Amount = deposit;
        this.agr_start_date = Ag_Start_Date;

    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public String getApartmentName() {
        return apartment_Name;
    }

    public void setApartmentName(String apartment_name) {
        this.apartment_Name = apartment_name;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOfferDate() {
        return offer_date;
    }

    public void setOfferDate(String offer_date) {
        this.offer_date = offer_date;
    }


    public String getStartDate() {
        return agr_start_date;
    }

    public void setStartDate(String Ag_Start_Date) {
        this.agr_start_date = Ag_Start_Date;
    }



    public int getRent() {
        return rent_Amount;
    }

    public void setRent(int rent) {
        this.rent_Amount = rent;
    }



    public int getDeposit() {
        return deposit_Amount;
    }

    public void setDeposit(int deposit) {
        this.deposit_Amount = deposit;
    }





}

