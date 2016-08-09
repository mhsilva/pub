package com.pub.pubcustomer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.entity.PubStatus;
import com.pub.pubcustomer.rest.PubCallWaiterRestHelper;
import com.pub.pubcustomer.rest.PubCallWaiterRestService;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiterActivity extends AppCompatActivity {


    private TextView textView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                PubStatus pubStatus = (PubStatus) bundle.getSerializable(PubConstants.PUB_STATUS);

                //TODO Change for List Adapter extends BaseAdapter
                StringBuilder stringBuilder = new StringBuilder();

                for(String item :pubStatus.getContent() ){
                    stringBuilder.append(item).append("\n");
                }

                if (bundle.getInt(PubConstants.RESULT) == RESULT_OK) {
                    Toast.makeText(PubCallWaiterActivity.this,
                            "Call Waiter successfully " ,
                            Toast.LENGTH_LONG).show();
                    textView.setText(stringBuilder);
                } else {
                    Toast.makeText(PubCallWaiterActivity.this, "Call Waiter failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Call Waiter failed try again");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pub_call_waiter);
        textView = (TextView) findViewById(R.id.textView2);
    }

    public void callWaiterOnClick(View view) {
        PubCallWaiterRestHelper.callWaiter(this,(PubCallWaiter) getIntent().getExtras().getSerializable("pubCallWaiter"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PubCallWaiterRestService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                PubConstants.CALL_WAITER_SERVICE_NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}