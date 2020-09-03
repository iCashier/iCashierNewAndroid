package com.icashier.app.autoservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ToastBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent= new Intent(context,BackgroundService.class);
        context.startService(serviceIntent);

    }
}
