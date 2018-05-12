package com.example.astrodashalib.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HealthResponse : Serializable {

    @SerializedName("healthPred")
    var healthPred: String = ""
}
