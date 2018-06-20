package com.example.astrodashalib.localDB;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

/**
 * Created by abhishek on 23/06/16.
 */
public interface SqlModelInterface{

    JSONObject toJsonDb();
    ContentValues getContentValues(JSONObject jsonObject);
    String getTableName();
    String[] getFields();
    String getColumnName();
    void setValues(Cursor cursor);
}
