package com.example.astrodashalib.service.fcm


import android.util.Log;
import com.example.astrodashalib.service.fcm.PushNotificationHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by himanshu on 03/10/17.
 */
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            // The Firebase console always sends notification messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.from)
            // Check if message contains a notification payload.
            if (remoteMessage.notification != null)
                Log.d(TAG, "Message Notification Body: " + remoteMessage.notification.body)

            // Check if message contains a data payload.
            if (!remoteMessage.data.isEmpty())
                processPayload(remoteMessage.getData())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processPayload(map: Map<String, String>) {

        try {
            val pushNotificationHandler = PushNotificationHandler(this, map, this@FCMService)
            pushNotificationHandler.showNotification(map)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        val TAG = FCMService::class.java.simpleName
    }
}