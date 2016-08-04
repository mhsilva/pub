package com.pub.pubcustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.pub.pubcustomer.api.request.PubCallWaiterApi;
import com.pub.pubcustomer.ui.PubCallWaiter;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;
import com.pub.pubcustomer.ui.PubPlace;
import com.pub.pubcustomer.utils.PubGsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    List<PubPlace> pubPlaceCollection = new ArrayList<>();

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
                    pubPlaceCollection.add(new PubPlace(placeLikelihood.getPlace().getId().toString(),placeLikelihood.getPlace().getName().toString()));
                }

                mListView = (ListView) findViewById(R.id.listView);

               if(pubPlaceCollection.size() == 0)
                   pubPlaceCollection.add(new PubPlace("0","No places found"));

                mListView.setAdapter(new PubCurrentPlaceAdapter(MainActivity.this, pubPlaceCollection));
                mListView.setOnItemClickListener(MainActivity.this);

                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {

        final PubPlace  pubPlace = this.pubPlaceCollection.get(idx);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Type your table")
                .setTitle("Are you at " + pubPlace.getPlaceName());

        final EditText input = new EditText(this);
        builder.setView(input);

        // Setting Positive "Yes" Button
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        PubCallWaiterApi pubCallWaiter = new PubCallWaiterApi();
                        pubCallWaiter.setLocationId(pubPlace.getLocationId());
                        pubCallWaiter.setTableNumber(input.getText().toString());

                        Toast.makeText(getApplicationContext(),"Location id" + pubCallWaiter.getLocationId() + "Table Number" + pubCallWaiter.getTableNumber() , Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, PubCallWaiter.class);
                        intent.putExtra("pubCallWaiter", PubGsonUtils.objectToJson(pubCallWaiter) );

                        startActivity(intent);

                    }
                });
        // Setting Negative "NO" Button
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();


        dialog.show();
    }
}
