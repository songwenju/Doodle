package com.zhangshuo.doodle.logic;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.zhangshuo.doodle.base.BaseApplication;

/**
 * Created by zhaokai on 16/4/9.
 *
 */
public class UIUitl {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        WindowManager wm = (WindowManager) BaseApplication.getAppContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        float scale = dm.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getColor(int resId){
       return BaseApplication.getAppContext().getResources().getColor(resId);
    }

    public static void runOnUiThread(Runnable r){
        BaseApplication.getMainHandler().post(r);
    }

    public static int getWindowWidth() {

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
