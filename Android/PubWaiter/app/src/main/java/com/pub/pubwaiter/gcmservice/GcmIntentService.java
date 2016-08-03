package com.pub.pubwaiter.gcmservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.pub.pubwaiter.entity.PubWaiter;
import com.pub.pubwaiter.rest.util.PubWaiterRestHelper;
import com.pub.pubwaiter.util.PubConstants;

import java.io.IOException;

/**
 * Created by Matheus on 02/08/2016.
 */
public class GcmIntentService extends IntentService {

    public static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(TAG, "Starting service.");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(PubConstants.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.getBoolean(PubConstants.TOKEN_SENT_TO_SERVER, false)) {
                sendToBackend(token, sharedPreferences);
            }
            sharedPreferences.edit().putBoolean(PubConstants.TOKEN_SENT_TO_SERVER, true).apply();
            sharedPreferences.edit().putString(PubConstants.TOKEN, token).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToBackend(String token, SharedPreferences sharedPreferences) {
        String login = sharedPreferences.getString(PubConstants.WAITER_LOGIN, null);
        PubWaiter pubWaiter = new PubWaiter();
        pubWaiter.setLogin(login);
        pubWaiter.setToken(token);
        PubWaiterRestHelper.registerWaiter(this, pubWaiter);
    }
}
