package com.xiao.testlibz;

import android.app.Application;

public class NetworkApp extends Application {
    private static NetworkApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NetworkApp getInstance() {
        return instance;
    }
}
