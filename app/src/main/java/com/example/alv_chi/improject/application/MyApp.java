package com.example.alv_chi.improject.application;

import android.app.Application;
import android.content.Context;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by Alv_chi on 2017/2/7.
 */

public class MyApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        initial freeline
        FreelineCore.init(this);
        context = this;
    }

    public static Context getMyAppContext() {
        return context;
    }
}
