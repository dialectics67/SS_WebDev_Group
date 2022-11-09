package com.example.helloworld.constants;


public class Consts {
    /**
     * 各种key的名字
     */
    public static String SESSION_KEY = "key:session:token";
    public static String SESSION_USER = "key:session:user"; //-> userEntity: UserEntity
    public static String SESSION_USER_ID = "key:session:user:id"; //-> id: Long
    public static String SESSION_USER_NAME = "key:session:user:name"; //-> id: Long
    public static String SESSION_USER_USERID = "key:session:user:userId";//-> userId: Long

    public static String COOKIE_SESSION_ID = "JSESSIONID";
    public static String COOKIE_USER = "cookie:user";//->userId: UserEntity
    public static String COOKIE_USER_USERID = "cookie:userId";//->userId: Long

    public static String TOKEN_USER_ID="key:token:user:id";
    public static String TOKEN_USER_NAME="key:token:user:name";

    public static Integer TEAM_MAX_LENGTH = 4;

}