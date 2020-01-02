package com.hotniao.livelibrary.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.adapter.holder.HnAddAdminHolder;
import com.hotniao.livelibrary.adapter.holder.HnAnchorLuckyHolder;
import com.hotniao.livelibrary.adapter.holder.HnAnchorLvHolder;
import com.hotniao.livelibrary.adapter.holder.HnBackToRoomHolder;
import com.hotniao.livelibrary.adapter.holder.HnBaseHolder;
import com.hotniao.livelibrary.adapter.holder.HnCancelAdminHolder;
import com.hotniao.livelibrary.adapter.holder.HnDanmuHolder;
import com.hotniao.livelibrary.adapter.holder.HnFollowHolder;
import com.hotniao.livelibrary.adapter.holder.HnGagHolder;
import com.hotniao.livelibrary.adapter.holder.HnGroupLuckyHolder;
import com.hotniao.livelibrary.adapter.holder.HnKlickHolderHn;
import com.hotniao.livelibrary.adapter.holder.HnLookNumHolder;
import com.hotniao.livelibrary.adapter.holder.HnMessageHolder;
import com.hotniao.livelibrary.adapter.holder.HnNotifyHolder;
import com.hotniao.livelibrary.adapter.holder.HnPriseHolder;
import com.hotniao.livelibrary.adapter.holder.HnRichLvHolder;
import com.hotniao.livelibrary.adapter.holder.HnSendGiftHolder;
import com.hotniao.livelibrary.adapter.holder.HnShareSucHolder;
import com.hotniao.livelibrary.adapter.holder.HnTempLeaveHolder;
import com.hotniao.livelibrary.adapter.holder.HnWelcomeHolder;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.EmojiUtil;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间消息列表Adapter
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveMessageAdapter extends BaseAdapter {

    private final ArrayList<HnReceiveSocketBean> dataList;
    private final Context context;
    private static final int TYPE_COUNT = 21;  //类型总数

    private static final int NOTIFY_TYPE = 0;  //公告类型
    private static final int MSG_TYPE = 1;  //消息类型
    private static final int SENDGIFT_TYPE = 2;  //礼物类型
    private static final int GROUP_LUCKY_TYPE = 3;  //群红包类型
    private static final int KLICK_TYPE = 4;  //踢人类型
    private static final int GAG_TYPE = 5;  //禁言类型
    private static final int PRISE_TYPE = 6;  //点赞类型
    private static final int ADDADMIN_TYPE = 7;  //添加管理员类型
    private static final int WELCOME_TYPE = 8;  //进入直播间类型
    private static final int FOLLOW_TYPE = 9;  //主播被关注类型
    private static final int TEMPLEAVE_TYPE = 10;  //主播暂时离开直播间类型
    private static final int BACKROOM_TYPE = 11;  //主播回到直播间类型
    private static final int ANCHOR_LUCKY_TYPE = 12;  //主播红包类型
    private static final int CANCEL_ADMIN_TYPE = 13;  //取消场控类型
    private static final int SHARE_SUC_TYPE = 14;  //分享成功类型
    private static final int SYSTEM = 15;  //用户等系统消息升级通知
    private static final int ANCHOR_LV_TYPE = 16;  //主播升级通知
    private static final int DANMU_TYPE = 17;  //弹幕通知
    private static final int LOOK_TYPE = 18;  //人数突破通知
    private static final int LEAVE_TYPE = 19;  //用户离开
    private static final int Rebot_Join = 20;  //机器人进入
    private final String uid;

    public HnLiveMessageAdapter(Context context, ArrayList<HnReceiveSocketBean> user, String uid) {
        this.context = context;
        this.dataList = user;
        this.uid = uid;  //主播ID
    }

    @Override
    public int getItemViewType(int position) {

        HnReceiveSocketBean bean = dataList.get(position);

        String type = bean.getType();

        if ((HnWebscoketConstants.Notice).equalsIgnoreCase(type)) { //直播公告
            return NOTIFY_TYPE;
        } else if ((HnWebscoketConstants.Public_Msg).equalsIgnoreCase(type)) {  //发送公共消息
            return MSG_TYPE;
        } else if ((HnWebscoketConstants.Send_Gift).equalsIgnoreCase(type)) {  //赠送礼物
            return SENDGIFT_TYPE;
        } else if (("sendluckmoney").equalsIgnoreCase(type)) {  //群红包
            return GROUP_LUCKY_TYPE;
        } else if (HnWebscoketConstants.Kick.equalsIgnoreCase(type)) {  //踢人
            return KLICK_TYPE;
        } else if (HnWebscoketConstants.Prohibit_Talk.equalsIgnoreCase(type)) { //禁言
            return GAG_TYPE;
        } else if ((HnWebscoketConstants.Attitude).equalsIgnoreCase(type)) {   //点赞
            return PRISE_TYPE;
        } else if (("addadmin").equalsIgnoreCase(type)) {   //任命管理员
            return ADDADMIN_TYPE;
        } else if ((HnWebscoketConstants.Join).equalsIgnoreCase(type)) {   //用户欢迎
            return WELCOME_TYPE;
        } else if ((HnWebscoketConstants.Robot_Join).equalsIgnoreCase(type)) {   //机器人进入
            return Rebot_Join;
        } else if (("addfollow").equalsIgnoreCase(type)) {   //关注
            return FOLLOW_TYPE;
        } else if (("leaveanchor").equalsIgnoreCase(type)) {   //主播暂时离开
            return TEMPLEAVE_TYPE;
        } else if (("anchortoenter").equalsIgnoreCase(type)) {   //主播回到直播间
            return BACKROOM_TYPE;
        } else if (("sendandmoney").equalsIgnoreCase(type)) {   //主播红包
            return ANCHOR_LUCKY_TYPE;
        } else if (("canceladmin").equalsIgnoreCase(type)) {   //取消场控
            return CANCEL_ADMIN_TYPE;
        } else if (("share_success").equalsIgnoreCase(type)) {   //分享成功
            return SHARE_SUC_TYPE;
        } else if (HnWebscoketConstants.System.equalsIgnoreCase(type)) {   //用户升级等系统消息
            return SYSTEM;
        } else if (("anchorlvlup").equalsIgnoreCase(type)) {   //主播升级
            return ANCHOR_LV_TYPE;
        } else if ((HnWebscoketConstants.Barrage).equalsIgnoreCase(type)) {   //弹幕
            return DANMU_TYPE;
        } else if (("looknum").equalsIgnoreCase(type)) {   //观看人数突破
            return LOOK_TYPE;
        } else if (HnWebscoketConstants.Leave.equalsIgnoreCase(type)) {//用户离开
            return LEAVE_TYPE;
        }

        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {

        if (dataList != null && dataList.size() > 0) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);

        HnMessageHolder msgholder = null;  //消息
        HnDanmuHolder danholder = null;  //弹幕
        HnNotifyHolder notifyholder = null;  //公告
        HnGroupLuckyHolder groupluckyholder = null;  //群红包
        HnAnchorLuckyHolder anchorluckyholder = null;  //主播红包
        HnSendGiftHolder sendGiftholder = null;  //礼物
//        HnKlickHolderHn klickholder = null; //踢人
//        HnGagHolder gagholder = null;  //禁言
        HnPriseHolder priseholder = null;  //点赞
        HnAddAdminHolder hnAddAdminholder = null;  //添加管理员
        HnCancelAdminHolder hnCancelAdminholder = null;  //添加管理员
        HnWelcomeHolder welcomeholder = null;  //欢迎
        HnWelcomeHolder rebotJoinholder = null;  //欢迎
        HnFollowHolder followholder = null;  //关注
        HnTempLeaveHolder templeaveholder = null;  //暂时离开
        HnBackToRoomHolder backtoroomholder = null;  //回到直播间
        HnShareSucHolder shareSucHolder = null; //分享
        HnNotifyHolder hnSystemHolder = null;  //用户升级等系统消息
        HnAnchorLvHolder hnAnchorLvHolder = null; //主播升级
        HnLookNumHolder hnLookNumHolder = null;  //观看人数突破
        HnWelcomeHolder leaveHolder = null;  //用户离开

        if (convertView == null) {

            switch (itemViewType) {

                case MSG_TYPE:  //消息

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    msgholder = new HnMessageHolder(convertView);
                    convertView.setTag(msgholder);
                    break;

                case DANMU_TYPE:  //弹幕

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    danholder = new HnDanmuHolder(convertView);
                    convertView.setTag(danholder);
                    break;

                case NOTIFY_TYPE:  //公告

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    notifyholder = new HnNotifyHolder(convertView);
                    convertView.setTag(notifyholder);
                    break;

                case GROUP_LUCKY_TYPE:  //群红包

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_group_lucky, null);
                    groupluckyholder = new HnGroupLuckyHolder(convertView);
                    convertView.setTag(groupluckyholder);
                    break;

                case ANCHOR_LUCKY_TYPE:  //主播红包

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_anchor_lucky, null);
                    anchorluckyholder = new HnAnchorLuckyHolder(convertView);
                    convertView.setTag(anchorluckyholder);
                    break;

                case SENDGIFT_TYPE:  //礼物

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    sendGiftholder = new HnSendGiftHolder(convertView);
                    convertView.setTag(sendGiftholder);
                    break;

                case KLICK_TYPE:  //踢人

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    notifyholder = new HnNotifyHolder(convertView);
                    convertView.setTag(notifyholder);
                    break;

                case GAG_TYPE:  //禁言

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    notifyholder = new HnNotifyHolder(convertView);
                    convertView.setTag(notifyholder);
                    break;


                case PRISE_TYPE: //点赞

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    priseholder = new HnPriseHolder(convertView);
                    convertView.setTag(priseholder);
                    break;

                case ADDADMIN_TYPE: //添加管理员

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    hnAddAdminholder = new HnAddAdminHolder(convertView);
                    convertView.setTag(hnAddAdminholder);
                    break;


                case CANCEL_ADMIN_TYPE: //取消场控

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    hnCancelAdminholder = new HnCancelAdminHolder(convertView);
                    convertView.setTag(hnCancelAdminholder);
                    break;

                case WELCOME_TYPE:  //欢迎

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    welcomeholder = new HnWelcomeHolder(convertView);
                    convertView.setTag(welcomeholder);
                    break;

                case FOLLOW_TYPE:  //关注

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    followholder = new HnFollowHolder(convertView);
                    convertView.setTag(followholder);
                    break;

                case TEMPLEAVE_TYPE:  //暂时离开

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    templeaveholder = new HnTempLeaveHolder(convertView);
                    convertView.setTag(templeaveholder);
                    break;

                case BACKROOM_TYPE:  //回到直播间

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    backtoroomholder = new HnBackToRoomHolder(convertView);
                    convertView.setTag(backtoroomholder);
                    break;

                case SHARE_SUC_TYPE: //分享成功

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    shareSucHolder = new HnShareSucHolder(convertView);
                    convertView.setTag(shareSucHolder);
                    break;

                case SYSTEM:  //用户升级等系统消息

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    hnSystemHolder = new HnNotifyHolder(convertView);
                    convertView.setTag(hnSystemHolder);
                    break;

                case ANCHOR_LV_TYPE:  //主播升级

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    hnAnchorLvHolder = new HnAnchorLvHolder(convertView);
                    convertView.setTag(hnAnchorLvHolder);
                    break;

                case LOOK_TYPE:  //人数突破

                    convertView = View.inflate(context, R.layout.live_item_anchor_room_notify, null);
                    hnLookNumHolder = new HnLookNumHolder(convertView);
                    convertView.setTag(hnLookNumHolder);
                    break;

                case LEAVE_TYPE:  //用户离开

                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    leaveHolder = new HnWelcomeHolder(convertView);
                    convertView.setTag(leaveHolder);
                    break;
                case Rebot_Join://机器人进入
                    convertView = View.inflate(context, R.layout.live_item_live_message, null);
                    rebotJoinholder = new HnWelcomeHolder(convertView);
                    convertView.setTag(rebotJoinholder);
                    break;
            }
        } else {

            switch (itemViewType) {

                case MSG_TYPE:  //消息
                    msgholder = (HnMessageHolder) convertView.getTag();
                    break;

                case DANMU_TYPE:  //弹幕
                    danholder = (HnDanmuHolder) convertView.getTag();
                    break;

                case NOTIFY_TYPE:  //公告
                    notifyholder = (HnNotifyHolder) convertView.getTag();
                    break;

                case GROUP_LUCKY_TYPE:  //群红包
                    groupluckyholder = (HnGroupLuckyHolder) convertView.getTag();
                    break;

                case ANCHOR_LUCKY_TYPE:  //主播红包
                    anchorluckyholder = (HnAnchorLuckyHolder) convertView.getTag();
                    break;

                case SENDGIFT_TYPE:  //礼物
                    sendGiftholder = (HnSendGiftHolder) convertView.getTag();
                    break;

                case KLICK_TYPE:  //踢人
                    notifyholder = (HnNotifyHolder) convertView.getTag();
                    break;

                case GAG_TYPE:  //禁言
                    notifyholder = (HnNotifyHolder) convertView.getTag();
                    break;

                case PRISE_TYPE:  //点赞
                    priseholder = (HnPriseHolder) convertView.getTag();
                    break;

                case ADDADMIN_TYPE:  //添加管理员
                    hnAddAdminholder = (HnAddAdminHolder) convertView.getTag();
                    break;

                case CANCEL_ADMIN_TYPE:  //取消管理员
                    hnCancelAdminholder = (HnCancelAdminHolder) convertView.getTag();
                    break;

                case WELCOME_TYPE:  //欢迎
                    welcomeholder = (HnWelcomeHolder) convertView.getTag();
                    break;

                case FOLLOW_TYPE:  //关注
                    followholder = (HnFollowHolder) convertView.getTag();
                    break;

                case TEMPLEAVE_TYPE:  //暂时离开
                    templeaveholder = (HnTempLeaveHolder) convertView.getTag();
                    break;

                case BACKROOM_TYPE:  //回到直播间
                    backtoroomholder = (HnBackToRoomHolder) convertView.getTag();
                    break;

                case SHARE_SUC_TYPE:  //分享成功
                    shareSucHolder = (HnShareSucHolder) convertView.getTag();
                    break;

                case SYSTEM:  //用户升级
                    hnSystemHolder = (HnNotifyHolder) convertView.getTag();
                    break;

                case ANCHOR_LV_TYPE:  //主播升级
                    hnAnchorLvHolder = (HnAnchorLvHolder) convertView.getTag();
                    break;

                case LOOK_TYPE:  //人数突破
                    hnLookNumHolder = (HnLookNumHolder) convertView.getTag();
                    break;
                case LEAVE_TYPE:  //用户离开
                    leaveHolder = (HnWelcomeHolder) convertView.getTag();
                    break;
                case Rebot_Join://机器人进入
                    rebotJoinholder = (HnWelcomeHolder) convertView.getTag();
                    break;
            }
        }

        final HnReceiveSocketBean user = dataList.get(position);


        switch (itemViewType) {

            case GROUP_LUCKY_TYPE:  //群红包

//                if (user.getData().getFuser() != null) {
//
//                    String name = user.getData().getLuckMoeny().getName();
//
//                    HnLiveLevelUtil.setAudienceLevBg(groupluckyholder.mLv, user.getData().getFuser().getRichlvl(), true);
//
//                    groupluckyholder.mNick.setText(user.getData().getFuser().getNick());
//                    groupluckyholder.mUserNick.setText(user.getData().getFuser().getNick());
//
//                    if (TextUtils.isEmpty(name)) {
//                        groupluckyholder.mTvDes.setText(context.getString(R.string.live_hongbao_con));
//                    } else {
//                        groupluckyholder.mTvDes.setText(name);
//                    }
//
//                    groupluckyholder.setData(user);
//                }

                break;


            case ANCHOR_LUCKY_TYPE:  //主播红包

//                if (user.getData().getFuser() != null) {
//
//                    String name = user.getData().getLuckMoeny().getName();
//
//                    HnLiveLevelUtil.setAudienceLevBg(anchorluckyholder.mLv, user.getData().getFuser().getRichlvl(), true);
//
//                    anchorluckyholder.mNick.setText(user.getData().getFuser().getNick());
//                    anchorluckyholder.mUserNick.setText(user.getData().getFuser().getNick());
//
//                    if (TextUtils.isEmpty(name)) {
//                        anchorluckyholder.mTvDes.setText(context.getString(R.string.live_hongbao_con));
//                    } else {
//                        anchorluckyholder.mTvDes.setText(name);
//                    }
//
//                    anchorluckyholder.setData(user);
//                }

                break;

            case MSG_TYPE:  //消息
                try {
                    if (user.getData().getChat() != null && user.getData().getUser() != null) {
                        String content = user.getData().getChat().getContent();
                        if (!TextUtils.isEmpty(content)) {
                            //用户信息
                            final HnReceiveSocketBean.DataBean.UserBean fuser = user.getData().getUser();
                            String userLv = fuser.getUser_level();
                            String userNick = fuser.getUser_nickname();
                            String nick = userNick + ":";
                            msgholder.setData(user);
                            if (!TextUtils.isEmpty(userLv) && !TextUtils.isEmpty(nick)) {
                                HnLiveLevelUtil.setAudienceLevBg(msgholder.mLevel, userLv, true);
                                SpannableStringBuilder sb = EmojiUtil.handlerEmojiText(nick, nick + content, context);
                                //设置字体前景色
                                sb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.comm_text_color_main)), 0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
                                msgholder.mContent.setText(sb);
                            }
                            msgholder.mContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (fuser == null) return;
                                    EventBus.getDefault().post(new HnLiveEvent(position, HnLiveConstants.EventBus.Click_Public_Message, fuser.getUser_id()));
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case DANMU_TYPE:  //弹幕


                try {
                    if (user.getData().getChat() != null && user.getData().getUser() != null) {
                        String content = user.getData().getChat().getContent();
                        if (!TextUtils.isEmpty(content)) {
                            //用户信息
                            HnReceiveSocketBean.DataBean.UserBean fuser = user.getData().getUser();
                            String userLv = fuser.getUser_level();
                            String userNick = fuser.getUser_nickname();
                            String nick = userNick + ":";
                            danholder.setData(user);
                            if (!TextUtils.isEmpty(userLv) && !TextUtils.isEmpty(nick)) {
                                HnLiveLevelUtil.setAudienceLevBg(danholder.mLevel, userLv, true);
                                SpannableStringBuilder sb = EmojiUtil.handlerEmojiText(nick, nick + content, context);
                                //设置字体前景色
                                sb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_color)), 0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
                                danholder.mContent.setText(sb);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case SENDGIFT_TYPE:  //礼物
                HnReceiveSocketBean.DataBean.LiveGiftBean gift = user.getData().getLive_gift();
                if (gift != null && !TextUtils.isEmpty(gift.getLive_gift_name())) {
                    String giftName = gift.getLive_gift_name();
                    tipTemplate(SENDGIFT_TYPE, sendGiftholder, user, context.getString(R.string.live_zeng_song_one_gitf) + gift.getLive_gift_nunmer() + context.getString(R.string.live_num) + giftName,
                            context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_live_gife_msg), false, false);
                }
                break;

            case NOTIFY_TYPE:  //公告

                inflateContent(notifyholder.mMsg, context.getString(R.string.live_sys_notice), user.getNotice(),
                        context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white));
                break;

            case KLICK_TYPE:  //踢人

//
                if (user != null) {
                    HnReceiveSocketBean.DataBean data = user.getData();
                    if (data != null) {
                        HnReceiveSocketBean.DataBean.UserBean user1 = data.getUser();
                        if (user1 != null) {
                            if (!TextUtils.isEmpty(user1.getUser_id())) {
                                inflateContent(notifyholder.mMsg, user1.getUser_nickname(), context.getString(R.string.live_anchor_kick_room), context.getResources().getColor(R.color.comm_live_notice_msg), context.getResources().getColor(R.color.comm_live_notice_msg));
                            }
                        }
                    }
                }

                break;

            case GAG_TYPE:  //禁言

                if (user != null) {
                    HnReceiveSocketBean.DataBean data = user.getData();
                    if (data != null) {
                        HnReceiveSocketBean.DataBean.UserBean fuser = data.getUser();
                        if (fuser != null) {
                            if (!TextUtils.isEmpty(fuser.getUser_id())) {
                                inflateContent(notifyholder.mMsg, fuser.getUser_nickname(), context.getString(R.string.live_anchor_not_speak), context.getResources().getColor(R.color.comm_live_notice_msg), context.getResources().getColor(R.color.comm_live_notice_msg));
                            }
                        }
                    }
                }

                break;

            case PRISE_TYPE:  //点赞
                tipTemplate(PRISE_TYPE, priseholder, user, context.getString(R.string.live_liked_anchor), context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white), false, false);


                break;

            case ADDADMIN_TYPE:  //添加管理员

//                tipTemplate(hnAddAdminholder, user, context.getString(R.string.live_set_manager), "#ffffff", "#f5d83e", false);

                break;

            case CANCEL_ADMIN_TYPE:  //取消管理员

//                tipTemplate(hnCancelAdminholder, user, context.getString(R.string.live_cancel_admin), "#ffffff", "#f5d83e", false);

                break;

            case WELCOME_TYPE:  //欢迎
                tipTemplate(WELCOME_TYPE, welcomeholder, user, context.getString(R.string.live_welcome), context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white), true, false);
                break;
            case Rebot_Join:  //机器人进入
                tipTemplate(Rebot_Join, rebotJoinholder, user, context.getString(R.string.live_welcome), context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white), true, true);
                break;
            case LEAVE_TYPE:  //用户离开
                tipTemplate(LEAVE_TYPE, leaveHolder, user, context.getString(R.string.live_levae_room), context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white), true, false);
                break;

            case FOLLOW_TYPE:  //关注

