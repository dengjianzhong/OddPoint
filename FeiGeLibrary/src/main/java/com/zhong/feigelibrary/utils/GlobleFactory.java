package com.zhong.feigelibrary.utils;

import android.app.Application;
import android.content.Context;

public class GlobleFactory extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context =this;

    }
    public static Context getGlobleContext(){
        return context;
    }
}