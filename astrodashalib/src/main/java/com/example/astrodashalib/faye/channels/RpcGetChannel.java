package com.example.astrodashalib.faye.channels;

import android.os.Handler;
import android.util.Log;


import com.example.astrodashalib.faye.FayeClient;

import org.json.JSONObject;

import java.net.URI;

/**
 * Created by himanshu on 13/06/16.
 */
public class RpcGetChannel implements FayeClient.FayeListener {
    private static final String LOG_TAG = RpcGetChannel.class.getSimpleName();

    protected FayeClient fayeClient;
    protected JSONObject extensions;
    public static boolean isConnected = false;
    private static RpcGetChannel rpcGetChannel;
    static String loginUserId = "";
    static String token = "";
    public static boolean shouldConnect = false;


    OnConnectedListener onConnectedListener;

    OnDisconnectedListener onDisconnectedListener;

    OnSubscribedListener onSubscribedListener;

    OnSubscribeFailedListener onSubscribeFailedListener;

    OnMessageReceivedListener onMessageReceivedListener;

    private RpcGetChannel(Handler handler, String uri, String channel, JSONObject extensions, String id, String deviceToken) {
        loginUserId = id;
        token = deviceToken;
        this.extensions = extensions;
        fayeClient = new FayeClient(handler, URI.create(uri), channel);
        fayeClient.setFayeListener(this);
    }

    public static RpcGetChannel getInstance(Handler handler, String uri, String channel, JSONObject extensions) {
        String userId = extensions.optString("user_id");
        String deviceToken = extensions.optString("token");
        return rpcGetChannel = (rpcGetChannel == null || !loginUserId.equalsIgnoreCase(userId) || !token.equalsIgnoreCase(deviceToken) || !shouldConnect) ? new RpcGetChannel(handler, uri, channel, extensions, userId, deviceToken) : rpcGetChannel;
    }


    public void connect() {
//        if (isConnected) return;
        shouldConnect = true;
        fayeClient.connectToServer(extensions);
    }

    public void sendMessage(JSONObject jsonMsg) {
        fayeClient.sendMessage(jsonMsg);
    }

    public void unsubscribe() {
        fayeClient.unsubscribe();
    }


    public void disconnect() {
        Log.e(LOG_TAG, "disconnect");
        try {
            shouldConnect = false;
            fayeClient.unsubscribe();
            fayeClient.disconnectFromServer();
            fayeClient.setFayeListener(null);
            rpcGetChannel = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectedToServer() {
        Log.e(LOG_TAG, "connectedToServer");
        isConnected = true;
        if (onConnectedListener != null)
            onConnectedListener.onConnected();


    }

    @Override
    public void disconnectedFromServer() {
        isConnected = false;
        Log.e(LOG_TAG, "disconnectedFromServer");
        if (onDisconnectedListener != null)
            onDisconnectedListener.onDisconnected();
        if (shouldConnect)
            fayeClient.reconnect();
    }

    @Override
    public void subscribedToChannel(String subscription) {
        Log.e(LOG_TAG, "subscribedToChannel");
        if (onSubscribedListener != null)
            onSubscribedListener.onSubscribed(subscription);


    }

    @Override
    public void subscriptionFailedWithError(String error) {
        Log.e(LOG_TAG, "subscriptionFailedWithError");
        if (onSubscribeFailedListener != null)
            onSubscribeFailedListener.onSubscribeFailed(error);


    }

    @Override
    public void messageReceived(JSONObject message) {
        if (onMessageReceivedListener != null)
            onMessageReceivedListener.onMessageReceived(message);


    }

    public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
        this.onConnectedListener = onConnectedListener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    public void setOnSubscribedListener(OnSubscribedListener onSubscribedListener) {
        this.onSubscribedListener = onSubscribedListener;
    }

    public void setOnSubscribeFailedListener(OnSubscribeFailedListener onSubscribeFailedListener) {
        this.onSubscribeFailedListener = onSubscribeFailedListener;
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
        this.onMessageReceivedListener = onMessageReceivedListener;
    }

    public interface OnConnectedListener {
        void onConnected();
    }

    public interface OnDisconnectedListener {
        void onDisconnected();
    }

    public interface OnSubscribedListener {
        void onSubscribed(String subscription);
    }

    public interface OnSubscribeFailedListener {
        void onSubscribeFailed(String error);
    }

    public interface OnMessageReceivedListener {
        void onMessageReceived(JSONObject message);
    }

}
