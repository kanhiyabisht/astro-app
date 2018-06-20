package com.example.astrodashalib.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Place implements Serializable {

    public boolean isProgressBar = false;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;

    @SerializedName("sno")
    public int sno;

    @SerializedName("time_correction_code")
    String timeCorrectionCode;

    @SerializedName("country_code")
    String countryCode;

    @SerializedName("place")
    public String place;

    @SerializedName("state_or_country_code")
    String stateOrCountryCode;

    @SerializedName("district")
    String district;

    @SerializedName("Place_Hindi")
    String placeHindi;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lon")
    public String lon;

    @SerializedName("timeZone")
    public String timeZone;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}