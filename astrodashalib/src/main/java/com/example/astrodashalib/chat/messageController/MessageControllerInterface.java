package com.example.astrodashalib.chat.messageController;



import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.generic.GenericQueryCallback;

import org.json.JSONArray;

/**
 * Created by himanshu on 16/09/16.
 */
public interface MessageControllerInterface {

    void postChatOnServer(ChatModel chatModel, GenericCallback genericCallback);

    ChatModel saveChat(String sendData, String chatUserId, String chatUserImage, String chatUserName);

    void sendChat(ChatModel chatModel);

    void sendChat(String sendData, String chatUserId, String chatUserImage, String chatUserName);

    void addChatLocally(ChatModel chatModel, GenericCallback genericCallback);

    void updateChatLocally(JSONArray jsonArray, ChatModel chatModel, GenericCallback genericCallback);

    void localChatQuery(JSONArray jsonArray, GenericQueryCallback genericQueryCallback);

    void sendReadTimestamp(String loginUserId, String chatUserId);

    void updateRecievedChatStatus(ChatModel chatModel, String loginUserId, String chatUserId, GenericCallback genericCallback);

    void fetchChatsOfTwoUser(String loginUserId, String chatUserId, GenericQueryCallback genericQueryCallback);

    void fetchAllChatsToSend(String loginUserId, GenericQueryCallback genericQueryCallback);

    void fetchUnreadRecievedChats(ChatModel chatModel, GenericQueryCallback genericQueryCallback);

    void fetchChatsByTimestamp(String loginUserId, long timestamp, GenericQueryCallback genericQueryCallback);

    void getReadTimestamp(String readerId, String WriterId, GenericCallback callback);

    void updateReadTimestampOfSentChats(double readTimestamp, String buddyId, String myId, GenericCallback genericCallback);

    void updateChatStatusOfSentChats(double sentTimestamp, String buddyId, String myId, GenericCallback genericCallback);

    void updateChatStatusOfSentChats(double sentTimestamp, String myId, GenericCallback genericCallback);
}
