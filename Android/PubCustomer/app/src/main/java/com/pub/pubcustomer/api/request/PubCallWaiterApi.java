package com.pub.pubcustomer.api.request;

import java.io.Serializable;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */


public class PubCallWaiterApi implements Serializable {

    private String locationId;
    private String tableNumber;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }


    public PubCallWaiterApi() {

    }

}