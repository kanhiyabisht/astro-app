package com.example.astrodashalib.generic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.astrodashalib.service.faye.FayeIntentService;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;


/**
 * Created by himanshu on 29/09/17.
 */

public class NetworkChangeReciever extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        Intent i = new Intent(ChatDetailActivity.CONNECTIVITY_CHANGE);
        Bundle bundle = new Bundle();
        if (isConnected) {
            fetchDataFromServer(context);
            bundle.putBoolean("isNetworkConnected", true);
        }else
            bundle.putBoolean("isNetworkConnected", false);

        i.putExtras(bundle);
        context.sendBroadcast(i);
    }

    protected void fetchDataFromServer(Context context) {
            FayeIntentService.startFayeService(context);
    }

}
