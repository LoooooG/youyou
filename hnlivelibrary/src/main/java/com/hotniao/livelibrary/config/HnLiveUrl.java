package com.hotniao.livelibrary.config;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播/私信模块的网络请求地址
 * 创建人：，mj
 * 创建时间：2017/9/13 10:16
 * 修改人：Administrator
 * 修改时间：2017/9/13 10:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveUrl {


    /**
     * 获取用户信息
     */
        public static final String Get_User_Info = "/user/profile/card";
    /**
     * 主播心跳 暂时为免费直播和vip直播需请求
     */
    public static final String Anchot_Heart_Beat = "/live/anchor/heartbeat";
    /**
     * 公共-发消息/弹幕
     */
    public static final String Send_Msg = "/live/room/sendChat";
    public static final String Send_Barrage = "/live/room/sendBarrage";
    /**
     * 公共-礼物列表 调用版本更新的接口
     */
    public static final String Gift_List = "/live/gift/index";
    /**
     * 消息列表
     */
    public static final String PRIVATE_LETTER_LIST = "/user/chat/dialogList";
    /**
     * 直播点赞接口
     */
    public static final String LIVE_ROOM_LIKE = "/live/room/like";
    /**
     * 忽略未读
     */
    public static final String IGNORE_NOTREAD = "/user/chat/ignoreUnread";


    /**
     * 删除私信
     */
    public static final String DESTORY_MSG = "/user/chat/deleteUserDialog";

    /**
     * 忽略消息
     */
    public static final String USER_CHAT_IGNORE_UNREAD = "/user/chat/ignoreUnread";

    /**
     * 发送私信
     */
    public static final String SEND_PRIVATELETTER = "/user/chat/send";
    /**
     * 私信详情
     */
    public static final String PRIVATELETTER_DETAIL = "/user/chat/dialog";


    /**
     * 关注
     */
    public static final String ADDFOLLOW = "/user/follow/add";
    /**
     * 取消关注
     */
    public static final String DELETE_FOLLOW = "/user/follow/delete";
    /**
     * 发送礼物
     */
    public static final String SEND_GIFT = "/live/gift/send";
    /**
     * 用户-点赞主播
     */
    public static final String Like = "/live/anchorLike";
    /**
     *门票  计时(每分钟)收费
     */
    public static final String Pay_Minute = "/live/pay/minute";
    /**
     * 门票(一次性)收费
     */
    public static final String Pay_Fare = "/live/pay/fare";
    /**
     * 用户-进入直播间
     */
    public static final String Join_To_Room = "/live/room/enter";
    /**
     * 用户-获取房间观众列表
     */
    public static final String LIVE_ROOM_USERLIST = "/live/room/userList";
    /**
     * 用户-离开直播间
     */
    public static final String Leave_To_Room = "/live/room/leave";
    /**
     * 乐播排行榜
     */
    public static final String LIVE_RANK_FIREBIRD = "/live/ranking/firebird";
    /**
     * 礼物
     */
    public static final String LIVE_GIFT_INDEX = "/live/gift/index";
    /**
     * 获取举报内容列表
     */
    public static final String LIVE_REPORT_INDEX= "/live/report/index";
    /**
     * 举报
     */
    public static final String LIVE_REPORT_ROOM= "/live/report/room";
    /**
     * 取消/添加管理员
     */
    public static final String LIVE_ANCHOR_DELROOM_ADMIN= "/live/roomAdmin/delete";
    public static final String LIVE_ANCHOR_ADDROOM_ADMIN= "/live/roomAdmin/add";
    /**
     *禁言
     */
    public static final String LIVE_ROOM_PROHIBIT_TALK= "/live/room/prohibitTalk";
    /**
     *踢人
     */
    public static final String LIVE_ROOM_KICK= "/live/room/kick";
    /**
     *直播分享成功
     */
    public static final String LIVE_ROOM_ADDSHARE_LOG= "/live/room/addShareLog";
    public static final String LIVE_ANCHOR_ADDSHARE_LOG= "/live/anchor/addShareLog";

    /**
     * 公共获取用户余额
     */
    public final static String USER_BALANCE = "/user/profile/balance";

}
