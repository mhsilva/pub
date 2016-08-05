package com.pub.pubcustomer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pub.pubcustomer.R;
import com.pub.pubcustomer.api.request.PubCallWaiter;
import com.pub.pubcustomer.rest.PubCallWaiterRestHelper;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiterUi extends AppCompatActivity {

    public static final String TAG ="PubCallWaiterUi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pub_call_waiter);

        TextView displayTextView =  (TextView)findViewById(R.id.textView2);
        //displayTextView.setText(getIntent().getStringExtra("pubCallWaiter"));
    }

    public void callWaiterOnClick(View view) {
        Gson gson = new Gson();
        PubCallWaiterRestHelper.callWaiter(this,gson.fromJson(getIntent().getStringExtra("pubCallWaiter"), PubCallWaiter.class));
    }
}
