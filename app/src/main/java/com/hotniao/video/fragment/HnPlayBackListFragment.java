package com.hotniao.video.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.view.CommDialog;
import com.hn.library.view.HnSpacesItemDecoration;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnMyRechargeActivity;
import com.hotniao.video.activity.HnMyVipMemberActivity;
import com.hotniao.video.activity.HnPlayBackPayActivity;
import com.hotniao.video.activity.HnPlayBackVideoActivity;
import com.hotniao.video.adapter.HnHomeChatAdapter;
import com.hotniao.video.adapter.HnHomeChatRecomAdapter;
import com.hotniao.video.adapter.HnPlaybackAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.model.HnBannerModel;
import com.hotniao.video.model.HnHomeFastChatModel;
import com.hotniao.video.model.HnHomeHotAnchorChatModel;
import com.hotniao.video.model.HnLiveBackPayFreeModel;
import com.hotniao.video.model.HnPlayBackModel;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：约聊
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

@SuppressLint("ValidFragment")
public class HnPlayBackListFragment extends BaseScollFragment implements BaseRequestStateListener {
    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;

    public static final String TAG = "HnPlayBackListFragment";

    private ConvenientBanner mBanner;
    private RelativeLayout mRlEmpty;
    private String categoryId;

    private List<HnHomeHotAnchorChatModel.DBean.AnchorBean> mHeadData = new ArrayList<>();
    private List<HnPlayBackModel.DBean.VideosBean.ItemsBean> mData = new ArrayList<>();
    private HnPlaybackAdapter mAdapter;

    private HnHomeBiz mHnHomeBiz;

    private int mPage = 1;

    public static HnPlayBackListFragment getInstance(String categoryId){
        HnPlayBackListFragment fragment = new HnPlayBackListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryId",categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 广告数据源
     */
    private List<String> mImgUrl = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.fragment_playback_list;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoryId = getArguments().getString("categoryId");
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
        initAdapter();
        initListener();
    }

    private void initAdapter() {
        mRecycler.addItemDecoration(new HnSpacesItemDecoration(6, true));
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mAdapter = new HnPlaybackAdapter(mData);
        mRecycler.setAdapter(mAdapter);
        View mHeaderView = mActivity.getLayoutInflater().inflate(R.layout.head_home_chat_layout, null);
        mBanner = (ConvenientBanner) mHeaderView.findViewById(R.id.mBanner);
        mRlEmpty = (RelativeLayout) mHeaderView.findViewById(R.id.mRlEmpty);
        View view = mHeaderView.findViewById(R.id.space_view);
        mAdapter.addHeaderView(mHeaderView);
        HnUiUtils.setBannerHeight(mActivity, mBanner);
        if("0".equals(categoryId)){
            mHeaderView.setVisibility(View.VISIBLE);
        }else{
            mHeaderView.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setMode(PtrFrameLayout.Mode.BOTH);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                mHnHomeBiz.getPlayBackList(categoryId,mPage);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                mHnHomeBiz.getPlayBackList(categoryId,mPage);
                mHnHomeBiz.getBanner(3);
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
    protected void initData() {
        mPage = 1;
        mHnHomeBiz.getPlayBackList(categoryId,mPage);
        mHnHomeBiz.getBanner(3);
    }


    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) return;
        refreshComplete();
        if (HnHomeBiz.Banner.equals(type)) {
            HnBannerModel model = (HnBannerModel) obj;
            if (model == null || model.getD().getCarousel() == null || mBanner == null || mHnHomeBiz == null)
                return;
            mHnHomeBiz.initViewpager(mBanner, mImgUrl, model.getD().getCarousel());

        } else if (HnHomeBiz.PLAYBACK_LIST.equals(type)) {
            mActivity.closeRefresh(mRefresh);
            HnPlayBackModel model = (HnPlayBackModel) obj;
            if (model == null || model.getD() == null || model.getD().getVideos().getItems() == null) {
                setEmpty();
            } else {
                if (mPage == 1)
                    mData.clear();
                mData.addAll(model.getD().getVideos().getItems());
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                setEmpty();
            }
            com.hotniao.video.utils.HnUiUtils.setRefreshMode(mRefresh, mPage, 10, mData.size());

        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        refreshComplete();
        mActivity.closeRefresh(mRefresh);
        if (HnHomeBiz.Banner.equals(type) || HnHomeBiz.ChatFast.equals(type)) {
            HnToastUtils.showToastShort(msg);
        }
    }

    private void setEmpty() {
        if (mActivity == null || mRlEmpty == null) return;
        if (mData == null || mData.size() < 1) {
            mRlEmpty.setVisibility(View.VISIBLE);
        } else {
            mRlEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void pullToRefresh() {
        mPage = 1;
        mHnHomeBiz.getPlayBackList(categoryId,mPage);
        mHnHomeBiz.getBanner(3);
    }

    @Override
    public void refreshComplete() {
        if (this.getParentFragment() instanceof HnHomeChatGropFragment) {
            ((HnHomeChatGropFragment) (this.getParentFragment())).refreshComplete();
        }
    }

    @Override
    public View getScrollableView() {
        return mRecycler;
    }
}
