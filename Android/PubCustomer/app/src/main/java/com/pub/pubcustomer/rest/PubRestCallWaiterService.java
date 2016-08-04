package com.pub.pubcustomer.rest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pub.pubcustomer.api.request.PubCallWaiterApi;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubRestCallWaiterService extends Service {

    public static final String TAG = "PubRestCallWaiterService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Start rest Service.");
        PubCallWaiterApi pubCallWaiterApi = (PubCallWaiterApi) intent.getExtras().getSerializable(PubConstants.PUB_CALL_WAITER);
        PubCallWaiterRequestTask pubCallWaiterRequestTask = new PubCallWaiterRequestTask(getApplicationContext(), pubCallWaiterApi);
        pubCallWaiterRequestTask.execute();
        Log.d(TAG, "onStartCommand: rest Service finish.");
        return START_STICKY;
    }
}
