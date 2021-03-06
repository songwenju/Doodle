package com.zhangshuo.doodle.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhangshuo.doodle.util.UIUitl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DrawView extends View {
    private final Path pathCache;
    private int view_width = 0;//屏幕宽度
    private int view_height = 0;//屏幕高度
    private float preX;//起始点的X坐标
    private float preY;//起始点的Y坐标
    private Path path;
    public Paint paint = null;
    Bitmap cacheBitmap = null;//定义一个内存中的图片，该图片将作为缓冲区
    Canvas cacheCanvas = null;//定义一个画布
    private float paintWidth = 1;//画笔宽度
    private int backColor = Color.BLACK;
    private int paintColor = Color.BLUE;
    private Paint bmpPaint ;
    private Bitmap lastBitmap;
    public boolean TOUCH_TYPE = TYPE_DRAW;

    private final static boolean TYPE_MOVE = false;
    private final static boolean TYPE_DRAW = true;

    //图像绘画起始坐标 左 上
    private float left = 0;
    private float top = 0;
    //实际bitmap 起始位置
    private float preXCache;
    private float preYCache;
    //上次的颜色
    private int lastColor;


    public DrawView(Context context, AttributeSet attributeSet)//写一个构造方法初始化类
    {
        super(context, attributeSet);

        cacheCanvas = new Canvas();
        /***
         *Bitmap.Config 	ALPHA_8 	Each pixel is stored as a single translucency (alpha) channel.
         Bitmap.Config 	ARGB_4444 	This field was deprecated in API level 13. Because of the poor quality of this configuration, it is advised to use ARGB_8888 instead.
         Bitmap.Config 	ARGB_8888 	Each pixel is stored on 4 bytes.
         Bitmap.Config 	RGB_565 	Each pixel is stored on 2 bytes and only the RGB channels are encoded: red is stored with 5 bits of precision (32 possible values), green is stored with 6 bits of precision (64 possible values) and blue is stored with 5 bits of precision.
         可以参考  http://blog.csdn.net/wulongtiantang/article/details/8481077
         */
        view_width = context.getResources().getDisplayMetrics().widthPixels;//获取屏幕的宽度   /*显示度量标准*宽像素
        view_height = context.getResources().getDisplayMetrics().widthPixels;
        //创建一个与该View相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(Color.WHITE);
        lastBitmap = cacheBitmap.copy(Bitmap.Config.RGB_565,true);
        path = new Path();
        pathCache = new Path();
//        cacheCanvas.setBitmap(cacheBitmap);//在cache的上边绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);//抗抖动
        /**Paint flag that enables dithering when blitting.
         Enabling this flag applies a dither to any blit operation
         where the target's colour space is more constrained than the source.*/
        paint.setColor(paintColor);
        //设置画笔风格
        paint.setStyle(Paint.Style.STROKE);//填充轮廓
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeWidth(paintWidth);//设置笔触宽度
        paint.setAntiAlias(true);//设置抗锯齿功能，稍微耗费内存
        paint.setDither(true);//使用抖动效果对手抖动结果进行处理
        bmpPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //此时 view 已经测量完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onDraw(Canvas canvas) {//重写绘制方法
        canvas.drawColor(backColor);//设置背景色
        canvas.drawBitmap(cacheBitmap, left, top, bmpPaint);//绘制cacheBitmap   //注释掉后画完一笔后不保留绘图
        /**   drawBitmap_API
         bitmap	The bitmap to be drawn
         left	The position of the left side of the bitmap being drawn
         top	The position of the top side of the bitmap being drawn
         paint	The paint used to draw the bitmap (may be null)*/
        canvas.drawPath(path, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);//保存Canvas的状态
        canvas.restore();//恢复canver之前保存的状态 ，防止保存后对canver执行的操作对后续的绘制有影响

    }
    //获取不到 第二个触摸点 废弃
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                TOUCH_TYPE = TYPE_DRAW;
//                LogUtil.e("MOTION_EVENT","主点按下");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN://第二点按下时
//                LogUtil.e("MOTION_EVENT","附点按下");
//                TOUCH_TYPE = TYPE_MOVE; //切换模式为平移
//                break;
//            default:
//                return super.dispatchTouchEvent(event);
//        }
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x ;
        float y ;
        float xCache = 0;
        float yCache = 0;
        if(TOUCH_TYPE) {
            x = event.getX();
            y = event.getY();
            xCache = x - left;
            yCache = y - top;
        }else {
            x = event.getX();
            y = event.getY();
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN://按下时  只是将该点坐标设置为起点坐标
                path.moveTo(x,y);//将这个点设置为路径的起点
                pathCache.moveTo(xCache,yCache);
                preX = x;//将x值赋给 起始X坐标
                preY = y;

                preXCache = xCache;
                preYCache = yCache;
                break;
            case MotionEvent.ACTION_MOVE://手势(ACTION_DOWN和ACTION_UP之间)。包含最近点的运动，以及任何中间点自上一下按下或移动事件。

                if(TOUCH_TYPE) {
                    float dx = Math.abs(x - preX);//取绝对值赋给dx
                    float dy = Math.abs(y - preY);
//                    if (dx >= 5 || dy >= 5)//判断是否在允许的范围内
//                    {
                        path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                        pathCache.quadTo(preXCache, preYCache, (xCache + preXCache) / 2, (yCache + preYCache) / 2);
                        /**添加一种二次贝塞尔曲线，从过去点，逼近控制点(x1,y1)和结束(x2,y2)。
                         * 如果没有moveTo()调用了这个轮廓，首先自动设置为(00)*/
                        preX = x;
                        preY = y;

                        preXCache = xCache;
                        preYCache = yCache;
//                    }
                }else {
                    float ddx = Math.abs(x - preX);//取绝对值赋给dx
                    float ddy = Math.abs(y - preY);
                    if (ddx >= 5 || ddy >= 5)//判断是否在允许的范围内
                    {
                        double dx = x - preX;
                        double dy = y - preY;
//                        LogUtil.e("MOTION_EVENT","dx: "+dx+" : "+"dy: "+dy);
                        left +=dx;
                        top +=dy;
                        preX = x;
                        preY = y;

                    }
                }
                break;
            case MotionEvent.ACTION_UP://位置以及任何中间点从过去或移动事件后，一个按下的动作完成了。
                cacheCanvas.drawPath(pathCache,paint);//绘制路径到缓存 bitmap
                path.reset();
                pathCache.reset();
//                TOUCH_TYPE = TYPE_DRAW;//模式复位
                break;
        }
        invalidate();//整个视图无效。 如果视图可见，onDraw(android.graphics.Canvas)将在未来某个时候调用。
        return true;//返回ture 表明处理方法已经处理该事件
    }
    /**Clear方法实现橡皮擦功能*/
    public void clear() {
        //用白色直接覆盖
        paint.setColor(Color.WHITE);
        paint.setMaskFilter(null);//清空画笔样式
    }

    public void clearFull(){
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if(lastBitmap !=null){
            setCacheBitmap(lastBitmap);
        }
        invalidate();
    }

    public void setPaintWidth(float paintWidth) {
        this.paintWidth = UIUitl.dip2px(paintWidth);
        paint.setStrokeWidth(UIUitl.dip2px(paintWidth));
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

    /**
     * 画布背景色
     * @param backColor 背景色
     */
    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    /**
     * 设置画笔颜色
     * @param paintColor 画笔颜色
     */
    public void setPaintColor(int paintColor) {
        lastColor = paintColor;
        this.paintColor = paintColor;
        paint.setColor(paintColor);
    }

    public void rotateImage() {
        this.cacheBitmap = getRotateBitmap(90, cacheBitmap);
        cacheCanvas.setBitmap(cacheBitmap);
        invalidate();
    }

    public void setCacheBitmap(Bitmap cacheBitmap) {
        pathCache.reset();
        path.reset();
        //此处 创建的bitmap 必须是可以更改的  否则 绘画无效
        lastBitmap = cacheBitmap.copy(Bitmap.Config.RGB_565,true);
        this.cacheBitmap = cacheBitmap.copy(Bitmap.Config.RGB_565,true);
//        cacheCanvas.drawBitmap(cacheBitmap,new Matrix(),bmpPaint);
        cacheCanvas.setBitmap(this.cacheBitmap);
        invalidate();
    }


    /**
     * 旋转图片
     * @param angle 旋转角度
     * @param bitmap 要处理的Bitmap
     * @return 处理后的Bitmap
     */
    public static Bitmap getRotateBitmap(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    /**
     * 翻转图片
     */
    public void ghostImage() {
        this.cacheBitmap = convertBmp(cacheBitmap,ConvertMode.LEVEL);
        cacheCanvas.setBitmap(cacheBitmap);
        invalidate();
    }

    /**
     * 图片翻转
     * @param bmp 原始图片
     * @param mode 翻转模式
     * @return 翻转后的图片
     */
    public Bitmap convertBmp(Bitmap bmp,ConvertMode mode) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Matrix matrix = new Matrix();
        if(mode == ConvertMode.LEVEL) {
            matrix.postScale(-1, 1); // 镜像水平翻转
        } else {
            matrix.postScale(1, -1);   //镜像垂直翻转
        }
        return Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
    }

    /**
     * 设置透明度
     * @param progress 透明度 0 ~ 255
     */
    public void setAlpha(int progress) {
        paint.setAlpha(progress);
    }

    /**
     * 翻转模式
     */
    public enum ConvertMode {
        VERTICAL,
        LEVEL,
    }

    /**
     * 获取bitmap
     *
     * @return 返回当前显示的bitmap
     */
    public Bitmap getCacheBitmap() {
        Bitmap bit = cacheBitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bit);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(cacheBitmap,new Matrix(),new Paint());
        return bit;
    }
    /**
     * * 功能：设置画笔风格
     * @param mPaintType 画笔风格 PEN_TYPE
     * @return MaskFilter
     */
    public MaskFilter setMaskFilter(int mPaintType){
        MaskFilter maskFilter = null;
        paint.setColor(lastColor);
        paint.setAlpha(255);
        switch (mPaintType) {
            case PEN_TYPE.PLAIN_PEN://签字笔风格
                maskFilter = null;
                break;
            case PEN_TYPE.BLUR://铅笔模糊风格
                maskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
                break;
            case PEN_TYPE.EMBOSS://毛笔浮雕风格
                maskFilter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
                break;
            case PEN_TYPE.FLUORESCENT://荧光
                maskFilter =new BlurMaskFilter(UIUitl.dip2px(5f), BlurMaskFilter.Blur.SOLID);
                break;
            case PEN_TYPE.TS_PEN://透明水彩风格
                maskFilter = null;
                paint.setAlpha(50);
                break;
            default:
                maskFilter = null;
                break;
        }
        paint.setMaskFilter(maskFilter);
        return maskFilter;
    }

    /**
     * 画笔可选风格
     */
    public interface PEN_TYPE{
        int PLAIN_PEN = 1;
        int BLUR = 2;
        int EMBOSS = 3;
        int TS_PEN = 4;
        int FLUORESCENT = 5;
    }
}