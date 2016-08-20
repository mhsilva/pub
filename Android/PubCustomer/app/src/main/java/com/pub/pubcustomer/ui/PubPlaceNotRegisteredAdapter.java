package com.pub.pubcustomer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubPlaceNotRegistered;

import java.util.List;

/**
 * Created by Fernando Santiago on 20/08/2016.
 */
public class PubPlaceNotRegisteredAdapter  extends BaseAdapter {

    private final Context context;
    private final List<PubPlaceNotRegistered> pubPlaceNotRegisteredCollection;

    public PubPlaceNotRegisteredAdapter(Context context, List<PubPlaceNotRegistered> placecCollection ){
        this.context = context;
        this.pubPlaceNotRegisteredCollection = placecCollection;
    }

    @Override
    public int getCount() {
        return pubPlaceNotRegisteredCollection != null ? pubPlaceNotRegisteredCollection.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return pubPlaceNotRegisteredCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_pub_place_not_registered, parent, false);

        TextView tvLocationId = (TextView) view.findViewById(R.id.tLocationId);
        TextView tvTableNumber = (TextView) view.findViewById(R.id.tPlaceName);

        PubPlaceNotRegistered pubPlaceNotRegistered = pubPlaceNotRegisteredCollection.get(position);

        tvLocationId.setText(pubPlaceNotRegistered.getId());
        tvTableNumber.setText(pubPlaceNotRegistered.getName());

        return view;
    }
}