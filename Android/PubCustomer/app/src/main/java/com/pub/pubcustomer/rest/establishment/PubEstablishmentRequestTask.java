package com.pub.pubcustomer.rest.establishment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pub.pubcustomer.utils.PubConstants;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRequestTask  extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = PubEstablishmentRequestTask.class.getSimpleName();
    private Boolean placeRegistered;
    private String locationId;
    private Context context;

    public PubEstablishmentRequestTask(Context context, String locationId) {
        this.context = context;
        this.locationId = locationId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            placeRegistered = restTemplate.postForObject(PubConstants.BASE_URL + PubConstants.REST_CALL_WAITER_METHOD,  this.locationId, Boolean.class);
        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
        }
        return placeRegistered;
    }

    @Override
    protected void onPostExecute(Boolean placeRegistered) {
    //TODO return status iF Establishment exist

    }
}