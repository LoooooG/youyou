package com.hotniao.video.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnSystemMsgAdapter;
import com.hotniao.video.biz.msg.HnSystemMsgBiz;
import com.hn.library.global.HnConstants;
import com.hotniao.video.model.GetSystemMsgModel;
import com.hotniao.video.model.bean.GetSystemMsg;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.model.event.HnReceiverSysMsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：系统消息
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
@Route(path = "/app/HnSystemMessageActivity") // 必须标明注解
public class HnSystemMessageActivity extends BaseActivity implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {


    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout ptrRefresh;
    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    private String TAG = "HnSystemMessageActivity";
    /**
     * 系统消息列表适配器
     */
    private HnSystemMsgAdapter mAdapter;
    /**
     * 系统消息数据源
     */
    private ArrayList<GetSystemMsg.SystemDialogBean> list = new ArrayList<>();
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 系统消息业务逻类
     */
    private HnSystemMsgBiz mHnSystemMsgBiz;

    /**
     * 未读的消息数
     */
    private String unread_msg = "0";

    private String mDialogId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_system_message;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(R.string.system_msg);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        mHnLoadingLayout.setOnReloadListener(this);
        mHnSystemMsgBiz = new HnSystemMsgBiz(this);
        mHnSystemMsgBiz.setBaseRequestStateListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unread_msg = bundle.getString("unread_msg");
        }
        //初始化适配器
        initAdapter();
        //设置刷新监听
        setRefreshListener();
    }

    @Override
    public void getInitData() {
        mPage = 1;
        mHnSystemMsgBiz.requestSystemMessage(mPage, mDialogId);
    }

    @Override
    public void requesting() {

    }

    /**
     * 请求成功
     *
     * @param type
     * @param response
     * @param obj
     */
    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mHnLoadingLayout == null||isFinishing()) return;
        if ("System_Msg_Detail_List".equals(type)) {//系统消息详情
            setLoadViewState(HnLoadingLayout.Success, mHnLoadingLayout);
            closeRefresh(ptrRefresh);
            GetSystemMsgModel model = (GetSystemMsgModel) obj;
            if (model != null && model.getD() != null && model.getD().getSystem_dialog() != null) {
                List<GetSystemMsg.SystemDialogBean> items = model.getD().getSystem_dialog();
                if (items.size() > 0)
                    mDialogId = items.get(items.size() - 1).getDialog_id();
                else mDialogId="";
                if (mPage == 1) {
                    list.clear();
                }
                list.addAll(items);
                mAdapter.notifyDataSetChanged();
                if (mPage == 1) {
                    mListview.setSelection(0);
                }
            } else {
                if (mPage == 1) {
                    setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
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
        if (mHnLoadingLayout == null||isFinishing()) return;
        if ("System_Msg_Detail_List".equals(type)) {//系统消息详情
            closeRefresh(ptrRefresh);
            HnToastUtils.showToastShort(msg);
            if (mPage == 1) {
                if (REQUEST_NET_ERROR == code) {
                    setLoadViewState(HnLoadingLayout.No_Network, mHnLoadingLayout);
                } else {
                    setLoadViewState(HnLoadingLayout.Empty, mHnLoadingLayout);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }
        }
    }

    @Override
    public void onReload(View v) {
        getInitData();
    }


    /**
     * 初始化适配器
     */
    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HnSystemMsgAdapter(this, list);
            mListview.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置刷新监听
     */
    private void setRefreshListener() {
        ptrRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage = mPage + 1;
                mHnSystemMsgBiz.requestSystemMessage(mPage, mDialogId);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mDialogId = "";
                mHnSystemMsgBiz.requestSystemMessage(mPage, mDialogId);
            }
        });
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
                getInitData();
            }
        }

    }

    @Override
    protected void onDestroy() {
        //该通知用于直播间
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Clear_Unread, unread_msg));
        EventBus.getDefault().post(new HnLiveEvent(0, HnLiveConstants.EventBus.Reset_Data, 0));
        //该通知用于消息fragment
        EventBus.getDefault().post(new EventBusBean(0, HnConstants.EventBus.Clean_Unread, "0"));
//        mHnSystemMsgBiz.requestToExitSysMsgDetail();
        super.onDestroy();
    }
}
