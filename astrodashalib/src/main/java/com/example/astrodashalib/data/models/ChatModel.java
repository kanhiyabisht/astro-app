package com.example.astrodashalib.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;


import com.google.gson.annotations.SerializedName;
import com.example.astrodashalib.Constant;
import com.example.astrodashalib.localDB.DatabaseHelper;
import com.example.astrodashalib.localDB.DbConstants;
import com.example.astrodashalib.localDB.ModelInterface;
import com.example.astrodashalib.localDB.SqlModelInterface;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatModel implements Serializable, ModelInterface, SqlModelInterface {


    @SerializedName("local_id")
    public String localId = null;

    @SerializedName("from_user_id")
    public String fromUserId = null;

    @SerializedName("from_user_name")
    public String fromUserName = null;

    @SerializedName("value")
    public String value = null;

    @SerializedName("to_user_id")
    public String toUserId = null;

    @SerializedName("sent_timestamp")
    public Double sentTimestamp = null;

    @SerializedName("type")
    public String type = "chat";

    @SerializedName("read_timestamp")
    public Double readTimestamp = null;

    @SerializedName("delivered_timestamp")
    public Double deliveredTimestamp = null;
    public Integer chatStatus = null;
    public Boolean isIncoming = null;
    public String myId = null;
    public String buddyId = null;
    public String date = null;


    public static final String LOCAL_ID = "local_id";
    public static final String FROM_USER_ID = "from_user_id";
    public static final String TO_USER_ID = "to_user_id";
    public static final String FROM_USER_NAME = "from_user_name";
    public static final String VALUE = "value";
    public static final String INCOMING = "incoming";
    public static final String SENT_TIMESTAMP = "sent_timestamp";
    public static final String READ_TIMESTAMP = "read_timestamp";
    public static final String DELIVERED_TIMESTAMP = "delivered_timestamp";
    public static final String MY_ID = "my_id";
    public static final String BUDDY_ID = "buddy_id";
    public static final String CHAT_STATUS = "chat_status";
    public static final String TYPE = "type";

    static HashMap<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put(DatabaseHelper.COLUMN_NAME_LOCAL_ID, LOCAL_ID);
        hashMap.put(DatabaseHelper.COLUMN_NAME_FROM_USER_ID, FROM_USER_ID);
        hashMap.put(DatabaseHelper.COLUMN_NAME_TO_USER_ID, TO_USER_ID);
        hashMap.put(DatabaseHelper.COLUMN_NAME_FROM_USER_NAME, FROM_USER_NAME);
        hashMap.put(DatabaseHelper.COLUMN_NAME_MESSAGE, VALUE);
        hashMap.put(DatabaseHelper.COLUMN_NAME_INCOMING, INCOMING);
        hashMap.put(DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP, SENT_TIMESTAMP);
        hashMap.put(DatabaseHelper.COLUMN_NAME_READ_TIMESTAMP, READ_TIMESTAMP);
        hashMap.put(DatabaseHelper.COLUMN_NAME_DELIVERED_TIMESTAMP, DELIVERED_TIMESTAMP);
        hashMap.put(DatabaseHelper.COLUMN_NAME_MY_ID, MY_ID);
        hashMap.put(DatabaseHelper.COLUMN_NAME_BUDDY_ID, BUDDY_ID);
        hashMap.put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS, CHAT_STATUS);
    }

    public ChatModel() {
    }

    public ChatModel(String date) {
        this.date = date;
        type = "chat";
    }

    public ChatModel(JSONObject jsonObject, String sourceType) {
        switch (sourceType) {
            case Constant.GCM_SOURCE:
                setIncomingChatModelFromPayLoad(jsonObject);
                break;
            case Constant.FAYE_SOURCE:
                setIncomingChatModelFromFaye(jsonObject);
                break;
            default:
                setChatModelFromFile(jsonObject);
                break;
        }

    }

    public ChatModel(String value, String toUserId, String fromUserId, String fromUserName, Double sentTimestamp, boolean isIncoming) {
        super();
        this.value = value;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.sentTimestamp = sentTimestamp;
        this.isIncoming = isIncoming;
        this.fromUserName = fromUserName;
        this.type = Constant.CHAT;
        this.localId = toUserId + sentTimestamp;
    }

    @Override
    public JSONObject toJson() {
        JSONObject chatJsonObj = new JSONObject();
        try {
            chatJsonObj.put(TO_USER_ID, toUserId);
            chatJsonObj.put(FROM_USER_ID, fromUserId);
            chatJsonObj.put(FROM_USER_NAME, fromUserName);
            chatJsonObj.put(TYPE, type);
            chatJsonObj.put(VALUE, value);
            chatJsonObj.put(SENT_TIMESTAMP, sentTimestamp);
            chatJsonObj.put(LOCAL_ID, localId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatJsonObj;
    }

    @Override
    public JSONObject toJsonDb() {
        JSONObject chatJsonObj = new JSONObject();
        try {
            chatJsonObj.put(TO_USER_ID, toUserId);
            chatJsonObj.put(FROM_USER_ID, fromUserId);
            chatJsonObj.put(FROM_USER_NAME, fromUserName);
            chatJsonObj.put(TYPE, type);
            chatJsonObj.put(VALUE, value);
            chatJsonObj.put(SENT_TIMESTAMP, sentTimestamp);
            chatJsonObj.put(LOCAL_ID, localId);
            chatJsonObj.put(MY_ID, myId);
            chatJsonObj.put(BUDDY_ID, this.buddyId);
            chatJsonObj.put(CHAT_STATUS, chatStatus);
            chatJsonObj.put(DELIVERED_TIMESTAMP, deliveredTimestamp);
            chatJsonObj.put(READ_TIMESTAMP, readTimestamp);
            chatJsonObj.put(INCOMING, this.isIncoming ? DbConstants.STATUS_RECIEVED : DbConstants.STATUS_SEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatJsonObj;
    }

    @Override
    public ModelInterface fromJson(JSONObject obj) {
        setChatModel(obj);
        return this;
    }

    public void setChatModel(JSONObject obj) {
        try {
            this.localId = obj.optString(LOCAL_ID);
            this.toUserId = obj.optString(TO_USER_ID);
            this.fromUserId = obj.optString(FROM_USER_ID);
            this.sentTimestamp  = obj.optDouble(SENT_TIMESTAMP);
            this.value = obj.optString(VALUE);
            this.type = obj.optString(TYPE);
            this.deliveredTimestamp = obj.optDouble(DELIVERED_TIMESTAMP);
            this.readTimestamp = obj.optDouble(READ_TIMESTAMP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChatModelFromFile(JSONObject jsonObj) {
        try {
            this.localId = jsonObj.optString(TO_USER_ID) + jsonObj.optLong("timestamp");
            this.toUserId = jsonObj.optString(TO_USER_ID);
            this.fromUserId = jsonObj.optString("user_id");
            this.sentTimestamp = jsonObj.optDouble("timestamp");
            this.value = jsonObj.optString("data");
            this.type = jsonObj.optString("type");
            this.fromUserName = jsonObj.optString("user_name");
            this.myId = jsonObj.optString(TO_USER_ID);
            this.buddyId = jsonObj.optString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setIncomingChatModelFromFaye(JSONObject obj) {
        try {
            this.localId = obj.optString(LOCAL_ID);
            this.toUserId = obj.optString(TO_USER_ID);
            this.fromUserId = obj.optString(FROM_USER_ID);
            this.sentTimestamp = obj.optDouble(SENT_TIMESTAMP);
            this.value = obj.optString(VALUE);
            this.type = obj.optString(TYPE);
            this.fromUserName = obj.optString(FROM_USER_NAME);
            this.myId = obj.optString(TO_USER_ID);
            this.buddyId = obj.optString(FROM_USER_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean setIncomingChatModelFromPayLoad(JSONObject jsonObj) {
        try {
            this.localId = jsonObj.optString(TO_USER_ID) + jsonObj.optLong("timestamp");
            this.toUserId = jsonObj.optString(TO_USER_ID);
            this.fromUserId = jsonObj.optString("user_id");
            this.sentTimestamp = jsonObj.optDouble("timestamp");
            this.value = jsonObj.optString("original_message");
            this.type = jsonObj.optString("type");
            this.fromUserName = jsonObj.optJSONObject("user_object").optString("name");
            this.myId = jsonObj.optString(TO_USER_ID);
            this.buddyId = jsonObj.optString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @NonNull
    public static ChatModel getWelcomeChatModel(Double timestamp, String loginUserId) {
        try {
            ChatModel chatModel = new ChatModel(Constant.WELCOME_MESSAGE, loginUserId, ChatDetailActivity.chatUserId, "system", timestamp, true);
            chatModel.myId = loginUserId;
            chatModel.buddyId = "systemId";
            chatModel.chatStatus = DbConstants.STATUS_READ;
            chatModel.readTimestamp = timestamp;
            chatModel.deliveredTimestamp = timestamp;
            return chatModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static ChatModel getLoginChatModel(Double timestamp, String loginUserId) {
        try {
            ChatModel chatModel = new ChatModel(Constant.LOGIN_MEESAGE, loginUserId, ChatDetailActivity.chatUserId, "system", timestamp, true);
            chatModel.myId = loginUserId;
            chatModel.buddyId = "systemId";
            chatModel.chatStatus = DbConstants.STATUS_READ;
            chatModel.readTimestamp = timestamp;
            chatModel.deliveredTimestamp = timestamp;
            return chatModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ContentValues getContentValues(JSONObject jsonObject) {
        ContentValues values = new ContentValues();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (jsonObject.has(String.valueOf(pair.getValue())))
                values.put(String.valueOf(pair.getKey()), jsonObject.optString(String.valueOf(pair.getValue())));
        }
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.TABLE_CHAT;
    }

    @Override
    public String[] getFields() {
        return new String[]{DatabaseHelper.COLUMN_NAME_LOCAL_ID, DatabaseHelper.COLUMN_NAME_FROM_USER_ID, DatabaseHelper.COLUMN_NAME_TO_USER_ID,
                DatabaseHelper.COLUMN_NAME_FROM_USER_NAME, DatabaseHelper.COLUMN_NAME_MESSAGE, DatabaseHelper.COLUMN_NAME_INCOMING,
                DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP, DatabaseHelper.COLUMN_NAME_READ_TIMESTAMP, DatabaseHelper.COLUMN_NAME_MY_ID,
                DatabaseHelper.COLUMN_NAME_BUDDY_ID, DatabaseHelper.COLUMN_NAME_CHAT_STATUS, DatabaseHelper.COLUMN_NAME_DELIVERED_TIMESTAMP};
    }

    @Override
    public void setValues(Cursor cursor) {
        this.localId = cursor.getString(0);
        this.fromUserId = cursor.getString(1);
        this.toUserId = cursor.getString(2);
        this.fromUserName = cursor.getString(3);
        this.value = cursor.getString(4);
        this.isIncoming = cursor.getInt(5) == DbConstants.STATUS_RECIEVED;
        this.buddyId = cursor.getString(6);
        this.myId = cursor.getString(7);
        this.chatStatus = cursor.getInt(8);
        this.readTimestamp = cursor.getDouble(9);
        this.deliveredTimestamp = cursor.getDouble(10);
        this.sentTimestamp = cursor.getDouble(11);

    }

    @Override
    public String getColumnName() {
        return DatabaseHelper.COLUMN_NAME_LOCAL_ID;
    }
}
