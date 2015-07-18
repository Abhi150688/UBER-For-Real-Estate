package com.nexchanges.hailyo.custom;

/**
 * Created by AbhishekWork on 21/06/15.
 */


public class MyMarker
{
    private String mLabel;
    private String mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public MyMarker(Double latitude, Double longitude)
    {
        //this.mLabel = label;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        //this.mIcon = icon;
    }

    public String getmLabel()
    {
        return mLabel;
    }

    public void setmLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }

    public String getmIcon()
    {
        return mIcon;
    }

    public void setmIcon(String icon)
    {
        this.mIcon = icon;
    }

    public Double getmLatitude()
    {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude)
    {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude()
    {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude)
    {
        this.mLongitude = mLongitude;
    }
}