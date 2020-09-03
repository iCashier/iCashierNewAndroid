package com.icashier.app.activity;

import android.content.IntentSender;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private Thread thread;
    SplashActivity context;
    private AppUpdateManager appUpdateManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setLanguage();
        setContentView(R.layout.activity_splash);
        //checkUpdate();

        thread= new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(AppConstant.SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String userId= SharedPrefManager.getInstance(context).getString(AppConstant.KEY_LOGIN_USER_ID,"");
                if(userId.equals(""))
                {
                    Utilities.clearAllgoToActivity(context, LoginActivity.class);
                }
                else
                {
                    //if the user is logged in
                    Utilities.clearAllgoToActivity(context, HomeActivity.class);

                }
            }
        });
        thread.start();
    }

    //==========================Mehtod to set app language============================//
    private void setLanguage() {

        String appLang=SharedPrefManager.getInstance(getBaseContext()).getString(AppConstant.APP_LANGUAGE,"en");

        Locale locale=new Locale("en");
        if(appLang.equals("en"))
        {
            locale = new Locale("en");
        }else if(appLang.equals("ar"))
        {
            locale = new Locale("ar","MA");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    /*
    Method to check app update from playstore
     */
    private void checkUpdate() {

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, SplashActivity.this, 102);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                    Log.e("UPDATE", "yes");
                    //Utilities.showToastLong(SplashActivity.this, "yes");
                } else {

                    Log.e("UPDATE", "no");
                    //Utilities.showToastLong(SplashActivity.this, "No");
                }
            }


        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
    }
}
