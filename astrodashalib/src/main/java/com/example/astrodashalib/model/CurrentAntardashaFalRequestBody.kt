package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

import java.util.ArrayList


class CurrentAntardashaFalRequestBody(@SerializedName("mainMahaDashaList") var mainMahaDashaList: ArrayList<MainMahaDasha>,
                                      @SerializedName("onlineResult") var onlineResult: String,
                                      @SerializedName("paramForPerskv") var paramForPerskv: String,
                                      @SerializedName("paramForProd") var paramForProd: String,
                                      @SerializedName("kpChart") var kpChartList: ArrayList<KPChart>,
                                      @SerializedName("cuspHouse") var cuspHouseList: ArrayList<CuspHouse>) : Serializable
