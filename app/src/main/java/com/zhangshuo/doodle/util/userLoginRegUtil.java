package com.zhangshuo.doodle.util;

import com.zhangshuo.doodle.Domain.User;
import com.zhangshuo.doodle.common.AppConstant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： songwenju on 2016/4/17 22:24.
 * 邮箱： songwenju@outlook.com
 */
public class UserLoginRegUtil {
    /**
     * 登录，并获得服务器登录信息
     *
     * @param user
     * @param pwd
     * @return
     */
    public static String getLoginStatus(String user, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("loginUser", user);
        params.put("loginPwd", pwd);

        try {
            return sendGETRequest(AppConstant.LOGIN_PATH, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String sendGETRequest(String path, Map<String, String> params,
                                         String encode) throws IOException {
        StringBuilder urlPath = new StringBuilder(path);
        urlPath.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlPath.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(), encode))
                    .append("&");
        }
        urlPath.deleteCharAt(urlPath.length() - 1);
        LogUtil.i("UserLoginRegUtil", urlPath.toString());
        URL url = new URL(urlPath.toString());
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            String info = StreamUtils.convertStreamToString(inputStream);

            LogUtil.i("UserLoginRegUtil", "info:" + info);
            if (info != null) {
                return info;
            } else {
                return "0";
            }
        } else {
            return "-1";
        }
    }

    /**
     * 注册，并获得注册信息
     *
     * @param user
     * @return
     */
    public static String getRegisterStatus(User user) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", user.name);
        params.put("email", user.email);
        params.put("pwd", user.pwd);
        try {
            return sendGETRequest(AppConstant.REGISTER_PATH, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得写入服务器后返回的代码的状态
     * @param emailStr
     * @param code
     * @return
     */
    public static String getSetCodeStatus(String emailStr,String code) {
        Map<String, String> params = new HashMap<>();
        params.put("email", emailStr);
        params.put("checkCode", code);
        params.put("method","set");
        try {
            return sendGETRequest(AppConstant.CHECK_CODE_PATH, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得验证服务器校验码的返回状态
     * @param emailStr
     * @param code
     * @return
     */
    public static String getIsCodeStatus(String emailStr,String code) {
        Map<String, String> params = new HashMap<>();
        params.put("email", emailStr);
        params.put("checkCode", code);
        params.put("method","check");
        try {
            return sendGETRequest(AppConstant.CHECK_CODE_PATH, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得重置密码的服务器返回状态
     * @param emailStr
     * @param pwd
     * @return
     */
    public static String getResetPwdStatus(String emailStr,String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("email", emailStr);
        params.put("userPassword", pwd);
        try {
            return sendGETRequest(AppConstant.RESET_PWD_PATH, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
