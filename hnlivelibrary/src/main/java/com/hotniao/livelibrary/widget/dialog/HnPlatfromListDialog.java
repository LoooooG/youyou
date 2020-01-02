package com.hotniao.livelibrary.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hn.library.HnBaseApplication;
import com.hn.library.base.baselist.BaseViewHolder;
import com.hn.library.base.baselist.CommRecyclerAdapter;
import com.hn.library.global.NetConstant;
import com.hn.library.http.HnHttpUtils;
import com.hn.library.http.HnResponseHandler;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.utils.FrescoConfig;
import com.hn.library.utils.HnDateUtils;
import com.hn.library.utils.HnNetworkUtils;
import com.hn.library.utils.HnServiceErrorUtil;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUiUtils;
import com.hn.library.utils.HnUtils;
import com.hn.library.view.FrescoImageView;
import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.config.HnLiveConstants;
import com.hotniao.livelibrary.config.HnLiveUrl;
import com.hotniao.livelibrary.model.HnFansContributeModel;
import com.hotniao.livelibrary.model.event.HnLiveEvent;
import com.hotniao.livelibrary.util.HnLiveLevelUtil;
import com.hotniao.livelibrary.util.MyCountDownTimer;
import com.loopj.android.http.RequestParams;
import com.reslibrarytwo.HnSkinTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 类描述：平台（悠悠直播）排行榜
 * 创建人：刘勇
 * 创建时间：2017/2/22 16:22
 * 修改人：刘勇
 * 修改时间：2017/2/22 16:34
 * 修改备注：
 * Version:  1.0.0
 */

public class HnPlatfromListDialog extends DialogFragment {


    private Activity mActivity;
    private static HnPlatfromListDialog dialog;
    private RecyclerView mRecycler;
    private CommRecyclerAdapter mAdapter;
    private List<HnFansContributeModel.DBean.RankBean.ItemsBean> mData = new ArrayList<>();
    private HnFansContributeModel.DBean mDbean;

    private FrescoImageView mIvAnchor;
    private TextView mTvName, mTvPay, mTvRank, mTvTime;
    private HnSkinTextView mTvLv;
    private ImageView mIvVip, mIvCancle;
    private HnLoadingLayout mLoadingLayout;
    private LinearLayout mLlRank;
    private static String mAnchorId;
    private MyCountDownTimer mCTimer;

    public static HnPlatfromListDialog newInstance(String anchorid) {
        dialog = new HnPlatfromListDialog();
        mAnchorId = anchorid;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.dialog_platfrom_list, null);

