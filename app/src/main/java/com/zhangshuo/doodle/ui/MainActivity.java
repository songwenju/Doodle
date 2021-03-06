package com.zhangshuo.doodle.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhangshuo.doodle.R;
import com.zhangshuo.doodle.common.AppConstant;
import com.zhangshuo.doodle.util.FileUtils;
import com.zhangshuo.doodle.util.LogUtil;
import com.zhangshuo.doodle.util.SpUtil;
import com.zhangshuo.doodle.util.ToastUtil;
import com.zhangshuo.doodle.util.UIUitl;
import com.zhangshuo.doodle.weight.ColorPickerDialog;
import com.zhangshuo.doodle.weight.DrawView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String Tag = MainActivity.class.getSimpleName();
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 10086;   //这里的IMAGE_CODE是自己任意定义的
    private static int[] colors = new int[]{
            R.color.user_icon_1, R.color.user_icon_2, R.color.user_icon_3, R.color.user_icon_4,
            R.color.user_icon_4, R.color.user_icon_6, R.color.user_icon_7, R.color.user_icon_8,
    };
    private DrawView drawView;
    private ScrollView colorLayout;
    private LinearLayout pen;
    private LinearLayout penColor;
    private LinearLayout penEraser;
    private SeekBar penWidth;
    private LinearLayout penRotate;
    private ImageView iv_pen;
    private ImageView iv_eraser;
    private ImageView iv_color;
    private LinearLayout bottom;
    private LinearLayout eraserLayout;
    private LinearLayout rotateLayout;
    private ImageView iv_rotate;
    private LinearLayout penLayout;
    private View wait;
    private SeekBar penAlpth;

    private Bitmap bm = null;
    private FrameLayout drawLayout;

    private AlertDialog mExistDialog;
    private String path;
    private TextView rotate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxa4489632bd8d69fd", "e4f7157924665f6ed032e1dad30de61f");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105294409", "qrqgmMU8Ez7DnQ4I");
        initView();
        initEvent();
    }

    private void initEvent() {
        penAlpth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1)
                    progress = 1;
                drawView.setAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        penWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1)
                    progress = 1;
                drawView.setPaintWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //底部菜单点击事件
        pen.setOnClickListener(this);
        penColor.setOnClickListener(this);
        penWidth.setOnClickListener(this);
        penEraser.setOnClickListener(this);
        penRotate.setOnClickListener(this);

        //橡皮选择区点击事件
        findViewById(R.id.eraser1).setOnClickListener(this);
        findViewById(R.id.eraser2).setOnClickListener(this);
        //旋转选择区点击事件
        findViewById(R.id.rotate1).setOnClickListener(this);
        findViewById(R.id.rotate2).setOnClickListener(this);
        findViewById(R.id.rotate3).setOnClickListener(this);
        //颜色选择区点击事件
        if (colorLayout != null) {
            findViewById(R.id.color_more).setOnClickListener(this);
            findViewById(R.id.color_1).setOnClickListener(this);
            findViewById(R.id.color_2).setOnClickListener(this);
            findViewById(R.id.color_3).setOnClickListener(this);
            findViewById(R.id.color_4).setOnClickListener(this);
            findViewById(R.id.color_5).setOnClickListener(this);
            findViewById(R.id.color_6).setOnClickListener(this);
            findViewById(R.id.color_7).setOnClickListener(this);
            findViewById(R.id.color_8).setOnClickListener(this);
        }
        //画笔选择区点击事件
        findViewById(R.id.pen1).setOnClickListener(this);
        findViewById(R.id.pen2).setOnClickListener(this);
        findViewById(R.id.pen3).setOnClickListener(this);
        findViewById(R.id.pen4).setOnClickListener(this);
        findViewById(R.id.pen5).setOnClickListener(this);
    }

    private void initView() {
        //画布
        drawView = (DrawView) findViewById(R.id.drawActivity);
        drawLayout = (FrameLayout) findViewById(R.id.draw_layout);
        drawView = (DrawView) findViewById(R.id.drawActivity);
        assert drawView != null;
        // 设置画布大小 与屏幕一样大 且为方形
        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) drawLayout.getLayoutParams());
        params.height = UIUitl.getWindowWidth();
        params.width = UIUitl.getWindowWidth();
        drawLayout.setLayoutParams(params);
        /**
         * 底部按钮
         */
        bottom = ((LinearLayout) findViewById(R.id.bottom));
        //颜色选择面板
        colorLayout = ((ScrollView) findViewById(R.id.color_layout));
        //橡皮擦面板
        eraserLayout = ((LinearLayout) findViewById(R.id.eraser_layout));
        //旋转面板
        rotateLayout = ((LinearLayout) findViewById(R.id.rotate_layout));
        //画笔面板
        penLayout = (LinearLayout) findViewById(R.id.pen_layout);
        //画笔
        pen = ((LinearLayout) findViewById(R.id.pen));
        iv_pen = ((ImageView) findViewById(R.id.iv_pen));//画笔图标
        //橡皮
        penEraser = ((LinearLayout) findViewById(R.id.eraser));
        iv_eraser = ((ImageView) findViewById(R.id.iv_eraser));//橡皮图标
        //画笔颜色
        penColor = ((LinearLayout) findViewById(R.id.color));
        iv_color = ((ImageView) findViewById(R.id.iv_color));//颜色图标
        //旋转
        penRotate = ((LinearLayout) findViewById(R.id.rotate));
        iv_rotate = (ImageView) findViewById(R.id.iv_rotate);
        //画笔宽度
        penWidth = ((SeekBar) findViewById(R.id.seekBar));
        //画笔透明度
        penAlpth = ((SeekBar) findViewById(R.id.seekBar1));

        //平移 绘画 切换
        rotate3 = ((TextView) findViewById(R.id.tv_rotate3));
        wait = findViewById(R.id.wait);
        initExistDialog();
    }

    /**
     * 初始化退出的dialog
     */
    private void initExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出登录");
        builder.setMessage("是否确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtil.putString(AppConstant.LOGIN_USER, "");
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", null);
        mExistDialog = builder.create();
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);//实例化一个MenuInflater对象 菜单填充器
        inflator.inflate(R.menu.tools, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //重写菜单方法
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){//返回菜单项的标识符
            //导入图片
            case R.id.insert:
                //开启系统相册选择界面
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
                ToastUtil.showToast("导入图片");
                break;
            case R.id.share:  //分享
                sharePic();
                break;
            case R.id.exit_login:
                existLogin();
                break;
            case R.id.save: //保存图片
                savePic();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e(Tag, "ActivityResult resultCode error");
            return;
        }
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == IMAGE_CODE) {
            try {

                Uri selectedImage = data.getData();
                String picturePath = selectedImage.getPath();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (null != cursor) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    picturePath = selectedImage.getPath();
                }
                Log.d(Tag, "picturePath = " + picturePath);
                bm = UIUitl.getSampledBitmap(picturePath, UIUitl.getWindowWidth(), UIUitl.getWindowWidth());
                drawView.setCacheBitmap(bm);
            } catch (Exception e) {
                Log.e(Tag, "从相册获取图片失败 : Exception " + e);
            }
        }
    }

    /**
     * 退出登录
     */
    private void existLogin() {
        mExistDialog.show();
    }

    /**
     * 保存图片
     */
    private void savePic() {
        Bitmap cacheBitmap = drawView.getCacheBitmap();
        new AsyncTask<Bitmap, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                wait.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                wait.setVisibility(View.GONE);
                path = s;
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.CHINA);
                return FileUtils.saveBitmap(params[0], format.format(new Date()));
            }

        }.execute(cacheBitmap);

    }

    /**
     * 分享图片
     */
    private void sharePic() {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
        };
        UMImage image;
        Bitmap bitmap = drawView.getCacheBitmap();
        if (bitmap != null) {
            image = new UMImage(this, bitmap);
        } else {
            image = new UMImage(this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }

        new ShareAction(this).setDisplayList(displaylist)
//                .withText("我从Doodle上分享了一张图片[随手涂鸦，快乐生活]")
//                .withTitle("Doodle分享")
//                .withTargetUrl("http://www.baidu.com")
                .withMedia(image)
                .setListenerList(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        LogUtil.i("share", platform.toString());
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        LogUtil.i("share", platform.toString() + t.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        LogUtil.i("share", platform.toString());
                    }
                })
                .open();
    }


    /**
     * 取色对话框
     */
    private void showColorPick() {

        ColorPickerDialog dialog = new ColorPickerDialog(this, R.style.dialog, new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                setPaintColor(color);
            }
        });
        dialog.show();
    }

    /**
     * 设置画笔颜色
     *
     * @param color 颜色值
     */
    private void setPaintColor(int color) {

        drawView.setPaintColor(color);
        iv_color.setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
        penAlpth.setProgress(255);//设置透明度 为255 不透明
        drawView.paint.setXfermode(null);//取消擦出效果
        colorLayout.setVisibility(View.GONE);
        eraserLayout.setVisibility(View.GONE);
        penLayout.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.pen:
                if (View.VISIBLE == rotateLayout.getVisibility())
                    rotateLayout.setVisibility(View.GONE);
                iv_pen.setBackgroundResource(R.mipmap.icon_bg_pen);
                iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                penLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.pen1:
                drawView.setMaskFilter(DrawView.PEN_TYPE.PLAIN_PEN);
                iv_pen.setImageResource(R.mipmap.icon_pencil);
                penLayout.setVisibility(View.GONE);
                break;
            case R.id.pen2:
                drawView.setMaskFilter(DrawView.PEN_TYPE.BLUR);
                iv_pen.setImageResource(R.mipmap.icon_blur);
                penLayout.setVisibility(View.GONE);
                break;
            case R.id.pen3:
                drawView.setMaskFilter(DrawView.PEN_TYPE.EMBOSS);
                iv_pen.setImageResource(R.mipmap.icon_emboss);
                penLayout.setVisibility(View.GONE);
                break;
            case R.id.pen4:
                drawView.setMaskFilter(DrawView.PEN_TYPE.TS_PEN);
                iv_pen.setImageResource(R.mipmap.icon_tspen);
                penLayout.setVisibility(View.GONE);
                break;
            case R.id.pen5:
                drawView.setMaskFilter(DrawView.PEN_TYPE.FLUORESCENT);
                iv_pen.setImageResource(R.mipmap.icon_fluorescent);
                penLayout.setVisibility(View.GONE);
                break;

            case R.id.eraser:
                if (View.VISIBLE == rotateLayout.getVisibility())
                    rotateLayout.setVisibility(View.GONE);
                iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                iv_eraser.setBackgroundResource(R.mipmap.icon_bg_eraser);
                iv_pen.setBackgroundResource(R.mipmap.icon_bg_default);
                eraserLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.eraser1:
                drawView.clear();
                break;
            case R.id.eraser2:
                iv_pen.setBackgroundResource(R.mipmap.icon_bg_pen);
                iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                drawView.clearFull();
                break;

            case R.id.rotate:
                if (View.VISIBLE != rotateLayout.getVisibility()) {
                    iv_rotate.setBackgroundResource(R.mipmap.icon_bg_rotate);
                    iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                    iv_pen.setBackgroundResource(R.mipmap.icon_bg_default);
                    rotateLayout.setVisibility(View.VISIBLE);
                } else {
                    iv_pen.setBackgroundResource(R.mipmap.icon_bg_pen);
                    iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                    iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                    rotateLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.rotate1:
                //镜像
                ghostImage();
                break;
            case R.id.rotate2:
                //旋转
                rotateImage();
                break;
            case R.id.rotate3:
                //绘制或者 平移
                drawView.TOUCH_TYPE = !drawView.TOUCH_TYPE;
                if (drawView.TOUCH_TYPE) {
                    rotate3.setText("绘画");
                } else {
                    rotate3.setText("平移");
                }
                break;
            case R.id.color:
                if (View.VISIBLE == rotateLayout.getVisibility())
                    rotateLayout.setVisibility(View.GONE);
                colorLayout.setVisibility(View.VISIBLE);
                iv_pen.setBackgroundResource(R.mipmap.icon_bg_pen);
                iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                break;
            case R.id.drawActivity:
                Log.e("hide ", "click");
                hideMenu();
                break;
            default:
                Log.e("hide ", "default");
                setColor(v);
                break;
        }
    }

    /**
     * 旋转图片
     */
    private void rotateImage() {
        drawView.rotateImage();
        Log.e("MainActivity ", "rotateImage");
    }

    /**
     * 图片镜像
     */
    private void ghostImage() {
        drawView.ghostImage();
        Log.e("MainActivity", "gostImage");
    }

    /**
     * 获取颜色面板的点击事件
     *
     * @param v 被点击的颜色
     */
    private void setColor(View v) {
        switch (v.getId()) {
            case R.id.color_1:
                setPaintColor(getResources().getColor(R.color.user_icon_1));
                break;
            case R.id.color_2:
                setPaintColor(getResources().getColor(R.color.user_icon_2));
                break;
            case R.id.color_3:
                setPaintColor(getResources().getColor(R.color.user_icon_3));
                break;
            case R.id.color_4:
                setPaintColor(getResources().getColor(R.color.user_icon_4));
                break;
            case R.id.color_5:
                setPaintColor(getResources().getColor(R.color.user_icon_5));
                break;
            case R.id.color_6:
                setPaintColor(getResources().getColor(R.color.user_icon_6));
                break;
            case R.id.color_7:
                setPaintColor(getResources().getColor(R.color.user_icon_7));
                break;
            case R.id.color_8:
                setPaintColor(getResources().getColor(R.color.user_icon_8));
                break;
            case R.id.color_more:
                showColorPick();
                break;
        }
    }

    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        Log.d("hideMenu", "hideMenu = " + bottom.getVisibility());
        if (View.VISIBLE == bottom.getVisibility()) {
            Log.d("hideMenu", "true");
            bottom.setVisibility(View.GONE);
            penWidth.setVisibility(View.INVISIBLE);
        } else {
            Log.d("hideMenu", "false");
            bottom.setVisibility(View.VISIBLE);
            penWidth.setVisibility(View.VISIBLE);
        }
    }
}
