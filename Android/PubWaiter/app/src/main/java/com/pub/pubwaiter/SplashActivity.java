package com.pub.pubwaiter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pub.pubwaiter.util.PubConstants;

/**
 * Created by Matheus on 03/08/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(PubConstants.TOKEN_SENT_TO_SERVER,false)) {
            i = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            i = new Intent(this, TablesCallingActivity.class);
        }
        startActivity(i);
        finish();
    }
}
