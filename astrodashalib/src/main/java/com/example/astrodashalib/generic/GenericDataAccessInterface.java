package com.example.astrodashalib.generic;


import com.example.astrodashalib.localDB.ModelInterface;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by himanshu on 21/07/16.
 */
public interface GenericDataAccessInterface<T extends ModelInterface> {
    void post(boolean bool, JSONObject obj, GenericCallback genericCallback);

    void put(boolean bool, JSONObject obj, JSONObject queryJsonObj, GenericCallback genericCallback);

    void get(boolean bool, GenericCallback genericCallback);

    void query(boolean bool, JSONObject JsonObj, GenericQueryCallback genericQueryCallback);

    void delete(boolean bool, GenericCallback genericCallback);

    void fetchAll(boolean bool, GenericQueryCallback genericQueryCallback);

    void sendCallback(JSONObject errorObj, T t);

    void sendCallback(JSONObject errorObj, ArrayList<T> t);

}
