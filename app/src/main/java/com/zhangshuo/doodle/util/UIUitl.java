package com.zhangshuo.doodle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.zhangshuo.doodle.base.BaseApplication;

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

    public static int getWindowHeight() {

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static Bitmap getSampledBitmap(String filePath, int reqWidth,
                                          int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = (int) Math.round((float) height / reqHeight);
            } else {
                inSampleSize = (int) Math.round((float) width / reqWidth);
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取合适大小的Bitmap
     * @param filePath 路径
     *
     * @return
     */
    public static Bitmap getBitmap(String filePath){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // 获取图片宽高 height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int bitWidth;
        int bitHeight;
        float rote = 0;
        if(height>width){//让宽度等于屏幕宽度
            bitWidth = getWindowWidth();
            bitHeight = width*getWindowHeight()/getWindowWidth();
        }else {//让高度等于屏幕宽度
            bitHeight = getWindowWidth();
            bitWidth = height*getWindowWidth()/getWindowHeight();
            rote =90;
        }
//        Bitmap temp = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.RGB_565);
        bitmap = getSampledBitmap(filePath,getWindowWidth(),getWindowHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(0.3f, 0.3f);
        Bitmap temp = Bitmap
                .createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
        return temp;
    }
}
