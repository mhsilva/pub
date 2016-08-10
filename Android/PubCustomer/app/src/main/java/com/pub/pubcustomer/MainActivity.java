package com.pub.pubcustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.entity.PubPlace;
import com.pub.pubcustomer.places.PlaceFilter;
import com.pub.pubcustomer.ui.PubCallWaiterActivity;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    private List<PubPlace> pubPlaceCollection = new ArrayList<>();
    private static final String TAG = "MainActivity";

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

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                final PubPlace pubPlace = pubPlaceCollection.get(arg2);
                Log.w("HHHHHHHHHHHHHH", pubPlace.getPlace().getId());
                return true;
            }
        });
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
                //pubPlaceCollection.add(new PubPlace("ChIJb4x_rlvPyJQRI-DvjnJ6-n8", "Thomson Reuters"));

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                    //Only present estabilishemtns Pub (Cafe, Bar, Cassino etc)
                    if (PlaceFilter.contaisPlaceType(placeLikelihood.getPlace().getPlaceTypes())) {
                        pubPlaceCollection.add(new PubPlace(placeLikelihood.getPlace().freeze(), placeLikelihood.getLikelihood()));
                    }

                    Log.i(TAG, String.format("Place '%s' with " + "likelihood: %g" + " with Place Types %s'", placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood(), placeLikelihood.getPlace().getPlaceTypes().toString()));
                }

               /* if (pubPlaceCollection.size() == 0)
                    pubPlaceCollection.add(new PubPlace("0", "No places found"));*/

                mListView = (ListView) findViewById(R.id.listView);
                mListView.setAdapter(new PubCurrentPlaceAdapter(MainActivity.this, pubPlaceCollection));
                mListView.setOnItemClickListener(MainActivity.this);

                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {

        final PubPlace pubPlace = this.pubPlaceCollection.get(idx);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Type your table")
                .setTitle("Are you at ".concat(pubPlace.getPlace().getName().toString().concat(" ?")));

        final EditText tableNumber = new EditText(this);
        builder.setView(tableNumber);

        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (StringUtils.hasText(tableNumber.getText().toString())) {

                            PubCallWaiter pubCallWaiter = new PubCallWaiter();
                            pubCallWaiter.setLocationId(pubPlace.getPlace().getId());
                            pubCallWaiter.setTableNumber(tableNumber.getText().toString());

                            Intent intent = new Intent(MainActivity.this, PubCallWaiterActivity.class);
                            intent.putExtra("pubCallWaiter", pubCallWaiter);
                            intent.putExtra("placeName", pubPlace.getPlace().getName());

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Table number is required", Toast.LENGTH_SHORT).show();
                        }
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