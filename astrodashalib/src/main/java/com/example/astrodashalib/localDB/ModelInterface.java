package com.example.astrodashalib.localDB;

import org.json.JSONObject;

/**
 * Created by abhishek on 11/12/15.
 */
public interface ModelInterface<T> {

    JSONObject toJson();
    ModelInterface fromJson(JSONObject obj);
}
