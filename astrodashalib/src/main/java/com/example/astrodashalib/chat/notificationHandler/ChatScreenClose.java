package com.example.astrodashalib.chat.notificationHandler;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.astrodashalib.Constant;
import com.example.astrodashalib.R;
import com.example.astrodashalib.chat.SaveImageInterface;
import com.example.astrodashalib.chat.messageController.MessageControllerInterface;
import com.example.astrodashalib.data.models.ChatModel;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.generic.GenericQueryCallback;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by himanshu on 14/06/16.
 */
public class ChatScreenClose implements ChatScreenCommandInterface, SaveImageInterface {/*, NotificationHelper*/
    Context context;


    public ChatScreenClose(Context context) {
        this.context = context;
    }

    @Override
    public void execute(ChatModel chatModel, MessageControllerInterface messageController) {
        try {
            fetchChatBetweenUsersAndNotify(chatModel, messageController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchChatBetweenUsersAndNotify(final ChatModel chatModel, final MessageControllerInterface messageController) {
        messageController.fetchUnreadRecievedChats(chatModel, new GenericQueryCallback() {

            @Override
            public void callback(JSONObject error, ArrayList list) {
                if (list.size() > 0)
                    proceedUpdatingDbAndScreens(list);
            }

            protected void proceedUpdatingDbAndScreens(ArrayList<ChatModel> chatList) {
                try {
                    String body = "";
                    for (ChatModel chatModel : chatList) {
                        body = body.trim().length() == 0 ? chatModel.value : body + "\n" + chatModel.value;
                    }
                    String title = "Disciple ("+chatList.size()+" new message) ";
                    Intent intent = ChatDetailActivity.createIntent(context);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.notification_icon)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentIntent(pendingIntent);

                    NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel(mNotifyMgr,mBuilder);
                    }

                    mNotifyMgr.notify(Constant.CHAT_NOTIFICATION_ID, mBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private void createNotificationChannel(NotificationManager mNotifyMgr,NotificationCompat.Builder mBuilder) {
        String CHANNEL_ID = "channel_01";
        CharSequence name = "New Message";
        String description = "Unread Message";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        mNotifyMgr.createNotificationChannel(channel);
        mBuilder.setChannelId(CHANNEL_ID);
    }




    /*private void refreshChatListScreen(StatusModel statusModel, ChatUser chatUser) {
        if (statusModel.statusId != -1 && statusModel.errorMesssage.isEmpty())
            BroadcastUtils.updateChatUserList(chatUser, context);
        else
            Toast.makeText(context, context.getString(R.string.chat_not_saved), Toast.LENGTH_SHORT).show();
    }

    public void sendNotification(UserModel chatUserModel, Bitmap bm) {
        try {

            ArrayList<ChatModel> chatsModelList = new ArrayList<>();
            chatsModelList.addAll(chatHashMap.get(chatUserModel.get_id()));
            String username = chatUserModel.getUserName();
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("isNewMessage",true);
            contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            int unreadMessageCount = chatsModelList.size();
            String body = "";
            for (ChatModel chatModel : chatsModelList) {
                body = body.trim().length() == 0 ? chatModel.value : body + "\n" + chatModel.value;
            }
            Intent intent = new Intent(context, DismissChatNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), Constant.DELETE_PENDING_INTENT_NOTIFICATION_ID, intent, 0);
            NotificationUtil notificationUtil = new NotificationUtil(this, context, username, body);
            notificationUtil.setUnreadMessageCount(unreadMessageCount);
            notificationUtil.setDeleteIntent(pendingIntent);
            notificationUtil.setChatBuddy(chatUserModel);
            notificationUtil.setPriorityHigh();
            notificationUtil.setParticipantForAutoNotification(chatUserModel.getUserName());


            setNotificationActionList(chatUserModel, notificationUtil);

            notificationUtil.setLargeIcon(bm);
            notificationUtil.sendNotification(CommonMethods.getChatUserNotificationId(chatUserModel.get_id()), R.string.label_chat_notification);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNotificationActionList(UserModel chatBuddy, NotificationUtil notificationUtil) {
        ArrayList<NotificationCompat.Action> actions = new ArrayList<>();
        if (chatBuddy.getContact() != null && !chatBuddy.getContact().isEmpty())
            actions.add(new NotificationCompat.Action(R.drawable.notification_call, context.getString(R.string.action_call), getCallIntent(chatBuddy)));
        actions.add(new NotificationCompat.Action(R.drawable.notification_chat, context.getString(R.string.chat), getChatIntent(chatBuddy)));
        notificationUtil.setActionList(actions);
    }

    PendingIntent getCallIntent(UserModel chatBuddy) {
        String uri = "tel:" + chatBuddy.getContact();
        Intent callingIntent = new Intent(Intent.ACTION_CALL);
        callingIntent.setData(Uri.parse(uri));
        return PendingIntent.getActivity(context, CommonMethods.getRandomIdForNotification(), callingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    PendingIntent getChatIntent(UserModel chatBuddy) {
        Intent chattingIntent = new Intent(context, ChatDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatUserId", chatBuddy.get_id());
        bundle.putString("chatUserImage", chatBuddy.getImage());
        bundle.putString("chatUserName", chatBuddy.getUserName());
        chattingIntent.putExtras(bundle);
        return PendingIntent.getActivity(context, CommonMethods.getRandomIdForNotification(), chattingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public PendingIntent getPendingIntent() {
        return contentIntent;
    }*/

    @Override
    public void imageSaved(Bitmap bm) {

    }

    @Override
    public void imageSaved(Bitmap bm, UserModel model) {
        /*sendNotification(model, bm);*/
    }
}
