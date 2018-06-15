package com.example.astrodashalib.view.modules.chat

import android.content.Context
import android.os.Bundle
import com.example.astrodashalib.helper.BroadcastUtils

/**
 * Created by himanshu on 29/09/17.
 */
class RefreshChatScreen : ChatBroadcastInterface  {
    override fun exec(activity: ChatDetailActivity, ctx: Context, b: Bundle) {
        activity.updateRecievedChatStatus(false,true)
    }
}