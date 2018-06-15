package com.example.astrodashalib.service.faye;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by abhishek on 20/09/16.
 */
public class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler;


    public MyWorkerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task){
        mWorkerHandler.post(task);
    }

    public void prepareHandler(){
        mWorkerHandler = new Handler(getLooper());
    }
}
