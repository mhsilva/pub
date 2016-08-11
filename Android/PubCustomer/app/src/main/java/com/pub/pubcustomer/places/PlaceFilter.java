package com.pub.pubcustomer.places;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fernando Santiago on 09/08/2016.
 */
public class PlaceFilter {

    private static List<Integer> placesFilter = new ArrayList<>();

    private static List<Integer> getPlacesFilter() {

        placesFilter.add(Place.TYPE_BAKERY);
        placesFilter.add(Place.TYPE_BAR);
        placesFilter.add(Place.TYPE_CAFE);
        placesFilter.add(Place.TYPE_CASINO);
        placesFilter.add(Place.TYPE_CONVENIENCE_STORE);
        placesFilter.add(Place.TYPE_FOOD);
        placesFilter.add(Place.TYPE_LIQUOR_STORE);
        placesFilter.add(Place.TYPE_NIGHT_CLUB);
        placesFilter.add(Place.TYPE_RESTAURANT);

        //TODO Delete line below
        placesFilter.add(Place.TYPE_ESTABLISHMENT);

        return placesFilter;
    }

    public static Boolean contaisPlaceType(List<Integer> placeTypeFrom) {
        return !Collections.disjoint(getPlacesFilter(), placeTypeFrom);
    }
}