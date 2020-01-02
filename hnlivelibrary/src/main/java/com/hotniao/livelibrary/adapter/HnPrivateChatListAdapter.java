package com.hotniao.livelibrary.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnUrl;
import com.hn.library.utils.DateUtils;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.bean.PrivateChatBean;
import com.hotniao.livelibrary.util.DataTimeUtils;
import com.hotniao.livelibrary.util.EmojiUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：直播间  -- 私信详情Adapter
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateChatListAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 4;  //类型总数
    private static final int OWNER_TYPE = 0;  //自身类型
    private static final int USER_TYPE = 1;  //用户类型
    private static final int OWNER_GIFT_TYPE = 2;  //自己礼物类型
    private static final int USER_GIFT_TYPE = 3;  //对方礼物类型

    private final Context context;
    private final List<PrivateChatBean> dataList;
    private final boolean mIsAnchor;

    public HnPrivateChatListAdapter(Context context, List<PrivateChatBean> dataList, boolean mIsAnchor) {

        this.context = context;
        this.dataList = dataList;
        this.mIsAnchor = mIsAnchor;
    }

    @Override
    public int getItemViewType(int position) {

        String gift = dataList.get(position).getShowMsgTyoe();

        if ("ownergift".equals(gift)) {
            return OWNER_GIFT_TYPE;
        } else if ("usergift".equals(gift)) {
            return USER_GIFT_TYPE;
        } else if ("ownermsg".equals(gift)) {
            return OWNER_TYPE;
        } else if ("usermsg".equals(gift)) {
            return USER_TYPE;
        }
        return 5;
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

        OwnerHolder ownerHolder = null;
        UserHolder userHolder = null;
        OwnerGiftHolder ownerGiftHolder = null;
        UserGiftHolder userGiftHolder = null;

        if (convertView == null) {

            switch (itemViewType) {

                case OWNER_TYPE:
                    convertView = View.inflate(context, R.layout.live_item_privatechat_owner, null);
                    ownerHolder = new OwnerHolder(convertView);
                    convertView.setTag(ownerHolder);
                    break;

                case USER_TYPE:
                    convertView = View.inflate(context, R.layout.live_item_privatechat_user, null);
                    userHolder = new UserHolder(convertView);
                    convertView.setTag(userHolder);

                    break;

                case OWNER_GIFT_TYPE:
                    convertView = View.inflate(context, R.layout.live_item_privatechat_owner_gift, null);
                    ownerGiftHolder = new OwnerGiftHolder(convertView);
                    convertView.setTag(ownerGiftHolder);

                    break;

                case USER_GIFT_TYPE:
                    convertView = View.inflate(context, R.layout.live_item_privatechat_user_gift, null);
                    userGiftHolder = new UserGiftHolder(convertView);
                    convertView.setTag(userGiftHolder);

                    break;
            }

        } else {
            switch (itemViewType) {
                case OWNER_TYPE:
                    ownerHolder = (OwnerHolder) convertView.getTag();
                    break;

                case USER_TYPE:
                    userHolder = (UserHolder) convertView.getTag();
                    break;

                case OWNER_GIFT_TYPE:
                    ownerGiftHolder = (OwnerGiftHolder) convertView.getTag();
                    break;

                case USER_GIFT_TYPE:
                    userGiftHolder = (UserGiftHolder) convertView.getTag();
                    break;
            }
        }

        String content = dataList.get(position).getMsgContent();
        String avatar = dataList.get(position).getAvator();
//        String giftAva = dataList.get(position).get;
//        String consume = dataList.get(position).giftConsume;
        String time = dataList.get(position).getAdd_time();
        final String id = dataList.get(position).getUid();

        switch (itemViewType) {

            case OWNER_TYPE:

                try {
                    EmojiUtil.handlerEmojiText(ownerHolder.mOnwerContent, content, context, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //头像
                ownerHolder.mOwnerHeader.setController(FrescoConfig.getController(avatar));
                //头像点击I
                ownerHolder.mOwnerHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                timeShow(ownerHolder.mOwnerTime, time, context, position);

                break;

            case USER_TYPE:

                try {
                    EmojiUtil.handlerEmojiText(userHolder.mUserContent, content, context, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //头像
                userHolder.mUserHeader.setController(FrescoConfig.getController(avatar));
                //头像点击I
                userHolder.mUserHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new EventBusBean(position, HnLiveConstants.EventBus.User_Info, dataList.get(position)));
                    }
                });
                timeShow(userHolder.mUserTime, time, context, position);

                break;

            case OWNER_GIFT_TYPE:

//                ownerGiftHolder.mGiftIcon.setImageURI(Uri.parse(NetConstant.FILE_SERVER + giftAva)); //礼物头像
//
//                ownerGiftHolder.mGiftName.setText(content);  //礼物昵称
//
//                ownerGiftHolder.mUserHeader.setImageURI(Uri.parse(NetConstant.FILE_SERVER + avatar));//用户头像
//
//                timeShow(ownerGiftHolder.mOwnerTime, time);

                break;

            case USER_GIFT_TYPE:

//                userGiftHolder.mGiftName.setText(content);
//
//                userGiftHolder.mTvGiftCoin.setText(HnUiUtils.getString(R.string.letter_get) + consume);
//
//                userGiftHolder.mUserHeader.setImageURI(Uri.parse(NetConstant.FILE_SERVER + avatar));//用户头像
//
//                userGiftHolder.mGiftIcon.setImageURI(Uri.parse(NetConstant.FILE_SERVER + giftAva));
//
//                timeShow(userGiftHolder.mUserTime, time);
//
//                userGiftHolder.mUserHeader.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (mIsAnchor)
//                            return;
//
//                        Intent intent = new Intent(context, HnAnchorInfoActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra(HnConstants.Intent.ANCHOR_ID, id);
//                        context.startActivity(intent);
//                    }
//                });


                break;

        }

        return convertView;
    }

    private void timeShow(TextView textView, String time, Context context, int position) {

        //设置时间
        if (dataList.size() > 1) {
            if (position == 0) {
                textView.setText(DataTimeUtils.getTimestampString(Long.valueOf(time)));
                textView.setVisibility(View.VISIBLE);
            } else {
                PrivateChatBean prevMessage = dataList.get(position - 1);
                if (prevMessage != null && DataTimeUtils.isCloseEnough(Long.valueOf(time), Long.valueOf(prevMessage.getAdd_time()))) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setText(DataTimeUtils.getTimestampString(Long.valueOf(time)));
                    textView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            //只有一条消息必须显示
            textView.setVisibility(View.VISIBLE);
            textView.setText(DataTimeUtils.getTimestampString(Long.valueOf(time)));
        }
//        String timeStr= HnUtils.getDateToString_3(Long.valueOf(time));
//        DateUtils.setTimeShow(timeStr, textView,context);
    }

    class OwnerHolder {

        public FrescoImageView mOwnerHeader;
        public TextView mOnwerContent;
        public TextView mOwnerTime;

        public OwnerHolder(View convertView) {
            mOwnerHeader = (FrescoImageView) convertView.findViewById(R.id.owner_header);
            mOnwerContent = (TextView) convertView.findViewById(R.id.owner_content);
            mOwnerTime = (TextView) convertView.findViewById(R.id.owner_time);
        }
    }

    class UserHolder {

        public FrescoImageView mUserHeader;
        public TextView mUserContent;
        public TextView mUserTime;

        public UserHolder(View convertView) {
            mUserHeader = (FrescoImageView) convertView.findViewById(R.id.user_header);
            mUserContent = (TextView) convertView.findViewById(R.id.user_content);
            mUserTime = (TextView) convertView.findViewById(R.id.user_time);
        }
    }

    class OwnerGiftHolder {

        public FrescoImageView mUserHeader;
        public TextView mGiftName;
        public FrescoImageView mGiftIcon;
        public TextView mOwnerTime;

        public OwnerGiftHolder(View convertView) {
            mUserHeader = (FrescoImageView) convertView.findViewById(R.id.owner_header);
            mGiftIcon = (FrescoImageView) convertView.findViewById(R.id.gift_icon);
            mGiftName = (TextView) convertView.findViewById(R.id.gift_name);
            mOwnerTime = (TextView) convertView.findViewById(R.id.owner_time);
        }
    }

    class UserGiftHolder {

        public FrescoImageView mUserHeader;
        public TextView mGiftName;
        public FrescoImageView mGiftIcon;
        public TextView mUserTime;
        public TextView mTvGiftCoin;

        public UserGiftHolder(View convertView) {
            mUserHeader = (FrescoImageView) convertView.findViewById(R.id.user_header);
            mGiftIcon = (FrescoImageView) convertView.findViewById(R.id.gift_icon);
            mGiftName = (TextView) convertView.findViewById(R.id.giftname);
            mUserTime = (TextView) convertView.findViewById(R.id.user_time);
            mTvGiftCoin = (TextView) convertView.findViewById(R.id.tv_gift_coin);
        }
    }
}

