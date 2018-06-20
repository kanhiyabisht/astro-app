package com.example.astrodashalib.view.modules.chat;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by himanshu on 15/09/16.
 */
public class NetworkChange implements ChatBroadcastInterface {
    public NetworkChange() {
    }

    @Override
    public void exec(ChatDetailActivity activity, Context ctx, Bundle b) {
        boolean isNetworkConnected = b.getBoolean("isNetworkConnected",true);
        if(isNetworkConnected)
            activity.getReadTimestamp();

    }
}
