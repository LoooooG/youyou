package com.hotniao.live.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.live.HnApplication;
import com.hotniao.live.R;
import com.hotniao.live.activity.HnUserHomeActivity;
import com.hotniao.live.base.CommListFragment;
import com.hotniao.live.biz.user.follow.HnFollowBiz;
import com.hotniao.live.eventbus.HnCountdownEvent;
import com.hotniao.live.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnFansContributeModel;
import com.hotniao.livelibrary.model.event.HnFollowEvent;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.hotniao.livelibrary.widget.dialog.HnUserDetailDialog;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

public class HnPlatformListFragment extends CommListFragment implements BaseRequestStateListener {
    public static final String PlatfromList = "platfromListRank";//悠悠直播排行榜
    public static final String FansContribute = "fansContribute";//粉丝贡献榜

    private CommRecyclerAdapter mAdapter;
    private List<HnFansContributeModel.DBean.RankBean.ItemsBean> mData = new ArrayList<>();

    private HnFollowBiz mHnFollowBiz;

    //是否主播榜   主播榜有关注 其余没有
    private boolean mIsAnchor = false;

    public static HnPlatformListFragment newInstance() {

        HnPlatformListFragment fragment = new HnPlatformListFragment();
        return fragment;
    }

    @Override
    protected String setTAG() {
        return HnUiUtils.getString(R.string.platform_list);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        pageSize = 10;
        mSpring.setMode(PtrFrameLayout.Mode.REFRESH);
        EventBus.getDefault().register(this);
        mHnFollowBiz = new HnFollowBiz(mActivity);
        mHnFollowBiz.setRegisterListener(this);
        setLoadMore();
        mIsAnchor = getArguments().getBoolean("isAnchor", false);
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                if (position < 3) {
                    if (mIsAnchor && !mData.get(position).getUser_id().equals(HnApplication.getmUserBean().getUser_id()))
                    /**是主播榜，但不是自己*/
                        holder.getView(R.id.mTvFocus).setVisibility(View.VISIBLE);
                    else
                        holder.getView(R.id.mTvFocus).setVisibility(View.GONE);

                    holder.getView(R.id.mFl123).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mIvOther).setVisibility(View.GONE);
                    holder.getView(R.id.mTvNumber).setVisibility(View.GONE);
//                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setImageURI(HnUrl.setImageUri(mData.get(position).getUser_avatar()));

                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                } else {
                    holder.getView(R.id.mTvFocus).setVisibility(View.GONE);
                    holder.getView(R.id.mFl123).setVisibility(View.GONE);
                    holder.getView(R.id.mIvOther).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mTvNumber).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.mTvNumber)).setText(position + 1 + "");
//                    ((FrescoImageView) holder.getView(R.id.mIvOther)).setImageURI(HnUrl.setImageUri(mData.get(position).getUser_avatar()));

                    ((FrescoImageView) holder.getView(R.id.mIvOther)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                }

                if (position == 0) {
                    ((ImageView) holder.getView(R.id.mIvRank)).setImageResource(R.drawable.no1);
                } else if (position == 1) {
                    ((ImageView) holder.getView(R.id.mIvRank)).setImageResource(R.drawable.no2);
                } else if (position == 2) {
                    ((ImageView) holder.getView(R.id.mIvRank)).setImageResource(R.drawable.no3);
                }


                //名称
                if (!TextUtils.isEmpty(mData.get(position).getUser_nickname())) {
                    ((TextView) holder.getView(R.id.mTvName)).setText(mData.get(position).getUser_nickname());
                }
                //等级
                if (!TextUtils.isEmpty(mData.get(position).getUser_level())) {
                    HnLiveLevelUtil.setAudienceLevBg((HnSkinTextView) holder.getView(R.id.mTvLv), mData.get(position).getUser_level(), true);
                }


                String type = "";
                String money = mData.get(position).getLive_gift_coin();
                type = HnUiUtils.getString(R.string.contribution);
                String value = mIsAnchor ? HnApplication.getmConfig().getDot() : HnApplication.getmConfig().getCoin();
                if (!PlatfromList.equals(getArguments().getString("type"))) {
                    value = HnApplication.getmConfig().getCoin();
                    money = mData.get(position).getLive_gift_dot();
                }
                if (mIsAnchor) {
                    type = "";
                    money = mData.get(position).getLive_gift_dot();
                } else if (PlatfromList.equals(getArguments().getString("type")) && !mIsAnchor) {
                    type = HnUiUtils.getString(R.string.week_contribution);
                }

                ((TextView) holder.getView(R.id.mTvPay)).setText(type + HnUtils.setTwoPoint(money) + value);

                //vip
                if ("Y".equals(mData.get(position).getIs_member())) {
                    holder.getView(R.id.mIvVip).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.mIvVip).setVisibility(View.GONE);
                }

                //关注
                if (mIsAnchor) {
                    final TextView mTvFocus = holder.getView(R.id.mTvFocus);
                    if ("Y".equals(mData.get(position).getIs_follow())) {
                        mTvFocus.setText(R.string.main_follow_on);
                        mTvFocus.setSelected(false);
                    } else {
                        mTvFocus.setText(R.string.add_follow);
                        mTvFocus.setSelected(true);
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
                }
                holder.getView(R.id.mLlClick).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FansContribute.equals(getArguments().getString("type"))) {
                            HnUserDetailDialog mHnUserDetailDialog = HnUserDetailDialog.newInstance(1, mData.get(position).getUser_id(), HnApplication.getmUserBean().getUser_id(), 0);
                            mHnUserDetailDialog.setActvity(mActivity);
                            mHnUserDetailDialog.show(mActivity.getSupportFragmentManager(), "HnUserDetailDialog");
                        } else {
                            HnUserHomeActivity.luncher(mActivity, mData.get(position).getUser_id());
                        }

                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_platform_list;
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
        /**
         * 粉丝贡献榜
         */
        if (HnPlatformListFragment.FansContribute.equals(getArguments().getString("type"))) {
            params.put("anchor_user_id", getArguments().getString("anchorId"));
            if (0 == getArguments().getInt("fansType")) {//	day：日榜，week：周榜，all：总榜
                params.put("type", "day");
            } else if (1 == getArguments().getInt("fansType")) {
                params.put("type", "week");
            } else {
                params.put("type", "all");
            }
        } else {
            /**
             * 主播，用户榜
             */

        }
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return getArguments().getString("url");
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnFansContributeModel>(HnFansContributeModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (mActivity == null) return;
                refreshFinish();

                if (model.getD() == null || model.getD().getRank() == null || model.getD().getRank().getItems() == null) {
                    setEmpty(HnUiUtils.getString(R.string.now_no_ranking), R.drawable.empty_com);
                    return;
                }
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getRank().getItems());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_ranking), R.drawable.empty_com);

                if (HnPlatformListFragment.PlatfromList.equals(getArguments().getString("type")) && mIsAnchor) {
                    EventBus.getDefault().post(new HnCountdownEvent(model.getD().getDeadline()));
                }

            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (mActivity == null) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_ranking), R.drawable.empty_com);

            }
        };
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
            HnToastUtils.showToastShort(getString(R.string.live_follow_success));
        } else if (HnFollowBiz.CANCLE.equals(type)) {
            int pos = (int) obj;
            mData.get(pos).setIs_follow("N");
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
            HnToastUtils.showToastShort(getString(R.string.live_cancle_follow_success));
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
                        if (!isFollow) {
                            mData.get(i).setIs_follow("N");
                        } else {
                            mData.get(i).setIs_follow("Y");
                        }
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
