package com.example.astrodashalib.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;


import com.example.astrodashalib.R;
import com.example.astrodashalib.data.models.UserModel;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by abhishek on 17/01/16.
 */
public class ChatCommonMethods {

    Context ctx;
    private static HashMap<Integer, UserModel> conversationIdBuddyIdMap;

    public ChatCommonMethods(Context ctx) {
        this.ctx = ctx;
    }

    public static boolean isMessageFromFaye(JSONObject jsonObject) {
        return jsonObject.has("local_id");
    }

/*
    public void fetchImage(DisplayImageOptions options, final SaveImageInterface sii, String url) {
        ImageLoader.getInstance().loadImage(url, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                Analytics.sendNotificationEvents(R.string.label_image_loading_fail,ctx);
                sii.imageSaved(null);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                Analytics.sendNotificationEvents(R.string.label_image_loading_success,ctx);
                sii.imageSaved(arg2);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                Analytics.sendNotificationEvents(R.string.label_image_loading_cancel,ctx);
            }
        });
    }


    public void saveImageForChattingAvatar(final UserModel userModel, DisplayImageOptions options, final SaveImageInterface sii) {
        ImageLoader.getInstance().loadImage(userModel.getImage(), options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                sii.imageSaved(null);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                sii.imageSaved(arg2);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                sii.imageSaved(null);
            }
        });
    }*/

    /*public void saveImageForChattingAvatarAndGetUserModel(final UserModel userModel, DisplayImageOptions options, final SaveImageInterface sii) {

//        sii.imageSaved(null, userModel);
        ImageLoader.getInstance().loadImage(userModel.getImage(), options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                Analytics.sendNotificationEvents(R.string.label_chat_user_model_image_fail,ctx);
                sii.imageSaved(null, userModel);
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                Analytics.sendNotificationEvents(R.string.label_chat_user_model_image_success,ctx);
                sii.imageSaved(arg2, userModel);
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                Analytics.sendNotificationEvents(R.string.label_chat_user_model_image_cancel,ctx);
                sii.imageSaved(null, userModel);
            }
        });
    }*/

    public static boolean isChatScreenOpen(String toUserId) {
        return !ChatDetailActivity.inBackground && ChatDetailActivity.chatUserId.equals(toUserId);
    }
}
