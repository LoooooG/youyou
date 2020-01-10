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
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hotniao.video.R;
import com.hotniao.video.activity.HnInviteChatBeforeActivity;
import com.hotniao.video.adapter.HnHomeChatAdapter;
import com.hotniao.video.adapter.HnHomeChatRecomAdapter;
import com.hotniao.video.base.BaseScollFragment;
import com.hotniao.video.biz.home.HnHomeBiz;
import com.hotniao.video.model.HnBannerModel;
import com.hotniao.video.model.HnHomeFastChatModel;
import com.hotniao.video.model.HnHomeHotAnchorChatModel;

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
public class HnHomeChatFragment extends BaseScollFragment implements BaseRequestStateListener {
    @BindView(R.id.recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mRefresh;

    private ConvenientBanner mBanner;
    private RecyclerView mRecyclerRecom;
    private RelativeLayout mRlEmpty;
    private String mId;
    private HnHomeChatRecomAdapter mRecomAdapter;

    private List<HnHomeHotAnchorChatModel.DBean.AnchorBean> mHeadData = new ArrayList<>();
    private List<HnHomeFastChatModel.DBean.ItemsBean> mData = new ArrayList<>();
    private HnHomeChatAdapter mAdapter;

    private HnHomeBiz mHnHomeBiz;

    private int mPage = 1;
    /**
     * 广告数据源
     */
    private List<String> mImgUrl = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public HnHomeChatFragment(String mId) {
        this.mId = mId;
    }

    //    public static BaseFragment get(String id) {
//        HnHomeChatFragment fragment = new HnHomeChatFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("id", id);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home_chat;
    }

    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mId = getArguments().getString("id");
        mHnHomeBiz = new HnHomeBiz(mActivity);
        mHnHomeBiz.setBaseRequestStateListener(this);
//        mRefresh.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        initAdapter();
        initListener();
    }

    private void initAdapter() {
//        mRecycler.addItemDecoration(new HnSpacesItemDecoration(6, false));
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HnHomeChatAdapter(mData);
        mRecycler.setAdapter(mAdapter);
        View mHeaderView = mActivity.getLayoutInflater().inflate(R.layout.head_home_chat_layout, null);
        mBanner = (ConvenientBanner) mHeaderView.findViewById(R.id.mBanner);
        mRlEmpty = (RelativeLayout) mHeaderView.findViewById(R.id.mRlEmpty);
        View view = mHeaderView.findViewById(R.id.space_view);
        mAdapter.addHeaderView(mHeaderView);

        //mRecyclerRecom gone
        mRecyclerRecom = (RecyclerView) mHeaderView.findViewById(R.id.mRecyclerRecom);
        mRecyclerRecom.setHasFixedSize(true);
        mRecyclerRecom.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRecomAdapter = new HnHomeChatRecomAdapter(mHeadData);
        mRecyclerRecom.setAdapter(mRecomAdapter);
        mRecyclerRecom.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                HnInviteChatBeforeActivity.luncher(mActivity, mHeadData.get(position).getUser_id(),
//                        mHeadData.get(position).getUser_avatar(), "100");
            }
        });

//        if (TextUtils.isEmpty(mId)) {

            HnUiUtils.setBannerHeight(mActivity, mBanner);
//        }else {
//            mBanner.setVisibility(View.GONE);
//        }
    }

    private void initListener() {
        mRefresh.disableWhenHorizontalMove(true);
        mRefresh.setMode(PtrFrameLayout.Mode.BOTH);
        mRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                if(TextUtils.isEmpty(mId)){
                    mHnHomeBiz.getFollowAnchorChatFast(mPage);
                }else{
                    mHnHomeBiz.getHomeAnchorChatFast(mPage, mId,false);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                if(TextUtils.isEmpty(mId)){
                    mHnHomeBiz.getFollowAnchorChatFast(mPage);
                }else{
                    mHnHomeBiz.getHomeAnchorChatFast(mPage, mId,false);
                }

//                mHnHomeBiz.getHomeHotAnchorChat(6, mId);
                mHnHomeBiz.getBanner(1);
            }
        });


    }

    @Override
    protected void initData() {
        mPage = 1;
//        mHnHomeBiz.getHomeHotAnchorChat(6, mId);
        if(TextUtils.isEmpty(mId)){
            mHnHomeBiz.getFollowAnchorChatFast(mPage);
        }else{
            mHnHomeBiz.getHomeAnchorChatFast(mPage, mId,false);
        }
        mHnHomeBiz.getBanner(1);
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

        } else if (HnHomeBiz.ChatFast.equals(type)) {
            mActivity.closeRefresh(mRefresh);
            HnHomeFastChatModel model = (HnHomeFastChatModel) obj;
            if (model == null || model.getD() == null || model.getD().getItems() == null) {
                setEmpty();
            } else {
                if (mPage == 1)
                    mData.clear();
                mData.addAll(model.getD().getItems());
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
                setEmpty();
            }
            com.hotniao.video.utils.HnUiUtils.setRefreshMode(mRefresh, mPage, 10, mData.size());

        } else if (HnHomeBiz.Love.equals(type)) {
            HnHomeHotAnchorChatModel model = (HnHomeHotAnchorChatModel) obj;
            if (model != null && model.getD() != null && model.getD().getAnchor().size() > 0) {
//                mRecyclerRecom.setVisibility(View.VISIBLE);
                mHeadData.clear();
                mHeadData.addAll(model.getD().getAnchor());
                if (mRecomAdapter != null) mRecomAdapter.notifyDataSetChanged();
            } else {
//                mRecyclerRecom.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        refreshComplete();
        mActivity.closeRefresh(mRefresh);
        if (HnHomeBiz.Banner.equals(type) || HnHomeBiz.ChatFast.equals(type)) {
            HnToastUtils.showToastShort(msg);
        } else if (HnHomeBiz.Love.equals(type)) {
//            mRecyclerRecom.setVisibility(View.GONE);
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
        if(TextUtils.isEmpty(mId)){
            mHnHomeBiz.getFollowAnchorChatFast(mPage);
        }else{
            mHnHomeBiz.getHomeAnchorChatFast(mPage, mId,false);
        }
        mHnHomeBiz.getHomeHotAnchorChat(6, mId);
        mHnHomeBiz.getBanner(1);
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
