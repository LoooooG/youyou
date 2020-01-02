package com.hotniao.live.fragment.search;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnPrefUtils;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnUserHomeActivity;
import com.hotniao.live.base.CommListFragment;
import com.hotniao.live.biz.user.follow.HnFollowBiz;
import com.hotniao.live.eventbus.HnSearchEvent;
import com.hotniao.live.model.HnSearchUserModel;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：主页面
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnSearchUserFragment extends CommListFragment implements BaseRequestStateListener {

    private CommRecyclerAdapter mAdapter;
    private List<HnSearchUserModel.DBean.ItemsBean> mData = new ArrayList<>();
    private String mKeyWords;
    private HnFollowBiz mHnFollowBiz;

    public static HnSearchUserFragment getInstance() {
        HnSearchUserFragment f = new HnSearchUserFragment();
        return f;
    }

    @Override
    protected String setTAG() {
        return HnUiUtils.getString(R.string.live);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        EventBus.getDefault().register(this);
        mKeyWords = getArguments().getString("keyWord");
        mHnFollowBiz = new HnFollowBiz(mActivity);
        mHnFollowBiz.setRegisterListener(this);
//        mLoadingLayout.setStatus(HnLoadingLayout.Success);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                ((FrescoImageView) holder.getView(R.id.fiv_header)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                ((TextView) holder.getView(R.id.tv_name)).setText(mData.get(position).getUser_nickname());
                ((TextView) holder.getView(R.id.tv_des)).setText(HnUiUtils.getString(R.string.fans_m) + mData.get(position).getUser_fans_total());
                //用户等级
                HnSkinTextView tvUserLevel = holder.getView(R.id.tv_user_level);
                HnLiveLevelUtil.setAudienceLevBg(tvUserLevel, mData.get(position).getUser_level(), true);

                final TextView mTvFocus = holder.getView(R.id.mTvFocus);
                if (mData.get(position).getUser_id().equals(HnPrefUtils.getString(NetConstant.User.UID, "0")))
                    mTvFocus.setVisibility(View.GONE);
                else mTvFocus.setVisibility(View.VISIBLE);

                if ("Y".equals(mData.get(position).getIs_follow())) {
                    mTvFocus.setText(R.string.main_follow_on);
                    mTvFocus.setSelected(false);
                } else {
                    mTvFocus.setText(R.string.add_follow);
                    mTvFocus.setSelected(true);
                }
                if (mData.get(position).getUser_id().equals(HnApplication.getmUserBean().getUser_id())) {
                    mTvFocus.setVisibility(View.GONE);
                } else {
                    mTvFocus.setVisibility(View.VISIBLE);
                }
                mTvFocus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("Y".equals(mData.get(position).getIs_follow())) {
                            mHnFollowBiz.cancelFollow(mData.get(position).getUser_id(), position);
                        } else {
                            mHnFollowBiz.addFollow(mData.get(position).getUser_id(), position);
                        }
                    }
                });

                holder.getView(R.id.rl_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HnUserDetailDialog dialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(), HnPrefUtils.getString(NetConstant.User.UID, ""), 0);
//                        dialog.setActvity(mActivity);
//                        dialog.show(getChildFragmentManager(), "HnUserDetailDialog");
                        HnUserHomeActivity.luncher(mActivity, mData.get(position).getUser_id());
                    }
                });


            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_search_user;
            }

            @Override
            public int getItemCount() {
                return mData.size();
            }
        };
        return mAdapter;
    }

    @Override
    protected RequestParams setRequestParam() {
        RequestParams params = new RequestParams();
        mKeyWords=TextUtils.isEmpty(mKeyWords)? getArguments().getString("keyWord"):mKeyWords;
        params.put("kw", mKeyWords);
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.SEARCH_ANCHOR;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnSearchUserModel>(HnSearchUserModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();
                if (model.getD() == null) return;
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getItems());

                mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_search_data), R.drawable.home_no_search);
//                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_search_data), R.drawable.home_no_search);
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(HnSearchEvent event) {
        if (!TextUtils.isEmpty(event.getKey())) {
            mKeyWords = event.getKey();
            getData(HnRefreshDirection.TOP, 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (mActivity == null) return;
        if (HnFollowBiz.ADD.equals(type)) {
            int pos = (int) obj;
            mData.get(pos).setIs_follow("Y");
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_follow_success));
        } else if (HnFollowBiz.CANCLE.equals(type)) {
            int pos = (int) obj;
            mData.get(pos).setIs_follow("N");
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
            HnToastUtils.showToastShort(HnUiUtils.getString(R.string.live_cancle_follow_success));
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (mActivity == null) return;
        if (HnFollowBiz.ADD.equals(type)) {
            HnToastUtils.showToastShort(msg);
        } else if (HnFollowBiz.CANCLE.equals(type)) {
            HnToastUtils.showToastShort(msg);
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
                        if (isFollow) {
                            mData.get(i).setIs_follow("Y");
                        } else {
                            mData.get(i).setIs_follow("N");
                        }
                        if (mAdapter != null) mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

    }

}
