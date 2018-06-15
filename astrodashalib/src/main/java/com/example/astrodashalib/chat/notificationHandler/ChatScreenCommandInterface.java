package com.example.astrodashalib.chat.notificationHandler;


import com.example.astrodashalib.chat.messageController.MessageControllerInterface;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.data.models.UserModel;

/**
 * Created by himanshu on 14/06/16.
 */
public interface ChatScreenCommandInterface {
    void execute(ChatModel chatModel, MessageControllerInterface messageController);
}
