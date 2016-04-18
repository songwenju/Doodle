package com.zhangshuo.doodle.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.zhangshuo.doodle.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zhaokai on 09/04/16.
 *
 */
public class FileUtils {

    public static String getDoodlePath() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "doodle");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    public static ArrayList<String> getPicturesPathFromAlbum1(int num) {


        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = BaseApplication.getAppContext().getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);

                    if (latestImagePaths.size() >= num || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    public static String saveBitmap(Bitmap bitmap,String name){
        if(!TextUtils.isEmpty(name)) {
            if(!name.endsWith(".png"))
                name = name+".png";
            File file = new File(getDoodlePath() + File.separator + name);
            if (bitmap != null) {
                FileOutputStream fileOS = null;
                try {
                    boolean b = file.createNewFile();
                    fileOS = new FileOutputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //将绘图内容压缩为Png格式输出到输出流的对象中
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
                try {
                    assert fileOS != null;
                    fileOS.flush();
                    fileOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file.getAbsolutePath();
        }else {
            return "";
        }
    }
}