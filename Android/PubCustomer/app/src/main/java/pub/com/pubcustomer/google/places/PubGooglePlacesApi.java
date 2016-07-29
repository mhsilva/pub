package pub.com.pubcustomer.google.places;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando Santiago on 29/07/2016.
 */
public class PubGooglePlacesApi {

    private List<PlaceLikelihood> placeLikelihoodCollection = new ArrayList<>();
    List<String> placesColection= new ArrayList<>();
    private String[] placesName;

    public String[] getEstablishments(GoogleApiClient mGoogleApiClient) throws SecurityException {

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
            }


        });

        return placesName;
    }
}