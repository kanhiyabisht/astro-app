package com.example.astrodashalib.generic;

import android.content.Context;


import com.example.astrodashalib.chat.ChatNotificationInterface;
import com.example.astrodashalib.chat.ChatStatusController;
import com.example.astrodashalib.chat.notificationHandler.ChatNotificationHelper;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by himanshu on 16/09/16.
 */
public class BroadcastEventHandler {
    HashMap<String, ChatNotificationInterface> notificationInterfaceHashMap = new HashMap<>();

    public BroadcastEventHandler(Context context) {
        notificationInterfaceHashMap.put("chatReceived", new ChatNotificationHelper(context));
        notificationInterfaceHashMap.put("updateReadTimestamp", new ChatStatusController(context));
    }

    public void execute(JSONObject message) {
        String action = message.optString("action");
        JSONObject result = message.optJSONObject("result");
        if (result != null)
            notificationInterfaceHashMap.get(action).execute(result);
    }
}
