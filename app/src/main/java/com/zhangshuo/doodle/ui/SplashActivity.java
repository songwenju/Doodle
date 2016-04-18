package com.zhangshuo.doodle.ui;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangshuo.doodle.R;
import com.zhangshuo.doodle.base.BaseActivity;
import com.zhangshuo.doodle.util.StatusBarUtils;

/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity{

    private TextView mSplashTitle;
    private Button mLogin;
    private Button mRegister;
    private AlertDialog mRegisterDialog;
    private AlertDialog mLoginDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onInitView() {
        StatusBarUtils.from(this) //沉浸状态栏
                .setTransparentStatusbar(true)
                .setLightStatusBar(true)
                .process();
        mSplashTitle = (TextView) findViewById(R.id.splash_title);
        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        initDialog();
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        initRegisterDialog(builder);
        initLoginDialog(builder);
    }

    private void initLoginDialog(AlertDialog.Builder builder) {
        mLoginDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_login, null);
        //设置自定义的view
        mLoginDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText user = (EditText) dialogView.findViewById(R.id.et_user);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        LinearLayout forget_pwd =  (LinearLayout) dialogView.findViewById(R.id.forget_pwd);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStr = user.getText().toString().trim();
                String pwdStr = pwd.getText().toString().trim();

                if (TextUtils.isEmpty(userStr)) {
                    toast("用户不能为空！");
                } else if (!(CommonUtil.isEmail(userStr) || CommonUtil.isCorrectName(userStr))) {
                    toast("用户格式不正确！");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空！");
                } else if (!CommonUtil.isCorrectPwd(pwdStr)) {
                    toast("密码的格式不正确！");
                } else {
                    toast("请求服务器，验证用户信息是否正确");

                    mLoginDialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
            }
        });
    }

    private void initRegisterDialog(AlertDialog.Builder builder) {
        mRegisterDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_register, null);
        //设置自定义的view
        mRegisterDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText email = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        final EditText name = (EditText) dialogView.findViewById(R.id.et_name);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                String pwdStr = pwd.getText().toString().trim();
                String nameStr = name.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)) {
                    toast("邮箱不能为空！");
                } else if (!CommonUtil.isEmail(emailStr)) {
                    toast("邮箱格式不正确！");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空！");
                } else if (!CommonUtil.isCorrectPwd(pwdStr)) {
                    toast("密码的格式不正确！"); }
                else if (TextUtils.isEmpty(nameStr)) {
                    toast("昵称不能为空！");
                } else if (!CommonUtil.isCorrectName(nameStr)) {
                    toast("昵称的格式不正确！");
                } else {
                    toast("请求服务器，写入数据到数据库");
                    mRegisterDialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterDialog.dismiss();
            }
        });
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/childFont.ttf");
        mSplashTitle.setTypeface(custom_font);
    }

    @Override
    protected void onInitListener() {
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()){
            case R.id.login:
                mLoginDialog.show();
                break;
            case R.id.register:
                mRegisterDialog.show();
                break;
        }
    }


}
