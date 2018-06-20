package com.example.astrodashalib.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;


import com.example.astrodashalib.data.models.DbStatusModel;
import com.example.astrodashalib.data.models.QueryModel;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.generic.GenericDataAccessInterface;
import com.example.astrodashalib.generic.GenericQueryCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by himanshu on 20/07/16.
 */
public class DbController<T extends SqlModelInterface> implements GenericDataAccessInterface {

    GenericCallback genericCallback;
    GenericQueryCallback genericQueryCallback;
    Class<T> classObj;
    Context ctx;
    private static DatabaseHelper databaseHelper;

    public DbController(Class<T> classObj,Context context) {
        ctx = context.getApplicationContext();
        databaseHelper = DatabaseHelper.getInstance(ctx);
        this.classObj = classObj;
    }

    @Override
    public void post(boolean bool, JSONObject obj, GenericCallback genericCallback) {
        try {
            this.genericCallback = genericCallback;
            new DbPostAsyncTask(databaseHelper.mySqlLiteOpenHelper, obj).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DbPostAsyncTask extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper;
        JSONObject obj;
        long id;
        DbStatusModel statusModel = new DbStatusModel();

        public DbPostAsyncTask(DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper, JSONObject jsonObject) {
            this.mySqlLiteOpenHelper = mySqlLiteOpenHelper;
            obj = jsonObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = this.mySqlLiteOpenHelper.getWritableDatabase();
                T t = classObj.newInstance();
                db.beginTransaction();
                ContentValues contentValues = t.getContentValues(obj);
                String tableName = t.getTableName();
                id = db.insert(tableName, null, contentValues);
                db.setTransactionSuccessful();
                db.endTransaction();
                statusModel.statusId = (int) id;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("insert", e.getLocalizedMessage());

                statusModel.statusId = DbConstants.STATUS_ERROR;
                statusModel.errorMesssage = e.getMessage();
                db.endTransaction();
            } finally {
                db.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            genericCallback.callback(null, statusModel);
        }
    }

    @Override
    public void put(boolean bool, JSONObject obj, JSONObject queryObject, GenericCallback genericCallback) {
        try {
            this.genericCallback = genericCallback;
            new DbPutAsyncTask(databaseHelper.mySqlLiteOpenHelper, obj, queryObject).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DbPutAsyncTask extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper;
        JSONObject obj, queryObj;
        long statusId;
        ContentValues contentValues;
        DbStatusModel statusModel = new DbStatusModel();
        QueryModel queryModel;
        QueryEngine queryEngine = new QueryEngine();

        public DbPutAsyncTask(DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper, JSONObject jsonObject, JSONObject queryObj) {
            this.mySqlLiteOpenHelper = mySqlLiteOpenHelper;
            obj = jsonObject;
            this.queryObj = queryObj;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = this.mySqlLiteOpenHelper.getWritableDatabase();
                T t = classObj.newInstance();
                queryModel = queryEngine.fetchQuery(t, queryObj);
                String tableName = t.getTableName();
                contentValues = t.getContentValues(obj);
                if (queryModel.query.isEmpty() && queryModel.whereClause.isEmpty())
                    statusId = db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                else if (queryModel.whereValues.length > 0 && !queryModel.whereClause.isEmpty() && queryModel.query.isEmpty())
                    statusId = db.update(t.getTableName(), contentValues, queryModel.whereClause, queryModel.whereValues);

                statusModel.statusId = (int) statusId;
            } catch (Exception e) {
                e.printStackTrace();
                statusModel.errorMesssage = e.getMessage();
            } finally {
                db.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            genericCallback.callback(null, statusModel);
        }
    }


    @Override
    public void query(boolean bool, JSONObject JsonObj, GenericQueryCallback genericQueryCallback) {
        try {
            this.genericQueryCallback = genericQueryCallback;
            DbQueryAsyncTask task = new DbQueryAsyncTask(databaseHelper.mySqlLiteOpenHelper, JsonObj);

            task.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DbQueryAsyncTask extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper;
        ArrayList<T> list;
        JSONObject errorObj = new JSONObject();
        QueryModel queryModel;
        QueryEngine queryEngine = new QueryEngine();
        JSONObject queryObj;


        public DbQueryAsyncTask(DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper, JSONObject queryObj) {
            this.mySqlLiteOpenHelper = mySqlLiteOpenHelper;
            this.queryObj = queryObj;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = this.mySqlLiteOpenHelper.getReadableDatabase();
                T t = classObj.newInstance();
                queryModel = queryEngine.fetchQuery(t, queryObj);
                list = new ArrayList<>();
                if (!queryModel.query.isEmpty()) {
                    Cursor cursor = db.rawQuery(queryModel.query, queryModel.whereValues);
                    cursor.moveToFirst();

                    for (int i = 0; i < cursor.getCount(); i++) {
                        T model = classObj.newInstance();
                        model.setValues(cursor);
                        list.add(model);
                        cursor.moveToNext();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

                try {
                    errorObj.put("Error", e.getMessage());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } finally {
                if (db != null)
                    db.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            genericQueryCallback.callback(errorObj, list);
        }
    }

    @Override
    public void fetchAll(boolean bool, GenericQueryCallback genericQueryCallback) {
        try {
            this.genericQueryCallback = genericQueryCallback;
            DbfetchAllAsyncTask task = new DbfetchAllAsyncTask(databaseHelper.mySqlLiteOpenHelper);

            task.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DbfetchAllAsyncTask extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper;
        ArrayList<T> list;
        JSONObject errorObj = new JSONObject();

        public DbfetchAllAsyncTask(DatabaseHelper.MySqlLiteOpenHelper mySqlLiteOpenHelper) {
            this.mySqlLiteOpenHelper = mySqlLiteOpenHelper;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                db = this.mySqlLiteOpenHelper.getReadableDatabase();
                T t = classObj.newInstance();
                Cursor cursor = db.query(t.getTableName(), t.getFields(), null, null, null, null, null);
                cursor.moveToFirst();
                list = new ArrayList<T>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    T model = classObj.newInstance();
                    model.setValues(cursor);
                    list.add(model);
                    cursor.moveToNext();
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    errorObj.put("Error", e.getMessage());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } finally {
                db.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            genericQueryCallback.callback(errorObj, list);
        }
    }

    @Override
    public void delete(boolean bool, GenericCallback genericCallback) {
        //not required
    }

    @Override
    public void get(boolean bool, GenericCallback genericCallback) {
        //not required
    }

    @Override
    public void sendCallback(JSONObject errorObj, ModelInterface modelInterface) {

    }

    @Override
    public void sendCallback(JSONObject errorObj, ArrayList t) {

    }
}
