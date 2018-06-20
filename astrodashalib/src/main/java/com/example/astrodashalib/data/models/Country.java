package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 25/09/17.
 */

public class Country implements Serializable {

    public boolean isProgressBar = false;

    @SerializedName("Zone_Start")
    String zoneStart;

    @SerializedName("Zone_End")
    String zoneEnd;

    @SerializedName("country")
    public String country;

    @SerializedName("country_code")
    public String countryCode;

    @SerializedName("start_boundary_of_latitude")
    String startBoundaryLat;

    @SerializedName("end_boundary_of_latitude")
    String endBoundaryLat;

    @SerializedName("start_boundary_of_longitude")
    String startBoundaryLon;

    @SerializedName("end_boundary_of_longitude")
    String endBoundaryLon;

    @SerializedName("sno")
    int sno;

    @SerializedName("time_correction_code")
    String timeCorrectionCode;

}
