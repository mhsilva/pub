package com.pub.pubcustomer.ui;

import android.app.Dialog;
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
import com.pub.pubcustomer.rest.callwaiter.PubCallWaiterRestService;
import com.pub.pubcustomer.utils.PubAlertUtils;
import com.pub.pubcustomer.utils.PubConstants;
import com.pub.pubcustomer.utils.PubNetworkUtils;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubCallWaiterActivity extends AppCompatActivity {

   private ListView mListView;
   private Dialog dialog;

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

            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_call_waiter);
        mListView = (ListView) findViewById(R.id.listView2);
        setTitle(getIntent().getStringExtra("placeName"));
        this.callWaiter();
    }

    public void callWaiterOnClick(View view) {
       this.callWaiter();
    }

    public void callWaiter(){
        if(PubNetworkUtils.isNetworkAvailable(this)) {

            dialog = PubAlertUtils.progressDialog(this,getResources().getString(R.string.calling_waiter) );
            dialog.show();

            PubCallWaiterRestHelper.callWaiterApi(this, (PubCallWaiter) getIntent().getExtras().getSerializable("pubCallWaiter"));
        }else{
            PubAlertUtils.errorDialog(this, getResources().getString(R.string.invalid_configuration), getResources().getString(R.string.no_network));
        }
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
                PubConstants.CALL_WAITER_SERVICE_NOTIFICATION_API));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}