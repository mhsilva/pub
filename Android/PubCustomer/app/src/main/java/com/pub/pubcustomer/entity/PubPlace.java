package com.pub.pubcustomer.entity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubPlace implements PlaceLikelihood {

    private Place place;
    private float likelihood;

    public PubPlace() {
    }

    public PubPlace(Place place, float likelihood) {
        this.place = place;
        this.likelihood = likelihood;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setLikelihood(float likelihood) {
        this.likelihood = likelihood;
    }

    @Override
    public PlaceLikelihood freeze() {
        return null;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }

    @Override
    public float getLikelihood() {
        return likelihood;
    }

    @Override
    public Place getPlace() {
        return place;
    }
}