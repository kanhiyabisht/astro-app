package com.example.astrodashalib.chat.messageController;

import android.content.Context;

import com.example.astrodashalib.chat.messageController.MessageController;

/**
 * Created by himanshu on 16/09/16.
 */
public class MessageControllerFactory {
    public static MessageControllerInterface getInstance(Context context){
        return new MessageController(context);
    }
}
