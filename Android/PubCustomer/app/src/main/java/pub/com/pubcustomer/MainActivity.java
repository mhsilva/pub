package pub.com.pubcustomer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;
    private List<PlaceLikelihood> placeLikelihoodCollection = new ArrayList<>();
    List<String> placesColection= new ArrayList<>();
    private String[] placesName;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        getEstablishments(mGoogleApiClient);
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

    public void getEstablishments(GoogleApiClient mGoogleApiClient) throws SecurityException {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    //placeLikelihoodCollection.add(placeLikelihood);
                    placesColection.add(placeLikelihood.getPlace().getName().toString());

                    Log.i("TESTE", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));

                }

             /*   Collections.sort(placeLikelihoodCollection, new Comparator<PlaceLikelihood>() {
                    @Override
                    public int compare(PlaceLikelihood o1, PlaceLikelihood o2) {
                        return o1.getLikelihood() < o2.getLikelihood() ? -1
                                : o1.getLikelihood() > o2.getLikelihood() ? 1
                                : 0;
                    }

                });*/

              /*  placesName = new String[placeLikelihoodCollection.size()];
                placeLikelihoodCollection.toArray(placesName);*/

                placesName = new String[placesColection.size()];
                placesColection.toArray(placesName);
                likelyPlaces.release();

                mListView = (ListView) findViewById(R.id.listView);

                if(placesName!= null){
                    mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main,R.id.textView, placesName));
                }else
                {
                    mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main,R.id.textView, new String[]{"No Places Found"}));
                }

            }
        });
    }


}