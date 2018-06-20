package com.example.astrodashalib.service.fcm;

import java.util.Map;

/**
 * Created by himanshu on 05/10/17.
 */

public interface NotificationHelper {
    void showNotification(Map<String, String> payloadMap);
}
