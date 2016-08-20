package com.pub.pubcustomer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.location.places.PlaceLikelihood;
import com.pub.pubcustomer.R;

import java.util.List;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubPlaceRegisteredAdapter extends BaseAdapter {

    private final Context context;
    private final List<PlaceLikelihood> pubPlaceCollection;

    public PubPlaceRegisteredAdapter(Context context, List<PlaceLikelihood> placecCollection ){
        this.context = context;
        this.pubPlaceCollection = placecCollection;
    }

    @Override
    public int getCount() {
        return pubPlaceCollection != null ? pubPlaceCollection.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return pubPlaceCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_pub_place_registered, parent, false);

        TextView tvLocationId = (TextView) view.findViewById(R.id.tLocationId);
        TextView tvTableNumber = (TextView) view.findViewById(R.id.tPlaceName);

        PlaceLikelihood pubPlace = pubPlaceCollection.get(position);

        tvLocationId.setText(pubPlace.getPlace().getId());
        tvTableNumber.setText(pubPlace.getPlace().getName());

        return view;
    }
}