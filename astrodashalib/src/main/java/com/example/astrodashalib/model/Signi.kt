package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Signi : Serializable {

    @SerializedName("rule")
    var rule: Int = 0

    @SerializedName("signi")
    var signi: Int = 0

    @SerializedName("whichPlanet")
    var whichPlanet: Int = 0
}

