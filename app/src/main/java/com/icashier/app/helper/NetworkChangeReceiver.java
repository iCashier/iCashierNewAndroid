package com.icashier.app.helper;

/**
 * Created by techugo on 6/2/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static boolean firstConnect = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if(firstConnect) {
                boolean status = Utilities.isNetworkAvailable(context);
                Intent i = new Intent("network_status");
                i.putExtra("network_status",status);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                firstConnect = false;
            }
        }
        else {
            if(!firstConnect) {
                boolean status = Utilities.isNetworkAvailable(context);
                Intent i = new Intent("network_status");
                i.putExtra("network_status",status);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                firstConnect = false;
            }
            firstConnect= true;

        }

    }

}
