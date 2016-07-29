package pub.com.pubcustomer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                  /*  Log.i("TESTE", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));*/

                }

           /*  Collections.sort(placeLikelihoodCollection, new Comparator<PlaceLikelihood>() {
                    @Override
                    public int compare(PlaceLikelihood o1, PlaceLikelihood o2) {
                        return o1.getLikelihood() < o2.getLikelihood() ? -1
                                : o1.getLikelihood() > o2.getLikelihood() ? 1
                                : 0;
                    }

                });*/

                mListView = (ListView) findViewById(R.id.listView);

                if(placesColection!= null){
                    mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main,R.id.textView, placesColection));
                }else
                {
                    mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main,R.id.textView, new String[]{"No Places Found"}));
                }

                likelyPlaces.release();

            }
        });
    }
}