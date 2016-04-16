package com.zhangshuo.doodle.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by zhaokai on 16/4/9.
 *
 */
public class BaseApplication extends Application {

    private static Context mAppContext;
    private static BaseApplication application = new BaseApplication();
    private static Handler mainHandler;

    public BaseApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局Application
        mAppContext = this;
        mainHandler = new Handler();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }
}
