package com.hotniao.livelibrary.biz.livebase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.utils.HnLogUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.biz.gift.HnGiftBiz;
import com.hotniao.livelibrary.giflist.bean.GiftListBean;
import com.hotniao.livelibrary.model.bean.HnReceiveSocketBean;
import com.hotniao.livelibrary.model.bean.ReceivedSockedBean;
import com.hotniao.livelibrary.widget.dialog.HnAnchorStopDialog;
import com.hotniao.livelibrary.widget.dialog.HnLiveFinishDialog;
import com.hotniao.livelibrary.widget.dialog.HnUserDetailDialog;
import com.hotniao.livelibrary.widget.gift.GiftModel;
import com.hotniao.livelibrary.widget.gift.LeftGiftControlLayout;
import com.hotniao.livelibrary.widget.gift.bigGift.BigGiftActionManager;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class HnLiveBaseViewBiz {


    private String TAG = "HnLiveBaseViewBiz";

    /**
     * 返回键/点击返回按钮时触发  先将界面的消息列表/键盘/表情等退出，再显示退出提示框
     *
     * @param rlMessage         消息列表
     * @param llBottomContainer 底部按钮控件父布局
     * @param mElEmotion        表情父布局
     * @param mLiveOnline       直播时长（秒） 主播端使用
     * @param type              类型  0 主播  1 用户
     * @param mLiveType         直播类型
     * @param context           基类上下文
     */
    public void onBack(View rlMessage, View llBottomContainer, View mElEmotion, long mLiveOnline, int type, String mLiveType, BaseActivity context) {

        if (rlMessage != null && rlMessage.getVisibility() == View.VISIBLE && llBottomContainer.getVisibility() == View.GONE) {
            rlMessage.setVisibility(View.GONE);
            llBottomContainer.setVisibility(View.VISIBLE);
            if (mElEmotion!=null&&mElEmotion.getVisibility() == View.VISIBLE) {
                mElEmotion.setVisibility(View.GONE);
            }
        } else {//退出提示框
            HnAnchorStopDialog exitDialogView = HnAnchorStopDialog.getInstance(mLiveOnline, type, mLiveType);
            exitDialogView.setBaseActvity(context);
            exitDialogView.show(context.getSupportFragmentManager(), "exit");
        }
    }


    /**
     * 显示用户信息弹出框
     *
     * @param mUid      点击的用户的id
     * @param ownUid    自己的id
     * @param mActivity 上下文
     */
    public void showUserInfoDialog(String mUid, String ownUid, BaseActivity mActivity, int from, String anchorId, String isAdmin) {
        if (TextUtils.isEmpty(mUid) || mActivity == null) return;
        HnUserDetailDialog dialog = HnUserDetailDialog.newInstance(from, mUid, ownUid, 1, anchorId, isAdmin,false);
        dialog.setActvity(mActivity);
        dialog.show(mActivity.getSupportFragmentManager(), "HnUserDetailDialog");
    }
    /**
     * 显示用户信息弹出框
     *
     * @param mUid      点击的用户的id
     * @param ownUid    自己的id
     * @param mActivity 上下文
     */
    public void showUserInfoDialog(String mUid, String ownUid, BaseActivity mActivity, int from, String anchorId, String isAdmin,boolean isLookAnchor) {
        if (TextUtils.isEmpty(mUid) || mActivity == null) return;
        HnUserDetailDialog dialog = HnUserDetailDialog.newInstance(from, mUid, ownUid, 1, anchorId, isAdmin,isLookAnchor);
        dialog.setActvity(mActivity);
        dialog.show(mActivity.getSupportFragmentManager(), "HnUserDetailDialog");
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
    public void initEmotionKeyboard(BaseActivity mActivity, LinearLayout mLlContent, final ImageView ivEmoj, EditText etSendData, final EmotionLayout mElEmotion) {
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
     * 推流未推上去，服务器已经结束直播，主播客户端需要显示直播结束对话框
     *
     * @param object
     * @param mUid
     */
    public void showLiveFinishDialog(Object object, String mUid, AppCompatActivity context) {
        ReceivedSockedBean result = (ReceivedSockedBean) object;
        if (result != null) {
            String id = result.getData().getUid();
            if (mUid.equals(id)) {
                HnLiveFinishDialog dialog = HnLiveFinishDialog.newInstance();
                dialog.show(context.getSupportFragmentManager(), "HnLiveFinishDialog");
            }
        }
    }
}
