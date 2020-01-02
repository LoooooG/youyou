package com.hotniao.livelibrary.config;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：直播/私信 全局变量类
 * 创建人：Administrator
 * 创建时间：2017/9/13 10:59
 * 修改人：Administrator
 * 修改时间：2017/9/13 10:59
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveConstants {

    /**
     * intent
     */
    public static final class Intent {

        //数据
        public static String DATA = "DATA";
        //name
        public static String  Name= "Name";
        public static String  ChatRoomId= "ChatRoomId";
    }

    /**
     * eventbus
     */
    public  static final class EventBus {
        //直播间返回按钮
        public static String  Live_Back= "Live_Back";
        //直播间切换摄像头
        public static String  Switch_Camera= "Switch_Camera";
        //美颜
        public static String  Opne_GL= "Opne_GL";
        //点击用户头像
        public static String  Click_User_Logo= "Click_User_Logo";
        //点击公聊
        public static String  Click_Public_Message= "Click_Public_Message";
        //清除未读数
        public static String  Clear_Unread_Count= "Clear_Unread_Count";
        //接到未读消息
        public static String  Receiver_New_Msg= "Receiver_New_Msg";
        //清楚未读消息
        public static String  Clear_Unread= "Clear_Unread";
        //重置数据
        public static String  Reset_Data= "Reset_Data";
        //关闭私信弹框
        public static String  Close_Private_Letter_Dialog= "Close_Private_Letter_Dialog";
        //时间
        public static String  Show_Time= "Show_Time";
        //正在聊天的uid
        public static String  Chatting_Uid= "Chatting_Uid";
        //用户信息
        public static String  User_Info= "User_Info";
        //关注
        public static String  Follow= "Follow";
        //更新用户的u票
        public static String  Update_User_Coin= "Update_User_Coin";
        //显示遮罩
        public static String  Show_Mask= "Show_Mask";
        //隐藏遮罩
        public static String  Hide_Mask= "Hide_Mask";
        //显示购买按钮
        public static String  Show_Buy= "Show_Buy";
        //隐藏购买按钮
        public static String  Hide_Buy= "Hide_Buy";
        //充值完成
        public static String  Excharge_Finish= "Excharge_Finish";
        //关闭dialog
        public static String  Close_Dialog= "Close_Dialog";
        //继续观看
        public static String  Countiune_Look= "Countiune_Look";
        //结束直播
        public static String  Leave_Live_Room= "Leave_Live_Room";
        //购买vip成功
        public static String  Buy_VIP_Success= "Buy_VIP_Success";
        //充值成功
        public static String  Pay_Success= "Pay_Success";
        //点击弹幕触发
        public static String  Click_Dan_Mu= "Click_Dan_Mu";
        //点击小礼物触发
        public static String  Click_Small_Gift= "Click_Small_Gift";
        //进入直播间
        public static String  Join_To_Room= "Join_To_Room";

        //禁止直播
        public static String  Live_Forbidden= "Live_Forbidden";
        //网络状态发生改变
        public static String  Net_Change= "Net_Change";
        //更新房间信息
        public static String  Update_Room_Info= "Update_Room_Info";
        //更新直播
        public static String  Update_Room_Live= "Update_Room_live";
        //是否可以滑动viewpager
        public static String  Scroll_viewPager= "Scroll_viewPager";
        //关闭软键盘
        public static String  Close_Soft= "Close_Soft";
        //更新礼物列表
        public static String  Update_Gift_List= "Update_Gift_List";
        //上传速度
        public static String  Update_Live_KBS= "Update_live_kbs";
        //下载gif成功
        public static String  Download_Gift_Gif_Success= "Download_Gift_Gif_Success";
        //推流状态显示
        public static String  Push_Stream_Statue= "Push_Stream_Statue";


        //看直播时点击通知
        public static String  Look_Live_Click_Notify= "Look_Live_Click_Notify";

        //直播时点击通知
        public static String  Liveing_Click_Notify= "Liveing_Click_Notify";

        //显示蒙层时关闭弹窗
        public static String  Close_Dialog_Show_Mark= "Close_Dialog_Show_Mark";
    }
}
