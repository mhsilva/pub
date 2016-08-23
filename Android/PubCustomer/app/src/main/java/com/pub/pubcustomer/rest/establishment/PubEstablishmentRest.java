package com.pub.pubcustomer.rest.establishment;

import android.content.Context;

import com.pub.pubcustomer.entity.PubPlaceNotRegistered;

import java.util.List;
import java.util.Map;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */
public class PubEstablishmentRest {

    //BASE_URL/establishment?locationId=ChIJb4x_rlvPyJQRI-DvjnJ6-n8,...,...
    public void checkPubEstablishementsStatus(Context context, Map<String, List<String>> checkLocationIdRegisteredMap) {

        PubEstablishmentRequestTask pubEstablishmentRequestTask = new PubEstablishmentRequestTask(context);
        pubEstablishmentRequestTask.execute(checkLocationIdRegisteredMap);
    }

    //BASE_URL/eestablishmentsNotAffiliated
    public void establishmentsNotAffiliated (PubPlaceNotRegistered pubPlaceNotRegistered){

        PubEstablishmentsNotAffiliatedRequestTask pubEstablishmentsNotAffiliatedRequestTask = new PubEstablishmentsNotAffiliatedRequestTask();
        pubEstablishmentsNotAffiliatedRequestTask.execute(pubPlaceNotRegistered);
    }
}