package com.pub.pubcustomer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.pub.pubcustomer.api.request.PubCallWaiter;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    List<PubCallWaiter> pubCallWaiterCollection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        getGooglePlacesApi(mGoogleApiClient);
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void getGooglePlacesApi(GoogleApiClient mGoogleApiClient) throws SecurityException {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    pubCallWaiterCollection.add(new PubCallWaiter(placeLikelihood.getPlace().getId().toString(),placeLikelihood.getPlace().getName().toString()));
                }

                mListView = (ListView) findViewById(R.id.listView);

               if(pubCallWaiterCollection.size() == 0)
                   pubCallWaiterCollection.add(new PubCallWaiter("0","No places found"));

                mListView.setAdapter(new PubCurrentPlaceAdapter(MainActivity.this,pubCallWaiterCollection));

                likelyPlaces.release();
            }
        });
    }
}