package com.example.astrodashalib.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ParentsResponse : Serializable {

    @SerializedName("parentsPred")
    var parentsPred: String = ""
}
