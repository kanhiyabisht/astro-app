package com.example.astrodashalib.service.faye;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by himanshu on 05/07/16.
 */
public class FayeIntentService extends IntentService {


    public static void startFayeService(Context ctx) {
        Intent myIntent = new Intent(ctx, FayeIntentService.class);
        ctx.startService(myIntent);
    }

    public FayeIntentService() {
        super("FayeIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            FayeService.startAction(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}