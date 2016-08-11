package com.pub.pubcustomer.entity;

import com.google.android.gms.location.places.Place;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubPlaceLikelihood {

    private Place place;
    private float likelihood;

    public PubPlaceLikelihood() {
    }

    public PubPlaceLikelihood(Place place, float likelihood) {
        this.place = place;
        this.likelihood = likelihood;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setLikelihood(float likelihood) {
        this.likelihood = likelihood;
    }

    public Place getPlace() {
        return place;
    }

    public float getLikelihood() {
        return likelihood;
    }
}