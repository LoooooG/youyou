package com.hotniao.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnLiveNoticeAdapter;
import com.hotniao.video.biz.live.startLiveRemind.HnStartLiveRemindBiz;
import com.hn.library.global.HnConstants;
import com.hotniao.video.model.HnLiveNoticeModel;
import com.hotniao.video.model.bean.HnLiveNoticeBean;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.model.event.HnFollowEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：开播提醒
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：
 * 修改时间：
 * 修改备注：
 * Version:  1.0.0
 */
public class HnLiveNoticeActivity extends BaseActivity implements HnLoadingLayout.OnReloadListener, BaseRequestStateListener {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefreshView;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    @BindView(R.id.mTvSeting)
    TextView mTvSeting;

//    private CheckBox mTbNotice;

    //设配器
    private HnLiveNoticeAdapter mAdapter;
    /**
     * 当打开开播提醒时的数据源
     */
    private List<HnLiveNoticeBean.FollowsBean.ItemsBean> mData = new ArrayList<>();
    //列表数据源
    //我的关注逻辑类，处理开播提醒相关业务
    private HnStartLiveRemindBiz mHnStartLiveRemindBiz;
    //页数
    private int mPage = 1;
    //是否打开全局开播提醒
    private boolean isOpen = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_live_notice;

    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        mLoading.setStatus(HnLoadingLayout.Loading);
        mLoading.setEmptyText(getString(R.string.go_to_follow_anchor)).setEmptyImage( R.drawable.empty_com);
        setTitle(R.string.str_live_notice);
        EventBus.getDefault().register(this);
        setShowBack(true);
        initRefreshView();
        mHnStartLiveRemindBiz = new HnStartLiveRemindBiz(this);
        mHnStartLiveRemindBiz.setBaseRequestStateListener(this);

        mTvSeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    mHnStartLiveRemindBiz.requestToSetLiveNoticeWay("0", "N", -1);
                } else {
                    mHnStartLiveRemindBiz.requestToSetLiveNoticeWay("0", "Y", -1);
                }
            }
        });
    }

    @Override
    public void getInitData() {
        mPage = 1;
        mHnStartLiveRemindBiz.requestToLiveNoticeList(mPage);
    }

    /**
     * 初始化recyclerview视图配置
     */
    private void initRefreshView() {
        mAdapter = new HnLiveNoticeAdapter(mData);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setHasFixedSize(true);

        mRecyclerview.setAdapter(mAdapter);
        mRefreshView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage++;
                mHnStartLiveRemindBiz.requestToLiveNoticeList(mPage);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnStartLiveRemindBiz.requestToLiveNoticeList(mPage);

            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HnLiveNoticeBean.FollowsBean.ItemsBean item = (HnLiveNoticeBean.FollowsBean.ItemsBean) adapter.getItem(position);
                if ("Y".equalsIgnoreCase(item.getIs_remind())) {
                    EventBus.getDefault().post(new EventBusBean(position, HnConstants.EventBus.Set_Live_Notify, "N"));
                } else {
                    EventBus.getDefault().post(new EventBusBean(position, HnConstants.EventBus.Set_Live_Notify, "Y"));
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                HnUserDetailDialog mHnUserDetailDialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(), HnApplication.getmUserBean().getUser_id(), 0);
//                mHnUserDetailDialog.setActvity(HnLiveNoticeActivity.this);
//                mHnUserDetailDialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                HnUserHomeActivity.luncher(HnLiveNoticeActivity.this,mData.get(position).getUser_id());
            }
        });
    }

    /**
     * 请求中
     */
    @Override
    public void requesting() {
        showDoing(getResources().getString(R.string.loading), null);
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
        if (mLoading == null||isFinishing()) return;
        done();
        setLoadViewState(HnLoadingLayout.Success, mLoading);
        if ("live_notice_list".equals(type)) {//开播提醒用户列表
            closeRefresh(mRefreshView);
            HnLiveNoticeModel model = (HnLiveNoticeModel) obj;
            if (model != null && model.getD() != null) {
                HnLiveNoticeBean bean = model.getD();
                //用户列表
                HnLiveNoticeBean.FollowsBean userList = bean.getFollows();
                if (userList != null && userList.getItems() != null) {
                    List<HnLiveNoticeBean.FollowsBean.ItemsBean> items = userList.getItems();
                    if (mPage == 1) {
                        mData.clear();
                    }
                    mData.addAll(items);
                }
                //全局提醒设置
                String notify = bean.getIs_remind();
                if (("Y").equals(notify)) {
                    mTvSeting.setSelected(true);
                    isOpen = true;
                } else {
                    mTvSeting.setSelected(false);
                    isOpen = false;
                }
                setAllNotice(isOpen);

                if (mAdapter != null) mAdapter.notifyDataSetChanged();

            } else {
                if (mPage == 1) {
                    setLoadViewState(HnLoadingLayout.Empty, mLoading);
                }
            }

            HnUiUtils.setRefreshMode(mRefreshView, mPage, 20, mData.size());
        } else if ("set_live_notice".equals(type)) {//设置开播提醒
            int pos = (int) obj;
            String result = response;

            if (pos == -1) {//设置全局
                if ("N".equals(result)) {
                    isOpen = false;
                    mTvSeting.setSelected(false);
                } else {
                    isOpen = true;
                    mTvSeting.setSelected(true);
                }
                setAllNotice(isOpen);
            } else if (pos >= 0) {//设置对莫个人的提醒
                if ("N".equals(result)) {
                    mData.get(pos).setIs_remind("N");
                } else {
                    mData.get(pos).setIs_remind("Y");
                }
                mAdapter.notifyDataSetChanged();
            }
            HnToastUtils.showToastShort(getString(R.string.set_success));
        }
    }

    /**
     * 设置全局提醒
     *
     * @param isRemend
     */
    private void setAllNotice(boolean isRemend) {
        if (mRefreshView != null)
            mRefreshView.setVisibility(isRemend ? View.VISIBLE : View.GONE);
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
        if (mLoading == null||isFinishing()) return;
        done();
        if ("live_notice_list".equals(type)) {//开播提醒用户列表
            if (mPage == 1) {
                if (code == REQUEST_NET_ERROR) {
                    setLoadViewState(HnLoadingLayout.No_Network, mLoading);
                } else {
                    setLoadViewState(HnLoadingLayout.Empty, mLoading);
                }
            } else {
                HnToastUtils.showToastShort(msg);
            }
        } else if ("set_live_notice".equals(type)) {//设置开播提醒
            HnToastUtils.showToastShort(msg);
        }
    }

    /**
     * 重新加载
     *
     * @param v
     */
    @Override
    public void onReload(View v) {
        getInitData();
    }


    @Subscribe
    public void onEventBusCallBack(EventBusBean event) {
        if (event != null) {
            if (HnConstants.EventBus.Set_Live_Notify.equals(event.getType())) {
                int pos = event.getPos();
                String type = (String) event.getObj();
                if (pos < mAdapter.getData().size()) {
                    String uid = mAdapter.getItem(pos).getUser_id();
                    mHnStartLiveRemindBiz.requestToSetLiveNoticeWay(uid, type, pos);
                }
            }
        }
    }


    @Subscribe
    public void onEventBusCallBack(HnFollowEvent event) {
        if (event != null) {
            String uid = event.getUid();
            boolean isFollow = event.isFollow();
            if (!TextUtils.isEmpty(uid) && mAdapter != null) {
                for (int i = 0; i < mData.size(); i++) {
                    if (uid.equals(mData.get(i).getUser_id())) {
                        if (!isFollow) {
                            mData.remove(i);
                            if (mAdapter != null)
                                mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
