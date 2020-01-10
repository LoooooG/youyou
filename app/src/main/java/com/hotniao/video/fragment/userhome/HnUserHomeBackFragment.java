package com.hotniao.video.fragment.userhome;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnAnchorFunctionActivity;
import com.hotniao.video.activity.HnMyRechargeActivity;
import com.hotniao.video.activity.HnMyVipMemberActivity;
import com.hotniao.video.activity.HnPlayBackPayActivity;
import com.hotniao.video.activity.HnPlayBackVideoActivity;
import com.hotniao.video.activity.HnUserHomeActivity;
import com.hotniao.video.activity.HnVideoAuthApplyActivity;
import com.hotniao.video.adapter.HnPlaybackAdapter;
import com.hotniao.video.adapter.HnUserHomeBackAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.dialog.HnDelBlackReportDialog;
import com.hotniao.video.fragment.HnMineFragment;
import com.hotniao.video.fragment.HnUserHomeFragment;
import com.hotniao.video.model.HnLiveBackPayFreeModel;
import com.hotniao.video.model.HnLivePlayBackModel;
import com.hotniao.video.model.HnPlayBackModel;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.model.bean.HnUserInfoDetailBean;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：用户主页回放
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnUserHomeBackFragment extends BaseScollFragment {
    public static final String TAG = "HnUserHomeBackFragment";

    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;
    @BindView(R.id.loading)
    HnLoadingLayout mLoading;
    @BindView(R.id.tv_apply)
    TextView tvApply;

//    private HnUserHomeBackAdapter mAdapter;
    private HnPlaybackAdapter mAdapter;
    private List<HnPlayBackModel.DBean.VideosBean.ItemsBean> mData = new ArrayList<>();
    private String mUserId;

    private View mEmptyLayout;

    private int mPage = 1;
    //Y：是，N：否
//    private String isVip = "N";

    public static HnUserHomeBackFragment getInstance(String uid) {
        HnUserHomeBackFragment fragment = new HnUserHomeBackFragment();
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

        mUserId = getArguments().getString("uid");
        initAdapter();
        setLisenter();
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(mActivity,2);
        mRecycler.setLayoutManager(manager);
        mRecycler.addItemDecoration(new HnSpacesItemDecoration(6,false));

        /**空数据*/
        mEmptyLayout = LayoutInflater.from(mActivity).inflate(R.layout.empty_data_layout, (ViewGroup) mRecycler.getParent(), false);
        HnLoadingLayout mLoadLayout = mEmptyLayout.findViewById(R.id.mLoading);
        String emptyTip;
        if(!HnApplication.getmUserBean().getUser_id().equals(mUserId)){
            emptyTip = getString(R.string.now_no_back_user);
        }else{
            emptyTip = getString(R.string.now_no_back_video);
        }
        mLoadLayout.setEmptyImage(R.drawable.empty_com).setEmptyText(emptyTip);
        mLoadLayout.setStatus(HnLoadingLayout.Empty);

        mAdapter = new HnPlaybackAdapter(mData);
        mRecycler.setAdapter(mAdapter);
    }

    private void setLisenter() {
        mRefresh.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                getData(HnRefreshDirection.BOTH, mPage);

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, final int position) {
                String userId = mData.get(position).getUser_id();
                String logId = mData.get(position).getAnchor_live_log_id();
                String imgUrl = mData.get(position).getImage_url();
                int type = mData.get(position).getType();
                if(HnApplication.getmUserBean().getUser_id().equals(userId)){
                    //自己发布是回放直接看
                    HnPlayBackVideoActivity.luncher(mActivity, userId,logId, 1,imgUrl);
                }else{
                    if(type == 0){
                        //免费
                        HnPlayBackVideoActivity.luncher(mActivity, userId,logId, 1,imgUrl);
                    }else if(type == 1){
                        //vip
                        if("Y".equals(HnApplication.getmUserBean().getUser_is_member())){
                            //是会员直接观看
                            HnPlayBackVideoActivity.luncher(mActivity, userId,logId, 1,imgUrl);
                        }else{
                            //不是会员去开通会员
                            HnPlayBackPayActivity.launcher(mActivity,HnPlayBackPayActivity.VIP,mData.get(position));
                        }
                    }else{
                        //付费
                        getPayFree(mData.get(position));
                    }
                }
            }
        });
    }

    //付费视频是否已付过费
    private void getPayFree(final HnPlayBackModel.DBean.VideosBean.ItemsBean bean) {
        RequestParams params = new RequestParams();
        params.put("anchor_live_log_id", bean.getAnchor_live_log_id());
        HnHttpUtils.postRequest(HnUrl.VIDEO_APP_PLAY_BACK_FREE, params, "VIDEO_APP_PLAY_BACK_FREE", new HnResponseHandler<HnLiveBackPayFreeModel>(HnLiveBackPayFreeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                if (model.getD() == null) return;
                //付过费返回 1
                if (0 == model.getD().getIs_free()) {
                    HnPlayBackPayActivity.launcher(mActivity,HnPlayBackPayActivity.PAY,bean);
                } else {
                    HnPlayBackVideoActivity.luncher(mActivity, bean.getUser_id(), model.getD().getUrl(), 2,bean.getImage_url());
                }
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (HnServiceErrorUtil.VIP_IS_NOT == errCode) {
                    CommDialog.newInstance(mActivity).setClickListen(new CommDialog.TwoSelDialog() {
                        @Override
                        public void leftClick() {

                        }

                        @Override
                        public void rightClick() {
                            mActivity.openActivity(HnMyVipMemberActivity.class);
                        }
                    }).setRightText(getString(R.string.now_open_vip)).setTitle(getString(R.string.look_playback)).setContent(getString(R.string.open_vip_can_look_video_now_open)).show();
                } else
                    HnToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUserId.equals(HnApplication.getmUserBean().getUser_id()) && "N".equals(HnApplication.getmUserBean().getUser_is_anchor())){
            tvApply.setVisibility(View.VISIBLE);
            mRefresh.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        }else{
            tvApply.setVisibility(View.GONE);
            mRefresh.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        mPage = 1;
        getData(HnRefreshDirection.TOP, mPage);
    }

    @Override
    public void pullToRefresh() {
        mPage = 1;
        getData(HnRefreshDirection.TOP, mPage);
    }

    @Override
    public void refreshComplete() {
        if (this.getActivity() instanceof HnUserHomeActivity) {
            ((HnUserHomeActivity) (this.getActivity())).refreshComplete();
        }else if(getParentFragment() instanceof HnUserHomeFragment){
            ((HnUserHomeFragment)getParentFragment()).refreshComplete();
        }else if(getParentFragment() instanceof HnMineFragment){
            ((HnMineFragment)getParentFragment()).refreshComplete();
        }
    }

    @Override
    public View getScrollableView() {
        return mRecycler;
    }


    protected void getData(final HnRefreshDirection state, final int page) {
        RequestParams param = new RequestParams();
        param.put("page", page + "");
        param.put("pagesize", 20 + "");
        param.put("user_id", mUserId + "");
        HnHttpUtils.postRequest(HnUrl.LIVE_PLAYBACK_ANCHOR, param, HnUrl.LIVE_PLAYBACK_ANCHOR, new HnResponseHandler<HnPlayBackModel>(HnPlayBackModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                refreshComplete();
                if (model.getD() == null || model.getD().getVideos() == null) {
                    showEmptyView();
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getVideos().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                showEmptyView();
                HnUiUtils.setRefreshModeNone(mRefresh, page, 20, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                refreshComplete();
                HnToastUtils.showToastShort(msg);
                showEmptyView();
            }
        });

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

    @OnClick(R.id.tv_apply)
    public void applyClick(){
        checkAnchorStatus();
    }

    private void checkAnchorStatus() {
        String mVideoStatue = HnApplication.getmUserBean().getVideo_authentication();
        if ("0".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.NoApply);
        } else if ("1".equals(mVideoStatue) || "4".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.Authing);
        } else if ("2".equals(mVideoStatue) || "5".equals(mVideoStatue)) {
            HnVideoAuthApplyActivity.lunchor(mActivity, HnVideoAuthApplyActivity.AuthNoPass);
        }
    }

    protected void refreshFinish() {
        if (mRefresh != null) mRefresh.refreshComplete();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
