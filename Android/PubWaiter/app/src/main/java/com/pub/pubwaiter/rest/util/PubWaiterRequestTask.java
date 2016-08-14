package com.pub.pubwaiter.rest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pub.pubwaiter.entity.PubWaiter;
import com.pub.pubwaiter.util.PubConstants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Matheus on 03/08/2016.
 */
public class PubWaiterRequestTask extends AsyncTask<Void, Void, PubWaiter> {

    private static final String TAG = "PubWaiterRequestTask";

    private Context mContext;
    private PubWaiter pubWaiterReq;

    public PubWaiterRequestTask(Context mContext, PubWaiter pubWaiter) {
        this.mContext = mContext;
        this.pubWaiterReq = pubWaiter;
    }

    @Override
    protected PubWaiter doInBackground(Void... voids) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        PubWaiter pubWaiter = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            pubWaiter = restTemplate.postForObject(PubConstants.BASE_URL + "waiter", pubWaiterReq, PubWaiter.class);
            sharedPreferences.edit().putBoolean(PubConstants.TOKEN_SENT_TO_SERVER, true).apply();
        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
            sharedPreferences.edit().putBoolean(PubConstants.TOKEN_SENT_TO_SERVER, false).apply();
        }
        return pubWaiter;
    }
}
