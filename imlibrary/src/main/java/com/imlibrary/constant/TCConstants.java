package com.imlibrary.constant;

/**
 * 静态函数
 */
public class TCConstants {


    /**
     * IM 互动消息类型
     */
    public static final int IMCMD_PAILN_TEXT = 1;   // 文本消息
    public static final int IMCMD_EXIT_LIVE = 3;   // 用户退出直播


    public static final int NO_LOGIN_CACHE = 1265;


    /**
     * 登录腾讯云信息保存
     */
    private static String identify;
    private static String userSig;
    private static String appid;
    private static String type;


    public static String getIdentify() {
        return identify;
    }

    public static void setIdentify(String identify) {
        TCConstants.identify = identify;
    }

    public static String getUserSig() {
        return userSig;
    }

    public static void setUserSig(String userSig) {
        TCConstants.userSig = userSig;
    }

    public static String getAppid() {
        return appid;
    }

    public static void setAppid(String appid) {
        TCConstants.appid = appid;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        TCConstants.type = type;
    }

}
