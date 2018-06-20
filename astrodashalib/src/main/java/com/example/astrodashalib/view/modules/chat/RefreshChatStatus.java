package com.example.astrodashalib.view.modules.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;

/**
 * Created by himanshu on 06/06/16.
 */
public class RefreshChatStatus implements ChatBroadcastInterface {

    @Override
    public void exec(ChatDetailActivity chatDetailActivity, Context ctx, Bundle b) {
        try {
            Log.e("RefreshChatStatus","");
            Double readTimestamp = b.getDouble("readTimestamp");
            if (readTimestamp != 0)
                chatDetailActivity.fetchChatFromDb(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
