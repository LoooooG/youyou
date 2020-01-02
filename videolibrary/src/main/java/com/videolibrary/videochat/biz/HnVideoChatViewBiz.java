package com.videolibrary.videochat.biz;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.anchor.HnAnchorInfoListener;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.util.HnLiveDateUtils;
import com.hotniao.livelibrary.util.HnLiveUIUtils;
import com.hotniao.livelibrary.widget.gift.GiftModel;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，该类用于处理直播间 客户端和主播端 公共的view交互，不做业务处理以及网络请求
 * 创建人：Administrator
 * 创建时间：2017/9/20 10:41
 * 修改人：Administrator
 * 修改时间：2017/9/20 10:41
 * 修改备注：
 * Version:  1.0.0
 */
public class HnVideoChatViewBiz {

    public static final String ChatTimeTask = "ChatTimeTask";
    public static final String WaitTimeTask = "WaitTimeTask";
    public static final String PayTimeTask = "PayTimeTask";

    private String TAG = "HnVideoChatViewBiz";
    /**
     * 时间 定时器
     */
    private Disposable mChatObservable;
    private Disposable mPayObservable;
    private long mChatTime = 0;
    private boolean isCancleWait = false;

    /**
     * 主播端信息回调接口
     */
    private HnAnchorInfoListener listener;
    private Context mContext;

    public HnVideoChatViewBiz(Context mActivity) {
        mContext = mActivity;
    }


    public void setListener(HnAnchorInfoListener listener) {
        this.listener = listener;
    }

    /**
     * 弹出发送消息界面
     *
     * @param mRlMessage       发送消息的父容器
     * @param mBottomContainer 底部按钮控制容器
     * @param etSendData       输入框
     * @param context          消息框
     */
    public void showMessageSendLayout(View mRlMessage, View mBottomContainer, EditText etSendData, Context context) {
        if (mBottomContainer != null) {
            mBottomContainer.setVisibility(View.GONE);
        }
        if (mRlMessage != null) {
            mRlMessage.setVisibility(View.VISIBLE);
        }
        if (etSendData != null) {
            //自动获取焦点
            etSendData.setFocusable(true);
            etSendData.setFocusableInTouchMode(true);
            etSendData.requestFocus();
            openSoftKeyBoard(context);
        }
    }

