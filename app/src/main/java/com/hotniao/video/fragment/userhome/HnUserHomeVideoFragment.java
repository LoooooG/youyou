package com.hotniao.video.fragment.userhome;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnUserHomeActivity;
import com.hotniao.video.activity.HnVideoDetailActivity;
import com.hotniao.video.adapter.HnHomeVideoAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.fragment.HnMineFragment;
import com.hotniao.video.fragment.HnUserHomeFragment;
import com.hotniao.video.model.HnVideoModel;
import com.hotniao.video.model.HnVideoRoomSwitchModel;
import com.hotniao.video.utils.HnUiUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：用户主页小视频
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserHomeVideoFragment extends BaseScollFragment implements BaseRequestStateListener {
    public static final String TAG = "HnUserHomeVideoFragment";

    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;

    private View mEmptyLayout;

    private int mPage = 1;

    private String mUid;
    private HnHomeVideoAdapter mAdapter;
    private List<HnVideoModel.DBean.ItemsBean> mData = new ArrayList<>();

    private HnHomeBiz mHomeBiz;

    public static HnUserHomeVideoFragment getInstance(String uid) {
        HnUserHomeVideoFragment fragment = new HnUserHomeVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.common_loading_layout;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeBiz = new HnHomeBiz(mActivity);
        mHomeBiz.setBaseRequestStateListener(this);
        mUid = getArguments().getString("uid");
        initAdapter();
        setLisenter();
    }

    private void initAdapter() {

        mRecycler.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mRecycler.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new HnHomeVideoAdapter(mData, false);

        /**空数据*/
        mEmptyLayout = LayoutInflater.from(mActivity).inflate(R.layout.empty_data_layout, (ViewGroup) mRecycler.getParent(), false);
        HnLoadingLayout mLoadLayout = mEmptyLayout.findViewById(R.id.mLoading);
        mLoadLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(getString(R.string.now_no_video_user));
        mLoadLayout.setStatus(HnLoadingLayout.Empty);

        mRecycler.setAdapter(mAdapter);
    }

    private void setLisenter() {
        mRefresh.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                mHomeBiz.getOneHomeHotVideo(mPage, 0, null, null, null, null, mUid, null);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<HnVideoRoomSwitchModel.DBean> datas = new ArrayList<>();
                HnVideoRoomSwitchModel.DBean bean = new HnVideoRoomSwitchModel.DBean();
                bean.setCover(mData.get(position).getCover());
                bean.setId(mData.get(position).getId());
                datas.add(bean);

                Bundle bundle = new Bundle();
                bundle.putInt("pos", 0);
                bundle.putSerializable("data", (Serializable) datas);

                HnVideoDetailActivity.luncher(mActivity, bundle);
                if (mActivity == null) return;
                try {
                    int num = Integer.parseInt(mData.get(position).getWatch_num());
                    mData.get(position).setWatch_num((num + 1) + "");
                    if (mAdapter != null) mAdapter.notifyItemChanged(position);
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    protected void initData() {
        mPage = 1;
        mHomeBiz.getOneHomeHotVideo(mPage, 0, null, null, null, null, mUid, null);
    }

    @Override
    public void pullToRefresh() {
        mPage = 1;
        mHomeBiz.getOneHomeHotVideo(mPage, 0, null, null, null, null, mUid, null);
    }

    @Override
    public void refreshComplete() {
        if (this.getActivity() instanceof HnUserHomeActivity) {
            ((HnUserHomeActivity) (this.getActivity())).refreshComplete();
        }else if(getParentFragment() instanceof HnUserHomeFragment){
            ((HnUserHomeFragment)getParentFragment()).refreshComplete();
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
        }
    }

    /**
     * 没有直播列表数据时调用
     */
    public void showEmptyView() {
        mActivity.setLoadViewState(HnLoadingLayout.Success, mLoading);
        if (mData == null || mData.size() < 1) {
            if (mAdapter != null && mEmptyLayout != null) {
                mAdapter.setNewData(null);
                if ((ViewGroup) (mEmptyLayout.getParent()) != null)
                    ((ViewGroup) (mEmptyLayout.getParent())).removeView(mEmptyLayout);
                mAdapter.setEmptyView(mEmptyLayout);
            }
        }

    }

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }
}
