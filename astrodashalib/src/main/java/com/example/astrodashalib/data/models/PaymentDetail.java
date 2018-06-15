package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 09/10/17.
 */

public class PaymentDetail implements Serializable {

    @SerializedName("amount")
    public String amount;

    @SerializedName("error_msg")
    public String errorMsg;

    @SerializedName("additional_charges")
    public String additionalCharges;

    @SerializedName("kundliName")
    public String kundliName;

    @SerializedName("merchant")
    public String merchant;

    @SerializedName("discount")
    public String discount;

    @SerializedName("userId")
    public String userId;

    @SerializedName("questionText")
    public String questionText;

    @SerializedName("others")
    public String others;

    @SerializedName("timestamp")
    public String timestamp;

    public PaymentDetail(String amount, String errorMsg, String additionalCharges,  String kundliName, String merchant, String discount, String userId, String questionText, String others, String timestamp) {
        this.amount = amount;
        this.errorMsg = errorMsg;
        this.additionalCharges = additionalCharges;
        this.kundliName = kundliName;
        this.merchant = merchant;
        this.discount = discount;
        this.userId = userId;
        this.questionText = questionText;
        this.others = others;
        this.timestamp = timestamp;
    }
}
