package com.example.astrodashalib.generic;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhishek on 21/12/15.
 */
public interface GenericQueryCallback<T> {

    void callback(JSONObject error, ArrayList<T> list);
}
