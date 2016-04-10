package com.zhangshuo.doodle.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhaokai on 2016/04/04.
 */
public class DrawView extends View {
    private int view_width = 0;//屏幕宽度
    private int view_height = 0;//屏幕高度
    private float preX;//起始点的X坐标
    private float preY;//起始点的Y坐标
    private Path path;
    public Paint paint = null;
    Bitmap cacheBitmap = null;//定义一个内存中的图片，该图片将作为缓冲区
    Canvas cacheCanvas = null;//定义一个画布

    public DrawView(Context context, AttributeSet attributeSet)//写一个构造方法初始化类
    {
        super(context, attributeSet);
        view_width = context.getResources().getDisplayMetrics().widthPixels;//获取屏幕的宽度   /*显示度量标准*宽像素
        view_height = context.getResources().getDisplayMetrics().heightPixels;//获取屏幕的高度

        //创建一个与该View相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(view_width,view_height, Bitmap.Config.ARGB_8888);
        /***
         * Bitmap.Config 	ALPHA_8 	Each pixel is stored as a single translucency (alpha) channel.
         Bitmap.Config 	ARGB_4444 	This field was deprecated in API level 13. Because of the poor quality of this configuration, it is advised to use ARGB_8888 instead.
         Bitmap.Config 	ARGB_8888 	Each pixel is stored on 4 bytes.
         Bitmap.Config 	RGB_565 	Each pixel is stored on 2 bytes and only the RGB channels are encoded: red is stored with 5 bits of precision (32 possible values), green is stored with 6 bits of precision (64 possible values) and blue is stored with 5 bits of precision.
         可以参考  http://blog.csdn.net/wulongtiantang/article/details/8481077
         */
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);//在cache的上边绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);//抗抖动
        /**Paint flag that enables dithering when blitting.
         Enabling this flag applies a dither to any blit operation
         where the target's colour space is more constrained than the source.*/
        paint.setColor(Color.RED);
        //设置画笔风格
        paint.setStyle(Paint.Style.STROKE);//填充轮廓
        paint.setStrokeJoin(Paint.Join.ROUND);//??????????????????????????????????????????
        paint.setStrokeCap(Paint.Cap.BUTT);//?????????????????????????????????
        paint.setStrokeWidth(8);//设置笔触宽度
        paint.setAntiAlias(true);//设置抗锯齿功能，稍微耗费内存
        paint.setDither(true);//使用抖动效果对手抖动结果进行处理
    }
    @Override
    public void onDraw(Canvas canvas) {//重写绘制方法
        canvas.drawColor(0xFFFFFFFF);//设置背景色
        Paint bmpPaint = new Paint();
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);//绘制cacheBitmap   //注释掉后画完一笔后不保留绘图
        /**   drawBitmap_API
         bitmap	The bitmap to be drawn
         left	The position of the left side of the bitmap being drawn
         top	The position of the top side of the bitmap being drawn
         paint	The paint used to draw the bitmap (may be null)*/
        canvas.drawPath(path, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);//保存Canvas的状态
        canvas.restore();//恢复canver之前保存的状态 ，防止保存后对canver执行的操作对后续的绘制有影响
        //super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN://按下时  只是将该点坐标设置为起点坐标
                path.moveTo(x,y);//将这个点设置为路径的起点
                preX = x;//将x值赋给 起始X坐标
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE://手势(ACTION_DOWN和ACTION_UP之间)。包含最近点的运动，以及任何中间点自上一下按下或移动事件。
                float dx = Math.abs(x-preX);//取绝对值赋给dx
                float dy = Math.abs(y-preY);
                if(dx >= 5 || dy>=5)//判断是否在允许的范围内
                {
                    path.quadTo(preX,preY,(x+preX)/2,(y+preY)/2);
                    /***添加一种二次贝塞尔曲线，从过去点，逼近控制点(x1,y1)和结束(x2,y2)。
                     * 如果没有moveTo()调用了这个轮廓，首先自动设置为(00)*/
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP://位置以及任何中间点从过去或移动事件后，一个按下的动作完成了。
                cacheCanvas.drawPath(path,paint);//绘制路径
                path.reset();//复位
                break;
        }
        invalidate();//整个视图无效。 如果视图可见，onDraw(android.graphics.Canvas)将在未来某个时候调用。
        return true;//返回ture 表明处理方法已经处理该事件
    }
    /**Clear方法实现橡皮擦功能*/
    public void clear()
    {                         // 波特-达夫相交    相交模式       清理
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//设置图形重叠时的处理方式
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);//相交宽度
    }
    /*public void fillClear()
    {
           啥时候学会了就把这个洞补起来
    }*/
    public void save()
    {
        try {
            saveBitmap("myPicture");
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void saveBitmap(String fileName)throws IOException
    {
       File file = new File("/data/NewFile/"+fileName+".png");//创建文件对象   //如果需要写入SD卡中那么需要添加加权限
       file.createNewFile();//创建一个新文件
        FileOutputStream fileOS = new FileOutputStream(file);
       //将绘图内容压缩为Png格式输出到输出流的对象中
        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
        /**@param format   The format of the compressed image
         * @param quality  Hint to the compressor, 0-100. 0 meaning compress for
         *                 small size, 100 meaning compress for max quality. Some
         *                 formats, like PNG which is lossless, will ignore the
         *                 quality setting   为0？100？null?
         * @param stream   The outputstream to write the compressed data.
         * */
        fileOS.flush();  //将缓冲区中的数据全部写出到输出流中
        fileOS.close();  //关闭文件输出流对象
    }
}
