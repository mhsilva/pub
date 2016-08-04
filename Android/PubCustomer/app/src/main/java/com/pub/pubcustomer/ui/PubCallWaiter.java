package com.pub.pubcustomer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.pub.pubcustomer.R;
import com.pub.pubcustomer.api.request.PubCallWaiterApi;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiter extends AppCompatActivity {

    public static final String TAG ="PubCallWaiter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pub_call_waiter);

        Gson gson = new Gson();
        PubCallWaiterApi pubCallWaiterApi = gson.fromJson(getIntent().getStringExtra("pubCallWaiter"), PubCallWaiterApi.class);

        Log.d(TAG, pubCallWaiterApi.getLocationId() + pubCallWaiterApi.getTableNumber() );
    }
}
