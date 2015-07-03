package com.nexchanges.hailyo.model;

import java.util.ArrayList;

/**
 * Created by AbhishekWork on 03/07/15.
 */
public class DealData {
    private String user_name, thumbnailUrl,apartment_Name;
    private int offer_date, rent_Amount, deposit_Amount,start_Date;
    private ArrayList<String> genre;

    public DealData() {
    }

    public DealData(String name, String thumbnailUrl, int year, int rent, int deposit, String apartment_name, int Ag_Start_Date
                    ) {
        this.user_name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.offer_date = year;
        this.apartment_Name=apartment_name;
        this.rent_Amount=rent;
        this.deposit_Amount = deposit;
        this.start_Date = Ag_Start_Date;

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

    public int getOfferDate() {
        return offer_date;
    }

    public void setOfferDate(int year) {
        this.offer_date = year;
    }


    public int getStartDate() {
        return start_Date;
    }

    public void setStartDate(int Ag_Start_Date) {
        this.start_Date = Ag_Start_Date;
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

