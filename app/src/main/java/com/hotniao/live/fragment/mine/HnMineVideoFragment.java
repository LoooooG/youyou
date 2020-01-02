package com.hotniao.live.fragment.mine;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.EventBusBean;
import com.hn.library.global.HnConstants;
import com.hn.library.global.NetConstant;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.model.HnLoginModel;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.PermissionHelper;
import com.hotniao.live.HnApplication;
import com.hotniao.live.HnMainActivity;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnAuthStateActivity;
import com.hotniao.live.activity.HnAuthenticationActivity;
import com.hotniao.live.activity.HnVideoAuthApplyActivity;
import com.hotniao.live.activity.HnVideoDetailActivity;
import com.hotniao.live.adapter.HnMineVideoAdapter;
import com.hotniao.live.base.BaseScollFragment;
import com.hotniao.live.biz.home.HnHomeBiz;
import com.hotniao.live.dialog.HnDelBlackReportDialog;
import com.hotniao.live.fragment.HnMineFragment;
import com.hotniao.live.model.HnVideoModel;
import com.hotniao.live.model.HnVideoRoomSwitchModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.control.HnUserControl;
import com.videolibrary.activity.HnChooseVideoActivity;
import com.videolibrary.activity.TCVideoRecordActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：我的小视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnMineVideoFragment extends BaseScollFragment implements BaseRequestStateListener {
    public static final String TAG = "HnUserHomeBackFragment";

    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;

    private HnHomeBiz mHomeBiz;
    private int mPage = 1;
    private RelativeLayout mRlEmpty;

    private HnMineVideoAdapter mAdapter;
    private List<HnVideoModel.DBean.ItemsBean> mData = new ArrayList<>();


    public static HnMineVideoFragment getInstance() {
        HnMineVideoFragment fragment = new HnMineVideoFragment();
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.common_loading_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mHomeBiz = new HnHomeBiz(mActivity);
        mHomeBiz.setBaseRequestStateListener(this);
        initAdapter();
        setLisenter();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mAdapter = new HnMineVideoAdapter(mData);
        mRecycler.setAdapter(mAdapter);


        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_mine_video_show, null);
        mRlEmpty = view.findViewById(R.id.mRlEmpty);
        view.findViewById(R.id.mRlPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!HnPrefUtils.getBoolean(NetConstant.User.IS_ANCHOR,false)){
                    HnAuthStateActivity.luncher(mActivity);
                    return;
                }
                HnChooseVideoActivity.luncher(getActivity(), HnChooseVideoActivity.PublishVideo);
            }
        });


        mAdapter.addHeaderView(view);
    }

    private void checkAnchorStatus() {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.NoApply);
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.Authing);
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.AuthNoPass);
        } else if ("3".equals(mVideoStatue) || "6".equals(mVideoStatue)) {
            toRecord();
        }
    }

    private void toRecord(){
        if (PermissionHelper.isCameraUseable() && PermissionHelper.isAudioRecordable()) {
            if (PermissionHelper.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                TCVideoRecordActivity.luncher(mActivity);
            } else {
                HnToastUtils.showToastShort("请开启存储权限");
            }
        } else {
            HnToastUtils.showToastShort("请开启相机或录音权限");
        }
    }

    private void setLisenter() {
        mRefresh.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                getData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }
        });

        mRecycler.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.mRlClick) {
                    List<HnVideoRoomSwitchModel.DBean> datas = new ArrayList<>();
                    for (int i = 0; i < mData.size(); i++) {
                        HnVideoRoomSwitchModel.DBean bean = new HnVideoRoomSwitchModel.DBean();
                        bean.setId(mData.get(i).getId());
                        bean.setCover(mData.get(i).getCover());
                        datas.add(bean);
                    }
                    if (datas != null && datas.size() > 0) {
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < datas.size(); i++) {
                            if (mData.get(position).getId().equals(datas.get(i).getId()))
                                bundle.putInt("pos", i);
                        }
                        bundle.putSerializable("data", (Serializable) datas);
                        HnVideoDetailActivity.luncher(mActivity, bundle);
                    }
                } else if (view.getId() == R.id.mIvMore) {
                    HnDelBlackReportDialog.newInstance(HnDelBlackReportDialog.OnlyDelete, false).setClickListen(new HnDelBlackReportDialog.SelDialogListener() {
                        @Override
                        public void deleteClick() {
                            mHomeBiz.deleteVideo(mData.get(position).getId(), position);
                        }

                        @Override
                        public void blackClick() {

                        }

                        @Override
                        public void reportClick() {

                        }
                    }).show(mActivity.getFragmentManager(), "delete");
                }

            }
        });
    }

    @Override
    protected void initData() {
        mPage = 1;
        getData();
    }

    @Override
    public void pullToRefresh() {
        mPage = 1;
        getData();
    }

    private void getData() {
        if (HnApplication.getmUserBean() == null) {
            mHomeBiz.getOneHomeHotVideo(mPage, 2, null, null, null, null, HnPrefUtils.getString(NetConstant.User.UID, ""),
                    null);
        } else {
            mHomeBiz.getOneHomeHotVideo(mPage, 2, null, null, null, null, HnApplication.getmUserBean().getUser_id(), null);

        }
    }

    @Override
    public void refreshComplete() {
        if (this.getParentFragment() instanceof HnMineFragment) {
            ((HnMineFragment) (this.getParentFragment())).refreshComplete();
        }
    }

    @Override
    public View getScrollableView() {
        return mRecycler;
    }


    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) return;
        if (HnHomeBiz.HotVideo.equals(type)) {
            refreshComplete();
            refreshFinish();
            HnVideoModel model = (HnVideoModel) obj;
            if (model == null || model.getD() == null || model.getD().getItems() == null) {
                showEmptyView();
            } else {
                if (mPage == 1) mData.clear();
                mData.addAll(model.getD().getItems());
                mAdapter.notifyDataSetChanged();
                showEmptyView();
            }
            HnUiUtils.setRefreshModeNone(mRefresh, mPage, 10, mData.size());
        } else if (HnHomeBiz.DeleteVideo.equals(type)) {
            int pos = (int) obj;
            if (response.equals(mData.get(pos).getId())) {
                mData.remove(pos);
                mAdapter.notifyDataSetChanged();
                showEmptyView();
            }
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        if (HnHomeBiz.HotVideo.equals(type)) {
            refreshComplete();
            refreshFinish();
            HnToastUtils.showToastShort(msg);
            showEmptyView();
            HnUiUtils.setRefreshModeNone(mRefresh, mPage, 10, mData.size());
        } else if (HnHomeBiz.DeleteVideo.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    /**
     * 没有直播列表数据时调用
     */
    public void showEmptyView() {
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
        if (mActivity == null || mRlEmpty == null) return;
        if (mData == null || mData.size() < 1) {
            mRlEmpty.setVisibility(View.VISIBLE);
        } else {
            mRlEmpty.setVisibility(View.GONE);
        }

    }

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void publishRefresh(EventBusBean event) {
        if (HnConstants.EventBus.RefreshVideoMineList.equals(event.getType()))
            if (mActivity != null && mHomeBiz != null) {
                pullToRefresh();
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
