package com.example.astrodashalib.view.modules.chat

import android.content.Context
import android.os.Bundle
import com.example.astrodashalib.data.models.ChatModel

/**
 * Created by himanshu on 29/09/17.
 */
class AddNewChat : ChatBroadcastInterface {

    var context: Context? = null
    var bundle: Bundle? = null
    var chatModel: ChatModel? = null

    override fun exec(activity: ChatDetailActivity, ctx: Context, b: Bundle) {
        try {
            this.context = ctx
            this.bundle = b

            chatModel = b.getSerializable("chat") as ChatModel
            if (!isChatDuplicate(chatModel, activity.chatModelList[activity.chatModelList.size - 1]))
                refreshChatScreenAndChatList(activity, chatModel)

        } catch (e: ArrayIndexOutOfBoundsException) { //for first time
            refreshChatScreenAndChatList(activity, chatModel)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun refreshChatScreenAndChatList(activity: ChatDetailActivity, chatModel: ChatModel?) {
        activity.addChatToScreen(chatModel)
    }


    internal fun isChatDuplicate(chat1: ChatModel?, chat2: ChatModel): Boolean {
        return chat1?.localId == chat2.localId
    }

}