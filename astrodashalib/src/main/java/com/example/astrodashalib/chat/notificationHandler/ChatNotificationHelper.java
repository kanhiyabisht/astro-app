package com.example.astrodashalib.chat.notificationHandler;

import android.content.Context;

import com.example.astrodashalib.Constant;
import com.example.astrodashalib.chat.ChatCommonMethods;
import com.example.astrodashalib.chat.ChatNotificationInterface;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.helper.DateTimeUtil;
import com.example.astrodashalib.localDB.DbConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

import static com.example.astrodashalib.helper.PrefGetter.getUserId;

/**
 * Created by himanshu on 13/06/16.
 */
public class ChatNotificationHelper implements ChatNotificationInterface {

    public static HashSet<String> localIdList = new HashSet<>();
    Context ctx;
    ChatModel chatModel;
    JSONObject messageObj;
    String buddyId;
    String msgSource;
    String  logInUserId;
    String myId;
    HashMap<Boolean, LocalIdCommandInterface> localIdHashMap;

    public ChatNotificationHelper(Context context) {
        this.ctx = context;
        logInUserId = getUserId(context.getApplicationContext());
        localIdHashMap = new HashMap<>();
        localIdHashMap.put(true, new ChatRecieved(ctx));
        localIdHashMap.put(false, new NullClass());
    }

    @Override
    public void execute(Object object) {
        try {
            JSONObject obj= (JSONObject) object;
            this.messageObj = obj;
            myId = obj.getString("to_user_id");
            boolean bool = (ChatCommonMethods.isMessageFromFaye(obj)) ? setVariablesFromFayePayload(obj) : setVariablesFromGcmPayload(obj);

            if (isValidChat(obj, myId)) {
                setChatModel(obj, msgSource);
                validateLocalId(chatModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected boolean setVariablesFromGcmPayload(JSONObject obj) throws JSONException {
        msgSource = Constant.GCM_SOURCE;
        buddyId = obj.getString("user_id");
//        Analytics.sendNotificationEvents(R.string.label_chat_from_gcm,ctx);
        return false;
    }

    protected boolean setVariablesFromFayePayload(JSONObject obj) throws JSONException {
        msgSource = Constant.FAYE_SOURCE;
        buddyId = obj.getString("from_user_id");
//        Analytics.sendNotificationEvents(R.string.label_chat_from_faye,ctx);
        return true;
    }

    boolean isValidChat(JSONObject payloadObj,String myId) {
        return myId.equals(logInUserId) && payloadObj.optString("type").equals(Constant.CHAT);
    }

    void setChatModel(JSONObject obj, String msgSource) {
        this.chatModel = new ChatModel(obj, msgSource);
        chatModel.isIncoming = true;
        chatModel.chatStatus = DbConstants.STATUS_NOTIFY;
        chatModel.readTimestamp = 0d;
        chatModel.deliveredTimestamp = Double.valueOf(DateTimeUtil.getCurrentTimestampSeconds());
    }

    public void validateLocalId(ChatModel chatModel) {
        try {
            String localId = chatModel.localId;
            boolean bool = isNewChat(localId);
            localIdHashMap.get(bool).execute(chatModel, messageObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNewChat(String localId) {
        return localIdList.add(localId);
    }
}
