package com.example.astrodashalib.generic;

import android.content.Context;

/**
 * Created by himanshu on 01/08/16.
 */
public class GenericControllerFactory {

    public static GenericControllerInterface getGenericControllerObj(Context context) {
        return GenericController.getInstance(context);
    }
}
