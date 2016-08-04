package com.pub.pubcustomer.rest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pub.pubcustomer.api.request.PubCallWaiterApi;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 04/08/2016.
 */
public class PubCallWaiterRestHelper {

    public static void callWaiter(Context context, PubCallWaiterApi pubCallWaiterApi) {
        Log.d("TAG", "Sending token.");
        Intent i = new Intent(context, PubRestCallWaiterService.class);
        i.setAction("start");
        i.putExtra(PubConstants.PUB_CALL_WAITER, pubCallWaiterApi);
        context.startService(i);
    }
}
