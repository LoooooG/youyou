package com.hotniao.video.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseFragment;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnBadgeView;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnLogUtils;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnOnlineListAct;
import com.hotniao.video.activity.HnSystemMessageActivity;
import com.hotniao.video.activity.HnVideoMessageActivity;
import com.hotniao.video.adapter.HnPrivLetterListAdapter;
import com.hotniao.video.biz.msg.HnMsgBiz;
import com.hotniao.video.eventbus.HnPriAndSysPushEvent;
import com.hotniao.video.model.HnSysMsgModel;
import com.hotniao.video.model.HnVideoNoReadModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.config.HnWebscoketConstants;
import com.hotniao.livelibrary.model.HnPrivateLetterListModel;
import com.hotniao.livelibrary.model.event.HnPrivateMsgEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;
import com.hotniao.livelibrary.model.webscoket.HnPrivateMsgModel;
import com.hotniao.livelibrary.util.DataTimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：私信模块
 * 创建人：阳石柏
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMsgFragment extends BaseFragment implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {

    @BindView(R.id.tv_ignore)
    TextView mTvIgnore;
    @BindView(R.id.tv_title)
    TextView mMsgTitleTv;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mPtr;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;


    /**
     * 消息列表逻辑类，处理消息列表相关业务
     */
    private HnMsgBiz mHnMsgBiz;
    /**
     * 头部布局  系统消息视图
     */
    private RelativeLayout rlSystemMsgBg;
    /**
     * 头部布局
     */
    private View mHeaderSystem;
    /**
     * 未读消息数
     */
    private HnBadgeView tvMsgNum;
    /**
     * 内容
     */
    private TextView tvContent;
    /**
     * 时间
     */
    private TextView tvTime;
    private RelativeLayout mRlOnline;
    /**
     * 小视频消息
     */
    private HnBadgeView mTvVideoNew, mTvOnline;//TODO
    private TextView mTvVideoTime;

    /**
     * 消息适配器   不包含系统消息（头部中装载系统消息）
     */
    private HnPrivLetterListAdapter mAdapter;
    /**
     * 页数
     */
    private int mPage = 1;

    private int unread_count = 0;

    public static HnMsgFragment newInstance() {
        HnMsgFragment fragment = new HnMsgFragment();
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        com.hn.library.utils.HnUiUtils.addStatusHeight2View(getActivity(),mRlTitle);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        mHnLoadingLayout.setOnReloadListener(this);
        //初始化业务逻辑类
        mHnMsgBiz = new HnMsgBiz(mActivity);
        mHnMsgBiz.setBaseRequestStateListener(this);
        //初始化适配器
        initAdapter();
    }

    /**
     * 获取系统消息列表数据
     */
    @Override
    protected void initData() {
        mPage = 1;
        mHnMsgBiz.requestToPriMsgList(mPage);

    }

    @Override
    public void onResume() {
        super.onResume();
        mHnMsgBiz.getSystemMsg();
        mHnMsgBiz.getVideoNewMessage();
        if (HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR, false)) {
            mRlOnline.setVisibility(View.VISIBLE);
        } else {
            mRlOnline.setVisibility(View.GONE);
        }
    }

    /**
     * 忽略未读
     */
    public void ignore() {
        mHnMsgBiz.requestToIgnoreUnRead();
    }

    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        initData();
    }


    /**
     * 请求中
     */
    @Override
    public void requesting() {
        mActivity.showDoing(getResources().getString(R.string.loading), null);
    }


    /**
     * 请求成功
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null || mHnLoadingLayout == null) return;
        mActivity.done();
        if ("private_msg_list".equals(type)) {//私信列表
            mActivity.setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            mActivity.closeRefresh(mPtr);
            HnPrivateLetterListModel model = (HnPrivateLetterListModel) obj;
            if (model != null && model.getD().getUser_dialogs() != null) {
                if (mPage >= model.getD().getUser_dialogs().getPagetotal()) {
                    mPtr.setMode(PtrFrameLayout.Mode.REFRESH);
                }
                updateUi(model.getD().getUser_dialogs().getItems());
            } else {
                if (mPage == 1) {
//                    mActivity.setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                }
            }
        } else if ("Ignore_Unread_Msg".equals(type)) {//忽略未读消息数据
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.readed));
            EventBus.getDefault().post(new HnPriAndSysPushEvent());
            tvMsgNum.setBadgeNumber(0);
            mTvVideoNew.setBadgeNumber(0);
            List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setUnread("0");
            }
            mAdapter.setNewData(items);
            EventBus.getDefault().post(new EventBusBean(2, HnConstants.EventBus.Update_Unread_Count, 1));
        } else if ("Delete_Msg".equals(type)) {//删除对话
            if (!TextUtils.isEmpty(response)) {
                int pos = Integer.valueOf(response);
                mAdapter.remove(pos);
            }
            HnToastUtils.showToastShort(R.string.delete_success);
            initData();
        } else if ("SystemMsg".equals(type)) {

            HnSysMsgModel model = (HnSysMsgModel) obj;
            if (model != null && model.getD().getSystem_dialog().size() > 0 && tvMsgNum != null) {
                //未读消息数
                unread_count = TextUtils.isEmpty(model.getD().getSystem_dialog().get(0).getUnread()) ? 0 : Integer.parseInt(model.getD().getSystem_dialog().get(0).getUnread());
                tvMsgNum.setBadgeNumber(unread_count);
                //时间
                String update_time = model.getD().getSystem_dialog().get(0).getTime() + "";
                if (!TextUtils.isEmpty(update_time))
                    tvTime.setText(DataTimeUtils.getTimestampString(Long.parseLong(update_time) * 1000));
                //内容
                String content = model.getD().getSystem_dialog().get(0).getContent();
                if (tvContent != null)
                    tvContent.setText(TextUtils.isEmpty(content) ? getString(R.string.now_not_system_msg) : content);
                //table_name
            }

        } else if (HnMsgBiz.NoReadMsg.equals(type)) {
            HnVideoNoReadModel model = (HnVideoNoReadModel) obj;
            if (model != null && model.getD() != null && mTvVideoNew != null && mTvVideoTime != null) {
                mTvVideoNew.setBadgeNumber(model.getD().getNum());
                if (0 == model.getD().getNum()) {
                    mTvVideoTime.setVisibility(View.INVISIBLE);
                } else {
                    mTvVideoTime.setText(HnDateUtils.stampToDateMm(model.getD().getTime()));
                    mTvVideoTime.setVisibility(View.VISIBLE);
                }
            }
        }


    }


    /**
     * 请求失败
     *
     * @param type
     * @param code
     * @param msg
     */
    @Override
    public void requestFail(String type, int code, String msg) {
        if (mHnLoadingLayout == null) return;
        mActivity.done();
        if ("private_msg_list".equals(type)) {//私信列表
            mActivity.closeRefresh(mPtr);
            if (mPage == 1) {
                if (REQUEST_NET_ERROR == code) {
                    mActivity.setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    mActivity.setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }
        } else if ("Ignore_Unread_Msg".equals(type)) {//忽略未读消息数据
            HnToastUtils.showToastShort(msg);
        } else if ("Delete_Msg".equals(type)) {//删除对话
            HnToastUtils.showToastShort(msg);
        } else if ("SystemMsg".equals(type) || HnMsgBiz.NoReadMsg.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }


    /**
     * 更新ui界面
     *
     * @param itemLists 消息列表数据   当uid为0时，更新系统消息，其他为私信消息  在私信列表中需要将系统消息数据清除
     */
    private void updateUi(List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> itemLists) {
        if (itemLists != null && itemLists.size() > 0) {
            //私信列表
            if (mPage == 1) {
                mAdapter.setNewData(itemLists);
            } else {
                mAdapter.addData(itemLists);
            }
        }
    }


    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter = new HnPrivLetterListAdapter(mActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        //添加头部视图
        mHeaderSystem = mActivity.getLayoutInflater().inflate(R.layout.header_item_private_list, (ViewGroup) mRecyclerView.getParent(), false);
        rlSystemMsgBg = (RelativeLayout) mHeaderSystem.findViewById(R.id.rl_system_msg_bg);
        tvMsgNum = (HnBadgeView) mHeaderSystem.findViewById(R.id.tv_new_msg);
        tvContent = (TextView) mHeaderSystem.findViewById(R.id.tv_content);
        tvTime = (TextView) mHeaderSystem.findViewById(R.id.tv_time);
        tvContent.setText(R.string.now_not_system_msg);
        rlSystemMsgBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入系统消息界面
                mActivity.openActivity(HnSystemMessageActivity.class);
            }
        });

        //小视频消息
        RelativeLayout mRlVideo = mHeaderSystem.findViewById(R.id.mRlVideo);
        mTvVideoNew = mHeaderSystem.findViewById(R.id.mTvVideoNew);
        mTvVideoTime = mHeaderSystem.findViewById(R.id.mTvVideoTime);
        mRlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入视频消息界面
                mActivity.openActivity(HnVideoMessageActivity.class);
            }
        });

        mAdapter.addHeaderView(mHeaderSystem);


        mRlOnline = mHeaderSystem.findViewById(R.id.mRlOnline);
        mRlOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HnOnlineListAct.class));
            }
        });

        if (HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR, false)) {
            mRlOnline.setVisibility(View.VISIBLE);
        } else {
            mRlOnline.setVisibility(View.GONE);
        }
    }

    /**
     * 事件处理
     */
    @Override
    protected void initEvent() {
        //长按删除消息
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean itemsBean = (HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean) adapter.getItem(position);
                mHnMsgBiz.deleteMsg(itemsBean, position);
                return true;
            }
        });
        //点击进入聊天室
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean bean = (HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean) adapter.getItem(position);
                mHnMsgBiz.joinToChatRoom(bean);
            }
        });
        //刷新监听
        mPtr.setMode(PtrFrameLayout.Mode.BOTH);
        mPtr.disableWhenHorizontalMove(true);
        mPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage++;
                mHnMsgBiz.requestToPriMsgList(mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnMsgBiz.requestToPriMsgList(mPage);
                mHnMsgBiz.getSystemMsg();
                mHnMsgBiz.getVideoNewMessage();

            }
        });
    }


    /**
     * 接收到webscoket推送过来的私信消息数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrivateEvent(HnPrivateMsgEvent event) {
        if (event != null) {
            if (HnWebscoketConstants.Send_Pri_Msg.equals(event.getType())) {
                HnLogUtils.i("HnWebSocketService", "22222222222222");
                HnPrivateMsgModel.DataBean result = event.getData().getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
                List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = mHnMsgBiz.receiverNewPrivateChatMsg(result, items);
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
    public void receiverSystemMsgEvent(final HnReceiverSysMsgEvent event) {
        if (event != null) {
            if ("system_msg".equals(event.getType())) {
                //未读消息数
                unread_count = unread_count + 1;
                tvMsgNum.setBadgeNumber(unread_count);
                //时间
                String update_time = event.getData().getData().getAdd_time();
//                String time = mHnMsgBiz.getAddTime(update_time);
//                DateUtils.setTimeShow(time, tvTime, mActivity);
                if (!TextUtils.isEmpty(update_time))
                    tvTime.setText(DataTimeUtils.getTimestampString(Long.parseLong(update_time) * 1000));
                //内容
                String content = event.getData().getData().getContent();
                tvContent.setText(content);
                //table_name
            }
        }

    }

    /**
     * 当用户进入私信/系统详情后返回 需要清除对应未读消息数
     *
     * @param event
     */
    @Subscribe
    public void cleanUnread(EventBusBean event) {
        try {
            if (event != null) {
                if (HnConstants.EventBus.Clean_Unread.equals(event.getType())) {//清除消息列表的未读数
                    String uid = (String) event.getObj();
                    if (!TextUtils.isEmpty(uid)) {
                        if ("0".equals(uid)) {//系统消息
                            tvMsgNum.setBadgeNumber(0);
                            if (unread_count > 0) {
                                unread_count = 0;
                            }
                        } else {//私聊消息
                            List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> items = mAdapter.getData();
                            List<HnPrivateLetterListModel.DBean.UserDialogsBean.ItemsBean> list = mHnMsgBiz.cleanUnreaByUid(uid, items);
                            mAdapter.setNewData(list);
                        }

                    }
                } else if (HnConstants.EventBus.Update_Msg.equals(event.getType())) {
                    if (mHnMsgBiz != null && mActivity != null) {
                        mPage = 1;
                        mHnMsgBiz.requestToPriMsgList(mPage);
                    }
                } else if (HnConstants.EventBus.RefreshVideoAuthStatue.equals(event.getType())) {
                    if (HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR, false)) {
                        mRlOnline.setVisibility(View.VISIBLE);
                    } else {
                        mRlOnline.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 刷新UI界面
     */
    public void refreshUI() {
//        //背景色
//        TypedValue background = new TypedValue();
//        //字体颜色#333333
//        TypedValue textColor333 = new TypedValue();
//        //条目背景颜色
//        TypedValue item_color = new TypedValue();
//
//        Resources.Theme theme = mActivity.getTheme();
//        Resources resources = getResources();
//
//        theme.resolveAttribute(R.attr.pageBg_color, background, true);
//        theme.resolveAttribute(R.attr.item_bg_color, item_color, true);
//        theme.resolveAttribute(R.attr.text_color_333, textColor333, true);
//
//        //根布局背景色
//        mHnLoadingLayout.setBackgroundResource(background.resourceId);
//        //标题背景色
//        mRlTitle.setBackgroundResource(item_color.resourceId);
//        //标题字体颜色
//        mMsgTitleTv.setTextColor(resources.getColor(textColor333.resourceId));

        //重新创建
        initAdapter();
        initData();
        initEvent();
    }


    @OnClick(R.id.tv_ignore)
    public void onClick() {
        ignore();
    }
}
