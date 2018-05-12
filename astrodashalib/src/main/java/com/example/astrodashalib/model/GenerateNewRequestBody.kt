package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GenerateNewRequestBody : Serializable {

    @SerializedName("place_name")
    var placeName: String = ""

    @SerializedName("day")
    var day: String = ""

    @SerializedName("month")
    var month: String = ""

    @SerializedName("year")
    var year: String = ""

    @SerializedName("hour")
    var hour: String = ""

    @SerializedName("minute")
    var minute: String = ""

    @SerializedName("tz")
    var tz: String = "82.30E"

    @SerializedName("timezone")
    var timezone: String = "82.30"

    @SerializedName("latitude")
    var latitude: String = ""

    @SerializedName("lon")
    var lon: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("longitude")
    var longitude: String = ""

    @SerializedName("ayan")
    var ayan = "K"

    @SerializedName("combocat")
    var combocat = "all"

    @SerializedName("language")
    var language = "hindi"

    @SerializedName("cmbRotate")
    var cmbRotate = 1

    @SerializedName("txtAge")
    var txtAge = 22

    @SerializedName("chk72")
    var chk72 = false

    @SerializedName("chkInclude")
    var chkInclude = false

    @SerializedName("chkShowRef")
    var chkShowRef = false

    @SerializedName("name")
    var name = "OM"

    @SerializedName("comboProd")
    var comboProd = "New Product"

    @SerializedName("relationshipCode")
    var relationshipCode = 0

    @SerializedName("chkcat")
    var chkcat = false

    private constructor(placeName: String, day: String, month: String, year: String, hour: String, minute: String, lat: String, lon: String, language: String, relationshipCode: Int) {
        this.placeName = placeName
        this.day = day
        this.month = month
        this.year = year
        this.hour = hour
        this.minute = minute
        this.tz = "82.30E"
        this.timezone = "82.30"
        this.lat = lat
        this.lon = lon
        this.latitude = lat.replace("N", "")
        this.longitude = lon.replace("E", "")
        this.ayan = "K"
        this.combocat = "all"
        this.language = language
        this.cmbRotate = 1
        this.txtAge = 22
        this.chk72 = false
        this.chkInclude = false
        this.chkShowRef = false
        this.name = "OM"
        this.comboProd = "New Product"
        this.relationshipCode = relationshipCode
        this.chkcat = false
    }

    constructor(placeName: String, day: String, month: String, year: String,
                hour: String, minute: String, lat: String, lon: String,
                englishEnabled: Boolean) :
            this(placeName, day, month, year, hour, minute, lat, lon, if (englishEnabled) "English" else "hindi", 0) {

    }
}