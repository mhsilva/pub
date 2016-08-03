package com.pub.pubcustomer.ui;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubPlace {

    private String locationId;
    private String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public PubPlace() {
    }

    public PubPlace(String locationId, String placeName) {
        this.locationId = locationId;
        this.placeName = placeName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
