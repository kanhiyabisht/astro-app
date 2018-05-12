package com.example.astrodashalib.model


import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class MahaDashaModelItem(@field:SerializedName("planet")
                         var planet: String, @field:SerializedName("period")
                         var period: String, var mAntarDashaList: ArrayList<AntarDashaModeltem>)
