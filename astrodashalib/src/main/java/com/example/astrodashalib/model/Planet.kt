package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Planet : Serializable {

    @SerializedName("planet")
    var mPlanet: String = ""

    @SerializedName("degree")
    var mDegree: String = ""

    @SerializedName("rlToSsl")
    var mRlToSsl: String = ""

    @SerializedName("significators")
    var mSignificators: String = ""

}
