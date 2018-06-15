package com.example.astrodashalib.chat.notificationHandler;


import com.example.astrodashalib.data.models.ChatModel;

import org.json.JSONObject;

/**
 * Created by himanshu on 14/06/16.
 */
public class NullClass implements LocalIdCommandInterface {
    public NullClass() {
    }

    @Override
    public void execute(ChatModel chatModel, JSONObject chatJsonObj) {

    }
}
