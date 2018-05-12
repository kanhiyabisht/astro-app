package com.example.astrodashalib.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class PredictionRequestBody(@field:SerializedName("kpChartCusp")
                            var mKpChart: ArrayList<KPChart>, @field:SerializedName("cuspHouse")
                            var mCuspHouse: ArrayList<CuspHouse>, @field:SerializedName("onlineResult")
                            var mOnlineResult: String, @field:SerializedName("paramForPerskv")
                            var mParamForPerskv: String, @field:SerializedName("paramForProd")
                            var mParamForProd: String)
