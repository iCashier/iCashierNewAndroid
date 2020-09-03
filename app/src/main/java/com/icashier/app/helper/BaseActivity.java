package com.icashier.app.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by techugo on 30/1/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseApp mMyApp;
    BroadcastReceiver mReceiver;
    String rideData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = BaseApp.getInstance();
        mMyApp.setVisible(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("network_status"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyApp.setVisible(true);
        mMyApp.setCurrentActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearReferences();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void clearReferences() {
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity)) {
            mMyApp.setVisible(false);
        }

    }

    public void onNetworkChange(boolean status) {

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean status = intent.getBooleanExtra("network_status", false);
            onNetworkChange(status);
        }
    };

}
