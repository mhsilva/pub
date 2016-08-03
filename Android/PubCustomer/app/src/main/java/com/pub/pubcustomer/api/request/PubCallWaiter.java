package com.pub.pubcustomer.api.request;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubCallWaiter {

    private String locationId;
    private String tableNumber;
    private String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public PubCallWaiter() {
    }

    public PubCallWaiter(String locationId, String tableNumber, String placeName) {
        this.locationId = locationId;
        this.tableNumber = tableNumber;
        this.placeName = placeName;
    }

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
}
