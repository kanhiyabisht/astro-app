package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 09/10/17.
 */

public class PaytmHashResponse implements Serializable {

    @SerializedName("CHECKSUMHASH")
    public String mChecksumhash;
}
