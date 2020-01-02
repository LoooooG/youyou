package com.hotniao.live.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hotniao.live.R;
import com.hotniao.live.adapter.HnMyFansAdapter;
import com.hotniao.live.biz.user.fans.HnFansBiz;
import com.hn.library.view.CommDialog;
import com.hotniao.live.model.HnMyFansBean;
import com.hotniao.live.model.HnMyFansModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.model.event.HnFollowEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

import static com.hn.library.global.NetConstant.REQUEST_NET_ERROR;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：悠悠直播
 * 类描述：我的粉丝
 * 创建人：mj
 * 创建时间：2017/3/6 16:16
 * 修改人：阳石柏
 * 修改时间：2017/3/6 16:16
 * 修改备注：
 * Version:  1.0.0
 */
public class HnMyFansActivity extends BaseActivity implements BaseRequestStateListener, HnLoadingLayout.OnReloadListener {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefreshView;
    @BindView(R.id.loading)
    HnLoadingLayout loading;

    //页数
    private int mPage = 1;
    private HnMyFansAdapter mAdapter;
    //我的关注逻辑类，处理我的粉丝相关业务
    private HnFansBiz mHnFansBiz;
    //用户详情卡片
//    private HnUserDetailDialog mHnUserDetailDialog;


    @Override
    public int getContentViewId() {
        return R.layout.common_layout;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setShowBack(true);
        setTitle(getString(R.string.mine_my_fans));
        EventBus.getDefault().register(this);
        loading.setStatus(HnLoadingLayout.Loading);
        loading.setEmptyImage(R.drawable.empty_com).setEmptyText(getString(R.string.now_no_fans));
        loading.setOnReloadListener(this);
        mHnFansBiz = new HnFansBiz(this);
        mHnFansBiz.setRegisterListener(this);
        initRefreshView();
    }

    @Override
    public void getInitData() {
        mPage = 1;
        mHnFansBiz.requestFans(mPage);
    }

    /**
     * recyclerview相关操作
     */
    private void initRefreshView() {
        mAdapter = new HnMyFansAdapter();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mRefreshView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage++;
                mHnFansBiz.requestFans(mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnFansBiz.requestFans(mPage);
            }
        });


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                HnMyFansBean.FansBean.ItemsBean item = (HnMyFansBean.FansBean.ItemsBean) adapter.getItem(position);
//                String mUid = HnPrefUtils.getString(NetConstant.User.UID, "");
//                mHnUserDetailDialog = HnUserDetailDialog.newInstance(1, item.getUser_id(), mUid, 0);
//                mHnUserDetailDialog.setActvity(HnMyFansActivity.this);
//                mHnUserDetailDialog.show(getSupportFragmentManager(), "HnUserDetailDialog");
                HnUserHomeActivity.luncher(HnMyFansActivity.this, item.getUser_id());

            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                HnMyFansBean.FansBean.ItemsBean item = (HnMyFansBean.FansBean.ItemsBean) adapter.getItem(position);
                if (item == null)
                    return;
                final String uid = item.getUser_id();
                if (view.getId() == R.id.mTvFocus) {
                    if ("Y".equals(item.getIs_follow())) {
                        mHnFansBiz.cancelFollow(uid, position);
                    } else if ("N".equals(item.getIs_follow())) {
                        mHnFansBiz.addFollow(uid, position);
                    }
                }
            }
        });
    }


    /**
     * 请求中
     */
    @Override
    public void requesting() {
        showDoing(HnUiUtils.getString(R.string.loading), null);

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
        if (loading == null||isFinishing()) return;
        done();
        setLoadViewState(HnLoadingLayout.Success, loading);
        if ("fans_list".equals(type)) {//关注列表
            closeRefresh(mRefreshView);
            HnMyFansModel model = (HnMyFansModel) obj;
            if (model != null && model.getD() != null && model.getD().getFans() != null && model.getD().getFans().getItems().size() > 0) {
                List<HnMyFansBean.FansBean.ItemsBean> items = model.getD().getFans().getItems();
                if (mPage == 1) {
                    mAdapter.setNewData(items);
                } else {
                    mAdapter.addData(items);
                }
            } else {
                if (mPage == 1) {
                    setLoadViewState(HnLoadingLayout.Empty, loading);
                }
            }
            HnUiUtils.setRefreshMode(mRefreshView, mPage, 20, mAdapter.getItemCount());
        } else if ("cancelFollow".equals(type)) {//取消关注
            int pos = (int) obj;
            mAdapter.getItem(pos).setIs_follow("N");
            mAdapter.notifyItemChanged(pos);
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_cancle_follow_success));
        } else if ("addFollow".equals(type)) {//添加关注
            int pos = (int) obj;
            mAdapter.getItem(pos).setIs_follow("Y");
            mAdapter.notifyItemChanged(pos);
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_follow_success));
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
        if (loading == null||isFinishing()) return;
        done();
        if ("fans_list".equals(type)) {//关注列表
            HnToastUtils.showToastShort(msg);
            if (mPage == 1) {
                if (code == REQUEST_NET_ERROR) {
                    setLoadViewState(HnLoadingLayout.No_Network, loading);
                } else {
                    setLoadViewState(HnLoadingLayout.Empty, loading);
                }
            }
        } else {

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
    public void onEventBusCallBack(HnFollowEvent event) {
        if (event != null) {
            String uid = event.getUid();
            boolean isFollow = event.isFollow();
            if (!TextUtils.isEmpty(uid) && mAdapter != null) {
                List<HnMyFansBean.FansBean.ItemsBean> items = mAdapter.getData();
                for (int i = 0; i < items.size(); i++) {
                    if (uid.equals(items.get(i).getUser_id())) {
                        if (isFollow) {
                            items.get(i).setIs_follow("Y");
                        } else {
                            items.get(i).setIs_follow("N");
                        }
                        mAdapter.setNewData(items);
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
