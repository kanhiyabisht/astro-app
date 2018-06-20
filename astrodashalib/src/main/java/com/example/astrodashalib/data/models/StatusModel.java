package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by himanshu on 26/09/17.
 */

public class StatusModel implements Serializable {

    @SerializedName("success")
    private SuccessModel mSuccessModel1;

    @SerializedName("error")
    private SuccessModel mSuccessModel2;
}
