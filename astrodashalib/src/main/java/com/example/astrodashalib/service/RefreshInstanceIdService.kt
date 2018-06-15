package com.example.astrodashalib.service

import com.google.firebase.iid.FirebaseInstanceIdService
import com.example.astrodashalib.service.device.DeviceIdService

/**
 * Created by himanshu on 03/10/17.
 */
class RefreshInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        DeviceIdService.startService(this)
    }
}