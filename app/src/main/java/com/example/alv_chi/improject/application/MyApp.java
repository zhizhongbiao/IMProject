package com.example.alv_chi.improject.application;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by Alv_chi on 2017/2/7.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        initial freeline
        FreelineCore.init(this);

    }
}
