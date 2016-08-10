package com.pub.pubcustomer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubPlace;

import java.util.List;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubCurrentPlaceAdapter  extends BaseAdapter {

    private final Context context;
    private final List<PubPlace> pubPlaceCollection;


    public PubCurrentPlaceAdapter(Context context,List<PubPlace> placecCollection ){
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

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_current_place, parent, false);

        TextView tvLocationId = (TextView) view.findViewById(R.id.tLocationId);
        TextView tvTableNumber = (TextView) view.findViewById(R.id.tPlaceName);

        PubPlace pubCallWaiter = pubPlaceCollection.get(position);

        tvLocationId.setText(pubCallWaiter.getPlace().getId());
        tvTableNumber.setText(pubCallWaiter.getPlace().getName().toString());

        return view;
    }
}