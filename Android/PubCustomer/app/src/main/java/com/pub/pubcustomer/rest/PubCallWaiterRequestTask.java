package com.pub.pubcustomer.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pub.pubcustomer.api.request.PubCallWaiterApi;
import com.pub.pubcustomer.utils.PubConstants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubCallWaiterRequestTask  extends AsyncTask<Void, Void, PubCallWaiterApi> {

    private static final String TAG = "PubWaiterRequestTask";

    private Context mContext;
    private PubCallWaiterApi pubCallWaiterApiReq;

    public PubCallWaiterRequestTask(Context mContext, PubCallWaiterApi pubCallWaiterApi) {
        this.mContext = mContext;
        this.pubCallWaiterApiReq = pubCallWaiterApi;
    }

    @Override
    protected PubCallWaiterApi doInBackground(Void... voids) {

        PubCallWaiterApi pubCallWaiterApi = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            pubCallWaiterApi = restTemplate.postForObject(PubConstants.BASE_URL + PubConstants.REST_CALL_WAITER_METHOD, pubCallWaiterApiReq, PubCallWaiterApi.class);
        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
        }
        return pubCallWaiterApi;
    }
}
