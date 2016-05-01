package com.zhangshuo.doodle.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhangshuo.doodle.Domain.User;
import com.zhangshuo.doodle.R;
import com.zhangshuo.doodle.base.BaseActivity;
import com.zhangshuo.doodle.common.AppConstant;
import com.zhangshuo.doodle.util.LogUtil;
import com.zhangshuo.doodle.util.SpUtil;
import com.zhangshuo.doodle.util.StatusBarUtils;
import com.zhangshuo.doodle.util.UserLoginRegUtil;

/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity {
    private TextView mSplashTitle;
    private Button mLogin;
    private Button mRegister;
    private AlertDialog mRegisterDialog;
    private AlertDialog mLoginDialog;
    private AlertDialog mSetCheckCodeDialog;
    private AlertDialog mIsCheckCodeDialog;

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
        enterMainActivity();
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        initRegisterDialog(builder);
        initLoginDialog(builder);
    }

    /**
     * 显示重置密码的对话框
     * @param email
     */
    private void showResetPwdDialog(final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog resetPwdDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_reset_pwd, null);
        //设置自定义的view
        resetPwdDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        final EditText pwdAgain = (EditText) dialogView.findViewById(R.id.et_pwd_again);

        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb_progress);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdStr = pwd.getText().toString().trim();
                String pwdAgainStr = pwdAgain.getText().toString().trim();

               if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空");
                } else if (!CommonUtil.isCorrectPwd(pwdStr)) {
                    toast("密码的格式不正确");
                } else if (TextUtils.isEmpty(pwdAgainStr)) {
                    toast("再次输入的密码不能为空");
                } else if (!CommonUtil.isCorrectPwd(pwdAgainStr)) {
                    toast("再次输入的密码格式不正确");
               } else if (!pwdStr.equals(pwdAgainStr)) {
                    toast("两次输入的密码不一致");
                } else {
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        @Override
                        protected String doInBackground(String... params) {
                            LogUtil.i("swj", "emailStr:" + params[0] + " pwdStr:" + params[1]);
                            return UserLoginRegUtil.getResetPwdStatus(params[0],params[1]);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            progressBar.setVisibility(View.GONE);
                            if ("-1".equals(result)) {
                                toast("网络连接异常");
                            } else if ("0".equals(result)) {
                                toast("服务器有误");
                            } else if ("2".equals(result)) {
                                mLoginDialog.dismiss();
                                enterMainActivity();
                                SpUtil.putString(AppConstant.LOGIN_USER,email);
                                resetPwdDialog.dismiss();
                            }
                        }
                    }.execute(email,pwdStr);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwdDialog.dismiss();
            }
        });
        resetPwdDialog.show();
    }


    /**
     * 显示设置验证码的dialog
     * @param inputEmail
     */
   private void showSetCheckCodeDialog(String inputEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mSetCheckCodeDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_set_check_code, null);
        //设置自定义的view
        mSetCheckCodeDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText email = (EditText) dialogView.findViewById(R.id.et_email);
        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb_progress);
        if (!TextUtils.isEmpty(inputEmail)){
            email.setText(inputEmail);
        }
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = email.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)) {
                    toast("邮箱不能为空");
                } else if (!(CommonUtil.isEmail(emailStr))) {
                    toast("邮箱格式不正确");
                } else {
                    String codeStr = String.valueOf((int)(Math.random()*10000));
                    LogUtil.i(this,"checkCode:"+codeStr);
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            LogUtil.i("swj", "emailStr:" + params[0] + " code:" + params[1]);
                            return UserLoginRegUtil.getSetCodeStatus(params[0], params[1]);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            progressBar.setVisibility(View.GONE);
                            super.onPostExecute(result);
                            if ("-1".equals(result)) {
                               toast("网络连接异常");
                            } else if ("0".equals(result)||"3".equals(result)) {
                                //验证失败
                                toast("服务器异常");
                            } else if ("1".equals(result)) {
                                toast("该邮箱未注册");
                            } else if ("2".equals(result)) {
                                mSetCheckCodeDialog.dismiss();
                                //显示验证邮箱的对话框
                                showIsCheckCodeDialog(emailStr);
                            }
                        }

                    }.execute(emailStr, codeStr);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetCheckCodeDialog.dismiss();
            }
        });
        mSetCheckCodeDialog.show();
    }

    /**
     * 显示校验验证码的dialog
     * @param inputEmail
     */
    private void showIsCheckCodeDialog(String inputEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mIsCheckCodeDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_is_check_code, null);
        //设置自定义的view
        mIsCheckCodeDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText email = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText checkCode = (EditText) dialogView.findViewById(R.id.et_code);
        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb_progress);
        if (!TextUtils.isEmpty(inputEmail)){
            email.setText(inputEmail);
        }
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = email.getText().toString().trim();
                String codeStr = checkCode.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)) {
                    toast("邮箱不能为空");
                } else if (!(CommonUtil.isEmail(emailStr))) {
                    toast("邮箱格式不正确");
                } else if (TextUtils.isEmpty(codeStr)) {
                    toast("校验码不能为空");
                } else {

                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            LogUtil.i("swj", "emailStr:" + params[0] + " code:" + params[1]);
                            return UserLoginRegUtil.getIsCodeStatus(params[0], params[1]);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            progressBar.setVisibility(View.GONE);
                            super.onPostExecute(result);
                            if ("-1".equals(result)) {
                                toast("网络连接异常");
                            } else if ("0".equals(result)) {
                                //验证失败
                                toast("服务器异常");
                            } else if ("1".equals(result)) {
                                toast("该邮箱未注册");
                            } else if ("2".equals(result)) {
                                mIsCheckCodeDialog.dismiss();
                                //显示修改密码的对话框
                                //此时用户信息已经确定，设置为登录状态
                                SpUtil.putString(AppConstant.LOGIN_USER,emailStr);
                                showResetPwdDialog(emailStr);

                            }else if ("3".equals(result)){
                                toast("校验码错误");
                            }
                        }

                    }.execute(emailStr, codeStr);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheckCodeDialog.dismiss();
            }
        });
        mIsCheckCodeDialog.show();
    }
    /**
     * 初始化登录的dialog
     * @param builder
     */
    private void initLoginDialog(AlertDialog.Builder builder) {
        mLoginDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_login, null);
        //设置自定义的view
        mLoginDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText user = (EditText) dialogView.findViewById(R.id.et_user);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        LinearLayout forget_pwd = (LinearLayout) dialogView.findViewById(R.id.forget_pwd);
        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb_progress);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
                String userStr = user.getText().toString().trim();
                if (CommonUtil.isEmail(userStr)){
                    showSetCheckCodeDialog(userStr);
                }else {
                    showSetCheckCodeDialog("");
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userStr = user.getText().toString().trim();
                String pwdStr = pwd.getText().toString().trim();

                if (TextUtils.isEmpty(userStr)) {
                    toast("用户不能为空");
                } else if (!(CommonUtil.isEmail(userStr) || CommonUtil.isCorrectName(userStr))) {
                    toast("用户格式不正确");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空");
                } else if (!CommonUtil.isCorrectPwd(pwdStr)) {
                    toast("密码的格式不正确");
                } else {
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            LogUtil.i("swj", "userStr:" + params[0] + " pwdStr:" + params[1]);
                            return UserLoginRegUtil.getLoginStatus(params[0], params[1]);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            progressBar.setVisibility(View.GONE);
                            super.onPostExecute(result);
                            if ("2".equals(result)) {
                                mLoginDialog.dismiss();
                                toast("登录成功");
                                SpUtil.putString(AppConstant.LOGIN_USER,userStr);
                               enterMainActivity();
                            } else if ("1".equals(result)) {
                                //验证失败
                                toast("用户不存在");
                            } else if ("3".equals(result)) {
                                toast("密码错误");
                            } else if ("-1".equals(result)) {
                                toast("网络连接异常");
                            } else if ("0".equals(result)) {
                                toast("登录失败，服务器异常");
                            }
                        }

                    }.execute(userStr, pwdStr);


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

    /**
     * 初始化注册的对话框
     * @param builder
     */
    private void initRegisterDialog(AlertDialog.Builder builder) {
        mRegisterDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_register, null);
        //设置自定义的view
        mRegisterDialog.setView(dialogView, 0, 0, 0, 0);

        //初始化控件
        final EditText email = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        final EditText name = (EditText) dialogView.findViewById(R.id.et_name);
        final EditText pwdAgain = (EditText) dialogView.findViewById(R.id.et_pwd_again);
        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb_progress);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString().trim();
                final String emailStr = email.getText().toString().trim();
                String pwdStr = pwd.getText().toString().trim();
                String pwdAgainStr = pwdAgain.getText().toString().trim();
                if (TextUtils.isEmpty(nameStr)) {
                    toast("昵称不能为空！");
                } else if (!CommonUtil.isCorrectName(nameStr)) {
                    toast("昵称的格式不正确！");
                } else if (TextUtils.isEmpty(emailStr)) {
                    toast("邮箱不能为空！");
                } else if (!CommonUtil.isEmail(emailStr)) {
                    toast("邮箱格式不正确！");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空！");
                } else if (!CommonUtil.isCorrectPwd(pwdStr)) {
                    toast("密码的格式不正确！");
                } else if (TextUtils.isEmpty(pwdAgainStr)) {
                    toast("再次输入的密码不能为空");
                } else if (!CommonUtil.isCorrectPwd(pwdAgainStr)) {
                    toast("再次输入的密码格式不正确");
                } else if (!pwdStr.equals(pwdAgainStr)) {
                    toast("两次输入的密码不一致");
                } else {

                    User user = new User(nameStr, pwdStr, emailStr);
                    new AsyncTask<User, Void, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected String doInBackground(User... params) {

                            return UserLoginRegUtil.getRegisterStatus(params[0]);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            progressBar.setVisibility(View.GONE);
                            if ("-1".equals(result)) {
                                toast("网络连接异常");
                            } else if ("0".equals(result)) {
                                toast("服务器有误");
                            } else if ("1".equals(result)) {
                                toast("邮箱已经存在");
                            } else if ("2".equals(result)) {
                                toast("用户名已经存在");
                            } else if ("3".equals(result)) {
                                mLoginDialog.dismiss();
                                toast("注册成功，欢迎使用随手涂鸦");
                                SpUtil.putString(AppConstant.LOGIN_USER,emailStr);
                                enterMainActivity();
                                mRegisterDialog.dismiss();
                            } else if ("4".equals(result)) {
                                toast("服务器有误");
                            }
                        }
                    }.execute(user);
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

    private void enterMainActivity() {
        if (!TextUtils.isEmpty(SpUtil.getString(AppConstant.LOGIN_USER,"root"))){
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            SplashActivity.this.finish();
        }
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
        switch (v.getId()) {
            case R.id.login:
                mLoginDialog.show();
                break;
            case R.id.register:
                mRegisterDialog.show();
                break;
        }
    }

}
