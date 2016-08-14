package com.pub.pubcustomer.rest.establishment;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRest {

    public void checkPubEstablishementsStatus(Context context, Map<String, List<String>> checkLocationIdRegisteredMap) {

        PubEstablishmentRequestTask pubEstablishmentRequestTask = new PubEstablishmentRequestTask(context);
        pubEstablishmentRequestTask.execute(checkLocationIdRegisteredMap);
    }
}