package com.zhangshuo.doodle.util;

import com.zhangshuo.doodle.common.AppConstant;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： songwenju on 2016/4/17 22:24.
 * 邮箱： songwenju@outlook.com
 */
public class ServiceUtil {
    public static boolean loginCheck(String user,String pwd){
        Map<String,String> params = new HashMap<>();
        params.put("loginUser",user);
        params.put("loginPwd",pwd);

        try {
            return sendGETRequest(AppConstant.remoteLoginPath,params,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean sendGETRequest(String path,Map<String,String>params,
            String encode) throws IOException {
        StringBuilder url = new StringBuilder(path);
        url.append("?");
        for (Map.Entry<String,String> entry:params.entrySet()) {
            url.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(),encode))
                    .append("&");
        }
        url.deleteCharAt(url.length() - 1);
        HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        return conn.getResponseCode() == 200;
    }
}
