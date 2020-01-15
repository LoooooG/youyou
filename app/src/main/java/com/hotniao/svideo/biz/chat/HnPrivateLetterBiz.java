package com.hotniao.svideo.biz.chat;

import android.animation.LayoutTransition;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.svideo.R;
import com.hotniao.livelibrary.control.HnUserControl;
import com.hotniao.livelibrary.model.HnSendPriMsgModel;
import com.hotniao.livelibrary.model.PrivateLetterDetailModel;
import com.hn.library.global.HnUrl;
import com.hotniao.svideo.utils.HnUiUtils;
import com.hotniao.svideo.utils.ScreenUtils;
import com.hotniao.livelibrary.util.SystemUtils;
import com.loopj.android.http.RequestParams;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于私信界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/12 16:33
 * 修改人：
 * 修改时间：2017/9/12 16:33
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateLetterBiz {
    private String TAG = "HnPrivateLetterBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnPrivateLetterBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网络请求：获取私信详情列表数据
     *
     * @param chatRoomId 聊天房间ID
     * @param dialogId   列表返回的最后一条ID，用来分页
     */
    public void requestToPrivateLetterDetail(String chatRoomId, String dialogId) {
        if (TextUtils.isEmpty(chatRoomId))
            return;
        RequestParams param = new RequestParams();
        param.put("chat_room_id", chatRoomId);
        param.put("dialog_id", dialogId + "");
        param.put("pagesize", 30 + "");
        HnHttpUtils.postRequest(HnUrl.PRIVATELETTER_DETAIL, param, TAG, new HnResponseHandler<PrivateLetterDetailModel>(context, PrivateLetterDetailModel.class) {
            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("Private_Msg_Detail_List", errCode, msg);
                }

            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("Private_Msg_Detail_List", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("Private_Msg_Detail_List", model.getC(), model.getM());
                    }
                }

            }
        });
    }

    /**
     * 网络请求：关注对方
     *
     * @param mUid 对方id
     */
    public void requestToFollow(String mUid) {
        if (TextUtils.isEmpty(mUid))
            return;
        if (listener != null) {
            listener.requesting();
        }
        HnUserControl.addFollow(mUid,null, new HnUserControl.OnUserOperationListener() {
            @Override
            public void onSuccess(String uid, Object obj, String response) {
                if (listener != null) {
                    listener.requestSuccess("add_follow", response, "");
                }
            }

            @Override
            public void onError(String uid, int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("add_follow", errCode, msg);
                }
            }
        });
    }


    /**
     * 网络请求:发送私信消息
     *
     * @param content 内容
     * @param mUid    对方id
     */
    public void requestToSendPrivateLetter(final String content, String mUid, String chatRoomId) {
        if (TextUtils.isEmpty(content)) {
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.letter_not_empty));
            return;
        }
        RequestParams param = new RequestParams();
        param.put("chat_room_id", chatRoomId);
        param.put("content", content);
        param.put("to_user_id", mUid);
        HnHttpUtils.postRequest(HnUrl.SEND_PRIVATELETTER, param, TAG, new HnResponseHandler<HnSendPriMsgModel>(context, HnSendPriMsgModel.class) {

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("send_msg", errCode, msg);
                  }
            }

            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("send_msg", content, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("send_msg", model.getC(), model.getM());
                    }
                }
            }
        });
    }

    /**
     * 显示gift视图
     */
    public void showGiftView(boolean showAnimation, LayoutTransition transitioner, RelativeLayout mGiftContainer, RelativeLayout mBottomCon, LinearLayout mContainer, EditText mPrivateChatInput, LinearLayout mOutcontainer) {
        if (showAnimation) {
            transitioner.setDuration(500);
        } else {
            transitioner.setDuration(0);
        }

        SystemUtils.hideSoftInput(mPrivateChatInput);

        mGiftContainer.getLayoutParams().height = ScreenUtils.dp2px(context, 278);
        mBottomCon.setVisibility(View.VISIBLE);
        mGiftContainer.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int lockHeight = SystemUtils.getAppContentHeight(context);
        lockContainerHeight(lockHeight, mOutcontainer);
    }

    /**
     * 显示emoji表情视图
     *
     * @param showAnimation
     * @param transitioner
     * @param mContainer
     * @param mBottomCon
     * @param mGiftContainer
     * @param mOutcontainer
     * @param mPrivateChatInput
     */
    public void showEmotionView(boolean showAnimation, LayoutTransition transitioner, LinearLayout mContainer, RelativeLayout mBottomCon, RelativeLayout mGiftContainer, LinearLayout mOutcontainer, EditText mPrivateChatInput) {
        if (showAnimation) {
            transitioner.setDuration(500);
        } else {
            transitioner.setDuration(0);
        }
        SystemUtils.hideSoftInput(mPrivateChatInput);
        mContainer.getLayoutParams().height = ScreenUtils.dp2px(context, 278);
        mBottomCon.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);
        mGiftContainer.setVisibility(View.GONE);
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int lockHeight = SystemUtils.getAppContentHeight(context);
        lockContainerHeight(lockHeight, mOutcontainer);
    }

    /**
     * 固定容器高度
     *
     * @param paramInt
     * @param mOutcontainer
     */
    private void lockContainerHeight(int paramInt, LinearLayout mOutcontainer) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) mOutcontainer.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    /**
     * 网络请求:退出对话详情  服务端清除对应的未读消息
     *
     * @param chatRoomId 聊天Id
     */
    public void requestToExitMsgDetail(String chatRoomId) {
        if (TextUtils.isEmpty(chatRoomId)) return;

        RequestParams param = new RequestParams();
        param.put("chat_room_id", chatRoomId);
        HnHttpUtils.postRequest(HnUrl.IGNORE_NOTREAD, param, "忽略未读", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {

            }

            @Override
            public void hnErr(int errCode, String msg) {
            }
        });
    }
}
