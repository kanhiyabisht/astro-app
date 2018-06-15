package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 09/10/17.
 */

public class PaytmOrderStatusBody implements Serializable {

    @SerializedName("ORDERID")
    public String orderId;

    public PaytmOrderStatusBody(String orderId) {
        this.orderId = orderId;
    }
}
