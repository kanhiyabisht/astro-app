package com.example.astrodashalib.view.modules.chat

import android.content.Context
import android.os.Bundle

class ConnectivityChangePreN :ChatBroadcastInterface {

    override fun exec(activity: ChatDetailActivity, ctx: Context, b: Bundle) {
        val isNetworkConnected = b.getBoolean("isNetworkConnected", true)
        if(isNetworkConnected)
            activity.showInternetConnectionView()
        else
            activity.showNoConnectionView()
    }
}