package com.pub.pubwaiter.gcmservice;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Matheus on 03/08/2016.
 */
public class PubInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
    }
}
