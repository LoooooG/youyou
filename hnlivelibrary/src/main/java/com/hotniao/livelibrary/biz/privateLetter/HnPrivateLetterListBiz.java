package com.hotniao.livelibrary.biz.privateLetter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.http.BaseResponseModel;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.http.RequestParam;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.CommDialog;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.model.webscoket.HnSysMsgModel;
import com.hotniao.livelibrary.widget.dialog.privateLetter.HnPrivateLetterDetailDialog;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：业务逻辑类,用于处理直播间私信列表业务和网络请求
 * 创建人：mj
 * 创建时间：2017/9/18 15:39
 * 修改人：Administrator
 * 修改时间：2017/9/18 15:39
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateLetterListBiz {

    private String TAG = "HnPrivateLetterListBiz";
    private BaseActivity context;

    private BaseRequestStateListener listener;

    public HnPrivateLetterListBiz(BaseActivity context) {
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
        HnHttpUtils.getRequest(HnLiveUrl.PRIVATE_LETTER_LIST, param, TAG, new HnResponseHandler<HnPrivateLetterListModel>(context,
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
        HnHttpUtils.postRequest(HnLiveUrl.IGNORE_NOTREAD, param, "忽略未读", new HnResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (model.getC() == 0) {
                    if (listener != null) {
                        listener.requestSuccess("private_msg_igonre", response, model);
                    }
                } else {
                    if (listener != null) {
                        listener.requestFail("private_msg_igonre", model.getC(), model.getM());
                    }
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (listener != null) {
                    listener.requestFail("private_msg_igonre", errCode, msg);
                }
            }
        });
    }

    /**
     * 若用户和主播没有私聊过，默认加在一条数据
     *
     * @param isAnchor    是否是主播的私信列表
     * @param anchor_id   主播id
     * @param avator      主播头像
     * @param anchor_nick 主播昵称
     * @param gender      主播性别
     * @param level       主播级别
     * @param itemLists   私信列表
     * @return
     */
    public List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> addDefaultChatData(boolean isAnchor, String anchor_id, String avator, String anchor_nick,
                                                                                             String gender, String level,
                                                                                             List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> itemLists,
                                                                                             String chatRoomId) {
        if (!isAnchor && !TextUtils.isEmpty(anchor_id)) {
            boolean isHavaAnchorLetter = false;
            int position = -1;
            for (int i = 0; i < itemLists.size(); i++) {
                if (anchor_id.equals(itemLists.get(i).getUser_id())) {
                    isHavaAnchorLetter = true;
                    position = i;
                    break;
                }
            }
            if (!isHavaAnchorLetter) {
                HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean = new HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean();
                bean.setUser_id(anchor_id);
                bean.setUser_avatar(avator);
                bean.setUser_nickname(anchor_nick);
                bean.setUnread("0");
                bean.setChat_room_id(chatRoomId);
                bean.setContent("哈喽，我是主播，快来和我聊吧");
                bean.setShowPriLetterStr(true);
                itemLists.add(0, bean);
            } else if (position != -1 && position != 0) {
                HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean itemsBean = itemLists.get(position);
                itemLists.remove(position);
                itemLists.add(0, itemsBean);
            }
        }
        return itemLists;
    }

    /**
     * 当接收到新的私信数据后，和当前的消息数据进行对比，如存在对应的uid，更新消息内容；锐不存在 ，则将其添加大炮列表中
     *
     * @param result
     * @param items
     * @param joinPrivateLetterUid
     * @return
     */
    public List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> receiverNewPrivateChatMsg(HnPrivateMsgModel.DataBean result, List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items, String joinPrivateLetterUid) {
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
            EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Receiver_New_Msg, 1));
        } else {//更新数据
            boolean isNewData = true;
            String uid = result.getDialog().getFrom_user().getUser_id();
            for (int i = 0; i < items.size(); i++) {
                String id = items.get(i).getUser_id();
                if (id.equals(uid)) {
                    isNewData = false;
                    String num = items.get(i).getUnread();
                    if (joinPrivateLetterUid.equals(id)) {
                        items.get(i).setUnread("0");
                    } else {
                        if (TextUtils.isEmpty(num) || "0".equals(num)) {
                            items.get(i).setUnread("1");
                        } else {
                            items.get(i).setUnread((Integer.valueOf(num) + 1) + "");
                        }
                        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Receiver_New_Msg, 1));
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
                EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Receiver_New_Msg, 1));
            }

        }

        return list;
    }


    /**
     * 更新系统消息数据
     *
     * @param result               推动过来的系统消息
     * @param items                私信列表
     * @param joinPrivateLetterUid
     * @return
     */
    public List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> updateSystemMsg(HnSysMsgModel.DataBean result, List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items, String joinPrivateLetterUid) {

        List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = new ArrayList<>();
        if (result == null) return items;
        for (int i = 0; i < items.size(); i++) {
            if ("0".equals(items.get(i).getUser_id())) {
                list.get(i).setContent(result.getContent());
                list.get(i).setTime(result.getAdd_time());
                if (joinPrivateLetterUid.equals("0")) {
                    list.get(i).setUnread("0");
                } else {
                    EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Receiver_New_Msg, 1));
                    String num = items.get(i).getUnread();
                    if (TextUtils.isEmpty(num) || "0".equals(num)) {
                        items.get(i).setUnread("1");
                    } else {
                        items.get(i).setUnread((Integer.valueOf(num) + 1) + "");
                    }
                }
            }
        }
        return list;
    }


    /**
     * 确认是否删除私信消息
     *
     * @param itemsBean
     * @param position
     * @param anchor_id 主播id
     */
    public void deleteMsg(final HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean itemsBean, final int position, Context context, String anchor_id) {
        if (itemsBean != null && context != null) {
            //系统消息不可删除
            String uid = itemsBean.getUser_id();
            if ("0".contains(uid)) return;
            //主播不可删除
            if (!TextUtils.isEmpty(anchor_id)) {
                if (anchor_id.equals(uid)) return;
            }
            CommDialog.newInstance(context).setClickListen(new CommDialog.TwoSelDialog() {
                @Override
                public void leftClick() {

                }

                @Override
                public void rightClick() {
                    destoryMsg(itemsBean.getChat_room_id(), position);
                }
            }).setTitle(HnUiUtils.getString(R.string.hint)).setContent(HnUiUtils.getString(R.string.live_delete_msg_tip)).show();
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
        HnHttpUtils.postRequest(HnLiveUrl.DESTORY_MSG, param, TAG, new HnResponseHandler<BaseResponseModel>(context, BaseResponseModel.class) {
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
     * 进入dialog聊天室
     *
     * @param mActivity 上下文
     * @param bean      消表中item数据
     */
    public void joinToChatRoomDialog(AppCompatActivity mActivity, HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean) {
        if (bean == null || context == null)
            return;
        String uid = bean.getUser_id();
        if (!TextUtils.isEmpty(uid)) {
            HnPrivateLetterDetailDialog mHnPrivateLetterDetailDialog = HnPrivateLetterDetailDialog.getInstance(uid, bean.getUser_nickname(), bean.getUnread(), bean.getChat_room_id());
            HnLogUtils.i("mj", "清除未读消息:" + bean.getUnread());
            mHnPrivateLetterDetailDialog.show(mActivity.getSupportFragmentManager(), "HnPrivateLetterDetailDialog");
        }
    }

}
