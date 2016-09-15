package org.thiki.kanban.user;

/**
 * Created by xubt on 9/14/16.
 */
public enum UsersCodes {
    USERNAME_IS_ALREADY_EXISTS(10011, "用户名已经被使用,请使用其他用户名。"),
    EMAIL_IS_ALREADY_EXISTS(10012, "邮箱已经存在,请使用其他邮箱。");
    public static final String identityIsRequired = "请提供用户标识:用户名或者密码。";
    public static final String emailIsRequired = "邮箱不可以为空。";
    public static final String userNameIsRequired = "用户名不可以为空。";
    public static final String md5PasswordIsRequired = "MD5密码不可以为空。";
    private int code;
    private String message;

    UsersCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}