package com.example.astrodashalib.chat;

import android.content.Context;
import android.util.Log;


import com.example.astrodashalib.R;
import com.example.astrodashalib.chat.messageController.MessageControllerFactory;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.helper.BroadcastUtils;

import org.json.JSONObject;

/**
 * Created by himanshu on 02/07/16.
 */
public class ChatStatusController implements ChatNotificationInterface {

    Context ctx;
    private static final String LOG_TAG = ChatStatusController.class.getSimpleName();

    public ChatStatusController(Context ctx) {
        this.ctx = ctx;
    }

    public void execute(Object object) {
        try {
            JSONObject jsonObject = (JSONObject) object;
            String buddyId = jsonObject.optString("reader_user_id");
            String myId = jsonObject.optString("writer_user_id");
            final Double readTimestamp = jsonObject.optDouble("read_timestamp");
            MessageControllerFactory.getInstance(ctx).updateReadTimestampOfSentChats(readTimestamp, buddyId, myId, new GenericCallback() {
                @Override
                public void callback(JSONObject error, Object object) {
                    Log.e(LOG_TAG,readTimestamp.toString());
                    BroadcastUtils.refreshChatStatus(ctx, readTimestamp);
                }
            });
//            Analytics.sendNotificationEvents(R.string.label_chat_read_status_recieved,ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