//                if (user.getData() != null) {
//                    SocketFuserBean fuser = user.getData().getFuserX();
//                    if (fuser != null) {
//                        tipTemplate(followholder, user, context.getString(R.string.live_add_follow), context.getResources().getColor(R.color.live_color_f49a0b), context.getResources().getColor(R.color.live_color_8d2cd5), true,false);
//                    }
//                }

                break;

            case TEMPLEAVE_TYPE:  //暂时离开

//                if (user.getData() != null) {
//                    inflateContent(templeaveholder.mMsg, user.getData().getMsg());
//                }

                break;

            case BACKROOM_TYPE:  //回到直播间

//                if (user.getData() != null) {
//                    inflateContent(backtoroomholder.mMsg, user.getData().getMsg());
//                }

                break;

            case SHARE_SUC_TYPE:  //分享成功

//                tipTemplate(shareSucHolder, user, context.getString(R.string.live_sha_suc), "#ffffff", "#f5d83e", true);

                break;

            case SYSTEM:  //用户升级

                inflateContent(hnSystemHolder.mMsg, context.getString(R.string.live_sys_msg), user.getNotice(),
                        context.getResources().getColor(R.color.comm_text_color_main), context.getResources().getColor(R.color.comm_text_color_white));


                break;

            case ANCHOR_LV_TYPE:  //主播升级
