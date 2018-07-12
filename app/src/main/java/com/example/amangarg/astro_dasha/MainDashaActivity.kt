package com.example.amangarg.astro_dasha

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.astrodashalib.DashaData
import com.example.astrodashalib.interfaces.DashaCallback
import com.example.astrodashalib.model.*
import com.example.astrodashalib.model.response.*
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity
import kotlinx.android.synthetic.main.activity_main_dasha.*
import kotlinx.android.synthetic.main.progress_layout.*

class MainDashaActivity : AppCompatActivity() {
    var dashaData = DashaData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dasha)

        var intent = Intent(this, DashaTextActivity::class.java)
        var horoscopeIntent = Intent(this, HoroscopeActivity::class.java)
        var dashaResponse: String? = null


        var kpChartValue: ArrayList<KPChart> = kpChartHashMap.get("kpChart") ?: ArrayList()
        var cuspHouseValue: ArrayList<CuspHouse> = cuspHouseHashMap.get("cuspHouse") ?: ArrayList()
        var cuspHouseCuspValue: ArrayList<CuspHouse> = cuspHouseCuspHashMap.get("cuspHouseCusp")
                ?: ArrayList()
        var mainMahadashaListValue: ArrayList<MainMahaDasha> = mainMahadashaListHashMap.get("mainMahadashaList")
                ?: ArrayList()
        var onlineResultValue: String = onlineResultHashMap.get("onlineResult") ?: ""
        var paramForPerskvValue: String = paramForPerskvHashMap.get("paramForPerskv") ?: ""
        var paramForProdValue: String = paramForProdHashMap.get("paramForProd") ?: ""

        chatBtn.setOnClickListener {
            val style = R.style.AppTheme1
            val currentAntardashaFalRequestBody = CurrentAntardashaFalRequestBody(mainMahadashaListValue,onlineResultValue,paramForPerskvValue,paramForProdValue,kpChartValue,cuspHouseValue)
            startActivity(ChatDetailActivity.createIntent(this@MainDashaActivity,style,"him.phogat123@gmail.com","9212200005","Himanshu",currentAntardashaFalRequestBody))
        }

        horoscopeBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE

            dashaData.getHoroscopeResponseAsync("",HoroscopeRequestBody(kpChartValue, onlineResultValue, paramForPerskvValue), object : DashaCallback<HoroscopeResponse> {
                override fun onSuccess(data: HoroscopeResponse) {

                    horoscopeIntent.putExtra("horoscopeResp", data)
                    startActivity(horoscopeIntent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }
            })


        }

        currentMahadashaBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getCurrentMahadashaFalAsync("",CurrentMahadashaFalRequestBody(mainMahadashaListValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<CurrentMahadashaFalResponse> {
                override fun onSuccess(data: CurrentMahadashaFalResponse) {
                    dashaResponse = data.mahadashaText
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        currentAntarDashaBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getCurrentAntardashaFalAsync("",CurrentAntardashaFalRequestBody(mainMahadashaListValue, onlineResultValue, paramForPerskvValue, paramForProdValue,kpChartValue,cuspHouseValue), object : DashaCallback<CurrentAntardashaFalResponse> {
                override fun onSuccess(data: CurrentAntardashaFalResponse) {
                    dashaResponse = data.antardashaText
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        yogYutiBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getYogYutiResponseAsync("",YogYutiRequestBody(kpChartValue, cuspHouseCuspValue, onlineResultValue, paramForPerskvValue), object : DashaCallback<YogYutiResponse> {
                override fun onSuccess(data: YogYutiResponse) {
                    dashaResponse = data.onlyYogYukti
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        healthBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getHealthResponseAsync("",PredictionRequestBody(kpChartValue, cuspHouseValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<HealthResponse> {
                override fun onSuccess(data: HealthResponse) {
                    dashaResponse = data.healthPred
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        marriedLifeBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getMarriedLifeResponseAsync("",PredictionRequestBody(kpChartValue, cuspHouseValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<MarriedLifeResponse> {
                override fun onSuccess(data: MarriedLifeResponse) {
                    dashaResponse = data.marriedLifePred
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        occupationBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getOccupationResponseAsync("",PredictionRequestBody(kpChartValue, cuspHouseValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<OccupationResponse> {
                override fun onSuccess(data: OccupationResponse) {
                    dashaResponse = data.occupationPred
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }
        parentsBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getParentsResponseAsync("",PredictionRequestBody(kpChartValue, cuspHouseValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<ParentsResponse> {
                override fun onSuccess(data: ParentsResponse) {
                    dashaResponse = data.parentsPred
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }

        childrenBtn.setOnClickListener {
            nsv.visibility = View.GONE
            progress_view_ll.visibility = View.VISIBLE
            dashaData.getChildrenResponseAsync("",PredictionRequestBody(kpChartValue, cuspHouseValue, onlineResultValue, paramForPerskvValue, paramForProdValue), object : DashaCallback<ChildrenResponse> {
                override fun onSuccess(data: ChildrenResponse) {
                    dashaResponse = data.santanPred
                    intent.putExtra("dashaResp", dashaResponse)
                    startActivity(intent)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Exception(e)
                    progress_view_ll.visibility = View.GONE
                    nsv.visibility = View.VISIBLE
                }

            })
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        dashaData.clear()
    }


}
