package com.pub.pubcustomer.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.entity.PubEstablishmentStatus;
import com.pub.pubcustomer.entity.PubEstablishmentTables;
import com.pub.pubcustomer.entity.PubPlaceNotRegistered;
import com.pub.pubcustomer.places.PlaceFilter;
import com.pub.pubcustomer.rest.establishment.PubEstablishmentRest;
import com.pub.pubcustomer.utils.PubAlertUtils;
import com.pub.pubcustomer.utils.PubConstants;
import com.pub.pubcustomer.utils.PubNetworkUtils;
import com.pub.pubcustomer.utils.PubObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubPlaceRegisteredAcitivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ResultCallback<LocationSettingsResult> {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    private List<PlaceLikelihood> placeLikelihoodAll = new ArrayList<>();
    private List<PlaceLikelihood> placeLikelihoodRegistered = new ArrayList<>();
    private List<PubPlaceNotRegistered> pubPlaceNotRegisteredUnregistered = new ArrayList<>();
    private List<String> checkAllPlacesRegistered = new ArrayList<>();
    private Map<String, List<String>> checkLocationIdRegisteredMap = new HashMap<>();
    private static final String TAG = PubPlaceRegisteredAcitivity.class.getSimpleName();
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Dialog dialog;
    private PubEstablishmentStatus pubEstablishmentStatus;
    private HashMap<String, PubEstablishmentStatus> pubEstablishmentStatusCollection;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            PubPlaceNotRegistered pubPlaceNotRegistered;

            if (bundle != null) {

                if (bundle.getBoolean(PubConstants.RESULT)) {

                    pubEstablishmentStatusCollection = (HashMap<String, PubEstablishmentStatus>) bundle.getSerializable(PubConstants.PUB_ESTABLISHMENTS_STATUS);

                    for (PlaceLikelihood pubPlaceLikelihoodAll : placeLikelihoodAll) {

                        pubEstablishmentStatus = pubEstablishmentStatusCollection.get(pubPlaceLikelihoodAll.getPlace().getId());

                        //Establishment is registered
                        if (pubEstablishmentStatus.isRegistered()) {
                            placeLikelihoodRegistered.add(pubPlaceLikelihoodAll);
                        } else {
                            pubPlaceNotRegistered = new PubPlaceNotRegistered();

                            if (pubPlaceLikelihoodAll.getPlace().getId() != null)
                                pubPlaceNotRegistered.setId(pubPlaceLikelihoodAll.getPlace().getId());

                            if (pubPlaceLikelihoodAll.getPlace().getAddress() != null)
                                pubPlaceNotRegistered.setAddress(pubPlaceLikelihoodAll.getPlace().getAddress().toString());

                            if (pubPlaceLikelihoodAll.getPlace().getName() != null)
                                pubPlaceNotRegistered.setName(pubPlaceLikelihoodAll.getPlace().getName().toString());

                            if (pubPlaceLikelihoodAll.getPlace().getPhoneNumber() != null)
                                pubPlaceNotRegistered.setName(pubPlaceLikelihoodAll.getPlace().getName().toString());

                            if (pubPlaceLikelihoodAll.getPlace().getWebsiteUri() != null)
                                pubPlaceNotRegistered.setWebSiteUri(pubPlaceLikelihoodAll.getPlace().getWebsiteUri().toString());

                            pubPlaceNotRegisteredUnregistered.add(pubPlaceNotRegistered);
                        }
                    }
                }
            }

            if (!placeLikelihoodRegistered.isEmpty()) {
                updateListView(placeLikelihoodRegistered);
            } else {
                //callEstablishementsNotRegistered();
            }

            dialog.dismiss();
        }
    };

    private void updateListView(List<PlaceLikelihood> currentPlace) {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new PubPlaceRegisteredAdapter(this, currentPlace));
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle(getString(R.string.where_are_you));

        dialog = PubAlertUtils.createDialog(this, getResources().getString(R.string.finding_places));
        dialog.show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_registered);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                final PlaceLikelihood pubPlace = placeLikelihoodRegistered.get(arg2);

                //TODO verify is value is different from null before append
                StringBuilder sb = new StringBuilder();
                sb.append(PubObjectUtil.ifNull(pubPlace.getPlace().getAddress(), "")).append("\n");
                sb.append(PubObjectUtil.ifNull(pubPlace.getPlace().getPhoneNumber(), "")).append("\n");
                sb.append(PubObjectUtil.ifNull(pubPlace.getPlace().getWebsiteUri(), "")).append("\n");

                Toast toast = Toast.makeText(PubPlaceRegisteredAcitivity.this, sb, Toast.LENGTH_LONG);
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

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                    //Only present Establishment such as Pub (Cafe, Bar, Cassino etc)
                    if (PlaceFilter.contaisPlaceType(placeLikelihood.getPlace().getPlaceTypes())) {

                        placeLikelihoodAll.add(placeLikelihood.freeze());
                        checkAllPlacesRegistered.add(placeLikelihood.getPlace().getId());
                    }

                    Log.d(TAG, String.format("Place '%s' with " + "likelihood: %g" + " with Id Place '%s'" + " with Place Types '%s'",
                            placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood(),
                            placeLikelihood.getPlace().getId(),
                            placeLikelihood.getPlace().getPlaceTypes().toString()));
                }

                ///Check status from Establishement (active or inactive) on Pub Backend
                if (checkAllPlacesRegistered.size() > 0) {
                    checkLocationIdRegisteredMap.put(PubConstants.LOCATION_ID_LIST, checkAllPlacesRegistered);
                    PubEstablishmentRest pubEstablishmentRestHelper = new PubEstablishmentRest();
                    pubEstablishmentRestHelper.checkPubEstablishementsStatus(PubPlaceRegisteredAcitivity.this, checkLocationIdRegisteredMap);
                } else {
                    //TODO How call again getGooglePlacesApi?
                    Toast.makeText(getApplicationContext(),
                            "No places found around try again", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {

        singleChoice(this.placeLikelihoodRegistered.get(idx));
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
                    status.startResolutionForResult(PubPlaceRegisteredAcitivity.this, REQUEST_CHECK_SETTINGS);
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


    public void callEstablishementsNotRegistered(View view) {
        Intent intent = new Intent(PubPlaceRegisteredAcitivity.this, PubPlaceNotRegisteredActivity.class);
        intent.putParcelableArrayListExtra("pubPlaceNotRegisteredUnregistered", (ArrayList<? extends Parcelable>) pubPlaceNotRegisteredUnregistered);
        startActivity(intent);
        finish();
    }

    private void singleChoice(final PlaceLikelihood placeLikelihood) {

        if (!PubObjectUtil.isEmpty(pubEstablishmentStatusCollection.get(placeLikelihood.getPlace().getId()).getTables())) {

            List<PubEstablishmentTables> pubEstablishmentTablesCollection = pubEstablishmentStatusCollection.get(placeLikelihood.getPlace().getId()).getTables();
            List<String> tables = new ArrayList<>();

            for (PubEstablishmentTables pubTables : pubEstablishmentTablesCollection) {
                tables.add(" " + pubTables.getTableNumber() + " " + getString(R.string.avaliable) + ": " +  pubTables.isAvailable() );
            }

            String[] tablesArrTemp = new String[tables.size()];
            tablesArrTemp = tables.toArray(tablesArrTemp);

            final String[] tablesArr = tablesArrTemp;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.choose_your_table);

            builder.setItems(tablesArr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callWaiterActivity(placeLikelihood, tablesArr[which]);

                }
            });
            builder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Toast.makeText(PubPlaceRegisteredAcitivity.this,
                   R.string.table_not_found, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void callWaiterActivity(PlaceLikelihood placeLikelihood, String selectedTable) {


        PubCallWaiter pubCallWaiter = new PubCallWaiter();
        pubCallWaiter.setLocationId(placeLikelihood.getPlace().getId());
        pubCallWaiter.setTableNumber(selectedTable);

        Intent intent = new Intent(PubPlaceRegisteredAcitivity.this, PubCallWaiterActivity.class);
        intent.putExtra("pubCallWaiter", pubCallWaiter);
        intent.putExtra("placeName", placeLikelihood.getPlace().getName());

        startActivity(intent);
        finish();
    }
}