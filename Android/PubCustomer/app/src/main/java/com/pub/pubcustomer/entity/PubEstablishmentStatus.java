package com.pub.pubcustomer.entity;

import java.io.Serializable;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */


public class PubEstablishmentStatus implements Serializable {

    private String locationId;

    private boolean registered;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
