package com.zhangshuo.doodle.util;

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
public class uerLoginRegUtil {
    public static String getLoginStatus(String user, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("loginUser", user);
        params.put("loginPwd", pwd);

        try {
            return sendGETRequest(AppConstant.remoteLoginPath, params, "UTF-8");
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
        LogUtil.i("LoginTask", urlPath.toString());
        URL url = new URL(urlPath.toString());
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            LogUtil.i("LoginTask", "进来了");
            InputStream inputStream = conn.getInputStream();
            String info = StreamUtils.convertStreamToString(inputStream);

            LogUtil.i("LoginTask", "info:" + info);
            if (info != null) {
               return info;
            }

        }
        return null;

    }

}
