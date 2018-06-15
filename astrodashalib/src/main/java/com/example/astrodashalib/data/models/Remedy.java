package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by himanshu on 11/10/17.
 */

public class Remedy {
    @SerializedName("gender")
    public int gender;

    @SerializedName("planet")
    public int planet;

    @SerializedName("upay_english")
    public String upayEnglish;

    @SerializedName("price_inr")
    public int priceInr;

    @SerializedName("upay_hindi")
    public String upayHindi;

    @SerializedName("ptype")
    public String ptype;

    @SerializedName("created_by")
    public String createdBy;

    @SerializedName("house")
    public int house;

    @SerializedName("rule_id")
    public int ruleId;

    @SerializedName("relevant")
    public String relevant;

    @SerializedName("price_dollar")
    public int priceDollar;

    @SerializedName("updated_by")
    public String updatedBy;

    @SerializedName("id")
    public int id;

    @SerializedName("remarks")
    public String remarks;

    @SerializedName("rule_text")
    public String ruleText;

    @SerializedName("purchaseTimestamp")
    public String purchaseTimestamp;

    public String categoryName;
}
