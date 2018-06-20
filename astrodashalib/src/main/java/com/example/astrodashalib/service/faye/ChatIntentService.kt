package com.example.astrodashalib.service.faye

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.example.astrodashalib.chat.messageController.MessageControllerFactory
import com.example.astrodashalib.chat.messageController.MessageControllerInterface
import com.example.astrodashalib.data.models.ChatModel
import com.example.astrodashalib.generic.GenericCallback
import com.example.astrodashalib.generic.GenericQueryCallback
import com.example.astrodashalib.helper.BroadcastUtils
import com.example.astrodashalib.helper.DateTimeUtil
import com.example.astrodashalib.helper.getUserId


class ChatIntentService : IntentService("ChatIntentService") {

    lateinit var messageController: MessageControllerInterface
    var context: Context = this
    var userId: String? = null

    override fun onHandleIntent(intent: Intent?) {
        try {
            userId = getUserId(context.applicationContext)

            messageController = MessageControllerFactory.getInstance(getApplicationContext());
            messageController.fetchAllChatsToSend(userId, GenericQueryCallback<ChatModel> { error, list ->
                if (list != null && list.isNotEmpty())
                    sendChats(list);
            })
        } catch (e: Exception) {
        }
    }

    fun sendChats(list : ArrayList<ChatModel>){
        for (chatModel in list) {
            messageController.postChatOnServer(chatModel, GenericCallback () {error, `object` ->
                updateChatStatus()
            })
        }
    }

    fun updateChatStatus() {
        messageController.updateChatStatusOfSentChats(DateTimeUtil.getCurrentTimestampSeconds().toDouble(), userId, GenericCallback () {
            error, `object` ->
            BroadcastUtils.refreshChatStatus(context, 1.toDouble())
        })
    }

    companion object {

        @JvmStatic
        fun startChatIntentService(ctx: Context) {
            val intent = Intent(ctx, ChatIntentService::class.java)
            ctx.startService(intent)
        }
    }
}
