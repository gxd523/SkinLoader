package com.gxd.skinloader.sample;

import android.app.Application;

import com.gxd.skinloader.SkinManager;

/**
 * Created by guoxiaodong on 2020/7/7 14:00
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.INSTANCE.init(this);
    }
}
