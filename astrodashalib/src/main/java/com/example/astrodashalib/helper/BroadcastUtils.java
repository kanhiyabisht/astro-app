package com.example.astrodashalib.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;

/**
 * Created by himanshu on 29/09/17.
 */

public class BroadcastUtils {


    public static void refreshChatList(ChatModel chatModel, Context context) {
        Intent intent = new Intent(ChatDetailActivity.ACTION_ADD_NEW_CHAT);
        Bundle b = new Bundle();
        b.putSerializable("chat", chatModel);
        intent.putExtras(b);
        setFlagAndBroadcast(context, intent);
    }

    public static void refreshChatStatus(Context context,Double readTimestamp) {
        Intent intent = new Intent(ChatDetailActivity.ACTION_REFRESH_CHAT_STATUS);
        Bundle bundle=new Bundle();
        bundle.putDouble("readTimestamp",readTimestamp);
        intent.putExtras(bundle);
        setFlagAndBroadcast(context, intent);
    }

    public static void refreshChatList(Context context) {
        Intent intent =  new Intent(ChatDetailActivity.ACTION_REFRESH_CHAT_LIST);
        context.sendBroadcast(intent);
    }


    private static void setFlagAndBroadcast(Context context, Intent intent){
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.sendBroadcast(intent);
    }
}
