package com.nexchanges.hailyo.model;

import java.util.ArrayList;

/**
 * Created by AbhishekWork on 03/07/15.
 */
public class DealData {
    private String user_name, thumbnailUrl,apartment_Name, offer_date, agr_start_date, deal_type, loan_status;
    private int rent_Amount, deposit_Amount, offer_price,loan_com;

    public DealData() {
    }

    public DealData(String name, String thumbnailUrl, String offer_date, int rent, int deposit, int offer_price, int loan_com, String loan_status, String apartment_name, String Ag_Start_Date, String deal_type
                    ) {
        this.user_name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.offer_date = offer_date;
        this.apartment_Name=apartment_name;
        this.rent_Amount=rent;
        this.deposit_Amount = deposit;
        this.agr_start_date = Ag_Start_Date;
        this.deal_type = deal_type;
        this.offer_price = offer_price;
        this.loan_com = loan_com;
        this.loan_status = loan_status;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public String getDealType() {
        return deal_type;
    }

    public void setDealType(String deal_type) {
        this.deal_type = deal_type;
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


    public String getLoanStatus() {
        return loan_status;
    }

    public void setLoanStatus(String loan_status) {
        this.loan_status = loan_status;
    }


    public int getRent() {
        return rent_Amount;
    }

    public void setRent(int rent) {
        this.rent_Amount = rent;
    }

    public int getOfferPrice() {
        return offer_price;
    }

    public void setOfferPrice(int offer_price) {
        this.offer_price = offer_price;
    }




    public int getDeposit() {
        return deposit_Amount;
    }

    public void setDeposit(int deposit) {
        this.deposit_Amount = deposit;
    }

    public int getLoanCom() {
        return loan_com;
    }

    public void setLoanCom(int loan_com) {
        this.loan_com = loan_com;
    }





}

