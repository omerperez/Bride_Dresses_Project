package com.example.bride_dresses_project;

import android.app.Application;
import android.content.Context;

public class ContextApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextApplication.context = getApplicationContext();
    }

    public static Context getContext(){return context;}
}

