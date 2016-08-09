package com.pub.pubcustomer.rest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pub.pubcustomer.api.request.PubCallWaiter;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubRestCallWaiterService extends Service {

    public static final String TAG = "PubRestCallWaiterServic";
    private final IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Start rest Service.");
        PubCallWaiter pubCallWaiterApi = (PubCallWaiter) intent.getExtras().getSerializable(PubConstants.PUB_CALL_WAITER);
        PubCallWaiterRequestTask pubCallWaiterRequestTask = new PubCallWaiterRequestTask(getApplicationContext(), pubCallWaiterApi);
        pubCallWaiterRequestTask.execute();
        Log.d(TAG, "onStartCommand: rest Service finish.");

        return Service.START_NOT_STICKY;
    }

    public class MyBinder extends Binder {
        PubRestCallWaiterService getService() {
            return PubRestCallWaiterService.this;
        }
    }
}
