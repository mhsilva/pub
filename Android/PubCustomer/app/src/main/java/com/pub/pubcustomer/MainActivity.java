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
import com.pub.pubcustomer.entity.PubEstablishmentStatus;
import com.pub.pubcustomer.entity.PubPlace;
import com.pub.pubcustomer.entity.PubPlaceLikelihood;
import com.pub.pubcustomer.places.PlaceFilter;
import com.pub.pubcustomer.rest.establishment.PubEstablishmentRest;
import com.pub.pubcustomer.ui.PubCallWaiterActivity;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;
import com.pub.pubcustomer.utils.PubConstants;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    private List<PubPlaceLikelihood> pubPlaceLikelihoodAll = new ArrayList<>();
    private List<PubPlaceLikelihood> pubPlaceLikelihoodUnRegistered = new ArrayList<>();
    private List<PubPlaceLikelihood> getPubPlaceLikelihoodRegistered = new ArrayList<>();
    private  List<String> checkLocationIdRegistered = new ArrayList<>();

    private Map<String,List<String>> checkLocationIdRegisteredMap = new HashMap<>();

    private List<PubEstablishmentStatus> pubEstablishmentStatusCollection;
    private static final String TAG = MainActivity.class.getSimpleName();

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

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                final PubPlaceLikelihood pubPlace = pubPlaceLikelihoodAll.get(arg2);

                // Context context = getApplicationContext();
                StringBuilder sb = new StringBuilder();
                sb.append(pubPlace.getPlace().getAddress()).append("\n").
                        append(pubPlace.getPlace().getPhoneNumber());
                Toast toast = Toast.makeText(MainActivity.this, sb, Toast.LENGTH_LONG);
                toast.show();

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

                //TODO Delete line Two below
                pubPlaceLikelihoodAll.add(new PubPlaceLikelihood(new PubPlace("ChIJb4x_rlvPyJQRI-DvjnJ6-n8", "Thomson Reuters"), 1));
                checkLocationIdRegistered.add("ChIJb4x_rlvPyJQRI-DvjnJ6-n8");

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                    //Only present Establishment such as Pub (Cafe, Bar, Cassino etc)
                    if (PlaceFilter.contaisPlaceType(placeLikelihood.getPlace().getPlaceTypes())) {
                        pubPlaceLikelihoodAll.add(new PubPlaceLikelihood(placeLikelihood.getPlace().freeze(), placeLikelihood.getLikelihood()));
                        checkLocationIdRegistered.add(placeLikelihood.getPlace().getId());
                    }

                    Log.i(TAG, String.format("Place '%s' with " + "likelihood: %g" + " with Place Types %s'", placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood(), placeLikelihood.getPlace().getPlaceTypes().toString()));
                }

                ///Check status from Establishement (active or inactive) on Pub Backend
                checkLocationIdRegisteredMap.put(PubConstants.LOCATION_ID_LIST,checkLocationIdRegistered);
                PubEstablishmentRest pubEstablishmentRestHelper = new PubEstablishmentRest();
                pubEstablishmentRestHelper.checkPubEstablishementsStatus(checkLocationIdRegisteredMap);

                if (pubPlaceLikelihoodAll.size() == 0)
                    pubPlaceLikelihoodAll.add(new PubPlaceLikelihood(new PubPlace("0", "No Places Found around"), 0));

                mListView = (ListView) findViewById(R.id.listView);
                mListView.setAdapter(new PubCurrentPlaceAdapter(MainActivity.this, pubPlaceLikelihoodAll));
                mListView.setOnItemClickListener(MainActivity.this);

                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {

        final PubPlaceLikelihood pubPlace = this.pubPlaceLikelihoodAll.get(idx);

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