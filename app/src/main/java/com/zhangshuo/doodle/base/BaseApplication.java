package com.zhangshuo.doodle.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.umeng.socialize.UMShareAPI;


public class BaseApplication extends Application {
    public static Context mAppContext;
    public static BaseApplication application = new BaseApplication();
    public static Handler mainHandler;
    private UMShareAPI mUmShareAPI;

    public BaseApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局Application
        mAppContext = this;
        mainHandler = new Handler();

        mUmShareAPI = UMShareAPI.get(this);

    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }
}
