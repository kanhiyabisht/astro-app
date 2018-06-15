package com.example.astrodashalib.generic;

import android.content.Context;


import com.example.astrodashalib.data.models.ChatModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by himanshu on 25/07/16.
 */
public class GenericController implements GenericControllerInterface {

    private static GenericController genericController;
    GenericDataAccessInterface localChatInterface;
    GenericDataAccessInterface fayeInterface;
    Context context;

    public static GenericController getInstance(Context context) {
        return genericController = (genericController == null) ? new GenericController(context) : genericController;
    }

    private GenericController(Context context) {
        this.context = context;
        this.localChatInterface = LocalDbFactory.getGenericDataAccessObj(ChatModel.class,context);
        this.fayeInterface = FayeFactory.getGenericDataAccessObj(context);
    }

    @Override
    public void addLocalChat(boolean bool, JSONObject obj, GenericCallback genericCallback) {
        localChatInterface.post(bool, obj, genericCallback);
    }

    @Override
    public void serverPost(boolean bool, JSONObject obj, GenericCallback genericCallback) {
        if (fayeInterface == null)
            this.fayeInterface = FayeFactory.getGenericDataAccessObj(context);
        fayeInterface.post(bool, obj, genericCallback);
    }

    @Override
    public void serverPut(boolean bool, JSONObject obj, GenericCallback genericCallback) {
        if (fayeInterface == null)
            this.fayeInterface = FayeFactory.getGenericDataAccessObj(context);
        fayeInterface.put(bool, obj, new JSONObject(), genericCallback);
    }

    @Override
    public void updateLocalChat(JSONArray jsonArray, String type, boolean bool, JSONObject obj, GenericCallback genericCallback) {
        try {
            JSONObject queryObject = new JSONObject();
            queryObject.put("jsonArray", jsonArray);
            queryObject.put("type", type);
            localChatInterface.put(bool, obj, queryObject, genericCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void localChatQuery(JSONArray jsonArray, String type, boolean bool, GenericQueryCallback genericQueryCallback) {
        try {
            JSONObject queryObject = new JSONObject();
            queryObject.put("jsonArray", jsonArray);
            queryObject.put("type", type);
            localChatInterface.query(bool, queryObject, genericQueryCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
