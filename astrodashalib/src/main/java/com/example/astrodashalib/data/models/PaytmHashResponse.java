package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmHashResponse implements Serializable {

    @SerializedName("CHECKSUMHASH")
    public String mChecksumhash;
}
