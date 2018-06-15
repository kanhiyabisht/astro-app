package com.example.astrodashalib.generic;



import android.content.Context;

import com.example.astrodashalib.localDB.DbController;
import com.example.astrodashalib.localDB.SqlModelInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by himanshu on 22/07/16.
 */
public class LocalDbFactory {

    public static <T extends SqlModelInterface> GenericDataAccessInterface getGenericDataAccessObj(Class<T> classObj,Context context) {
        return new DbController<>(classObj,context);
    }

    public static JSONArray getJsonArray(HashMap<String, String> hashMap) {
        JSONArray jsonArray = new JSONArray();

        if (!hashMap.isEmpty()) {
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("field", pair.getKey());
                    jsonObject.put("value", pair.getValue());
                    jsonArray.put(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }
}
