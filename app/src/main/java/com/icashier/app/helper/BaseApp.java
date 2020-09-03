package com.icashier.app.helper;

import android.app.Activity;
import android.app.Application;

/**
 * Created by techugo on 30/1/17.
 */

public class BaseApp extends Application {
   static BaseApp instance;

    public BaseApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new BaseApp();
    }

    public static BaseApp getInstance() {
        if (instance != null)
            return instance;
        else
            return instance = new BaseApp();
    }

    private Activity mCurrentActivity = null;
    private  boolean activityVisible;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    public boolean isVisible() {
        return activityVisible;
    }

    public void setVisible(Boolean activityVisible) {
        this.activityVisible = activityVisible;
    }
}
