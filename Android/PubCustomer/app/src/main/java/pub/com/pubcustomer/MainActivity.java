package pub.com.pubcustomer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import pub.com.pubcustomer.google.places.PubGooglePlacesApi;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mListView = (ListView) findViewById(R.id.listView);

      /*  PubGooglePlacesApi pubGooglePlacesApi = new PubGooglePlacesApi();
        String[] places = pubGooglePlacesApi.getEstablishments(mGoogleApiClient);

        if(places!= null){
            mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,R.id.textView, places));
        }else
        {
            mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,R.id.textView, new String[]{"No Places Found"}));
        }*/
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();

        mListView = (ListView) findViewById(R.id.listView);


        PubGooglePlacesApi pubGooglePlacesApi = new PubGooglePlacesApi();
        String[] places = pubGooglePlacesApi.getEstablishments(mGoogleApiClient);

        if(places!= null){
            mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,R.id.textView, places));
        }else
        {
            mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,R.id.textView, new String[]{"No Places Found"}));
        }


        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}