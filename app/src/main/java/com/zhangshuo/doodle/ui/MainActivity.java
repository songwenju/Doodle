package com.zhangshuo.doodle.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangshuo.doodle.R;
import com.zhangshuo.doodle.weight.ColorPickerDialog;
import com.zhangshuo.doodle.weight.DrawView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static int[] colors = new int[]{
            R.color.user_icon_1,R.color.user_icon_2,R.color.user_icon_3,R.color.user_icon_4,
            R.color.user_icon_4,R.color.user_icon_6,R.color.user_icon_7,R.color.user_icon_8,
    };
    private DrawView drawView;
    private ScrollView colorLayout;
    private LinearLayout pen;
    private LinearLayout penColor;
    private LinearLayout penEraser;
    private SeekBar      penWidth;
    private LinearLayout penRotate;

    private ImageView iv_pen;
    private ImageView iv_eraser;
    private ImageView iv_color;
    private LinearLayout bottom;
    private TextView tv_penWidth;
    private LinearLayout eraserLayout;
    private LinearLayout rotateLayout;
    private ImageView iv_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    @SuppressWarnings("ConstantConditions")
    private void initEvent() {

        penWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1)
                    progress = 1;
                drawView.setPaintWidth(progress);
                tv_penWidth.setText(progress + "");
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
        //颜色选择区点击事件
        if(colorLayout!=null) {
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
    }

    private void initView() {
        //画布
        drawView = (DrawView)findViewById(R.id.drawActivity);

        /**
         * 底部按钮
         */
        bottom = ((LinearLayout) findViewById(R.id.bottom));
        //颜色选择面板
        colorLayout = ((ScrollView) findViewById(R.id.color_layout));
        colorLayout.setVisibility(View.GONE);
        //橡皮擦面板
        eraserLayout = ((LinearLayout) findViewById(R.id.eraser_layout));
        eraserLayout.setVisibility(View.GONE);
        //旋转面板
        rotateLayout = ((LinearLayout) findViewById(R.id.rotate_layout));
        rotateLayout.setVisibility(View.GONE);
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
        iv_rotate= (ImageView)findViewById(R.id.iv_rotate);
        //画笔宽度
        penWidth = ((SeekBar) findViewById(R.id.seekBar));
        tv_penWidth = ((TextView) findViewById(R.id.pen_width));

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

//        drawView.paint.setXfermode(null);//取消擦出效果
//        drawView.paint.setStrokeWidth(1);//初始化画笔的宽度

        switch (item.getItemId())//返回菜单项的标识符
        {
            /***
             * 设置paint颜色
             */
            case R.id.red:
                drawView.paint.setColor(Color.RED);
                item.setChecked(true);
                break;
            case R.id.blue:
                drawView.paint.setColor(Color.BLUE);
                item.setChecked(true);
                break;
            case R.id.green:
                drawView.paint.setColor(Color.GREEN);
                item.setChecked(true);
                break;
            /***
             * 设置笔宽
             */
            case R.id.width_1:
                drawView.paint.setStrokeWidth(1);
                item.setChecked(true);
                break;
            case R.id.width_5:
                drawView.paint.setStrokeWidth(5);
                item.setChecked(true);
                break;
            case R.id.width_10:
                drawView.paint.setStrokeWidth(10);
                item.setChecked(true);
                break;
            //导入图片
            case R.id.insert:

                Toast.makeText(this,"导入图片",Toast.LENGTH_SHORT).show();

                break;
            //分享
            case R.id.share:
                sharePic();
                break;
            /**
             * 保存图片
             */
            case  R.id.save:
                savePic();
                break;
        }
        return true;
    }

    /**
     * 保存图片
     */
    private void savePic() {
        Toast.makeText(this,"保存图片",Toast.LENGTH_SHORT).show();
    }

    /**
     * 分享图片
     */
    private void sharePic() {
        Toast.makeText(this,"分享",Toast.LENGTH_SHORT).show();
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
     * @param color 颜色值
     */
    private void setPaintColor(int color) {
        drawView.setPaintColor(color);
        iv_color.setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
        drawView.paint.setXfermode(null);//取消擦出效果
        colorLayout.setVisibility(View.GONE);
        eraserLayout.setVisibility(View.GONE);
        switch (v.getId()){
            case R.id.pen:
                if(View.VISIBLE == rotateLayout.getVisibility())
                    rotateLayout.setVisibility(View.GONE);
                iv_pen.setBackgroundResource(R.mipmap.icon_bg_pen);
                iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                iv_rotate.setBackgroundResource(R.mipmap.icon_bg_default);
                break;

            case R.id.eraser:
                if(View.VISIBLE == rotateLayout.getVisibility())
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
                if(View.VISIBLE!=rotateLayout.getVisibility()) {
                    iv_rotate.setBackgroundResource(R.mipmap.icon_bg_rotate);
                    iv_eraser.setBackgroundResource(R.mipmap.icon_bg_default);
                    iv_pen.setBackgroundResource(R.mipmap.icon_bg_default);
                    rotateLayout.setVisibility(View.VISIBLE);
                }else {
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
            case R.id.color:
                if(View.VISIBLE == rotateLayout.getVisibility())
                    rotateLayout.setVisibility(View.GONE);
                colorLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.drawActivity:
                Log.e("hide ","click");
                hideMenu();
                break;
            default:
                Log.e("hide ","default");
                setColor(v);
                break;
        }
    }

    /**
     * 旋转图片
     */
    private void rotateImage() {
        drawView.rotateImage();
        Log.e("MainActivity ","rotateImage");
    }

    /**
     * 图片镜像
     */
    private void ghostImage() {
        drawView.ghostImage();
        Log.e("MainActivity","gostImage");
    }

    /**
     * 获取颜色面板的点击事件
     * @param v 被点击的颜色
     */
    private void setColor(View v) {
        switch (v.getId()){
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
        Log.d("hideMenu","hideMenu = "+bottom.getVisibility());
        if(View.VISIBLE ==bottom.getVisibility()) {
            Log.d("hideMenu","true");
            bottom.setVisibility(View.GONE);
            penWidth.setVisibility(View.INVISIBLE);
        }else {
            Log.d("hideMenu","false");
            bottom.setVisibility(View.VISIBLE);
            penWidth.setVisibility(View.VISIBLE);
        }
    }
}
