package com.hn.library.global;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：常量配置
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnConstants {


    public static final String MUSIC_PATH = "HnMp3Dir";
    public static final String MUSIC_DATD = "MUSIC_DATD";
    public static final String IMAGE_CDN_PATH = "IMAGE_CDN_PATH";

    public static final String PERMISSION_SIZE= "PERMISSION_SIZE";
    public static final String NICK_NAME = "nick_name";
    public static final String CHAT_ROOM_ID = "chat_room_id";
    /**
     * 时间
     */
    public static final class Time {
        //TODO 60000  1分钟
        public static int COUNTDOWN = 60000; //倒计时
    }

    /**
     * SharedPreferences 参数
     */
    public static final class Sp {

        //从cookie里面获取的 uid
        public static String USER_ID = "USER_ID";
        public static String SHARE_PLAT = "SHARE_PLAT";
    }


    /**
     * intent
     */
    public static final class Intent {

        //相关话题 title
        public static String TOPIC_INFO = "TOPIC_INFO";
        //相关话题 id
        public static String TOPIC_INFO_ID = "TOPIC_INFO_ID";
        //主播ID
        public static String ANCHOR_ID = "ANCHOR_ID";
        //用户id
        public static String USER_ID = "USER_ID";
        //用户昵称
        public static String USER_NAME = "USER_NAME";
        //用户简介
        public static String USER_INTRO = "USER_INTRO";
        //用户头像链接
        public static String USER_HEADIMG = "USER_HEADIMG";
        //用户V银
        public static String USER_SILVER = "USER_SILVER";
        //搜索字段
        public static String USER_SEARCH = "USER_SEARCH";
        //房间id
        public static String ROOM_ID = "ROOM_ID";
        //轮播图链接
        public static String CATOUSE_URL = "CATOUSE_URL";
        //轮播图title
        public static String CATOUSE_TITLE = "CATOUSE_TITLE";
        //数据
        public static String DATA = "DATA";
        //手机号码
        public static String Phone = "Phone";
        //key
        public static String Key = "Key";
        //VIP是否过期
        public static String Expire = "Expire";
        //标题
        public static String Title = "Title";
        //url
        public static String Url = "Url";
        //状态
        public static String State = "State";
        //金额
        public static String Money = "Money";
        //优票
        public static String Dot = "Dot";
        //理由
        public static String Reason = "Reason";
        //类型
        public static String Type = "Type";
        //table_name
        public static String Name = "Name";

        public static String ChatRoomId = "ChatRoomId";
        //比例
        public static String Rate = "Rate";
    }

    /**
     * 用户设置
     */
    public static final class Setting {
        //判断用户是否是第一次使用
        public static final String SPLASH_FIRST_USE = "SPLASH_FIRST_USE";
        //配置信息
        public static final String USER_CONFIG_MSG = "USER_CONFIG_MSG";
        //直播分类
        public static final String LIVE_CATE = "LIVE_CATE";
    }

    /**
     * 第三方登录保存信息
     */
    public static final class LogInfo {

        public static String USERNAME = "USERNAME";
        public static String HEAD_IMG = "HEAD_IMG";
        public static String OPENID = "OPENID";
        public static String ACCESSTOKEN = "ACCESSTOKEN";
        public static String PLATFORMNAME = "PLATFORMNAME";
        public static String USER_PHONE = "USER_PHONE";
        public static String USER_PASSWORD = "user_password";
        public static String UID = "UID";
        public static String TOKEN = "TOKEN";
    }


    /**
     * 支付的appId
     */
    public static final class Pay {
        /*
         *微信支付APP_ID
         */
        public static final String WEIXINPAY_APPID = "WEIXINPAY_APPID";
    }

    /**
     * 发送eventbus
     */
    public static final class EventBus {
        //微信支付APP_ID
        public static final String WEIXINPAY_APPID = "WEIXINPAY_APPID";
        //更新用户头像
        public static final String Update_User_Header = "Update_User_Header";
        //更新用户昵称
        public static final String Update_User_Nick = "Update_User_Nick";
        //更新用户头像
        public static final String Update_User_Sex = "Update_User_Sex";
        //更新用户签名
        public static final String Update_User_Intro = "Update_User_Intro";
        //保存昵称
        public static final String Sava_Nick = "Sava_Nick";
        //保存签名
        public static final String Sava_Intro = "Sava_Intro";
        //支付宝账号
        public static final String Alipay_Account = "Alipay_Account";
        //支付宝解绑成功
        public static final String Unbind_Alipay_Success = "Unbind_Alipay_Success";
        //退出
        public static final String Logout = "Logout";
        //设置开播提醒
        public static final String Set_Live_Notify = "Set_Live_Notify";
        //充值
        public static final String Recharge = "Recharge";
        //购买vip
        public static final String Buy_Vip = "Buy_Vip";
        //更新账户余额
        public static final String Update_Coint = "Update_Coint";
        //更新VIP时间
        public static final String Update_Expire = "Update_Expire";
        //撤销提现申请
        public static final String Withdrawal_Again = "Withdrawal_Again";
        //u票金额
        public static final String Update_U_Piao = "Update_U_Piao";
        //重置
        public static final String Reset = "Resset";
        //清除消息数
        public static final String Clean_Unread = "Clean_Unread";
        public static final String Update_Msg = "Updata_Msg";
        //用户信息
        public static final String User_Info = "User_Info";
        //所有未读消息数
        public static final String Update_Unread_Count = "Update_Unread_Count";
        //切换热门fragment
        public static final String Switch_Fragment = "Switch_Fragment";
        //切换我的fragment
        public static final String Switch_Fragment_Mine = "Switch_Fragment_Mine";
        //更新提现列表
        public static final String Update_Withdraw_List = "Update_Withdraw_List";
        // 登陆失效
        public static final String LoginFailure = "LoginFailure";
        // 刷新直播列表
        public static final String RefreshLiveList = "RefreshLiveList";
        // 刷新视频列表
        public static final String RefreshVideoList = "RefreshVideoList";
        // 刷新视频评论列表
        public static final String RefreshVideoCommList = "RefreshVideoCommList";
        // 刷新视频认证状态
        public static final String RefreshVideoAuthStatue = "RefreshVideoAuthStatue";

        // 发布视频刷新视频列表
        public static final String RefreshVideoMineList = "RefreshVideoMineList";
        // 选择简介视屏
        public static final String ChooseChatVideo = "ChooseChatVideo";
    }
}