        initView(view);
        mRecycler = (RecyclerView) view.findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycler.setHasFixedSize(true);
        setAdapter();
        mLoadingLayout.setLoadViewState(HnLoadingLayout.Loading, mLoadingLayout);
        getData(mAnchorId);
        Dialog dialog = new Dialog(mActivity, R.style.dialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth());
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight() * 0.7);
        window.setAttributes(params);
        return dialog;
    }

    private void initView(View v) {
        mIvAnchor = (FrescoImageView) v.findViewById(R.id.mIvAnchor);
        mLlRank = (LinearLayout) v.findViewById(R.id.mLlRank);
        mTvName = (TextView) v.findViewById(R.id.mTvName);
        mTvLv = (HnSkinTextView) v.findViewById(R.id.mTvLv);
        mIvVip = (ImageView) v.findViewById(R.id.mIvVip);
        mIvCancle = (ImageView) v.findViewById(R.id.mIvCancle);
        mTvPay = (TextView) v.findViewById(R.id.mTvPay);
        mTvRank = (TextView) v.findViewById(R.id.mTvRank);
        mTvTime = (TextView) v.findViewById(R.id.mTvTime);
        mLoadingLayout = (HnLoadingLayout) v.findViewById(R.id.mHnLoadingLayout);
        mIvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void setData() {

    }


    public HnPlatfromListDialog setClickListen(SelDialogListener listener) {
        this.listener = listener;
        return dialog;
    }

    private SelDialogListener listener;

    public interface SelDialogListener {
        void sureClick();
    }

    private void setAdapter() {
        mAdapter = new CommRecyclerAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                holder.getView(R.id.mTvFocus).setVisibility(View.GONE);

                if (position < 3) {

                    holder.getView(R.id.mFl123).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mIvOther).setVisibility(View.GONE);
                    holder.getView(R.id.mTvNumber).setVisibility(View.GONE);
                    ((FrescoImageView) holder.getView(R.id.mIvImg)).setController(FrescoConfig.getController(mData.get(position).getUser_avatar()));
                } else {
                    holder.getView(R.id.mTvFocus).setVisibility(View.GONE);
                    holder.getView(R.id.mFl123).setVisibility(View.GONE);
                    holder.getView(R.id.mIvOther).setVisibility(View.VISIBLE);
                    holder.getView(R.id.mTvNumber).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.mTvNumber)).setText(position + 1 + "");
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

                //贡献V银
                if (!TextUtils.isEmpty(mData.get(position).getLive_gift_coin())) {
                    ((TextView) holder.getView(R.id.mTvPay)).setText(HnUtils.setTwoPoint(mData.get(position).getLive_gift_dot()) + HnBaseApplication.getmConfig().getDot());
                }
                //vip
                if ("Y".equals(mData.get(position).getIs_member())) {
                    holder.getView(R.id.mIvVip).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.mIvVip).setVisibility(View.GONE);
                }


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

        mRecycler.setAdapter(mAdapter);
    }


    private void getData(String anchorId) {
        RequestParams params = new RequestParams();
        params.put("anchor_user_id", anchorId);
        params.put("page", 1);
        params.put("pagesize", 10);
        HnHttpUtils.postRequest(HnLiveUrl.LIVE_RANK_FIREBIRD, params, HnLiveUrl.LIVE_RANK_FIREBIRD, new HnResponseHandler<HnFansContributeModel>(HnFansContributeModel.class) {
            @Override
            public void hnSuccess(String response) {
                try {
                    if (mActivity == null) return;
                    if (model.getD() == null) return;
                    if (mLoadingLayout == null)
                        mLoadingLayout.setLoadViewState(HnLoadingLayout.Success, mLoadingLayout);
                    if (model.getD().getRank() != null || model.getD().getRank().getItems() != null) {
                        mData.clear();
                        mData.addAll(model.getD().getRank().getItems());
                        if (mAdapter != null) mAdapter.notifyDataSetChanged();
                    }
                    setEmpty();
                    mDbean = model.getD();
                    setMessage();
                } catch (Exception e) {
                }
            }


            @Override
            public void hnErr(int errCode, String msg) {
                try {
                    if (mActivity == null) return;
                    HnToastUtils.showToastShort(msg);
                    setEmpty();
                    if (HnServiceErrorUtil.ACCESS_TOKEN_INVALID == errCode) {

                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void setEmpty() {
        if (mLoadingLayout == null) return;
        if (mData != null && mData.size() > 0) {
            mLoadingLayout.setLoadViewState(HnLoadingLayout.Success, mLoadingLayout);
        } else {
            mLoadingLayout.setEmptyText(getString(R.string.live_now_no_rank)).setEmptyImage(R.drawable.empty_com);
            mLoadingLayout.setLoadViewState(HnLoadingLayout.Empty, mLoadingLayout);
        }

    }

    private void setMessage() {

        if (mDbean != null) {
            //倒计时
            String deadline = mDbean.getDeadline();
            if (!TextUtils.isEmpty(deadline)) {
                long nowTime = System.currentTimeMillis();
                long l = Long.parseLong(deadline) * 1000 - nowTime;
                mCTimer = new MyCountDownTimer(l, 1000, mTvTime);
                mCTimer.start();
            }

            if (TextUtils.isEmpty(mDbean.getAnchor().getAnchor_ranking())) {
                mLlRank.setVisibility(View.GONE);
            } else {
                if ("暂无排名".equals(mDbean.getAnchor().getAnchor_ranking())) {
                    mTvRank.setText(mDbean.getAnchor().getAnchor_ranking());
                    mLlRank.setVisibility(View.VISIBLE);
                    mTvRank.setTextColor(getResources().getColor(R.color.comm_text_color_black_ss));
                } else if (10 < Integer.parseInt(mDbean.getAnchor().getAnchor_ranking())) {
                    mTvRank.setText(mDbean.getAnchor().getAnchor_ranking() + "名");
                    mLlRank.setVisibility(View.VISIBLE);
                } else {
                    mLlRank.setVisibility(View.GONE);
                }

                mIvAnchor.setController(FrescoConfig.getController(mDbean.getAnchor().getUser_avatar()));
                mTvName.setText(mDbean.getAnchor().getUser_nickname());
                HnLiveLevelUtil.setAudienceLevBg(mTvLv, mDbean.getAnchor().getUser_level(), true);
                //vip
                if ("Y".equals(mDbean.getAnchor().getIs_member())) {
                    mIvVip.setVisibility(View.VISIBLE);
                } else {
                    mIvVip.setVisibility(View.GONE);
                }
                mTvPay.setText(mDbean.getAnchor().getLive_gift_coin() + HnBaseApplication.getmConfig().getDot());

            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeDialogShowMark(HnLiveEvent event) {
        if (HnLiveConstants.EventBus.Close_Dialog_Show_Mark.equals(event.getType()) && isAdded())
            dismiss();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCTimer != null) {
            mCTimer.cancel();
            mCTimer = null;
        }

    }


}
