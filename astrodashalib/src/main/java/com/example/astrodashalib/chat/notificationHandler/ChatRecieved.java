package com.example.astrodashalib.chat.notificationHandler;

import android.content.Context;


import com.example.astrodashalib.R;
import com.example.astrodashalib.chat.ChatCommonMethods;
import com.example.astrodashalib.chat.messageController.MessageControllerFactory;
import com.example.astrodashalib.chat.messageController.MessageControllerInterface;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.generic.GenericQueryCallback;
import com.example.astrodashalib.helper.PrefGetter;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by himanshu on 14/06/16.
 */
public class ChatRecieved implements LocalIdCommandInterface {
    Context context;
    HashMap<Boolean, ChatScreenCommandInterface> chatScreenHashMap;
    MessageControllerInterface messageController;
    static HashSet<String> localIdList = new HashSet<>();

    public ChatRecieved(Context context) {
        this.context =context;
        chatScreenHashMap = new HashMap<>();
        chatScreenHashMap.put(true, new ChatScreenOpen(context));
        chatScreenHashMap.put(false, new ChatScreenClose(context));
        messageController = MessageControllerFactory.getInstance(context);
    }

    @Override
    public void execute(final ChatModel chatModel, JSONObject chatJsonObj) {
        try {
            if (localIdList.add(chatModel.localId))
                proceedForUpdatingDbAndView(chatModel, chatJsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void proceedForUpdatingDbAndView(ChatModel chatModel, JSONObject chatJsonObj) {
        saveChatInDb(chatModel);
        PrefGetter.setLastChatTimestamp(getLastTimestamp(chatJsonObj),context.getApplicationContext());
    }

    protected void saveChatInDb(final ChatModel chatModel) {
        messageController.addChatLocally(chatModel, new GenericCallback() {
            @Override
            public void callback(JSONObject error, Object model) {
                final String buddyId = chatModel.buddyId;
                chatScreenHashMap.get(ChatCommonMethods.isChatScreenOpen(buddyId)).execute(chatModel,  messageController);

                /*chatBuddyHashMap.put(buddyId, buddyUsermodel);
                Analytics.sendNotificationEvents(R.string.label_chat_user_model_success, context);
                chatScreenHashMap.get(ChatCommonMethods.isChatScreenOpen(buddyId)).execute(chatModel, buddyUsermodel, messageController);*/
            }
        });
    }

    private String getLastTimestamp(JSONObject chatJsonObj) {
        return ChatCommonMethods.isMessageFromFaye(chatJsonObj) ? getTimestampFromFaye(chatJsonObj) : getTimestampFromGCM(chatJsonObj);
    }

    private String getTimestampFromGCM(JSONObject chatJsonObj) {
        return chatJsonObj.optString("timestamp");
    }

    private String getTimestampFromFaye(JSONObject chatJsonObj) {
        return chatJsonObj.optString("sent_timestamp");

    }
}
