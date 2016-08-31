package com.pub.pubcustomer.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fernando Santiago on 11/08/2016.
 */


public class PubEstablishmentStatus implements Serializable {

    private String locationId;
    private boolean registered;
    private List<PubEstablishmentTables> tables;

    public List<PubEstablishmentTables> getTables() {
        return tables;
    }

    public void setTables(List<PubEstablishmentTables> tables) {
        this.tables = tables;
    }

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
