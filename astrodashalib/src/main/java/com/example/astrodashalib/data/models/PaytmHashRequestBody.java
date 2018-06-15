package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 09/10/17.
 */

public class PaytmHashRequestBody implements Serializable {

    @SerializedName("orderId")
    public String orderId;

    @SerializedName("customerId")
    public String customerId;

    @SerializedName("mobileNo")
    public String mobileNo;

    @SerializedName("txnAmt")
    public String txnAmt;

    @SerializedName("email")
    public String email;

    public PaytmHashRequestBody(String orderId, String customerId, String mobileNo, String txnAmt, String email) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.mobileNo = mobileNo;
        this.txnAmt = txnAmt;
        this.email = email;
    }
}
