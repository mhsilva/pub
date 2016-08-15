package com.pub.pubcustomer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Fernando Santiago on 14/08/2016.
 */

public class PubNetworkUtils {

    private static final String TAG = PubNetworkUtils.class.getSimpleName();

    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            PubAlertUtils.alertDialog(context, e.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }
}