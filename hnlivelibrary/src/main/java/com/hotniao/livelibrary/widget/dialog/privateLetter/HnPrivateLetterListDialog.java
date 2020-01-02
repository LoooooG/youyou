package com.hotniao.livelibrary.widget.dialog.privateLetter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.adapter.HnLivePrivLetterListAdapter;
import com.hotniao.livelibrary.biz.privateLetter.HnPrivateLetterListBiz;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.model.webscoket.HnSysMsgModel;
import com.hotniao.livelibrary.util.HnLiveScreentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：youbo
 * 类描述：私信列表对话框
 * 创建人：mj
 * 创建时间：2017/9/18 15:17
 * 修改人：Administrator
 * 修改时间：2017/9/18 15:17
 * 修改备注：
 * Version:  1.0.0
 */
public class HnPrivateLetterListDialog extends AppCompatDialogFragment implements View.OnClickListener, BaseRequestStateListener, HnLoadingLayout.OnReloadListener {


    private TextView tvIgone;
    private HnLoadingLayout mHnLoadingLayout;
    private PtrClassicFrameLayout mSwiperefresh;
    private RecyclerView mRecyclerView;

    /**
     * 上下文
     */
    private BaseActivity mActivity;
    /**
     * 业务逻辑类
     */
    private HnPrivateLetterListBiz mHnPrivateLetterListBiz;
    /**
     * 消息列表适配器
     */
    private HnLivePrivLetterListAdapter mAdapter;


    /**
     * 页数
     */
    private int mPage = 1;

    /**
     * 是否是主播
     */
    private boolean isAnchor = false;
    /**
     * 主播id
     */
    private String anchor_id = "";
    /**
     * 主播昵称
     */
    private String anchor_nick = "";
    /**
     * 主播头像
     */
    private String avator = "";
    /**
     * 主播等级
     */
    private String level = "";
    /**
     * 主播性别
     */
    private String gender = "";
    /**
     * 房间Id
     */
    private String mChatRoomId = "";

    /**
     * 是否显示私聊详情信息
     */
    private String joinPrivateLetterUid = "-1";

    public static HnPrivateLetterListDialog getInstance(boolean isAnchor, String anchor_id, String anchor_nick, String avator, String level, String gender, String mChatRoomId) {
        HnPrivateLetterListDialog sDialog = new HnPrivateLetterListDialog();
        Bundle b = new Bundle();
        b.putBoolean("isAnchor", isAnchor);
        b.putString("anchor_id", anchor_id);
        b.putString("anchor_nick", anchor_nick);
        b.putString("avator", avator);
        b.putString("level", level);
        b.putString("gender", gender);
        b.putString("mChatRoomId", mChatRoomId);
        sDialog.setArguments(b);
        return sDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = (BaseActivity) getActivity();
        mHnPrivateLetterListBiz = new HnPrivateLetterListBiz(mActivity);
        mHnPrivateLetterListBiz.setBaseRequestStateListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isAnchor = bundle.getBoolean("isAnchor");
            anchor_id = bundle.getString("anchor_id");
            anchor_nick = bundle.getString("anchor_nick");
            avator = bundle.getString("avator");
            level = bundle.getString("level");
            mChatRoomId = bundle.getString("mChatRoomId");
            gender = bundle.getString("gender");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.live_dialog_private_letter_list_layout, null);
        Dialog dialog = new Dialog(mActivity, R.style.BottomDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window alertWindow = dialog.getWindow();
        alertWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = alertWindow.getAttributes();
        params.width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        params.height = (params.width / 2) + HnLiveScreentUtils.dp2px(mActivity, 80.2f);
        alertWindow.setAttributes(params);
        //初始化组件
        initView(view);
        //初始化数据
        initData();
        return dialog;
    }


