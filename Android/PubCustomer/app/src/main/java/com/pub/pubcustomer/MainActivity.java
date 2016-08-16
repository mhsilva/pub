package com.pub.pubcustomer;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.entity.PubPlace;
import com.pub.pubcustomer.entity.PubPlaceLikelihood;
import com.pub.pubcustomer.places.PlaceFilter;
import com.pub.pubcustomer.rest.establishment.PubEstablishmentRest;
import com.pub.pubcustomer.ui.PubCallWaiterActivity;
import com.pub.pubcustomer.ui.PubCurrentPlaceAdapter;
import com.pub.pubcustomer.utils.PubAlertUtils;
import com.pub.pubcustomer.utils.PubConstants;
import com.pub.pubcustomer.utils.PubNetworkUtils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,ResultCallback<LocationSettingsResult>  {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    private List<PubPlaceLikelihood> pubPlaceLikelihoodAll = new ArrayList<>();
    private List<PubPlaceLikelihood> pubPlaceLikelihoodRegistered = new ArrayList<>();
    private List<PubPlaceLikelihood> pubPlaceLikelihoodUnRegistered = new ArrayList<>();
    private List<String> checkLocationIdRegistered = new ArrayList<>();
    private Map<String, List<String>> checkLocationIdRegisteredMap = new HashMap<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Dialog dialog;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                if (bundle.getBoolean(PubConstants.RESULT)) {

                    HashMap<String, Boolean> establishmentStatusByLocationId = (HashMap<String, Boolean>) bundle.getSerializable(PubConstants.PUB_ESTABLISHMENTS_STATUS);

                    for (PubPlaceLikelihood pubPlaceLikelihood : pubPlaceLikelihoodAll) {

                        //Establishment is registered
                        if (establishmentStatusByLocationId.get(pubPlaceLikelihood.getPlace().getId())) {
                            pubPlaceLikelihoodRegistered.add(pubPlaceLikelihood);
                        } else {
                            pubPlaceLikelihoodUnRegistered.add(pubPlaceLikelihood);
                        }
                    }

                    updateListView(pubPlaceLikelihoodRegistered);
                }
            }

            if (pubPlaceLikelihoodRegistered.size() == 0) {
                pubPlaceLikelihoodRegistered.add(new PubPlaceLikelihood(new PubPlace("0", "No Places Found around"), 0));
                updateListView(pubPlaceLikelihoodRegistered);
            }

            dialog.dismiss();
        }
    };

    private void updateListView(List<PubPlaceLikelihood> currentPlace) {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new PubCurrentPlaceAdapter(MainActivity.this, currentPlace));
        mListView.setOnItemClickListener(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog = PubAlertUtils.createDialog(this,getResources().getString(R.string.finding_places) );
        dialog.show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_main);

            mListView = (ListView) findViewById(R.id.listView);
            mListView.setLongClickable(true);
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                    final PubPlaceLikelihood pubPlace = pubPlaceLikelihoodAll.get(arg2);

                    StringBuilder sb = new StringBuilder();
                    sb.append(pubPlace.getPlace().getAddress()).append("\n");
                    sb.append(pubPlace.getPlace().getPhoneNumber()).append("\n");
                    sb.append(pubPlace.getPlace().getWebsiteUri()).append("\n");

                    Toast toast = Toast.makeText(MainActivity.this, sb, Toast.LENGTH_LONG);
                    toast.show();

                    return true;
                }
            });

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    @Override
    public void onStart() {
        if (PubNetworkUtils.isNetworkAvailable(this)) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (PubNetworkUtils.isNetworkAvailable(this)) mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void getGooglePlacesApi(GoogleApiClient mGoogleApiClient) throws SecurityException {

       PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                //TODO Delete line Two below
                pubPlaceLikelihoodAll.add(new PubPlaceLikelihood(new PubPlace("ChIJb4x_rlvPyJQRI-DvjnJ6-n8", "Thompson"), 1));
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
                if (checkLocationIdRegistered.size() > 0) {
                    checkLocationIdRegisteredMap.put(PubConstants.LOCATION_ID_LIST, checkLocationIdRegistered);
                    PubEstablishmentRest pubEstablishmentRestHelper = new PubEstablishmentRest();
                    pubEstablishmentRestHelper.checkPubEstablishementsStatus(MainActivity.this, checkLocationIdRegisteredMap);
                } else {
                    pubPlaceLikelihoodRegistered.add(new PubPlaceLikelihood(new PubPlace("0", "No Places Found around"), 0));
                    updateListView(pubPlaceLikelihoodRegistered);
                }

                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {

        final PubPlaceLikelihood pubPlace = this.pubPlaceLikelihoodRegistered.get(idx);

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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                PubConstants.CALL_PUB_ESTABLISHMENTS_STATUS_API));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                getGooglePlacesApi(mGoogleApiClient);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        getGooglePlacesApi(mGoogleApiClient);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        dialog.dismiss();
                        PubAlertUtils.errorDialog(this, getResources().getString(R.string.invalid_configuration), getResources().getString(R.string.error_enable_location));
                        break;
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .build();
    }
}