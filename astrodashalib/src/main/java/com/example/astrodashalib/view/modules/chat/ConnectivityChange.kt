package com.example.astrodashalib.view.modules.chat

import android.content.Context
import android.os.Bundle
import com.example.astrodashalib.isNetworkAvailable
import com.example.astrodashalib.service.faye.FayeIntentService

/**
 * Created by himanshu on 09/10/17.
 */
class ConnectivityChange :ChatBroadcastInterface {

    override fun exec(activity: ChatDetailActivity, ctx: Context, b: Bundle) {
        if(ctx.isNetworkAvailable())
            FayeIntentService.startFayeService(ctx)
    }
}