package com.icashier.app.autoservices;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class BackgroundService extends IntentService {
    public static final String ACTION = "com.icashier.app.autoservices.ResponseBroadcastReceiver";

    // Must create a default constructor
    public BackgroundService() {
        // Used to name the worker thread, important only for debugging.
        super("backgroundService");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // This describes what will happen when service is triggered
        Log.i("backgroundService", "Service running");
      //  autoPrint();
        //create a broadcast to send the toast message
        Intent toastIntent = new Intent(ACTION);
        toastIntent.putExtra("resultCode", Activity.RESULT_OK);
        toastIntent.putExtra("toastMessage", "Automatic Printing Start");
        sendBroadcast(toastIntent);

    }



}
