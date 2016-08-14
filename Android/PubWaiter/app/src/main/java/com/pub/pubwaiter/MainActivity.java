package com.pub.pubwaiter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.pub.pubwaiter.entity.PubWaiter;
import com.pub.pubwaiter.gcmservice.GcmIntentService;
import com.pub.pubwaiter.rest.util.PubWaiterRestHelper;
import com.pub.pubwaiter.util.PubConstants;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    EditText waiterName;
    EditText waiterPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString(PubConstants.TOKEN, null);
        if (token == null) {
            Intent i = new Intent(MainActivity.this, GcmIntentService.class);
            i.setAction("start");
            startService(i);
        }
        waiterName = (EditText) findViewById(R.id.waiterName);
        waiterPhone = (EditText) findViewById(R.id.waiterPhone);
    }

    public void registerWaiterOnClick(View view) {
        String token = sharedPreferences.getString(PubConstants.TOKEN, null);
        PubWaiter pubWaiter = new PubWaiter();
        String login = waiterPhone.getText().toString();
        pubWaiter.setName(waiterName.getText().toString());
        pubWaiter.setLogin(login);
        pubWaiter.setToken(token);
        PubWaiterRestHelper.registerWaiter(this,pubWaiter);
        sharedPreferences.edit().putString(PubConstants.WAITER_LOGIN, login);
        Intent i = new Intent(this, TablesCallingActivity.class);
        startActivity(i);
    }
}
