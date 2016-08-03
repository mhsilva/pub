package com.pub.pubwaiter.rest.util;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.pub.pubwaiter.entity.PubWaiter;
import com.pub.pubwaiter.util.PubConstants;

import java.io.Serializable;

/**
 * Created by Matheus on 03/08/2016.
 */
public class PubRestService extends Service {

    public static final String TAG = "PubRestService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Start rest Service.");
        PubWaiter pubWaiter = (PubWaiter) intent.getExtras().getSerializable(PubConstants.PUB_WAITER);
        PubWaiterRequestTask pubWaiterRequestTask = new PubWaiterRequestTask(this, pubWaiter);
        pubWaiterRequestTask.execute();
        Log.d(TAG, "onStartCommand: rest Service finish.");
        return START_STICKY;
    }
}
