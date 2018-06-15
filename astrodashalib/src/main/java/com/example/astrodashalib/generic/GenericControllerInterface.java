package com.example.astrodashalib.generic;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by himanshu on 01/08/16.
 */
public interface GenericControllerInterface {

    void addLocalChat(boolean bool, JSONObject obj, GenericCallback genericCallback);
    void serverPost(boolean bool, JSONObject obj, GenericCallback genericCallback);
    void serverPut(boolean bool, JSONObject obj, GenericCallback genericCallback);
    void updateLocalChat(JSONArray jsonArray, String type, boolean bool, JSONObject obj, GenericCallback genericCallback);
    void localChatQuery(JSONArray jsonArray, String type, boolean bool, GenericQueryCallback genericQueryCallback);

}
