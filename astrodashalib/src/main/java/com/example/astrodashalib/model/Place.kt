package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Place(@SerializedName("latitude") var latitude: String,
            @SerializedName("longitude") var longitude: String,
            @SerializedName("sno") var sno: Int,
            @SerializedName("time_correction_code") var timeCorrectionCode: String,
            @SerializedName("country_code") var countryCode: String,
            @SerializedName("place") var place: String,
            @SerializedName("state_or_country_code") var stateOrCountryCode: String,
            @SerializedName("district") var district: String,
            @SerializedName("Place_Hindi") var placeHindi: String) : Serializable {

}