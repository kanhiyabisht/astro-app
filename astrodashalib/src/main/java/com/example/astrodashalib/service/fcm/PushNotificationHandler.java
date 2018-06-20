package com.example.astrodashalib.service.fcm;

import android.content.Context;


import com.example.astrodashalib.Constant;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.service.fcm.FCMService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by himanshu on 05/10/17.
 */

public class PushNotificationHandler implements NotificationHelper {


    Context ctx;
    UserModel user;
    FCMService fcmService;
    HashMap<String, NotificationHandlerInterface> notificationHandlerHashMap;

    public PushNotificationHandler(Context context, Map<String, String> payloadMap, FCMService fcmService) {
        this.ctx = context;
        this.fcmService = fcmService;
        notificationHandlerHashMap = new HashMap<>();

        notificationHandlerHashMap.put(Constant.CHAT, new ExtractChatFromFCM(ctx, payloadMap));
    }

    @Override
    public void showNotification(Map<String, String> payloadMap) {
        try {
            notificationHandlerHashMap.get(payloadMap.get("type")).showNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
