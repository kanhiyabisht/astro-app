package com.example.astrodashalib.chat.notificationHandler;

import android.content.Context;

import com.example.astrodashalib.chat.messageController.MessageControllerInterface;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.helper.BroadcastUtils;

/**
 * Created by himanshu on 14/06/16.
 */
public class ChatScreenOpen implements ChatScreenCommandInterface {
    Context context;

    public ChatScreenOpen(Context context) {
        this.context = context;
    }

    @Override
    public void execute(ChatModel chatModel, MessageControllerInterface messageController) {

        try {

//                    BroadcastUtils.refreshChatList(context);
            BroadcastUtils.refreshChatList(chatModel,context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
