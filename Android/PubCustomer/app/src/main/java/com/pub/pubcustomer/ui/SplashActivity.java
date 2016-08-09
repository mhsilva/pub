package com.pub.pubcustomer.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pub.pubcustomer.MainActivity;
import com.pub.pubcustomer.utils.PubAlertUtils;
import com.pub.pubcustomer.utils.PubPermissonUtils;

import com.pub.pubcustomer.R;

/**
 * Created by Fernando Santiago on 28/07/2016.
 */
public class SplashActivity extends AppCompatActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String permissons[] = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
        };

        boolean ok = PubPermissonUtils.validate(this, 0, permissons);

        if(ok){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                PubAlertUtils.alert(this, R.string.app_name, R.string.msg_alerta_permissao,R.string.ok, new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
                return;
            }
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}