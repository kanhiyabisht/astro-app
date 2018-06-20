package com.example.astrodashalib.view.modules.chat

import android.content.Context
import android.os.Bundle



/**
 * Created by himanshu on 29/09/17.
 */
interface ChatBroadcastInterface {
    fun exec(activity: ChatDetailActivity, ctx: Context, b: Bundle)
}