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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.ui.PubCallWaiterActivity;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;
import com.pub.pubcustomer.entity.PubPlace;

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

                //TODO Delete line below
                pubPlaceCollection.add(new PubPlace("ChIJb4x_rlvPyJQRI-DvjnJ6-n8","Thomson Reuters"));

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    pubPlaceCollection.add(new PubPlace(placeLikelihood.getPlace().getId().toString(),placeLikelihood.getPlace().getName().toString()));
                }

               if(pubPlaceCollection.size() == 0)
                   pubPlaceCollection.add(new PubPlace("0","No places found"));

                mListView = (ListView) findViewById(R.id.listView);
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

        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        PubCallWaiter pubCallWaiter = new PubCallWaiter();
                        pubCallWaiter.setLocationId(pubPlace.getLocationId());
                        pubCallWaiter.setTableNumber(input.getText().toString());

                        Intent intent = new Intent(MainActivity.this, PubCallWaiterActivity.class);
                        intent.putExtra("pubCallWaiter", pubCallWaiter );
                        intent.putExtra("placeName",pubPlace.getPlaceName());

                        startActivity(intent);
                        finish();

                    }
                });

        builder.setNegativeButton(R.string.no,
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
