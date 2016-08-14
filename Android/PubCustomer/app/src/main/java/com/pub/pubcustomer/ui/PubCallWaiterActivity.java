package com.pub.pubcustomer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubCallWaiter;
import com.pub.pubcustomer.entity.PubStatus;
import com.pub.pubcustomer.rest.callwaiter.PubCallWaiterRestHelper;
import com.pub.pubcustomer.rest.callwaiter.PubCallWaiterRestAsyncService;
import com.pub.pubcustomer.utils.PubConstants;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiterActivity extends AppCompatActivity {

   private ListView mListView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                PubStatus pubStatus = (PubStatus) bundle.getSerializable(PubConstants.PUB_STATUS);

                if (bundle.getBoolean(PubConstants.RESULT)) {
                    Toast.makeText(PubCallWaiterActivity.this,
                            "Call Waiter successfully " ,
                            Toast.LENGTH_LONG).show();
                    mListView.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1,pubStatus.getContent()));
                } else {
                    Toast.makeText(PubCallWaiterActivity.this, "Call Waiter failed",
                            Toast.LENGTH_LONG).show();
                    mListView.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1,new String[] {"Call Waiter failed, Try Again"}));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pub_call_waiter);
        mListView = (ListView) findViewById(R.id.listView2);
        setTitle(getIntent().getStringExtra("placeName"));
    }

    public void callWaiterOnClick(View view) {
        PubCallWaiterRestHelper.callWaiterApi(this,(PubCallWaiter) getIntent().getExtras().getSerializable("pubCallWaiter"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PubCallWaiterRestAsyncService.class));
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