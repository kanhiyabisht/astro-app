package com.example.astrodashalib.view.modules.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by himanshu on 29/09/17.
 */
class ChatReciever(val mChatRecieverInterface: ChatRecieverInterface) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        mChatRecieverInterface.execute(context, intent);
    }

    interface ChatRecieverInterface {
        fun execute(context: Context, intent: Intent)
    }
}