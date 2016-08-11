package com.pub.pubcustomer.rest.establishment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRestHelper {

    private static final String TAG = PubEstablishmentRestHelper.class.getSimpleName();

    public static void getRegisteredEstablishmentsByLocationId(Context context, String locationId) {

        Log.d(TAG, "Sending id Establishment.");
        Intent i = new Intent(context, PubEstablishmentRestService.class);
        i.setAction("start");
        i.putExtra(PubConstants.PUB_CALL_WAITER, locationId);
        context.startService(i);
    }
}
