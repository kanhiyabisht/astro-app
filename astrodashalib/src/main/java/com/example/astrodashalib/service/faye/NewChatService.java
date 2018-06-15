package com.example.astrodashalib.service.faye;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;


import com.example.astrodashalib.chat.ChatCommonMethods;
import com.example.astrodashalib.chat.SaveImageInterface;
import com.example.astrodashalib.chat.messageController.MessageControllerFactory;
import com.example.astrodashalib.chat.messageController.MessageControllerInterface;
import com.example.astrodashalib.chat.notificationHandler.ChatNotificationHelper;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.helper.BroadcastUtils;
import com.example.astrodashalib.helper.DateTimeUtil;
import com.example.astrodashalib.helper.PrefGetter;
import com.example.astrodashalib.localDB.DbConstants;
import com.example.astrodashalib.provider.rest.RestProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by himanshu on 10/06/16.
 */
public class NewChatService extends IntentService implements SaveImageInterface {

    public static final String IS_FROM_SCHEDULER = "isFromScheduler";
    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    String myId;
    Context ctx;
    public String TAG = "Poolmyride Refresh Chat";
    int count1 = 0;

    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

    public HashMap<String, ArrayList<ChatModel>> chatListHashMap = new HashMap<String, ArrayList<ChatModel>>();
    MessageControllerInterface messageController;
    boolean isFromScheduler = false;

    public static void startService(Context ctx, boolean isFromScheduler) {
        Intent intent = new Intent(ctx, NewChatService.class);
        intent.putExtra(IS_FROM_SCHEDULER, isFromScheduler);
        ctx.startService(intent);
    }

