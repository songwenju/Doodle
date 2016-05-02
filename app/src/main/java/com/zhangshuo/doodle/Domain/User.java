package com.zhangshuo.doodle.Domain;

/**
 * 用户类
 */
public class User {
    public String name;
    public String pwd;
    public String email;

    public User(String name, String pwd, String email) {
        this.name = name;
        this.pwd = pwd;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
