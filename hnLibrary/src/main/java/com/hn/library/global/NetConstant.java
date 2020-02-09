package com.hn.library.global;

import android.net.Uri;
import android.text.TextUtils;

import com.hn.library.utils.HnPrefUtils;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类描述：网络请求常量
 * 创建人：Kevin
 * 创建时间：2016/4/26 10:53
 * 修改人：Kevin
 * 修改时间：2016/4/26 10:53
 * 修改备注：
 * Version: 1.0.0
 */
public class NetConstant {

    public final static int REQUEST_NET_ERROR = 0x2; //网络异常
    public final static int RESPONSE_CODE_ERR = 0x3;//网络请求失败
    public final static int RESPONSE_CODE_TIME_OUT = 0x4;//请求刷新
    public final static int RESPONSE_CODE_BAD = 0x5;//服务器数据异常
    public final static int REQUEST_CODE_FAILURE = 201;//业务请求失败
    public final static int REQUEST_CODE_LOGIN_ERR = 403;//没有登录或已失效
    /**
     * 配置信息缓存
     */
    public final static String PX_CONFIG_CACHE_FILE = "px_config_cache_file";//缓存配置信息的文件
    public final static String PX_CONFIG_CACHE_IS_FIRST_RUN = "px_config_cache_is_first_run";//第一次运行，卸载后再安装运行也默认为第一次运行
    public final static String PX_CONFIG_CACHE_DEVICE_ID = "px_config_cache_device_id";//手机设备id
    public final static String PX_CONFIG_CACHE_NET_WORK = "px_config_cache_net_work";//当前网络类型
    public final static String PX_CONFIG_CACHE_DEVICE_MODEL = "px_config_cache_device_model";//手机设备型号
    /**
     * 登录用户信息缓存
     */
    public final static String PX_USER_CACHE_FILE = "px_user_cache_file";
    public final static String PX_USER_CACHE_FILE_TOKEN = "px_user_cache_token";//token
    /**
     * 用户直播分享选中默认
     */
    public final static String LIVE_SHARE_CHOOSER = "live_share_choose";//token
    /**
     * 用户直播分享第一次选中默认
     */
    public final static String LIVE_SHARE_CHOOSER_FIRST = "live_share_choose_first";//token
    /**
     * 接口服务器信息
     */

    //接口服务器
//    public final static String BASE_SERVER = "http://yyapi.youx1436.com";
    public final static String BASE_SERVER = "http://api.haision-industry.com";
//    public final static String BASE_SERVER = "http://2534y9g406.qicp.vip";
    public final static String SERVER = BASE_SERVER + "/v1";
    //文件服务器
//    public final static String BASE_FILE_SERVER = "http://yystatic.youx1436.com";
    public final static String BASE_FILE_SERVER = "http://static.haision-industry.com";
//    public final static String BASE_FILE_SERVER = "http://temmmm.imwork.net";
    public final static String FILE_SERVER = BASE_FILE_SERVER + "/upload/";//文件服务器
    public final static String FILE_UPLOAD_IMG_API = BASE_FILE_SERVER + "/upload_img.php";// 上传接口
    public final static String FILE_UPLOAD_API = BASE_FILE_SERVER + "/upload.php";// 视频上传接口
    public final static String FILE_UPLOAD_API_TEN = BASE_FILE_SERVER + "/qcloud.php";// 腾讯上传接口签名获取
    public final static String DEFRLT_IMG = BASE_SERVER + "/assets/images/logo.png";// 默认图片

    public static Uri setImageUri(String url) {
        if (TextUtils.isEmpty(url)) return Uri.parse("111111");//为null时防止返回null
        if (!url.startsWith("http")) {
            return Uri.parse(FILE_SERVER + url);
        } else if (url.contains(HnPrefUtils.getString(HnConstants.IMAGE_CDN_PATH, ""))) {
            return Uri.parse(url + "?imageView2/0/w/400/h/400");
        } else {
            return Uri.parse(url);
        }
    }

    public static String setImageUrl(String url) {
        if (TextUtils.isEmpty(url)) return null;
        if (url.contains("http")) {
            return url;
        }
        return FILE_SERVER + url;
    }

    /**
     * 存储用户信息
     */
    public static class User {
        public final static String UID = "UID";// 用户uid
        public final static String USER_INFO = "USER_INFO";// 用户信息
        public final static String IS_ANCHOR = "IS_ANCHOR";// 用户信息
        public final static String ANCHOR_CHAT_CATEGORY = "ANCHOR_CHAT_CATEGORY";// 私聊类型
        public final static String TOKEN = "TOKEN";// 用户token
        public final static String Webscket_Url = "Webscket_Url";// webscoket地址
        public final static String Unread_Count = "Unread_Count";// 未读聊天消息数
        public final static String User_Forbidden = "User_Forbidden";//账号禁用

    }

    /**
     * 全局配置数据 清单
     */
    public static class GlobalConfig {
        public final static String Coin = "Coin";//  可消费虚拟币名称
        public final static String Dot = "Dot";//  可提现虚拟币名称

    }
}
