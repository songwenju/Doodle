package com.zhangshuo.doodle.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.zhangshuo.doodle.R;
import com.zhangshuo.doodle.weight.DrawView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawView drawView = (DrawView)findViewById(R.id.drawActivity_1);
        drawView.paint.setXfermode(null);//取消擦出效果
        drawView.paint.setStrokeWidth(1);//初始化画笔的宽度

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
            case R.id.clear:
                drawView.clear();
                break;
            case R.id.fullClear:
                Toast.makeText(MainActivity.this, "Sorry  不知道咋弄了  用橡皮擦大宽度重写笔按说可以", Toast.LENGTH_SHORT).show();
                //drawActivity.fillClear();
                break;
            case  R.id.save:
                drawView.save();
                Toast.makeText(MainActivity.this,"貌似有错",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
