package com.pub.pubwaiter.rest.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pub.pubwaiter.entity.PubWaiter;
import com.pub.pubwaiter.util.PubConstants;

/**
 * Created by Matheus on 03/08/2016.
 */
public class PubWaiterRestHelper {

    public static void registerWaiter(Context context, PubWaiter pubWaiter) {
        Log.d("TAG", "Sending token.");
        Intent i = new Intent(context, PubRestService.class);
        i.setAction("start");
        i.putExtra(PubConstants.PUB_WAITER, pubWaiter);
        context.startService(i);
    }

}
