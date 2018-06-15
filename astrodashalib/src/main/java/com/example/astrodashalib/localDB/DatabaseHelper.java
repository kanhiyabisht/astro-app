package com.example.astrodashalib.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abhishek on 23/06/16.
 */
public class DatabaseHelper {

    public static String COLUMN_NAME_LOCAL_ID = "local_id";
    public static String COLUMN_NAME_FROM_USER_ID = "from_user_id";
    public static String COLUMN_NAME_TO_USER_ID = "to_user_id";
    public static String COLUMN_NAME_FROM_USER_NAME = "from_user_name";
    public static String COLUMN_NAME_MESSAGE = "message";
    public static String COLUMN_NAME_SENT_TIMESTAMP = " timestamp";
    public static String COLUMN_NAME_READ_TIMESTAMP = "read_timestamp";
    public static String COLUMN_NAME_DELIVERED_TIMESTAMP = "delivered_timestamp";
    public static String COLUMN_NAME_INCOMING = "incoming";
    public static String COLUMN_NAME_MY_ID = "my_id";
    public static String COLUMN_NAME_BUDDY_ID = "buddy_id";
    public static String COLUMN_NAME_CHAT_STATUS = "chat_status";

    public static String TABLE_CHAT = "chats";

    private static String SQL_CREATE_CHAT = "CREATE TABLE " + TABLE_CHAT + " (" +
            COLUMN_NAME_LOCAL_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME_FROM_USER_ID + " TEXT NOT NULL," +
            COLUMN_NAME_TO_USER_ID + " TEXT NOT NULL," +
            COLUMN_NAME_FROM_USER_NAME + " TEXT NOT NULL," +
            COLUMN_NAME_MESSAGE + " TEXT NOT NULL," +
            COLUMN_NAME_INCOMING + " INTEGER NOT NULL," +
            COLUMN_NAME_BUDDY_ID + " TEXT NOT NULL," +
            COLUMN_NAME_MY_ID + " TEXT NOT NULL," +
            COLUMN_NAME_CHAT_STATUS + " INTEGER NOT NULL," +
            COLUMN_NAME_READ_TIMESTAMP + " REAL NOT NULL," +
            COLUMN_NAME_DELIVERED_TIMESTAMP + " REAL NOT NULL," +
            COLUMN_NAME_SENT_TIMESTAMP + " REAL NOT NULL)";



    private static final String SQL_DELETE_ENTRIES_CHAT =
            "DROP TABLE IF EXISTS " + TABLE_CHAT;



    Context context;
    MySqlLiteOpenHelper mySqlLiteOpenHelper;

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getInstance(Context context) {
        return databaseHelper = (databaseHelper == null) ? new DatabaseHelper(context) : databaseHelper;
    }

    private DatabaseHelper(Context ctx) {
        this.context = ctx;
        this.mySqlLiteOpenHelper = new MySqlLiteOpenHelper(ctx);
    }


    public class MySqlLiteOpenHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "astroTML";
        public static final int VERSION = 1;

        public MySqlLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_CHAT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES_CHAT);
            onCreate(sqLiteDatabase);
        }
    }
}