    /**
     * 用于处理界面的emoji容器和键盘之间关系
     *
     * @param mActivity
     * @param mLlContent
     * @param ivEmoj
     * @param etSendData
     * @param mElEmotion
     */
    public void initEmotionKeyboard(BaseActivity mActivity, RelativeLayout mLlContent, final ImageView ivEmoj, EditText etSendData, final EmotionLayout mElEmotion) {
        EmotionKeyboard mEmotionKeyboard = EmotionKeyboard.with(mActivity);
        mEmotionKeyboard.bindToContent(mLlContent);
        mEmotionKeyboard.bindToEmotionButton(ivEmoj);
        mEmotionKeyboard.bindToEditText(etSendData);
        mEmotionKeyboard.setEmotionLayout(mElEmotion);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                if (!mElEmotion.isShown()) {
                    ivEmoj.setImageResource(R.mipmap.keyboard);

                } else if (mElEmotion.isShown()) {
                    ivEmoj.setImageResource(R.mipmap.smile);
                }
                return false;
            }
        });
    }


    /**
     * 隐藏键盘
     *
     * @param view    控件
     * @param context 上下文
     */
    public void hideSoftKeyBoard(View view, Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 弹出软键盘
     */
    public void openSoftKeyBoard(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 加载礼物动画
     *
     * @param bean                  数据源
     * @param mBigGiftActionManager 礼物管理器
     * @param leftFiftLl            小礼物显示控件
     * @param gifts                 礼物列表数据
     * @param mHnGiftBiz
     */
    public boolean loadGift(HnReceiveSocketBean bean, Context context, BigGiftActionManager mBigGiftActionManager, LeftGiftControlLayout leftFiftLl, ArrayList<GiftListBean> gifts, HnGiftBiz mHnGiftBiz) {
        if (bean == null || gifts.size() == 0) return true;
        String giftId = bean.getData().getLive_gift().getLive_gift_id();
        List<String> giftIdList = new ArrayList<>();
        for (int i = 0; i < gifts.size(); i++) {
            giftIdList.add(gifts.get(i).getGift_id());
        }
        if (giftIdList.contains(giftId)) {
            for (int i = 0; i < gifts.size(); i++) {
                String id = gifts.get(i).getGift_id();
                String name = gifts.get(i).getGiftName();
                String zipUrl = gifts.get(i).getZipDownUrl();
                if (id.equals(giftId)) {
                    HnLogUtils.i(TAG, "需要加载的礼物id:" + giftId + "----》大礼物地址：" + zipUrl);
                    //小礼物
                    if (TextUtils.isEmpty(zipUrl)) {
                        HnReceiveSocketBean.DataBean.UserBean fuser = bean.getData().getUser();
                        String fuserNick = fuser.getUser_nickname();
                        String fuserAvatar = fuser.getUser_avatar();
                        String fuserid = fuser.getUser_id();
                        String fuserLv = fuser.getUser_level();
                        String giftName = gifts.get(i).getGiftName();
                        String gid = id;
                        String giftAvatar = gifts.get(i).getStaticGiftUrl();
                        HnLogUtils.i(TAG, giftAvatar);
                        leftFiftLl.loadGift(GiftModel.create(gid, giftName, bean.getData().getLive_gift().getLive_gift_nunmer(), giftAvatar, fuserid, fuserNick, fuserAvatar, fuserLv));
                        break;
                        //大礼物
                    } else {
                        String localUrl = gifts.get(i).getZipDownLocalUrl();
                        if (!TextUtils.isEmpty(localUrl)) {
                            File file = new File(localUrl);
                            if (file.exists()) {
                                bean.getData().setBigGiftAddress(localUrl);
                                bean.getData().getLive_gift().setLive_gift_name(name);
                                mBigGiftActionManager.addDanmu(bean.getData());
                                break;
                            } else {
                                if (mHnGiftBiz != null) {
                                    mHnGiftBiz.downloadBigGift(gifts.get(i), context, bean);
                                }
                                break;
                            }
                        } else {
                            if (mHnGiftBiz != null) {
                                mHnGiftBiz.downloadBigGift(gifts.get(i), context, bean);
                            }
                            break;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * 当大礼物下载完成后，更新礼物数据
     *
     * @param isShow                是否显示需要下载的数据
     * @param data                  礼物数据
     * @param mBigGiftActionManager 大礼物管理器
     * @param gifts                 礼物列表
     * @return
     */
    public ArrayList<GiftListBean> updateGiftListData(Object obj, boolean isShow, GiftListBean data, BigGiftActionManager mBigGiftActionManager, ArrayList<GiftListBean> gifts) {
        if (data != null) {
            String gift_id = data.getGift_id();
            String localUrl = data.getZipDownLocalUrl();
            for (int i = 0; i < gifts.size(); i++) {
                String id = gifts.get(i).getGift_id();
                if (gift_id.equals(id)) {
                    gifts.get(i).setZipDownLocalUrl(localUrl);
                }
            }
            if (isShow) {
                if (obj != null) {
                    HnReceiveSocketBean bean = (HnReceiveSocketBean) obj;
                    if (bean != null && bean.getData() != null) {
                        bean.getData().setBigGiftAddress(data.getZipDownLocalUrl());
                    }
                    if (bean != null && bean.getData() != null && !TextUtils.isEmpty(bean.getData().getBigGiftAddress())) {
                        mBigGiftActionManager.addDanmu(bean.getData());
                    }
                }
            }
        }
        return gifts;
    }

    /**
     * 获取对应的礼物数据
     *
     * @param object   推送的数据
     * @param giftList 礼物列表
     * @return
     */
    public HnReceiveSocketBean getGiftData(Object object, List<GiftListBean> giftList) {
        final HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if (bean != null && giftList.size() > 0) {
            String id = bean.getData().getLive_gift().getLive_gift_id();
            for (int i = 0; i < giftList.size(); i++) {
                String gift_id = giftList.get(i).getGift_id();
                String name = giftList.get(i).getGiftName();
                if (id.equals(gift_id)) {
                    bean.getData().getLive_gift().setLive_gift_name(name);
                    return bean;
                }
            }
        }
        return null;
    }

    /**
     * 将公聊消息添加到适配器中
     *
     * @param object      数据源
     * @param messageList 公聊消息列表
     * @return
     */
    public ArrayList<HnReceiveSocketBean> addMsgData(Object object, ArrayList<HnReceiveSocketBean> messageList) {
        if (object == null) {
            return messageList;
        }
        final HnReceiveSocketBean bean = (HnReceiveSocketBean) object;
        if (bean != null) {
            if (messageList != null) {
                if (messageList.size() > 200) {
                    messageList.remove(0);
                    messageList.add(bean);
                } else {
                    messageList.add(bean);
                }
            }
        }
        return messageList;
    }


    /**
     * 界面touch事件处理  当用户点击界面触发touch事件时，将发送消息的布局/键盘影藏，底部/顶部的按钮显示出来。
     *
     * @param view              onTouch 返回的view
     * @param mElEmotion        表情控制容器
     * @param rlMessage         发送消息容器
     * @param llBottomContainer 底部按钮容器
     * @param context           上下文
     */
    public void onTouch(View view, EmotionLayout mElEmotion, RelativeLayout rlMessage, RelativeLayout llBottomContainer, Context context) {
        if (!(view instanceof EditText) || !(view instanceof ImageView)) {
            if (rlMessage.getVisibility() == View.VISIBLE) {
                llBottomContainer.setVisibility(View.VISIBLE);
                rlMessage.setVisibility(View.GONE);

                if (mElEmotion.getVisibility() == View.VISIBLE) {
                    mElEmotion.setVisibility(View.GONE);
                }

                hideSoftKeyBoard(rlMessage, context);
            }
        }
    }

    /**
     * 直播间视图变化时的ui处理
     *
     * @param bottom            底部距离
     * @param oldBottom         之前的底部距离
     * @param mElEmotion        表情控件
     * @param leftFiftLl        礼物控制器
     * @param rlMessage         消息父容器
     * @param llBottomContainer 底部按钮容器
     * @param context           上下文
     * @param keyHeight         阀值设置为屏幕高度的1/3
     * @param ivEmoj
     */
    public void onLayoutChnage(int bottom, int oldBottom, EmotionLayout mElEmotion, LeftGiftControlLayout leftFiftLl,
                               RelativeLayout rlMessage, RelativeLayout llBottomContainer, Context context, int keyHeight, ImageView ivEmoj) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {  //键盘弹出
            if (ivEmoj != null) ivEmoj.setImageResource(R.mipmap.smile);
            if (leftFiftLl != null) {
                setMargins(leftFiftLl, 0, 0, 0, HnLiveUIUtils.dip2px(context, -60));
            }
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {  //键盘隐藏
            if (mElEmotion != null && mElEmotion.getVisibility() == View.GONE) {
                if (rlMessage != null && rlMessage.getVisibility() == View.VISIBLE) {
                    rlMessage.setVisibility(View.GONE);
                }
                if (llBottomContainer != null && llBottomContainer.getVisibility() == View.GONE) {
                    llBottomContainer.setVisibility(View.VISIBLE);
                }
            }
            if (leftFiftLl != null) {
                setMargins(leftFiftLl, 0, 0, 0, 0);
            }
        }
    }

    /**
     * 设置边距
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /**
     * 定时器：用于计算直播间的时间
     */
    public void startChatTimeTask() {
        mChatObservable = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mChatTime += 1;
                        String timeStr = HnLiveDateUtils.getLiveTime(mChatTime);
                        HnLogUtils.i(TAG, "直播时间：" + timeStr + "---->" + aLong);
                        if (listener != null) {
                            listener.showTimeTask(mChatTime, timeStr, ChatTimeTask);
                        }
                    }
                });


    }

    /**
     * 定时器：用于计算扣费的时间
     */
    public void startPayChatTask() {
        mPayObservable = Observable.interval(0, 60 * 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (listener != null) {
                            listener.showTimeTask(mChatTime, "", PayTimeTask);
                        }
                    }
                });

    }

    /**
     * 用于等待接收者接收的时间
     */
    public void startWaitTimeTast(Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancleWait && mContext != null) {
                    if (listener != null) {
                        listener.showTimeTask(mChatTime, "", WaitTimeTask);
                    }
                }
            }
        }, 60 * 1000);

    }

    /**
     * 关闭定时器
     */
    public void closeChatObservable() {
        if (mChatObservable != null) {
            mChatObservable.dispose();
            HnLogUtils.i(TAG, "关闭时间定时器");
        }

    }

    /**
     * 关闭定时器
     */
    public void closePayObservable() {
        if (mPayObservable != null) {
            mPayObservable.dispose();
            HnLogUtils.i(TAG, "关闭时间定时器");
        }

    }

    /**
     * 关闭定时器
     */
    public void closeWaitObservable() {
        isCancleWait = true;
        mContext = null;
    }

}
