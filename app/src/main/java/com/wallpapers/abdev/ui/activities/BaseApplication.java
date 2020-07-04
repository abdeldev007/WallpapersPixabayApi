package com.wallpapers.abdev.ui.activities;

import android.app.Application;
import android.content.Context;

import com.wallpapers.abdev.R;
import com.wallpapers.abdev.util.Auth;


public class BaseApplication extends Application  {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.context = getApplicationContext();

        Auth.getInstance().setUsername(getString(R.string.username));
        Auth.getInstance().setKey(getString(R.string.pixabay_api));
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }

}
