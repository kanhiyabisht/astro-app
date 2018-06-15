package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 26/09/17.
 */

public class UserModel implements Serializable {
    public static final int ANDROID_OS = 0;

    @SerializedName("_id")
    public String id = "";

    public String userKey = "";

    @SerializedName("userName")
    public String userName;

    @SerializedName("day")
    public String day;

    @SerializedName("month")
    public String month;

    @SerializedName("year")
    public String year;

    @SerializedName("hour")
    public String hour;

    @SerializedName("min")
    public String minute;

    @SerializedName("place")
    public String city;

    public String country;

    @SerializedName("lat")
    public String latitude = "";

    @SerializedName("lng")
    public String longitude = "";

    @SerializedName("deviceId")
    public String deviceId = "";

    @SerializedName("deviceOs")
    public int deviceOs = ANDROID_OS;

    @SerializedName("phone")
    public String phone = "";

    @SerializedName("email")
    public String email = "";

    @SerializedName("gender")
    public Integer gender = 0;

    @SerializedName("kundli_id")
    public String kundliId ;

    public String userModelKey ;


    public UserModel(String id, String userKey, String userName, String day, String month, String year, String hour, String minute, String city, String country, String latitude, String longitude, String deviceId, String phone, String email, int gender) {
        this.id = id;
        this.userKey = userKey;
        this.userName = userName;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceId = deviceId;
        this.deviceOs = ANDROID_OS;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }


    public UserModel(String deviceId, String phone) {
        this.deviceId = deviceId;
        this.deviceOs = ANDROID_OS;
        this.id = null;
        this.userKey = null;
        this.userName = null;
        this.day = null;
        this.month = null;
        this.year = null;
        this.hour = null;
        this.minute = null;
        this.city = null;
        this.country = null;
        this.latitude = null;
        this.longitude = null;
        this.phone = phone;
        this.email = null;
        this.gender = 0;
    }


    public UserModel(UserModel userModel){
        this.id = null;
        this.deviceId = userModel.deviceId;
        this.deviceOs = ANDROID_OS;

        this.userKey = null;
        this.userName = userModel.userName;
        this.day = userModel.day;
        this.month =  userModel.month;
        this.year =  userModel.year;
        this.hour =  userModel.hour;
        this.minute =  userModel.minute;
        this.city =  userModel.city;
        this.country =  userModel.country;
        this.latitude =  userModel.latitude;
        this.longitude =  userModel.longitude;
        this.phone =  userModel.phone;
        this.email =  userModel.email;
        this.gender =  userModel.gender;
    }
}
