package com.hotniao.live.biz.msg;

import android.os.Bundle;
import android.text.TextUtils;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.utils.HnUtils;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnPrivateChatActivity;
import com.hn.library.global.HnConstants;
import com.hn.library.global.HnUrl;
import com.hn.library.view.CommDialog;
import com.hotniao.live.model.HnSysMsgModel;
import com.hotniao.live.model.HnVideoNoReadModel;
import com.hotniao.livelibrary.model.HnNoReadMessageModel;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类，用于处理消息界面的网络请求以及业务逻辑
 * 创建人：mj
 * 创建时间：2017/9/12 9:16
 * 修改人：
 * 修改时间：2017/9/12 9:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMsgBiz {

    public static final String NoReadMsg = "NoReadMsg";//为阅读消息
    private String TAG = "HnMsgBiz";
    private BaseActivity context;
    private BaseRequestStateListener listener;

    public HnMsgBiz(BaseActivity context) {
        this.context = context;
    }

    public void setBaseRequestStateListener(BaseRequestStateListener listener) {
        this.listener = listener;
    }

    /**
     * 网络请求：获取私信列表数据
     *
     * @param mPage 页数
     */
    public void requestToPriMsgList(int mPage) {
        RequestParam param = new RequestParam();
        param.put("page", mPage + "");
        param.put("pagesize", 100 + "");
        HnHttpUtils.getRequest(HnUrl.PRIVATE_LETTER_LIST, param, TAG, new HnResponseHandler<HnPrivateLetterListModel>(context,
                HnPrivateLetterListModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("private_msg_list", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("private_msg_list", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("private_msg_list", errCode, msg);
                }
            }
        });
    }

    /**
     * 网络请求:忽略消息未读
     */
    public void requestToIgnoreUnRead() {
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("chat_room_id", "");
        param.put("type", "");
        HnHttpUtils.postRequest(HnUrl.IGNORE_NOTREAD, param, "忽略未读", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("Ignore_Unread_Msg", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("Ignore_Unread_Msg", model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("Ignore_Unread_Msg", errCode, msg);
                }
            }
        });
    }

    /**
     * 确认是否删除私信消息
     *
     * @param itemsBean
     * @param position
     */
    public void deleteMsg(final HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean itemsBean, final int position) {
        if (itemsBean != null && context != null) {
            CommDialog.newInstance(context).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {

                }

                @Override
                public void rightClick() {
                    destoryMsg(itemsBean.getChat_room_id(), position);
                }
            }).setTitle(HnUiUtils.getString(R.string.delete_msg)).setContent(HnUiUtils.getString(R.string.delete_msg_tip)).show();
        }
    }

    /**
     * 网络请求:删除该条私信
     *
     * @param dialogId 对方的用户id
     * @param position
     */
    private void destoryMsg(String dialogId, final int position) {
        if (TextUtils.isEmpty(dialogId)) return;
        if (listener != null) {
            listener.requesting();
        }
        RequestParams param = new RequestParams();
        param.put("chat_room_id", dialogId);
        HnHttpUtils.postRequest(HnUrl.DESTORY_MSG, param, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("Delete_Msg", position + "", model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("Delete_Msg", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("Delete_Msg", errCode, msg);
                }
            }
        });

    }

    /**
     * 网络请求:获取系统消息
     */
    public void getSystemMsg() {
        HnHttpUtils.postRequest(HnUrl.CHAT_SYSTEM_DIALOG_LIST, null, TAG, new HnResponseHandler<HnSysMsgModel>(context, HnSysMsgModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("SystemMsg", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("SystemMsg", model.getC(), model.getM());
                    }
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("SystemMsg", errCode, msg);
                }
            }
        });

    }

    /**
     * 获取未读消息数
     */
    public void getVideoNewMessage() {
        HnHttpUtils.postRequest(HnUrl.VIDEO_MSG_GET_UNREAD_NUM, null, HnUrl.VIDEO_MSG_GET_UNREAD_NUM, new HnResponseHandler<HnVideoNoReadModel>(HnVideoNoReadModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess(NoReadMsg, response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail(NoReadMsg, model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail(NoReadMsg, errCode, msg);
                }
            }
        });
    }

    /**
     * 进入聊天室
     *
     * @param bean 消息列表中item数据
     */
    public void joinToChatRoom(HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean) {
        if (bean == null || context == null)
            return;
        String uid = bean.getUser_id();
        if (!TextUtils.isEmpty(uid)) {
            Bundle bundle = new Bundle();
            bundle.putString(HnConstants.Intent.DATA, uid);
            bundle.putString(HnConstants.Intent.Name, bean.getUser_nickname());
            bundle.putString(HnConstants.Intent.ChatRoomId, bean.getChat_room_id());
            context.openActivity(HnPrivateChatActivity.class, bundle);
        }
    }

    /**
     * 获取未读数量
     *
     * @param unReadNum
     * @return
     */
    public int getUnreadNum(String unReadNum) {
        int num = 0;
        if (TextUtils.isEmpty(unReadNum) || "0".equals(unReadNum)) {
            num = 1;
        } else {
            num = Integer.valueOf(unReadNum) + 1;
        }

        return num;
    }

    /**
     * 获取消息时间
     *
     * @param update_time
     * @return
     */
    public String getAddTime(String update_time) {
        if (!TextUtils.isEmpty(update_time)) {
            long l = Long.parseLong(update_time);
            String time = HnUtils.getDateToString_4(l);
            return time;
        }
        return null;
    }

    /**
     * 当接收到新的私信数据后，和当前的消息数据进行对比，如存在对应的uid，更新消息内容；锐不存在 ，则将其添加大炮列表中
     *
     * @param result
     * @param items
     * @return
     */
    public List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> receiverNewPrivateChatMsg(HnPrivateMsgModel.DataBean result, List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items) {
        List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = new ArrayList<>();
        if (result == null) return items;
        if (items.size() == 0) {//第一条数据
            HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean = new HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean();
            bean.setUnread("1");
            bean.setUser_id(result.getDialog().getFrom_user().getUser_id());
            bean.setUser_avatar(result.getDialog().getFrom_user().getUser_avatar());
            bean.setUser_nickname(result.getDialog().getFrom_user().getUser_nickname());
            bean.setContent(result.getDialog().getMsg());
            bean.setTime(result.getDialog().getTime() + "");
            bean.setChat_room_id(result.getChat_room_id());
            list.add(bean);
        } else {//更新数据
            boolean isNewData = true;
            String uid = result.getDialog().getFrom_user().getUser_id();
            for (int i = 0; i < items.size(); i++) {
                String id = items.get(i).getUser_id();
                if (id.equals(uid)) {
                    isNewData = false;
                    String num = items.get(i).getUnread();
                    if (TextUtils.isEmpty(num) || "0".equals(num)) {
                        items.get(i).setUnread("1");
                    } else {
                        items.get(i).setUnread((Integer.valueOf(num) + 1) + "");
                    }
                    items.get(i).setUser_id(result.getDialog().getFrom_user().getUser_id());
                    items.get(i).setUser_avatar(result.getDialog().getFrom_user().getUser_avatar());
                    items.get(i).setUser_nickname(result.getDialog().getFrom_user().getUser_nickname());
                    items.get(i).setContent(result.getDialog().getMsg());
                    items.get(i).setTime(result.getDialog().getTime() + "");
                    items.get(i).setChat_room_id(result.getChat_room_id());
                    list.addAll(items);
                    break;
                }
            }
            //新数据
            if (isNewData) {
                HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean = new HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean();
                bean.setUnread("1");
                bean.setUser_id(result.getDialog().getFrom_user().getUser_id());
                bean.setUser_avatar(result.getDialog().getFrom_user().getUser_avatar());
                bean.setUser_nickname(result.getDialog().getFrom_user().getUser_nickname());
                bean.setContent(result.getDialog().getMsg());
                bean.setTime(result.getDialog().getTime() + "");
                bean.setChat_room_id(result.getChat_room_id());
                items.add(bean);
                list.addAll(items);
            }

        }

        return list;
    }

    /**
     * 根据uid，清除所对应的未读数
     *
     * @param uid   uid
     * @param items 列表数据
     * @return
     */
    public List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> cleanUnreaByUid(String uid, List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items) {
        if (items != null && items.size() > 0 && !TextUtils.isEmpty(uid)) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getUser_id().equals(uid)) {
                    String data = items.get(i).getUnread();
                    if (!TextUtils.isEmpty(data) && !"0".equals(data)) {
                    }
                    items.get(i).setUnread("0");
                    break;
                }
            }
        }
        return items;
    }
}
