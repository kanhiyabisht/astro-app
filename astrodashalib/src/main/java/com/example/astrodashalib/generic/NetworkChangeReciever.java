package com.example.astrodashalib.generic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.astrodashalib.service.faye.FayeIntentService;


/**
 * Created by himanshu on 29/09/17.
 */

public class NetworkChangeReciever extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected)
            fetchDataFromServer(context);
    }

    protected void fetchDataFromServer(Context context) {
            FayeIntentService.startFayeService(context);
    }

}
