package com.pub.pubcustomer.rest.establishment;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pub.pubcustomer.entity.PubEstablishmentsNotAffiliated;
import com.pub.pubcustomer.entity.PubPlaceNotRegistered;
import com.pub.pubcustomer.utils.PubConstants;
import com.pub.pubcustomer.utils.PubObjectUtil;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Fernando Santiago on 22/08/2016.
 */
public class PubEstablishmentsNotAffiliatedRequestTask  extends AsyncTask<PubPlaceNotRegistered, Void, PubEstablishmentsNotAffiliated> {

    private PubEstablishmentsNotAffiliated pubEstablishmentsNotAffiliated;
    private final String URL = PubConstants.BASE_URL + PubConstants.REST_ESTABLISHIMENT_NOT_AFFILIATED_METHOD;
    private static final String TAG = PubEstablishmentsNotAffiliatedRequestTask.class.getSimpleName();

    @Override
    protected PubEstablishmentsNotAffiliated doInBackground(PubPlaceNotRegistered... pubPlaceNotRegistered) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            pubEstablishmentsNotAffiliated = restTemplate.postForObject(URL, pubPlaceNotRegistered[0], PubEstablishmentsNotAffiliated.class);
        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
        }

        return pubEstablishmentsNotAffiliated;
    }

    @Override
    protected void onPostExecute(PubEstablishmentsNotAffiliated pubEstablishmentsNotAffiliated) {

        //TODO tratar este retorno corretamente na Activity que chamou (PubPlaceNotRegisteredActivity)
        Boolean result = false;
        Intent intent = new Intent(PubConstants.CALL_PUB_ESTABLISHMENTS_NOT_AFFILIATED_API);

        if(!PubObjectUtil.isEmpty(pubEstablishmentsNotAffiliated)){
            result = true;
        }

    }
}