//
//                ReceivedSockedBean.DataBean dataBean = user.getData();
//
//                if (dataBean != null) {
//
//                    SocketFuserBean fuser = user.getData().getFuser();
//
//                    if (fuser != null) {
//                        String nick = fuser.getNick();
//                        String richlvl = dataBean.getLvl();
//                        inflateContent(hnAnchorLvHolder.mMsg, String.format(context.getString(R.string.live_congri_anchor_lv), nick, richlvl));
//                    }
//                }

                break;

            case LOOK_TYPE:  //人数突破

//                ReceivedSockedBean.DataBean dataBe = user.getData();
//
//                if (dataBe != null) {
//
//                    String msg = dataBe.getMsg();
//
//                    if (!TextUtils.isEmpty(msg)) {
//
//                        inflateContent(hnLookNumHolder.mMsg, msg);
//                    }
//                }

                break;

        }

        return convertView;
    }

    private void tipTemplate(final int type, HnBaseHolder holder, final HnReceiveSocketBean user, String content, int nickColor, int conColor, boolean isFuser, boolean isRebot) {

        try {

            if (user != null) {
                if (TextUtils.isEmpty(content))
                    return;
                String userLv = "";
                String userNick = "";

                //等级
                final HnReceiveSocketBean.DataBean data = user.getData();

                if (data == null)
                    return;
                if (WELCOME_TYPE == type) {
                    userLv = data.getFuser().getUser().getUser_level();
                    userNick = data.getFuser().getUser().getUser_nickname();
                } else {
                    userLv = data.getUser().getUser_level();
                    userNick = data.getUser().getUser_nickname();
                }

                String nick = userNick + "  ";

                holder.setData(user);

                if (TextUtils.isEmpty(userLv) || TextUtils.isEmpty(nick))
                    return;

                HnLiveLevelUtil.setAudienceLevBg(holder.mLevel, userLv, true);
                SpannableStringBuilder msgbuilder = new SpannableStringBuilder(nick + content);
                ForegroundColorSpan klicknickSpan = new ForegroundColorSpan(nickColor);
                ForegroundColorSpan klickcontentSpan = new ForegroundColorSpan(conColor);
                msgbuilder.setSpan(klicknickSpan, 0, nick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                msgbuilder.setSpan(klickcontentSpan, nick.length(), nick.length() + content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.mContent.setText(msgbuilder);

                holder.mContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data == null) return;
                        if (WELCOME_TYPE == type) {
                            if (data.getFuser() == null || data.getFuser().getUser() == null)
                                return;
                            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Click_Public_Message, data.getFuser().getUser().getUser_id()));
                        } else {
                            if (data.getUser() == null) return;
                            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Click_Public_Message, data.getUser().getUser_id()));
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inflateContent(TextView textView, String title, String userMsg, int titleColor, int msgColor) {

        try {

            if (TextUtils.isEmpty(userMsg))
                return;

            String mes = title + userMsg;

            SpannableStringBuilder giftbuilder = new SpannableStringBuilder(mes);
            ForegroundColorSpan klicknickSpan = new ForegroundColorSpan(titleColor);
            ForegroundColorSpan klickcontentSpan = new ForegroundColorSpan(msgColor);
            giftbuilder.setSpan(klicknickSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            giftbuilder.setSpan(klickcontentSpan, title.length(), mes.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(giftbuilder);
            textView.setTextColor(context.getResources().getColor(R.color.comm_text_color_main));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
