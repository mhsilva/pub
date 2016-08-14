package com.pub.pubcustomer.rest.establishment;

import java.util.List;
import java.util.Map;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRest {

    public void checkPubEstablishementsStatus(Map<String, List<String>> checkLocationIdRegisteredMap) {

        PubEstablishmentRequestTask pubEstablishmentRequestTask = new PubEstablishmentRequestTask();
        pubEstablishmentRequestTask.execute(checkLocationIdRegisteredMap);
    }
}