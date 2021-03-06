package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class CurrentAntardashaFalRequestBody(@field:SerializedName("mainMahaDashaList") var mainMahaDashaList: ArrayList<MainMahaDasha>,
                                      @field:SerializedName("onlineResult") var onlineResult: String,
                                      @field:SerializedName("paramForPerskv") var paramForPerskv: String,
                                      @field:SerializedName("paramForProd") var paramForProd: String)
