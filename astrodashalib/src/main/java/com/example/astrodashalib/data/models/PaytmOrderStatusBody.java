package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmOrderStatusBody implements Serializable {

    @SerializedName("ORDERID")
    public String orderId;

    public PaytmOrderStatusBody(String orderId) {
        this.orderId = orderId;
    }
}