    public NewChatService() {
        super("NewChatService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            boolean bool = (intent != null) ? getUserModelFromPreferences(intent) : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean getUserModelFromPreferences(Intent intent) {
        ctx = this;
        isFromScheduler = intent.getBooleanExtra(IS_FROM_SCHEDULER, false);
        if (!PrefGetter.getUserId(ctx.getApplicationContext()).equals("-1")) {
            messageController = MessageControllerFactory.getInstance(ctx);
            fetchLastChatTimestampFromPreferences();
        }
        return false;
    }


    public boolean fetchLastChatTimestampFromPreferences() {
        try {
            myId = PrefGetter.getUserId(ctx.getApplicationContext());
            String lastChatTimestamp = PrefGetter.getLastChatTimestamp(ctx.getApplicationContext());
            fetchChatFromServer(lastChatTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public void fetchChatFromServer(String lastChatTimestamp) {
        if (lastChatTimestamp.equals("0"))
            downloadAllChats();
        else
            downloadChatByTimestamp(lastChatTimestamp);
    }

    public void downloadAllChats() {
        mCompositeSubscription.add(RestProvider.getDownloadChatService().downloadAllChats(myId, PrefGetter.getFayeToken(ctx.getApplicationContext()))
                .subscribe(new Subscriber<ArrayList<ChatModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ChatModel> chatModelList) {
                        try {
                            if (chatModelList.size() > 0)
                                startCreatingChatListForDb(chatModelList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    public void downloadChatByTimestamp(String lastChatTimestamp) {
        mCompositeSubscription.add(RestProvider.getDownloadChatService().downloadChatsByTimestamp(myId, lastChatTimestamp, PrefGetter.getFayeToken(ctx.getApplicationContext()))
                .subscribe(new Subscriber<ArrayList<ChatModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ChatModel> chatModelList) {
                        try {
                            if (chatModelList.size() > 0)
                                startCreatingChatListForDb(chatModelList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));

    }

    protected void startCreatingChatListForDb(ArrayList list) {
        chatModelArrayList = new ArrayList<ChatModel>(list);
        sortChatModelList();
        createChatModelList();
        PrefGetter.setLastChatTimestamp(chatModelArrayList.get(chatModelArrayList.size() - 1).sentTimestamp.toString(),ctx.getApplicationContext());
        proceedToAddChatListInDb();
    }

    private void sortChatModelList() {
        Collections.sort(chatModelArrayList, new Comparator<ChatModel>() {
            @Override
            public int compare(ChatModel lhs, ChatModel rhs) {
                return lhs.sentTimestamp < rhs.sentTimestamp ? -1 : 1;
            }
        });
    }


    private void createChatModelList() {
        for (ChatModel chatModel : chatModelArrayList) {
            //for creating chat array list for Db
            chatModel.myId = myId;
            chatModel.fromUserName = "";

            chatModel = (chatModel.fromUserId.equalsIgnoreCase(myId)) ? setIncomingAndBuddyId(false, chatModel.toUserId, chatModel)
                    : setIncomingAndBuddyId(true, chatModel.fromUserId, chatModel);

            int chatStatus = isChatRecieved(chatModel) ? getChatStatus(chatModel, DbConstants.STATUS_NOTIFY, DbConstants.STATUS_READ)
                    : getChatStatus(chatModel, DbConstants.STATUS_UNREAD, DbConstants.STATUS_READ);

            chatModel.chatStatus = chatStatus;
            chatModel.deliveredTimestamp = isChatStatusNotify(chatModel) ? Double.valueOf(DateTimeUtil.getCurrentTimestampSeconds()) : chatModel.deliveredTimestamp;
            String buddyId = chatModel.buddyId;
            ArrayList<ChatModel> chatModelList = new ArrayList<ChatModel>();
            if (chatListHashMap.containsKey(buddyId))
                chatModelList = chatListHashMap.get(buddyId);
            chatModelList.add(chatModel);
            chatListHashMap.put(buddyId, chatModelList);
        }
    }

    private ChatModel setIncomingAndBuddyId(boolean isIncoming, String buddyId, ChatModel chatModel) {
        chatModel.isIncoming = isIncoming;
        chatModel.buddyId = buddyId;
        return chatModel;
    }

    private Boolean isChatRecieved(ChatModel chatModel) {
        return chatModel.isIncoming;
    }

    private int getChatStatus(ChatModel chatModel, int status1, int status2) {
        int chatStatus;
        chatStatus = (chatModel.readTimestamp == 0) ? status1 : status2;
        return chatStatus;
    }

    private boolean isChatStatusNotify(ChatModel chatModel) {
        return chatModel.chatStatus == DbConstants.STATUS_NOTIFY;
    }

    public void proceedToAddChatListInDb() {
        try {
            count1 = 0;
            Iterator it = chatListHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ArrayList<ChatModel> chatModelList = (ArrayList<ChatModel>) pair.getValue();
                ListIterator listIterator = chatModelList.listIterator();
                while (listIterator.hasNext()) {
                    ChatModel chatModel = (ChatModel) listIterator.next();
                    if (!ChatNotificationHelper.isNewChat(chatModel.localId))
                        listIterator.remove();
                    else
                        proceedToAddChatInDb(chatModel);
                }
                if (chatModelList.isEmpty())
                    it.remove();

                if (!it.hasNext())
                    BroadcastUtils.refreshChatStatus(ctx, 1d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void proceedToAddChatInDb(ChatModel chatModel) {
        if (ChatCommonMethods.isChatScreenOpen(chatModel.buddyId))
            sendBroadcastAndAddChatInDb(chatModel);
        else
            addChatInDb(chatModel);
    }

    protected void sendBroadcastAndAddChatInDb(ChatModel chatModel) {
        if (chatModel.isIncoming) {
            chatModel.chatStatus = DbConstants.STATUS_READ;
            chatModel.readTimestamp = Double.valueOf(DateTimeUtil.getCurrentTimestampSeconds());
        }
        addChatInDb(chatModel);
        messageController.sendReadTimestamp(chatModel.myId, chatModel.buddyId);
    }

    protected void addChatInDb(ChatModel chatModel) {
        messageController.addChatLocally(chatModel, new GenericCallback() {
            @Override
            public void callback(JSONObject error, Object model) {
            }
        });
    }


    @Override
    public void imageSaved(Bitmap bm) {

    }

    @Override
    public void imageSaved(Bitmap bm, UserModel model) {
//        if (sendNotification)
//            sendNotification(notificationChatHashMap.get(model.get_id()), model, bm);
    }

    /*@Override
    public PendingIntent getPendingIntent() {
        return contentIntent;
    }*/

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mCompositeSubscription.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

