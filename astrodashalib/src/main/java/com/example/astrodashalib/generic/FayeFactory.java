package com.example.astrodashalib.generic;

import android.content.Context;

import com.example.astrodashalib.faye.FayeChannelController;


/**
 * Created by himanshu on 25/07/16.
 */
public class FayeFactory {

    public static GenericDataAccessInterface getGenericDataAccessObj(Context context) {
        return FayeChannelController.newInstance(context);
    }
}