    private void initView(View view) {
        //忽略
        tvIgone = (TextView) view.findViewById(R.id.tv_ignore);
        tvIgone.setOnClickListener(this);
        mSwiperefresh = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        initAdapter();
        mHnLoadingLayout = (HnLoadingLayout) view.findViewById(R.id.loading);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mSwiperefresh.setMode(PtrFrameLayout.Mode.BOTH);
        //刷新监听
        mSwiperefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                mHnPrivateLetterListBiz.requestToPriMsgList(mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnPrivateLetterListBiz.requestToPriMsgList(mPage);

            }
        });


    }

    /**
     * 初始化数据
     */
    private void initData() {
        mPage = 1;
        mHnPrivateLetterListBiz.requestToPriMsgList(mPage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ignore) {//忽略未读
            mHnPrivateLetterListBiz.requestToIgnoreUnRead();
        }
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null) return;
        if ("private_msg_list".equals(type)) {//获取私信列表
            mActivity.setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            mActivity.closeRefresh(mSwiperefresh);
            HnPrivateLetterListModel model = (HnPrivateLetterListModel) obj;
            if (model != null && model.getD().getUser_dialogs() != null) {
                if (mPage >= model.getD().getUser_dialogs().getPagetotal()) {
                    mSwiperefresh.setMode(PtrFrameLayout.Mode.REFRESH);
                }
                updateUi(model.getD().getUser_dialogs().getItems());
            } else {
                if (mPage == 1) {
                    mActivity.setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                }
            }

        } else if ("private_msg_igonre".equals(type)) {//忽略未读
            HnToastUtils.showToastShort(mActivity.getResources().getString(R.string.live_readed));
            List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setUnread("0");
            }
            mAdapter.setNewData(items);
            EventBus.getDefault().post(new HnLiveEvent(2, HnLiveConstants.EventBus.Clear_Unread_Count, "0"));
        } else if ("Delete_Msg".equals(type)) {//删除对话
            if (!TextUtils.isEmpty(response)) {
                int pos = Integer.valueOf(response);
                mAdapter.remove(pos);
                HnToastUtils.showToastShort(R.string.live_delete_success);
            }

//            initData();
        }

    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null) return;
        if ("private_msg_list".equals(type)) {//获取私信列表
            mActivity.closeRefresh(mSwiperefresh);
            if (mPage == 1) {
                if (REQUEST_NET_ERROR == code) {
                    mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    mActivity.setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }
        } else if ("private_msg_igonre".equals(type)) {//忽略未读
            HnToastUtils.showToastShort(msg);
        } else if ("Delete_Msg".equals(type)) {//删除对话
            HnToastUtils.showToastShort(msg);
        }
    }

    @Override
    public void onReload(View v) {
        initData();
    }

    /*
     *  初始化适配器
     */
    public void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HnLivePrivLetterListAdapter(mActivity);
            mRecyclerView.setAdapter(mAdapter);

            //长按删除消息
            mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                    HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean itemsBean = (HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean) adapter.getItem(position);
                    mHnPrivateLetterListBiz.deleteMsg(itemsBean, position, mActivity, anchor_id);
                    return true;
                }
            });
            //点击进入聊天室
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean = (HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean) adapter.getItem(position);
                    String uid = bean.getUser_id();
                    if ("0".equals(uid)) {
                        joinPrivateLetterUid = "0";
                        Bundle bundle = new Bundle();
                        bundle.putString("unread_msg", bean.getUnread());
                        ARouter.getInstance().build("/app/HnSystemMessageActivity").with(bundle).navigation();
                    } else {
                        joinPrivateLetterUid = uid;
                        mHnPrivateLetterListBiz.joinToChatRoomDialog(mActivity, bean);
                    }
                    mAdapter.getItem(position).setUnread("0");
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 更新ui界面  若用户和主播没有私聊过，默认加在一条数据
     *
     * @param itemLists 消息列表数据   当uid为0时，更新系统消息，其他为私信消息  在私信列表中需要将系统消息数据清除
     */
    private void updateUi(List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> itemLists) {
        itemLists = mHnPrivateLetterListBiz.addDefaultChatData(isAnchor, anchor_id, avator, anchor_nick, gender, level, itemLists, mChatRoomId);
        //私信列表
        if (mPage == 1) {
            mAdapter.setNewData(itemLists);
        } else {
            mAdapter.addData(itemLists);
        }
    }


    /**
     * 接收到webscoket推送过来的私信消息数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrivateEvent(HnPrivateMsgEvent event) {
        if (event != null) {
            if (HnWebscoketConstants.Send_Pri_Msg.equals(event.getType())) {
                HnPrivateMsgModel.DataBean result = event.getData().getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = mHnPrivateLetterListBiz.receiverNewPrivateChatMsg(result, items, joinPrivateLetterUid);
                mAdapter.setNewData(list);
            }
        }
    }


    /**
     * 接收到webscoket推送过来的系统消息数据
     *
     * @param event
     */
    @Subscribe
    public void receiverSystemMsgEvent(HnReceiverSysMsgEvent event) {
        if (event != null) {
            if ("system_msg".equals(event.getType())) {
                HnSysMsgModel.DataBean result = event.getData().getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = mHnPrivateLetterListBiz.updateSystemMsg(result, items, joinPrivateLetterUid);
                mAdapter.setNewData(list);
            }
        }
    }


    @Subscribe
    public void receiverChnageUidEvent(HnLiveEvent event) {
        if (event != null) {
            if (HnLiveConstants.EventBus.Reset_Data.equals(event.getType())) {
                joinPrivateLetterUid = "-1";
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Close_Private_Letter_Dialog, 0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

}
