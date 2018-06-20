package com.example.astrodashalib.data.models;

import com.example.astrodashalib.localDB.ModelInterface;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by himanshu on 21/07/16.
 */
public class DbStatusModel implements Serializable,ModelInterface {

    public int statusId = 0;
    public String errorMesssage = "";

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public ModelInterface fromJson(JSONObject obj) {
        return null;
    }
}
