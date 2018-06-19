package com.example.astrodashalib.faye;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.example.astrodashalib.R;
import com.example.astrodashalib.faye.channels.RpcGetChannel;
import com.example.astrodashalib.faye.channels.RpcPostChannel;
import com.example.astrodashalib.generic.BroadcastEventHandler;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.generic.GenericDataAccessInterface;
import com.example.astrodashalib.generic.GenericQueryCallback;
import com.example.astrodashalib.helper.PrefGetter;
import com.example.astrodashalib.helper.Util;
import com.example.astrodashalib.localDB.ModelInterface;
import com.example.astrodashalib.service.faye.ChatIntentService;
import com.example.astrodashalib.utils.BaseConfiguration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.UUID;

/**
 * Created by himanshu on 23/06/16.
 */
public class FayeChannelController implements GenericDataAccessInterface {

    private static FayeChannelController sFayeChannelController;
    private static RpcGetChannel rpcGetChannel;
    private static RpcPostChannel rpcPostChannel;
    Context context;
    private static HashMap<String, GenericCallback> callbackHashMap = new HashMap<>();
    private static final String LOG_TAG = FayeChannelController.class.getSimpleName();
    static LinkedList<Object> pendingQueue = new LinkedList<>();

    private FayeChannelController(Context context) {
        this.context = context;
    }

    public static FayeChannelController newInstance(Context context) {
        return sFayeChannelController == null ? new FayeChannelController(context) : sFayeChannelController;
    }


    public boolean createFayeChannels(final GenericCallback genericCallback) {
        Log.e("FCC", "createFayeChannels: " );
        if (!PrefGetter.getFayeToken(context.getApplicationContext()).isEmpty() && !PrefGetter.getDeviceId(context.getApplicationContext()).isEmpty() && !PrefGetter.getUserId(context.getApplicationContext()).equals("-1")) {
            createRpcGetChannel(new GenericCallback() {
                @Override
                public void callback(JSONObject error, Object object) {
                    Log.e(LOG_TAG, "login channel callback");
                }
            });
            createRpcPostChannel(new GenericCallback() {
                @Override
                public void callback(JSONObject error, Object object) {
                    try {
                        Log.e(LOG_TAG, "Chat channel callback");
                        if (pendingQueue.isEmpty())
                            ChatIntentService.startChatIntentService(context);
                        else
                            processPendingRequests();
                        genericCallback.callback(error, object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        return false;
    }

    private void createRpcGetChannel(final GenericCallback callback) {
        JSONObject extension = Util.getExtension(context.getApplicationContext());
        Log.e("EXTENSION",extension.toString());
        String userId = extension.optString("user_id");
        String channelName = "/rpc/get/" + userId;
        rpcGetChannel = RpcGetChannel.getInstance(new Handler(), Util.FAYE_URL, channelName, extension);
        rpcGetChannel.setOnSubscribedListener(new RpcGetChannel.OnSubscribedListener() {
            @Override
            public void onSubscribed(String subscription) {
                callback.callback(null, subscription);
            }
        });
        rpcGetChannel.setOnMessageReceivedListener(new RpcGetChannel.OnMessageReceivedListener() {
                                                       @Override
                                                       public void onMessageReceived(JSONObject message) {
                                                           try {
                                                               Log.e("Faye", "onMessageReceived: "+message);
                                                               fayeMessageReceived(message);
                                                           } catch (Exception e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   }

        );
        rpcGetChannel.connect();
    }

    public void refreshChannels() {
        createFayeChannels(new GenericCallback() {
            @Override
            public void callback(JSONObject error, Object object) {

            }
        });
    }

    protected void fayeMessageReceived(JSONObject message) {
        String requestId = message.optString("request_id");
        GenericCallback callback = callbackHashMap.get(requestId);
        boolean bool = callback != null ? callbackMessageReceived(message, callback) : broadCastMessageReceived(message);
        callbackHashMap.remove(requestId);
    }

    protected boolean callbackMessageReceived(JSONObject message, GenericCallback callback) {
        try {
            boolean success = message.optBoolean("success");
            String requestId = message.optString("request_id");
            if (success)
                callback.callback(null, message);
            else
                callback.callback(new JSONObject().put("error", "Failed with requestId: " + requestId), message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    protected boolean broadCastMessageReceived(JSONObject message) {
        Log.e("FCC", "broadcast: "+message );
        Log.e(LOG_TAG, "New Faye Message");
        BroadcastEventHandler broadcastEventHandler = new BroadcastEventHandler(context);
        broadcastEventHandler.execute(message);
        return false;
    }

    private void createRpcPostChannel(final GenericCallback callback) {
        JSONObject extension = Util.getExtension(context.getApplicationContext());
        Log.e("EXTENSION",extension.toString());
        String userId = extension.optString("user_id");
        String channelName = "/rpc/post/" + userId;
        rpcPostChannel = RpcPostChannel.getInstance(new Handler(), Util.FAYE_URL, channelName, extension);
        rpcPostChannel.setOnSubscribedListener(new RpcPostChannel.OnSubscribedListener() {
            @Override
            public void onSubscribed(String subscription) {
                callback.callback(null, subscription);
            }
        });
        rpcPostChannel.connect();
    }

    public void disconnect() {
        try {
            sFayeChannelController = null;
            rpcGetChannel.disconnect();
            rpcPostChannel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void post(boolean bool, JSONObject obj, final GenericCallback genericCallback) {
        try {
            String requestId = UUID.randomUUID().toString();
            obj.put("request_id", requestId);
            Log.e("FCC", "post: "+obj );
            callbackHashMap.put(requestId, genericCallback);
            pendingQueue.add(obj);
            boolean flag = (rpcPostChannel == null || !rpcPostChannel.isConnected) ? createFayeChannels(new GenericCallback() {
                @Override
                public void callback(JSONObject error, Object object) {

                }
            }) : processPendingRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean processPendingRequests() {
        Log.e("FCC", "processPendingRequests: " );
        ListIterator listIterator = pendingQueue.listIterator();
        while (listIterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) listIterator.next();
            sendMessage(jsonObject);
            listIterator.remove();
        }
        return true;

    }

    private void sendMessage(JSONObject obj) {
        rpcPostChannel.sendMessage(obj);
    }


    @Override
    public void put(boolean bool, JSONObject obj, JSONObject queryObj, GenericCallback genericCallback) {
        //not required
    }

    @Override
    public void get(boolean bool, GenericCallback genericCallback) {
        //not required
    }

    @Override
    public void query(boolean bool, JSONObject JsonObj, GenericQueryCallback genericQueryCallback) {
        //not required
    }

    @Override
    public void fetchAll(boolean bool, GenericQueryCallback genericQueryCallback) {
        //not required
    }

    @Override
    public void delete(boolean bool, GenericCallback genericCallback) {
        //not required
    }

    @Override
    public void sendCallback(JSONObject errorObj, ModelInterface modelInterface) {

    }

    @Override
    public void sendCallback(JSONObject errorObj, ArrayList t) {

    }
}


