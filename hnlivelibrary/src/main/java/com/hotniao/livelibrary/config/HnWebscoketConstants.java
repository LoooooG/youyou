package com.hotniao.livelibrary.config;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：标识webscoket的数据类型
 * 创建人：mj
 * 创建时间：2017/9/18 9:27
 * 修改人：Administrator
 * 修改时间：2017/9/18 9:27
 * 修改备注：
 * Version:  1.0.0
 */
public class HnWebscoketConstants {


    /**
     * 系统消息
     */
    public static final String System_Msg = "system_msg";
    /**
     * 私聊消息
     */
    public static final String Send_Pri_Msg = "send_chat";


    /**
     * 被挤下线
     */
    public static final String logout = "user_kill_online";
    /**
     * 用户账号禁用
     */
    public static final String User_Forbidden = "user_forbidden";

    /**
     * 禁播
     */
    public static final String Live_Forbidden = "kill_live";

    /**
     * 开播提醒
     */
    public static final String Live_Notify = "live_notify";
    /**
     * 主播获得房间收入
     */
    public static final String Anchor_Get_Room_Price = "anchor_get_room_price";
    /**
     * 弹幕
     */
    public static final String Barrage = "send_barrage";
    /**
     * 接收到发送礼物的数据
     */
    public static final String Send_Gift = "send_gift";
    /**
     * 用户离开
     */
    public static final String Leave = "leave";
    /**
     * 用户进入
     */
    public static final String Join = "join";
    /**
     * 机器人进入
     */
    public static final String Robot_Join = "robot_join";
    /**
     * 机器人离开
     */
    public static final String Robot_Leave = "robot_leave";
    /**
     * 接收到公聊消息
     */
    public static final String Public_Msg = "send_room_chat";
    /**
     * 等级提升
     */
    public static final String Level_Up = "level_up";
    /**
     * 主播停止直播
     */
    public static final String Stop_Live = "live_end";
    /**
     * 直播间真实在线人推送
     */
    public static final String Onlines = "room_users";
    /**
     * 点赞
     */
    public static final String Attitude = "send_like";
    /**
     * 公告
     */
    public static final String Notice = "notice";
    /**
     * 更新主播信息
     */
    public static final String Send_Anchor = "send_anchor";
    /**
     * 房管（单独）
     */
    public static final String Room_Admin = "room_admin";
    /**
     * 禁言
     */
    public static final String Prohibit_Talk = "prohibit_talk";
    /**
     * 踢出房间
     */
    public static final String Kick = "kick";
    /**
     * 禁播
     */
    public static final String Kill_Live = "kill_live";

    /**
     * 系统消息  升级或其他
     */
    public static final String System = "system";

    //#################################################  handler  ###########################################################
    /**
     * 公聊
     */
    public static final int Handler_Public_Msg = 1;
    /**
     * 更新主播信息
     */
    public static final int Handler_Send_Anchor = 2;
    /**
     * 用户进入
     */
    public static final int Handler_Join = 3;
    /**
     * 接收到发送礼物的数据
     */
    public static final int Handler_Send_Gift = 4;
    /**
     * 弹幕
     */
    public static final int Handler_Barrage = 5;

    /**
     * 主播停止直播
     */
    public static final int Handler_Stop_Live = 6;
    /**
     * 直播间真实在线人推送
     */
    public static final int Handler_Onlines = 7;
    /**
     * 禁言
     */
    public static final int Handler_Prohibit_Talk = 8;
    /**
     * 踢出房间
     */
    public static final int Handler_Kick = 9;
    /**
     * 点赞
     */
    public static final int Handler_Attitude = 10;
    /**
     * 房管
     */
    public static final int Handler_Room_Admin = 11;
    /**
     * 禁播
     */
    public static final int Handler_Kill_Live = 12;

    /**
     * 系统消息  升级或其他
     */
    public static final int Handler_System = 13;


    //#####################################      视频聊天       #########################
    /**
     * 视频邀请消息
     */
    public static final String Private_Chat_Video = "private_chat";
    /**
     * 取消一对一私聊推送
     */
    public static final String Private_Chat_Cancel = "cancel_private_chat";
    /**
     * 拒绝一对一私聊推送
     */
    public static final String Private_Chat_Refuse = "refuse_private_chat";
    /**
     * 接受一对一私聊推送
     */
    public static final String Private_Chat_Accept = "accept_private_chat";
    /**
     * 挂断一对一私聊推送
     */
    public static final String Private_Chat_HangUp = "hang_up_private_chat";
    /**
     * 模糊一对一私聊推送
     */
    public static final String Private_Chat_Vague = "vague_chat";
    /**
     * 余额
     */
    public static final String Push_Balance = "push_balance";

}
