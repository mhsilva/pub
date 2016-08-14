package com.pub.pubcustomer.rest.callwaiter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubCallWaiterRestHelper {

    private static final String TAG = PubCallWaiterRestHelper.class.getSimpleName();

    public static void callWaiterApi(Context context, PubCallWaiter pubCallWaiterApi) {
        Log.d(TAG, "Sending Call Waiter.");
        Intent i = new Intent(context, PubCallWaiterRestService.class);
        i.setAction("start");
        i.putExtra(PubConstants.PUB_CALL_WAITER, pubCallWaiterApi);
        context.startService(i);
    }
}