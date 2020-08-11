package com.example.crossprocesseventbus;

import android.app.Application;

import com.nobugexception.hermes.hermes.Hermes;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Hermes初始化
        Hermes.getDefault().init(this);

    }
}
