package com.example.live;

import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.module.IAppLife;
import com.example.live.dagger.DaggerMainComponent;
import com.example.live.dagger.MainComponent;
import com.example.live.dagger.MainModule;


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      10:35
 * QQ:             1981367757
 */

public class LiveApplication implements IAppLife {
    private static MainComponent mainComponent;


    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
            mainComponent= DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(BaseApplication.getAppComponent()).build();
    }

    @Override
    public void onTerminate(Application application) {
        if (mainComponent != null) {
            mainComponent = null;
        }
    }


    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
