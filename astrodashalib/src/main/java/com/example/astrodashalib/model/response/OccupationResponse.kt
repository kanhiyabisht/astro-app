package com.example.astrodashalib.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OccupationResponse : Serializable {

    @SerializedName("occupationPred")
    var occupationPred: String = ""
}
