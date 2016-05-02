package com.zhangshuo.doodle.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 邮箱验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isEmail(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        //验证邮箱
        p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    /**
     * 姓名验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isCorrectName(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        //验证邮箱
        p = Pattern.compile("^[a-zA-Z0-9_]{3,10}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 密码格式验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isCorrectPwd(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[a-zA-Z0-9_]{4,15}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
