package com.example.amangarg.astro_dasha

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.astrodashalib.model.response.GenerateNewResponse

import com.example.astrodashalib.DashaData
import com.example.astrodashalib.interfaces.DashaCallback
import com.example.astrodashalib.model.GenerateNewRequestBody
import com.example.astrodashalib.model.Place
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var generateNewResponse = GenerateNewResponse()
    var dashaData = DashaData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dasha_btn.setOnClickListener {
            dashaData.getPlacesAsync("new", object : DashaCallback<List<Place>> {
                override fun onSuccess(data: List<Place>) {
                    val place = data[0]
                    val generateNewRequestBody = GenerateNewRequestBody(place.place,"3","5","1990","18","30",place.latitude,place.longitude,true)
                    dashaData.getGenerateNewResponseAsync("",generateNewRequestBody, object : DashaCallback<GenerateNewResponse> {
                        override fun onSuccess(data: GenerateNewResponse) {
                            generateNewResponse = data
                            kpChartHashMap.put("kpChart", generateNewResponse.mKpChartList)
                            cuspHouseHashMap.put("cuspHouse", generateNewResponse.mCuspHouseList)
                            kpChartCuspHashMap.put("kpChartCusp", generateNewResponse.mKpChartCuspList)
                            cuspHouseCuspHashMap.put("cuspHouseCusp", generateNewResponse.mCuspHouseCuspList)
                            mahadashaModelHashMap.put("mahadashaModel", generateNewResponse.mMahaDashaModelItemList)
                            mainMahadashaListHashMap.put("mainMahadashaList", generateNewResponse.mMainMahaDashaList)
                            onlineResultHashMap.put("onlineResult", generateNewResponse.mOnlineResult)
                            paramForPerskvHashMap.put("paramForPerskv", generateNewResponse.mParamForPerskv)
                            paramForProdHashMap.put("paramForProd", generateNewResponse.mParamForProd)
                            val intent = Intent(this@MainActivity, MainDashaActivity::class.java)
                            startActivity(intent)

                        }

                        override fun onError(e: Throwable) {
                            Log.e("onError", e.message)
                        }
                    })
                }

                override fun onError(e: Throwable) {
                    Log.e("onError", e.message)
                }
            })

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dashaData.clear()
    }
}
