package com.hotniao.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.base.BaseActivity;
import com.hn.library.base.BaseRequestStateListener;
import com.hn.library.loadstate.HnLoadingLayout;
import com.hn.library.refresh.PtrClassicFrameLayout;
import com.hn.library.refresh.PtrDefaultHandler2;
import com.hn.library.refresh.PtrFrameLayout;
import com.hn.library.utils.HnToastUtils;
import com.hn.library.utils.HnUtils;
import com.hotniao.video.HnApplication;
import com.hotniao.video.R;
import com.hotniao.video.adapter.HnRewardAdapter;
import com.hotniao.video.biz.share.HnShareBiz;
import com.hotniao.video.dialog.HnEarningTotalTypePopWindow;
import com.hotniao.video.model.HnGeneralizeModel;
import com.hotniao.video.model.HnRewardLogModel;
import com.hotniao.video.utils.HnUiUtils;
import com.reslibrarytwo.HnSkinTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HnGeneralizeActivity extends BaseActivity implements BaseRequestStateListener {

    @BindView(R.id.mHnLoadingLayout)
    HnLoadingLayout mHnLoadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mLvBillRec;
    @BindView(R.id.ptr_refresh)
    PtrClassicFrameLayout mSwipeRefresh;
    @BindView(R.id.tv_person_num)
    TextView tvPersonNum;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.iv_reward)
    ImageView ivReward;
    @BindView(R.id.tv_earnings)
    TextView tvEarnings;

    /**
     * 提现记录列表适配器
     */
    private HnRewardAdapter rewardLogAdapter;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 时间类型
     */
    private String mDateType = HnEarningTotalTypePopWindow.DAY;

    private HnEarningTotalTypePopWindow mPopWindow;
    private TextView mTvTotal, mTvEmpty;
    private HnSkinTextView mTvType;

    private List<HnRewardLogModel.DBean.ItemsBean> mData = new ArrayList<>();

    private HnShareBiz shareBiz;

    @Override
    public int getContentViewId() {
        return R.layout.activity_generalize;
    }

    @Override
    public void onCreateNew(Bundle savedInstanceState) {
        setTitle("我的推广");
        setShowBack(true);
        shareBiz = new HnShareBiz(this);
        shareBiz.setBaseRequestStateListener(this);
        //初始化适配器
        rewardLogAdapter = new HnRewardAdapter(mData);
        mLvBillRec.setLayoutManager(new LinearLayoutManager(this));
        mLvBillRec.setHasFixedSize(true);
        mLvBillRec.setAdapter(rewardLogAdapter);
        mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
        addHead();
        initEvent();
    }

    private void initEvent() {
        //刷新处理
        mSwipeRefresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPage += 1;
                getInitData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPage = 1;
                getInitData();
            }
        });

        //错误重新加载
        mHnLoadingLayout.setOnReloadListener(new HnLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPage = 1;
                mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                getInitData();
            }
        });
    }

    private void initHeaderView(HnGeneralizeModel.DBean model){
        if(model != null){
            mTvTotal.setText(HnUtils.setTwoPoints(model.getTotal_money()+"")+HnApplication.getmConfig().getDot());
            tvPersonNum.setText(model.getUser_invite_total() + "人");
            tvEarnings.setText(HnUtils.setTwoPoints(model.getUser_invite_reward()+"") + "元");
        }
    }

    /**
     * 添加头部
     */
    private void addHead() {
        View view = LayoutInflater.from(this).inflate(R.layout.head_earning_total, null);
        mTvTotal = (TextView) view.findViewById(R.id.mTvTotal);
        mTvEmpty = (TextView) view.findViewById(R.id.mTvEmpty);
        mTvType = (HnSkinTextView) view.findViewById(R.id.mTvType);
        view.findViewById(R.id.rl_reward_header).setVisibility(View.VISIBLE);

        if (mPopWindow == null) {
            mPopWindow = new HnEarningTotalTypePopWindow(this);
            mPopWindow.setOnItemClickListener(new HnEarningTotalTypePopWindow.OnItemClickListener() {
                @Override
                public void itemClick(String name, String type) {
                    mTvType.setText(name);
                    mDateType = type;
                    mPage = 1;
                    mHnLoadingLayout.setStatus(HnLoadingLayout.Loading);
                    getInitData();
                }

                @Override
                public void dismissLis() {
                    mTvType.setRightDrawable(R.drawable.account_lower);
                }
            });
        }
        mTvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopWindow == null) {
                    mPopWindow = new HnEarningTotalTypePopWindow(HnGeneralizeActivity.this);
                }
                mPopWindow.showUp(v);
                mTvType.setRightDrawable(R.drawable.account_upper);

            }
        });
        rewardLogAdapter.addHeaderView(view);
    }

    @OnClick({R.id.tv_exchange,R.id.rl_invite_user,R.id.rl_get_money})
    public void click(View view){
        switch (view.getId()){
            case R.id.tv_exchange:
                openActivity(HnMyExchangeActivity.class);
                break;
            case R.id.rl_invite_user:
                openActivity(HnMyInviteUserActivity.class);
                break;
            case R.id.rl_get_money:
                openActivity(HnGetMoneyActivity.class);
                break;
        }
    }

    @Override
    public void getInitData() {
        shareBiz.rewardLog(mPage, mDateType);
        shareBiz.generalizeIndex();
    }

    private void setEmpty() {
        if (mTvEmpty == null) return;
        mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
        mTvEmpty.setVisibility(mData.size() < 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void requesting() {

    }

    @Override
    public void requestSuccess(String type, String response, Object obj) {
        if (HnShareBiz.REWARD_LOG.equals(type)) {
            HnRewardLogModel model = (HnRewardLogModel) obj;
            try {
                mSwipeRefresh.refreshComplete();
                HnRewardLogModel.DBean d = model.getD();
                List<HnRewardLogModel.DBean.ItemsBean> items = d.getItems();
//                mTvTotal.setText( HnUtils.setTwoPoints(model.getD().getAmount_total())+HnApplication.getmConfig().getDot());
                mHnLoadingLayout.setStatus(HnLoadingLayout.Success);
                if (mPage == 1) {
                    mData.clear();
                }
                mData.addAll(items);
                if (rewardLogAdapter != null)
                    rewardLogAdapter.notifyDataSetChanged();
                setEmpty();
                HnUiUtils.setRefreshMode(mSwipeRefresh, mPage, 20, rewardLogAdapter.getItemCount());

            } catch (Exception e) {
                setEmpty();
            }
        } else if (HnShareBiz.GENERALIZE_INDEX.equals(type)) {
            HnGeneralizeModel model = (HnGeneralizeModel) obj;
            initHeaderView(model.getD());
        }
    }

    @Override
    public void requestFail(String type, int code, String msg) {
        if (HnShareBiz.REWARD_LOG.equals(type)) {
            mSwipeRefresh.refreshComplete();
            setEmpty();
        } else {
            HnToastUtils.showToastShort(msg);
        }
    }

}
