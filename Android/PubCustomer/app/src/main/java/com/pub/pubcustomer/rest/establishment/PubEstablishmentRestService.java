package com.pub.pubcustomer.rest.establishment;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRestService extends Service {

    public static final String TAG = PubEstablishmentRestService.class.getSimpleName();
    private final IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Start rest Service.");
        PubEstablishmentRequestTask pubEstablishmentRequestTask = new PubEstablishmentRequestTask(getApplicationContext(), intent.getExtras().getString(PubConstants.LOCATION_ID));
        pubEstablishmentRequestTask.execute();
        Log.d(TAG, "onStartCommand: rest Service finish.");

        return Service.START_NOT_STICKY;
    }

    public class MyBinder extends Binder {
        PubEstablishmentRestService getService() {
            return PubEstablishmentRestService.this;
        }
    }
}
