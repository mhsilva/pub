package com.pub.pubcustomer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pub.pubcustomer.R;

import java.util.List;

/**
 * Created by Fernando Santiago on 02/08/2016.
 */
public class PubCurrentPlaceAdapter  extends BaseAdapter {


    private final Context context;
    private final List<PubPlace> placescCollection;


    public PubCurrentPlaceAdapter(Context context,List<PubPlace> placescCollection ){
        this.context = context;
        this.placescCollection = placescCollection;
    }


    @Override
    public int getCount() {
        return placescCollection != null ? placescCollection.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return placescCollection.get(position);
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

        PubPlace pubCallWaiter = placescCollection.get(position);

        tvLocationId.setText(pubCallWaiter.getLocationId());
        tvTableNumber.setText(pubCallWaiter.getPlaceName());

        return view;
    }
}
