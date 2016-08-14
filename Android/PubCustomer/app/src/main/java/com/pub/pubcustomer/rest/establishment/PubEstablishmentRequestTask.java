package com.pub.pubcustomer.rest.establishment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.pub.pubcustomer.entity.PubEstablishmentStatus;
import com.pub.pubcustomer.utils.PubConstants;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRequestTask extends AsyncTask<Map<String, List<String>>, Void, ResponseEntity<PubEstablishmentStatus[]> > {

    private static final String TAG = PubEstablishmentRequestTask.class.getSimpleName();
    private final String url = PubConstants.BASE_URL + PubConstants.REST_ESTABLISHIMENT_METHOD;
    private  ResponseEntity<PubEstablishmentStatus[]> pubEstablishmentStatusCollection;
    private Context mContext;

    public PubEstablishmentRequestTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ResponseEntity<PubEstablishmentStatus[]>  doInBackground(Map<String, List<String>>... checkLocationIdRegisteredMap) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //Build query parameters from URL, for example: https://pubbackend.herokuapp.com/establishment?locationIdList=ChIJb4x_rlvPyJQRI-DvjnJ6-n8,location2,location2..locationN
            //TODO Check if checkLocationIdRegisteredMap[0 is not null (delete Thomson)
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam(PubConstants.LOCATION_ID_LIST, TextUtils.join(",", checkLocationIdRegisteredMap[0].get(PubConstants.LOCATION_ID_LIST)));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            pubEstablishmentStatusCollection = restTemplate.exchange(
                    builder.build().encode().toUri(), HttpMethod.GET,
                    entity,
                    PubEstablishmentStatus[].class);

            Log.d(TAG, "Get /establishment?locationIdList Api successfully" );

        } catch (Throwable e) {
            Log.e(TAG, "doInBackground: Error! " + e.getMessage(), e);
        }
        return pubEstablishmentStatusCollection;
    }

    @Override
    protected void onPostExecute(ResponseEntity<PubEstablishmentStatus[]> pubEstablishmentStatusCollection ) {

        Boolean result;
        Intent intent = new Intent(PubConstants.CALL_PUB_ESTABLISHMENTS_STATUS_API);

        if(pubEstablishmentStatusCollection.getStatusCode().equals(HttpStatus.OK) || pubEstablishmentStatusCollection.getStatusCode().equals(HttpStatus.ACCEPTED)){
            result = true;
            intent.putExtra(PubConstants.RESULT, result);
            intent.putExtra(PubConstants.PUB_ESTABLISHMENTS_STATUS, copyArrayToMap(pubEstablishmentStatusCollection.getBody()));
            mContext.sendBroadcast(intent);
        }
    }

    private HashMap<String, Boolean> copyArrayToMap(PubEstablishmentStatus[] pubEstablishmentStatusCollection){

        HashMap<String, Boolean> establishmentStatusByLocationId
                = new HashMap<String, Boolean>(pubEstablishmentStatusCollection.length);

        for(int i=0; i<pubEstablishmentStatusCollection.length; i++){
            establishmentStatusByLocationId.put(pubEstablishmentStatusCollection[i].getLocationId(),pubEstablishmentStatusCollection[i].isRegistered());
        }

        return establishmentStatusByLocationId;
    }
}