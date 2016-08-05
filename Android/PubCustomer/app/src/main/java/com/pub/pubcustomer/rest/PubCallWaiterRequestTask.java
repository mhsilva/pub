package com.pub.pubcustomer.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pub.pubcustomer.api.request.PubCallWaiter;
import com.pub.pubcustomer.api.response.PubStatus;
import com.pub.pubcustomer.utils.PubConstants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubCallWaiterRequestTask  extends AsyncTask<Void, Void, PubStatus> {

    private static final String TAG = "PubWaiterRequestTask";

    private Context mContext;
    private PubCallWaiter pubCallWaiter;
    private PubStatus pubStatus;

    public PubCallWaiterRequestTask(Context mContext, PubCallWaiter pubCallWaiterApi) {
        this.mContext = mContext;
        this.pubCallWaiter = pubCallWaiterApi;
    }

    @Override
    protected PubStatus doInBackground(Void... voids) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            pubStatus = restTemplate.postForObject(PubConstants.BASE_URL + PubConstants.REST_CALL_WAITER_METHOD, this.pubCallWaiter, PubStatus.class);
        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
        }
        return null;
    }


 /*   @Override
    protected void onPostExecute(PubStatus status) {
      super.onPostExecute(status);
    }*/
}
