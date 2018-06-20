package com.example.astrodashalib.service.fcm;

import android.content.Context;
import android.util.Log;


import com.example.astrodashalib.chat.notificationHandler.ChatNotificationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by himanshu on 05/10/17.
 */

public class ExtractChatFromFCM implements NotificationHandlerInterface {

    private static final String LOG_TAG = ExtractChatFromFCM.class.getSimpleName();
    Context context;
    public Map<String, String> payloadMap;

    public ExtractChatFromFCM(Context context, Map<String, String> payloadMap) {
        this.context = context;
        this.payloadMap = payloadMap;
    }


    @Override
    public void showNotification() {
        try {

//            if (UserCommonMethods.isUserLogin(user))
            executeChatNotificationHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void executeChatNotificationHelper() throws JSONException {
        JSONObject payloadObj = new JSONObject(payloadMap.get("payload"));
        ChatNotificationHelper chatNotificationHelper = new ChatNotificationHelper(context);
        chatNotificationHelper.execute(payloadObj);
        Log.e(LOG_TAG, "new message GCM");
    }
}
