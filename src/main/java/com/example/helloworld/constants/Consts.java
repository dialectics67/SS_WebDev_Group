package com.example.helloworld.constants;


import org.springframework.stereotype.Component;

@Component
public class Consts {
    /**
     * 存储基本常量的值
     */
    public static int IS_VALID = 1; //数据库的is_valid为有效所对应的值
    public static int IS_NOT_VALID = 0; //数据库的is_valid为无效所对应的值
    public static int IS_DEL = 1; //数据库的is_del表示删除所对应的值
    public static int IS_NOT_DEL = 0; //数据库的is_del表示没有删除所对应的值

    public static int MAX_LEN_USERNAME = 50;  // 用户名的最大长度
    public static int MAX_LEN_PASSWORD = 50; // 明文密码的最大长度
    public static int MAX_LEN_EX_PASSWORD = 32; // 密文密码的长度，MD5加密固定为128bit,32位字符串

    public static String SESSION_KEY = "key:session:token";
    public static String SESSION_USER = "key:session:user"; //-> userEntity: UserEntity
    public static String SESSION_USERS_UID = "key:session:user:id"; //-> id: Long
    public static String SESSION_USERS_NAME = "key:session:user:name"; //-> id: Long
    public static String SESSION_USER_USERID = "key:session:user:userId";//-> userId: Long

    public static String COOKIE_SESSION_ID = "JSESSIONID";
    public static String COOKIE_USER = "cookie:user";//->userId: UserEntity
    public static String COOKIE_USER_USERID = "cookie:userId";//->userId: Long

    public static String TOKEN_USERS_UID = "key:token:user:id";
    public static String TOKEN_USERS_NAME = "key:token:user:name";

    public static Integer TEAM_MAX_LENGTH = 4;

}