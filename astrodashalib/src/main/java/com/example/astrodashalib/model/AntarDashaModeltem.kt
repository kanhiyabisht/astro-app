package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName


class AntarDashaModeltem(@field:SerializedName("Planet")
                         var mMainPlanetName: String, @field:SerializedName("Antar")
                         var mAntarPlanetName: String, @field:SerializedName("Period")
                         var mPeriod: String, @field:SerializedName("Age")
                         var mAge: String) {


    var isLoading = true

}
