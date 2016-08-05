package com.pub.pubcustomer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.api.request.PubCallWaiter;
import com.pub.pubcustomer.rest.PubCallWaiterRestHelper;
import com.pub.pubcustomer.rest.PubRestServiceCallWaiter;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiterUi extends AppCompatActivity {

    public static final String TAG ="PubCallWaiterUi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pub_call_waiter);
    }

    public void callWaiterOnClick(View view) {
        PubCallWaiterRestHelper.callWaiter(this,(PubCallWaiter) getIntent().getExtras().getSerializable("pubCallWaiter"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PubRestServiceCallWaiter.class));
    }

}
