package com.example.amangarg.astro_dasha

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.astrodashalib.model.response.HoroscopeResponse
import kotlinx.android.synthetic.main.activity_horoscope.*

class HoroscopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horoscope)

        var horoscopeResponse = intent.getSerializableExtra("horoscopeResp") as HoroscopeResponse

        janamrashi_header_tv.text = horoscopeResponse.janamrashiHeader
        janamrashi_value_tv.text = horoscopeResponse.janamrashiValue

        janamlagn_header_tv.text = horoscopeResponse.janamlagnaHeader
        janamlagn_value_tv.text = horoscopeResponse.janamlagnaValue

        nak_header_tv.text = horoscopeResponse.nakHeader
        nak_value_tv.text = horoscopeResponse.nakValue

        bday_planet_header_tv.text = horoscopeResponse.birthdayPlanetHeader
        bday_planet_value_tv.text = horoscopeResponse.birthdayPlanetValue

        birthtime_planet_header_tv.text = horoscopeResponse.birthTimePlanetHeader
        birthtime_planet_value_tv.text = horoscopeResponse.birthTimePlanetValue

        mahadasha_header_tv.text = horoscopeResponse.currentMahadashaHeader
        mahadasha_value_tv.text = horoscopeResponse.currentMahadashaValue

        antardasha_header_tv.text = horoscopeResponse.currentAntardashaHeader
        antardasha_value_tv.text = horoscopeResponse.currentAntardashaValue

        manglik_header_tv.text = horoscopeResponse.manglikHeader
        manglik_value_tv.text = horoscopeResponse.manglicValue

        kaalsarp_header_tv.text = horoscopeResponse.kaalSarpHeader
        kaalsarp_value_tv.text = horoscopeResponse.kaalSarpValue

        gandmool_header_tv.text = horoscopeResponse.gandMoolHeader
        gandmool_value_tv.text = horoscopeResponse.gandMoolValue

        luckyday_header_tv.text = horoscopeResponse.luckyDayHeader
        luckyday_value_tv.text = horoscopeResponse.luckyDayValue

        luckynum_header_tv.text = horoscopeResponse.luckynumberHeader
        luckynum_value_tv.text = horoscopeResponse.luckynumberValue

        ratna_header_tv.text = horoscopeResponse.ratna4YouSubHeader
        ratna_value_tv.text = horoscopeResponse.ratna4YouValue

        surya_header_tv.text = horoscopeResponse.suryaHeader
        surya_value_tv.text = horoscopeResponse.suryaValue

        chandra_header_tv.text = horoscopeResponse.chandraHeader
        chandra_value_tv.text = horoscopeResponse.chandraValue

        mangal_header_tv.text = horoscopeResponse.mangalHeader
        mangal_value_tv.text = horoscopeResponse.mangalValue

        budh_header_tv.text = horoscopeResponse.budhHeader
        budh_value_tv.text = horoscopeResponse.budhValue

        guru_header_tv.text = horoscopeResponse.guruHeader
        guru_value_tv.text = horoscopeResponse.guruValue

        sukra_header_tv.text = horoscopeResponse.sukraHeader
        sukra_value_tv.text = horoscopeResponse.sukraValue

        shani_header_tv.text = horoscopeResponse.shaniHeader
        shani_value_tv.text = horoscopeResponse.shaniValue

        rahu_header_tv.text = horoscopeResponse.rahuHeader
        rahu_value_tv.text = horoscopeResponse.rahuValue

        ketu_header_tv.text = horoscopeResponse.ketuHeader
        ketu_value_tv.text = horoscopeResponse.ketuValue

        v_daan_header_tv.text = horoscopeResponse.vdaanHeader
        v_daan_value_tv.text = horoscopeResponse.vdaanValue

        daan_header_tv.text = horoscopeResponse.daanHeader
        daan_value_tv.text = horoscopeResponse.daanValue

        v_color_header_tv.text = horoscopeResponse.vcolorHeader
        v_color_value_tv.text = horoscopeResponse.vcolorValue

        planet_lord_value_tv.text = horoscopeResponse.planetLordValue
    }
}

