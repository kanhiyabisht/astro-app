package com.example.astrodashalib.service.faye;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.example.astrodashalib.Constant;
import com.example.astrodashalib.data.models.FayeToken;
import com.example.astrodashalib.faye.FayeChannelController;
import com.example.astrodashalib.generic.GenericCallback;
import com.example.astrodashalib.helper.PrefGetter;
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity;

import org.json.JSONObject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.astrodashalib.provider.rest.RestProvider.getFayeTokenService;

/**
 * Created by himanshu on 05/07/16.
 */
public class FayeService extends Service {

    FayeChannelController fayeChannels;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    Context context = this;
    private static final String TAG = FayeService.class.getSimpleName();
    public static boolean isAlive = false;
    NetworkReciever networkReciever;
    MyWorkerThread mWorkerThread;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void startAction(Context context) {
        if (!isAlive)
            startFayeService(context);
    }

    public static void startFayeService(Context context) {
        isAlive = true;
        Intent intent = new Intent(context, FayeService.class);
        context.startService(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            registerNetworkReceiver();
            context = this.getApplicationContext();
            if (!PrefGetter.getUserId(context).equals("-1"))
                initialize();
            else
                stopFayeService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void registerNetworkReceiver() {
        networkReciever = new NetworkReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReciever, filter);
    }


    void initialize() {
        try {
            mWorkerThread = new MyWorkerThread("myWorkerThread");
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    init();
                }
            };

            mWorkerThread.start();
            mWorkerThread.prepareHandler();
            mWorkerThread.postTask(run);
        } catch (Exception e) {
        }
    }

    private void init() {
        try {
            isAlive = true;
            fayeChannels = FayeChannelController.newInstance(context);
            Log.e(TAG, "OnCreating fayeChannels");
            String fayeToken = PrefGetter.getFayeToken(context);
            boolean bool = (fayeToken.isEmpty()) ? fetchTokenFromServer() : createFayeChannelAndStartFetchNewChatService();
        } catch (Exception e) {
        }
    }

    protected boolean fetchTokenFromServer() {
        mCompositeSubscription.add(getFayeTokenService().getFayeToken(PrefGetter.getUserId(context), Constant.KEY_VALUE, Constant.CHAT_KEY, Constant.CHAT_SECRET)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FayeToken>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(FayeToken fayeToken) {
                        Log.e(TAG, "onNext ");
                        PrefGetter.setFayeToken(fayeToken.token,context);
                        createFayeChannelAndStartFetchNewChatService();

                    }
                })
        );

        return true;
    }

    protected boolean createFayeChannelAndStartFetchNewChatService() {
        Log.e(TAG, "onSuccess FayeToken");
        if (fayeChannels == null)
            fayeChannels = FayeChannelController.newInstance(context);
        fayeChannels.createFayeChannels(new GenericCallback() {
            @Override
            public void callback(JSONObject error, Object object) {
                sendNetworkStatus();
            }
        });
        NewChatService.startService(context, false);
        return true;
    }

    private void sendNetworkStatus() {
        Intent i = new Intent(ChatDetailActivity.ACTION_NETWORK_CHANGE);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNetworkConnected", true);
        i.putExtras(bundle);
        context.sendBroadcast(i);
    }

    public class NetworkReciever extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
            if (!isConnected)
                stopFayeService();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(TAG, "onTrimMemory");
        if (level >= TRIM_MEMORY_RUNNING_LOW)
            stopFayeService();
    }

    private void stopFayeService() {
        try {
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.e(TAG, "Service Destroy");
            isAlive = false;
            if (fayeChannels != null)
                fayeChannels.disconnect();
            unregisterReciver();
            if (mWorkerThread != null)
                mWorkerThread.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterReciver() {
        try {
            unregisterReceiver(networkReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
