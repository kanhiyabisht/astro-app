package com.example.astrodashalib.generic;

import org.json.JSONObject;

/**
 * Created by abhishek on 21/12/15.
 */
public interface GenericCallback {

    void callback(JSONObject error, Object object);
}
