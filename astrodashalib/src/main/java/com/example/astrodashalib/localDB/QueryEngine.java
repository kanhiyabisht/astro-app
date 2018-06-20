package com.example.astrodashalib.localDB;

import android.util.Log;


import com.example.astrodashalib.data.models.QueryModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by himanshu on 24/06/16.
 */
public class QueryEngine<T extends SqlModelInterface> {
    public QueryEngine() {
    }

    public QueryModel fetchQuery(T t, JSONObject queryObj) {
        QueryModel queryModel = new QueryModel();
        String selectionText = "";
        String limitText = "";
        String orderByText = "";
        int size = 0;
        int count = -1;
        try {
            JSONArray jsonArray = queryObj.optJSONArray("jsonArray");
            String type = queryObj.optString("type");

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    if (jsonObject != null) {
                        if (jsonObject.optString("field").equals(DbConstants.SORT_ASEC))
                            orderByText += "ORDER BY " + jsonObject.optString("value") + " ASC";
                        else if (jsonObject.optString("field").equals(DbConstants.SORT_DSEC))
                            orderByText += "ORDER BY " + jsonObject.optString("value") + " DESC";
                        else if (jsonObject.optString("field").equals(DbConstants.LIMIT))
                            limitText += "LIMIT " + jsonObject.optString("value");
                        else {
                            size++;
                            if (selectionText.isEmpty())
                                selectionText += jsonObject.optString("field") + " ? ";
                            else
                                selectionText += "AND " + jsonObject.optString("field") + " ? ";
                        }
                    }
                }

            }
            String[] paramsArray = new String[size];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                if (jsonObject != null) {
                    if (!jsonObject.optString("field").equals(DbConstants.SORT_ASEC) && !jsonObject.optString("field").equals(DbConstants.SORT_DSEC) && !jsonObject.optString("field").equals(DbConstants.LIMIT))
                        paramsArray[++count] = jsonObject.optString("value");
                }
            }
            String query = "";
            switch (type) {
                case "query":
                    query = selectionText.isEmpty() ? "SELECT * FROM " + t.getTableName() : "SELECT * FROM " + t.getTableName() + " WHERE " + selectionText;
                    query = orderByText.isEmpty() ? query : query + orderByText;
                    query = limitText.isEmpty() ? query : query + " " + limitText;
                    break;

            }
            Log.e("QueryEngine", "query " + query);
            Log.e("QueryEngine", "params " + paramsArray);
            Log.e("QueryEngine", "selection " + selectionText);
            queryModel.query = query;
            queryModel.whereValues = paramsArray;
            queryModel.whereClause = selectionText;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("QueryEngine", e.toString());
        }
        return queryModel;
    }

}
