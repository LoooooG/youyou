package com.hn.library.global;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：项目接口
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUrl extends NetConstant {


    /**
     * 用户配置
     */
    public static final String USER_APP_CONFIG = "/user/app/config";
    /**
     * 未读消息数
     */
    public static final String USER_CHAT_UNREAD = "/user/chat/unread";
    /**
     * 是否签到
     */
    public static final String USER_SIGN_JUDGE = "/user/signin/judge";
    public static final String ONLNINE = "/user/app/addOnlineTime";
    /**
     * 开播协议
     */
    public static final String LIVE_AGREEMENT = NetConstant.SERVER + "/user/app/liveAgreement";
    /**
     * 注册协议
     */
    public static final String REGISTER_AGREEMENT = NetConstant.SERVER + "/user/app/registerAgreement";


    /**
     * 用户登录
     */
    public final static String LOGIN = "/account/login/index";
    /**
     * 用户QQ登录
     */
    public final static String LOGIN_QQ = "/account/qqoauth/index";
    /**
     * 用户WX登录
     */
    public final static String LOGIN_WX = "/account/wxoauth/index";
    /**
     * 用户SINA登录
     */
    public final static String LOGIN_SINA = "/account/wboauth/index";
    /**
     * 注册验证码
     */
    public final static String VERIFY_CODE_REGISTER = "/account/verifycode/register";

    /**
     * 登录验证码
     */
    public final static String VERIFY_CODE_LOGIN = "/account/verifycode/phoneLogin";

    /**
     * 验证码登录
     */
    public final static String REGISTER_LOGIN = "/account/register/login";

    /**
     * 忘记密码验证码
     */
    public final static String VERIFY_CODE_FORGETPWD = "/account/verifycode/forgetPwd";
    /**
     * 获取绑定手机验证码
     */
    public final static String VERIFY_CODE_BINDPHONE = "/account/verifycode/bindPhone";
    /**
     * 获取更换手机验证码
     */
    public final static String VERIFY_CODE_CHANGE_PHONE = "/account/verifycode/changePhone";
    /**
     * 判断更换手机号码验证码(下一步)
     */
    public final static String VERIFY_CODE_JUDGECHANGE_PHONE = "/account/verifycode/judgeChangePhone";


    /**
     * 绑定手机
     */
    public final static String BIND_PHONE = "/user/bind/phone";
    /**
     * 更换绑定手机
     */
    public final static String BIND_CHANGE_PHONE = "/user/bind/changePhone";


    /**
     * 注册
     */
    public final static String REGISTER_PHONE = "/account/register/index";

    /**
     * 获取系统消息
     */
    public final static String SYSTEM_MESSAGE = "/user/chat/systemDialog";

    /**
     * 推送开始直播接口
     */
    public static final String PUSH_STARTLIVE = "/live/anchor/startLive";

    /**
     * 收礼物记录
     */
    public static final String GET_GIFT_LOG = "/user/amountrecord/giftIncreaseDot";

    /**
     * 获取送礼记录
     */
    public static final String SEND_GIFT_LOG = "/user/amountrecord/giftDecreaseCoin";
    /**
     * 购买VIP记录
     */
    public static final String BUY_VIP_RECORD = "/user/amountrecord/vipDecreaseCoin";
    /**
     * 开播收入记录
     */
    public static final String LIVE_INCOME_LOG = "/user/amountrecord/liveIncreaseDot";
    /**
     * 收益邀请详情
     */
    public static final String USER_AMOUNRECORD_INVITE_COIN = "/user/amountrecord/inviteIncreaseCoin";
    /**
     * 收益游戏下注详情
     */
    public static final String USER_GAME_INCEWASE_COIN = "/user/game/gameIncreaseCoin";
    /**
     * 消费游戏下注详情
     */
    public static final String USER_GAME_DECREASE_COIN = "/user/game/gameDecreaseCoin";
    /**
     * 观看收入记录
     */
    public static final String LIVE_PAY_LOG = "/user/amountrecord/liveDecreaseCoin";

    /**
     * 获取充值记录
     */
    public static final String API_GETPAYLOGS = "/user/recharge/log";

    /**
     * 获取提现记录
     */
    public static final String API_GETWITHDRAWLOGS = "/user/withdraw/log";


    /**
     * 私信列表
     */
    public static final String PRIVATE_LETTER_LIST = "/user/chat/dialogList";

    /**
     * 获取用户关注列表
     */
    public static final String USER_FOCUS = "/user/follow/follows";

    /**
     * 获取用户粉丝列表
     */
    public static final String USER_FANS = "/user/follow/fans";

    /**
     * 私信详情
     */
    public static final String PRIVATELETTER_DETAIL = "/user/chat/dialog";

    /**
     * 发送私信
     */
    public static final String SEND_PRIVATELETTER = "/user/chat/send";

    /**
     * 退出登录
     */
    public static final String USER_EXIT = "/account/logout/index";

    /**
     * 用户信息保存
     */
    public static final String SAVE_USER_INFO = "/user/profile/update";

    /**
     * 建议与反馈
     */
    public static final String FEED_BACK = "/user/help/feedback";

    /**
     * 实名认证申请
     */
    public static final String CERTIFICATION_ADD = "/user/certification/add";


    /**
     * 获取验证码
     */
    public static final String SENDSMS = "/index/sendSms";
    /**
     * 修改密码
     */
    public static final String MODIFYPWD = "/user/profile/updatePwd";


    /**
     * 忽略未读
     */
    public static final String IGNORE_NOTREAD = "/user/chat/ignoreUnread";

    /**
     * 忘记密码
     */
    public static final String FORGETPWD = "/account/login/resetPwd";

    /**
     * 开播提醒设置
     */
    public static final String SET_LIVE_REMIND = "/user/follow/updateRemind";

    /**
     * 实名认证结果
     */
    public static final String CERTIFICATION_CHECK = "/user/certification/check";
    /**
     * 主播获取开始直播信息
     */
    public static final String LIVE_GET_LIVE_INFO = "/live/anchor/getLiveInfo";


    /**
     * 删除私信
     */
    public static final String DESTORY_MSG = "/user/chat/deleteUserDialog";
    /**
     * 系统最近消息对话列表
     */
    public static final String CHAT_SYSTEM_DIALOG_LIST = "/user/chat/systemDialogList";
    /**
     * 用户信息
     */
    public static final String PROFILE = "/user/profile/index";
    /**
     * 获取账户信息
     */
    public static final String Get_Account = "/user/recharge/index";
    /**
     * 购买VIP
     */
    public static final String Buy_vip = "/user/vip/recharge";
    /**
     * 退出对话详情
     */
    public static final String Exit_Msg_Detail = "/msg/quitMessageDetail";
    /**
     * 直播分类
     */
    public static final String Live_NAVBAR = "/live/live/navbar";
    /**
     * 聊天分类
     */
    public static final String Chat_Type = "/live/chat/navbar";
    /**
     * 首页热门和其他分类直播
     */
    public static final String Live_HOT = "/live/live/hot";
    /**
     * 首页游戏接口
     */
    public static final String Live_Game = "/live/live/game";
    /**
     * 搜索
     */
    public static final String SEARCH_GET_RECOMMEND = "/live/search/getRecommend";
    /**
     * 搜索主播
     */
    public static final String SEARCH_ANCHOR = "/live/search/anchor";
    /**
     * 搜索直播
     */
    public static final String SEARCH_LIVE = "/live/search/live";

    /**
     * 主播-结束直播
     */
    public static final String Stop_Live = "/live/anchor/endLive";
    /**
     * 我的收益-充值支付宝
     */
    public static final String Pre_Pay_ZFB = "/user/recharge/zfb";
    /**
     * 我的收益-充值微信
     */
    public static final String Pre_Pay_WX = "/user/recharge/wx";
    /**
     * 测试充值
     */
    public static final String USER_RECHARGE_TEXTPAY = "/user/recharge/testPay";
    /**
     * 关注的直播
     */
    public static final String Follow_Live_List = "/live/live/follow";
    /**
     * 附近的直播
     */
    public static final String NEAR_Live_List = "/live/live/near";
    /**
     * 上下切换房间列表
     */
    public static final String LIVE_ROOM_SWITCH = "/live/live/room";

    /**
     * 会员中心
     */
    public static final String VIP_INDEX = "/user/vip/index";
    /**
     * 粉丝贡献榜
     */
    public static final String LIVE_RANK_ANCHORFANS = "/live/ranking/anchorFans";
    /**
     * 亲密榜
     */
    public static final String LIVE_RANK_USER_GIFT = "/live/ranking/userGiftRank";

    /**
     * 主播排行榜
     */
    public static final String LIVE_RANK_ANCHOR = "/live/ranking/anchor";
    /**
     * 用户排行榜
     */
    public static final String LIVE_RANK_USER = "/live/ranking/user";

    /**
     * 网页
     */
//    public static final String Web_Url = "http://h5.jltf99.cn/";//SERVER + "/h5/index/page/";
    public static final String Web_Url = "http://h5.028wq.com/";//SERVER + "/h5/index/page/";
    /**
     * 轮播图
     */
    public static final String BANNER = "/live/live/banner";
    /**
     * 主播回放
     */
    public static final String LIVE_PLAYBACK_ANCHOR = "/live/playback/anchor";
    public static final String BOUNS = "/user/amountrecord/inviteIncreaseCoin";
    public static final String ONLINE_LIST = "/user/app/getOnline";
    /**
     * 我的邀请页面
     */
    public static final String USER_INVITE_DETAIL = "/user/invite/detail";
    /**
     * 我邀请的人
     */
    public static final String USER_INVITE_INDEX = "/user/invite/index";
    /**
     * 提现账户
     */
    public static final String USER_WITHDRAW_INDEX = "/user/withdraw/index";
    public static final String CHECK_STATUS = "/user/withdraw/checkStatus";
    /**
     * 发送提现验证码
     */
    public static final String USER_WITHDRAW_VERCODE = "/account/verifycode/withdraw";
    /**
     * 申请提现
     */
    public static final String USER_WITHDRAW_ADD = "/user/withdraw/add";
    /**
     * 提现详情
     */
    public static final String USER_WITHDRAW_DETAIL = "/user/withdraw/detail";
    /**
     * 邀请提现详情
     */
    public static final String INVITE_WITHDRAW_DETAIL = "/invite/withdraw/detail";
    /**
     * 开播提醒
     */
    public static final String USER_FOLLOW_REMINDS = "/user/follow/reminds";
    /**
     * 我的房管
     */
    public static final String LIVE_ROOMADMIN_INDEX = "/live/roomadmin/index";
    /**
     * 搜索房管
     */
    public static final String LIVE_ROOMADMIN_SEARCH = "/live/roomadmin/search";
    /**
     * 添加房管
     */
    public static final String LIVE_ROOMADMIN_ADD = "/live/roomadmin/add";
    /**
     * 删除房管
     */
    public static final String LIVE_ROOMADMIN_DELETE = "/live/roomadmin/delete";

    /**
     * 签到
     */
    public static final String USER_SIGNIN_DETAIL = NetConstant.SERVER + "/user/signin/detail";
    /**
     * 帮助
     */
    public static final String USER_HELP_HOTQUESTION = NetConstant.SERVER + "/user/help/hotQuestion";
    /**
     * 关于我们
     */
    public static final String USER_APP_ABOUTS = "/user/app/aboutUs";
    /**
     * 关于我们详情
     */
    public static final String USER_APP_ABOUTS_DETAIL = NetConstant.SERVER + "/user/app/aboutUsDetail";
    /**
     * 主播等级
     */
    public static final String USER_LEVEL_ANCHOR = NetConstant.SERVER + "/user/level/anchor";
    /**
     * 用户等级
     */
    public static final String USER_LEVEL_USER = NetConstant.SERVER + "/user/level/index";


    //#############################################           新增        ###############################
    /**
     * 获取首页热门直播
     */
    public static final String LIVE_GET_HOME_HOT_LIVE = "/live/anchor/getHomeHotLive";
    /**
     * 获取首页分类视频
     */
    public static final String VIDEO_APP_FIRST = "/video/app/first";
    /**
     * 获取首页热门视频
     */
    public static final String VIDEO_APP_LIST = "/video/app/list";
    /**
     * 获取视频分类
     */
    public static final String VIDEO_APP_INDEX = "/video/app/index";
    /**
     * 获取同一类型所有的视频
     */
    public static final String VIDEO_APP_VIDEO_BY_TYPE = "/video/app/getVideoByType";
    /**
     * 获取视频详情
     */
    public static final String VIDEO_APP_VIDEO_DETAIL = "/video/app/videoDetail";
    /**
     * 获取视频地址
     */
    public static final String VIDEO_APP_VIDEO_URL = "/video/app/getVideoUrl";
    /**
     * 电赞视频
     */
    public static final String VIDEO_APP_VIDEO_LICK = "/video/app/videoLike";
    /**
     * 获取视频评论
     */
    public static final String VIDEO_APP_REPLY_LIST = "/video/app/replyList";
    /**
     * 发布视频评论
     */
    public static final String VIDEO_APP_ADD_REPLY = "/video/app/addReply";
    /**
     * 删除视频
     */
    public static final String VIDEO_APP_DELETE_VIDEO = "/video/app/deleteVideo";
    /**
     * 搜索音乐
     */
    public static final String VIDEO_APP_MUSIC_LIST = "/video/app/musicList";
    /**
     * 上传视频
     */
    public static final String VIDEO_APP_ADD_VIDEO = "/video/app/addVideo";
    /**
     * 分享视频成功
     */
    public static final String VIDEO_APP_SHARE_VIDEO_SUCCESS = "/video/app/shareVideoSuccess";
    /**
     * 获取红人主播
     */
    public static final String LIVE_ANCHOR_HOT_MAN = "/live/anchor/getHotMan";
    /**
     * 获取随机开通私聊的主播
     */
    public static final String LIVE_ANCHOR_ANCHOR_CHAT = "/live/anchor/getAnchor";
    /**
     * 获取约聊主播
     */
    public static final String LIVE_ANCHOR_PRIVATE_CHAT = "/live/anchor/getPrivateChat";
    /**
     * 获取附近约聊主播
     */
    public static final String LIVE_ANCHOR_NEARNY_ANCHOR = "/live/anchor/getNearbyAnchor";
    /**
     * 获取约聊对话列表
     */
    public static final String LIVE_ANCHOR_GET_CHAT_DIALOG = "/live/anchor/getChatDialog";
    /**
     * 获取聊天Id
     */
    public static final String GET_CHAT_ROOM_ID = "/live/anchor/getChatRoomId";
    /**
     * 获取主播一对一私聊状态
     */
    public static final String LIVE_ANCHOR_PRIVATE_CHAT_INFO = "/live/anchor/getPrivateChatInfo";
    /**
     * 开通一对一私聊
     */
    public static final String LIVE_ANCHOR_UPDATA_CHAT_STATUS = "/live/anchor/updateChatStatus";
    /**
     * 获取约聊对话详情
     */
    public static final String LIVE_ANCHOR_CHAT_DIALOG_DETAIL = "/live/anchor/getChatDialogdetail";
    /**
     * 获取视频消息
     */
    public static final String VIDEO_MSG_INDEX = "/video/msg/index";
    /**
     * 获取主播礼物
     */
    public static final String USER_PROFILE_ANCHOR_GIFT = "/user/profile/getAnchorGift";
    /**
     * 获取回放地址
     */
    public static final String VIDEO_APP_GET_PALY_BACK_URL = "/video/app/getPlayBackUrl";
    /**
     * 加入黑名单
     */
    public static final String USER_PROFILR_ADD_BLACK = "/user/profile/addBlack";
    /**
     * 设置回放价格
     */
    public static final String VIDEO_APP_SET_BACK_PRICE = "/video/app/setPlayBackPrice";
    /**
     * 获取主播信息
     */
    public static final String LIVE_ANCHOR_ANCHOR_INFO = "/live/anchor/getAnchorInfo";


    /**
     * 邀请主播一对一私聊
     */
    public static final String LIVE_ANCHOR_START_PRIVATE_CHAT = "/live/anchor/startPrivateChat";
    /**
     * 取消一对一私聊
     */
    public static final String LIVE_ANCHOR_CANCEL_PRIVATE_CHAT = "/live/anchor/cancelPrivateChat";
    /**
     * 拒绝一对一聊天
     */
    public static final String LIVE_ANCHOR_REFUSE_PRIVATE_CHAT = "/live/anchor/refusePrivateChat";
    /**
     * 接受一对一聊天
     */
    public static final String LIVE_ANCHOR_ACCEPT_PRIVATE_CHAT = "/live/anchor/acceptPrivateChat";
    /**
     * 挂断一对一聊天
     */
    public static final String LIVE_ANCHOR_HANGUP_PRIVATE_CHAT = "/live/anchor/hangUpChat";
    /**
     * 扣费一对一聊天
     */
    public static final String LIVE_ANCHOR_PRIVATE_CHAT_MINUTE = "/live/anchor/privateChatMinute";
    /**
     * 模糊一对一聊天
     */
    public static final String LIVE_ANCHOR_VAGUE_CHAT = "/live/anchor/vagueChat";
    /**
     * 一聊天心跳检测
     */
    public static final String LIVE_ANCHOR_CHAT_HEARTBEAT = "/live/anchor/chatHeartbeat";
    /**
     * 获取城市列表
     */
    public static final String VIDEO_APP_GET_CITY = "/video/app/getCity";
    /**
     * 获取视频消息未读数
     */
    public static final String VIDEO_MSG_GET_UNREAD_NUM = "/video/msg/getUnReadNum";
    /**
     * 判断回放是否收费
     */
    public static final String VIDEO_APP_PLAY_BACK_FREE = "/video/app/playBackIsFree";


    /**
     * 回放收益
     */
    public static final String PLAY_BACK_CONSUME_DOT = "/user/amountrecord/playbackConsumeDot";
    /**
     * 回放消费
     */
    public static final String PLAY_BACK_CONSUME_COIN = "/user/amountrecord/playbackConsumeCoin";
    /**
     * 私聊收益详情
     */
    public static final String CHAT_CONSUME_DOT = "/user/amountrecord/chatConsumeDot";
    /**
     * 私聊消费详情
     */
    public static final String CHAT_CONSUME_COIN = "/user/amountrecord/chatConsumeCoin";

    /**
     * 私信消费
     */
    public static final String CHAT_CONSUME = "/user/amountrecord/chatConsume";

    /**
     * 视频收益
     */
    public static final String VIDEO_CONSUME_DOT = "/user/amountrecord/videoConsumeDot";
    /**
     * 视频消费
     */
    public static final String VIDEO_CONSUME_COIN = "/user/amountrecord/videoConsumeCoin";
    /**
     * 拉黑列表
     */
    public static final String USER_BLACK_LIST = "/user/profile/blackList";
    /**
     * 更新用户城市
     */
    public static final String UPDATE_USER_CITY = "/user/app/updateCity";
    /**
     * 分享加经验
     */
    public static final String SHARE_ADD_EXPER = "/user/invite/share";
    /**
     * 分享赚钱主页
     */
    public static final String SHARE_RULE = "/invite/index/rule";
    /**
     * 奖励记录
     */
    public static final String REWARD_LOG = "/invite/index/rewardLog";
    /**
     * 我的推广首页
     */
    public static final String GENERALIZE_INDEX = "/invite/index/index";
    /**
     * 兑换套餐
     */
    public static final String INVITE_EXCHANGECOMBO = "/invite/index/exchangeCombo";
    /**
     * 兑换
     */
    public static final String INVITE_EXCHANGE = "/invite/index/exchange";
    /**
     * 兑换记录
     */
    public static final String INVITE_EXCHANGELOG = "/invite/index/exchangeLog";
    /**
     * 我邀请的人
     */
    public static final String MY_INVITE_USER = "/invite/index/myInvite";
    /**
     * 邀请提现
     */
    public static final String INVITE_WITHDRAW = "/invite/withdraw/add";
    /**
     * 邀请提现记录
     */
    public static final String INVITE_WITHDRAW_RECORD = "/invite/withdraw/log";
    /**
     * 用户关注主播
     */
    public static final String FOLLOW_ANCHOR = "/live/anchor/getFollowAnchor";
    /**
     * 回放列表
     */
    public static final String PLAYBACK_LIST = "/live/playback/playbackList";
    /**
     * 发布回放
     */
    public static final String RELEASE_PLAYBACK = "/live/playback/deliveryPlay";
    /**
     * 回放公告
     */
    public static final String PLAY_BACK_NOTICE = "/live/playback/getNotice";



}
