package com.hotniao.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.HnUrl;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnRefreshDirection;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.video.R;
import com.hotniao.video.base.CommListActivity;
import com.hotniao.video.utils.HnUiUtils;
import com.hotniao.livelibrary.model.HnUserClosedModel;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;


import java.util.ArrayList;
import java.util.List;

public class HnUserClosedRankActivity extends CommListActivity{
    private CommRecyclerAdapter mAdapter;
    private List<HnUserClosedModel.DBean.RankBean> mData = new ArrayList<>();
    private String mUid;
    private String mNickName;

    public static void luncher(Activity activity, String uid, String nickName) {
        activity.startActivity(new Intent(activity, HnUserClosedRankActivity.class).putExtra("uid", uid).putExtra("nickName", nickName));
    }

    @Override
    protected String setTitle() {
        return mNickName + HnUiUtils.getString(R.string.closed_rank);
    }

    @Override
    public int getContentViewId() {
        return R.layout.comm_list;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        mUid = getIntent().getStringExtra("uid");
        mNickName = getIntent().getStringExtra("nickName");
        setToolbarWhite();
        super.onCreateNew(savedInstanceState);
    }

    @Override
    protected CommRecyclerAdapter setAdapter() {
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position) {
                //等级标识
                if (position < 3) {
                    holder.getView(R.id.mFl123).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mIvOther).setVisibility(View.GONE);
                    holder.getView(R.id.mTvNumber).setVisibility(View.GONE);
//                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setImageURI(HnUrl.setImageUri(mData.get(position).getUser_avatar()));

                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                } else {
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
                HnLiveLevelUtil.setAudienceLevBg((HnSkinTextView) holder.getView(R.id.mTvLv), String.valueOf(mData.get(position).getUser_level()), true);

                //亲密指数
                ((TextView) holder.getView(R.id.mTvCount)).setText(mData.get(position).getLive_gift_coin());

                holder.getView(R.id.mLlClick).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            HnUserHomeActivity.luncher(HnUserClosedRankActivity.this, String.valueOf(mData.get(position).getUser_id()));

                    }
                });

            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.adapter_closed_list;
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
        params.put("anchor_user_id", mUid + "");
        params.put("page", page + "");
        return params;
    }

    @Override
    protected String setRequestUrl() {
        return HnUrl.LIVE_RANK_USER_GIFT;
    }

    @Override
    protected HnResponseHandler setResponseHandler(final HnRefreshDirection state) {
        return new HnResponseHandler<HnUserClosedModel>(HnUserClosedModel.class) {
            @Override
            public void hnSuccess(String response) {
                if (isFinishing()) return;
                refreshFinish();
                if (HnRefreshDirection.TOP == state) {
                    mData.clear();
                }
                mData.addAll(model.getD().getRank());
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                setEmpty(HnUiUtils.getString(R.string.now_no_closed), R.drawable.empty_com);
                HnUiUtils.setRefreshMode(mSpring, page, pageSize, mData.size());
            }

            @Override
            public void hnErr(int errCode, String msg) {
                if (isFinishing()) return;
                refreshFinish();
                HnToastUtils.showToastShort(msg);
                setEmpty(HnUiUtils.getString(R.string.now_no_closed), R.drawable.empty_com);
            }
        };
    }

}
