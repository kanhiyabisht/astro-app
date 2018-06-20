package com.example.astrodashalib.helper

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Created by himanshu on 04/10/17.
 */
object AppHelper {


    @JvmStatic
    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        return status != ConnectionResult.SERVICE_DISABLED && status == ConnectionResult.SUCCESS
    }
}